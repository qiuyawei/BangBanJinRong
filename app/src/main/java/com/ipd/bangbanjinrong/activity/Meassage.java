package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.adapter.MessageAdapter;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.MessageEntity;
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
 * 消息
 */

public class Meassage extends BaseActivity {
    private int pageNumber = 0;
    @ViewInject(R.id.puRefresh)
    private PullToRefreshListView listView;

    private List<MessageEntity> data = new ArrayList<>();
    private List<MessageEntity> Tempdata = new ArrayList<>();

    private MessageAdapter adapter;

    @Override
    public int setLayout() {
        return R.layout.message;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.xx));

        adapter = new MessageAdapter(context, data);
        listView.setMyDividerHeight(0);
        listView.getRefreshableView().setAdapter(adapter);
    }

    @Override
    public void setListeners() {
        listView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(context, MessageDetail.class);
                intent.putExtra("entity", data.get(position));
                startActivity(intent);
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNumber=0;
                        getMessage();
                    }
                }, 500);


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber++;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


    private void getMessage() {
        Tempdata.clear();
        if (pageNumber == 0) {
            data.clear();
        }
        dialog();
        //TYPE 0 系统消息
        String[] keys = {"APPUSER_ID", "PAGE"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID), String.valueOf(pageNumber)};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_info/findPage.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("Mesage", result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        Tempdata = JSON.parseArray(json.getString("data"), MessageEntity.class);
                        data.addAll(Tempdata);
                        if (data.size() == 0)
                            ToastUtil.showShort(context, "暂无消息！");

                    } else
                        ToastUtil.showShort(context, json.getString("desc"));

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
                pullComplet();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
    }

    private void pullComplet() {
        listView.onPullUpRefreshComplete();
        listView.onPullDownRefreshComplete();
    }
}
