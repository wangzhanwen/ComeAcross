package com.lyy_wzw.comeacross;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.addressbook.AddressBookFragment;
import com.lyy_wzw.comeacross.chat.ChatFragment;
import com.lyy_wzw.comeacross.discovery.DiscoveryFragment;
import com.lyy_wzw.comeacross.footprint.FootPrintFragment;
import com.lyy_wzw.comeacross.footprint.FootPrintPresenter;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;
import com.lyy_wzw.comeacross.footprint.ui.ShareFootPrintPopupWin;
import com.lyy_wzw.comeacross.homecommon.FragmentAdapter;
import com.lyy_wzw.comeacross.ui.AddfriendActivity;
import com.lyy_wzw.comeacross.user.activitys.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TAG = "connect";
    private static final int REQUEST_CODE = 0x00000011;
    private String rongToken = "TngWAHxIqJJlaoJvTfzivQvcUh0C0/rQgnE+7tbMgRXj9+prH8rB9xk/iFRvSzj8yf0GYps9IISSH3Rl/eEGkzMP1Dk3o7xV";

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
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationTypes = null;
    private long mSystemTime = 0;
    private boolean isMenuShow = false;
    private ImageView img_search_top;
    private ImageView img_add_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        connectRongServer(rongToken);
    }


    private void connectRongServer(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e(TAG, "onTokenIncorrect: token错误");
                Toast.makeText(MainActivity.this,"连接融云服务器失败.token错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String uid) {
                Toast.makeText(MainActivity.this,uid+"连接服务器成功",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onSuccess:uid"+uid );

            }

            @Override
            public void onError(RongIMClient.ErrorCode code) {
                Log.e(TAG, "onError: 连接服务器失败" );
                Toast.makeText(MainActivity.this,"连接融云服务器失败."+code,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initViews() {
        //侧滑栏处理
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mShareFootPrintBtn = (ImageView)findViewById(R.id.main_share_footprint_btn);
        mShareFootPrintBtn.setOnClickListener(this);
        img_search_top = (ImageView) findViewById(R.id.img_search_top);
        img_add_top = (ImageView) findViewById(R.id.img_add_top);
        img_search_top.setOnClickListener(this);
        img_add_top.setOnClickListener(this);

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
                        img_add_top.setVisibility(View.GONE);
                        img_search_top.setVisibility(View.GONE);
                        mShareFootPrintBtn.setVisibility(View.VISIBLE);
                        break;
                    case R.id.action_chat:
                        img_add_top.setVisibility(View.VISIBLE);
                        img_search_top.setVisibility(View.VISIBLE);
                        mShareFootPrintBtn.setVisibility(View.GONE);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_discovery:
                        img_add_top.setVisibility(View.VISIBLE);
                        img_search_top.setVisibility(View.VISIBLE);
                        mShareFootPrintBtn.setVisibility(View.GONE);
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_address_book:
                        img_add_top.setVisibility(View.VISIBLE);
                        img_search_top.setVisibility(View.VISIBLE);
                        mShareFootPrintBtn.setVisibility(View.GONE);
                        viewPager.setCurrentItem(3);
                        break;
                }
                invalidateOptionsMenu();
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
        //mChatFragment = ChatFragment.instance(this);
        mDiscoveryFragment = DiscoveryFragment.instance(this);
        mAddressBookFragment = AddressBookFragment.instance(this);

        fragments.add(mFootPrintFragment);
       // fragments.add(mChatFragment);
        Fragment conversationList = initConversationList();

        fragments.add(conversationList);
        fragments.add(mDiscoveryFragment);
        fragments.add(mAddressBookFragment);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);

    }

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapter(MainActivity.this));
            Uri uri = Uri.parse("rong://"+getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")//设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(),"true")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(),"false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(),"true")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(),"true")//系统
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(),"true")//
                    .build();
            mConversationTypes = new Conversation.ConversationType[]{
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.DISCUSSION
            };
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        }
        return mConversationListFragment;
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
                shareFootPrintPW.setSelectImageCount(FootPrintConstantValue.SHARE_IMAGE_MAX_COUNT);
                shareFootPrintPW.showAtLocation(mShareFootPrintBtn, Gravity.CENTER, 0, 0);
                break;
            case R.id.img_add_top:
                Intent intent = new Intent(this, AddfriendActivity.class);
                startActivity(intent);
                break;
            case R.id.img_search_top:
                Toast.makeText(MainActivity.this,"查询",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()- mSystemTime < 1000 && mSystemTime != 0){
                Intent intentLogin = new Intent(this, LoginActivity.class);
                intentLogin.setAction("ExitApp");
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intentLogin);
            }else{
                mSystemTime = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "双击返回按钮退出应用", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }


}
