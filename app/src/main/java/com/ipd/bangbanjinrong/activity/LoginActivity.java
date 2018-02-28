package com.ipd.bangbanjinrong.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.ipd.bangbanjinrong.global.MyApplaction;
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

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/8
 * @Email 448739075@qq.com
 * 登陆页面
 */

public class LoginActivity extends BaseActivity {
    private LoginEntity loginEntity = null;
    private String phone, password, invitecode;
    //第三方返回的Id
    private String openId = "";
    //登录类型 0:QQ 1:微信 2:新浪
    private int type = 0;
    @ViewInject(R.id.bt_login)
    private Button bt_login;
    @ViewInject(R.id.bt_register)
    private Button bt_register;
    @ViewInject(R.id.bt_forgotPwd)
    private Button bt_forgotPwd;
    @ViewInject(R.id.cb_look)
    private CheckBox cb_look;

    @ViewInject(R.id.et_loginPwd)
    private EditText et_loginPwd;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;

    @ViewInject(R.id.tv_qq)
    private TextView tv_qq;
    @ViewInject(R.id.tv_wx)
    private TextView tv_wx;
    @ViewInject(R.id.tv_sina)
    private TextView tv_sina;
    private boolean permision = false;

    @Override
    public int setLayout() {
        return R.layout.login_activty;
    }

    @Override
    public void innitData() {
//        检查读取权限
        CommonUtil.checkPersiomion(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        setTopTitle(getResources().getString(R.string.login));

    }

    @Override
    public void setListeners() {
        bt_forgotPwd.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        tv_qq.setOnClickListener(this);
        tv_wx.setOnClickListener(this);
        tv_sina.setOnClickListener(this);


        cb_look.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    et_loginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    et_loginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_forgotPwd:
//              忘记密码
                intent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_register:
//                注册
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

                break;

            case R.id.bt_login:
//                登录
                loginServer();
                break;
            case R.id.tv_qq:
                type = 0;
//                ToastUtil.showShort(context,"暂未开放！");
                authorQQ();
                break;
            case R.id.tv_wx:
                type = 1;
                authorWx();
                break;
            case R.id.tv_sina:
                type = 2;
                ToastUtil.showShort(context, "暂未开放！");
                break;
        }
    }

    /**
     * 登录接口调用
     */
    private void loginServer() {
        phone = et_phone.getText().toString().trim();
        password = et_loginPwd.getText().toString().trim();
        if (!CommonUtil.isMobileNO(phone)) {
            ToastUtil.showShort(context, "手机号码格式错误！");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShort(context, "密码不能为空！");
            return;
        }
        dialog();
        String[] keys = {"PHONE", "PASSWORD"};
        String[] values = {phone, password};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/login.do");
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
                        ToastUtil.showShort(context, "登录成功！");
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


    /**
     * type :QQ 1:微信 2:新浪
     * 第三方登录接口
     */
    private void thirdLogin() {
        dialog();
        String[] keys = {"OPENID", "TYPE"};
        String[] values = {openId, String.valueOf(type)};
        final String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/open.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("resultOpen", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        Gson gson = new Gson();
                        loginEntity = gson.fromJson(jsonObject.getString("data"), LoginEntity.class);
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.USER_ID, loginEntity.APPUSER_ID);
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.MANAGE_ID, loginEntity.MANAGER_ID);
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.JI_GOU_ID, loginEntity.MYORGAN);
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.CATEGORY, loginEntity.CATEGORY);

                        intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        ToastUtil.showShort(context, "登录成功！");
                        finish();
                    } else if (jsonObject.getString("response").equals("10000")) {
                        intent = new Intent(context, BandPhoneActivity.class);
                        intent.putExtra("openId", openId);
                        intent.putExtra("type", String.valueOf(type));
                        startActivity(intent);
                        ToastUtil.showShort(context, jsonObject.getString("desc"));
                    } else
                        ToastUtil.showShort(context, jsonObject.getString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.print("onError", ex.getMessage());

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
     * qq 第三方授权获取 id
     */
    private void authorQQ() {
        final Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(false);//设置false表示使用SSO授权方式
//        qq.authorize();//单独授权,OnComplete返回的hashmap是空的  qq.authorize()和 qq.showUser(null) 只能调用一个
        qq.showUser(null);//授权并获取用户信息
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    openId = platDB.getUserId();
                    LogUtils.print("openId", openId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            thirdLogin();
                        }
                    });
                }

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtils.print("throwQQ", throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }

    /**
     * wx第三方授权获取 id
     */
    private void authorWx() {
        final Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
        wx.SSOSetting(false);//设置false表示使用SSO授权方式
//        wx.authorize();//单独授权,OnComplete返回的hashmap是空的  qq.authorize()和 qq.showUser(null) 只能调用一个
        wx.showUser(null);//授权并获取用户信息

        wx.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    openId = platDB.getUserId();
                    LogUtils.print("openId", openId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            thirdLogin();
                        }
                    });
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if (throwable.toString().contains("WechatClientNotExistException")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(context, "您还没有安装微信，请安装后再试！");
                        }
                    });
                }

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
//        登录界面 销毁所有其它activity
        for (Activity activity : MyApplaction.activities) {
            if (activity.getClass() != LoginActivity.class)
                activity.finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constant.REQUEST_CHECK_PERMISION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ToastUtil.showShort(context,"存储权限关闭，会影响手机部分功能！");
                }
                return;
            }
        }
    }
}
