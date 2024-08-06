package com.example.notesapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.notesapp.LoginActivity;
import com.example.notesapp.R;

public class SettingsFragment extends Fragment {

    private EditText etNickname, etPassword, etConfirmPassword;
    private Button btnSave, btnLogout;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        etNickname = view.findViewById(R.id.etNickname);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnSave = view.findViewById(R.id.btnSave);
        btnLogout = view.findViewById(R.id.btnLogout);

        sharedPreferences = getActivity().getSharedPreferences("NotesApp", getActivity().MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        etNickname.setText(sharedPreferences.getString(username + "_nickname", ""));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = etNickname.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(username + "_nickname", nickname);
                    if (!TextUtils.isEmpty(password)) {
                        editor.putString(username, password);
                    }
                    editor.apply();
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 退出登录
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.remove("username");
                editor.apply();

                // 跳转到登录页面
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
