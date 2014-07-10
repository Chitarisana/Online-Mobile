package com.android.onlinehcmup.Model;

public class ScoreDetail {
	public String ScoreDetailID;
	public String StudyUnitID;
	public String AssignmentID;
	public String CurriculumName;
	public String AssignmentName;
	public String FirstMark;
	public String SecondMark;

	public ScoreDetail() {
	}

	public ScoreDetail(String... values) { // save to db
		StudyUnitID = values[1];
		AssignmentID = values[2];
		CurriculumName = values[3];
		AssignmentName = values[4];
		FirstMark = values[5];
		SecondMark = values[6];
		ScoreDetailID = StudyUnitID + AssignmentID;
	}
}
