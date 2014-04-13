package com.android.onlinehcmup.Model;

public class StudentInfo {
	public String StudentID;
	public String LastName;
	public String FirstName;
	public String FullName;
	public String BirthDay;
	public String BirthPlace;
	public String Gender;
	public String EthnicName;
	public String ReligionName;
	public String IDCard;
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

	public StudentInfo() {
	}

	public StudentInfo(String _StudentID, String _LastName, String _FirstName,
			String _FullName, String _BirthDay, String _BirthPlace,
			String _Gender, String _EthnicName, String _ReligionName,
			String _IDCard, String _SocialPersonName, String _AreaName,
			String _PriorityName, String _Party, String _PartyDate,
			String _StudentTypeName, String _StudyStatusName,
			String _ProvinceName, String _DistrictName, String _CountryName,
			String _PermanentResidence, String _FileImage) {
		StudentID = _StudentID;
		LastName = _LastName;
		FirstName = _FirstName;
		FullName = _FullName;
		BirthDay = _BirthDay;
		BirthPlace = _BirthPlace;
		Gender = _Gender;
		EthnicName = _EthnicName;
		ReligionName = _ReligionName;
		IDCard = _IDCard;
		SocialPersonName = _SocialPersonName;
		AreaName = _AreaName;
		PriorityName = _PriorityName;
		Party = _Party;
		PartyDate = _PartyDate;
		StudentTypeName = _StudentTypeName;
		StudyStatusName = _StudyStatusName;
		ProvinceName = _ProvinceName;
		DistrictName = _DistrictName;
		CountryName = _CountryName;
		PermanentResidence = _PermanentResidence;
		FileImage = _FileImage;
	}
}