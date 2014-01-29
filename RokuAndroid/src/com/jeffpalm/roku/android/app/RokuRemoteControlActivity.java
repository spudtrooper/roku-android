package com.jeffpalm.roku.android.app;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeffpalm.roku.android.Command;
import com.jeffpalm.roku.android.RokUtil;
import com.jeffpalm.roku.android.RokuAppInfo;
import com.jeffpalm.roku.android.RokuDeviceState;

public class RokuRemoteControlActivity extends Activity {

  private final static String TAG = "RokuDeviceChooserActivity";
  private final AtomicBoolean startedApp = new AtomicBoolean(false);
  private RokuDeviceState deviceState;
  private RokuAppInfo appInfo;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_roku_remote_control);

    Bundle extras = getIntent().getExtras();
    deviceState = (RokuDeviceState) extras.get(Roku.DEVICE_STATE);
    if (deviceState == null) {
      throw new IllegalArgumentException("Expected device state for " + Roku.DEVICE_STATE + "extra");
    }

    appInfo = (RokuAppInfo) extras.get(Roku.APP_INFO);
    if (appInfo != null) {
      ImageView imageView = (ImageView) findViewById(R.id.app_image);
      TextView textTitle = (TextView) findViewById(R.id.text_app_title);
      TextView textSubtitle = (TextView) findViewById(R.id.text_app_sub_title);
      RokUtil.setImageView(deviceState, appInfo, imageView);
      textTitle.setText(appInfo.getName());
      textSubtitle.setText(String.valueOf(appInfo.getVersion()));
    }

    Button backButton = (Button) findViewById(R.id.button_back);
    backButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.REV);
      }
    });
    Button playButton = (Button) findViewById(R.id.button_play);
    playButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.PLAY);
      }
    });
    Button forwardButton = (Button) findViewById(R.id.button_forward);
    forwardButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.FORWARD);
      }
    });
    ((Button) findViewById(R.id.button_home)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.HOME);
      }
    });
    ((Button) findViewById(R.id.button_replay)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.REPLAY);
      }
    });

    ((Button) findViewById(R.id.button_left)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.LEFT);
      }
    });
    ((Button) findViewById(R.id.button_select)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.SELECT);
      }
    });
    ((Button) findViewById(R.id.button_right)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.RIGHT);
      }
    });
    ((Button) findViewById(R.id.button_up)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.UP);
      }
    });
    ((Button) findViewById(R.id.button_down)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.DOWN);
      }
    });

    ((Button) findViewById(R.id.button_show_keyboard))
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            //TODOshowKeyboard();
          }
        });


    startedApp.set(false);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 1) {
      char unicodeChar = (char) event.getUnicodeChar();
      RokUtil.pressKey(deviceState, unicodeChar);
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override protected void onResume() {
    super.onResume();
    if (appInfo != null && !startedApp.getAndSet(true)) {
      try {
        RokUtil.launchApp(deviceState, appInfo);
      } catch (Exception e) {
        RokUtil.handle(TAG, e, "Launching " + appInfo);
      }
    }
  }

}
