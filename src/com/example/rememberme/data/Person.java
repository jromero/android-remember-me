package com.example.rememberme.data;

import android.net.Uri;

public class Person {

	private long mId;
	private String mName;
	private Uri mImage;
	private String mNotes;
	
	public Person(String name, String notes, Uri image) {
		this(-1, name, notes, image);
	}
	
	public Person(long id, String name, String notes, Uri image) {
		mId = id;
		mName = name;
		mImage = image;
		mNotes = notes;
	}
	
	public long getId() {
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(mId);
		sb.append("|");
		sb.append(mName);
		sb.append("|");
		sb.append(mImage);
		sb.append("|");
		sb.append(mNotes);
		return sb.toString();
	}
}