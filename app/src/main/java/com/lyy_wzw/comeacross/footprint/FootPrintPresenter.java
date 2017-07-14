package com.lyy_wzw.comeacross.footprint;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.bean.FootPrint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yidong9 on 17/5/12.
 */

public class FootPrintPresenter implements FootPrintContract.Presenter {
    private FootPrintContract.View mView;
    private Context mContext;



    public FootPrintPresenter(Context context, FootPrintContract.View view){
        mContext = context;
        mView = view;
        //将presenter 传递给view
        this.mView.setPresenter(this);

    }


    @Override
    public void setFootPrintMark() {

    }

    @Override
    public void setSelfMark() {

    }


    @Override
    public void setFootPrintMarks(List<FootPrint> footPrints) {

        for (int i = 0; i < footPrints.size(); i++) {
            mView.showFootPrintMark(footPrints.get(i), i);
        }

    }

}
