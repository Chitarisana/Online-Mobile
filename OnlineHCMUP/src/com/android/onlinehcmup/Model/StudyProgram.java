package com.android.onlinehcmup.Model;

public class StudyProgram {
	public String CurriculumID;
	public String CurriculumName;
	public String CurriculumTypeName;
	public String StudyProgramID;
	public String StudyProgramName;
	public Double Credits;
	public String SemesterID;
	public String SemesterName;

	public StudyProgram() {
	}

	public StudyProgram(String _CurriculumID, String _CurriculumName,
			String _CurriculumTypeName, String _SemesterID,
			String _SemesterName, Double _Credits, String _StudyProgramID,
			String _StudyProgramName) {
		CurriculumID = _CurriculumID;
		CurriculumName = _CurriculumName;
		CurriculumTypeName = _CurriculumTypeName;
		StudyProgramID = _StudyProgramID;
		StudyProgramName = _StudyProgramName;
		Credits = _Credits;
		SemesterID = _SemesterID;
		SemesterName = _SemesterName;
	}

	public StudyProgram(String... values) {
		CurriculumID = values[0];
		CurriculumName = values[1];
		CurriculumTypeName = values[2];
		SemesterID = values[3];
		SemesterName = values[4];
		Credits = Double.parseDouble(values[5]);
		StudyProgramID = values[6];
		StudyProgramName = values[7];
	}
}