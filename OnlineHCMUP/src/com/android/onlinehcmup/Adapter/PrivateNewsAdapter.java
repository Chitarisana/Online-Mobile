package com.android.onlinehcmup.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.onlinehcmup.NewsDetailsFragment;
import com.android.onlinehcmup.R;
import com.android.onlinehcmup.Model.PrivateNews;
import com.android.onlinehcmup.Support.StaticTAG;

public class PrivateNewsAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<PrivateNews> data;

	public PrivateNewsAdapter(Activity a, ArrayList<PrivateNews> d) {
		activity = a;
		data = d;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = (activity.getLayoutInflater()).inflate(R.layout.row_news,
				null);
		TextView title = (TextView) row.findViewById(R.id.title);
		TextView date = (TextView) row.findViewById(R.id.date);
		TextView shortDetail = (TextView) row.findViewById(R.id.shortdetails);

		PrivateNews news = new PrivateNews();
		news = data.get(position);

		title.setText(news.MessageSubject);
		date.setText(news.CreationDate);
		shortDetail.setText(Html.fromHtml(news.MessageBody));
		shortDetail.setMovementMethod(LinkMovementMethod.getInstance());// chua
																		// test
		// Set Image
		// new DownloadImageTask(activity, shortDetail).execute(news.ImageURL);
		// Set Click Event
		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Fragment fragment = new NewsDetailsFragment();
				Bundle args = new Bundle();
				PrivateNews news = new PrivateNews();
				news = data.get(position);
				args.putInt(NewsDetailsFragment.KEY_TYPE,
						NewsDetailsFragment.KEY_PRIVATE);
				args.putString(NewsDetailsFragment.KEY_TITLE,
						news.MessageSubject);
				args.putString(NewsDetailsFragment.KEY_CONTENT,
						news.MessageBody);
				fragment.setArguments(args);

				FragmentManager fragmentManager = activity.getFragmentManager();

				fragmentManager
						.beginTransaction()
						.replace(R.id.newsdetails, fragment,
								StaticTAG.TAG_PRIVATE_NEWS_DETAILS)
						.addToBackStack(null).commit();
			}
		});
		return row;
	}
}
