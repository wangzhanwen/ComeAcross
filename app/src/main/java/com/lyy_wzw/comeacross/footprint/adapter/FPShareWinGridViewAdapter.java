package com.lyy_wzw.comeacross.footprint.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.FootPrintFile;
import com.lyy_wzw.comeacross.footprint.activity.FootPrintImageLookActivity;
import com.lyy_wzw.comeacross.footprint.finalvalue.FPPopupWinValue;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.utils.GlideUtil;
import com.lyy_wzw.comeacross.utils.PixelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yidong9 on 17/6/1.
 */

public class FPShareWinGridViewAdapter extends WzwBaseAdapter<FootPrintFile>{
    private static final String TAG = "ShareWinGridViewAdapter";

    private List<FootPrintFile> mFootPrintFiles;
    private WzwViewHolder mViewHolder;
    private Activity mShareActivity;

    public FPShareWinGridViewAdapter(Activity activity, int resource, List<FootPrintFile> datas) {
        super(activity, resource, datas);
        mFootPrintFiles = datas;

        //将发朋友圈添加图片按钮背景文件添加进去
        FootPrintFile addImage = new FootPrintFile();
        addImage.setFilePath(String.valueOf(R.mipmap.footprint_image_add));
        addImage.setType(1);

        mFootPrintFiles.add(addImage);
        mShareActivity = activity;
    }

    @Override
    public void onBindData(WzwViewHolder viewHolder, final FootPrintFile itemFile, final int position) {
        mViewHolder = viewHolder;
        final ImageView imageView = viewHolder.findViewById(R.id.footprint_item_image);
        final ImageView videoPlayBtn = viewHolder.findViewById(R.id.footprint_item_video_play_btn);
        String  imagePath = null;

        if (itemFile.getType()==2) {
            imagePath = itemFile.getThumbnailPath();
            videoPlayBtn.setVisibility(View.VISIBLE);
        }else{
            imagePath = itemFile.getFilePath();
            videoPlayBtn.setVisibility(View.GONE);
        }

        //如果是添加图片按钮
        if (position == mFootPrintFiles.size()-1){

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
                        if (isExitVideo()) {
                            Toast.makeText(mContext,
                                    "视频只能单独发送.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (mFootPrintFiles.size() < FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT+1) {
                            ShareFootPrintPopupWin shareFootPrintPW = new ShareFootPrintPopupWin(mContext);
                            shareFootPrintPW.setSelectImageCount(FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT+1-mFootPrintFiles.size());
                            shareFootPrintPW.showAtLocation(imageView, Gravity.CENTER, 0, 0);

                        }else{
                            Toast.makeText(mContext,
                                    "最多只能发布"+FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT + "个文件.",
                                    Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }else{
            if (imagePath.endsWith(".gif")) {
                GlideUtil.loadPicAsGif(mContext,imagePath, imageView);

            }else{
                GlideUtil.loadPic(mContext,imagePath, imageView);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemFile.getType() == 1){
                        final Intent intent = new Intent(mContext, FootPrintImageLookActivity.class);
                        ArrayList<String> imagesFilePath = new ArrayList<String>();
                        for (int i = 0; i < mFootPrintFiles.size()-1; i++) {
                                FootPrintFile footPrintFile = mFootPrintFiles.get(i);
                                if (footPrintFile.getType() == 1) {
                                    imagesFilePath.add(footPrintFile.getFilePath());
                                }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt(FPPopupWinValue.IMAGE_LOOK_SELECT_INDEX, position);
                        bundle.putStringArrayList(FPPopupWinValue.IMAGE_LOOK_IMAGE_URLS, imagesFilePath);
                        intent.putExtra(FPPopupWinValue.IMAGE_LOOK_TRANSMIT_BUNDLE, bundle);
                        mContext.startActivity(intent);

                    }else if(itemFile.getType() == 2){
                        Toast.makeText(mShareActivity, "播放视频。。。", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    private boolean isExitVideo(){
        boolean ret = false;
        for (int i = 0; i < mFootPrintFiles.size(); i++) {
            FootPrintFile footPrintFile = mFootPrintFiles.get(i);
            if (footPrintFile.getType() == 2) {
                ret = true;
            }
        }
        return ret;
    }

}
