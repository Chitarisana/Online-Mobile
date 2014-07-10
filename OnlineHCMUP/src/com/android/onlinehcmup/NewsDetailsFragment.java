package com.android.onlinehcmup;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsDetailsFragment extends BaseFragment {

	public NewsDetailsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.details_news, container,
				false);
		TextView title = (TextView) rootView.findViewById(R.id.title);
		TextView content = (TextView) rootView.findViewById(R.id.content);
		Bundle extras = getArguments();
		int TYPE;
		if (extras != null) {
			TYPE = extras.getInt(NewsListFragment.KEY_TYPE);
			title.setText(extras.getString(NewsListFragment.KEY_TITLE));
			String text = extras.getString(NewsListFragment.KEY_CONTENT);
			Spanned temp = Html.fromHtml(text);
			content.setText(temp);

			Linkify.addLinks(content, Linkify.ALL);
			content.setMovementMethod(LinkMovementMethod.getInstance());

			if (TYPE == NewsListFragment.TYPE_PUBLIC) {
				setTitle(R.string.public_news_details);
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
				setHasOptionsMenu(true);
			} else {
				setOnFragment(R.string.private_news_details);
				PrivateMainActivity.itemTitle = getActivity().getTitle();
			}
		}
		return rootView;
	}
}