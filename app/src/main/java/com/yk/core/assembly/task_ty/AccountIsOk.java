package com.yk.core.assembly.task_ty;

import android.content.Context;
import android.os.Build;

import com.yk.core.assembly.AbsExecutionProcess;
import com.yk.core.interfaces.IExecutionProcess;
import com.yk.core.utils.Tools;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.conf.ModelConstans;
import com.zplh.zplh_android_yk.utils.SPUtils;

import java.util.ArrayList;

/**
 * Author：liaogulong
 * Time: 2018/5/5/005   15:32
 * Description：
 */
public class AccountIsOk extends AbsExecutionProcess {


    public AccountIsOk(Context context, MessageListBean.ContentBean.DataBean dataBean) {
        super(context, dataBean, "检查帐号是否被封");
    }

    @Override
    public int ExecutionParsing() {
        Tools.ThreadTools.sleep(2);
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("紧急冻结") && xmlData.contains("找回密码") && xmlData.contains("微信安全中心")) {
            setTaskLog("帐号已被封");
            String currentLocation = SPUtils.getString(context, "WxAccountLocation", "0");
            wxUtils.adb("input keyevent 4");//返回
            switch (Build.MODEL) {
                case ModelConstans.coolpad_8737:
                    if (currentLocation.equals("1")) {
                        wxUtils.adbWxClick(454, 690);
                    } else {
                        wxUtils.adbWxClick(201, 700);
                    }
                    break;
                case ModelConstans.tvyk:
                    if (currentLocation.equals("1")) {
                        wxUtils.adbClick(288, 457, 384, 553);
                    } else {
                        wxUtils.adbClick(96, 457, 192, 553);
                    }
                    break;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return EX_FAIL;
        }
        return EX_Next;
    }

    @Override
    public void ExecutionBack() {

    }
}
