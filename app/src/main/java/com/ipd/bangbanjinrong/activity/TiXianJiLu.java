package com.ipd.bangbanjinrong.activity;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.adapter.TiXianJiLuAdapter;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.TiXianJiLuEnity;
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
 * @Date 2017/8/29
 * @Email 448739075@qq.com
 * 提现记录
 */

public class TiXianJiLu extends BaseActivity {
    private int pageNumber = 0;

    @ViewInject(R.id.listview)
    private PullToRefreshListView listView;
    private List<TiXianJiLuEnity> data = new ArrayList<>();
    private List<TiXianJiLuEnity> Tdata = new ArrayList<>();
    private TiXianJiLuAdapter adapter;

    @Override
    public int setLayout() {
        return R.layout.ti_xian_ji_lu;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle("提现记录");
        listView.setMyDividerHeight(0);
        adapter = new TiXianJiLuAdapter(context, data);
        listView.getRefreshableView().setAdapter(adapter);
    }

    @Override
    public void setListeners() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.onPullDownRefreshComplete();
                pageNumber = 0;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.onPullUpRefreshComplete();
                pageNumber++;
                getData();
            }
        });

        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                show(data.get(position).APPLY_ID,position);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void getData() {
        Tdata.clear();
        if (pageNumber == 0)
            data.clear();

        dialog();
        String[] keys = {"APPUSER_ID", "PAGE"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID), String.valueOf(pageNumber)};
        final String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_apply/findPage.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("jiLu", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        Tdata = JSON.parseArray(jsonObject.getString("data"), TiXianJiLuEnity.class);
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
                deletBankCard(bankId, pos);
            }
        });
        bt_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(viewcall);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }

    /**
     * @param bankId 删除
     */
    private void deletBankCard(String bankId, final int pos) {
        dialog();
        String[] keys = {"APPUSER_ID", "APPLY_ID"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID),
                bankId
        };
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_apply/remove.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("adapterB", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        data.remove(pos);
                        adapter.notifyDataSetChanged();
                        ToastUtil.showShort(context,"删除成功！");
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
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
}
