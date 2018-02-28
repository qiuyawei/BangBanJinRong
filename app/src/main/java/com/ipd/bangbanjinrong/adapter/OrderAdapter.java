package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.activity.OrderDetail;
import com.ipd.bangbanjinrong.activity.PayServiceFee;
import com.ipd.bangbanjinrong.activity.YuYueXiaHu;
import com.ipd.bangbanjinrong.entity.OrderEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


/**
 * 订单适配器
 */
public class OrderAdapter extends BaseAdapter {
    private List<OrderEntity> data;
    private Context context;

    public OrderAdapter(Context mContext, List<OrderEntity> data) {
        this.data = data;
        this.context = mContext;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_order, null);
            viewHolder = new ViewHolder();
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.tv_orderNumber);
            viewHolder.state = (TextView) convertView.findViewById(R.id.tv_state);
            viewHolder.jieKuanRen = (TextView) convertView.findViewById(R.id.tv_jieKuanRen);
            viewHolder.baoDanJiGou = (TextView) convertView.findViewById(R.id.tv_baoDanJiGou);
            viewHolder.ll_bt = (LinearLayout) convertView.findViewById(R.id.ll_bt);
            viewHolder.bt_pre = (Button) convertView.findViewById(R.id.bt_pre);
            viewHolder.bt_cancell = (Button) convertView.findViewById(R.id.bt_cancell);
            viewHolder.bt_pay = (Button) convertView.findViewById(R.id.bt_pay);
            viewHolder.ll_par = (LinearLayout) convertView.findViewById(R.id.ll_par);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.orderNumber.setText(data.get(position).ORDER_NO);
        viewHolder.bt_cancell.setVisibility(View.GONE);
        viewHolder.bt_pay.setVisibility(View.GONE);
        viewHolder.bt_pre.setVisibility(View.GONE);

        switch (Integer.parseInt(data.get(position).STATUS)) {

            case 6:
                //6,7出现下户按钮
                viewHolder.ll_bt.setVisibility(View.VISIBLE);
                viewHolder.bt_pre.setVisibility(View.VISIBLE);
                break;
            case 7:
                //6,7出现下户按钮   7是取消过后一次了
                viewHolder.ll_bt.setVisibility(View.VISIBLE);
                viewHolder.bt_pre.setVisibility(View.VISIBLE);
                break;
            case 10:
                //已下户(待预约  还没有分配经理)
                viewHolder.ll_bt.setVisibility(View.VISIBLE);
                viewHolder.bt_cancell.setVisibility(View.VISIBLE);
                break;
            case 11:
                //预约失败     出现下户按钮
                viewHolder.ll_bt.setVisibility(View.VISIBLE);
                viewHolder.bt_pre.setVisibility(View.VISIBLE);
                break;
            case 12:
                //12出现去支付
                viewHolder.ll_bt.setVisibility(View.VISIBLE);
                viewHolder.bt_pay.setVisibility(View.VISIBLE);
                viewHolder.bt_cancell.setVisibility(View.VISIBLE);

            case 13:
                //已支付
                viewHolder.ll_bt.setVisibility(View.VISIBLE);
                viewHolder.bt_cancell.setVisibility(View.VISIBLE);
                break;
            default:
                viewHolder.ll_bt.setVisibility(View.GONE);
                break;

        }


        viewHolder.state.setText(data.get(position).ORDER_STATUS);
        viewHolder.jieKuanRen.setText(data.get(position).TRUENAME);

        if (TextUtils.isEmpty(data.get(position).COMPANY)) {
            viewHolder.baoDanJiGou.setText("个人");
        } else
            viewHolder.baoDanJiGou.setText(data.get(position).COMPANY);

//        预约下户
        viewHolder.bt_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() == 0)
                    return;
                Intent intent = new Intent(context, YuYueXiaHu.class);
                intent.putExtra("entity", data.get(position));
                context.startActivity(intent);
            }
        });
        //        取消预约下户
        viewHolder.bt_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() == 0)
                    return;
                cancellPre(data.get(position).ORDER_NO);
            }
        });

        //        支付
        viewHolder.bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() == 0)
                    return;
                Intent intent=new Intent(context, PayServiceFee.class);
                intent.putExtra("entity",data.get(position));
                context.startActivity(intent);
            }
        });
        viewHolder.ll_par.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() == 0)
                    return;
                Intent intent = new Intent(context, OrderDetail.class);
                intent.putExtra("id", data.get(position).ORDER_ID);
                context.startActivity(intent);
           }
        });


        return convertView;
    }


    public class ViewHolder {
        TextView orderNumber, state, jieKuanRen, baoDanJiGou;
        LinearLayout ll_bt, ll_par;
        Button bt_pre, bt_cancell, bt_pay;
    }

    /**
     * @param orderNumber 取消预约
     */
    private void cancellPre(String orderNumber) {
        String[] keys = {"APPUSER_ID", "ORDER_NO"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID),
                orderNumber
        };
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/cancelAppoint.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        Intent intent = new Intent("RefreshFragmentOne");
                        context.sendBroadcast(intent);
                    } else
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

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


}
