package com.lyy_wzw.comeacross.footprint.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * Created by yidong9 on 17/6/30.
 */

public class CardRecyclerView  extends RecyclerView{
    public CardRecyclerView(Context context) {
        super(context);
    }

    public CardRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
