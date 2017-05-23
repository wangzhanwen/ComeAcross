package com.lyy_wzw.comeacross.footprint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lyy_wzw.comeacross.R;

import java.util.List;

/**
 * Created by yidong9 on 17/5/18.
 */

public class FPPopupWinGridViewAdapter extends BaseAdapter{
    private List<String>  mImageUrls;
    private Context mContext;
    private LayoutInflater mInflater;

    //GridView最多显示几张图片
    private int maxImages = 9;

    public FPPopupWinGridViewAdapter(Context context, List<String>  imageUrls){
        this.mContext = context;
        this.mImageUrls = imageUrls;
        this.mInflater = LayoutInflater.from(context);
    }

    public int getMaxImages() {
        return maxImages;
    }

    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    @Override
    public int getCount() {
        int count = mImageUrls == null ? 0 : mImageUrls.size();
        if (count > maxImages) {
            return maxImages;
        }else{
            return count;
        }

    }

    @Override
    public String getItem(int position) {
        return mImageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class ViewHolder{
        public final ImageView mImageView;
        public final View mRoot ;

        public ViewHolder(){
            this.mRoot = mInflater.inflate(R.layout.foot_print_popupwin_gridview_item, null);
            this.mImageView = (ImageView)mRoot.findViewById(R.id.footprint_popupwin_item_pic);
        }

    }
}
