package com.android.onlinehcmup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.onlinehcmup.Adapter.KeyValueAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.JSON.Key;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Support.SessionManager;

public class StudentEditInfoFragment extends BaseFragment {
	public static SessionManager session;
	public static String[][] keys, values;
	public static LinearLayout[] contents;
	public static Activity activity;

	public StudentEditInfoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		session = new SessionManager(activity);
		session.checkLogin();
		setOnFragment(R.string.student_edit_info_title);

		View view = inflater.inflate(R.layout.fragment_student_edit_info,
				container, false);
		contents = new LinearLayout[] {
				(LinearLayout) view.findViewById(R.id.content1),
				(LinearLayout) view.findViewById(R.id.content2),
				(LinearLayout) view.findViewById(R.id.content3) };

		keys = new String[][] { Key.KEY_STUDENT_LOAD_EDITCONTACT_1_VI,
				Key.KEY_STUDENT_LOAD_EDITCONTACT_2_VI,
				Key.KEY_STUDENT_LOAD_EDITCONTACT_3_VI };
		values = new String[keys.length][];

		final String studentID = session.getStudentID();
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
				if (values.size() > 1) {
					PrivateMainActivity.currentTask = connect.EditInfo(values);
				} else {
					Toast.makeText(activity, R.string.error_connect,
							Toast.LENGTH_SHORT).show();
					getActivity().onBackPressed();
				}
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.onBackPressed();
			}
		});

		PrivateMainActivity.currentTask = connect.LoadContact(studentID);
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
				listAdapter.add(new JSONType(keys[i][j], values[i][j]));
			}

			KeyValueAdapter adapter = new KeyValueAdapter(activity,
					listAdapter, R.layout.row_student_edit_info, true);

			contents[i].removeAllViews();
			for (int j = 0; j < adapter.getCount(); j++) {
				View item = adapter.getView(j, null, null);
				contents[i].addView(item);
			}
		}
	}
}
