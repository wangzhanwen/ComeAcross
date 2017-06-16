package com.lyy_wzw.comeacross.rong.server;

import android.content.Context;

import com.lyy_wzw.comeacross.rong.server.network.http.HttpException;
import com.lyy_wzw.comeacross.rong.server.network.http.SyncHttpClient;
import com.lyy_wzw.comeacross.rong.server.utils.json.JsonMananger;

import java.util.List;

/**
 * Created by 27459 on 2017/6/15.
 */

public class BaseAction {

    private static final String DOMAIN = "http://api.sealtalk.im";
    protected Context mContext;
    protected SyncHttpClient httpManager;

    public BaseAction(Context context) {
        mContext = context;
        this.httpManager = SyncHttpClient.getInstance(context);
    }


    /**
     * JSON转JAVA对象
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws HttpException
     */

    public<T> T jsonTobean(String json,Class<T> cls) throws HttpException{
        return JsonMananger.jsonToBean(json,cls);
    }

    /**
     * JSON转JAVA集合
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws HttpException
     */
    public<T> List<T> jsonToList(String json, Class<T> cls)throws HttpException{
        return JsonMananger.jsonToList(json,cls);
    }

    /**
     * JAVA转JSON
     * @param obj
     * @return
     * @throws HttpException
     */
    public String BeanTojson(Object obj)throws HttpException{
        return JsonMananger.beanToJson(obj);
    }

    /**
     * 获取完整url
     * @param url
     * @return
     */
    protected String getURL(String url){
        return getURL(url,new String[] {});
    }

    /**
     * 获取完整url
     * @param url
     * @param params
     * @return
     */
    protected String getURL(String url,String... params){
        StringBuilder urlBUilder = new StringBuilder(DOMAIN).append(url);
        if (params != null) {
            for (String param : params
                 ) {
                if (!urlBUilder.toString().endsWith("/") ) {
                    urlBUilder.append("/");
                }
                urlBUilder.append(param);
            }
        }
        return urlBUilder.toString();
    }



}
