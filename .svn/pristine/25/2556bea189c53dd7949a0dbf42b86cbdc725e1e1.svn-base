package com.zplh.zplh_android_yk.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.ClipboardManager;
import android.util.Log;
import android.widget.Toast;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseApplication;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.bean.WxFriendsMessageCultivate;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.utils.FileUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.StringUtils;

import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lichun on 2017/7/29.
 * Description:微信接收视频和语音聊天
 */

public class NotificationMonitorService extends NotificationListenerService {
    WxUtils wxUtils = new WxUtils();
    private String xmlData;
    private BaseApplication app;
    Random random = new Random();
    private ClipboardManager cm;
    private List<Integer> listXY;
    private Handler handler = new Handler() {

        // 该方法运行在主线程中
        // 接收到handler发送的消息，对UI进行操作
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case 0x123:
                    cm = (ClipboardManager) app.getContext().getSystemService(CLIPBOARD_SERVICE);
                    break;
                case 0x124:
                    Toast.makeText(app.getApplicationContext(), "数据下载中,等待25秒", Toast.LENGTH_SHORT).show();
                    break;
                case 0x125:
                    Toast.makeText(app.getApplicationContext(), "互聊结束", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };

    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        app = (BaseApplication) getApplication();
        handler.sendEmptyMessage(0x123);

        // TODO Auto-generated method stub
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);

        Log.i("XSL_Test", "Notification posted " + notificationTitle + " & " + notificationText + " & " + notificationPkg);

        //-----------------------------------------------------------------
        if (StringUtils.isEmpty(notificationText) && !"notificationPkg".equals(notificationPkg)) {
            return;
        }

        if (notificationText.equals("我通过了你的朋友验证请求，现在我们可以开始聊天了")) {

        }

        if (notificationText.contains("你好。。")) {
            if (SPUtils.getBoolean(app.getApplicationContext(), "chitchat", true)) {//双向互聊开始
                SPUtils.putBoolean(app.getApplicationContext(), "chitchat", false);

                //打开消息
                PendingIntent pendingIntent = sbn.getNotification().contentIntent;
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }

                //开始互聊回复消息
                chitchatTwo(notificationTitle);

            }
        } else if (notificationText.contains("下次再聊，拜拜")) {//双向互聊结束  TODO
            SPUtils.putBoolean(app.getApplicationContext(), "chitchat", true);
        } else if (notificationText.contains("你好现在方便吗")) {
            videoChat();
        }
    }


    // 在删除消息时触发
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // TODO Auto-generated method stub
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);
        Log.i("XSL_Test", "Notification removed " + notificationTitle + " & " + notificationText);
    }

    /**
     * 视频语音聊天
     */
    private void videoChat() {
        //接听视频
        LogUtils.d("收到请求，等待3s");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        LogUtils.d("我接听了");
        wxUtils.adbClick(350, 733, 350, 733);
        String xmlData = wxUtils.getXmlData();
        if (xmlData.contains("在移动网络环境下会影响视频和音频质量，并产生手机流量。")) {
            wxUtils.adbClick(300, 517, 396, 562);//确定
        } else if (xmlData.contains("微信 正在尝试 启动录音。")) {
            wxUtils.adbClick(51, 456, 429, 504);//记住选择
            wxUtils.adbClick(330, 528, 426, 600);//允许
        }
        String xmlData1 = wxUtils.getXmlData();
        if (xmlData1.contains("在移动网络环境下会影响视频和音频质量，并产生手机流量。")) {
            wxUtils.adbClick(300, 517, 396, 562);//确定
        } else if (xmlData1.contains("微信 正在尝试 启动录音。")) {
            wxUtils.adbClick(51, 456, 429, 504);//记住选择
            wxUtils.adbClick(330, 528, 426, 600);//允许
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置间隔时间
                int start;
                if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getVoice_time_s())) {
                    start = 20;
                } else {
                    start = Integer.valueOf(app.getWxGeneralSettingsBean().getVoice_time_s());
                }
                int end;
                if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getVoice_time_e())) {
                    end = 30;
                } else {
                    end = Integer.valueOf(app.getWxGeneralSettingsBean().getVoice_time_e());
                }
                int timeSleep = new Random().nextInt(end - start + 1) + start;
                LogUtils.e("end=" + end + "__start=" + start + "___通话随机数=" + timeSleep);
                try {
                    Thread.sleep(timeSleep * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                wxUtils.adbClick(189, 682, 291, 784);
                wxUtils.adbClick(189, 682, 291, 784);//挂断视频
                wxUtils.adb("input keyevent KEYCODE_HOME");//回到home
            }
        }).start();
    }

    /**
     * 双向互聊
     */
    private void chitchatTwo(String notificationTitle) {
        int x = app.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.x136);
        int y = app.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
        handler.sendEmptyMessage(0x124);
        getChatData();

        w:
        while (true) {
            xmlData = wxUtils.getXmlData();
            //判断是否在与好友聊天界面
            if (xmlData.contains("当前所在页面,与") && xmlData.contains("聊天信息")) {

                if (xmlData.contains("按住 说话")) {
                    wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                }

                List<String> copyList = wxUtils.getNodeList(xmlData);
                for (int c = 0; c < copyList.size(); c++) {
                    NodeXmlBean.NodeBean copyBean = wxUtils.getNodeXmlBean(copyList.get(c)).getNode();
                    if (copyBean != null && copyBean.getResourceid() != null && "com.tencent.mm:id/a3b".equals(copyBean.getResourceid())) {
                        if (!StringUtils.isEmpty(copyBean.getText())) {
                            int xx = app.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.x296);
                            int yy = app.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.y343);//删除
                            wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 7000);  //删除
                            wxUtils.adb("input keyevent 4");
                        }
                        break;
                    }
                }

                int randomNum = random.nextInt(3);
                switch (randomNum) {
                    case 0://文字
                        // 将文本内容放到系统剪贴板里。
                        if (textList.size() > 0) {

                            cm.setText(wxUtils.getFaceText(textList.get(0)));
                            textList.remove(0);
                        }
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                        wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                        wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                        break;
                    case 1://图片
                        if (imageList.size() > 0) {
                            downFlockImgAliPay(imageList.get(0));
                            imageList.remove(0);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                        wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册

                        int a = 0;
                        while (a < 5) {
                            a++;
                            xmlData = wxUtils.getXmlData();
                            if (!xmlData.contains("图片和视频")) {
                                wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册
                            } else {
                                break;
                            }
                        }

                        if (xmlData.contains("图片和视频")) {
                            wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x80, R.dimen.y56, R.dimen.x99, R.dimen.y69);//确定
                            wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        break;
                    case 2://语音

                        xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("切换到按住说话")) {
                            wxUtils.adbDimensClick(app.getApplicationContext(), R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                        }
                        //录制时间
                        int start;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRecord_time_s())) {
                            start = 10;
                        } else {
                            start = Integer.valueOf(app.getWxGeneralSettingsBean().getRecord_time_s());
                        }
                        int end;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRecord_time_e())) {
                            end = 20;
                        } else {
                            end = Integer.valueOf(app.getWxGeneralSettingsBean().getRecord_time_e());
                        }
                        int timeSleep = random.nextInt(end - start + 1) + start;

                        LogUtils.e("end=" + end + "__start=" + start + "___语音时间=" + timeSleep);
//                    Toast.makeText(app.getApplicationContext(),"语音录音时间：" + timeSleep + "秒",Toast.LENGTH_SHORT).show();
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + timeSleep * 1000);  //长按EdiText

                        break;
                }

             f:   for (int b=0;b<3;b++) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();
                    List<String> nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = nodeList.size(); a >0; a--) {
                        LogUtils.d(nodeList.get(a-1));
                        if(nodeList.get(a-1).contains(" />")){
                            continue ;
                        }
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a-1)).getNode();
                        if (nodeBean.getResourceid()!=null&&nodeBean.getResourceid().equals("com.tencent.mm:id/id")&&nodeBean.getContentdesc()!=null&&nodeBean.getContentdesc().contains("头像")) {
                            if(nodeBean.getContentdesc().contains(notificationTitle+"头像")){
                                LogUtils.d("回复了消息");
                                NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(nodeList.get(a+1)).getNode();
                                if(nodeBean1.getResourceid()!=null){
                                    switch (nodeBean1.getResourceid()){
                                        case "com.tencent.mm:id/a7k":
                                            listXY = wxUtils.getXY(nodeBean1.getBounds());//点击语音消息
                                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                            break ;
                                        case "com.tencent.mm:id/if":
                                            if("下次再聊，拜拜".equals(nodeBean1.getText())){
                                                LogUtils.d("任务完成");
                                                wxUtils.adb("input keyevent KEYCODE_HOME");
                                                SPUtils.putBoolean(app.getApplicationContext(), "chitchat", true);
                                                handler.sendEmptyMessage(0x125);

                                                break w;
                                            }
                                            break ;
                                    }
                                }

                                continue w;
                            }else {
                                LogUtils.d("还没有回复消息");
                                continue f ;
                            }
                        }
                    }

                }
                LogUtils.d("超时，任务结束");
                wxUtils.adb("input keyevent KEYCODE_HOME");
                SPUtils.putBoolean(app.getApplicationContext(), "chitchat", true);
                break w;
            } else {
                wxUtils.adb("input keyevent KEYCODE_HOME");
                SPUtils.putBoolean(app.getApplicationContext(), "chitchat", true);
                break w;
            }
        }
    }

    /**
     * 获取双向互聊数据
     */
    private void getChatData() {
        httpChatData(1);
        httpChatData(0);


        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    List<String> textList = new ArrayList<>();
    List<String> imageList = new ArrayList<>();

    /**
     * 双向互撩数据
     * 0图片   1文字
     */
    private void httpChatData(final int type) {
        String uid = SPUtils.getString(app.getApplicationContext(), "uid", "0000");
        RequestParams params = new RequestParams(URLS.wechat_list());
        params.addQueryStringParameter("type", type + "");
        LogUtils.d(URLS.wechat_list() + "?type=" + type);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<WxFriendsMessageCultivate>() {

            @Override
            public void onSuccess(final WxFriendsMessageCultivate bean) {
                if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
                    if (type == 1) {
                        textList.clear();
                        textList.addAll(bean.getData());
                    } else {
                        imageList.clear();
                        imageList.addAll(bean.getData());

                        if (imageList.size()>0) {
                            for (String imageUrl : imageList) {
                                wxUtils.xDownloadFile(imageUrl);
                            }
                        }
                    }

                } else {
                    LogUtils.d("养号互聊数据有误");
                }

            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("错误返回的结果" + errorString);
            }
        });
    }

    /**
     * 支付宝群消息发图文  图片下载
     *
     * @param messageData
     * @return
     */
    private boolean downFlockImgAliPay(String messageData) {
        String path = "";
        String strMark = "";
        String fileName = "";
        String filePath = "";
        String text = "";
        String fileUrl = "";

        if (!StringUtils.isEmpty(messageData)) {//判断请求地址是否为空
            text = messageData;
        } else {
            LogUtils.d("地址为空");
            return false;
        }
        path = URLS.pic_vo + text.replace("\\", "/");
        LogUtils.d("文件url__" + path);
        strMark = text.replace("\\", "/");
        fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");
        LogUtils.d("a" + fileName);
        filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + fileName;
        LogUtils.d("b" + filePath);
        LogUtils.d("c" + FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages"));

        if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载
            LogUtils.d("存在");
            fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
            LogUtils.e("aa文件写入:" + FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false));//改名把文件添加到第一个
            wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), app.getApplicationContext());
            return true;
        } else {
            LogUtils.d("不存在");
            return false;
        }

    }


}
