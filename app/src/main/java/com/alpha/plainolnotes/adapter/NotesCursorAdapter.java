package com.alpha.plainolnotes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.alpha.plainolnotes.R;
import com.alpha.plainolnotes.db.DBOpenHelper;

/**
 * Created by Saran on 4/7/15.
 */
public class NotesCursorAdapter extends CursorAdapter {
    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String noteText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
        int pos = noteText.indexOf(10); //checking for \n. 10 is ascii of new line

        if(pos!=-1){
            noteText = noteText.substring(0,pos)+"....";
        }

        TextView textView = (TextView) view.findViewById(R.id.tvNote);
        textView.setText(noteText);
    }
}
