package com.zplh.zplh_android_yk.bean;

/**
 * Created by Administrator on 2017/8/4.
 */

/**
 * 判断任务是否取消 200为任务取消 400 为任务没取消
 */
public class LogidBean {
    private String ret;
    private String msg;
    private String data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
