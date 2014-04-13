package com.android.onlinehcmup.Support;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

/* Getting Image from URL
 * and show for PublicNews
 * (News without Login) */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

	Activity activity;
	TextView textView;
	ImageView imageView;

	public DownloadImageTask(Activity a, TextView tv) {
		activity = a;
		textView = tv;
	}

	public DownloadImageTask(Activity a, ImageView img) {
		activity = a;
		imageView = img;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(
					urldisplay).openConnection();
			connection.connect();
			InputStream input = connection.getInputStream();
			mIcon = BitmapFactory.decodeStream(input);
		} catch (MalformedURLException e1) {
		} catch (IOException e1) {
		}
		return mIcon;
	}

	protected void onPostExecute(Bitmap result) {
		if (result == null)
			return;
		Drawable drawable = new BitmapDrawable(activity.getResources(),
				Bitmap.createScaledBitmap(result, 120, 120, true));
		if (textView != null)
			textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
					null, null);
		if (imageView != null)
			imageView.setImageDrawable(drawable);
		// save image to Local Database
		// co nen save PublicNews???
	}
}
