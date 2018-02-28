package com.ipd.bangbanjinrong.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ipd.bangbanjinrong.global.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/18
 * @Email 448739075@qq.com
 */

public class JsonUtil {

    public static String getJsonString(String[] keys, Object[] valus) {
        JSONObject jsonObject = new JSONObject();
        SortedMap<String, Object> map = new TreeMap<String, Object>();
        map.put("createTime", myFormat());
        if(Constant.IF_JIA_MI==0){
            map.put("data", get(keys, valus));
            map.put("sign", MD5.getMD5Str(map.get("createTime").toString() + jsonObject.toJSONString(get(keys, valus))));
        }else {
//            String mm=jsonObject.toJSONString(get(keys, valus));
//            map.put("data", a(mm));
//            map.put("sign", MD5.getMD5Str(map.get("createTime").toString() + mm));
        }

        String myJson= JSON.toJSONString(map);
        return myJson;
    }


    /**
     * @return
     * 返回当前系统时间
     * 格式 yy-MM-dd hh:mm:ss
     */
    private static String myFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private static Map<String, Object> get(String[] params, Object[] arrays){
        SortedMap<String, Object> map = new TreeMap<String, Object>();
        if (null!=params&&params.length>0) {
            for (int i = 0; i < params.length; i++) {
                map.put(params[i], arrays[i]);
            }
        }
        return map;
    }


}
