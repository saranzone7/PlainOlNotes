package com.alpha.plainolnotes.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Saran on 3/7/15.
 */
public class NotesProvider extends ContentProvider{

    private static final String AUTHORITY = "com.alpha.plainolnotes.notesprovider"; //id of provider ->unique
    private static final String BASE_PATH = "notes"; //dataset
    private static final Uri URI = Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);

    //To identify operations
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    //To parse uri and identify operation
    private static final UriMatcher uriMATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    //static will execute first in class
    static {
        uriMATCHER.addURI(AUTHORITY,BASE_PATH,NOTES);
        uriMATCHER.addURI(AUTHORITY,BASE_PATH+"/#",NOTES_ID);
    }
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        db = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
