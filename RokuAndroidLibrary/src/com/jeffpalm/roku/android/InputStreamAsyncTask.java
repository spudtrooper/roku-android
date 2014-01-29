package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.net.Uri;
import android.os.AsyncTask;

import com.jeffpalm.util.Option;

/**
 * A class to create an {@link Option} result from an input stream.
 * 
 * @param <T>
 */
public class InputStreamAsyncTask<T> extends AsyncTask<String, Void, Option<T, Exception>> {

  private final static int CONNECTION_TIMEOUT_MILLIS = 1000;
  private int connectionTimeout = CONNECTION_TIMEOUT_MILLIS;
  private final ReadsFromInputStream<T> inputStreamReader;

  /**
   * This task needs a url as the first parameter of {@link #execute(Object...)}
   * .
   * 
   * @param inputStreamReader
   *          the stream reader
   */
  public InputStreamAsyncTask(ReadsFromInputStream<T> inputStreamReader) {
    this.inputStreamReader = inputStreamReader;
  }

  @Override final protected Option<T, Exception> doInBackground(String... params) {
    String url = Uri.parse(params[0]).toString();
    InputStream in = null;
    try {
      in = RokUtil.openStream(url, connectionTimeout);
      if (in != null) {
        Option<T, Exception> result  = Option.fromSuccess(inputStreamReader.fromInputStream(in));
        return result;
      }
    } catch (Exception e) {
      return Option.fromFailure(e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
    return Option.fromFailure(new Exception("Couldn't find device"));
  }

}
