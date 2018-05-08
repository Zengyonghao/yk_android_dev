package com.yk.core.assembly;

import android.content.Context;
import android.util.Log;


import com.yk.core.Constant.TaskConstant;
import com.yk.core.assembly.task_ty.AccountIsOk;
import com.yk.core.assembly.task_ty.OpenWx;
import com.yk.core.assembly.task_ty.SwitchAccount;
import com.yk.core.assembly.task_ty.WxHome;
import com.yk.core.assembly.task_xgbz.SuperModifyRemark;
import com.yk.core.interfaces.IExecutionProcess;
import com.zplh.zplh_android_yk.bean.MessageBean;
import com.zplh.zplh_android_yk.bean.MessageListBean;

import java.util.ArrayList;

/**
 * Author:liaoguilong
 * Date 2018-05-03
 * FIXME
 * Todo 组装
 */
public class AssExecutionProcess {

    private static final String TAG = "AssExecutionProcess";

    /**
     * 分配
     * @param context
     * @param dataBean
     */
    public static void InitAssembyProcess(Context context, MessageListBean.ContentBean.DataBean dataBean) {
        try {
            switch (dataBean.getTask_id()) {
                case TaskConstant.Task_WX_SUPER_REMARK:
                    AssExecutionProcess.modifyRemarkAssExecution(context, dataBean);
                    break;
                //等等。。。。
            }
        } catch (Exception e) {
            Log.e(TAG, "组装错误:" + e.getMessage());
        }
    }


    /**
     * 组装任务链
     * （修改备注）
     */
    public static void modifyRemarkAssExecution(Context context, MessageListBean.ContentBean.DataBean dataBean) {
        ArrayList<IExecutionProcess> assembys = new ArrayList<>();
        assembys.add(new OpenWx(context, dataBean)); // 第一步
        assembys.add(new WxHome(context, dataBean)); // 第二步
        assembys.add(new SwitchAccount(context, dataBean));// 第三步
        assembys.add(new WxHome(context, dataBean)); // 第四步
        assembys.add(new AccountIsOk(context, dataBean)); // 第五步
        assembys.add(new WxHome(context, dataBean)); // 第六步
        assembys.add(new SuperModifyRemark(context, dataBean)); // 第六步
        for (int i = 0; i < assembys.size() - 1; i++) {
            ((AbsExecutionProcess) assembys.get(i))._Next = assembys.get(i + 1);
            ((AbsExecutionProcess) assembys.get(i))._exAlignMent = assembys;
        }
        //执行
        assembys.get(0).Execution();
    }


}
