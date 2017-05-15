package com.lyy_wzw.comeacross.footprint;

import android.graphics.Bitmap;

import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.homecommon.BasePresenter;
import com.lyy_wzw.comeacross.homecommon.BaseView;

import java.util.List;

/**
 * Created by yidong9 on 17/5/12.
 */

public interface FootPrintContract {
    interface  View extends BaseView<Presenter>{
        void showFootPrintMark(LatLng point);


    }

    interface Presenter extends BasePresenter{
        void setFootPrintMark();
        void setFootPrintMarks();
        void setSelfMark();

    }
}
