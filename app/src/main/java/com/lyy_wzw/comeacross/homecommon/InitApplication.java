package com.lyy_wzw.comeacross.homecommon;

import android.app.Application;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;


/**
 * Created by yidong9 on 17/5/11.
 */

public class InitApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
       //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

    }
}
