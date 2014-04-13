package com.android.onlinehcmup;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.android.onlinehcmup.Adapter.PublicNewsAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.Model.PublicNews;
import com.android.onlinehcmup.Support.StaticTAG;

public class MainActivity extends Activity {

	PublicNewsAdapter adapter;
	public static ViewGroup listGroup;
	public static ArrayList<PublicNews> data;
	public static Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_main);
		activity = this;
		// create data
		// get News

		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// show UP button
		// getActionBar().setIcon(R.drawable.ic_login);
		// change icon of title
		// getActionBar().setTitle(R.string.menu_main_public);
		// change title
		data = new ArrayList<PublicNews>();

		listGroup = (ViewGroup) findViewById(R.id.listview);
		StaticTAG.ACTIVITY = this;

		//if (savedInstanceState == null) {
			Connect connect = new Connect(activity);
			connect.LoadPublicNews(2, 0);
		//}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/*
		 * if (navDetailsToList()) currentTag =
		 * StaticTAG.TAG_PUBLIC_NEWS_DETAILS;
		 */
	}

	@Override
	public void onBackPressed() {
		boolean nav = navDetailsToList();
		if (nav) {
			getFragmentManager().popBackStack();
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setHomeButtonEnabled(false);
			getActionBar()
					.setTitle(getResources().getString(R.string.app_name));
		} else
			super.onBackPressed();
	}

	public boolean navDetailsToList() {
		NewsDetailsFragment frag = (NewsDetailsFragment) getFragmentManager()
				.findFragmentByTag(StaticTAG.TAG_PUBLIC_NEWS_DETAILS);
		return frag != null && frag.isVisible();
	}
}
