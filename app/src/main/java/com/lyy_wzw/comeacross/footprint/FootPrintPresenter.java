package com.lyy_wzw.comeacross.footprint;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yidong9 on 17/5/12.
 */

public class FootPrintPresenter implements FootPrintContract.Presenter{
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
        LatLng pt1 = new LatLng(39.53923, 116.357428);
        mView.showFootPrintMark(pt1);
    }

    @Override
    public void setSelfMark() {

    }


    @Override
    public void setFootPrintMarks() {
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.73923, 116.327428);
        LatLng pt3 = new LatLng(39.6398, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);
        List<LatLng> pts = new ArrayList<LatLng>();

        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);
        for (int i = 0; i < pts.size(); i++) {
            mView.showFootPrintMark(pts.get(i));
        }

    }

}
