package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {

    private EditText etNote;
    private Button btnSave;
    private DatabaseManager dbManager;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        etNote = findViewById(R.id.etNote);
        btnSave = findViewById(R.id.btnSave);
        dbManager = new DatabaseManager(this);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);
        String noteContent = intent.getStringExtra("noteContent");
        etNote.setText(noteContent);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newContent = etNote.getText().toString().trim();
                if (TextUtils.isEmpty(newContent)) {
                    Toast.makeText(EditNoteActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    boolean result = dbManager.updateNote(noteId, newContent);
                    if (result) {
                        Toast.makeText(EditNoteActivity.this, "笔记更新成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditNoteActivity.this, "笔记更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
