package com.ad.tibi.lib.imgad;

import java.io.Serializable;

/**
 * ImageAdEntity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author yelian on 2020/5/19
 */
public class ImageAdEntity implements Serializable {

    private int width;

    private int height;
    /**
     * 图片路径
     */
    private String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec" +
            "=1589947868797&di=c900e4daf896c2eabffdaf9bd84a501a&imgtype=0&src=http%3A%2F%2Fa3" +
            ".att.hudong.com%2F61%2F98%2F01300000248068123885985729957.jpg";
    /**
     * 跳转路径
     */
    private String jumpPath = "http://www.baidu.com";
    /**
     * 下载路径
     * http://tbandroid.timingbar.com/safe-online-apk/safe.apk
     */
    private String downloadPath = "http://tbandroid.timingbar.com/safe-online-apk/safe.apk";
    /**
     * 预留字段，后期可能做视频
     * 1:图片跳转；2：文件下载；3：视频播放
     */
    private int type = 2;
    /**
     * 是否滚动（0：不滚动 ，1：滚动)
     */
    private int roll = 1;

    /**
     * 滚动时间(秒)
     */
    private int rollTime = 10;
    /**
     * 广告文章
     */
    private String adText;

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

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getRollTime() {
        return rollTime;
    }

    public void setRollTime(int rollTime) {
        this.rollTime = rollTime;
    }

    public String getAdText() {
        return adText;
    }

    public void setAdText(String adText) {
        this.adText = adText;
    }
}
