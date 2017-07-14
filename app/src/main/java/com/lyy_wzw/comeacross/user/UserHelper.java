package com.lyy_wzw.comeacross.user;


import com.lyy_wzw.comeacross.bean.CAUser;


import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wzw on 17/6/19.
 *
 */

public class UserHelper {
    private CompositeSubscription mCompositeSubscription;
    private static UserHelper instance;
    private UserHelper(){}
    public static synchronized UserHelper getInstance(){
        if (instance == null) {
            instance = new UserHelper();
        }
        return instance;
    }



    public interface UserCallback{
        void onSuccess(CAUser user);
        void onError(BmobException e);
    }

    public interface UserQueryCallback{
        void onResult(List<CAUser> object, BmobException e);

    }




    /**
     * 获得本地缓存的当前用户
     * @return void
     * @exception
     */
    public CAUser getCurrentUser(){
       return BmobUser.getCurrentUser(CAUser.class);
    }



    /**
     *
     *  修改密码（手机号 + 验证码）
     *
     * @return void
     * @exception
     */
    public void changePsw(String smsCode, String newPsw, final UserCallback userCallback){
        BmobUser.resetPasswordBySMSCode(smsCode, newPsw, new UpdateListener() {

            @Override
            public void done(BmobException ex) {
                if(ex==null){
                    userCallback.onSuccess(null);
                }else{
                    userCallback.onError(ex);
                }
            }
        });
    }


    /**
     * 请求短信验证码
     * @method requestSmsCode
     * @return void
     * @exception
     */
    public void requestSmsCode(String phoneNumber, final UserCallback userCallback){
        BmobSMS.requestSMSCode(phoneNumber, "signSMS",new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if(ex==null){
                    userCallback.onSuccess(null);
                }else{
                    userCallback.onError(ex);
                }
            }
        });
    }


    /**
     * 用户登录（手机号 + 密码  ）
     *
     * @return void
     * @exception
     */
    public  void login(String phoneNumber, String psw, final UserCallback userCallback){
        BmobUser.loginByAccount(phoneNumber, psw, new LogInListener<CAUser>() {

            @Override
            public void done(CAUser user, BmobException e) {
                if(user!=null){
                    userCallback.onSuccess(user);
                }else{
                    userCallback.onError(e);
                }
            }
        });
    }

    /**
     * 用户注册（手机号 + 验证码  ）
     *
     * @return void
     * @exception
     */
    public  void register(CAUser user, String smsCode, final UserCallback userCallback){
        addSubscription(user.signOrLogin(smsCode, new SaveListener<CAUser>() {
            @Override
            public void done(CAUser myUser, BmobException e) {
                if(e==null){
                    userCallback.onSuccess(myUser);
                }else{
                    userCallback.onError(e);
                }
            }
        }));
    }


    /**
     * 查询用户（字段，值）
     *
     * @return void
     * @exception
     */
    public void queryUser(String key, String valuse, final UserQueryCallback userQueryCallback){

        BmobQuery<CAUser> queryRg = new BmobQuery<CAUser>();
        queryRg.addWhereEqualTo(key, valuse);
        queryRg.findObjects(new FindListener<CAUser>() {
            @Override
            public void done(List<CAUser> objects, BmobException e) {
                userQueryCallback.onResult(objects, e);
            }
        });
    }

    /**
     * 解决Subscription内存泄露问题
     * @param s
     */
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }
}
