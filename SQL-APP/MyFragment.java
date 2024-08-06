package com.example.notesapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.notesapp.DatabaseManager;
import com.example.notesapp.EditNoteActivity;
import com.example.notesapp.NewNoteActivity;
import com.example.notesapp.R;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notes;
    private DatabaseManager dbManager;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        listView = view.findViewById(R.id.listView);
        notes = new ArrayList<>();
        dbManager = new DatabaseManager(getActivity());

        userId = getActivity().getSharedPreferences("NotesApp", getActivity().MODE_PRIVATE).getInt("userId", -1);

        loadNotes();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                intent.putExtra("noteId", id);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除笔记")
                        .setMessage("确定要删除这条笔记吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dbManager.deleteNote((int) id)) {
                                    notes.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });

        view.findViewById(R.id.btnAddNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewNoteActivity.class));
            }
        });

        return view;
    }

    private void loadNotes() {
        notes.clear();
        Cursor cursor = dbManager.getNotes(userId);
        while (cursor.moveToNext()) {
            notes.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE_CONTENT)));
        }
        cursor.close();
    }
}
