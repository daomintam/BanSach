package com.example.bansach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.bansach.BestSeller.BestSellerActivity;
import com.example.bansach.Firebase.UserFirebase;
import com.example.bansach.Bill.ListBillActivity;
import com.example.bansach.Account.ChangePasswordActivity;
import com.example.bansach.Account.LogInActivity;
import com.example.bansach.Model.UserModel;
import com.example.bansach.User.EditUserActivity;
import com.example.bansach.User.ListUserActivity;
import com.example.bansach.User.MyUserActivity;
import com.example.bansach.Book.ListBookActivity;
import com.example.bansach.Category.ListCategoryActivity;
import com.example.bansach.Statistics.StatisticsActivity;
import com.example.bansach.LoadingDialog;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView nguoidung, theloai, sach, bill, banchay, thongke, doimk, dangxuat;
    static TextView hello;
    public static String tenTk = "";
    UserFirebase userFireBase;
    static ArrayList<UserModel> userModelArrayList;
    public static Boolean checkAdmin;
    LoadingDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setTitle("QUẢN LÝ SÁCH");
        Intent i = getIntent();
        tenTk = i.getStringExtra("user");
        userFireBase = new UserFirebase(this);
        userModelArrayList = userFireBase.getAllNone();

        init();


        findViewById(R.id.btnCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, EditUserActivity.class);
                startActivity(in);
            }
        });
        load = new LoadingDialog(this);
    }

    public void checkAdmin() {
        if (tenTk.matches("minhtam030797@gmail.com")) {
            hello.setText("Admin");
            checkAdmin = true;
            banchay.isShown();
            banchay.setVisibility(View.VISIBLE);
            thongke.setVisibility(View.VISIBLE);
            doimk.setVisibility(View.GONE);
            ((android.widget.TextView) findViewById(R.id.textHello)).setText("Admin: ");
            ((android.widget.TextView) findViewById(R.id.tenUser)).setTextColor(Color.RED);

        } else {
            checkAdmin = false;
            theloai.setVisibility(View.GONE);
            banchay.setVisibility(View.GONE);
            thongke.setVisibility(View.GONE);
            ((android.widget.TextView) findViewById(R.id.textButton)).setText("Thông tin người dùng");
        }
        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, MyUserActivity.class);
                startActivity(in);
            }
        });
    }

    public static void setTenTk() {
        for (int i = 0; i < userModelArrayList.size(); i++) {
            String ten = userModelArrayList.get(i).getUserName();
            if (ten.matches(tenTk)) {
                if (userModelArrayList.get(i).getHoTen().isEmpty()) {
                    hello.setText(tenTk);
                } else {
                    hello.setText(userModelArrayList.get(i).getHoTen());
                }
                break;
            }
        }
    }

    private void init() {
        nguoidung = findViewById(R.id.mnnguoidung);
        theloai = findViewById(R.id.mntheloai);
        sach = findViewById(R.id.mnbook);
        bill = findViewById(R.id.mnhoadon);
        banchay = findViewById(R.id.mnbanchay);
        thongke = findViewById(R.id.mnthongke);
        doimk = findViewById(R.id.mndoimk);
        dangxuat = findViewById(R.id.mndangxuat);
        hello = findViewById(R.id.tenUser);

        checkAdmin();

        nguoidung.setOnClickListener(this);
        theloai.setOnClickListener(this);
        sach.setOnClickListener(this);
        bill.setOnClickListener(this);
        banchay.setOnClickListener(this);
        thongke.setOnClickListener(this);
        doimk.setOnClickListener(this);
        dangxuat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Activity x = null;
            switch (v.getId()) {
                case R.id.mnnguoidung:
                    if (MainActivity.checkAdmin == false) {
                        //load.startLoad(2);
                        x = new MyUserActivity();
                        break;
                    } else {
                        // load.startLoad(2);
                        x = new ListUserActivity();
                        // x = new MyUserActivity();
                        break;
                    }
                case R.id.mntheloai:

                    x = new ListCategoryActivity();
                    break;
                case R.id.mnbook:
                    x = new ListBookActivity();
                    break;
                case R.id.mnhoadon:
                    x = new ListBillActivity();
                    break;
                case R.id.mnbanchay:
                    x = new BestSellerActivity();
                    break;
                case R.id.mnthongke:
                    x = new StatisticsActivity();
                    break;
                case R.id.mndoimk:
                    x = new ChangePasswordActivity();
                    break;
                case R.id.mndangxuat:
                    x = new LogInActivity();
                    break;
            }
            Intent i = new Intent(this, x.getClass());
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
