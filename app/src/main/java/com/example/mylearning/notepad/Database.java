package com.example.mylearning.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String NAME = "database";
    private static final String TABLE = "note";

    private static final String ID = "Id";
    private static final String TITLE = "Title";
    private static final String CONTENT = "Content";
    private static final String DATE = "Date";
    private static final String TIME = "Time";

    Database(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE + "(" + ID +
                " INT PRIMARY KEY," + TITLE + " TEXT, " + CONTENT
                + " TEXT," + DATE + " TEXT," + TIME + " TEXT" + ")";


        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, note.getTitle());
        contentValues.put(CONTENT, note.getContent());
        contentValues.put(DATE, note.getDate());
        contentValues.put(TIME, note.getTime());

        long id = db.insert(TABLE, null, contentValues);
        //  Toast.makeText(this, "Inserted" + "ID: " + id, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();


        Log.d("Inserted", "ID: " + id);
        return id;
    }

    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{ID, TITLE, CONTENT, DATE, TIME},
                ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();



        }
        Note note = new Note(cursor.getLong(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return note;
        //return null;
    }

    public List<Note> getNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE;
        Cursor cursor = db.rawQuery(query, null);
        //  if (cursor!=null) {
        if (cursor.moveToFirst()) {

            do {
                Note note = new Note();
                note.setId(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                allNotes.add(note);

            } while (cursor.moveToNext());
        }
        return allNotes;
    }

}
