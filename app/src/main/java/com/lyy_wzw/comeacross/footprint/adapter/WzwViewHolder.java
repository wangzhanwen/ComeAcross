package com.lyy_wzw.comeacross.footprint.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yidong9 on 17/5/23.
 */

public class WzwViewHolder {
    private Context mContext;
    private SparseArray<View> mViews;
    private View mItemView;

    private WzwViewHolder(Context context, View itemView){
        mContext = context;
        mItemView = itemView;
        mViews = new SparseArray<>();
        mItemView.setTag(this);
    }

    public static WzwViewHolder getViewHolder(Context context, int resource, View convertView, ViewGroup parent){
        if (null == convertView) {
            View view = LayoutInflater.from(context).inflate(resource, parent, false);
            return new WzwViewHolder(context, view);

        }
        return (WzwViewHolder)convertView.getTag();
    };

    public <T extends View> T findViewById(int id){
        View view = mViews.get(id);
        if (null == view) {
            view  = mItemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T)view;
    }

    public WzwViewHolder setText(int viewId, int textId) {
        return setText(viewId, mContext.getString(textId));
    }

    /**
     * 设置 TextView 的显示的字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public WzwViewHolder setText(int viewId, String text) {
        View view = findViewById(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }



    public View getItemView() {
        return mItemView;
    }
}
