package com.ipd.bangbanjinrong.activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.MessageEntity;
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
 * 关于我们
 */

public class MessageDetail extends BaseActivity {
    private MessageEntity entity;

    @ViewInject(R.id.tv_content)
    private TextView tv_content;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @Override
    public int setLayout() {
        return R.layout.message_detail;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.xxxq));
        entity = (MessageEntity) getIntent().getSerializableExtra("entity");

        if (entity == null)
            return;

        tv_content.setText(entity.CONTENT);
        tv_time.setText(entity.CREATETIME);

        if (entity.STATUS.equals("1")) {
            changeMessageState();
        }
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 改变消息状态
     */
    private void changeMessageState() {
        String[] keys = {"INFO_ID"};
        String[] values = {entity.INFO_ID};
        final String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_info/modify.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        Log.i("TAG", "消息已查看");
                    }
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

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
}
