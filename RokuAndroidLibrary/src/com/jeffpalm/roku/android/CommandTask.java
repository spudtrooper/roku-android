package com.jeffpalm.roku.android;

import android.os.AsyncTask;
import android.util.Log;

public class CommandTask extends AsyncTask<Command, Integer, String> {
  
  private final static String TAG = "CommandTask";

  @Override protected String doInBackground(Command... params) {
    Command command = params[0];
    try {
      return RokUtil.postCommand(command);
    } catch (Exception e) {
    }
    return null;
  }

}