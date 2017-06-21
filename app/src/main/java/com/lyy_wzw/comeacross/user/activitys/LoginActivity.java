package com.lyy_wzw.comeacross.user.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.homecommon.FragmentAdapter;
import com.lyy_wzw.comeacross.user.fragments.ChangePswFragment;
import com.lyy_wzw.comeacross.user.fragments.LoginFragment;
import com.lyy_wzw.comeacross.user.fragments.RegisterFragment;
import com.lyy_wzw.comeacross.user.ui.LoginVideoView;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity{

    private LoginVideoView mLoginVieoView;
    private ViewPager mLoginViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
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

}
