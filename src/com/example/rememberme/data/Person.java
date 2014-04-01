package com.example.rememberme.data;

import android.net.Uri;

public class Person {

	private String mName;
	private Uri mImage;
	private String mNotes;
	
	public Person(String name, String notes, Uri image) {
		mName = name;
		mImage = image;
		mNotes = notes;
	}
	
	public String getName() {
		return mName;
	}
	
	public Uri getImage() {
		return mImage;
	}
	
	public String getNotes() {
		return mNotes;
	}
}
