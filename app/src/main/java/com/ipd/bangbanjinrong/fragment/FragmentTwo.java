package com.ipd.bangbanjinrong.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.adapter.OrderAdapter;
import com.ipd.bangbanjinrong.base.BaseFragment;
import com.ipd.bangbanjinrong.entity.OrderEntity;
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
 * 订单页
 */

public class FragmentTwo extends BaseFragment {
    private int count = 0;
    private int pageNumber = 0, type = 0;// tpye   0:全部 1:贷款中 2:还款中 3:已还清 4:已失效
    private List<String> titleList = new ArrayList<>();

    @ViewInject(R.id.radioGroup)
    private RadioGroup radioGroup;
    @ViewInject(R.id.listView)
    private PullToRefreshListView listView;


    private List<OrderEntity> data;
    private List<OrderEntity> Tempdata;

    private OrderAdapter orderAdapter;

    @Override
    public View setView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_two, null);
    }

    @Override
    public void innitData(Bundle savedInstanceState) {
        data = new ArrayList<>();
        Tempdata = new ArrayList<>();
        orderAdapter = new OrderAdapter(context, data);

        listView.setDividercolor(R.color.gray3);
        listView.setMyDividerHeight(20);
        listView.getRefreshableView().setAdapter(orderAdapter);

    }

    @Override
    public void setListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                pageNumber = 0;
                switch (checkedId) {
                    case R.id.rb1:
                        type = 0;
                        break;
                    case R.id.rb2:
                        type = 1;
                        break;
                    case R.id.rb3:
                        type = 2;
                        break;
                    case R.id.rb4:
                        type = 3;
                        break;
                    case R.id.rb5:
                        type = 4;
                        break;
                }
                getData();
            }
        });


        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 2000);
            }

            ;

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                pageNumber++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 2000);            }
        });

    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 获取订单列表
     */
    private void getData() {
        dialog();
        if (pageNumber == 0) {
            data.clear();
        }
        Tempdata.clear();
        String[] keys = {"APPUSER_ID", "PAGE", "TYPE"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID), String.valueOf(pageNumber), String.valueOf(type)};
        final String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/findPage.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("result", result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        Tempdata = JSON.parseArray(jsonObject.getString("data"), OrderEntity.class);
                        data.addAll(Tempdata);
                    } else {
                        if (count != 0)
                            ToastUtil.showShort(context, jsonObject.getString("desc"));
                    }
                    orderAdapter.notifyDataSetChanged();

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
                LogUtils.print("count",count+"");
                count++;
                listView.onPullDownRefreshComplete();
                listView.onPullUpRefreshComplete();
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.print("onResume",String.valueOf("onResume"));
        if (count > 2&&!isHidden())
            getData();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RefreshFragmentOne");
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("RefreshFragmentOne")) {
                getData();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(broadcastReceiver);
    }
}
