package com.android.onlinehcmup.Model;

import java.lang.reflect.Field;

public class Student {
	public String StudentID;
	public String FullName;
	public String BirthDay;
	public String BirthPlace;
	public String Gender;
	public String EthnicName;
	public String IDCard;
	public String ReligionName;
	public String SocialPersonName;
	public String AreaName;
	public String PriorityName;
	public String Party;
	public String PartyDate;
	public String StudentTypeName;
	public String StudyStatusName;
	public String ProvinceName;
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
	public String CourseName;
	public String CourseTime;
	public String EnrollTestingYear;
	public String GraduateYear;
	public String StudyProgramID;
	public String ClassStudentID;
	public String ClassRoleName;
	public String ProfessorName;
	public String ProfessorPhone;
	public String ProfessorEmail;

	public Student() {
	}

	public Student(String _StudentID, String _FullName, String _Birthday,
			String _BirthPlace, Boolean _Gender, String _EthnicName,
			String _ReligionName, String _IDCard, String _SocialPersonName,
			String _AreaName, String _PriorityName, String _Party,
			String _PartyDate, String _StudentTypeName,
			String _StudyStatusName, String _ProviceName, String _DistrictName,
			String _CountryName, String _PermanentResidence, String _FileImage,
			String _CourseName, String _CourseTime, String _EnrollTestingYear,
			String _GraduateYear, String _StudyProgramID,
			String _ClassStudentID, String _ClassRoleName,
			String _ProfessorName, String _ProfessorPhone,
			String _ProfessorEmail, String _HomePhone, String _MobilePhone,
			String _Email, String _ContactAddress, String _Note,
			String _ContactPersonName, String _ContactPersonPhone,
			String _ContactPersonAddress) {
		StudentID = _StudentID;
		FullName = _FullName;
		BirthDay = _Birthday;
		BirthPlace = _BirthPlace;
		Gender = (_Gender) ? "Nam" : "Nữ";
		EthnicName = _EthnicName;
		IDCard = _IDCard;
		ReligionName = _ReligionName;
		SocialPersonName = _SocialPersonName;
		AreaName = _AreaName;
		PriorityName = _PriorityName;
		Party = _Party;
		PartyDate = _PartyDate;
		StudentTypeName = _StudentTypeName;
		StudyStatusName = _StudyStatusName;
		ProvinceName = _ProviceName;
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
		CourseName = _CourseName;
		CourseTime = _CourseTime;
		EnrollTestingYear = _EnrollTestingYear;
		GraduateYear = _GraduateYear;
		StudyProgramID = _StudyProgramID;
		ClassStudentID = _ClassStudentID;
		ClassRoleName = _ClassRoleName;
		ProfessorName = _ProfessorName;
		ProfessorPhone = _ProfessorPhone;
		ProfessorEmail = _ProfessorEmail;
	}

	public Student(Object... values) {
		StudentID = getValue(0, values);
		FullName = getValue(1, values);
		BirthDay = getValue(2, values);
		BirthPlace = getValue(3, values);
		String gender = getValue(4, values);
		if (gender != null) {
			gender = (gender.matches("True") || gender.matches("Nam") ? "Nam"
					: "Nữ");
		}
		Gender = gender;
		EthnicName = getValue(5, values);
		ReligionName = getValue(6, values);
		IDCard = getValue(7, values);
		SocialPersonName = getValue(8, values);
		AreaName = getValue(9, values);
		PriorityName = getValue(10, values);
		Party = ((String) (values[11] != null ? values[11] : "")).matches("1") ? "Có tham gia"
				: "";
		PartyDate = getValue(12, values);
		StudentTypeName = getValue(13, values);
		StudyStatusName = getValue(14, values);
		ProvinceName = getValue(15, values);
		DistrictName = getValue(16, values);
		CountryName = getValue(17, values);
		PermanentResidence = getValue(18, values);
		FileImage = getValue(19, values);
		CourseName = getValue(20, values);
		CourseTime = getValue(21, values);
		EnrollTestingYear = getValue(22, values);
		GraduateYear = getValue(23, values);
		StudyProgramID = getValue(24, values);
		ClassStudentID = getValue(25, values);
		ClassRoleName = getValue(26, values);
		ProfessorName = getValue(27, values);
		ProfessorPhone = getValue(28, values);
		ProfessorEmail = getValue(29, values);
		HomePhone = getValue(30, values);
		MobilePhone = getValue(31, values);
		Email = getValue(32, values);
		ContactAddress = getValue(33, values);
		Note = getValue(34, values);
		ContactPersonName = getValue(35, values);
		ContactPersonPhone = getValue(36, values);
		ContactPersonAddress = getValue(37, values);
	}

	private String getValue(int i, Object... values) {
		if (values[i] != null) {
			return (String) values[i];
		}
		return null;
	}

	/*
	 * Add some fields of std2 to std1
	 */
	public static Student Merge(Student std1, Student std2) {
		Student std = new Student();
		Field[] flds = Student.class.getDeclaredFields();
		for (Field f : flds) {
			try {
				String value = (String) (f.get(std1));
				if (value != null)
					f.set(std, value);
				else {
					String v = (String) (f.get(std2));
					if (v != null)
						f.set(std, v);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return std1;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return std1;
			}
		}
		return std;
	}
}