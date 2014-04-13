package com.android.onlinehcmup.SQLite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.onlinehcmup.JSON.Key;
import com.android.onlinehcmup.Model.Student;
import com.android.onlinehcmup.Support.Reflect;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "DB_Online";
	private static final String TABLE_STUDENT = "Student";
	private static final String TABLE_PRIVATE_NEWS = "PrivateNews";
	private static final String TABLE_STUDY_PROGRAM = "StudyProgram";
	private static final String TABLE_PUBLIC_NEWS = "PublicNews";

	public static String[] KEY_STUDENT, KEY_PUBLIC_NEWS, KEY_PRIVATE_NEWS,
			KEY_STUDY_PROGRAM;

	private static String CREATE_STUDENT_TABLE, CREATE_PUBLIC_NEWS_TABLE,
			CREATE_PRIVATE_NEWS_TABLE, CREATE_STUDY_PROGRAM_TABLE;

	private String[] tableList = new String[] { TABLE_STUDENT,
			TABLE_PRIVATE_NEWS, TABLE_STUDY_PROGRAM };
	private String[][] keyList;

	DatabaseHandler thisHandler = this;

	private String[] multiToOne(String[][] values) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values[i].length; j++)
				result.add(values[i][j]);
		return result.toArray(new String[result.size()]);
	}

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
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		KEY_STUDENT = multiToOne(new String[][] { Key.KEY_STUDENT_INFO,
				Key.KEY_STUDENT_COURSE, Key.KEY_STUDENT_CONTACT_1,
				Key.KEY_STUDENT_CONTACT_2 });
		// KEY_PUBLIC_NEWS=Key.KEY_PUBLIC_NEWS;
		KEY_PRIVATE_NEWS = Key.KEY_PRIVATE_NEWS;
		KEY_STUDY_PROGRAM = Key.KEY_STUDY_PROGRAMS_INFO;

		keyList = new String[][] { KEY_STUDENT, KEY_PRIVATE_NEWS,
				KEY_STUDY_PROGRAM };

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

	public class StudentDAL {
		public void AddStudent(Student student) {
			SQLiteDatabase db = thisHandler.getReadableDatabase();
			ContentValues values = new ContentValues();
			Field [] flds= Reflect.reflectField("Student");
			for (int i = 0; i < KEY_STUDENT.length; i++)
				values.put(KEY_STUDENT[i], flds[i].getName());

			db.beginTransaction();
			try {
				db.insert(TABLE_STUDENT, null, values);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
			db.close();
		}
	}
}