package com.lyy_wzw.comeacross.discovery.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.lyy_wzw.comeacross.discovery.adapter.CircleGridViewAdapter;
import com.lyy_wzw.comeacross.discovery.widgets.CommentListView;
import com.lyy_wzw.comeacross.discovery.widgets.ExpandTextView;
import com.lyy_wzw.comeacross.discovery.widgets.PraiseListView;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.GlideUtil;
import com.lyy_wzw.comeacross.utils.PixelUtil;
import com.lyy_wzw.comeacross.utils.SoftInputUtil;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class DetailCommentImgActivity extends AppCompatActivity {
    private static final String TAG = "DetailCommentImgActivit";
    private FootPrint mFootPrint;

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
    private GridView mGradView;
    private EditText mCommentEt;
    private TextView mCommentSendTv;
    private LinearLayout mCommentContainerView;
    private FrameLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_comment_img);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(DicoveryConstantValue.DETAIL_COMMENT_BUNDLE_KEY);


        if (bundle!=null){
            Serializable serializable = bundle.getSerializable(DicoveryConstantValue.DETAIL_COMMENT_BUNDLE_FOOTPRINT_KEY);
            if (serializable != null) {
                mFootPrint = (FootPrint)serializable;
                Log.d(TAG, mFootPrint.toString());
            }

        }


        initView();
    }

    private void initView() {
        mUserPhotoImg    = (ImageView)findViewById(R.id.detail_recyclerView_item_user_photo);
        mUserNameTv      = (TextView)findViewById(R.id.detail_recyclerView_item_user_name);
        mContentTv       = (ExpandTextView)findViewById(R.id.detail_recyclerView_item_content);
        mLocationTv      = (TextView)findViewById(R.id.detail_recyclerView_item_location);
        mTimeTv          = (TextView)findViewById(R.id.detail_recyclerView_item_time);
        mPriseTv         = (TextView)findViewById(R.id.detail_recyclerView_item_praise);
        mCommentTv       = (TextView)findViewById(R.id.detail_recyclerView_item_comment);
        mCommentBodyView = (LinearLayout)findViewById(R.id.detail_comment_body);
        mPraiseListView  = (PraiseListView)findViewById(R.id.detail_praise_listView);
        mCommentListView = (CommentListView)findViewById(R.id.detail_comment_listview);
        mCommentLineView = findViewById(R.id.detail_comment_line);
        mGradView = (GridView)findViewById(R.id.detail_recyclerView_item_gridview);
        mCommentEt = (EditText) findViewById(R.id.detail_comment_editview);
        mCommentSendTv = (TextView) findViewById(R.id.detail_comment_send_tv);
        mCommentContainerView = (LinearLayout) findViewById(R.id.detail_comment_container);
        mRootView = (FrameLayout) findViewById(R.id.detail_root_view);

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInputUtil.hideSoftInput(DetailCommentImgActivity.this, mCommentEt);
                mCommentContainerView.setVisibility(View.GONE);
            }
        });

        proCommentViewShow();

        initData();

    }

    private void initData() {
        //初始化点赞
        if (isPrised() != -1) {
           mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
        } else {
            mPriseTv.setBackgroundResource(R.mipmap.circle_praise_icon);
        }


        //处理朋友圈文本
        if (TextUtils.isEmpty(mFootPrint.getContent())) {
            mContentTv.setVisibility(View.GONE);
        } else {
            mContentTv.setVisibility(View.VISIBLE);
            mContentTv.setText(mFootPrint.getContent());
        }


        if(!TextUtils.isEmpty(mFootPrint.getContent())){
            mContentTv.setExpand(mFootPrint.isExpand());
            mContentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                @Override
                public void statusChange(boolean isExpand) {
                    mFootPrint.setExpand(isExpand);
                }
            });

            mContentTv.setText(UrlUtils.formatUrlString(mFootPrint.getContent()));
        }
        mContentTv.setVisibility(TextUtils.isEmpty(mFootPrint.getContent()) ? View.GONE : View.VISIBLE);


        //处理位置
        if (mFootPrint.isShowLocation()) {
            String address = "未知";
            if (mFootPrint.getFootPrintAddress() != null) {
                address = mFootPrint.getFootPrintAddress().getCity() + "."
                        + mFootPrint.getFootPrintAddress().getDistrict() + "."
                        + mFootPrint.getFootPrintAddress().getStreet();
            }
            mLocationTv.setText(address);
        } else {
            mLocationTv.setVisibility(View.GONE);
        }

        //处理日期
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date strD = lsdStrFormat.parse(mFootPrint.getCreatedAt());
            DateFormat dFormat = new SimpleDateFormat("MM月dd日  HH:mm");
            String format = dFormat.format(strD);
            mTimeTv.setText(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //设置用户信息
        String userId = mFootPrint.getUserId();
        UserHelper.getInstance().queryUser("objectId", userId, new UserHelper.UserQueryCallback() {
            @Override
            public void onResult(List<CAUser> users, BmobException e) {

                if (users != null && users.size() > 0) {
                    CAUser caUser = users.get(0);
                    if (caUser != null) {
                        GlideUtil.loadPic(DetailCommentImgActivity.this, caUser.getUserPhoto(), mUserPhotoImg);
                        mUserNameTv.setText(caUser.getUsername());
                    }

                }
            }
        });


        //根据媒体文件，数量，GridView设置布局
        if (getType(mFootPrint) == 1) {

            List<String> imageUrls = new ArrayList<String>();
            for (int i = 0; i < mFootPrint.getFootPrintFiles().size(); i++) {
                imageUrls.add(mFootPrint.getFootPrintFiles().get(i).getFilePath());
            }

            CircleGridViewAdapter circleGridViewAdapter;
            ViewGroup.LayoutParams layoutParams = mGradView.getLayoutParams();

            switch (imageUrls.size()) {
                case 1:
                    layoutParams.height = PixelUtil.dip2px(this, 200);
                    layoutParams.width = PixelUtil.dip2px(this, 120);
                    mGradView.setNumColumns(1);
                    circleGridViewAdapter = new CircleGridViewAdapter(this, R.layout.discover_circle_gridview_item1, imageUrls);
                    mGradView.setAdapter(circleGridViewAdapter);
                    break;
                case 2:
                    layoutParams.width = PixelUtil.dip2px(this, 155);
                    layoutParams.height = PixelUtil.dip2px(this, 75);
                    mGradView.setNumColumns(2);
                    circleGridViewAdapter = new CircleGridViewAdapter(this, R.layout.discover_circle_gridview_item, imageUrls);
                    mGradView.setAdapter(circleGridViewAdapter);
                    break;
                case 3:
                    layoutParams.width = PixelUtil.dip2px(this, 235);
                    layoutParams.height = PixelUtil.dip2px(this, 75);
                    mGradView.setNumColumns(3);
                    circleGridViewAdapter = new CircleGridViewAdapter(this, R.layout.discover_circle_gridview_item, imageUrls);
                    mGradView.setAdapter(circleGridViewAdapter);
                    break;

                case 4:
                    layoutParams.width = PixelUtil.dip2px(this, 155);
                    layoutParams.height = PixelUtil.dip2px(this, 155);
                    mGradView.setNumColumns(2);
                    circleGridViewAdapter = new CircleGridViewAdapter(this, R.layout.discover_circle_gridview_item, imageUrls);
                    mGradView.setAdapter(circleGridViewAdapter);
                    break;
                case 5:
                case 6:
                    layoutParams.width = PixelUtil.dip2px(this, 235);
                    layoutParams.height = PixelUtil.dip2px(this, 155);
                    mGradView.setNumColumns(3);
                    circleGridViewAdapter = new CircleGridViewAdapter(this, R.layout.discover_circle_gridview_item, imageUrls);
                    mGradView.setAdapter(circleGridViewAdapter);
                    break;

                default:
                    layoutParams.width = PixelUtil.dip2px(this, 235);
                    layoutParams.height = PixelUtil.dip2px(this, 235);
                    mGradView.setNumColumns(3);
                    circleGridViewAdapter = new CircleGridViewAdapter(this, R.layout.discover_circle_gridview_item, imageUrls);
                    mGradView.setAdapter(circleGridViewAdapter);
                    break;
            }


        }


        //处理评论
        proCommentMsg(-1,0);
        mCommentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentContainerView.setVisibility(View.VISIBLE);
                SoftInputUtil.showSoftInput(DetailCommentImgActivity.this, mCommentEt);
                proCommentMsg(-1,0);

            }
        });

        mCommentListView.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                CommentItem commentItem = mFootPrint.getComments().get(position);
                if (commentItem != null) {
                    if (!commentItem.getUser().getObjectId().equals(UserHelper.getInstance().getCurrentUser().getObjectId())) {
                        Toast.makeText(DetailCommentImgActivity.this, "对不起，没有权限删除此条评论.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    return;
                }
                showDeleteCommentDialog(position);
            }
        });

        mCommentListView.setOnItemClickListener(new CommentListView.OnItemClickListener() {
             @Override
             public void onItemClick(int position) {
                 mCommentContainerView.setVisibility(View.VISIBLE);
                 SoftInputUtil.showSoftInput(DetailCommentImgActivity.this, mCommentEt);
                 proCommentMsg(position,1);

             }
         });

        mCommentListView.setOnItemUserClickListener(new CommentListView.OnItemUserClickListener() {
            @Override
            public void onItemUserClick(String userId) {
                Toast.makeText(DetailCommentImgActivity.this, "点击评论用户-> userId:" + userId, Toast.LENGTH_SHORT).show();
            }
        });

        List<CommentItem> commentItems = mFootPrint.getComments();
        if (commentItems != null) {
            mCommentListView.setDatas(commentItems);
        }


        //处理点赞
        mPriseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PraiseItem> praiseItems = mFootPrint.getPraises();
                CAUser currentUser = UserHelper.getInstance().getCurrentUser();
                if (praiseItems != null) {
                    int index = isPrised();
                    if (index != -1) {
                        //已赞
                        final PraiseItem praiseItem = mFootPrint.getPraises().get(index);
                        mFootPrint.getPraises().remove(index);
                        //CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                        mFootPrint.update(mFootPrint.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    //数据更新失败，再将删除的数据添加回去
                                    mFootPrint.getPraises().add(praiseItem);
                                    mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
                                }

                            }
                        });
                        proCommentViewShow();
                        mPriseTv.setBackgroundResource(R.mipmap.circle_praise_icon);
                        if ( mFootPrint.getPraises() != null) {
                            mPraiseListView.setDatas( mFootPrint.getPraises());
                        }
                    } else {
                        //未赞
                        final PraiseItem praiseItem = new PraiseItem();
                        praiseItem.setFootPrintId(mFootPrint.getObjectId());
                        praiseItem.setUser(currentUser);
                        mFootPrint.getPraises().add(praiseItem);
                        //CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                        mFootPrint.update(mFootPrint.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    //数据更新失败，再将添加的数据删除
                                    mFootPrint.getPraises().remove(praiseItem);
                                    mPriseTv.setBackgroundResource(R.mipmap.circle_praise_icon);
                                }
                            }
                        });
                        proCommentViewShow();
                        mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);
                        if ( mFootPrint.getPraises() != null) {
                            mPraiseListView.setDatas( mFootPrint.getPraises());
                        }
                    }

                } else {
                    //未赞
                    mFootPrint.setPraises(new ArrayList<PraiseItem>());
                    final PraiseItem praiseItem = new PraiseItem();
                    praiseItem.setFootPrintId(mFootPrint.getObjectId());
                    praiseItem.setUser(currentUser);
                    mFootPrint.getPraises().add(praiseItem);
                    //CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                    mFootPrint.update(mFootPrint.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                //数据更新失败，再将添加的数据删除
                                mFootPrint.getPraises().remove(praiseItem);
                                mPriseTv.setBackgroundResource(R.mipmap.circle_praise_icon);
                            }
                        }
                    });
                    proCommentViewShow();
                    mPriseTv.setBackgroundResource(R.mipmap.circle_praised_icon);

                    if ( mFootPrint.getPraises() != null) {
                        mPraiseListView.setDatas( mFootPrint.getPraises());
                    }
                }
            }
        });

        mPraiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(DetailCommentImgActivity.this, "点击点赞用户-> position:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        List<PraiseItem> praiseItems = mFootPrint.getPraises();
        if (praiseItems != null) {
            mPraiseListView.setDatas(praiseItems);
        }
    }


    private int isPrised(){
        int ret = -1;
        CAUser currentUser = UserHelper.getInstance().getCurrentUser();
        List<PraiseItem> praiseItems= mFootPrint.getPraises();
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

    private void proCommentViewShow(){

        List<CommentItem> comments = mFootPrint.getComments();
        List<PraiseItem>  praises = mFootPrint.getPraises();

        if (mFootPrint == null) {
            return;
        }

        if (isEmityList(comments) && isEmityList(praises) ){
            mCommentBodyView.setVisibility(View.GONE);
            return;
        }

        if (isEmityList(comments)){
            mCommentListView.setVisibility(View.GONE);

        }else{
            mCommentListView.setVisibility(View.VISIBLE);
            mCommentBodyView.setVisibility(View.VISIBLE);
        }

        if (isEmityList(praises)){
            mPraiseListView.setVisibility(View.GONE);
            mCommentLineView.setVisibility(View.GONE);
        }else{
            mPraiseListView.setVisibility(View.VISIBLE);
            mCommentLineView.setVisibility(View.VISIBLE);
            mCommentBodyView.setVisibility(View.VISIBLE);
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


    private void showDeleteCommentDialog(final int commentIndex){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("删除此条评论？");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final CommentItem commentItem = mFootPrint.getComments().get(commentIndex);
                mFootPrint.getComments().remove(commentIndex);
               // CircleRecyclerViewAdapter.this.notifyDataSetChanged();
                mFootPrint.update(mFootPrint.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null) {
                            Toast.makeText(DetailCommentImgActivity.this, "删除成功.", Toast.LENGTH_SHORT).show();
                            if (mFootPrint.getComments() != null) {
                                mCommentListView.setDatas(mFootPrint.getComments());
                            }
                        }else{
                            mFootPrint.getComments().add(commentIndex,commentItem);
                            Toast.makeText(DetailCommentImgActivity.this, "删除失败.", Toast.LENGTH_SHORT).show();
                            if (mFootPrint.getComments() != null) {
                                mCommentListView.setDatas(mFootPrint.getComments());
                            }
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

    private void proCommentMsg(int commentIndex, final int type) {

        if(mFootPrint.getComments() == null){
            mFootPrint.setComments(new ArrayList<CommentItem>());
        }

        final CommentItem commentItem = new CommentItem();
        commentItem.setUser(UserHelper.getInstance().getCurrentUser());
        commentItem.setObjectId(mFootPrint.getObjectId());

        String hintTxt = "评论:";
        if (type == 1){
            CAUser toReplyUser = mFootPrint.getComments().get(commentIndex).getUser();
            commentItem.setToReplyUser(toReplyUser);
            hintTxt = "回复:" + toReplyUser.getUsername();
        }
        mCommentEt.setHint(hintTxt);
        mCommentSendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "评论内容不能为空...";
                if (type == 1){
                    tipMsg = "回复内容不能为空...";
                }

                String content =  mCommentEt.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(DetailCommentImgActivity.this, tipMsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                commentItem.setContent(content);
                mFootPrint.getComments().add(commentItem);

                mFootPrint.update(mFootPrint.getObjectId(), new UpdateListener() {
                    @Override public void done(BmobException e) {
                        SoftInputUtil.hideSoftInput(DetailCommentImgActivity.this, mCommentEt);

                        if (e==null) {
                            mCommentContainerView.setVisibility(View.GONE);
                            if (mFootPrint.getComments() != null) {
                                mCommentListView.setDatas(mFootPrint.getComments());
                            }
                            mCommentEt.setText("");

                        }else{
                            Toast.makeText(DetailCommentImgActivity.this, "评论失败.", Toast.LENGTH_SHORT).show();
                            mFootPrint.getComments().remove(commentItem);
                            if (mFootPrint.getComments() != null) {
                                mCommentListView.setDatas(mFootPrint.getComments());
                            }

                        }

                    }
                });

            }
        });
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
