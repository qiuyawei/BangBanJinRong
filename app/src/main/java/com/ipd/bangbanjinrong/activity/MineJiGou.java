package com.ipd.bangbanjinrong.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.LoginEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;

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
 * 我的机构
 */

public class MineJiGou extends BaseActivity {
    private LoginEntity loginEntity;
    private String id;//机构id
    @ViewInject(R.id.tv_companyName)
    TextView tv_companyName;
    @ViewInject(R.id.tv_address)
    TextView tv_address;
    @ViewInject(R.id.tv_code)
    TextView tv_code;

    @Override
    public int setLayout() {
        return R.layout.mine_ji_gou;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.wo_de_ji_gou));
        id = SharedPreferencesUtils.getSharedPreferences(context, Constant.JI_GOU_ID);

        if (!TextUtils.isEmpty(id))
            getMineJiGou();
        else
            ToastUtil.showShort(context,"您还没有机构！");
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 我的机构接口
     */
    private void getMineJiGou() {
        dialog();
        String[] keys = {"ORGAN"};
        String[] values = {id};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/myOrgan.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        Gson gson = new Gson();
                        loginEntity = gson.fromJson(json.getString("data"), LoginEntity.class);
                        setData();
                    } else
                        ToastUtil.showShort(context, json.getString("desc"));
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

    private void setData() {
        if (loginEntity == null)
            return;

        tv_companyName.setText(loginEntity.COMPANY);
        tv_address.setText(loginEntity.ADDRESS);
        tv_code.setText(loginEntity.ORGAN);
    }
}
