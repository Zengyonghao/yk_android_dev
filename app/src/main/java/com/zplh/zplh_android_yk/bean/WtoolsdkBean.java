package com.zplh.zplh_android_yk.bean;

/**
 * Created by lichun on 2017/6/28.
 * Description:
 */

public class WtoolsdkBean {

    /**
     * errmsg : 连接微控xposed模块失败
     * result : -1001
     */

    private String errmsg;
    private int result;

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "WtoolsdkBean{" +
                "errmsg='" + errmsg + '\'' +
                ", result=" + result +
                '}';
    }
}
