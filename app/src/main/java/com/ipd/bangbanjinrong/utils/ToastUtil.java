package com.ipd.bangbanjinrong.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/4
 * @Email 448739075@qq.com
 * 土司工具类
 */

public class ToastUtil {
    /**
     * @param context
     * @param content
     * 短时间吐司
     */
    public static void showShort(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @param content
     * 长时间吐司
     */
    public static void showLong(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_LONG).show();
    }
}
