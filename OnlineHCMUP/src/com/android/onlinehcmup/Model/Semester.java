package com.android.onlinehcmup.Model;

import java.util.ArrayList;

public class Semester {
	public String SemesterID;
	public String SemesterName;
	public Double CreditNum;
	public ArrayList<StudyProgram> StudyProgram;

	public Semester(String id, String name, ArrayList<StudyProgram> std) {
		SemesterID = id;
		SemesterName = name;
		StudyProgram = std;
	}

	public Semester() {
	}

	public int getCreditNum() {
		int num = 0;
		for (int i = 0; i < StudyProgram.size(); i++)
			num += StudyProgram.get(i).Credits;
		return num;
	}

	public String getHeader() {
		String str = SemesterName;
		str += " (" + (int) getCreditNum() + " tín chỉ)";
		return str;
	}
}
