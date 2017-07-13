package com.lyy_wzw.comeacross.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.addressbook.NewFriendListActivity;

public class ConversationActivity extends FragmentActivity {

    private static final String TAG = "ConversationActivity";
    private TextView tvFriendName;
    private String sId;
    private String sName;
    private String targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Intent intent = getIntent();
        //rong://com.lyy_wzw.comeacross/conversation/system?targetId=070a0251ef&title=%E5%88%98id:070a0251ef
        //得到"system"
        String type = intent.getData().getLastPathSegment();

        Log.e(TAG, "onCreate: ConversationActivity"+intent.getData().toString());
        if (intent == null || intent.getData() == null) {
            return;
        }
        Log.e(TAG, "onCreate: "+intent.getData().toString() );
        targetId = intent.getData().getQueryParameter("targetId");
        //进入添加好友界面,不进入聊天界面
        if (intent != null && type.equals("system")) {
            startActivity(new Intent(this, NewFriendListActivity.class));
            return;
        }



    }
}
