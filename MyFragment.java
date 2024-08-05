package com.example.notesapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.notesapp.EditNoteActivity;
import com.example.notesapp.NewNoteActivity;
import com.example.notesapp.R;
import java.util.ArrayList;
import java.util.Collections;

public class MyFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notes;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        listView = view.findViewById(R.id.listView);
        notes = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("NotesApp", getActivity().MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // 加载笔记
        loadNotes(username);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到编辑笔记页面
                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                intent.putExtra("note", notes.get(position));
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
                                notes.remove(position);
                                adapter.notifyDataSetChanged();
                                saveNotes(username);
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
                // 跳转到新增笔记页面
                startActivity(new Intent(getActivity(), NewNoteActivity.class));
            }
        });

        return view;
    }

    private void loadNotes(String username) {
        notes.clear();
        int size = sharedPreferences.getInt(username + "_size", 0);
        for (int i = 0; i < size; i++) {
            notes.add(sharedPreferences.getString(username + "_note_" + i, null));
        }
        Collections.sort(notes, Collections.reverseOrder());
    }

    private void saveNotes(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(username + "_size", notes.size());
        for (int i = 0; i < notes.size(); i++) {
            editor.putString(username + "_note_" + i, notes.get(i));
        }
        editor.apply();
    }
}
