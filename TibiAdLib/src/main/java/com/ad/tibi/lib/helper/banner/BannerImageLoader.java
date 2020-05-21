package com.ad.tibi.lib.helper.banner;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.ad.tibi.lib.R;
import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;
import com.ad.tibi.lib.util.ImageLoadUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * 包名：com.timingbar.android.safe.util
 * 类名：$CLASS_NAME$
 * -------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2020/4/21
 * Description:
 */
public class BannerImageLoader extends ImageLoader {
    AdListenerSplashFull adListener;

    public BannerImageLoader(AdListenerSplashFull adListener) {
        Log.i("BannerImageLoader", "显示广告=");
        this.adListener = adListener;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Log.i("BannerImageLoader", "显示广告请求开始URL=" +path);
        ImageLoadUtil.loadImage(context, String.valueOf(path), imageView, adListener);
    }

}
