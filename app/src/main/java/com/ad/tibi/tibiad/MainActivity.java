package com.ad.tibi.tibiad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.ad.tibi.lib.AdInit;
import com.ad.tibi.lib.helper.inter.TibiAdInter;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ll_main)
    LinearLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        TibiAdInter.getSingleAdInter().showAdInter(this,
                "tb:1,csj:0",
                AdInit.getSingleAdInit().getIdMapCsj().get(AdConst.AD_INTER),
                new AdListenerSplashFull() {
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
                });
    }
}
