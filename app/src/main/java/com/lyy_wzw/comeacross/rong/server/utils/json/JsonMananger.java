/*
    ShengDao Android Client, JsonMananger
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.lyy_wzw.comeacross.rong.server.utils.json;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.lyy_wzw.comeacross.rong.server.network.http.HttpException;

import java.util.List;



/**
 * [JSON解析管理类]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2014-3-5
 *
 **/
public class JsonMananger {

    static {
        TypeUtils.compatibleWithJavaBean = true;
    }
    private static final String tag = JsonMananger.class.getSimpleName();

    /**
     * 将json字符串转换成java对象
     * @param json
     * @param cls
     * @return
     * @throws HttpException
     */
    public static <T> T jsonToBean(String json, Class<T> cls) throws HttpException {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成java List对象
     * @param json
     * @param cls
     * @return
     * @throws HttpException
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) throws HttpException {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     * @param obj
     * @return
     * @throws HttpException
     */
    public static String beanToJson(Object obj) throws HttpException {
        String result = JSON.toJSONString(obj);
        Log.e(tag, "beanToJson: " + result);
        return result;
    }

}
