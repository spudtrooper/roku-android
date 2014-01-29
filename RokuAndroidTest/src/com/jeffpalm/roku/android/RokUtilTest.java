package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class RokUtilTest extends TestCase {

  public void testOpenStream() throws IllegalStateException, IOException {
    String url = "http://google.com";
    InputStream in = RokUtil.openStream(url, 1000);
    assertNotNull(in);
    try {
      in.close();
    } catch (Exception e) {
      // don't care
    }
  }

}
