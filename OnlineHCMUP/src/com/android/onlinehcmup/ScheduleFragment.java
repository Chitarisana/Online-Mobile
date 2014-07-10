package com.android.onlinehcmup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlinehcmup.Adapter.KeyValueAdapter;
import com.android.onlinehcmup.Adapter.MenuAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.JSON.LocalTask;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Model.ScheduleCalendar;
import com.android.onlinehcmup.Model.ScheduleExamination;
import com.android.onlinehcmup.Model.TermSchedule;
import com.android.onlinehcmup.Support.DialogManager;
import com.android.onlinehcmup.Support.SessionManager;

public class ScheduleFragment extends BaseFragment {
	static Activity activity;

	static LinearLayout content;
	static Spinner spnYear;
	static TextView date;
	static String dateString;
	public static ArrayList<ScheduleCalendar> scheduleCalendar;
	public static ArrayList<TermSchedule> TermYears;
	public static String startDate, endDate;

	public ScheduleFragment() {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		 * case R.id.action_view_exam: if (!MenuAdapter.menuLV.isShown()) {
		 * getFragmentManager() .beginTransaction() .replace(R.id.content, new
		 * ExaminationFragment(), StaticTAG.TAG_SCHEDULE_EXAMINATE)
		 * .addToBackStack(null).commit(); } else {
		 * MenuAdapter.mainLayout.closeDrawer(MenuAdapter.menuLV); } return
		 * true;
		 */
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		SessionManager session = new SessionManager(activity);
		session.checkLogin();

		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(R.string.menu_4_1);
		setHasOptionsMenu(true);

		View row = inflater.inflate(R.layout.fragment_schedule, container,
				false);

		spnYear = (Spinner) row.findViewById(R.id.spnYear);

		date = (TextView) row.findViewById(R.id.date);
		dateString = "Từ %s đến %s";

		TextView viewCurrent = (TextView) row.findViewById(R.id.viewCurrent);
		viewCurrent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startDate = "";
				setCalenderDate(0);
			}
		});

		ImageView btnLeft = (ImageView) row.findViewById(R.id.btnLeft);
		ImageView btnRight = (ImageView) row.findViewById(R.id.btnRight);
		btnLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setCalenderDate(-Calendar.DAY_OF_WEEK);
			}
		});
		btnRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setCalenderDate(Calendar.DAY_OF_WEEK);
			}
		});
		content = (LinearLayout) row.findViewById(R.id.calendar_content);

		String studentID = session.getStudentID();
		Connect connect = new Connect(activity);
		PrivateMainActivity.currentTask = connect
				.LoadScheduleCalendar(studentID);
		return row;
	}

	private static void setCalenderDate(int addNum) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		Calendar cal = Calendar.getInstance();
		try {
			if (startDate != "" && startDate != null) {
				cal.setTime(dateFormat.parse(startDate));
			}
			cal.add(Calendar.DAY_OF_WEEK, addNum);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			startDate = dateFormat.format(cal.getTime());
			cal.add(Calendar.DAY_OF_WEEK, 6);
			endDate = dateFormat.format(cal.getTime());
			LocalTask.ScheduleCalendarContentTask();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static void setSpinner(final Spinner spn, ArrayList<String> list) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				R.layout.row_spinner, R.id.text, list);
		adapter.setDropDownViewResource(R.layout.row_popup_spinner);
		spn.setAdapter(adapter);
		spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				startDate = LocalTask.GetStartDate(
						TermYears.get(position).YearStudy,
						TermYears.get(position).TermID);
				setCalenderDate(0);
				LocalTask.ScheduleCalendarContentTask();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public static void LoadFragment() {
		if (TermYears == null) {
			Toast.makeText(activity, R.string.noti_null, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < TermYears.size(); i++) {
			list.add(TermYears.get(i).GetName());
		}
		setSpinner(spnYear, list);
	}

	public static void LoadContentCalendar() {
		if (scheduleCalendar == null) {
			Toast.makeText(activity, R.string.noti_null, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		date.setText(String.format(dateString, startDate, endDate));
		CalendarContentAdapter adapter = new CalendarContentAdapter();
		int adapterCount = adapter.getCount();
		content.removeAllViews();
		for (int i = 0; i < adapterCount; i++) {
			View item = adapter.getView(i, null, null);
			content.addView(item);
		}
	}

	public static class CalendarContentAdapter extends BaseAdapter {
		final int periodTotal = 12;
		final String[] startTime = new String[] { "06h30", "07h20", "08h10",
				"09h05", "09h55", "10h45", "12h30", "13h20", "14h10", "15h05",
				"15h55", "16h45" };
		final int[] dateOfWeek = new int[] { R.id.time_0_1, R.id.time_0_2,
				R.id.time_0_3, R.id.time_0_4, R.id.time_0_5, R.id.time_0_6,
				R.id.time_0_7 };
		final String[] dateOfWeekVi = new String[] { "Thứ 2", "Thứ 3", "Thứ 4",
				"Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật" };
		final int[] colors = new int[] { Color.CYAN, Color.BLUE, Color.GRAY,
				Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.RED,
				Color.LTGRAY };

		public CalendarContentAdapter() {
		}

		@Override
		public int getCount() {
			return periodTotal;
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
			View row = (activity.getLayoutInflater()).inflate(
					R.layout.row_content_calendar, null);
			TextView hour = (TextView) row.findViewById(R.id.time_0);
			hour.setText((position + 1) + "\n"
					+ startTime[position].replace("h", ":"));

			for (int i = 0; i < 7; i++) {// 7 days
				for (int j = 0; j < scheduleCalendar.size(); j++) {
					final ScheduleCalendar calendar = scheduleCalendar.get(j);
					if (calendar.DayOfWeek.compareTo(dateOfWeekVi[i]) == 0) {
						int start = Integer.parseInt(calendar.StartPeriod);
						int end = Integer.parseInt(calendar.EndPeriod);
						if ((position + 1 <= end && position + 1 >= start)) {
							TextView text = (TextView) row
									.findViewById(dateOfWeek[i]);
							text.setBackgroundColor(colors[j % colors.length]);
							text.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									ArrayList<JSONType> data = new ArrayList<JSONType>();
									data.add(new JSONType("Số tín chỉ: ",
											calendar.GetCredits()));
									data.add(new JSONType("Ngày học: ",
											calendar.DayOfWeek + ", Tiết "
													+ calendar.StartPeriod
													+ " - "
													+ calendar.EndPeriod));
									data.add(new JSONType("Thời gian: ",
											calendar.BeginTime + " - "
													+ calendar.EndTime));
									data.add(new JSONType("Phòng: ",
											calendar.RoomID));
									data.add(new JSONType("GV: ", calendar
											.GetTeacherName()));
									DialogManager.showPopupDialog(activity,
											calendar.GetCurriculumName(), data,
											0, 0, null).show();
								}
							});
						}
					}
				}
			}
			return row;
		}
	}

	public static class ExaminationFragment extends BaseFragment {
		public static ArrayList<TermSchedule> TermSchedules;
		public static TermSchedule current;
		public static ArrayList<ScheduleExamination> ExamCurris;
		public static ListView examDetails, examList;
		TextView title;
		public static TextView help;
		SessionManager session;

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.examinate, menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_choose_term:
				if (!MenuAdapter.menuLV.isShown()) {
					final String[] termList = new String[TermSchedules.size()];
					for (int i = 0; i < termList.length; i++) {
						termList[i] = TermSchedules.get(i).GetName();
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							activity, R.layout.row_popup_list, R.id.text,
							termList);
					final Dialog dialog = DialogManager.showPopupDialog(
							activity, getString(R.string.menu_choose_term),
							adapter, 0, 0, null);

					final ListView list = (ListView) dialog
							.findViewById(R.id.listview);

					list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> adapterView,
								View v, int position, long arg3) {
							list.setItemChecked(position, true);
							title.setText(termList[position]);
							dialog.dismiss();

							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									activity, R.layout.row_registered_current,
									R.id.curriName, new String[] {});
							examList.setAdapter(adapter);
							examDetails.setAdapter(adapter);

							Connect connect = new Connect(activity);
							PrivateMainActivity.currentTask = connect.LoadScheduleExamination(
									session.getStudentID(),
									TermSchedules.get(position).YearStudy,
									TermSchedules.get(position).TermID);
						}
					});
					dialog.show();
				} else {
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
			activity = getActivity();
			session = new SessionManager(activity);
			session.checkLogin();

			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			setTitle(R.string.menu_4_2);
			setHasOptionsMenu(true);

			View row = inflater.inflate(R.layout.fragment_registered_current,
					container, false);

			HashMap<String, String> user = session.getUserDetails();
			String studentID = user.get(SessionManager.KEY_STUDENT_ID);
			String studentName = user.get(SessionManager.KEY_STUDENT_NAME);
			TextView stdName = (TextView) row.findViewById(R.id.studentName);
			stdName.setText(studentName);
			TextView stdID = (TextView) row.findViewById(R.id.studentID);
			stdID.setText(studentID);

			TermSchedules = LocalTask.GetTermSchedule();

			title = (TextView) row.findViewById(R.id.title);

			title.setBackgroundColor(getColor(R.color.titleScheduleColor));
			help = (TextView) row.findViewById(R.id.help);
			help.setText(R.string.help_title_exam);
			examDetails = (ListView) row.findViewById(R.id.details);
			examList = (ListView) row.findViewById(R.id.content);
			examList.setItemsCanFocus(true);

			HashMap<String, String> current = session.getCurrentTermYear();
			String termID = current.get(SessionManager.KEY_TERM_ID);
			String yearStudy = current.get(SessionManager.KEY_YEAR_STUDY);
			Connect connect = new Connect(activity);
			PrivateMainActivity.currentTask = connect.LoadScheduleExamination(
					studentID, yearStudy, termID);
			return row;
		}

		public static void LoadFragment() {
			ArrayList<String> curriNames = new ArrayList<String>();
			for (int i = 0; i < ExamCurris.size(); i++) {
				curriNames.add(ExamCurris.get(i).CurriculumName);
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
					R.layout.row_registered_current, R.id.curriName, curriNames);
			examList.setAdapter(adapter);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					(int) (displaymetrics.heightPixels / 5));
			examList.setLayoutParams(params);
			examList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long arg3) {
					// get Details of that curriculum
					ArrayList<JSONType> data = new ArrayList<JSONType>();
					data.add(new JSONType("Ngày thi",
							ExamCurris.get(position).Day));
					data.add(new JSONType("Giờ thi",
							ExamCurris.get(position).Time));
					data.add(new JSONType("Địa điểm",
							ExamCurris.get(position).Address));
					data.add(new JSONType("Phòng thi",
							ExamCurris.get(position).Room));
					examDetails.setAdapter(new KeyValueAdapter(activity, data,
							R.layout.row_study_program_term_details, null));
					String text = activity.getString(R.string.help_change_exam)
							+ " " + ExamCurris.get(position).CurriculumName;
					help.setText(text.trim());
				}
			});
		}
	}
}