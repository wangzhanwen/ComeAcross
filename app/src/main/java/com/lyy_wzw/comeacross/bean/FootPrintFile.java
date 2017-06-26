package com.lyy_wzw.comeacross.bean;

/**
 * Created by yidong9 on 17/6/22.
 */

public class FootPrintFile {
    private Integer type;          // 文件类型 1. 图片， 2.视频
    private String  filePath;      // 文件路径
    private String  thumbnailPath; // 文件缩略图路径

    public Integer getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    @Override
    public String toString() {
        return "FootPrintFile{" +
                "type=" + type +
                ", filePath='" + filePath + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                '}';
    }
}
