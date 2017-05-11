package com.lyy_wzw.comeacross.footprint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.MyLocationData;
import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.discovery.DiscoveryFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootPrintFragment extends Fragment {

    private static MainActivity mainActivity;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;


    public FootPrintFragment() {

    }

    public static FootPrintFragment instance(MainActivity activity){
        mainActivity = activity;
        return new FootPrintFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foot_print, container, false);
        mainActivity.toolbar.setTitle("足迹");


        mMapView =  (MapView) view.findViewById(R.id.footprint_mapView);
        //获取核心
        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1); // 去掉百度logo
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        

        return view;
    }



}
