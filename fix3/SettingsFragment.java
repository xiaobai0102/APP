package com.example.notesapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notesapp.DatabaseManager;
import com.example.notesapp.LoginActivity;
import com.example.notesapp.R;

import java.io.InputStream;

public class SettingsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfilePicture;
    private EditText etNickname, etPassword, etConfirmPassword;
    private Button btnSave, btnLogout;
    private SharedPreferences sharedPreferences;
    private DatabaseManager dbManager;
    private int userId;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        etNickname = view.findViewById(R.id.etNickname);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnSave = view.findViewById(R.id.btnSave);
        btnLogout = view.findViewById(R.id.btnLogout);

        sharedPreferences = getActivity().getSharedPreferences("NotesApp", getActivity().MODE_PRIVATE);
        dbManager = new DatabaseManager(getActivity());

        username = sharedPreferences.getString("username", "");
        userId = sharedPreferences.getInt("userId", -1);
        etNickname.setText(sharedPreferences.getString(username + "_nickname", ""));

        // Load profile picture
        loadProfilePicture();

        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery to choose profile picture
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

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
                        dbManager.updateUserPassword(userId, password);
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
                // Clear login status
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.remove("username");
                editor.remove("userId");
                editor.apply();

                // Redirect to login page
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivProfilePicture.setImageBitmap(bitmap);
                // Save the selected image (implement this as needed)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProfilePicture() {
        // Implement logic to load the saved profile picture (if any)
    }
}
