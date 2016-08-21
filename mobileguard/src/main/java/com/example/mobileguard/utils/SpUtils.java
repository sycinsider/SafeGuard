
package com.example.mobileguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ClassName:SpUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月6日 下午8:28:36 <br/>
 * 
 * @author dell
 * @version
 */
public class SpUtils {
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultvalue) {
        SharedPreferences sp =
                context.getSharedPreferences(Constantset.SP_FILE_NAME, Context.MODE_PRIVATE);

        return sp.getBoolean(key, defaultvalue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp =
                context.getSharedPreferences(Constantset.SP_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key, String defValuevalue) {
        SharedPreferences sp =
                context.getSharedPreferences(Constantset.SP_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValuevalue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp =
                context.getSharedPreferences(Constantset.SP_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defValuevalue) {
        SharedPreferences sp =
                context.getSharedPreferences(Constantset.SP_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValuevalue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp =
                context.getSharedPreferences(Constantset.SP_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }
}
