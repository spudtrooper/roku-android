package com.jeffpalm.roku.android;

import java.io.InputStream;
import java.util.List;

public class RokuTasks {

  /** Gets the task to retrieve the app infos. */
  public static InputStreamAsyncTask<List<RokuAppInfo>> getAppInfosTask() {
    return new InputStreamAsyncTask<List<RokuAppInfo>>(
        new ReadsFromInputStream<List<RokuAppInfo>>() {
          @Override public List<RokuAppInfo> fromInputStream(InputStream in) throws Exception {
            return RokuAppInfo.fromInputStream(in);
          }
        });
  }

  /** Gets the stask to retrieve the device info. */
  public static InputStreamAsyncTask<RokuDeviceInfo> getDeviceInfoTask() {
    return new InputStreamAsyncTask<RokuDeviceInfo>(new ReadsFromInputStream<RokuDeviceInfo>() {
      @Override public RokuDeviceInfo fromInputStream(InputStream in) throws Exception {
        return RokuDeviceInfo.fromInputStream(in);
      }
    });
  }
}
