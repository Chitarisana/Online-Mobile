package com.android.onlinehcmup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Model.Semester;
import com.android.onlinehcmup.Model.StudyProgram;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class StudyProgramFragment extends BaseFragment {

	static Activity activity;
	public static LinearLayout termLayout;
	public static Semester[] Semester;
	SessionManager session;

	public StudyProgramFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setRetainInstance(true);
		activity = this.getActivity();
	}

	// load all data from JSON
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		session = new SessionManager(getActivity().getApplicationContext());
		session.checkLogin();

		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setTitle(
				getActivity().getResources().getString(R.string.menu_2));

		View row = inflater.inflate(R.layout.fragment_study_program, container,
				false);
		TextView studentID = (TextView) row.findViewById(R.id.studentID);
		TextView studentName = (TextView) row.findViewById(R.id.studentName);

		termLayout = (LinearLayout) row
				.findViewById(R.id.term_layout);		

		session = new SessionManager(StaticTAG.ACTIVITY);
		HashMap<String, String> user = session.getUserDetails();
		String stdID = user.get(SessionManager.KEY_STUDENTID);
		String stdName = user.get(SessionManager.KEY_STUDENT_NAME);
		studentID.setText(stdID);
		studentName.setText(stdName);
		Connect connect = new Connect(activity);
		connect.LoadStudyProgram(stdID);
		return row;
	}
	
	public static void LoadFragment(){
		Log.d("length semester",Semester.length+"");
		for(int i=0;i<Semester.length;i++){
			Log.d(i+"",Semester[i].SemesterName);
		}
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
			return Semester.length;
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
			title.setText(Semester[position].SemesterName);
			LinearLayout list = (LinearLayout) row.findViewById(R.id.content);
			
			StudyProgramTermDetailsAdapter ad = new StudyProgramTermDetailsAdapter(
					Semester[position].StudyProgram);

			final int adapterCount = ad.getCount();
			list.removeAllViews();
			for (int i = 0; i < adapterCount; i++) {
				View item = ad.getView(i, null, null);
				list.addView(item);
			}
			return row;
		}

		public class StudyProgramTermDetailsAdapter extends BaseAdapter {

			public ArrayList<StudyProgram> studyPrograms;

			public StudyProgramTermDetailsAdapter(ArrayList<StudyProgram> d) {
				studyPrograms = d;
			}

			@Override
			public int getCount() {
				return studyPrograms.size();
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

				key.setText(studyPrograms.get(position).CurriculumName);
				value.setText(studyPrograms.get(position).Credit + "");
				
				DisplayMetrics displaymetrics = new DisplayMetrics();
				activity.getWindowManager().getDefaultDisplay()
						.getMetrics(displaymetrics);
				int padding = (int) (displaymetrics.widthPixels*0.2);
				key.setWidth(displaymetrics.widthPixels - padding
						- value.getWidth());
				return row;
			}
		}
	}
}