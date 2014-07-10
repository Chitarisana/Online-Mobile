package com.android.onlinehcmup.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.onlinehcmup.LoginActivity;
import com.android.onlinehcmup.MainActivity;
import com.android.onlinehcmup.PrivateMainActivity;
import com.android.onlinehcmup.R;
import com.android.onlinehcmup.ScheduleFragment.ExaminationFragment;
import com.android.onlinehcmup.StudentEditInfoFragment;
import com.android.onlinehcmup.StudentInfoFragment;
import com.android.onlinehcmup.Model.Curriculum;
import com.android.onlinehcmup.Model.PrivateNews;
import com.android.onlinehcmup.Model.PublicNews;
import com.android.onlinehcmup.Model.RegisteredStudyUnit;
import com.android.onlinehcmup.Model.ScheduleCalendar;
import com.android.onlinehcmup.Model.ScheduleExamination;
import com.android.onlinehcmup.Model.ScoreDetail;
import com.android.onlinehcmup.Model.Student;
import com.android.onlinehcmup.Model.StudentScore;
import com.android.onlinehcmup.Model.StudyProgram;
import com.android.onlinehcmup.Model.TermScore;
import com.android.onlinehcmup.SQLite.DatabaseHandler;
import com.android.onlinehcmup.SQLite.DatabaseHandler.NotAccumulatedCurriculumDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.PrivateNewsDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.RegisteredCurriculumDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.ScheduleCalendarDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.ScoreDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.ScoreDetailDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.StudentDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.StudyProgramDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.TermScoreDAL;
import com.android.onlinehcmup.Support.DownloadImageTask;
import com.android.onlinehcmup.Support.Parser;
import com.android.onlinehcmup.Support.SessionManager;

public class DownloadTask extends AsyncTask<String, Void, String[]> {

	int readTimeoutMillis = 10000;
	int connectTimeoutMillis = 15000;
	static Activity activity;
	ProgressDialog dialog;

	public static DatabaseHandler db;
	static DownloadTask task;

	SessionManager session;
	Student std;

	protected void onPreExecute() {
		activity = Connect.activity;
		dialog = new ProgressDialog(activity, R.style.ProgressDialogCustom);
		dialog.setTitle(R.string.app_name);
		dialog.setMessage(activity.getString(R.string.noti_loading));
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.setCancelable(false);
		// if (Connect.TYPE != Connect.TYPE_INIT)
		dialog.show();
	}

	@Override
	protected String[] doInBackground(String... urls) {
		InputStream is = null;
		try {
			ArrayList<String> results = new ArrayList<String>();
			for (String uri : urls) {
				URL url = new URL(uri);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setReadTimeout(readTimeoutMillis);
				conn.setConnectTimeout(connectTimeoutMillis);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(is));
				String line = "";
				String result = "";
				while ((line = bufferedReader.readLine()) != null)
					result += line;
				is.close();
				results.add(result);
			}
			return results.toArray(new String[results.size()]);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String[] results) {
		if (dialog.isShowing())
			dialog.dismiss();
		if (db == null)
			db = new DatabaseHandler(activity);
		if (!isNull(results))
			handlerJSON(results);
		else
			Toast.makeText(activity, R.string.error_connect, Toast.LENGTH_SHORT)
					.show();
		LocalTask.LoadDB(Connect.TYPE);
	}

	private Boolean isNull(String[] array) {
		if (array == null)
			return true;
		for (int i = 0; i < array.length; i++)
			if (array[i] == null) {
				return true;
			}
		return false;
	}

	/*
	 * Save JSON to DB if need, or load from JSON to VIEW if it wasn't saved to
	 * DB
	 */
	private void handlerJSON(String[] results) {
		try {
			std = new Student();
			session = new SessionManager(activity);
			for (int i = 0; i < results.length; i++) {
				JSONObject obj = new JSONObject(results[i]);
				if (obj.has(Key.KEY_STATUS)) {
					int stt = Integer.parseInt(obj.getString(Key.KEY_STATUS));
					if (stt == 0) {
						if (obj.has(Key.KEY_ERRORS)) {
							if (Connect.TYPE == Connect.TYPE_LOGIN) {
								Toast.makeText(activity,
										R.string.login_fail_noti_detail1,
										Toast.LENGTH_LONG).show();
							} else {
								String error = obj.getString(Key.KEY_ERRORS);
								Toast.makeText(activity, error,
										Toast.LENGTH_SHORT).show();
							}
						}
					} else {
						switch (Connect.TYPE) {
						case 0: // TYPE_PUBLIC_NEWS
							publicNewsTask(obj);
							break;
						case 1: // TYPE_INIT
							Log.d("i", i + "");
							initTask(obj, i);
							break;
						case 111: // TYPE_INIT_NOTACC
							initNotAccTask(obj);
							break;
						case 112: // TYPE_INIT_SCORE_DETAILS
							scoreDetailsTask(obj);
							break;
						case 2: // TYPE_LOGIN
							loginTask(obj);
							break;
						case 3: // TYPE_PRIVATE_NEWS
							privateNewsTask(obj);
							break;
						case 4: // TYPE_STUDENT_INFO
							studentInfoTask(obj);
							break;
						case 5: // TYPE_STUDENT_CHANGE_PASSWORD
							studentChangePasswordTask();
							break;
						case 6: // TYPE_LOAD_CONTACT --> EDIT CONTACT
							// load contact at EditContactFragment
							studentLoadContactTask(obj);
							break;
						case 7: // TYPE_STUDENT_EDIT_INFO
							studentEditInfoTask(obj);
							break;
						case 8: // TYPE_STUDY_PROGRAM
							studyProgramTask(obj);
							break;
						case 9: // TYPE_REGISTER_RESULT_ALL
							registerResultAllTask(obj);
							break;
						case 10: // TYPE_REGISTER_RESULT_CURRENT
							registerResultCurrentTask(obj);
							break;
						case 11: // TYPE_REGISTER_NOT_ACCUMULATE
							registerNotAccumulateTask(obj);
							break;
						case 12:
							scheduleCalendarTask(obj);
							break;
						case 13:
							scheduleExaminationTask(obj);
							break;
						case 14:
							scoreTask(obj, i);
							break;
						case 15:
							scoreDetailsTask(obj);
							break;
						}
					}
				}
			}
			if (session.isLoggedIn()) {
				// INIT FUNCTION
				Connect connect = new Connect(activity);
				switch (Connect.TYPE) {
				case 2: // LOGIN
					// call init after login
					task = connect.Init(session.getStudentID());
					break;
				case 1: // INIT
					// call init not acc curri after the first init
					task = connect.InitNotAccumulateCurriculum(
							session.getStudentID(), session.getStudyProgram());
					break;
				case 111: // INIT NOT ACCUMULATE CURRICULUM
					// init score details after not acc curri
					ArrayList<String> studyUnitID = ScoreDAL
							.GetAllStudyUnitID();
					String[] links = new String[studyUnitID.size()];
					String studentID = session.getStudentID();
					for (int i = 0; i < links.length; i++) {
						links[i] = String.format(Link.SCORE_DETAIL, studentID,
								studyUnitID.get(i));
					}
					connect.InitScoreDetails(links);
					break;
				case 112: // INIT SCORE DETAILS
					// start private main activity after done init
					// start new intent
					// Starting PrivateMainActivity
					Intent i = new Intent(activity, PrivateMainActivity.class);
					activity.startActivity(i);
					activity.finish();
					MainActivity.activity.finish();
					task = PrivateMainActivity.currentTask;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String[] getValues(JSONObject js, String[] keys) {
		String[] values = new String[keys.length];
		try {
			for (int i = 0; i < keys.length; i++) {
				if (js.has(keys[i]))
					values[i] = Parser.TrimSpace(js.getString(keys[i]));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return values;
	}

	private String[] getValuesNotNull(JSONObject js, String[] keys) {
		String[] values = new String[keys.length];
		try {
			for (int i = 0; i < keys.length; i++) {
				if (js.has(keys[i])) {
					values[i] = Parser.TrimSpace(js.getString(keys[i]));
					if (values[i] == null || values[i].matches(""))
						return null;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return values;
	}

	private void publicNewsTask(JSONObject obj) {
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			ArrayList<PublicNews> data = new ArrayList<PublicNews>();
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValues(js, Key.KEY_PUBLIC_NEWS);
				if (values != null) {
					data.add(new PublicNews(values));
				}
			}
			MainActivity.data = data;
			MainActivity.Reload();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initTask(JSONObject obj, int type) {
		String[][] keys = new String[][] {
				/* Key.KEY_PRIVATE_NEWS, */
				Key.KEY_STUDENT_INFO,
				Key.KEY_STUDENT_COURSE,
				Parser.MultiToOne(new String[][] { Key.KEY_STUDENT_CONTACT_1,
						Key.KEY_STUDENT_CONTACT_2 }),
				Key.KEY_STUDY_PROGRAMS_INFO, Key.KEY_REGISTER_SCHEDULE,
				Key.KEY_TERM_YEAR,/* Key.KEY_NOT_ACCUMULATE_CURRICULUM, */
				Key.KEY_SCHEDULE_CALENDAR, Key.KEY_SCORE, Key.KEY_SCORE_SUM,
				Key.KEY_BEHAVIOR_SCORE };
		try {
			String[] key = keys[type];
			JSONArray datas;
			if (type == 6) { // schedule calendar
				JSONObject data = obj.getJSONObject(Key.KEY_DATA);
				datas = data.getJSONArray(Key.KEY_CALENDAR);
			} else
				datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValues(js, key);
				switch (type) {// in link array of init (Connect)
				case 0: // student info
				case 1: // student course
				case 2: // student contact
					values = getValues(js, DatabaseHandler.KEY_STUDENT);
					std = Student.Merge(std, new Student((Object[]) values));
					StudentDAL.AddStudent(std);
					session.setStudentInfo(std.FullName);
					if (std.StudyProgramID != null)
						session.setStudyProgram(std.StudyProgramID);
					break;
				case 3: // study program
					values = getValuesNotNull(js, key);
					if (values != null)
						StudyProgramDAL
								.AddStudyProgram(new StudyProgram(values));
					break;
				case 4: // registered curriculum
					if (values != null)
						RegisteredCurriculumDAL
								.AddRegisteredCurriculum(new RegisteredStudyUnit(
										values));
					break;
				case 5: // current term and year study
					values = getValuesNotNull(js, key);
					if (values != null) {
						session.setCurrentTermYear(values[0], values[1]);
					}
					break;
				case 6: // schedule calendar
					if (values != null)
						ScheduleCalendarDAL
								.AddScheduleCalendar(new ScheduleCalendar(
										values));
					break;
				case 7: // score
					if (values != null)
						ScoreDAL.AddScore(new StudentScore(values));
					break;
				case 8: // score sum
					values = getValues(js, DatabaseHandler.KEY_TERM_SCORE);
					TermScore term = new TermScore(values);
					if (values != null) {
						TermScoreDAL.AddTermScore(term);
					}
					break;
				case 9: // behavior score
					String yearStudy = js.getString(key[0]);
					String termID = js.getString(key[1]);
					term = TermScoreDAL.GetTermScore(yearStudy, termID);
					if (term != null) {
						term.LastScore = js.getString(key[2]);
						term.BehaviorScoreRank = js.getString(key[3]);
						TermScoreDAL.AddTermScore(term);
					}
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initNotAccTask(JSONObject obj) {
		String[] keys = Key.KEY_NOT_ACCUMULATE_CURRICULUM;
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			NotAccumulatedCurriculumDAL.Delete();
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValues(js, keys);
				if (values == null)
					continue;
				NotAccumulatedCurriculumDAL
						.AddNotAccumulatedCurriculum(new Curriculum(values));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void loginTask(JSONObject obj) {
		DatabaseHandler.DeleteAll();
		// to reset all if last session has some fails
		session = new SessionManager(activity);
		session.createLoginSession(LoginActivity.StudentID,
				LoginActivity.Password);
	}

	private void privateNewsTask(JSONObject obj) {
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValues(js, Key.KEY_PRIVATE_NEWS);
				if (values != null) {
					PrivateNews news = new PrivateNews((String[]) values);
					PrivateNewsDAL.AddPrivateNews(news);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void studentInfoTask(JSONObject obj) {
		String studentID = session.getStudentID();
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				// save to model
				String[] value = getValues(js, DatabaseHandler.KEY_STUDENT);
				if (value == null)
					return;

				Student std = StudentDAL.GetStudent(studentID);
				std = Student.Merge(new Student((Object[]) value), std);
				StudentDAL.AddStudent(std);

				new DownloadImageTask(activity, StudentInfoFragment.image)
						.execute("http://free.clipartof.com/76-Free-Cute-Cartoon-Monkey-Clipart-Illustration.jpg");
				// values[values.length-1] --> link
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void studentChangePasswordTask() {
		Toast.makeText(activity, R.string.edit_password_noti_success,
				Toast.LENGTH_LONG).show();
		activity.onBackPressed();
	}

	private void studentLoadContactTask(JSONObject obj) {
		String[][] keys = new String[][] { Key.KEY_STUDENT_LOAD_EDITCONTACT_1,
				Key.KEY_STUDENT_LOAD_EDITCONTACT_2,
				Key.KEY_STUDENT_LOAD_EDITCONTACT_3 };
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				for (int j = 0; j < keys.length; j++) {
					String[] values = getValues(js, keys[j]);
					if (values != null)
						StudentEditInfoFragment.values[j] = values;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		StudentEditInfoFragment.LoadFragment();
	}

	private void studentEditInfoTask(JSONObject obj) {
		Toast.makeText(activity, R.string.student_edit_info_success,
				Toast.LENGTH_SHORT).show();
		activity.onBackPressed();
		session = new SessionManager(activity);
		String studentID = session.getStudentID();
		Connect connect = new Connect(activity);
		PrivateMainActivity.currentTask = connect.LoadStudentInfo(studentID,
				StudentInfoFragment.TYPE_CONTACT);
	}

	private void studyProgramTask(JSONObject obj) {
		String[] keys = Key.KEY_STUDY_PROGRAMS_INFO;
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValuesNotNull(js, keys);
				if (values == null)
					continue;
				StudyProgramDAL.AddStudyProgram(new StudyProgram(values));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void registerResultAllTask(JSONObject obj) {
		String[] keys = Key.KEY_REGISTER_SCHEDULE;
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValues(js, keys);
				if (values == null)
					continue;
				RegisteredCurriculumDAL
						.AddRegisteredCurriculum(new RegisteredStudyUnit(values));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void registerResultCurrentTask(JSONObject obj) {
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			JSONObject js = datas.getJSONObject(0);
			String term = js.getString(Key.KEY_TERM_YEAR[1]);
			String year = js.getString(Key.KEY_TERM_YEAR[0]);
			session.setCurrentTermYear(year, term);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void registerNotAccumulateTask(JSONObject obj) {
		String[] keys = Key.KEY_NOT_ACCUMULATE_CURRICULUM;
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			NotAccumulatedCurriculumDAL.Delete();
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValues(js, keys);
				if (values == null)
					continue;
				NotAccumulatedCurriculumDAL
						.AddNotAccumulatedCurriculum(new Curriculum(values));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void scheduleCalendarTask(JSONObject obj) {
		String[] keys = Key.KEY_SCHEDULE_CALENDAR;
		try {
			JSONObject data = obj.getJSONObject(Key.KEY_DATA);
			JSONArray calendar = data.getJSONArray(Key.KEY_CALENDAR);
			for (int i = 0; i < calendar.length(); i++) {
				JSONObject js = calendar.getJSONObject(i);
				String[] values = getValues(js, keys);
				if (values == null)
					continue;
				ScheduleCalendarDAL.AddScheduleCalendar(new ScheduleCalendar(
						values));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void scheduleExaminationTask(JSONObject obj) {
		String[] keys = Key.KEY_SCHEDULE_EXAMINATION;
		try {
			ArrayList<ScheduleExamination> list = new ArrayList<ScheduleExamination>();
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValues(js, keys);
				if (values == null)
					continue;
				list.add(new ScheduleExamination(values));
			}
			ExaminationFragment.ExamCurris = list;
			ExaminationFragment.LoadFragment();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void scoreTask(JSONObject obj, int type) {
		String[][] keyList = new String[][] { Key.KEY_SCORE, Key.KEY_SCORE_SUM,
				Key.KEY_BEHAVIOR_SCORE };
		String[] keys = keyList[type];
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values;
				TermScore term;
				switch (type) {
				case 0:
					values = getValues(js, keys);
					if (values != null)
						ScoreDAL.AddScore(new StudentScore(values));
					break;
				case 1: // score sum
					values = getValues(js, DatabaseHandler.KEY_TERM_SCORE);
					term = new TermScore(values);
					if (values != null) {
						TermScoreDAL.AddTermScore(term);
					}
					break;
				case 2: // behavior
					String yearStudy = js.getString(keys[0]);
					String termID = js.getString(keys[1]);
					term = TermScoreDAL.GetTermScore(yearStudy, termID);
					if (term != null) {
						term.LastScore = js.getString(keys[2]);
						term.BehaviorScoreRank = js.getString(keys[3]);
						TermScoreDAL.AddTermScore(term);
					}
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void scoreDetailsTask(JSONObject obj) {
		String[] keys = DatabaseHandler.KEY_SCORE_DETAILS;
		try {
			JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject js = datas.getJSONObject(i);
				String[] values = getValues(js, keys);
				if (values == null)
					continue;
				ScoreDetailDAL.AddScoreDetail(new ScoreDetail(values));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}