package com.lyy_wzw.comeacross.footprint.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.activity.FootPrintImageLookActivity;
import com.lyy_wzw.comeacross.footprint.activity.TestActivity;
import com.lyy_wzw.comeacross.footprint.finalvalue.FPPopupWinValue;
import com.lyy_wzw.comeacross.utils.EasyTransition;
import com.lyy_wzw.comeacross.utils.EasyTransitionOptions;
import com.lyy_wzw.comeacross.utils.GlideUtil;
import com.lyy_wzw.comeacross.utils.PixelUtil;
import com.lyy_wzw.imageselector.utils.ImageSelectorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzw on 17/5/18.
 */

public class FPPopupWinGridViewAdapter extends WzwBaseAdapter<String>{
    private static final int REQUEST_CODE = 0x00000011;

    private List<String> mImageUrls;
    private WzwViewHolder mViewHolder;

    public FPPopupWinGridViewAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mImageUrls = objects;
    }

    @Override
    public void onBindData(WzwViewHolder viewHolder, String url, int position) {
        mViewHolder = viewHolder;
        final ImageView imageView = viewHolder.findViewById(R.id.footprint_item_image);
        if (null==imageView) {
            Log.d("PopupWinGridViewAdapter", "imageView为null");
        }

        if (url.endsWith(".gif")) {
            GlideUtil.loadPicAsGif(mContext,url, 100, 100, imageView);
        }else{
            GlideUtil.loadPic(mContext,url, 100, 100, imageView);
        }


        final Intent intent = new Intent(mContext, FootPrintImageLookActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(FPPopupWinValue.IMAGE_LOOK_SELECT_INDEX, position);
        bundle.putStringArrayList(FPPopupWinValue.IMAGE_LOOK_IMAGE_URLS, (ArrayList)mObjects);
        intent.putExtra(FPPopupWinValue.IMAGE_LOOK_TRANSMIT_BUNDLE, bundle);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(intent);

            }
        });
    }

}

