package com.jeffpalm.roku.android;

public class Command {

  public final static class SimpleCommands {
    public final static String PLAY = "Play";
    public final static String UP = "Up";
    public final static String DOWN = "Down";
    public final static String LEFT = "Left";
    public final static String RIGHT = "Right";
    public final static String HOME = "Home";
    public final static String SELECT = "Select";
    public final static String NEXT = "Next";
    public final static String REV = "Rev";
    public final static String FORWARD = "Fwd";
    public final static String REPLAY = "InstantReplay";
    public final static String INFO = "Info";
  }

  private final String path;

  public Command(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
