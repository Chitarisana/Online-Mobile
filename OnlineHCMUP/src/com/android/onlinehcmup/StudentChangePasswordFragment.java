package com.android.onlinehcmup;

import java.util.HashMap;

import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StudentChangePasswordFragment extends BaseFragment {
	public SessionManager session;
	public static EditText oldPass;
	public static EditText newPass;
	public static EditText reNewPass;

	public StudentChangePasswordFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		session = new SessionManager(getActivity().getApplicationContext());
		session.checkLogin();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setTitle(
				getActivity().getResources().getString(
						R.string.student_change_password_title));
		PrivateMainActivity.menuToggle.setDrawerIndicatorEnabled(false);
		PrivateMainActivity.mainLayout
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_change_password,
				container, false);

		oldPass = (EditText) view.findViewById(R.id.txtOld);
		newPass = (EditText) view.findViewById(R.id.txtNew);
		reNewPass = (EditText) view.findViewById(R.id.txtReNew);
		Button btnOK = (Button) view.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if disconnect ==> fail, return
				if (isEmpty(oldPass) || isEmpty(newPass) || isEmpty(reNewPass)) {
					Toast.makeText(getActivity(),
							R.string.edit_password_noti_null,
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!newPass.getText().toString().trim()
						.matches(reNewPass.getText().toString().trim())) {
					Toast.makeText(
							getActivity(),
							getActivity().getResources().getString(
									R.string.edit_password_noti_dismatch),
							Toast.LENGTH_SHORT).show();
					return;
				}
				session = new SessionManager(StaticTAG.ACTIVITY);
				HashMap<String, String> user = session.getUserDetails();
				String studentID = user.get(SessionManager.KEY_STUDENTID);
				Connect connect = new Connect(getActivity());				
				connect.ChangePassword(studentID, oldPass.getText().toString().trim(), newPass.getText().toString().trim());				
				getActivity().onBackPressed();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().onBackPressed();
			}
		});
		return view;
	}

	private boolean isEmpty(EditText etText) {
		return etText.getText().toString().trim().length() == 0;
	}
}
