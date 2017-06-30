package com.lyy_wzw.comeacross.discovery.activitys;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;


import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.discovery.adapter.CircleRecyclerViewAdapter;

import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.server.FootPrintServer;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.GlideUtil;


import java.util.List;

import cn.bmob.v3.exception.BmobException;


public class FootPrintCircleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FootPrintCircleActivity";
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private ImageView mShareFootPrintbtn;
    private ImageView mBackbtn;
    private RecyclerView mRecyClerView;
    private List<FootPrint> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print_circle);
        initView();

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.circle_toolbar);
        mBackbtn = (ImageView) findViewById(R.id.circle_back_btn);
        mShareFootPrintbtn = (ImageView) findViewById(R.id.circle_share_footprint_btn);
        mRecyClerView = (RecyclerView) findViewById(R.id.circle_recyclerView);

        mBackbtn.setOnClickListener(this);
        mShareFootPrintbtn.setOnClickListener(this);

        FootPrintServer.getInstance().getAll(new FootPrintServer.FootPrintQueryCallback() {
            @Override
            public void onSuccess(List<FootPrint> footPrints) {
                Log.d(TAG, "footPrints:"+footPrints.toString());
                if (footPrints != null){
                    mDatas = footPrints;
                    mRecyClerView.setLayoutManager(new LinearLayoutManager(FootPrintCircleActivity.this, LinearLayoutManager.VERTICAL, false));
                    //mRecyClerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
                    mRecyClerView.setAdapter(new CircleRecyclerViewAdapter(FootPrintCircleActivity.this, mDatas));
                }
            }

            @Override
            public void onError(BmobException e) {
                Log.d(TAG, "e:"+e.getMessage());
            }
        });



        setSupportActionBar(mToolbar);
        mToolbar.setTitle("足迹圈");

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        CAUser currentUser = UserHelper.getInstance().getCurrentUser();
        GlideUtil.loadCirclePic(this,
                 currentUser.getUserPhoto(),
                 mFab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle_back_btn:
                finish();
                break;
            case R.id.circle_share_footprint_btn:
                ShareFootPrintPopupWin shareFootPrintPW = new ShareFootPrintPopupWin(this);
                shareFootPrintPW.setSelectImageCount(FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT);
                shareFootPrintPW.showAtLocation(mShareFootPrintbtn, Gravity.CENTER, 0, 0);
                break;
        }
    }

}
