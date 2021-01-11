package com.leyline.computervision

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {
    private var relativeLayout: RelativeLayout? = null
    private var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        relativeLayout = findViewById(R.id.relative)
        viewPager = findViewById(R.id.slideViewPager)
    }
}