package com.example.notesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class NewNoteActivity extends AppCompatActivity {

    private EditText etNote;
    private Button btnSave;
    private SharedPreferences sharedPreferences;
    private String username;
    private ArrayList<String> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        etNote = findViewById(R.id.etNote);
        btnSave = findViewById(R.id.btnSave);
        sharedPreferences = getSharedPreferences("NotesApp", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        notes = new ArrayList<>();
        loadNotes();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = etNote.getText().toString().trim();
                if (TextUtils.isEmpty(note)) {
                    Toast.makeText(NewNoteActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    notes.add(note);
                    saveNotes();
                    finish();
                }
            }
        });
    }

    private void loadNotes() {
        notes.clear();
        int size = sharedPreferences.getInt(username + "_size", 0);
        for (int i = 0; i < size; i++) {
            notes.add(sharedPreferences.getString(username + "_note_" + i, null));
        }
        Collections.sort(notes, Collections.reverseOrder());
    }

    private void saveNotes() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(username + "_size", notes.size());
        for (int i = 0; i < notes.size(); i++) {
            editor.putString(username + "_note_" + i, notes.get(i));
        }
        editor.apply();
    }
}

public class EditNoteActivity extends AppCompatActivity {

    private EditText etNote;
    private Button btnSave;
    private SharedPreferences sharedPreferences;
    private String username;
    private ArrayList<String> notes;
    private String originalNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        etNote = findViewById(R.id.etNote);
        btnSave = findViewById(R.id.btnSave);
        sharedPreferences = getSharedPreferences("NotesApp", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        notes = new ArrayList<>();
        loadNotes();

        Intent intent = getIntent();
        originalNote = intent.getStringExtra("note");
        etNote.setText(originalNote);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = etNote.getText().toString().trim();
                if (TextUtils.isEmpty(note)) {
                    Toast.makeText(EditNoteActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    notes.remove(originalNote);
                    notes.add(note);
                    saveNotes();
                    finish();
                }
            }
        });
    }

    private void loadNotes() {
        notes.clear();
        int size = sharedPreferences.getInt(username + "_size", 0);
        for (int i = 0; i < size; i++) {
            notes.add(sharedPreferences.getString(username + "_note_" + i, null));
        }
        Collections.sort(notes, Collections.reverseOrder());
    }

    private void saveNotes() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(username + "_size", notes.size());
        for (int i = 0; i < notes.size(); i++) {
            editor.putString(username + "_note_" + i, notes.get(i));
        }
        editor.apply();
    }
}
