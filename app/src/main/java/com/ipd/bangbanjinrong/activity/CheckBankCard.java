package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * 验证银行卡
 */

public class CheckBankCard extends BaseActivity {
    //-1 银行卡列表  0  抵押保单  1  过桥垫资  2  信贷  跳转判断
    private int type=-1;

    @ViewInject(R.id.bt_complet)
    private Button bt_complet;

    @ViewInject(R.id.tv_cardMumber)
    private TextView tv_cardMumber;
    @ViewInject(R.id.et_verCode)
    private EditText et_verCode;

    private String cardMumber="";
    private String smsCode="000000";
    private String content;
    @Override
    public int setLayout() {
        return R.layout.check_bank_card;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.yzyhk));
        type=getIntent().getIntExtra("type",-1);
        cardMumber=getIntent().getStringExtra("cardMumber");
        content=getIntent().getStringExtra("values");
        tv_cardMumber.setText(cardMumber);

    }

    @Override
    public void setListeners() {
        bt_complet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_complet:
                smsCode=et_verCode.getText().toString().trim();
                if (TextUtils.isEmpty(smsCode)){
                    ToastUtil.showShort(context,"请输入验证码！");
                    return;
                }
                content=content+smsCode
                +";"+SharedPreferencesUtils.getSharedPreferences(context,Constant.USER_ID);
                idCheck();
                break;
        }}

    /**
     * 身份校验
     */
    private void idCheck() {
        dialog();
        String[] keys = {"CARDID", "NUMBERID","PHONE","TRUENAME","TYPE","RESULT","APPUSER_ID"};
        String[] values = content.split(";");
        String json = JsonUtil.getJsonString(keys, values);
        LogUtils.print("json", json);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_banks/msg.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("checkSMS", result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        if(type==-1){
                            intent=new Intent(context,BankCard.class);
                        }else if(type==0){
                            intent=new Intent(context,DiYaBaoDanActivity.class);
                        }else if(type==1){
                            intent=new Intent(context,GuoQiaoActivity.class);
                        }else {
                            intent=new Intent(context,XinDaiBaoDan.class);
                        }
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
