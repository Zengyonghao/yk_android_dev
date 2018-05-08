package com.zplh.zplh_android_yk.bean;

/**
 * Created by Administrator on 2017/8/8.
 */

import java.util.List;

/**
 * 对wx任务列表 将取消的任务过滤
 */
public class FiltrationBean {
    private String ret;
    private String msg;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FiltrationBean{" +
                "ret='" + ret + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
