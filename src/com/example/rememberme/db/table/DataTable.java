package com.example.rememberme.db.table;

import android.database.sqlite.SQLiteDatabase;

public interface DataTable {

	public void onCreate(SQLiteDatabase db);

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
