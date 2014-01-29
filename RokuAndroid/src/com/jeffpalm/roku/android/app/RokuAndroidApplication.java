package com.jeffpalm.roku.android.app;

import android.app.Activity;
import android.app.Application;

public class RokuAndroidApplication extends Application {

  public static RokuAndroidApplication getApplication(Activity a) {
    return (RokuAndroidApplication) a.getApplication();
  }
}
