package com.alpha.plainolnotes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alpha.plainolnotes.utils.QLog;

/**
 * Created by Saran on 2/7/15.
 */
public class DBOpenHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String NOTE_ID = "_id";
    public static final String NOTE_TEXT = "noteText";
    public static final String NOTE_CREATED = "noteCreated";

    public static final String[] ALL_COLUMNS = {NOTE_ID,NOTE_TEXT,NOTE_CREATED};

    private static final String TABLE_CREATE =
            "CREATE TABLE "+TABLE_NOTES+" ( "+
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_TEXT + " TEXT " +
                    NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP " +
                    ");";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+
            TABLE_NOTES;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        QLog.d(TABLE_CREATE);
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        QLog.d(DROP_TABLE);
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
