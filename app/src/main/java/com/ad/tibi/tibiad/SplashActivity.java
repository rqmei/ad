package com.ad.tibi.tibiad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.helper.splash.TibiAdSplash;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.mFlAdContainer)
    FrameLayout mFlAdContainer;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.skip_view)
    RelativeLayout skipView;
    private boolean canJumpImmediately = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        requestAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJumpImmediately) {
            actionHome(0);
        }
        canJumpImmediately = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJumpImmediately = false;
    }

    private void requestAd() {
        String splashConfigAd = "baidu:0,gdt:1,csj:1";
        TibiAdSplash.getSingleAdSplash().showAdFull(
                this,
                splashConfigAd,
                AdInit.getSingleAdInit().getIdMapCsj().get(AdConst.AD_SPLASH),
                mFlAdContainer,
                null,null,
                new TibiAdSplash.AdListenerSplashFull() {
                    @Override
                    public void onStartRequest(String var1) {
                        Log.e("ifmvo", "onStartRequest:channel:$channel");
                    }

                    @Override
                    public void onAdClick(String var1) {
                        Log.e("ifmvo", "onAdClick:channel:$channel");
                    }

                    @Override
                    public void onAdFailed(String var1) {
                        Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg");
                        actionHome(0);
                    }

                    @Override
                    public void onAdDismissed() {
                        if (canJumpImmediately) {
                            actionHome(0);
                        }
                        canJumpImmediately = true;
                    }

                    @Override
                    public void onAdPrepared(String var1) {
                        Log.e("ifmvo", "onAdPrepared:channel:$channel");
                    }

                });
    }
    private void actionHome(long delayMillis){
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }
}
