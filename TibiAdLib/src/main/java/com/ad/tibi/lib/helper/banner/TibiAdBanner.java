package com.ad.tibi.lib.helper.banner;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.R;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;
import com.ad.tibi.lib.http.TibiAdHttp;
import com.ad.tibi.lib.imgad.ImageAdEntity;
import com.ad.tibi.lib.util.AdNameType;
import com.ad.tibi.lib.util.AdRandomUtil;
import com.ad.tibi.lib.util.DensityUtils;
import com.ad.tibi.lib.util.UIUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

public class TibiAdBanner {
    static TibiAdBanner adBanner;

    public static TibiAdBanner getSingleAdBanner() {
        if (adBanner == null) {
            synchronized (AdInit.class) { // 避免不必要的同步
                if (adBanner == null) { // 在null的情况下创建实例
                    adBanner = new TibiAdBanner();
                }
            }
        }
        return adBanner;
    }

    /**
     * banner广告
     *
     * @param activity        展示广告的activity
     * @param splashConfigStr 广告类型
     * @param adConstStr      广告位id
     * @param adsParentLayout 展示广告的大容器
     * @param adListener      广告状态监听器
     */
    public void showAdBanner(final Activity activity, final String splashConfigStr, final String adConstStr,
                             final ViewGroup adsParentLayout, int expressViewHeight, final AdListenerSplashFull adListener) {
        // 随机获取广告类型
        String adType = AdRandomUtil.getRandomAdName(splashConfigStr);
        switch (adType) {
            case AdNameType.GDT:
                showAdBannerGdt(activity, splashConfigStr, adConstStr, adsParentLayout, expressViewHeight, adListener);
                break;
            case AdNameType.CSJ:
                showAdBannerCsj(activity, splashConfigStr, adConstStr, adsParentLayout, expressViewHeight, adListener);
                break;
            case AdNameType.TB:
                // 替比
                showAdBannerTb(activity, splashConfigStr, adConstStr, adsParentLayout, expressViewHeight, adListener);
                break;
            default:
                adListener.onAdFailed(activity.getString(R.string.all_ad_error));
        }

    }

    /**
     * 广点通
     *
     * @param activity
     * @param splashConfigStr
     * @param adConstStr      广告位id
     * @param adsParentLayout
     * @param adListener
     */
    UnifiedBannerView bannerView;

    public void showAdBannerGdt(final Activity activity, final String splashConfigStr, final String adConstStr,
                                final ViewGroup adsParentLayout, int expressViewHeight, final AdListenerSplashFull adListener) {
        String posId = AdInit.getSingleAdInit().getIdMapGDT().get(adConstStr);
        if (bannerView != null) {
            adsParentLayout.removeView(bannerView);
            bannerView.destroy();
        }
        bannerView = new UnifiedBannerView(activity, posId, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {

            }

            @Override
            public void onADReceive() {
                adsParentLayout.removeAllViews();
                adsParentLayout.addView(bannerView);
            }

            @Override
            public void onADExposure() {

            }

            @Override
            public void onADClosed() {

            }

            @Override
            public void onADClicked() {

            }

            @Override
            public void onADLeftApplication() {

            }

            @Override
            public void onADOpenOverlay() {

            }

            @Override
            public void onADCloseOverlay() {

            }
        });
        bannerView.loadAD();

    }

    /**
     * 穿山甲
     *
     * @param activity
     * @param splashConfigStr
     * @param adConstStr
     * @param mBannerContainer
     * @param expressViewHeight banner的高度
     * @param adListener
     */
    public void showAdBannerCsj(final Activity activity, final String splashConfigStr, final String adConstStr,
                                final ViewGroup mBannerContainer, int expressViewHeight, final AdListenerSplashFull adListener) {
        TTAdSdk.getAdManager().requestPermissionIfNecessary(activity);
        float expressViewWidth = UIUtils.getScreenWidthDp(activity);
        String posId = AdInit.getSingleAdInit().getIdMapCsj().get(adConstStr);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(posId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .build();
        TTAdSdk.getAdManager().createAdNative(activity).loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.i("showAdBannerCsj", "onError=" + s);
                mBannerContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(activity, mTTAd, mBannerContainer);
                mTTAd.render();
            }
        });

    }

    private void bindAdListener(Activity activity, TTNativeExpressAd ad, final ViewGroup mBannerContainer) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i("showAdBannerCsj", "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i("showAdBannerCsj", "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.i("showAdBannerCsj", msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.i("showAdBannerCsj", "渲染成功");
                mBannerContainer.removeAllViews();
                mBannerContainer.addView(view);
            }
        });
        //dislike设置
        bindDislike(activity, ad, false, mBannerContainer);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                Log.i("showAdBannerCsj", "点击开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    Log.i("showAdBannerCsj", "下载中，点击暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                Log.i("showAdBannerCsj", "下载暂停，点击继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Log.i("showAdBannerCsj", "下载失败，点击重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                Log.i("showAdBannerCsj", "安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Log.i("showAdBannerCsj", "点击安装");
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(Activity activity, TTNativeExpressAd ad, boolean customStyle, final ViewGroup mBannerContainer) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(activity, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    Log.i("showAdBannerCsj", "点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    mBannerContainer.removeAllViews();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(activity, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.i("showAdBannerCsj", "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                mBannerContainer.removeAllViews();
            }

            @Override
            public void onCancel() {
                Log.i("showAdBannerCsj", "点击取消 ");
            }
        });
    }

    private boolean mHasShowDownloadActive = false;

    /**
     * 替比
     *
     * @param activity
     * @param splashConfigStr
     * @param adConstStr
     * @param adsParentLayout
     * @param adListener
     */
    public void showAdBannerTb(final Activity activity, final String splashConfigStr, final String adConstStr,
                               final ViewGroup adsParentLayout, final int expressViewHeight, final AdListenerSplashFull adListener) {
        HttpParams httpParams = new HttpParams();
        TibiAdHttp.getSingleAdHttp().getAdInfo(httpParams, new CallBack<ImageAdEntity>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {
                Log.i("showAdFullTb", "onCompleted =");
            }

            @Override
            public void onError(ApiException e) {
                Log.i("showAdFullTb", "onError =" + e.getDisplayMessage());
                // 请求替比广告失败，加载第三方广告
                showAdBanner(activity, splashConfigStr, adConstStr, adsParentLayout, expressViewHeight, adListener);
            }

            @Override
            public void onSuccess(ImageAdEntity imageAdEntity) {
                Log.i("showAdFullTb", "result=" + imageAdEntity);
                imageAdEntity = new ImageAdEntity();
                List<String> iamges = new ArrayList<>();
                iamges.add(imageAdEntity.getUrl());
                iamges.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=403900030,2074040631&fm=26&gp=0.jpg");

                int Height = DensityUtils.dp2px(activity, expressViewHeight);
                // 请求替比广告成功
                // 设置广告view
                View view = LayoutInflater.from(activity).inflate(R.layout.tb_ad_banner, null);
                LinearLayout.LayoutParams layoutParams
                        = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Height);
                Banner banner = view.findViewById(R.id.banner);
                banner.setLayoutParams(layoutParams);
                // 设置图片加载器
                banner.setImageLoader(new BannerImageLoader(adListener))
                        // 设置banner动画效果
                        .setBannerAnimation(Transformer.DepthPage)
                        // 设置自动轮播，默认为true
                        .isAutoPlay(imageAdEntity.getRoll() == 1)
                        // 设置轮播时间
                        .setDelayTime(imageAdEntity.getRollTime() * 1000)
                        // 设置指示器位置（当banner模式中有指示器时）
                        .setIndicatorGravity(BannerConfig.CENTER)
                        .setImages(iamges);

                // 设置当前广告信息
                AdInit.getSingleAdInit().setImageAdEntity(imageAdEntity);
                // 加入广告
                adsParentLayout.removeAllViews();
                adsParentLayout.addView(view);
                // banner设置方法全部调用完毕时最后调用
                banner.start();
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        TibiAdHttp.getSingleAdHttp().onAdClick(adListener);
                    }
                });
            }
        });
    }
}
