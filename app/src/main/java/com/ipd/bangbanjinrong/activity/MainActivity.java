package com.ipd.bangbanjinrong.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.AppVersionEntity;
import com.ipd.bangbanjinrong.fragment.FragmentFour;
import com.ipd.bangbanjinrong.fragment.FragmentOne;
import com.ipd.bangbanjinrong.fragment.FragmentThree;
import com.ipd.bangbanjinrong.fragment.FragmentTwo;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.global.MyApplaction;
import com.ipd.bangbanjinrong.utils.AppVersionUpdateUtil;
import com.ipd.bangbanjinrong.utils.CommonUtil;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.LogUtils;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Date;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.ipd.bangbanjinrong.utils.SharedPreferencesUtils.POSTION;
import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/4
 * @Email 448739075@qq.com
 * 主页
 */

public class MainActivity extends BaseActivity {

    private int pos = 0;
    private long preTime = 0;
    private static final long TWO_SECOND = 2 * 1000;
    @ViewInject(R.id.radioGroup)
    private RadioGroup radioGroup;
    @ViewInject(R.id.radioButton1)
    private RadioButton radioButton1;
    @ViewInject(R.id.radioButton2)
    private RadioButton radioButton2;
    @ViewInject(R.id.radioButton3)
    private RadioButton radioButton3;
    @ViewInject(R.id.radioButton4)
    private RadioButton radioButton4;

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;
    private FragmentFour fragmentFour;

    private FragmentManager fragmentManager;

    private AppBean appBean;

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void innitData() {
        CommonUtil.checkPersiomion(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        setAlisa();

//检测更新
        PgyUpdateManager.register(this, "com.ipd.bangbanjinrong.fileprovider", new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {

            }

            @Override
            public void onUpdateAvailable(String s) {
                appBean=getAppBeanFromString(s);
                if(appBean!=null){
                    LogUtils.print("dowUrl",appBean.getDownloadURL());
                    getVersionCode();
                }

            }
        });
        /*if (!TextUtils.isEmpty(Constant.NEW_VERSION_APK)) {
         //当下载链接不为空的时候  检查版本  确定是否更新
            getVersionCode();
        }*/
    }

    @Override
    public void setListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                hideAllFragments();
                switch (checkedId) {
                    case R.id.radioButton1:
                        fragmentManager.beginTransaction().show(fragmentOne).commit();
                        break;
                    case R.id.radioButton2:
                        fragmentManager.beginTransaction().show(fragmentTwo).commit();
                        break;
                    case R.id.radioButton3:
                        fragmentManager.beginTransaction().show(fragmentThree).commit();
                        break;
                    case R.id.radioButton4:
                        fragmentManager.beginTransaction().show(fragmentFour).commit();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        pos = SharedPreferencesUtils.getIntSharedPreferences(context, POSTION);
        if (pos != 0)
            setSelet(pos);
    }

    private void hideAllFragments() {
        if (fragmentOne != null)
            fragmentManager.beginTransaction().hide(fragmentOne).commit();

        if (fragmentTwo != null)
            fragmentManager.beginTransaction().hide(fragmentTwo).commit();

        if (fragmentThree != null)
            fragmentManager.beginTransaction().hide(fragmentThree).commit();

        if (fragmentFour != null)
            fragmentManager.beginTransaction().hide(fragmentFour).commit();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 截获后退键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = new Date().getTime();

            // 如果时间间隔大于2秒, 不处理
            if ((currentTime - preTime) > TWO_SECOND) {
                // 显示消息
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT)
                        .show();

                // 更新时间
                preTime = currentTime;

                // 截获事件,不再处理
                return true;
            } else {
                for (Activity activity : MyApplaction.activities) {
                    activity.finish();
                }
                System.exit(0);
            }
        }

        return false;
    }


    /**
     * @param pos 设置选中
     */
    private void setSelet(int pos) {
        switch (pos) {
            case 0:
                radioButton1.setChecked(true);
                break;
            case 1:
                radioButton2.setChecked(true);
                break;
            case 2:
                radioButton3.setChecked(true);
                break;
            case 3:
                radioButton4.setChecked(true);
                break;
        }
        SharedPreferencesUtils.putIntSharedPreferences(context, POSTION, 0);

    }


    private void setAlisa() {
        String user_id = SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID);
        String alisa = "bk_" + user_id;
        LogUtils.print("alisa", alisa);
        //设置过了不再设置
        if (SharedPreferencesUtils.getbooleanSharedPreferences(context, Constant.USER_ALISA)) {
            return;
        }

        JPushInterface.setAlias(context, alisa, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    LogUtils.print("别名", "设置成功！");
                    SharedPreferencesUtils.putbooleanSharedPreferences(context, Constant.USER_ALISA, true);
                }
            }
        });
    }





    //      这样子仍是对之前保存的fragment操作，成功解决了重叠的问题。
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            fragmentOne = (FragmentOne) fragmentManager.findFragmentByTag("fragmentOne");
            fragmentTwo = (FragmentTwo) fragmentManager.findFragmentByTag("fragmentTwo");
            fragmentThree = (FragmentThree) fragmentManager.findFragmentByTag("fragmentThree");
            fragmentFour = (FragmentFour) fragmentManager.findFragmentByTag("fragmentFour");

        } else {
            if (fragmentOne == null)
                fragmentOne = new FragmentOne();
            if (fragmentTwo == null)
                fragmentTwo = new FragmentTwo();
            if (fragmentThree == null)
                fragmentThree = new FragmentThree();
            if (fragmentFour == null)
                fragmentFour = new FragmentFour();

            fragmentManager.beginTransaction().add(R.id.ll_container, fragmentOne, "fragmentOne").commit();
            fragmentManager.beginTransaction().add(R.id.ll_container, fragmentTwo, "fragmentTwo").commit();
            fragmentManager.beginTransaction().add(R.id.ll_container, fragmentThree, "fragmentThree").commit();
            fragmentManager.beginTransaction().add(R.id.ll_container, fragmentFour, "fragmentFour").commit();

            hideAllFragments();
            fragmentManager.beginTransaction().show(fragmentOne).commit();
        }

    }

    private AppVersionEntity appVersionEntity;

    /**
     * 检查是否是最新 版本
     */
    private void getVersionCode() {

        String[] keys = {"VERSION_NO"};
        String[] vaules = {String.valueOf(AppVersionUpdateUtil.getServerAppVersion(MainActivity.this))};
        String json = JsonUtil.getJsonString(keys, vaules);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_version/get.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("verion", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        appVersionEntity = JSON.parseObject(jsonObject.getString("data"), AppVersionEntity.class);
                        if (appVersionEntity.NEWS.equals("0")) {
                            //不是最新版本
                            if (appVersionEntity.MODIFY.equals("0")) {
                                //不强制更新 弹出框选择
                                show();
                            } else {
                                //强制更新  直接下载
//                                dowLoad();
                                startDownloadTask(
                                        MainActivity.this,
                                        appBean.getDownloadURL());
                            }
                        }
                    } else if (jsonObject.getString("response").equals("10003")) {
                        //找不到对比版本号  强制更新
//                        dowLoad();
                        startDownloadTask(
                                MainActivity.this,
                                appBean.getDownloadURL());
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


    public void show() {
        final Dialog dialog = new Dialog(context, R.style.custom_dialog);
        final LayoutInflater inflater = LayoutInflater.from(context);
        View viewcall = inflater.inflate(R.layout.um_dialog, null, true);

        TextView tv_content = (TextView) viewcall.findViewById(R.id.tv_content);
        Button bt_sure = (Button) viewcall.findViewById(R.id.bt_sure);
        Button bt_cancell = (Button) viewcall.findViewById(R.id.bt_cancell);

        tv_content.setText("发现新的版本，是否更新？");

//            更新
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                dowLoad();
                startDownloadTask(
                        MainActivity.this,
                        appBean.getDownloadURL());
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
     * 下载新版本apk
     */
    private void dowLoad() {
        final String savePaht = Constant.IMAGE_CACH_DIRECTORY + SystemClock.currentThreadTimeMillis() + ".apk";
        File file = new File(savePaht);
        file.getParentFile().mkdirs();
        RequestParams params = new RequestParams(appBean.getDownloadURL());
        params.setSaveFilePath(savePaht);
        params.setAutoResume(true);//断点下载
        params.setAutoRename(true);//根据文件头部命名
        x.http().post(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File file) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //判读版本是否在7.0以上
                if (Build.VERSION.SDK_INT >= 24) {
                    //provider authorities
                    Uri apkUri = FileProvider.getUriForFile(context, "com.ipd.bangbanjinrong.fileprovider", file);
                    //Granting Temporary Permissions to a URI
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                }

                startActivity(intent);

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
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax((int) total);
                progressBar.setProgress((int) current);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constant.REQUEST_CHECK_PERMISION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ToastUtil.showShort(context,"存储权限关闭，会影响手机部分功能！");
                }
                return;
            }
        }
    }
}
