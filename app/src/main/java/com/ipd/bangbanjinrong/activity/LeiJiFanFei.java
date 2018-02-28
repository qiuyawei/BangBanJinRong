package com.ipd.bangbanjinrong.activity;

import android.view.View;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 累计返费
 */

public class LeiJiFanFei extends BaseActivity {
    @Override
    public int setLayout() {
        return R.layout.lei_ji_fan_fei;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.ljff));
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }
}
