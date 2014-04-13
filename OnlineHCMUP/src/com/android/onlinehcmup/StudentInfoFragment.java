package com.android.onlinehcmup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.onlinehcmup.Adapter.StudentInfoAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.JSON.Key;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class StudentInfoFragment extends BaseFragment {

	public static final String KEY_TYPE = "type";
	public static final int KEY_TYPE_INFO = 0, KEY_TYPE_COURSE = 1,
			KEY_TYPE_CONTACT = 2;
	public static int TYPE = KEY_TYPE_INFO;
	private Bundle extras;
	private static View rootView;
	public static String[] keys, values, key1, value1;
	public static Activity activity;
	public static ImageView image;
	private static ArrayList<JSONType> listAdapter, listAdapter1;
	private static DisplayMetrics displaymetrics;
	private static int[] color = new int[] { R.color.btnInfoColor,
			R.color.btnCourseColor, R.color.btnContactColor };
	private int[] actionBarTitle = new int[] { R.string.menu_1,
			R.string.menu_1, R.string.key_contact_title };
	private static int[] btnNav = new int[] { R.string.key_info_title,
			R.string.key_course_title, R.string.key_contact_title };
	private static int[] listTitle = new int[] { R.string.key_info_title,
			R.string.key_course_title, R.string.key_contact_1_title,
			R.string.key_contact_2_title };
	private int[] layout = new int[] { R.layout.fragment_student_info,
			R.layout.fragment_student_info, R.layout.fragment_student_contact };
	private static String[] tag = new String[] { StaticTAG.TAG_STUDENT_INFO,
			StaticTAG.TAG_STUDENT_COURSE, StaticTAG.TAG_STUDENT_CONTACT };
	private static String[][] keyType = new String[][] {
			Key.KEY_STUDENT_INFO_VI, Key.KEY_STUDENT_COURSE_VI,
			Key.KEY_STUDENT_CONTACT_1_VI, Key.KEY_STUDENT_CONTACT_2_VI };
	public static SessionManager session;

	public StudentInfoFragment() {
	}

	// load all data from JSON
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		session = new SessionManager(getActivity().getApplicationContext());
		session.checkLogin();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		activity = getActivity();
		extras = getArguments();
		if (extras != null) {
			TYPE = extras.getInt(KEY_TYPE);
		}
		getActivity().getActionBar().setTitle(
				getActivity().getResources().getString(actionBarTitle[TYPE]));
		rootView = inflater.inflate(layout[TYPE], container, false);
		displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		image = (ImageView) rootView.findViewById(R.id.profile);
		double newHeight = displaymetrics.heightPixels / 7;
		image.getLayoutParams().height = (int) newHeight;
		image.getLayoutParams().width = (int) newHeight;

		RelativeLayout layoutbtn = (RelativeLayout) rootView
				.findViewById(R.id.btn);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layoutbtn
				.getLayoutParams();
		lp.height = displaymetrics.heightPixels / 6;
		layoutbtn.setLayoutParams(lp);

		Button btn1 = (Button) rootView.findViewById(R.id.btnInfo1);
		Button btn2 = (Button) rootView.findViewById(R.id.btnInfo2);

		btn1.setText(activity.getResources().getString(btnNav[(TYPE + 1) % 3]));
		btn2.setText(activity.getResources().getString(btnNav[(TYPE + 2) % 3]));

		btn1.setBackgroundColor(activity.getResources().getColor(
				color[(TYPE + 1) % 3]));
		btn2.setBackgroundColor(activity.getResources().getColor(
				color[(TYPE + 2) % 3]));

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				openFragment((TYPE + 1) % 3);
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				openFragment((TYPE + 2) % 3);
			}
		});
		TextView title = (TextView) rootView.findViewById(R.id.title);
		title.setText(activity.getResources().getString(listTitle[TYPE]));
		title.setBackgroundColor(activity.getResources().getColor(color[TYPE]));
		TextView btnChangePassword = (TextView) rootView
				.findViewById(R.id.btnChangePass);

		btnChangePassword.measure(MeasureSpec.UNSPECIFIED,
				MeasureSpec.UNSPECIFIED);
		float scale = (float) (1.0 * displaymetrics.heightPixels / 16)
				/ btnChangePassword.getMeasuredHeight();
		if (scale < 1)
			btnChangePassword.setScaleY(scale);
		btnChangePassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.getFragmentManager()
						.beginTransaction()
						.replace(R.id.content,
								new StudentChangePasswordFragment(),
								StaticTAG.TAG_STUDENT_CHANGE_PASSWORD)
						.addToBackStack(null).commit();
			}
		});
		if (rootView.findViewById(R.id.btnEdit) != null) {
			TextView btnEdit = (TextView) rootView.findViewById(R.id.btnEdit);
			btnEdit.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			scale = (float) (1.0 * displaymetrics.heightPixels / 16)
					/ btnEdit.getMeasuredHeight();
			if (scale < 1)
				btnEdit.setScaleY(scale);
			btnEdit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					activity.getFragmentManager()
							.beginTransaction()
							.replace(R.id.content,
									new StudentEditInfoFragment(),
									StaticTAG.TAG_STUDENT_EDIT_INFO)
							.addToBackStack(null).commit();
				}
			});
		}
		keys = keyType[TYPE];
		values = new String[keys.length];
		key1 = keyType[keyType.length - 1];
		value1 = new String[key1.length];

		session = new SessionManager(StaticTAG.ACTIVITY);
		HashMap<String, String> user = session.getUserDetails();
		String studentID = user.get(SessionManager.KEY_STUDENTID);
		Connect connect = new Connect(activity);
		connect.LoadStudentInfo(studentID, TYPE);

		return rootView;
	}

	private static void openFragment(int btnType) {
		Fragment fragment = new StudentInfoFragment();
		Bundle args = new Bundle();
		args.putInt(StudentInfoFragment.KEY_TYPE, btnType);
		fragment.setArguments(args);
		FragmentManager fragmentManager = activity.getFragmentManager();
		if (fragmentManager.getBackStackEntryCount() > 0)
			fragmentManager.popBackStack();
		fragmentManager.beginTransaction()
				.replace(R.id.content, fragment, tag[btnType]).commit();
	}

	public static void LoadFragment() {
		listAdapter = new ArrayList<JSONType>();
		for (int i = 0; i < keys.length; i++) {
			listAdapter.add(new JSONType(keys[i], values[i]));
		}
		if (rootView.findViewById(R.id.title1) == null) {
			ListView content = (ListView) rootView.findViewById(R.id.content);
			content.setAdapter(new StudentInfoAdapter(activity, listAdapter));
		} else
			LoadContactFragment();
	}

	public static void LoadContactFragment() {
		// setting for contact fragment
		TextView title1 = (TextView) rootView.findViewById(R.id.title1);
		title1.setText(activity.getResources().getString(
				listTitle[listTitle.length - 1]));
		title1.setBackgroundColor(activity.getResources().getColor(color[TYPE]));
		
		listAdapter1 = new ArrayList<JSONType>();
		for (int i = 0; i < key1.length; i++) {
			listAdapter1.add(new JSONType(key1[i], value1[i]));
		}

		LinearLayout content = (LinearLayout) rootView
				.findViewById(R.id.content);
		StudentInfoAdapter adapter = new StudentInfoAdapter(activity,
				listAdapter);
		content.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, null);
			content.addView(item);
		}

		LinearLayout content1 = (LinearLayout) rootView
				.findViewById(R.id.content1);
		adapter = new StudentInfoAdapter(activity, listAdapter1);
		content1.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, null);
			content1.addView(item);
		}
	}
}
