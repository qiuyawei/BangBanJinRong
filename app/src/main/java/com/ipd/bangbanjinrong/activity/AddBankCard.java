package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.CommonUtil;
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
 * 绑定银行卡
 */

public class AddBankCard extends BaseActivity {
    /*false 代表不是提现过程中发现没有银行卡而添加银行卡  true 代表是*/
    private boolean flage=false;
    /*TYPE=0是默认 TYPE=1 代表是机构添加银行卡（提现发现没有银行卡）*/
    private int type=-1,TYPE=0;
    private String yhkh,xm,sfzh,sjh;

    @ViewInject(R.id.bt_next)
    private Button bt_next;
    @ViewInject(R.id.tv_rgRuler)
    private TextView tv_rgRuler;

    @ViewInject(R.id.et_bankCardNumber)
    private EditText et_bankCardNumber;
    @ViewInject(R.id.et_Name)
    private EditText et_Name;
    @ViewInject(R.id.et_idNuber)
    private EditText et_idNuber;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;

    @ViewInject(R.id.checkbox1)
    private CheckBox checkBox;
    @Override
    public int setLayout() {
        return R.layout.add_bank_card;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.bdyhk));
        type=getIntent().getIntExtra("type",-1);
        flage=getIntent().getBooleanExtra("flage",false);

        if(flage&&SharedPreferencesUtils.getSharedPreferences(context,Constant.CATEGORY).equals("1")){
            TYPE=1;
        }
    }

    @Override
    public void setListeners() {
        bt_next.setOnClickListener(this);
        tv_rgRuler.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:

                addBankCard();
                break;
            case R.id.tv_rgRuler:
                intent=new Intent(context,ShangHuQianYueXieYi.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 添加是银行卡接口
     */
    private void addBankCard(){
        yhkh=et_bankCardNumber.getText().toString().trim();
        xm=et_Name.getText().toString().trim();
        sfzh=et_idNuber.getText().toString().trim();
        sjh=et_phone.getText().toString().trim();

      /*  if(!CommonUtil.checkBankCard(yhkh)){
            ToastUtil.showShort(context,"请输入正确的银行卡号！");
            return;
        }*/
        if(TextUtils.isEmpty(xm)){
            ToastUtil.showShort(context,"请输入真实姓名！");
            return;
        }
        /*if(!CardIdCheck.verify(sfzh)){
            ToastUtil.showShort(context,"请输入正确的身份证号码！");
            return;
        }*/
        if(!CommonUtil.isMobileNO(sjh)){
            ToastUtil.showShort(context,"请输入正确的手机号码！");
            return;
        }
        if(!checkBox.isChecked()){
            ToastUtil.showShort(context,"请同意商户授权协议！");
            return;
        }
        dialog();

        final String[]keys={"APPUSER_ID","CARDID","NUMBERID","PHONE","TRUENAME"};
        final   String[]values={SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID)
        ,yhkh,sfzh,sjh,xm};
        final String json= JsonUtil.getJsonString(keys,values);
        RequestParams params=new RequestParams(Constant.SEVER_API+"app_banks/sign.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("ADDBank",result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("response").equals("0")){
                        intent=new Intent(context,CheckBankCard.class);
                        intent.putExtra("cardMumber",yhkh);
                        intent.putExtra("values",yhkh+";"+sfzh+";"+sjh+";"+xm+";"+TYPE+";");
                        intent.putExtra("type",type);

                        startActivity(intent);
                    }else
                        ToastUtil.showShort(context,jsonObject.getString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG","ex="+ex.getMessage());

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
