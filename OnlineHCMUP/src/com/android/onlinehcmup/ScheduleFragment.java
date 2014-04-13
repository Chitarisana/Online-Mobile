package com.android.onlinehcmup;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.android.onlinehcmup.Support.StaticTAG;

public class ScheduleFragment extends BaseFragment {
	public static int schedulePosition;
	public SessionManager session;

	public ScheduleFragment() {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.schedule, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_view_exam:
			if (!MenuAdapter.menuLV.isShown()) {
				// navigate to fragment view exam
				// MenuAdapter.mainLayout.closeDrawer(MenuAdapter.menuLV);
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.content, new ExaminateFragment(),
								StaticTAG.TAG_SCHEDULE_EXAMINATE)
						.addToBackStack(null).commit();
			} else {
				MenuAdapter.menuLV.setItemChecked(schedulePosition, true);
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
				getActivity().getResources().getString(R.string.menu_4_1));
		setHasOptionsMenu(true);
		View row = inflater.inflate(R.layout.fragment_registered_current,
				container, false);
		TextView studentName = (TextView) row.findViewById(R.id.studentName);
		studentName.setText("Phạm Thị Ngọc Diệp");
		TextView studentID = (TextView) row.findViewById(R.id.studentID);
		studentID.setText("K36.104.013");
		TextView title = (TextView) row.findViewById(R.id.title);
		title.setText("Thời khóa biểu hiện tại");
		title.setBackgroundColor(getActivity().getResources().getColor(
				R.color.titleScheduleColor));
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
			curriNames.add(curris.get(i).CurriculumID);
		}
		list.setItemsCanFocus(true);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.row_registered_current, R.id.curriName, curriNames);
		list.setAdapter(adapter);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displaymetrics.heightPixels / 3));
		list.setLayoutParams(params);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// get Details of that curriculum
				ArrayList<JSONType> data = new ArrayList<JSONType>();
				data.add(new JSONType("Tên học phần",
						curris.get(position).CurriculumName));
				data.add(new JSONType("Số tín chỉ", curris.get(position).Credit
						+ ""));
				data.add(new JSONType("Ngày học", "Chưa add"));
				data.add(new JSONType("Giảng viên", "Chưa add"));
				details.setAdapter(new ScheduleDetailsAdapter(data));
			}
		});
		return row;
	}

	public class ScheduleDetailsAdapter extends BaseAdapter {
		private ArrayList<JSONType> data;

		public ScheduleDetailsAdapter(ArrayList<JSONType> d) {
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

	public static class ExaminateFragment extends BaseFragment {
		String titleString="Năm học 2013-2014, học kỳ ";
		int term=1;
		TextView title ;
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.examinate, menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_choose_term:

				final String[] termList = new String[8];
				for (int i = 0; i < 8; i++)
					termList[i] = titleString + (i + 1);
				ListView list = new ListView(getActivity());
				list.setAdapter(new ArrayAdapter<String>(getActivity(),
						R.layout.row_popup_list, termList));
				View parent = (getActivity().getLayoutInflater()).inflate(
						R.layout.activity_private_main, null);
				final PopupWindow popup= DialogManager.showListDialog(getActivity(),
						list, parent);

				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView, View v,
							int position, long arg3) {
						Log.d("position", termList[position] + "");
						term = position+1;
						 title.setText(titleString+term);
						// set content here.....
						popup.dismiss();
					}
				});
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setTitle(
					getActivity().getResources().getString(R.string.menu_4_2));
			PrivateMainActivity.menuToggle.setDrawerIndicatorEnabled(false);
			PrivateMainActivity.mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			setHasOptionsMenu(true);

			View row = inflater.inflate(R.layout.fragment_registered_current,
					container, false);
			TextView studentName = (TextView) row
					.findViewById(R.id.studentName);
			studentName.setText("Phạm Thị Ngọc Diệp");
			TextView studentID = (TextView) row.findViewById(R.id.studentID);
			studentID.setText("K36.104.013");
			title= (TextView) row.findViewById(R.id.title);
			title.setText(titleString+term);
			title.setBackgroundColor(getActivity().getResources().getColor(
					R.color.titleScheduleColor));
			TextView help = (TextView) row.findViewById(R.id.help);
			help.setText("Chọn một học phần để xem lịch thi");
			final ListView details = (ListView) row.findViewById(R.id.details);
			ListView list = (ListView) row.findViewById(R.id.content);
			final ArrayList<Curriculum> curris = new ArrayList<Curriculum>();
			curris.add(new Curriculum("MATH1008",
					"Đại số tuyến tính và Hình học giải tích", "Bắt buộc", 3));
			curris.add(new Curriculum("MATH1002", "Giải tích 1", "Bắt buộc", 3));
			curris.add(new Curriculum("POLI1901",
					"Những nguyên lý cơ bản của chủ nghĩa Mac-Lenin",
					"Bắt buộc", 2));
			curris.add(new Curriculum("FLAN1001", "Tiếng Anh học phần 1",
					"Tự chọn", 4));
			curris.add(new Curriculum("COMP1001", "Tin học căn bản",
					"Bắt buộc", 3));
			curris.add(new Curriculum("COMP1001", "Tin học căn bản",
					"Bắt buộc", 3));
			curris.add(new Curriculum("COMP1001", "Tin học căn bản",
					"Bắt buộc", 3));
			curris.add(new Curriculum("COMP1001", "Tin học căn bản",
					"Bắt buộc", 3));
			curris.add(new Curriculum("COMP1001", "Tin học căn bản",
					"Bắt buộc", 3));
			ArrayList<String> curriNames = new ArrayList<String>();
			for (int i = 0; i < curris.size(); i++) {
				curriNames.add(curris.get(i).CurriculumName);
			}
			list.setItemsCanFocus(true);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), R.layout.row_registered_current,
					R.id.curriName, curriNames);
			list.setAdapter(adapter);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					(int) (displaymetrics.heightPixels / 3));
			list.setLayoutParams(params);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long arg3) {
					// get Details of that curriculum
					ArrayList<JSONType> data = new ArrayList<JSONType>();
					data.add(new JSONType("Ngày thi",
							curris.get(position).CurriculumName));
					data.add(new JSONType("Giờ thi",
							curris.get(position).Credit + ""));
					data.add(new JSONType("Địa điểm", "Chưa add"));
					data.add(new JSONType("Phòng thi", "Chưa add"));
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
}