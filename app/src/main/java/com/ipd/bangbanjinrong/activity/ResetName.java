package com.ipd.bangbanjinrong.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
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
 * @Date 2017/8/16
 * @Email 448739075@qq.com
 * 修改姓名
 */

public class ResetName extends BaseActivity{
    private String region;
    @ViewInject(R.id.bt_sure)
    private Button bt_sure;
    @ViewInject(R.id.et_name)
    private EditText et_name;

    @Override
    public int setLayout() {
        return R.layout.name_choice;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.xgxm));
    }

    @Override
    public void setListeners() {
        bt_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_sure){
            region=et_name.getText().toString().trim();
            if(TextUtils.isEmpty(region)){
                ToastUtil.showShort(context,"请输入所在城市！");
                return;
            }
            updateInformation();
        }
    }


    /**
     * 更新个人信息
     */
    private void updateInformation() {
        dialog();
        String[] keys = {"APPUSER_ID", "TRUENAME"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID)
                , region};
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/update.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        ToastUtil.showShort(context, "更新成功！");
                        finish();
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
}
