package com.android.onlinehcmup.Model;

import java.util.ArrayList;

public class TermStudy {
	public String YearStudy;
	public String TermID; // HK1, HK2
	public String TermName;
	public Double CreditNum;
	public ArrayList<RegisteredStudyUnit> StudyUnit;

	public TermStudy() {
	}

	public TermStudy(String y, String t, ArrayList<RegisteredStudyUnit> std) {
		YearStudy = y;
		TermID = t;
		TermName = getName(y, t);
		StudyUnit = std;
	}

	private String getName(String y, String t) {
		return t + ", " + y;
	}

	public TermStudy(String y, String t) {
		this(y, t, new ArrayList<RegisteredStudyUnit>());
	}

	public int getCreditNum() {
		int num = 0;
		for (int i = 0; i < StudyUnit.size(); i++)
			num += StudyUnit.get(i).Credits;
		return num;
	}

	public String getHeader() {
		return TermName + " (" + (int) getCreditNum() + " tc)";
	}
}
