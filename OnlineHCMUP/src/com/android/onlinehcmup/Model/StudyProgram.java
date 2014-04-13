package com.android.onlinehcmup.Model;

public class StudyProgram {
	public String StudentID;
	public String CurriculumID;
	public String CurriculumName;
	public String CurriculumType;
	public String StudyProgramID;
	public String StudyProgramName;
	public Double Credit;
	public String SemesterID;
	public String SemesterName;

	public StudyProgram() {
	}

	public StudyProgram(String _StudentID, String _StudyProgramID,
			String _StudyProgramName, String _CurriculumID,
			String _CurriculumName, String _CurriculumTypeName,
			String _SemesterID, String _SemesterName, Double _Credits) {

		StudentID = _StudentID;
		CurriculumID = _CurriculumID;
		CurriculumName = _CurriculumName;
		CurriculumType = _CurriculumTypeName;
		StudyProgramID = _StudyProgramID;
		StudyProgramName = _StudyProgramName;
		Credit = _Credits;
		SemesterID = _SemesterID;
		SemesterName = _SemesterName;
	}
}