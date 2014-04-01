package com.example.rememberme.db;

import java.util.ArrayList;
import java.util.List;

import com.example.rememberme.db.table.DataTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	/**
	 * Initial database schema
	 */
	public static final int DATABASE_VERSION_1 = 1;

	private static final String DATABASE_NAME = "remeber_me.db";
	private static final int DATABASE_VERSION = DATABASE_VERSION_1;

	/**
	 * {@link DataTable} managed by this class
	 */
	private List<DataTable> mDataTables = new ArrayList<DataTable>();
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * Add {@link DataTable}s to be managed by this class
	 * @param table
	 */
	public void addDataTable(DataTable table) {
		mDataTables.add(table);
	}
	
	/**
	 * Called when the database needs to be created. Each table in the database
	 * should then be created.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (DataTable table : mDataTables) {
			table.onCreate(db);
		}
	}

	/**
	 * Called when the database is upgraded ({@link SQLiteHelper#DATABASE_VERSION} changes).
	 * Each table then should be altered or created as necessary.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (DataTable table : mDataTables) {
			table.onUpgrade(db, oldVersion, newVersion);
		}
	}
}
