package com.android.onlinehcmup.Support;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.onlinehcmup.R;

public class DialogManager {

	private static SessionManager session;

	public static void showAlertDialog(Context context, String title,
			String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(
				new ContextThemeWrapper(context, R.style.AlertDialogCustom))
				.create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			alertDialog.setIcon((status) ? R.drawable.ic_success
					: R.drawable.ic_fail);

		// Setting OK Button
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		// Showing Alert Message
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
							session.logoutUser();
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

	public static PopupWindow showListDialog(final Activity activity, ListView list,
			View parent) {
		if (list.getAdapter() == null)
			// list set adapter (and event) already >.<
			return null;
		final PopupWindow popup = new PopupWindow(activity);
		popup.setTouchable(true);
		popup.setFocusable(true);
		popup.setOutsideTouchable(true);
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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