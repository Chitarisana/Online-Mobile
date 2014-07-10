package com.android.onlinehcmup.JSON;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.widget.Toast;

import com.android.onlinehcmup.PrivateNewsFragment;
import com.android.onlinehcmup.R;
import com.android.onlinehcmup.RegisterCurriculumFragment.NotAccumulatedFragment;
import com.android.onlinehcmup.RegisterCurriculumFragment.ResultCurrentFragment;
import com.android.onlinehcmup.RegisterCurriculumFragment.ResultFragment;
import com.android.onlinehcmup.ScheduleFragment;
import com.android.onlinehcmup.ScoreFragment;
import com.android.onlinehcmup.ScoreFragment.MarkInfoAdapter;
import com.android.onlinehcmup.StudentInfoFragment;
import com.android.onlinehcmup.StudyProgramFragment;
import com.android.onlinehcmup.Model.Curriculum;
import com.android.onlinehcmup.Model.RegisteredStudyUnit;
import com.android.onlinehcmup.Model.Student;
import com.android.onlinehcmup.Model.TermSchedule;
import com.android.onlinehcmup.Model.TermStudy;
import com.android.onlinehcmup.SQLite.DatabaseHandler.NotAccumulatedCurriculumDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.PrivateNewsDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.RegisteredCurriculumDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.ScheduleCalendarDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.ScoreDetailDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.StudentDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.StudyProgramDAL;
import com.android.onlinehcmup.SQLite.DatabaseHandler.TermScoreDAL;
import com.android.onlinehcmup.Support.SessionManager;

public class LocalTask {
	public static void LoadDB(int type) {
		SessionManager session = new SessionManager(Connect.activity);
		HashMap<String, String> user = session.getUserDetails();
		String studentID = user.get(SessionManager.KEY_STUDENT_ID);

		switch (type) {
		// don't need case 0 (public news): not in DB
		// don't need case 1 (initialize): don't show initialized values to view
		// don't need case 111 and 112 (initialize not accumulate curriculum and
		// score details): same above
		// don't need case 2 (login): need Internet to use this
		case 3: // private news
			PrivateNewsFragment.data = PrivateNewsDAL.GetAll();
			PrivateNewsFragment.LoadFragment();
			break;
		case 4: // student info (both 3)
			String[][] KEY = new String[][] { Key.KEY_STUDENT_INFO,
					Key.KEY_STUDENT_COURSE, Key.KEY_STUDENT_CONTACT_1,
					Key.KEY_STUDENT_CONTACT_2 };
			String[] keys = KEY[StudentInfoFragment.TYPE];
			Student std = StudentDAL.GetStudent(studentID);
			String[] values = getValues(std, std.getClass(), keys);
			StudentInfoFragment.values = values;

			if (StudentInfoFragment.TYPE == StudentInfoFragment.TYPE_CONTACT) {
				String[] key1 = KEY[KEY.length - 1];
				String[] value1 = getValues(std, std.getClass(), key1);
				StudentInfoFragment.value1 = value1;
			}
			StudentInfoFragment.LoadFragment();
			break;

		/*
		 * don't need case 5: change password fragment has nothing to show and
		 * it needs Internet to work.
		 */
		// don't need case 6, case 7: edit contact info need Internet access
		case 8: // study program
			StudyProgramFragment.Semester = StudyProgramDAL.GetAllSemester();
			StudyProgramFragment.LoadFragment();
			break;
		case 9: // result all registered curriculum
			ResultFragment.TermStudies = RegisteredCurriculumDAL
					.GetAllTermYear();
			ResultFragment.LoadFragment();
			break;
		case 10: // current registered curriculum
			HashMap<String, String> current = session.getCurrentTermYear();
			String termID = current.get(SessionManager.KEY_TERM_ID);
			String year = current.get(SessionManager.KEY_YEAR_STUDY);
			if (termID == null || year == null) {
				Toast.makeText(Connect.activity, R.string.noti_null,
						Toast.LENGTH_SHORT).show();
				break;
			}
			ArrayList<RegisteredStudyUnit> list = RegisteredCurriculumDAL
					.GetByTermYear(year, termID);
			TermStudy term = new TermStudy(year, termID, list);
			ResultCurrentFragment.current = term;
			ResultCurrentFragment.LoadFragment();
			break;
		case 11: // not accumulate curriculum
			ArrayList<Curriculum> curris = NotAccumulatedCurriculumDAL.GetAll();
			NotAccumulatedFragment.curris = curris;
			NotAccumulatedFragment.LoadFragment();
			break;
		case 12: // schedule calendar
			ScheduleCalendarHeaderTask();
			/*
			 * ScheduleFragment.scheduleCalendar = ScheduleCalendarDAL
			 * .GetAllByWeek(ScheduleFragment.startDate);
			 */
			ScheduleFragment.LoadFragment();
			break;
		// don't need case 13 (schedule examination): not in DB
		case 14: // score fragment
			ScoreFragment.TermScores = TermScoreDAL.GetAllTermScore();
			int position = session.getSelectedPosition();
			ScoreFragment.current = ScoreFragment.TermScores.get(position);
			ScoreFragment.LoadFragment(position);
			break;
		case 15: // score detail
			MarkInfoAdapter.ScoreDetails = ScoreDetailDAL
					.GetDetailByID(MarkInfoAdapter.curriID);
			MarkInfoAdapter.ViewDetails();
			break;
		}
	}

	public static void ScheduleCalendarHeaderTask() {
		ScheduleFragment.TermYears = ScheduleCalendarDAL.GetAllTermYear();
	}

	public static void ScheduleCalendarContentTask() {
		ScheduleFragment.scheduleCalendar = ScheduleCalendarDAL
				.GetAllByWeek(ScheduleFragment.startDate);
		ScheduleFragment.LoadContentCalendar();
	}

	public static String GetStartDate(String y, String t) {
		return ScheduleCalendarDAL.GetStartDate(y, t);
	}

	public static ArrayList<TermSchedule> GetTermSchedule() {
		return ScheduleCalendarDAL.GetAllTermYear();
	}

	private static String[] getValues(Object obj, Class<?> cls, String[] key) {
		String[] values = new String[key.length];
		for (int i = 0; i < key.length; i++) {
			try {
				Field f = cls.getField(key[i]);
				values[i] = (String) (f.get(obj));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				continue;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				continue;
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				continue;
			}
		}
		return values;
	}
}
