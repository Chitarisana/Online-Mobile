package com.android.onlinehcmup.Model;

public class StudentSchedule {
	public String StudentID;
	public String StudentStudyUnitID;
	public String StudentStudyUnitName;
	public String CurriculumID;
	public Double Credit;
	public int DayOfWeek;
	public String RoomID;
	public String CampusName;
	public String BeginTime;
	public String EndTime;
	public String FullName;
	public String StartDate;
	public String EndDate;
	public String Address;
	public String PeriodName;
	public String TermID;
	public String YearStudy;

	public StudentSchedule() {
	}

	public StudentSchedule(String _StudentID, String _StudentStudyUnitID,
			String _StudentStudyUnitName, String _CurriculumID, Double _Credit,
			int _DayOfWeek, String _RoomID, String _CampusName,
			String _BeginTime, String _EndTime, String _FullName,
			String _StartDate, String _EndDate, String _Address,
			String _PeriodName, String _TermID, String _YearStudy) {
		StudentID = _StudentID;
		StudentStudyUnitID = _StudentStudyUnitID;
		StudentStudyUnitName = _StudentStudyUnitName;
		CurriculumID = _CurriculumID;
		Credit = _Credit;
		DayOfWeek = _DayOfWeek;
		RoomID = _RoomID;
		CampusName = _CampusName;
		BeginTime = _BeginTime;
		EndTime = _EndTime;
		FullName = _FullName;
		StartDate = _StartDate;
		EndDate = _EndDate;
		Address = _Address;
		PeriodName = _PeriodName;
		TermID = _TermID;
		YearStudy = _YearStudy;
	}
}