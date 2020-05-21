package com.ad.tibi.tibiad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.helper.splash.TibiAdSplash;
import com.ad.tibi.lib.http.TibiAdHttp;
import com.ad.tibi.lib.imgad.ImageAdEntity;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {
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
    /**
     * 是否能跳转
     */
    private boolean canJump = false;

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
        if (canJump) {
            next(0);
        }
        canJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TibiAdHttp.getSingleAdHttp().unDispose();
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void requestAd() {
        String splashConfigAd = "baidu:0,gdt:1,csj:1";
        TibiAdSplash.getSingleAdSplash().showAdFullTb(
                this,
                splashConfigAd,
                AdInit.getSingleAdInit().getIdMapCsj().get(AdConst.AD_SPLASH),
                mFlAdContainer,
                skipView, tvTime,
                new AdListenerSplashFull() {
                    @Override
                    public void onStartRequest(String var1) {
                        Log.e("ifmvo", "onStartRequest:channel:$channel");
                    }

                    @Override
                    public void onAdClick(String var1) {
                        ImageAdEntity imageAdEntity = AdInit.getSingleAdInit().getImageAdEntity();
                        Log.e("onAdClick", "onAdClick:"+var1+",paht="+imageAdEntity.getJumpPath());
                        // 点击
                        Log.e("onAdClick", "onAdClick:"+imageAdEntity.toString());
                    }

                    @Override
                    public void onAdFailed(String var1) {
                        Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg");
                        next(0);
                    }

                    @Override
                    public void onAdDismissed() {
                        if (canJump) {
                            next(0);
                        }
                        canJump = true;
                    }

                    @Override
                    public void onAdPrepared(String var1) {
                        Log.e("ifmvo", "onAdPrepared:channel:$channel");
                    }

                });
    }

    private void next(long delayMillis) {
        if(canJump) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            canJump = true;
        }
    }
}
