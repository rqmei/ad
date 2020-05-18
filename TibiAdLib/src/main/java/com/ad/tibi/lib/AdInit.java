package com.ad.tibi.lib;

import android.app.Application;
import android.util.Log;

import com.ad.tibi.lib.util.AdNameType;
import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdSdk;

import java.util.HashMap;
import java.util.Map;

/**
 * 广告初始化
 */
public class AdInit {
    static AdInit singleAdInit;
    /**
     * 位ID的Map
     */
    private Map<String, String> idMapBaidu = new HashMap<>();
    private Map<String, String> idMapGDT = new HashMap<>();
    private Map<String, String> idMapCsj = new HashMap<>();
    /**
     * 保存application
     */
    private Application mContext;

    /**
     * 广点通的 AppId
     */
    private String appIdGDT = "";

    /**
     * 超时时间
     */
    private long timeOutMillis;

    /**
     * 前贴
     */
    private int preMoivePaddingSize = 0;

    public static AdInit getSingleAdInit() {
        if (singleAdInit == null) {
            synchronized (AdInit.class) { // 避免不必要的同步
                if (singleAdInit == null) { // 在null的情况下创建实例
                    singleAdInit = new AdInit();
                }
            }
        }
        return singleAdInit;
    }

    /**
     * 初始化广告
     */
    //baidu
    public void initBaiduAd(Application context, String baiduAdAppId,
                            Map<String, String> baiduIdMap) {
        mContext = context;
        AdView.setAppSid(context, baiduAdAppId);
        idMapBaidu = baiduIdMap;
        Log.i("AdInit", "初始化：" + AdNameType.BAIDU);
    }

    //广点通
    public void initGDTAd(Application context, String gdtAdAppId, Map<String, String> gdtIdMap) {
        mContext = context;
        idMapGDT = gdtIdMap;
        appIdGDT = gdtAdAppId;
        Log.i("AdInit", "初始化：" + AdNameType.GDT);
    }

    //穿山甲
    public void initCsjAd(Application context, String csjAdAppId, String appName,
                          Map<String, String> csjIdMap, boolean useTextureView) {
        mContext = context;
        idMapCsj = csjIdMap;
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdSdk.init(context, new TTAdConfig.Builder()
                .appId(csjAdAppId)
                .appName(appName)
                .useTextureView(useTextureView) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(BuildConfig.DEBUG) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                .supportMultiProcess(false) //是否支持多进程，true支持
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build()
        );
        Log.i("AdInit", "初始化：" + AdNameType.CSJ);
    }

    public void setAdTimeOutMillis(long millis) {
        timeOutMillis = millis;
        Log.i("AdInit", "全局设置超时时间：" + millis);
    }

    public long getTimeOutMillis() {
        return timeOutMillis;
    }

    public void setPreMoiveMarginTopSize(int height) {
        preMoivePaddingSize = height;
    }

    public Map<String, String> getIdMapBaidu() {
        return idMapBaidu;
    }

    public Map<String, String> getIdMapGDT() {
        return idMapGDT;
    }

    public Map<String, String> getIdMapCsj() {
        return idMapCsj;
    }

    public String getAppIdGDT() {
        return appIdGDT;
    }

    public void setAppIdGDT(String appIdGDT) {
        this.appIdGDT = appIdGDT;
    }
}
