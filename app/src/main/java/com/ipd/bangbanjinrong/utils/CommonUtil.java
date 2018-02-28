package com.ipd.bangbanjinrong.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ipd.bangbanjinrong.global.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ipd.bangbanjinrong.global.Constant.REQUEST_CHECK_PERMISION_CODE;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/4
 * @Email 448739075@qq.com
 * 常用工具类
 */

public class CommonUtil {


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动＿134?135?136?137?138?139?150?151?157(TD)?158?159?187?188
		 * 联?：130?131?132?152?155?156?185?186 电信＿133?153?180?189、（1349卫?）
		 * 总结起来就是第一位必定为1，第二位必定丿3房5房8，其他位置的可以丿0-9
		 */
        String telRegex = "[1][35847]\\d{9}";// "[1]"代表笿1位为数字1＿"[358]"代表第二位可以为3?5?8中的丿个，"\\d{9}"代表后面是可以是0?9的数字，朿9位?
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    // 验证身份证号码
    public static boolean idCardNumber(String number) {
        String rgx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";

        return isCorrect(rgx, number);
    }

    // 正则验证
    public static boolean isCorrect(String rgx, String res) {
        Pattern p = Pattern.compile(rgx);

        Matcher m = p.matcher(res);

        return m.matches();
    }

    /**
     * @param context
     * @return 检查网络链接
     */
    public static boolean checkNetChange(Context context) {
        boolean hasNet = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = manager.getAllNetworkInfo();
        int netType = -1;
        for (int i = 0, length = infos.length; i < length; i++) {
            NetworkInfo info = infos[i];
            if (info != null && info.isConnected()) {
                hasNet = true;
                netType = info.getType();
                if (netType == ConnectivityManager.TYPE_MOBILE && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE) {
                    netType = ConnectivityManager.TYPE_WIFI;
                }
                break;
            } else {
                hasNet = false;
            }
        }

        return hasNet;
    }


    /**
     * @param activity
     * @param persimion
     * @return 检查是否开启该权限
     */
    public static boolean checkPersiomion(Activity activity, String persimion) {
        //    检查所需要权限 只有api》+23才需要见查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取检查权限  结果
            int hasReadPermission = activity.checkSelfPermission(persimion);
            //判断 结果 是否开启了该权限
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                //未开启
                ActivityCompat.requestPermissions(activity, new String[]{persimion}, REQUEST_CHECK_PERMISION_CODE);
                return false;
            } else {
                //开启
                return true;
            }


        }
        return true;

    }


    /**
     * 返回屏幕尺寸
     *
     * @param
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(DisplayMetrics metrics, float dipValue) {
        final float scale = metrics.density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * @param context
     * @param url
     * @param imageView
     */
    public static void MyGlide(Context context, String url, ImageView imageView) {
        RequestOptions options = RequestOptions.centerCropTransform();
        Glide.with(context).load(Constant.IMAGE_API + url).apply(options).into(imageView);
    }


    /**
     * 判断是否是银行卡号
     *
     * @param cardNo
     * @return
     * @author WJ
     */
    public static boolean checkBankCard(String cardNo) {
        char bit = getBankCardCheckCode(cardNo
                .substring(0, cardNo.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardNo.charAt(cardNo.length() - 1) == bit;

    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N  
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
