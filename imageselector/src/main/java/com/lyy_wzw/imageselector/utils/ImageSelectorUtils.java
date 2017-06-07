package com.lyy_wzw.imageselector.utils;

import android.app.Activity;
import android.content.Context;

import com.lyy_wzw.imageselector.ImageSelectorActivity;
import com.lyy_wzw.imageselector.SelectResultListener;


/**
 * 提供给外界相册的调用的工具类
 */
public class ImageSelectorUtils {

    //图片选择的结果
    public static final String SELECT_RESULT = "select_result";

    /**
     * 打开相册，选择图片,可多选,不限数量。
     *
     * @param context
     *
     */
    public static void openPhoto(Context context, SelectResultListener selectResultListener) {
        openPhoto( context, selectResultListener, false, 0);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param context
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public static void openPhoto(Context context,  SelectResultListener selectResultListener, boolean isSingle, int maxSelectCount) {
        ImageSelectorActivity.openActivity(context, selectResultListener, isSingle, maxSelectCount);
    }
}
