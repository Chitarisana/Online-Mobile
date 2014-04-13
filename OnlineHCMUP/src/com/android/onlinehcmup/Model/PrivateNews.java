package com.android.onlinehcmup.Model;

public class PrivateNews {
	public String MessageSubject;
	public String CreationDate;
	public String SenderName;
	public String IsRead;
	public String MessageBody;
	public String FileAttach;

	public PrivateNews() {
	}

	public PrivateNews(String _MessageSubject, String _CreationDate,
			String _SenderName, String _IsRead, String _MessageBody,
			String _FileAttach) {
		MessageSubject = _MessageSubject;
		CreationDate = _CreationDate;
		SenderName = _SenderName;
		IsRead = _IsRead;
		MessageBody = _MessageBody;
		FileAttach = _FileAttach;
	}
}