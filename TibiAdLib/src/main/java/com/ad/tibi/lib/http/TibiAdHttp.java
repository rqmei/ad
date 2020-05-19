package com.ad.tibi.lib.http;


import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.callback.OnErrorCallback;
import com.liwy.easyhttp.callback.OnSuccessCallback;
import com.liwy.easyhttp.common.EasyRequest;

/**
 * 替比广告相关的网络请求
 */
public class TibiAdHttp {
    /**
     * 获取广告相关的数据
     *
     * @param url             路径
     * @param successCallback 接口回调
     */
    public static void getAdInfo(String url, OnSuccessCallback successCallback, OnErrorCallback errorCallback) {
        EasyRequest easyRequest = EasyHttp.getBuilder()
                .setUrl(url)
                .addParam("usePositionType", "2")
                .addParam("docSize", "5")
                .setTag("testTag")
                .setSync(false)
                .setOnSuccessCallback(successCallback)
                .setOnErrorCallback(errorCallback)
                .build();
        EasyHttp.getInstance().get(easyRequest);
    }
}
