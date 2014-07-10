package com.android.onlinehcmup;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public BaseFragment() {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().onBackPressed();
			return true;
		case R.id.action_login:
			// start Activity Login
			Intent i = new Intent(getActivity(), LoginActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setOnFragment(String title) {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setTitle(title);
		PrivateMainActivity.menuToggle.setDrawerIndicatorEnabled(false);
		PrivateMainActivity.mainLayout
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		setHasOptionsMenu(true);
	}

	public void setOnFragment(int title) {
		setOnFragment(getString(title));
	}

	public void setTitle(int id) {
		getActivity().getActionBar().setTitle(
				getActivity().getResources().getString(id));
	}

	public void setTitle(CharSequence title) {
		getActivity().getActionBar().setTitle(title);
	}

	public int getColor(int id) {
		return getActivity().getResources().getColor(id);
	}
}