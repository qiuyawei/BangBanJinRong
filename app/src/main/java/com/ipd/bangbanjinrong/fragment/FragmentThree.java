package com.ipd.bangbanjinrong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.activity.AboutUs;
import com.ipd.bangbanjinrong.activity.LeiJiFanFei;
import com.ipd.bangbanjinrong.activity.MineJiGou;
import com.ipd.bangbanjinrong.activity.MineManage;
import com.ipd.bangbanjinrong.activity.MineWallet;
import com.ipd.bangbanjinrong.activity.PersonInforMation;
import com.ipd.bangbanjinrong.activity.YiJianFanKui;
import com.ipd.bangbanjinrong.base.BaseFragment;
import com.ipd.bangbanjinrong.entity.LoginEntity;
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
 * 我的主页
 */

public class FragmentThree extends BaseFragment {
    private LoginEntity loginEntity;

    @ViewInject(R.id.iv_photo)
    private ImageView iv_photo;

    @ViewInject(R.id.ll_wdjl)
    private LinearLayout ll_wdjl;
    @ViewInject(R.id.ll_aboutUs)
    private LinearLayout ll_aboutUs;
    @ViewInject(R.id.ll_yiJianFanKui)
    private LinearLayout ll_yiJianFanKui;
    @ViewInject(R.id.ll_ljfe)
    private LinearLayout ll_ljfe;

    @ViewInject(R.id.rl_wallet)
    private RelativeLayout rl_wallet;
    @ViewInject(R.id.rl_jiGou)
    private RelativeLayout rl_jiGou;

    @ViewInject(R.id.tv_nickName)
    private TextView tv_nickName;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;

    @ViewInject(R.id.tv_address)
    private TextView tv_address;

    @Override
    public View setView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_three, null);
    }

    @Override
    public void innitData(Bundle savedInstanceState) {

    }

    @Override
    public void setListeners() {
        iv_photo.setOnClickListener(this);
        ll_wdjl.setOnClickListener(this);
        ll_aboutUs.setOnClickListener(this);
        ll_yiJianFanKui.setOnClickListener(this);
        ll_ljfe.setOnClickListener(this);
        rl_wallet.setOnClickListener(this);
        rl_jiGou.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_photo:
                intent = new Intent(context, PersonInforMation.class);
                startActivity(intent);
                break;
            case R.id.rl_wallet:
//                我的钱包
                intent = new Intent(context, MineWallet.class);
                startActivity(intent);
                break;
            case R.id.rl_jiGou:
//                我的机构
                intent = new Intent(context, MineJiGou.class);
                startActivity(intent);
                break;
            case R.id.ll_wdjl:
//                我的经理
                intent = new Intent(context, MineManage.class);
                startActivity(intent);
                break;
            case R.id.ll_aboutUs:
//                关于我们
                intent = new Intent(context, AboutUs.class);
                startActivity(intent);
                break;
            case R.id.ll_yiJianFanKui:
//                意见反馈
                intent = new Intent(context, YiJianFanKui.class);
                startActivity(intent);
                break;
            case R.id.ll_ljfe:
//                累计返费
                intent = new Intent(context, LeiJiFanFei.class);
                startActivity(intent);
                break;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        LogUtils.print("onResume","onResume");

        getInfor();
    }

    /**
     * 获取个人信息资料
     */
    private void getInfor() {
        dialog();
        String[] keys = {"APPUSER_ID"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID)};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/get.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        Gson gson = new Gson();
                        loginEntity = gson.fromJson(json.getString("data"), LoginEntity.class);
                        setData();
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


    private void setData() {
        if (loginEntity == null)
            return;
        RequestOptions options=RequestOptions.circleCropTransform();
        Glide.with(context).load(Constant.IMAGE_API + loginEntity.LOGO).apply(options).into(iv_photo);

        if (!TextUtils.isEmpty(loginEntity.TRUENAME))
            tv_nickName.setText(loginEntity.TRUENAME);
        if (!TextUtils.isEmpty(loginEntity.REGION))
            tv_address.setText(loginEntity.REGION);
        if (!TextUtils.isEmpty(loginEntity.PHONE))
            tv_phone.setText(loginEntity.PHONE);
    }
}
