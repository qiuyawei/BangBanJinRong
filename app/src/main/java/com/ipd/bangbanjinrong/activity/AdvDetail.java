package com.ipd.bangbanjinrong.activity;

import android.view.View;
import android.webkit.WebView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.AdvEntity;

import org.xutils.view.annotation.ViewInject;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 广告详情
 */

public class AdvDetail extends BaseActivity {
    private AdvEntity advEntity;
    @ViewInject(R.id.webView)
    private WebView webView;
    @Override
    public int setLayout() {
        return R.layout.adv_detail;
    }

    @Override
    public void innitData() {
        advEntity= (AdvEntity) getIntent().getSerializableExtra("entity");
        setBack();
        setTopTitle(advEntity.PICTURENAME);
        webView.loadDataWithBaseURL(null, advEntity.CONTENT, "text/html", "utf-8", null);

    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }




}
