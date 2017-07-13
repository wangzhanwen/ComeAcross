package com.lyy_wzw.comeacross.addressbook;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.Friends;
import com.lyy_wzw.comeacross.rong.server.pinyin.PinyinComparator;
import com.lyy_wzw.comeacross.ui.UserdetailActivity;
import com.lyy_wzw.comeacross.user.fragments.LoginFragment;
import com.lyy_wzw.comeacross.user.rongyun.messages.ContactNtfMessage;
import com.lyy_wzw.comeacross.user.rongyun.models.CodeSuccessResult;
import com.lyy_wzw.comeacross.user.task.SendCmdNtfMsgAsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import io.rong.imkit.mention.SideBar;
import io.rong.imkit.tools.CharacterParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressBookFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "DEBUG";
    private static MainActivity mainActivity;
    private Button button;
    private final int CLICK_ADDRESS_FRAGMENT_FRIEND = 2;
    private SideBar mSideBar;
    private TextView mDialogTextView;
    private View mHeadView;
    private TextView mUnreadTextView;
    private ListView mListView;
    private FriendListAdapter mFriendListAdapter;
    private List<Friend> mFriendList;
    private CharacterParser mCharacter;

    private TextView mTvDialogSidebar;
    private PinyinComparator mPinyinComparator;
    private Button button_test;
    private EditText edit_search_phone;
    private String search_phone;
    private String targetUid;
    private String sourceId;


    public AddressBookFragment() {

    }

    public static AddressBookFragment instance(MainActivity activity){
         mainActivity = activity;
        return new AddressBookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_book, container, false);
        initView(view);
       // initData();
        //updateUI();
        return view;
    }

    private void updateUI() {
        mSideBar.setVisibility(View.VISIBLE);
        UserInfoManager userInfoManager = new UserInfoManager(getContext());
        userInfoManager.getFriends(new UserInfoManager.ResultCallback<List<Friend>>() {
            @Override
            public void onSuccess(final List<Friend> friends){
                mFriendList .addAll(friends);
                if (mFriendList != null && mPinyinComparator != null) {
                    Collections.sort(mFriendList,mPinyinComparator);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //直接在这里调用notifyDataSetChanged不会刷新,或者改成mFriendList.addAll(friends);
                        mFriendListAdapter.updateListView(mFriendList);
                    }
                });

            }

            @Override
            public void onError(String errString) {

            }
        });

    }

    private void initData() {
        mFriendList = new ArrayList<>();
        mFriendListAdapter = new FriendListAdapter(getActivity(),mFriendList);
        mListView.setAdapter(mFriendListAdapter);
        mPinyinComparator = PinyinComparator.getInstance();

    }

    private void initView(View view) {

        mListView = ((ListView) view.findViewById(R.id.listview));
        mTvDialogSidebar = ((TextView) view.findViewById(R.id.tv_dialog_sidebar));
        mSideBar = ((SideBar) view.findViewById(R.id.sidebar_address));
        mSideBar.setTextView(mTvDialogSidebar);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mHeadView = inflater.inflate(R.layout.item_addresss_list_header,null);
        mUnreadTextView = ((TextView) mHeadView.findViewById(R.id.tv_unread));
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        RelativeLayout groupLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_chatroom);
        RelativeLayout publicServiceLayout = (RelativeLayout) mHeadView.findViewById(R.id.publicservice);
        RelativeLayout selfLayout = (RelativeLayout) mHeadView.findViewById(R.id.contact_me_item);

        mListView.addHeaderView(mHeadView);

        newFriendsLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
        publicServiceLayout.setOnClickListener(this);
        selfLayout.setOnClickListener(this);

        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int position, long l) {

                if (mListView.getHeaderViewsCount() >0) {
                    startFriendDetailsPage(mFriendList.get(position - 1));
                }else{
                    startFriendDetailsPage(mFriendList.get(position));
                }
            }
        });

        button_test = ((Button) view.findViewById(R.id.test));
        button_test.setOnClickListener(this);
        edit_search_phone = ((EditText) view.findViewById(R.id.edit_search_phone));


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.test:

                sourceId = LoginFragment.getCurrentUser().getObjectId();
                String phoneNum = edit_search_phone.getText().toString().trim();
                BmobQuery<CAUser> query = new BmobQuery<>();
                query.addWhereEqualTo("mobilePhoneNumber",phoneNum);
                query.findObjects(new FindListener<CAUser>() {


                    @Override
                    public void done(List<CAUser> list, BmobException e) {
                        if (list != null && list.size() != 0) {
                            targetUid = list.get(0).getObjectId();
                            Log.e(TAG, "done: "+sourceId );
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });

                break;
            default:
                break;
        }

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){


                new SendCmdNtfMsgAsyncTask(new SendCmdNtfMsgAsyncTask.SendCmdNtfMsgCallback() {
                    @Override
                    public void onSuccess(CodeSuccessResult result) {

                        Log.e(TAG, "onSuccess: 发送消息成功");
                        //添加到好友数据库
                        Friends friends = new Friends(sourceId,targetUid,false);

                        friends.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {

                                if (e == null) {
                                    Toast.makeText(getContext(),"发送请求成功,并添加到数据库",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(),"发送请求成功,添加到数据库失败"+e,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(String errorMsg) {

                    }
                }).execute(
                        sourceId,
                        targetUid,
                        new ContactNtfMessage("Request",sourceId,"070a0251ef","请求添加你为好友",""),
                        "content",
                        "添加好友请求",
                        0,
                        0);
            }
        }
    };

    public void startFriendDetailsPage(Friend friend){
        Intent intent = new Intent(getActivity(), UserdetailActivity.class);
        intent.putExtra("type",CLICK_ADDRESS_FRAGMENT_FRIEND);
        intent.putExtra("frient",friend);
        startActivity(intent);
    }

    public void fileterData(String filteter){
        List<Friend> fileterDataList = new ArrayList<>();
        if (TextUtils.isEmpty(filteter)) {
            fileterDataList = mFriendList;
        }

    }

}
