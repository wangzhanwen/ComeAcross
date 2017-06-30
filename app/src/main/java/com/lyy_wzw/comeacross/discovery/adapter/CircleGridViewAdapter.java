package com.lyy_wzw.comeacross.discovery.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.activity.FootPrintImageLookActivity;
import com.lyy_wzw.comeacross.footprint.adapter.WzwBaseAdapter;
import com.lyy_wzw.comeacross.footprint.adapter.WzwViewHolder;
import com.lyy_wzw.comeacross.footprint.finalvalue.FPPopupWinValue;
import com.lyy_wzw.comeacross.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yidong9 on 17/6/30.
 */

public class CircleGridViewAdapter extends WzwBaseAdapter<String>{
    private List<String> mImageUrls;
    private WzwViewHolder mViewHolder;

    public CircleGridViewAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mImageUrls = objects;
        Log.d("CircleGridViewAdapter:", ""+mImageUrls.size());
    }

    @Override
    public void onBindData(WzwViewHolder viewHolder, String url, int position){
        mViewHolder = viewHolder;
        final ImageView imageView = viewHolder.findViewById(R.id.discover_circle_item_image);

        Log.d("CircleGridViewAdapter:", ""+position);
        if (null==imageView) {
            Log.d("CircleGridViewAdapter", "imageView为null");
        }

        if (url.endsWith(".gif")) {
            GlideUtil.loadPicAsGif(mContext,url, imageView);
        }else{
            GlideUtil.loadPic(mContext,url, imageView);
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
