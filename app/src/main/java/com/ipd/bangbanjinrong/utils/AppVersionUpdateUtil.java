package com.ipd.bangbanjinrong.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author qiu_ya_wei
 * @Date 2017/9/6
 * @Email 448739075@qq.com
 * 检查app 是否更新
 */

public class AppVersionUpdateUtil {

    /**
     * @param context
     * @return 返回app 版本号
     */
    public  static String  getServerAppVersion(Context context){
        String  currAppVersion="1.01";
        try {
             currAppVersion=context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(),0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currAppVersion;

    }
}
