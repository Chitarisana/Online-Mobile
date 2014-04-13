package com.android.onlinehcmup.Model;

import java.util.ArrayList;

public class TermStudy {
	public String YearStudy;
	public String TermID; // HK1, HK2
	public String TermName;
	public Double CreditNum;
	public ArrayList<RegisterScheduleStudyUnit> StudyUnit;

	public TermStudy() {
	}

	public TermStudy(String y, String t, Double c,
			ArrayList<RegisterScheduleStudyUnit> std) {
		YearStudy = y;
		TermID = t;
		CreditNum = c;
		TermName = getName(t, y);
		StudyUnit = std;
	}

	private String getName(String t, String y) {
		String num = t.substring(t.length() - 1, t.length());
		return "HK " + num + ", " + y;
	}

	public TermStudy(String y, String t) {
		this(y, t, null, new ArrayList<RegisterScheduleStudyUnit>());
	}

	public int getCreditNum() {
		int num = 0;
		for (int i = 0; i < StudyUnit.size(); i++)
			num += StudyUnit.get(i).Credits;
		return num;
	}

	public String getHeader() {
		String str = TermName;
		str += " (" + (int)getCreditNum() + " tc)";
		return str;
	}	
}
