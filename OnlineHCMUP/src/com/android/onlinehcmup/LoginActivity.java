package com.android.onlinehcmup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Support.DialogManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class LoginActivity extends Activity {
	EditText txtUsername, txtPassword;
	Button btnLogin;
	public static Activity activity;
	public static String StudentID, Password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity = this;
		StaticTAG.ACTIVITY = activity;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);

		// Toast.makeText(getApplicationContext(), "User Login Status: " +
		// session.isLoggedIn(), Toast.LENGTH_LONG).show();

		// Login button
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View arg0) {
				// Get username, password from EditText
				StudentID = txtUsername.getText().toString();
				Password = txtPassword.getText().toString();

				// Check if username, password is filled
				StudentID = StudentID.trim().toUpperCase();
				Password = Password.trim();
				if (StudentID.length() > 0 && Password.length() > 0) {
					Connect connect = new Connect(activity);
					connect.Login(StudentID, Password);
				} else {
					DialogManager.showAlertDialog(
							LoginActivity.this,
							getResources().getString(
									R.string.login_fail_noti_title),
							getResources().getString(
									R.string.login_fail_noti_detail2), false);
				}
				txtUsername.setText("");
				txtPassword.setText("");
				txtUsername.requestFocus();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
