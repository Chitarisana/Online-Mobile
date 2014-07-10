package com.android.onlinehcmup;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Model.PrivateNews;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class PrivateNewsFragment extends BaseFragment {

	public static ArrayList<PrivateNews> data;
	static Activity activity;

	public PrivateNewsFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		PrivateMainActivity.itemTitle = PrivateMainActivity.title = getString(R.string.menu_main_private);
		setTitle(R.string.menu_main_private);

		View v = inflater.inflate(R.layout.activity_public_main, container,
				false);
		SessionManager session = new SessionManager(activity);
		String studentID = session.getStudentID();
		// Log.d("run on create view", "Private News Fragment");
		Connect connect = new Connect(getActivity());
		PrivateMainActivity.currentTask = connect.LoadPrivateNews(studentID);
		return v;
	}

	public static void LoadFragment() {
		Log.d("run Load Fragment", "Private News Fragment");
		if (data == null)
			Log.d("data null", "Private News Fragment");
		NewsListFragment fragment = new NewsListFragment();
		Bundle args = new Bundle();
		args.putInt(NewsListFragment.KEY_TYPE, NewsListFragment.TYPE_PRIVATE);
		fragment.setArguments(args);

		activity.getFragmentManager()
				.beginTransaction()
				.replace(R.id.listview, fragment,
						StaticTAG.TAG_PRIVATE_NEWS_LIST)
				.disallowAddToBackStack().commit();
	}
}
