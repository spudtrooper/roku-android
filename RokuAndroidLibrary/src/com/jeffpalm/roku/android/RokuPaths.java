package com.jeffpalm.roku.android;

import java.util.Arrays;
import java.util.List;

public class RokuPaths {

  private final String prefix;

  /**
   * Host should be a simple IP -- e.g. <code>"192.168.1.114"</code>.
   */
  public RokuPaths(String host) {
    this.prefix = "http://" + host + ":8060";
  }

  public RokuPaths(RokuDeviceState deviceState) {
    this(deviceState.getHost());
  }

  /** Gets the command for a simple command. */
  public Command getSimpleCommandPath(String simpleCommand) {
    return getCommand("keypress", simpleCommand);
  }

  /** Gets the command to launch an app. */
  public Command getLaunchPath(RokuAppInfo app) {
    return getCommand("launch", app.getId());
  }

  /** Gets the path for an apps icon. */
  public String getAppIconURL(RokuAppInfo app) {
    return getPath("query", "icon", app.getId());
  }

  /** Gets the commands for a simple keyboard press. */
  public Command[] getCharPaths(char c) {
    return new Command[] { getCommand("keydown", "Lit_" + c), getCommand("keyup", "Lit_" + c) };
  }

  /** Gets the commands for a simple keyboard press. */
  public List<Command> getCharPathList(char c) {
    return Arrays.asList(getCommand("keydown", "Lit_" + c), getCommand("keyup", "Lit_" + c));
  }

  private Command getCommand(Object... parts) {
    return new Command(getPath(parts));
  }

  private String getPath(Object... parts) {
    StringBuilder sb = new StringBuilder();
    sb.append(prefix);
    for (Object part : parts) {
      sb.append("/");
      sb.append(String.valueOf(part));
    }
    return sb.toString();
  }

  public String getAppInfoPath() {
    return getPath("query", "apps");
  }
}
