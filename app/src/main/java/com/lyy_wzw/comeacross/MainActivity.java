package com.lyy_wzw.comeacross;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.addressbook.AddressBookFragment;
import com.lyy_wzw.comeacross.chat.ChatFragment;
import com.lyy_wzw.comeacross.discovery.DiscoveryFragment;
import com.lyy_wzw.comeacross.footprint.FootPrintFragment;
import com.lyy_wzw.comeacross.footprint.FootPrintPresenter;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.homecommon.FragmentAdapter;
import com.lyy_wzw.imageselector.entry.Image;
import com.lyy_wzw.imageselector.utils.ImageSelectorUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 0x00000011;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    public Toolbar toolbar;



    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private FootPrintFragment mFootPrintFragment;
    private ChatFragment mChatFragment;
    private DiscoveryFragment mDiscoveryFragment;
    private AddressBookFragment mAddressBookFragment;
    private ImageView mShareFootPrintBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


    }

    private void initViews() {
        //侧滑栏处理
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mShareFootPrintBtn = (ImageView)findViewById(R.id.main_share_footprint_btn);
        mShareFootPrintBtn.setOnClickListener(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //底部导航栏处理
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bnv_menu);

        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_footprint:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_chat:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_discovery:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_address_book:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        } );

        initViewPager();

        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        viewPager.setOnPageChangeListener(this);

    }


    private void initViewPager() {

        //构造适配器
        List<Fragment> fragments=new ArrayList<Fragment>();
        mFootPrintFragment = FootPrintFragment.instance(this);
        new FootPrintPresenter(this, mFootPrintFragment);
        mChatFragment = ChatFragment.instance(this);
        mDiscoveryFragment = DiscoveryFragment.instance(this);
        mAddressBookFragment = AddressBookFragment.instance(this);

        fragments.add(mFootPrintFragment);
        fragments.add(mChatFragment);
        fragments.add(mDiscoveryFragment);
        fragments.add(mAddressBookFragment);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //关闭抽屉
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 将当前的页面对应的底部标签设为选中状态
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {
            //ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            //Toast.makeText(this, images.toString(), Toast.LENGTH_SHORT).show();
            mFootPrintFragment.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_share_footprint_btn:
                Toast.makeText(this, "分享足迹", Toast.LENGTH_SHORT).show();

                ShareFootPrintPopupWin shareFootPrintPW = new ShareFootPrintPopupWin(this);
                shareFootPrintPW.showAtLocation(mShareFootPrintBtn, Gravity.CENTER, 0, 0);
                break;
        }
    }
}
