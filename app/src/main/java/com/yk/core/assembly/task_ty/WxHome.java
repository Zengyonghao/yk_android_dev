package com.yk.core.assembly.task_ty;

import android.content.Context;

import com.yk.core.assembly.AbsExecutionProcess;
import com.yk.core.utils.Tools;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.bean.MessageListBean;

/**
 * Author：liaogulong
 * Time: 2018/5/5/005   13:53
 * Description：
 */
public class WxHome extends AbsExecutionProcess {


    public WxHome(Context context, MessageListBean.ContentBean.DataBean dataBean) {
        super(context, dataBean, "微信主页");
    }

    @Override
    public int ExecutionParsing() {
        int backNum = 0;
        while (backNum < 10) {
            backNum++;
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我") && xmlData.contains("微信")) {
                //判断是否在微信主界面
                return EX_Next;
            }
            wxUtils.adb("input keyevent 4");
            setTaskLog("不在微信主页，点击返回");
        }
        return EX_BACK;
    }

    @Override
    public void ExecutionBack() {
        _exAlignMent.get(0).Execution();
    }
}
