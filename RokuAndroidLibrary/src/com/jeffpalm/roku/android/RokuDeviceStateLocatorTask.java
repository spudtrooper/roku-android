package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.jeffpalm.util.Option;

/**
 * Tries to locate a device and returns the list of all known devices on the
 * local subnet. This task cannot be re-used.
 */
public class RokuDeviceStateLocatorTask extends
    AsyncTask<Void, Integer, Option<List<RokuDeviceState>, Exception>> {

  private final static String TAG = "RokuDeviceStateLocatorTask";

  private final static int CONNECTION_TIMEOUT_MILLIS = 100;
  private final static int TODO_PORT_TODO = 8060;

  private final List<RokuDeviceState> devices = new ArrayList<RokuDeviceState>();
  private boolean executeCalled = false;

  private boolean isReachable(String host, int port) {
    Socket sock = null;
    try {
      SocketAddress sockaddr = new InetSocketAddress(host, port);
      sock = new Socket();
      sock.connect(sockaddr, CONNECTION_TIMEOUT_MILLIS);
      return true;
    } catch (Exception e) {
    } finally {
      if (sock != null) {
        try {
          sock.close();
        } catch (IOException e) {
        }
      }
    }
    return false;
  }

  private final static int N;
  private final static int start;
  static {
    if (BuildConfig.DEBUG) {
      start = 100;
      N = 120;
    } else {
      start = 50;
      N = 200;
    }
  }

  public int getProgessLength() {
    return N - start;
  }

  @Override protected Option<List<RokuDeviceState>, Exception> doInBackground(Void... params) {
    assert (!executeCalled);
    int port = TODO_PORT_TODO;
    for (int i = start; i <= N; i++) {
      try {
        String host = String.format("192.168.1.%03d", i);
        Log.d(TAG, String.format("Trying host %s:%d", host, port));
        if (isReachable(host, port)) {
          try {
            // TODO(jeff) Uri.Builder
            String url = String.format("http://%s:%d", host, port);
            Log.d(TAG, String.format("Have connection, trying url %s", url));
            InputStream in = RokUtil.openStream(url, 1000);
            if (in != null) {
              RokuDeviceInfo info = RokuDeviceInfo.fromInputStream(in);
              RokuDeviceState device = new RokuDeviceState(info, host);
              devices.add(device);
              in.close();
            }
          } catch (Exception e) {
            return Option.fromFailure(e);
          }
        }
      } catch (Exception _) {
      }
      publishProgress(1);
    }
    return devices.isEmpty() ? Option.<List<RokuDeviceState>, Exception> fromFailure(new Exception(
        "Couldn't find a device")) : Option.<List<RokuDeviceState>, Exception> fromSuccess(devices);
  }
}
