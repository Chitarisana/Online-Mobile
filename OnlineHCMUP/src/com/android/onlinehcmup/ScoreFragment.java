package com.android.onlinehcmup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlinehcmup.Adapter.KeyValueAdapter;
import com.android.onlinehcmup.Adapter.MenuAdapter;
import com.android.onlinehcmup.JSON.Connect;
import com.android.onlinehcmup.JSON.LocalTask;
import com.android.onlinehcmup.Model.JSONType;
import com.android.onlinehcmup.Model.ScoreDetail;
import com.android.onlinehcmup.Model.StudentScore;
import com.android.onlinehcmup.Model.TermScore;
import com.android.onlinehcmup.Support.DialogManager;
import com.android.onlinehcmup.Support.SessionManager;

public class ScoreFragment extends BaseFragment {

	static SessionManager session;
	static Activity activity;
	public static ArrayList<TermScore> TermScores;
	public static TermScore current;
	public static ListView list, details;
	static TextView title, help,markAvg, rlsv, markRank, rlsvRank;
	Switch mark4;
	int selectedItem = 0;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.examinate, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_choose_term:
			if (!MenuAdapter.menuLV.isShown()) {
				String[] termList = new String[TermScores.size()];
				for (int i = 0; i < termList.length; i++) {
					termList[i] = TermScores.get(i).TermName;
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						activity, R.layout.row_popup_list, R.id.text, termList);
				final Dialog dialog = DialogManager.showPopupDialog(activity,
						getString(R.string.menu_choose_term), adapter, 0, 0,
						null);

				final ListView list = (ListView) dialog
						.findViewById(R.id.listview);

				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView, View v,
							int position, long arg3) {
						selectedItem = 0; // make the first item be selected
						list.setItemChecked(position, true);
						reload(position, mark4.isChecked());
						loadDetails();
						dialog.dismiss();
					}
				});
				dialog.show();
			} else {
				MenuAdapter.mainLayout.closeDrawer(MenuAdapter.menuLV);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		session = new SessionManager(activity);
		session.checkLogin();
		session.setMark4(false);
		session.setSelectedPosition(0);
		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(R.string.menu_5_1);
		setHasOptionsMenu(true);

		View row = inflater.inflate(R.layout.fragment_score, container, false);
		HashMap<String, String> user = session.getUserDetails();
		String studentID = user.get(SessionManager.KEY_STUDENT_ID);
		String studentName = user.get(SessionManager.KEY_STUDENT_NAME);

		TextView stdName = (TextView) row.findViewById(R.id.studentName);
		stdName.setText(studentName);
		TextView stdID = (TextView) row.findViewById(R.id.studentID);
		stdID.setText(studentID);

		title = (TextView) row.findViewById(R.id.title);
		markAvg = (TextView) row.findViewById(R.id.markAvg);
		rlsv = (TextView) row.findViewById(R.id.rlsv);
		markRank = (TextView) row.findViewById(R.id.markRank);
		rlsvRank = (TextView) row.findViewById(R.id.rlsvRank);

		mark4 = (Switch) row.findViewById(R.id.mark4);
		mark4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				reload(session.getSelectedPosition(), isChecked);
				loadDetails();
			}
		});

		help = (TextView) row.findViewById(R.id.help);
		help.setText(R.string.help_title);
		details = (ListView) row.findViewById(R.id.details);
		list = (ListView) row.findViewById(R.id.content);
		list.setItemsCanFocus(true);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displaymetrics.heightPixels / 5));
		list.setLayoutParams(params);

		Connect connect = new Connect(activity);
		PrivateMainActivity.currentTask = connect.LoadScore(studentID);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				selectedItem = position;
				loadDetails();
			}
		});
		return row;
	}

	private void reload(int position, Boolean isChecked) {
		session.setSelectedPosition(position);
		session.setMark4(isChecked);
		LocalTask.LoadDB(Connect.TYPE_SCORE);
	}

	private void loadDetails() {
		list.setItemChecked(selectedItem, true);
		ArrayList<JSONType> data = new ArrayList<JSONType>();
		data.add(new JSONType("Điểm", (session.getMark4() ? current.Score
				.get(selectedItem).Mark4
				: current.Score.get(selectedItem).Mark10)
				+ ""));
		data.add(new JSONType("Điểm chữ",
				current.Score.get(selectedItem).MarkLetter));
		data.add(new JSONType("Kết quả", current.Score.get(selectedItem).IsPass));
		data.add(new JSONType("Số tín chỉ",
				current.Score.get(selectedItem).Credits + ""));
		KeyValueAdapter adapter = new KeyValueAdapter(activity, data,
				R.layout.row_study_program_term_details, false);
		details.setAdapter(adapter);
		help.setText((getString(R.string.help_change_curri)+" "+current.Score.get(selectedItem).CurriculumName).trim());
	}

	public static void LoadFragment(int termPosition) {
		if (TermScores == null) {
			Toast.makeText(activity, R.string.noti_null, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		title.setText(TermScores.get(termPosition).TermName);
		markAvg.setText("Điểm trung bình: "
				+ (session.getMark4() ? TermScores.get(termPosition).AverageScore4
						: TermScores.get(termPosition).AverageScore));
		rlsv.setText("Điểm rèn luyện: "
				+ TermScores.get(termPosition).LastScore);
		markRank.setText("Xếp loại: " + TermScores.get(termPosition).RankName);
		rlsvRank.setText("Xếp loại: "
				+ TermScores.get(termPosition).BehaviorScoreRank);
		list.setAdapter(new MarkInfoAdapter(TermScores.get(termPosition).Score));
	}

	public static class MarkInfoAdapter extends BaseAdapter {
		static ArrayList<StudentScore> data;
		public static ArrayList<ScoreDetail> ScoreDetails;
		public static String curriID;

		public MarkInfoAdapter(ArrayList<StudentScore> d) {
			data = d;
			curriID = data.get(0).CurriculumID;
		}

		@Override
		public int getCount() {
			return data.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = (activity.getLayoutInflater()).inflate(
					R.layout.row_score, null);
			TextView curri = (TextView) row.findViewById(R.id.curriName);
			curri.setText(data.get(position).CurriculumName);

			ImageView info = (ImageView) row.findViewById(R.id.info);
			/*
			 * info.setImageResource(data.get(position).IsPass
			 * .compareTo(StudentScore.PassSuccess) == 0 ? R.drawable.ic_info :
			 * R.drawable.ic_failed);
			 */
			info.setImageResource(R.drawable.ic_info);
			info.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					curriID = data.get(position).CurriculumID;
					session.setSelectedScoreDetail(position);
					Connect connect = new Connect(activity);
					connect.LoadScoreDetail(session.getStudentID(), curriID);
				}
			});

			info.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int paddingRight = info.getMeasuredWidth();
			curri.setPadding(curri.getPaddingLeft(), curri.getPaddingTop(),
					paddingRight, curri.getPaddingBottom());
			int c = curri.getCurrentTextColor();
			curri.setTextColor(data.get(position).IsPass
					.compareTo(StudentScore.PassSuccess) == 0 ? c : Color.RED);
			return row;
		}

		public static void ViewDetails() {
			if (ScoreDetails == null) {
				Toast.makeText(activity, R.string.noti_null, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			int position = session.getSelectedScoreDetails();

			ArrayList<JSONType> scoreDetailsData = new ArrayList<JSONType>();
			for (int i = 0; i < ScoreDetails.size(); i++) {
				scoreDetailsData.add(new JSONType(
						ScoreDetails.get(i).AssignmentName,
						ScoreDetails.get(i).FirstMark));
			}
			Dialog dialog = DialogManager.showPopupDialog(activity,
					data.get(position).CurriculumName, scoreDetailsData, 0, 0,
					false);
			dialog.show();
		}
	}
}
