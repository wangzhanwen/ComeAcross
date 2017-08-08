package com.lyy_wzw.comeacross.discovery.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.CommentItem;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.bean.FootPrintFile;
import com.lyy_wzw.comeacross.bean.ImageClickEvent;
import com.lyy_wzw.comeacross.bean.PraiseItem;
import com.lyy_wzw.comeacross.discovery.DicoveryConstantValue;
import com.lyy_wzw.comeacross.discovery.adapter.DetailViewPagerAdapter;
import com.lyy_wzw.comeacross.discovery.widgets.ExpandTextView;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ImageLookViewPager;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.Animutil;
import com.lyy_wzw.comeacross.utils.GlideUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class FootPrintDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FootPrintDetailActivity";

    private FootPrint mFootPrint;
    private ImageLookViewPager mImgViewPager;
    private DetailViewPagerAdapter mAdapter;
    private LinearLayout mTopBar;
    private VideoView mVideoView;
    private ImageView mPlayBtn;
    private FrameLayout  mVideoContainer;
    private TextView mImageCountTv;
    private TextView mDateTv;
    private ExpandTextView mContentView;
    private RelativeLayout mBottomBar;
    private LinearLayout mPriseLeftBtn;
    private LinearLayout mCommentLeftBtn;
    private ImageView mPriseRightBtn;
    private ImageView mCommentRightBtn;
    private TextView mCommentCountTv;
    private TextView mPriseCountTv;
    private FrameLayout mRootView;

    private boolean isPlaying = false;
    private boolean isBarShowed = true;
    private FrameLayout mViewPagerContainer;
    private ImageView mPriseView;
    private ImageView mBackView;
    private ImageView mVideoBgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print_detail);
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(FootPrintConstantValue.CIRCLE_DETAIL_BUNDLE_KEY);

        if (bundle!=null){
            Serializable serializable = bundle.getSerializable(FootPrintConstantValue.CIRCLE_DETAIL_BUNDLE_FOOTPRINT_KEY);
            if (serializable != null) {
                mFootPrint = (FootPrint)serializable;
                Log.d(TAG, mFootPrint.toString());
            }

        }

        intView();
        initData();


        if (mFootPrint != null) {
            if (getFootPrintType(mFootPrint) == 1){
                initImageView();
            }else{
                initVideoView();
            }
        }

    }


    private void intView(){
        mRootView = (FrameLayout) findViewById(R.id.circle_detail_root_view);
        mImgViewPager = (ImageLookViewPager) findViewById(R.id.circle_detail_imagelook_viewpager);
        mViewPagerContainer = (FrameLayout) findViewById(R.id.circle_detail_viewpager_container);
        mVideoContainer = (FrameLayout) findViewById(R.id.circle_detail_video_container);
        mVideoView = (VideoView) findViewById(R.id.circle_detail_video_view);
        mPlayBtn = (ImageView) findViewById(R.id.circle_detail_video_play_btn);
        mTopBar = (LinearLayout) findViewById(R.id.circle_detail_topbar_container);
        mDateTv = (TextView) findViewById(R.id.circle_detail_date);
        mImageCountTv = (TextView) findViewById(R.id.circle_detail_image_count);
        mContentView = (ExpandTextView) findViewById(R.id.circle_detail_content);
        mBottomBar = (RelativeLayout) findViewById(R.id.circle_detail_bottom_bar_container);
        mPriseLeftBtn = (LinearLayout) findViewById(R.id.circle_detail_prise_left_container);
        mPriseView = (ImageView) findViewById(R.id.circle_detail_prise_iv);
        mCommentLeftBtn = (LinearLayout) findViewById(R.id.circle_detail_comment_left_container);
        mPriseRightBtn = (ImageView) findViewById(R.id.circle_detail_prise_right);
        mCommentRightBtn = (ImageView) findViewById(R.id.circle_detail_comment_right);
        mCommentCountTv = (TextView) findViewById(R.id.circle_detail_comment_count);
        mPriseCountTv = (TextView) findViewById(R.id.circle_detail_prise_count);
        mBackView = (ImageView) findViewById(R.id.circle_detail_back_iv);
        mVideoBgView = (ImageView) findViewById(R.id.circle_detail_video_bg);

        mBackView.setOnClickListener(this);
        mPriseLeftBtn.setOnClickListener(this);
        mPriseRightBtn.setOnClickListener(this);

        mCommentRightBtn.setOnClickListener(this);



    }

    private void initData() {
        mDateTv.setText(mFootPrint.getCreatedAt());
        mContentView.setContentTextColor(Color.parseColor("#ffffff"));
        if (!TextUtils.isEmpty(mFootPrint.getContent())) {
            mContentView.setText(mFootPrint.getContent());
        }else{
            mContentView.setVisibility(View.GONE);
        }

        List<PraiseItem> praises = mFootPrint.getPraises();
        if (praises != null) {
            mPriseCountTv.setText(String.valueOf(praises.size()));
        }else{
            mPriseCountTv.setText("0");
        }


        List<CommentItem> comments = mFootPrint.getComments();
        if (comments != null) {
            mCommentCountTv.setText(String.valueOf(comments.size()));
        }else{
            mCommentCountTv.setText("0");
        }

        if ( isPrised() != -1) {
            mPriseView.setImageResource(R.mipmap.icon_circle_praised);
        }else{
            mPriseView.setImageResource(R.mipmap.icon_circle_praise);
        }
    }

    private void initImageView() {
        mVideoContainer.setVisibility(View.GONE);
        mPlayBtn.setVisibility(View.GONE);
        mVideoView.setVisibility(View.GONE);
        mVideoBgView.setVisibility(View.GONE);

        mImgViewPager.setVisibility(View.VISIBLE);
        mAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(), getImageUrls(mFootPrint));
        mImgViewPager.setAdapter(mAdapter);
        mImageCountTv.setText("1 / " + mFootPrint.getFootPrintFiles().size());

        mImgViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isBarShowed) {
                    hideBar();
                    isBarShowed = false;
                }
                mImageCountTv.setText((position + 1) + " / " + mFootPrint.getFootPrintFiles().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void initVideoView() {
        mVideoContainer.setVisibility(View.VISIBLE);
        mPlayBtn.setVisibility(View.VISIBLE);
        mVideoBgView.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.GONE);

        mImgViewPager.setVisibility(View.GONE);

        GlideUtil.loadPic(FootPrintDetailActivity.this, mFootPrint.getFootPrintFiles().get(0).getThumbnailPath(), mVideoBgView);

        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.GONE);

        mVideoView.setMediaController(mediaController);

        String url = mFootPrint.getFootPrintFiles().get(0).getFilePath();
        if (url!= null){
            Uri uri = Uri.parse(url);
            mVideoView.setVideoURI(uri);
            mPlayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isPlaying) {
                        mVideoView.start();
                        mPlayBtn.setVisibility(View.GONE);
                        isPlaying  = true;
                        hideBar();
                        isBarShowed = false;
                    }

                }
            });


        }


        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (isPlaying) {
                    mVideoView.pause();
                    mPlayBtn.setVisibility(View.VISIBLE);
                    isPlaying = false;
                    showBar();
                    isBarShowed = true;
                }
                return false;
            }
        });


        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoBgView.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);

                mVideoView.start();

            }
        });
    }



    private void showBar(){

        if (mTopBar.getVisibility()== View.GONE) {
            mTopBar.setVisibility(View.VISIBLE);
            Animutil.moveAnimDown(FootPrintDetailActivity.this,
                    mTopBar,
                    100,
                    500,
                    new Animutil.OnAnimListener(){
                        @Override
                        public void onEnd() {

                        }
                    });
        }

        if (mBottomBar.getVisibility() == View.GONE){
              mBottomBar.setVisibility(View.VISIBLE);
        }

    }

    private void hideBar(){

        if (mTopBar.getVisibility()== View.VISIBLE) {
            Animutil.moveAnimUp(FootPrintDetailActivity.this,
                    mTopBar,
                    100,
                    500,
                    new Animutil.OnAnimListener(){
                        @Override
                        public void onEnd() {
                            mTopBar.setVisibility(View.GONE);
                        }
                    });
        }

        if (mBottomBar.getVisibility() == View.VISIBLE){
            mBottomBar.setVisibility(View.GONE);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageClick(ImageClickEvent event){
        if (isBarShowed) {
            hideBar();
            isBarShowed = false;
        }else{
            showBar();
            isBarShowed = true;
        }
    }

    private List<String> getImageUrls(FootPrint footprint) {
        List<String> imageUrls = new ArrayList<>();
        List<FootPrintFile> footPrintFiles = footprint.getFootPrintFiles();
        if (footPrintFiles != null) {
            for (int i = 0; i < footPrintFiles.size(); i++) {
                imageUrls.add(footPrintFiles.get(i).getFilePath());
            }
        }

        return imageUrls;
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

    private void proPrise(){
        if (mFootPrint  == null){
            return;
        }

        List<PraiseItem> praiseItems = mFootPrint.getPraises();
        CAUser currentUser = UserHelper.getInstance().getCurrentUser();
        if (praiseItems != null) {
            int index = isPrised();

            if (index != -1) {
                //已赞
                final PraiseItem praiseItem = mFootPrint.getPraises().get(index);
                mFootPrint.getPraises().remove(index);

                mFootPrint.update(mFootPrint.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e != null) {
                            //数据更新失败，再将删除的数据添加回去
                            mFootPrint.getPraises().add(praiseItem);
                            mPriseView.setImageResource(R.mipmap.icon_circle_praised);
                        }

                    }
                });

               mPriseView.setImageResource(R.mipmap.icon_circle_praise);
            } else {
                //未赞
                final PraiseItem praiseItem = new PraiseItem();
                praiseItem.setFootPrintId(mFootPrint.getObjectId());
                praiseItem.setUser(currentUser);
                mFootPrint.getPraises().add(praiseItem);

                mFootPrint.update(mFootPrint.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e != null) {
                            //数据更新失败，再将添加的数据删除
                            mFootPrint.getPraises().remove(praiseItem);
                            mPriseView.setImageResource(R.mipmap.icon_circle_praise);
                        }
                    }
                });
                mPriseView.setImageResource(R.mipmap.icon_circle_praised);
            }

        } else {
            //未赞
            mFootPrint.setPraises(new ArrayList<PraiseItem>());
            final PraiseItem praiseItem = new PraiseItem();
            praiseItem.setFootPrintId(mFootPrint.getObjectId());
            praiseItem.setUser(currentUser);
            mFootPrint.getPraises().add(praiseItem);

            mFootPrint.update(mFootPrint.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null) {
                        //数据更新失败，再将添加的数据删除
                        mFootPrint.getPraises().remove(praiseItem);
                        mPriseView.setImageResource(R.mipmap.icon_circle_praise);
                    }
                }
            });

            mPriseView.setImageResource(R.mipmap.icon_circle_praised);
        }
    }


    private int isPrised(){
        int ret = -1;
        CAUser currentUser = UserHelper.getInstance().getCurrentUser();
        if (mFootPrint.getPraises() != null) {
            for (int i = 0; i < mFootPrint.getPraises().size(); i++) {
                PraiseItem praiseItem =  mFootPrint.getPraises().get(i);
                if (currentUser.getObjectId().equals(praiseItem.getUser().getObjectId())) {
                    //已赞
                    ret = i;
                    break;
                }
            }

        }
        return ret;
    }

    private int getFootPrintType(FootPrint footPrint) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle_detail_back_iv:
                finish();
                break;
            case R.id.circle_detail_prise_left_container:

                proPrise();
                List<PraiseItem> praises = mFootPrint.getPraises();
                if (praises != null) {
                    mPriseCountTv.setText(String.valueOf(praises.size()));
                }else{
                    mPriseCountTv.setText("0");
                }

                break;
            case R.id.circle_detail_comment_right:
            case R.id.circle_detail_prise_right:

                Bundle bundle = new Bundle();
                bundle.putSerializable(DicoveryConstantValue.DETAIL_COMMENT_BUNDLE_FOOTPRINT_KEY, mFootPrint);
                if (getType(mFootPrint) == 1) {
                    Intent intent = new Intent(this, DetailCommentImgActivity.class);
                    intent.putExtra(DicoveryConstantValue.DETAIL_COMMENT_BUNDLE_KEY, bundle);
                    startActivity(intent);
                }else if(getType(mFootPrint) == 2){
                    Intent intent = new Intent(this, DetailCommentVideoActivity.class);
                    intent.putExtra(DicoveryConstantValue.DETAIL_COMMENT_BUNDLE_KEY, bundle);
                    startActivity(intent);
                }

                break;
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
