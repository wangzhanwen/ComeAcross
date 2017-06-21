package com.lyy_wzw.comeacross.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by yidong9 on 17/6/19.
 */

public class CAUser extends BmobUser{
    private String  ryUid;
    private boolean  sex;
    private String userPhoto;

    public CAUser() {
    }

    public void setRyUid(String ryUid) {
        this.ryUid = ryUid;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getRyUid() {
        return ryUid;
    }

    public boolean isSex() {
        return sex;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    @Override
    public String toString() {
        return "CAUser{" +
                "ryUid='" + ryUid + '\'' +
                ", sex=" + sex +
                ", userPhoto='" + userPhoto + '\'' +
                '}';
    }
}
