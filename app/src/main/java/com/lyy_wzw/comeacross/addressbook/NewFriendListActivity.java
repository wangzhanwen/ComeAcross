package com.lyy_wzw.comeacross.addressbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.Friends;
import com.lyy_wzw.comeacross.user.fragments.LoginFragment;
import com.lyy_wzw.comeacross.utils.CommonAdapter;
import com.lyy_wzw.comeacross.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.lyy_wzw.comeacross.R.layout.item_new_friend_list;

public class NewFriendListActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "[NewFriendListActivity]";
    private ListView mLvNewFriend;
    private List<Friends> mList = new ArrayList<>();
    private Button mbtn_to_agree;
    private TextView mtv_isagreed;
    private CommonAdapter<Friends> mAdapter;
    private String curUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend_list);
        initView();
        initData();
    }

    private void initData() {
        mList = new ArrayList<>();
        //在数据库中的到好友关系
        String uid = LoginFragment.getCurrentUser().getObjectId();
        /*
        复合查询
         */
        BmobQuery<Friends> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("inviteId", uid);

        BmobQuery<Friends> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("invitedId",uid);
        List<BmobQuery<Friends>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<Friends> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.findObjects(new FindListener<Friends>() {
            @Override
            public void done(List<Friends> list, BmobException e) {
                Log.e(TAG, "done: "+list.size() );
                if (list != null) {
                    mAdapter = new CommonAdapter<Friends>(getApplicationContext(), list, item_new_friend_list) {
                        @Override
                        public void convert(ViewHolder viewHolder, Friends item) {
                            Log.e(TAG, "convertView: ");
                            String inviteId = item.getInviteId();
                            if (LoginFragment.getCurrentUser().getObjectId() == inviteId && !item.isAgree()) {
                                //邀请者,并且没同意呢

                                return;
                            }
                            if (curUid.equals(inviteId)) {
                                //邀请者
                                Log.e(TAG, "convertView: 设置数据");
                                viewHolder.setText(R.id.tv_name_friend, item.getInvitedId());
                            } else {
                                viewHolder.setText(R.id.tv_name_friend, item.getInviteId());
                            }

                            if (item.isAgree()) {
                                mtv_isagreed = viewHolder.getView(R.id.text_already_agree);
                                mtv_isagreed.setVisibility(View.VISIBLE);
                                mbtn_to_agree = viewHolder.getView(R.id.btn_agree_invite);
                                mbtn_to_agree.setVisibility(View.GONE);
                            } else {
                                mtv_isagreed = viewHolder.getView(R.id.text_already_agree);
                                mtv_isagreed.setVisibility(View.GONE);
                                mbtn_to_agree = viewHolder.getView(R.id.btn_agree_invite);
                                mbtn_to_agree.setVisibility(View.VISIBLE);
                            }
                        }
                    };
                    mLvNewFriend.setAdapter(mAdapter);

                }
            }
        });


    }

    private void initView() {
        mLvNewFriend = (ListView) findViewById(R.id.lv_newFriend);
        curUid = LoginFragment.getCurrentUser().getObjectId();




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_agree_invite:
                //同意好友申请

                break;

        }
    }
}
