package com.lyy_wzw.comeacross.user.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.user.UserHelper;

import java.util.List;
import java.util.UUID;


import cn.bmob.v3.exception.BmobException;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RegisterFragment";

    private EditText mRegisterName;
    private EditText mRegisterPhone;
    private EditText mRegisterPsw;
    private EditText mRegisterSMSCode;
    private TextView mRegisterBtn;
    private TextView mBackLoginBtn;
    private TextView mGetSMSCodeBtn;

    private View mRootView;
    private ViewPager mViewPager;
    private RadioGroup mSexRG;
    private boolean mSex = true;



    private MyCountTimer timer;
    public RegisterFragment() {

    }

    public RegisterFragment(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_register, container, false);
        initView ();
        return mRootView;
    }

    private void initView (){
        mRegisterName    = (EditText) mRootView.findViewById(R.id.ed_register_name);
        mRegisterPhone   = (EditText) mRootView.findViewById(R.id.ed_register_phone);
        mRegisterPsw     = (EditText) mRootView.findViewById(R.id.ed_register_psw);
        mRegisterSMSCode = (EditText) mRootView.findViewById(R.id.ed_register_SMSCode);
        mRegisterBtn     = (TextView) mRootView.findViewById(R.id.btn_user_register);
        mBackLoginBtn    = (TextView) mRootView.findViewById(R.id.tv_register_back_login);
        mGetSMSCodeBtn   = (TextView) mRootView.findViewById(R.id.tv_register_getSMSCode);
        mSexRG = (RadioGroup) mRootView.findViewById(R.id.rg_register_sex);

        mRegisterBtn.setOnClickListener(this);
        mBackLoginBtn.setOnClickListener(this);
        mGetSMSCodeBtn.setOnClickListener(this);
        mSexRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (0 == checkedId){
                    mSex = true;
                }else{
                    mSex = false;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_user_register:
                final String phoneNumberRg = mRegisterPhone.getText().toString().trim();
                UserHelper.getInstance().queryUser("mobilePhoneNumber", phoneNumberRg, new UserHelper.UserQueryCallback() {
                    @Override
                    public void onResult(List<CAUser> object, BmobException e) {
                        if(object.size() != 0){
                            Log.d(TAG,"register(RegisterFragment.java:119)-->> object.size(): "+ object.size() );
                            Toast.makeText(RegisterFragment.this.getContext(), "该手机号已被注册!", Toast.LENGTH_SHORT).show();
                        }else{
                            register();
                        }
                    }
                });

                break;

            case R.id.tv_register_back_login:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(1);
                }

             break;
            case R.id.tv_register_getSMSCode:
                final String phoneNumber = mRegisterPhone.getText().toString().trim();
                UserHelper.getInstance().queryUser("mobilePhoneNumber", phoneNumber, new UserHelper.UserQueryCallback() {
                    @Override
                    public void onResult(List<CAUser> object, BmobException e) {
                        if(object.size() != 0){
                            Log.d(TAG,"register(RegisterFragment.java:119)-->> object.size(): "+ object.size() );
                            Toast.makeText(RegisterFragment.this.getContext(), "该手机号已被注册!", Toast.LENGTH_SHORT).show();
                        }else{
                            requestSmsCode(phoneNumber);
                        }
                    }
                });

                break;
        }

    }



    /**
     * 请求短信验证码
     * @method requestSmsCode
     * @return void
     * @exception
     */
    private void requestSmsCode(String phoneNumber){

        if(!TextUtils.isEmpty(phoneNumber)){
            UserHelper.getInstance().requestSmsCode(phoneNumber, new UserHelper.UserCallback() {
                @Override
                public void onSuccess() {
                    timer = new MyCountTimer(120000, 1000);
                    timer.start();

                    //验证码发送成功
                    Toast.makeText(RegisterFragment.this.getContext(), "验证码发送成功!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(BmobException ex) {
                    Toast.makeText(RegisterFragment.this.getContext(), "验证码发送失败.code:"+ ex.getErrorCode() + "  msg:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(RegisterFragment.this.getContext(), "请输入手机号码.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 用户注册
     * @method register
     * @return void
     * @exception
     */
    private void register() {
        String name        = mRegisterName.getText().toString().trim();
        final String psw         = mRegisterPsw.getText().toString().trim();
        final  String phoneNumber = mRegisterPhone.getText().toString().trim();
        String smsCode     = mRegisterSMSCode.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterFragment.this.getContext(), "用户名不能为空.", Toast.LENGTH_SHORT).show();
            return;
          }

        if (TextUtils.isEmpty(psw)) {

            Toast.makeText(RegisterFragment.this.getContext(), "密码不能为空.", Toast.LENGTH_SHORT).show();
            return;
         }

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(RegisterFragment.this.getContext(), "手机号不能为空.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(smsCode)) {

            Toast.makeText(RegisterFragment.this.getContext(), "验证码不能为空.", Toast.LENGTH_SHORT).show();
            return;
        }

        final CAUser user =  new CAUser();
        user.setRyUid(UUID.randomUUID().toString());
        user.setUsername(name);
        user.setMobilePhoneNumber(phoneNumber);
        user.setPassword(psw);
        user.setSex(mSex);
        user.setUserPhoto("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-24-18013547_1532023163498554_215541963087151104_n.jpg");
        final ProgressDialog progress = new ProgressDialog(RegisterFragment.this.getContext());
        progress.setMessage("正在注册中...");
        progress.show();

        UserHelper.getInstance().register(user, smsCode, new UserHelper.UserCallback() {
            @Override
            public void onSuccess() {
                progress.cancel();
                timer.cancel();
                mGetSMSCodeBtn.setText("获得验证码");
                mGetSMSCodeBtn.setTextColor(Color.WHITE);
                mGetSMSCodeBtn.setEnabled(true);
                Toast.makeText(RegisterFragment.this.getContext(), "注册成功.", Toast.LENGTH_SHORT).show();
                //注册成功，进行登录，进入应用
                UserHelper.getInstance().login(phoneNumber, psw, new UserHelper.UserCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(RegisterFragment.this.getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(BmobException e) {
                        Toast.makeText(RegisterFragment.this.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onError(BmobException e) {
                progress.cancel();
                timer.cancel();
                mGetSMSCodeBtn.setText("获得验证码");
                mGetSMSCodeBtn.setTextColor(Color.WHITE);
                mGetSMSCodeBtn.setEnabled(true);
                Toast.makeText(RegisterFragment.this.getContext(), "注册失败：", Toast.LENGTH_SHORT).show();

            }
        });


    }

    class MyCountTimer extends CountDownTimer {

        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {

            mGetSMSCodeBtn.setText((millisUntilFinished / 1000) +"秒后重发");
            mGetSMSCodeBtn.setTextColor(Color.WHITE);
            mGetSMSCodeBtn.setEnabled(false);
        }
        @Override
        public void onFinish() {
            mGetSMSCodeBtn.setText("获得验证码");
            mGetSMSCodeBtn.setTextColor(Color.WHITE);
            mGetSMSCodeBtn.setEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }


}
