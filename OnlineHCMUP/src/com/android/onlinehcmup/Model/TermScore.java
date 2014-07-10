package com.android.onlinehcmup.Model;

import java.util.ArrayList;

public class TermScore {
	public String TermYearID;
	public String YearStudy;
	public String TermID; // HK1, HK2
	public String TermName;
	public String LastScore; // Diem RLSV
	public String BehaviorScoreRank; // XL RLSV
	public String AverageScore; // Diem TB he 10
	public String AverageScore4; // Diem TB he 4
	public String RankName; // XL Hoc luc
	public ArrayList<StudentScore> Score;

	public TermScore() {
	}

	public TermScore(String _YearStudy, String _TermID, String _LastScore,
			String _BehaviorScoreRank, String _AverageScore,
			String _AverageScore4, String _RankName,
			ArrayList<StudentScore> _Score) { // to load from db to view
		YearStudy = _YearStudy;
		TermID = _TermID;
		Score = _Score;
		String score = _LastScore;
		LastScore = (score.matches("null")) ? "0" : score;
		String scoreRank = _BehaviorScoreRank;
		BehaviorScoreRank = (scoreRank.matches("null")) ? "Không có"
				: scoreRank;
		AverageScore = _AverageScore;
		AverageScore4 = _AverageScore4;
		RankName = _RankName;
		TermName = getName(_YearStudy, _TermID);
		TermYearID = YearStudy.substring(2, 4)
				+ TermID.substring(TermID.length() - 1) + "1";
	}

	public TermScore(String... values) { // to save to db
		YearStudy = getValue(1, values);
		TermID = getValue(2, values);
		LastScore = getValue(3, values);
		BehaviorScoreRank = getValue(4, values);
		AverageScore = getValue(5, values);
		AverageScore4 = getValue(6, values);
		RankName = getValue(7, values);
		TermYearID = YearStudy.substring(2, 4)
				+ TermID.substring(TermID.length() - 1) + "1";
	}

	private String getValue(int i, String... values) {
		if (values[i] != null) {
			return values[i];
		}
		return null;
	}

	private String getName(String y, String t) {
		return "Học kỳ " + t.substring(t.length() - 1) + "/" + y + " ("
				+ getCreditNum() + " tc)";
	}

	public int getCreditNum() {
		int num = 0;
		for (int i = 0; i < Score.size(); i++)
			num += Score.get(i).Credits;
		return num;
	}
}
