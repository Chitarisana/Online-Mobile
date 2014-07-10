package com.android.onlinehcmup.Model;

public class ScheduleCalendar {
	/*
	 * public String StudentID; public String StudentStudyUnitID; public String
	 * StudentStudyUnitName; public String CurriculumID; public Double Credit;
	 * public int DayOfWeek; public String RoomID; public String CampusName;
	 * public String BeginTime; public String EndTime; public String FullName;
	 * public String StartDate; public String EndDate; public String Address;
	 * public String PeriodName; public String TermID; public String YearStudy;
	 */
	public String CalendarStudyUnitID;
	public String ScheduleStudyUnitID;
	public String CurriculumName;
	public String Information;
	public String DayOfWeek;
	public String RoomID;
	public String StartPeriod;
	public String EndPeriod;
	public String BeginTime;
	public String EndTime;
	public String StartDate;
	public String EndDate;
	public String Week;
	public String YearStudy;
	public String TermID;

	public ScheduleCalendar() {
	}

	public ScheduleCalendar(String... values) {
		ScheduleStudyUnitID = values[0];
		CurriculumName = values[1];
		Information = values[2];
		DayOfWeek = values[3];
		RoomID = values[4];
		StartPeriod = values[5];
		EndPeriod = values[6];
		BeginTime = values[7];
		EndTime = values[8];
		StartDate = values[9];
		EndDate = values[10];
		Week = values[11];
		YearStudy = values[12];
		TermID = values[13];
		CalendarStudyUnitID = ScheduleStudyUnitID + StartDate;
	}

	public String GetStartYear() {
		return ScheduleStudyUnitID.substring(0, 3);
	}

	public String GetTermID() {
		return ScheduleStudyUnitID.substring(3, 4);
	}

	public String GetCredits() {
		String s = CurriculumName;
		return s.substring(s.indexOf("[") + 1, s.indexOf("]"));
	}

	public String GetCurriculumName() {
		String s = CurriculumName;
		s = s.trim();
		return s.substring(0, s.length() - GetCredits().length() - 2).trim();
		// - 2: 2 dau ngoac
	}

	public String GetCurriculumID() {
		return ScheduleStudyUnitID.substring(4);
	}

	public String GetScheduleDate() {// ngay hoc
		return (!(DayOfWeek.matches("null")) ? DayOfWeek + ", " : "")
				+ BeginTime + "-" + EndTime
				+ ((!RoomID.matches("null")) ? ", " + RoomID : "");
	}

	public String GetTeacherName() {
		int position = Math.max(0, Information.indexOf("<br"));// find enter
		return Information.substring(0, position).trim();
	}
}