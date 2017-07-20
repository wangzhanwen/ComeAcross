package com.lyy_wzw.comeacross.discovery.activitys;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.discovery.adapter.PersonalRecyclerViewAdapter;
import com.lyy_wzw.comeacross.discovery.widgets.CircleBottomRefreshView;
import com.lyy_wzw.comeacross.server.FootPrintServer;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

public class PersonalCircleActivity extends AppCompatActivity {
    private static final String TAG = "PersonalCircleActivity";

    private List<FootPrint> mDatas = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PersonalRecyclerViewAdapter mRecyclerViewAdapter;


    private TextView mUserNameTV;
    private ImageView mUserPhotoIv;
    private CircleBottomRefreshView mRefreshView;
    private LinearLayout mRefreshContainer;

    private boolean isRefreshing = false;
    private boolean  isLoading = false;
    private int mLastVisibleItem;
    private NestedScrollView mNestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_circle);

        initView();

    }

    private void initView() {
        mUserNameTV = (TextView) findViewById(R.id.personal_circle_user_name);
        mUserPhotoIv = (ImageView) findViewById(R.id.personal_circle_user_photo);
        mRefreshView = (CircleBottomRefreshView) findViewById(R.id.personal_circle_bottom_refresh_view);
        mRefreshContainer = (LinearLayout) findViewById(R.id.personal_circle_bottom_refresh_container);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.personal_circle_nestedscrollview);

        mRecyclerView = (RecyclerView) findViewById(R.id.personal_circle_recyclerview);
        mLayoutManager = new LinearLayoutManager(PersonalCircleActivity.this, LinearLayoutManager.VERTICAL, false);
       //mLayoutManager = new CALinearLayoutManager(PersonalCircleActivity.this, LinearLayoutManager.VERTICAL, false, getScreenHeight(this));

        mRecyclerView.setLayoutManager(mLayoutManager);



        mRecyclerViewAdapter = new PersonalRecyclerViewAdapter(this, mDatas);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        mNestedScrollView.scrollTo(0, 5);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    //Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    //Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    mNestedScrollView.scrollTo(0, 5);
                    onRefresh();
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");
                    mNestedScrollView.scrollTo(scrollX, scrollY-5);
                    mRecyclerViewAdapter.setIsLoadMore(true);
                    onLoad();
                }
            }
        });

//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mRecyclerViewAdapter.getItemCount()) {
//                    mRecyclerViewAdapter.setIsLoadMore(true);
//                    onLoad();
//                }
//
//            }
//        });
        onRefresh();
    }

    public void onRefresh(){
        if (!isRefreshing) {
            isRefreshing = true;
            mRefreshContainer.setVisibility(View.VISIBLE);
            mRefreshView.startRefreshAnim();
            FootPrintServer.getInstance().getAll(new FootPrintServer.FootPrintQueryCallback() {
                @Override
                public void onSuccess(List<FootPrint> footPrints) {
                    isRefreshing = false;
                    mRefreshContainer.setVisibility(View.GONE);
                    mRefreshView.stopRefreshAnim();
                    if (footPrints != null){
                        mDatas.clear();
                        mDatas.addAll(footPrints);
                        mDatas.addAll(footPrints);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(BmobException e) {
                    isRefreshing = false;
                    mRefreshContainer.setVisibility(View.GONE);
                    mRefreshView.stopRefreshAnim();
                    Toast.makeText(PersonalCircleActivity.this, "刷新失败:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PersonalCircleActivity.this, "加载数据失败:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(PersonalCircleActivity.this, "正在加载请稍后", Toast.LENGTH_SHORT).show();
        }
    }


    private int getScreenHeight(Context context) {
        int measuredHeight;
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            measuredHeight = size.y;
        } else {
            Display d = wm.getDefaultDisplay();
            measuredHeight = d.getHeight();
        }

        return measuredHeight;
    }
}
