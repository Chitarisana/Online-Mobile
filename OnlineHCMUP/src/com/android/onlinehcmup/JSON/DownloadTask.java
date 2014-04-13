package com.android.onlinehcmup.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.android.onlinehcmup.LoginActivity;
import com.android.onlinehcmup.MainActivity;
import com.android.onlinehcmup.NewsList;
import com.android.onlinehcmup.PrivateMainActivity;
import com.android.onlinehcmup.PrivateNewsFragment;
import com.android.onlinehcmup.R;
import com.android.onlinehcmup.RegisterCurriculumFragment.DisaccumulatedFragment;
import com.android.onlinehcmup.RegisterCurriculumFragment.ResultCurrentFragment;
import com.android.onlinehcmup.RegisterCurriculumFragment.ResultFragment;
import com.android.onlinehcmup.StudentEditInfoFragment;
import com.android.onlinehcmup.StudentInfoFragment;
import com.android.onlinehcmup.StudyProgramFragment;
import com.android.onlinehcmup.Model.Curriculum;
import com.android.onlinehcmup.Model.PrivateNews;
import com.android.onlinehcmup.Model.PublicNews;
import com.android.onlinehcmup.Model.RegisterScheduleStudyUnit;
import com.android.onlinehcmup.Model.Semester;
import com.android.onlinehcmup.Model.StudyProgram;
import com.android.onlinehcmup.Model.TermStudy;
import com.android.onlinehcmup.Support.DialogManager;
import com.android.onlinehcmup.Support.DownloadImageTask;
import com.android.onlinehcmup.Support.SessionManager;
import com.android.onlinehcmup.Support.StaticTAG;

public class DownloadTask extends AsyncTask<String, Void, String> {
	Activity activity;
	ProgressDialog dialog;
	String studentID = "";
	SessionManager session;

	protected void onPreExecute() {
		activity = Connect.activity;
		dialog = new ProgressDialog(activity, R.style.ProgressDialogCustom);
		dialog.setTitle(R.string.app_name);
		dialog.setMessage(activity.getResources().getString(
				R.string.noti_loading));
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.show();
		dialog.setCancelable(false);
	}

	@Override
	protected String doInBackground(String... urls) {
		InputStream is = null;
		try {
			URL url = new URL(urls[0]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
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
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		dialog.dismiss();
		session = new SessionManager(StaticTAG.ACTIVITY);
		if (result == null) {
			Toast.makeText(activity,
					activity.getResources().getString(R.string.error_server),
					Toast.LENGTH_SHORT).show();
			if (Connect.TYPE == 15)
				test();
			return;
		}
		switch (Connect.TYPE) {
		case -1: // TYPE_INIT
			initTask(result);
			break;
		case 0: // TYPE_LOGIN
			loginTask(result);
			break;
		case 1: // TYPE_PRIVATE_NEWS
			privateNewsTask(result);
			break;
		case 2: // TYPE_STUDENT_INFO
			studentInfoTask(result);
			break;
		case 3: // TYPE_STUDENT_CHANGE_PASSWORD
			studentChangePasswordTask(result);
			break;
		case 4: // TYPE_LOAD_CONTACT --> EDIT CONTACT
			// load contact at EditContactFragment
			studentLoadContactTask(result);
			break;
		case 5: // TYPE_STUDENT_EDIT_INFO
			studentEditInfoTask(result);
			break;
		case 6: // TYPE_STUDY_PROGRAM
			studyProgramTask(result);
			break;
		case 7: // TYPE_REGISTER_RESULT_ALL
			registerResultAll(result);
			break;
		case 8: // TYPE_REGISTER_RESULT_CURRENT
			registerResultCurrent(result);
			break;
		case 9: // TYPE_REGISTER_NOT_ACCUMULATE
			registerNotAccumulate(result);
			break;

		case 15: // TYPE_PUBLIC_NEWS
			publicNewsTask(result);
			break;
		}
	}

	private void initTask(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = StudentInfoFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						DialogManager.showAlertDialog(activity, "Thông báo",
								error, false);
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					for (int i = 0; i < datas.length(); i++) {
						JSONObject js = datas.getJSONObject(i);
						if (js.has(Key.KEY_STUDENT_INFO_FULLNAME)) {
							String studentName = js
									.getString(Key.KEY_STUDENT_INFO_FULLNAME);
							session.setStudentInfo(studentName);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void loginTask(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = LoginActivity.activity;
				if (stt == 1) {
					session.createLoginSession(LoginActivity.StudentID,
							LoginActivity.Password);
					studentID = LoginActivity.StudentID;

					// Starting PrivateMainActivity
					Intent i = new Intent(activity, PrivateMainActivity.class);
					activity.startActivity(i);
					activity.finish();
				} else {
					DialogManager.showAlertDialog(
							activity,
							activity.getResources().getString(
									R.string.login_fail_noti_title),
							activity.getResources().getString(
									R.string.login_fail_noti_detail1), false);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void privateNewsTask(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = PrivateNewsFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						DialogManager.showAlertDialog(activity, "Thông báo",
								error, false);
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					for (int i = 0; i < datas.length(); i++) {
						JSONObject js = datas.getJSONObject(i);
						String[] values = new String[Key.KEY_PRIVATE_NEWS.length];
						for (int j = 0; j < Key.KEY_PRIVATE_NEWS.length; j++) {
							if (js.has(Key.KEY_PRIVATE_NEWS[j])) {
								values[j] = js
										.getString(Key.KEY_PRIVATE_NEWS[j]);
							}
						}
						PrivateNewsFragment.data.add(new PrivateNews(values[0],
								values[1], values[2], values[3], values[4],
								values[5]));
						NewsList fragment = new NewsList();
						Bundle args = new Bundle();
						args.putInt(NewsList.KEY_TYPE, NewsList.KEY_PRIVATE);
						fragment.setArguments(args);

						activity.getFragmentManager()
								.beginTransaction()
								.replace(PrivateNewsFragment.listGroup.getId(),
										fragment,
										StaticTAG.TAG_PRIVATE_NEWS_LIST)
								.commit();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Connect connect = new Connect(activity);
		connect.Init(LoginActivity.StudentID);
	}

	private void studentInfoTask(String result) {
		String[][] KEY = new String[][] { Key.KEY_STUDENT_INFO,
				Key.KEY_STUDENT_COURSE, Key.KEY_STUDENT_CONTACT_1,
				Key.KEY_STUDENT_CONTACT_2 };
		String[] keys = KEY[StudentInfoFragment.TYPE];
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = StudentInfoFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						DialogManager.showAlertDialog(activity, "Thông báo",
								error, false);
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					for (int i = 0; i < datas.length(); i++) {
						JSONObject js = datas.getJSONObject(i);
						String[] values = new String[keys.length];
						for (int j = 0; j < keys.length; j++) {
							if (js.has(keys[j])) {
								values[j] = js.getString(keys[j]);
							}
							if (keys[j] == Key.KEY_STUDENT_INFO_GENDER) {
								values[j] = (values[j].compareTo("True") == 0) ? "Nam"
										: "Nữ";
							} else if (keys[j] == Key.KEY_STUDENT_INFO_PARTY) {
								values[j] = (values[j].compareTo("1") == 0) ? "Có tham gia"
										: "";
							}
						}
						if (js.has(Key.KEY_STUDENT_INFO_FULLNAME)) {
							String studentName = js
									.getString(Key.KEY_STUDENT_INFO_FULLNAME);
							session.setStudentInfo(studentName);
						}
						new DownloadImageTask(activity,
								StudentInfoFragment.image)
								.execute("http://free.clipartof.com/76-Free-Cute-Cartoon-Monkey-Clipart-Illustration.jpg");
						// values[values.length-1] --> link
						StudentInfoFragment.values = values;

						if (StudentInfoFragment.TYPE == StudentInfoFragment.KEY_TYPE_CONTACT) {
							String[] key1 = KEY[KEY.length - 1];
							String[] value1 = new String[key1.length];
							for (int j = 0; j < key1.length; j++) {
								if (js.has(key1[j])) {
									value1[j] = js.getString(key1[j]);
								}
							}
							StudentInfoFragment.value1 = value1;
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		StudentInfoFragment.LoadFragment();
	}

	private void studentChangePasswordTask(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = PrivateNewsFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						Toast.makeText(activity, error, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					JSONObject js = obj.getJSONObject(Key.KEY_DATA);
					String[] values = new String[Key.KEY_CHANGE_PASSWORD.length];
					for (int j = 0; j < Key.KEY_CHANGE_PASSWORD.length; j++) {
						if (js.has(Key.KEY_CHANGE_PASSWORD[j])) {
							values[j] = js
									.getString(Key.KEY_CHANGE_PASSWORD[j]);
						}
					}
					Toast.makeText(
							activity,
							activity.getResources().getString(
									R.string.edit_password_noti_success),
							Toast.LENGTH_SHORT).show();
					// update pass to Local DB here!!!
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void studentLoadContactTask(String result) {
		String[][] keys = new String[][] { Key.KEY_STUDENT_LOAD_EDITCONTACT_1,
				Key.KEY_STUDENT_LOAD_EDITCONTACT_2,
				Key.KEY_STUDENT_LOAD_EDITCONTACT_3 };
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = PrivateNewsFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						Toast.makeText(activity, error, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					for (int i = 0; i < datas.length(); i++) {
						JSONObject js = datas.getJSONObject(i);
						for (int j = 0; j < keys.length; j++) {
							String[] values = new String[keys[j].length];
							for (int k = 0; k < keys[j].length; k++) {
								if (js.has(keys[j][k])) {
									values[k] = js.getString(keys[j][k]);
								}
							}
							StudentEditInfoFragment.valuesLoad[j] = values;
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		StudentEditInfoFragment.LoadFragment();
	}

	private void studentEditInfoTask(String result) {
		/*
		 * String[][] keyList = new String[][] { Key.KEY_STUDENT_EDIT_CONTACT_1,
		 * Key.KEY_STUDENT_EDIT_CONTACT_2, Key.KEY_STUDENT_EDIT_CONTACT_3 };
		 */
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = PrivateNewsFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						Toast.makeText(activity, error, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(activity,
							R.string.student_edit_info_success,
							Toast.LENGTH_SHORT).show();
					// load new data and save to db here
					activity.onBackPressed();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void studyProgramTask(String result) {
		String[] keys = Key.KEY_STUDY_PROGRAMS_INFO;
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = PrivateNewsFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						Toast.makeText(activity, error, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					StudyProgram[] stdPrg = new StudyProgram[datas.length()];
					ArrayList<Semester> sems = new ArrayList<Semester>();
					for (int i = 0; i < datas.length(); i++) {
						JSONObject js = datas.getJSONObject(i);
						String[] values = new String[keys.length];
						for (int j = 0; j < keys.length; j++) {
							if (js.has(keys[j])) {
								values[j] = js.getString(keys[j]);
							}
						}
						ArrayList<StudyProgram> list = new ArrayList<StudyProgram>();
						stdPrg[i] = new StudyProgram(studentID, values[0],
								values[1], values[2], values[3], values[4],
								values[5], values[6],
								Double.parseDouble(values[7]));
						list.add(stdPrg[i]);
						Semester sem = new Semester(
								js.getString(Key.KEY_STUDY_PROGRAMS_INFO_SEMESTERID),
								js.getString(Key.KEY_STUDY_PROGRAMS_INFO_SEMESTERNAME),
								list);
						int position = positionArray(sems, sem);
						if (sems.size() == 0 || position == -1)
							sems.add(sem);
						else
							sems.get(position).StudyProgram.add(stdPrg[i]);
					}
					StudyProgramFragment.Semester = sems
							.toArray(new Semester[sems.size()]);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		StudyProgramFragment.LoadFragment();
	}

	private int positionArray(ArrayList<Semester> target, Semester values) {
		for (int i = 0; i < target.size(); i++)
			if (values.SemesterID.compareTo(target.get(i).SemesterID) == 0)
				return i;
		return -1;
	}

	private int positionArray(ArrayList<TermStudy> target, TermStudy values) {
		for (int i = 0; i < target.size(); i++)
			if (values.TermID.compareTo(target.get(i).TermID) == 0
					&& values.YearStudy.compareTo(target.get(i).YearStudy) == 0)
				return i;
		return -1;
	}

	private void registerResultAll(String result) {
		String[] keys = Key.KEY_REGISTER_SCHEDULE;
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = PrivateNewsFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						Toast.makeText(activity, error, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					RegisterScheduleStudyUnit[] studyUnits = new RegisterScheduleStudyUnit[datas
							.length()];
					ArrayList<TermStudy> termStudies = new ArrayList<TermStudy>();
					for (int i = 0; i < datas.length(); i++) {
						JSONObject js = datas.getJSONObject(i);
						String[] values = new String[keys.length];
						for (int j = 0; j < keys.length; j++) {
							if (js.has(keys[j])) {
								values[j] = js.getString(keys[j]);
							}
						}
						ArrayList<RegisterScheduleStudyUnit> list = new ArrayList<RegisterScheduleStudyUnit>();
						studyUnits[i] = new RegisterScheduleStudyUnit(
								studentID, values[0], values[1], values[2],
								values[3], values[4], values[5], values[6],
								values[7], values[8], values[9], values[10]);
						list.add(studyUnits[i]);
						TermStudy term = new TermStudy(
								js.getString(Key.KEY_REGISTER_SCHEDULE_YEAR_STUDY),
								js.getString(Key.KEY_REGISTER_SCHEDULE_TERM),
								0.0, list);
						int position = positionArray(termStudies, term);
						if (termStudies.size() == 0 || position == -1)
							termStudies.add(term);
						else
							termStudies.get(position).StudyUnit
									.add(studyUnits[i]);
					}
					Collections.sort(termStudies, new Comparator<TermStudy>() {
						public int compare(TermStudy a, TermStudy b) {
							int com = a.YearStudy.compareTo(b.YearStudy);
							if (com == 0) {
								com = a.TermID.compareTo(b.TermID);
							}
							return com;
						}
					});

					ResultFragment.TermStudies = termStudies
							.toArray(new TermStudy[termStudies.size()]);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResultFragment.LoadFragment();
	}

	private void registerResultCurrent(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = PrivateMainActivity.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						Toast.makeText(activity, error, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					JSONObject js = datas.getJSONObject(0);

					ResultCurrentFragment.currentTermID = js
							.getString(Key.KEY_REGISTER_SCHEDULE_TERM);
					ResultCurrentFragment.currentYearStudy = js
							.getString(Key.KEY_REGISTER_SCHEDULE_YEAR_STUDY);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResultCurrentFragment.LoadFragment();
	}

	private void registerNotAccumulate(String result) {
		String[] keys = Key.KEY_NOT_ACCUMULATE_CURRICULUM;
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = PrivateNewsFragment.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						Toast.makeText(activity, error, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					ArrayList<Curriculum> curris = new ArrayList<Curriculum>();
					for (int i = 0; i < datas.length(); i++) {
						JSONObject js = datas.getJSONObject(i);
						String[] values = new String[keys.length];
						for (int j = 0; j < keys.length; j++) {
							if (js.has(keys[j])) {
								values[j] = js.getString(keys[j]);
							}
						}
						curris.add(new Curriculum(values[0], values[1],
								values[2], Double.parseDouble(values[3])));
					}
					DisaccumulatedFragment.curris = curris;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DisaccumulatedFragment.LoadFragment();
	}

	private void publicNewsTask(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Key.KEY_STATUS)) {
				String status = obj.getString(Key.KEY_STATUS);
				int stt = Integer.parseInt(status);
				Activity activity = MainActivity.activity;
				if (stt == 0) {
					if (obj.has(Key.KEY_ERRORS)) {
						String error = obj.getString(Key.KEY_ERRORS);
						DialogManager.showAlertDialog(activity, "Thông báo",
								error, false);
					}
				} else {
					JSONArray datas = obj.getJSONArray(Key.KEY_DATA);
					for (int i = 0; i < datas.length(); i++) {
						JSONObject js = datas.getJSONObject(i);
						String[] values = new String[Key.KEY_PUBLIC_NEWS.length];
						for (int j = 0; j < Key.KEY_PUBLIC_NEWS.length; j++) {
							if (js.has(Key.KEY_PUBLIC_NEWS[j])) {
								values[j] = js
										.getString(Key.KEY_PUBLIC_NEWS[j]);
							}
						}
						MainActivity.data.add(new PublicNews(values[0],
								values[1], values[2], values[3], values[4]));

						NewsList fragment = new NewsList();
						Bundle args = new Bundle();
						args.putInt(NewsList.KEY_TYPE, NewsList.KEY_PUBLIC);
						fragment.setArguments(args);

						activity.getFragmentManager()
								.beginTransaction()
								.replace(MainActivity.listGroup.getId(),
										fragment,
										StaticTAG.TAG_PUBLIC_NEWS_LIST)
								.commit();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void test() {
		MainActivity.data
				.add(new PublicNews(
						"Kế hoạch thăm đoàn thực tập năm 2014",
						"10/2/2014",
						"",
						"Xin bấm vào link để xem toàn bộ kế hoạch thăm đoàn https://drive.google.com/file/d/0BxzNv4aPWJ4mbm9SdFg4VkRWRHUzMkZpbDhaUmMyZTFKeUZV/edit",
						""));
		MainActivity.data
				.add(new PublicNews(
						"test 1 cái link",
						"2/3/2014",
						"",
						"Link nak:<br><p><a href=\"http://google.com\">click here</a> để xem chi tiết nhé!!</p>",
						""));

		NewsList fragment = new NewsList();
		Bundle args = new Bundle();
		args.putInt(NewsList.KEY_TYPE, NewsList.KEY_PUBLIC);
		fragment.setArguments(args);

		activity.getFragmentManager()
				.beginTransaction()
				.replace(MainActivity.listGroup.getId(), fragment,
						StaticTAG.TAG_PUBLIC_NEWS_LIST).commit();
	}
}