package com.lyy_wzw.comeacross.footprint;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.bean.FootPrintAddress;
import com.lyy_wzw.comeacross.footprint.cards.SliderAdapter;
import com.lyy_wzw.comeacross.footprint.cardslider.CardSliderLayoutManager;
import com.lyy_wzw.comeacross.footprint.cardslider.CardSnapHelper;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.footprint.utils.AddressAsyncTask;
import com.lyy_wzw.comeacross.server.FootPrintServer;
import com.lyy_wzw.comeacross.user.UserHelper;
import com.lyy_wzw.comeacross.utils.BitmapUtil;
import com.lyy_wzw.comeacross.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

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


    private TextSwitcher userNameSwitcher;
    private TextSwitcher contentSwitcher;

    private TextView location1TextView;
    private TextView location2TextView;
    private int locationOffset1;
    private int locationOffset2;
    private long locationAnimDuration;
    private int currentPosition = 0;
    private CircleImageView mCurrentUserImg;
    private CircleImageView mFootPrintUserImg;

    private FootPrintAddress mCurrentUserAddress;

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
        initViews(mContainView);

        return mContainView;
    }
    @Override
    public void initViews(View view) {
        initMapView(view);
        initRecyclerView(view);
        initLocationText(view);
        initSwitchers(view);

        mCurrentUserImg = (CircleImageView) view.findViewById(R.id.img_footprint_current_user);
        mFootPrintUserImg = (CircleImageView) view.findViewById(R.id.img_footprint_user_photo);
        mShareFootPrintBtn = (ImageView) view.findViewById(R.id.img_share_footprint_btn);
        mShareFootPrintBtn.setOnClickListener(this);
        mCurrentUserImg.setOnClickListener(this);

        CAUser currentUser = UserHelper.getInstance().getCurrentUser();
        if (currentUser != null) {
            GlideUtil.loadPic(this.getContext(),currentUser.getUserPhoto(),mCurrentUserImg);
        }

    }

    private void initRecyclerView(final View rootView) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        new CardSnapHelper().attachToRecyclerView(recyclerView);

        FootPrintServer.getInstance().getAll(new FootPrintServer.FootPrintQueryCallback() {
            @Override
            public void onSuccess(List<FootPrint> footPrints) {
                if (footPrints != null){
                    mDatas = footPrints;
                    sliderAdapter = new SliderAdapter(FootPrintFragment.this.getContext(), mDatas, new OnCardClickListener());
                    recyclerView.setAdapter(sliderAdapter);
                    //数据异步步获取成功后,初始化第一个数据
                    initSwitcherText();

                }
            }

            @Override
            public void onError(BmobException e) {
                Log.d(TAG, "e:"+e.getMessage());
            }
        });


    }


    private void initSwitchers(View rootView) {
        userNameSwitcher = (TextSwitcher) rootView.findViewById(R.id.ts_footprint_user_name);
        userNameSwitcher.setFactory(new TextViewFactory(R.style.UserNameTextView, true));

        contentSwitcher = (TextSwitcher) rootView.findViewById(R.id.ts_footprint_content);
        contentSwitcher.setInAnimation(this.getContext(), android.R.anim.fade_in);
        contentSwitcher.setOutAnimation(this.getContext(), android.R.anim.fade_out);
        contentSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));

    }


    private void initSwitcherText(){
        if (mDatas == null || mDatas.size()<1) {
            return;
        }
        FootPrint footPrint = mDatas.get(currentPosition);
        if (footPrint == null){
            return;
        }

        contentSwitcher.setCurrentText(footPrint.getContent());

        //根据当前用户跟足迹的地址是否在一个城市，显示地址信息
        String address = footPrint.getFootPrintAddress().getCity()+"."+ footPrint.getFootPrintAddress().getDistrict();
        if (mCurrentUserAddress != null && mCurrentUserAddress.getCity().equals(footPrint.getFootPrintAddress().getCity())){
            address = footPrint.getFootPrintAddress().getDistrict() + "." + footPrint.getFootPrintAddress().getStreet();
        }
        location1TextView.setText(address);

        String  userId = footPrint.getUserId();
        UserHelper.getInstance().queryUser("objectId", userId, new UserHelper.UserQueryCallback() {
            @Override
            public void onResult(List<CAUser> users, BmobException e) {

                if (users != null && users.size()>0 ){
                    CAUser caUser = users.get(0);
                    if (caUser != null){
                        userNameSwitcher.setCurrentText(caUser.getUsername());
                        GlideUtil.loadPic(FootPrintFragment.this.getContext(),caUser.getUserPhoto(),mFootPrintUserImg);
                    }
                }
            }
        });

        mPresenter.setFootPrintMarks(mDatas);
    }

    private void initLocationText(View rootView) {
        locationAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        locationOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        locationOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        location1TextView = (TextView) rootView.findViewById(R.id.tv_footprint_city_1);
        location2TextView = (TextView) rootView.findViewById(R.id.tv_footprint_city_2);

        location1TextView.setX(locationOffset1);
        location2TextView.setX(locationOffset2);
        //location1TextView.setText("北京");
        location2TextView.setAlpha(0f);

        location1TextView.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "open-sans-extrabold.ttf"));
        location2TextView.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "open-sans-extrabold.ttf"));
    }



    private void setCountryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (location1TextView.getAlpha() > location2TextView.getAlpha()) {
            visibleText = location1TextView;
            invisibleText = location2TextView;
        } else {
            visibleText = location2TextView;
            invisibleText = location1TextView;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = locationOffset2;
        } else {
            invisibleText.setX(locationOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", locationOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(locationAnimDuration);
        animSet.start();
    }


    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        if (mDatas == null && mDatas.size()<1) {
            return;
        }
        FootPrint footPrint = mDatas.get(pos);
        if (footPrint == null){
            return;
        }
        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        if (mDatas == null || mDatas.size()<1) {
            return;
        }
        FootPrint footPrint = mDatas.get(pos);
        if (footPrint == null){
            return;
        }

        final int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
        final int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        //根据当前用户跟足迹的地址是否在一个城市，显示地址信息
        String address = footPrint.getFootPrintAddress().getCity()+"."+ footPrint.getFootPrintAddress().getDistrict();
        int zoomSize = 10;
        if (mCurrentUserAddress != null &&mCurrentUserAddress.getCity().equals(footPrint.getFootPrintAddress().getCity())){
            address = footPrint.getFootPrintAddress().getDistrict() + "." + footPrint.getFootPrintAddress().getStreet();
            zoomSize = 18;
        }
        setCountryText(address, left2right);


        userNameSwitcher.setInAnimation(FootPrintFragment.this.getContext(), animH[0]);
        userNameSwitcher.setOutAnimation(FootPrintFragment.this.getContext(), animH[1]);

        contentSwitcher.setText(footPrint.getContent());

        //将足迹所在的位置设置到中心点
        LatLng centerLatLng = new LatLng(footPrint.getLatitude(), footPrint.getLongitude());
        setMapCenter(centerLatLng, zoomSize);

        String  userId = footPrint.getUserId();
        UserHelper.getInstance().queryUser("objectId", userId, new UserHelper.UserQueryCallback() {
            @Override
            public void onResult(List<CAUser> users, BmobException e) {
                if (users != null && users.size()>0 ){
                    CAUser caUser = users.get(0);
                    if (caUser != null){
                        userNameSwitcher.setCurrentText(caUser.getUsername());
                        GlideUtil.loadPic(FootPrintFragment.this.getContext(),caUser.getUserPhoto(),mFootPrintUserImg);
                    }
                }
            }
        });

        currentPosition = pos;
    }


    @Override
    public void setPresenter(FootPrintContract.Presenter presenter) {
        if (presenter != null) {
            this.mPresenter = presenter;
        }
    }


    public void initMapView(View view){
        mMapView =  (MapView) view.findViewById(R.id.footprint_mapView);

        //获取核心
        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1); // 去掉百度logo
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mMapView.showZoomControls(true);
        //设置缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        //设置地图覆盖物点击事件
        mBaiduMap.setOnMarkerClickListener(this);

        mLayoutInflater = LayoutInflater.from(this.getActivity().getApplicationContext());

        initLocationClient();

    }

    private List<LatLng> footPrintToLatLong(List<FootPrint> footPrints){

        List<LatLng> pts = null;
        if (footPrints != null && footPrints.size()>0) {
            pts = new ArrayList<>();
            for (int i = 0; i < footPrints.size(); i++) {
                FootPrint footPrint = footPrints.get(i);
                LatLng latLng = new LatLng(footPrint.getLatitude(), footPrint.getLongitude());
                pts.add(latLng);
            }
        }

        return pts;
    }

    @Override
    public void showFootPrintMark(FootPrint footPrint, int position) {
        LatLng point = new LatLng(footPrint.getLatitude(), footPrint.getLongitude());
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
        //View markView = mLayoutInflater.inflate(R.layout.footprint_map_mark, null);
        View markView = mLayoutInflater.inflate(R.layout.footprint_map_label_mark, null);

        TextView labelView = (TextView)markView.findViewById(R.id.footprint_mark_label);
        labelView.setText(FootPrintConstantValue.FOOTPRINT_MARK_LABEL[footPrint.getLabel()]);
        labelView.setTextColor(Color.parseColor(FootPrintConstantValue.FOOTPRINT_MARK_LABEL_COLOR[footPrint.getLabel()]));

        Bitmap bitmap = BitmapUtil.getBitmapFromView(markView);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        options.icon(bitmapDescriptor);

        // 可以给Marker添加附加信息,在点击的时候,实现特定的参数获取;
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        options.extraInfo(bundle);

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
        option.setScanSpan(1000);
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
                myCurrentX = x;
            }
        });
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        double latitude = bdLocation.getLatitude();
        double longitude = bdLocation.getLongitude();
        LatLng point = new LatLng(latitude, longitude);
        mSelfLocation = point;
        if (isFirstIn) {
            new AddressAsyncTask(new AddressAsyncTask.AsyncTaskCallback() {
                @Override
                public void onSuccess(FootPrintAddress address) {
                    mCurrentUserAddress = address;
                }

                @Override
                public void onError(String msg) {

                }
            }).execute(mSelfLocation);

        }

        // TODO: 添加/移动 自己位置Marker
//        if(mSelfMarker == null){
//            MarkerOptions options = new MarkerOptions();
//            options.position(point);
//            BitmapDescriptor descriptor =
//                    BitmapDescriptorFactory.fromAsset("Icon_start.png");
//            options.icon(descriptor);
//
//
//
//            mSelfMarker = (Marker) mBaiduMap.addOverlay(options);
//        }
        // 如果已经创建过 Marker,那么更新位置
//        mSelfMarker.setPosition(point);
        setSelfLocation(bdLocation);

    }

    private void setSelfLocation(BDLocation bdLocation){
        //BDLocation 回调的百度坐标类，内部封装了如经纬度、半径等属性信息
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

        if (isFirstIn) {

            //配置定位图层显示方式,三个参数的构造器
            /*
            * 1.定位图层显示模式
            * 2.是否允许显示方向信息
            * 3.用户自定义定位图标
            *
            * */
            BitmapDescriptor myBitmapLocation = BitmapDescriptorFactory.fromResource(R.mipmap.calibration_arrow);
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,myBitmapLocation);
            //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationConfigeration(configuration);

            //地理坐标基本数据结构
            LatLng latLng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
            MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
            //改变地图状态
            mBaiduMap.setMapStatus(msu);
            isFirstIn=false;
            Toast.makeText(this.getContext(), bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 设置地图中心点
     */
    private void setMapCenter(LatLng latLng, int zoomSize) {

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(zoomSize)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);


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
    public boolean onMarkerClick(Marker marker) {

        Bundle bundle = marker.getExtraInfo();
        int position = bundle.getInt("position", 0);
        Toast.makeText(this.getContext(), "position:" + position, Toast.LENGTH_SHORT).show();

        layoutManger.scrollToPosition(position);
        //layoutManger.smoothScrollToPosition(recyclerView, new RecyclerView.State(), position);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_share_footprint_btn:
                ShareFootPrintPopupWin shareFootPrintPW = new ShareFootPrintPopupWin(this.getContext());
                shareFootPrintPW.setSelectImageCount(FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT);
                shareFootPrintPW.showAtLocation(mShareFootPrintBtn, Gravity.CENTER, 0, 0);
                break;
            case R.id.img_footprint_current_user:
                Toast.makeText(FootPrintFragment.this.getContext(), "点击当前用户头像", Toast.LENGTH_SHORT).show();
                mainActivity.drawer.openDrawer(GravityCompat.START);
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

            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);

                Toast.makeText(FootPrintFragment.this.getContext(), "onActiveCardChange(clickedPosition)", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private class TextViewFactory implements  ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(FootPrintFragment.this.getContext());

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(FootPrintFragment.this.getContext(), styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }

    private class ImageViewFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            final ImageView imageView = new ImageView(FootPrintFragment.this.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            final ViewGroup.LayoutParams lp = new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);

            return imageView;
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
        isFirstIn = true;
        //在onPause 取消定位操作
        mLocationClient.unRegisterLocationListener(this);
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        mMapView.onDestroy();
        mSelfMarker = null;
        super.onDestroyView();
    }
}
