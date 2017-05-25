package com.lyy_wzw.comeacross.footprint.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.adapter.ImageViewPagerAdapter;
import com.lyy_wzw.comeacross.footprint.finalvalue.FPPopupWinValue;
import com.lyy_wzw.comeacross.footprint.ui.ImageLookViewPager;
import com.lyy_wzw.comeacross.utils.EasyTransition;
import com.lyy_wzw.comeacross.utils.PixelUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FootPrintImageLookActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "ImageLookActivity";
    private ImageViewPagerAdapter mAdapter;
    private ImageLookViewPager mViewPager;
    private LinearLayout mBottomBtnsContainer;
    private int mSelectPosition = 0;
    private boolean finishEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去掉信息栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print_image_look);
        initView();

    }


    private void initView(){
        mViewPager = (ImageLookViewPager)findViewById(R.id.footprint_imagelook_viewpager);
        mBottomBtnsContainer = (LinearLayout) findViewById(R.id.footprint_imagelook_bottombtns_container);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(FPPopupWinValue.IMAGE_LOOK_TRANSMIT_BUNDLE);
        mSelectPosition = bundle.getInt(FPPopupWinValue.IMAGE_LOOK_SELECT_INDEX);
        ArrayList<String> imageUrls = bundle.getStringArrayList(FPPopupWinValue.IMAGE_LOOK_IMAGE_URLS);

        for (int i = 0; i < imageUrls.size(); i++) {
            mBottomBtnsContainer.addView(getBottomBtn());
        }

        mAdapter = new ImageViewPagerAdapter(getSupportFragmentManager(), imageUrls);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mSelectPosition);
        setBottomSelectedColor(mSelectPosition);

    }

    private CircleImageView getBottomBtn(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PixelUtil.dip2px(this, 8), PixelUtil.dip2px(this,8));
        params.setMargins(PixelUtil.dip2px(this, 10), 0, 0, 0);
        CircleImageView circleImageView = new CircleImageView(this);
        circleImageView.setLayoutParams(params);
        circleImageView.setImageDrawable(new ColorDrawable(Color.parseColor("#009688")));
        //circleImageView.setId(R.id.);
        return circleImageView;
    }

    private void setBottomSelectedColor(int positon){
        for (int i = 0; i < mBottomBtnsContainer.getChildCount(); i++) {
            CircleImageView childView = (CircleImageView)mBottomBtnsContainer.getChildAt(i);
            if (i == positon) {
                childView.setImageDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            }else{
                childView.setImageDrawable(new ColorDrawable(Color.parseColor("#009688")));
            }
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mSelectPosition = position;
        setBottomSelectedColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
