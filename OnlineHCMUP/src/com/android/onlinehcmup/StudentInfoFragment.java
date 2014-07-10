package com.android.onlinehcmup;

import java.util.ArrayList;

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
import android.widget.Toast;

import com.android.onlinehcmup.Adapter.KeyValueAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.JSON.Key;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class StudentInfoFragment extends BaseFragment {

	// constant values
	public static final String KEY_TYPE = "TYPE";
	public static final int TYPE_INFO = 0, TYPE_COURSE = 1, TYPE_CONTACT = 2;
	static final int[] color = new int[] { R.color.btnInfoColor,
			R.color.btnCourseColor, R.color.btnContactColor };
	final int[] actionBarTitle = new int[] { R.string.menu_1, R.string.menu_1,
			R.string.key_contact_title };
	final static int[] btnNav = new int[] { R.string.key_info_title,
			R.string.key_course_title, R.string.key_contact_title };
	final static int[] listTitle = new int[] { R.string.key_info_title,
			R.string.key_course_title, R.string.key_contact_1_title,
			R.string.key_contact_2_title };
	final int[] layout = new int[] { R.layout.fragment_student_info,
			R.layout.fragment_student_info, R.layout.fragment_student_contact };
	final String[] tag = new String[] { StaticTAG.TAG_STUDENT_INFO,
			StaticTAG.TAG_STUDENT_COURSE, StaticTAG.TAG_STUDENT_CONTACT };
	final static String[][] keyType = new String[][] { Key.KEY_STUDENT_INFO_VI,
			Key.KEY_STUDENT_COURSE_VI, Key.KEY_STUDENT_CONTACT_1_VI,
			Key.KEY_STUDENT_CONTACT_2_VI };

	public static Activity activity;
	public static String[] values, value1;
	public static int TYPE = TYPE_INFO;
	public static ImageView image;

	SessionManager session;
	DisplayMetrics displaymetrics;
	static String[] keys, key1;
	static View rootView;
	static ArrayList<JSONType> listAdapter, listAdapter1;

	public StudentInfoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		session = new SessionManager(activity);
		session.checkLogin();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle extras = getArguments();
		if (extras != null) {
			TYPE = extras.getInt(KEY_TYPE);
		}

		setTitle(actionBarTitle[TYPE]);

		rootView = inflater.inflate(layout[TYPE], container, false);
		image = (ImageView) rootView.findViewById(R.id.profile);

		displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);

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

		btn1.setText(btnNav[(TYPE + 1) % 3]);
		btn2.setText(btnNav[(TYPE + 2) % 3]);

		btn1.setBackgroundColor(getColor(color[(TYPE + 1) % 3]));
		btn2.setBackgroundColor(getColor(color[(TYPE + 2) % 3]));

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
		title.setText(getString(listTitle[TYPE]));
		title.setBackgroundColor(getColor(color[TYPE]));
		TextView btnChgPass = (TextView) rootView
				.findViewById(R.id.btnChangePass);

		btnChgPass = scale(btnChgPass, 16);

		btnChgPass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Connect.isConnectingToInternet(activity))
					activity.getFragmentManager()
							.beginTransaction()
							.replace(R.id.content,
									new ChangePasswordFragment(),
									StaticTAG.TAG_STUDENT_CHANGE_PASSWORD)
							.addToBackStack(null).commit();
				else
					Toast.makeText(activity, R.string.error_connect,
							Toast.LENGTH_LONG).show();
			}
		});

		if (rootView.findViewById(R.id.btnEdit) != null) {
			TextView btnEdit = (TextView) rootView.findViewById(R.id.btnEdit);
			btnEdit = scale(btnEdit, 16);
			btnEdit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (Connect.isConnectingToInternet(activity))
						activity.getFragmentManager()
								.beginTransaction()
								.replace(R.id.content,
										new StudentEditInfoFragment(),
										StaticTAG.TAG_STUDENT_EDIT_INFO)
								.addToBackStack(null).commit();
					else
						Toast.makeText(activity, R.string.error_connect,
								Toast.LENGTH_LONG).show();
				}
			});
		}
		keys = keyType[TYPE];
		values = new String[keys.length];
		key1 = keyType[keyType.length - 1];
		value1 = new String[key1.length];

		String studentID = session.getStudentID();
		Connect connect = new Connect(activity);
		PrivateMainActivity.currentTask = connect.LoadStudentInfo(studentID,
				TYPE);

		return rootView;
	}

	private TextView scale(TextView btn, int divide) {
		btn.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		float scale = (float) (1.0 * displaymetrics.heightPixels / 16)
				/ btn.getMeasuredHeight();
		if (scale < 1)
			btn.setScaleY(scale);
		return btn;
	}

	private void openFragment(int btnType) {
		Fragment fragment = new StudentInfoFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_TYPE, btnType);
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
			content.setAdapter(new KeyValueAdapter(activity, listAdapter,
					R.layout.row_student_info, null));
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
		KeyValueAdapter adapter = new KeyValueAdapter(activity, listAdapter,
				R.layout.row_student_contact, null);
		content.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, null);
			content.addView(item);
		}

		LinearLayout content1 = (LinearLayout) rootView
				.findViewById(R.id.content1);
		adapter = new KeyValueAdapter(activity, listAdapter1,
				R.layout.row_student_contact, null);
		content1.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, null);
			content1.addView(item);
		}
	}
}
