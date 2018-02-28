package com.ipd.bangbanjinrong.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.LogUtils;
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
 * 意见反馈
 */

public class YiJianFanKui extends BaseActivity {
    @ViewInject(R.id.bt_submit)
    private Button bt_submit;
    @ViewInject(R.id.et_content)
    private EditText et_content;

    private String content;
    @Override
    public int setLayout() {
        return R.layout.yi_jian_fankui;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.yjfk));
    }

    @Override
    public void setListeners() {
        bt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_submit:
                submitAdvice();
                break;
        }
    }


    /**
     * 意见提交接口
     */
    private void submitAdvice() {
        noNet(context);
        content=et_content.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            ToastUtil.showShort(context,"内容不能为空！");
            return;
        }
        String[] keys = {"CONTENT", "APPUSER_ID"};
        String[] values = {content, SharedPreferencesUtils.getSharedPreferences(context,Constant.USER_ID),"0"};
        final String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_feedback/add.do");
        params.setBodyContent(json);
        dialog();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("意见",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("response").equals("0")){
                        ToastUtil.showShort(context,"意见提交成功！");
                        finish();
                    }else
                        ToastUtil.showShort(context,jsonObject.getString("desc"));

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
