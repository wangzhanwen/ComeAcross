package com.lyy_wzw.comeacross.footprint.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.activity.CameraActivity;
import com.lyy_wzw.comeacross.footprint.activity.ShareFootPrintActivity;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.homecommon.BasePopupWindow;
import com.lyy_wzw.comeacross.utils.PixelUtil;
import com.lyy_wzw.imageselector.SelectResultListener;
import com.lyy_wzw.imageselector.utils.ImageSelectorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yidong9 on 17/6/1.
 */

public class ShareFootPrintPopupWin extends BasePopupWindow implements View.OnClickListener {
    private static final String TAG = "FootPrintPopupWin";

    private static final int REQUEST_CODE = 0x00000011;
    private View mContainerView = null ;
    private final RelativeLayout mShootBtn;
    private final TextView mSelectPhotoBtn;
    private Context mContext;
    public  int mSelectImageCount = FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT;

    public void setSelectImageCount(int count){
        mSelectImageCount = count;
    }

    public ShareFootPrintPopupWin(Context context) {
        super(context);
        mContext = context;
        mContainerView = LayoutInflater.from(context).inflate(R.layout.footprint_share_popup_win, null);
        this.setWidth(PixelUtil.dip2px(getContext(), 260));
        this.setHeight(PixelUtil.dip2px(getContext(), 90));
       // mContainerView.setBackgroundColor(Color.WHITE);
        this.setContentView(mContainerView);

        mShootBtn = (RelativeLayout)mContainerView.findViewById(R.id.share_footprint_shoot_btn);
        mSelectPhotoBtn = (TextView)mContainerView.findViewById(R.id.share_footprint_select_photo_btn);

        mShootBtn.setOnClickListener(this);
        mSelectPhotoBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_footprint_shoot_btn:
                Toast.makeText(mContext, "点击拍摄", Toast.LENGTH_SHORT).show();
                //打开摄像机
                Intent intent = new Intent(mContext, CameraActivity.class);
                mContext.startActivity(intent);
                this.dismiss();

                break;
            case R.id.share_footprint_select_photo_btn:
                Toast.makeText(mContext, "点击相册", Toast.LENGTH_SHORT).show();
                //打开图片选择器
                SelectResultListener selectResultListener = new SelectResultListener() {
                    @Override
                    public void onSelectResult(List<String> result) {
                        Log.d(TAG, "imageUrls:"+result.toString());
                        Intent intentShare = new Intent(mContext, ShareFootPrintActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_KEY, (ArrayList) result);
                        intentShare.putExtra(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY, bundle);

                        mContext.startActivity(intentShare);

                    }
                };

                ImageSelectorUtils.openPhoto(mContext, selectResultListener, false, mSelectImageCount);
                this.dismiss();
                break;
        }
    }
}
