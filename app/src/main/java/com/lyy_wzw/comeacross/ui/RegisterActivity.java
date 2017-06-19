package com.lyy_wzw.comeacross.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.rong.server.utils.downtime.DownTimerListener;
import com.lyy_wzw.comeacross.rong.server.widget.ClearWriteEditText;




public class RegisterActivity extends BaseActivity implements View.OnClickListener,DownTimerListener {

    private ClearWriteEditText mPhoneEdit;
    private ClearWriteEditText mCodeEdit;
    private ClearWriteEditText mNickEdit;
    private ClearWriteEditText mPasswordEdit;
    private Button mGetCode;
    private Button mConfirm;
    private TextView goLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    private void initView() {

        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.reg_phone);
        mCodeEdit = (ClearWriteEditText) findViewById(R.id.reg_code);
        mNickEdit = (ClearWriteEditText) findViewById(R.id.reg_username);
        mPasswordEdit = (ClearWriteEditText) findViewById(R.id.reg_password);
        mGetCode = (Button) findViewById(R.id.reg_getcode);
        mConfirm = (Button) findViewById(R.id.reg_button);

        mGetCode.setOnClickListener(this);
        mGetCode.setClickable(false);
        mConfirm.setOnClickListener(this);

        goLogin = (TextView) findViewById(R.id.reg_login);


    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {

    }
}
