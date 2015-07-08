package com.alpha.plainolnotes.ui;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.alpha.plainolnotes.R;
import com.alpha.plainolnotes.adapter.NotesCursorAdapter;
import com.alpha.plainolnotes.db.DBOpenHelper;
import com.alpha.plainolnotes.db.NotesProvider;
import com.alpha.plainolnotes.utils.QLog;


public class MainActivity extends ActionBarActivity
implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int EDITOR_REQUEST_CODE = 1;
    CursorAdapter mCursorAdapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(android.R.id.list);
        mCursorAdapter = new NotesCursorAdapter(this,null,0);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*id corresponds to _id that we used in db. ContentProvider gets that and make
                _id long id
                */
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                Uri uri = Uri.parse(NotesProvider.URI+ "/" +id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE,uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });
        getLoaderManager().initLoader(0,null,this);
    }

    private void insertNote(String noteText){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);

        //getContentResolver() is like getResources
        //specify the path of the provider
        Uri noteUri = getContentResolver().insert(NotesProvider.URI, values);
        QLog.d("Inserted note " + noteUri.getLastPathSegment());
    }


    public void openEditorNote(View view){
        startActivityForResult(new Intent(this, EditorActivity.class), EDITOR_REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDITOR_REQUEST_CODE && resultCode==RESULT_OK){
            restartLoader();
        }
    }
}
