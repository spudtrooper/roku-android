package com.jeffpalm.roku.android.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;

import com.jeffpalm.roku.android.Command;
import com.jeffpalm.roku.android.RokUtil;
import com.jeffpalm.roku.android.RokuAppInfo;
import com.jeffpalm.roku.android.RokuDeviceState;
import com.jeffpalm.util.Callback;

public class SpeechLauncher {

  public void actOn(List<String> matches, Activity activity, RokuDeviceState deviceState) {

    List<String> allWords = new ArrayList<String>();
    for (String match : matches) {
      for (String part : match.split(" ")) {
        allWords.add(part.toLowerCase());
      }
    }

    // See if it's home
    if (allWords.contains("home")) {
      launchHome(activity, deviceState);
      return;
    }

    if (allWords.contains("remote")) {
      launchRemote(activity, deviceState);
      return;
    }

    for (RokuAppInfo appInfo : deviceState.getAppInfos()) {
      for (String match : matches) {
        for (String part : match.split(" ")) {
          String name = appInfo.getName().toLowerCase();
          if (name.contains(part) || part.contains(name)) {
            launchApp(activity, deviceState, appInfo);
            return;
          }
        }
      }
    }
  }

  private void launchApp(Activity activity, RokuDeviceState deviceState, RokuAppInfo appInfo) {
    Intent intent = new Intent(activity, RokuRemoteControlActivity.class);
    intent.putExtra(Roku.DEVICE_STATE, deviceState);
    intent.putExtra(Roku.APP_INFO, appInfo);
    activity.startActivity(intent);
  }

  private void launchRemote(Activity activity, RokuDeviceState deviceState) {
    Intent intent = new Intent(activity, RokuRemoteControlActivity.class);
    intent.putExtra(Roku.DEVICE_STATE, deviceState);
    activity.startActivity(intent);
  }

  private void launchHome(final Activity activity, final RokuDeviceState deviceState) {
    RokUtil.executeSimpleCommand(deviceState, Command.SimpleCommands.HOME, new Callback<String>() {
      @Override public void callback(String value) {
        Intent intent = new Intent(activity, RokuAppChooserActivity.class);
        intent.putExtra(Roku.DEVICE_STATE, deviceState);
        activity.startActivity(intent);
      }
    });
  }
}
