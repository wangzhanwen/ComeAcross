package com.lyy_wzw.comeacross.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by yidong9 on 17/6/21.
 */

public class FootPrint extends BmobObject{
    private String uid;
    private String content;
    private boolean isShowLocation;
    private Double longitude;
    private Double latitude;
    private List<FootPrintFile> footPrintFiles;
    private Integer visitRight;
    private Integer label;
    private FootPrintAddress footPrintAddress;

    public void setFootPrintAddress(FootPrintAddress footPrintAddress) {
        this.footPrintAddress = footPrintAddress;
    }

    public FootPrintAddress getFootPrintAddress() {
        return footPrintAddress;
    }

    public String getUid() {
        return uid;
    }

    public String getContent() {
        return content;
    }

    public boolean isShowLocation() {
        return isShowLocation;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public List<FootPrintFile> getFootPrintFiles() {
        return footPrintFiles;
    }

    public Integer getVisitRight() {
        return visitRight;
    }

    public Integer getLabel() {
        return label;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setShowLocation(boolean showLocation) {
        isShowLocation = showLocation;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setFootPrintFiles(List<FootPrintFile> footPrintFiles) {
        this.footPrintFiles = footPrintFiles;
    }

    public void setVisitRight(Integer visitRight) {
        this.visitRight = visitRight;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "FootPrint{" +
                "uid='" + uid + '\'' +
                ", content='" + content + '\'' +
                ", isShowLocation=" + isShowLocation +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", footPrintFiles=" + footPrintFiles +
                ", visitRight=" + visitRight +
                ", label=" + label +
                ", footPrintAddress=" + footPrintAddress +
                '}';
    }
}
