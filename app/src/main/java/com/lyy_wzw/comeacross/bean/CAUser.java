package com.lyy_wzw.comeacross.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by yidong9 on 17/6/19.
 */

public class CAUser extends BmobUser implements Serializable{
    private boolean  sex;
    private String userPhoto;
    private String ryUserToken;

    public CAUser() {
    }

    public boolean isSex() {
        return sex;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getRyUserToken() {
        return ryUserToken;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setRyUserToken(String ryUserToken) {
        this.ryUserToken = ryUserToken;
    }

    @Override
    public String toString() {
        return "CAUser{" +
                "sex=" + sex +
                ", userPhoto='" + userPhoto + '\'' +
                ", ryUserToken='" + ryUserToken + '\'' +
                '}';
    }
}