package com.yk2_0.utils;

import android.support.annotation.Nullable;

/**
 * Created by yong hao zeng on 2018/4/14/014.
 */
public class TaskErrorCreater {
    public static Exception creat(String msg, @Nullable Exception e){
        if (e!=null) {
            return e;
        } else
            return new Exception(msg);
    }
}
