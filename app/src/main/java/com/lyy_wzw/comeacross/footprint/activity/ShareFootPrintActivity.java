package com.lyy_wzw.comeacross.footprint.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.adapter.FPShareWinGridViewAdapter;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;

import java.util.ArrayList;
import java.util.List;


public class ShareFootPrintActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView mImagesGridView;
    private List<String> mImageUrls;
    private FPShareWinGridViewAdapter mAdapter;
    private FrameLayout btnBack;
    private FrameLayout btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_foot_print);

        mImagesGridView = (GridView) findViewById(R.id.share_footprint_grid_view);
        btnBack = (FrameLayout) findViewById(R.id.share_footprint_btn_back);
        btnSend = (FrameLayout) findViewById(R.id.share_self_btn_send);
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY);
        mImageUrls = bundle.getStringArrayList(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_KEY);
        mAdapter = new FPShareWinGridViewAdapter(this, R.layout.footprint_sharewin_gridview_item, mImageUrls);

        Log.d("ShareFootPrintActivity", "onCreate->mImageUrls:" + mImageUrls);

        mImagesGridView.setAdapter(mAdapter);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle = intent.getBundleExtra(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY);
        if (bundle != null) {
            ArrayList<String> addImageUrls = bundle.getStringArrayList(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_KEY);
            Log.d("ShareFootPrintActivity", "onNewIntent->addImageUrls:" + addImageUrls);
            if (addImageUrls != null) {
                for (int i = 0; i < addImageUrls.size(); i++) {
                    if (mImageUrls.size() < FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT + 1) {
                        mImageUrls.add(mImageUrls.size() - 1, addImageUrls.get(i));

                    } else {
                        Snackbar.make(mImagesGridView, "最多只能选择" + FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT + "张图片.", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                }

            }
        }

        mAdapter.notifyDataSetChanged();

    }

    public static Handler mShareHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FootPrintConstantValue.SHARE_IMAGEURLS_HANDLE_KEY:

                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_footprint_btn_back:
                finish();
                break;
            case R.id.share_self_btn_send:
                //TODO: 上传服务器分享数据

                Toast.makeText(this, "发送。。。", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

    }
}
