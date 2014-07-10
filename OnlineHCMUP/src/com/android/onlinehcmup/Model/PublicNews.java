package com.android.onlinehcmup.Model;

public class PublicNews {
	public String MessageSubject;
	public String CreationDate;
	public String SenderName;
	public String MessageNote;
	public String FileAttach;

	public PublicNews() {
	}

	public PublicNews(String _MessageSubject, String _CreationDate,
			String _SenderName, String _MessageNote, String _FileAttach) {
		MessageSubject = _MessageSubject;
		CreationDate = _CreationDate;
		SenderName = _SenderName;
		MessageNote = _MessageNote;
		FileAttach = _FileAttach;
	}

	public PublicNews(String... strings) {
		MessageSubject = strings[0];
		CreationDate = strings[1];
		SenderName = strings[2];
		MessageNote = strings[3];
		FileAttach = strings[4];
	}
}
