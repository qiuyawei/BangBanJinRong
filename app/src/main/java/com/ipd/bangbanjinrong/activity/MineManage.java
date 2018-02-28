package com.ipd.bangbanjinrong.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
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
 * 我的经理
 */

public class MineManage extends BaseActivity {
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;
    private String id;//经理id
    private LoginEntity loginEntity;
    @ViewInject(R.id.iv_photo)
    private ImageView iv_photo;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_city)
    private TextView tv_city;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @Override
    public int setLayout() {
        return R.layout.mine_manage;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.wdjl));
        /*setRightText("扫描", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开二维码扫描界面
                if(CommonUtil.checkPersiomion(MineManage.this, Manifest.permission.CAMERA)){
                    Intent intent = new Intent(MineManage.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    ToastUtil.showShort(context,"请打开此应用的摄像头权限！");
                }
            }
        });*/
        id= SharedPreferencesUtils.getSharedPreferences(context,Constant.MANAGE_ID);
        getMineMannge();
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 我的经理接口
     */
    private void getMineMannge() {
        dialog();
        String[] keys = {"MANAGER_ID"};
        String[] values = {id};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/myManager.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("Manage",result);
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

    private void setData(){
        if(loginEntity==null)
            return;
        RequestOptions requestOptions = RequestOptions.circleCropTransform().placeholder(R.mipmap.defalt_photo).error(R.mipmap.defalt_photo);
        Glide.with(context).load(Constant.IMAGE_API+loginEntity.LOGO).apply(requestOptions).into(iv_photo);
        tv_name.setText(loginEntity.TRUENAME);
        tv_city.setText(loginEntity.ADDRESS);
        tv_phone.setText(loginEntity.PHONE);
    }


  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.print("resultCode",resultCode+"");

            if(requestCode==REQUEST_CODE&&resultCode==CaptureActivity.RESULT_CODE_QR_SCAN){
                Bundle bundle=data.getExtras();
                String result=bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                if(!TextUtils.isEmpty(result)){
                    handScan(result);
                }
            }

    }*/


    /**
     * @param result
     * 扫描成功调接口
     */
    private void handScan(String result){
        dialog();
        String[] keys = {"APPUSER_ID","CODE"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context,Constant.USER_ID),result};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/twoCode.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("scan",result);
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


}
