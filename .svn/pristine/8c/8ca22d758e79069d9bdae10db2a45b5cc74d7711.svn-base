package com.zplh.zplh_android_yk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zplh.zplh_android_yk.utils.SPUtils;

import java.io.Serializable;

/**
 * Created by zhangshuai on 2018/4/16.
 * Description:
 */

public class MyVoiceAndVideoService extends Service {
    @Override
    public void onCreate() {
        Log.i("zhangshuai","onCreate - Thread ID = " + Thread.currentThread().getId());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("zhangshuai", "onStartCommand - startId = " + startId + ", Thread ID = " + Thread.currentThread().getId());
        SPUtils.putString(this,"StartVoiceAndVideoService",1+"");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("zhangshuai", "onBind - Thread ID = " + Thread.currentThread().getId());
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("zhangshuai", "onDestroy - Thread ID = " + Thread.currentThread().getId());
        super.onDestroy();
    }
}