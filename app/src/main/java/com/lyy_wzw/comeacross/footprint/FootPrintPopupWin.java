package com.lyy_wzw.comeacross.footprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.homecommon.BasePopupWindow;

/**
 * Created by yidong9 on 17/5/18.
 */

public class FootPrintPopupWin extends BasePopupWindow {
    private View mContainerView = null ;

    public FootPrintPopupWin(Context context) {
        super(context);
        mContainerView = LayoutInflater.from(context).inflate(R.layout.footprint_map_popup_win, null);
        this.setWidth(600);
        this.setHeight(400);
        this.setContentView(mContainerView);
    }

}
