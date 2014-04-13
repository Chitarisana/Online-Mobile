package com.android.onlinehcmup.JSON;

public class Key {
	public static final String KEY_STATUS = "status";
	public static final String KEY_ERRORS = "_errors";
	public static final String KEY_DATA = "data";
	public static final String[] KEY_PUBLIC_NEWS = new String[] {
			"MessageSubject", "CreationDate", "SenderName", "MessageNote",
			"FileAttach" };
	public static final String[] KEY_PRIVATE_NEWS = new String[] {
			"MessageSubject", "CreationDate", "SenderName", "IsRead",
			"MessageBody", "FileAttach" };
	public static final String[] KEY_STUDENT_INFO = new String[] { "StudentID",
	/* "LastName", "FirstName", */"FullName", "BirthDay", "BirthPlace",
			"Gender", "EthnicName", "ReligionName", "IDCard",
			"SocialPersonName", "AreaName", "PriorityName", "Party",
			"PartyDate", "StudentTypeName", "StudyStatusName", "ProvinceName",
			"DistrictName", "CountryName", "PermanentResidence", "FileImage" };
	public static final String KEY_STUDENT_INFO_GENDER = "Gender";
	public static final String KEY_STUDENT_INFO_PARTY = "Party";
	public static final String KEY_STUDENT_INFO_FULLNAME = "FullName";
	public static final String[] KEY_STUDENT_INFO_VI = new String[] {
			"Mã số SV", /* "Họ", "Tên", */"Tên đầy đủ", "Ngày sinh",
			"Nơi sinh", "Giới tính", "Dân tộc", "Tôn giáo", "Số CMND",
			"Đối tượng", "Khu vực", "Ưu tiên", "Đoàn/Đảng", "Ngày vào",
			"Loại sinh viên", "Tình trạng học", "Tỉnh/Thành phố", "Quận/Huyện",
			"Quốc gia", "Địa chỉ thường trú"/* , "Hình ảnh" */};
	public static final String[] KEY_STUDENT_COURSE = new String[] {
			"CourseName", "CourseTime", "EnrollTestingYear", "GraduateYear",
			"StudyProgramID", "ClassStudentID", "ClassRoleName",
			"ProfessorName", "ProfessorPhone", "ProfessorEmail" };
	public static final String[] KEY_STUDENT_COURSE_VI = new String[] {
			"Khóa học", "Niên khóa", "Năm nhập học",
			"Năm hết thời gian đào tạo", "Chương trình đào tạo",
			"Lớp sinh viên", "Chức vụ", "Tên cố vấn học tập",
			"SĐT cố vấn học tập", "Email cố vấn học tập" };
	public static final String[] KEY_STUDENT_CONTACT_1 = new String[] {
			"HomePhone", "MobilePhone", "Email", "ContactAddress", "Note" };
	public static final String[] KEY_STUDENT_CONTACT_1_VI = new String[] {
			"Điện thoại", "Di động", "Email", "Địa chỉ", "Ghi chú" };
	public static final String[] KEY_STUDENT_CONTACT_2 = new String[] {
			"ContactPersonName", "ContactPersonPhone", "ContactPersonAddress" };
	public static final String[] KEY_STUDENT_CONTACT_2_VI = new String[] {
			"Người liên hệ", "Điện thoại", "Địa chỉ" };
	public static final String[] KEY_CHANGE_PASSWORD = new String[] { "UserID",
			"UserPass" };
	public static final String[] KEY_STUDENT_LOAD_EDITCONTACT_1 = new String[] {
			"HomePhone", "MobilePhone", "Email", "ContactAddress" };
	public static final String[] KEY_STUDENT_LOAD_EDITCONTACT_1_VI = new String[] {
			"Điện thoại", "Di động", "Email", "Địa chỉ liên lạc" };
	public static final String[] KEY_STUDENT_LOAD_EDITCONTACT_2 = KEY_STUDENT_CONTACT_2;
	public static final String[] KEY_STUDENT_LOAD_EDITCONTACT_2_VI = KEY_STUDENT_CONTACT_2_VI;
	public static final String[] KEY_STUDENT_LOAD_EDITCONTACT_3 = new String[] {
			"SMSAccount1", "SMSAccount2", "Note" };
	public static final String[] KEY_STUDENT_LOAD_EDITCONTACT_3_VI = new String[] {
			"Số điện thoại 1", "Số điện thoại 2", "Ghi chú" };
	public static final String[] KEY_STUDENT_EDIT_CONTACT_1 = new String[] {
			"StudentPhone", "StudentMobile", "StudentEmail",
			"StudentContactAddress" };
	public static final String[] KEY_STUDENT_EDIT_CONTACT_2 = new String[] {
			"ContactPersonName", "ContactPersonPhone", "ContactPersonAddress" };
	public static final String[] KEY_STUDENT_EDIT_CONTACT_3 = new String[] {
			"SMSAccount1", "SMSAccount2", "Note" };
	public static final String[] KEY_STUDY_PROGRAMS_INFO = new String[] {
			"StudyProgramID", "StudyProgramName", "CurriculumID",
			"CurriculumName", "CurriculumTypeName", "SemesterID",
			"SemesterName", "Credits" };
	public static final String KEY_STUDY_PROGRAMS_INFO_CURRICULUMID = "CurriculumID";
	public static final String KEY_STUDY_PROGRAMS_INFO_SEMESTERID = "SemesterID";
	public static final String KEY_STUDY_PROGRAMS_INFO_SEMESTERNAME = "SemesterName";
	public static final String[] KEY_STUDY_PROGRAMS_INFO_VI = new String[] {
			"Mã chương trình học", "Tên chương trình học", "Mã học phần",
			"Tên học phần", "Loại học phần", "Mã học kỳ", "Tên học kỳ",
			"Số tín chỉ" };
	public static final String[] KEY_REGISTER_SCHEDULE = new String[] {
			"State", "IsAccepted", "Credits", "Informations", "ProfessorName",
			"BeginDate", "EndDate", "CurriculumID", "CurriculumName", "TermID",
			"YearStudy" };
	public static final String KEY_REGISTER_SCHEDULE_TERM = "TermID";
	public static final String KEY_REGISTER_SCHEDULE_YEAR_STUDY = "YearStudy";
	public static final String[] KEY_REGISTER_SCHEDULE_VI = new String[] {
			"Trạng thái", "Đã chấp nhận", "Tín chỉ", "Thông tin chi tiết",
			"Tên giảng viên", "Ngày bắt đầu", "Ngày kết thúc", "Mã học phần",
			"Tên học phần", "Học kỳ", "Năm học" };
	public static final String[] KEY_NOT_ACCUMULATE_CURRICULUM = new String[] {
			"CurriculumID", "CurriculumName", "CurriculumTypeName", "Credits" };
	public static final String[] KEY_NOT_ACCUMULATE_CURRICULUM_VI = new String[] {
			"Mã học phần", "Tên học phần", "Loại học phần", "Tín chỉ" };
}
