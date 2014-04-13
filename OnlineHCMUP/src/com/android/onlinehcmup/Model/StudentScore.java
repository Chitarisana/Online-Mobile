package com.android.onlinehcmup.Model;

public class StudentScore {
	public String StudentID;
	public String ScheduleStudyUnitID;
	public String ScheduleStudyUnitName;
	public Double Credit;
	public String CurriculumType;
	public String Info;
	public Double Mark10;
	public Double Mark4;
	public String MarkLetter;
	public Boolean IsPass;
	public String StudyProgramID;
	public String TermID;
	public String YearStudy;

	public StudentScore() {
	}

	public StudentScore(String _StudentID, String _ScheduleStudyUnitID,
			String _ScheduleStudyUnitName, Double _Credit,
			String _CurriculumType, String _Info, Double _Mark10,
			Double _Mark4, String _MarkLetter, Boolean _IsPass,
			String _StudyProgramID, String _TermID, String _YearStudy) {
		StudentID = _StudentID;
		ScheduleStudyUnitID = _ScheduleStudyUnitID;
		ScheduleStudyUnitName = _ScheduleStudyUnitName;
		Credit = _Credit;
		CurriculumType = _CurriculumType;
		Info = _Info;
		Mark10 = _Mark10;
		Mark4 = _Mark4;
		MarkLetter = _MarkLetter;
		IsPass = _IsPass;
		StudyProgramID = _StudyProgramID;
		TermID = _TermID;
		YearStudy = _YearStudy;
	}
}