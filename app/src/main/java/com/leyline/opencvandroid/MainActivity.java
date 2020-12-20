package com.leyline.opencvandroid;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        relativeLayout = findViewById(R.id.relative);
        relativeLayout.setBackgroundResource(R.drawable.background);
        viewPager = findViewById(R.id.slideViewPager);
    }
}