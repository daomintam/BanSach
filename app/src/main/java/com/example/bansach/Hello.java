package com.example.bansach;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.Account.LogInActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Hello extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new
                        Intent(getApplicationContext(), SlideShow.class));
                finish();
            }
        }, 1200);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, Đào Minh Tâm!");
    }
}
