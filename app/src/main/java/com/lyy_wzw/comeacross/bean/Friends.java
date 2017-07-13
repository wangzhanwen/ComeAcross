package com.lyy_wzw.comeacross.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 27459 on 2017/6/30.
 */

public class Friends extends BmobObject {

    private String inviteId;
    private String invitedId;
    private boolean isAgree;

    public Friends(String inviteId, String invitedId, boolean isAgree) {
        this.inviteId = inviteId;
        this.invitedId = invitedId;
        this.isAgree = isAgree;

    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getInvitedId() {
        return invitedId;
    }

    public void setInvitedId(String invitedId) {
        this.invitedId = invitedId;
    }

    public boolean isAgree() {
        return isAgree;
    }

    public void setAgree(boolean agree) {
        isAgree = agree;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "inviteId='" + inviteId + '\'' +
                ", invitedId='" + invitedId + '\'' +
                ", isAgree=" + isAgree +
                '}';
    }
}
