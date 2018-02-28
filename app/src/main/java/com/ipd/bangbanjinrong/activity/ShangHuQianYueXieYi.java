package com.ipd.bangbanjinrong.activity;

import android.view.View;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.RegisterRulerEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 商户授权协议
 */

public class ShangHuQianYueXieYi extends BaseActivity {
    private RegisterRulerEntity registerRulerEntity;

    @ViewInject(R.id.webView)
    private WebView webView;
    @Override
    public int setLayout() {
        return R.layout.shou_quan_xie_yi;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle("商户授权协议");
        getAbout();
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取商户授权协议
     */
    private void getAbout() {
        noNet(context);
        String[] keys = {"CATEGORY"};
        String[] values = {"2"};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_text/get.do");
        params.setBodyContent(json);
        dialog();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Gson gson = new Gson();
                    registerRulerEntity = gson.fromJson(jsonObject.getString("data"), RegisterRulerEntity.class);
                    loadWebView(registerRulerEntity.CONTENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


    private void loadWebView(String data) {
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }
}
