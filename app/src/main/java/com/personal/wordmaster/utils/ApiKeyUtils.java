package com.personal.wordmaster.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.personal.wordmaster.WordMasterApp;

public class ApiKeyUtils {

    private static final String PREFS_NAME = "wordmaster_config";
    private static final String KEY_API_KEY = "deepseek_api_key";

    private ApiKeyUtils() {
    }

    private static SharedPreferences prefs() {
        return WordMasterApp.getInstance()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static String getApiKey() {
        return prefs().getString(KEY_API_KEY, "");
    }

    public static void saveApiKey(String key) {
        prefs().edit().putString(KEY_API_KEY, key).apply();
    }

    public static void deleteApiKey() {
        prefs().edit().remove(KEY_API_KEY).apply();
    }

    public static boolean hasApiKey() {
        String key = getApiKey();
        return key != null && !key.trim().isEmpty();
    }
}
