package com.lyy_wzw.comeacross.discovery.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.lyy_wzw.comeacross.discovery.UrlUtils;
import com.lyy_wzw.comeacross.discovery.widgets.CircleBottomRefreshView;
import com.lyy_wzw.comeacross.discovery.widgets.CommentListView;
import com.lyy_wzw.comeacross.discovery.widgets.ExpandTextView;
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

public class CircleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "CircleAdapter";

    private Context mContext;
    private List<FootPrint> mDatas;
    private Handler mHandler;

    //上拉加载更多布局
    public static final int view_Foot = 3;

    //是否隐藏
    public boolean isLoadMore = false;

    public CircleRecyclerViewAdapter(Context context, List<FootPrint> datas, Handler handler){
        mContext = context;
        mDatas = datas;
        mHandler = handler;
    }


    @Override
    public int getItemViewType(int position) {
        int type = 1;

        if (position == getItemCount()-1){
            type = view_Foot;
        }else {
            FootPrint footPrint = mDatas.get(position);
            type = getType(footPrint);
        }
        return type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case 1:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.circle_recyclerview_item_image, parent, false);
                viewHolder = new CircleImageViewHolder(itemView);
                break;
            case 2:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.circle_recyclerview_item_video, parent, false);
                viewHolder = new CircleVideoViewHolder(itemView);
                break;
            case 3:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.circle_recyclerview_foot_refresh_layout, parent, false);
                viewHolder = new FootViewHolder(itemView);

                break;
            default:
                viewHolder = new CircleViewHolder(itemView);
                break;
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (position == getItemCount()-1){
            FootViewHolder footViewHolder = (FootViewHolder)viewHolder;
            if (isLoadMore){
                footViewHolder.mRefreshView.startRefreshAnim();
                footViewHolder.itemView.setVisibility(View.VISIBLE);

            }else {
                footViewHolder.mRefreshView.stopRefreshAnim();
                footViewHolder.itemView.setVisibility(View.GONE);
            }
        }else {
            final CircleViewHolder holder = (CircleViewHolder)viewHolder;
            final FootPrint footPrint = mDatas.get(position);
            proCommentViewShow(holder, position);

            if (isPrised(position) != -1) {
                holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
            } else {
                holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praise_icon);
            }
            if (position == 0) {
                holder.mItemContainer.setPadding(0, PixelUtil.dip2px(mContext, 20), 0, 0);
            }

            holder.mItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "点击了mItemContainer", Toast.LENGTH_SHORT).show();
                    if (mHandler != null) {
                        Message message = new Message();
                        message.what = DicoveryConstantValue.CIRCLE_COMMENT_HANDLER_Hide_SOFTINPUT_KEY;
                        mHandler.sendMessage(message);
                    }
                }
            });

             //处理朋友圈文本
            if (TextUtils.isEmpty(footPrint.getContent())) {
                holder.mContentTv.setVisibility(View.GONE);
            } else {
                holder.mContentTv.setVisibility(View.VISIBLE);
                holder.mContentTv.setText(footPrint.getContent());
            }


            if(!TextUtils.isEmpty(footPrint.getContent())){
                holder.mContentTv.setExpand(footPrint.isExpand());
                holder.mContentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                    @Override
                    public void statusChange(boolean isExpand) {
                        footPrint.setExpand(isExpand);
                    }
                });

                holder.mContentTv.setText(UrlUtils.formatUrlString(footPrint.getContent()));
            }
            holder.mContentTv.setVisibility(TextUtils.isEmpty(footPrint.getContent()) ? View.GONE : View.VISIBLE);




            //处理位置
            if (footPrint.isShowLocation()) {
                String address = "未知";
                if (footPrint.getFootPrintAddress() != null) {
                    address = footPrint.getFootPrintAddress().getCity() + "."
                            + footPrint.getFootPrintAddress().getDistrict() + "."
                            + footPrint.getFootPrintAddress().getStreet();
                }
                holder.mLocationTv.setText(address);
            } else {
                holder.mLocationTv.setVisibility(View.GONE);
            }

            //处理日期
            SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date strD = lsdStrFormat.parse(footPrint.getCreatedAt());
                DateFormat dFormat = new SimpleDateFormat("MM月dd日  HH:mm");
                String format = dFormat.format(strD);
                holder.mTimeTv.setText(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //根据媒体文件类型，数量，GridView设置布局
            if (getType(footPrint) == 1) {
                CircleImageViewHolder imageViewHolder = (CircleImageViewHolder) holder;

                List<String> imageUrls = new ArrayList<String>();
                for (int i = 0; i < footPrint.getFootPrintFiles().size(); i++) {
                    imageUrls.add(footPrint.getFootPrintFiles().get(i).getFilePath());
                }

                CircleGridViewAdapter circleGridViewAdapter;
                ViewGroup.LayoutParams layoutParams = imageViewHolder.mGradView.getLayoutParams();

                switch (imageUrls.size()) {
                    case 1:
                        layoutParams.height = PixelUtil.dip2px(mContext, 200);
                        layoutParams.width = PixelUtil.dip2px(mContext, 120);
                        imageViewHolder.mGradView.setNumColumns(1);
                        circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item1, imageUrls);
                        imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                        break;
                    case 2:
                        layoutParams.width = PixelUtil.dip2px(mContext, 155);
                        layoutParams.height = PixelUtil.dip2px(mContext, 75);
                        imageViewHolder.mGradView.setNumColumns(2);
                        circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                        imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                        break;
                    case 3:
                        layoutParams.width = PixelUtil.dip2px(mContext, 235);
                        layoutParams.height = PixelUtil.dip2px(mContext, 75);
                        imageViewHolder.mGradView.setNumColumns(3);
                        circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                        imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                        break;

                    case 4:
                        layoutParams.width = PixelUtil.dip2px(mContext, 155);
                        layoutParams.height = PixelUtil.dip2px(mContext, 155);
                        imageViewHolder.mGradView.setNumColumns(2);
                        circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                        imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                        break;
                    case 5:
                    case 6:
                        layoutParams.width = PixelUtil.dip2px(mContext, 235);
                        layoutParams.height = PixelUtil.dip2px(mContext, 155);
                        imageViewHolder.mGradView.setNumColumns(3);
                        circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                        imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                        break;

                    default:
                        layoutParams.width = PixelUtil.dip2px(mContext, 235);
                        layoutParams.height = PixelUtil.dip2px(mContext, 235);
                        imageViewHolder.mGradView.setNumColumns(3);
                        circleGridViewAdapter = new CircleGridViewAdapter(mContext, R.layout.discover_circle_gridview_item, imageUrls);
                        imageViewHolder.mGradView.setAdapter(circleGridViewAdapter);
                        break;
                }


            } else if (getType(footPrint) == 2) {
                CircleVideoViewHolder videoViewHolder = (CircleVideoViewHolder) holder;
                FootPrintFile printFile = footPrint.getFootPrintFiles().get(0);
                GlideUtil.loadPic(mContext, printFile.getThumbnailPath(), videoViewHolder.mVideoBgImg);
            }

            //设置用户信息
            String userId = footPrint.getUserId();
            UserHelper.getInstance().queryUser("objectId", userId, new UserHelper.UserQueryCallback() {
                @Override
                public void onResult(List<CAUser> users, BmobException e) {

                    if (users != null && users.size() > 0) {
                        CAUser caUser = users.get(0);
                        if (caUser != null) {
                            GlideUtil.loadPic(mContext, caUser.getUserPhoto(), holder.mUserPhotoImg);
                            holder.mUserNameTv.setText(caUser.getUsername());
                        }

                    }
                }
            });


            //处理点赞

            holder.mPriseTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<PraiseItem> praiseItems = mDatas.get(position).getPraises();
                    CAUser currentUser = UserHelper.getInstance().getCurrentUser();
                    if (praiseItems != null) {
                        int index = isPrised(position);
                        if (index != -1) {
                            //已赞
                            final PraiseItem praiseItem = mDatas.get(position).getPraises().get(index);
                            mDatas.get(position).getPraises().remove(index);
                            CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                            mDatas.get(position).update(mDatas.get(position).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e != null) {
                                        //数据更新失败，再将删除的数据添加回去
                                        mDatas.get(position).getPraises().add(praiseItem);
                                        CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                                    }

                                }
                            });
                            proCommentViewShow(holder, position);
                            holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praise_icon);
                        } else {
                            //未赞
                            final PraiseItem praiseItem = new PraiseItem();
                            praiseItem.setFootPrintId(footPrint.getObjectId());
                            praiseItem.setUser(currentUser);
                            mDatas.get(position).getPraises().add(praiseItem);
                            CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                            mDatas.get(position).update(footPrint.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e != null) {
                                        //数据更新失败，再将添加的数据删除
                                        mDatas.get(position).getPraises().remove(praiseItem);
                                        CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                                    }
                                }
                            });
                            proCommentViewShow(holder, position);
                            holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
                        }

                    } else {
                        //未赞
                        mDatas.get(position).setPraises(new ArrayList<PraiseItem>());
                        final PraiseItem praiseItem = new PraiseItem();
                        praiseItem.setFootPrintId(mDatas.get(position).getObjectId());
                        praiseItem.setUser(currentUser);
                        mDatas.get(position).getPraises().add(praiseItem);
                        CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                        mDatas.get(position).update(mDatas.get(position).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    //数据更新失败，再将添加的数据删除
                                    mDatas.get(position).getPraises().remove(praiseItem);
                                    CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                                }
                            }
                        });
                        proCommentViewShow(holder, position);
                        holder.mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
                    }
                }
            });

            holder.mPraiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    Toast.makeText(mContext, "点击点赞用户-> position:" + position, Toast.LENGTH_SHORT).show();
                }
            });

            List<PraiseItem> praiseItems = mDatas.get(position).getPraises();
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
                        message.what = DicoveryConstantValue.CIRCLE_COMMENT_HANDLER_COMMENT_MSG_KEY;
                        Bundle bundle = new Bundle();
                        bundle.putInt(DicoveryConstantValue.CIRCLE_COMMENT_BUNDLE_FOOTPRINT_INDEX_KEY, footPrintPosition);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                }
            });

            holder.mCommentListView.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int position) {
                    CommentItem commentItem = mDatas.get(footPrintPosition).getComments().get(position);
                    if (commentItem != null) {
                        if (!commentItem.getUser().getObjectId().equals(UserHelper.getInstance().getCurrentUser().getObjectId())) {
                            Toast.makeText(mContext, "对不起，没有权限删除此条评论.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        return;
                    }
                    showDeleteCommentDialog(footPrintPosition, position);
                }
            });

            holder.mCommentListView.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (mHandler != null) {
                        Message message = new Message();
                        message.what = DicoveryConstantValue.CIRCLE_COMMENT_HANDLER_REPLY_MSG_KEY;
                        Bundle bundle = new Bundle();
                        bundle.putInt(DicoveryConstantValue.CIRCLE_COMMENT_BUNDLE_FOOTPRINT_INDEX_KEY, footPrintPosition);
                        bundle.putInt(DicoveryConstantValue.CIRCLE_COMMENT_BUNDLE_COMMENT_ITEM_INDEX_KEY, position);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                }
            });

            holder.mCommentListView.setOnItemUserClickListener(new CommentListView.OnItemUserClickListener() {
                @Override
                public void onItemUserClick(String userId) {
                    Toast.makeText(mContext, "点击评论用户-> userId:" + userId, Toast.LENGTH_SHORT).show();
                }
            });

            List<CommentItem> commentItems = mDatas.get(position).getComments();
            if (commentItems != null) {
                holder.mCommentListView.setDatas(commentItems);
            }



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


    private void showDeleteCommentDialog(final int footPrintPosition, final int commentIndex){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("删除此条评论？");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final CommentItem commentItem = mDatas.get(footPrintPosition).getComments().get(commentIndex);
                mDatas.get(footPrintPosition).getComments().remove(commentIndex);
                CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                mDatas.get(footPrintPosition).update(mDatas.get(footPrintPosition).getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null) {
                            Toast.makeText(mContext, "删除成功.", Toast.LENGTH_SHORT).show();
                        }else{
                            mDatas.get(footPrintPosition).getComments().add(commentIndex,commentItem);
                            CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPos = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPos.setTextColor(Color.parseColor("#25b249"));
                buttonPos.setTextSize(18);

                Button buttonNeg = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNeg.setTextSize(18);
            }
        });

        alertDialog.show();
    }



    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

   public Handler mCircleAdapterHandler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what) {

          }

      }
  };

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        notifyDataSetChanged();
    }

    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private CircleBottomRefreshView mRefreshView;

        public FootViewHolder(View itemView) {
            super(itemView);
            mRefreshView = (CircleBottomRefreshView)itemView.findViewById(R.id.circle_bottom_refresh_view);
        }
    }


    public static class CircleViewHolder extends RecyclerView.ViewHolder{
        private ImageView mUserPhotoImg;
        private TextView mUserNameTv;
        private ExpandTextView mContentTv;
        private TextView mLocationTv;
        private TextView mTimeTv;
        private TextView mPriseTv;
        private TextView mCommentTv;
        private LinearLayout mCommentBodyView;
        private PraiseListView mPraiseListView;
        private CommentListView mCommentListView;
        private View mCommentLineView;
        private FrameLayout mItemContainer;

        public CircleViewHolder(View itemView) {
            super(itemView);

            mUserPhotoImg    = (ImageView)itemView.findViewById(R.id.circle_recyclerView_item_user_photo);
            mUserNameTv      = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_user_name);
            mContentTv       = (ExpandTextView)itemView.findViewById(R.id.circle_recyclerView_item_content);
            mLocationTv      = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_location);
            mTimeTv          = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_time);
            mPriseTv         = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_praise);
            mCommentTv       = (TextView)itemView.findViewById(R.id.circle_recyclerView_item_comment);
            mCommentBodyView = (LinearLayout)itemView.findViewById(R.id.circle_comment_body);
            mPraiseListView  = (PraiseListView)itemView.findViewById(R.id.circle_praise_listView);
            mCommentListView = (CommentListView)itemView.findViewById(R.id.circle_comment_listview);
            mCommentLineView = itemView.findViewById(R.id.circle_comment_line);
            mItemContainer   = (FrameLayout)itemView.findViewById(R.id.circle_recyclerview_item_container);

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
