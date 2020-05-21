package com.ad.tibi.tibiad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.helper.splash.TibiAdSplash;
import com.ad.tibi.lib.http.TibiAdHttp;
import com.ad.tibi.lib.imgad.ImageAdEntity;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;

import java.util.ArrayList;
import java.util.List;

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
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            requestAd();
        }
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
        String splashConfigAd = "baidu:0,gdt:1,csj:0";
        TibiAdSplash.getSingleAdSplash().showAdFull(
                this,
                splashConfigAd,
                AdConst.AD_SPLASH,
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

    /**
     *
     * ----------非常重要----------
     *
     * Android6.0以上的权限适配简单示例：
     *
     * 如果targetSDKVersion >= 23，那么建议动态申请相关权限，再调用广点通SDK
     *
     * SDK不强制校验下列权限（即:无下面权限sdk也可正常工作），但建议开发者申请下面权限，尤其是READ_PHONE_STATE权限
     *
     * READ_PHONE_STATE权限用于允许SDK获取用户标识,
     * 针对单媒体的用户，允许获取权限的，投放定向广告；不允许获取权限的用户，投放通投广告，媒体可以选择是否把用户标识数据提供给优量汇，并承担相应广告填充和eCPM单价下降损失的结果。
     *
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 快手SDK所需相关权限，存储权限，此处配置作用于流量分配功能，关于流量分配，详情请咨询商务;如果您的APP不需要快手SDK的流量分配功能，则无需申请SD卡权限
        if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )){
            lackedPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!(checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // 如果需要的权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            requestAd();
        } else {
            // 否则，建议请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            requestAd();
        } else {
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }
}
