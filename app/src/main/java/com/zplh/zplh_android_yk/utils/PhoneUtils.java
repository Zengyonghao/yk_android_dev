package com.zplh.zplh_android_yk.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.wanj.x007_common.util.*;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.ui.activity.BindingActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.KEYGUARD_SERVICE;


/**
 * Created by Administrator on 2017/7/19.
 */

public class PhoneUtils {
    private WifiManager wifiManager;
    private AudioManager am;
    // 设置系统亮度
    // 程序退出之后亮度依旧有效
    private static Context context;
    private AudioManager audioManage;
    private ScreenObserver screenObserver;
    WxUtils wxUtils = new WxUtils();
    private String xmlData;
    private NodeXmlBean.NodeBean nodeBean;
    private List<Integer> listXY;
    private List<String> nodeList;
    private int Starting_hours;//定时开机 小时
    private int Starting_minute;//定时开机分钟
    private int  Shutdown_hours;//定时 关机小时
    private int Shutdown_minute;//定时 关机分钟

     public PhoneUtils(Context context){
        this.context=context;
         wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
         am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
         audioManage= (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);

     }
     public void getHomes() {
         Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
         mHomeIntent.addCategory(Intent.CATEGORY_HOME);
         mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                 | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
         context.startActivity(mHomeIntent);

//         Intent intent=new Intent(context,BindingActivity.class);
//         context.startActivity(intent);
     }
    /**
     * 检测wifi
     * @return
     */
    public  boolean isWifiConnected()
    {

        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }

        return false ;
    }
    /**
     * wifi设置
     */
    public void wifi(){
        if (isWifiConnected()){
            //连接了wifi 关闭
            wifiManager.setWifiEnabled(false);
            intiData();//关闭蓝牙
        }else {
            intiData();//关闭蓝牙
        }
    }

    /**
     * 关闭蓝牙
     */
    public void intiData() {
        /**
         * 获取蓝牙适配器
         */
        BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
        if (blueadapter==null){
        }
        if (blueadapter.isEnabled()){
            blueadapter.disable();//关闭蓝牙
            PhoneVoice();//声音调节到最小
        }else {
            PhoneVoice();//声音调节到最小
        }

    }

    /**
     * 调节声音
     */
    public void PhoneVoice(){
        audioManage.setStreamMute(AudioManager.STREAM_SYSTEM,true);
        audioManage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//        try {
//            setDateTime(2015,8,15,10,25);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        ShowToast.show("wifi蓝牙音量已经设置成功", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始打开手机开发者模块", (Activity) context);
        getoGuanyu();
        ShowToast.show("手机打开开发者模块任务完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置锁定屏幕任务", (Activity) context);
        getSuoPingNo();//设置不锁定屏幕
        ShowToast.show("设置锁定屏幕任务已完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始调节手机亮度任务", (Activity) context);
        gotoLuminance();//设置手机亮度
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,50);
//            }
//        }).start();
//
        ShowToast.show("调节手机亮度任务完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置关闭定位任务", (Activity) context);
        ClosedLocation();//关闭定位
        ShowToast.show("关闭手机定位任务完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始关闭支付宝通知任务", (Activity) context);
        OpendZFB();//关闭支付宝通知
        ShowToast.show("支付宝通知任务已经完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置禁止屏幕切换", (Activity) context);
        SettingRotate();//禁止手机屏幕横竖切换
        ShowToast.show("设置禁止手机屏幕切换任务完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置手机休眠时间", (Activity) context);
        gotoDormancy();
        ShowToast.show("设置手机休眠时间任务完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置手机时间", (Activity) context);
        setData();
        ShowToast.show("设置手机时间任务完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置手机锁屏任务", (Activity) context);
        gotoSetting();
        ShowToast.show("手机锁屏任务设置完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置微信通知栏任务", (Activity) context);
        OpendWeixin();
        ShowToast.show("微信通知栏任务完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置搜狗所有权限", (Activity) context);
        SettingAllJurisdiction_sougou();
        ShowToast.show("设置搜狗权限任务完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置支付宝所有权限", (Activity) context);
        SettingAllJurisdiction_apli();
        ShowToast.show("支付宝权限设置完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置微信所有权限", (Activity) context);
        SettingAllJurisdiction_weixin();
        ShowToast.show("微信所有权限设置完成", (Activity) context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始设置wx助手所有权限", (Activity) context);
        SettingAllJurisdiction_wx();
        ShowToast.show("wx助手所有权限设置完成", (Activity) context);
//        wxUtils.adb("input keyevent KEYCODE_HOME");//回到home
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ShowToast.show("开始设置读取手机联系人权限", (Activity) context);
//        gotoPermission();//设置读取联系人q权限



    }
    /**
     * 初始化设置安全页面
     */
    private void gotoAnquan(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        wxUtils.adbUpSlide(context);
        wxUtils.adbUpSlide(context);
    }
    /**
     * 设置
     */
    public void gotoSetting() {
            gotoAnquan();
            xmlData = wxUtils.getXmlData();//获取当前页面
            List<String> list = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < list.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
                if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                    if ("安全".equals(nodeBean.getText())) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击安全
                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y82, R.dimen.x320, R.dimen.y134);//屏幕锁定方式
                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y56, R.dimen.x320, R.dimen.y108);//当前屏幕锁定设置
                    }
                }
            }
    }

    /**
     * 手机关机
     */
    public void shutdown() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(
                    process.getOutputStream());
            out.writeBytes("reboot -p\n");
            out.writeBytes("exit\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // wakeup();
    }

    /**
     * 设备休眠
     */
    public void hibernate(){
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(
                    process.getOutputStream());
            out.writeBytes("echo mem > /sys/power/state \n");
            out.writeBytes("exit\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
         * 唤醒设备
         */
    public void wakeup(){
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(
                    process.getOutputStream());
            out.writeBytes("echo on > /sys/power/state \n");
            out.writeBytes("exit\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
        keyguardLock.disableKeyguard();
    }

    /**
     * 重启设备
     */
    public void restart() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(
                    process.getOutputStream());
            out.writeBytes("reboot \n");
            out.writeBytes("exit\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置关于手机的版本号
     */
    public void getoGuanyu() {
        gotoAnquan();
        wxUtils.adbUpSlide(context);
        wxUtils.adbUpSlide(context);
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("关于手机".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击安全
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();//获取当前页面
                    List<String> list_treew = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < list_treew.size(); b++) {
                        NodeXmlBean.NodeBean nodeBean_treew = wxUtils.getNodeXmlBean(list_treew.get(b)).getNode();
                        if ("android:id/title".equals(nodeBean_treew.getResourceid())) {
                            if ("版本号".equals(nodeBean_treew.getText())) {
                                listXY = wxUtils.getXY(nodeBean_treew.getBounds());//
                                for (int s=0;s<7;s++){
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击版本号
                                }
                                wxUtils.adb("input keyevent 4");

                            }
                        }
                    }
                }
            }

        }

    }
    /**
     * 设置手机的权限
     */
    public void gotoPermission(){
        gotoAnquan();
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("安全".equals(nodeBean.getText())) {

                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击安全
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    wxUtils.adbUpSlide(context);
                    xmlData=wxUtils.getXmlData();
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();

                        if ("android:id/title".equals(nodeBean_two.getResourceid())){
                            if ("\uFEFF应用权限".equals(nodeBean_two.getText())){
                                if (wxUtils.getNodeXmlBean(list_two.get(a+3)).getNode().isChecked()==false){
                                    listXY = wxUtils.getXY(wxUtils.getNodeXmlBean(list_two.get(a+3)).getNode().getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                                }
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击应用权限
                                wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                                wxUtils.adbUpSlide(context);
                                xmlData = wxUtils.getXmlData();//获取当前页面
                                List<String> list_treew = wxUtils.getNodeList(xmlData);
                                for (int b = 0; b < list_treew.size(); b++) {
                                    NodeXmlBean.NodeBean nodeBean_treew = wxUtils.getNodeXmlBean(list_treew.get(b)).getNode();
                                    if ("com.mediatek.security:id/app_name".equals(nodeBean_treew.getResourceid())){
                                        if ("读取联系人".equals(nodeBean_treew.getText())){
                                            listXY = wxUtils.getXY(nodeBean_treew.getBounds());//
                                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击读取联系人权限
                                            xmlData = wxUtils.getXmlData();//获取当前页面
                                            List<String> list_four = wxUtils.getNodeList(xmlData);
                                            for (int c=0;c<list_four.size();c++){
                                                NodeXmlBean.NodeBean nodeBean_four = wxUtils.getNodeXmlBean(list_four.get(c)).getNode();
                                                if ("com.mediatek.security:id/app_name".equals(nodeBean_four.getResourceid())){
                                                    if ("微信".equals(nodeBean_four.getText())){
                                                        listXY = wxUtils.getXY(nodeBean_four.getBounds());
                                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//设置微信
                                                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y174, R.dimen.x304, R.dimen.y208);//点击总是允许
                                                    }
                                                    if ("支付宝".equals(nodeBean_four.getText())){
                                                        listXY = wxUtils.getXY(nodeBean_four.getBounds());
                                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//设置微信
                                                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y174, R.dimen.x304, R.dimen.y208);//点击总是允许
                                                        wxUtils.adb("input keyevent 4");//返回
                                                    }
                                                }
                                            }
                                        }
                                        if ("写/删联系人".equals(nodeBean_treew.getText())){
                                            listXY = wxUtils.getXY(nodeBean_treew.getBounds());//
                                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击写/删联系人
                                            xmlData = wxUtils.getXmlData();//获取当前页面
                                            List<String> list_four = wxUtils.getNodeList(xmlData);
                                            for (int c=0;c<list_four.size();c++){
                                                NodeXmlBean.NodeBean nodeBean_four = wxUtils.getNodeXmlBean(list_four.get(c)).getNode();
                                                if ("com.mediatek.security:id/app_name".equals(nodeBean_four.getResourceid())){
                                                    if ("微信".equals(nodeBean_four.getText())){
                                                        listXY = wxUtils.getXY(nodeBean_four.getBounds());
                                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//设置微信
                                                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y174, R.dimen.x304, R.dimen.y208);//点击总是允许
                                                    }
                                                    if ("支付宝".equals(nodeBean_four.getText())){
                                                        listXY = wxUtils.getXY(nodeBean_four.getBounds());
                                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//设置微信
                                                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y174, R.dimen.x304, R.dimen.y208);//点击总是允许
                                                        //wxUtils.adb("input keyevent 4");//返回
                                                        ShowToast.show("设置手机联系人的增删改查权限任务完成", (Activity) context);
                                                        wxUtils.adb("input keyevent 4");//返回
                                                        wxUtils.adb("input keyevent 4");//返回
                                                        wxUtils.adb("input keyevent 4");//返回
                                                        wxUtils.adb("input keyevent 4");//返回

                                                    }
                                                }
                                            }
                                        }

                                    }

                                }
//                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
//                                context.startActivity(intent);
//                                wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部

                            }
                        }

                    }

                }
            }
        }

    }

    /**
     * 流量使用情况
     */
    public void gotoFlow(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("流量使用情况".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击流量使用情况
                    wxUtils.adbDimensClick(context, R.dimen.x160, R.dimen.y56, R.dimen.x320, R.dimen.y96);//点击卡二
                    xmlData = wxUtils.getXmlData();//获取当前页面
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        LogUtils.d("打印出来的日志:"+list_two.get(a));
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("android:id/title".equals(nodeBean_two.getResourceid())) {
                            if ("移动数据网络".equals(nodeBean_two.getText())){
                                if (wxUtils.getNodeXmlBean(list_two.get(a+2)).getNode().isChecked()==true){
                                }else {
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//开启移动数据网络
                                }

                            }

                        }
                    }
                }
            }
        }

    }

    /**
     * 在开发者选择中设置不锁定屏幕
     */
    public void getSuoPingNo(){
        gotoAnquan();
        wxUtils.adbUpSlide(context);
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();

            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("开发者选项".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击开发者选项
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    xmlData = wxUtils.getXmlData();//获取当前页面
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();

                        if ("com.android.settings:id/switch_widget".equals(nodeBean_two.getResourceid())){ //获取开启按钮
                            if (nodeBean_two.isChecked()==false){
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击开启
                                wxUtils.adbDimensClick(context, R.dimen.x247, R.dimen.y232, R.dimen.x307, R.dimen.y255);//确定
                               }
                            }
                        if ("android:id/title".equals(nodeBean_two.getResourceid())){
                            LogUtils.d("第一个");
                            if ("不锁定屏幕".equals(nodeBean_two.getText())){
                                LogUtils.d("第二个");
                                if (wxUtils.getNodeXmlBean(list_two.get(a+3)).getNode().isChecked()==false){
                                    LogUtils.d("第三个");
                                    listXY = wxUtils.getXY(wxUtils.getNodeXmlBean(list_two.get(a+3)).getNode().getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 开启时间设置
     */
    public void setData() {
        gotoAnquan();
        wxUtils.adbUpSlide(context);
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("日期和时间".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击开发者选项
                    xmlData = wxUtils.getXmlData();//获取当前页面
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("android:id/title".equals(nodeBean_two.getResourceid())){
                            if ("自动确定日期和时间".equals(nodeBean_two.getText())) {
                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y56, R.dimen.x320, R.dimen.y108);//选择自动确定时间和日期
                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y137, R.dimen.x304, R.dimen.y188);//使用网络提供时间
                            }
                            if ("自动确定时区".equals(nodeBean_two.getText())){
                                if (wxUtils.getNodeXmlBean(list_two.get(a+3)).getNode().isChecked()==false){
                                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y109, R.dimen.x320, R.dimen.y160);//开启自动确定时区
                                }
                        }

                            if ("使用 24 小时制".equals(nodeBean_two.getText())){//设置使用24小时制
                                if ( wxUtils.getNodeXmlBean(list_two.get(a+3)).getNode().isChecked()==false){
                                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y319, R.dimen.x320, R.dimen.y370);//选择使开启24小时制度
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * 设置手机的定时开机和关机
     * @param Starting_hours
     * @param Starting_minute
     * @param Shutdown_hours
     * @param Shutdown_minute
     */
   public void SettingTimegj(int Starting_hours,int Starting_minute,int  Shutdown_hours,int Shutdown_minute){
       gotoAnquan();
       wxUtils.adbUpSlide(context);
       xmlData = wxUtils.getXmlData();//获取当前页面
       List<String> list = wxUtils.getNodeList(xmlData);
       for (int i = 0; i < list.size(); i++) {
           NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
           if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
               if ("定时开关机".equals(nodeBean.getText())) {
                   listXY = wxUtils.getXY(nodeBean.getBounds());//
                   wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击开发者选项
                    xmlData = wxUtils.getXmlData();//获取当前页面
                   wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y56, R.dimen.x320, R.dimen.y110);//设置定时开机
                   wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y56, R.dimen.x320, R.dimen.y110);//设置定时开机
                   /**
                    * 判断定时的小时
                    */
                   getSettingTime(Starting_hours,Starting_minute);
                   wxUtils.adbDimensClick(context, R.dimen.x228, R.dimen.y310, R.dimen.x292, R.dimen.y343);//设置时间后的确定按钮
                   wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y109, R.dimen.x320, R.dimen.y160);//设置开机定时 重复
                   xmlData = wxUtils.getXmlData();//获取当前页面
                   List<String> list_two = wxUtils.getNodeList(xmlData);
                   for (int a = 0; a < list_two.size(); a++) {
                       NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                       if ("android:id/text1".equals(nodeBean_two.getResourceid())){//设置每天都选择上
                               if (wxUtils.getNodeXmlBean(list_two.get(a)).getNode().isChecked()==false){
                                   listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                   wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击开发者选项
                           }
                       }

                   }
                   wxUtils.adbDimensClick(context, R.dimen.x228, R.dimen.y331, R.dimen.x292, R.dimen.y365);//设置开机重复点击确定按钮
                   wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y20, R.dimen.x320, R.dimen.y53);//设置时间后的完成按钮

                   wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y157, R.dimen.x320, R.dimen.y164);////设置关机时间
                   wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y56, R.dimen.x320, R.dimen.y110);//设置定时关机
                   getSettingTime(Shutdown_hours,Shutdown_minute);
                   wxUtils.adbDimensClick(context, R.dimen.x228, R.dimen.y310, R.dimen.x292, R.dimen.y343);//设置时间后的确定按钮
                   wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y109, R.dimen.x320, R.dimen.y160);//设置开机定时 重复
                   xmlData = wxUtils.getXmlData();//获取当前页面
                   List<String> list_threw = wxUtils.getNodeList(xmlData);
                   for (int b=0; b < list_threw.size(); b++) {
                       NodeXmlBean.NodeBean nodeBean_three = wxUtils.getNodeXmlBean(list_threw.get(b)).getNode();
                       LogUtils.d("打印的界面布局"+list_threw.get(b));
                       if ("android:id/text1".equals(nodeBean_three.getResourceid())){//设置每天都选择上
                           if (wxUtils.getNodeXmlBean(list_threw.get(b)).getNode().isChecked()==false){
                               listXY = wxUtils.getXY(nodeBean_three.getBounds());//
                               wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击开发者选项
                           }
                       }

                   }
                   wxUtils.adbDimensClick(context, R.dimen.x228, R.dimen.y331, R.dimen.x292, R.dimen.y365);//设置开机重复点击确定按钮
                   wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y20, R.dimen.x320, R.dimen.y53);//设置时间后的完成按钮

               }
           }

       }


   }

    /**
     * 调节手机亮度到一半
     */
   public void gotoLuminance_Two(){
       Intent intent = new Intent(Settings.ACTION_SETTINGS);
       context.startActivity(intent);
       wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
       wxUtils.adbUpSlide(context);
       xmlData = wxUtils.getXmlData();//获取当前页面
       List<String> list = wxUtils.getNodeList(xmlData);
       for (int i = 0; i < list.size(); i++) {
           NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
           if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
               if ("显示".equals(nodeBean.getText())) {
                   listXY = wxUtils.getXY(nodeBean.getBounds());//
                   wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击开发者选项
                   xmlData = wxUtils.getXmlData();//获取当前页面
                   List<String> list_two = wxUtils.getNodeList(xmlData);
                   for (int a = 0; a < list_two.size(); a++) {
                       NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                           if ("亮度".equals(nodeBean_two.getText())){//点击亮度 设置最小亮度
                               listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                               wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击亮度
                               xmlData = wxUtils.getXmlData();//获取当前页面
                               wxUtils.adbDimensClick(context, R.dimen.x160, R.dimen.y28, R.dimen.x160, R.dimen.y70);//
                           }
                       if ("android:id/title".equals(nodeBean_two.getResourceid())){
                           if ("自动调节亮度".equals(nodeBean_two.getText())){
                               if (wxUtils.getNodeXmlBean(list_two.get(a+3)).getNode().isCheckable()==true){//如何开启了自动调节亮度则关闭
                                   listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                   wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击关闭自动调节亮度
                               }
                           }

                       }
                   }
               }
           }
       }
   }
    /**
     * 设置手机亮度
     */
    public void gotoLuminance(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        wxUtils.adbUpSlide(context);
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("显示".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击开发者选项
                    xmlData = wxUtils.getXmlData();//获取当前页面
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();

                        if ("android:id/title".equals(nodeBean_two.getResourceid())){
                            if ("自动调节亮度".equals(nodeBean_two.getText())){
                                   if (wxUtils.getNodeXmlBean(list_two.get(a+3)).getNode().isCheckable()==true){//如何开启了自动调节亮度则关闭
                                       listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                       wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击关闭自动调节亮度
                                   }
                            }
                            if ("亮度".equals(nodeBean_two.getText())){//点击亮度 设置最小亮度
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击亮度
                                xmlData = wxUtils.getXmlData();//获取当前页面
                                List<String> list_three = wxUtils.getNodeList(xmlData);
//                                for (int b=0;b<list_three.size();b++){
//                                    NodeXmlBean.NodeBean nodeBean_three = wxUtils.getNodeXmlBean(list_three.get(b)).getNode();
//                                    if ("com.android.systemui:id/slider".equals(nodeBean_three.getResourceid())){
//
//                                    }
//                                }
                                wxUtils.adbDimensClick(context, R.dimen.x32, R.dimen.y28, R.dimen.x52, R.dimen.y70);//
                            }

                        }
                    }
                }
            }
        }

    }

    /**
     * 关闭支付宝的通知
     */
    public void OpendZFB(){
        boolean TAG=true;
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        wxUtils.adbUpSlide(context);
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("提示音和通知".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//声音和提示
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y291, R.dimen.x320, R.dimen.y329);//通知
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y109, R.dimen.x320, R.dimen.y147);//应用通知
                    while (TAG){
                        xmlData = wxUtils.getXmlData();//获取当前页面
                        List<String> list_two = wxUtils.getNodeList(xmlData);
                        for (int a=0;a<list_two.size();a++){
                            NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                            if ("android:id/title".equals(nodeBean_two.getResourceid())){
                                if ("支付宝".equals(nodeBean_two.getText())){
                                    listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击支付宝
                                    xmlData = wxUtils.getXmlData();//获取当前页面
                                    List<String> list_three = wxUtils.getNodeList(xmlData);
                                    for (int b=0;b<list_three.size();b++){
                                        NodeXmlBean.NodeBean nodeBean_three =wxUtils.getNodeXmlBean(list_three.get(b)).getNode();
                                        if ("android:id/title".equals(nodeBean_three.getResourceid())){
                                            if("全部阻止".equals(nodeBean_three.getText())){
                                                if (wxUtils.getNodeXmlBean(list_three.get(b+3)).getNode().isChecked()==false){
                                                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y96, R.dimen.x320, R.dimen.y147);//全部阻止通知
                                                 //   wxUtils.adb("input keyevent 4");//返回
                                                    break;
                                                }else {
                                                    LogUtils.d("你进来了吗4");
                                                    ShowToast.show("已经关闭", (Activity) context);
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        wxUtils.adbUpSlide(context);
                        String TAGs = xmlData;
                        xmlData = wxUtils.getXmlData();
                        if (TAGs.equals(xmlData)) {
                            break;
                        }
                    }

                }
            }
        }
    }
    /**
     * 关闭支微信通知
     */
    public void OpendWeixin(){
        boolean TAG=true;

        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        wxUtils.adbUpSlide(context);
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("提示音和通知".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//声音和提示
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y291, R.dimen.x320, R.dimen.y329);//通知
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y109, R.dimen.x320, R.dimen.y147);//应用通知
                    while (TAG){
                        xmlData = wxUtils.getXmlData();//获取当前页面
                        List<String> list_two = wxUtils.getNodeList(xmlData);
                        for (int a=0;a<list_two.size();a++){
                            NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                            if ("android:id/title".equals(nodeBean_two.getResourceid())){
                                if ("微信".equals(nodeBean_two.getText())){
                                    listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击支付宝
                                    xmlData = wxUtils.getXmlData();//获取当前页面
                                    List<String> list_three = wxUtils.getNodeList(xmlData);
                                    for (int b=0;b<list_three.size();b++){
                                        NodeXmlBean.NodeBean nodeBean_three =wxUtils.getNodeXmlBean(list_three.get(b)).getNode();
                                        if ("android:id/title".equals(nodeBean_three.getResourceid())){
                                            if("全部阻止".equals(nodeBean_three.getText())){
                                                if (wxUtils.getNodeXmlBean(list_three.get(b+3)).getNode().isChecked()==false){
                                                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y96, R.dimen.x320, R.dimen.y147);//全部阻止通知
                                                   // wxUtils.adb("input keyevent 4");//返回
                                                    break;
                                                }else {
                                                    LogUtils.d("你进来了吗4");
                                                    ShowToast.show("已经关闭", (Activity) context);
                                                }
                                            }
                                        }
                                    }

                                }

                            }
                        }
                        wxUtils.adbUpSlide(context);
                        String TAGs = xmlData;
                        xmlData = wxUtils.getXmlData();
                        if (TAGs.equals(xmlData)) {
                            break;
                        }
                        //wxUtils.adbUpSlide(context);
                    }

                }
            }
        }
    }

    /**
     * 设置手机禁止切换屏幕
     */
    public void SettingRotate(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        wxUtils.adbUpSlide(context);
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("显示".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击显示
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();//获取当前页面
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("android:id/title".equals(nodeBean_two.getResourceid())) {
                            if ("设备旋转时".equals(nodeBean_two.getText())) {
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击设备旋转时
                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y352, R.dimen.x160, R.dimen.y386);//选择设备保持纵向
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置手机休眠
     */
     public void gotoDormancy(){
         Intent intent = new Intent(Settings.ACTION_SETTINGS);
         context.startActivity(intent);
         wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
         wxUtils.adbUpSlide(context);
         xmlData = wxUtils.getXmlData();//获取当前页面
         List<String> list = wxUtils.getNodeList(xmlData);
         for (int i = 0; i < list.size(); i++) {
             NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
             if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                 if ("显示".equals(nodeBean.getText())) {
                     listXY = wxUtils.getXY(nodeBean.getBounds());//
                     wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击显示
                     wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                     xmlData = wxUtils.getXmlData();//获取当前页面
                     List<String> list_two = wxUtils.getNodeList(xmlData);
                     for (int a = 0; a < list_two.size(); a++) {
                         NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                         if ("android:id/title".equals(nodeBean_two.getResourceid())) {
                             if ("休眠".equals(nodeBean_two.getText())) {
                                 listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                 wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击设备休眠状态
                                 wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y289, R.dimen.x304, R.dimen.y323);//选择休眠时间30分钟

                             }
                         }
                     }
                 }
             }

         }

    }

    /**
     * 清理手机垃圾
     */
    public void Cleancache(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        wxUtils.adbUpSlide(context);
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("存储设备和 USB".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击存储设备和usb
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                   // wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y150, R.dimen.x320, R.dimen.x287);//点击应用
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();//获取当前页面
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a=0;a<list_two.size();a++){
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("android:id/title".equals(nodeBean_two.getResourceid())){
                            if ("缓存数据".equals(nodeBean_two.getText())){
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//店家缓存
                                wxUtils.adbDimensClick(context, R.dimen.x228, R.dimen.y228, R.dimen.x292, R.dimen.y262);//点击确定清除所有应用的缓存
                                wxUtils.adb("input keyevent 4");
                            }


                        }
                    }


                }
            }

        }


    }


    /**
     * 修改手机系统时间
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @throws IOException
     * @throws InterruptedException
     */
    public static void setDateTime(int year, int month, int day, int hour, int minute) throws IOException, InterruptedException {

        requestPermission();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);


        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            SystemClock.setCurrentTimeMillis(when);
        }

        long now = Calendar.getInstance().getTimeInMillis();
        //Log.d(TAG, "set tm="+when + ", now tm="+now);

        if(now - when > 1000)
            throw new IOException("failed to set Date.");

    }

    static void requestPermission() throws InterruptedException, IOException {
        createSuProcess("chmod 666 /dev/alarm").waitFor();
    }

    static Process createSuProcess() throws IOException  {
        File rootUser = new File("/system/xbin/ru");
        if(rootUser.exists()) {
            return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
        } else {
            return Runtime.getRuntime().exec("su");
        }
    }

    static Process createSuProcess(String cmd) throws IOException {
        DataOutputStream os = null;
        Process process = createSuProcess();
        try {
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit $?\n");
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }

        return process;
    }
    private void getSettingTime(int time,int gours){
        if (time==1){
            wxUtils.adbDimensClick(context, R.dimen.y130, R.dimen.y161, R.dimen.y158, R.dimen.y187);//
        }else if (time==2){
            wxUtils.adbDimensClick(context, R.dimen.y153, R.dimen.y182, R.dimen.y181, R.dimen.y210);//
        }else if (time==3){
            wxUtils.adbDimensClick(context, R.dimen.x230, R.dimen.y214, R.dimen.x270, R.dimen.y242);//
        }else if (time==4){
            wxUtils.adbDimensClick(context, R.dimen.x217, R.dimen.y245, R.dimen.y181, R.dimen.y274);//
        }else if (time==5){
            wxUtils.adbDimensClick(context, R.dimen.x185, R.dimen.y268, R.dimen.x225, R.dimen.y311);//
        }else if (time==6){
            wxUtils.adbDimensClick(context, R.dimen.x140, R.dimen.y277, R.dimen.x180, R.dimen.y305);//
        }else if (time==7){
            wxUtils.adbDimensClick(context, R.dimen.x95, R.dimen.y268, R.dimen.x135, R.dimen.y297);//
        }else if (time==8){
            wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y245, R.dimen.x102, R.dimen.y274);//
        }else if (time==9){
            wxUtils.adbDimensClick(context, R.dimen.x50, R.dimen.y214, R.dimen.x90, R.dimen.y242);//
        }else if (time==10){
            wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y182, R.dimen.x102, R.dimen.y210);//
        }else if (time==11){
            wxUtils.adbDimensClick(context, R.dimen.x95, R.dimen.y159, R.dimen.x135, R.dimen.y206);//
        }else if (time==12){
            wxUtils.adbDimensClick(context, R.dimen.x140, R.dimen.y151, R.dimen.x180, R.dimen.y179);//
        }else if (time==13){
            wxUtils.adbDimensClick(context, R.dimen.x167, R.dimen.y181, R.dimen.x207, R.dimen.y209);//
        }else if (time==14){
            wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y195, R.dimen.x227, R.dimen.y223);//
        }else if (time==15){
            wxUtils.adbDimensClick(context, R.dimen.x194, R.dimen.y214, R.dimen.x234, R.dimen.y242);//
        }else if (time==16){
            wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y233, R.dimen.x227, R.dimen.y261);//
        }else if (time==17){
            wxUtils.adbDimensClick(context, R.dimen.x167, R.dimen.y247, R.dimen.x207, R.dimen.y275);//
        }else if (time==18){
            wxUtils.adbDimensClick(context, R.dimen.x140, R.dimen.y252, R.dimen.x180, R.dimen.y280);//
        }else if (time==19){
            wxUtils.adbDimensClick(context, R.dimen.x113, R.dimen.y247, R.dimen.x153, R.dimen.y275);//
        }else if (time==20){
            wxUtils.adbDimensClick(context, R.dimen.x93, R.dimen.y233, R.dimen.x133, R.dimen.y261);//
        }else if (time==21){
            wxUtils.adbDimensClick(context, R.dimen.x86, R.dimen.y214, R.dimen.x126, R.dimen.y242);//
        }else if (time==22){
            wxUtils.adbDimensClick(context, R.dimen.x93, R.dimen.y195, R.dimen.x133, R.dimen.y223);//
        }else if (time==23){
            wxUtils.adbDimensClick(context, R.dimen.x113, R.dimen.y181, R.dimen.x153, R.dimen.y209);//
        }else if (time==24){
            wxUtils.adbDimensClick(context, R.dimen.x140, R.dimen.y176, R.dimen.x180, R.dimen.y204);//
        }else if (time==0){
            wxUtils.adbDimensClick(context, R.dimen.x140, R.dimen.y176, R.dimen.x180, R.dimen.y204);//
        }
        /**
         * 判断定时的分钟
         */
        if (gours==5){
            wxUtils.adbDimensClick(context, R.dimen.y130, R.dimen.y161, R.dimen.y158, R.dimen.y187);//
        }else if (gours==10){
            wxUtils.adbDimensClick(context, R.dimen.y153, R.dimen.y182, R.dimen.y181, R.dimen.y210);//
        }else if (gours==15){
            wxUtils.adbDimensClick(context, R.dimen.x230, R.dimen.y214, R.dimen.x270, R.dimen.y242);//
        }else if (gours==20){
            wxUtils.adbDimensClick(context, R.dimen.x217, R.dimen.y245, R.dimen.y181, R.dimen.y274);//
        }else if (gours==25){
            wxUtils.adbDimensClick(context, R.dimen.x185, R.dimen.y268, R.dimen.x225, R.dimen.y311);//
        }else if (gours==30){
            wxUtils.adbDimensClick(context, R.dimen.x140, R.dimen.y277, R.dimen.x180, R.dimen.y305);//
        }else if (gours==35){
            wxUtils.adbDimensClick(context, R.dimen.x95, R.dimen.y268, R.dimen.x135, R.dimen.y297);//
        }else if (gours==40){
            wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y245, R.dimen.x102, R.dimen.y274);//
        }else if (gours==45){
            wxUtils.adbDimensClick(context, R.dimen.x50, R.dimen.y214, R.dimen.x90, R.dimen.y242);//
        }else if (gours==50){
            wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y182, R.dimen.x102, R.dimen.y210);//
        }else if (gours==55){
            wxUtils.adbDimensClick(context, R.dimen.x95, R.dimen.y159, R.dimen.x135, R.dimen.y206);//
        }else if (gours==60){
            wxUtils.adbDimensClick(context, R.dimen.x140, R.dimen.y151, R.dimen.x180, R.dimen.y179);//
        }else if (gours==0){
            wxUtils.adbDimensClick(context, R.dimen.x140, R.dimen.y151, R.dimen.x180, R.dimen.y179);//
        }
    }
    //支付宝 微信  wx助手的相关权限初始化设置
    public void SettingAllJurisdiction_sougou(){
        List<String> zifu=new ArrayList<String>();
        boolean flag=true;
        gotoAnquan();
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("安全".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击安全
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("android:id/title".equals(nodeBean_two.getResourceid())) {
                            if ("\uFEFF应用权限".equals(nodeBean_two.getText())) {
                                if (wxUtils.getNodeXmlBean(list_two.get(a + 3)).getNode().isChecked() == false) {
                                    listXY = wxUtils.getXY(wxUtils.getNodeXmlBean(list_two.get(a + 3)).getNode().getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                                }
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击应用权限
                                //wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                                wxUtils.adbDimensClick(context, R.dimen.x161, R.dimen.y56, R.dimen.x320, R.dimen.y90);//应用、
                               while (flag) {
                                    xmlData = wxUtils.getXmlData();//获取当前页面
                                    List<String> list_san = wxUtils.getNodeList(xmlData);
                                    for (int b = 0; b < list_san.size(); b++) {
                                        NodeXmlBean.NodeBean nodeBean_san = wxUtils.getNodeXmlBean(list_san.get(b)).getNode();
                                        if ("com.mediatek.security:id/app_name".equals(nodeBean_san.getResourceid())) {
                                            if ("搜狗输入法".equals(nodeBean_san.getText())) {
                                                listXY = wxUtils.getXY(nodeBean_san.getBounds());//
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                xmlData = wxUtils.getXmlData();//获取当前页面
                                                List<String> list_si = wxUtils.getNodeList(xmlData);
                                                for (int c = 0; c < list_si.size(); c++) {
                                                    NodeXmlBean.NodeBean nodeBean_si = wxUtils.getNodeXmlBean(list_si.get(c)).getNode();
                                                    if ("com.mediatek.security:id/app_name".equals(nodeBean_si.getResourceid())) {

                                                        if ("使用\uFEFF摄像头".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("使用\uFEFF摄像头")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                            }
                                                        }
                                                        if ("开启 WLAN".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("开启 WLAN")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("开启 WLAN");
                                                            }

                                                        }

                                                            if ("发送邮件".equals(nodeBean_si.getText())) {
                                                                if (!zifu.contains("发送邮件")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("发送邮件");
                                                            }
                                                        }

                                                            if ("发送彩信".equals(nodeBean_si.getText())) {
                                                                if (!zifu.contains("发送彩信")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("发送彩信");
                                                            }
                                                        }

                                                        if ("启动录音".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("启动录音")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("启动录音");
                                                            }
                                                        }
                                                        if ("读取短信记录".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("读取短信记录")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("读取短信记录");
                                                            }
                                                        }

                                                        if ("读取彩信记录".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("读取彩信记录")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("读取彩信记录");
//                                                            wxUtils.adbUpSlide(context);
//                                                            xmlData = wxUtils.getXmlData();
                                                            }
                                                        }
                                                        if ("读取联系人".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("读取联系人")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("读取联系人");
                                                            }
                                                        }
                                                        if ("获取当前位置".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("获取当前位置")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.contains("获取当前位置");
                                                                //wxUtils.adb("input keyevent 4");//返回
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    String TAG = xmlData;
                                    wxUtils.adbUpSlide(context);
                                    xmlData = wxUtils.getXmlData();
                                    if (TAG.equals(xmlData)) {
                                        wxUtils.adb("input keyevent 4");
                                        wxUtils.adb("input keyevent 4");
                                        wxUtils.adb("input keyevent 4");
                                        break;
                                    }



                               }
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * 支付宝设置
     */
    public void SettingAllJurisdiction_apli(){
        List<String> zifu=new ArrayList<String>();
        boolean flag=true;
        gotoAnquan();
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("安全".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击安全
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("android:id/title".equals(nodeBean_two.getResourceid())) {
                            if ("\uFEFF应用权限".equals(nodeBean_two.getText())) {
                                if (wxUtils.getNodeXmlBean(list_two.get(a + 3)).getNode().isChecked() == false) {
                                    listXY = wxUtils.getXY(wxUtils.getNodeXmlBean(list_two.get(a + 3)).getNode().getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                                }
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击应用权限
                                //wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                                wxUtils.adbDimensClick(context, R.dimen.x161, R.dimen.y56, R.dimen.x320, R.dimen.y90);//应用、
                                while (flag) {
                                    xmlData = wxUtils.getXmlData();//获取当前页面
                                    List<String> list_san = wxUtils.getNodeList(xmlData);
                                    for (int b = 0; b < list_san.size(); b++) {
                                        NodeXmlBean.NodeBean nodeBean_san = wxUtils.getNodeXmlBean(list_san.get(b)).getNode();
                                        if ("com.mediatek.security:id/app_name".equals(nodeBean_san.getResourceid())) {
                                            if ("支付宝".equals(nodeBean_san.getText())) {
                                                listXY = wxUtils.getXY(nodeBean_san.getBounds());//
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                xmlData = wxUtils.getXmlData();//获取当前页面
                                                List<String> list_si = wxUtils.getNodeList(xmlData);
                                                for (int c = 0; c < list_si.size(); c++) {
                                                    NodeXmlBean.NodeBean nodeBean_si = wxUtils.getNodeXmlBean(list_si.get(c)).getNode();
                                                    if ("com.mediatek.security:id/app_name".equals(nodeBean_si.getResourceid())) {
                                                         if ("拨打电话".equals(nodeBean_si.getText())){
                                                             if (!zifu.contains("拨打电话")){
                                                                 listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                 wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                 wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                 zifu.add("拨打电话");
                                                             }

                                                         }
                                                         if ("\uFEFF发起多方通话".equals(nodeBean_si.getText())){
                                                             if (!zifu.contains("\uFEFF发起多方通话")){
                                                                 listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                 wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                 wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                 zifu.add("\uFEFF发起多方通话");
                                                             }
                                                         }
                                                         if ("使用\uFEFF摄像头".equals(nodeBean_si.getText())){
                                                             if (!zifu.contains("使用\uFEFF摄像头")){
                                                                 listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                 wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                 wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                 zifu.add("使用\uFEFF摄像头");
                                                             }
                                                         }

                                                        if ("开启数据连接".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("开启数据连接")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("开启数据连接");
                                                            }
                                                        }
                                                        if ("开启 WLAN".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("开启 WLAN")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("开启 WLAN");
                                                            }
                                                        }
                                                        if ("开启蓝牙".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("开启蓝牙")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("开启蓝牙");
                                                            }
                                                        }
                                                        if ("写/删联系人".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("写/删联系人")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("写/删联系人");
                                                            }
                                                        }
                                                        if ("发送邮件".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("发送邮件")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("发送邮件");
                                                            }
                                                        }
                                                        if ("发送彩信".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("发送彩信")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("发送彩信");
                                                            }
                                                        }
                                                        if ("启动录音".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("启动录音")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("启动录音");
                                                            }
                                                        }
                                                        if ("读取短信记录".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("读取短信记录")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("读取短信记录");
                                                            }
                                                        }
//                                                        if ("读取短信记录".equals(nodeBean_si.getText())){
//                                                            if (!zifu.contains("读取短信记录")){
//                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
//                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
//                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
//                                                                zifu.add("读取短信记录");
//                                                            }
//                                                        }
                                                        if ("读取彩信记录".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("读取彩信记录")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("读取彩信记录");
                                                            }
                                                        }
                                                        if ("读取联系人".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("读取联系人")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("读取联系人");
                                                            }
                                                        }
                                                        if ("获取当前位置".equals(nodeBean_si.getText())){
                                                            if (!zifu.contains("获取当前位置")){
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("获取当前位置");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    String TAG = xmlData;
                                    wxUtils.adbUpSlide(context);
                                    xmlData = wxUtils.getXmlData();
                                    if (TAG.equals(xmlData)) {
                                        wxUtils.adb("input keyevent 4");
                                        wxUtils.adb("input keyevent 4");
                                        wxUtils.adb("input keyevent 4");
                                        break;
                                    }


                                }
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 微信权限
     */
    public void SettingAllJurisdiction_weixin(){
        List<String> zifu=new ArrayList<String>();
        boolean flag=true;
        gotoAnquan();
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("安全".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击安全
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("android:id/title".equals(nodeBean_two.getResourceid())) {
                            if ("\uFEFF应用权限".equals(nodeBean_two.getText())) {
                                if (wxUtils.getNodeXmlBean(list_two.get(a + 3)).getNode().isChecked() == false) {
                                    listXY = wxUtils.getXY(wxUtils.getNodeXmlBean(list_two.get(a + 3)).getNode().getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                                }
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击应用权限
                                //wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                                wxUtils.adbDimensClick(context, R.dimen.x161, R.dimen.y56, R.dimen.x320, R.dimen.y90);//应用、
                                while (flag) {
                                    xmlData = wxUtils.getXmlData();//获取当前页面
                                    List<String> list_san = wxUtils.getNodeList(xmlData);
                                    for (int b = 0; b < list_san.size(); b++) {
                                        NodeXmlBean.NodeBean nodeBean_san = wxUtils.getNodeXmlBean(list_san.get(b)).getNode();
                                        if ("com.mediatek.security:id/app_name".equals(nodeBean_san.getResourceid())) {
                                            if ("微信".equals(nodeBean_san.getText())){
                                                listXY = wxUtils.getXY(nodeBean_san.getBounds());//
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                xmlData = wxUtils.getXmlData();//获取当前页面
                                                List<String> list_si = wxUtils.getNodeList(xmlData);
                                                for (int c = 0; c < list_si.size(); c++) {
                                                    NodeXmlBean.NodeBean nodeBean_si = wxUtils.getNodeXmlBean(list_si.get(c)).getNode();
                                                    if ("com.mediatek.security:id/app_name".equals(nodeBean_si.getResourceid())) {
                                                        if ("使用\uFEFF摄像头".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("使用\uFEFF摄像头")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("使用\uFEFF摄像头");
                                                            }

                                                        }
                                                        if ("开启 WLAN".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("开启 WLAN")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("开启 WLAN");
                                                            }
                                                        }
                                                        if ("开启蓝牙".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("开启蓝牙")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("开启蓝牙");
                                                            }
                                                        }
                                                        if ("写/删联系人".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("写/删联系人")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("写/删联系人");
                                                            }
                                                        }
                                                        if ("发送邮件".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("发送邮件")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("发送邮件");
                                                            }
                                                        }
                                                        if ("发送彩信".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("发送彩信")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("发送彩信");
                                                            }
                                                        }
                                                        if ("启动录音".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("启动录音")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("启动录音");
                                                            }
                                                        }
                                                        if ("读取短信记录".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("读取短信记录")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("读取短信记录");
                                                            }
                                                        }
                                                        if ("读取彩信记录".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("读取彩信记录")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("读取彩信记录");
                                                            }
                                                        }
                                                        if ("读取联系人".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("读取联系人")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("读取联系人");
                                                            }
                                                        }
                                                        if ("获取当前位置".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("获取当前位置")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y241, R.dimen.x304, R.dimen.y275);//总是拒绝
                                                                zifu.add("获取当前位置");
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                    String TAG = xmlData;
                                    wxUtils.adbUpSlide(context);
                                    xmlData = wxUtils.getXmlData();
                                    if (TAG.equals(xmlData)) {
                                        wxUtils.adb("input keyevent 4");
                                        wxUtils.adb("input keyevent 4");
                                        wxUtils.adb("input keyevent 4");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }


    }

    /**
     * wx助手权限
     */
    public void SettingAllJurisdiction_wx(){
        List<String> zifu=new ArrayList<String>();
        boolean flag=true;
        gotoAnquan();
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("安全".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击安全
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("android:id/title".equals(nodeBean_two.getResourceid())) {
                            if ("\uFEFF应用权限".equals(nodeBean_two.getText())) {
                                if (wxUtils.getNodeXmlBean(list_two.get(a + 3)).getNode().isChecked() == false) {
                                    listXY = wxUtils.getXY(wxUtils.getNodeXmlBean(list_two.get(a + 3)).getNode().getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                                }
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击应用权限
                                //wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                                wxUtils.adbDimensClick(context, R.dimen.x161, R.dimen.y56, R.dimen.x320, R.dimen.y90);//应用、
//                                while (flag) {
                                    xmlData = wxUtils.getXmlData();//获取当前页面
                                    List<String> list_san = wxUtils.getNodeList(xmlData);
                                    for (int b = 0; b < list_san.size(); b++) {
                                        NodeXmlBean.NodeBean nodeBean_san = wxUtils.getNodeXmlBean(list_san.get(b)).getNode();
                                        if ("com.mediatek.security:id/app_name".equals(nodeBean_san.getResourceid())) {
                                            if ("wx助手".equals(nodeBean_san.getText())) {

                                                listXY = wxUtils.getXY(nodeBean_san.getBounds());//
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                xmlData = wxUtils.getXmlData();//获取当前页面
                                                List<String> list_si = wxUtils.getNodeList(xmlData);
                                                for (int c = 0; c < list_si.size(); c++) {
                                                    NodeXmlBean.NodeBean nodeBean_si = wxUtils.getNodeXmlBean(list_si.get(c)).getNode();
                                                    if ("com.mediatek.security:id/app_name".equals(nodeBean_si.getResourceid())) {
                                                        if ("开启蓝牙".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("开启蓝牙")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("开启蓝牙");
                                                            }

                                                        }
                                                        if ("写/删联系人".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("写/删联系人")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("写/删联系人");
                                                            }

                                                        }
                                                        if ("发送邮件".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("发送邮件")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("发送邮件");
                                                            }

                                                        }
                                                        if ("发送彩信".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("发送彩信")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("发送彩信");
                                                            }

                                                        }
                                                        if ("启动录音".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("启动录音")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("启动录音");
                                                            }

                                                        }
                                                        if ("读取联系人".equals(nodeBean_si.getText())) {
                                                            if (!zifu.contains("读取联系人")) {
                                                                listXY = wxUtils.getXY(nodeBean_si.getBounds());//
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y174, R.dimen.x304, R.dimen.y207);//总是允许
                                                                zifu.add("读取联系人");
                                                                wxUtils.adb("input keyevent 4");
                                                                wxUtils.adb("input keyevent 4");
                                                                wxUtils.adb("input keyevent 4");
                                                                getHomes();
                                                            }

                                                        }

                                                    }
                                                }

                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 支付宝手机定时开关机
     * @param dataBean
     */
    public void SettingPhoneOpen(final MessageListBean.ContentBean.DataBean dataBean){
        Starting_hours=dataBean.getParam().getBoot_time_h();//开机小时
        Starting_minute=dataBean.getParam().getBoot_time_m();//开机分钟
        Shutdown_hours=dataBean.getParam().getOff_time_h();//关机分钟
        Shutdown_minute=dataBean.getParam().getOff_time_m();//关机小时
//        if (Starting_hours==0){
//            Starting_hours=8;
//        }
//        if (Starting_minute==0){
//            Starting_minute=30;
//        }
//        if (Shutdown_hours==0){
//            Shutdown_hours=23;
//        }
//        if (Shutdown_minute==0){
//            Shutdown_minute=30;
//        }
        /**
         * 手机的定时开机和关机
         */
        SettingTimegj(Starting_hours,Starting_minute,Shutdown_hours,Shutdown_minute);
        wxUtils.adb("input keyevent 4");
        wxUtils.adb("input keyevent 4");
        getHomes();

    }

    /**
     * 支付宝手机设置
     * @param dataBean
     */
    public void SettingPhone(final MessageListBean.ContentBean.DataBean dataBean){
        String ph_num=dataBean.getParam().getPhoneRadio();
        if ("1".equals(ph_num)){
            restart();
        }else if ("2".equals(ph_num)){
            shutdown();
        }else if ("3".equals(ph_num)){
            ShowToast.show("开始清理手机缓存任务", (Activity) context);
            Cleancache();
            ShowToast.show("手机清理缓存任务执行完毕", (Activity) context);
        }else if ("7".equals(ph_num)){//设置手机屏幕亮度显示一半
            gotoLuminance_Two();
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
            getHomes();

        }
    }

    /**
     * 关闭手机定位
     */
    public void ClosedLocation(){
        gotoAnquan();
        xmlData = wxUtils.getXmlData();//获取当前页面
        List<String> list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(i)).getNode();
            if ("com.android.settings:id/title".equals(nodeBean.getResourceid())) {
                if ("位置信息".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击安全
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    List<String> list_two = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < list_two.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean_two = wxUtils.getNodeXmlBean(list_two.get(a)).getNode();
                        if ("com.android.settings:id/switch_widget".equals(nodeBean_two.getResourceid())){
                            if (nodeBean_two.isChecked()==true){
                                listXY = wxUtils.getXY(nodeBean_two.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                wxUtils.adb("input keyevent 4");//返回
                                //wxUtils.adb("input keyevent 4");//返回
                            }else {
                                wxUtils.adb("input keyevent 4");//返回
                                //wxUtils.adb("input keyevent 4");//返回
                            }
                        }
                    }
                }
            }
        }

    }
    /**
     * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
     *
     * @param
     * @return
     */
    public static int getAPNType() {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G
            } else {
                netType = 3;// 2G
            }
        }
        return netType;
    }

    /**
     * 单独的支付宝初始化
     */
    public void ZFB_phone(){
        SettingAllJurisdiction_apli();
        OpendZFB();
        wxUtils.adb("input keyevent 4");//返回
        wxUtils.adb("input keyevent 4");//返回
        getHomes();
    }
   public void WXinit(){
        SettingAllJurisdiction_wx();
   }
}
