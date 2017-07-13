package com.lyy_wzw.comeacross.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 27459 on 2017/7/6.
 */

public abstract class NewFriendAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mList;
    private int layoutId;

    public NewFriendAdapter(Context context, List<T> list, int layoutId) {
        mContext = context;
        mList = list;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        if (mList == null) {
            return null;
        }
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup group) {
        view = LayoutInflater.from(mContext).inflate(layoutId,group,false);
        bindView(i,view);
        return view;
    }

    public void addData(List<T> list){
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public abstract void bindView(int i,View view);

    public class ViewHolder{

    }

}
