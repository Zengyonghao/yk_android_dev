package com.zplh.zplh_android_yk.bean;

/**
 * Created by lichun on 2017/7/19.
 * Description:
 */

public class WxFlockMessageBean {


    /**
     * data : 你好，七月十九下午两点十二
     * type : txt
     */

    private String data;
    private String type;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WxFlockMessageBean{" +
                "data='" + data + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
