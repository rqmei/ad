package com.ad.tibi.lib.http;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.helper.banner.TibiAdBanner;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;
import com.ad.tibi.lib.imgad.ImageAdEntity;
import com.ad.tibi.lib.util.AdNameType;
import com.ad.tibi.lib.util.FileUtil;
import com.ad.tibi.lib.util.StringUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;
import com.zhouyou.http.utils.HttpLog;

import java.util.LinkedHashMap;

import io.reactivex.disposables.Disposable;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 替比广告相关的网络请求
 */
public class TibiAdHttp extends ApiBase {
    static TibiAdHttp adHttp;
    /**
     * 广告信息路径
     */
    public static String AD_INFO_URL = "http://mobile.safe.tb.com/mobile/my/current/notice";
    /**
     * 广告曝光统计路径
     */
    public static String AD_SHOW_COUNT_URL = "http://mobile.safe.tb.com/mobile/my/current/notice";
    /**
     * 广告点击统计路径
     */
    public static String AD_CLICK_COUNT_URL = "http://mobile.safe.tb.com/mobile/my/current/notice";

    public static TibiAdHttp getSingleAdHttp() {
        if (adHttp == null) {
            synchronized (AdInit.class) { // 避免不必要的同步
                if (adHttp == null) { // 在null的情况下创建实例
                    adHttp = new TibiAdHttp();
                }
            }
        }
        return adHttp;
    }

    public static void initEasyHttp(Application application) {
        EasyHttp.init(application);
    }

    /**
     * 广告的操作（点击跳转或者下载）
     *
     * @param adListener 广告操作回调
     */
    public void onAdOperation(Context mContext, AdListenerSplashFull adListener) {
        ImageAdEntity imageAdEntity = AdInit.getSingleAdInit().getImageAdEntity();
        if (imageAdEntity != null) {
            int type = imageAdEntity.getType();
            if (type == 1 && !StringUtil.isNullOrEmpty(imageAdEntity.getJumpPath())) {
                // 统计点击量
                onAdClick(adListener);

            } else if (type == 2 && !StringUtil.isNullOrEmpty(imageAdEntity.getDownloadPath())) {
                //下载文件
                downFile(mContext, imageAdEntity.getDownloadPath());
            }
        } else {
            Log.i("onAdOperation", "当前广告位空");
        }
    }

    /**
     * 获取广告相关的数据
     *
     * @param params   请求参数
     * @param callBack 接口回调
     */
    public void getAdInfo(HttpParams params, CallBack<ImageAdEntity> callBack) {
        Log.i("getAdInfo", "请求开始URL=" + AD_INFO_URL);
        params.put("userId", "930643");
        params.put("productCode", "prod_antubang");
        params.put("cardNo", "441283198409149945");
        Disposable disposable = EasyHttp.get(AD_INFO_URL)
                .readTimeOut(30 * 1000)//局部定义读超时
                .writeTimeOut(30 * 1000)
                .connectTimeout(30 * 1000)
                .retryCount(2)//本次请求重试次数
                .params(params)
                .timeStamp(true)
                .addConverterFactory(GsonConverterFactory.create())
                .execute(callBack);
        addDispose(disposable);
    }

    /**
     * 点击广告
     */
    public void onAdClick(final AdListenerSplashFull adListener) {
        Log.i("onAdClick", "点击广告请求开始URL=" + AD_CLICK_COUNT_URL);
        // 入参
        LinkedHashMap<String, String> httpParams = new LinkedHashMap<>();
        Disposable disposable = EasyHttp.put(AD_CLICK_COUNT_URL)
                .upObject(httpParams)
                .retryCount(3)//本次请求重试次数
                .addConverterFactory(GsonConverterFactory.create())
                .execute(new CallBack<String>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted() {
                        // 点击广告 跳转
                        if (adListener != null) {
                            adListener.onAdClick(AdNameType.TB);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                    }
                });
        addDispose(disposable);
    }

    /**
     * 显示广告统计广告
     */
    public void onAdshow(final AdListenerSplashFull adListener) {
        Log.i("onAdshow", "显示广告请求开始URL=" + AD_SHOW_COUNT_URL);
        // 入参
        LinkedHashMap<String, String> httpParams = new LinkedHashMap<>();
        Disposable disposable = EasyHttp.put(AD_SHOW_COUNT_URL)
                .upObject(httpParams)
                .retryCount(1)//本次请求重试次数
                .addConverterFactory(GsonConverterFactory.create())
                .execute(new CallBack<String>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted() {
                        // 显示广告
                        if (adListener != null) {
                            adListener.onAdPrepared(AdNameType.TB);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.i("onAdshow", "onSuccess s=" + s);
                    }
                });
        addDispose(disposable);
    }

    /**
     * 下载文件
     *
     * @param url 下载路径
     */
    public void downFile(final Context context, String url) {
        Log.i("TibiAdHttp", "downFile is url =" + url);
        final String savePath = Environment.getExternalStorageDirectory().toString() + "/safe/ad/";
        final String fileName = url.split("/")[url.split("/").length - 1];
        String filePath = savePath + fileName;
        if (FileUtil.checkFile(filePath)) {
            // 文件存在
            FileUtil.openFile(context, filePath);
        } else {
            // 下载文件
            final ProgressDialog dialog = new ProgressDialog(context);
            // 设置进度条的形式为圆形转动的进度条
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("正在下载...");
            // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
            dialog.setTitle("下载文件");
            dialog.setMax(100);
            Disposable disposable = EasyHttp.downLoad(url)
                    .savePath(savePath)
                    // 不设置默认名字是时间戳生成的
                    .saveName(fileName)
                    .execute(new DownloadProgressCallBack<String>() {
                        @Override
                        public void update(long bytesRead, long contentLength, boolean done) {
                            int progress = (int) (bytesRead * 100 / contentLength);
                            HttpLog.e(progress + "% ");
                            dialog.setProgress(progress);
                        }

                        @Override
                        public void onStart() {
                            // 开始下载
                            if (dialog != null && !dialog.isShowing()) {
                                dialog.show();
                            }
                        }

                        @Override
                        public void onComplete(String path) {
                            Log.i("TibiAdHttp", "onComplete 下载完成path =" + path);
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            // 下载完成，path：下载文件保存的完整路径
                            if (!StringUtil.isNullOrEmpty(path)) {
                                // 打开文件
                                FileUtil.openFile(context, path);
                            }
                        }

                        @Override
                        public void onError(ApiException e) {
                            Toast.makeText(context, "文件下载失败！", Toast.LENGTH_SHORT).show();
                            // 下载失败
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    });
            addDispose(disposable);
        }
    }

    /**
     * 取消网络连接
     */
    @Override
    public void unDispose() {
        super.unDispose();
    }
}
