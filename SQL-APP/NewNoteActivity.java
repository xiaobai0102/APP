package com.example.notesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewNoteActivity extends AppCompatActivity {

    private EditText etNote;
    private Button btnSave;
    private DatabaseManager dbManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        etNote = findViewById(R.id.etNote);
        btnSave = findViewById(R.id.btnSave);
        dbManager = new DatabaseManager(this);

        SharedPreferences sharedPreferences = getSharedPreferences("NotesApp", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteContent = etNote.getText().toString().trim();
                if (TextUtils.isEmpty(noteContent)) {
                    Toast.makeText(NewNoteActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    boolean result = dbManager.addNote(userId, noteContent);
                    if (result) {
                        Toast.makeText(NewNoteActivity.this, "笔记保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NewNoteActivity.this, "笔记保存失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
