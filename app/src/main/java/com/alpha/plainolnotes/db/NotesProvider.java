package com.alpha.plainolnotes.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.alpha.plainolnotes.utils.QLog;

/**
 * Created by Saran on 3/7/15.
 */
public class NotesProvider extends ContentProvider{

    private static final String AUTHORITY = "com.alpha.plainolnotes.notesprovider"; //id of provider ->unique
    private static final String BASE_PATH = "notes"; //dataset
    public static final Uri URI = Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = "note";

    //To identify operations
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    //To parse uri and identify operation
    private static final UriMatcher uriMATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    //static will execute first in class
    static {
        QLog.d("In NotesProvider: static");
        uriMATCHER.addURI(AUTHORITY,BASE_PATH,NOTES);
        uriMATCHER.addURI(AUTHORITY,BASE_PATH+"/#",NOTES_ID);
    }
    SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        QLog.d("In NotesProvider: onCreate");
        DBOpenHelper helper = new DBOpenHelper(getContext());
        mDb = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        QLog.d("In NotesProvider: query");

        if(uriMATCHER.match(uri)==NOTES_ID){
            selection = DBOpenHelper.NOTE_ID+"="+uri.getLastPathSegment();
        }
        return mDb.query(DBOpenHelper.TABLE_NOTES,DBOpenHelper.ALL_COLUMNS,selection,null,null,null,DBOpenHelper.NOTE_CREATED+" DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        QLog.d("In NotesProvider: insert");
        //This method must return a URI which should match format BASE_PATH/id
        long id = mDb.insert(DBOpenHelper.TABLE_NOTES,null,values);
        return Uri.parse(BASE_PATH+"/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //return no of rows deleted
        QLog.d("In NotesProvider: delete");
        return mDb.delete(DBOpenHelper.TABLE_NOTES,selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        QLog.d("In NotesProvider: update");
        return mDb.update(DBOpenHelper.TABLE_NOTES,values,selection,selectionArgs);
    }
}
