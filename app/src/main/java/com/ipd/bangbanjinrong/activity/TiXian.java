package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.BankEntity;
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

import static com.ipd.bangbanjinrong.global.Constant.TI_XIAN_REQUEST_CODE;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 提现
 */

public class TiXian extends BaseActivity {
    private String allMoney, tiXianMoney;
    private BankEntity bankEntity;
    @ViewInject(R.id.bt_tiXian)
    private Button bt_tiXian;
    @ViewInject(R.id.tv_all)
    private TextView tv_all;
    @ViewInject(R.id.tv_bankName)
    private TextView tv_bankName;
    @ViewInject(R.id.et_txje)
    private EditText et_txje;

    @ViewInject(R.id.tv_money)
    private TextView tv_money;

    @Override
    public int setLayout() {
        return R.layout.ti_xian;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.ti_xian));
        setRightText("提现记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,TiXianJiLu.class));
            }
        });
        allMoney = getIntent().getStringExtra("money");
        tv_money.setText("钱包余额¥" + allMoney);
    }

    @Override
    public void setListeners() {
        bt_tiXian.setOnClickListener(this);
        tv_bankName.setOnClickListener(this);
        tv_all.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_tiXian:
                tiXianMoney = et_txje.getText().toString().trim();
                if (TextUtils.isEmpty(tiXianMoney)) {
                    ToastUtil.showShort(context, "提现金额不能为空！");
                    return;
                }
                tiXian();
                break;
            case R.id.tv_bankName:
                intent = new Intent(context, BankCard.class);
                intent.putExtra("type",SharedPreferencesUtils.getSharedPreferences(context,Constant.CATEGORY));
                intent.putExtra("flage",true);
                startActivityForResult(intent, TI_XIAN_REQUEST_CODE);
                break;
            case R.id.tv_all:
                tiXianMoney=allMoney;
                tiXian();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TI_XIAN_REQUEST_CODE:
                if (data == null)
                    return;
                bankEntity = (BankEntity) data.getSerializableExtra("entity");
                tv_bankName.setText(bankEntity.BANK_NAME);
                break;
        }
    }


    /**
     * 提现接口
     */
    private void tiXian() {
        if(bankEntity==null){
            ToastUtil.showShort(context,"请选择银行卡！");
            return;
        }
        dialog();
        String[] keys = {"APPUSER_ID", "BANK", "MONEY", "PAYNAME", "TRUENAME"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID),
                bankEntity.BANK_NAME,tiXianMoney,bankEntity.BANK_CODE,bankEntity.TRUENAME};

        final String json= JsonUtil.getJsonString(keys,values);
        RequestParams params=new RequestParams(Constant.SEVER_API+"app_apply/add.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("tixian",result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("response").equals("0")){
                        ToastUtil.showShort(context,"提现申请提交成！");
                        startActivity(new Intent(context,TiXianJiLu.class));
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
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }
}
