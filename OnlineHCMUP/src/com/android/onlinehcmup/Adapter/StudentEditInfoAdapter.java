package com.android.onlinehcmup.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.onlinehcmup.R;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Support.SessionManager;

public class StudentEditInfoAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<JSONType> data;

	SessionManager session;

	public StudentEditInfoAdapter(Activity a, ArrayList<JSONType> d) {
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
				R.layout.row_student_edit_info, null);
		session.checkLogin();
		TextView key = (TextView) row.findViewById(R.id.key);
		JSONType item = new JSONType();
		item = data.get(position);
		key.setText(item.key);

		EditText value = (EditText) row.findViewById(R.id.value);
		value.setText(item.value);

		// set dynamic size for value
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		key.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int padding = 20;
		value.setPadding(key.getMeasuredWidth() + padding, 10, padding, 10);

		return row;
	}
}