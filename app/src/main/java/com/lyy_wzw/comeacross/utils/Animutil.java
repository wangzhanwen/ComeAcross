package com.lyy_wzw.comeacross.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

/**
 * Created by yidong9 on 17/7/13.
 */

public class Animutil {

    public static void moveAnimDown(Context context, View view, int instance, int time, final OnAnimListener onAnimListener){
        float dpHeight = PixelUtil.dip2px(context,instance);
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(view, "translationY", -dpHeight,0);
        translationYAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimListener != null) {
                    onAnimListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        translationYAnim.setDuration(time);
        translationYAnim.start();
    }

    public static void moveAnimUp(Context context, View view, int instance, int time, final OnAnimListener onAnimListener){
        float dpHeight = PixelUtil.dip2px(context,instance);
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(view, "translationY", 0, -dpHeight);
        translationYAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimListener != null) {
                    onAnimListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        translationYAnim.setDuration(time);
        translationYAnim.start();
    }


    public interface  OnAnimListener{
        void onEnd();
    }
}
