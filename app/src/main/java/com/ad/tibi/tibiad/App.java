package com.ad.tibi.tibiad;

import android.app.Application;

import com.ad.tibi.lib.AdInit;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Map<String, String> gdtIdMap = new HashMap<String, String>();
        gdtIdMap.put(AdConst.AD_SPLASH, "5070550501041614");
        gdtIdMap.put(AdConst.AD_INTER, "4061006419774284");
        gdtIdMap.put(AdConst.AD_FLOW_INDEX, "6041707449579237");
        gdtIdMap.put(AdConst.AD_TIEPIAN_LIVE, "3031506499071361");
        gdtIdMap.put(AdConst.AD_WEBVIEW_BANNER, "3050767842595815");
        gdtIdMap.put(AdConst.AD_BACK, "8021700419077347");
        gdtIdMap.put(AdConst.AD_MID, "8021700419077347");
        // 第一个参数是Context上下文;第二个参数是您在DMP上获得的行为数据源ID;第三个参数是您在DMP上获得AppSecretKey
        AdInit.getSingleAdInit().initGDTAd(this, "1110522576,", gdtIdMap);

        Map<String, String> csjIdMap = new HashMap<String, String>();
        csjIdMap.put(AdConst.AD_SPLASH, "887324838");
        csjIdMap.put(AdConst.AD_INTER, "920413056");
        csjIdMap.put(AdConst.AD_FLOW_INDEX, "920413297");
        csjIdMap.put(AdConst.AD_TIEPIAN_LIVE, "920413238");
        csjIdMap.put(AdConst.AD_WEBVIEW_BANNER, "920413358");
        csjIdMap.put(AdConst.AD_BACK, "920413512");
        csjIdMap.put(AdConst.AD_MID, "920413056");
        AdInit.getSingleAdInit().initCsjAd(this, "5066244",
                this.getString(R.string.app_name), csjIdMap, true);
        AdInit.getSingleAdInit().setAdTimeOutMillis(5000);
    }
}
