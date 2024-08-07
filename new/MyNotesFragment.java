package com.example.notesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MyNotesFragment extends Fragment {
    private SQLiteDatabase db;
    private ListView listView;
    private NoteAdapter adapter;
    private ArrayList<Note> notesList;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_notes, container, false);

        // 初始化数据库
        NotesDBHelper dbHelper = new NotesDBHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        listView = view.findViewById(R.id.notes_list);
        Button addButton = view.findViewById(R.id.add_note_button);

        // 获取用户名
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            username = activity.getIntent().getStringExtra("username");
        }

        notesList = new ArrayList<>();
        adapter = new NoteAdapter(getActivity(), R.layout.note_item, notesList);
        listView.setAdapter(adapter);

        loadNotes();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到新增笔记页面
                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到编辑笔记页面
                Note note = notesList.get(position);
                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                intent.putExtra("title", note.getTitle());
                intent.putExtra("content", note.getContent());
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除笔记")
                        .setMessage("确定要删除这条笔记吗？")
                        .setPositiveButton("删除", (dialog, which) -> {
                            Note note = notesList.get(position);
                            deleteNote(note.getTitle(), note.getContent());
                            notesList.remove(position);
                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });

        return view;
    }

    private void loadNotes() {
        notesList.clear();
        Cursor cursor = db.query("notes", null, "username=?", new String[]{username}, null, null, "timestamp DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                notesList.add(new Note(title, content));
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    private void deleteNote(String title, String content) {
        db.delete("notes", "username=? AND title=? AND content=?", new String[]{username, title, content});
    }
}
