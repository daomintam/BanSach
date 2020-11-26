package com.example.bansach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bansach.R;
import com.example.bansach.ScreenItem;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    Context mContext;
    List<ScreenItem> screenItemList;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> screenItemList) {
        this.mContext = mContext;
        this.screenItemList = screenItemList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen, null);
        ImageView imgSlide = layoutScreen.findViewById(R.id.introImg);
        TextView txtSlide = layoutScreen.findViewById(R.id.textImgIntro);
        txtSlide.setText(screenItemList.get(position).getText());
        imgSlide.setImageResource(screenItemList.get(position).getImage());

        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public int getCount() {
        return screenItemList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
