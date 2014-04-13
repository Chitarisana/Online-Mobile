package com.android.onlinehcmup.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.onlinehcmup.MarkFragment;
import com.android.onlinehcmup.PrivateNewsFragment;
import com.android.onlinehcmup.R;
import com.android.onlinehcmup.RegisterCurriculumFragment;
import com.android.onlinehcmup.ScheduleFragment;
import com.android.onlinehcmup.StudentInfoFragment;
import com.android.onlinehcmup.StudyProgramFragment;
import com.android.onlinehcmup.Model.SliderMenu;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class MenuAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<SliderMenu> menu;

	public static DrawerLayout mainLayout;
	public static ListView menuLV;
	SessionManager session;

	public MenuAdapter(Activity a, ArrayList<SliderMenu> m, DrawerLayout mL,
			ListView mLV) {
		activity = a;
		menu = m;
		mainLayout = mL;
		menuLV = mLV;
		session = new SessionManager(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return menu.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = (activity.getLayoutInflater()).inflate(R.layout.menu_item,
				parent, false);
		TextView item = (TextView) row.findViewById(R.id.menuitem);
		item.setText(menu.get(position).Text);

		item.setCompoundDrawablesWithIntrinsicBounds(menu.get(position).Icon,
				0, 0, 0);

		// Set Click Event
		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// check login
				session.checkLogin();

				Log.w("position", position + "");
				menuLV.setItemChecked(position, true);
				mainLayout.closeDrawer(menuLV);

				switch (position) {
				case 0: // Main --> PrivateNewsFragment
					openFragment(new PrivateNewsFragment(),
							StaticTAG.TAG_PRIVATE_NEWS_LIST,
							menu.get(position).Text);
					break;
				case 1: // Personal Information --> StudentInfoFragment
					openFragment(new StudentInfoFragment(),
							StaticTAG.TAG_STUDENT_INFO, menu.get(position).Text);
					break;
				case 2: // Study Program --> StudyProgramFragment
					openFragment(new StudyProgramFragment(),
							StaticTAG.TAG_STUDY_PROGRAM,
							menu.get(position).Text);
					break;
				case 3: // Register Curriculum --> RegisterCurriculumFragment
					openFragment(new RegisterCurriculumFragment(),
							StaticTAG.TAG_REGISTER_CURRICULUM,
							menu.get(position).Text);
					break;
				case 4: // Schedule --> ScheduleFragment
					openFragment(new ScheduleFragment(),
							StaticTAG.TAG_SCHEDULE, activity.getResources()
									.getString(R.string.menu_4_1));
					ScheduleFragment.schedulePosition=4;
					break;
				case 5: // Mark --> MarkFragment
					openFragment(new MarkFragment(),
							StaticTAG.TAG_MARK, activity.getResources()
									.getString(R.string.menu_5_1));
					MarkFragment.markPosition=5;
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				case 11: // Thoát
					activity.onBackPressed();

					// Logout
					// session.logoutUser();

					// return to main page
					// Intent i = new Intent(activity.getApplicationContext(),
					// MainActivity.class);
					// activity.startActivity(i);
					// activity.finish();
					break;
				default:
				}
			}
		});
		return row;
	}

	public void openFragment(Fragment frag, String tag, String title) {
		FragmentManager fragmentManager = activity.getFragmentManager();
		while (fragmentManager.getBackStackEntryCount() > 1)
			fragmentManager.popBackStack();
		fragmentManager.beginTransaction().replace(R.id.content, frag, tag)
				.commit();
		activity.setTitle(title);
	}
}
