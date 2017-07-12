package com.lyy_wzw.comeacross.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by yidong9 on 17/6/21.
 */

public class FootPrint extends BmobObject{
    private String userId;
    private String content;
    private boolean isShowLocation;
    private Double longitude;
    private Double latitude;
    private List<FootPrintFile> footPrintFiles;
    private Integer visitRight;
    private Integer label;
    private FootPrintAddress footPrintAddress;
    private List<CommentItem> comments;
    private List<PraiseItem> praises;
    private boolean isExpand;

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public boolean isExpand() {

        return isExpand;
    }

    public List<CommentItem> getComments() {
        return comments;
    }

    public List<PraiseItem> getPraises() {
        return praises;
    }

    public void setComments(List<CommentItem> comments) {
        this.comments = comments;
    }

    public void setPraises(List<PraiseItem> praises) {
        this.praises = praises;
    }

    public void setFootPrintAddress(FootPrintAddress footPrintAddress) {
        this.footPrintAddress = footPrintAddress;
    }


    public FootPrintAddress getFootPrintAddress() {
        return footPrintAddress;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "FootPrint{" +
                "userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", isShowLocation=" + isShowLocation +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", footPrintFiles=" + footPrintFiles +
                ", visitRight=" + visitRight +
                ", label=" + label +
                ", footPrintAddress=" + footPrintAddress +
                ", comments=" + comments +
                ", praises=" + praises +
                ", isExpand=" + isExpand +
                '}';
    }
}
