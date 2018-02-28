package com.ipd.bangbanjinrong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.activity.DiYaBaoDanActivity;
import com.ipd.bangbanjinrong.activity.GuoQiaoActivity;
import com.ipd.bangbanjinrong.activity.Meassage;
import com.ipd.bangbanjinrong.activity.XinDaiBaoDan;
import com.ipd.bangbanjinrong.adapter.BannerAdapter;
import com.ipd.bangbanjinrong.adapter.DaiKuanSortAdapter;
import com.ipd.bangbanjinrong.base.BaseFragment;
import com.ipd.bangbanjinrong.entity.AdvEntity;
import com.ipd.bangbanjinrong.entity.DaiKuanSortEnity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.CommonUtil;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.LogUtils;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.ipd.bangbanjinrong.widget.MyListView;
import com.ipd.bangbanjinrong.widget.VpSwipeRefreshLayout;

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
 * @Date 2017/8/7
 * @Email 448739075@qq.com
 */

public class FragmentOne extends BaseFragment {
    private int pos = 0;
    private BannerAdapter adapter;

    @ViewInject(R.id.sf)
    private VpSwipeRefreshLayout sf;
    @ViewInject(R.id.sll)
    private ScrollView scrollView;

    @ViewInject(R.id.myList)
    private MyListView myListView;

    @ViewInject(R.id.bt_baoDan)
    private Button bt_baoDan;

    @ViewInject(R.id.iv_rightImage)
    private ImageView iv_rightImage;
    private List<DaiKuanSortEnity> data = new ArrayList<>();
    ;
    private DaiKuanSortAdapter daiKuanSortAdapter;

    @ViewInject(R.id.vp_shuffling)
    private ViewPager vp_shuffling;
    @ViewInject(R.id.layout_point)
    private LinearLayout dotLayout; // 圈圈 布局

    private List<AdvEntity> mADParseArray = new ArrayList<AdvEntity>();
    ;
    private final int HOME_AD_RESULT = 1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 广告
                case HOME_AD_RESULT:
                    vp_shuffling.setCurrentItem(vp_shuffling.getCurrentItem() + 1,
                            true);
                    break;
            }
        }

        ;
    };

    @Override
    public View setView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_one, null);
    }

    @Override
    public void innitData(Bundle bundle) {



        sf.setColorSchemeColors(context.getResources().getColor(R.color.base_color));
//        swiperefreshlayout与scrollview的冲突问题。
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (sf != null) {
                        sf.setEnabled(scrollView.getScrollY() == 0);
                    }
                }
            });
        }
//        模拟数据
        daiKuanSortAdapter = new DaiKuanSortAdapter(context, data);
        myListView.setAdapter(daiKuanSortAdapter);
        getData();
    }

    @Override
    public void setListeners() {
        bt_baoDan.setOnClickListener(this);
        iv_rightImage.setOnClickListener(this);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean ischek = data.get(position).isChecked;
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).isChecked = false;
                }
                data.get(position).isChecked = !ischek;
                daiKuanSortAdapter.notifyDataSetChanged();
            }
        });

        sf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sf.setRefreshing(false);
                getData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_baoDan:
                boolean hasChecked = false;
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked) {
                        hasChecked = true;
                        pos = i;
                    }
                }
                if (!hasChecked) {
                    ToastUtil.showShort(context, "请选择一种报单方式！");
                    return;
                }
                if (data.get(pos).TYPE.equals("0"))
                    intent = new Intent(getActivity(), DiYaBaoDanActivity.class);
                if (data.get(pos).TYPE.equals("1"))
                    intent = new Intent(getActivity(), XinDaiBaoDan.class);
                if (data.get(pos).TYPE.equals("2"))
                    intent = new Intent(getActivity(), GuoQiaoActivity.class);
                if (intent != null)
                    startActivity(intent);
                break;
            case R.id.iv_rightImage:
                intent = new Intent(context, Meassage.class);
                startActivity(intent);
                break;
        }
    }

    private void innitAdv() {
        final int size = mADParseArray.size();

        vp_shuffling.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                refreshPoint(position % size);
                if (mHandler.hasMessages(HOME_AD_RESULT)) {
                    mHandler.removeMessages(HOME_AD_RESULT);
                }
                mHandler.sendEmptyMessageDelayed(HOME_AD_RESULT, 3000);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (ViewPager.SCROLL_STATE_DRAGGING == arg0
                        && mHandler.hasMessages(HOME_AD_RESULT)) {
                    mHandler.removeMessages(HOME_AD_RESULT);
                }
            }
        });
        adapter = new BannerAdapter(context, mADParseArray);
        vp_shuffling.setAdapter(adapter);

        initPointsLayout(size);
        vp_shuffling.setCurrentItem(size * 1000, false);
        // 自动轮播线程
        mHandler.sendEmptyMessageDelayed(HOME_AD_RESULT, 3000);


    }


    private void initPointsLayout(int size) {
        if (dotLayout.getChildCount() > 0) {
            dotLayout.removeAllViews();
        }
        for (int i = 0; i < size; i++) {
            ImageView image = null;
            if (context != null) {
                image = new ImageView(context);
            }
            float denstity = getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) (6 * denstity), (int) (6 * denstity));
            params.leftMargin = (int) (2 * denstity);
            params.rightMargin = (int) (2 * denstity);
            image.setLayoutParams(params);
            if (i == 0) {
                image.setBackgroundResource(R.mipmap.dot_enable);
            } else {
                image.setBackgroundResource(R.mipmap.dot_normal);
            }

            dotLayout.addView(image);
        }
    }

    private void refreshPoint(int position) {
        if (dotLayout != null) {
            for (int i = 0; i < dotLayout.getChildCount(); i++) {
                if (i == position) {
                    dotLayout.getChildAt(i).setBackgroundResource(
                            R.mipmap.dot_enable);
                } else {
                    dotLayout.getChildAt(i).setBackgroundResource(
                            R.mipmap.dot_normal);
                }
            }
        }
    }


    /**
     * 获取数据
     */
    private void getData() {
        if (!CommonUtil.checkNetChange(context)) {
            ToastUtil.showShort(context, "请检查网络！");
            return;
        }
        data.clear();
        dialog();
        String[] keys = {"APPUSER_ID"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.MANAGE_ID)};
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_category/listAll.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("fragmentOne", result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        data.addAll(JSON.parseArray(json.getString("data"), DaiKuanSortEnity.class));
                        mADParseArray = JSON.parseArray(json.getString("data2"), AdvEntity.class);
                    } else
                        ToastUtil.showShort(context, json.getString("desc"));
                    daiKuanSortAdapter.notifyDataSetChanged();
                    if (mADParseArray.size() > 0) {
                        innitAdv();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "ex=" + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismiss();
//                假如没有 获取到数据 或者 获取失败  则不显示报单按钮
                if (data.size() <= 0) {
                    bt_baoDan.setVisibility(View.GONE);
                } else {
                    bt_baoDan.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
}
