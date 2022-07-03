package com.hypermarket_android.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hypermarket_android.R;
import com.hypermarket_android.activity.ProductListActivity;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {


    private int[] GalImages = new int[] {
            R.drawable.main_banner,
            R.drawable.main_banner,
            R.drawable.main_banner
    };
    Context context;

    public MainSliderAdapter(Context context){
        this.context = context;

    }
    @Override
    public int getItemCount() {

        return GalImages.length;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*context.startActivity(new Intent(context, ProductListActivity.class)
                .putExtra("categoryId","127").putExtra("name","New Arrival"));*/

            }
        });
        viewHolder.bindImageSlide(GalImages[position]);//"https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg");
    }
}