package com.ad.tibi.lib.helper.splash.inter;

/**
 * 倒计时涉及的接口回掉
 */
public interface TimeListener {
    // 时间结束，点击跳过  都是结束广告，一个回调就可以了
    void onDismiss();
}
