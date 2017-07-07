package com.lyy_wzw.comeacross.homecommon;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;


/**
 * Created by yidong9 on 17/5/11.
 */

public class InitApplication  extends Application{
    public static String APPID = "575d92ce0454363528535cf901fb9d06";
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //融云初始化
        RongIM.init(this);
        //初始化bmob
        Bmob.initialize(this, APPID);



    }

    public static Context getContext(){
        return mContext;
    }
}
