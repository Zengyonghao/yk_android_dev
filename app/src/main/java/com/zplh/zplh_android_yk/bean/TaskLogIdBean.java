package com.zplh.zplh_android_yk.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lichun on 2017/8/21.
 * Description:
 */

public class TaskLogIdBean extends DataSupport {

    private String logIdTask;

    public String getLogIdTask() {
        return logIdTask;
    }

    public void setLogIdTask(String logIdTask) {
        this.logIdTask = logIdTask;
    }
}
