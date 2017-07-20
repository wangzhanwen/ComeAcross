package com.lyy_wzw.comeacross.discovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.bean.FootPrintFile;
import com.lyy_wzw.comeacross.discovery.widgets.CircleBottomRefreshView;
import com.lyy_wzw.comeacross.utils.GlideUtil;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yidong9 on 17/7/17.
 */

public class PersonalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "PersonalAdapter";

    private Context mContext;
    private List<FootPrint> mDatas;

    public static final int view_Foot = 3;
    public boolean isLoadMore = false;

    public PersonalRecyclerViewAdapter(Context context, List<FootPrint> datas){
        mContext = context;
        mDatas = datas;
    }


    @Override
    public int getItemViewType(int position) {
        int type = 1;
        if (position == getItemCount()-1){
            type = view_Foot;
        }else {
            FootPrint footPrint = mDatas.get(position);
            type = getFileType(footPrint);
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case 1:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.personal_circle_item, parent, false);
                viewHolder =  new PersonalRecyclerViewAdapter.PersonalViewHolder(itemView);
                break;
            case 2:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.personal_circle_item, parent, false);
                viewHolder =  new PersonalRecyclerViewAdapter.PersonalViewHolder(itemView);
                break;
            case 3:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.circle_recyclerview_foot_refresh_layout, parent, false);
                viewHolder = new PersonalRecyclerViewAdapter.FootViewHolder(itemView);

                break;
            default:
                viewHolder =  new PersonalViewHolder(itemView);
                break;
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == getItemCount()-1){
           FootViewHolder footViewHolder = (FootViewHolder)holder;
            if (isLoadMore){
                footViewHolder.mRefreshView.startRefreshAnim();
                footViewHolder.itemView.setVisibility(View.VISIBLE);

            }else {
                footViewHolder.mRefreshView.stopRefreshAnim();
                footViewHolder.itemView.setVisibility(View.GONE);
            }
            return;
        }

        FootPrint footPrint = mDatas.get(position);
        PersonalViewHolder viewHolder = null;

        if (holder instanceof PersonalViewHolder) {
            viewHolder = (PersonalViewHolder)holder;
        }else{
            return;
        }

        //
        //处理日期
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date strD = lsdStrFormat.parse(footPrint.getCreatedAt());
            DateFormat dFormat = new SimpleDateFormat("MM.dd");
            String format = dFormat.format(strD);
            viewHolder.mDateView.setText(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (getFileType(footPrint) == 2) {
            viewHolder.mVideobtn.setVisibility(View.VISIBLE);
            viewHolder.mCountView.setVisibility(View.GONE);
            GlideUtil.loadPic(mContext, footPrint.getFootPrintFiles().get(0).getThumbnailPath(),viewHolder.mImageView);

        }else{
            viewHolder.mVideobtn.setVisibility(View.GONE);
            viewHolder.mCountView.setVisibility(View.VISIBLE);
            viewHolder.mCountView.setText("共" + footPrint.getFootPrintFiles().size() + "张");
            GlideUtil.loadPic(mContext, footPrint.getFootPrintFiles().get(0).getFilePath(),viewHolder.mImageView);
        }

        viewHolder.mContentView.setText(footPrint.getContent());



    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }


    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private CircleBottomRefreshView mRefreshView;

        public FootViewHolder(View itemView) {
            super(itemView);
            mRefreshView = (CircleBottomRefreshView)itemView.findViewById(R.id.circle_bottom_refresh_view);
        }
    }



    public static class PersonalViewHolder extends RecyclerView.ViewHolder{

        private final TextView mDateView;
        private final ImageView mImageView;
        private final ImageView mVideobtn;
        private final TextView mContentView;
        private final TextView mCountView;

        public PersonalViewHolder(View itemView) {
            super(itemView);
            mDateView = (TextView) itemView.findViewById(R.id.personal_circle_item_date);
            mImageView = (ImageView) itemView.findViewById(R.id.personal_circle_item_pic);
            mVideobtn = (ImageView) itemView.findViewById(R.id.personal_circle_item_video_pic);
            mContentView = (TextView) itemView.findViewById(R.id.personal_circle_item_content);
            mCountView = (TextView) itemView.findViewById(R.id.personal_circle_item_pic_count);

        }
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        notifyDataSetChanged();
    }

    private int getFileType(FootPrint footPrint) {
        int type = 1;
        List<FootPrintFile> footPrintFiles = footPrint.getFootPrintFiles();
        if (footPrintFiles != null && footPrintFiles.size() > 0){
            for (int i = 0; i < footPrintFiles.size(); i++) {
                if (footPrintFiles.get(i).getType() == 2){
                    type = 2;
                    break;
                }
            }
        }
        return type;
    }
}
