package com.android.onlinehcmup.Support;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.onlinehcmup.LoginActivity;
import com.android.onlinehcmup.PrivateMainActivity;
import com.android.onlinehcmup.StudentInfoFragment;
import com.android.onlinehcmup.Adapter.MenuAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.SQLite.DatabaseHandler;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared pref file name
	private static final String PREF_NAME = "Online HCMUP";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	public static final String KEY_STUDENT_ID = "StudentID";

	public static final String KEY_PASSWORD = "Password";

	public static final String KEY_STUDENT_NAME = "StudentName";

	public static final String KEY_STUDY_PROGRAM = "StudyProgramID";

	public static final String KEY_SEMESTER_ID = "SemesterID";

	public static final String KEY_SEMESTER_NAME = "SemesterName";

	public static final String KEY_TERM_ID = "TermID";

	public static final String KEY_YEAR_STUDY = "YearStudy";

	public static final String KEY_TERM_SUM = "TermSum";

	public static final String KEY_ERROR = "Errors";

	public static final String IS_CONNECTED = "IsConnected";

	public static final String KEY_SELECTED_POSITION = "SelectedPosition";

	public static final String KEY_MARK4 = "Mark4";

	public static final String KEY_POSITION_SCORE_DETAIL = "ScoreDetail";

	// Constructor
	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String studentID, String password) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		editor.putString(KEY_STUDENT_ID, studentID);

		editor.putString(KEY_PASSWORD, password);

		editor.commit();
	}

	/**
	 * Check login method will check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();

		user.put(KEY_STUDENT_ID, pref.getString(KEY_STUDENT_ID, null));

		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

		user.put(KEY_STUDENT_NAME, pref.getString(KEY_STUDENT_NAME, ""));

		return user;
	}

	public String getStudentID() {
		return pref.getString(KEY_STUDENT_ID, null);
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser(Class<?> cls) {
		// reset default values
		PrivateMainActivity.title = null;
		PrivateMainActivity.currentTask = null;
		MenuAdapter.menuLV.setItemChecked(0, true);
		StudentInfoFragment.TYPE = StudentInfoFragment.TYPE_INFO;
		Connect.TYPE = Connect.TYPE_PUBLIC_NEWS;
		DatabaseHandler.DeleteAll();

		editor.clear();
		editor.commit();

		Intent i = new Intent(_context, cls);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}

	public HashMap<String, String> getError() {
		HashMap<String, String> error = new HashMap<String, String>();
		error.put(KEY_ERROR, pref.getString(KEY_ERROR, null));
		return error;
	}

	public void setError(String error) {
		// editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_ERROR, error);
		editor.commit();
	}

	public boolean isConnected() {
		return pref.getBoolean(IS_CONNECTED, false);
	}

	public void setConnectStatus(boolean value) {
		editor.putBoolean(IS_CONNECTED, value);
		editor.commit();
	}

	public void setStudentInfo(String studentName) { // and many other...
		// editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_STUDENT_NAME, studentName);
		editor.commit();
	}

	public void setCurrentTermYear(String yearStudy, String termID) {
		editor.putString(KEY_TERM_ID, termID);
		editor.putString(KEY_YEAR_STUDY, yearStudy);
		editor.commit();
	}

	public HashMap<String, String> getCurrentTermYear() {
		HashMap<String, String> term = new HashMap<String, String>();
		term.put(KEY_TERM_ID, pref.getString(KEY_TERM_ID, null));
		term.put(KEY_YEAR_STUDY, pref.getString(KEY_YEAR_STUDY, null));
		return term;
	}

	public void setStudyProgram(String studyProgramID) {
		editor.putString(KEY_STUDY_PROGRAM, studyProgramID);
		editor.commit();
	}

	public String getStudyProgram() {
		return pref.getString(KEY_STUDY_PROGRAM, null);
	}

	public void setSelectedPosition(int position) {
		editor.putInt(KEY_SELECTED_POSITION, position);
		editor.commit();
	}

	public int getSelectedPosition() {
		return pref.getInt(KEY_SELECTED_POSITION, 0);
	}

	public void setMark4(Boolean isChecked) {
		editor.putBoolean(KEY_MARK4, isChecked);
		editor.commit();
	}

	public Boolean getMark4() {
		return pref.getBoolean(KEY_MARK4, false);
	}

	public void setSelectedScoreDetail(int position) {
		editor.putInt(KEY_POSITION_SCORE_DETAIL, position);
		editor.commit();
	}

	public int getSelectedScoreDetails() {
		return pref.getInt(KEY_POSITION_SCORE_DETAIL, 0);
	}
}