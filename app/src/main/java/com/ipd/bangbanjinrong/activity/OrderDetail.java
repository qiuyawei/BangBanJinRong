package com.ipd.bangbanjinrong.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.adapter.Adapter1;
import com.ipd.bangbanjinrong.adapter.Adapter2;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.Entity1;
import com.ipd.bangbanjinrong.entity.Entity2;
import com.ipd.bangbanjinrong.entity.OrderDetailEntity;
import com.ipd.bangbanjinrong.entity.OrderEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.LogUtils;
import com.ipd.bangbanjinrong.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;


/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 订单详情
 */

public class OrderDetail extends BaseActivity {
    private List<OrderDetailEntity> orderDetailEntity = new ArrayList<>();
    private OrderEntity orderEntity;
    private String id;

    //一级标题
    //二级标题
    private String[][] titleTwo = {{"订单编号", "提交订单时间", "借款人", "借款人身份证号", "订单状态", "签约机构"},
            {"初评申请金额", "房产证号", "抵押顺位"},
            {"初评结果", "最高可贷金额"},
            {"调查预约结果", "调查员姓名", "调查事件", "联系电话", "支付情况"},
            {"调查状态", "补充资料", "奖励情况"},
            {"审批结果", "产品类型", "审批金额", "最高期限", "基准借贷利率", "基准服务利率"},
            {"产品类型", "合同借款金额", "合同借款期限", "总点位", "合同借款利率", "月利息", "合同服务费率", "合同服务费", "平台费率", "返佣平台费", "返佣服务费", "返佣金额"},
            {"面签地址", "面签资料"},
            {"进抵预约结果", "权证员姓名", "预约时间", "联系电话"},
            {"放款时间", "放款户名", "放款账户", "放款金额"},

            {"卡号", "户名", "开户行"}


    };
    private List<Entity2> data11 = new ArrayList<>();
    private Adapter2 adapter11;


    @ViewInject(R.id.myList1)
    private MyListView myList1;
    @ViewInject(R.id.myList2)
    private MyListView myList2;
    @ViewInject(R.id.myList3)
    private MyListView myList3;
    @ViewInject(R.id.myList4)
    private MyListView myList4;
    @ViewInject(R.id.myList5)
    private MyListView myList5;
    @ViewInject(R.id.myList6)
    private MyListView myList6;
    @ViewInject(R.id.myList7)
    private MyListView myList7;
    @ViewInject(R.id.myList8)
    private MyListView myList8;
    @ViewInject(R.id.myList9)
    private MyListView myList9;
    @ViewInject(R.id.myList10)
    private MyListView myList10;
    @ViewInject(R.id.myList11)
    private MyListView myList11;
    @ViewInject(R.id.myList12)
    private MyListView myList12;
    @ViewInject(R.id.myList13)
    private MyListView myList13;

    private Adapter1 adapter1, adapter2, adapter3, adapter4, adapter5, adapter6, adapter7, adapter8, adapter9, adapter10, adapter12, adapter13;
    private List<Entity1> data1 = new ArrayList<>();
    private List<Entity1> data2 = new ArrayList<>();
    private List<Entity1> data3 = new ArrayList<>();
    private List<Entity1> data4 = new ArrayList<>();
    private List<Entity1> data5 = new ArrayList<>();
    private List<Entity1> data6 = new ArrayList<>();
    private List<Entity1> data7 = new ArrayList<>();
    private List<Entity1> data8 = new ArrayList<>();
    private List<Entity1> data9 = new ArrayList<>();
    private List<Entity1> data10 = new ArrayList<>();
    private List<Entity1> data12 = new ArrayList<>();
    private List<Entity1> data13 = new ArrayList<>();

    private List<String> Tdata1 = new ArrayList<>();
    private List<String> Tdata2 = new ArrayList<>();
    private List<String> Tdata3 = new ArrayList<>();
    private List<String> Tdata4 = new ArrayList<>();
    private List<String> Tdata5 = new ArrayList<>();
    private List<String> Tdata6 = new ArrayList<>();
    private List<String> Tdata7 = new ArrayList<>();
    private List<String> Tdata8 = new ArrayList<>();
    private List<String> Tdata9 = new ArrayList<>();
    private List<String> Tdata10 = new ArrayList<>();
    private List<String> Tdata12 = new ArrayList<>();
    private List<String> Tdata13 = new ArrayList<>();

    @ViewInject(R.id.ll_jkxx)
    private LinearLayout ll2;
    @ViewInject(R.id.ll_cpjg)
    private LinearLayout ll3;
    @ViewInject(R.id.ll_xhyyxx)
    private LinearLayout ll4;
    @ViewInject(R.id.ll_dcxx)
    private LinearLayout ll5;
    @ViewInject(R.id.ll_spyj)
    private LinearLayout ll6;
    @ViewInject(R.id.ll_htxx)
    private LinearLayout ll7;
    @ViewInject(R.id.ll_mqxx)
    private LinearLayout ll8;
    @ViewInject(R.id.ll_jdyyxx)
    private LinearLayout ll9;
    @ViewInject(R.id.ll_fkxx)
    private LinearLayout ll10;
    @ViewInject(R.id.ll_hkjh)
    private LinearLayout ll11;
    @ViewInject(R.id.ll_title)
    private LinearLayout ll_title;
    @ViewInject(R.id.cb2)
    private CheckBox cb2;
    @ViewInject(R.id.cb3)
    private CheckBox cb3;
    @ViewInject(R.id.cb4)
    private CheckBox cb4;
    @ViewInject(R.id.cb5)
    private CheckBox cb5;
    @ViewInject(R.id.cb6)
    private CheckBox cb6;
    @ViewInject(R.id.cb7)
    private CheckBox cb7;
    @ViewInject(R.id.cb8)
    private CheckBox cb8;
    @ViewInject(R.id.cb9)
    private CheckBox cb9;
    @ViewInject(R.id.cb10)
    private CheckBox cb10;
    @ViewInject(R.id.cb11)
    private CheckBox cb11;

    @ViewInject(R.id.rl2)
    private RelativeLayout rl2;
    @ViewInject(R.id.rl3)
    private RelativeLayout rl3;
    @ViewInject(R.id.rl4)
    private RelativeLayout rl4;
    @ViewInject(R.id.rl5)
    private RelativeLayout rl5;
    @ViewInject(R.id.rl6)
    private RelativeLayout rl6;
    @ViewInject(R.id.rl7)
    private RelativeLayout rl7;
    @ViewInject(R.id.rl8)
    private RelativeLayout rl8;
    @ViewInject(R.id.rl9)
    private RelativeLayout rl9;
    @ViewInject(R.id.rl10)
    private RelativeLayout rl10;
    @ViewInject(R.id.rl11)
    private RelativeLayout rl11;
    @ViewInject(R.id.tv_extra)
    private TextView tv_extra;

    //新加  失败原因
    @ViewInject(R.id.ll_cause)
    private LinearLayout ll_cause;
    @ViewInject(R.id.tv_cause)
    private TextView tv_cause;

    @Override
    public int setLayout() {
        return R.layout.order_detail;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.ddxq));
        id = getIntent().getStringExtra("id");

        adapter1 = new Adapter1(context, data1, Tdata1);
        adapter2 = new Adapter1(context, data2, Tdata2);
        adapter3 = new Adapter1(context, data3, Tdata3);
        adapter4 = new Adapter1(context, data4, Tdata4);
        adapter5 = new Adapter1(context, data5, Tdata5);
        adapter6 = new Adapter1(context, data6, Tdata6);
        adapter7 = new Adapter1(context, data7, Tdata7);
        adapter8 = new Adapter1(context, data8, Tdata8);
        adapter9 = new Adapter1(context, data9, Tdata9);
        adapter10 = new Adapter1(context, data10, Tdata10);
        adapter11 = new Adapter2(context, data11);

        adapter12 = new Adapter1(context, data12, Tdata12);
        adapter13 = new Adapter1(context, data13, Tdata13);

        myList1.setAdapter(adapter1);
        myList2.setAdapter(adapter2);
        myList3.setAdapter(adapter3);
        myList4.setAdapter(adapter4);
        myList5.setAdapter(adapter5);
        myList6.setAdapter(adapter6);
        myList7.setAdapter(adapter7);
        myList8.setAdapter(adapter8);
        myList9.setAdapter(adapter9);
        myList10.setAdapter(adapter10);
        myList10.setAdapter(adapter10);
        myList11.setAdapter(adapter11);
        myList12.setAdapter(adapter12);
        myList13.setAdapter(adapter13);

//        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void setListeners() {
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        rl6.setOnClickListener(this);
        rl7.setOnClickListener(this);
        rl8.setOnClickListener(this);
        rl9.setOnClickListener(this);
        rl10.setOnClickListener(this);
        rl11.setOnClickListener(this);

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll2.setVisibility(View.VISIBLE);
                } else
                    ll2.setVisibility(View.GONE);

            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll3.setVisibility(View.VISIBLE);
                } else
                    ll3.setVisibility(View.GONE);

            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll4.setVisibility(View.VISIBLE);
                } else
                    ll4.setVisibility(View.GONE);

            }
        });
        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll5.setVisibility(View.VISIBLE);
                } else
                    ll5.setVisibility(View.GONE);

            }
        });
        cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll6.setVisibility(View.VISIBLE);
                } else
                    ll6.setVisibility(View.GONE);

            }
        });
        cb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll7.setVisibility(View.VISIBLE);
                } else
                    ll7.setVisibility(View.GONE);

            }
        });
        cb8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll8.setVisibility(View.VISIBLE);
                } else
                    ll8.setVisibility(View.GONE);

            }
        });
        cb9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll9.setVisibility(View.VISIBLE);
                } else
                    ll9.setVisibility(View.GONE);

            }
        });

        cb10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll10.setVisibility(View.VISIBLE);
                } else
                    ll10.setVisibility(View.GONE);

            }
        });
        cb11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll11.setVisibility(View.VISIBLE);
                } else
                    ll11.setVisibility(View.GONE);

            }
        });


        myList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4 && Tdata1.get(position).contains("预约下户") && (Tdata1.get(Tdata1.size() - 1).contains("6") || Tdata1.get(Tdata1.size() - 1).contains("7") || Tdata1.get(Tdata1.size() - 1).contains("11"))) {
                    intent = new Intent(context, YuYueXiaHu.class);
                    intent.putExtra("entity", orderEntity);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl2:
                if (cb2.isChecked()) {
                    cb2.setChecked(false);
                } else
                    cb2.setChecked(true);

                break;
            case R.id.rl3:
                if (cb3.isChecked()) {
                    cb3.setChecked(false);
                } else
                    cb3.setChecked(true);
                break;
            case R.id.rl4:
                if (cb4.isChecked()) {
                    cb4.setChecked(false);
                } else
                    cb4.setChecked(true);
                break;
            case R.id.rl5:
                if (cb5.isChecked()) {
                    cb5.setChecked(false);
                } else
                    cb5.setChecked(true);
                break;
            case R.id.rl6:
                if (cb6.isChecked()) {
                    cb6.setChecked(false);
                } else
                    cb6.setChecked(true);
                break;
            case R.id.rl7:
                if (cb7.isChecked()) {
                    cb7.setChecked(false);
                } else
                    cb7.setChecked(true);
                break;
            case R.id.rl8:
                if (cb8.isChecked()) {
                    cb8.setChecked(false);
                } else
                    cb8.setChecked(true);
                break;
            case R.id.rl9:
                if (cb9.isChecked()) {
                    cb9.setChecked(false);
                } else
                    cb9.setChecked(true);
                break;
            case R.id.rl10:
                if (cb10.isChecked()) {
                    cb10.setChecked(false);
                } else
                    cb10.setChecked(true);
                break;
            case R.id.rl11:
                if (cb11.isChecked()) {
                    cb11.setChecked(false);
                } else
                    cb11.setChecked(true);
                break;

        }
    }

    private void setData() {
        for (int i = 0; i < titleTwo[0].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[0][i];
            data1.add(entity1);
        }


        for (int i = 0; i < titleTwo[1].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[1][i];
            data2.add(entity1);
        }


        for (int i = 0; i < titleTwo[2].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[2][i];
            data3.add(entity1);
        }


        for (int i = 0; i < titleTwo[3].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[3][i];
            data4.add(entity1);
        }


        for (int i = 0; i < titleTwo[4].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[4][i];
            data5.add(entity1);
        }


        for (int i = 0; i < titleTwo[5].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[5][i];
            data6.add(entity1);
        }


        for (int i = 0; i < titleTwo[6].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[6][i];
            data7.add(entity1);
        }


        for (int i = 0; i < titleTwo[7].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[7][i];
            data8.add(entity1);
        }


        for (int i = 0; i < titleTwo[8].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[8][i];
            data9.add(entity1);
        }


        for (int i = 0; i < titleTwo[9].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[9][i];
            data10.add(entity1);
        }


        for (int i = 0; i < titleTwo[10].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[10][i];
            data12.add(entity1);
        }


        for (int i = 0; i < titleTwo[10].length; i++) {
            Entity1 entity1 = new Entity1();
            entity1.title = titleTwo[10][i];
            data13.add(entity1);
        }

        if (data11.size() == 0) {
            ll_title.setVisibility(View.GONE);
        } else if (data11.size() > 0) {
            ll_title.setVisibility(View.VISIBLE);
        }


        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        adapter3.notifyDataSetChanged();
        adapter4.notifyDataSetChanged();
        adapter5.notifyDataSetChanged();
        adapter6.notifyDataSetChanged();
        adapter7.notifyDataSetChanged();
        adapter8.notifyDataSetChanged();
        adapter9.notifyDataSetChanged();
        adapter10.notifyDataSetChanged();
        adapter11.notifyDataSetChanged();
        adapter12.notifyDataSetChanged();
        adapter13.notifyDataSetChanged();


        LogUtils.print("size11", data11.size() + "");

    }


    /**
     * 获取订单列表
     */
    private void getData() {
        data1.clear();
        data2.clear();
        data3.clear();
        data4.clear();
        data5.clear();
        data6.clear();
        data7.clear();
        data8.clear();
        data9.clear();
        data10.clear();
        data11.clear();
        data12.clear();
        data13.clear();

        Tdata1.clear();
        Tdata2.clear();
        Tdata3.clear();
        Tdata4.clear();
        Tdata5.clear();
        Tdata6.clear();
        Tdata7.clear();
        Tdata8.clear();
        Tdata9.clear();
        Tdata10.clear();
        Tdata12.clear();
        Tdata13.clear();

        dialog();
        String[] keys = {"ORDER_ID"};
        String[] values = {id};
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_order/get.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("orderDetail", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    orderEntity = JSON.parseObject(jsonObject.getString("data"), OrderEntity.class);
                    orderDetailEntity = parseArray(jsonObject.getString("data2"), OrderDetailEntity.class);
                    data11.addAll(JSON.parseArray(jsonObject.getString("data3"), Entity2.class));
                    //失败原因
                    if (orderEntity.ORDER_STATUS.contains("失败") || orderEntity.ORDER_STATUS.contains("拒绝")) {
                        ll_cause.setVisibility(View.VISIBLE);
                        tv_cause.setText(orderEntity.REMARK);
                    } else {
                        ll_cause.setVisibility(View.GONE);
                    }


                    Tdata1.add(orderEntity.ORDER_NO);
                    Tdata1.add(orderEntity.CREATETIME);
                    Tdata1.add(orderEntity.TRUENAME);
                    Tdata1.add(orderEntity.NUMBERID);
                    if (orderEntity.STATUS.contains("6") || orderEntity.STATUS.contains("7") || orderEntity.STATUS.contains("11")) {
                        Tdata1.add("预约下户");
                    } else {
                        Tdata1.add(orderEntity.ORDER_STATUS);
                    }
                    Tdata1.add(orderEntity.COMPANY);
                    Tdata1.add(orderEntity.STATUS);

                    //child2 借款信息
                    Tdata2.add(orderEntity.FIRST_AMOUNT + "万元");
                    Tdata2.add(orderEntity.HOUSE_NO);
                    switch (orderEntity.PLEDGE_TYPE) {
                        case "0":
                            Tdata2.add("一低");
                            break;
                        case "1":
                            Tdata2.add("二低");
                            break;
                    }
                    //child3 初评结果
                    Tdata3.add(orderEntity.STATUS1);
                    Tdata3.add(orderEntity.MAX_AMOUNT + "万元");
                    //child4下户预约信息
                    Tdata4.add(orderEntity.STATUS2);
                    Tdata4.add(orderEntity.CHECK_NAME);
                    Tdata4.add(orderEntity.CHECKTIME);
                    Tdata4.add(orderEntity.PHONE);
                    Tdata4.add(orderEntity.STATUS3);

                    //调查信息
                    Tdata5.add(orderEntity.STATUS4);
                    Tdata5.add(orderEntity.SUPPLY);
                    Tdata5.add("无奖励");//暂且写死


                    //审批意见
                    Tdata6.add(orderEntity.STATUS5);

                    /*switch (orderEntity.TYPE){
                        case "0":
                            Tdata6.add("抵押");
                            break;
                        case "1":
                            Tdata6.add("信用");
                            break;

                    }*/
                    if (orderDetailEntity.size() > 0) {
                        Tdata6.add(orderDetailEntity.get(0).GOODS_NAME);
                        Tdata6.add(orderDetailEntity.get(0).CHECK_AMOUNT + "万元");
                        Tdata6.add(orderDetailEntity.get(0).MAX_LIMIT + "个月");
                        Tdata6.add(orderDetailEntity.get(0).BASE_REBATE + "%");
                        Tdata6.add(orderDetailEntity.get(0).CONTRACT_REBATE + "%");
                    } else {
                        Tdata6.add("");
                        Tdata6.add("");
                        Tdata6.add("");
                        Tdata6.add("");
                        Tdata6.add("");
                    }
                    tv_extra.setText(orderEntity.EXTRA);


                    //7 合同信息
                    /*switch (orderEntity.TYPE){
                        case "0":
                            Tdata7.add("抵押");
                            break;
                        case "1":
                            Tdata7.add("信用");
                           break;
                    }*/

                    Tdata7.add(orderEntity.GOODS_NAME);
                    Tdata7.add(orderEntity.CONTRACT_AMOUNT + "万元");
                    Tdata7.add(orderEntity.CONTRACT_LIMIT + "个月");
                    Tdata7.add(orderEntity.POINT + "%");
                    Tdata7.add(orderEntity.CONTRACT_REBATE + "%");
                    Tdata7.add(orderEntity.MONTH_AMOUNT + "万元");
                    Tdata7.add(orderEntity.SERVER_LIMIT + "%");
                    Tdata7.add(orderEntity.SERVER_AMOUNT + "万元");
                    Tdata7.add(orderEntity.PC_LIMIT + "%");
                    Tdata7.add(orderEntity.BACK_PC_AMOUNT + "万元");
                    Tdata7.add(orderEntity.BACK_SERVER_AMOUNT + "万元");
                    Tdata7.add(orderEntity.BACK_AMOUNT + "万元");

                    //8面签信息
                    Tdata8.add(orderEntity.FACE_ADDRESS);
                    Tdata8.add(orderEntity.FACE_INFO);

                    //9 进抵预约信息
                    Tdata9.add(orderEntity.STATUS6);
                    Tdata9.add(orderEntity.WARRANT);
                    Tdata9.add(orderEntity.APPOINTTIME);
                    Tdata9.add(orderEntity.APPOINT_PHONE);

                    //10  放款信息
                    Tdata10.add(orderEntity.CREDIT_TIME);
                    Tdata10.add(orderEntity.CREDIT_NAME);
                    Tdata10.add(orderEntity.CREDIT_ID);
                    Tdata10.add(orderEntity.CREDIT_AMOUNT + "万元");

                    //11  还款计划 咱时没有


                    //  12
                    Tdata12.add(orderEntity.BASE_CARNO);
                    Tdata12.add(orderEntity.BASE_NAME);
                    Tdata12.add(orderEntity.BASE_BANK);
                    //  13
                    Tdata13.add(orderEntity.SERVER_CARNO);
                    Tdata13.add(orderEntity.SERVER_NAME);
                    Tdata13.add(orderEntity.SERVER_BANK);


                    setData();
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
