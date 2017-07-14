package com.lyy_wzw.comeacross.homecommon;

import android.app.Application;
import android.content.Context;

import android.util.Log;
import android.view.View;
import com.baidu.mapapi.SDKInitializer;
import com.lyy_wzw.comeacross.R;

import cn.bmob.v3.Bmob;
import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;


/**
 * Created by yidong9 on 17/5/11.
 */

public class InitApplication  extends Application implements RongIM.ConversationListBehaviorListener ,RongIMClient.OnReceiveMessageListener{
    public static String APPID = "575d92ce0454363528535cf901fb9d06";
    private String TAG = "InitApplication";
    private static DisplayImageOptions options;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //融云初始化
        RongIM.init(this);
        //初始化bmob
        Bmob.initialize(this, APPID);

        //初始化ImageLoader
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher_round)
                .showImageOnFail(R.drawable.ic_launcher_round)
                .showImageOnLoading(R.drawable.ic_launcher_round)
                .displayer(new FadeInBitmapDisplayer(300))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        /**
         * 融云sdk事件监听处理
         * 注册相关代码,只需要在主进程里做
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ) {
          /*  RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
            RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
            RongIM.registerMessageType(TestMessage.class);
            RongIM.registerMessageTemplate(new TestMessageProvider());*/
        }
        //接收消息监听
        setOnReceiveMessageListener();



    }
    /**
     * 设置接收消息的监听器。
     * <p/>
     * 所有接收到的消息、通知、状态都经由此处设置的监听器处理。包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
     *
     *
     */
    private void setOnReceiveMessageListener() {

        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                //收到消息的处理 true表示自己处理铃声和后台通知,false走融云默认处理方式
                Log.e(TAG, "onReceived: 收到消息"+message.toString() );
                MessageContent content = message.getContent();
                if (content instanceof ContactNotificationMessage) {
                    Log.e(TAG, "onReceived: 是好友消息" );
                    ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) content;
                    if (contactNotificationMessage.getOperation().equals("Request")) {

                    }else if (contactNotificationMessage.getOperation().equals("AcceptResponse")){
                        //对方同意我的好友请求
                    }

                    //对方发来的好友消息

                }

                return false;
            }
        });
    }

    public static DisplayImageOptions getOptions(){
        return options;
    }

    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType type, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType type, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation conversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation conversation) {


        return false;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        Log.e(TAG, "onConversationClick: 得到一条消息" );
        return false;
    }

    public static Context getContext(){
        return mContext;
    }
}
