package com.ad.tibi.lib.helper.banner;

import android.content.Context;
import android.widget.ImageView;

import com.ad.tibi.lib.R;
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
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                // 预加载图片
                .centerCrop()
                .placeholder(R.drawable.tt_ad_cover_btn_begin_bg)
                // 优先级
                .priority(Priority.HIGH)
                // 缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        return super.createImageView(context);
    }
}
