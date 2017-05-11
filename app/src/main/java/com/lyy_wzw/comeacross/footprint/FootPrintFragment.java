package com.lyy_wzw.comeacross.footprint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.discovery.DiscoveryFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootPrintFragment extends Fragment implements BDLocationListener {

    private static MainActivity mainActivity;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    private Marker mSelfMarker;
    private InfoWindow mInfoWindow;
    private BMapManager mapManager;



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

        initLocationClient();

        return view;
    }

    private void initLocationClient() {
        // 初始化定位
        mLocationClient = new LocationClient(getContext());

        // 设置定位参数
        LocationClientOption option = new LocationClientOption();

        // 1. 设置定位的精度
        // 高精度
        // 省电模式
        // GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 2. 设置定位的时间间隔, 单位 ms, < 1000 代表只定位一次
        option.setScanSpan(5000);
        // 3. 定位的结果的类型, 针对百度地图,或者高德、或者其他
        option.setCoorType("bd09ll"); // bd09ll 代表百度地图, gcj02
        // 4. 打开GPS
        option.setOpenGps(true);
        // 5. 是否接收地址信息
        option.setIsNeedAddress(true);
        // 6. 设置
        mLocationClient.setLocOption(option);

        // 7. 设置定位结果接口回调
        mLocationClient.registerLocationListener(this);

        // 针对定位, 自定进行定位的请求
        mLocationClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // 定位接口可能返回错误码,要根据结果错误码,来判断是否是正确的地址;

        int locType = bdLocation.getLocType();


        switch (locType){
            case BDLocation.TypeGpsLocation:
            case BDLocation.TypeCacheLocation:
            case BDLocation.TypeNetWorkLocation:
            case BDLocation.TypeOffLineLocation:

                double latitude = bdLocation.getLatitude();
                double longitude = bdLocation.getLongitude();
                LatLng point = new LatLng(latitude, longitude);

                Log.d("baidu","得到定位结果");

                // 始终保证当前位置是屏幕的中心
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
                mBaiduMap.setMapStatus(update);

                // TODO: 添加/移动 Marker对象
                if(mSelfMarker == null){
                    MarkerOptions options = new MarkerOptions();
                    options.position(point);
                    BitmapDescriptor descriptor =
                            BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
                    options.icon(descriptor);



                    mSelfMarker = (Marker) mBaiduMap.addOverlay(options);
                }
                // 如果已经创建过 Marker,那么更新位置
                mSelfMarker.setPosition(point);
                break;

        }

    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
