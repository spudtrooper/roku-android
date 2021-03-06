package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

import android.util.Log;

import com.jeffpalm.util.Callback;

public class RokUtil {

  private final static String TAG = "RokUtil";

  /** Opens the input stream for the url. */
  public static InputStream openStream(String url, int connectionTimeout)
      throws IllegalStateException, IOException {
    HttpGet httpGet = new HttpGet(url);
    HttpParams httpParameters = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
    HttpClient httpClient = new DefaultHttpClient(httpParameters);
    HttpResponse response = httpClient.execute(httpGet, new BasicHttpContext());
    if (response.getStatusLine().getStatusCode() == 200) {
      return response.getEntity().getContent();
    }
    return null;
  }

  /** Execute an HTTP POST to the url. */
  public static String post(String url) throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost(url);
    StringEntity se = new StringEntity("");
    httpPost.setEntity(se);
    HttpClient httpClient = new DefaultHttpClient();
    HttpResponse response = httpClient.execute(httpPost);
    return response.toString();
  }

  /** Launches the given app for the device state. */
  public static void launchApp(RokuDeviceState deviceState, final RokuAppInfo appInfo)
      throws InterruptedException, ExecutionException {
    final RokuPaths paths = new RokuPaths(deviceState);

    Log.d(TAG, "Launching app " + appInfo.getName());

    // TODO: Do these off the UI thread.
    Command command = paths.getSimpleCommandPath(Command.SimpleCommands.HOME);
    new CommandTask() {
      protected void onPostExecute(String result) {
        new CommandTask().execute(paths.getLaunchPath(appInfo));
      };
    }.execute(command);

  }

  /** Handles the exception is a consistent way. */
  public static void handle(String tag, Throwable e, String msg) {
    Log.e(tag, msg + ":" + e.getMessage());
  }

  public static String getImagePath(RokuDeviceState deviceState, RokuAppInfo appInfo) {
    return new RokuPaths(deviceState.getHost()).getAppIconURL(appInfo);
  }

  public static void executeSimpleCommand(RokuDeviceState deviceState, String command) {
    executeSimpleCommand(deviceState, command, null /* callback */);
  }

  public static void executeSimpleCommand(RokuDeviceState deviceState, String command,
      final Callback<String> callback) {
    RokuPaths paths = new RokuPaths(deviceState);
    new CommandTask() {
      protected void onPostExecute(String result) {
        if (callback != null) {
          callback.callback(result);
        }
      };
    }.execute(paths.getSimpleCommandPath(command));
  }

  public static List<Command> getPressKeyCommands(RokuDeviceState deviceState, char c) {
    RokuPaths paths = new RokuPaths(deviceState);
    return paths.getCharPathList(c);
  }

  public static void pressKey(RokuDeviceState deviceState, char c) {
    final List<Command> commands = getPressKeyCommands(deviceState, c);
    new Thread(new Runnable() {
      public void run() {
        for (Command command : commands) {
          try {
            postCommand(command);
          } catch (Exception e) {
            handle(TAG, e, "pressKey " + command.getPath());
          }
        }
      }
    }).start();
  }

  public static String postCommand(Command command) throws ClientProtocolException, IOException {
    String path = command.getPath();
    Log.d(TAG, "Sending task " + path);
    return RokUtil.post(path);

  }
}
