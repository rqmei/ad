package com.ad.tibi.lib.util;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.ad.tibi.lib.helper.splash.inter.AdListenerSplashFull;
import com.ad.tibi.lib.http.TibiAdHttp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageLoadUtil {
    /**
     * @param mContext
     * @param imageUrl  图片网络路径
     * @param imageView 图片控件
     * @param listener  回调
     */
    public static void loadImage(Context mContext, String imageUrl, ImageView imageView, final AdListenerSplashFull listener) {
////设置图片圆角角度
//        RoundedCorners roundedCorners= new RoundedCorners(6);
////通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//        RequestOptions options=RequestOptions.bitmapTransform(roundedCorners).override(300, 300)
        Glide.with(mContext).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                       boolean isFirstResource) {
                // 图片加载失败
                if (listener != null) {
                    listener.onAdFailed(AdNameType.TB);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model,
                                           Target<GlideDrawable> target, boolean isFromMemoryCache,
                                           boolean isFirstResource) {
                Log.i("loadImage", "onResourceReady=");
                // 图片加载完成
                TibiAdHttp.getSingleAdHttp().onAdshow(listener);
                return false;
            }
        }).into(imageView);
    }
}
