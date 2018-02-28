package com.ipd.bangbanjinrong.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.global.MyApplaction;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.ipd.bangbanjinrong.widget.HKDialogLoading;

import org.xutils.x;

import static com.ipd.bangbanjinrong.utils.CommonUtil.checkNetChange;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/4
 * @Email 448739075@qq.com
 * 基础Activity 所有新建的Activity 继承此类
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    public Context context;
    public Intent intent = null;
    public HKDialogLoading hkDialogLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = BaseActivity.this;
        hkDialogLoading = new HKDialogLoading(context, R.style.custom_dialog);
        setContentView(setLayout());
        setLayout();
        x.view().inject(this);
        innitData();
        setListeners();


        //设置屏幕一直为竖屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        MyApplaction.addActivity(BaseActivity.this);
//        AndroidBug5497Workaround.assistActivity(this);

//        仿照IOS标题栏
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.base_color), true);
    }


    /**
     * @return 初始化布局
     */
    public abstract int setLayout();

    /**
     * @return 初始化其它东西
     */
    public abstract void innitData();

    /**
     * 设置监听
     */
    public abstract void setListeners();

    /**
     * 设置返回监听
     */
    public void setBack() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * @param topTitle 设置标题
     */
    public void setTopTitle(String topTitle) {
        TextView title = (TextView) findViewById(R.id.tv_topBarTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(topTitle);
    }

    /**
     * @param rightText 设置右边文字
     */
    public void setRightText(String rightText, View.OnClickListener onClickListener) {
        TextView right = (TextView) findViewById(R.id.tv_rightText);
        right.setVisibility(View.VISIBLE);
        right.setText(rightText);
        right.setOnClickListener(onClickListener);
    }

    /**
     * @param rightImage 设置右边图片
     */
    public void setRightText(int rightImage, View.OnClickListener onClickListener) {
        ImageView ivRight = (ImageView) findViewById(R.id.iv_rightImage);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(rightImage);
        ivRight.setOnClickListener(onClickListener);
    }

    public static void noNet(Context context) {
        if (!checkNetChange(context))
            ToastUtil.showShort(context, "请检查网络！");
    }


    public void dismiss() {
        if (hkDialogLoading.isShowing()) {
            hkDialogLoading.dismiss();
        }
    }

    public void dialog() {
        hkDialogLoading.show();// 显示加载中对话框
    }


  /*  private void setIosTitlBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            android 4.3 以上
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            View statusBarView = new View(window.getContext());
            int statusBarHeight = getStatusBarHeight(window.getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(getResources().getColor(R.color.base_color));
            decorViewGroup.addView(statusBarView);


            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                mChildView.setFitsSystemWindows(true);

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            android 5.0以
                Window windows = getWindow();
                windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                windows.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                windows.setStatusBarColor(getResources().getColor(R.color.base_color));
            }
        }
    }


    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }*/

}