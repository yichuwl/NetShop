package com.shop.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author victory
 * @time 2018/6/25
 * @about
 */

public class OkUtils {

    OkHttpClient mHttpClient;

    private OkUtils() {
        if (mHttpClient == null) {
            int cacheSize = 10 * 1024 * 1024;
            mHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
        }
    }

    private static OkUtils mUtils;

    public static OkUtils getOkUtilsInstance() {
        if (mUtils == null) {
            mUtils = new OkUtils();
        }
        return mUtils;
    }

    public OkUtils setNewClient() {
        int cacheSize = 10 * 1024 * 1024;
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        return mUtils;
    }

    /**
     * HTTP get请求 若有参数的话，参数拼接在URL后面
     *
     * @param url
     * @param callback
     */
    public void httpGet(String url, Callback callback) {
        Log.i("url", url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * HTTP post 参数为string字符串
     *
     * @param requestBody
     * @param url
     * @param callback
     */
    public void httpPostString(String requestBody, String url, Callback callback) {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        mHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * HTTP post请求 参数为JSonObject，以键值对的方式提交到请求体中
     *
     * @param jsonRequest
     * @param url
     * @param callback
     * @throws JSONException
     */
    public void httpPostJson(JSONObject jsonRequest, String url, Callback callback) throws JSONException {
        FormBody.Builder builder = new FormBody.Builder();
        if (jsonRequest != null) {
            Iterator<String> keys = jsonRequest.keys();
            while (keys.hasNext()) {
                String name = keys.next();
                Object value = jsonRequest.get(name);
                builder.add(name, value + "");
            }
        }
        FormBody formBody = builder.build();
        Log.i("url", url + "---" + formBody.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        mHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 取消请求
     *
     * @param url
     */
    public void cancelRequest(String url) {
        Request request = new Request.Builder()
                .url(url) // This URL is served with a 2 second delay.
                .build();
        mHttpClient.newCall(request).cancel();
    }

    /**
     * get 请求url 拼接参数
     *
     * @param url
     * @param params
     * @return
     */
    public String getUrl(String url, HashMap<String, String> params) {
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }
}
