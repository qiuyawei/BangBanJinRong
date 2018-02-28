package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 实名认证
 */

public class NameAuthor extends BaseActivity {
    @ViewInject(R.id.bt_next)
    private Button bt_next;
    @Override
    public int setLayout() {
        return R.layout.shi_ming_ren_zheng;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.smrz));
    }

    @Override
    public void setListeners() {
        bt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                intent=new Intent(context,AddBankCard.class);
                startActivity(intent);

                break;
        }
    }
}
