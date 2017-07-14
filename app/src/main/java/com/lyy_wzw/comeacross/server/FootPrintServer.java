package com.lyy_wzw.comeacross.server;

import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.user.UserHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yidong9 on 17/6/29.
 */

public class FootPrintServer {
    private static  FootPrintServer instance;
    private FootPrintServer(){}
    public static  synchronized FootPrintServer getInstance(){
        if (instance == null) {
            instance = new FootPrintServer();
        }
        return instance;
    }

    public interface FootPrintQueryCallback{
        void onSuccess(List<FootPrint> footPrints);
        void onError(BmobException e);
    }

    public void getAll(final FootPrintQueryCallback callback){
        BmobQuery<FootPrint> query = new BmobQuery<FootPrint>();
        query.findObjects(new FindListener<FootPrint>() {
            @Override
            public void done(List<FootPrint> list, BmobException e) {
                if(e==null){
                    callback.onSuccess(list);
                }else{
                    callback.onError(e);
                }
            }
        });

    }
}
