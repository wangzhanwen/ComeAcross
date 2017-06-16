package com.lyy_wzw.comeacross.user.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.user.ui.LoginVideoView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginVideoView mLoginVieoView;
    private EditText mAccountView;
    private EditText mPswView;
    private TextView mLoginBtn;
    private TextView mRegisterBtn;
    private TextView mForgetPswBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initVideo();
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
        mAccountView = (EditText) findViewById(R.id.ed_login_account);
        mPswView = (EditText) findViewById(R.id.ed_login_psw);
        mLoginBtn = (TextView) findViewById(R.id.btn_user_login);
        mRegisterBtn = (TextView) findViewById(R.id.tv_login_register);
        mForgetPswBtn = (TextView) findViewById(R.id.tv_login_forget_psw);

        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mForgetPswBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_user_login:

                login();
                break;
            case R.id.tv_login_register:

                register();
                break;
            case R.id.tv_login_forget_psw:

                forgetPsw();
                break;
        }
    }

    private void forgetPsw() {
        Toast.makeText(this, "忘记密码了啊？ 活该. ^&^", Toast.LENGTH_SHORT).show();
    }

    private void register() {
        Toast.makeText(this, "还未实现，请等待。。。", Toast.LENGTH_SHORT).show();
    }

    private void login() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
