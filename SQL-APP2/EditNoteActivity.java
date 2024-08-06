package com.example.notesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {
    private EditText titleEditText, contentEditText;
    private Button saveButton, cancelButton;
    private SQLiteDatabase db;
    private String username, noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleEditText = findViewById(R.id.title);
        contentEditText = findViewById(R.id.content);
        saveButton = findViewById(R.id.save);
        cancelButton = findViewById(R.id.cancel);

        NotesDBHelper dbHelper = new NotesDBHelper(this);
        db = dbHelper.getWritableDatabase();

        username = getIntent().getStringExtra("username");
        noteContent = getIntent().getStringExtra("note");

        if (noteContent != null) {
            // 编辑模式，填充现有内容
            String[] parts = noteContent.split("\n", 2);
            titleEditText.setText(parts[0]);
            contentEditText.setText(parts[1]);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    Toast.makeText(EditNoteActivity.this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (noteContent == null) {
                    addNote(title, content);
                } else {
                    updateNote(title, content);
                }

                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addNote(String title, String content) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("title", title);
        values.put("content", content);
        values.put("timestamp", System.currentTimeMillis());
        db.insert("notes", null, values);
    }

    private void updateNote(String title, String content) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        db.update("notes", values, "username=? AND content=?", new String[]{username, noteContent});
    }
}
