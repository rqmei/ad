package com.ad.tibi.lib.util;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 广告随机获取工具类
 */
public class AdRandomUtil {
    public static String getRandomAdName(String configStr) {
        Log.i("AdRandomUtil", "广告的配置:" + configStr);
        if (StringUtil.isNullOrEmpty(configStr)) {
            return AdNameType.NO;
        }

        List<String> list = new ArrayList<>();
        //{baidu:2},{gdt:8}
        String[] split = configStr.split(",");
        for (int i = 0; i < split.length; i++) {
            String itemStr = split[i];
            //不能为空
            if (itemStr.isEmpty()) continue;
            String[] splitKeyValue = itemStr.split(":");
            //必须分割两份才正确
            if (splitKeyValue.length != 2) continue;
            //"baidu:2"
            String keyStr = splitKeyValue[0];
            String valueStr = splitKeyValue[1];
            //都不能为空
            if (keyStr.isEmpty() || valueStr.isEmpty()) continue;
            //加到 list 里面 2 个 "baidu"
            int len = Integer.parseInt(valueStr);
            if (len > 0) {
                for (int j = 0; j < len; j++) {
                    list.add(keyStr);
                }
            }
        }
        //没有匹配的
        String adNameType = AdNameType.NO;
        if (list.size() > 0) {
            //在list里面随机选择一个
            Random random = new Random();
            int index = random.nextInt(list.size());
            adNameType = list.get(index);
        }
        Log.i("AdRandomUtil", "随机到的广告:" + adNameType);
        return adNameType;
    }

}
