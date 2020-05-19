package com.ad.tibi.lib.imgad;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * ImageAdInterface
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author yelian on 2020/5/19
 */
public interface ImageAdInterface {
    /**
     * 图片广告加载成功
     */
    void loadImageSuccess();

    /**
     * 跳转到网页广告
     * @param path
     */
    void jumpToWeb(String path);

    /**
     * 下载
     * @param path
     */
    void download(String path);
    
}
