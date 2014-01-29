package com.jeffpalm.roku.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jeffpalm.roku.android.Command;
import com.jeffpalm.roku.android.CommandTask;
import com.jeffpalm.roku.android.RokuPaths;

public class RemoteActivity extends Activity {

  final RokuPaths paths = new RokuPaths("192.168.1.115");

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remote);

    Button left = (Button) findViewById(R.id.button_left);
    Button right = (Button) findViewById(R.id.button_right);
    Button select = (Button) findViewById(R.id.button_select);
    Button up = (Button) findViewById(R.id.button_up);
    Button down = (Button) findViewById(R.id.button_down);
    Button home = (Button) findViewById(R.id.button_home);

    left.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View arg0) {
        new CommandTask().execute(paths.getSimpleCommandPath(Command.SimpleCommands.LEFT));
      }
    });
    right.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View arg0) {
        new CommandTask().execute(paths.getSimpleCommandPath(Command.SimpleCommands.RIGHT));
      }
    });
    up.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View arg0) {
        new CommandTask().execute(paths.getSimpleCommandPath(Command.SimpleCommands.UP));
      }
    });
    down.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View arg0) {
        new CommandTask().execute(paths.getSimpleCommandPath(Command.SimpleCommands.DOWN));
      }
    });
    select.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View arg0) {
        new CommandTask().execute(paths.getSimpleCommandPath(Command.SimpleCommands.SELECT));
      }
    });
    home.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View arg0) {
        new CommandTask().execute(paths.getSimpleCommandPath(Command.SimpleCommands.HOME));
      }
    });
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    char unicodeChar = (char) event.getUnicodeChar();
    Command[] commands = paths.getCharPaths(unicodeChar);
    for (Command c : commands) {
      new CommandTask().execute(c);
    }
    // return super.onKeyDown(keyCode, event);
    return true;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.remote, menu);
    return true;
  }

}
