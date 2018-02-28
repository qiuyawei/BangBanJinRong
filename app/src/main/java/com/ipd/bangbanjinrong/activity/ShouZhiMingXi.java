package com.ipd.bangbanjinrong.activity;

import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.adapter.Adapter3;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.RecordEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.LogUtils;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.jumpbox.jumpboxlibrary.pulltorefresh.PullToRefreshBase;
import com.jumpbox.jumpboxlibrary.pulltorefresh.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 收支明细
 */

public class ShouZhiMingXi extends BaseActivity {
    private int pageNumber=0;
    private List<RecordEntity> Tdata = new ArrayList<>();
    private List<RecordEntity> data = new ArrayList<>();
    private Adapter3 adapter;

    @ViewInject(R.id.listview)
    private PullToRefreshListView listView;

    @Override
    public int setLayout() {
        return R.layout.shou_zhi_ming_xi;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.szmx));
        adapter=new Adapter3(context,data);
        listView.getRefreshableView().setAdapter(adapter);
        getBalance();
    }

    @Override
    public void setListeners() {


        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.onPullDownRefreshComplete();
                pageNumber=0;
                getBalance();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.onPullUpRefreshComplete();
                pageNumber++;
                getBalance();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取收支明细
     */
    private void getBalance() {
        dialog();
        Tdata.clear();
        if(pageNumber==0){
            data.clear();
        }
        String[] keys = {"APPUSER_ID","PAGE"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID),String.valueOf(pageNumber)};
        final String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_record/findPage.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("re",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        Tdata.addAll(JSON.parseArray(jsonObject.getString("data"), RecordEntity.class));
                        data.addAll(Tdata);
                    } else
                        ToastUtil.showShort(context, jsonObject.getString("desc"));
                    adapter.notifyDataSetChanged();
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
