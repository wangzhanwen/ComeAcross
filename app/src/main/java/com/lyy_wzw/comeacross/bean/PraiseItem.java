package com.lyy_wzw.comeacross.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by yidong9 on 17/7/6.
 */

public class PraiseItem extends BmobObject implements Serializable{
    private CAUser user;
    private String footPrintId;

    public CAUser getUser() {
        return user;
    }

    public String getFootPrintId() {
        return footPrintId;
    }

    public void setUser(CAUser user) {
        this.user = user;
    }

    public void setFootPrintId(String footPrintId) {
        this.footPrintId = footPrintId;
    }

    @Override
    public String toString() {
        return "PraiseItem{" +
                "user=" + user +
                ", footPrintId='" + footPrintId + '\'' +
                '}';
    }
}
