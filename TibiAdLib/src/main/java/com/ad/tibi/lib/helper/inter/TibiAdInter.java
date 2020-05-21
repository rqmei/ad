package com.ad.tibi.lib.helper.inter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.R;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;
import com.ad.tibi.lib.http.TibiAdHttp;
import com.ad.tibi.lib.imgad.ImageAdEntity;
import com.ad.tibi.lib.util.AdNameType;
import com.ad.tibi.lib.util.AdRandomUtil;
import com.ad.tibi.lib.util.ImageLoadUtil;
import com.ad.tibi.lib.util.UIUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.util.List;

/**
 * 插屏广告
 */
public class TibiAdInter {
    static TibiAdInter adInter;
    boolean stop = false;

    public static TibiAdInter getSingleAdInter() {
        if (adInter == null) {
            synchronized (AdInit.class) { // 避免不必要的同步
                if (adInter == null) { // 在null的情况下创建实例
                    adInter = new TibiAdInter();
                }
            }
        }
        return adInter;
    }

    /**
     * 显示插屏广告
     */
    public void showAdInter(Activity activity, String interConfigStr, String adConstStr,
                            AdListenerSplashFull adListener) {
        // 广告类型
        String adType = AdRandomUtil.getRandomAdName(interConfigStr);
        switch (adType) {
            case AdNameType.GDT:
                showAdInterTecentGDT(activity, interConfigStr, adConstStr, adListener);
                break;
            case AdNameType.CSJ:
                TTAdSdk.getAdManager().requestPermissionIfNecessary(activity);
                showAdInterCsj(activity, interConfigStr, adConstStr, adListener);
                break;
            case AdNameType.TB:
                // 替比
                showAdInterTb(activity, interConfigStr, adConstStr, adListener);
                break;
            case AdNameType.NO:
                // 加载广告失败
                if (adListener != null) {
                    adListener.onAdFailed(adType);
                }
                break;
            default:
                if (stop) {
                    return;
                }
                adListener.onAdFailed(activity.getString(R.string.all_ad_error));
        }
    }

    UnifiedInterstitialAD iad;

    public void showAdInterTecentGDT(Activity activity, String interConfigStr, String adConstStr,
                                     AdListenerSplashFull adListener) {
        String posId = AdInit.getSingleAdInit().getIdMapGDT().get(adConstStr);
        Log.i("showAdInterTecentGDT","posId="+posId);
        iad = new UnifiedInterstitialAD(activity, posId, new UnifiedInterstitialADListener() {
            @Override
            public void onADReceive() {
                // 广告加载成功
                Log.i("showAdInterTecentGDT", "onADReceive====");
                iad.show();
            }

            @Override
            public void onVideoCached() {
                Log.i("showAdInterTecentGDT", "onVideoCached====");
            }

            @Override
            public void onNoAD(AdError adError) {
                Log.i("showAdInterTecentGDT", "code====" + adError.getErrorCode()+",msg="+adError.getErrorMsg());
            }

            @Override
            public void onADOpened() {
                Log.i("showAdInterTecentGDT", "onADOpened====");
            }

            @Override
            public void onADExposure() {
                Log.i("showAdInterTecentGDT", "onADExposure====");
            }

            @Override
            public void onADClicked() {
                Log.i("showAdInterTecentGDT", "onADClicked====");
            }

            @Override
            public void onADLeftApplication() {
                Log.i("showAdInterTecentGDT", "onADLeftApplication====");
            }

            @Override
            public void onADClosed() {

            }
        });
        setVideoOption(iad);
        iad.loadAD();
    }

    private void setVideoOption(UnifiedInterstitialAD iad) {
        VideoOption.Builder builder = new VideoOption.Builder();
        VideoOption option = builder.setAutoPlayMuted(false)
                .setDetailPageMuted(false)
                .build();
        iad.setVideoOption(option);
        iad.setMinVideoDuration(10);
        iad.setMaxVideoDuration(100);

        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
         * 如果广告位仅支持图文广告，则无需调用
         */

        /**
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
         *
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
         *
         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
         */
        iad.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.MANUAL);
    }

    /**
     * 穿山甲
     *
     * @param activity
     * @param interConfigStr
     * @param adConstStr
     * @param adListener
     */
    public void showAdInterCsj(final Activity activity, final String interConfigStr, final String adConstStr,
                               final AdListenerSplashFull adListener) {
        int n = UIUtils.getAdWidth(activity);
        String posId = AdInit.getSingleAdInit().getIdMapCsj().get(adConstStr);
        // 设置广告参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(posId) // 广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) // 请求广告数量为1到3条
                .setExpressViewAcceptedSize(n, n * 9 / 16) //期望模板广告view的size,单位dp
                .build();

        TTAdSdk.getAdManager().createAdNative(activity).loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.i("插屏", "onError=" + s + "," + i);
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                String newSplashConfigStr = interConfigStr.replace(AdNameType.CSJ, AdNameType.NO);
                showAdInter(activity, newSplashConfigStr, adConstStr, adListener);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                Log.i("插屏", "onNativeExpressAdLoad=" + list.size());
                if (list == null || list.size() == 0) {
                    // 穿山甲返回的广告是 null
                    String newConfigStr = AdNameType.NO;
                    showAdInter(activity, newConfigStr, adConstStr, adListener);
                    return;
                }
                TTNativeExpressAd ttNativeExpressAd = list.get(0);
                bindAdCsjListener(activity, ttNativeExpressAd, adListener);
                ttNativeExpressAd.render();
            }
        });
    }

    /**
     * 绑定穿山甲广告行为
     */
    private void bindAdCsjListener(final Activity activity, final TTNativeExpressAd ad, final AdListenerSplashFull adListener) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {

            @Override
            public void onAdDismiss() {
                Toast.makeText(activity, "广告关闭", Toast.LENGTH_SHORT).show();
                // 关闭广告框
                if (adListener != null) {
                    adListener.onAdDismissed();
                }
            }

            @Override
            public void onAdClicked(View view, int type) {
                Toast.makeText(activity, "广告被点击", Toast.LENGTH_SHORT).show();
                // 关闭广告框
                if (adListener != null) {
                    adListener.onAdClick(AdNameType.CSJ);
                }
            }

            @Override
            public void onAdShow(View view, int type) {
                Toast.makeText(activity, "广告展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Toast.makeText(activity, msg + " code:" + code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Toast.makeText(activity, "渲染成功", Toast.LENGTH_SHORT).show();
                ad.showInteractionExpressAd(activity);
                // 在渲染成功回调时展示广告，提升体验
                if (adListener != null) {
                    adListener.onAdPrepared(AdNameType.CSJ);
                }
            }
        });

        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        //可选，下载监听设置
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                Log.i("bindAdCsjListener", "点击开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                Log.i("bindAdCsjListener", "下载中，点击暂停");
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                Log.i("bindAdCsjListener", "下载暂停，点击继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Log.i("bindAdCsjListener", "下载失败，点击重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                Log.i("bindAdCsjListener", "安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Log.i("bindAdCsjListener", "点击安装");
            }
        });
    }

    /**
     * 替比广告
     *
     * @param activity
     * @param interConfigStr
     * @param adConstStr
     * @param adListener
     */
    CustomDialog customDialog;

    public void showAdInterTb(final Activity activity, final String interConfigStr, final String adConstStr,
                              final AdListenerSplashFull adListener) {
        HttpParams httpParams = new HttpParams();
        TibiAdHttp.getSingleAdHttp().getAdInfo(httpParams, new CallBack<ImageAdEntity>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {
                Log.i("showAdInterTb", "onCompleted =");
            }

            @Override
            public void onError(ApiException e) {
                Log.i("showAdInterTb", "onError =" + e.getDisplayMessage());
                // 请求替比广告失败，加载第三方广告
                showAdInter(activity, interConfigStr, adConstStr, adListener);
            }

            @Override
            public void onSuccess(ImageAdEntity imageAdEntity) {
                Log.i("showAdInterTb", "result=" + imageAdEntity);
                imageAdEntity = new ImageAdEntity();
                // 请求替比广告成功
                //设置当前广告信息
                AdInit.getSingleAdInit().setImageAdEntity(imageAdEntity);
                // 加入广告
                View view = LayoutInflater.from(activity).inflate(R.layout.tb_ad_inter, null);
                ImageView ivAd = view.findViewById(R.id.iv_ad);
                ImageView ivAdClose = view.findViewById(R.id.iv_ad_close);
                ivAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 统计点击量
                        TibiAdHttp.getSingleAdHttp().onAdOperation(activity, adListener);
                        // 关闭广告框
                        if (customDialog != null && customDialog.isShowing()) {
                            customDialog.dismiss();
                        }
                    }
                });
                ivAdClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 关闭广告框
                        if (adListener != null) {
                            adListener.onAdDismissed();
                        }
                        if (customDialog != null && customDialog.isShowing()) {
                            customDialog.dismiss();
                        }
                    }
                });
                ImageLoadUtil.loadImage(activity, imageAdEntity.getUrl(), ivAd, adListener);
                int n = UIUtils.getAdWidth(activity);
                customDialog = new CustomDialog(activity, n * 3 / 4, n, view, R.style.CustomDialog);
                customDialog.show();
                if (adListener != null) {
                    adListener.onAdPrepared(AdNameType.TB);
                }
            }
        });
    }
}
