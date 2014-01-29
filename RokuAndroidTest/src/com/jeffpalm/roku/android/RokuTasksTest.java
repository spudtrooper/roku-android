package com.jeffpalm.roku.android;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.test.ActivityTestCase;

import com.jeffpalm.util.Option;

/**
 * The parameters to execute are the full URL -- e.g.
 * http://192.168.1.114:8060/query/apps.
 */
public class RokuTasksTest extends ActivityTestCase {

  public void testRokuAppInfo() throws InterruptedException, ExecutionException {
    String path = "http://" + RokuTestUtils.HOST + ":8060/query/apps";
    InputStreamAsyncTask<List<RokuAppInfo>> task = RokuTasks.getAppInfosTask();
    Option<List<RokuAppInfo>, Exception> result = task.execute(path).get();
    assertTrue(result.isSuccess());
    List<RokuAppInfo> infos = result.getSuccess();
    for (RokuAppInfo a : RokuTestUtils.TEST_APP_INFOS) {
      assertTrue(infos.contains(a));
    }
  }

  public void testRokuDeviceInfo() throws InterruptedException, ExecutionException {
    String path = "http://" + RokuTestUtils.HOST + ":8060";
    InputStreamAsyncTask<RokuDeviceInfo> task = RokuTasks.getDeviceInfoTask();
    Option<RokuDeviceInfo, Exception> result = task.execute(path).get();
    assertTrue(result.isSuccess());
    assertTrue(RokuTestUtils.TEST_DEVICE_INFO_COMPARABLE.compareTo(result.getSuccess()) == 0);
  }
}
