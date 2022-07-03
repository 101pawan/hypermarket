package com.hypermarket_android.activity;

import android.annotation.SuppressLint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.tabs.TabLayout;
import com.hypermarket_android.Adapter.ImagePagerAdapter;
import com.hypermarket_android.R;
import com.hypermarket_android.dataModel.ProductDetailDataModel;

import java.util.ArrayList;


public class FullscreenImageActivity extends AppCompatActivity {

 ViewPager viewPager;
 TabLayout tabLayout;
 ImageView closeIc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        closeIc = findViewById(R.id.close);
        closeIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String img = getIntent().getStringExtra("image");
        ArrayList<ProductDetailDataModel.ProductData.ImageData> imageList = getIntent().getParcelableArrayListExtra("image");
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(this,imageList);
        viewPager.setAdapter(imagePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        statusBackGroundColor();
        //Glide.with(this).load(img).placeholder(R.drawable.no_image).thumbnail(0.01f)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                //.into(photoView);

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public void statusBackGroundColor() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        getWindow().setBackgroundDrawableResource(R.drawable.gradiant_back);

    }
}