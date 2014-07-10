package com.android.onlinehcmup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlinehcmup.Adapter.KeyValueAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Model.Curriculum;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Model.RegisteredStudyUnit;
import com.android.onlinehcmup.Model.TermStudy;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class RegisterCurriculumFragment extends BaseFragment {
	static Activity activity;
	SessionManager session;
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
		session = new SessionManager(activity);
		session.checkLogin();
		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(R.string.menu_3);

		View row = inflater.inflate(R.layout.fragment_register_curriculum,
				container, false);

		HashMap<String, String> user = session.getUserDetails();
		studentID = user.get(SessionManager.KEY_STUDENT_ID);
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
				Fragment frag = new NotAccumulatedFragment();
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
		public static ArrayList<TermStudy> TermStudies;

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
			setOnFragment(R.string.register_curriculum_result_registered);

			View row = inflater.inflate(R.layout.fragment_study_program,
					container, false);
			TextView stdID = (TextView) row.findViewById(R.id.studentID);
			TextView stdName = (TextView) row.findViewById(R.id.studentName);

			// test
			if (studentID == null) {
				Toast.makeText(activity, "studentID null", Toast.LENGTH_SHORT)
						.show();
			}
			stdID.setText(studentID);
			stdName.setText(studentName);
			termLayout = (LinearLayout) row.findViewById(R.id.term_layout);

			Connect connect = new Connect(activity);
			PrivateMainActivity.currentTask = connect
					.LoadRegisterResultAll(studentID);

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
				return TermStudies.size();
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
				title.setText(TermStudies.get(position).getHeader());
				RelativeLayout header = (RelativeLayout) row
						.findViewById(R.id.header);
				header.setBackgroundColor(activity.getResources().getColor(
						R.color.titleRegisterColor));
				LinearLayout list = (LinearLayout) row
						.findViewById(R.id.content);
				ArrayList<JSONType> data = new ArrayList<JSONType>();
				ArrayList<RegisteredStudyUnit> studyUnits = TermStudies
						.get(position).StudyUnit;
				for (int i = 0; i < studyUnits.size(); i++) {
					data.add(new JSONType(studyUnits.get(i).CurriculumName,
							studyUnits.get(i).Credits + ""));
				}
				KeyValueAdapter ad = new KeyValueAdapter(activity, data,
						R.layout.row_study_program_term_details, false);

				final int adapterCount = ad.getCount();
				list.removeAllViews();
				for (int i = 0; i < adapterCount; i++) {
					View item = ad.getView(i, null, null);
					list.addView(item);
				}
				return row;
			}
		}
	}

	public static class ResultCurrentFragment extends BaseFragment {
		public static TermStudy current;
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
			setOnFragment(R.string.register_curriculum_result_current);

			View row = inflater.inflate(R.layout.fragment_registered_current,
					container, false);
			// test
			if (studentName == null)
				Toast.makeText(activity, "student name null",
						Toast.LENGTH_SHORT).show();
			TextView stdName = (TextView) row.findViewById(R.id.studentName);
			stdName.setText(studentName);
			TextView stdID = (TextView) row.findViewById(R.id.studentID);
			stdID.setText(studentID);

			details = (ListView) row.findViewById(R.id.details);
			list = (ListView) row.findViewById(R.id.content);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					(int) (displaymetrics.heightPixels / 5));

			list.setLayoutParams(params);

			Connect connect = new Connect(activity);
			PrivateMainActivity.currentTask = connect
					.LoadRegisterResultCurrent();
			return row;
		}

		public static void LoadFragment() {
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
					KeyValueAdapter adapter = new KeyValueAdapter(activity,
							data, R.layout.row_study_program_term_details, null);
					details.setAdapter(adapter);
				}
			});
		}

		protected static ArrayList<String> getCurrisName(TermStudy target) {
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < target.StudyUnit.size(); i++)
				list.add(target.StudyUnit.get(i).CurriculumName);
			return list;
		}
	}

	public static class NotAccumulatedFragment extends BaseFragment {
		public static ArrayList<Curriculum> curris;
		public static LinearLayout content, termLayout;
		static View subView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			setOnFragment(R.string.register_curriculum_disaccumulate);

			View row = inflater.inflate(R.layout.fragment_study_program,
					container, false);
			TextView stdentID = (TextView) row.findViewById(R.id.studentID);
			TextView stdentName = (TextView) row.findViewById(R.id.studentName);

			stdentID.setText(studentID);
			stdentName.setText(studentName);

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
			SessionManager session = new SessionManager(activity);
			Connect connect = new Connect(activity);
			PrivateMainActivity.currentTask = connect
					.LoadRegisterNotAccumulate(studentID,
							session.getStudyProgram());
			return row;
		}

		public static void LoadFragment() {
			if (curris == null)
				return;
			ArrayList<JSONType> data = new ArrayList<JSONType>();
			for (int i = 0; i < curris.size(); i++) {
				data.add(new JSONType(curris.get(i).CurriculumName, curris
						.get(i).Credits + ""));
			}
			KeyValueAdapter adapter = new KeyValueAdapter(activity, data,
					R.layout.row_study_program_term_details, false);
			final int adapterCount = adapter.getCount();
			content.removeAllViews();
			for (int i = 0; i < adapterCount; i++) {
				View item = adapter.getView(i, null, null);
				content.addView(item);
			}
			termLayout.addView(subView);
		}
	}

	public static class RegisterActionFragment extends BaseFragment {

	}
}