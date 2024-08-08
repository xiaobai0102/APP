// Updated MyNotesFragment.java
package com.example.notesapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notesapp.DatabaseManager;
import com.example.notesapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyNotesFragment extends Fragment {

    private ListView listView;
    private Button btnAddNote;
    private DatabaseManager dbManager;
    private int userId;
    private SimpleAdapter adapter;

    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadNotes();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        listView = view.findViewById(R.id.listView);
        btnAddNote = view.findViewById(R.id.btnAddNote);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("NotesApp", getActivity().MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        dbManager = new DatabaseManager(getActivity());
        loadNotes();

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("UPDATE_NOTES");
        getActivity().registerReceiver(updateReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateReceiver);
    }

    private void loadNotes() {
        List<Map<String, String>> notesList = new ArrayList<>();
        List<String[]> notes = dbManager.getNotes(userId);

        for (String[] note : notes) {
            Map<String, String> map = new HashMap<>();
            map.put("title", note[1]);
            notesList.add(map);
        }

        adapter = new SimpleAdapter(getActivity(), notesList, android.R.layout.simple_list_item_1,
                new String[]{"title"}, new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
    }
}
