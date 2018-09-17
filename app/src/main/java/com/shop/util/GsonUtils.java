package com.shop.util;

import android.text.TextUtils;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by victoryf on 2018-06-28.
 */
public class GsonUtils {

    public static List<Map<String, Object>> parseArrayGson(String json) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Iterator<String> iterator = jsonObject.keys();
                    Map<String, Object> map = new HashMap<>();
                    while (iterator.hasNext()) {
                        String keys = iterator.next();
                        map.put(keys, jsonObject.get(keys));
                    }
                    dataList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    public static JSONArray listToArray(List<Map<String, Object>> dataList) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        if (dataList == null)
            return jsonArray;
        if (dataList.size() <= 0)
            return jsonArray;

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            Set<String> stringSet = map.keySet();
            JSONObject jsonObject = new JSONObject();
            for (String key :
                    stringSet) {
                jsonObject.put(key, map.get(key));
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

   /* public static JSONArray listToArray(List<Map<String, String>> dataList) throws JSONException {
        if (dataList == null)
            return null;
        if (dataList.size() <= 0)
            return null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> map = dataList.get(i);
            Set<String> stringSet = map.keySet();
            JSONObject jsonObject = new JSONObject();
            for (String key :
                    stringSet) {
                jsonObject.put(key, map.get(key));
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
*/
    public static JSONObject mapToArray(Map<String, Object> data) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (data == null)
            return jsonObject;
        Map<String, Object> map = data;
        Set<String> stringSet = map.keySet();

        for (String key :
                stringSet) {
            jsonObject.put(key, map.get(key));
        }
        return jsonObject;
    }

    public static Map<String, Object> parseJsonObject(String json) {
        Map<String, Object> mapData = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                mapData.put(key, jsonObject.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapData;
    }
}
