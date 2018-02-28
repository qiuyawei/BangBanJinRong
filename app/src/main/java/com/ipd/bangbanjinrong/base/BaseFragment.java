package com.ipd.bangbanjinrong.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.widget.HKDialogLoading;

import org.xutils.x;

/**
 * Created by lenovo on 2017/6/14.
 * 基础Fragment 所有Fragment 继承该类
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    public Context context;
    public Intent intent=null;
    public View view;
    public HKDialogLoading hkDialogLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        intent=new Intent();
        hkDialogLoading=new HKDialogLoading(context, R.style.custom_dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=setView(inflater);
        //初始化控件 绑定
        x.view().inject(this,view);
        //设置监听
        setListeners();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        innitData(savedInstanceState);
    }

    /**
     * @param inflater
     * @return
     * 返回布局
     */
    public abstract View  setView(LayoutInflater inflater);


    /**
     * 初始化数据
     */
    public abstract void innitData(Bundle savedInstanceState);


    /**
     * 设置点击  监听
     */
    public abstract void setListeners();

    public void dismiss() {
        if (hkDialogLoading.isShowing()) {
            hkDialogLoading.dismiss();
        }
    }

    public void dialog() {
        hkDialogLoading.show();// 显示加载中对话框
    }
}
