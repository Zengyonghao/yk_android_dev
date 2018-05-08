package com.zplh.zplh_android_yk.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseApplication;
import com.zplh.zplh_android_yk.bean.CheckImei;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.StateRenwuBean;
import com.zplh.zplh_android_yk.bean.WxFlockMessageBean;
import com.zplh.zplh_android_yk.bean.ZhiXIngBean;
import com.zplh.zplh_android_yk.conf.MyConstains;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.conf.ZFB_URLS;
import com.zplh.zplh_android_yk.db.StateDao;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.ui.activity.MainActivity;
import com.zplh.zplh_android_yk.utils.FileUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.StringUtils;
import com.zplh.zplh_android_yk.utils.TimeUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lichun on 2017/5/27.
 * Description:极光推送获取服务器消息
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static String TAG = "pushreceiver";
    Gson gson = new Gson();
    WxUtils wxUtils = new WxUtils();
    Context context;
    private StateDao dao;
    private StateRenwuBean stateRenwuBean;
    private ZhiXIngBean zhiXIngBean;
    private ZFB_URLS zfb_urls;
    private Timer mytime = new Timer(true);
    private TimerTask timerTasks;
    private TimeUtil timeUtil;

    @Override
    public void onReceive(final Context context, Intent intent) {

        this.context = context;
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());
        zfb_urls = new ZFB_URLS();
        timeUtil = new TimeUtil();
        dao = new StateDao(context);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            System.out.println("收到了自定义消息@@消息内容是:" + content);
            System.out.println("收到了自定义消息@@消息extra是:" + extra);
            if (content != null && content.startsWith("wxversion")) {//版本升级
                double version = Double.valueOf(wxUtils.getVersionName(context));
                double ver = Double.valueOf(content.replace("wxversion", ""));
                LogUtils.d("versionold=" + version + "ver=" + ver);
                if (ver > version) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LogUtils.d("开始更新任务");//wxzs1.apk 正式       wxzs.apk测试
                                String uid = SPUtils.getString(context, "uid", "0001");
                                int sleepTime = Integer.valueOf(uid);
                                if (sleepTime > 0) {
                                    LogUtils.d("等待" + sleepTime + "秒下载");
                                    Thread.sleep(sleepTime * 1000);
                                }
                                File filr = wxUtils.getFileFromServer("http://103.94.20.102:8087/download/wxzs.apk");
                                String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/wxykupdata.apk";
                                LogUtils.d("下载完成开始安装");
                                wxUtils.adb("pm install -r " + path);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
            //**************解析推送过来的json数据并存放到集合中 begin******************
            if (SPUtils.getBoolean(context, "imei", false)) {//绑定过设备才执行任务
                if (!StringUtils.isEmpty(extra) && extra.contains(SPUtils.getString(context, "uid", "0000"))) {
                    MessageListBean messageBean = gson.fromJson(extra, MessageListBean.class);
                    List<MessageListBean.ContentBean.DataBean> dataBeanList = messageBean.getContent().getData();
                    String uid_1 = SPUtils.getString(context, "uid", "0000");
                    String[] isAccType = new String[1];
                    isAccType[0] = "1";  //默认给左边的手机
                    if (extra.contains(uid_1 + "_1")) {
//                        SPUtils.putInt(context, "is_accType", 1);
                        isAccType[0] = "1";
                    } else if (extra.contains(uid_1 + "_2")) {
//                        SPUtils.putInt(context, "is_accType", 2);
                        isAccType[0] = "2";
                    } else if (extra.contains(uid_1 + "_3")) {
//                        SPUtils.putInt(context, "is_accType", 3);
                        isAccType[0] = "3";
                    }
                    if (dataBeanList != null) {
                        for (int a = 0; a < dataBeanList.size(); a++) {
                            dataBeanList.get(a).getParam().setIs_accType(isAccType);
                        }
                    }
                    if (dataBeanList != null) {
                        for (int a = 0; a < dataBeanList.size(); a++) {//处理多任务
                            if (dataBeanList.size() == 1) {
                                dataBeanList.get(a).setListTask(false);
                            } else {
                                dataBeanList.get(a).setListTask(true);
                            }
                            if (!StringUtils.isEmpty(dataBeanList.get(a).getTodo_time())) {
                                if (!StringUtils.isEmpty(dataBeanList.get(a).getTodo_time())) {//如果时间过了，马上执行
                                    if (timeUtil.getCurrentTimeMilies() >= Long.valueOf(dataBeanList.get(a).getTodo_time())) {
                                        LogUtils.d("时间过了，马上执行");
                                        dataBeanList.get(a).setTodo_time("");
                                    }
                                }
                            }

                            taskTime(dataBeanList.get(a));
                        }
                    }
                } else {
                    LogUtils.d("不解析");
                }
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    MessageListBean.ContentBean.DataBean dataBean = (MessageListBean.ContentBean.DataBean) msg.obj;
                    LogUtils.d("哈哈哈哈h" + dataBean.getLog_id());
                    setBorad(dataBean);
                    break;
            }
        }
    };

    private void setBorad(final MessageListBean.ContentBean.DataBean data) {
        if (openApplicationFromBackground(context)) {//判断app是否关闭
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Intent intent2 = new Intent();
                    intent2.setAction(MyConstains.Broadcast_Task);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("messageBean", data);
                    intent2.putExtras(bundle);
                    context.sendBroadcast(intent2);
                }
            };
            timer.schedule(timerTask, 20000);
        } else {
            Intent intent2 = new Intent();
            intent2.setAction(MyConstains.Broadcast_Task);
            Bundle bundle = new Bundle();
            bundle.putSerializable("messageBean", data);
            intent2.putExtras(bundle);
            context.sendBroadcast(intent2);
        }

    }

    /**
     * 处理收到的任务
     *
     * @param data
     */
    private void taskTime(final MessageListBean.ContentBean.DataBean data) {

        String todoTimes = data.getTodo_time();//执行时间
        String interval_time = data.getParam().getInterval_time();//任务执行的间隔时间
        List<String> rangeList = data.getRange();//判断是否是指定设备执行任务da
        LogUtils.d("集合中log_id的长度是" + dao.select_log_id().size() + "");
        if (rangeList != null && (rangeList.size() == 0 || (rangeList.size() > 0 && rangeList.toString().contains(SPUtils.getString(context, "uid", "0000"))))) {
            if (data.getTask_id() == 59 || data.getTask_id() == 30) {
                downImg(data);
            }
            // if(data.getTask_id()!=50) {
            BaseApplication.getDataBeanList().add(data);//保存任务
            wxUtils.saveTask(context, BaseApplication.getDataBeanList());
            //   }
            stateRenwuBean = new StateRenwuBean(data.getTask_id(), Integer.parseInt(data.getLog_id()), "任务待执行", timeUtil.getDtae());
            dao.addPerson(stateRenwuBean);
            updata_task_status(data.getLog_id());//反馈到服务器
            //网络请求 判断该lod_id任务是否取消 取消则不在往下进行
            if ((data.getTask_id() == 499) || (data.getTask_id() == 497) || (data.getTask_id() == 498)  || (data.getTask_id() == 69)) {  // B 账户收到语音互聊任务 ，立马执行
                setBorad(data);
            }
            if (data.getTask_id() == 61 || data.getTask_id() == 59 || data.getTask_id() == 25 || data.getTask_id() == 54 || data.getTask_id() == 52 || data.getTask_id() == 53 || data.getTask_id() == 27 || data.getTask_id() == 28 || data.getTask_id() == 30 || data.getTask_id() == 32 || data.getTask_id() == 33 || data.getTask_id() == 55 || data.getTask_id() == 56 || data.getTask_id() == 57 || data.getTask_id() == 58 || data.getTask_id() == 60 || data.getTask_id() == 37 || data.getTask_id() == 496) {//微信统计任务不需要加随机时间
                // 0505 18:00  1525514400
                if (StringUtils.isEmpty(todoTimes)) {//没有设置时间，马上执行
                    if (data.getTask_id() == 496) {
                        long time = timeUtil.getLongTime(150 * 60 * 60);

                        LogUtils.d("执行时间" + time);
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                setBorad(data);
                            }
                        };
                        LogUtils.d("启动的时间是" + (time) * 1000 + "毫秒");
                        timer.schedule(timerTask, (time) * 1000);
                    } else {
                        setBorad(data);
                    }
                } else {//定时执行
                    if (data.getTask_id() == 496) {
                        long time = timeUtil.getLongTime(Long.parseLong(todoTimes) + 150 * 60 * 60);
                        LogUtils.d("执行时间" + time);
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                setBorad(data);
                            }
                        };
                        LogUtils.d("启动的时间是" + (time) * 1000 + "毫秒");
                        timer.schedule(timerTask, (time) * 1000);
                    } else {
                        long time = timeUtil.getLongTime(Long.parseLong(todoTimes));
                        LogUtils.d("执行时间" + time);
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                setBorad(data);
                            }
                        };
                        LogUtils.d("启动的时间是" + (time) * 1000 + "毫秒");
                        timer.schedule(timerTask, (time) * 1000);
                    }

                }
                return;
            }
            //支付宝加粉任务的判断
            if (data.getTask_id() == 50) {
                String start_time_e = data.getParam().getStart_time_e();//随机结束
                String start_time_s = data.getParam().getStart_time_s();//随机开始
                int max = 0;
                int min = 0;
                SPUtils.putString(context, "newsLog_id", "");
                if (StringUtils.isEmpty(start_time_e)) {
                    max = 200;
                } else {
                    max = Integer.parseInt(start_time_e);
                }
                if (StringUtils.isEmpty(start_time_s)) {
                    min = 0;
                } else {
                    min = Integer.parseInt(start_time_s);
                }
                Random random = new Random();
                final int s = random.nextInt(max) % (max - min + 1) + min;
                SPUtils.putString(context, "data_taskid", String.valueOf(data));
                if (StringUtils.isEmpty(todoTimes)) {//如果执行时间为空
                    if (StringUtils.isEmpty(interval_time)) {//如果没有设置 则给定默认的执行时间
                        long time = (24 * 60 * 60 + 30 * 60) * 1000;
                        timerTasks = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = data;
                                handler.sendMessage(message);
                            }
                        };
                        long time_wait = s + time / 1000 + timeUtil.getCurrentTimeMilies();
                        SPUtils.putString(context, "time_wait", time_wait * 1000 + "");
                        LogUtils.d("执行的是时间为空、设置间隔时间为空的情况下,执行等待时间是" + s + "秒:执行周期的时间是" + time / 1000 + "秒:" + "下一个周期预计执行的时间是" + timeUtil.getTimesCuo(time_wait * 1000));
                        mytime.schedule(timerTasks, s * 1000);
                    } else {
                        long time = (Long.parseLong(interval_time) * 60 * 60 + 30 * 60) * 1000;
                        timerTasks = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = data;
                                handler.sendMessage(message);
                            }
                        };
                        long time_wait = s + time / 1000 + timeUtil.getCurrentTimeMilies();
                        SPUtils.putString(context, "time_wait", time_wait * 1000 + "");
                        LogUtils.d("执行的是时间为空、间隔周期时间是" + time / 1000 + "秒:" + "任务随机的等待时间是" + s + "秒:" + "下一个周期预计执行的时间是" + timeUtil.getTimesCuo(time_wait * 1000));
                        mytime.schedule(timerTasks, s * 1000);
                    }
                } else {//执行时间不为空的情况下

                    long times = timeUtil.getLongTime(Long.parseLong(todoTimes));
                    if (StringUtils.isEmpty(interval_time)) {//间隔时间也为空
                        long one_day = (24 * 60 * 60 + 30 * 60) * 1000;
                        timerTasks = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = data;
                                handler.sendMessage(message);
                            }
                        };
                        long time_wait = s + one_day / 1000 + timeUtil.getCurrentTimeMilies() + times / 1000;
                        SPUtils.putString(context, "time_wait", time_wait * 1000 + "");
                        LogUtils.d("执行的是时间为" + todoTimes + "、设置间隔时间为空的情况下执行等待时间是" + s + "秒:执行周期的时间是" + times / 1000 + "秒:" + "下一个周期执行的预计时间是" + timeUtil.getTimesCuo(time_wait * 1000));
                        mytime.schedule(timerTasks, times * 1000 + s * 1000);

                    } else {
                        long r = (Long.parseLong(interval_time) * 60 * 60 + 30 * 60) * 1000;
                        LogUtils.d("Long.parseLong(interval_time)" + Long.parseLong(interval_time) + "秒" + "Long.parseLong(interval_time)*60*60" + Long.parseLong(interval_time) * 60 * 60);
                        timerTasks = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = data;
                                handler.sendMessage(message);
                            }
                        };
                        long time_wait = s + r / 1000 + timeUtil.getCurrentTimeMilies() + times;
                        SPUtils.putString(context, "time_wait", time_wait * 1000 + "");
                        LogUtils.d("执行的是时间为" + todoTimes + "、间隔时间为" + r / 1000 + "秒." + "情况下执行等待时间是" + s + "秒:" + "下一个周期预计执行的时间是" + timeUtil.getTimesCuo(time_wait * 1000));
                        mytime.schedule(timerTasks, times * 1000 + s * 1000);

                    }
                }
                return;
            }
            /**
             * //支付宝和微信的寻找手机任务
             */
            if (data.getTask_id() == 60 || data.getTask_id() == 37) {
                setBorad(data);
                return;
            }
            /**
             * 其他任务
             *
             */
            int max = 0;
            int min = 0;
            if (StringUtils.isEmpty(todoTimes)) {//没有设置时间，马上执行
                long time = 0;
                LogUtils.d("执行时间" + time);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        setBorad(data);

                    }
                };
                if (StringUtils.isEmpty(SPUtils.getString(context, "random_time_s", "")) && StringUtils.isEmpty(SPUtils.getString(context, "random_time_e", ""))) {
                    max = 200;
                    min = 0;
                } else {
                    max = Integer.parseInt(SPUtils.getString(context, "random_time_e", "").trim());
                    min = Integer.parseInt(SPUtils.getString(context, "random_time_s", "").trim());
                    LogUtils.d(max + "____" + min);
                }
                Random random = new Random();
                int s = random.nextInt(max) % (max - min + 1) + min;
                LogUtils.d("启动的时间是" + (time + s) * 1000 + "毫秒");
                timer.schedule(timerTask, (time + s) * 1000);
            } else {//定时执行
                long time = timeUtil.getLongTime(Long.parseLong(todoTimes));
                LogUtils.d("执行时间" + time);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        setBorad(data);
                    }
                };
                if (StringUtils.isEmpty(SPUtils.getString(context, "random_time_s", "")) && StringUtils.isEmpty(SPUtils.getString(context, "random_time_e", ""))) {
                    max = 200;
                    min = 0;
                } else {
                    max = Integer.parseInt(SPUtils.getString(context, "random_time_e", "").trim());
                    min = Integer.parseInt(SPUtils.getString(context, "random_time_s", "").trim());
                }
                Random random = new Random();
                int s = random.nextInt(max) % (max - min + 1) + min;
                LogUtils.d("启动的时间是" + (time + s) * 1000 + "毫秒");
                timer.schedule(timerTask, (time + s) * 1000);
            }
        }

    }

    /**
     * 发单图片下载
     *
     * @param dataBean
     */
    private void downImg(MessageListBean.ContentBean.DataBean dataBean) {

        if (StringUtils.isEmpty(dataBean.getParam().getMateria_ss())) {
            return;
        }
        String messageData = dataBean.getParam().getMateria_ss();

        WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
        if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans != null && wxFlockMessageBeans.length > 0) {
            LogUtils.d(wxFlockMessageBeans.length + "条信息");
            for (int b = 0; b < wxFlockMessageBeans.length; b++) {//图片下载
                if (wxFlockMessageBeans[b].getType().equals("img")) {
                    String imgUrl = wxFlockMessageBeans[b].getData();
                    if (!StringUtils.isEmpty(imgUrl)) {
                        downloadFile(imgUrl);
                    }
                }
            }

        }

    }

    /**
     * 打开应用. 应用在前台不处理,在后台就直接在前台展示当前界面, 未开启则重新启动
     */
    public static boolean openApplicationFromBackground(Context context) {
        Intent intent;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (!list.isEmpty() && list.get(0).topActivity.getPackageName().equals(context.getPackageName())) {
            //此时应用正在前台, 不作处理
            LogUtils.d("zplh在前台不处理");
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(context.getPackageName())) {
                LogUtils.d("zplh在运行不处理");
                return false;
            }
        }
        LogUtils.d("zplh在重新打开");
        intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        context.startActivity(intent);
        return true;
    }

    /**
     * 收到任务后反馈
     */
    public void updata_task_status(String log_id) {
        String uid = SPUtils.getString(context, "uid", "0000");
        RequestParams params = new RequestParams(URLS.updata_task_status());
        params.addQueryStringParameter("log_id", log_id);
        params.addQueryStringParameter("uid", uid);
        LogUtils.d(URLS.updata_task_status() + "?log_id=" + log_id + "&uid=" + uid);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<CheckImei>() {

            @Override
            public void onSuccess(CheckImei bean) {
                LogUtils.d("收到任务后反馈成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("收到任务后反馈失败:" + errorString);
            }
        });
    }


    /**
     * 图片下载
     *
     * @param messageData
     */
    private void downloadFile(String messageData) {

        String path = "";
        String strMark = "";
        String fileName = "";
        String filePath = "";
        String text = "";

        if (!StringUtils.isEmpty(messageData)) {//判断请求地址是否为空
            text = messageData;
        } else {
            LogUtils.d("x图文发布地址为空");
            return;
        }
        path = URLS.pic_vo_flock + text.replace("\\", "/");
        LogUtils.d("x文件url__" + path);
        strMark = text.replace("\\", "/");
        fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");
        LogUtils.d("xa" + fileName);
        filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + fileName;
        LogUtils.d("xb" + filePath);
        LogUtils.d("xc" + FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages"));

        String pathUrl = Environment.getExternalStorageDirectory() + "/ykimages/" + fileName;

        if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载
            LogUtils.d("x存在");
            return;
        } else {
            LogUtils.d("x不存在");
        }


        RequestParams requestParams = new RequestParams(path);
        requestParams.setSaveFilePath(pathUrl);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

                LogUtils.d((int) total + "");
                LogUtils.d((int) current + "");
            }

            @Override
            public void onSuccess(File result) {
                LogUtils.d("xutils文件下载成功");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                LogUtils.d("x下载失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

}
