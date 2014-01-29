package com.jeffpalm.roku.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class RokuAndroidApp extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = new Intent(this, RokuDeviceChooserActivity.class);
    startActivity(intent);
  }

}
