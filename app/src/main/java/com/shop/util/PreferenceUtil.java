package com.shop.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.shop.MyApplication;

/**
 * 首选项 工具类
 */
public class PreferenceUtil {

    private static final String PREFER_NAME = "sunshine";
    public static final String PREFER_URL_NAME = "HttpUrl";
    public static final String USERPWD = "userpwd";
    public static final String USERNAME = "username";
    public static final String USERID = "userid";
    public static final String USERPHONE = "userphone";
    public static final String ADDR = "address";

    private static SharedPreferences mSharedPreferences;

    private static synchronized SharedPreferences getPreferneces() {
        if (mSharedPreferences == null) {
            mSharedPreferences = MyApplication.getInstance().getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void putParam(String key, Object object) {
        if (PREFER_URL_NAME.equals(key)) {
            Log.i("FFF", " --- : --- " + object);
        }
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = getPreferneces();
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        Object obj = null;
        SharedPreferences sp = getPreferneces();
        Log.i("getParam", "type: " + type);
        if ("String".equals(type)) {
            obj = sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            obj = sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            obj = sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            obj = sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            obj = sp.getLong(key, (Long) defaultObject);
        }
        Log.i("VVV", " key : " + key + "类型 :　" + defaultObject.getClass().getSimpleName() + " 默认值 : " + defaultObject + " 取值 : " + obj);
        return obj;
    }

    /**
     * 打印SharePreference下所有数据
     */
    public static void print() {
        System.out.println(getPreferneces().getAll());
    }

    /**
     * 清空保存在默认SharePreference下的所有数据
     */
    public static void clear() {
        getPreferneces().edit().clear().commit();
    }

    /**
     * 移除SharePreference下的数据
     *
     * @param key
     */
    public static void removeString(String key) {
        getPreferneces().edit().remove(key).commit();
    }
}
