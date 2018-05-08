package com.zplh.zplh_android_yk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.bean.MessageBean;
import com.zplh.zplh_android_yk.presenter.JPushOperationPresenter;
import com.zplh.zplh_android_yk.ui.activity.MainActivity;
import com.zplh.zplh_android_yk.ui.view.OperationView;
import com.zplh.zplh_android_yk.utils.SPUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lichun on 2017/5/27.
 * Description:极光推送获取服务器消息
 */
public class MyJPushReceiver extends BroadcastReceiver implements OperationView {

    private static String TAG = "pushreceiver";
    private MessageBean messageBean;
    Gson gson = new Gson();
    WxUtils wxUtils = new WxUtils();
    Context context;
    private JPushOperationPresenter operationPresenter;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());
        operationPresenter = new JPushOperationPresenter(context, this);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

            System.out.println("收到了自定义消息@@消息内容是:" + content);
            System.out.println("收到了自定义消息@@消息extra是:" + extra);
//            ShowToast.show("收到任务", (Activity) context);
            //**************解析推送过来的json数据并存放到集合中 begin******************
            messageBean = gson.fromJson(extra, MessageBean.class);
            //**************解析推送过来的json数据并存放到集合中 end******************


            //{"data":{"imei":"87975431324687132417123,869848020772184","range":"执行范围:部分执行","task_type":"任务类型:自动拉群任务"}}
            //执行任务
//            System.out.println(messageBean.getContent()+"_____IMEI:"+wxUtils.getIMEI(context));

            if(!SPUtils.getBoolean(context, "imei", false)){//是否绑定过设备
                return;
            }

            if (SPUtils.getBoolean(context, "task", false)) {//任务是否正在执行
                return;
            }
            if (messageBean == null) {
                return;
            }
            MessageBean.ContentBean.DataBean dataBean = messageBean.getContent().getData();
            if (dataBean != null && dataBean.getTask_type() != null && dataBean.getTask_type().contains("自动拉群任务") && (dataBean.getRange() != null && (dataBean.getRange().contains("所有的手机全部执行") || (dataBean != null && dataBean.getTask_type() != null && dataBean.getTask_type().contains("自动拉群任务")&&(dataBean.getImei() != null && dataBean.getImei().equals(wxUtils.getIMEI(context))))))) {
                SPUtils.putBoolean(context, "task", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        operationPresenter.task(0);//修改备注
                        operationPresenter.task(1);//拉群
                        SPUtils.putBoolean(context, "task", false);
                    }
                }).start();

            }


        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, MainActivity.class); // 自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }


    }

    @Override
    public void alterName() {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showException(String msg) {

    }

    @Override
    public void showNetError(View.OnClickListener listener) {

    }

    @Override
    public void showErrorMsg(String msg) {

    }

}
