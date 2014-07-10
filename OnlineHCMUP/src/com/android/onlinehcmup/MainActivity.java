package com.android.onlinehcmup;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Model.PublicNews;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class MainActivity extends Activity {

	public static ArrayList<PublicNews> data;
	public static Activity activity;
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_main);

		activity = this;

		// session manager
		session = new SessionManager(this);
		if (session.isLoggedIn()) {
			Intent i = new Intent(this, PrivateMainActivity.class);
			startActivity(i);
			finish();
		} else {
			Connect connect = new Connect(activity);
			connect.LoadPublicNews();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.public_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_login:
			// start Activity Login
			Intent i = new Intent(this, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_public_main);
	}

	@Override
	public void onBackPressed() {
		boolean nav = navDetailsToList();
		if (nav) {
			getFragmentManager().popBackStack();
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setHomeButtonEnabled(false);
			getActionBar().setTitle(R.string.app_name);
		} else
			super.onBackPressed();
	}

	public boolean navDetailsToList() {
		NewsDetailsFragment frag = (NewsDetailsFragment) getFragmentManager()
				.findFragmentByTag(StaticTAG.TAG_PUBLIC_NEWS_DETAILS);
		return frag != null && frag.isVisible();
	}

	public static void Reload() {
		NewsListFragment fragment = new NewsListFragment();
		Bundle args = new Bundle();
		args.putInt(NewsListFragment.KEY_TYPE, NewsListFragment.TYPE_PUBLIC);
		fragment.setArguments(args);

		activity.getFragmentManager()
				.beginTransaction()
				.replace(R.id.listview, fragment,
						StaticTAG.TAG_PUBLIC_NEWS_LIST).commit();
	}
}
