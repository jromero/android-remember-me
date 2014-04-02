package com.example.rememberme.provider;

import java.util.HashMap;

import com.example.rememberme.db.SQLiteHelper;
import com.example.rememberme.db.table.DataTable;
import com.example.rememberme.db.table.PersonTable;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class PersonContentProvider extends ContentProvider {
	
	private static final String TAG = PersonContentProvider.class.getSimpleName();
	
	private static final String AUTHORITY = PersonContentProvider.class.getCanonicalName();
    private static final String VND_PATH = "vnd.example.rememberme";
    private static final String CONTENT_URI_STRING = "content://" + AUTHORITY;
    private static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/";
    private static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/";

    /* *********************************************************************
     * URI MATCHING
     */
    public static final int URI_CODE_PERSON = 1;
    public static final int URI_CODE_PERSON_ID = 2;
    
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
		//Parameters:
		//	authority - the authority to match
		//	path - the path to match. * may be used as a wild card for any text, and # may be used as a wild card for numbers.
		//	code - the code that is returned when a URI is matched against the given components. Must be positive.
    	
    	// content://com.example.rememberme.provider.PersonContentProvider/person/
        URI_MATCHER.addURI(AUTHORITY, PersonTable.TABLE_NAME, URI_CODE_PERSON);
        
    	// content://com.example.rememberme.provider.PersonContentProvider/person/[0-9]+
        URI_MATCHER.addURI(AUTHORITY, PersonTable.TABLE_NAME + "/#", URI_CODE_PERSON_ID);
    }
    
    /* **********************************************************************
     * BASE CONTENT URIS
     */
    private static final HashMap<Class<? extends DataTable>, Uri> BASE_CONTENT_URIS = new HashMap<Class<? extends DataTable>, Uri>();
    static {
        BASE_CONTENT_URIS.put(PersonTable.class,
                Uri.parse(CONTENT_URI_STRING + "/" + PersonTable.TABLE_NAME));
    }

    /**
     * Gets the base Uri for the given {@link Class}
     * @param clazz
     * @return
     */
    public static Uri getContentUri(Class<? extends DataTable> clazz) {
        return BASE_CONTENT_URIS.get(clazz);
    }
    
    /**
     * Gets the Uri for the given {@link Class} with the appended id
     * @param clazz
     * @param id
     * @return the constructed Uri
     */
    public static Uri getContentUriWithId(Class<? extends DataTable> clazz, long id) {
    	Uri uri = BASE_CONTENT_URIS.get(clazz);
    	
    	if (uri == null) {
    		return null;
    	}
    	
        return ContentUris.withAppendedId(uri, id);
    }
    /* ********************************************************************/
    
    private SQLiteHelper mSQLiteHelper;
	
	@Override
	public boolean onCreate() {
		Log.v(TAG, "Starting...");
		return true; // true if the provider was successfully loaded, false otherwise
	}
	
	@Override
	public String getType(Uri uri) {
		Log.v(TAG, "Get Type: " + uri);
		
		int code = URI_MATCHER.match(uri);
		switch (code) {
			case URI_CODE_PERSON:
				// this uri can return more than just one item (person)
				return getMimeType(CONTENT_TYPE_DIR, PersonTable.TABLE_NAME);
			case URI_CODE_PERSON_ID:
				// this uri should return only one item at most (person)
				return getMimeType(CONTENT_TYPE_ITEM, PersonTable.TABLE_NAME);
			case UriMatcher.NO_MATCH:
			default:
				return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.v(TAG, "Insert: " + uri);

        // get a database object to query
        SQLiteDatabase db = getSQLiteHelperInstance().getWritableDatabase();

        Uri baseUri = null;
        String tableName = null;
        String nullColHack = null;
        int conflictAlgorithim = SQLiteDatabase.CONFLICT_REPLACE;

        switch (URI_MATCHER.match(uri)) {
            case URI_CODE_PERSON:
                baseUri = getContentUri(PersonTable.class);
            	tableName = PersonTable.TABLE_NAME;
                nullColHack = PersonTable.Column.ID.getColumnName();
                break;
            default:
                Log.w(TAG, "No insert defined for: " + uri);
                return null;
        }

        long rowId = db.insertWithOnConflict(tableName, nullColHack, values, conflictAlgorithim);
        
        Uri newItemUri = null;
        if (rowId >= 0 && baseUri != null) {
        	// create a new Uri for the newly inserted item
            newItemUri = ContentUris.withAppendedId(baseUri, rowId);
            
            // notify all listening classes that the baseUri contents have changed
            getContext().getContentResolver().notifyChange(baseUri, null);
        }
        return newItemUri;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.v(TAG, "Query: " + uri);

        // get a database object to query
        SQLiteDatabase db = getSQLiteHelperInstance().getWritableDatabase();
        
        String tableName = null;
        String groupBy = null;
        String having = null;
        Cursor cursor = null;
        
		// fix selection statement to allow concatenation
        selection = selection == null ? "" : selection;

        switch (URI_MATCHER.match(uri)) {
            case URI_CODE_PERSON:
            	tableName = PersonTable.TABLE_NAME;
            	break;
            case URI_CODE_PERSON_ID:
            	tableName = PersonTable.TABLE_NAME;
            	// append selection of single item
            	selection += PersonTable.Column.ID.getColumnName() + "=" + uri.getLastPathSegment();
                break;
            default:
                Log.w(TAG, "No query defined for: " + uri);
                return null;
        }

        cursor = db.query(tableName, projection, selection, selectionArgs, groupBy, having, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		Log.v(TAG, "Update: " + uri);
		
		SQLiteDatabase db = getSQLiteHelperInstance().getWritableDatabase();

		String tableName = null;
		int conflictAlgorithm = SQLiteDatabase.CONFLICT_IGNORE;
		
		// fix where statement to allow concatenation
		where = where == null ? "" : where;
        
        switch (URI_MATCHER.match(uri)) {
            case URI_CODE_PERSON:
            	tableName = PersonTable.TABLE_NAME;
                break;
            case URI_CODE_PERSON_ID:
            	tableName = PersonTable.TABLE_NAME;
            	// append selection of single item
            	where += PersonTable.Column.ID.getColumnName() + "=" + uri.getLastPathSegment();
                break;
            default:
                Log.w(TAG, "No update defined for: " + uri);
                // we do nothing!
                return 0;
        }
        
        int count = db.updateWithOnConflict(tableName, values,
        		where, whereArgs, conflictAlgorithm);

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		Log.v(TAG, "Delete: " + uri);
		
		SQLiteDatabase db = getSQLiteHelperInstance().getWritableDatabase();

		String tableName = null;
		
		// fix where statement to allow concatenation
        where = where == null ? "" : where;
        
        switch (URI_MATCHER.match(uri)) {
            case URI_CODE_PERSON:
            	tableName = PersonTable.TABLE_NAME;
                break;
            case URI_CODE_PERSON_ID:
            	tableName = PersonTable.TABLE_NAME;
            	// append selection of single item
            	where += PersonTable.Column.ID.getColumnName() + "=" + uri.getLastPathSegment();
                break;
            default:
                Log.w(TAG, "No update defined for: " + uri);
                // we do nothing!
                return 0;
        }
        
        int count = db.delete(tableName, where, whereArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
	}
	
	/**
	 * Get the full mime type string for the given table and content type
	 * @param contentType - {@link PersonContentProvider#CONTENT_TYPE_ITEM} or {@link PersonContentProvider#CONTENT_TYPE_DIR} 
	 * @param tableName
	 * @return
	 */
	private String getMimeType(String contentType, String tableName) {
		// VND_PATH = "vnd.example.rememberme";
		// eg. vnd.android.cursor.item/vnd.example.rememberme.person
		return contentType + VND_PATH + "." + tableName;
	}

	/**
	 * Gets or creates a reusable instance of our {@link SQLiteHelper}
	 * @return
	 */
	private SQLiteHelper getSQLiteHelperInstance() {
		if (mSQLiteHelper == null) {
			mSQLiteHelper = new SQLiteHelper(getContext());
			
			// configure the managed tables
			mSQLiteHelper.addDataTable(new PersonTable());
		}
		
		return mSQLiteHelper;
	}
}
