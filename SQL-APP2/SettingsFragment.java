package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    private EditText nicknameEditText, passwordEditText, confirmPasswordEditText;
    private Button saveButton, logoutButton;
    private SQLiteDatabase db;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        nicknameEditText = view.findViewById(R.id.nickname);
        passwordEditText = view.findViewById(R.id.password);
        confirmPasswordEditText = view.findViewById(R.id.confirmPassword);
        saveButton = view.findViewById(R.id.save);
        logoutButton = view.findViewById(R.id.logout);

        NotesDBHelper dbHelper = new NotesDBHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            username = activity.getIntent().getStringExtra("username");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (!TextUtils.isEmpty(password) && !password.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "密码和确认密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                if (!TextUtils.isEmpty(nickname)) {
                    values.put("nickname", nickname);
                }
                if (!TextUtils.isEmpty(password)) {
                    values.put("password", password);
                }

                if (values.size() > 0) {
                    db.update("users", values, "username=?", new String[]{username});
                    Toast.makeText(getActivity(), "信息已更新", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "没有要更新的信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        return view;
    }
}
