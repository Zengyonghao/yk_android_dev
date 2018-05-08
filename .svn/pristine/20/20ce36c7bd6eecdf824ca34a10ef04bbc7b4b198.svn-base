package com.zplh.zplh_android_yk.ui.activity;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.easy.wtool.sdk.WToolSDK;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wanj.x007_common.util.ShellUtils;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseActivity;
import com.zplh.zplh_android_yk.base.BaseApplication;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.conf.MyConstains;
import com.zplh.zplh_android_yk.service.DeviceOutService;
import com.zplh.zplh_android_yk.ui.fragment.MessageFragment;
import com.zplh.zplh_android_yk.ui.fragment.OperationFragment;
import com.zplh.zplh_android_yk.ui.fragment.RecordFragment;
import com.zplh.zplh_android_yk.ui.fragment.RedactFragment;
import com.zplh.zplh_android_yk.utils.FileUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.NodeUtils;
import com.zplh.zplh_android_yk.utils.PhoneUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.StringUtils;
import com.zplh.zplh_android_yk.utils.TimeUtil;

import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by lichun on 2017/5/24.
 * Description:
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {


    private DrawerLayout mDrawerLayout;
    private MessageFragment messageFragment;
    private RecordFragment recordFragment;
    public OperationFragment operationFragment;
    private RedactFragment redactFragment;
    private TextView aTextView, bTextView, cTextView, dTextView;
    private static Context context;
    private PhoneUtils phoneUtils;
    private BaseApplication app;
    private Gson gson = new Gson();

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        phoneUtils = new PhoneUtils(context);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        aTextView = (TextView) findViewById(R.id.aTextView);
        bTextView = (TextView) findViewById(R.id.bTextView);
        cTextView = (TextView) findViewById(R.id.cTextView);
        dTextView = (TextView) findViewById(R.id.dTextView);
//        mTitle.setText("wx助手zfb发单v1");
//          mTitle.setText("wx助手wx发单v4");
//        mTitle.setText("wx助手beta_v " + wxUtils.getVersionName(mContext));
        mTitle.setText("wx助手v " + wxUtils.getVersionName(mContext));
        clickC();
        clickB();
//        Intent intent = new Intent(this, AlarmCountService.class);
//        startService(intent);
        Intent deviceOutIntent = new Intent(this, DeviceOutService.class);
        startService(deviceOutIntent);
//        Intent startIntent = new Intent(this, ShellService.class);
//        startService(startIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent stopIntent = new Intent(this, ShellService.class);
//        stopService(stopIntent);
        saveTask();
        new WToolSDK().unload();//释放发消息sdk
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.zplh.zplh_android_yk/com.zplh.zplh_android_yk.ui.activity.BindingActivity");
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    @Override
    protected void initEvents() {
        aTextView.setOnClickListener(this);
        bTextView.setOnClickListener(this);
        cTextView.setOnClickListener(this);
        dTextView.setOnClickListener(this);

    }

    WxUtils wxUtils = new WxUtils();

    @Override
    protected void initDatas() {
        SQLiteDatabase db = Connector.getDatabase();
        SPUtils.putBoolean(mContext, "isinit", false);
        app = (BaseApplication) getApplication();
        new Thread(new Runnable() {
            @Override
            public void run() {
                wxUtils.adb("input keyevent 82");//点亮屏幕
                String xmlData = wxUtils.getXmlData();
                if (xmlData.contains("电话") && xmlData.contains("解锁") && xmlData.contains("相机")) {
                    wxUtils.adb("input swipe 200 700 200 200 50");//解锁
                }

                wxUtils.getOpenWRITE(mContext, (MainActivity) mContext);//6.0获取权限
                try {
                    Thread.sleep(1000);
                    NodeUtils.clickNode("允许", "com.android.packageinstaller:id/permission_allow_button");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                    NodeUtils.clickNode("允许", "com.android.packageinstaller:id/permission_allow_button");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                    NodeUtils.clickNode("允许", "com.android.packageinstaller:id/permission_allow_button");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //region 获取通知访问权限
                if (!isEnabled()) {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();
                    List<String> stringList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < stringList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(stringList.get(a)).getNode();
                        if ("NotificationMonitor".equals(nodeBean.getText())) {
                            NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(stringList.get(a + 1)).getNode();
                            List<Integer> listXY = wxUtils.getXY(nodeBean1.getBounds());//
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击权限
                        }
                    }
                    if (wxUtils.getXmlData().contains("要允许NotificationMonitor获取通知访问权限吗？")) {
                        NodeUtils.clickNode("允许", "android:id/button1");
                        wxUtils.adb("input keyevent 4");
                    }
                }
                //endregion
                //region 申请辅助点击权限
                if (!isAccessibilitySettingsOn(MainActivity.this)) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        List<String> li = wxUtils.getNodeList(wxUtils.getXmlData());
                        for (String l : li) {
                            if (l.contains(getAppName(MainActivity.this))) {
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(l).getNode();
                                int[] xy = wxUtils.getLinkedXY(nodeBean.getBounds());
                                wxUtils.adbClick(xy[0], xy[1], xy[2], xy[3]);
                                List<String> li1 = wxUtils.getNodeList(wxUtils.getXmlData());
                                int i = 0;
                                for (String str : li1) {
                                    if (str.contains("关闭")) {
                                        i = -1;
                                        NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(str).getNode();
                                        int[] xy1 = wxUtils.getLinkedXY(nodeBean1.getBounds());
                                        wxUtils.adbClick(xy1[0], xy1[1], xy1[2], xy1[3]);

                                        List<String> li2 = wxUtils.getNodeList(wxUtils.getXmlData());
                                        for (String s1 : li2) {
                                            if (s1.contains("确定")) {
                                                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(s1).getNode();
                                                int[] xy2 = wxUtils.getLinkedXY(nodeBean2.getBounds());
                                                wxUtils.adbClick(xy2[0], xy2[1], xy2[2], xy2[3]);
                                                wxUtils.adb("input keyevent 4");
                                                wxUtils.adb("input keyevent 4");
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                if (i == 0) {
                                    wxUtils.adb("input keyevent 4");
                                    wxUtils.adb("input keyevent 4");
                                }
                                break;
                            }
                        }
                    }
                }
                //endregion
/*                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);  //无障碍
                startActivity(intent);*/
                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                //region 删除aa开头文件文件
                if (FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages")) {
                    String fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
                    File folder = new File(fileUrl);
                    File[] files = folder.listFiles();
                    for (File file : files) {
                        LogUtils.d("删除了:" + file.getName());
                        file.delete();
                    }
                }
                //endregion
                SPUtils.putBoolean(mContext, "isinit", true);
                startTask();
            }
        }).start();


    }

    public String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + AccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            // com.z.buildingaccessibilityservices/com.z.buildingaccessibilityservices.TestService
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        } else {
        }
        return false;
    }


    // 判断是否打开了通知监听权限
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            if (names!=null && names.length!=0){
                for (int i = 0; i < names.length; i++) {
                    final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                    if (cn != null) {
                        if (TextUtils.equals(pkgName, cn.getPackageName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aTextView:
                clickA();
                break;
            case R.id.bTextView:
                clickB();
                break;
            case R.id.cTextView:
                clickC();
                break;
            case R.id.dTextView:
                clickD();
                break;
        }
    }

    public void clickA() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //打开手势滑动
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
            fragmentTransaction.add(R.id.fragmentLayout, messageFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.show(messageFragment);
            fragmentTransaction.commit();
        }
        aTextView.setEnabled(false);
    }

    public void clickB() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //打开手势滑动
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        if (recordFragment == null) {
            recordFragment = new RecordFragment();
            fragmentTransaction.add(R.id.fragmentLayout, recordFragment);
            fragmentTransaction.commit();
        } else {
            recordFragment = null;
            recordFragment = new RecordFragment();
            fragmentTransaction.add(R.id.fragmentLayout, recordFragment);
            fragmentTransaction.commit();
//            fragmentTransaction.show(recordFragment);
//            fragmentTransaction.commit();
        }
        bTextView.setEnabled(false);
    }

    private void clickC() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        if (operationFragment == null) {
            operationFragment = new OperationFragment();
            fragmentTransaction.add(R.id.fragmentLayout, operationFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.show(operationFragment);
            fragmentTransaction.commit();
        }
        cTextView.setEnabled(false);
    }


    private void clickD() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        if (redactFragment == null) {
            redactFragment = new RedactFragment();
            fragmentTransaction.add(R.id.fragmentLayout, redactFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.show(redactFragment);
            fragmentTransaction.commit();
        }
        dTextView.setEnabled(false);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (recordFragment != null) {
            transaction.hide(recordFragment);
        }

        if (operationFragment != null) {
            transaction.hide(operationFragment);
        }
        if (redactFragment != null) {
            transaction.hide(redactFragment);
        }

        aTextView.setEnabled(true);
        bTextView.setEnabled(true);
        cTextView.setEnabled(true);
        dTextView.setEnabled(true);
    }

    //----------------------------------------关于任务-------------------------------------------------------------


    /**
     * 保存任务
     */
    public void saveTask() {
        List<MessageListBean.ContentBean.DataBean> dataBeanList = app.getDataBeanList();
        String saveTaskData = gson.toJson(dataBeanList);

        SPUtils.putString(mContext, "saveTaskData", saveTaskData);
        LogUtils.d("保存saveTaskData:" + saveTaskData);
    }


    /**
     * 打开app检查是否有要执行的任务
     */
    public void startTask() {
        String saveTaskData = SPUtils.getString(mContext, "saveTaskData", "");
        LogUtils.d("获取saveTaskData:" + saveTaskData);

        List<MessageListBean.ContentBean.DataBean> dataBeanList = gson.fromJson(saveTaskData, new TypeToken<List<MessageListBean.ContentBean.DataBean>>() {
        }.getType());


        if (dataBeanList != null && dataBeanList.size() > 0) {
//            ShowToast.show("还有"+dataBeanList.size()+"个任务待执行",(Activity) mContext);
            LogUtils.d("saveTaskData任务长度" + dataBeanList.size());
//            wxUtils.saveTask(mContext,dataBeanList);//保存到本地
            for (int a = 0; a < dataBeanList.size(); a++) {
                app.getDataBeanList().add(dataBeanList.get(a));//保存任务
                if (!StringUtils.isEmpty(dataBeanList.get(a).getTodo_time())) {
                    if (timeUtil.getCurrentTimeMilies() >= Long.valueOf(dataBeanList.get(a).getTodo_time())) {//如果时间过了，马上执行
                        LogUtils.d("时间过了，马上执行");
                        dataBeanList.get(a).setTodo_time("");
                    }
                }
                taskTime(dataBeanList.get(a));
            }

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
        if (rangeList != null && (rangeList.size() == 0 || (rangeList.size() > 0 && rangeList.toString().contains(SPUtils.getString(context, "uid", "0000"))))) {
            //网络请求 判断该lod_id任务是否取消 取消则不在往下进行
            if (data.getTask_id() == 61 || data.getTask_id() == 59 || data.getTask_id() == 25 || data.getTask_id() == 54 || data.getTask_id() == 52 || data.getTask_id() == 53 || data.getTask_id() == 27 || data.getTask_id() == 28 || data.getTask_id() == 30 || data.getTask_id() == 32 || data.getTask_id() == 33 || data.getTask_id() == 55 || data.getTask_id() == 56 || data.getTask_id() == 57 || data.getTask_id() == 58 || data.getTask_id() == 60 || data.getTask_id() == 37) {//微信统计任务不需要加随机时间
                if (StringUtils.isEmpty(todoTimes)) {//没有设置时间，马上执行
                    setBorad(data);
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
                    LogUtils.d("启动的时间是" + (time) * 1000 + "毫秒");
                    timer.schedule(timerTask, (time) * 1000);
                }
                return;
            }

            if (data.getTask_id() == 60 || data.getTask_id() == 37) {//支付宝和微信的寻找手机任务
                setBorad(data);
                return;
            }
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

    private TimeUtil timeUtil = new TimeUtil();

    /**
     * 发送任务
     *
     * @param data
     */
    private void setBorad(final MessageListBean.ContentBean.DataBean data) {
        Intent intent2 = new Intent();
        intent2.setAction(MyConstains.Broadcast_Task);
        Bundle bundle = new Bundle();
        bundle.putSerializable("messageBean", data);
        intent2.putExtras(bundle);
        context.sendBroadcast(intent2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SPUtils.putBoolean(app.getApplicationContext(), "chitchat", true);//双向互聊初始化
    }

    //------------------------------------------任务--------------------------------------------------------

}
