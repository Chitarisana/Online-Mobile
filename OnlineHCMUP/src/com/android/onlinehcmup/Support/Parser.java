package com.android.onlinehcmup.Support;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Parser {

	@SuppressLint("SimpleDateFormat")
	public static Date DateParser(String stringDate) {
		String dMy = "dd/MM/yyyy";
		String Mdy = "MM/dd/yyyy";
		SimpleDateFormat parser = new SimpleDateFormat(dMy);
		try {
			return parser.parse(stringDate);
		} catch (ParseException e) {
			parser = new SimpleDateFormat(Mdy);
			try {
				return parser.parse(stringDate);
			} catch (ParseException e1) {
				return null;
			}
		}
	}

	public static URL URLParser(String stringURL) {
		try {
			return new URL(stringURL);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public static Bitmap URLtoBitmapParser(URL url) {
		try {
			return BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
		} catch (IOException e) {
			return null;
		}
	}

	public static String TrimSpace(String str) {
		while (str.contains("  "))
			str = str.replace("  ", " ");
		return str;
	}

	public static String[] MultiToOne(String[][] values) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values[i].length; j++)
				result.add(values[i][j]);
		return result.toArray(new String[result.size()]);
	}

	/*public static String toStudyProgramID(String studentID) {
		return studentID.substring(0, 3) + studentID.substring(4, 7);
	}*/
}
