package com.lyy_wzw.comeacross.user.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.user.UserHelper;

import cn.bmob.v3.exception.BmobException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePswFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private EditText mPhoneNumberView;
    private EditText mNewPswView;
    private EditText mSMSCode;
    private TextView mChangePswBtn;
    private TextView mBackLoginBtn;
    private TextView mGetSMSCodeBtn;
    private ViewPager mViewPager;

    private ChangePswFragment.MyCountTimer timer;

    public ChangePswFragment() {

    }
    public ChangePswFragment(ViewPager viewPager) {
        mViewPager = viewPager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_change_psw, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        mPhoneNumberView = (EditText) mRootView.findViewById(R.id.ed_changepsw_phonenumber);
        mNewPswView      = (EditText) mRootView.findViewById(R.id.ed_changepsw_newpsw);
        mSMSCode         = (EditText) mRootView.findViewById(R.id.ed_changepsw_SMSCode);

        mChangePswBtn    = (TextView) mRootView.findViewById(R.id.btn_changepsw_change);
        mBackLoginBtn    = (TextView) mRootView.findViewById(R.id.tv_changepsw_bacaklogin);
        mGetSMSCodeBtn   = (TextView) mRootView.findViewById(R.id.tv_changepsw_getSMSCode);

        mChangePswBtn.setOnClickListener(this);
        mBackLoginBtn.setOnClickListener(this);
        mGetSMSCodeBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_changepsw_bacaklogin:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(1);
                }
                break;
            case R.id.tv_changepsw_getSMSCode:
                final String phoneNumber = mPhoneNumberView.getText().toString().trim();
                requestSmsCode(phoneNumber);
                break;
            case R.id.btn_changepsw_change:
                changePsw();
                break;
        }
    }

    /**
     * 修改密码
     *
     */
    private void changePsw() {
        String phoneNumber    = mPhoneNumberView.getText().toString().trim();
        final String newPsw   = mNewPswView.getText().toString().trim();
        String smsCode        = mSMSCode.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(ChangePswFragment.this.getContext(), "手机号不能为空.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newPsw)) {

            Toast.makeText(ChangePswFragment.this.getContext(), "密码不能为空.", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(smsCode)) {

            Toast.makeText(ChangePswFragment.this.getContext(),
                    "验证码不能为空.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        UserHelper.getInstance().changePsw(smsCode, newPsw, new UserHelper.UserCallback() {
            @Override
            public void onSuccess(CAUser user) {
                timer.cancel();

                Toast.makeText(ChangePswFragment.this.getContext(),
                        "修改成功.",
                        Toast.LENGTH_SHORT).show();
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(1);
                }
            }

            @Override
            public void onError(BmobException e) {
                timer.cancel();

                Toast.makeText(ChangePswFragment.this.getContext(),
                        "修改失败：" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

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
                public void onSuccess(CAUser user) {
                    mGetSMSCodeBtn.setEnabled(false);
                    timer = new ChangePswFragment.MyCountTimer(120000, 1000);
                    timer.start();

                    //验证码发送成功
                    Toast.makeText(ChangePswFragment.this.getContext(),
                            "验证码发送成功!",
                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(BmobException ex) {
                    Toast.makeText(ChangePswFragment.this.getContext(),
                            "验证码发送失败.code:"+ ex.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(ChangePswFragment.this.getContext(),
                    "请输入手机号码.",
                    Toast.LENGTH_SHORT).show();
        }
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
