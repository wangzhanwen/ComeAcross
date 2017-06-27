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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.user.FileDataBmobHelper;
import com.lyy_wzw.comeacross.user.UserConstantValue;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.user.rongyun.methods.User;
import com.lyy_wzw.comeacross.user.rongyun.models.TokenResult;
import com.lyy_wzw.comeacross.user.task.RequestTokenAsyncTask;
import com.lyy_wzw.comeacross.utils.PixelUtil;
import com.lyy_wzw.imageselector.SelectResultListener;
import com.lyy_wzw.imageselector.utils.ImageSelectorUtils;

import java.util.List;
import java.util.UUID;


import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


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
    private ImageView mUserPhotoView;

    private View mRootView;
    private ViewPager mViewPager;
    private RadioGroup mSexRG;
    private boolean mSex = true;



    private MyCountTimer timer;
    private String mUserPhotoPath = null;


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
        mUserPhotoView = (ImageView) mRootView.findViewById(R.id.img_register_user_photo);
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
        mUserPhotoView.setOnClickListener(this);
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
            //注册
            case R.id.btn_user_register:
                final String phoneNumberRg = mRegisterPhone.getText().toString().trim();
                //注册前判断该手机号是否被注册
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
            //返回登录界面
            case R.id.tv_register_back_login:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(1);
                }

             break;
            //获得验证码
            case R.id.tv_register_getSMSCode:
                final String phoneNumber = mRegisterPhone.getText().toString().trim();
                //请求发送验证码前，判断该手机号是否被注册
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
            //选择头像
            case R.id.img_register_user_photo:
                Toast.makeText(RegisterFragment.this.getContext(), "选择头像!", Toast.LENGTH_SHORT).show();

                proUserPhotoSelect();
                break;
        }

    }

    private void proUserPhotoSelect() {
        ImageSelectorUtils.openPhoto(RegisterFragment.this.getContext(), new SelectResultListener() {
            @Override
            public void onSelectResult(List<String> result) {
                Log.d(TAG, "onSelectResult()-->>"+ result.toString());
                if (result!=null && result.size()>0){
                    mUserPhotoPath = result.get(0);
                    if (!mUserPhotoPath.endsWith("gif")) {
                        Glide.with(RegisterFragment.this.getContext())
                                .load(mUserPhotoPath)
                                .override(PixelUtil.dip2px(RegisterFragment.this.getContext(), 100), PixelUtil.dip2px(RegisterFragment.this.getContext(), 100)) // 重新改变图片大小成这些尺寸(像素)比
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(mUserPhotoView);
                    }else{
                        Toast.makeText(RegisterFragment.this.getContext(), "头像不能使用gif图片.", Toast.LENGTH_SHORT).show();
                        mUserPhotoPath = null;
                    }
                }

            }
        }, true, 1);
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
                    timer = new MyCountTimer(120000, 1000);
                    timer.start();

                    //验证码发送成功
                    Toast.makeText(RegisterFragment.this.getContext(), "验证码发送成功!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(BmobException ex) {
                    Toast.makeText(RegisterFragment.this.getContext(), "验证码发送失败." + ex.getMessage(), Toast.LENGTH_SHORT).show();
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
        final String name        = mRegisterName.getText().toString().trim();
        final String psw         = mRegisterPsw.getText().toString().trim();
        final String phoneNumber = mRegisterPhone.getText().toString().trim();
        final String smsCode     = mRegisterSMSCode.getText().toString().trim();
        final CAUser user =  new CAUser();

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

        if (TextUtils.isEmpty(mUserPhotoPath)){
            Toast.makeText(RegisterFragment.this.getContext(), "头像还未选择", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progress = new ProgressDialog(RegisterFragment.this.getContext());
        progress.setMessage("正在注册中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        Log.d(TAG,"register()-->> mUserPhotoPath:"+ mUserPhotoPath);

        FileDataBmobHelper.getInstance().upLoadFile(mUserPhotoPath, new FileDataBmobHelper.UserUpLoadFileCallback() {
            @Override
            public void onSuccess(BmobFile bmobFile) {
                user.setUserPhoto(bmobFile.getFileUrl());
                user.setUsername(name);
                user.setMobilePhoneNumber(phoneNumber);
                user.setPassword(psw);
                user.setSex(mSex);
                //头像上传成功后，进行注册
                UserHelper.getInstance().register(user, smsCode, new UserHelper.UserCallback() {
                    @Override
                    public void onSuccess(final CAUser caUser) {

                        timer.cancel();
                        mGetSMSCodeBtn.setText("获得验证码");
                        mGetSMSCodeBtn.setTextColor(Color.WHITE);
                        mGetSMSCodeBtn.setEnabled(true);

                        Log.d(TAG,"register()-->> uid:"+ caUser.getObjectId());
                        Log.d(TAG,"register()-->> name:"+ caUser.getUsername());
                        Log.d(TAG,"register()-->> photo:"+ caUser.getUserPhoto());

                        new RequestTokenAsyncTask(new RequestTokenAsyncTask.GetTokenCallback() {
                            @Override
                            public void onSuccess(TokenResult result) {
                                if (result.getCode() == 200){
                                    Log.d(TAG, "token:" + result.getToken());
                                    caUser.setRyUserToken(result.getToken());
                                    //设置用户融云token
                                    caUser.update(caUser.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e == null){
                                                //融云用户token获得成功，进行登录，进入应用
                                                UserHelper.getInstance().login(phoneNumber, psw, new UserHelper.UserCallback() {
                                                    @Override
                                                    public void onSuccess(CAUser user) {
                                                        progress.cancel();
                                                        Toast.makeText(RegisterFragment.this.getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onError(BmobException e) {
                                                        Toast.makeText(RegisterFragment.this.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }else{
                                                progress.cancel();
                                                Toast.makeText(RegisterFragment.this.getContext(), "注册失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                                }else{
                                    progress.cancel();
                                    Log.d(TAG, "code:" + result.getCode() + "msg:" + result.getErrorMessage());
                                    Toast.makeText(RegisterFragment.this.getContext(), "注册失败：" + "code:"+ result.getCode(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(String errorMsg) {
                                progress.cancel();
                                Log.d(TAG, "errorMsg:" + errorMsg);
                                Toast.makeText(RegisterFragment.this.getContext(), "注册失败：" + errorMsg, Toast.LENGTH_SHORT).show();

                            }
                        }).execute(caUser.getObjectId(),
                                   caUser.getUsername(),
                                   caUser.getUserPhoto());

                    }

                    @Override
                    public void onError(BmobException e) {
                        progress.cancel();
                        Toast.makeText(RegisterFragment.this.getContext(), "注册失败.", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onError(BmobException e) {
                progress.cancel();
                Toast.makeText(RegisterFragment.this.getContext(),
                        "头像上传失败:" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(Integer value) {

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
