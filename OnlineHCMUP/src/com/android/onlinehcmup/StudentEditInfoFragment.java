package com.android.onlinehcmup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.onlinehcmup.Adapter.StudentEditInfoAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.JSON.Key;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class StudentEditInfoFragment extends BaseFragment {
	public static SessionManager session;
	public static String[][] keys, valuesLoad;
	public static LinearLayout[] contents;
	public static Activity activity;

	public StudentEditInfoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		session = new SessionManager(activity.getApplicationContext());
		session.checkLogin();
		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		activity.getActionBar().setTitle(
				activity.getResources().getString(
						R.string.student_edit_info_title));
		PrivateMainActivity.menuToggle.setDrawerIndicatorEnabled(false);
		PrivateMainActivity.mainLayout
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		setHasOptionsMenu(true);

		View view = inflater.inflate(R.layout.fragment_student_edit_info,
				container, false);
		contents = new LinearLayout[] {
				(LinearLayout) view.findViewById(R.id.content1),
				(LinearLayout) view.findViewById(R.id.content2),
				(LinearLayout) view.findViewById(R.id.content3) };

		keys = new String[][] { Key.KEY_STUDENT_LOAD_EDITCONTACT_1_VI,
				Key.KEY_STUDENT_LOAD_EDITCONTACT_2_VI,
				Key.KEY_STUDENT_LOAD_EDITCONTACT_3_VI };
		valuesLoad = new String[keys.length][];

		session = new SessionManager(StaticTAG.ACTIVITY);
		HashMap<String, String> user = session.getUserDetails();
		final String studentID = user.get(SessionManager.KEY_STUDENTID);
		final Connect connect = new Connect(activity);
		Button btnOK = (Button) view.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ArrayList<String> values = new ArrayList<String>();
				values.add(studentID);
				for (int i = 0; i < keys.length; i++) {
					String[] value = getValues(contents[i]);
					for (int j = 0; j < value.length; j++) {
						values.add(value[j]);
					}
				}
				connect.EditInfo(values);
				activity.onBackPressed();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.onBackPressed();
			}
		});

		connect.LoadContact(studentID);
		return view;
	}

	private String[] getValues(LinearLayout list) {
		int num = list.getChildCount();
		String[] values = new String[num];
		for (int i = 0; i < num; i++) {
			View view = list.getChildAt(i);
			EditText value = (EditText) view.findViewById(R.id.value);
			try {
				values[i] = URLEncoder.encode(value.getText().toString(),
						"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				values[i] = "";
			}
		}
		return values;
	}

	public static void LoadFragment() {
		for (int i = 0; i < contents.length; i++) {
			ArrayList<JSONType> listAdapter = new ArrayList<JSONType>();
			for (int j = 0; j < keys[i].length; j++) {
				listAdapter.add(new JSONType(keys[i][j], valuesLoad[i][j]));
			}

			StudentEditInfoAdapter adapter = new StudentEditInfoAdapter(
					activity, listAdapter);
			contents[i].removeAllViews();
			for (int j = 0; j < adapter.getCount(); j++) {
				View item = adapter.getView(j, null, null);
				contents[i].addView(item);
			}
		}
	}
}
