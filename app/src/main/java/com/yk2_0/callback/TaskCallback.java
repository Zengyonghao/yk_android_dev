package com.yk2_0.callback;

import android.content.Context;

import com.yk2_0.bean.TaskErrorBean;
import com.yk2_0.imp.BaseTask;

/**
 * 这是
 * Created by yong hao zeng on 2018/4/12.
 */

public interface TaskCallback  {
    void onTaskStart(BaseTask iTask);//一个任务正在开始
    void onTaskSuccess(BaseTask iTask);//一个任务成功完成
    void onTaskProgress(BaseTask iTask,String progress) throws Exception;//任务进度
    void onTaskError(BaseTask iTask, TaskErrorBean taskErrorBean) throws Exception;//任务错误
    Context getContext();
}
