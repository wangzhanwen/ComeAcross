package com.lyy_wzw.comeacross.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.addressbook.Friend;
import com.lyy_wzw.comeacross.homecommon.InitApplication;

import java.util.ArrayList;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserOnlineStatusInfo;

public class UserdetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvNickName;
    private TextView mTvDispalyName;
    private TextView mTvphone;
    private TextView mTvOnlineStatus;
    private ImageView mIvactar;
    private LinearLayout mLLChat;
    private Button mBtAddFriend;
    private LinearLayout mNoteNameLinearLayout;

    private Friend mFriend;
    private Toolbar mToolbar;
    private Button btn_startchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetail);
        initView();
        initData();


    }

    private void initData() {
        //本质上把Parcel当成一个Serialize,不同在于他是在内存中完成序列化和反序列化,
        // 利用的是连续的空间,更加高效.在activity之间通过intent传值,对象要继承Parcelable
        mFriend = getIntent().getParcelableExtra("friend");
        if (mFriend != null) {
            if (mFriend .isExitsDisplayName()) {
                mTvNickName.setVisibility(ImageView.VISIBLE);
                mTvNickName.setText("昵称:"+mFriend.getName());
                mTvDispalyName.setText(mFriend.getDisplayName());
            }else{
                mTvDispalyName.setVisibility(View.VISIBLE);
                mTvDispalyName.setText(mFriend.getName());
            }
            mTvphone.setVisibility(View.VISIBLE);
            mTvphone.setText("手机号:"+mFriend.getPhoneNumber());
            String protrait = "";
            ImageLoader.getInstance().displayImage(protrait,mIvactar, InitApplication.getOptions());
            RongIMClient.getInstance().getUserOnlineStatus(mFriend.getUserId(), new IRongCallback.IGetUserOnlineStatusCallback() {
                @Override
                public void onSuccess(ArrayList<UserOnlineStatusInfo> list) {
                    if (list != null) {
                        if (list.size() > 1) {
                            Message msg = Message.obtain();
                            msg.arg1 = 0;
                            mHandler.sendMessage(msg);
                        }else if (list.size() == 1){
                            Message msg = Message.obtain();
                            msg.arg1 = list.get(0).getPlatform().getValue();
                            mHandler.sendMessage(msg);
                        }
                    }else{
                        Message msg = mHandler.obtainMessage();
                        msg.arg1 = 5;
                        mHandler.sendMessage(msg);
                    }
                }

                @Override
                public void onError(int i) {

                }
            });
        }


    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    //多个设备在线
                case 3:
                case 4:
                    mTvOnlineStatus.setText("电脑在线");
                    mTvOnlineStatus.setTextColor(Color.parseColor("#60E23F"));
                    break;//pc
                case 1:
                case 2:
                    mTvOnlineStatus.setText("手机在线");
                    mTvOnlineStatus.setTextColor(Color.parseColor("#60E23F"));
                    break;//phone
                case 5:
                    mTvOnlineStatus.setText("离线");
                    mTvOnlineStatus.setTextColor(Color.parseColor("#666666"));
                    break;//offline
            }

        }
    };

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
       // mToolbar.setTitle("用户详情");

        mTvNickName = (TextView) findViewById(R.id.contact_below);
        mTvDispalyName = (TextView) findViewById(R.id.contact_top);
        mTvphone = (TextView) findViewById(R.id.contact_phone);
        mTvOnlineStatus = (TextView) findViewById(R.id.user_online_status);
        mIvactar = (ImageView) findViewById(R.id.ac_iv_user_portrait);
        mLLChat = (LinearLayout) findViewById(R.id.ac_ll_chat_button_group);
        mBtAddFriend = (Button) findViewById(R.id.ac_bt_add_friend);
        mNoteNameLinearLayout = (LinearLayout) findViewById(R.id.ac_ll_note_name);
        btn_startchat = (Button) findViewById(R.id.btn_startChat);
        btn_startchat.setOnClickListener(this);
    }





    @Override
    protected void onDestroy() {
        //将所有的message和callback全部清除
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_startChat:
                RongIM.getInstance().startPrivateChat(UserdetailActivity.this,"27adb4f736","标题");
                break;
            default:
                break;
        }

    }
}
