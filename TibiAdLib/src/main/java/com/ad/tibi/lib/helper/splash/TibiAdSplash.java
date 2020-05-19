package com.ad.tibi.lib.helper.splash;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.R;
import com.ad.tibi.lib.interf.AdListenerSplashFull;
import com.ad.tibi.lib.util.AdNameType;
import com.ad.tibi.lib.util.AdRandomUtil;
import com.ad.tibi.lib.util.UIUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.util.Timer;
import java.util.TimerTask;

public class TibiAdSplash {
    static TibiAdSplash adSplash;
    private Timer timer = null;
    private TimerTask overTimerTask = null;
    private boolean stop = false;

    public static TibiAdSplash getSingleAdSplash() {
        if (adSplash == null) {
            synchronized (AdInit.class) { // 避免不必要的同步
                if (adSplash == null) { // 在null的情况下创建实例
                    adSplash = new TibiAdSplash();
                }
            }
        }
        return adSplash;
    }

    /**
     * 显示开屏广告
     *
     * @param splashConfigStr "baidu:2,gdt:8"
     * @param adsParentLayout 容器
     * @param adListener      监听
     */
    public void showAdFull(Activity activity, String splashConfigStr, String adConstStr
            , ViewGroup adsParentLayout, View skipView, TextView timeView, AdListenerSplashFull adListener) {
        stop = false;
        startTimerTask(adListener);
        switch (AdRandomUtil.getRandomAdName(splashConfigStr)) {
            case AdNameType.BAIDU:
//                showAdFullBaiduMob(activity, splashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener);
                break;
            case AdNameType.GDT:
                showAdFullGDT(activity, splashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener);
                break;
            case AdNameType.CSJ:
                showAdFullCsj(activity, splashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener);
                break;
//            case AdNameType.TB:
//                // 替比
//                showAdFullTb(activity, splashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener);
//                break;
            default:
                if (stop) {
                    return;
                }
                cancelTimerTask();
                adListener.onAdFailed(activity.getString(R.string.all_ad_error));
        }
    }

    /**
     * 腾讯广点通
     *
     * @param activity        展示广告的activity
     * @param splashConfigStr 广告类型
     * @param adConstStr      广告位id
     * @param adsParentLayout 展示广告的大容器
     * @param skipView        自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。
     *                        SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param timeView        倒计时view
     * @param adListener      广告状态监听器
     */
    private void showAdFullGDT(final Activity activity, final String splashConfigStr, final String adConstStr,
                               final ViewGroup adsParentLayout, final View skipView, final TextView timeView,
                               final AdListenerSplashFull adListener) {
        adListener.onStartRequest(AdNameType.GDT);
        AdInit adInit = AdInit.getSingleAdInit();
        SplashAD splash = new SplashAD(activity, skipView, adInit.getIdMapGDT().get(adConstStr),
                new SplashADListener() {
                    @Override
                    public void onADDismissed() {
                        if (adListener != null) {
                            adListener.onAdDismissed();
                        }
                    }

                    @Override
                    public void onNoAD(AdError adError) {
                        if (stop) {
                            return;
                        }
                        cancelTimerTask();
                        String newSplashConfigStr = splashConfigStr.replace(AdNameType.GDT, AdNameType.NO);
                        showAdFull(activity, newSplashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener);
                    }

                    @Override
                    public void onADPresent() {
                        if (stop) {
                            return;
                        }
                        skipView.setVisibility(View.VISIBLE);
                        cancelTimerTask();
                        if (adListener != null) {
                            adListener.onAdPrepared(AdNameType.GDT);
                        }
                    }

                    @Override
                    public void onADClicked() {
                        if (adListener != null) {
                            adListener.onAdClick(AdNameType.GDT);
                        }
                    }

                    /**
                     * 倒计时回调，返回广告还将被展示的剩余时间。
                     * @param l 剩余毫秒数
                     */
                    @Override
                    public void onADTick(long l) {
                        timeView.setText(String.valueOf((l / 1000 + 1)));
                    }

                    @Override
                    public void onADExposure() {

                    }

                    @Override
                    public void onADLoaded(long l) {

                    }
                }, 0);

        splash.fetchAndShowIn(adsParentLayout);
    }

    /**
     * 穿山甲
     */
    private void showAdFullCsj(final Activity activity, final String splashConfigStr, final String adConstStr,
                               final ViewGroup adsParentLayout, final View skipView, final TextView timeView,
                               final AdListenerSplashFull adListener) {
        try {
            adListener.onStartRequest(AdNameType.CSJ);
            //比如：广告下方拼接logo、适配刘海屏等，需要考虑实际广告大小
            float expressViewWidth = UIUtils.getScreenWidthDp(activity);
            float expressViewHeight = UIUtils.getHeight(activity);
            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            Log.i("showAdFullCsj", "adConstStr=" + adConstStr);
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(adConstStr)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
//                    .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight)
                    .build();
            TTAdSdk.getAdManager().createAdNative(activity).loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                @Override
                public void onError(int i, String s) {
                    if (stop) {
                        return;
                    }
                    cancelTimerTask();

                    String newSplashConfigStr = splashConfigStr.replace(AdNameType.CSJ, AdNameType.NO);
                    showAdFull(activity, newSplashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener);
                }

                @Override
                public void onTimeout() {

                }

                @Override
                public void onSplashAdLoad(TTSplashAd ad) {
                    if (ad == null) {
                        // 广告是空
                        return;
                    }
                    //获取SplashView
                    View view = ad.getSplashView();
                    if (view != null && adsParentLayout != null && !activity.isFinishing()) {
                        if (adListener != null) {
                            adListener.onAdPrepared(AdNameType.CSJ);
                        }
                        adsParentLayout.removeAllViews();
                        //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                        adsParentLayout.addView(view);
                        //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                        //ad.setNotAllowSdkCountdown();
                    } else {
                        // 跳转页面
                        if (adListener != null) {
                            adListener.onAdDismissed();
                        }
                    }

                    //设置SplashView的交互监听器
                    ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int type) {
                            Log.i("TibiAdSplash", "onAdClicked开屏广告点击...");
                            if (adListener != null) {
                                adListener.onAdClick(AdNameType.CSJ);
                            }
                        }

                        @Override
                        public void onAdShow(View view, int type) {
                            Log.i("TibiAdSplash", "onAdShow开屏广告展示...");
                            if (adListener != null) {
                                adListener.onAdDismissed();
                            }
                        }

                        @Override
                        public void onAdSkip() {
                            Log.i("TibiAdSplash", "onAdSkip开屏广告跳过...");
                            if (adListener != null) {
                                adListener.onAdDismissed();
                            }
                        }

                        @Override
                        public void onAdTimeOver() {
                            Log.i("TibiAdSplash", "onAdTimeOver开屏广告倒计时结束...");
                            if (adListener != null) {
                                adListener.onAdDismissed();
                            }
                        }
                    });
                    if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                        ad.setDownloadListener(new TTAppDownloadListener() {
                            boolean hasShow = false;

                            @Override
                            public void onIdle() {
                            }

                            @Override
                            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                                if (!hasShow) {
                                    Log.i("TibiAdSplash", "下载中...");
                                    hasShow = true;
                                }
                            }

                            @Override
                            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                                Log.i("TibiAdSplash", "下载暂停...");
                            }

                            @Override
                            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                                Log.i("TibiAdSplash", "下载失败...");
                            }

                            @Override
                            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                                Log.i("TibiAdSplash", "下载完成...");
                            }

                            @Override
                            public void onInstalled(String fileName, String appName) {
                                Log.i("TibiAdSplash", "安装完成...");

                            }
                        });
                    }
                }
            }, 2500);


        } catch (Exception e) {
            e.printStackTrace();
            if (stop) {
                return;
            }
            cancelTimerTask();
            String newSplashConfigStr = splashConfigStr.
                    replace(AdNameType.CSJ, AdNameType.NO);
            showAdFull(activity, newSplashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener);
        }
    }

    /**
     * 显示替比原生
     */
    public void showAdFullTb(final Activity activity, final String splashConfigStr, final String adConstStr,
                             final ViewGroup adsParentLayout, final View skipView, final TextView timeView,
                             final AdListenerSplashFull adListener) {
//        TibiAdHttp.getAdInfo("https://students.safe-new.tb.com/home", new OnSuccessCallback<String>() {
//            @Override
//            public void success(String result) {
//                Log.i("showAdFullTb", "result=" + result);
//                // 请求替比广告成功
//                AdSplashView adSplashView = new AdSplashView(activity);
//                adSplashView.setImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589869298976&di=d484e8fb5780b9c6b2e36fabd9badd1a&imgtype=0&src=http%3A%2F%2Fa0.att.hudong.com%2F56%2F12%2F01300000164151121576126282411.jpg");
//                adSplashView.setAdvertListener(adListener);
//            }
//        }, new OnErrorCallback() {
//            @Override
//            public void error(Exception exception) {
//                exception.printStackTrace();
//                Log.i("showAdFullTb", "result=" + exception.getMessage());
//                // 请求替比广告失败，加载第三方广告
//                showAdFullTb(activity, splashConfigStr, adConstStr, adsParentLayout,
//                        skipView, timeView, adListener);
//            }
//        });

    }


    /**
     * 取消超时任务
     */
    private void cancelTimerTask() {
        stop = false;
        if (timer != null) {
            timer.cancel();
        }
        if (overTimerTask != null) {
            overTimerTask.cancel();
        }
    }

    /**
     * 开始超时任务
     */
    private void startTimerTask(AdListenerSplashFull listener) {
        cancelTimerTask();
        timer = new Timer();
        overTimerTask = new OverTimerTask(listener);
        if (timer != null) {
            timer.schedule(overTimerTask, AdInit.getSingleAdInit().getTimeOutMillis());
        }
    }

    /**
     * 超时任务
     */
    private class OverTimerTask extends TimerTask {
        private AdListenerSplashFull weakReference;

        public void run() {
            if (this.weakReference != null) {
                this.weakReference.onAdFailed(AdNameType.CSJ);
            }
        }

        public OverTimerTask(AdListenerSplashFull listener) {
            super();
            if (listener != null) {
                this.weakReference = listener;
            }
        }
    }
}
