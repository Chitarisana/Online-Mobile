package com.android.onlinehcmup.Model;

public class Curriculum {
	public String CurriculumID;
	public String CurriculumName;
	public String CurriculumType;
	public double Credit;

	public Curriculum() {
	}

	public Curriculum(String id, String name, String type, double credit) {
		CurriculumID = id;
		CurriculumName = name;
		CurriculumType = type;
		Credit = credit;
	}
}
