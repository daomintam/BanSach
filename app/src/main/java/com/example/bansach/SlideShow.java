package com.example.bansach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bansach.Account.LogInActivity;

import java.util.ArrayList;
import java.util.List;

public class SlideShow extends AppCompatActivity {
    public ImageButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        ImageSlider imageSlider = findViewById(R.id.slider);
        List<SlideModel> slideModelList = new ArrayList<>();
        slideModelList.add(new SlideModel((R.drawable.s1), ""));
        slideModelList.add(new SlideModel((R.drawable.s3), ""));
//        slideModelList.add(new SlideModel((R.drawable.s4),""));
//        slideModelList.add(new SlideModel((R.drawable.s5),""));
//        slideModelList.add(new SlideModel((R.drawable.s6),""));
        next = findViewById(R.id.btnNext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SlideShow.this, LogInActivity.class);
                startActivity(in);
            }
        });

        imageSlider.setImageList(slideModelList, true);
    }
}