package com.ipd.bangbanjinrong.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地存储
 */
public class SharedPreferencesUtils {
    /*下标存储标记*/
    public static final String POSTION="postion";

    static  SharedPreferences sharedPreferences;
    /****字符串存储***/
    public static void putSharedPreferences(Context context,String key,String data){
            sharedPreferences = context.getSharedPreferences("config",context.MODE_PRIVATE|context.MODE_MULTI_PROCESS);
            sharedPreferences.edit().putString(key,data).commit();
    };
    public static String getSharedPreferences(Context context,String key){
        sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE|context.MODE_MULTI_PROCESS);
        return sharedPreferences.getString(key, "");

    };
    /****布尔值存储***/
    public  static void putbooleanSharedPreferences(Context context, String key, boolean flag) {
        sharedPreferences = context.getSharedPreferences("config",context.MODE_PRIVATE|context.MODE_MULTI_PROCESS);
        sharedPreferences.edit().putBoolean(key, flag).commit();
    }
    public static boolean getbooleanSharedPreferences(Context context,String key){
        sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE|context.MODE_MULTI_PROCESS);
        return sharedPreferences.getBoolean(key, false);

    };

    /****整形存储***/
    public static void putIntSharedPreferences(Context context,String key,int data){
        sharedPreferences = context.getSharedPreferences("config",context.MODE_APPEND|context.MODE_MULTI_PROCESS);
        sharedPreferences.edit().putInt(key,data).commit();
    };
    public static int getIntSharedPreferences(Context context,String key){
        sharedPreferences = context.getSharedPreferences("config", context.MODE_APPEND|context.MODE_MULTI_PROCESS);
        return sharedPreferences.getInt(key, 0);

    };
}
