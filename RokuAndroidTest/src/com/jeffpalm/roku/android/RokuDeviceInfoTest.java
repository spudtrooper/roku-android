package com.jeffpalm.roku.android;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.test.ActivityTestCase;

public class RokuDeviceInfoTest extends ActivityTestCase {

  public void testFromInputStream() throws IOException, XmlPullParserException {
    RokuDeviceInfo info = RokuDeviceInfo.fromInputStream(getInstrumentation().getContext()
        .getAssets().open(RokuTestUtils.DEVICE_INFO_XML));
    assertTrue(RokuTestUtils.TEST_DEVICE_INFO_COMPARABLE.compareTo(info) == 0);
  }
}
