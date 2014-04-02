package com.example.rememberme.db.adapter;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.rememberme.data.Person;
import com.example.rememberme.db.table.PersonTable;

public class PersonTableAdapter {
	
	/**
	 * Build a {@link ContentValues} object with the values from the {@link Person} passed
	 * to represent the record inserted into {@link PersonTable}
	 * @param person
	 * @return
	 */
	public ContentValues toContentValues(Person person) {
		ContentValues cv = new ContentValues();
		
		// put the value of id or null if less than or equal to zero so it can be created
		cv.put(PersonTable.Column.ID.getColumnName(), 
				person.getId() < 0 ? null : person.getId());
		
		cv.put(PersonTable.Column.NAME.getColumnName(), person.getName());
		
		// put the value of image uri as a string or null if not set.
		cv.put(PersonTable.Column.IMAGE_URI.getColumnName(),
				person.getImage() == null ? null : person.getImage().toString());
		
		cv.put(PersonTable.Column.NOTES.getColumnName(), person.getNotes());
		return cv;
	}
	
	/**
	 * Construct a {@link Person} from the {@link Cursor} which comes from the {@link PersonTable}
	 * @param cursor
	 * @return
	 */
	public Person toPerson(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndex(PersonTable.Column.ID.getColumnName()));
		String name = cursor.getString(cursor.getColumnIndex(PersonTable.Column.NAME.getColumnName()));
		String imageUri = cursor.getString(cursor.getColumnIndex(PersonTable.Column.IMAGE_URI.getColumnName()));
		String notes = cursor.getString(cursor.getColumnIndex(PersonTable.Column.NOTES.getColumnName()));
		return new Person(id, name, notes, imageUri == null ? null : Uri.parse(imageUri));
	}
}
