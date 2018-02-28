package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
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

import com.google.gson.Gson;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.LoginEntity;
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
 * @Date 2017/8/8
 * @Email 448739075@qq.com
 * 注册
 */

public class RegisterActivity extends BaseActivity{
    private LoginEntity loginEntity=null;
    private String phone,password,vercode,inviteCode;
    private MyCountDownTimer myCountDownTimer;

    @ViewInject(R.id.tv_rgRuler)
    private TextView tv_ruler;
    @ViewInject(R.id.cb_look)
    private CheckBox cb_look;
    @ViewInject(R.id.et_regPwd)
    private EditText et_regPwd;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.tv_login)
    private TextView tv_login;
    @ViewInject(R.id.bt_login)
    private Button bt_login;
    @ViewInject(R.id.bt_getAuthCode)
    private Button bt_getAuthCode;
    @ViewInject(R.id.et_inviteCode)
    private EditText et_inviteCode;
    @ViewInject(R.id.et_verCode)
    private EditText et_verCode;
    @ViewInject(R.id.checkbox1)
    private CheckBox checkBox;

    @Override
    public int setLayout() {
        return R.layout.register_activty;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.register));
        myCountDownTimer=new MyCountDownTimer(60000,1000);

    }

    @Override
    public void setListeners() {
        tv_ruler.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        bt_login.setOnClickListener(this);
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
            case R.id.tv_rgRuler:
                intent=new Intent(RegisterActivity.this,RegisterRuler.class);
                startActivity(intent);
                break;
            case R.id.tv_login:
                intent=new Intent(context,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_login:
                resgisterServer();
                break;
            case R.id.bt_getAuthCode:
//                获取验证码
                getCheckCode();
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getCheckCode(){
        phone=et_phone.getText().toString().trim();
        if(!CommonUtil.isMobileNO(phone)){
            ToastUtil.showShort(context,"手机号码格式错误！");
            return;
        }
        //调接口
        String[]keys={"PHONE"};
        String[]values={phone};
        final String json= JsonUtil.getJsonString(keys,values);
        Log.i("TAG","json="+json);

        RequestParams params=new RequestParams(Constant.SEVER_API+"app_user/getCode.do");
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
                Log.i("TAG","ex="+ex.getMessage());

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


    /**
     * 注册
     */
    private void resgisterServer(){
        noNet(context);
        phone=et_phone.getText().toString().trim();
        password=et_regPwd.getText().toString().trim();
        inviteCode=et_inviteCode.getText().toString().trim();
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
        if(!checkBox.isChecked()){
            ToastUtil.showShort(context,"请选择同意注册协议！");
            return;
        }
        dialog();
        String[]keys={"PHONE","PASSWORD","OPENID","CODE","CATEGORY","RESULT"};
        String[]values={phone,password,"",inviteCode,"0",vercode};
        RequestParams params=new RequestParams(Constant.SEVER_API+"app_user/register.do");
        String json=JsonUtil.getJsonString(keys,values);
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        Gson gson = new Gson();
                        loginEntity = gson.fromJson(json.getString("data"), LoginEntity.class);
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.USER_ID, loginEntity.APPUSER_ID);
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.MANAGE_ID, loginEntity.MANAGER_ID);
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.JI_GOU_ID, loginEntity.MYORGAN);
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.CATEGORY, loginEntity.CATEGORY);

                        intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        ToastUtil.showShort(context, "注册成功！");
                        finish();
                    } else
                        ToastUtil.showShort(context, json.getString("desc"));
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
