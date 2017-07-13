package com.lyy_wzw.comeacross.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.CAUser;
import com.lyy_wzw.comeacross.bean.Friends;
import com.lyy_wzw.comeacross.homecommon.InitApplication;
import com.lyy_wzw.comeacross.rong.server.utils.AMUtils;
import com.lyy_wzw.comeacross.rong.server.utils.NToast;
import com.lyy_wzw.comeacross.rong.server.widget.DialogWithYesOrNoUtils;
import com.lyy_wzw.comeacross.rong.server.widget.SelectableRoundedImageView;
import com.lyy_wzw.comeacross.user.fragments.LoginFragment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ContactNotificationMessage;


public class SearchFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_phone_search;
    private LinearLayout mLl_result_search;
    private String mFriendUid;
    private String TAG = "SearchFriendActivity";
    private SelectableRoundedImageView mIv_avatar_search;
    private TextView mTvNameSearch;
    private String mCurrentUserId;
    private boolean isFriend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        initView();

    }

    private void initView() {
        mLl_result_search = (LinearLayout) findViewById(R.id.ll_result_search);
        et_phone_search = (EditText) findViewById(R.id.et_phone_search);
        mIv_avatar_search = (SelectableRoundedImageView) findViewById(R.id.iv_avatar_searchfriend);
        mTvNameSearch = (TextView) findViewById(R.id.tv_name_searchfriend);
        et_phone_search.addTextChangedListener(new TextWatcher() {
            private String mPhone;

            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 11) {
                    mPhone = s.toString().trim();
                    if (!AMUtils.isMobile(mPhone)) {
                        NToast.shortToast(SearchFriendActivity.this,"非法手机号");

                        //在用户列表查询该用户
                        queryUidWithPhone(mPhone);
                        //同时还要查询是否是好友关系





                        return;
                    }

                }else{
                    mLl_result_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void queryIsFriend() {
        //当前用户的uid
        mCurrentUserId = LoginFragment.getCurrentUser().getObjectId();
        //根据当前用户id查询表中u1id 或者u2id符合条件的值  得到当前
    }

    private void queryUidWithPhone(String phone) {

        BmobQuery<CAUser> bmobQuery = new BmobQuery<CAUser>();
        bmobQuery.addWhereEqualTo("mobilePhoneNumber",phone);
        bmobQuery.findObjects(new FindListener<CAUser>() {
            @Override
            public void done(List<CAUser> list, BmobException e) {
                if (e == null) {
                    //得到好友信息并展示
                    mFriendUid = list.get(0).getObjectId();
                    ImageLoader.getInstance().displayImage(list.get(0).getUserPhoto(),mIv_avatar_search, InitApplication.getOptions());
                    mTvNameSearch.setText(list.get(0).getUsername());
                    //查询是否是好友
                    isFriendorself(mFriendUid);

                    //设置监听
                    mLl_result_search.setVisibility(View.VISIBLE);
                    mLl_result_search.setOnClickListener(SearchFriendActivity.this);


                }else{
                    //查询出错
                    Log.e(TAG, "done: 查询bmob云端账户信息出错"+e.toString());
                }
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_result_search:
                if (isFriend){
                    //打开用户详情界面
                    Intent intent = new Intent(SearchFriendActivity.this,UserdetailActivity.class);
                    startActivity(intent);
                }else{

                    DialogWithYesOrNoUtils.getInstance().showEditDialog(SearchFriendActivity.this, "添加好友信息...", "添加好友", new DialogWithYesOrNoUtils.DialogCallBack() {
                        @Override
                        public void executeEvent() {

                        }

                        @Override
                        public void executeEditEvent(String editText) {

                            ContactNotificationMessage contactNotificationMessage = ContactNotificationMessage.obtain("Requeset",mCurrentUserId,mFriendUid,editText);
                            Message msg = Message.obtain(mFriendUid, Conversation.ConversationType.PRIVATE,contactNotificationMessage);
                            RongIM.getInstance().getInstance().sendMessage(msg, null, null, new IRongCallback.ISendMediaMessageCallback() {
                                @Override
                                public void onProgress(Message message, int i) {

                                }

                                @Override
                                public void onCanceled(Message message) {

                                }

                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {

                                    Log.e(TAG, "onSuccess: 发送好友请求成功" );
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode code) {

                                }
                            });

                        }

                        @Override
                        public void updatePassword(String oldPassword, String newPassword) {

                        }
                    });
                }


                break;
        }
    }

    private boolean isFriendorself(final String frienduId) {
        if (frienduId.equals(mCurrentUserId)){
            //是自己
            return true;
        }
        //得到uid之后查询是否是好友
        BmobQuery<Friends> bmobQueryFriend = new BmobQuery<Friends>();
        bmobQueryFriend.addWhereEqualTo("myId",mCurrentUserId);
        bmobQueryFriend.findObjects(new FindListener<Friends>() {
            @Override
            public void done(List<Friends> list, BmobException e) {
                for (Friends friend:list
                        ) {
                    if (friend.getObjectId().equals(frienduId)) {
                        //异步的不能直接使用  是好友
                        //设置监听
                        isFriend = true;
                        return;
                    }
                }
                //不是好友判断是否是自己
                if (frienduId.equals(mCurrentUserId)){
                    //是自己
                    isFriend = true;
                    return;
                }
                //需要添加好友
                isFriend = false;

                mLl_result_search.setVisibility(View.VISIBLE);
                mLl_result_search.setOnClickListener(SearchFriendActivity.this);

            }
        });
        return isFriend;
    }
}
