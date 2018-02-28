package com.ipd.bangbanjinrong.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseFragment;
import com.ipd.bangbanjinrong.entity.RegisterRulerEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import static com.ipd.bangbanjinrong.base.BaseActivity.noNet;

/**
 * @author qiu_ya_wei
 * @Date 2017/9/12
 * @Email 448739075@qq.com
 * 产品      后加上的
 */

public class FragmentFour extends BaseFragment {
    private RegisterRulerEntity registerRulerEntity;

    @ViewInject(R.id.webView)
    private WebView webView;
    @Override
    public View setView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_four,null);
    }

    @Override
    public void innitData(Bundle savedInstanceState) {
        getAbout();
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 获取产品h5
     *
     */
    private void getAbout() {
        noNet(context);
        String[] keys = {"CATEGORY"};
        String[] values = {"3"};
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
