package com.example.bansach.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.R;
import com.example.bansach.Firebase.UserFirebase;
import com.example.bansach.Model.UserModel;
import com.example.bansach.User.MyUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class SignInActivity extends AppCompatActivity {
    Button btnThemNguoiDung, btnHuy;
    //    NguoiDungDAO nguoiDungDAO;
    EditText edUser, edPass, edRePass, edPhone, edFullName;
    ImageView picture;
    Bitmap selectBitmap;
    private FirebaseAuth mAuth;
    UserFirebase userFirebase;
    ArrayList<UserModel> userModelArrayList;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("ĐĂNG KÝ");
        btnThemNguoiDung = (Button) findViewById(R.id.btnAddUser);

        edUser = (EditText) findViewById(R.id.edUserName);
        edPass = (EditText) findViewById(R.id.edPassword);
        edPhone = (EditText) findViewById(R.id.edPhone);
        edFullName = (EditText) findViewById(R.id.edFullName);
        edRePass = (EditText) findViewById(R.id.edRePassword);
        picture = null;
        mAuth = FirebaseAuth.getInstance();
        userFirebase = new UserFirebase(SignInActivity.this);
        getAll();

        //huy
        findViewById(R.id.btnCancelUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
    }

    public void showUsers(View view) {
        finish();
    }

    public void addUser(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        nguoiDungDAO = new NguoiDungDAO(ThemNguoiDung.this);
        DatabaseReference myRef = database.getReference("NguoiDung");
        String email = edUser.getText().toString();
        String mk = edPass.getText().toString();
        String sdt = edPhone.getText().toString();
        String hoTen = edFullName.getText().toString();

        // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // selectBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        //  byte[] byteArray = byteArrayOutputStream .toByteArray();
        //   String imgeEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //myRef.child(email).child("picture").setValue(null);

//        UserModel userModel = new UserModel(edUser.getText().toString(),
//                edPass.getText().toString(),
//                edPhone.getText().toString(), edFullName.getText().toString(), null);
        try {
            if (email.isEmpty() || mk.isEmpty() || sdt.isEmpty() || hoTen.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!",
                        Toast.LENGTH_SHORT).show();
            }
            if (email.isEmpty()) {
                edUser.setError("Vui lòng nhập email!");
            }
            if (mk.isEmpty()) {
                edPass.setError("Vui lòng nhập email!");
            }
            if (sdt.isEmpty()) {
                edPhone.setError("Vui lòng nhập số điện thoại!");
            }
            if (hoTen.isEmpty()) {
                edFullName.setError("Vui lòng họ tên!");
            } else if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
                Toast.makeText(getApplicationContext(), "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
            } else {
                String pass = edPass.getText().toString();
                String rePass = edRePass.getText().toString();
                if (!pass.equals(rePass)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp!",
                            Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 6 ký tự!",
                            Toast.LENGTH_SHORT).show();
                } else if (edPhone.getText().toString().length() < 10 || edPhone.getText().toString().length() > 11) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_SHORT).show();
                } else {
                    UserModel user = new UserModel(email, mk, sdt, hoTen);
                    if (xetTrung(email) == true) {
                        Toast.makeText(getApplicationContext(), "Tài khoản đã tồn tại!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (userFirebase.insert(user)) {
                            Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            }
        } catch (
                Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    //Xét trùng mã
    public boolean xetTrung(String user) {
        Boolean xet = false;
        for (int i = 0; i < userModelArrayList.size(); i++) {
            String ma = userModelArrayList.get(i).getUserName();
            if (ma.equalsIgnoreCase(user)) {
                xet = true;
                break;
            }
        }
        return xet;
    }

    public ArrayList<UserModel> getAll() {
        userModelArrayList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("NguoiDung").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userModelArrayList.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        UserModel user = next.getValue(UserModel.class);
                        userModelArrayList.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return userModelArrayList;
    }
}
