package com.lyy_wzw.comeacross.footprint.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.platform.comapi.map.B;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.utils.FileUtil;
import com.wzw.camerarecord.listener.CameraResultListener;
import com.wzw.camerarecord.listener.WCameraLisenter;
import com.wzw.camerarecord.util.WCameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.common.FileUtils;

public class CameraActivity extends AppCompatActivity {
    private  static final String TAG =  "CameraActivity";

    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    private WCameraView mCameraView;
    private boolean granted = false;
    private String photoFileName;
    private String savePhotoPath="";
    private String photoFileAbsPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wzw.camerarecord.R.layout.activity_camera);

        mCameraView = (WCameraView) findViewById(com.wzw.camerarecord.R.id.cameraview);

        //设置视频保存路径
        mCameraView.setSaveVideoPath(FootPrintConstantValue.FILE_SAVE_PATH + File.separator+"CameraRecord");
        setSavePhotoPath(FootPrintConstantValue.FILE_SAVE_PATH + File.separator + "CameraCapture");


        //WCameraView监听
        mCameraView.setJCameraLisenter(new WCameraLisenter() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap

                photoFileName = "ComeAcross_pic_" + System.currentTimeMillis() + ".jpg";
                if (savePhotoPath.equals("")) {
                    savePhotoPath = Environment.getExternalStorageDirectory().getPath();
                }
                photoFileAbsPath = savePhotoPath + File.separator + photoFileName;
                Log.d(TAG, "photoFileAbsPath:"+photoFileAbsPath);

                //boolean isSuccess =  saveBitmap(bitmap, photoFileAbsPath);
                boolean isSuccess = FileUtil.bitmap2File(bitmap, photoFileAbsPath);
                if (isSuccess) {
                    bitmap.recycle();
                    bitmap = null;
                    ArrayList<String>  picPathList = new ArrayList<String>();
                    picPathList.add(photoFileAbsPath);

                    //打开发朋友圈窗口，将拍摄的照片路径传过去
                    Intent intentShare = new Intent(CameraActivity.this, ShareFootPrintActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(FootPrintConstantValue.SHARE_FOOTPRINT_FILE_TYPE_KEY, 1);
                    bundle.putStringArrayList(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_KEY, picPathList);
                    intentShare.putExtra(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY, bundle);

                    CameraActivity.this.startActivity(intentShare);
                }else{
                    Toast.makeText(CameraActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
                }
                CameraActivity.this.finish();

            }

            @Override
            public void recordSuccess(String url) {
                //获取视频路径
                Log.d(TAG, "url = " + url);

                //打开发朋友圈窗口，将拍摄的短视屏路径传过去
                Intent intentShare = new Intent(CameraActivity.this, ShareFootPrintActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(FootPrintConstantValue.SHARE_FOOTPRINT_FILE_TYPE_KEY, 2);
                bundle.putString(FootPrintConstantValue.SHARE_FOOTPRINT_VIDEO_URLS_KEY, url);
                intentShare.putExtra(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY, bundle);
                CameraActivity.this.startActivity(intentShare);

                CameraActivity.this.finish();
            }

            @Override
            public void quit() {
                //退出按钮
                CameraActivity.this.finish();
            }
        });
        //6.0动态权限获取
        getPermissions();
    }

    public void setSavePhotoPath(String savePhotoPath) {
        this.savePhotoPath = savePhotoPath;
        File file = new File(savePhotoPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private  boolean saveBitmap(Bitmap bitmap, String savePath) {
        File filePic;

        try {
            filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (granted) {
            mCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.onPause();
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
                granted = true;
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
                granted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    granted = true;
                    mCameraView.onResume();
                }else{
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}
