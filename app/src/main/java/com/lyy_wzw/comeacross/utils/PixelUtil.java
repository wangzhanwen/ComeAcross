package com.lyy_wzw.comeacross.utils;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class PixelUtil {

    //########################################################################
    // 屏幕像素转换相关方法

    private static final int DP_PHONE          = 320; // a typical phone screen (240x320 ldpi, 320x480 mdpi, 480x800 hdpi, etc).
    private static final int DP_TWEENER_TABLET = 480; // a tweener tablet like the Streak (480x800 mdpi).
    private static final int DP_7_INCH_TABLET  = 600; // a 7” tablet (600x1024 mdpi).
    private static final int DP_10_INCH_TABLET = 720; // a 10” tablet (720x1280 mdpi, 800x1280 mdpi, etc).

    private static float sLayoutScale = 0.0f;

    /**
     * 屏幕像素点缩放比率
     * @param context
     * @return
     */
    private static float getScale(Context context) {
        if (sLayoutScale <= 0) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();

            int width = metrics.widthPixels;   // 屏幕绝对宽度（px）
            int height = metrics.heightPixels; // 屏幕绝对高度（px）

            // 转换为基准dp
            float density = metrics.density;
            int w = (int) (width / density);
            int h = (int) (height / density);

            if (w > h) {
                w = w * 2 / 3;
            }

            float scale = 0.0f;
            if (w <= DP_TWEENER_TABLET) {
                scale = 1.0f;
            } else if (w < DP_7_INCH_TABLET) {
                scale = (float) DP_TWEENER_TABLET / DP_PHONE;
            } else if (w < DP_10_INCH_TABLET) {
                scale = (float) DP_7_INCH_TABLET / DP_PHONE;
            } else {
                scale = (float) DP_10_INCH_TABLET / DP_PHONE;
            }

            if (density >= 1.0f && scale > density) {
                scale /= density;
            } else {
                scale = 1.0f;
            }

            sLayoutScale = scale;
        }

        return sLayoutScale;
    }

    /**
     * DP转像素
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = getScale(context);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue * scale,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 像素转DP
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取实际字体大小
     * @param context
     * @param fontSize
     * @return
     */
    public static float parseFontSize(Context context, float fontSize) {
        return fontSize * getScale(context);
    }

    /**
     * 获取屏幕宽高
     * @param context
     * @return
     */
    public static int[] getDeviceScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int width = metrics.widthPixels; // 屏幕绝对宽度（px）
        int height = metrics.heightPixels; // 屏幕绝对高度（px）

        // 转换为基准dp
        float density = metrics.density;
        width = (int) (width / density);
        height = (int) (height / density);

        return (new int[] {width, height});
    }

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
            if (networkInfos == null) {
                return false;
            }
            for (NetworkInfo networkInfo : networkInfos) {
                if (networkInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 使用外部浏览器打开网页
     * @param uri
     */
    public static void openURI(Context context, String uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }
    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
