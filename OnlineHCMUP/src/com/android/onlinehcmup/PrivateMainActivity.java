package com.android.onlinehcmup;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
//import android.widget.Toast;

import com.android.onlinehcmup.Adapter.MenuAdapter;
import com.android.onlinehcmup.JSON.DownloadTask;
import com.android.onlinehcmup.Model.SliderMenu;
import com.android.onlinehcmup.SQLite.DatabaseHandler;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class PrivateMainActivity extends Activity {

	public static Activity activity;
	public static DrawerLayout mainLayout;
	public static DownloadTask currentTask;
	public static ActionBarDrawerToggle menuToggle;
	private CharSequence mainTitle;
	public static CharSequence itemTitle, title;
	SessionManager session;

	// list of fragment TAG that will pop back when button back pressed.
	private final String[] fragTag = new String[] {
			StaticTAG.TAG_PRIVATE_NEWS_DETAILS,
			StaticTAG.TAG_STUDENT_CHANGE_PASSWORD,
			StaticTAG.TAG_STUDENT_EDIT_INFO,
			StaticTAG.TAG_REGISTER_CURRICULUM_RESULT,
			StaticTAG.TAG_REGISTER_CURRICULUM_RESULT_ALL,
			StaticTAG.TAG_REGISTER_CURRICULUM_DISACCUMULATED,
			StaticTAG.TAG_REGISTER_CURRICULUM_REGISTER,
			/*StaticTAG.TAG_SCHEDULE_EXAMINATE */};

	@Override
	public void onStop() {
		super.onStop();
		title = getActionBar().getTitle();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (title != null)
			getActionBar().setTitle(title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_main);

		activity = this;
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (DownloadTask.db == null)
			DownloadTask.db = new DatabaseHandler(this);

		// add slider bar
		mainTitle = getString(R.string.app_name);
		mainLayout = (DrawerLayout) findViewById(R.id.mainLayout);
		ListView menuLV = (ListView) findViewById(R.id.menu);

		String[] menuArray = getResources().getStringArray(R.array.menu_array);
		TypedArray iconArray = getResources().obtainTypedArray(
				R.array.menu_icon);
		ArrayList<SliderMenu> sliderMenu = new ArrayList<SliderMenu>();

		for (int i = 0; i < menuArray.length; i++) {
			sliderMenu.add(new SliderMenu(iconArray.getResourceId(i, -1),
					menuArray[i]));
		}
		iconArray.recycle();

		menuLV.setAdapter(new MenuAdapter(this, sliderMenu, mainLayout, menuLV));

		menuToggle = new ActionBarDrawerToggle(this, mainLayout,
				R.drawable.ic_menu, 0, 0) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(itemTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mainTitle);
				invalidateOptionsMenu();
			}
		};
		mainLayout.setDrawerListener(menuToggle);

		// if (savedInstanceState == null) {
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.content, new PrivateNewsFragment(),
						StaticTAG.TAG_PRIVATE_NEWS_LIST).commit();
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (menuToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setTitle(CharSequence title) {
		itemTitle = title;
		getActionBar().setTitle(itemTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		menuToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		menuToggle.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_private_main);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		if (navBack()) {
			getFragmentManager().popBackStack();
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(itemTitle);
			menuToggle.setDrawerIndicatorEnabled(true);
			mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		} else
			super.onBackPressed();
	}

	public boolean navBack() {
		for (int i = 0; i < fragTag.length; i++) {
			Fragment frag = getFragmentManager().findFragmentByTag(fragTag[i]);
			if (frag != null && frag.isVisible())
				return true;
		}
		return false;
	}
}