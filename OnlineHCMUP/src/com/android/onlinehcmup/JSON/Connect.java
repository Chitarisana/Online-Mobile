package com.android.onlinehcmup.JSON;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.onlinehcmup.MainActivity;
import com.android.onlinehcmup.R;
import com.android.onlinehcmup.Support.SessionManager;

public class Connect {
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	public Connect(Activity _activity) {
		session = new SessionManager(_activity);
		activity = _activity;
	}

	SessionManager session;
	public static Activity activity = MainActivity.activity;
	public static final int TYPE_PUBLIC_NEWS = 0;
	public static final int TYPE_INIT = 1;
	public static final int TYPE_INIT_NOT_ACC_CURRI = 111;
	public static final int TYPE_INIT_SCORE_DETAILS = 112;
	public static final int TYPE_LOGIN = 2;
	public static final int TYPE_PRIVATE_NEWS = 3;
	public static final int TYPE_STUDENT_INFO = 4;
	public static final int TYPE_STUDENT_CHANGE_PASSWORD = 5;
	public static final int TYPE_LOAD_CONTACT = 6;
	public static final int TYPE_STUDENT_EDIT_INFO = 7;
	public static final int TYPE_STUDY_PROGRAM = 8;
	public static final int TYPE_REGISTER_RESULT_ALL = 9;
	public static final int TYPE_REGISTER_RESULT_CURRENT = 10;
	public static final int TYPE_REGISTER_NOT_ACCUMULATE = 11;
	public static final int TYPE_SCHEDULE_CALENDAR = 12;
	public static final int TYPE_SCHEDULE_EXAMINATION = 13;
	public static final int TYPE_SCORE = 14;
	public static final int TYPE_SCORE_DETAIL = 15;

	public static int TYPE = TYPE_PUBLIC_NEWS;

	private DownloadTask getJson(int type, String... urls) {
		TYPE = type;
		if (!isConnectingToInternet(activity)) {
			Toast.makeText(activity, R.string.error_connect, Toast.LENGTH_SHORT)
					.show();
			LocalTask.LoadDB(TYPE);
			return null;
		}
		DownloadTask task = new DownloadTask();
		task.execute(urls);
		return task;
	}

	public DownloadTask LoadPublicNews() {
		return getJson(TYPE_PUBLIC_NEWS, String.format(Link.PUBLIC_NEWS, 1, 0));
	}

	public DownloadTask Init(String studentID) { // init values here
		/* String.format(Link.PRIVATE_NEWS, studentID), // position */
		String[] links = new String[] {
				String.format(Link.STUDENT_INFO, studentID), // 0
				String.format(Link.STUDENT_COURSE, studentID), // 1
				String.format(Link.STUDENT_CONTACT, studentID), // 2
				String.format(Link.STUDY_PROGRAM, studentID), // 3
				String.format(Link.REGISTER_SCHEDULE, studentID), // 4
				Link.CURRENT_TERM, // 5
				// String.format(Link.REGISTER_NOT_ACCUMULATE, studentID,
				// studyProgramID),
				String.format(Link.SCHEDULE_CALENDAR, studentID), // 6
				String.format(Link.SCORE, studentID), // 7
				String.format(Link.SCORE_SUM, studentID), // 8
				String.format(Link.BEHAVIOR_SCORE, studentID) // 9
		};
		return getJson(TYPE_INIT, links);
	}

	public DownloadTask InitNotAccumulateCurriculum(String studentID,
			String studyProgramID) {
		Log.d("init","not acc curri");
		return getJson(TYPE_INIT_NOT_ACC_CURRI, String.format(
				Link.REGISTER_NOT_ACCUMULATE, studentID, studyProgramID));
	}

	public DownloadTask InitScoreDetails(String[] links) {
		return getJson(TYPE_INIT_SCORE_DETAILS, links);
	}

	public DownloadTask Login(String studentID, String password) {
		// password md5??
		return getJson(TYPE_LOGIN,
				String.format(Link.LOGIN, studentID, password));
	}

	public DownloadTask LoadPrivateNews(String studentID) {
		return getJson(TYPE_PRIVATE_NEWS,
				String.format(Link.PRIVATE_NEWS, studentID));
	}

	public DownloadTask LoadStudentInfo(String studentID, int typeInfo) {
		String[] link = new String[] { Link.STUDENT_INFO, Link.STUDENT_COURSE,
				Link.STUDENT_CONTACT };
		return getJson(TYPE_STUDENT_INFO,
				String.format(link[typeInfo], studentID));
	}

	public DownloadTask ChangePassword(String studentID, String oldPass,
			String newPass) {
		return getJson(TYPE_STUDENT_CHANGE_PASSWORD, String.format(
				Link.STUDENT_CHANGE_PASSWORD, studentID, oldPass, newPass));
	}

	public DownloadTask LoadContact(String studentID) {
		return getJson(TYPE_LOAD_CONTACT,
				String.format(Link.STUDENT_CONTACT, studentID));
	}

	public DownloadTask EditInfo(ArrayList<String> values) {
		String[] valuess = values.toArray(new String[values.size()]);
		return getJson(TYPE_STUDENT_EDIT_INFO,
				String.format(Link.STUDENT_EDIT_INFO, (Object[]) valuess));
	}

	public DownloadTask LoadStudyProgram(String studentID) {
		return getJson(TYPE_STUDY_PROGRAM,
				String.format(Link.STUDY_PROGRAM, studentID));
	}

	public DownloadTask LoadRegisterResultAll(String studentID) {
		return getJson(TYPE_REGISTER_RESULT_ALL,
				String.format(Link.REGISTER_SCHEDULE, studentID));
	}

	public DownloadTask LoadRegisterResultCurrent() {
		return getJson(TYPE_REGISTER_RESULT_CURRENT, Link.CURRENT_TERM);
	}

	public DownloadTask LoadRegisterNotAccumulate(String studentID,
			String studyProgram) {
		return getJson(TYPE_REGISTER_NOT_ACCUMULATE, String.format(
				Link.REGISTER_NOT_ACCUMULATE, studentID, studyProgram));
	}

	public DownloadTask LoadScheduleCalendar(String studentID) {
		return getJson(TYPE_SCHEDULE_CALENDAR,
				String.format(Link.SCHEDULE_CALENDAR, studentID));
	}

	public DownloadTask LoadScheduleExamination(String studentID,
			String yearStudy, String termID) {
		return getJson(TYPE_SCHEDULE_EXAMINATION, String.format(
				Link.SCHEDULE_EXAMINATION, studentID, yearStudy, termID));
	}

	public DownloadTask LoadScore(String studentID) {
		String[] links = new String[] { String.format(Link.SCORE, studentID), // 0
				String.format(Link.SCORE_SUM, studentID), // 1
				String.format(Link.BEHAVIOR_SCORE, studentID) // 2
		};
		return getJson(TYPE_SCORE, links);
	}

	public DownloadTask LoadScoreDetail(String studentID, String studyUnitID) {
		return getJson(TYPE_SCORE_DETAIL,
				String.format(Link.SCORE_DETAIL, studentID, studyUnitID));
	}
}
