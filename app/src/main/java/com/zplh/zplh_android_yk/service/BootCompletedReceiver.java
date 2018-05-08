package com.zplh.zplh_android_yk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lichun on 2017/6/26.
 * Description://开机自启
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300000);
                    Intent intent2 = context.getPackageManager().getLaunchIntentForPackage("com.zplh.zplh_android_yk");
                    context.startActivity(intent2 );

//                    new WxUtils().adb("am start -a android.intent.action.MAIN -n com.zplh.zplh_android_yk/com.zplh.zplh_android_yk.ui.activity.BindingActivity");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
