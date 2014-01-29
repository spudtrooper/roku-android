package com.jeffpalm.roku.android.app;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffpalm.roku.android.InputStreamAsyncTask;
import com.jeffpalm.roku.android.ReadsFromInputStream;
import com.jeffpalm.roku.android.RokUtil;
import com.jeffpalm.roku.android.RokuAppInfo;
import com.jeffpalm.roku.android.RokuDeviceState;
import com.jeffpalm.roku.android.RokuPaths;
import com.jeffpalm.roku.android.util.SystemUiHider;
import com.jeffpalm.util.Option;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class RokuAppChooserActivity extends ListActivity {

  private final static String TAG = "RokuDeviceChooserActivity";
  private static final int REQUEST_CODE = 1234;
  private RokuAppInfoAdapter listAdapter;
  private RokuDeviceState deviceState;

  public class RokuAppInfoAdapter extends ArrayAdapter<RokuAppInfo> {
    public RokuAppInfoAdapter(Context context, ArrayList<RokuAppInfo> appInfos) {
      super(context, R.layout.roku_app_linear_layout, appInfos);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      RokuAppInfo appInfo = getItem(position);
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.roku_app_linear_layout,
            null);
      }
      TextView textName = (TextView) convertView.findViewById(R.id.text_name);
      TextView textDescription = (TextView) convertView.findViewById(R.id.text_description);
      ImageView imageDevice = (ImageView) convertView.findViewById(R.id.image_app);
      textName.setText(appInfo.getName());
      textDescription.setText(appInfo.getVersion());
      //RokUtil.setImageView(deviceState, appInfo, imageDevice);
      return convertView;
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_roku_app_chooser);
    listAdapter = new RokuAppInfoAdapter(this, new ArrayList<RokuAppInfo>());
    setListAdapter(listAdapter);

    Bundle extras = getIntent().getExtras();
    deviceState = (RokuDeviceState) extras.get(Roku.DEVICE_STATE);
    if (deviceState == null) {
      throw new IllegalArgumentException("Expected device state for " + Roku.DEVICE_STATE + "extra");
    }

    ListView list = (ListView) findViewById(android.R.id.list);
    list.setClickable(true);

    Button speakButton = (Button) findViewById(R.id.button_speak);
    speakButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View arg0) {
        speak();
      }
    });

    if (deviceState.getAppInfos().isEmpty()) {
      RokuPaths paths = new RokuPaths(deviceState.getHost());
      String url = paths.getAppInfoPath();
      final ProgressDialog dialog = new ProgressDialog(this);
      new InputStreamAsyncTask<List<RokuAppInfo>>(new ReadsFromInputStream<List<RokuAppInfo>>() {
        @Override public List<RokuAppInfo> fromInputStream(InputStream in) throws Exception {
          return RokuAppInfo.fromInputStream(in);
        }
      }) {
        @Override protected void onPreExecute() {
          dialog.setMessage(getResources().getText(R.string.finding_apps));
          dialog.show();
        }

        @Override protected void onPostExecute(Option<List<RokuAppInfo>, Exception> result) {
          if (dialog.isShowing()) {
            dialog.dismiss();
          }
          if (result.isSuccess()) {
            Log.d(TAG, String.format("Have %d apps", result.getSuccess().size()));
            // TODO: Take these from the device state, not a separate adapter
            deviceState.addAllAppInfos(result.getSuccess());
            listAdapter.addAll(result.getSuccess());
          } else {
            Toast.makeText(getApplicationContext(),
                getResources().getText(R.string.could_not_find_any_apps), Toast.LENGTH_LONG).show();
          }
        }
      }.execute(url);
    }
  }

  @Override protected void onListItemClick(ListView l, View v, int position, long id) {
    RokuAppInfo appInfo = (RokuAppInfo) getListAdapter().getItem(position);
    launchApp(appInfo);
  }

  private void launchApp(RokuAppInfo appInfo) {
    Intent intent = new Intent(this, RokuRemoteControlActivity.class);
    intent.putExtra(Roku.DEVICE_STATE, deviceState);
    intent.putExtra(Roku.APP_INFO, appInfo);
    startActivity(intent);
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
    outer: for (RokuAppInfo appInfo : deviceState.getAppInfos()) {
      for (String match : matches) {
        for (String part : match.split(" ")) {
          if (appInfo.getName().contains(part) || part.contains(appInfo.getName())) {
            launchApp(appInfo);
            break outer;
          }
        }
      }
    }
  }
}
