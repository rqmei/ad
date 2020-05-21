package com.ad.tibi.lib.http;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.ad.tibi.lib.imgad.ImageAdEntity;
import com.ad.tibi.lib.util.FileUtil;
import com.ad.tibi.lib.util.StringUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.utils.HttpLog;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 替比广告相关的网络请求
 */
public class TibiAdHttp {
    public static void initEasyHttp(Application application) {
        EasyHttp.init(application);
    }

    private static String BASE_URL = "http://mobile.safe.tb.com/mobile/";

    /**
     * 获取广告相关的数据
     *
     * @param url      路径
     * @param callBack 接口回调
     */
    public static void getAdInfo(String url, CallBack<ImageAdEntity> callBack) {
        Log.i("getAdInfo", "请求开始URL=" + BASE_URL + url);
        EasyHttp.get(url)
                .baseUrl(BASE_URL)
                .readTimeOut(30 * 1000)//局部定义读超时
                .writeTimeOut(30 * 1000)
                .connectTimeout(30 * 1000)
                .params("userId", "930643")
                .params("productCode", "prod_antubang")
                .params("cardNo", "441283198409149945")
                .timeStamp(true)
                .addConverterFactory(GsonConverterFactory.create())
                .execute(callBack);
        Log.i("getAdInfo", "pram=" + EasyHttp.getInstance().getCommonParams());
    }

    /**
     * 点击广告
     *
     * @param url 接口路径
     */
    public static void onAdClick(String url) {

    }

    /**
     * 下载文件
     *
     * @param url 下载路径
     */
    public static void downFile(final Context context, String url) {
        final ProgressDialog dialog = new ProgressDialog(context);
        // 设置进度条的形式为圆形转动的进度条
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("正在下载...");
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("下载文件");
        dialog.setMax(100);
        final String savePath = Environment.getExternalStorageDirectory().toString() + "/safe/ad/";
        final String fileName = url.split("/")[url.split("/").length - 1];
        EasyHttp.downLoad(url)
                .savePath(savePath)
                // 不设置默认名字是时间戳生成的
                .saveName(fileName)
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        int progress = (int) (bytesRead * 100 / contentLength);
                        HttpLog.e(progress + "% ");
                        dialog.setProgress(progress);
                        if (done) {
                            // 下载完成

                        }
                    }

                    @Override
                    public void onStart() {
                        //开始下载
                    }

                    @Override
                    public void onComplete(String path) {
                        //下载完成，path：下载文件保存的完整路径
                        if (!StringUtil.isNullOrEmpty(path)) {
                            // 打开文件
                            FileUtil.openFile1(context, path);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        //下载失败
                    }
                });
    }
}
