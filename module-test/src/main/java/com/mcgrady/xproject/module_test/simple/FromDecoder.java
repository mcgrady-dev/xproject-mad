package com.mcgrady.xproject.module_test.simple;

import android.os.Build;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mcgrady on 2/25/21.
 */
public class FromDecoder {

    private Map<String, String> parameters;

    public FromDecoder(String str) {
        this.parameters = new HashMap<>();
        parse(this.parameters, str);
    }

    public String get(String key) {
        return this.get(key, null);
    }

    public String get(String key, String defaultValue) {
        String value = this.parameters.get(key);
        return TextUtils.isEmpty(value) ? defaultValue : value;
    }

    private int getInt32(String key) {
        return this.getInt32(key, 0);
    }

    private int getInt32(String key, int defaultValue) {
        String value = this.parameters.get(key);
        return TextUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }

    public long getInt64(String key) {
        return this.getInt64(key, 0L);
    }

    private long getInt64(String key, long defaultValue) {
        String value = this.parameters.get(key);
        return TextUtils.isEmpty(value) ? defaultValue : Long.parseLong(value);
    }

    private float getFloat32(String key) {
        return this.getFloat32(key, 0F);
    }

    private float getFloat32(String key, float defaultValue) {
        String value = this.parameters.get(key);
        return TextUtils.isEmpty(value) ? defaultValue : Float.parseFloat(value);
    }

    public double getFloat64(String key) {
        return this.getFloat64(key, 0D);
    }

    private double getFloat64(String key, double defaultValue) {
        String value = this.parameters.get(key);
        return TextUtils.isEmpty(value) ? defaultValue : Double.parseDouble(value);
    }

    private boolean getBool(String key) {
        return this.getBool(key, false);
    }

    private boolean getBool(String key, boolean defaultValue) {
        String value = this.parameters.get(key);
        return TextUtils.isEmpty(value) ? defaultValue : Boolean.parseBoolean(value);
    }

    private boolean contains(String key) {
        return this.parameters.containsKey(key);
    }

    private static void parse(Map<String, String> map, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.stream(str.split("&"))
                    .filter(kv -> kv.contains("="))
                    .map(kv -> kv.split("="))
                    .forEach(array -> map.put(array[0], array[1]));
        } else {
            if (str.contains("&")) {
                final String[] pairs = str.split("&");
                for (String s : pairs) {
                    if (s.contains("=")) {
                        final String pair = s;
                        final String[] keyValue = pair.split("=");
                        map.put(keyValue[0], keyValue[1]);
                    }
                }
            } else if (str.contains("=")) {
                final String pair = str;
                final String[] keyValue = pair.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
        }
    }


}