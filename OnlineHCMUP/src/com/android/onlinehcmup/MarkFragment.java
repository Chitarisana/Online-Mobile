package com.android.onlinehcmup;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.onlinehcmup.Adapter.MenuAdapter;
import com.android.onlinehcmup.Model.Curriculum;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Support.DialogManager;
import com.android.onlinehcmup.Support.SessionManager;

public class MarkFragment extends BaseFragment {
	public SessionManager session;
	public static int markPosition;
	String titleString = "Năm học 2013-2014, học kỳ ";
	int term = 1;
	TextView title;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.examinate, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_choose_term:
			if (!MenuAdapter.menuLV.isShown()) {
				final String[] termList = new String[8];
				for (int i = 0; i < 8; i++)
					termList[i] = titleString + (i + 1);
				ListView list = new ListView(getActivity());
				list.setAdapter(new ArrayAdapter<String>(getActivity(),
						R.layout.row_popup_list, termList));
				View parent = (getActivity().getLayoutInflater()).inflate(
						R.layout.activity_private_main, null);
				final PopupWindow popup = DialogManager.showListDialog(
						getActivity(), list, parent);

				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView, View v,
							int position, long arg3) {
						Log.d("position", termList[position] + "");
						term = position + 1;
						title.setText(titleString + term);
						// set content here.....
						popup.dismiss();
					}
				});
			} else {
				MenuAdapter.menuLV.setItemChecked(markPosition, true);
				MenuAdapter.mainLayout.closeDrawer(MenuAdapter.menuLV);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		session = new SessionManager(getActivity().getApplicationContext());
		session.checkLogin();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setTitle(
				getActivity().getResources().getString(R.string.menu_5_1));
		setHasOptionsMenu(true);

		View row = inflater.inflate(R.layout.fragment_mark, container, false);
		TextView studentName = (TextView) row.findViewById(R.id.studentName);
		studentName.setText("Phạm Thị Ngọc Diệp");
		TextView studentID = (TextView) row.findViewById(R.id.studentID);
		studentID.setText("K36.104.013");
		title = (TextView) row.findViewById(R.id.title);
		title.setText(titleString + term);

		TextView help = (TextView) row.findViewById(R.id.help);
		help.setText("Chọn một học phần để xem chi tiết");
		final ListView details = (ListView) row.findViewById(R.id.details);
		ListView list = (ListView) row.findViewById(R.id.content);
		final ArrayList<Curriculum> curris = new ArrayList<Curriculum>();
		curris.add(new Curriculum("MATH1008",
				"Đại số tuyến tính và Hình học giải tích", "Bắt buộc", 3));
		curris.add(new Curriculum("MATH1002", "Giải tích 1", "Bắt buộc", 3));
		curris.add(new Curriculum("POLI1901",
				"Những nguyên lý cơ bản của chủ nghĩa Mac-Lenin", "Bắt buộc", 2));
		curris.add(new Curriculum("FLAN1001", "Tiếng Anh học phần 1",
				"Tự chọn", 4));
		curris.add(new Curriculum("COMP1001", "Tin học căn bản", "Bắt buộc", 3));
		curris.add(new Curriculum("COMP1001", "Tin học căn bản", "Bắt buộc", 3));
		curris.add(new Curriculum("COMP1001", "Tin học căn bản", "Bắt buộc", 3));
		curris.add(new Curriculum("COMP1001", "Tin học căn bản", "Bắt buộc", 3));
		curris.add(new Curriculum("COMP1001", "Tin học căn bản", "Bắt buộc", 3));
		ArrayList<String> curriNames = new ArrayList<String>();
		for (int i = 0; i < curris.size(); i++) {
			curriNames.add(curris.get(i).CurriculumName);
		}
		list.setItemsCanFocus(true);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.row_mark, R.id.curriName, curriNames);
		list.setAdapter(adapter);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displaymetrics.heightPixels / 3));
		list.setLayoutParams(params);

		list.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				
				Log.d("event", event.getX() + "");
				Log.d("event", event.getY() + "");
				return false;
			}
		});
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// get Details of that curriculum
				ArrayList<JSONType> data = new ArrayList<JSONType>();
				data.add(new JSONType("Điểm",
						curris.get(position).CurriculumName));
				data.add(new JSONType("Điểm chữ", curris.get(position).Credits
						+ ""));
				data.add(new JSONType("Kết quả", "Đậu"));
				details.setAdapter(new ExaminateDetailsAdapter(data));
			}
		});
		return row;
	}

	public class ExaminateDetailsAdapter extends BaseAdapter {
		private ArrayList<JSONType> data;

		public ExaminateDetailsAdapter(ArrayList<JSONType> d) {
			data = d;
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = (getActivity().getLayoutInflater()).inflate(
					R.layout.row_study_program_term_details, null);
			TextView key = (TextView) row.findViewById(R.id.key);
			TextView value = (TextView) row.findViewById(R.id.value);

			key.setText(data.get(position).key);
			value.setText(data.get(position).value);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			key.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			value.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

			int padding = 60; // total padding
			value.setWidth(displaymetrics.widthPixels - padding
					- key.getMeasuredWidth());
			return row;
		}
	}
}
