package com.zplh.zplh_android_yk.bean;

import java.io.Serializable;

/**
 * Created by 张帅 on 2018/3/28.
 * Description: 微信请求手机帐号
 */

public class WxPhoneNumeAskBean implements Serializable {
    private String uid;               //设备uid
    private String account;          // 微信号
    private String location;        // 号码位置
    private String count;           // 申请数

    private  String  flag ; //随机数

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public WxPhoneNumeAskBean(String uid, String account, String location, String count,String  flag) {
        this.uid = uid;
        this.account = account;
        this.location = location;
        this.count = count;
        this.flag =flag;
    }
}
