package com.example.bansach;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.Account.LogInActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Hello extends AppCompatActivity {
    ImageView icBookStore;
    TextView appName;
    Animation iconapp, nameapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        getSupportActionBar().hide();
        iconapp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_animation);
        nameapp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_animation);
        findViewById(R.id.iconBookStore).setAnimation(iconapp);
        findViewById(R.id.textBookStore).setAnimation(nameapp);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new
                        Intent(getApplicationContext(), IntroActivity.class));
                finish();
            }
        }, 1200);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, Đào Minh Tâm!");
    }
}
