package com.lyy_wzw.comeacross.addressbook;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.lyy_wzw.comeacross.rong.server.WeixinAction;
import com.lyy_wzw.comeacross.rong.server.network.http.HttpException;
import com.lyy_wzw.comeacross.rong.server.response.UserRelationshipResponse;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.tools.CharacterParser;

/**
 * Created by 27459 on 2017/6/24.
 */

public class UserInfoManager {
    private WeixinAction mAction;
    private String TAG = "DEBUG";
    static Handler mHandler;
    private Handler mWorkHandler;
    private HandlerThread mWorkThread;
    private static UserInfoManager instance;
    private List<Friend> friendsList;

    private static Context mContext;
    public UserInfoManager(Context context) {
        mAction = new WeixinAction(context);
        mHandler = new Handler(Looper.getMainLooper());
        mWorkThread = new HandlerThread("UserInfoManager");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper());
    }



    public void getFriends(final ResultCallback<List<Friend>> callback){
        //子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        friendsList = pullFriends();
                        Log.e(TAG, "run: "+friendsList.size() );

                    } catch (HttpException e) {
                        if (callback != null) {
                            callback.onFail(null);
                        }
                        e.printStackTrace();
                        return;
                    }
                    if (friendsList != null && callback != null) {
                        callback.onCallback(friendsList);
                    }
            }
        }).start();

    }



    public boolean isNetWorkConnected(){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
    private List<Friend> pullFriends() throws HttpException{
        List<Friend> friendList = new ArrayList<>();
        UserRelationshipResponse relationshipResponse = mAction.getAllUserRelationship();
        if (relationshipResponse != null && relationshipResponse.getCode() ==200) {
            List<UserRelationshipResponse.ResultEntity> list = relationshipResponse.getResult();
            if (list != null && list.size()>0) {
                friendList.clear();
                //TODO添加到数据库
                for (UserRelationshipResponse.ResultEntity resultEntity: list
                     ) {
                    if (resultEntity.getStatus() == 20) {
                        Friend friend = new Friend(
                                resultEntity.getUser().getId(),
                                resultEntity.getUser().getNickname(),
                                Uri.parse(resultEntity.getUser().getPortraitUri()),
                                resultEntity.getDisplayName(),
                                null,
                                null,null,null,
                                CharacterParser.getInstance().getSelling(resultEntity.getUser().getNickname()),
                                CharacterParser.getInstance().getSelling(resultEntity.getDisplayName())
                        );
                        friendList.add(friend);
                    }
                }

            }
        }

        return friendList;
    }

    public static abstract class ResultCallback<T> {
        public static class Result<T>{
            public T t;
        }
        public ResultCallback(){

        }

        /**
         * 成功时回调
         * @param t
         */
        public abstract void onSuccess(T t);

        /**
         * 失败时回调
         * @param errString
         */
        public abstract void onError(String errString);

        //异步
        public void onFail(final String errString){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onError(errString);
                }
            });
        }

        //异步
        public void onCallback(final T t){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(t);
                }
            });
        }

    }

}
