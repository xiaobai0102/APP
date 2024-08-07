package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    private int resourceId;

    public NoteAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Note note = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView titleTextView = view.findViewById(R.id.note_title);
        TextView contentTextView = view.findViewById(R.id.note_content);

        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());

        // 设置每个item最多显示两行数据，超过两行用省略号表示
        contentTextView.setMaxLines(2);
        contentTextView.setEllipsize(TextUtils.TruncateAt.END);

        return view;
    }
}
