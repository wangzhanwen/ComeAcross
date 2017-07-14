package com.lyy_wzw.comeacross.footprint.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonObject;
import com.lyy_wzw.comeacross.bean.FootPrintAddress;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by yidong9 on 17/7/4.
 */

public class AddressAsyncTask extends AsyncTask<LatLng, Integer, String>{
    private static final String TAG = "AddressAsyncTask";
    private AsyncTaskCallback mCallback;

    public AddressAsyncTask(AsyncTaskCallback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(LatLng... params) {
        String ret = null;

        LatLng latLng = params[0];
        if (latLng != null){
            ret = BaiduUtil.getLocationInfo(latLng);
        }

        return ret;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null){
            mCallback.onError("异步任务参数为null.");
            Log.d(TAG,"onPostExecute()-->> 异步任务参数为null");
            return;
        }

        try {
            JSONObject jsonObj = new JSONObject(result);
            int status = jsonObj.getInt("status");
            if (status == 0){
                JSONObject resultObj = jsonObj.getJSONObject("result");
                FootPrintAddress footPrintAddress = FootPrintAddress.jsonStrToFootPrintAddress(resultObj.toString());
                if(footPrintAddress != null){
                    mCallback.onSuccess(footPrintAddress);
                }else {
                    mCallback.onError("json解析出错.");
                }

            }else{
                mCallback.onError("网络请求出错，code："+ status);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public interface AsyncTaskCallback{
        void onSuccess(FootPrintAddress footPrintAddress);
        void onError(String msg);
    }


}
