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
import com.ipd.bangbanjinrong.widget.MySelfSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/9
 * @Email 448739075@qq.com
 * 抵押报单
 */

public class DiYaBaoDanActivity extends BaseActivity {
    private String content, PLEDGE_TYPE, MAKE_TYPE, YI_DI_JIN_E,jiGouNumber;
    @ViewInject(R.id.ll_bllx)
    private LinearLayout ll_bllx;
    @ViewInject(R.id.ll_dylex)
    private LinearLayout ll_dylex;
    @ViewInject(R.id.ll_yiDi)
    private LinearLayout ll_yiDi;
    @ViewInject(R.id.view)
    private View view_yiDi;
    @ViewInject(R.id.view_bllx)
    private View view_bllx;

    @ViewInject(R.id.tv_bllx)
    private TextView tv_bllx;
    @ViewInject(R.id.tv_dylx)
    private TextView tv_dylx;

    @ViewInject(R.id.bt_next)
    private Button bt_next;

    @ViewInject(R.id.ll_bllx)
    private LinearLayout ll_bllx_parent;
    @ViewInject(R.id.ll_bllx2)
    private LinearLayout ll_bllx_child;

    @ViewInject(R.id.et_jkrxm)
    private EditText et_jkrxm;

    @ViewInject(R.id.et_sfzh)
    private EditText et_sfzh;

    @ViewInject(R.id.et_ydje)
    private EditText et_ydje;
    @ViewInject(R.id.et_jgdm)
    private EditText et_jgdm;

    @Override
    public int setLayout() {
        return R.layout.di_ya_bao_dan;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.di_ya_bao_dan));
    }

    @Override
    public void setListeners() {
        ll_bllx.setOnClickListener(this);
        ll_dylex.setOnClickListener(this);
        bt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_dylex:
//                抵押类型
                new MySelfSheetDialog(context).addSheetItem("一抵", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        tv_dylx.setText("一抵");
                        view_yiDi.setVisibility(View.GONE);
                        ll_yiDi.setVisibility(View.GONE);
                    }
                }).addSheetItem("二抵", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        tv_dylx.setText("二抵");
                        view_yiDi.setVisibility(View.VISIBLE);
                        ll_yiDi.setVisibility(View.VISIBLE);
                    }
                }).builder()
                        .show();
                break;
            case R.id.ll_bllx:
//                办理类型
                new MySelfSheetDialog(context).addSheetItem("个人", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        tv_bllx.setText("个人");
                        view_bllx.setVisibility(View.GONE);
                        ll_bllx_child.setVisibility(View.GONE);
                    }
                }).addSheetItem("机构", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        tv_bllx.setText("机构");
                        view_bllx.setVisibility(View.VISIBLE);
                        ll_bllx_child.setVisibility(View.VISIBLE);
                        et_jgdm.setText("");
                        et_jgdm.setHint("请输入机构代码");
                    }
                }).builder()
                        .show();
                break;

            case R.id.bt_next:
                check();

                break;


        }
    }


    /**
     * 检查资料是否完整
     */
    private void check() {
//        startActivity(new Intent(context,DiYaBaoDanActivity2.class));

        if (TextUtils.isEmpty(et_jkrxm.getText().toString().trim())) {
            ToastUtil.showShort(context, "借款人姓名不能为空！");

            return;
        }
        if (!CardIdCheck.verify(et_sfzh.getText().toString().trim())) {
            ToastUtil.showShort(context, "身份证号码格式错误！");
            return;
        }
        //抵押类型判断
        if (TextUtils.isEmpty(tv_dylx.getText().toString().trim())) {
            ToastUtil.showShort(context, "请选则抵押类型！");
            return;
        }
        if (tv_dylx.getText().toString().contains("二抵")) {
            if (TextUtils.isEmpty(et_ydje.getText().toString().trim())) {
                ToastUtil.showShort(context, "一抵金额不能为空！");
                return;
            }
            YI_DI_JIN_E = et_ydje.getText().toString().trim();
            PLEDGE_TYPE = "1";
        } else {
            PLEDGE_TYPE = "0";
            YI_DI_JIN_E = "0";
        }
        //办理类型判断
        if (TextUtils.isEmpty(tv_bllx.getText().toString().trim())) {
            ToastUtil.showShort(context, "请选则办理类型！");
            return;
        }

        jiGouNumber=et_jgdm.getText().toString().trim();
        if (tv_bllx.getText().toString().contains("机构")) {
            MAKE_TYPE = "1";
            if(TextUtils.isEmpty(jiGouNumber)){
                ToastUtil.showShort(context,"请填写机构码！");
                return;
            }
        } else {
            MAKE_TYPE = "0";
        }

        idCheck();
    }


    /**
     * 身份校验
     */
    private void idCheck() {
        dialog();
        String[] keys = {"APPUSER_ID", "NUMBERID", "TRUENAME","ORGAN"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID), et_sfzh.getText().toString().trim(), et_jkrxm.getText().toString().trim()
        ,et_jgdm.getText().toString().trim()};
        String json = JsonUtil.getJsonString(keys, values);
        LogUtils.print("json",json);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/verify.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("Diya", result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        content = et_jkrxm.getText().toString().trim() + ";" +
                                SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID) + ";" +
                                et_sfzh.getText().toString().trim() + ";" +
                                PLEDGE_TYPE + ";" +
                                YI_DI_JIN_E + ";" +
                                MAKE_TYPE + ";" +
                                et_jgdm.getText().toString().trim() + ";";

                        intent = new Intent(context, DiYaBaoDanActivity2.class);
                        intent.putExtra("content", content);
                        startActivity(intent);
                    } else if (json.getString("response").equals("10001")) {
                        ToastUtil.showShort(context, json.getString("desc"));
                        intent=new Intent(context,AddBankCard.class);
                        //0 代表是抵押报单
                        intent.putExtra("type",0);
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
