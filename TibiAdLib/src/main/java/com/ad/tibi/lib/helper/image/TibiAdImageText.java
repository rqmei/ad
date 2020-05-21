package com.ad.tibi.lib.helper.image;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.R;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;
import com.ad.tibi.lib.http.TibiAdHttp;
import com.ad.tibi.lib.imgad.ImageAdEntity;
import com.ad.tibi.lib.util.AdNameType;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

/**
 * 图文广告
 */
public class TibiAdImageText {
    static TibiAdImageText adImageText;

    public static TibiAdImageText getSingleAdImageText() {
        if (adImageText == null) {
            synchronized (AdInit.class) { // 避免不必要的同步
                if (adImageText == null) { // 在null的情况下创建实例
                    adImageText = new TibiAdImageText();
                }
            }
        }
        return adImageText;
    }

    /**
     * 显示替比原生
     */
    public void showAdImageTextTb(final Activity activity, final ViewGroup adsParentLayout,
                                  final AdListenerSplashFull adListener) {
        Log.i("showAdImageTextTb", "tb图文广告");
        TibiAdHttp.getAdInfo("my/current/notice", new CallBack<ImageAdEntity>() {
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
            }

            @Override
            public void onSuccess(ImageAdEntity imageAdEntity) {
                Log.i("showAdFullTb", "result=" + imageAdEntity);
                imageAdEntity = new ImageAdEntity();
                // 请求替比广告成功
                // 设置广告view
                View view = LayoutInflater.from(activity).inflate(R.layout.tb_ad_image_text, null);
                ImageView ivAdLogo = view.findViewById(R.id.iv_ad_logo);
                TextView tvAdContent = view.findViewById(R.id.tv_ad_content);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 点击
                        if (adListener != null) {
                            adListener.onAdClick(AdNameType.TB);
                        }
                    }
                });
                //设置当前广告信息
                AdInit.getSingleAdInit().setImageAdEntity(imageAdEntity);
                // 加入广告
                adsParentLayout.removeAllViews();
                adsParentLayout.addView(view);
            }
        });
    }
}
