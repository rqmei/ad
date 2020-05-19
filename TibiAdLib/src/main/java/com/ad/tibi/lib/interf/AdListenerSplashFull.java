package com.ad.tibi.lib.interf;

/**
 * 闪屏广告回调工具类
 */
public interface AdListenerSplashFull {
    /**
     * 开始请求广告之前
     *
     * @param var1
     */
    void onStartRequest(String var1);

    /**
     * 广告点击
     *
     * @param var1
     */
    void onAdClick(String var1);

    /**
     * 广告加载失败
     *
     * @param var1
     */
    void onAdFailed(String var1);

    /**
     * 广告消失的时候回调该方法
     * 1、跳过广告
     * 2、点击广告进入广告落地页且从落地页back回去
     * 3。倒计时结束
     */
    void onAdDismissed();

    /**
     * 广告请求成功，准备展示
     *
     * @param var1
     */
    void onAdPrepared(String var1);
}
