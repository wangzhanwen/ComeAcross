package com.lyy_wzw.comeacross.footprint.cards;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.utils.DecodeBitmapTask;
import com.lyy_wzw.imageselector.entry.Image;


public class SliderCard extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public  ImageView playBtn;


    public SliderCard(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.card_slider_image);
        playBtn = (ImageView) itemView.findViewById(R.id.card_slider_play_btn);
    }


}