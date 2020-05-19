package com.ad.tibi.lib.imgad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.ad.tibi.lib.R;
import com.ad.tibi.lib.util.StringUtil;

/**
 * ImageAd
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author yelian on 2020/5/19
 */
public class ImageAd {
    View view;
    Context context;
    ImageAdEntity vo;
    ImageAdInterface event;
    
    public ImageAd(Context context,ImageAdEntity vo,ImageAdInterface event){
        this.context = context;
        this.vo = vo;
        this.event = event;
    }
    
    public View getView() {
        if(vo == null){
            return  null;
        }
        if(StringUtil.isNullOrEmpty(vo.getUrl())){
            return  null;
        }
        if(view == null){
           View  parent = LayoutInflater.from(context).inflate(R.layout.view_ad_image, null);
           view = parent.findViewById(R.id.ad_image);
           // todo 加载图片
            
           if(vo.getHeight() > 0 && vo.getWidth() > 0 ){
               //  设置图片宽高
               //设置图片的位置
               ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(
                       view.getLayoutParams());
               margin9.setMargins(0, 0, 0, 0);//设置margin
               RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
               layoutParams9.height = vo.getHeight();//设置图片的高度
               layoutParams9.width = vo.getWidth(); //设置图片的宽度
               view.setLayoutParams(layoutParams9);
           }
           
           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(event != null ){
                       if(StringUtil.isNotNullOrEmpty(vo.getJumpPath())){
                           //  跳转网页
                           event.jumpToWeb(vo.getJumpPath());
                       }

                       if(StringUtil.isNotNullOrEmpty(vo.getDownloadPath())){
                           // 下载广告apk
                           event.download(vo.getDownloadPath());
                       }
                   }
                  
               }
           });
        }
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setVo(ImageAdEntity vo) {
        this.vo = vo;
    }

    public void setEvent(ImageAdInterface event) {
        this.event = event;
    }
}
