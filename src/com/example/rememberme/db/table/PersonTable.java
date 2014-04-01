package com.example.rememberme.db.table;

import android.database.sqlite.SQLiteDatabase;

public class PersonTable implements DataTable {
	
	public static String TABLE_NAME = "person";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME 
				+ "(" 
				+ "'" + Column.ID.getColumnName() + "' INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "'" + Column.NAME.getColumnName() + "' TEXT NOT NULL, " 
				+ "'" + Column.IMAGE_URI.getColumnName() + "' TEXT, " 
				+ "'" + Column.NOTES.getColumnName() + "' TEXT NOT NULL" 
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// nothing to do for now
	}
	
	/**
	 * Enumeration of current columns in database
	 */
	public static enum Column {
		ID("_id"),
		NAME("name"),
		IMAGE_URI("image_uri"),
		NOTES("notes");
		
		private String mColumnName;

		private Column(String columnName) {
			mColumnName = columnName;
		}
		
		public String getColumnName() {
			return mColumnName;
		}
	}
}
