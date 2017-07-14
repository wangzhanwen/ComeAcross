package com.lyy_wzw.comeacross.discovery.widgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.lyy_wzw.comeacross.rong.server.widget.LoadDialog;
import com.lyy_wzw.comeacross.utils.PixelUtil;


/**
 * Created by yidong9 on 17/7/10.
 */

public class CircleTopRefreshView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "CircleTopRefreshView";
    private Context mContext;
    private ObjectAnimator rotateAnim;
    public AnimSatus mRefreshAnimStatus = AnimSatus.NULL;
    public RefreshCallback mRefreshCallback ;

    public enum AnimSatus {
        NULL, START, END, CANCEL, REPEAT
    }

    public void setRefreshCallback(RefreshCallback refreshCallback){
        mRefreshCallback = refreshCallback;
    }

    public CircleTopRefreshView(Context context) {
        super(context);
        mContext = context;
    }

    public CircleTopRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CircleTopRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    public void moveAndRotate(int height){
        float dpHeight = PixelUtil.px2dip(mContext,height);
        float angle = dpHeight * 10;
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(this, "rotation", 0, 720f);
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(this, "translationY", dpHeight);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnim, translationYAnim);
        animatorSet.setDuration(600);
        animatorSet.start();
    }



    public void startRefreshAnim(){
        this.setVisibility(View.VISIBLE);
        rotateAnim = ObjectAnimator.ofFloat(this, "rotation", 0, 360f);
        rotateAnim.setDuration(600);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnim.setInterpolator(lin);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRefreshAnimStatus = AnimSatus.START;
                Log.d(TAG, "mRefreshAnimStatus = AnimSatus.START");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRefreshAnimStatus = AnimSatus.END;
                //Log.d(TAG, "mRefreshAnimStatus = AnimSatus.END");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRefreshAnimStatus = AnimSatus.CANCEL;
                Log.d(TAG, "mRefreshAnimStatus = AnimSatus.CANCEL");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mRefreshAnimStatus = AnimSatus.REPEAT;
                //Log.d(TAG, "mRefreshAnimStatus = AnimSatus.REPEAT");
            }
        });
        rotateAnim.start();

    }


    public void stopRefreshAnim(){
        if (rotateAnim != null && mRefreshAnimStatus == AnimSatus.START){
            rotateAnim.cancel();
            rotateAnim.cancel();
        }
        this.moveAndRotate(-PixelUtil.dip2px(mContext, 400));

    }

    public void cancleRefreshAnim(){
        if (rotateAnim != null && mRefreshAnimStatus == AnimSatus.START){
            rotateAnim.cancel();
            rotateAnim.cancel();
        }
        this.moveAndRotate(-PixelUtil.dip2px(mContext, 400));
    }

    public interface RefreshCallback{
        void onRefresh();
    }
}
