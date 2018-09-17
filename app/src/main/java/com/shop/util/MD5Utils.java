package com.shop.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by victoryf on 2018-07-06.
 */
public class MD5Utils {

    public static String md5(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
