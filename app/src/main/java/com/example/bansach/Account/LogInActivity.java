package com.example.bansach.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.LoadingDialog;
import com.example.bansach.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class LogInActivity extends AppCompatActivity {
    EditText edUserName;
    TextInputEditText edPassword;
    Button btnLogin, btnCancel, btnAdd;
    CheckBox chkRememberPass;
    String strUser, strPass;
    private FirebaseAuth mAuth;
    ArrayList<UserModel> userModelArrayList;
    LoadingDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("ĐĂNG NHẬP");

        load = new LoadingDialog(this);
        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        chkRememberPass = (CheckBox) findViewById(R.id.check);
        mAuth = FirebaseAuth.getInstance();
        autoFill();
        getAll();
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogInActivity.this, SignInActivity.class);
                ;
                startActivity(i);
            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });

    }

    public void checkLogin(View v) {
        load.startLoad(1);
        strUser = edUserName.getText().toString();
        strPass = edPassword.getText().toString();
        if (strUser.isEmpty()) {
            edUserName.setError("Vui lòng nhập tài khoản");
            return;
        } else if (strPass.isEmpty()) {
            edPassword.setError("Vui lòng nhập mật khẩu");
            return;
        } else {
            //Đăng nhập bằng FireBase
            mAuth.signInWithEmailAndPassword(strUser, strPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                rememberUser(strUser, strPass, chkRememberPass.isChecked());
                                Toast.makeText(LogInActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(LogInActivity.this, MainActivity.class);
                                i.putExtra("user", strUser);
                                startActivity(i);
                            } else {
                                Boolean ok = false;

                                for (int i = 0; i < userModelArrayList.size(); i++) {
                                    String tk = userModelArrayList.get(i).getUserName();
                                    String mk = userModelArrayList.get(i).getPassword();
                                    if (tk.matches(strUser) && mk.matches(strPass)) {
                                        ok = true;
                                        break;
                                    }
                                }
                                if (ok == true) {
                                    rememberUser(strUser, strPass, chkRememberPass.isChecked());
                                    Toast.makeText(LogInActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                                    Intent a = new Intent(LogInActivity.this, MainActivity.class);
                                    a.putExtra("user", strUser);
                                    startActivity(a);
                                } else {
                                    Toast.makeText(LogInActivity.this, "Tên tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_LONG).show();
                                    load.dismisLoad();
                                }
                            }

                        }
                    });
        }
    }

    private void autoFill() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("REMEMBER", false);
        if (check) {
            String tenNguoiDung = sharedPreferences.getString("USERNAME", "");
            String matKhau = sharedPreferences.getString("PASSWORD", "");
            edUserName.setText(tenNguoiDung);
            edPassword.setText(matKhau);
        } else {
            edUserName.setText("");
            edPassword.setText("");
        }
        chkRememberPass.setChecked(check);
    }

    public void rememberUser(String u, String p, boolean status) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        if (!status) {
            //xoa tinh trang luu tru truoc do
            edit.clear();
        } else {
            //luu du lieu
            edit.putString("USERNAME", u);
            edit.putString("PASSWORD", p);
            edit.putBoolean("REMEMBER", status);
        }
        //luu lai toan bo
        edit.commit();
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
                        UserModel userModel = next.getValue(UserModel.class);
                        userModelArrayList.add(userModel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LogInActivity.this, "Lấy người dùng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        return userModelArrayList;
    }
}
