package com.android.onlinehcmup.Model;

import java.util.Date;

public class Student {
	public String StudentID;
	public String StudentName;
	public String Password;
	public Date Birthday;
	public String BirthPlace;
	public Boolean Gender;
	public String EthnicName;
	public String IDCard;
	public String ReligionName;
	public String SocialPersonName;
	public String AreaName;
	public String PriorityName;
	public String Party;
	public Date PartyDay;
	public String StudentTypeName;
	public String StudyStatusName;
	public String ProviceName;
	public String DistrictName;
	public String CountryName;
	public String PermanentResidence;
	public String FileImage;
	public String HomePhone;
	public String MobilePhone;
	public String Email;
	public String ContactAddress;
	public String Note;
	public String ContactPersonName;
	public String ContactPersonAddress;
	public String ContactPersonPhone;
	public String CourseID;
	public String CourseTime;
	public String EnrollYear;
	public String GraduateYear;
	public String StudyProgramID;
	public String ClassStudentID;
	public String ClassRoleName;
	public String ProfessorName;
	public String ProfessorPhone;
	public String ProfessorEmail;

	public Student() {
	}

	public Student(String _StudentID, String _StudentName, String _Password,
			Date _Birthday, String _BirthPlace, Boolean _Gender,
			String _EthnicName, String _IDCard, String _ReligionName,
			String _SocialPersonName, String _AreaName, String _PriorityName,
			String _Party, Date _PartyDay, String _StudentTypeName,
			String _StudyStatusName, String _ProviceName, String _DistrictName,
			String _CountryName, String _PermanentResidence, String _FileImage,
			String _HomePhone, String _MobilePhone, String _Email,
			String _ContactAddress, String _Note, String _ContactPersonName,
			String _ContactPersonAddress, String _ContactPersonPhone,
			String _CourseID, String _CourseTime, String _EnrollYear,
			String _GraduateYear, String _StudyProgramID,
			String _ClassStudentID, String _ClassRoleName,
			String _ProfessorName, String _ProfessorPhone,
			String _ProfessorEmail) {
		StudentID = _StudentID;
		StudentName = _StudentName;
		Password = _Password;
		Birthday = _Birthday;
		BirthPlace = _BirthPlace;
		Gender = _Gender;
		EthnicName = _EthnicName;
		IDCard = _IDCard;
		ReligionName = _ReligionName;
		SocialPersonName = _SocialPersonName;
		AreaName = _AreaName;
		PriorityName = _PriorityName;
		Party = _Party;
		PartyDay = _PartyDay;
		StudentTypeName = _StudentTypeName;
		StudyStatusName = _StudyStatusName;
		ProviceName = _ProviceName;
		DistrictName = _DistrictName;
		CountryName = _CountryName;
		PermanentResidence = _PermanentResidence;
		FileImage = _FileImage;
		HomePhone = _HomePhone;
		MobilePhone = _MobilePhone;
		Email = _Email;
		ContactAddress = _ContactAddress;
		Note = _Note;
		ContactPersonName = _ContactPersonName;
		ContactPersonAddress = _ContactPersonAddress;
		ContactPersonPhone = _ContactPersonPhone;
		CourseID = _CourseID;
		CourseTime = _CourseTime;
		EnrollYear = _EnrollYear;
		GraduateYear = _GraduateYear;
		StudyProgramID = _StudyProgramID;
		ClassStudentID = _ClassStudentID;
		ClassRoleName = _ClassRoleName;
		ProfessorName = _ProfessorName;
		ProfessorPhone = _ProfessorPhone;
		ProfessorEmail = _ProfessorEmail;
	}
}