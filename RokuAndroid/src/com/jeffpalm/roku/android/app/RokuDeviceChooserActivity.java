package com.jeffpalm.roku.android.app;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.jeffpalm.roku.android.RokuDeviceInfo;
import com.jeffpalm.roku.android.RokuDeviceState;
import com.jeffpalm.roku.android.RokuDeviceStateLocatorTask;
import com.jeffpalm.roku.android.util.SystemUiHider;
import com.jeffpalm.util.Option;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class RokuDeviceChooserActivity extends ListActivity {

  private final static String TAG = "RokuDeviceChooserActivity";
  private RokuDeviceStateAdapter listAdapter;

  public class RokuDeviceStateAdapter extends ArrayAdapter<RokuDeviceState> {
    public RokuDeviceStateAdapter(Context context, ArrayList<RokuDeviceState> deviceStates) {
      super(context, R.layout.roku_device_linear_layout, deviceStates);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      RokuDeviceState deviceState = getItem(position);
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.roku_device_linear_layout,
            null);
      }
      TextView textName = (TextView) convertView.findViewById(R.id.text_name);
      TextView textDescription = (TextView) convertView.findViewById(R.id.text_description);
      ImageView imageDevice = (ImageView) convertView.findViewById(R.id.image_device);
      textName.setText(String.format(getResources().getString(R.string.device_name_and_model),
          deviceState.getDeviceInfo().getFriendlyName(),
          String.valueOf(deviceState.getDeviceInfo().getModelNumber())));
      textDescription.setText(deviceState.getHost());
      imageDevice.setImageResource(getImageResource(deviceState));
      return convertView;
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_roku_device_chooser);
    listAdapter = new RokuDeviceStateAdapter(this, new ArrayList<RokuDeviceState>());
    setListAdapter(listAdapter);

    ListView list = (ListView) findViewById(android.R.id.list);
    list.setClickable(true);

    Button refreshButton = (Button) findViewById(R.id.button_refresh);
    refreshButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View arg0) {
        refresh();
      }
    });

    refresh();
  }

  private void refresh() {
    final ProgressDialog dialog = new ProgressDialog(this);
    new RokuDeviceStateLocatorTask() {
      protected void onProgressUpdate(Integer... values) {
        dialog.setProgress(values[0]);
      }
      @Override protected void onPreExecute() {
        dialog.setMessage(getResources().getText(R.string.finding_devices));
        dialog.setMax(getProgessLength());
        dialog.show();
      }
      @Override protected void onPostExecute(Option<List<RokuDeviceState>, Exception> result) {
        if (dialog.isShowing()) {
          dialog.dismiss();
        }
        Log.d(TAG, "Find devices success=" + result.getSuccess());
        if (result.isSuccess()) {
          Log.d(TAG, String.format("Have %d devices", result.getSuccess().size()));
          listAdapter.addAll(result.getSuccess());
        } else {
          Toast.makeText(getApplicationContext(),
              getResources().getText(R.string.could_not_find_any_devices), Toast.LENGTH_LONG)
              .show();
        }
      }
    }.execute();
  }

  @Override protected void onListItemClick(ListView l, View v, int position, long id) {
    final RokuDeviceState deviceState = (RokuDeviceState) getListAdapter().getItem(position);
    Intent intent = new Intent(RokuDeviceChooserActivity.this, RokuAppChooserActivity.class);
    intent.putExtra(Roku.DEVICE_STATE, deviceState);
    startActivity(intent);
  }

  public int getImageResource(RokuDeviceState deviceState) {
    RokuDeviceInfo info = deviceState.getDeviceInfo();
    String name = info.getModelName();
    if (name.contains("3")) {
      return R.drawable.roku_3_small;
    }
    if (name.contains("2")) {
      return R.drawable.roku_2_small;
    }
    if (name.contains("1")) {
      return R.drawable.roku_1_small;
    }
    return R.drawable.roku_lt_small;
  }
}
