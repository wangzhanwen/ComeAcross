package com.lyy_wzw.comeacross.footprint.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.bean.FootPrintAddress;
import com.lyy_wzw.comeacross.bean.FootPrintFile;
import com.lyy_wzw.comeacross.footprint.adapter.FPShareWinGridViewAdapter;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.utils.AddressAsyncTask;
import com.lyy_wzw.comeacross.user.FileDataBmobHelper;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.FileUtil;
import com.lyy_wzw.comeacross.utils.ScreenUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class ShareFootPrintActivity extends AppCompatActivity implements View.OnClickListener, BDLocationListener {
    private static String TAG = "ShareFootPrintActivity";

    private GridView mImagesGridView;
    private ArrayList<FootPrintFile> mFootPrintFiles;
    private FPShareWinGridViewAdapter mAdapter;
    private FrameLayout btnBack;
    private FrameLayout btnSend;
    private EditText mContentView;
    private int upLoadedCount = 0;
    private int mChoicedRight = 0;
    private int mChoicedLabel = 0;
    private boolean isShowLocation = true;

    private ArrayList<FootPrintFile> upLoadFootPrintFiles = new ArrayList<>();

    private ProgressDialog progressView;
    private RelativeLayout mRightView;
    private RelativeLayout mLabelView;
    private TextView mRightTxt;
    private TextView mLabelTxt;
    private Switch mSwitchView;

    private LocationClient mLocationClient = null;
    private Double mLatitude;
    private Double mLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_foot_print);
        initLocationClient();
        //开始定位
        mLocationClient.start();

        initView();

        mFootPrintFiles = new ArrayList<FootPrintFile>();
        //获取选择的文件的路径
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY);
        if(bundle != null){
            addFootPrintFiles(bundle,1);
        }

        mAdapter  = new FPShareWinGridViewAdapter(this, R.layout.footprint_sharewin_gridview_item, mFootPrintFiles);
        mImagesGridView.setAdapter(mAdapter);
    }

    private void initView() {
        mImagesGridView = (GridView) findViewById(R.id.share_footprint_grid_view);
        btnBack = (FrameLayout) findViewById(R.id.share_footprint_btn_back);
        btnSend = (FrameLayout) findViewById(R.id.share_self_btn_send);
        mContentView = (EditText) findViewById(R.id.share_footprint_edit_text);

        mRightView = (RelativeLayout) findViewById(R.id.btn_share_footprint_right);
        mLabelView = (RelativeLayout) findViewById(R.id.btn_share_footprint_label);
        mRightTxt  = (TextView) findViewById(R.id.txt_share_footprint_right);
        mLabelTxt  = (TextView) findViewById(R.id.txt_share_footprint_label);
        mSwitchView = (Switch) findViewById(R.id.share_footprint_switch_view);

        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        mRightView.setOnClickListener(this);
        mLabelView.setOnClickListener(this);
        mSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowLocation = isChecked;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_footprint_btn_back:
                showExitDialog();
                break;
            case R.id.share_self_btn_send:
                //TODO: 上传服务器分享数据
                shareFootPrint();
                break;
            case R.id.btn_share_footprint_right:
                //TODO: 设置说说查看权限
                showRightChoiceDialog();
                break;
            case R.id.btn_share_footprint_label:
                //TODO: 设置说说标签
                showLabelChoiceDialog();
                break;
        }

    }

    private void addFootPrintFiles(Bundle bundle, int type) {
        int fileType = bundle.getInt(FootPrintConstantValue.SHARE_FOOTPRINT_FILE_TYPE_KEY, 1);
        if (fileType == 1) {
            List<String> imageUrls    = bundle.getStringArrayList(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_KEY);
            if (imageUrls != null && imageUrls.size() > 0) {
                for (int i = 0; i < imageUrls.size(); i++) {
                    if (mFootPrintFiles.size() < FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT + 1) {
                        FootPrintFile imageFile = new FootPrintFile();
                        imageFile.setType(1);
                        imageFile.setFilePath(imageUrls.get(i));
                        int position = mFootPrintFiles.size();
                        if (1 == type){
                            mFootPrintFiles.add(imageFile);
                        }else if(type == 2){
                            mFootPrintFiles.add(position-1,imageFile);
                        }


                    } else {
                       Snackbar.make(mImagesGridView,
                               "最多只能选择" + FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT + "张图片.",
                               Snackbar.LENGTH_SHORT).show();
                        break;
                    }

                }
            }

        }else if(fileType == 2){
            if (mFootPrintFiles.size()>1) {
                Toast.makeText(this,
                       "视频不能与图片混合发送.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            FootPrintFile videoFile = new FootPrintFile();
            String videoPath = bundle.getString(FootPrintConstantValue.SHARE_FOOTPRINT_VIDEO_URLS_KEY);
            Bitmap videoThumbnail = FileUtil.getVideoThumbnail(videoPath, ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            if (videoThumbnail == null) {
                Snackbar.make(mImagesGridView,
                        "视频缩略图获取失败.",
                        Snackbar.LENGTH_SHORT).show();
                return;
            }

            String videoThumbnailName = "thum_"+ System.currentTimeMillis() + ".jpg";
            String videoThumbnailPath = FootPrintConstantValue.FILE_SAVE_PATH
                                        + File.separator
                                        + "VideoThumbnail"
                                        + File.separator + videoThumbnailName;
            Log.d(TAG,"videoThumbnailPath：" + videoThumbnailPath );
            if (!FileUtil.bitmap2File(videoThumbnail,videoThumbnailPath)) {
                Snackbar.make(mImagesGridView,
                        "视频缩略图保存失败.",
                        Snackbar.LENGTH_SHORT).show();
                return;
            }

            videoFile.setFilePath(videoPath);
            videoFile.setType(2);
            videoFile.setThumbnailPath(videoThumbnailPath);
            Log.d(TAG, "videopath:" + videoFile.getFilePath());
            int position = mFootPrintFiles.size();
            if (1 == type){
                mFootPrintFiles.add(videoFile);
            }else if(type == 2){
                mFootPrintFiles.add(position-1,videoFile);
            }
            Log.d(TAG, "mFootPrintFiles:" + mFootPrintFiles.toString());
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle = intent.getBundleExtra(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY);
        if (bundle != null) {
            addFootPrintFiles(bundle,2);
        }

        mAdapter.notifyDataSetChanged();

    }

    public static Handler mShareHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FootPrintConstantValue.SHARE_IMAGEURLS_HANDLE_KEY:

                    break;
            }
        }
    };


    private void shareFootPrint() {
        progressView = new ProgressDialog(ShareFootPrintActivity.this);
        progressView.setCanceledOnTouchOutside(false);
        progressView.setMessage("发送中...");

        String content = mContentView.getText().toString().trim();
        String uid = UserHelper.getInstance().getCurrentUser().getObjectId();
        final FootPrint footPrint = new FootPrint();
        footPrint.setContent(content);
        footPrint.setUserId(uid);
        footPrint.setLabel(mChoicedLabel);
        footPrint.setVisitRight(mChoicedRight);
        footPrint.setShowLocation(isShowLocation);
        footPrint.setLatitude(mLatitude);
        footPrint.setLongitude(mLongitude);

        Log.d(TAG,"shareFootPrint()-->> " +  UserHelper.getInstance().getCurrentUser().toString());

        //根据经纬获得地址
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        new AddressAsyncTask(new AddressAsyncTask.AsyncTaskCallback() {
            @Override
            public void onSuccess(FootPrintAddress footPrintAddress) {

                footPrint.setFootPrintAddress(footPrintAddress);
                //上传说说图片，视频文件
                if (mFootPrintFiles != null && mFootPrintFiles.size() > 1){
                    progressView.show();
                    btnSend.setEnabled(false);

                    //mFootPrintFiles最后一张图片是添加按钮背景图
                    for (int i = 0; i < mFootPrintFiles.size()-1 ; i++) {
                        Log.d(TAG,"file" + i +":" + mFootPrintFiles.get(i) );
                        final FootPrintFile footPrintFile = mFootPrintFiles.get(i);

                        if (footPrintFile.getType() == 1) {
                            upLoadFile(footPrintFile.getFilePath(), footPrint, new UpLoadFileCallback() {
                                @Override
                                public void onSuccess(String url) {
                                    if (url != null) {
                                        footPrintFile.setFilePath(url);
                                        footPrintFile.setType(1);
                                    }
                                }
                            });


                        }else if(footPrintFile.getType() == 2){
                            footPrintFile.setType(2);
                            upLoadFile(footPrintFile.getFilePath(), footPrint, new UpLoadFileCallback() {
                                @Override
                                public void onSuccess(String url) {
                                    if (url != null) {
                                        footPrintFile.setFilePath(url);
                                    }
                                }
                            });
                            upLoadFile(footPrintFile.getThumbnailPath(), footPrint, new UpLoadFileCallback() {
                                @Override
                                public void onSuccess(String url) {
                                    if (url != null) {
                                        footPrintFile.setThumbnailPath(url);
                                    }
                                }
                            });

                        }

                        upLoadFootPrintFiles.add(footPrintFile);
                    }


                }else{
                    progressView.cancel();
                    btnSend.setEnabled(true);
                    Toast.makeText(ShareFootPrintActivity.this, "你还未选择图片.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(String msg) {
                progressView.cancel();
                btnSend.setEnabled(true);
                Toast.makeText(ShareFootPrintActivity.this, "地址请求出错："+msg, Toast.LENGTH_SHORT).show();
            }
        }).execute(latLng);


    }

    private int getUpLoadFileCount() {
        int upLoadFileCount = 0;
        //mFootPrintFiles最后一张图片是添加按钮背景图
        for (int i = 0; i < mFootPrintFiles.size() - 1; i++) {
            FootPrintFile footPrintFile = mFootPrintFiles.get(i);
            if (footPrintFile.getType() == 1){
                upLoadFileCount ++;
            }else if(footPrintFile.getType() == 2){
                upLoadFileCount  = upLoadFileCount + 2;
            }
        }
        return upLoadFileCount;
    }


    private void upLoadFile(String filePath, final FootPrint footPrint, final UpLoadFileCallback upLoadFileCallback){

        FileDataBmobHelper.getInstance().upLoadFile(filePath, new FileDataBmobHelper.UserUpLoadFileCallback() {
            @Override
            public void onSuccess(BmobFile bmobFile) {
                Log.d(TAG, bmobFile.getFileUrl());
                upLoadFileCallback.onSuccess(bmobFile.getFileUrl());
                upLoadedCount++;

                if (upLoadedCount == getUpLoadFileCount()) {
                    Log.d(TAG, "图片上传完成");
                    footPrint.setFootPrintFiles(upLoadFootPrintFiles);
                    //图片上传完成，设置用户，提交说说数据

                    upLoadFootPrint(footPrint);
                }

            }

            @Override
            public void onError(BmobException e) {
                Toast.makeText(ShareFootPrintActivity.this, "文件上传失败:"+ e.getMessage(),  Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(Integer value) {

            }
        });

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        mLatitude = bdLocation.getLatitude();
        mLongitude = bdLocation.getLongitude();
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    private interface UpLoadFileCallback{
        void onSuccess(String url);
    }


    private void upLoadFootPrint(final FootPrint footPrint) {
        //图片上传成功后，上传说说数据
        footPrint.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (progressView != null) {
                    progressView.cancel();
                }
                btnSend.setEnabled(true);
                if(e==null){
                    Log.d(TAG,"footPrint:"+footPrint.toString() );
                    Toast.makeText(ShareFootPrintActivity.this, "发送成功.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Log.d(TAG,"dataUp:" + e.getMessage() );
                    Toast.makeText(ShareFootPrintActivity.this, "发送失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("退出此次编辑？");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    private void showRightChoiceDialog(){
       final String[]  items = {"所有人", "仅朋友", "仅自己"};
        mChoicedRight = 0;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("谁可以看");
        dialogBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mChoicedRight = which;
            }
        });

        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(ShareFootPrintActivity.this, "点击标签：" + which, Toast.LENGTH_SHORT).show();

                switch (mChoicedRight){
                    case 0:
                        mRightTxt.setText(items[0]);
                        break;
                    case 1:
                        mRightTxt.setText(items[1]);
                        break;
                    case 2:
                        mRightTxt.setText(items[2]);
                        break;
                }
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setTextColor(Color.parseColor("#25b249"));
                button.setTextSize(18);
            }
        });
        alertDialog.show();

    }


    private void showLabelChoiceDialog(){
        mChoicedLabel = 0;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("标签");
        dialogBuilder.setSingleChoiceItems(FootPrintConstantValue.FOOTPRINT_MARK_LABEL, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mChoicedLabel = which;
            }
        });

        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ShareFootPrintActivity.this, "点击标签：" + which, Toast.LENGTH_SHORT).show();
                switch (mChoicedLabel){
                    case 0:
                        mLabelTxt.setText(FootPrintConstantValue.FOOTPRINT_MARK_LABEL[0]);
                        break;
                    case 1:
                        mLabelTxt.setText(FootPrintConstantValue.FOOTPRINT_MARK_LABEL[1]);
                        break;
                    case 2:
                        mLabelTxt.setText(FootPrintConstantValue.FOOTPRINT_MARK_LABEL[2]);
                        break;
                }
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setTextColor(Color.parseColor("#25b249"));
                button.setTextSize(18);
            }
        });

        alertDialog.show();

    }


    private void initLocationClient() {
        // 初始化定位
        mLocationClient = new LocationClient(this);
        // 设置定位参数
        LocationClientOption option = new LocationClientOption();

        // 1. 设置定位的精度
        // 高精度
        // 省电模式
        // GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 2. 设置定位的时间间隔, 单位 ms, < 1000 代表只定位一次
        option.setScanSpan(3000);
        // 3. 定位的结果的类型, 针对百度地图,或者高德、或者其他
        option.setCoorType("bd09ll"); // bd09ll 代表百度地图, gcj02
        // 4. 打开GPS
        option.setOpenGps(true);
        // 5. 是否接收地址信息
        option.setIsNeedAddress(false);
        // 6. 设置
        mLocationClient.setLocOption(option);

        // 7. 设置定位结果接口回调
        mLocationClient.registerLocationListener(this);

    }

    @Override
    public void onBackPressed() {
       showExitDialog();
    }
}
