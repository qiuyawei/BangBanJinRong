package com.ipd.bangbanjinrong.entity;

import java.io.Serializable;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/17
 * @Email 448739075@qq.com
 */

public class MessageEntity  implements Serializable {

    /* "INFO_ID": 1,
             "APPUSER_ID": 1,
             "TITLE": "下户成功",
             "CONTENT": "您预约下户已经成功",
             "TYPE": 0,
             "CREATETIME": "2017-08-21 14:22:06",
             "STATUS": 1*/

    public String INFO_ID;
    public String APPUSER_ID;
    public String TITLE;
    public String CONTENT;
    public String TYPE;
    public String CREATETIME;
    /*1:未读 2:已读*/
    public String STATUS;


}
