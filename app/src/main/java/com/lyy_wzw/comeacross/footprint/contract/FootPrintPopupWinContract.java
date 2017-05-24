package com.lyy_wzw.comeacross.footprint.contract;

import com.lyy_wzw.comeacross.footprint.FootPrintContract;
import com.lyy_wzw.comeacross.homecommon.BasePresenter;
import com.lyy_wzw.comeacross.homecommon.BaseView;

import java.util.List;

/**
 * Created by yidong9 on 17/5/18.
 */

public interface  FootPrintPopupWinContract {
    interface View extends BaseView<FootPrintPopupWinContract.Presenter>{
        void setGridViewHeight(int height);

    }

    interface Presenter extends BasePresenter {
        List<String>  getImageUrls();
    }
}
