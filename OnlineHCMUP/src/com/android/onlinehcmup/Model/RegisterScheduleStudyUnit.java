package com.android.onlinehcmup.Model;

import android.text.Html;

public class RegisterScheduleStudyUnit {
	public String StudentID;
	public String State;
	public String IsAccepted;
	public Double Credits;
	public String Informations;
	public String ProfessorName;
	public String BeginDate;
	public String EndDate;
	public String CurriculumID;
	public String CurriculumName;
	public String TermID;
	public String YearStudy;

	public RegisterScheduleStudyUnit() {
	}

	public RegisterScheduleStudyUnit(String _StudentID, String _State,
			String _IsAccepted, String _Credits, String _Informations,
			String _ProfessorName, String _BeginDate, String _EndDate,
			String _CurriculumID, String _CurriculumName, String _TermID,
			String _YearStudy) {
		StudentID = _StudentID;
		State = _State;
		IsAccepted = _IsAccepted;
		Credits = Double.parseDouble(_Credits);
		Informations = Html.fromHtml(_Informations).toString();
		ProfessorName = _ProfessorName;
		BeginDate = _BeginDate;
		EndDate = _EndDate;
		CurriculumID = _CurriculumID;
		CurriculumName = _CurriculumName;
		TermID = _TermID;
		YearStudy = _YearStudy;
	}
}