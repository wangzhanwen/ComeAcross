package com.lyy_wzw.comeacross.homecommon;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;


/**
 * Created by yidong9 on 17/5/11.
 */

public class InitApplication  extends Application{
    public static String APPID = "575d92ce0454363528535cf901fb9d06";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //融云初始化
        RongIM.init(this);
        //初始化bmob
        Bmob.initialize(this, APPID);



    }
}
