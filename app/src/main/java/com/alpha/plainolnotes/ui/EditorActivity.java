package com.alpha.plainolnotes.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alpha.plainolnotes.R;
import com.alpha.plainolnotes.db.DBOpenHelper;
import com.alpha.plainolnotes.db.NotesProvider;

public class EditorActivity extends ActionBarActivity {

    private EditText mEditor;
    private String mAction;
    private String mNoteFilter;
    private String mOldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mEditor = (EditText) findViewById(R.id.editText);
        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        if(uri == null){
            mAction = Intent.ACTION_INSERT;
            setTitle("New Note");
        }else{
            mAction = Intent.ACTION_EDIT;
            mNoteFilter = DBOpenHelper.NOTE_ID+ "=" +uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(NotesProvider.URI,DBOpenHelper.ALL_COLUMNS,mNoteFilter,null,null);
            cursor.moveToFirst();
            mOldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            mEditor.setText(mOldText);
            mEditor.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mAction.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finishEditting();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finishEditting();
    }

    private void finishEditting(){
        String inputText = mEditor.getText().toString();

        switch (mAction){
            case Intent.ACTION_INSERT:
                if(TextUtils.isEmpty(inputText)){
                    setResult(RESULT_CANCELED);
                }else{
                    insertNote(inputText);
                }
                break;

            case Intent.ACTION_EDIT:
                if(TextUtils.isEmpty(inputText)){
                    //delete notes
                }else if(mOldText.equals(inputText)){
                   setResult(RESULT_CANCELED);
                }else{
                    updateNote(inputText);
                }
                break;
        }
        finish();
    }

    private void updateNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().update(NotesProvider.URI, values, mNoteFilter, null);
        Toast.makeText(this, "Note updated", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().insert(NotesProvider.URI, values);
        setResult(RESULT_OK);
    }
}
