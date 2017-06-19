package com.lyy_wzw.comeacross.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import com.lyy_wzw.comeacross.rong.server.network.async.AsyncTaskManager;
import com.lyy_wzw.comeacross.rong.server.network.async.OnDataListener;
import com.lyy_wzw.comeacross.rong.server.network.http.HttpException;
import com.lyy_wzw.comeacross.rong.server.utils.NToast;

/**
 * Created by 27459 on 2017/6/19.
 */

public abstract class BaseActivity extends FragmentActivity implements OnDataListener {
    private AsyncTaskManager mAsyncTaskManager;
    protected Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(setLayoutId());
        mAsyncTaskManager = AsyncTaskManager.getInstance(getApplicationContext());


    }

    protected abstract int setLayoutId();


    @Override
    public Object doInBackground(int requestCode, String parameter) throws HttpException {
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {

    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (state) {
            case AsyncTaskManager.HTTP_NULL_CODE:
                NToast.shortToast(mContext,"当前网络不可用");
                break;
            case AsyncTaskManager.HTTP_ERROR_CODE:
                NToast.shortToast(mContext,"网络问题请稍候重试");
                break;
            case AsyncTaskManager.REQUEST_ERROR_CODE:
                NToast.shortToast(mContext,"请求有问题请稍候重试");
                break;
        }

    }

    /**
     * 发送请求(需要检查网络)
     * @param resquestCode 请求码
     */
    public void request(int resquestCode){
        if (mAsyncTaskManager != null) {
            mAsyncTaskManager.request(resquestCode,this);
        }
    }

    /**
     * 发送请求(需要检查网络)
     * @param id 请求数据的用户ID或者groupID
     * @param requestCode
     */
    public void request(String id,int requestCode){
        if (mAsyncTaskManager != null) {
            mAsyncTaskManager.request(id,requestCode,this);
        }
    }

    /**
     * 发送请求
     * @param requestCode 请求码
     * @param isCheckNetWork  是否需要检查网络
     */
    public void request(int requestCode,boolean isCheckNetWork){
        if (mAsyncTaskManager != null) {
            mAsyncTaskManager.request(requestCode,isCheckNetWork,this);
        }
    }

    public void cancelRequest(){
        if (mAsyncTaskManager != null) {
            mAsyncTaskManager.cancelRequest();
        }
    }
}
