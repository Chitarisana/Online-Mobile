package com.android.onlinehcmup.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.onlinehcmup.R;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Support.SessionManager;

public class StudentInfoAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<JSONType> data;

	SessionManager session;

	public StudentInfoAdapter(Activity a, ArrayList<JSONType> d) {
		activity = a;
		data = d;
		session = new SessionManager(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = (activity.getLayoutInflater()).inflate(
				R.layout.row_student_info, null);
		session.checkLogin();
		TextView key = (TextView) row.findViewById(R.id.key);
		TextView value = (TextView) row.findViewById(R.id.value);
		JSONType item = new JSONType();
		item = data.get(position);

		key.setText(item.key);
		value.setText(item.value);

		// set dynamic size for key and value textview
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		key.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		value.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		int keyWidth = key.getMeasuredWidth();
		int valueWidth = value.getMeasuredWidth();
		double padding = 0.15 * keyWidth;
		double total = displaymetrics.widthPixels - padding;
		double percent = 1.0 * keyWidth / (keyWidth + valueWidth);
		percent = Math.max(percent, 0.3);
		key.setMaxWidth((int) (total * percent));
		value.setMaxWidth((int) (total * (1 - percent)));

		// int padding = 60; // total padding ~ 60
		// value.setWidth(displaymetrics.widthPixels // - padding
		// - key.getMeasuredWidth());

		return row;
	}
}