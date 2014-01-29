package com.jeffpalm.util.android;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {

  ImageView imageView = null;

  @Override protected Bitmap doInBackground(ImageView... imageViews) {
    this.imageView = imageViews[0];
    return download_Image((String) imageView.getTag());
  }

  @Override protected void onPostExecute(Bitmap result) {
    imageView.setImageBitmap(result);
  }

  private Bitmap download_Image(String url) {

    Bitmap bmp = null;
    try {
      URL ulrn = new URL(url);
      HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
      InputStream is = con.getInputStream();
      bmp = BitmapFactory.decodeStream(is);
      if (null != bmp)
        return bmp;
    } catch (Exception e) {
    }
    return bmp;
  }

  public static void setImageURL(ImageView imageDevice, String imageURL) {
    imageDevice.setTag(imageURL);
    new DownloadImageTask().execute(imageDevice);
  }
}