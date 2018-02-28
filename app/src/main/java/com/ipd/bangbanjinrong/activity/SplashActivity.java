package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/25
 * @Email 448739075@qq.com
 * 闪图页
 */

public class SplashActivity extends BaseActivity {
    @Override
    public int setLayout() {
        return R.layout.splash;
    }

    @Override
    public void innitData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID).equals("")){
                    startActivity(new Intent(context, LoginActivity.class));
                }else{
                    startActivity(new Intent(context, MainActivity.class));
                }
                finish();
            }
        },2000);


    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }
}
