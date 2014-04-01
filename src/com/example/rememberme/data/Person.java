package com.example.rememberme.data;

import android.net.Uri;

public class Person {

	private int mId;
	private String mName;
	private Uri mImage;
	private String mNotes;
		
	public Person(int id, String name, String notes, Uri image) {
		mId = id;
		mName = name;
		mImage = image;
		mNotes = notes;
	}
	
	public int getId() {
		return mId;
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