package com.android.onlinehcmup.SQLite;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.onlinehcmup.JSON.Key;
import com.android.onlinehcmup.Model.Curriculum;
import com.android.onlinehcmup.Model.PrivateNews;
import com.android.onlinehcmup.Model.RegisteredStudyUnit;
import com.android.onlinehcmup.Model.ScheduleCalendar;
import com.android.onlinehcmup.Model.ScoreDetail;
import com.android.onlinehcmup.Model.Semester;
import com.android.onlinehcmup.Model.Student;
import com.android.onlinehcmup.Model.StudentScore;
import com.android.onlinehcmup.Model.StudyProgram;
import com.android.onlinehcmup.Model.TermSchedule;
import com.android.onlinehcmup.Model.TermScore;
import com.android.onlinehcmup.Model.TermStudy;
import com.android.onlinehcmup.Support.Parser;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "DB_Online.db";
	private static final String TABLE_STUDENT = "Student";
	private static final String TABLE_PRIVATE_NEWS = "PrivateNews";
	private static final String TABLE_STUDY_PROGRAM = "StudyProgram";
	private static final String TABLE_REGISTERED = "Registered";
	private static final String TABLE_DISACCUMULATE = "Disaccumulate";
	private static final String TABLE_SCHEDULE_CALENDAR = "Calendar";
	private static final String TABLE_SCORE = "Score";
	private static final String TABLE_TERM_SCORE = "TermScore";
	private static final String TABLE_SCORE_DETAILS = "ScoreDetails";
	// hem add lich thi

	public static String[] KEY_PRIVATE_NEWS, KEY_STUDENT, KEY_STUDY_PROGRAM,
			KEY_REGISTERED, KEY_DISACCUMULATE, KEY_SCHEDULE_CALENDAR,
			KEY_SCORE, KEY_TERM_SCORE, KEY_SCORE_DETAILS;

	private static String[] tableList = new String[] { TABLE_PRIVATE_NEWS,
			TABLE_STUDENT, TABLE_STUDY_PROGRAM, TABLE_REGISTERED,
			TABLE_DISACCUMULATE, TABLE_SCHEDULE_CALENDAR, TABLE_SCORE,
			TABLE_TERM_SCORE, TABLE_SCORE_DETAILS };
	private String[][] keyList;

	static DatabaseHandler thisHandler;

	private String createSQL(String table, String[] keys) {
		String str = "CREATE TABLE " + table + "(";
		str += keys[0] + " TEXT PRIMARY KEY ON CONFLICT REPLACE, ";
		for (int i = 1; i < keys.length; i++) {
			str += keys[i] + " TEXT, ";
		}
		str = str.substring(0, str.length() - 2);
		str += ")";
		return str;
	}

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		thisHandler = this;
		KEY_PRIVATE_NEWS = Key.KEY_PRIVATE_NEWS;
		KEY_STUDENT = Parser.MultiToOne(new String[][] { Key.KEY_STUDENT_INFO,
				Key.KEY_STUDENT_COURSE, Key.KEY_STUDENT_CONTACT_1,
				Key.KEY_STUDENT_CONTACT_2 });
		KEY_STUDY_PROGRAM = Key.KEY_STUDY_PROGRAMS_INFO;
		KEY_REGISTERED = addToList("TermScheduleID", Key.KEY_REGISTER_SCHEDULE);
		KEY_DISACCUMULATE = Key.KEY_NOT_ACCUMULATE_CURRICULUM;
		KEY_SCHEDULE_CALENDAR = addToList("CalendarStudyUnitID",
				Key.KEY_SCHEDULE_CALENDAR);
		KEY_SCORE = Key.KEY_SCORE;
		KEY_TERM_SCORE = Key.KEY_TERM_SCORE;
		KEY_SCORE_DETAILS = addToList("ScoreDetailID", Key.KEY_SCORE_DETAILS);
		keyList = new String[][] { KEY_PRIVATE_NEWS, KEY_STUDENT,
				KEY_STUDY_PROGRAM, KEY_REGISTERED, KEY_DISACCUMULATE,
				KEY_SCHEDULE_CALENDAR, KEY_SCORE, KEY_TERM_SCORE,
				KEY_SCORE_DETAILS };
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// create tables
		for (int i = 0; i < tableList.length; i++)
			db.execSQL(createSQL(tableList[i], keyList[i]));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		for (int i = 0; i < tableList.length; i++)
			db.execSQL("DROP TABLE IF EXISTS " + tableList[i]);
		onCreate(db);
	}

	// use when logout --> delete all local DB
	public static void DeleteAll() {
		SQLiteDatabase db = thisHandler.getReadableDatabase();
		for (int i = 0; i < tableList.length; i++)
			db.delete(tableList[i], null, null);
		db.close();
	}

	public static class PrivateNewsDAL {
		public static Boolean AddPrivateNews(PrivateNews news) {
			return insert(news, PrivateNews.class, KEY_PRIVATE_NEWS,
					TABLE_PRIVATE_NEWS);
		}

		public static ArrayList<PrivateNews> GetAll() {
			ArrayList<PrivateNews> list = new ArrayList<PrivateNews>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();

			String selectQuery = "SELECT * FROM " + TABLE_PRIVATE_NEWS;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new PrivateNews(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}
	}

	public static class StudentDAL {
		public static Boolean AddStudent(Student student) {
			return insert(student, Student.class, KEY_STUDENT, TABLE_STUDENT);
		}

		public static Student GetStudent(String studentID) {
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(TABLE_STUDENT, KEY_STUDENT, KEY_STUDENT[0]
					+ "=?", new String[] { String.valueOf(studentID) }, null,
					null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
			return new Student((Object[]) cursor2Array(cursor));
		}
	}

	public static class StudyProgramDAL {
		public static Boolean AddStudyProgram(StudyProgram stdPrg) {
			return insert(stdPrg, StudyProgram.class, KEY_STUDY_PROGRAM,
					TABLE_STUDY_PROGRAM);
		}

		public static ArrayList<StudyProgram> GetAll() {
			ArrayList<StudyProgram> list = new ArrayList<StudyProgram>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			String selectQuery = "SELECT * FROM " + TABLE_STUDY_PROGRAM;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new StudyProgram(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}

		public static ArrayList<Semester> GetAllSemester() {
			ArrayList<Semester> list = new ArrayList<Semester>();
			String[] keys = Key.KEY_SEMESTER;
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(true, TABLE_STUDY_PROGRAM, keys, null,
					null, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					String semID = cursor.getString(0);
					list.add(new Semester(semID, cursor.getString(1),
							GetBySemester(semID)));
				} while (cursor.moveToNext());
			}
			return list;
		}

		public static ArrayList<StudyProgram> GetBySemester(String semID) {
			ArrayList<StudyProgram> list = new ArrayList<StudyProgram>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(TABLE_STUDY_PROGRAM, KEY_STUDY_PROGRAM,
					Key.KEY_SEMESTER[0] + "=?",
					new String[] { String.valueOf(semID) }, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new StudyProgram(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}
	}

	public static class RegisteredCurriculumDAL { // registerED
		public static Boolean AddRegisteredCurriculum(RegisteredStudyUnit obj) {
			return insert(obj, RegisteredStudyUnit.class, KEY_REGISTERED,
					TABLE_REGISTERED);
		}

		public static ArrayList<TermStudy> GetAllTermYear() {
			ArrayList<TermStudy> list = new ArrayList<TermStudy>();
			String[] keys = Key.KEY_TERM_YEAR;
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(true, TABLE_REGISTERED, keys, null, null,
					null, null, keys[0] + " ASC, " + keys[1] + " ASC", null);
			if (cursor.moveToFirst()) {
				do {
					String year = cursor.getString(0);
					String termID = cursor.getString(1);
					list.add(new TermStudy(year, termID, GetByTermYear(year,
							termID)));
				} while (cursor.moveToNext());
			}
			return list;
		}

		public static ArrayList<RegisteredStudyUnit> GetByTermYear(String year,
				String termID) {
			ArrayList<RegisteredStudyUnit> list = new ArrayList<RegisteredStudyUnit>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(
					TABLE_REGISTERED,
					Key.KEY_REGISTER_SCHEDULE,// don't get PK
					Key.KEY_TERM_YEAR[0] + "=?" + " AND "
							+ Key.KEY_TERM_YEAR[1] + "=?", new String[] {
							String.valueOf(year), String.valueOf(termID) },
					null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new RegisteredStudyUnit(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}
	}

	public static class NotAccumulatedCurriculumDAL {
		public static Boolean AddNotAccumulatedCurriculum(Curriculum obj) {
			return insert(obj, Curriculum.class, KEY_DISACCUMULATE,
					TABLE_DISACCUMULATE);
		}

		public static ArrayList<Curriculum> GetAll() {
			ArrayList<Curriculum> list = new ArrayList<Curriculum>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			String selectQuery = "SELECT * FROM " + TABLE_DISACCUMULATE;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new Curriculum(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}

		/*
		 * because after student registered a curriculum, this table will lost a
		 * row, so delete all and add again to refresh is better than finding
		 * which rows had been lost then delete it because of the conflict to
		 * server
		 */
		public static void Delete() {
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			db.delete(TABLE_DISACCUMULATE, null, null);
			db.close();
		}
	}

	public static class ScheduleCalendarDAL {
		public static Boolean AddScheduleCalendar(ScheduleCalendar obj) {
			return insert(obj, ScheduleCalendar.class, KEY_SCHEDULE_CALENDAR,
					TABLE_SCHEDULE_CALENDAR);
		}

		public static ArrayList<ScheduleCalendar> GetAllByWeek(String startDate) {
			ArrayList<ScheduleCalendar> list = new ArrayList<ScheduleCalendar>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(
					TABLE_SCHEDULE_CALENDAR,
					Key.KEY_SCHEDULE_CALENDAR, // don't get PK
					Key.KEY_SCHEDULE_CALENDAR_STARTDATE + "=?",
					new String[] { String.valueOf(startDate) }, null, null,
					Key.KEY_SCHEDULE_CALENDAR_STARTDATE + " DESC", null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new ScheduleCalendar(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}

		public static ArrayList<String> GetAllYear() {
			ArrayList<String> list = new ArrayList<String>();
			String[] keys = new String[] { Key.KEY_TERM_YEAR[0] };
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(true, TABLE_SCHEDULE_CALENDAR, keys, null,
					null, null, null, keys[0] + " DESC ", null);
			// sort theo year --> term
			if (cursor.moveToFirst()) {
				do {
					list.add(cursor.getString(cursor
							.getColumnIndex(Key.KEY_TERM_YEAR[0])));
				} while (cursor.moveToNext());
			}
			return list;
		}

		public static ArrayList<String> GetAllTerm() {
			ArrayList<String> list = new ArrayList<String>();
			String[] keys = new String[] { Key.KEY_TERM_YEAR[1] };
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(true, TABLE_SCHEDULE_CALENDAR, keys, null,
					null, null, null, keys[0] + " DESC ", null);
			if (cursor.moveToFirst()) {
				do {
					list.add(cursor.getString(cursor
							.getColumnIndex(Key.KEY_TERM_YEAR[1])));
				} while (cursor.moveToNext());
			}
			return list;
		}

		public static ArrayList<TermSchedule> GetAllTermYear() {
			ArrayList<TermSchedule> list = new ArrayList<TermSchedule>();
			String[] keys = Key.KEY_TERM_YEAR;
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(true, TABLE_SCHEDULE_CALENDAR, keys, null,
					null, null, null, keys[0] + " DESC, " + keys[1] + " DESC ",
					null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new TermSchedule(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}

		public static String GetStartDate(String year, String termID) {
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(
					TABLE_SCHEDULE_CALENDAR,
					Key.KEY_SCHEDULE_CALENDAR,// don't get PK
					Key.KEY_TERM_YEAR[0] + "=?" + " AND "
							+ Key.KEY_TERM_YEAR[1] + "=?", new String[] {
							String.valueOf(year), String.valueOf(termID) },
					null, null, Key.KEY_TERM_YEAR[0] + " ASC, "
							+ Key.KEY_TERM_YEAR[1] + " ASC", null);
			if (cursor.moveToFirst())
				return cursor.getString(cursor
						.getColumnIndex(Key.KEY_SCHEDULE_CALENDAR_STARTDATE));
			else
				return null;
		}

		public static ArrayList<ScheduleCalendar> GetByTermYear(String year,
				String termID) {
			ArrayList<ScheduleCalendar> list = new ArrayList<ScheduleCalendar>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(
					TABLE_SCHEDULE_CALENDAR,
					Key.KEY_SCHEDULE_CALENDAR,// don't get PK
					Key.KEY_TERM_YEAR[0] + "=?" + " AND "
							+ Key.KEY_TERM_YEAR[1] + "=?", new String[] {
							String.valueOf(year), String.valueOf(termID) },
					null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new ScheduleCalendar(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}
	}

	public static class ScoreDAL {
		public static Boolean AddScore(StudentScore obj) {
			return insert(obj, StudentScore.class, KEY_SCORE, TABLE_SCORE);
		}

		public static ArrayList<StudentScore> GetByTermYear(String year,
				String termID) {
			ArrayList<StudentScore> list = new ArrayList<StudentScore>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(TABLE_SCORE, KEY_SCORE,
					Key.KEY_TERM_YEAR[0] + "=?" + " AND "
							+ Key.KEY_TERM_YEAR[1] + "=?", new String[] {
							String.valueOf(year), String.valueOf(termID) },
					null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					list.add(new StudentScore(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}

		public static ArrayList<String> GetAllStudyUnitID() {
			ArrayList<String> list = new ArrayList<String>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			String selectQuery = "SELECT " + KEY_SCORE[0] + " FROM "
					+ TABLE_SCORE;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					list.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
			return list;
		}
	}

	public static class TermScoreDAL {
		public static Boolean AddTermScore(TermScore obj) {
			return insert(obj, TermScore.class, KEY_TERM_SCORE,
					TABLE_TERM_SCORE);
		}

		public static TermScore GetTermScore(String yearStudy, String termID) {
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(TABLE_TERM_SCORE, KEY_TERM_SCORE,
					KEY_TERM_SCORE[1] + "=?" + " AND " + KEY_TERM_SCORE[2]
							+ "=?", new String[] { String.valueOf(yearStudy),
							String.valueOf(termID) }, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
			if (cursor.getCount() > 0)
				return new TermScore(yearStudy, termID, cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7),
						ScoreDAL.GetByTermYear(yearStudy, termID));
			return null;
		}

		public static ArrayList<TermScore> GetAllTermScore() {
			ArrayList<TermScore> list = new ArrayList<TermScore>();
			String[] keys = KEY_TERM_SCORE;
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(true, TABLE_TERM_SCORE, keys, null, null,
					null, null, keys[0] + " DESC", null); // sort theo pk key
			if (cursor.moveToFirst()) {
				do {
					String year = cursor.getString(1);
					String termID = cursor.getString(2);
					list.add(new TermScore(year, termID, cursor.getString(3),
							cursor.getString(4), cursor.getString(5), cursor
									.getString(6), cursor.getString(7),
							ScoreDAL.GetByTermYear(year, termID)));
				} while (cursor.moveToNext());
			}
			return list;
		}
	}

	public static class ScoreDetailDAL {
		public static Boolean AddScoreDetail(ScoreDetail obj) {
			return insert(obj, ScoreDetail.class, KEY_SCORE_DETAILS,
					TABLE_SCORE_DETAILS);
		}

		public static ArrayList<ScoreDetail> GetDetailByID(String curriID) {
			ArrayList<ScoreDetail> list = new ArrayList<ScoreDetail>();
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			Cursor cursor = db.query(TABLE_SCORE_DETAILS, KEY_SCORE_DETAILS,
					KEY_SCORE_DETAILS[1] + "=?",
					new String[] { String.valueOf(curriID) }, null, null, null,
					null);
			if (cursor.getCount() == 0)
				return null;
			if (cursor.moveToFirst()) {
				do {
					list.add(new ScoreDetail(cursor2Array(cursor)));
				} while (cursor.moveToNext());
			}
			return list;
		}
	}

	private static Boolean insert(Object obj, Class<?> cls, String[] key,
			String table) {
		SQLiteDatabase db = thisHandler.getWritableDatabase();
		ContentValues values = new ContentValues();
		// Class<?> cls = obj.getClass(); // co khi nao ra Object hem??
		for (int i = 0; i < key.length; i++) {
			try {
				Field f = cls.getField(key[i]);
				values.put(key[i], (String) (f.get(obj) + ""));
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
		db.beginTransaction();
		try {
			long result = db.insert(table, null, values);
			db.setTransactionSuccessful();
			if (result == -1)
				return false;
		} finally {
			db.endTransaction();
		}
		db.close();
		return true;
	}

	private static String[] cursor2Array(Cursor cursor) {
		int count = cursor.getColumnCount();
		String[] str = new String[count];
		for (int i = 0; i < count; i++) {
			str[i] = cursor.getString(i);
		}
		return str;
	}

	private static String[] addToList(String id, String[] list) {
		String[] result = new String[list.length + 1];
		result[0] = id;
		for (int i = 1; i < result.length; i++) {
			result[i] = list[i - 1];
		}
		return result;
	}
}