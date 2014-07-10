package com.android.onlinehcmup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.onlinehcmup.Adapter.PrivateNewsAdapter;
import com.android.onlinehcmup.Adapter.PublicNewsAdapter;

public class NewsList extends BaseFragment {
	public ListView listView;
	public static String KEY_TYPE = "type";
	public static int KEY_PUBLIC = 0;
	public static int KEY_PRIVATE = 1;

	public NewsList() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frame_news_list, container, false);
		listView = (ListView) v.findViewById(R.id.listview);
		Bundle extras = getArguments();
		int type = extras.getInt(KEY_TYPE);
		if (type == KEY_PUBLIC) {
			listView.setAdapter(new PublicNewsAdapter(MainActivity.activity,
					MainActivity.data));
		} else {
			listView.setAdapter(new PrivateNewsAdapter(
					PrivateMainActivity.activity, PrivateNewsFragment.data));
		}
		listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

		return v;
	}
}