package com.lyy_wzw.comeacross.discovery.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.CommentItem;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.discovery.DicoveryConstantValue;
import com.lyy_wzw.comeacross.discovery.adapter.CircleRecyclerViewAdapter;

import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.server.FootPrintServer;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.GlideUtil;
import com.lyy_wzw.comeacross.utils.SoftInputUtil;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class FootPrintCircleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FootPrintCircleActivity";

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private ImageView mShareFootPrintbtn;
    private ImageView mBackbtn;
    private RecyclerView mRecyClerView;
    private List<FootPrint> mDatas = new ArrayList<>();
    private EditText mCommentEt;
    private TextView mCommentSendTv;
    private LinearLayout mCommentContainerView;
    private CircleRecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print_circle);
        onRefresh();
        initView();

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.circle_toolbar);
        mBackbtn = (ImageView) findViewById(R.id.circle_back_btn);
        mShareFootPrintbtn = (ImageView) findViewById(R.id.circle_share_footprint_btn);
        mRecyClerView   = (RecyclerView) findViewById(R.id.circle_recyclerView);
        mCommentContainerView = (LinearLayout) findViewById(R.id.circle_comment_container);
        mCommentEt = (EditText) findViewById(R.id.circle_comment_editview);
        mCommentSendTv = (TextView) findViewById(R.id.circle_comment_send_tv);
        mCommentContainerView.setVisibility(View.GONE);

        mBackbtn.setOnClickListener(this);
        mShareFootPrintbtn.setOnClickListener(this);

        mRecyClerView.setOnClickListener(this);
        mRecyClerView.setLayoutManager(new LinearLayoutManager(FootPrintCircleActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewAdapter = new CircleRecyclerViewAdapter(FootPrintCircleActivity.this, mDatas, mHander);
        mRecyClerView.setAdapter(mRecyclerViewAdapter);

        mRecyClerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCommentContainerView.getVisibility() == View.VISIBLE) {
                    mCommentContainerView.setVisibility(View.GONE);
                    SoftInputUtil.hideSoftInput(FootPrintCircleActivity.this, mCommentEt);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mCommentContainerView.getVisibility() == View.VISIBLE) {
                    mCommentContainerView.setVisibility(View.GONE);
                    SoftInputUtil.hideSoftInput(FootPrintCircleActivity.this, mCommentEt);
                }


            }
        });

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("足迹圈");

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        CAUser currentUser = UserHelper.getInstance().getCurrentUser();
        GlideUtil.loadCirclePic(this,
                 currentUser.getUserPhoto(),
                 mFab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void onRefresh(){
        FootPrintServer.getInstance().getAll(new FootPrintServer.FootPrintQueryCallback() {
            @Override
            public void onSuccess(List<FootPrint> footPrints) {
                Log.d(TAG, "footPrints:"+footPrints.toString());
                if (footPrints != null){

                    mDatas.clear();
                    mDatas.addAll(footPrints);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(BmobException e) {
                Log.d(TAG, "e:"+e.getMessage());
            }
        });
    }

    Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case DicoveryConstantValue.CIRCLE_COMMENT_HANDLER_MSG_KEY:
                   Log.d(TAG,"handleMessage()-->>收到评论handler消息");
                   if (mCommentContainerView.getVisibility() == View.GONE) {
                       mCommentContainerView.setVisibility(View.VISIBLE);
                       SoftInputUtil.showSoftInput(FootPrintCircleActivity.this, mCommentEt);

                   }

                   Bundle bundle = msg.getData();
                   final int position = bundle.getInt(DicoveryConstantValue.CIRCLE_COMMENT_BUNDLE_FOOTPRINT_INDEX_KEY);
                   mCommentSendTv.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Toast.makeText(FootPrintCircleActivity.this, "发送评论", Toast.LENGTH_SHORT).show();
                           //发布评论
                           String content =  mCommentEt.getText().toString().trim();
                           if(TextUtils.isEmpty(content)){
                               Toast.makeText(FootPrintCircleActivity.this, "评论内容不能为空...", Toast.LENGTH_SHORT).show();
                               return;
                           }

                           FootPrint footPrint = mDatas.get(position);
                           if (footPrint == null) {
                               return;
                           }

                           if(footPrint.getComments() == null){
                               footPrint.setComments(new ArrayList<CommentItem>());
                           }

                           CommentItem commentItem = new CommentItem();
                           commentItem.setUser(UserHelper.getInstance().getCurrentUser());
                           commentItem.setContent(content);
                           commentItem.setObjectId(footPrint.getObjectId());
                           footPrint.getComments().add(commentItem);

                           footPrint.update(footPrint.getObjectId(), new UpdateListener() {
                               @Override public void done(BmobException e) {
                                   if (e==null) {
                                       mRecyclerViewAdapter.notifyDataSetChanged();
                                       mCommentContainerView.setVisibility(View.GONE);
                                       if (SoftInputUtil.isShowSoftInput(FootPrintCircleActivity.this)) {
                                           SoftInputUtil.hideSoftInput(FootPrintCircleActivity.this, mCommentEt);
                                       }


                                   }else{
                                       Toast.makeText(FootPrintCircleActivity.this, "评论出错.", Toast.LENGTH_SHORT).show();
                                   }

                               }
                           });

                       }
                   });
                   break;
           }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle_back_btn:
                finish();
                break;
            case R.id.circle_share_footprint_btn:
                ShareFootPrintPopupWin shareFootPrintPW = new ShareFootPrintPopupWin(this);
                shareFootPrintPW.setSelectImageCount(FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT);
                shareFootPrintPW.showAtLocation(mShareFootPrintbtn, Gravity.CENTER, 0, 0);
                break;
            case R.id.circle_comment_send_tv:
                break;
            case R.id.circle_recyclerView:
                if (mCommentContainerView.getVisibility() == View.VISIBLE) {
                    mCommentContainerView.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(mCommentContainerView != null && mCommentContainerView.getVisibility() == View.VISIBLE){
                mCommentContainerView.setVisibility(View.GONE);
                if (SoftInputUtil.isShowSoftInput(FootPrintCircleActivity.this)) {
                    SoftInputUtil.hideSoftInput(FootPrintCircleActivity.this, mCommentEt);
                }

                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
