package com.jeffpalm.roku.android;

import java.util.concurrent.ExecutionException;

import android.test.ActivityTestCase;

public class CommandTaskTest extends ActivityTestCase {

  private final static String HOST = "192.168.1.115";

  public void testLeft() throws InterruptedException, ExecutionException {
    RokuPaths paths = new RokuPaths(HOST);
    CommandTask task = new CommandTask();
    Command command = paths.getSimpleCommandPath(Command.SimpleCommands.LEFT);
    String result = task.execute(command).get();
    assertNotNull(result);
  }

  public void testRight() throws InterruptedException, ExecutionException {
    RokuPaths paths = new RokuPaths(HOST);
    CommandTask task = new CommandTask();
    Command command = paths.getSimpleCommandPath(Command.SimpleCommands.RIGHT);
    String result = task.execute(command).get();
    assertNotNull(result);
  }

  public void testA() throws InterruptedException, ExecutionException {
    RokuPaths paths = new RokuPaths(HOST);
    char c = 'a';
    Command[] commands = paths.getCharPaths(c);
    for (Command cmd : commands) {
      String result = new CommandTask().execute(cmd).get();
      assertNotNull(result);
    }
  }
}
