package com.lyy_wzw.comeacross.footprint.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.utils.PixelUtil;

import java.util.List;

/**
 * Created by wzw on 17/5/18.
 */

public class FPPopupWinGridViewAdapter extends WzwBaseAdapter<String>{


    public FPPopupWinGridViewAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public void onBindData(WzwViewHolder viewHolder, String url, int position) {
        ImageView imageView = viewHolder.findViewById(R.id.footprint_popupwin_item_pic);
        if (null==imageView) {
            Log.d("PopupWinGridViewAdapter", "imageView为null");
        }

        Glide.with(mContext)
                .load(url)
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                .override(PixelUtil.dip2px(mContext, 100), PixelUtil.dip2px(mContext, 100)) // 重新改变图片大小成这些尺寸(像素)比
                .centerCrop()
                .into(imageView);


    }
}
