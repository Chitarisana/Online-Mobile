package com.android.onlinehcmup.Model;

import java.util.ArrayList;

public class TermSchedule {
	public String YearStudy;
	public String TermID; // HK1, HK2
	public ArrayList<ScheduleExamination> Exams;

	public TermSchedule() {
	}

	public TermSchedule(String... values) { // to save to db
		YearStudy = getValue(0, values);
		TermID = getValue(1, values);

	}

	private String getValue(int i, String... values) {
		if (values[i] != null) {
			return values[i];
		}
		return null;
	}

	public String GetName() {
		return "Học kỳ " + TermID.substring(TermID.length() - 1) + "/"
				+ YearStudy;
	}
}