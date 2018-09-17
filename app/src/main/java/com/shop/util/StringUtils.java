package com.shop.util;

import android.text.TextUtils;

/**
 * @author victory
 * @time 2018/7/2
 * @about
 */
public class StringUtils {
    public static boolean isEmpty(String s) {
        if (TextUtils.isEmpty(s) || "null".equals(s))
            return true;
        return false;
    }

    public static boolean isNotEmpty(String s) {
        if (TextUtils.isEmpty(s) || "null".equals(s))
            return false;
        return true;
    }
}
