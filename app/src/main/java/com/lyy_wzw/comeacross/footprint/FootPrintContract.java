package com.lyy_wzw.comeacross.footprint;



import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.homecommon.BasePresenter;
import com.lyy_wzw.comeacross.homecommon.BaseView;

import java.util.List;


/**
 * Created by yidong9 on 17/5/12.
 */

public interface FootPrintContract {
    interface  View extends BaseView<Presenter>{
        void showFootPrintMark(FootPrint footPrint, int position);


    }

    interface Presenter extends BasePresenter{
        void setFootPrintMark();
        void setFootPrintMarks(List<FootPrint> footPrints);
        void setSelfMark();

    }
}
