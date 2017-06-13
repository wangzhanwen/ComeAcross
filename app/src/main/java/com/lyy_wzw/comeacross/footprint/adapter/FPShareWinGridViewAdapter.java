package com.lyy_wzw.comeacross.footprint.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.activity.FootPrintImageLookActivity;
import com.lyy_wzw.comeacross.footprint.finalvalue.FPPopupWinValue;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.utils.PixelUtil;
import com.lyy_wzw.imageselector.SelectResultListener;
import com.lyy_wzw.imageselector.utils.ImageSelectorUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yidong9 on 17/6/1.
 */

public class FPShareWinGridViewAdapter extends WzwBaseAdapter<String>{
    private static final String TAG = "ShareWinGridViewAdapter";

    private List<String> mImageUrls;
    private WzwViewHolder mViewHolder;
    private Activity mShareActivity;

    public FPShareWinGridViewAdapter(Activity activity, int resource, List<String> datas) {
        super(activity, resource, datas);
        mImageUrls = datas;
        mImageUrls.add(String.valueOf(R.mipmap.footprint_image_add));
        mShareActivity = activity;
    }

    @Override
    public void onBindData(WzwViewHolder viewHolder, String path, final int position) {
        mViewHolder = viewHolder;
        final ImageView imageView = viewHolder.findViewById(R.id.footprint_item_image);

        File imagePath = new File(path);


        //如果是添加图片按钮
        if (position == mImageUrls.size()-1){

            Glide.with(mContext)
                    .load(R.mipmap.footprint_image_add)
                    .placeholder(R.mipmap.footprint_image_add)
                    .error(R.mipmap.footprint_image_add)
                    // 重新改变图片大小成这些尺寸(像素)比
                    .override(PixelUtil.dip2px(mContext, 100), PixelUtil.dip2px(mContext, 100))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mImageUrls.size() < FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT+1) {
                        ShareFootPrintPopupWin shareFootPrintPW = new ShareFootPrintPopupWin(mContext);
                        shareFootPrintPW.setSelectImageCount(FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT+1-mImageUrls.size());
                        shareFootPrintPW.showAtLocation(imageView, Gravity.CENTER, 0, 0);

                    }else{
                        Toast.makeText(mContext,
                                "最多只能选择"+FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT + "张图片.",
                                Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }else{

            if (path.endsWith(".gif")) {
                loadPicAsGif(path, imageView);
            }else{
                loadPic(path, imageView);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 final Intent intent = new Intent(mContext, FootPrintImageLookActivity.class);
                 ArrayList<String> imagesFilePath = new ArrayList<String>();
                 for (int i = 0; i < mImageUrls.size()-1; i++) {
                        imagesFilePath.add(mImageUrls.get(i));
                 }

                 Bundle bundle = new Bundle();
                 bundle.putInt(FPPopupWinValue.IMAGE_LOOK_SELECT_INDEX, position);
                 bundle.putStringArrayList(FPPopupWinValue.IMAGE_LOOK_IMAGE_URLS, imagesFilePath);
                 intent.putExtra(FPPopupWinValue.IMAGE_LOOK_TRANSMIT_BUNDLE, bundle);
                 mContext.startActivity(intent);

                }
            });
        }


    }
    private void loadPicAsGif(String path, ImageView imageView ){
        Glide.with(mContext)
                .load(path)
                .asGif()
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                // 重新改变图片大小成这些尺寸(像素)比
                .override(PixelUtil.dip2px(mContext, 100), PixelUtil.dip2px(mContext, 100))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    private void loadPic(String path, ImageView imageView ){
        Glide.with(mContext)
                .load(path)
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                // 重新改变图片大小成这些尺寸(像素)比
                .override(PixelUtil.dip2px(mContext, 100), PixelUtil.dip2px(mContext, 100))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

}
