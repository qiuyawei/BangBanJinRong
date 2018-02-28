package com.ipd.bangbanjinrong.utils;

import android.util.Log;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/25
 * @Email 448739075@qq.com
 * 打印帮助类
 */

public class LogUtils {
    private static boolean ifLog=true;

    /**
     * @param tag
     * @param content
     * 测试时候打印   正式版本关闭打印提高性能
     */
    public static void print(String tag,String content){
        if(ifLog){
            Log.i("TAG",tag+"="+content);
        }
    }
}
