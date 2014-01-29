package com.jeffpalm.roku.android;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.test.ActivityTestCase;

import com.jeffpalm.util.Option;

/**
 * The parameters to execute are the full URL -- e.g.
 * http://192.168.1.114:8060/query/apps.
 */
public class RokuDeviceStateLocatorTest extends ActivityTestCase {

  public void testRokuAppInfo() throws InterruptedException, ExecutionException {
    RokuDeviceStateLocatorTask task = new RokuDeviceStateLocatorTask();
    Option<List<RokuDeviceState>, Exception> result = task.execute().get();
    assertTrue(result.isSuccess());
    assertFalse(result.getSuccess().isEmpty());
    boolean haveDevice = false;
    for (RokuDeviceState deviceState : result.getSuccess()) {
      haveDevice |= RokuTestUtils.TEST_DEVICE_INFO_COMPARABLE
          .compareTo(deviceState.getDeviceInfo()) == 0;
    }
    assertTrue(haveDevice);
  }
}
