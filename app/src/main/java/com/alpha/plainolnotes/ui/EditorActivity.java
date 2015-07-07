package com.alpha.plainolnotes.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.alpha.plainolnotes.R;
import com.alpha.plainolnotes.db.DBOpenHelper;
import com.alpha.plainolnotes.db.NotesProvider;

public class EditorActivity extends ActionBarActivity {

    private EditText mEditor;
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mEditor = (EditText) findViewById(R.id.editText);
        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        if(uri == null){
            mAction = Intent.ACTION_INSERT;
        }else{

        }
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
        }
        finish();
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().insert(NotesProvider.URI, values);
        setResult(RESULT_OK);
    }
}
