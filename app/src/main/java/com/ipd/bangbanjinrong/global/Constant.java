package com.ipd.bangbanjinrong.global;

import android.os.Environment;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/4
 * @Email 448739075@qq.com
 * 全局常量
 */

public class Constant {
    //*是否加密  0不加密  1加密*/
    public static final int IF_JIA_MI=0;
    /*服务器跟接口*/
    public static final String SEVER_API="http://a.shcaoqi.com/Bank/";
//    public static final String SEVER_API="http://121.199.8.244:8280/Bank/";

    /*服务器图片接口*/
    public static final String IMAGE_API="http://a.shcaoqi.com/";
//    public static final String IMAGE_API="http://121.199.8.244:8280/";

    /*下载  新版本 apk */
    public static final String NEW_VERSION_APK="";
    /*分享 */
    public static final String WX_APPID="wx32321d6eb036b46a";
    public static final String WX_APPSECRET="96996dc91821e1c633b18b2cb4518fa5";
    public static final String QQ_APPID="1106397838";
    public static final String QQ_APPSECRET="VFaOstLMv385t6ev";
    /*第三方登录*/
    public static final String MOB_APPKEY="206c2942ba8d8";
    public static final String MOB_APPSECRET="8bc74d56e7c198ca3e99ac96e2ab3c5b";

    /*用户ID*/
    public static final String USER_ID="USER_ID";
    /*用户别名*/
    public static final String USER_ALISA="USER_ALISA";
    /*机构ID*/
    public static final String JI_GOU_ID="JI_GOU_ID";
    /*经理ID*/
    public static final String MANAGE_ID="MANAGE_ID";
    /*二维码*/
    public static final String EWM_KEY="EWM_KEY";
    /*CATEGORY 0:散客  1:机构 2:客户经理*/
    public static final String CATEGORY="CATEGORY";

    /*缓存 目录*/
    public static final String IMAGE_CACH_DIRECTORY= Environment.getExternalStorageDirectory().getAbsolutePath()+"/帮办金融/";

    /*所有请求码*/

    public static final int  REQUEST_CHECK_PERMISION_CODE=0;
    public static final int  PHOTOZOOM=1;
    public static final int  PHOTOTAKE=2;
    public static final int  IMAGE_COMPLETE=3;
    public static final int  TI_XIAN_REQUEST_CODE=4;
    public static final int  TI_XIAN_RESULT_CODE=5;

}
