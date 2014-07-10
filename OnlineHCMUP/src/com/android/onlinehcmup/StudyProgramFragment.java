package com.android.onlinehcmup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.onlinehcmup.Adapter.KeyValueAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Model.Semester;
import com.android.onlinehcmup.Model.StudyProgram;
import com.android.onlinehcmup.Support.SessionManager;

public class StudyProgramFragment extends BaseFragment {

	static Activity activity;
	public static LinearLayout termLayout;
	public static ArrayList<Semester> Semester;
	SessionManager session;

	public StudyProgramFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		session = new SessionManager(activity);
		session.checkLogin();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(R.string.menu_2);

		View row = inflater.inflate(R.layout.fragment_study_program, container,
				false);
		TextView studentID = (TextView) row.findViewById(R.id.studentID);
		TextView studentName = (TextView) row.findViewById(R.id.studentName);

		termLayout = (LinearLayout) row.findViewById(R.id.term_layout);

		HashMap<String, String> user = session.getUserDetails();
		String stdID = user.get(SessionManager.KEY_STUDENT_ID);
		String stdName = user.get(SessionManager.KEY_STUDENT_NAME);
		studentID.setText(stdID);
		studentName.setText(stdName);

		Connect connect = new Connect(activity);
		PrivateMainActivity.currentTask = connect.LoadStudyProgram(stdID);
		return row;
	}

	public static void LoadFragment() {
		StudyProgramTermAdapter adapter = new StudyProgramTermAdapter();
		final int adapterCount = adapter.getCount();
		termLayout.removeAllViews();
		for (int i = 0; i < adapterCount; i++) {
			View item = adapter.getView(i, null, null);
			termLayout.addView(item);
		}
	}

	public static class StudyProgramTermAdapter extends BaseAdapter {

		public StudyProgramTermAdapter() {
		}

		@Override
		public int getCount() {
			return Semester.size();
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
			title.setText(Semester.get(position).getHeader());
			LinearLayout list = (LinearLayout) row.findViewById(R.id.content);

			ArrayList<StudyProgram> studyPrograms = Semester.get(position).StudyProgram;
			ArrayList<JSONType> data = new ArrayList<JSONType>();
			for (int i = 0; i < studyPrograms.size(); i++) {
				data.add(new JSONType(studyPrograms.get(i).CurriculumName,
						studyPrograms.get(i).Credits + ""));
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