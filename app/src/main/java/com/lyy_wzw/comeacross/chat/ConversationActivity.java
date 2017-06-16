package com.lyy_wzw.comeacross.chat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.lyy_wzw.comeacross.R;

public class ConversationActivity extends FragmentActivity {

    private TextView tvFriendName;
    private String sId;
    private String sName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
/**
 * 1.上下文
 * 2.与之聊天的用户的ID
 * 3.聊天的标题.开发者需要在聊天界面通过intent.getData().getQueryparameter(title);
 */
        tvFriendName = (TextView) findViewById(R.id.tvFriendName);
        sId = getIntent().getData().getQueryParameter("targetId");//获取id
        sName = getIntent().getData().getQueryParameter("title");//获取昵称
        if (!TextUtils.isEmpty(sName)) {
            tvFriendName.setText(sName);
        }else{
            //TODO 拿到id去请求服务端
        }
    }
}
