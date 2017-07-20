package com.lyy_wzw.comeacross.user.activitys;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.homecommon.FragmentAdapter;
import com.lyy_wzw.comeacross.user.fragments.ChangePswFragment;
import com.lyy_wzw.comeacross.user.fragments.LoginFragment;
import com.lyy_wzw.comeacross.user.fragments.RegisterFragment;
import com.lyy_wzw.comeacross.user.ui.LoginVideoView;
import com.lyy_wzw.comeacross.utils.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements PermissionHelper.PermissionCallback{

    private LoginVideoView mLoginVieoView;
    private ViewPager mLoginViewPager;

    private static final int RC_LOCATION_PERM = 227;
    private String[] mPermissions;
    private boolean isPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        PermissionHelper.with(this)
                .addRequestCode(RC_LOCATION_PERM)
                .permissions(mPermissions)
                //.nagativeButtonText(android.R.string.ok)
                //.positveButtonText(android.R.string.cancel)
                .rationale("未授与权限，会影响应用运行！")
                .request();
    }

    private void initVideo() {
        mLoginVieoView = (LoginVideoView) findViewById(R.id.user_login_videoview);
        mLoginVieoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login_bg_video));
        mLoginVieoView.start();
        mLoginVieoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mLoginVieoView.start();
            }
        });
    }

    private void initView (){
        initVideo();
        //登录和注册分别在两个Fragment，以ViewPager为载体切换
        mLoginViewPager = (ViewPager) findViewById(R.id.login_viewpager);
        List<Fragment> fragments=new ArrayList<Fragment>();
        fragments.add(new ChangePswFragment(mLoginViewPager));
        fragments.add(new LoginFragment(mLoginViewPager));
        fragments.add(new RegisterFragment(mLoginViewPager));
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        mLoginViewPager.setAdapter(adapter);
        mLoginViewPager.setCurrentItem(1);

        //禁止ViewPager滑动
        mLoginViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //若是MainActivity点击两次退出应用
        if ("ExitApp".equals(intent.getAction())) {
            finish();
        }
    }

    @Override
    protected void onRestart() {
        initVideo();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        mLoginVieoView.stopPlayback();
        super.onStop();
    }


    @Override
    public void onEasyPermissionGranted(int requestCode, String... perms) {
        Toast.makeText(this, "已授权", Toast.LENGTH_SHORT).show();
        isPermission = true;
    }

    @Override
    public void onEasyPermissionDenied(int requestCode, String... perms) {
        Toast.makeText(this, "拒绝授权", Toast.LENGTH_SHORT).show();
        PermissionHelper.with(this)
                .addRequestCode(RC_LOCATION_PERM)
                .permissions(mPermissions)
                //.nagativeButtonText(android.R.string.ok)
                //.positveButtonText(android.R.string.cancel)
                .rationale("未授权位置权限，应用无法进行")
                .request();

    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
            从Settings界面跳转回来，标准代码，就这么写
        */
        if (requestCode == PermissionHelper.SETTINGS_REQ_CODE) {
            if (PermissionHelper.hasPermissions(this, mPermissions)) {
                //已授权，处理业务逻辑
                onEasyPermissionGranted(RC_LOCATION_PERM, mPermissions);
            } else {
                onEasyPermissionDenied(RC_LOCATION_PERM, mPermissions);
            }
        }


    }
}
