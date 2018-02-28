package com.ipd.bangbanjinrong.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.CommonUtil;
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
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 修改密码
 */

public class ResetPwd extends BaseActivity {
    private MyCountDownTimer myCountDownTimer;
    private String phone,password,vercode;

    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.et_verCode)
    private EditText et_verCode;
    @ViewInject(R.id.cb_look)
    private CheckBox cb_look;
    @ViewInject(R.id.et_regPwd)
    private EditText et_regPwd;

    @ViewInject(R.id.bt_sure)
    private Button bt_sure;
    @ViewInject(R.id.bt_getAuthCode)
    private Button bt_getAuthCode;
    @Override
    public int setLayout() {
        return R.layout.reset_pwd;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.xgmm));
        myCountDownTimer=new MyCountDownTimer(60000,1000);
        tv_phone.setText(getIntent().getStringExtra("phone"));
    }

    @Override
    public void setListeners() {
        bt_sure.setOnClickListener(this);
        bt_getAuthCode.setOnClickListener(this);
        cb_look.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //如果选中，显示密码
                    et_regPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    et_regPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_sure:
                resetPwd();
                break;
            case R.id.bt_getAuthCode:
                getCheckCode();
                break;
        }    }



    /**
     * 修改密码
     */
    private void resetPwd(){
        noNet(context);
        phone=tv_phone.getText().toString().trim();
        password=et_regPwd.getText().toString().trim();
        vercode=et_verCode.getText().toString().trim();
        if(!CommonUtil.isMobileNO(phone)){
            ToastUtil.showShort(context,"手机号码格式错误！");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtil.showShort(context,"密码不能为空！");
            return;
        }
        if(TextUtils.isEmpty(vercode)){
            ToastUtil.showShort(context,"验证码不能为空！");
            return;
        }
        dialog();
        String[]keys={"PASSWORD","RESULT","APPUSER_ID"};
        String[]values={password,vercode, SharedPreferencesUtils.getSharedPreferences(context,Constant.USER_ID)};
        RequestParams params=new RequestParams(Constant.SEVER_API+"app_user/reset.do");
        String json=JsonUtil.getJsonString(keys,values);
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG","result="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    if(json.getString("response").equals("0")){
                        ToastUtil.showShort(context,"密码修改成功！");
                        finish();
                    }
                    ToastUtil.showShort(context,json.getString("desc"));
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
     * 获取验证码
     */
    private void getCheckCode(){
        phone=tv_phone.getText().toString().trim();
        if(!CommonUtil.isMobileNO(phone)){
            ToastUtil.showShort(context,"手机号码格式错误！");
            return;
        }
        //调接口
        String[]keys={"PHONE"};
        String[]values={phone};
        final String json= JsonUtil.getJsonString(keys,values);

        RequestParams params=new RequestParams(Constant.SEVER_API+"app_user/findCode.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG","result="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("response").equals("0")){
                        ToastUtil.showShort(context,"验证码发送成功！");
                        myCountDownTimer.start();
                        //验证码暂时写死123456
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

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            bt_getAuthCode.setText("重新验证");
            bt_getAuthCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            bt_getAuthCode.setClickable(false);
            bt_getAuthCode.setText(millisUntilFinished / 1000 + "s");
        }
    }
}
