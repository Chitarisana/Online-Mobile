package com.android.onlinehcmup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.onlinehcmup.Adapter.PrivateNewsAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Model.PrivateNews;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class PrivateNewsFragment extends BaseFragment {

	PrivateNewsAdapter adapter;
	SessionManager session;
	public static ViewGroup listGroup;
	public static ArrayList<PrivateNews> data;
	public static Activity activity;

	public PrivateNewsFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle(
				getActivity().getResources().getString(
						R.string.menu_main_private));
		View v = inflater.inflate(R.layout.activity_public_main, container,
				false);
		session = new SessionManager(StaticTAG.ACTIVITY);
		activity = getActivity();
		HashMap<String, String> user = session.getUserDetails();
		String studentID = user.get(SessionManager.KEY_STUDENTID);
		data = new ArrayList<PrivateNews>();
		listGroup = (ViewGroup) v.findViewById(R.id.listview);
		Connect connect = new Connect(activity);
		connect.LoadPrivateNews(studentID);
		return v;
	}
}
