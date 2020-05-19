package com.ad.tibi.lib.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ad.tibi.lib.R;
import com.ad.tibi.lib.interf.AdListenerSplashFull;
import com.ad.tibi.lib.interf.PermissionListener;
import com.ad.tibi.lib.interf.TimeListener;
import com.ad.tibi.lib.util.AdNameType;
import com.ad.tibi.lib.util.DensityUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


/**
 * 自定义闪屏广告view
 */
public class AdSplashView extends FrameLayout {
    private Context mContext;
    private ImageView imageView;
    private CountDownView countDownView;
    ProgressDialog progressDialog;

    private final int defCircleBg = 0x583d3d3d;
    private final int defTextColor = 0xffefefef;
    private final int defTextSize = 12;//sp
    private final int defProgressColor = 0xffff0000;
    private final int defProgressWidth = 3;// dp
    private final int defTime = 3000; //单位 ms
    private final int defRadius = 25; //单位 dp

    private final int defLoadingColor = 0xffff0000;
    private final int defLoadingRadius = 25;
    private final int defLoadingWidth = 2;


    private boolean time_view_show;
    private boolean time_show;
    private int time_bg;
    private int time_text_color;
    private int time_text_size;
    private int time_pro_color;
    private int time_pro_width;
    private int time_radius;
    private int time_num;

    private AdListenerSplashFull advertListener;

    public void setAdvertListener(AdListenerSplashFull advertListener) {
        this.advertListener = advertListener;
    }

    public AdSplashView(Context context) {
        this(context, null);
    }

    public AdSplashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdSplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdvertView,
                defStyleAttr, 0);
        time_bg = typedArray.getColor(R.styleable.AdvertView_ad_time_bg, defCircleBg);

        time_pro_color = typedArray.getColor(R.styleable.AdvertView_ad_time_pro_color, defProgressColor);
        time_pro_width = typedArray.getDimensionPixelSize(R.styleable.AdvertView_ad_time_pro_width,
                DensityUtils.dp2px(context, defProgressWidth));
        time_text_color = typedArray.getColor(R.styleable.AdvertView_ad_time_text_color, defTextColor);
        time_text_size = typedArray.getDimensionPixelSize(R.styleable.AdvertView_ad_time_text_size,
                DensityUtils.sp2px(context, defTextSize));
        time_num = typedArray.getInteger(R.styleable.AdvertView_ad_time_num, defTime);
        time_radius = typedArray.getDimensionPixelSize(R.styleable.AdvertView_ad_time_radius,
                DensityUtils.dp2px(context, defRadius));


        time_show = typedArray.getBoolean(R.styleable.AdvertView_ad_time_show, false);
        time_view_show = typedArray.getBoolean(R.styleable.AdvertView_ad_time_view_show, true);

        typedArray.recycle();

        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.advert_layout, this);
        imageView = view.findViewById(R.id.image);
        countDownView = view.findViewById(R.id.cd_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("");

        countDownView.setCircleBg(time_bg);
        countDownView.setProgressColor(time_pro_color);
        countDownView.setProgressWidth(time_pro_width);
        countDownView.setTextColor(time_text_color);
        countDownView.setTextSize(time_text_size);
        countDownView.setTime(time_num);
        countDownView.setRadius(time_radius);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击广告
                if (advertListener != null) {
                    advertListener.onAdClick(AdNameType.TB);
                    countDownView.stop();
                }
            }
        });

        countDownView.setTimeListener(new TimeListener() {
            @Override
            public void onDismiss() {
                // 倒计时结束
                if (advertListener != null) {
                    advertListener.onAdDismissed();
                }
            }
        });
    }

    public void setTime_view_show(boolean time_view_show) {
        this.time_view_show = time_view_show;
        if (time_show) {
            countDownView.setVisibility(View.VISIBLE);
        } else {
            countDownView.setVisibility(View.GONE);
        }
    }

    public void setTime_show(boolean time_show) {
        this.time_show = time_show;
        if (time_show) {
            countDownView.setShowTime(time_show);
            countDownView.setVisibility(View.VISIBLE);
        } else {
            countDownView.setVisibility(View.GONE);
        }
    }

    public void setTime_bg(int time_bg) {
        this.time_bg = time_bg;
        countDownView.setCircleBg(time_bg);
    }

    public void setTime_text_color(int time_text_color) {
        this.time_text_color = time_text_color;
        countDownView.setTextColor(time_text_color);
    }

    public void setTime_text_size(int time_text_size) {
        this.time_text_size = DensityUtils.sp2px(mContext, time_text_size);
        countDownView.setTextSize(this.time_text_size);
    }

    public void setTime_pro_color(int time_pro_color) {
        this.time_pro_color = time_pro_color;
        countDownView.setProgressColor(time_pro_color);
    }

    public void setTime_pro_width(int time_pro_width) {
        this.time_pro_width = DensityUtils.dp2px(mContext, time_pro_width);
        countDownView.setProgressWidth(this.time_pro_width);
    }

    public void setTime_radius(int time_radius) {
        this.time_radius = DensityUtils.dp2px(mContext, time_radius);
        countDownView.setRadius(this.time_radius);
    }

    public void setTime_num(int time_num) {
        this.time_num = time_num;
        countDownView.setTime(time_num);
    }

    private void startTime() {
        if (time_view_show) {
            countDownView.setVisibility(View.VISIBLE);
            countDownView.showStartTime();
        } else {
            countDownView.setVisibility(View.GONE);
            countDownView.unShowStart();
        }
    }

    /**
     * 设置广告图片
     *
     * @param resourceId
     */
    public void setImage(int resourceId) {
        if (imageView != null) {
            imageView.setImageResource(resourceId);
        }
        startTime();
    }

    /**
     * 设置广告图片
     * @param bitmap
     */
    public void setImage(Bitmap bitmap) {
        if (imageView != null) {
            imageView.setVisibility(VISIBLE);
            imageView.setImageBitmap(bitmap);
        }
        startTime();
    }

    /**
     * 设置广告图片
     * @param httpUrl 图片网络路径
     */
    public void setImage(String httpUrl) {
        if (imageView != null) {
            imageView.setVisibility(VISIBLE);
//            Glide.with(mContext).load(httpUrl).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(GlideException e, Object model,
//                                            Target<Drawable> target, boolean isFirstResource) {
//                    // 图片加载完成
//                    if (advertListener != null) {
//                        advertListener.onAdPrepared(AdNameType.TB);
//                    }
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model,
//                                               Target<Drawable> target, DataSource dataSource,
//                                               boolean isFirstResource) {
//                    // 图片加载完成
//                    if (advertListener != null) {
//                        advertListener.onAdFailed(AdNameType.TB);
//                    }
//                    return false;
//                }
//            }).into(imageView);
        }
        startTime();
    }

    /**
     * 申请权限
     *
     * @param permissionListener
     * @return
     */
    private boolean checkPermission(PermissionListener permissionListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            int checkReadResutl = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
            boolean hasReadPermission = checkReadResutl == PackageManager.PERMISSION_GRANTED;
            if (!hasReadPermission) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            int checkWriteResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean hasWritePermission = checkWriteResult == PackageManager.PERMISSION_GRANTED;
            if (!hasWritePermission) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() > 0) {
                permissionListener.onPermission(permissions);
                return true;
            }
        }
        return false;
    }
}
