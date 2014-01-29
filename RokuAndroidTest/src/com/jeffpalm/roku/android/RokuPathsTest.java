package com.jeffpalm.roku.android;

import junit.framework.TestCase;

public class RokuPathsTest extends TestCase {

  private final static String prefix = "http://" + RokuTestUtils.HOST + ":8060";

  public void testKeypressDown() {
    RokuPaths paths = new RokuPaths(RokuTestUtils.HOST);
    assertEquals(prefix + "/keypress/Down", paths.getSimpleCommandPath(Command.SimpleCommands.DOWN)
        .getPath());
  }

  public void testGetAppInfoPath() {
    RokuPaths paths = new RokuPaths(RokuTestUtils.HOST);
    assertEquals(prefix + "/query/apps", paths.getAppInfoPath());
  }

}
