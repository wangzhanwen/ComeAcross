package com.lyy_wzw.comeacross.user.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.user.UserHelper;


import cn.bmob.v3.exception.BmobException;


public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText mPhoneNumberView;
    private EditText mPswView;
    private TextView mLoginBtn;
    private TextView mRegisterBtn;
    private TextView mForgetPswBtn;
    private View mRootView;
    private ViewPager mViewPager;

    public LoginFragment() {

    }


    public LoginFragment(ViewPager viewPager) {
        mViewPager = viewPager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        initView ();
        //获得本地缓存的当前用，不为空进入应用
        CAUser userInfo = UserHelper.getInstance().getCurrentUser();
        if (userInfo != null) {
            //Intent intent = new Intent(getContext(), MainActivity.class);
            //startActivity(intent);
            mPhoneNumberView.setText(userInfo.getMobilePhoneNumber());
        }

        return  mRootView;
    }

    private void initView (){
        mPhoneNumberView = (EditText) mRootView.findViewById(R.id.ed_login_account);
        mPswView = (EditText) mRootView.findViewById(R.id.ed_login_psw);
        mLoginBtn = (TextView) mRootView.findViewById(R.id.btn_user_login);
        mRegisterBtn = (TextView) mRootView.findViewById(R.id.tv_login_register);
        mForgetPswBtn = (TextView) mRootView.findViewById(R.id.tv_login_forget_psw);

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
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(2);
                }
                break;
            case R.id.tv_login_forget_psw:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(0);
                }
                break;
        }

    }

    private void forgetPsw() {
        Toast.makeText(getContext(), "忘记密码了啊？ 活该. ^&^", Toast.LENGTH_SHORT).show();
    }


    /**
     * 用户登录(使用手机号+密码)
     * @method register
     * @return void
     * @exception
     */
    private void login() {
        String phoneNumber = mPhoneNumberView.getText().toString().trim();
        String psw = mPswView.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(LoginFragment.this.getContext(), "手机号不能为空.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(LoginFragment.this.getContext(), "密码不能为空.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserHelper.getInstance().login(phoneNumber, psw, new UserHelper.UserCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginFragment.this.getContext(), "登录成功.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(BmobException e) {
                Toast.makeText(LoginFragment.this.getContext(), "登录失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
