package com.lyy_wzw.comeacross.homecommon;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import io.rong.imkit.RongIM;


/**
 * Created by yidong9 on 17/5/11.
 */

public class InitApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
       //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //融云初始化
        RongIM.init(this);



    }
}
