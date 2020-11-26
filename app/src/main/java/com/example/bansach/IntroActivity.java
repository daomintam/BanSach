package com.example.bansach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bansach.Account.LogInActivity;
import com.example.bansach.Adapter.IntroViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introAdapter;
    TabLayout tabLayout;
    TextView txtGetStarted;
    ImageButton btnNext, btnGetStarted;
    int positon = 0;
    Animation btnAnim, textAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        if (restorePrefData()) {
            Intent loginActivity = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(loginActivity);
            finish();
        }
        txtGetStarted = findViewById(R.id.txtGetStarted);
        btnNext = findViewById(R.id.btnNext);
        btnGetStarted = findViewById(R.id.btnGetStarted);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        textAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.getstarted_animation);
        tabLayout = findViewById(R.id.tabMode);
        txtGetStarted.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.INVISIBLE);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("", R.drawable.s1));
        mList.add(new ScreenItem("", R.drawable.s2));
        mList.add(new ScreenItem("", R.drawable.s3));
        mList.add(new ScreenItem("", R.drawable.s4));

        screenPager = findViewById(R.id.screenViewPager);
        introAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introAdapter);
        tabLayout.setupWithViewPager(screenPager);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positon = screenPager.getCurrentItem();
                if (positon < mList.size()) {
                    positon++;
                    screenPager.setCurrentItem(positon);
                    btnGetStarted.setVisibility(View.INVISIBLE);
                    txtGetStarted.setVisibility(View.INVISIBLE);

                }
                if (positon == mList.size() - 1) {
                    //Show last Screen
                    loadLastScreen();
                }

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()) {
                    loadLastScreen();
                } else {
                    loadDefaultScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()) {
                    loadLastScreen();
                } else {
                    loadDefaultScreen();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()) {
                    loadLastScreen();
                } else {
                    loadDefaultScreen();
                }
            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(IntroActivity.this, LogInActivity.class);
                startActivity(in);
                savePrefsData();
//                finish();
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefts", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();

    }

    private void loadDefaultScreen() {
        btnNext.setVisibility(View.VISIBLE);
        btnGetStarted.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        txtGetStarted.setVisibility(View.INVISIBLE);

    }

    private void loadLastScreen() {
        btnNext.setVisibility(View.GONE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        btnGetStarted.setAnimation(btnAnim);
        txtGetStarted.setVisibility(View.VISIBLE);
        txtGetStarted.setAnimation(textAnim);

    }

}