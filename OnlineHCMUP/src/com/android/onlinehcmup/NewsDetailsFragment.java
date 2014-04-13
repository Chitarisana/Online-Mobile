package com.android.onlinehcmup;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsDetailsFragment extends BaseFragment {

	public static String KEY_TYPE = "type";
	public static int KEY_PUBLIC = 0;
	public static int KEY_PRIVATE = 1;
	public static String KEY_TITLE = "title";
	public static String KEY_CONTENT = "content";
	private int type;
	public Bundle extras;

	public NewsDetailsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.details_news, container,
				false);
		TextView title = (TextView) rootView.findViewById(R.id.title);
		TextView content = (TextView) rootView.findViewById(R.id.content);		
		extras = getArguments();
		if (extras != null) {
			type = extras.getInt(KEY_TYPE);
			title.setText(extras.getString(KEY_TITLE));			
			String text = extras.getString(KEY_CONTENT);
			Spanned temp = Html.fromHtml(text);
			content.setText(temp);
			content.setMovementMethod(LinkMovementMethod.getInstance());
			// content.setLinksClickable(true);
		}
		
		int idTitle;
		if (type == KEY_PUBLIC) {
			idTitle = R.string.public_news_details;
		} else {
			idTitle = R.string.private_news_details;
			PrivateMainActivity.menuToggle.setDrawerIndicatorEnabled(false);
			PrivateMainActivity.mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}
		getActivity().getActionBar().setTitle(
				getActivity().getResources().getString(idTitle));
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);

		return rootView;
	}
}