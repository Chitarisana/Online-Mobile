package com.android.onlinehcmup.JSON;

public class Link {
	private static final String BASE_URL = "http://10.0.0.199/mobilews/mservice/Service.svc/";
	public static final String PUBLIC_NEWS = BASE_URL + "mainInfo?groupCateID=%s&messageCateID=%s";
	public static final String LOGIN = BASE_URL
			+ "actionSel?studentID=%s&password=%s";
	public static final String PRIVATE_NEWS = BASE_URL + "newsInfo?studentID=%s";
	public static final String STUDENT_INFO = BASE_URL
			+ "studentInfo?studentID=%s";
	public static final String STUDENT_COURSE = BASE_URL
			+ "courseInfo?studentID=%s";
	public static final String STUDENT_CONTACT = BASE_URL
			+ "contactInfo?studentID=%s";
	public static final String STUDENT_CHANGE_PASSWORD = BASE_URL
			+ "changePass?userID=%s&oldPass=%s&newPass=%s";
	public static final String STUDENT_EDIT_INFO = BASE_URL
			+ "updateInfo?studentID=%s&studentPhone=%s&studentMobile=%s&studentEmail=%s&studentContactAddress=%s&contactPersonName=%s&contactPersonPhone=%s&contactPersonAddress=%s&SMSAccount1=%s&SMSAccount2=%s&note=%s";
	public static final String STUDY_PROGRAM = BASE_URL
			+ "studyProgramsInfo?studentID=%s";
	// what???
	public static final String REGISTER_SCHEDULE = BASE_URL
			+ "listRegisterSchedule?studentID=%s";
	public static final String CURRENT_TERM = BASE_URL
			+ "getYearStudyandTermID";
	public static final String REGISTER_NOT_ACCUMULATE = BASE_URL
			+ "notAccumulateCurriculum?studentID=%s&studyProgramID=%s";
}
