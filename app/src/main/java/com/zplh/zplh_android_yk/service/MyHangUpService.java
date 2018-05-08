package com.zplh.zplh_android_yk.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.bean.wxReplyMessageBean;
import com.zplh.zplh_android_yk.bean.wxUidMessageBean;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.eventbus.EventCenter;
import com.zplh.zplh_android_yk.httpcallback.GsonUtil;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MyHangUpService extends AccessibilityService {
    public static final int HANGUP = 10086;
    public static final int HANGUP_NEW = 1008611;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
    String   TaskId   =    SPUtils.getString(this, "taskId", "0");
    String   StartVoiceAndVideoService  =  SPUtils.getString(this,"StartVoiceAndVideoService","");
    Log.d("zhangshuai","4399");
    if ((TaskId.equals("497")||TaskId.equals("498"))&&StartVoiceAndVideoService.equals("1")){
        Log.d("zhangshuai","4398");
        AccessibilityNodeInfo rootNode = accessibilityEvent.getSource();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.d("accessibilityevent", eventType + "");
                if (rootNode != null) {
                    List<AccessibilityNodeInfo> l = rootNode.findAccessibilityNodeInfosByText("接听");
                    if (l.size() != 0 ) {  //接听和挂断 都存在
//                        l.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        EventBus.getDefault().post(new EventCenter<Boolean>(HANGUP, false));

                    }
                }
                break;
        }
    }

    }


    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
