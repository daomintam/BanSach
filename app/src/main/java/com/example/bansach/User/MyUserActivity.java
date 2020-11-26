package com.example.bansach.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.ArrayList;

public class MyUserActivity extends AppCompatActivity {
    ImageView avatar;
    ImageButton camera;//
    static TextView ten, sdt, email;
    UserFirebase userFirebase;
    static ArrayList<UserModel> userModelArrayList;
    static UserModel userModel;
    Bitmap selectBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user);
        userFirebase = new UserFirebase(this);
        userModelArrayList = userFirebase.getADMIN();
        ten = findViewById(R.id.tenMyUser);
        sdt = findViewById(R.id.sdtUser);
        email = findViewById(R.id.emailUser);
        findViewById(R.id.avatarUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyUserActivity.this, "Chọn ảnh từ bộ sưu tập để thay đổi avatar", Toast.LENGTH_LONG).show();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 200);//
            }
        });
        findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, 100);
            }
        });
        ImageView avatar = findViewById(R.id.avatarUser);
        if (MainActivity.checkAdmin == true) {
            avatar.setImageResource(R.drawable.my);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //xử lý lấy ảnh trực tiếp lúc chụp hình:
            selectBitmap = (Bitmap) data.getExtras().get("picture");
            avatar.setImageBitmap(selectBitmap);
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                selectBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                avatar.setImageBitmap(selectBitmap);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String userName = email.getText().toString();

                DatabaseReference myRef = database.getReference("NguoiDung");
                myRef.child(userName).child("userName").setValue(userName);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String imgeEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                myRef.child(userName).child("picture").setValue(imgeEncoded);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setAdmin() {
        String user = MainActivity.tenTk;
        for (int i = 0; i < userModelArrayList.size(); i++) {
            if (user.matches(userModelArrayList.get(i).getUserName())) {
                userModel = userModelArrayList.get(i);
                break;
            }
        }
        ten.setText(userModel.getHoTen());
        sdt.setText(userModel.getPhone());
        email.setText(userModel.getUserName());

    }

}
