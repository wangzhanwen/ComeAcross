package com.lyy_wzw.comeacross.user.task;

import android.os.AsyncTask;
import android.util.Log;


import com.lyy_wzw.comeacross.user.UserConstantValue;
import com.lyy_wzw.comeacross.user.rongyun.methods.User;
import com.lyy_wzw.comeacross.user.rongyun.models.TokenResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by yidong9 on 17/6/27.
 */

public class RequestTokenAsyncTask extends AsyncTask<String, Integer, TokenResult>{
    private static  final  String  TAG = "RequestTokenAsyncTask";
    private GetTokenCallback mGetTokenCallback;


    public RequestTokenAsyncTask(GetTokenCallback getTokenCallback){
        mGetTokenCallback = getTokenCallback;
    }

    @Override
    protected TokenResult doInBackground(String... params) {
        TokenResult ret = null ;
        String uid  = params[0];
        String name = params[1];
        String portraitUri = params[2];
        Log.d(TAG, "uid: " + uid);
        Log.d(TAG, "name: " + name);
        Log.d(TAG, "portraitUri: " + portraitUri);

        User ryUserHelper = new User(UserConstantValue.APP_KEY, UserConstantValue.APP_SECRET);

        try {
            TokenResult tokenResult = ryUserHelper.getToken(uid,
                    name,
                    portraitUri);
            if (tokenResult!= null && tokenResult.getCode() == 200){
                Log.d(TAG, "TokenResult: " + tokenResult.toString());
                ret = tokenResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    protected void onPostExecute(TokenResult result) {
        if( result!=null){
            mGetTokenCallback.onSuccess(result);
        }else{
            mGetTokenCallback.onError("token获取失败");
        }

    }

    public interface GetTokenCallback{
        void onSuccess(TokenResult result);
        void onError(String errorMsg);
    }


    public static String getSha1(String str) {
        String ret = null ;
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            ret =  new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

       return ret;
    }


    /**
     * 将一个输入流转换成指定编码的字符串
     *
     * @param inputStream
     * @param encode
     * @return
     */
    private static String inputStreamToStr(InputStream inputStream, String encode) {

        // 内存流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = null;
        try {
            if (inputStream != null) {

                while ((len = inputStream.read(data)) != -1) {
                    byteArrayOutputStream.write(data, 0, len);
                }
                result = new String(byteArrayOutputStream.toByteArray(), encode);
                inputStream.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

}
