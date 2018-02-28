package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.ipd.bangbanjinrong.widget.PhotoPreviewIntent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import static com.ipd.bangbanjinrong.utils.SharedPreferencesUtils.POSTION;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/9
 * @Email 448739075@qq.com
 * 抵押报单 第三步
 */

public class DiYaBaoDanActivity3 extends BaseActivity {
    private static final String DEFAULT_STR = "000000";

    @ViewInject(R.id.bt_next)
    private Button bt_submit;
    @ViewInject(R.id.ivImage)
    private ImageView imageView;
    @ViewInject(R.id.iv_left)
    private ImageView iv_left;
    @ViewInject(R.id.iv_right)
    private ImageView iv_right;

    @ViewInject(R.id.et_fczh)
    private EditText et_fczh;

    private ArrayList<String> pics = new ArrayList<>();
    private String content;
    private int index = 0;


    @Override
    public int setLayout() {
        return R.layout.di_ya_bao_dan3;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.di_ya_bao_dan));
        pics = getIntent().getStringArrayListExtra("pics");
        content=getIntent().getStringExtra("content");

        Glide.with(context).load(pics.get(index)).into(imageView);

    }

    @Override
    public void setListeners() {
        bt_submit.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                submitData();
                break;
            case R.id.iv_left:
                if (index > 0) {
                    index--;
                    if (index >= 0) {
                        Glide.with(context).load(pics.get(index)).into(imageView);

                    }
                }
                if (index == 0)
                    ToastUtil.showShort(context, "已经是第一张了哦！");
                break;
            case R.id.iv_right:

                if (index < pics.size()) {
                    index++;
                    if (index <= pics.size() - 1)
                        Glide.with(context).load(pics.get(index)).into(imageView);
                    else {
                        ToastUtil.showShort(context, "已经是最后一张了！");
                        index = pics.size() - 1;
                    }
                }
                Log.i("TAG", "++-index=" + index);

                break;
            case R.id.ivImage:
                PhotoPreviewIntent intent = new PhotoPreviewIntent(DiYaBaoDanActivity3.this);
                intent.setCurrentItem(index);
                intent.setFromPage(PhotoPreviewActivity.FROM_PIC_PIC);
                ArrayList<String> previewImages = pics;
                previewImages.remove(DEFAULT_STR);
                intent.setPhotoPaths(previewImages);
                intent.setClass(getApplicationContext(),PhotoPreviewActivity.class);
                startActivity(intent);
                break;
        }
    }



    private void submitData(){
        noNet(context);
        if(TextUtils.isEmpty(et_fczh.getText().toString().trim())){
            ToastUtil.showShort(context,"请输入房产证号！");
            return;
        }else {
            content=content+et_fczh.getText().toString().trim();
        }



        dialog();
        String[] keys={"TRUENAME","APPUSER_ID","NUMBERID","PLEDGE_TYPE","PLEDGE_AMOUNT","MAKE_TYPE","ORGAN","PIC_NO","PIC_HOME","PIC_EXTRA","FACE_CERT","BACK_CERT","HOUSE_NO"};
        String[] values=content.split(";");
        Log.i("TAG","length="+values.length);

        String json= JsonUtil.getJsonString(keys,values);
        RequestParams params=new RequestParams(Constant.SEVER_API+"app_order/addp.do");

        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG","reslit="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("response").equals("0")){
                        ToastUtil.showShort(context,"提交成功！");
                        SharedPreferencesUtils.putIntSharedPreferences(context,POSTION,1);
                        intent=new Intent(context,MainActivity.class);
                        startActivity(intent);
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
