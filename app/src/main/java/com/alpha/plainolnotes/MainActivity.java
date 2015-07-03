package com.alpha.plainolnotes;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alpha.plainolnotes.adapter.NotesCursorAdapter;
import com.alpha.plainolnotes.db.DBOpenHelper;
import com.alpha.plainolnotes.db.NotesProvider;
import com.alpha.plainolnotes.utils.QLog;


public class MainActivity extends ActionBarActivity
implements LoaderManager.LoaderCallbacks<Cursor>
{
    CursorAdapter mCursorAdapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        mCursorAdapter = new NotesCursorAdapter(this,null,0);
        listView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(0,null,this);
    }

    private void insertNote(String noteText){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,noteText);

        //getContentResolver() is like getResources
        //specify the path of the provider
        Uri noteUri = getContentResolver().insert(NotesProvider.URI, values);
        QLog.d("Inserted note " + noteUri.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_create_sample:
                insertSampleData();
                break;

            case R.id.action_delete_all:
                deleteNotes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteNotes() {
        Toast.makeText(this,"Deleting all notes",Toast.LENGTH_LONG).show();
        getContentResolver().delete(NotesProvider.URI,null,null);
        restartLoader();
    }

    private void insertSampleData() {
        insertNote("Simple note");
        insertNote("Multi-line \n note");
        insertNote("This is a very big line. This is a very big line.This is a very big line.This is a very big line.This is a very big line.This is a very big line.This is a very big line");
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        QLog.d("In MainActivity: onCreateLoader");
        //called when data is needed
        return new CursorLoader(this,NotesProvider.URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        QLog.d("In MainActivity: onLoadFinished");
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        QLog.d("In MainActivity: onLoaderReset");
        mCursorAdapter.swapCursor(null);
    }
}
