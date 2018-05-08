package com.zplh.zplh_android_yk.utils;


import android.app.Application;
import android.util.Log;

import com.yk2_0.utils.AdbUtils;
import com.zplh.zplh_android_yk.base.BaseActivity;
import com.zplh.zplh_android_yk.base.BaseApplication;

/**
 * 检查各种突如其来的view 在onprogress回掉 可以避免界面的弹窗
 * Created by yong hao zeng on 2018/4/24/024.
 */
public class ViewCheckUtils {
    public static void check() throws Exception {
        Log.e("WG", "check: 权限框进来了");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //检查微信的弹窗
                while (AdbUtils.dumpXml2String().contains("安全警告")) {
                    Log.e("WG", "check: 警告弹框出来了");
                    NodeUtils.clickNode("记住我的选择。", "com.mediatek.security:id/checkbox");

                    NodeUtils.clickNode("允许", "android:id/button1");

                }
                SPUtils.putBoolean(BaseApplication.getContext(), "flag", true);
            }
        }).start();
    }

}
