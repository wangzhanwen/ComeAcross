package com.lyy_wzw.comeacross.user.task;

import android.os.AsyncTask;
import android.util.Log;

import com.lyy_wzw.comeacross.user.UserConstantValue;
import com.lyy_wzw.comeacross.user.rongyun.messages.ContactNtfMessage;
import com.lyy_wzw.comeacross.user.rongyun.methods.Message;
import com.lyy_wzw.comeacross.user.rongyun.models.CodeSuccessResult;


/**
 * Created by yidong9 on 17/6/27.
 */

public class SendCmdNtfMsgAsyncTask extends AsyncTask<Object, Integer, CodeSuccessResult>{
    private static  final  String  TAG = "RequestTokenAsyncTask";
    private SendCmdNtfMsgCallback callback;
    private CodeSuccessResult codeResult = null;


    public SendCmdNtfMsgAsyncTask(SendCmdNtfMsgCallback mCalback){
        this.callback = mCalback;
    }

    @Override
    protected CodeSuccessResult doInBackground(Object... params) {
        //String fromUserId, String[] toUserId, BaseMessage message, String pushContent, String pushData, Integer isPersisted, Integer isCounted
        String fromUserId  = (String) params[0];
        String  toUserId = (String) params[1];
        ContactNtfMessage msg = (ContactNtfMessage) params[2];
        String pushContent = (String) params[3];
        String pushData = (String) params[4];
        Integer isPersisted = (Integer) params[5];
        Integer isCounted = (Integer) params[6];
        Log.d(TAG, "fromUserId: " + fromUserId);
        Log.d(TAG, "toUserId: " + toUserId);
        Log.d(TAG, "msg: " + msg);
        Log.d(TAG, "pushContent: " + pushContent);
        Log.d(TAG, "pushData: " + pushData);
        Log.d(TAG, "isPersisted: " + isPersisted);
        Log.d(TAG, "isCounted: " + isCounted);

        Message message = new Message(UserConstantValue.APP_KEY, UserConstantValue.APP_SECRET);

        try {
            CodeSuccessResult result = message.publishSystem(fromUserId, toUserId, msg, pushContent, pushData, isPersisted, isCounted);
            if (result != null && result.getCode() == 200){
                codeResult = result;
                Log.d(TAG, "doInBackground: "+result.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return codeResult;
    }

    @Override
    protected void onPostExecute(CodeSuccessResult result) {
        if( result!=null){
            //空
            callback.onSuccess(result);
        }else{
            callback.onError("发送CmdNtfMessage消息失败");
        }

    }

    public interface SendCmdNtfMsgCallback{
        void onSuccess(CodeSuccessResult result);
        void onError(String errorMsg);
    }




}
