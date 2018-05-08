package com.yk.core.assembly;


import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.yk.core.interfaces.IExecutionProcess;
import com.yk.core.utils.Tools;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.utils.ShowToast;

import java.util.ArrayList;

/**
 * Author:liaoguilong
 * Date 2018年5月3日 19:17:13
 * FIXME
 * Todo  职责组装的抽象方法
 */
public abstract class AbsExecutionProcess implements IExecutionProcess {

    private static final String TAG = "AbsExecutionProcess";
    protected static final int EX_Next = 1;
    protected static final int EX_FAIL = 0;
    protected static final int EX_BACK = -99;


    protected Context context;
    protected MessageListBean.ContentBean.DataBean dataBean;
    protected IExecutionProcess _Next; //下一步任务
    protected String _taskMessage;
    protected WxUtils wxUtils = new WxUtils();
    protected String xmlData;
    protected ArrayList<IExecutionProcess> _exAlignMent;

    public AbsExecutionProcess(Context context, MessageListBean.ContentBean.DataBean dataBean, String taskMessage) {
        this.context = context;
        this.dataBean = dataBean;
        this._taskMessage = taskMessage;
    }

    @Override
    public void Execution() {
        try {
            Log.d(TAG, "开始执行：" + _taskMessage);
            int ex_result = ExecutionParsing();
            if (ex_result == EX_Next) { // 继续下一个处理
                setTaskLog("执行完毕");
                Tools.ThreadTools.sleep(1); //执行完，等待1秒,准备执行下一步
                if (Tools.ObjectTools.isNotNull(_Next)) {
                    _Next.Execution();
                }
            } else if (ex_result == EX_FAIL) {
                setTaskLog("执行失败");
            } else if (ex_result == EX_BACK) {
                setTaskLog("执行回退");
                ExecutionBack();
            }
        } catch (Exception e) {
            Log.e(TAG, "Execution()-->" + e.getMessage());
        }
    }

    public void setTaskLog(String log) {
        Log.i(TAG, _taskMessage + "->：" + log);
    }

    /**
     * 执行子类操作
     *
     * @return
     */
    public abstract int ExecutionParsing();


    /**
     * 执行回退操作
     */
    public abstract void ExecutionBack();

}
