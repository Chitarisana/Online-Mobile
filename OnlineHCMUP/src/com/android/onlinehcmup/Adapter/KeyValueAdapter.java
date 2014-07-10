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

public class KeyValueAdapter extends BaseAdapter {
	ArrayList<JSONType> data;
	Activity activity;
	Boolean isKey;
	int layout;

	/*
	 * isKeyDepend = true: Key's size = static, Value's size = dynamic
	 * isKeyDepend = false: Key's size = dynamic, Value's size = static
	 * isKeyDepend = null: Key's size = dynamic, Value's size = dynamic
	 */
	public KeyValueAdapter(Activity a, ArrayList<JSONType> d, int layoutID,
			Boolean isKeyDepend) {
		data = d;
		activity = a;
		isKey = isKeyDepend;
		layout = layoutID;
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
		View row = (activity.getLayoutInflater()).inflate(layout != 0 ? layout
				: R.layout.row_popup_score_detail, null);
		TextView key = (TextView) row.findViewById(R.id.key);
		TextView value = (TextView) row.findViewById(R.id.value);

		key.setText(data.get(position).key);
		value.setText(data.get(position).value);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		key.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		value.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		int padding = (int) (displaymetrics.widthPixels * 0.08);

		int keyWidth = key.getMeasuredWidth();
		int valueWidth = value.getMeasuredWidth();
		int total = displaymetrics.widthPixels - padding;

		if (isKey == null) {
			double percent = 1.0 * keyWidth / (keyWidth + valueWidth);
			percent = Math.max(percent, 0.2);
			key.setMaxWidth((int) (total * percent));
			value.setMaxWidth((int) (total * (1 - percent))); // chua on lam...
		} else if (isKey) {
			// value.setMaxWidth(total - key.getMeasuredWidth());
			value.setPadding(key.getMeasuredWidth() + padding,
					value.getPaddingTop(), value.getPaddingRight(),
					value.getPaddingBottom());
		} else {
			// key.setMaxWidth(total - value.getMeasuredWidth());
			key.setPadding(key.getPaddingLeft(), key.getPaddingTop(),
					value.getMeasuredWidth() + padding, key.getPaddingBottom());
		}
		return row;
	}
}