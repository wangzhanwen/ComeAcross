package com.lyy_wzw.comeacross.addressbook;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.homecommon.InitApplication;
import com.lyy_wzw.comeacross.rong.server.widget.SelectableRoundedImageView;

import java.util.List;

import io.rong.imageloader.core.ImageLoader;

import static java.lang.String.valueOf;

/**
 * Created by 27459 on 2017/6/22.
 */

public class FriendListAdapter extends BaseAdapter implements SectionIndexer {
    private static final String TAG = "FriendListAdapter";
    private Context mContext;
    private List<Friend> list;

    public FriendListAdapter(Context context, List<Friend> list) {
        Log.e(TAG, "FriendListAdapter: "+list.size() );
        mContext = context;
        this.list = list;
    }

    public void updateListView(List<Friend> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        if (i >= list.size()) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        Friend mContent = list.get(position);
        if ( convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_item,parent,false);
            viewHolder.tvTitle = ((TextView) convertView.findViewById(R.id.friendname));
            viewHolder.tvLetter = ((TextView) convertView.findViewById(R.id.catalog));
            viewHolder.mImageView = ((SelectableRoundedImageView) convertView.findViewById(R.id.frienduri));
            viewHolder.tvUserId = ((TextView) convertView.findViewById(R.id.friend_id));
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.e(TAG, "getView: "+mContent.toString());
        //根据position获取分类的首字母的Char ascII值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母第一次出现的位置
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            String letterFirst = String.valueOf(mContent.getNameSpelling().charAt(0));
            if (!TextUtils.isEmpty(letterFirst)) {
                //转换为大写
                letterFirst = valueOf(letterFirst.toUpperCase().charAt(0));
            }
            viewHolder.tvLetter.setText(letterFirst);
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        if (mContent.isExitsDisplayName()) {
            viewHolder.tvTitle.setText(list.get(position).getDisplayName());
        }else{
            viewHolder.tvTitle.setText(list.get(position).getName());
        }
        //viewHolder.tvTitle.setText(list.get(position).getName());
        String portraitUri = list.get(position).getPortraitUri().getPath();
        ImageLoader.getInstance().displayImage(portraitUri,viewHolder.mImageView, InitApplication.getOptions());
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     *从list集合中找打第一个有该ascii码值的letters的位置
     * @param sectionIndex
     * @return
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getNameSpelling();
            char firstChar = sortStr.charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int i) {
        return list.get(i).getNameSpelling().charAt(0);
    }

    final static class ViewHolder {
        /**
         * 首字母
         */
        TextView tvLetter;
        /**
         * 昵称
         */
        TextView tvTitle;
        /**
         * 头像
         */
        SelectableRoundedImageView mImageView;
        /**
         * userid
         */
        TextView tvUserId;

    }
}
