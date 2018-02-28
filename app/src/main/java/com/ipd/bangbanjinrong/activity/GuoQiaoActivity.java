package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.CardIdCheck;
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

import static com.ipd.bangbanjinrong.R.id.et_jkrxm;
import static com.ipd.bangbanjinrong.R.id.et_sfzh;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/9
 * @Email 448739075@qq.com
 * 过桥垫资
 */

public class GuoQiaoActivity extends BaseActivity {
    private String xm, sfzh, fcjz, xqje, sysj, content;
    @ViewInject(et_jkrxm)
    private EditText et_name;
    @ViewInject(et_sfzh)
    private EditText et_idCard;
    @ViewInject(R.id.et_fcjz)
    private EditText et_fcjz;
    @ViewInject(R.id.et_xqje)
    private EditText et_xqje;
    @ViewInject(R.id.et_startTime)
    private TextView tv_time;

    @ViewInject(R.id.ll_startTime)
    private LinearLayout ll_time;
    @ViewInject(R.id.bt_next)
    private Button bt_next;

    @Override
    public int setLayout() {
        return R.layout.guo_qiao_dian_zi;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.guo_qiao_dian_zi));
    }

    @Override
    public void setListeners() {
        ll_time.setOnClickListener(this);
        bt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_next:
                idCheck();
                break;
            case R.id.ll_startTime:
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
                        tv_time.setText(time);
                    }
                }, startTime, endTime);
                timeSelector.show();

                break;


        }
    }

    private void idCheck() {
        xm = et_name.getText().toString().trim();
        sfzh = et_idCard.getText().toString().trim();
        fcjz = et_fcjz.getText().toString().trim();
        xqje = et_xqje.getText().toString().trim();
        sysj = tv_time.getText().toString().trim();
        if (TextUtils.isEmpty(xm)) {
            ToastUtil.showShort(context, "姓名不能为空！");
            return;
        }
        if (!CardIdCheck.verify(sfzh)) {
            ToastUtil.showShort(context, "身份证格式错误！");
            return;
        }
        if (TextUtils.isEmpty(fcjz)) {
            ToastUtil.showShort(context, "请输入房产价值！");
            return;
        }
        if (TextUtils.isEmpty(xqje)) {
            ToastUtil.showShort(context, "请输入需求金额！");
            return;
        }
        if (TextUtils.isEmpty(sysj)) {
            ToastUtil.showShort(context, "请选择使用时间！");
            return;
        }

        dialog();
        String[] keys = {"APPUSER_ID", "NUMBERID", "TRUENAME"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID), sfzh, xm};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/verify.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("Diya", result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        content = xm + ";" +
                                SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID) + ";" +
                                sfzh + ";" +
                                xqje + ";" +
                                fcjz + ";" +
                                sysj + ";";

                        intent = new Intent(context, GuoQiaoDianZi2.class);
                        intent.putExtra("content", content);
                        startActivity(intent);
                    } else if (json.getString("response").equals("10001")) {
                        ToastUtil.showShort(context, json.getString("desc"));
                        intent = new Intent(context, AddBankCard.class);
                        //1 代表是过桥垫资
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    } else ToastUtil.showShort(context, json.getString("desc"));

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
