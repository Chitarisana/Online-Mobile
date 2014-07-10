package com.android.onlinehcmup.Model;

import android.text.Html;

public class RegisteredStudyUnit {
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
	public String TermScheduleID;

	public RegisteredStudyUnit() {
	}

	/*
	 * public RegisteredStudyUnit(String _CurriculumID, String _CurriculumName,
	 * String _TermID, String _YearStudy, String _State, String _IsAccepted,
	 * String _Credits, String _Informations, String _ProfessorName, String
	 * _BeginDate, String _EndDate) { State = _State; IsAccepted = _IsAccepted;
	 * Credits = Double.parseDouble(_Credits); Informations =
	 * Html.fromHtml(_Informations).toString(); ProfessorName = _ProfessorName;
	 * BeginDate = _BeginDate; EndDate = _EndDate; CurriculumID = _CurriculumID;
	 * CurriculumName = _CurriculumName; TermID = _TermID; YearStudy =
	 * _YearStudy; }
	 */

	public RegisteredStudyUnit(String[] values) {
		CurriculumID = values[0];
		CurriculumName = values[1];
		TermID = values[2];
		YearStudy = values[3];
		State = values[4];
		IsAccepted = values[5];
		Credits = Double.parseDouble(values[6]);
		Informations = Html.fromHtml(values[7]).toString();
		ProfessorName = values[8];
		BeginDate = values[9];
		EndDate = values[10];
		TermScheduleID = YearStudy.substring(2, 4)
				+ TermID.substring(TermID.length() - 1) + "1" + CurriculumID;
	}
}