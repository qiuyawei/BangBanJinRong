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
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.ipd.bangbanjinrong.widget.MySelfSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.ipd.bangbanjinrong.utils.SharedPreferencesUtils.POSTION;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 信贷报单
 */

public class XinDaiBaoDan extends BaseActivity {
    private String name,idnumber,gjj,sb,yyzz,rsbd,csz,ajfyg,qfsz;

    @ViewInject(R.id.ll_gjj)
    private LinearLayout ll_gjj;
    @ViewInject(R.id.ll_sb)
    private LinearLayout ll_sb;
    @ViewInject(R.id.ll_yyzz)
    private LinearLayout ll_yyzz;

    @ViewInject(R.id.tv_gjj)
    private TextView tv_gjj;
    @ViewInject(R.id.tv_sb)
    private TextView tv_sb;
    @ViewInject(R.id.tv_yyzz)
    private TextView tv_yyzz;
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.et_id)
    private EditText et_id;
    @ViewInject(R.id.et_rsbd)
    private EditText et_rsbd;
    @ViewInject(R.id.et_csz)
    private EditText et_csz;
    @ViewInject(R.id.et_ajfyg)
    private EditText et_ajfyg;
    @ViewInject(R.id.et_qfsz)
    private EditText et_qfsz;

    @ViewInject(R.id.bt_submit)
    private Button bt_submit;
    @Override
    public int setLayout() {
        return R.layout.xin_dai_bao_dan;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.xdbd));
    }

    @Override
    public void setListeners() {
        ll_gjj.setOnClickListener(this);
        ll_sb.setOnClickListener(this);
        ll_yyzz.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_gjj:
                choice(tv_gjj);
                break;
            case R.id.ll_sb:
                choice(tv_sb);
                break;
            case R.id.ll_yyzz:
                choice(tv_yyzz);
                break;
            case R.id.bt_submit:
                submitData();

                break;
        }
    }


    private void choice(final TextView textView){
       new MySelfSheetDialog(context).builder().addSheetItem("有", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
           @Override
           public void onClick(int which) {
               textView.setText("有");
           }
       }).addSheetItem("无", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
           @Override
           public void onClick(int which) {
               textView.setText("无");

           }
       }).show();
    }


    private void submitData(){
        noNet(context);
        name=et_name.getText().toString().trim();
        idnumber=et_id.getText().toString().trim();
        gjj=tv_gjj.getText().toString().trim();
        sb=tv_sb.getText().toString().trim();
        yyzz=tv_yyzz.getText().toString().trim();

        rsbd=et_rsbd.getText().toString().trim();
        csz=et_csz.getText().toString().trim();
        ajfyg=et_ajfyg.getText().toString().trim();
        qfsz=et_qfsz.getText().toString().trim();

        //判断
        if(TextUtils.isEmpty(name)){
            ToastUtil.showShort(context,"借款人姓名不能为空！");
            return;
        }
        if(!CardIdCheck.verify(idnumber)){
            ToastUtil.showShort(context,"身份证格式错误！");
            return;
        }

        if(TextUtils.isEmpty(gjj)){
            ToastUtil.showShort(context,"请选择公积金！");
            return;
        }else {
            if(gjj.equals("无")){
                gjj="0";
            }else
                gjj="1";
        }

        if(TextUtils.isEmpty(sb)){
            ToastUtil.showShort(context,"请选择社保！");
            return;
        }else {
            if(sb.equals("无")){
                sb="0";
            }else
                sb="1";
        }

        if(TextUtils.isEmpty(yyzz)){
            ToastUtil.showShort(context,"请选择营业执照！");
            return;
        }else {
            if(yyzz.equals("无")){
                yyzz="0";
            }else
                yyzz="1";
        }

        dialog();
        String[] keys={"APPUSER_ID","TRUENAME","NUMBERID","PUBLIC","SECURITY","LICENCE","POLICY_AMOUNT","CAR_AMOUNT","HOUSE_AMOUNT","HOME_AMOUNT"};
        String[] values={SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID),
                name,idnumber,gjj,sb,yyzz,rsbd,csz,ajfyg,qfsz};

        String json= JsonUtil.getJsonString(keys,values);
        RequestParams params=new RequestParams(Constant.SEVER_API+"app_order/add.do");

        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("response").equals("0")){
                        ToastUtil.showShort(context,"提交成功！");
                        SharedPreferencesUtils.putIntSharedPreferences(context,POSTION,1);
                        intent=new Intent(context,MainActivity.class);
                        startActivity(intent);
                    }else if (jsonObject.getString("response").equals("10001")) {
                        ToastUtil.showShort(context, jsonObject.getString("desc"));
                        intent=new Intent(context,AddBankCard.class);
                        //2代表是信贷保单
                        intent.putExtra("type",2);
                        startActivity(intent);
                    }else
                        ToastUtil.showShort(context, jsonObject.getString("desc"));

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
