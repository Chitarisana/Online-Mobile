package com.android.onlinehcmup.Model;

public class ScheduleExamination {

	public String ScheduleStudyUnitID;
	public String CurriculumID;
	public String CurriculumName;
	public String Day;
	public String Time;
	public String Room;
	public String Address;
	public String Note;

	public ScheduleExamination() {
	}

	public ScheduleExamination(String... values) {
		ScheduleStudyUnitID = values[0];
		CurriculumID = values[1];
		CurriculumName = values[2];
		Day = values[3];
		String value = values[4];
		Time = value.substring(0, value.length() - 1);
		Room = values[5];
		Address = values[6];
		Note = values[7];
	}

	public String GetStartYear() {
		return ScheduleStudyUnitID.substring(0, 3);
	}

	public String GetTermID() {
		return ScheduleStudyUnitID.substring(3, 4);
	}
}