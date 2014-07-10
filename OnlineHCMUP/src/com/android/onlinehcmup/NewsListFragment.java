package com.android.onlinehcmup;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.onlinehcmup.Model.PrivateNews;
import com.android.onlinehcmup.Model.PublicNews;
import com.android.onlinehcmup.SQLite.DatabaseHandler.PrivateNewsDAL;
import com.android.onlinehcmup.Support.StaticTAG;

public class NewsListFragment extends BaseFragment {
	public static String KEY_TYPE = "TYPE";
	public static int TYPE_PUBLIC = 0;
	public static int TYPE_PRIVATE = 1;
	public static String KEY_TITLE = "TITLE";
	public static String KEY_CONTENT = "CONTENT";
	static int TYPE = TYPE_PUBLIC;

	public NewsListFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frame_news_list, container, false);
		ListView listView = (ListView) v.findViewById(R.id.listview);
		Bundle extras = getArguments();
		TYPE = extras.getInt(KEY_TYPE);
		if (TYPE == TYPE_PUBLIC) {
			listView.setAdapter(new PublicNewsAdapter(MainActivity.activity,
					MainActivity.data));
		} else {
			if (PrivateNewsFragment.data == null) {
				// Log.d("data null", "News List Fragment");
				PrivateNewsFragment.data = PrivateNewsDAL.GetAll();
			}
			listView.setAdapter(new PrivateNewsAdapter(
					PrivateMainActivity.activity, PrivateNewsFragment.data));
		}
		listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		return v;
	}

	public View loadView(final Activity activity, final String titleString,
			String dateString, final String shortDetailString, final String tag) {
		View row = (activity.getLayoutInflater()).inflate(R.layout.row_news,
				null);
		TextView title = (TextView) row.findViewById(R.id.title);
		TextView date = (TextView) row.findViewById(R.id.date);
		TextView shortDetail = (TextView) row.findViewById(R.id.shortdetails);
		title.setText(titleString);
		date.setText(dateString);

		//shortDetail.setText(Html.fromHtml(shortDetailString));
		shortDetail.setText(addLinks(shortDetailString));
		/*Linkify.addLinks(
				shortDetail,
				Pattern.compile("(<a.*?>)(.+?)(</a>)"),
				shortDetailString.substring(
						shortDetailString.indexOf("href=\"")+6, ...));*/
		// s.substring(s.indexOf("[") + 1, s.indexOf("]"));
		// Linkify.addLinks(shortDetail, Linkify.ALL);
		// shortDetail.setMovementMethod(LinkMovementMethod.getInstance());

		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = new NewsDetailsFragment();
				Bundle args = new Bundle();
				args.putInt(KEY_TYPE, TYPE);
				args.putString(KEY_TITLE, titleString);
				args.putString(KEY_CONTENT, shortDetailString);
				fragment.setArguments(args);

				FragmentManager fragmentManager = activity.getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.newsdetails, fragment, tag)
						.addToBackStack(null).commit();
			}
		});
		return row;
	}

	private Spannable addLinks(String html) {
		Spanned s = Html.fromHtml(html);
		URLSpan[] currentSpans = s.getSpans(0, s.length(), URLSpan.class);

		SpannableString buffer = new SpannableString(s);
		Linkify.addLinks(buffer, Linkify.ALL);

		for (URLSpan span : currentSpans) {
			int end = s.getSpanEnd(span);
			int start = s.getSpanStart(span);
			buffer.setSpan(span, start, end, 0);
			
		}
		return buffer;
	}

	public class PublicNewsAdapter extends BaseAdapter {
		public Activity activity;
		public ArrayList<PublicNews> data;

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public PublicNewsAdapter(Activity a, ArrayList<PublicNews> d) {
			activity = a;
			data = d;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			PublicNews news = new PublicNews();
			news = data.get(position);
			return loadView(activity, news.MessageSubject, news.CreationDate,
					news.MessageNote, StaticTAG.TAG_PUBLIC_NEWS_DETAILS);
		}
	}

	public class PrivateNewsAdapter extends BaseAdapter {
		public ArrayList<PrivateNews> data;
		public Activity activity;

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public PrivateNewsAdapter(Activity a, ArrayList<PrivateNews> d) {
			activity = a;
			data = d;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			PrivateNews news = new PrivateNews();
			news = data.get(position);
			return loadView(activity, news.MessageSubject, news.CreationDate,
					news.MessageBody, StaticTAG.TAG_PRIVATE_NEWS_DETAILS);
		}
	}
}