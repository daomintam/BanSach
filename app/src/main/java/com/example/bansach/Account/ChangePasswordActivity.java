package com.example.bansach.Account;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.Firebase.UserFirebase;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edPass, edRePass;
    UserFirebase userFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("ĐỔI MẬT KHẨU");
        edPass = (EditText) findViewById(R.id.edPassword);
        edRePass = (EditText) findViewById(R.id.edRePassword);
    }

    public int validateForm() {
        int check = 1;
        if (edPass.getText().length() == 0 || edRePass.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Bạn phải nhập đầy đủ thông ",
                    Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            String pass = edPass.getText().toString();
            String rePass = edRePass.getText().toString();
            if (!pass.equals(rePass)) {
                Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp",
                        Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }

        return check;
    }

    public void changePassword(View view) {
        if (MainActivity.checkAdmin == false) {
            String strUserName = MainActivity.tenTk;
            String pass = edPass.getText().toString();
            userFirebase = new UserFirebase(ChangePasswordActivity.this);

            try {
                if (validateForm() > 0) {
                    userFirebase.changePass(strUserName, pass);
                    Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception ex) {
                Log.e("Error", ex.toString());
            }
        } else {
            Toast.makeText(this, "Admin không được đổi mật khẩu!", Toast.LENGTH_SHORT).show();
        }

    }
}
