package com.lyy_wzw.comeacross.discovery.activitys;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.design.widget.AppBarLayout;
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
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.CommentItem;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.bean.ReleaseFootPrintEvent;
import com.lyy_wzw.comeacross.discovery.DicoveryConstantValue;
import com.lyy_wzw.comeacross.discovery.adapter.CircleRecyclerViewAdapter;

import com.lyy_wzw.comeacross.discovery.widgets.CircleTopRefreshView;
import com.lyy_wzw.comeacross.discovery.widgets.CommentListView;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.server.FootPrintServer;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.GlideUtil;

import com.lyy_wzw.comeacross.utils.SoftInputUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class FootPrintCircleActivity extends AppCompatActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = "FootPrintCircleActivity";

    private CoordinatorLayout mCircleRootView;
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
    private AppBarLayout mAppBarLayout;
    private CircleTopRefreshView mTopRefreshView;
    private LinearLayoutManager mLayoutManager;
    private int mLastVisibleItem;


    private int screenHeight;
    private int appBarHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;
    private int circleItemPosition;
    private int commentItemPosition;

    private boolean  isLoading = false;
    private boolean  isRefreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print_circle);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mCircleRootView  = (CoordinatorLayout) findViewById(R.id.circle_container_layout);
        mToolbar = (Toolbar) findViewById(R.id.circle_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.circle_app_bar);
        mBackbtn = (ImageView) findViewById(R.id.circle_back_btn);
        mShareFootPrintbtn = (ImageView) findViewById(R.id.circle_share_footprint_btn);
        mRecyClerView   = (RecyclerView) findViewById(R.id.circle_recyclerView);
        mCommentContainerView = (LinearLayout) findViewById(R.id.circle_comment_container);
        mCommentEt = (EditText) findViewById(R.id.circle_comment_editview);
        mCommentSendTv = (TextView) findViewById(R.id.circle_comment_send_tv);
        mCommentContainerView.setVisibility(View.GONE);

        mTopRefreshView = (CircleTopRefreshView) findViewById(R.id.circle_top_refresh_view);
        mTopRefreshView.setVisibility(View.GONE);
        mTopRefreshView.setRefreshCallback(new CircleTopRefreshView.RefreshCallback() {
            @Override
            public void onRefresh() {
                if (!isRefreshing){
                    Message refreshMsg = new Message();
                    refreshMsg.what = DicoveryConstantValue.CIRCLE_HANDLER_REFRESH_MSG_KEY;
                    mHander.sendMessage(refreshMsg);
                }

            }
        });

        mBackbtn.setOnClickListener(this);
        mShareFootPrintbtn.setOnClickListener(this);

        mRecyClerView.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(FootPrintCircleActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyClerView.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapter = new CircleRecyclerViewAdapter(FootPrintCircleActivity.this, mDatas, mHander);
        mRecyClerView.setAdapter(mRecyclerViewAdapter);

        mRecyClerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                  //updateEditTextBodyVisible(View.GONE);
                  mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                //updateEditTextBodyVisible(View.GONE);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mRecyclerViewAdapter.getItemCount()) {
                    mRecyclerViewAdapter.setIsLoadMore(true);
                    onLoad();
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
                Intent intent = new Intent(FootPrintCircleActivity.this, PersonalCircleActivity.class);
                startActivity(intent);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setViewTreeObserver();
        onRefresh();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReleaseFootPrintEvent(ReleaseFootPrintEvent event){
        if (event!=null  && event.getFootPrint()!=null ){
            mDatas.add(event.getFootPrint());
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = mCircleRootView.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mCircleRootView.getWindowVisibleDisplayFrame(r);
                int statusBarH =  getStatusBarHeight();//状态栏高度
                int screenH = mCircleRootView.getRootView().getHeight();
                if(r.top != statusBarH ){
                    //r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d(TAG, "screenH＝ "+ screenH +" &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if(keyboardH == currentKeyboardH){//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = mCommentContainerView.getHeight();

                if (keyboardH < 200) {
                    //说明是隐藏键盘的情况
                    return;
                }

                if(mLayoutManager!=null){
                    mLayoutManager.scrollToPositionWithOffset(circleItemPosition,
                            screenHeight - appBarHeight - getStatusBarHeight() -
                                    (selectCircleItemH + currentKeyboardH) + selectCommentItemOffset ) ;

                }

            }
        });
    }




    public void onRefresh(){
        if (!isRefreshing) {
            isRefreshing = true;
            mTopRefreshView.startRefreshAnim();
            FootPrintServer.getInstance().getAll(new FootPrintServer.FootPrintQueryCallback() {
                @Override
                public void onSuccess(List<FootPrint> footPrints) {
                    isRefreshing = false;
                    mTopRefreshView.stopRefreshAnim();
                    if (footPrints != null){
                        mDatas.clear();
                        mDatas.addAll(footPrints);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(BmobException e) {
                    isRefreshing = false;
                    mTopRefreshView.stopRefreshAnim();
                    Toast.makeText(FootPrintCircleActivity.this, "刷新失败:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void onLoad(){
        if (!isLoading) {
            isLoading = true;
            FootPrintServer.getInstance().getAll(new FootPrintServer.FootPrintQueryCallback() {
                @Override
                public void onSuccess(List<FootPrint> footPrints) {
                    isLoading = false;
                    mRecyclerViewAdapter.setIsLoadMore(false);
                    if (footPrints != null && footPrints.size() >0){
                        mDatas.addAll(footPrints);
                    }
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(BmobException e) {
                    isLoading = false;
                    mRecyclerViewAdapter.setIsLoadMore(false);
                    Toast.makeText(FootPrintCircleActivity.this, "加载数据失败:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(FootPrintCircleActivity.this, "正在加载请稍后", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * 获取状态栏高度
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void measureCircleItemHighAndCommentItemOffset(int circleItemPosition, int commentItemPosition){

        int firstPosition = mLayoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = mLayoutManager.getChildAt(circleItemPosition - firstPosition);

        if(selectCircleItem != null){
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if (mDatas.get(circleItemPosition).getComments()==null || mDatas.get(circleItemPosition).getComments().size() < 1) {
            selectCommentItemOffset = 0;
            return;
        }
        int commentCount = mDatas.get(circleItemPosition).getComments().size();
           //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.circle_comment_listview);
            if(commentLv!=null && commentItemPosition!=-1){
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                selectCommentItemOffset = (commentLv.getHeight() / commentCount) * (commentCount - commentItemPosition-1);
            }else{
                selectCommentItemOffset = 0;
            }

    }

    public void updateEditTextBodyVisible(int visibility) {

        mCommentEt.setVisibility(visibility);
        mCommentContainerView.setVisibility(visibility);

        if(View.VISIBLE==visibility){
            measureCircleItemHighAndCommentItemOffset(circleItemPosition, commentItemPosition);
            mCommentEt.requestFocus();
            //弹出键盘
            SoftInputUtil.showSoftInput( mCommentEt.getContext(),  mCommentEt);
        }else if(View.GONE==visibility){
            //隐藏键盘
            SoftInputUtil.hideSoftInput( mCommentEt.getContext(),  mCommentEt);
        }
    }

    Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
               //评论
               case DicoveryConstantValue.CIRCLE_COMMENT_HANDLER_COMMENT_MSG_KEY:
                    proCommentMsg(msg,0);
                    break;

               //回复
               case DicoveryConstantValue.CIRCLE_COMMENT_HANDLER_REPLY_MSG_KEY:
                    proCommentMsg(msg, 1);
                   break;
               //隐藏键盘
               case DicoveryConstantValue.CIRCLE_COMMENT_HANDLER_Hide_SOFTINPUT_KEY:
                    updateEditTextBodyVisible(View.GONE);
                    break;
                case DicoveryConstantValue.CIRCLE_HANDLER_REFRESH_MSG_KEY:
                    if (!isRefreshing) {
                        onRefresh();
                    }
                    break;
           }
        }

        private void proCommentMsg(final Message msg, final int type) {

            Bundle bundle = msg.getData();
            if (bundle == null) {
                return;
            }

            final int position = bundle.getInt(DicoveryConstantValue.CIRCLE_COMMENT_BUNDLE_FOOTPRINT_INDEX_KEY);
            circleItemPosition = position;
            final FootPrint footPrint = mDatas.get(position);
            if (footPrint == null) {
                return;
            }
            if(footPrint.getComments() == null){
                footPrint.setComments(new ArrayList<CommentItem>());
            }

            final CommentItem commentItem = new CommentItem();
            commentItem.setUser(UserHelper.getInstance().getCurrentUser());
            commentItem.setObjectId(footPrint.getObjectId());
            final int commentIndex = bundle.getInt(DicoveryConstantValue.CIRCLE_COMMENT_BUNDLE_COMMENT_ITEM_INDEX_KEY,  -1);
            commentItemPosition = commentIndex;

            updateEditTextBodyVisible(View.VISIBLE);

            String hintTxt = "评论:";
            if (type == 1){
                CAUser toReplyUser = footPrint.getComments().get(commentIndex).getUser();
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
                        Toast.makeText(FootPrintCircleActivity.this, tipMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    commentItem.setContent(content);
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
                                footPrint.getComments().remove(commentItem);
                                mRecyclerViewAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                }
            });
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
                updateEditTextBodyVisible(View.GONE);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(mCommentContainerView != null && mCommentContainerView.getVisibility() == View.VISIBLE){
                updateEditTextBodyVisible(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTopRefreshView.stopRefreshAnim();
        updateEditTextBodyVisible(View.GONE);
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        appBarHeight = mAppBarLayout.getHeight() + verticalOffset;

    }
}
