package com.example.bansach.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.Firebase.UserFirebase;
import com.example.bansach.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditUserActivity extends AppCompatActivity {
    EditText edFullName, edPhone, edEmail;
    String username, fullname, phone, pass;

    UserFirebase userFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sua_nguoi_dung);
        setTitle("CHI TIẾT NGƯỜI DÙNG");
        edFullName = (EditText) findViewById(R.id.edFullName);
        edPhone = (EditText) findViewById(R.id.edPhone);
        edEmail = findViewById(R.id.edEmail);
        userFirebase = new UserFirebase(EditUserActivity.this);
        ImageView avatar = findViewById(R.id.avatarUserEdit);
        avatar.setImageResource(R.drawable.pin);
        init();

    }

    public void init() {
        Intent in = getIntent();
        Bundle b = in.getExtras();
        fullname = b.getString("FULLNAME");
        phone = b.getString("PHONE");
        username = b.getString("USERNAME");
        pass = b.getString("PASSWORD");
        edFullName.setText(fullname);
        edPhone.setText(phone);
        edEmail.setText(username);
        edEmail.setKeyListener(null);
        edEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Không được phép thay đổi", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateUser(View view) {
        String hoTen, sdt, username;
        hoTen = edFullName.getText().toString();
        sdt = edPhone.getText().toString();
        username = edEmail.getText().toString();

        if (hoTen.isEmpty() || sdt.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (sdt.length() < 10 || sdt.length() > 12) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_SHORT).show();
        } else {
            UserModel userModel = new UserModel(username, pass, sdt, hoTen);
            userFirebase.update(userModel);
            Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void Huy(View view) {
        finish();
    }
}
