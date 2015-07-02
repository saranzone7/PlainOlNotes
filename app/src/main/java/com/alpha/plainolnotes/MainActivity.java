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
import android.widget.SimpleCursorAdapter;

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

        insertNote("Sample note");

        Cursor cursor = getContentResolver().query(NotesProvider.URI,DBOpenHelper.ALL_COLUMNS,null,null,null,null);
        //Columns to be displayed in UI
        String[] from = {DBOpenHelper.NOTE_TEXT};
        //ids of ui elements in which data is mapped
        int[] to = {android.R.id.text1};
        mCursorAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,from,to,0);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(mCursorAdapter);
    }

    private void insertNote(String noteText){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,noteText);

        //getContentResolver() is like getResources
        //specify the path of the provider
        Uri noteUri = getContentResolver().insert(NotesProvider.URI, values);
        QLog.d("Inserted note "+noteUri.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
