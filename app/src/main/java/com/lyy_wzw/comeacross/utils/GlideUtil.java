package com.lyy_wzw.comeacross.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lyy_wzw.comeacross.R;

/**
 * Created by yidong9 on 17/6/29.
 */

public class GlideUtil {
    //加载圆形图片
    public static void loadCirclePic(final Context context, String url, float overrideWidth,float overrideHeight,final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .load(url)
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                // 重新改变图片大小成这些尺寸(像素)比
                .override(PixelUtil.dip2px(context, overrideWidth), PixelUtil.dip2px(context, overrideHeight))
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);

                        imageView.setImageDrawable(circularBitmapDrawable);
                    }});

    }


    public static void loadPicAsGif(final Context context, String path, float overrideWidth,float overrideHeight,final ImageView imageView){
        Glide.with(context)
                .load(path)
                .asGif()
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                // 重新改变图片大小成这些尺寸(像素)比
                .override(PixelUtil.dip2px(context, 100), PixelUtil.dip2px(context, 100))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void loadPic(final Context context, String path, float overrideWidth,float overrideHeight,final ImageView imageView){
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                // 重新改变图片大小成这些尺寸(像素)比
                .override(PixelUtil.dip2px(context, overrideWidth), PixelUtil.dip2px(context, overrideHeight))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}
