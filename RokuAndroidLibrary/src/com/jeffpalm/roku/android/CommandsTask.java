package com.jeffpalm.roku.android;

import java.util.List;

public class CommandsTask extends CommandTask {
  private final List<Command> commands;

  public CommandsTask(List<Command> commands) {
    this.commands = commands;
  }

  @Override protected String doInBackground(Command... params) {
    String result = super.doInBackground(commands.get(0));
    if (commands.size() > 1) {
      new CommandsTask(commands.subList(1, commands.size())).execute();
    }
    return result;
  }

}