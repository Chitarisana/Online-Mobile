package com.android.onlinehcmup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Support.SessionManager;

public class ChangePasswordFragment extends BaseFragment {

	public ChangePasswordFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final SessionManager session = new SessionManager(getActivity());
		session.checkLogin();
		setOnFragment(R.string.student_change_password_title);

		View view = inflater.inflate(R.layout.fragment_change_password,
				container, false);

		final EditText oldPass = (EditText) view.findViewById(R.id.txtOld);
		final EditText newPass = (EditText) view.findViewById(R.id.txtNew);
		final EditText reNewPass = (EditText) view.findViewById(R.id.txtReNew);

		Button btnOK = (Button) view.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isEmpty(oldPass) || isEmpty(newPass) || isEmpty(reNewPass)) {
					Toast.makeText(getActivity(),
							R.string.edit_password_noti_null,
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!newPass.getText().toString().trim()
						.matches(reNewPass.getText().toString().trim())) {
					Toast.makeText(getActivity(),
							R.string.edit_password_noti_dismatch,
							Toast.LENGTH_SHORT).show();
					return;
				}

				String studentID = session.getStudentID();
				Connect connect = new Connect(getActivity());
				PrivateMainActivity.currentTask = connect.ChangePassword(
						studentID, getText(oldPass), getText(newPass));
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

	private boolean isEmpty(EditText editText) {
		return getText(editText).length() == 0;
	}

	private String getText(EditText editText) {
		return editText.getText().toString().trim();
	}
}
