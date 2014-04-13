package com.android.onlinehcmup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Model.Curriculum;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Model.RegisterScheduleStudyUnit;
import com.android.onlinehcmup.Model.TermStudy;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class RegisterCurriculumFragment extends BaseFragment {
	static Activity activity;
	public SessionManager session;
	static String studentID, studentName;
	String[] tag = new String[] { StaticTAG.TAG_REGISTER_CURRICULUM_RESULT,
			StaticTAG.TAG_REGISTER_CURRICULUM_DISACCUMULATED,
			StaticTAG.TAG_REGISTER_CURRICULUM_REGISTER };

	public RegisterCurriculumFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		session = new SessionManager(activity.getApplicationContext());
		session.checkLogin();
		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		activity.getActionBar().setTitle(
				activity.getResources().getString(R.string.menu_3));

		View row = inflater.inflate(R.layout.fragment_register_curriculum,
				container, false);

		HashMap<String, String> user = session.getUserDetails();
		studentID = user.get(SessionManager.KEY_STUDENTID);
		studentName = user.get(SessionManager.KEY_STUDENT_NAME);

		Button btnResult = (Button) row.findViewById(R.id.btnResult);
		btnResult.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment frag = new ResultFragment();
				setHandlerButton(frag, tag[0]);
			}
		});
		Button btnDisAcc = (Button) row.findViewById(R.id.btnDisaccumulate);
		btnDisAcc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment frag = new DisaccumulatedFragment();
				setHandlerButton(frag, tag[1]);
			}
		});
		Button btnRegister = (Button) row.findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment frag = new RegisterActionFragment();
				setHandlerButton(frag, tag[2]);
			}
		});
		return row;
	}

	private void setHandlerButton(Fragment frag, String tag) {
		activity.getFragmentManager().beginTransaction()
				.replace(R.id.content, frag, tag).addToBackStack(null).commit();
	}

	public static class ResultFragment extends BaseFragment {
		public static LinearLayout termLayout;
		public static TermStudy[] TermStudies;

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.registered_curriculum, menu);
			menu.removeItem(R.id.action_view_all);
			// -> if in view current: remove view current
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_view_current:
				// navigate to fragment view current
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.content, new ResultCurrentFragment(),
								StaticTAG.TAG_REGISTER_CURRICULUM_RESULT_ALL)
						.addToBackStack(null).commit();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity.getActionBar().setDisplayHomeAsUpEnabled(true);
			activity.getActionBar().setTitle(
					activity.getResources().getString(
							R.string.register_curriculum_result_registered));
			PrivateMainActivity.menuToggle.setDrawerIndicatorEnabled(false);
			PrivateMainActivity.mainLayout
					.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			setHasOptionsMenu(true);

			View row = inflater.inflate(R.layout.fragment_study_program,
					container, false);
			TextView studentID = (TextView) row.findViewById(R.id.studentID);
			TextView studentName = (TextView) row
					.findViewById(R.id.studentName);

			studentID.setText(RegisterCurriculumFragment.studentID);
			studentName.setText(RegisterCurriculumFragment.studentName);
			termLayout = (LinearLayout) row.findViewById(R.id.term_layout);

			Connect connect = new Connect(activity);
			connect.LoadRegisterResultAll(RegisterCurriculumFragment.studentID);

			return row;
		}

		public static void LoadFragment() {
			ResultAdapter adapter = new ResultAdapter();
			int adapterCount = adapter.getCount();
			termLayout.removeAllViews();
			for (int i = 0; i < adapterCount; i++) {
				View item = adapter.getView(i, null, null);
				termLayout.addView(item);
			}
		}

		public static class ResultAdapter extends BaseAdapter {

			public ResultAdapter() {
			}

			@Override
			public int getCount() {
				return TermStudies.length;
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
				View row = activity.getLayoutInflater().inflate(
						R.layout.fragment_study_program_term, null);

				TextView title = (TextView) row.findViewById(R.id.title);
				title.setText(TermStudies[position].getHeader());
				RelativeLayout header = (RelativeLayout) row
						.findViewById(R.id.header);
				header.setBackgroundColor(activity.getResources().getColor(
						R.color.titleRegisterColor));
				LinearLayout list = (LinearLayout) row
						.findViewById(R.id.content);
				ResultDetailsAdapter ad = new ResultDetailsAdapter(
						TermStudies[position].StudyUnit);

				final int adapterCount = ad.getCount();
				list.removeAllViews();
				for (int i = 0; i < adapterCount; i++) {
					View item = ad.getView(i, null, null);
					list.addView(item);
				}
				return row;
			}

			public class ResultDetailsAdapter extends BaseAdapter {

				public ArrayList<RegisterScheduleStudyUnit> data;

				public ResultDetailsAdapter(
						ArrayList<RegisterScheduleStudyUnit> d) {
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
					View row = (activity.getLayoutInflater()).inflate(
							R.layout.row_study_program_term_details, null);
					TextView key = (TextView) row.findViewById(R.id.key);
					TextView value = (TextView) row.findViewById(R.id.value);

					key.setText(data.get(position).CurriculumName);
					value.setText(data.get(position).Credits + "");
					DisplayMetrics displaymetrics = new DisplayMetrics();
					activity.getWindowManager().getDefaultDisplay()
							.getMetrics(displaymetrics);
					int padding = (int) (displaymetrics.widthPixels * 0.2);
					key.setWidth(displaymetrics.widthPixels - padding
							- value.getWidth());
					return row;
				}
			}
		}
	}

	public static class ResultCurrentFragment extends BaseFragment {
		public static String currentTermID;
		public static String currentYearStudy;
		static ListView list, details;

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.registered_curriculum, menu);
			menu.removeItem(R.id.action_view_current);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_view_all:
				// back to view all result
				activity.onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity.getActionBar().setDisplayHomeAsUpEnabled(true);
			activity.getActionBar().setTitle(
					activity.getResources().getString(
							R.string.register_curriculum_result_current));
			PrivateMainActivity.menuToggle.setDrawerIndicatorEnabled(false);
			PrivateMainActivity.mainLayout
					.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			setHasOptionsMenu(true);

			View row = inflater.inflate(R.layout.fragment_registered_current,
					container, false);
			TextView studentName = (TextView) row
					.findViewById(R.id.studentName);
			studentName.setText(RegisterCurriculumFragment.studentName);
			TextView studentID = (TextView) row.findViewById(R.id.studentID);
			studentID.setText(RegisterCurriculumFragment.studentID);
			details = (ListView) row.findViewById(R.id.details);
			list = (ListView) row.findViewById(R.id.content);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, displaymetrics.widthPixels / 5);
			list.setLayoutParams(params);

			Connect connect = new Connect(activity);
			connect.LoadRegisterResultCurrent();
			return row;
		}

		public static void LoadFragment() {
			Log.d(currentTermID, currentYearStudy);
			final TermStudy current = getCurrentTermYear(
					ResultFragment.TermStudies, currentTermID, currentYearStudy);
			if (current == null) {
				Toast.makeText(activity,
						activity.getResources().getString(R.string.noti_null),
						Toast.LENGTH_SHORT).show();
				return;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
					R.layout.row_registered_current, R.id.curriName,
					getCurrisName(current));
			list.setAdapter(adapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View v,
						int position, long arg3) {
					v.setSelected(true);
					// get Details of that curriculum
					ArrayList<JSONType> data = new ArrayList<JSONType>();
					data.add(new JSONType("Tên học phần", current.StudyUnit
							.get(position).CurriculumName));
					data.add(new JSONType("Số tín chỉ", current.StudyUnit
							.get(position).Credits + ""));
					data.add(new JSONType("Ngày học", current.StudyUnit
							.get(position).Informations));
					data.add(new JSONType("Giảng viên", current.StudyUnit
							.get(position).ProfessorName));
					details.setAdapter(new RegisteredCurrentDetailsAdapter(data));
				}
			});
		}

		protected static TermStudy getCurrentTermYear(TermStudy[] list,
				String termID, String year) {
			for (int i = 0; i < list.length; i++)
				if (list[i].TermID.matches(termID)
						&& list[i].YearStudy.matches(year))
					return list[i];
			return null;
		}

		protected static ArrayList<String> getCurrisName(TermStudy target) {
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < target.StudyUnit.size(); i++)
				list.add(target.StudyUnit.get(i).CurriculumName);
			return list;
		}

		public static class RegisteredCurrentDetailsAdapter extends BaseAdapter {
			private ArrayList<JSONType> data;

			public RegisteredCurrentDetailsAdapter(ArrayList<JSONType> d) {
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
				View row = (activity.getLayoutInflater()).inflate(
						R.layout.row_study_program_term_details, null);
				TextView key = (TextView) row.findViewById(R.id.key);
				TextView value = (TextView) row.findViewById(R.id.value);

				key.setText(data.get(position).key);
				value.setText(data.get(position).value);

				DisplayMetrics displaymetrics = new DisplayMetrics();
				activity.getWindowManager().getDefaultDisplay()
						.getMetrics(displaymetrics);
				key.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				value.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

				int padding = (int) (displaymetrics.widthPixels * 0.2);
				value.setWidth(displaymetrics.widthPixels - padding
						- key.getMeasuredWidth());
				return row;
			}
		}
	}

	public static class DisaccumulatedFragment extends BaseFragment {
		public static ArrayList<Curriculum> curris;
		public static LinearLayout content;
		public static LinearLayout termLayout;
		public static View subView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity.getActionBar().setDisplayHomeAsUpEnabled(true);
			activity.getActionBar().setTitle(
					activity.getResources().getString(
							R.string.register_curriculum_disaccumulate));
			PrivateMainActivity.menuToggle.setDrawerIndicatorEnabled(false);
			PrivateMainActivity.mainLayout
					.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			setHasOptionsMenu(true);

			View row = inflater.inflate(R.layout.fragment_study_program,
					container, false);
			TextView studentID = (TextView) row.findViewById(R.id.studentID);
			TextView studentName = (TextView) row
					.findViewById(R.id.studentName);

			studentID.setText(RegisterCurriculumFragment.studentID);
			studentName.setText(RegisterCurriculumFragment.studentName);
			termLayout = (LinearLayout) row.findViewById(R.id.term_layout);
			curris = new ArrayList<Curriculum>();

			subView = inflater.inflate(R.layout.fragment_study_program_term,
					container, false);
			TextView title = (TextView) subView.findViewById(R.id.title);
			title.setText(activity.getResources().getString(
					R.string.register_curriculum_disaccumulate));
			RelativeLayout header = (RelativeLayout) subView
					.findViewById(R.id.header);
			header.setBackgroundColor(activity.getResources().getColor(
					R.color.titleDisaccColor));
			content = (LinearLayout) subView.findViewById(R.id.content);
			Connect connect = new Connect(activity);

			String stdID = RegisterCurriculumFragment.studentID;
			String studyProgram = stdID.substring(0, 3) + stdID.substring(4, 7);
			connect.LoadRegisterNotAccumulate(stdID, studyProgram);
			return row;
		}

		public static void LoadFragment() {
			DisaccumulatedAdapter adapter = new DisaccumulatedAdapter();
			final int adapterCount = adapter.getCount();
			content.removeAllViews();
			for (int i = 0; i < adapterCount; i++) {
				View item = adapter.getView(i, null, null);
				content.addView(item);
			}
			termLayout.addView(subView);
		}

		public static class DisaccumulatedAdapter extends BaseAdapter {

			public DisaccumulatedAdapter() {
			}

			@Override
			public int getCount() {
				return curris.size();
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
						R.layout.row_study_program_term_details, null);
				TextView key = (TextView) row.findViewById(R.id.key);
				TextView value = (TextView) row.findViewById(R.id.value);

				key.setText(curris.get(position).CurriculumName);
				value.setText(curris.get(position).Credit + "");
				DisplayMetrics displaymetrics = new DisplayMetrics();
				activity.getWindowManager().getDefaultDisplay()
						.getMetrics(displaymetrics);
				int padding = (int) (displaymetrics.widthPixels * 0.2);
				key.setWidth(displaymetrics.widthPixels - padding
						- value.getWidth());
				return row;
			}
		}
	}

	public static class RegisterActionFragment extends BaseFragment {

	}
}