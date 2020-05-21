package com.ad.tibi.tibiad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.helper.banner.TibiAdBanner;
import com.ad.tibi.lib.helper.image.TibiAdImageText;
import com.ad.tibi.lib.helper.inter.TibiAdInter;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.fl_ad_iamge_text)
    FrameLayout flAdIamgeText;
    @BindView(R.id.fl_banner)
    FrameLayout flBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        TibiAdBanner.getSingleAdBanner()
                .showAdBanner(this,
                        "tb:1",
                        AdConst.AD_WEBVIEW_BANNER,
                        flBanner, 160,
                        adListenerSplashFull);
        TibiAdInter.getSingleAdInter().showAdInter(this,
                "tb:0,gdt:1",
                AdConst.AD_INTER,
                adListenerSplashFull);
//        TibiAdImageText.getSingleAdImageText().showAdImageTextTb(this, flAdIamgeText, adListenerSplashFull);
    }

    AdListenerSplashFull adListenerSplashFull = new AdListenerSplashFull() {
        @Override
        public void onStartRequest(String var1) {

        }

        @Override
        public void onAdClick(String var1) {

        }

        @Override
        public void onAdFailed(String var1) {

        }

        @Override
        public void onAdDismissed() {

        }

        @Override
        public void onAdPrepared(String var1) {

        }
    };
}
