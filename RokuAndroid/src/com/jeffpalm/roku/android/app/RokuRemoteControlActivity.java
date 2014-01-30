package com.jeffpalm.roku.android.app;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.imagedownloader.ImageDownloader;
import com.jeffpalm.roku.android.Command;
import com.jeffpalm.roku.android.CommandQueue;
import com.jeffpalm.roku.android.RokUtil;
import com.jeffpalm.roku.android.RokuAppInfo;
import com.jeffpalm.roku.android.RokuDeviceState;
import com.jeffpalm.util.Callback;

public class RokuRemoteControlActivity extends Activity {

  private final static String TAG = "RokuDeviceChooserActivity";
  private static final int REQUEST_CODE = 1234;
  private final AtomicBoolean startedApp = new AtomicBoolean(false);
  private final CommandQueue commandQueue = new CommandQueue();
  private SpeechLauncher speechLauncher = new SpeechLauncher();

  private RokuDeviceState deviceState;
  private RokuAppInfo appInfo;

  private final ImageDownloader imageDownloader = new ImageDownloader();
  {
    imageDownloader.setMode(ImageDownloader.Mode.CORRECT);
  }

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
      imageDownloader.download(RokUtil.getImagePath(deviceState, appInfo), imageView);
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

    ((Button) findViewById(R.id.button_home)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        home();
      }
    });
    ((Button) findViewById(R.id.button_show_keyboard))
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            showKeyboard();
          }
        });
    ((Button) findViewById(R.id.button_speak)).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        speak();
      }
    });

    startedApp.set(false);
  }

  private void home() {
    RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.HOME, new Callback<String>() {
      @Override public void callback(String value) {
        Intent intent = new Intent(RokuRemoteControlActivity.this, RokuAppChooserActivity.class);
        intent.putExtra(Roku.DEVICE_STATE, deviceState);
        startActivity(intent);
      }
    });
  }

  private void showKeyboard() {
    InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMgr.toggleSoftInput(0, 0);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    char unicodeChar = (char) event.getUnicodeChar();
    RokUtil.pressKey(deviceState, unicodeChar);
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

  /**
   * Fire an intent to start the voice recognition activity.
   */
  private void speak() {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
    startActivityForResult(intent, REQUEST_CODE);
  }

  /**
   * Handle the results from the voice recognition activity.
   */
  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      // Populate the wordsList with the String values the recognition engine
      // thought it heard
      ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      Log.d("Words", matches.toString());

      launchApp(matches);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void launchApp(ArrayList<String> matches) {
    speechLauncher.actOn(matches, this, deviceState);
  }

}
