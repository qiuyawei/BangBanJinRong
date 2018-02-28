package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.adapter.Adapter3;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.RecordEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.LogUtils;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.ipd.bangbanjinrong.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 我的钱包
 */

public class MineWallet extends BaseActivity {
    @ViewInject(R.id.rl_szmx)
    private LinearLayout rl_szmx;

    @ViewInject(R.id.bt_tiXian)
    private Button bt_tiXian;

    @ViewInject(R.id.listView)
    private MyListView myListView;

    private Adapter3 adapter;
    private List<RecordEntity> data = new ArrayList<>();

    @ViewInject(R.id.tv_banlance)
    private TextView tv_banlance;

    @Override
    public int setLayout() {
        return R.layout.mine_wallet;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.wo_de_qian_bao));

        setRightText(getResources().getString(R.string.yhk), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, BankCard.class);
                startActivity(intent);
            }
        });


        adapter = new Adapter3(context, data);
        myListView.setAdapter(adapter);
    }

    @Override
    public void setListeners() {
        rl_szmx.setOnClickListener(this);
        bt_tiXian.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_szmx:
                intent = new Intent(context, ShouZhiMingXi.class);
                startActivity(intent);
                break;
            case R.id.bt_tiXian:
                intent = new Intent(context, TiXian.class);
                intent.putExtra("money",tv_banlance.getText().toString().trim());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBalance();
    }

    /**
     * 获取余额
     */
    private void getBalance() {
        data.clear();
        dialog();
        String[] keys = {"APPUSER_ID"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID)};
        final String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_record/listAll.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("re",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        tv_banlance.setText(jsonObject.getString("data"));
                        data.addAll(JSON.parseArray(jsonObject.getString("data2"), RecordEntity.class));
                    } else
                        ToastUtil.showShort(context, jsonObject.getString("desc"));
                    adapter.notifyDataSetChanged();

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
