package com.android.onlinehcmup.Model;

public class StudentScore {
	public String CurriculumID;
	public String CurriculumName;
	public Double Credits;
	public String CurriculumTypeName;
	public String Info;
	public Double Mark10;
	public Double Mark4;
	public String MarkLetter;
	public String IsPass;
	public String TermID;
	public String YearStudy;

	public static final String PassSuccess = "Đậu";
	public static final String PassFail = "Rớt";

	public StudentScore() {
	}

	public StudentScore(String _CurriculumID, String _CurriculumName,
			Double _Credits, String _CurriculumType, String _Info,
			Double _Mark10, Double _Mark4, String _MarkLetter, String _IsPass,
			String _TermID, String _YearStudy) {
		CurriculumID = _CurriculumID;
		CurriculumName = _CurriculumName;
		Credits = _Credits;
		CurriculumTypeName = _CurriculumType;
		Info = _Info;
		Mark10 = _Mark10;
		Mark4 = _Mark4;
		MarkLetter = _MarkLetter;
		IsPass = _IsPass;
		TermID = _TermID;
		YearStudy = _YearStudy;
	}

	public StudentScore(String... values) {
		CurriculumID = values[0];
		CurriculumName = values[1];
		Credits = Double.parseDouble(values[2] + "");
		CurriculumTypeName = values[3];
		Info = values[4];
		Mark10 = Double.parseDouble(values[5] + "");
		Mark4 = Double.parseDouble(values[6] + "");
		MarkLetter = values[7];
		String isPass = values[8];
		if (isPass != null) {
			isPass = (isPass.matches("True")
					|| isPass.compareTo(PassSuccess) == 0 ? "Đậu" : "Rớt");
		}
		IsPass = isPass;
		TermID = values[9];
		YearStudy = values[10];
	}
}