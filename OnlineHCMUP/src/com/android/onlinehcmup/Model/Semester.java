package com.android.onlinehcmup.Model;

import java.util.ArrayList;

public class Semester {
	public String SemesterID;
	public String SemesterName;
	public ArrayList<StudyProgram> StudyProgram;

	public Semester(String id, String name, ArrayList<StudyProgram> std) {
		SemesterID = id;
		SemesterName = name;
		StudyProgram = std;
	}

	public Semester() {
	}
}
