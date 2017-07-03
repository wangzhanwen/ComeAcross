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
    public static void loadCirclePic(final Context context, String url,final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .load(url)
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                .centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //设置缓存
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);

                        imageView.setImageDrawable(circularBitmapDrawable);
                    }});

    }


    public static void loadPicAsGif(final Context context, String path, final ImageView imageView){
        Glide.with(context)
                .load(path)
                .asGif()
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                .centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void loadPic(final Context context, String path,final ImageView imageView){
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                .centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}
