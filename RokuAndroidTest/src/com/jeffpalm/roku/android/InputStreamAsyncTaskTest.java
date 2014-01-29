package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.test.ActivityTestCase;

import com.jeffpalm.test.TestUtils;
import com.jeffpalm.util.Option;

/**
 * The parameters to execute are the full URL -- e.g.
 * http://192.168.1.114:8060/query/apps.
 */
@RunWith(RobolectricTestRunner.class)
public class InputStreamAsyncTaskTest extends ActivityTestCase {

  public void testRokuAppInfo() throws InterruptedException, ExecutionException, IOException {
    Robolectric.addPendingHttpResponse(200, TestUtils.readDataFile("query_apps.xml"));
    InputStreamAsyncTask<List<RokuAppInfo>> task = new InputStreamAsyncTask<List<RokuAppInfo>>(
        new ReadsFromInputStream<List<RokuAppInfo>>() {
          @Override public List<RokuAppInfo> fromInputStream(InputStream in) throws Exception {
            return RokuAppInfo.fromInputStream(in);
          }
        });
    Option<List<RokuAppInfo>, Exception> result = task.execute(
        "http://" + RokuTestUtils.HOST + ":8060/query/apps").get();
    assertTrue(result.isSuccess());
    List<RokuAppInfo> infos = result.getSuccess();
    for (RokuAppInfo a : RokuTestUtils.TEST_APP_INFOS) {
      assertTrue(infos.contains(a));
    }
  }

  public void testRokuAppInfo_badPort() throws InterruptedException, ExecutionException {
    InputStreamAsyncTask<List<RokuAppInfo>> task = new InputStreamAsyncTask<List<RokuAppInfo>>(
        new ReadsFromInputStream<List<RokuAppInfo>>() {
          @Override public List<RokuAppInfo> fromInputStream(InputStream in) throws Exception {
            return RokuAppInfo.fromInputStream(in);
          }
        });
    Option<List<RokuAppInfo>, Exception> result = task.execute(
        "http://" + RokuTestUtils.HOST + ":8040/query/apps").get();
    assertFalse(result.isSuccess());
  }

  public void testRokuDeviceInfo() throws InterruptedException, ExecutionException {
    InputStreamAsyncTask<RokuDeviceInfo> task = new InputStreamAsyncTask<RokuDeviceInfo>(
        new ReadsFromInputStream<RokuDeviceInfo>() {
          @Override public RokuDeviceInfo fromInputStream(InputStream in) throws Exception {
            return RokuDeviceInfo.fromInputStream(in);
          }
        });
    Option<RokuDeviceInfo, Exception> result = task.execute(
        "http://" + RokuTestUtils.HOST + ":8060/").get();
    assertTrue(result.isSuccess());
    assertTrue(RokuTestUtils.TEST_DEVICE_INFO_COMPARABLE.compareTo(result.getSuccess()) == 0);
  }
}
