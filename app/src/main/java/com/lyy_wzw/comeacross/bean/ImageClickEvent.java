package com.lyy_wzw.comeacross.bean;

/**
 * Created by yidong9 on 17/7/20.
 */

public class ImageClickEvent {
    private  int code;
    private int position;

    public ImageClickEvent() {
    }

    public ImageClickEvent(int code) {
        this.code = code;
    }

    public ImageClickEvent(int code, int position) {
        this.code = code;
        this.position = position;
    }

    public int getCode() {
        return code;
    }

    public int getPosition() {
        return position;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
