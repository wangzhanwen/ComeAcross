package com.lyy_wzw.comeacross.footprint;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.discovery.activitys.FootPrintCircleActivity;
import com.lyy_wzw.comeacross.discovery.adapter.CircleRecyclerViewAdapter;
import com.lyy_wzw.comeacross.footprint.cards.SliderAdapter;
import com.lyy_wzw.comeacross.footprint.cardslider.CardSliderLayoutManager;
import com.lyy_wzw.comeacross.footprint.cardslider.CardSnapHelper;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.server.FootPrintServer;
import com.lyy_wzw.comeacross.utils.BitmapUtil;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootPrintFragment extends Fragment implements FootPrintContract.View, BDLocationListener, BaiduMap.OnMarkerClickListener, View.OnClickListener {
    private static final String TAG = "FootPrintFragment";
    private static final int REQUEST_CODE = 0x00000011;

    private static MainActivity mainActivity;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private ImageView mShareFootPrintBtn;
    private LocationClient mLocationClient = null;
    private LatLng mSelfLocation ;
    private Marker mSelfMarker = null;
    private boolean isFirstIn = true; //是否第一次初始化自己位置
    private FootPrintMapOrientationListener myOrientationListener;
    private float myCurrentX;

    private InfoWindow mInfoWindow = null;
    private BMapManager mapManager = null;
    private LayoutInflater mLayoutInflater = null;

    private FootPrintContract.Presenter mPresenter;
    private View mContainView;
    private InfoWindow mFootPrintWin;


    private RecyclerView recyclerView;
    private CardSliderLayoutManager layoutManger;
    private  SliderAdapter sliderAdapter ;
    private List<FootPrint> mDatas;


    public FootPrintFragment() {

    }

    public static FootPrintFragment instance(MainActivity activity){
        mainActivity = activity;
        return new FootPrintFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContainView = inflater.inflate(R.layout.fragment_foot_print, container, false);
        mShareFootPrintBtn = (ImageView)mContainView.findViewById(R.id.fragment_share_footprint_btn);
        mShareFootPrintBtn.setOnClickListener(this);
        initViews(mContainView);
        initRecyclerView(mContainView);
        return mContainView;
    }


    private void initRecyclerView(View rootView) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // onActiveCardChange();
                    Toast.makeText(FootPrintFragment.this.getContext(), "onActiveCardChange()", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        new CardSnapHelper().attachToRecyclerView(recyclerView);

        FootPrintServer.getInstance().getAll(new FootPrintServer.FootPrintQueryCallback() {
            @Override
            public void onSuccess(List<FootPrint> footPrints) {
                Log.d(TAG, "footPrints:"+footPrints.toString());
                if (footPrints != null){
                    mDatas = footPrints;
                    sliderAdapter = new SliderAdapter(FootPrintFragment.this.getContext(), mDatas, new OnCardClickListener());
                    recyclerView.setAdapter(sliderAdapter);
                }
            }

            @Override
            public void onError(BmobException e) {
                Log.d(TAG, "e:"+e.getMessage());
            }
        });


    }

//    private void onActiveCardChange() {
//        final int pos = layoutManger.getActiveCardPosition();
//        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
//            return;
//        }
//
//        onActiveCardChange(pos);
//    }
//
//    private void onActiveCardChange(int pos) {
//        int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
//        int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};
//
//        final boolean left2right = pos < currentPosition;
//        if (left2right) {
//            animH[0] = R.anim.slide_in_left;
//            animH[1] = R.anim.slide_out_right;
//
//            animV[0] = R.anim.slide_in_bottom;
//            animV[1] = R.anim.slide_out_top;
//        }
//
//        setCountryText(countries[pos % countries.length], left2right);
//
//        temperatureSwitcher.setInAnimation(MainActivity.this, animH[0]);
//        temperatureSwitcher.setOutAnimation(MainActivity.this, animH[1]);
//        temperatureSwitcher.setText(temperatures[pos % temperatures.length]);
//
//        placeSwitcher.setInAnimation(MainActivity.this, animV[0]);
//        placeSwitcher.setOutAnimation(MainActivity.this, animV[1]);
//        placeSwitcher.setText(places[pos % places.length]);
//
//        clockSwitcher.setInAnimation(MainActivity.this, animV[0]);
//        clockSwitcher.setOutAnimation(MainActivity.this, animV[1]);
//        clockSwitcher.setText(times[pos % times.length]);
//
//        descriptionsSwitcher.setText(getString(descriptions[pos % descriptions.length]));
//
//        showMap(maps[pos % maps.length]);
//
//        ViewCompat.animate(greenDot)
//                .translationX(dotCoords[pos % dotCoords.length][0])
//                .translationY(dotCoords[pos % dotCoords.length][1])
//                .start();
//
//        currentPosition = pos;
//    }




    @Override
    public void setPresenter(FootPrintContract.Presenter presenter) {
        if (presenter != null) {
            this.mPresenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {

        mMapView =  (MapView) view.findViewById(R.id.footprint_mapView);
        //获取核心
        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1); // 去掉百度logo
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        //设置地图覆盖物点击事件
        mBaiduMap.setOnMarkerClickListener(this);

        mLayoutInflater = LayoutInflater.from(this.getActivity().getApplicationContext());


        initLocationClient();
        mPresenter.setFootPrintMark();
        mPresenter.setFootPrintMarks();
    }


    @Override
    public void showFootPrintMark(LatLng point) {
        MarkerOptions options = new MarkerOptions();
        options.position(point);
        options.perspective(true);
        Random random = new Random();
        int rotate = random.nextInt(30);
        int oritation =  random.nextBoolean() ? 1 : -1;
        rotate = rotate * oritation;
        options.rotate(rotate);
        options.flat(false);
        //加载覆盖物布局View
        View markView = mLayoutInflater.inflate(R.layout.footprint_map_mark, null);
        Bitmap bitmap = BitmapUtil.getBitmapFromView(markView);
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
        myOrientationListener=new FootPrintMapOrientationListener(this.getActivity());
        myOrientationListener.setMyOrientationListener(new FootPrintMapOrientationListener.onOrientationListener() {
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

        // TODO: 添加/移动 自己位置Marker
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
        //setSelfLocation(bdLocation);

    }

    private void setSelfLocation(BDLocation bdLocation){
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

        MyLocationData data= new MyLocationData.Builder()
                .direction(myCurrentX)//设定图标方向
                .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
                .latitude(bdLocation.getLatitude())//百度纬度坐标
                .longitude(bdLocation.getLongitude())//百度经度坐标
                .build();
        //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
        mBaiduMap.setMyLocationData(data);
        //配置定位图层显示方式,三个参数的构造器
            /*
            * 1.定位图层显示模式
            * 2.是否允许显示方向信息
            * 3.用户自定义定位图标
            *
            * */
        BitmapDescriptor myBitmapLocation = BitmapDescriptorFactory.fromResource(R.mipmap.calibration_arrow);
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS,true,myBitmapLocation);
        //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
        mBaiduMap.setMyLocationConfigeration(configuration);
        //判断是否为第一次定位,是的话需要定位到用户当前位置
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
        //myOrientationListener.start();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //showFootPrintWin(marker);
        FootPrintPopupWin footPrintPopupWindow = new FootPrintPopupWin(this.getActivity());

        footPrintPopupWindow.showAtLocation(mContainView, Gravity.CENTER, 0, 0);
        //Toast.makeText(getContext(), "点击了mark", Toast.LENGTH_SHORT).show();

        //打开摄像机
//        Intent intent = new Intent(getActivity(), CameraActivity.class);
//        getActivity().startActivity(intent);

        //发朋友圈
        //Intent intent = new Intent(getActivity(), ShareFootPrintActivity.class);
        //getActivity().startActivity(intent);

        return true;
    }

    private void  showFootPrintWin(final Marker marker){
        if (null!=marker) {
            BitmapDescriptor icon = marker.getIcon();
            if (null!=icon) {
                View footPrintWinView = mLayoutInflater.inflate(R.layout.footprint_map_popup_win, null);
                double latitude, longitude;
                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
                LatLng latLng = new LatLng(latitude, longitude );
                Log.d(TAG, "icon!=null");

                mFootPrintWin = new InfoWindow(footPrintWinView, latLng, -80);
                mBaiduMap.showInfoWindow(mFootPrintWin);
            }else {
                Log.d(TAG, "icon==null");
            }

        }else{
            Log.d(TAG, "marker==null");
        }

    }

    @Override
    public void onStop() {

        super.onStop();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_share_footprint_btn:
                Toast.makeText(this.getContext(), "分享足迹", Toast.LENGTH_SHORT).show();

                ShareFootPrintPopupWin shareFootPrintPW = new ShareFootPrintPopupWin(this.getContext());
                shareFootPrintPW.setSelectImageCount(FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT);
                shareFootPrintPW.showAtLocation(mShareFootPrintBtn, Gravity.CENTER, 0, 0);
                break;

        }
    }



    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm =  (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {

                Toast.makeText(FootPrintFragment.this.getContext(), "点击card", Toast.LENGTH_SHORT).show();

//                final Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, pics[activeCardPosition % pics.length]);
//
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent);
//                } else {
//                    final CardView cardView = (CardView) view;
//                    final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
//                    final ActivityOptions options = ActivityOptions
//                            .makeSceneTransitionAnimation(MainActivity.this, sharedView, "shared");
//                    startActivity(intent, options.toBundle());
//                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                //onActiveCardChange(clickedPosition);

                Toast.makeText(FootPrintFragment.this.getContext(), "onActiveCardChange(clickedPosition)", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
