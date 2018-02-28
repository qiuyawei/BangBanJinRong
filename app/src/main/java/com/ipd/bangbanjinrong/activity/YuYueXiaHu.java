package com.ipd.bangbanjinrong.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.OrderEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.LogUtils;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.ipd.bangbanjinrong.widget.timeselect.TimeSelectorDayAndHour;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 预约下户
 */

public class YuYueXiaHu extends BaseActivity {
    private OrderEntity orderEntity;

    @ViewInject(R.id.bt_yyxh)
    private Button bt_yyxh;

    @ViewInject(R.id.ll_yuYueTime)
    private LinearLayout ll_yuYueTime;
    @ViewInject(R.id.tv_yuYueTime)
    private TextView tv_yuYueTime;

    @ViewInject(R.id.tv_orderNumber)
    private TextView tv_orderNumber;
    @ViewInject(R.id.tv_borrowName)
    private TextView tv_borrowName;
    @ViewInject(R.id.tv_desc)
    private TextView tv_desc;

    private String money;

    @Override
    public int setLayout() {
        return R.layout.yu_yue_xia_hu;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.yyxh));

        orderEntity = (OrderEntity) getIntent().getSerializableExtra("entity");
        tv_orderNumber.setText(orderEntity.ORDER_NO);
        tv_borrowName.setText(orderEntity.TRUENAME);
        getMoney();
    }

    @Override
    public void setListeners() {
        bt_yyxh.setOnClickListener(this);
        ll_yuYueTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_yyxh:
                submit();
                break;
            case R.id.ll_yuYueTime:
                showTimePicker();
                break;
        }
    }


    private void showTimePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startTime = sdf.format(new Date());
        String endTime=sdf.format(new Date(new Date().getTime()+3*24*3600*1000));
        LogUtils.print("endTime",endTime);


        TimeSelectorDayAndHour timeSelector = new TimeSelectorDayAndHour(context, new TimeSelectorDayAndHour.ResultHandler() {
            @Override
            public void handle(String time) {
                int index = time.indexOf(" ");
//                        time = time.substring(0, index);
                LogUtils.print("time", time);
                tv_yuYueTime.setText(time);
            }
        }, startTime, endTime);
        timeSelector.show();
    }


    private void submit() {
        if (tv_yuYueTime.getText().toString().trim().equals("")) {
            ToastUtil.showShort(context, "请选择预约时间！");
            return;
        }
        dialog();
        String[] keys = {"APPUSER_ID", "CHECKTIME", "ORDER_NO"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID),
                tv_yuYueTime.getText().toString().trim(),
                orderEntity.ORDER_NO};
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/appoint.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("YUYUE_XIA_HU", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable onError, boolean isOnCallback) {
                LogUtils.print("onError", onError.getMessage());

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

    private void getMoney() {
        dialog();
        String[] keys = {"APPUSER_ID"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID)};
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/getPay.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("Money", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        money=jsonObject.getString("data");
                        tv_desc.setText("调查预约时间范围为包含当日在内的3个自然日，\n预约调查需收取" + money+ "元下户费，取消预约不退还。");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable onError, boolean isOnCallback) {
                LogUtils.print("onError", onError.getMessage());

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
