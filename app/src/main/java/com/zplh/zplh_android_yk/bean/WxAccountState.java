package com.zplh.zplh_android_yk.bean;

/**
 * Created by lichun on 2018/4/9.
 * Description:
 */

public class WxAccountState {
    private  String  account;
    private  int   task_id;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }
    public WxAccountState( String  account,   int   task_id) {
        this.account = account;
        this.task_id = task_id;
    }
}
