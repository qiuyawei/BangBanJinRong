package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.alipay.AuthResult;
import com.ipd.bangbanjinrong.alipay.PayResult;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.OrderEntity;
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

import java.util.Map;

import static com.ipd.bangbanjinrong.utils.SharedPreferencesUtils.POSTION;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 支付下户服务费
 */

public class PayServiceFee extends BaseActivity {
    private OrderEntity orderEntity;
    private String money;

    private String orderNumber = "";
    @ViewInject(R.id.ll_zfb)
    private LinearLayout ll_zfb;
    @ViewInject(R.id.ll_wx)
    private LinearLayout ll_wx;
    @ViewInject(R.id.ll_ye)
    private LinearLayout ll_ye;

    @ViewInject(R.id.cbZfb)
    private CheckBox cbZfb;
    @ViewInject(R.id.cbWx)
    private CheckBox cbWx;
    @ViewInject(R.id.cbYe)
    private CheckBox cbYe;

    @ViewInject(R.id.bt_pay)
    private Button bt_pay;
    @ViewInject(R.id.tv_money)
    private TextView tv_money;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayServiceFee.this, "支付成功", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtils.putIntSharedPreferences(context,POSTION,1);
                        intent=new Intent(context,MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(PayServiceFee.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayServiceFee.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayServiceFee.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    @Override
    public int setLayout() {
        return R.layout.pay_fee;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.zfxhfwf));
        orderEntity= (OrderEntity) getIntent().getSerializableExtra("entity");
        orderNumber = orderEntity.ORDER_NO;
        getMoney();
    }

    @Override
    public void setListeners() {
        ll_zfb.setOnClickListener(this);
        ll_wx.setOnClickListener(this);
        ll_ye.setOnClickListener(this);
        bt_pay.setOnClickListener(this);
        cbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbYe.setChecked(false);
                cbZfb.setChecked(false);
            }
        });
        cbZfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbYe.setChecked(false);
                cbWx.setChecked(false);
            }
        });
        cbYe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbWx.setChecked(false);
                cbZfb.setChecked(false);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_zfb:
                setAllFalse();
                cbZfb.setChecked(true);
                break;
            case R.id.ll_wx:
                setAllFalse();
                cbWx.setChecked(true);
                break;
            case R.id.ll_ye:
                setAllFalse();
                cbYe.setChecked(true);
                break;
            case R.id.bt_pay:
               /* SharedPreferencesUtils.putIntSharedPreferences(context, POSTION, 1);
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);*/
               if(cbZfb.isChecked()){
                   aliPay();
               }
               if(cbYe.isChecked()){
                   balancePay();
               }
                break;
        }
    }


    /**
     * 设置所有未选
     */
    private void setAllFalse() {
        cbZfb.setChecked(false);
        cbWx.setChecked(false);
        cbYe.setChecked(false);

    }

    /**
     * 支付宝支付
     */
    private void aliPay() {
        dialog();
        String[] keys = {"APPUSER_ID", "ORDER_NO"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID), orderNumber};
        String json = JsonUtil.getJsonString(keys, values);
        LogUtils.print("json",json);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/pay.do");

        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        final String orderInfo = jsonObject.getString("data");   // 订单信息

                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(PayServiceFee.this);
                                String result = alipay.pay(orderInfo, true);

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
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


    /**
     * 余额支付
     */
    private void balancePay() {
        dialog();
        String[] keys = {"APPUSER_ID", "ORDER_NO"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID), orderNumber};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/balance.do");

        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("banlance",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        ToastUtil.showShort(context,"支付成功！");
                        SharedPreferencesUtils.putIntSharedPreferences(context,POSTION,1);
                        intent=new Intent(context,MainActivity.class);
                        startActivity(intent);
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


    /**
     * 获取需要支付多少服务费
     */
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
                        tv_money.setText(money);
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
