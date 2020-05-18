package com.ad.tibi.lib.util;

public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0 || str.equals("null")||str.equals("NULL");
    }
}
