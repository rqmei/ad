package com.ad.tibi.lib.imgad;

/**
 * ImageAdEntity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author yelian on 2020/5/19
 */
public class ImageAdEntity {
    
    private int width;
    
    private int height;
    /**
     * 图片路径
     */
    private String url;
    /**
     * 跳转路径
     */
    private String jumpPath;
    /**
     * 下载路径
     */
    private String downloadPath;
    /**
     * 预留字段，后期可能做视频
     */
    private int type;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumpPath() {
        return jumpPath;
    }

    public void setJumpPath(String jumpPath) {
        this.jumpPath = jumpPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
