package com.lyy_wzw.comeacross.discovery.widgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.lyy_wzw.comeacross.utils.PixelUtil;

/**
 * Created by yidong9 on 17/7/11.
 */

public class CircleBottomRefreshView extends android.support.v7.widget.AppCompatImageView{
    private Context mContext;
    private ObjectAnimator rotateAnim;
    public  AnimSatus mRefreshAnimStatus = AnimSatus.NULL;

    public enum AnimSatus {
        NULL, START, END, CANCEL, REPEAT
    }

    public CircleBottomRefreshView(Context context) {
        super(context);
        mContext = context;
    }

    public CircleBottomRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CircleBottomRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void moveAndRotate(int height){
        float dpHeight = PixelUtil.px2dip(mContext,height);
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(this, "rotation", 0, 720f);
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(this, "translationY", dpHeight);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnim, translationYAnim);
        animatorSet.setDuration(600);
        animatorSet.start();
    }


    public void startRefreshAnim(){
        rotateAnim = ObjectAnimator.ofFloat(this, "rotation", 0, 360f);
        rotateAnim.setDuration(600);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnim.setInterpolator(lin);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRefreshAnimStatus = CircleBottomRefreshView.AnimSatus.START;

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRefreshAnimStatus = CircleBottomRefreshView.AnimSatus.END;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRefreshAnimStatus = CircleBottomRefreshView.AnimSatus.CANCEL;

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mRefreshAnimStatus = CircleBottomRefreshView.AnimSatus.REPEAT;

            }
        });
        rotateAnim.start();

    }

    public void stopRefreshAnim(){
        if (rotateAnim != null && mRefreshAnimStatus == AnimSatus.START){
            rotateAnim.cancel();
        }

    }

    public void cancleRefreshAnim(){
        if (rotateAnim != null && mRefreshAnimStatus == AnimSatus.START){
            rotateAnim.cancel();
        }
    }

}
