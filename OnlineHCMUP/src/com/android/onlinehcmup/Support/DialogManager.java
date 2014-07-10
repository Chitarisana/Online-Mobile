package com.android.onlinehcmup.Support;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.onlinehcmup.MainActivity;
import com.android.onlinehcmup.R;
import com.android.onlinehcmup.Adapter.KeyValueAdapter;
import com.android.onlinehcmup.Model.JSONType;

public class DialogManager {

	private static SessionManager session;

	/*
	 * Setting alertDialog: context, titleID, messageID are just same normal;
	 * status to show success icon or fail icon.
	 */
	public static void showAlertDialog(Context context, int titleID,
			int messageID, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(
				new ContextThemeWrapper(context, R.style.AlertDialogCustom))
				.create();

		alertDialog.setTitle(titleID);

		alertDialog.setMessage(context.getString(messageID));

		if (status != null)
			alertDialog.setIcon((status) ? R.drawable.ic_success
					: R.drawable.ic_fail);

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		alertDialog.show();
	}

	public static void showYesNoDialog(final Activity activity, String title,
			String message, String posButton, String negButton,
			final boolean isToLogout) {
		AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(
				activity, R.style.AlertDialogCustom)).create();
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIcon(R.drawable.ic_signout);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, posButton,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (isToLogout) {
							session = new SessionManager(activity);
							session.logoutUser(MainActivity.class);
							activity.finish();
						}
					}
				});
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, negButton,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	public static Dialog showPopupDialog(Activity activity, String titleString,
			ArrayList<JSONType> data, int width, int height, Boolean isKey) {
		return initPopup(activity, titleString, null, data, width, height,
				isKey);
	}

	public static Dialog showPopupDialog(Activity activity, String titleString,
			ArrayAdapter<String> adapter, int width, int height, Boolean isKey) {
		return initPopup(activity, titleString, adapter, null, width, height,
				isKey);
	}

	private static Dialog initPopup(Activity activity, String titleString,
			ArrayAdapter<String> adapter, ArrayList<JSONType> data, int width,
			int height, Boolean isKey) {
		Dialog dialog = new Dialog(activity, R.style.ScoreDetailDialog);
		dialog.setContentView(R.layout.frame_popup);

		TextView title = (TextView) dialog.findViewById(android.R.id.title);
		title.setSingleLine(false);
		title.setGravity(Gravity.CENTER);
		title.measure(0, 0);

		ListView list = (ListView) dialog.findViewById(R.id.listview);
		int count = 0;
		if (adapter != null) {
			list.setAdapter(adapter);
			count = adapter.getCount();
		} else if (data != null) {
			KeyValueAdapter adt = new KeyValueAdapter(activity, data, 0, isKey);
			list.setAdapter(adt);
			count = adt.getCount();
		} else
			return null;

		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		if (width == 0)
			width = displaymetrics.widthPixels * 9 / 10;
		if (height == 0) {
			height = Math.min(displaymetrics.heightPixels / 2,
					(int) (title.getMeasuredHeight() * (2 + 0.5 * count)));
		}
		dialog.getWindow().setLayout(width, height);
		dialog.setTitle(titleString);
		dialog.setCancelable(true);
		return dialog;
	}

	public static PopupWindow showListDialog(final Activity activity,
			ListView list, View parent) {
		if (list.getAdapter() == null)
			// list set adapter (and event) already >.<
			return null;
		final PopupWindow popup = new PopupWindow(activity);
		popup.setTouchable(true);
		popup.setFocusable(true);
		popup.setOutsideTouchable(true);
		activity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popup.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popup.dismiss();
					return true;
				}
				return false;
			}
		});
		popup.setContentView(list);
		popup.showAtLocation(parent, Gravity.CENTER, 0, 0);
		return popup;
	}
}