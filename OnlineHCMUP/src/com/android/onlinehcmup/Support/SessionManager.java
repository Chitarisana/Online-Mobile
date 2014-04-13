package com.android.onlinehcmup.Support;

import java.util.HashMap;

import com.android.onlinehcmup.LoginActivity;
import com.android.onlinehcmup.PrivateMainActivity;
import com.android.onlinehcmup.StudentInfoFragment;
import com.android.onlinehcmup.JSON.Connect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "Online HCMUP";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	public static final String KEY_STUDENTID = "StudentID";

	public static final String KEY_PASSWORD = "Password";

	public static final String KEY_STUDENT_NAME = "StudentName";

	public static final String KEY_ERROR = "Errors";

	public static final String IS_CONNECTED = "IsConnected";

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

		editor.putString(KEY_STUDENTID, studentID);

		editor.putString(KEY_PASSWORD, password);

		// commit changes
		editor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();

		user.put(KEY_STUDENTID, pref.getString(KEY_STUDENTID, null));

		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

		user.put(KEY_STUDENT_NAME, pref.getString(KEY_STUDENT_NAME, ""));
		// return user
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// reset default values
		PrivateMainActivity.title = null;
		StudentInfoFragment.TYPE = StudentInfoFragment.KEY_TYPE_INFO;
		Connect.TYPE = Connect.TYPE_LOGIN;

		editor.clear();
		editor.commit();

		// After logout redirect user to Logging Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
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
		editor.putBoolean(IS_LOGIN, true);
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
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_STUDENT_NAME, studentName);
		editor.commit();
	}
}