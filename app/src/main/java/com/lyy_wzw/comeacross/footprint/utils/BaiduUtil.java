package com.lyy_wzw.comeacross.footprint.utils;


import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;

/**
 * Created by yidong9 on 17/7/4.
 */

public class BaiduUtil {

    public static String getLocationInfo(LatLng latLng){
        //http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=39.983424,116.322987&output=json&pois=0&ak=mT08b9sP4x40kfxDjjTBPKusLDpaeQNG&mcode=E7:9C:58:A2:85:3D:ED:62:ED:17:3D:C5:E7:4C:72:A8:C1:74:D1:8A;com.lyy_wzw.comeacross
        String url = "http://api.map.baidu.com/geocoder/v2/?location=" + latLng.latitude + ","
                + latLng.longitude + "&output=json&ak=" + FootPrintConstantValue.BAIDU_APPKEY +"&pois=0&mcode="+ FootPrintConstantValue.BAIDU_SAFE_CODE;
        Log.d("BaiduUtil","getLocationInfo()-->>URL："+ url);
        return BaiduHttpUtil.getRequest(url);
    }


}
