package com.lyy_wzw.comeacross.discovery;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lyy_wzw.comeacross.discovery.widgets.CircleTopRefreshView;
import com.lyy_wzw.comeacross.utils.PixelUtil;

/**
 * Created by yidong9 on 17/7/10.
 */

public class RefreshBehaivor extends CoordinatorLayout.Behavior<CircleTopRefreshView>  {
    private static final String TAG = "RefreshBehaivor";
    private int  mAppBarHeight  = -1;
    private boolean isFirst = true;
    private boolean isDrawing = false;
    private int mLastHeight = -1;
    private int mLastMoveHeight = 0;
    private long lastRefreshTime = 0;
    private Context mContext;

    public RefreshBehaivor(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        mContext = context;

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleTopRefreshView child, View dependency) {
        boolean ret = false;
        if (dependency instanceof AppBarLayout){
            ret = true;
            if (isFirst && dependency.getHeight() > 0){
                mAppBarHeight = dependency.getHeight();
                mLastHeight = dependency.getHeight();
                isFirst = false;
            }
        }
        return ret;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleTopRefreshView child, View dependency) {

        int moveHeight =  dependency.getHeight() - mAppBarHeight;
        if ( (moveHeight > 0)){
            if(mLastHeight != -1){
                if ( moveHeight < PixelUtil.dip2px(mContext, 80)) {
                    child.setTranslationY(moveHeight);
                }else{
                    child.setTranslationY(PixelUtil.dip2px(mContext, 80));
                }
                child.setRotation(moveHeight);
                isDrawing = true;
            }
        }else if(isDrawing){
            long currentTime = System.currentTimeMillis();
            long time = currentTime - lastRefreshTime;
            if( time > 1000){
                if (child.mRefreshAnimStatus != CircleTopRefreshView.AnimSatus.START) {
                    child.mRefreshCallback.onRefresh();
                }
                lastRefreshTime = System.currentTimeMillis();
            }
            isDrawing = false;
        }
        return true;
    }
}
