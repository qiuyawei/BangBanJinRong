package com.ipd.bangbanjinrong.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.adapter.BankAdapter;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.BankEntity;
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

import static com.ipd.bangbanjinrong.global.Constant.TI_XIAN_RESULT_CODE;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 银行卡
 */

public class BankCard extends BaseActivity {
    private boolean flag = false;//点击银行卡列表是否有作用   false  无反应   true  选择卡
    private int page = 0;
    private String type = "0";
    @ViewInject(R.id.listview)
    private PullToRefreshListView listView;

    private List<BankEntity> Tdata;
    private List<BankEntity> data;
    private BankAdapter bankAdapter;

    @ViewInject(R.id.bt_addBk)
    private Button bt_addBk;

    @Override
    public int setLayout() {
        return R.layout.bank_card;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.yhk));
        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
        }
        flag = getIntent().getBooleanExtra("flage", false);
        Tdata = new ArrayList<>();
        data = new ArrayList<>();
        listView.setMyDividerHeight(0);
        bankAdapter = new BankAdapter(context, data);
        listView.getRefreshableView().setAdapter(bankAdapter);
    }

    @Override
    public void setListeners() {
        bt_addBk.setOnClickListener(this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onPullDownRefreshComplete();
                        page = 0;
                        getBankList();
                    }
                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.onPullUpRefreshComplete();
                page++;
                getBankList();
            }
        });

        listView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!flag)
                    return;
                intent = new Intent();
                intent.putExtra("entity", data.get(position));
                setResult(TI_XIAN_RESULT_CODE, intent);
                finish();
            }
        });

        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                show(data.get(position).BANKS_ID, position);
                return false;

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_addBk:
                intent = new Intent(context, AddBankCard.class);
                intent.putExtra("flage",flag);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取银行卡列表
     */
    private void getBankList() {
        if (page == 0) {
            data.clear();
        }
        Tdata.clear();
        dialog();
        String[] keys = {"APPUSER_ID", "PAGE", "TYPE"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID), String.valueOf(page), type};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_home/findPage.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("checkSMS", result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        Tdata = JSON.parseArray(json.getString("data"), BankEntity.class);
                        data.addAll(Tdata);
                    } else ToastUtil.showShort(context, json.getString("desc"));

                    bankAdapter.notifyDataSetChanged();
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

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RefreshBank");
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("RefreshBank")) {
                getBankList();
            }
        }
    };


    public void show(final String bankId, final int pos) {
        final Dialog dialog = new Dialog(context, R.style.custom_dialog);
        final LayoutInflater inflater = LayoutInflater.from(context);
        View viewcall = inflater.inflate(R.layout.um_dialog, null, true);

        Button bt_sure = (Button) viewcall.findViewById(R.id.bt_sure);
        Button bt_cancell = (Button) viewcall.findViewById(R.id.bt_cancell);


//            更新
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                ToastUtil.show(context, msg + "成功！");
                deletBankCard(bankId, pos);
            }
        });
        bt_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                data.get(pos).ifcanSee = false;
            }
        });
        dialog.setContentView(viewcall);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }


    /**
     * @param bankId 删除银行卡
     */
    private void deletBankCard(String bankId, final int pos) {
        String[] keys = {"APPUSER_ID", "BANKS_ID"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID),
                bankId
        };
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_banks/remove.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("adapterB", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        data.remove(pos);
                        bankAdapter.notifyDataSetChanged();
                    } else
                        ToastUtil.showShort(context, jsonObject.getString("desc"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.print("ex", ex.getMessage());
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
}
