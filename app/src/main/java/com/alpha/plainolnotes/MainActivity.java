package com.alpha.plainolnotes;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.alpha.plainolnotes.db.DBOpenHelper;
import com.alpha.plainolnotes.db.NotesProvider;
import com.alpha.plainolnotes.utils.QLog;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNote("Sample note");
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
}
