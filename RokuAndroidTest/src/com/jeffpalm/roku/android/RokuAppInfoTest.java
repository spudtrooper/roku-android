package com.jeffpalm.roku.android;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.test.ActivityTestCase;

public class RokuAppInfoTest extends ActivityTestCase {

  public void testFromInputStream() throws IOException, XmlPullParserException {
    List<RokuAppInfo> infos = RokuAppInfo.fromInputStream(getInstrumentation().getContext()
        .getAssets().open(RokuTestUtils.TEST_APP_XML));
    for (RokuAppInfo a : RokuTestUtils.TEST_APP_INFOS) {
      assertTrue(infos.contains(a));
    }
  }
}
