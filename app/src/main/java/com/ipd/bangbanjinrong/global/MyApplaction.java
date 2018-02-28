package com.ipd.bangbanjinrong.global;

import android.app.Activity;

import com.mob.MobApplication;
import com.pgyersdk.crash.PgyCrashManager;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/4
 * @Email 448739075@qq.com
 */

public class MyApplaction extends MobApplication {
    public static MyApplaction instance;
    public static List<Activity> activities=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        初始化 xutils 框架
        x.Ext.init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
//        异常捕捉  并上传到蒲公英   可到平台查看
        PgyCrashManager.register(this);

    }


    /**
     * @return 获取全局入口实例
     */
    private static MyApplaction getInstance() {
        if (instance == null)
            instance = new MyApplaction();
        return instance;
    }


    public static void addActivity(Activity activity){
        activities.add(activity);
    }
}
