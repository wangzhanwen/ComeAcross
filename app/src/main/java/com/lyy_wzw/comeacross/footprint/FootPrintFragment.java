package com.lyy_wzw.comeacross.footprint;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.utils.BMapUtil;

import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootPrintFragment extends Fragment implements FootPrintContract.View, BDLocationListener {

    private static MainActivity mainActivity;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    private LatLng mSelfLocation ;
    private Marker mSelfMarker = null;
    private boolean isFirstIn = true; //是否第一次初始化自己位置
    private MyOrientationListener myOrientationListener;
    private float myCurrentX;

    private InfoWindow mInfoWindow = null;
    private BMapManager mapManager = null;
    private LayoutInflater mLayoutInflater = null;

    private FootPrintContract.Presenter mPresenter;

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
        initViews(view);
        return view;
    }


    @Override
    public void setPresenter(FootPrintContract.Presenter presenter) {
        if (presenter != null) {
            this.mPresenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        mainActivity.toolbar.setTitle("足迹");


        mMapView =  (MapView) view.findViewById(R.id.footprint_mapView);
        //获取核心
        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1); // 去掉百度logo
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        mLayoutInflater = LayoutInflater.from(this.getContext());


        initLocationClient();
        mPresenter.setFootPrintMark();
        mPresenter.setFootPrintMarks();
    }


    @Override
    public void showFootPrintMark(LatLng point) {
        MarkerOptions options = new MarkerOptions();
        options.position(point);
        options.perspective(true);
//        Random random = new Random();
//        int rotate = random.nextInt(30);
//        int oritation =  random.nextBoolean() ? 1 : -1;
//        rotate = rotate * oritation;
//        options.rotate(rotate);
        options.flat(false);
        //加载覆盖物布局View
        View markView = mLayoutInflater.inflate(R.layout.footprint_map_mark, null);
        Bitmap bitmap = BMapUtil.getBitmapFromView(markView);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        options.icon(bitmapDescriptor);
        //在地图上标注覆盖物
        mBaiduMap.addOverlay(options);


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
        option.setScanSpan(3000);
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

        setLocationOrientationListener();

    }

    private void setLocationOrientationListener() {
        myOrientationListener=new MyOrientationListener(this.getActivity());
        myOrientationListener.setMyOrientationListener(new MyOrientationListener.onOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                myCurrentX=x;
            }
        });
    }



    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        double latitude = bdLocation.getLatitude();
        double longitude = bdLocation.getLongitude();
        LatLng point = new LatLng(latitude, longitude);
        mSelfLocation = point;

        // TODO: 添加/移动 Marker对象
        if(mSelfMarker == null){
            MarkerOptions options = new MarkerOptions();
            options.position(point);
            BitmapDescriptor descriptor =
                    BitmapDescriptorFactory.fromAsset("Icon_start.png");
            options.icon(descriptor);



            mSelfMarker = (Marker) mBaiduMap.addOverlay(options);
        }
        // 如果已经创建过 Marker,那么更新位置
        mSelfMarker.setPosition(point);


//        //BDLocation 回调的百度坐标类，内部封装了如经纬度、半径等属性信息
//        //MyLocationData 定位数据,定位数据建造器
//            /*
//            * 可以通过BDLocation配置如下参数
//            * 1.accuracy 定位精度
//            * 2.latitude 百度纬度坐标
//            * 3.longitude 百度经度坐标
//            * 4.satellitesNum GPS定位时卫星数目 getSatelliteNumber() gps定位结果时，获取gps锁定用的卫星数
//            * 5.speed GPS定位时速度 getSpeed()获取速度，仅gps定位结果时有速度信息，单位公里/小时，默认值0.0f
//            * 6.direction GPS定位时方向角度
//            * */
//
//        Log.d("baidu:set", "myCurrentX:" + myCurrentX);
//        MyLocationData data= new MyLocationData.Builder()
//                .direction(myCurrentX)//设定图标方向
//                .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
//                .latitude(bdLocation.getLatitude())//百度纬度坐标
//                .longitude(bdLocation.getLongitude())//百度经度坐标
//                .build();
//        //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
//        mBaiduMap.setMyLocationData(data);
//        //配置定位图层显示方式,三个参数的构造器
//            /*
//            * 1.定位图层显示模式
//            * 2.是否允许显示方向信息
//            * 3.用户自定义定位图标
//            *
//            * */
//        BitmapDescriptor myBitmapLocation = BitmapDescriptorFactory.fromResource(R.mipmap.calibration_arrow);
//        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS,true,myBitmapLocation);
//        //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
//        mBaiduMap.setMyLocationConfigeration(configuration);
//        //判断是否为第一次定位,是的话需要定位到用户当前位置
//        if(isFirstIn)
//        {
//            //地理坐标基本数据结构
//            LatLng latLng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
//            //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
//            MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
//            //改变地图状态
//            mBaiduMap.setMapStatus(msu);
//            isFirstIn=false;
//            Toast.makeText(this.getContext(), bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
//        }
//
//



    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mLocationClient.isStarted()) {
            mLocationClient.start();// 针对定位, 自定进行定位的请求
        }
        myOrientationListener.start();
    }

    @Override
    public void onStop() {

        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        myOrientationListener.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        //在onPause 取消定位操作
        mLocationClient.unRegisterLocationListener(this);
        mMapView.onDestroy();
        mSelfMarker = null;
        super.onDestroyView();
    }
}
