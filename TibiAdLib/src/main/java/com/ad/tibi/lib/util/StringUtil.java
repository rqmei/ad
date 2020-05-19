package com.ad.tibi.lib.util;

public class StringUtil {
    /**
     * 判断字符串为空
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0 || str.equals("null")||str.equals("NULL");
    }

    /**
     * 判断字符串不为空
     * @param str
     * @return
     */
    public static boolean isNotNullOrEmpty(String str){
        return str != null && str.length() > 0 ;
    }
}
