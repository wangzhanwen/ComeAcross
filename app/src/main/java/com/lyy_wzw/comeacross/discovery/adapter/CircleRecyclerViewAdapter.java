package com.lyy_wzw.comeacross.discovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.bean.FootPrintFile;

import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.GlideUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yidong9 on 17/6/29.
 */

public class CircleRecyclerViewAdapter extends RecyclerView.Adapter<CircleRecyclerViewAdapter.CircleViewHolder>{
    private Context mContext;
    private List<FootPrint> mDatas;

    public CircleRecyclerViewAdapter(Context context, List<FootPrint> datas){
        mContext = context;
        mDatas = datas;
    }


    @Override
    public int getItemViewType(int position) {
        int type = 1;
        FootPrint footPrint = mDatas.get(position);
        type = getType(footPrint);

        return type;
    }


    @Override
    public CircleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        CircleViewHolder viewHolder = null;
        switch (viewType){
            case 1:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.circle_recyclerview_item_image, parent, false);
                viewHolder = new CircleImageViewHolder(itemView);
                break;
            case 2:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.circle_recyclerview_item_video, parent, false);
                viewHolder = new CircleVideoViewHolder(itemView);
                break;
            default:
                viewHolder = new CircleViewHolder(itemView);
                break;
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CircleViewHolder holder, int position) {
        final FootPrint footPrint = mDatas.get(position);
        UserHelper.getInstance().queryUser("objectId", footPrint.getUid(), new UserHelper.UserQueryCallback() {
            @Override
            public void onResult(List<CAUser> users, BmobException e) {
                if (users != null && users.size()>0){
                    CAUser caUser = users.get(0);
                    Log.d("adapter:", ""+caUser.getUserPhoto());
                    GlideUtil.loadPic(mContext,caUser.getUserPhoto(), 100, 100, holder.mUserPhotoImg);
                    holder.mUserNameTv.setText(caUser.getUsername());
                }

                holder.mContentTv.setText(footPrint.getContent());

                if (footPrint.isShowLocation()) {
                    holder.mLocationTv.setText(footPrint.getLatitude() + "," +footPrint.getLatitude());
                }else{
                    holder.mLocationTv.setVisibility(View.GONE);
                }

                holder.mTimeTv.setText(footPrint.getCreatedAt());


                if (getType(footPrint) == 1) {
                    CircleImageViewHolder imageViewHolder = (CircleImageViewHolder)holder;

                }else if(getType(footPrint) == 2){
                    CircleVideoViewHolder videoViewHolder = (CircleVideoViewHolder)holder;
                    FootPrintFile printFile = footPrint.getFootPrintFiles().get(0);
                    GlideUtil.loadPic(mContext, printFile.getThumbnailPath(), 140,240,videoViewHolder.mVideoBgImg);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class CircleViewHolder extends RecyclerView.ViewHolder{
        private ImageView mUserPhotoImg;
        private TextView mUserNameTv;
        private TextView mContentTv;
        private TextView mLocationTv;
        private TextView mTimeTv;
        private TextView mPriseTv;
        private TextView mCommentTv;

        public CircleViewHolder(View itemView) {
            super(itemView);

            mUserPhotoImg = (ImageView)itemView.findViewById(R.id.circle_recyclerView_item_user_photo);
            mUserNameTv   = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_user_name);
            mContentTv    = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_content);
            mLocationTv   = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_location);
            mTimeTv       = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_time);
            mPriseTv      = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_praise);
            mCommentTv    = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_comment);
        }
    }

    public static class CircleImageViewHolder extends CircleViewHolder{
        private GridView mGradView;

        public CircleImageViewHolder(View itemView) {
            super(itemView);

            mGradView = (GridView)itemView.findViewById(R.id.circle_recyclerView_item_gridview);
        }
    }

    public static class CircleVideoViewHolder extends CircleViewHolder{
        private ImageView mVideoBgImg;
        private ImageView mVideoPlayImg;

        public CircleVideoViewHolder(View itemView) {
            super(itemView);
            mVideoBgImg   = (ImageView)itemView.findViewById(R.id.circle_recyclerView_item_video_bg);
            mVideoPlayImg = (ImageView)itemView.findViewById(R.id.circle_recyclerView_item_video_play);
        }
    }


    private int getType(FootPrint footPrint) {
        int type = 1;
        List<FootPrintFile> footPrintFiles = footPrint.getFootPrintFiles();
        if (footPrintFiles != null && footPrintFiles.size() > 0){
            for (int i = 0; i < footPrintFiles.size(); i++) {
                if (footPrintFiles.get(i).getType() == 2){
                    type = 2;
                }
            }
        }
        return type;
    }
}
