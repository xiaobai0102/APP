package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public boolean registerUser(String username, String password, String nickname) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_NICKNAME, nickname);

        long result = database.insert(DatabaseHelper.TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                null,
                DatabaseHelper.COLUMN_USERNAME + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null,
                null,
                null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int getUserId(String username) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID},
                DatabaseHelper.COLUMN_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null
        );
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID));
        }
        cursor.close();
        return userId;
    }

    public Cursor getNotes(int userId) {
        return database.query(
                DatabaseHelper.TABLE_NOTES,
                null,
                DatabaseHelper.COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                DatabaseHelper.COLUMN_TIMESTAMP + " DESC"
        );
    }

    public boolean addNote(int userId, String content) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);
        values.put(DatabaseHelper.COLUMN_NOTE_CONTENT, content);

        long result = database.insert(DatabaseHelper.TABLE_NOTES, null, values);
        return result != -1;
    }

    public boolean updateNote(int noteId, String content) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE_CONTENT, content);

        int rows = database.update(
                DatabaseHelper.TABLE_NOTES,
                values,
                DatabaseHelper.COLUMN_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)}
        );
        return rows > 0;
    }

    public boolean deleteNote(int noteId) {
        int rows = database.delete(
                DatabaseHelper.TABLE_NOTES,
                DatabaseHelper.COLUMN_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)}
        );
        return rows > 0;
    }
}
