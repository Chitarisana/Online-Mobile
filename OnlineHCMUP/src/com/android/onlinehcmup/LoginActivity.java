package com.android.onlinehcmup;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Support.DialogManager;

public class LoginActivity extends Activity {
	public static Activity activity;
	public static String StudentID, Password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		activity = this;
		final EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
		final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);

		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				StudentID = txtUsername.getText().toString();
				Password = txtPassword.getText().toString();

				StudentID = StudentID.trim().toUpperCase(Locale.US);
				Password = Password.trim();
				if (StudentID.length() > 0 && Password.length() > 0) {
					Connect connect = new Connect(activity);
					connect.Login(StudentID, Password);
				} else {
					DialogManager.showAlertDialog(LoginActivity.this,
							R.string.login_fail_noti_title,
							R.string.login_fail_noti_detail2, false);					
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
