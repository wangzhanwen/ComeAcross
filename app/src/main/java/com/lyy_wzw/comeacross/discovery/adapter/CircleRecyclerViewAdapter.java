package com.lyy_wzw.comeacross.discovery.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.CommentItem;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.bean.FootPrintFile;

import com.lyy_wzw.comeacross.bean.PraiseItem;
import com.lyy_wzw.comeacross.discovery.DicoveryConstantValue;
import com.lyy_wzw.comeacross.discovery.widgets.CommentListView;
import com.lyy_wzw.comeacross.discovery.widgets.PraiseListView;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.GlideUtil;
import com.lyy_wzw.comeacross.utils.PixelUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by yidong9 on 17/6/29.
 */

public class CircleRecyclerViewAdapter extends RecyclerView.Adapter<CircleRecyclerViewAdapter.CircleViewHolder>{
    private static final String TAG = "CircleAdapter";

    private Context mContext;
    private List<FootPrint> mDatas;
    private Handler mHandler;

    public CircleRecyclerViewAdapter(Context context, List<FootPrint> datas, Handler handler){
        mContext = context;
        mDatas = datas;
        mHandler = handler;
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
    public void onBindViewHolder(final CircleViewHolder holder, final int position) {
        final FootPrint footPrint = mDatas.get(position);
        proCommentViewShow(holder,position);
        if (isPrised(position)!=-1) {
            holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
        }else{
            holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praise_icon);
        }

        if (TextUtils.isEmpty(footPrint.getContent())){
            holder.mContentTv.setVisibility(View.GONE);
        }else{
            holder.mContentTv.setVisibility(View.VISIBLE);
            holder.mContentTv.setText(footPrint.getContent());
        }

         //处理位置
        if (footPrint.isShowLocation()) {
            String address = "未知";
            if (footPrint.getFootPrintAddress() != null) {
                address = footPrint.getFootPrintAddress().getCity()+"."
                        + footPrint.getFootPrintAddress().getDistrict() + "."
                        + footPrint.getFootPrintAddress().getStreet();
            }
            holder.mLocationTv.setText(address);
        }else{
            holder.mLocationTv.setVisibility(View.GONE);
        }

        //处理日期
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date strD = lsdStrFormat.parse(footPrint.getCreatedAt());
            DateFormat dFormat = new SimpleDateFormat("MM月dd日  HH:mm"); //HH表示24小时制；
            String format = dFormat.format(strD);
            holder.mTimeTv.setText(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //根据媒体文件类型，数量，GridView设置布局
        if (getType(footPrint) == 1) {
            CircleImageViewHolder imageViewHolder = (CircleImageViewHolder)holder;

            List<String> imageUrls = new ArrayList<String>();
            for (int i = 0; i < footPrint.getFootPrintFiles().size(); i++) {
                imageUrls.add(footPrint.getFootPrintFiles().get(i).getFilePath());
            }

            CircleGridViewAdapter circleGridViewAdapter;
            ViewGroup.LayoutParams layoutParams = imageViewHolder.mGradView.getLayoutParams();

            switch (imageUrls.size()){
                case 1:
                    layoutParams.height = PixelUtil.dip2px(mContext,200);
                    layoutParams.width = PixelUtil.dip2px(mContext,120);
                    imageViewHolder.mGradView.setNumColumns(1);
                    circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item1, imageUrls);
                    imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                    break;
                case 2:
                    layoutParams.width = PixelUtil.dip2px(mContext,155);
                    layoutParams.height = PixelUtil.dip2px(mContext,75);
                    imageViewHolder.mGradView.setNumColumns(2);
                    circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                    imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                    break;
                case 3:
                    layoutParams.width = PixelUtil.dip2px(mContext, 235);
                    layoutParams.height = PixelUtil.dip2px(mContext,75);
                    imageViewHolder.mGradView.setNumColumns(3);
                    circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                    imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                    break;

                case 4:
                    layoutParams.width = PixelUtil.dip2px(mContext, 155);
                    layoutParams.height = PixelUtil.dip2px(mContext,155);
                    imageViewHolder.mGradView.setNumColumns(2);
                    circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                    imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                    break;
                case 5:
                case 6:
                    layoutParams.width = PixelUtil.dip2px(mContext, 235);
                    layoutParams.height = PixelUtil.dip2px(mContext,155);
                    imageViewHolder.mGradView.setNumColumns(3);
                    circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                    imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                    break;

                default:
                    layoutParams.width = PixelUtil.dip2px(mContext, 235);
                    layoutParams.height = PixelUtil.dip2px(mContext,235);
                    imageViewHolder.mGradView.setNumColumns(3);
                    circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                    imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                    break;
            }


        }else if(getType(footPrint) == 2){
            CircleVideoViewHolder videoViewHolder = (CircleVideoViewHolder)holder;
            FootPrintFile printFile = footPrint.getFootPrintFiles().get(0);
            GlideUtil.loadPic(mContext, printFile.getThumbnailPath(),videoViewHolder.mVideoBgImg);
        }

         //设置用户信息
        String  userId = footPrint.getUserId();
        Log.d(TAG,"onBindViewHolder()-->> caUser" + userId);

        UserHelper.getInstance().queryUser("objectId", userId, new UserHelper.UserQueryCallback() {
            @Override
            public void onResult(List<CAUser> users, BmobException e) {

                if (users != null && users.size()>0 ){
                    CAUser caUser = users.get(0);
                    if (caUser != null) {
                        GlideUtil.loadPic(mContext,caUser.getUserPhoto(), holder.mUserPhotoImg);
                        holder.mUserNameTv.setText(caUser.getUsername());
                    }

                }
            }
        });


        //处理点赞

        holder.mPriseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PraiseItem>  praiseItems= mDatas.get(position).getPraises();
                CAUser currentUser = UserHelper.getInstance().getCurrentUser();
                if (praiseItems != null) {
                    int index = isPrised(position);
                    if (index != -1){
                        //已赞
                        mDatas.get(position).getPraises().remove(index);
                        CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                        mDatas.get(position).update(mDatas.get(position).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null) {
                                        Toast.makeText(mContext, "已取消点赞", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Log.d(TAG,"点赞-->>e:" + e.getMessage());
                                    }
                                }
                        });
                        proCommentViewShow(holder,position);
                        holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praise_icon);
                    }else{
                        //未赞
                        PraiseItem praiseItem = new PraiseItem();
                        praiseItem.setFootPrintId(footPrint.getObjectId());
                        praiseItem.setUser(currentUser);
                        mDatas.get(position).getPraises().add(praiseItem);
                        CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                        mDatas.get(position).update(footPrint.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        proCommentViewShow(holder,position);
                        holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
                    }

                }else{
                    //未赞
                    mDatas.get(position).setPraises(new ArrayList<PraiseItem>());
                    PraiseItem praiseItem = new PraiseItem();
                    praiseItem.setFootPrintId(mDatas.get(position).getObjectId());
                    praiseItem.setUser(currentUser);
                    mDatas.get(position).getPraises().add(praiseItem);
                    CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                    mDatas.get(position).update(mDatas.get(position).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                        }
                    });
                    proCommentViewShow(holder,position);
                    holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
                }
            }
        });


        List<PraiseItem>  praiseItems= mDatas.get(position).getPraises();
        if (praiseItems != null) {
            holder.mPraiseListView.setDatas(praiseItems);
        }
        holder.mPraiseListView.setDatas(praiseItems);

        //处理评论
        final int footPrintPosition = position;
        holder.mCommentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandler != null) {
                    Message message = new Message();
                    message.what = DicoveryConstantValue.CIRCLE_COMMENT_HANDLER_MSG_KEY;
                    Bundle bundle = new Bundle();
                    bundle.putInt(DicoveryConstantValue.CIRCLE_COMMENT_BUNDLE_FOOTPRINT_INDEX_KEY, footPrintPosition);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            }
        });

        List<CommentItem>  commentItems = mDatas.get(position).getComments();
        if (commentItems != null) {
            holder.mCommentListView.setDatas(commentItems);
        }

    }

    private int isPrised(int  position){
        int ret = -1;
        CAUser currentUser = UserHelper.getInstance().getCurrentUser();
        List<PraiseItem>  praiseItems= mDatas.get(position).getPraises();
        if (praiseItems != null) {
            for (int i = 0; i < praiseItems.size(); i++) {
                PraiseItem praiseItem =  praiseItems.get(i);
                if (currentUser.getObjectId().equals(praiseItem.getUser().getObjectId())) {
                    //已赞
                    ret = i;
                    break;
                }
            }

        }
        return ret;
    }


    private void proCommentViewShow(CircleViewHolder holder, int position){
        FootPrint footPrint = mDatas.get(position);
        List<CommentItem> comments = footPrint.getComments();
        List<PraiseItem>  praises = footPrint.getPraises();

        if (footPrint == null) {
            return;
        }

        if (isEmityList(comments) && isEmityList(praises) ){
            holder.mCommentBodyView.setVisibility(View.GONE);
            return;
        }

        if (isEmityList(comments)){
            holder.mCommentListView.setVisibility(View.GONE);

        }else{
            holder.mCommentListView.setVisibility(View.VISIBLE);
            holder.mCommentBodyView.setVisibility(View.VISIBLE);
        }

        if (isEmityList(praises)){
            holder.mPraiseListView.setVisibility(View.GONE);
            holder.mCommentLineView.setVisibility(View.GONE);
        }else{
            holder.mPraiseListView.setVisibility(View.VISIBLE);
            holder.mCommentLineView.setVisibility(View.VISIBLE);
            holder.mCommentBodyView.setVisibility(View.VISIBLE);
        }
    }


    private boolean isEmityList(List list){
        if (list == null){
            return true;
        }
        if (list.size() < 1) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

   public Handler mCircleAdapterHandler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what) {

          }

      }
  };





    public static class CircleViewHolder extends RecyclerView.ViewHolder{
        private ImageView mUserPhotoImg;
        private TextView mUserNameTv;
        private TextView mContentTv;
        private TextView mLocationTv;
        private TextView mTimeTv;
        private TextView mPriseTv;
        private TextView mCommentTv;
        private LinearLayout mCommentBodyView;
        private PraiseListView mPraiseListView;
        private CommentListView mCommentListView;
        private View mCommentLineView;

        public CircleViewHolder(View itemView) {
            super(itemView);

            mUserPhotoImg    = (ImageView)itemView.findViewById(R.id.circle_recyclerView_item_user_photo);
            mUserNameTv      = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_user_name);
            mContentTv       = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_content);
            mLocationTv      = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_location);
            mTimeTv          = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_time);
            mPriseTv         = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_praise);
            mCommentTv       = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_comment);
            mCommentBodyView = (LinearLayout)itemView.findViewById(R.id.circle_comment_body);
            mPraiseListView  = (PraiseListView)itemView.findViewById(R.id.circle_praise_listView);
            mCommentListView = (CommentListView)itemView.findViewById(R.id.circle_comment_listview);
            mCommentLineView = itemView.findViewById(R.id.circle_comment_line);

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
