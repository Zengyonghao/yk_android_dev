package com.zplh.zplh_android_yk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zplh.zplh_android_yk.eventbus.EventCenter;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lichun on 2018/3/17.
 * Description:
 */

public class DeviceOutReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DeviceOutReceiver","收到检查广播");
        EventBus.getDefault().post(new EventCenter<Integer>(99,0));
    }
}