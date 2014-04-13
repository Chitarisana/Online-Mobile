package com.android.onlinehcmup.JSON;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.onlinehcmup.LoginActivity;
import com.android.onlinehcmup.MainActivity;
import com.android.onlinehcmup.PrivateMainActivity;
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

	public Connect(Context _context) {
		context = _context;
		session = new SessionManager(_context);
	}

	SessionManager session;
	public static Activity activity;
	Context context;
	public static final int TYPE_INIT = -1;
	public static final int TYPE_LOGIN = 0;
	public static final int TYPE_PRIVATE_NEWS = 1;
	public static final int TYPE_STUDENT_INFO = 2;
	public static final int TYPE_STUDENT_CHANGE_PASSWORD = 3;
	public static final int TYPE_LOAD_CONTACT = 4;
	public static final int TYPE_STUDENT_EDIT_INFO = 5;
	public static final int TYPE_STUDY_PROGRAM = 6;
	public static final int TYPE_REGISTER_RESULT_ALL = 7;
	public static final int TYPE_REGISTER_RESULT_CURRENT = 8;
	public static final int TYPE_REGISTER_NOT_ACCUMULATE = 9;
	public static final int TYPE_PUBLIC_NEWS = 15;

	public static int TYPE = TYPE_LOGIN;

	private void getJson(String url, int type) {
		if (!isConnectingToInternet(context)) {
			String error = activity.getResources().getString(
					R.string.error_connect);
			session.setError(error);
			session.setConnectStatus(false);
			Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
			// use local db here
			return;
		}
		TYPE = type;
		new DownloadTask().execute(url);
	}

	public void Init(String studentID) {
		activity = PrivateMainActivity.activity;
		getJson(String.format(Link.STUDENT_INFO, studentID), TYPE_INIT);
	}

	public void Login(String studentID, String password) {
		// password md5??
		activity = LoginActivity.activity;
		getJson(String.format(Link.LOGIN, studentID, password), TYPE_LOGIN);
	}

	public void LoadPrivateNews(String studentID) {
		activity = PrivateMainActivity.activity;
		getJson(String.format(Link.PRIVATE_NEWS, studentID), TYPE_PRIVATE_NEWS);
	}

	public void LoadStudentInfo(String studentID, int typeInfo) {
		activity = PrivateMainActivity.activity;
		String[] link = new String[] { Link.STUDENT_INFO, Link.STUDENT_COURSE,
				Link.STUDENT_CONTACT };
		getJson(String.format(link[typeInfo], studentID), TYPE_STUDENT_INFO);
	}

	public void ChangePassword(String studentID, String oldPass, String newPass) {
		activity = PrivateMainActivity.activity;
		getJson(String.format(Link.STUDENT_CHANGE_PASSWORD, studentID, oldPass,
				newPass), TYPE_STUDENT_CHANGE_PASSWORD);
	}

	public void LoadContact(String studentID) {
		activity = PrivateMainActivity.activity;
		getJson(String.format(Link.STUDENT_CONTACT, studentID),
				TYPE_LOAD_CONTACT);
	}

	public void EditInfo(ArrayList<String> values) {
		activity = PrivateMainActivity.activity;
		String[] valuess = values.toArray(new String[values.size()]);
		getJson(String.format(Link.STUDENT_EDIT_INFO, (Object[]) valuess),
				TYPE_STUDENT_EDIT_INFO);
	}

	public void LoadStudyProgram(String studentID) {
		activity = PrivateMainActivity.activity;
		getJson(String.format(Link.STUDY_PROGRAM, studentID),
				TYPE_STUDY_PROGRAM);
	}

	public void LoadRegisterResultAll(String studentID) {
		activity = PrivateMainActivity.activity;
		getJson(String.format(Link.REGISTER_SCHEDULE, studentID),
				TYPE_REGISTER_RESULT_ALL);
	}

	public void LoadRegisterResultCurrent() {
		activity = PrivateMainActivity.activity;
		getJson(Link.CURRENT_TERM, TYPE_REGISTER_RESULT_CURRENT);
	}

	public void LoadRegisterNotAccumulate(String studentID, String studyProgram) {
		activity = PrivateMainActivity.activity;
		getJson(String.format(Link.REGISTER_NOT_ACCUMULATE, studentID,
				studyProgram), TYPE_REGISTER_NOT_ACCUMULATE);
	}
	
	
	public void LoadPublicNews(Integer groupCateID, Integer messageCateID) {
		activity = MainActivity.activity;
		getJson(String.format(Link.PUBLIC_NEWS, groupCateID, messageCateID), TYPE_PUBLIC_NEWS);
	}
}
