package com.zplh.zplh_android_yk.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanj.x007_common.util.ShellUtils;
import com.yk.core.assembly.AssExecutionProcess;
import com.zplh.zplh_android_yk.BuildConfig;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.Threads.SingleThread;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseApplication;
import com.zplh.zplh_android_yk.base.BaseFragment;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.bean.CheckImei;
import com.zplh.zplh_android_yk.bean.LogidBean;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.StateRenwuBean;
import com.zplh.zplh_android_yk.bean.TaskLogIdBean;
import com.zplh.zplh_android_yk.bean.WxFlockMessageBean;
import com.zplh.zplh_android_yk.bean.WxGeneralSettingsBean;
import com.zplh.zplh_android_yk.conf.MyConstains;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.conf.ZFB_URLS;
import com.zplh.zplh_android_yk.db.StateDao;
import com.zplh.zplh_android_yk.eventbus.EventCenter;
import com.zplh.zplh_android_yk.httpcallback.GsonUtil;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.presenter.OldOperationAlipayPresenter;
import com.zplh.zplh_android_yk.presenter.OperationAlipayPresenter;
import com.zplh.zplh_android_yk.presenter.OperationPresenter;
import com.zplh.zplh_android_yk.service.MyVoiceAndVideoService;
import com.zplh.zplh_android_yk.ui.view.OperationView;
import com.zplh.zplh_android_yk.utils.FileUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.PhoneUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;
import com.zplh.zplh_android_yk.utils.StringUtils;
import com.zplh.zplh_android_yk.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.zplh.zplh_android_yk.service.MyHangUpService.HANGUP;


/**
 * Created by lichun on 2017/5/31.
 * Description:操作
 */


public class OperationFragment extends BaseFragment implements View.OnClickListener, OperationView {
    private boolean isNormal = true;
    private TextView aTextView, bTextView, cTextView, dTextView, eTextView, fTextView, gTextView, hTextView, iTextView, jTextView, kTextView, lTextView, mTextView, nTextView, oTextView, zfbATextView, zfbBTextView, zfbCTextView, again_task_tv;
    private View view;
    private OperationPresenter operationPresenter;
    private OperationAlipayPresenter operationAlipayPresenter;
    private WxUtils wxUtils = new WxUtils();
    private TaskClientBroadcastRecv m_client;
    private StateDao stateDao;
    private BaseApplication app;
    private PhoneUtils phoneUtils;
    private URLS urls;
    private OldOperationAlipayPresenter oldOperationAlipayPresenter;
    private int version_ali = 0;//支付宝新旧版本区分
    private String uid;
    private String alipay_statu = "z";//支付宝搜索加好友的执行完成状态
    private Intent intent;
    private String login_ret = "";
    private ZFB_URLS zfb_urls;
    private int blockType;//初始化类型
    //    private MyList_Dao myList_dao;
    List<MessageListBean.ContentBean.DataBean> dataBeanList;//保存任务，关闭app打开后执行剩下的任务
    private TimeUtil timeUtil;
    //    private List<My_list_da_bean>list_da_beans;
    private String newsLog_ids;
    private String[] s1;
    private String s2 = "";
    private boolean state = false;
    private int zfbAccount = 0;
    private int zfbQunAccount = 0;
    private TextView mMessageStatistics;
    private TextView mWxNewFriendsToQun;


    /**
     * 接收推送过来的广播
     */
    class TaskClientBroadcastRecv extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strAction = intent.getAction();
            if (strAction.equals(MyConstains.Broadcast_Task)) {
                MessageListBean.ContentBean.DataBean dataBean = (MessageListBean.ContentBean.DataBean) intent.getSerializableExtra("messageBean");
                login_ret = "";
                LogUtils.d("WG" + dataBean.toString());
                HttpLog_id(dataBean);
                //如果返回过来的是200 则该任务停止执行任务 等待下一个任务传递过来执行
                version_ali = dataBean.getParam().getVersion();

            } else if (strAction.equals(MyConstains.Broadcast_Task_TWO)) {
                MessageListBean.ContentBean.DataBean dataBean = (MessageListBean.ContentBean.DataBean) intent.getSerializableExtra("messageBean");
                LogUtils.d("新的new_id_login是" + SPUtils.getString(mContext, "newsLog_id", ""));
                SPUtils.putString(mContext, "newsLog_id", "");
                HttpLog_id(dataBean);

            }
        }
    }


    @Override
    public void onEventComming(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == 24) {//如果event是24 表示执行统计任务
            if (operationPresenter != null) {
                ShowToast.show("将定时执行统计任务", (Activity) mContext);
                SPUtils.putInt(mContext, "is_accType", 3);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        operationPresenter.pushTask(24, "", "", "");
                    }
                }).start();
            }
        } else if (eventCenter.getEventCode() == 99) {
            String uid = SPUtils.getString(getContext(), "uid", "0000");
            operationPresenter.checkoutDevice(uid);
        } else if (eventCenter.getEventCode() == HANGUP) {
            Log.d("zhangshuai", "4377");
            wxUtils.adbWxClick(370, 735);
            wxUtils.adbWxClick(350, 530); //点击确定
            SPUtils.putString(mContext, "StartVoiceAndVideoService", "0");
            Intent intentFour = new Intent(getContext(), MyVoiceAndVideoService.class);
            getContext().stopService(intentFour);

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mContext.unregisterReceiver(m_client);
        operationPresenter.unReceiver();
        operationAlipayPresenter = null;
        oldOperationAlipayPresenter = null;
    }

    @Override
    protected BasePresenter createPresenter() {
        return operationPresenter;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        intent = getActivity().getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        uid = SPUtils.getString(getContext(), "uid", "0000");
        app = (BaseApplication) getActivity().getApplication();
        zfb_urls = new ZFB_URLS();
        dataBeanList = app.getDataBeanList();//初始化任务数据
        LogUtils.d("zplh初始化了哦....................................................");
        stateDao = new StateDao(getContext());
        // myList_dao=new MyList_Dao(getContext());
        phoneUtils = new PhoneUtils(getContext());
        timeUtil = new TimeUtil();
        urls = new URLS();
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText("你好。");
        oldOperationAlipayPresenter = new OldOperationAlipayPresenter(mContext, this);
        operationPresenter = new OperationPresenter(mContext, this);
        operationAlipayPresenter = new OperationAlipayPresenter(mContext, this);
        m_client = new TaskClientBroadcastRecv();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyConstains.Broadcast_Task);
        filter.addAction(MyConstains.Broadcast_Task_TWO);
        mContext.registerReceiver(m_client, filter);

    }


    @Override
    protected View initViews() {
        view = View.inflate(mContext, R.layout.fragment_operation, null);
        //   aTextView = (TextView) view.findViewById(R.id.aTextView);
        bTextView = (TextView) view.findViewById(R.id.bTextView);
        cTextView = (TextView) view.findViewById(R.id.cTextView);
        dTextView = (TextView) view.findViewById(R.id.dTextView);
        //  eTextView = (TextView) view.findViewById(R.id.eTextView);

        fTextView = (TextView) view.findViewById(R.id.fTextView);
        gTextView = (TextView) view.findViewById(R.id.gTextView);
        hTextView = (TextView) view.findViewById(R.id.hTextView);
        iTextView = (TextView) view.findViewById(R.id.iTextView);
        jTextView = (TextView) view.findViewById(R.id.jTextView);
        kTextView = (TextView) view.findViewById(R.id.kTextView);
        lTextView = (TextView) view.findViewById(R.id.lTextView);
        mTextView = (TextView) view.findViewById(R.id.mTextView);
        nTextView = (TextView) view.findViewById(R.id.nTextView);
        oTextView = (TextView) view.findViewById(R.id.oTextView);
        zfbATextView = (TextView) view.findViewById(R.id.zfbATextView);
        zfbBTextView = (TextView) view.findViewById(R.id.zfbBTextView);
        zfbCTextView = (TextView) view.findViewById(R.id.zfbCTextView);
        again_task_tv = (TextView) view.findViewById(R.id.again_task_tv);
        mMessageStatistics = (TextView) view.findViewById(R.id.mMessageStatistics);
        mWxNewFriendsToQun = (TextView) view.findViewById(R.id.mWxNewFriendsToQun);
        return view;
    }

    @Override
    protected void initEvents() {
        bTextView.setOnClickListener(this);
        cTextView.setOnClickListener(this);
        dTextView.setOnClickListener(this);
        fTextView.setOnClickListener(this);
        gTextView.setOnClickListener(this);
        hTextView.setOnClickListener(this);
        iTextView.setOnClickListener(this);
        jTextView.setOnClickListener(this);
        kTextView.setOnClickListener(this);
        lTextView.setOnClickListener(this);
        mTextView.setOnClickListener(this);
        nTextView.setOnClickListener(this);
        oTextView.setOnClickListener(this);
        zfbATextView.setOnClickListener(this);
        zfbBTextView.setOnClickListener(this);
        zfbCTextView.setOnClickListener(this);
        again_task_tv.setOnClickListener(this);
        mMessageStatistics.setOnClickListener(this);
        mWxNewFriendsToQun.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.aTextView://建群
                break;

            case R.id.bTextView://拉群
                task(0);
                task(1);
                break;

            case R.id.cTextView://判断男女，修改备注
                //                task(0);
                MessageListBean.ContentBean.DataBean dataBean = new MessageListBean.ContentBean.DataBean();
                dataBean.setTask_id(0);
                AssExecutionProcess.InitAssembyProcess(mContext, dataBean);
                break;
            case R.id.dTextView:
                showToast("当前版本号:" + wxUtils.getVersionName(mContext));
                break;
            case R.id.eTextView://打开附近的人
                //                task(2);
                break;
            case R.id.fTextView://自动通过好友申请
                task(3);
                break;
            case R.id.gTextView://获取通讯录添加好友
                task(4);
                break;
            case R.id.hTextView://搜索添加好友
                task(5);
                break;
            case R.id.iTextView://微信群保存到通讯录
                //                task(6);
                break;
            case R.id.jTextView://版本更新
                //                task(7);
                updata();
                break;
            case R.id.kTextView://朋友圈发布
                task(8);
                break;
            case R.id.lTextView://朋友圈点赞
                task(9);
                break;
            case R.id.mTextView://进入购物
                task(10);
                break;
            case R.id.nTextView://进入游戏
                task(11);
                break;
            case R.id.oTextView://查看设备号
                showToast("设备编号:" + SPUtils.getString(mContext, "uid", "0000"));
                break;
            case R.id.mMessageStatistics://微信统计
                operationPresenter.pushTask(24, "", "", "");
                break;
            case R.id.mWxNewFriendsToQun://拉新好友进群
                operationPresenter.pushTask(45, "", "", "");
                //                operationPresenter.pushTask(40,"dyfu43555","","");
                break;


            //----------------------支付宝--------------------------------------
            case R.id.zfbATextView://
                task(-1);
                break;
            case R.id.zfbBTextView://修改备注
                task(-2);
                break;
            case R.id.zfbCTextView://拉群
                task(-3);
                break;
            case R.id.again_task_tv://重启任务
                againTask();
                break;

        }

    }

    //重启任务的逻辑
    private void againTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("请输入logid，请求任务数据！");
        builder.setCancelable(false);
        final EditText et = new EditText(mContext);
        et.setHint("请输入logid");
        builder.setView(et);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                final String logid = et.getText().toString().trim();
                if (TextUtils.isEmpty(logid)) {
                    ShowToast.show("logid不能为空", (Activity) mContext);
                    return;
                }
                try {
                    Integer.valueOf(logid);
                } catch (Exception ex) {
                    ShowToast.show("logid只能为数字", (Activity) mContext);
                    return;
                }
                arg0.dismiss();
                new Thread() {
                    @Override
                    public void run() {
                        RequestParams params = new RequestParams(zfb_urls.getTaskByLogid());//url
                        params.addQueryStringParameter("log_id", logid);

                        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
                            @Override
                            public void onSuccess(LogidBean bean) {

                                String data = bean.getData();
                                MessageListBean.ContentBean.DataBean.ParamBean paramBean = GsonUtil.parseJsonWithGson(data, MessageListBean.ContentBean.DataBean.ParamBean.class);
                                MessageListBean.ContentBean.DataBean dataBean = new MessageListBean.ContentBean.DataBean();
                                dataBean.setLog_id(logid); //设置logid
                                dataBean.setParam(paramBean);
                                dataBean.setTask_id(50);//只有支付宝循环加粉，需要这个需求。
                                again(dataBean, 1);
                                ShowToast.show("请求成功", (Activity) mContext);
                                //alertDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int errorCode, String errorString) {
                                ShowToast.show("请求失败", (Activity) mContext);
                            }
                        });
                    }
                }.start();

            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private boolean uncaughtExceptionMark = true;//崩溃重启初始化

    /**
     * 执行收到的任务
     *
     * @param dataBean
     */
    public void task(final MessageListBean.ContentBean.DataBean dataBean) {//执行收到的任务

        new Thread(new Runnable() {
            @Override
            public void run() {
                dataBeanList = app.getDataBeanList();//初始化任务数据
                LogUtils.d("zplh还有" + dataBeanList.size() + "个任务");

                if (SPUtils.getBoolean(mContext, "isinit", false)) {//判断是否获取完权限
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                switch (dataBean.getTask_id()) {
                    case 27://通用设置
                        MessageListBean.ContentBean.DataBean.ParamBean param = dataBean.getParam();
                        WxGeneralSettingsBean wxGeneralSettingsBean = new WxGeneralSettingsBean();

                        wxGeneralSettingsBean.setAdd_interval_time_e(param.getAdd_interval_time_e());
                        wxGeneralSettingsBean.setAdd_interval_time_s(param.getAdd_interval_time_s());
                        wxGeneralSettingsBean.setAgree_interval_time_e(param.getAgree_interval_time_e());
                        wxGeneralSettingsBean.setAgree_interval_time_s(param.getAgree_interval_time_s());
                        wxGeneralSettingsBean.setMsg_interval_time_e(param.getMsg_interval_time_e());
                        wxGeneralSettingsBean.setMsg_interval_time_s(param.getMsg_interval_time_s());
                        wxGeneralSettingsBean.setRandom_time_e(param.getRandom_time_e());
                        wxGeneralSettingsBean.setRandom_time_s(param.getRandom_time_s());
                        wxGeneralSettingsBean.setRemark_interval_time_e(param.getRemark_interval_time_e());
                        wxGeneralSettingsBean.setRemark_interval_time_s(param.getRemark_interval_time_s());
                        wxGeneralSettingsBean.setTask_time_e(param.getTask_time_e());
                        wxGeneralSettingsBean.setTask_time_s(param.getTask_time_s());
                        wxGeneralSettingsBean.setDz_interval_e(param.getDz_interval_e());
                        wxGeneralSettingsBean.setDz_interval_s(param.getDz_interval_s());
                        wxGeneralSettingsBean.setVideo_time_e(param.getVideo_time_e());
                        wxGeneralSettingsBean.setVideo_time_s(param.getVideo_time_s());
                        wxGeneralSettingsBean.setVoice_time_e(param.getVoice_time_e());
                        wxGeneralSettingsBean.setVoice_time_s(param.getVoice_time_s());

                        wxGeneralSettingsBean.setCrowd_ad_time_e(param.getCrowd_ad_time_e());
                        wxGeneralSettingsBean.setCrowd_ad_time_s(param.getCrowd_ad_time_s());
                        wxGeneralSettingsBean.setRecord_time_e(param.getRecord_time_e());
                        wxGeneralSettingsBean.setRecord_time_s(param.getRecord_time_s());
                        app.setWxGeneralSettingsBean(wxGeneralSettingsBean);//保存通用设置到application
                        operationPresenter.setApp(app);
                        SPUtils.putString(mContext, "msg_interval_time_e", param.getMsg_interval_time_e());//发消息间隔
                        SPUtils.putString(mContext, "msg_interval_time_s", param.getMsg_interval_time_s());
                        SPUtils.putString(mContext, "random_time_e", param.getRandom_time_e());
                        SPUtils.putString(mContext, "random_time_s", param.getRandom_time_s());
                        SPUtils.putString(mContext, "remark_interval_time_e", param.getRemark_interval_time_e());//改备注间隔
                        SPUtils.putString(mContext, "remark_interval_time_s", param.getRemark_interval_time_s());
                        SPUtils.putString(mContext, "task_time_e", param.getTask_time_e());//任务间隔
                        SPUtils.putString(mContext, "task_time_s", param.getTask_time_s());
                        SPUtils.putString(mContext, "dz_interval_e", param.getDz_interval_e());//点赞间隔
                        SPUtils.putString(mContext, "dz_interval_s", param.getDz_interval_s());

                        SPUtils.putString(mContext, "video_time_e", param.getVideo_time_e());
                        SPUtils.putString(mContext, "video_time_s", param.getVideo_time_s());
                        SPUtils.putString(mContext, "voice_time_e", param.getVoice_time_e());
                        SPUtils.putString(mContext, "voice_time_s", param.getVoice_time_s());

                        StatuRequest(dataBean);
                        ShowToast.show("通用设置完成", (Activity) mContext);
                        return;

                }


                SingleThread.getInstance().threadPoolExecute(new Runnable() {
                    @Override
                    public void run() {
                        for (int a = 0; a < dataBeanList.size(); a++) {
                            if (dataBeanList.get(a).getLog_id().equals(dataBean.getLog_id())) {
                                //if(dataBean.getTask_id()!=50) {
                                dataBeanList.remove(a);
                                // }
                            }
                        }
                        LogUtils.d("zplh删除后还有" + dataBeanList.size() + "个任务");
                        wxUtils.saveTask(mContext, dataBeanList);//保存到本地
                        //获取数据库数据判断任务是否执行过
                        List<TaskLogIdBean> taskLogIdBeanList = DataSupport.where("logidtask = ?", dataBean.getLog_id()).find(TaskLogIdBean.class);
                        LogUtils.d("任务是否重复" + taskLogIdBeanList.size());
                        if (taskLogIdBeanList.size() > 0) {
                            LogUtils.d("任务重复" + taskLogIdBeanList.size(), "不执行该任务");
                            return;
                        }
                        log_id = Integer.parseInt(dataBean.getLog_id());
                        operationPresenter.backMark = true;//初始化登录微信
                        SPUtils.putBoolean(mContext, "chitchat", false);//双向互聊初始化
                        //                    //保存任务到数据库避免重复执行
                        //                    TaskLogIdBean taskLogIdBean = new TaskLogIdBean();
                        //                    taskLogIdBean.setLogIdTask(dataBean.getLog_id());
                        //                    LogUtils.e("添加数据库" + taskLogIdBean.save() + "内容是:" + dataBean.getLog_id());

                        //                if (dataBean.getTask_id()==50){
                        //                    getXunhuan(dataBean);
                        //                    ShowToast.show("正在生成循环任务,请稍后",getActivity());
                        //                    try {
                        //                        Thread.sleep(10000);
                        //                    } catch (InterruptedException e) {
                        //                        e.printStackTrace();
                        //                    }
                        //                }

                        StateRenwuBean stateRenwuBean = new StateRenwuBean(dataBean.getTask_id(), log_id, "", getDtae());
                        stateDao.updatePerson(stateRenwuBean);
                        wxUtils.adb("input keyevent 82");//点亮屏幕
                        String xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("电话") && xmlData.contains("解锁") && xmlData.contains("相机")) {
                            wxUtils.adb("input swipe 200 700 200 200 50");//解锁
                        }

                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        wxUtils.openApplicationFromBackground(mContext);//收到任务，打开app

                        //获取新增类型的字符串数组

                        s1 = dataBean.getParam().getIs_newAdd();
                        StringBuffer sb = new StringBuffer();
                        if (s1 == null) {
                            s2 = null;
                        } else {
                            for (int i = 0; i < s1.length; i++) {
                                sb.append(s1[i]);
                            }
                            s2 = sb.toString();
                        }
                        if (uncaughtExceptionMark)//崩溃重启
                            uncaughtException();
                        uncaughtExceptionMark = false;

                        isLogin = true;
                        String[] str = new String[1];
                        if (dataBean.getParam().getIs_accType() == null) {
                            str[0] = "3";
                        } else {
                            str[0] = dataBean.getParam().getIs_accType()[0];
                        }
                        int is_accType = Integer.parseInt(str[0]);
                        SPUtils.putInt(mContext, "is_accType", is_accType);//发送任务的帐号的类型（1为新号 2为老号 3为全部）
                        SPUtils.putString(mContext, "taskId", "" + dataBean.getTask_id());
                        SPUtils.putString(mContext, "StartVoiceAndVideoService", "0");
                        Intent intent_zs;
                        SPUtils.putString(mContext, "QunPersonNum", "0");
                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        switch (dataBean.getTask_id()) {
                            case 1://朋友圈点赞
                                operationPresenter.pushTask(9, "", "", "");
                                break;
                            case 2://朋友圈图文发布
                                operationPresenter.pushTask(8, "1", dataBean.getParam().getMateria_ss(), dataBean.getParam().getSs_comment());//将评论参数传入到任务中
                                break;
                            case 3://朋友圈分享链接
                                operationPresenter.pushTask(17, dataBean.getParam().getMateria_url(), dataBean.getParam().getSend_type(), dataBean.getParam().getSend_text());
                                break;
                            case 4://搜索加好友
                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                                operationPresenter.pushTask(5, "", "", "");
                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                                break;
                            case 5://通讯录加好友
                                if (dataBean.getParam().getGender() == null) {
                                    operationPresenter.setGender("3");
                                } else {
                                    operationPresenter.setGender(dataBean.getParam().getGender());
                                }
                                operationPresenter.pushTask(4, "", "", "");
                                break;
                            case 6://嗅探加好友
                                ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.wanj.x007/com.wanj.x007.MainActivity");

                                //设置加好友数
                                int start;
                                if (StringUtils.isEmpty(dataBean.getParam().getDay_add_num_s())) {
                                    start = 300;
                                } else {
                                    start = Integer.valueOf(dataBean.getParam().getDay_add_num_s());
                                }
                                int end;
                                if (StringUtils.isEmpty(dataBean.getParam().getDay_add_num_e())) {
                                    end = 600;
                                } else {
                                    end = Integer.valueOf(dataBean.getParam().getDay_add_num_s());
                                }
                                int timeSleep = new Random().nextInt(end - start + 1) + start;
                                LogUtils.d("自助加好友数" + timeSleep);
                                //                        showToast("自助加好友数" + timeSleep + "个");
                                operationPresenter.pushTask(7, dataBean.getParam().getSniffing_type(), timeSleep + "", dataBean.getParam().getContact_verify_msg());
                                try {
                                    Thread.sleep(1000 * 60 * 2 * timeSleep);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                break;
                            case 7://好友发消息（群发和逐个发）
                                operationPresenter.pushTask(18, dataBean.getParam().getMateria_msg(), dataBean.getParam().getDesignated(), s2);
                                break;
                            case 8://好友发图片(群发和逐个发)
                                operationPresenter.pushTask(19, dataBean.getParam().getMateria_pic(), dataBean.getParam().getDesignated(), s2);
                                break;
                            case 9://好友逐个发消息
                                operationPresenter.pushTask(18, dataBean.getParam().getMateria_msg(), dataBean.getParam().getDesignated(), "");
                                break;
                            case 10://好友逐个发图片
                                operationPresenter.pushTask(19, dataBean.getParam().getMateria_pic(), dataBean.getParam().getDesignated(), "");
                                break;
                            case 11://微信群发消息
                                operationPresenter.pushTask(21, dataBean.getParam().getMateria_msg(), "", "");
                                break;
                            case 12://微信群发图片
                                operationPresenter.pushTask(22, dataBean.getParam().getMateria_pic(), "", "");
                                break;
                            case 13://微信群发名片//TODO
                                break;
                            case 14://自动通过好友申请
                                operationPresenter.pushTask(3, "", "", "");
                                break;
                            case 15://统计好友数量
                                break;
                            case 16://修改个性签名
                                break;
                            case 17://发送公众号名片
                                break;
                            case 18://朋友圈启动游戏
                                operationPresenter.pushTask(11, "", "", "");
                                break;
                            case 19://朋友圈启动京东购物
                                operationPresenter.pushTask(10, "", "", "");
                                break;
                            case 20://朋友圈小视频发布
                                operationPresenter.pushTask(8, "2", dataBean.getParam().getMateria_vedio(), "");
                                break;
                            case 21://好友发视频
                                operationPresenter.pushTask(20, dataBean.getParam().getMateria_vedio(), dataBean.getParam().getDesignated(), s2);
                                break;
                            case 22://微信群发视频
                                operationPresenter.pushTask(23, dataBean.getParam().getMateria_vedio(), "", "");
                                break;
                            case 23://嗅探加好友  TODO和6重复了
                                break;
                            case 24://拉群
                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                                operationPresenter.task(0);
                                operationPresenter.task(1);
                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                                break;
                            case 25://统计好友 和 群数量
                                operationPresenter.pushTask(24, "", "", "");
                                break;
                            case 26://获取后台传递过来的手机号码并进行添加
                                operationPresenter.pushTask(14, "", "", "");
                                break;
                            case 28://手机设置
                                phoneUtils.SettingPhone(dataBean);
                                break;
                            case 29://自定义修改备注
                                operationPresenter.pushTask(0, dataBean.getParam().getRemark(), "", "");
                                break;
                            case 30://微信群发图文
                                if (true) {
                                    if (StringUtils.isEmpty(dataBean.getParam().getMateria_ss())) {
                                        showToast("数据有误，任务失败");
                                        return;
                                    }

                                    if (StringUtils.isEmpty(dataBean.getTodo_time())) {
                                        try {
                                            Thread.sleep(25000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    String messageData = dataBean.getParam().getMateria_ss();

                                    WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
                                    boolean bl = true;
                                    if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans != null && wxFlockMessageBeans.length > 0) {
                                        LogUtils.d(wxFlockMessageBeans.length + "条信息");
                                        for (int b = 0; b < wxFlockMessageBeans.length; b++) {//图片下载
                                            if (wxFlockMessageBeans[b].getType().equals("img")) {
                                                String imgUrl = wxFlockMessageBeans[b].getData();
                                                if (!StringUtils.isEmpty(imgUrl)) {
                                                    downloadFile(imgUrl);
                                                    if (bl)
                                                        ShowToast.show("图片下载中...", (Activity) mContext);
                                                    bl = false;
                                                }
                                            }
                                        }
                                        try {
                                            Thread.sleep(25000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        operationPresenter.pushTask(15, dataBean.getParam().getMateria_ss(), "a" + uid, "");

                                    } else {
                                        ShowToast.show("数据有误", (Activity) mContext);
                                        return;
                                    }
                                }

                                break;
                            case 31://养号互撩
                                operationPresenter.pushTask(16, dataBean.getParam().getType(), "", "");
                                break;
                            case 32://手机初始化任务
                                phoneUtils.wifi();//手机相关设置的初始化
                                ShowToast.show("微信手机初始化任务全部执行完毕！", (Activity) getContext());
                                break;
                            case 33:
                                phoneUtils.SettingPhoneOpen(dataBean);//微信定时开关机
                                ShowToast.show("手机定时开关机设置完成", (Activity) getContext());
                                break;
                            case 43://统计微信好友的信息
                                operationPresenter.pushTask(31, "", "", "");
                                ShowToast.show("统计微信好友信息完成！", (Activity) getContext());
                                break;
                            case 44:// 微信群  好友转发
                                operationPresenter.pushTask(32, "", dataBean.getParam().getCrowd(), dataBean.getParam().getCount());
                                ShowToast.show(" 微信群 好友转发 完成！", (Activity) getContext());
                                break;
                            case 45:// 微信群  群转发
                                operationPresenter.pushTask(33, "", dataBean.getParam().getCrowd(), dataBean.getParam().getCount());
                                ShowToast.show(" 微信群 群转发 完成！", (Activity) getContext());
                                break;
                            case 46:// 二维码拉好友进群
                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                                String personNum2 = dataBean.getParam().getPersonNum();
                                SPUtils.putString(mContext, "QunPersonNum", personNum2);
                                operationPresenter.pushTask(34, "", "", "");
                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                                break;
                            case 47:// 自定义拉群
                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                                operationPresenter.pushTask(35, dataBean.getParam().getFanType(), dataBean.getParam().getFanId(),
                                        dataBean.getParam().getAddGroupNum() + "@" + dataBean.getParam().getGroupType() + "@" + dataBean.getParam().getPreGrpName() + "@" + dataBean.getParam().getPersonNum());
                                break;
                            case 48:// 阅读未读信息并回复
                                operationPresenter.pushTask(37, "", "", "");
                                //                                operationPresenter.pushTask(37, "", "", "");
                                break;
                            case 41: // 语音互聊（双向互聊） 智能聊天
                                operationPresenter.pushTask(36, "", "", "");
                                break;
                            //                            case 59:
                            //                                operationPresenter.pushTask(dataBean.getTask_id(), dataBean.getParam().ali_get_num_e, dataBean.getParam().ali_get_num_s, dataBean.getParam().app_keyword);
                            //                                break;
                            case 66:  //删除指定朋友圈
                                operationPresenter.pushTask(38, dataBean.getParam().getDel_content(), "", "");
                                break;
                            case 68: //超级自定义修改备注
                                //                                operationPresenter.pushTask(39, dataBean.getParam().getPreRemark(), dataBean.getParam().getRemark(), "");
                                AssExecutionProcess.InitAssembyProcess(mContext, dataBean);
                                break;
                            case 69: //二维码自定义拉群
                                String fanId = dataBean.getParam().getFanId();              //粉丝类型
                                String fanNum_s = dataBean.getParam().getFanNum_s();       //人数 区间 start
                                String fanNum_e = dataBean.getParam().getFanNum_e();       //人数 区间 end
                                String fanNum_default = dataBean.getParam().getFanNum_default();   // 0  为全部
                                String personNum = dataBean.getParam().getPersonNum(); // 具体群是多少人
                                SPUtils.putString(mContext, "QunPersonNum", personNum);
                                String fanType = dataBean.getParam().getFanType();  //新粉丝还是老粉丝
                                String groupType = dataBean.getParam().getGroupType();
                                String  batch   = dataBean.getParam().getBatch();
                                SPUtils.putString(mContext,"QunBatch",batch);
                                if (fanType.equals("2")) {  // 老粉丝
                                    if (fanNum_default != null && fanNum_default.equals("0")) {
                                        operationPresenter.pushTask(43, fanId, "0", groupType);
                                    } else {
                                        operationPresenter.pushTask(43, fanId, fanNum_s + "@" + fanNum_e, groupType);
                                    }

                                } else {
                                    //新粉丝
                                    if (fanNum_default != null && fanNum_default.equals("0")) {
                                        operationPresenter.pushTask(44, fanId, "0", groupType);
                                    } else {
                                        operationPresenter.pushTask(44, fanId, fanNum_s + "@" + fanNum_e, groupType);
                                    }

                                    //                                    operationPresenter.pushTask(44, fanId, "0", groupType);
                                }
                                break;

                            case 70:
                                String  friendsWxName = dataBean.getParam().getAccount();
                                String  friendsAccount_Id = dataBean.getParam().getAccount_id();
                                String friendsUid ="";
                                String friendsLocation ="2";

                                if (friendsAccount_Id!=null){
                                    String[] str_friends= friendsAccount_Id.split("_");
                                    friendsUid =str_friends[0];
                                    friendsLocation =str_friends[1];
                                }
                                operationPresenter.pushTask(45, friendsWxName, friendsUid, friendsLocation);

                                break;
                            case 496: // B账户收到加好友任务
                                String  friendsWxName2 = dataBean.getParam().getAccount();
                                String  friendsAccount_Id2 = dataBean.getParam().getAccount_id();
                                String friendsUid2 ="";
                                String friendsLocation2 ="2";
                                if (friendsAccount_Id2!=null){
                                    String[] str_friends2= friendsAccount_Id2.split("_");
                                    friendsUid2 =str_friends2[0];
                                    friendsLocation2 =str_friends2[1];
                                }
                                operationPresenter.pushTask(46, friendsWxName2, friendsUid2, friendsLocation2);

                                break;

                            case 499: // 语音互聊B账户
                                operationPresenter.pushTask(40, dataBean.getParam().getAccount(), "", "");
                                break;

                            case 497: // 语音聊天B账户
                                intent_zs = new Intent(mContext, MyVoiceAndVideoService.class);
                                mContext.startService(intent_zs);// 启动服务
                                SPUtils.putString(mContext, "StartVoiceAndVideoService", "1");
                                operationPresenter.pushTask(41, "", "", "");
                                break;
                            case 498: // 视频聊天B账户
                                intent_zs = new Intent(mContext, MyVoiceAndVideoService.class);
                                mContext.startService(intent_zs);// 启动服务
                                SPUtils.putString(mContext, "StartVoiceAndVideoService", "1");
                                operationPresenter.pushTask(42, "", "", "");
                                break;

                            //____________________支付宝_____________________________________________________
                            case 500://搜索加好友

                                if (!StringUtils.isEmpty(dataBean.getParam().getAli_add_num_s()) && !StringUtils.isEmpty(dataBean.getParam().getAli_add_num_e())) {
                                    if (version_ali == 1) {//新版本
                                        operationAlipayPresenter.setLogId(dataBean.getTask_id(), dataBean.getLog_id(), Integer.valueOf(dataBean.getParam().getAli_add_num_s()), Integer.valueOf(dataBean.getParam().getAli_add_num_e()), dataBean.getParam().getContact_verify_msg(), dataBean.getParam().getCrowd());//设置logId
                                        operationAlipayPresenter.task(-1);
                                    } else {//旧版本
                                        if (StringUtils.isEmpty(SPUtils.getString(mContext, "newsLog_id", ""))) {
                                            oldOperationAlipayPresenter.setLogId(dataBean.getTask_id(), dataBean.getLog_id(), Integer.valueOf(dataBean.getParam().getAli_add_num_s()), Integer.valueOf(dataBean.getParam().getAli_add_num_e()), dataBean.getParam().getContact_verify_msg());////设置logId
                                        } else {
                                            oldOperationAlipayPresenter.setLogId(dataBean.getTask_id(), SPUtils.getString(mContext, "newsLog_id", ""), Integer.valueOf(dataBean.getParam().getAli_add_num_s()), Integer.valueOf(dataBean.getParam().getAli_add_num_e()), dataBean.getParam().getContact_verify_msg());////设置logId
                                        }
                                        LogUtils.d("newsLog_id在本地存储的值是（发送到旧版本的值）?" + SPUtils.getString(mContext, "newsLog_id", ""));
                                        oldOperationAlipayPresenter.task(-1);
                                    }
                                } else {
                                    return;
                                }
                                break;
                            case 501://拉群
                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");

                                if (dataBean.getParam().getVersion() == 1) {  //新版
                                    if (StringUtils.isEmpty(dataBean.getParam().getMaxPullNum())) {
                                        operationAlipayPresenter.qunMaxNum = 50;
                                    } else {
                                        operationAlipayPresenter.qunMaxNum = Integer.valueOf(dataBean.getParam().getMaxPullNum());
                                    }

                                    if (StringUtils.isEmpty(dataBean.getParam().getPullNum())) {
                                        operationAlipayPresenter.qunMaxNum = 1000;
                                    } else {
                                        operationAlipayPresenter.qunMaxNum = Integer.valueOf(dataBean.getParam().getPullNum());
                                    }

                                    operationAlipayPresenter.setLogId(dataBean.getLog_id(), dataBean.getTask_id(), dataBean.getParam().getAli_add_num());//设置logId
                                    if (StringUtils.isEmpty(dataBean.getParam().getIs_gender())) {//是否分男女
                                        operationAlipayPresenter.setAstrictRemark(true);
                                    } else {
                                        if (dataBean.getParam().getIs_gender().equals("1")) {
                                            operationAlipayPresenter.setAstrictRemark(true);
                                        } else {
                                            operationAlipayPresenter.setAstrictRemark(false);
                                        }
                                    }
                                    operationAlipayPresenter.task(-2);

                                } else {
                                    if (StringUtils.isEmpty(dataBean.getParam().getMaxPullNum())) {
                                        oldOperationAlipayPresenter.qunMaxNum = 50;
                                    } else {
                                        oldOperationAlipayPresenter.qunMaxNum = Integer.valueOf(dataBean.getParam().getMaxPullNum());
                                    }

                                    if (StringUtils.isEmpty(dataBean.getParam().getPullNum())) {
                                        oldOperationAlipayPresenter.qunMaxNum = 1000;
                                    } else {
                                        oldOperationAlipayPresenter.qunMaxNum = Integer.valueOf(dataBean.getParam().getPullNum());
                                    }


                                    oldOperationAlipayPresenter.setLogId(dataBean.getLog_id(), dataBean.getTask_id(), dataBean.getParam().getAli_add_num());//设置logId
                                    if (StringUtils.isEmpty(dataBean.getParam().getIs_gender())) {//是否分男女
                                        oldOperationAlipayPresenter.setAstrictRemark(true);
                                    } else {
                                        if (dataBean.getParam().getIs_gender().equals("1")) {
                                            oldOperationAlipayPresenter.setAstrictRemark(true);
                                        } else {
                                            oldOperationAlipayPresenter.setAstrictRemark(false);
                                        }
                                    }
                                    oldOperationAlipayPresenter.task(-2);
                                }

                                wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");


                                break;
                            case 502://好友统计
                                operationAlipayPresenter.setLogId(dataBean.getLog_id(), dataBean.getTask_id(), dataBean.getParam().getAli_add_num());//设置logId
                                operationAlipayPresenter.task(-4);
                                ShowToast.show("该手机帐号好友统计成功", getActivity());
                                break;
                            case 503://群好友统计
                                operationAlipayPresenter.setLogId(dataBean.getLog_id(), dataBean.getTask_id(), dataBean.getParam().getAli_add_num());//设置logId
                                operationAlipayPresenter.task(-3);

                                //                        oldOperationAlipayPresenter.setLogId(dataBean.getLog_id(), dataBean.getTask_id(), dataBean.getParam().getAli_add_num());//设置logId
                                //                        oldOperationAlipayPresenter.task(-3);
                                break;
                            case 504://推送apk

                                operationAlipayPresenter.setLogId(dataBean.getLog_id(), dataBean.getTask_id(), dataBean.getParam().getAli_add_num());//设置logId
                                String url = dataBean.getParam().getDownload().trim();
                                operationAlipayPresenter.downApk(url);
                                break;
                            case 506://支付宝通用设置
                                phoneUtils.SettingPhone(dataBean);
                                break;
                            case 507://支付宝初始化
                                blockType = dataBean.getParam().getBlockType();
                                if (blockType == 2) {
                                    phoneUtils.ZFB_phone();
                                } else if (blockType == 3) {
                                    phoneUtils.WXinit();
                                } else {
                                    phoneUtils.wifi();//手机相关设置的初始化
                                }

                                ShowToast.show("支付宝手机初始化认识执行完成！", (Activity) getContext());
                                break;
                            case 508://支付宝定时开关机
                                phoneUtils.SettingPhoneOpen(dataBean);
                                break;
                            case 509://支付宝发单
                                if (StringUtils.isEmpty(dataBean.getParam().getMateria_ss())) {
                                    showToast("数据有误，任务失败");
                                    return;
                                }

                                if (StringUtils.isEmpty(dataBean.getTodo_time())) {
                                    try {
                                        Thread.sleep(25000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                String messageData = dataBean.getParam().getMateria_ss();
                                String sendmore = dataBean.getParam().getMore();//多发条数

                                WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
                                boolean bl = true;
                                if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans != null && wxFlockMessageBeans.length > 0) {
                                    LogUtils.d(wxFlockMessageBeans.length + "条信息");
                                    for (int b = 0; b < wxFlockMessageBeans.length; b++) {//图片下载
                                        if (wxFlockMessageBeans[b].getType().equals("img")) {
                                            String imgUrl = wxFlockMessageBeans[b].getData();
                                            if (!StringUtils.isEmpty(imgUrl)) {
                                                downloadFile(imgUrl);
                                                if (bl)
                                                    ShowToast.show("图片下载中...", (Activity) mContext);
                                                bl = false;
                                            }
                                        }
                                    }
                                    try {
                                        Thread.sleep(25000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    operationAlipayPresenter.setMessageData(dataBean.getParam().getMateria_ss(), dataBean.getParam().getCrowd(), dataBean.getParam().getIs_statistic(), sendmore, dataBean.getLog_id());
                                    operationAlipayPresenter.task(-5);

                                } else {
                                    ShowToast.show("数据有误", (Activity) mContext);
                                    return;
                                }

                                break;
                            case 513://支付宝发单(给好友发)
                                if (StringUtils.isEmpty(dataBean.getParam().getMateria_ss())) {
                                    showToast("数据有误，任务失败");
                                    return;
                                }

                                if (StringUtils.isEmpty(dataBean.getTodo_time())) {
                                    try {
                                        Thread.sleep(25000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                String messageData2 = dataBean.getParam().getMateria_ss();
                                String sendmore2 = dataBean.getParam().getMore();//多发条数

                                WxFlockMessageBean[] wxFlockMessageBeans2 = new Gson().fromJson(messageData2.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
                                boolean b2 = true;
                                if (!StringUtils.isEmpty(messageData2) && wxFlockMessageBeans2 != null && wxFlockMessageBeans2.length > 0) {
                                    LogUtils.d(wxFlockMessageBeans2.length + "条信息");
                                    for (int b = 0; b < wxFlockMessageBeans2.length; b++) {//图片下载
                                        if (wxFlockMessageBeans2[b].getType().equals("img")) {
                                            String imgUrl = wxFlockMessageBeans2[b].getData();
                                            if (!StringUtils.isEmpty(imgUrl)) {
                                                downloadFile(imgUrl);
                                                if (b2)
                                                    ShowToast.show("图片下载中...", (Activity) mContext);
                                                bl = false;
                                            }
                                        }
                                    }
                                    try {
                                        Thread.sleep(25000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    operationAlipayPresenter.setMessageData(dataBean.getParam().getMateria_ss(), dataBean.getParam().getCrowd(), dataBean.getParam().getIs_statistic(), sendmore2, dataBean.getLog_id());
                                    operationAlipayPresenter.task(-7);

                                } else {
                                    ShowToast.show("数据有误", (Activity) mContext);
                                    return;
                                }

                                break;
                            case 515:   //关注生活号
                                operationAlipayPresenter.pushTask(-10, dataBean.getParam().getPublicType(), dataBean.getParam().getTypeName(), "");
                                break;
                            case 516:  //支付宝生活圈点赞
                                operationAlipayPresenter.pushTask(-11, dataBean.getParam().getDz_num_s(), dataBean.getParam().getDz_num_e(), "");
                                break;
                            case 517:  // 支付宝生活圈图文发布
                                operationAlipayPresenter.pushTask(-12, "1", dataBean.getParam().getMateria_ss(), "");
                                break;
                            case 518:  // 支付宝生活圈小视频发布
                                operationAlipayPresenter.pushTask(-12, "2", dataBean.getParam().getMateria_vedio(), "");
                                break;
                            case 519: //支付宝 获取名字
                                String start_ali = dataBean.getParam().getAli_get_num_s();
                                String end_ali = dataBean.getParam().getAli_get_num_e();
                                if (start_ali == null || end_ali == null) {
                                    start_ali = "40";
                                    end_ali = "60";
                                }
                                int start2 = Integer.parseInt(start_ali);
                                int end2 = Integer.parseInt(end_ali);
                                int count = (int) (start2 + Math.random() * (end2 - start2 + 1));
                                SPUtils.putInt(mContext, "zfbAccountProblem", 0);
                                operationAlipayPresenter.setAliNameCount(count, dataBean.getParam().getTable(), dataBean.getLog_id());
                                operationAlipayPresenter.task(-10);
                                break;

                            case 520: // 指定群转发图文给好友
                                SPUtils.putInt(mContext, "zfbQunAccount", 0);
                                operationAlipayPresenter.setMessageData(dataBean.getParam().getMateria_ss(), dataBean.getParam().getCrowd(), dataBean.getParam().getIs_statistic(), dataBean.getParam().getMore(), dataBean.getLog_id());
                                operationAlipayPresenter.task(-8);
                                zfbAccount = SPUtils.getInt(mContext, "zfbAccount", 0);
                                zfbQunAccount = SPUtils.getInt(mContext, "zfbQunAccount", 0);
                                break;
                            case 521: //指定群转发图文给群
                                SPUtils.putInt(mContext, "zfbQunAccount", 0);
                                operationAlipayPresenter.setMessageData(dataBean.getParam().getMateria_ss(), dataBean.getParam().getCrowd(), dataBean.getParam().getIs_statistic(), dataBean.getParam().getMore(), dataBean.getLog_id());
                                operationAlipayPresenter.task(-9);
                                zfbAccount = SPUtils.getInt(mContext, "zfbAccount", 0);
                                zfbQunAccount = SPUtils.getInt(mContext, "zfbQunAccount", 0);
                                break;


                            case 34://语音聊天
                                operationPresenter.pushTask(25, "", "", "");
                                break;
                            case 35://视频聊天
                                operationPresenter.pushTask(26, "", "", "");
                                break;
                            case 40://版本更新（微信）
                            case 511://版本更新（支付宝）
                                StateRenwuBean stateRenwuBeanUpdata = new StateRenwuBean(61, log_id, "200", getDtae());
                                stateDao.updatePerson(stateRenwuBeanUpdata);
                                updata();
                                StatuRequest(dataBean);
                                return;
                            case 38://浏览公众号
                                if (StringUtils.isEmpty(dataBean.getParam().getArticle_num_e()) || StringUtils.isEmpty(dataBean.getParam().getArticle_num_s())) {
                                    dataBean.getParam().setArticle_num_e("5");
                                    dataBean.getParam().setArticle_num_s("2");
                                }

                                if (StringUtils.isEmpty(dataBean.getParam().getSlip_time_e()) || StringUtils.isEmpty(dataBean.getParam().getSlip_time_s())) {
                                    dataBean.getParam().setSlip_time_e("3");
                                    dataBean.getParam().setSlip_time_s("1");
                                }

                                if (StringUtils.isEmpty(dataBean.getParam().getRead_time_e()) || StringUtils.isEmpty(dataBean.getParam().getRead_time_s())) {
                                    dataBean.getParam().setRead_time_e("60");
                                    dataBean.getParam().setRead_time_s("10");
                                }

                                operationPresenter.setPublicMark(Integer.valueOf(dataBean.getParam().getArticle_num_e()), Integer.valueOf(dataBean.getParam().getArticle_num_s()), Integer.valueOf(dataBean.getParam().getSlip_time_e()), Integer.valueOf(dataBean.getParam().getSlip_time_s()), Integer.valueOf(dataBean.getParam().getRead_time_e()), Integer.valueOf(dataBean.getParam().getRead_time_s()));
                                operationPresenter.pushTask(28, "", "", "");
                                break;
                            case 39://关注公众号
                                if (!(StringUtils.isEmpty(dataBean.getParam().getPublicType()) || StringUtils.isEmpty(dataBean.getParam().getTypeName()))) {
                                    operationPresenter.pushTask(27, dataBean.getParam().getPublicType(), dataBean.getParam().getTypeName(), "");
                                }
                                break;
                            //                    case 41://双向互聊
                            //                        operationPresenter.pushTask(29, "", "", "");
                            //                        break;
                            case 42://微信发红包
                                MessageListBean.ContentBean.DataBean.ParamBean param = dataBean.getParam();
                                String pay_num_s = param.pay_num_s;
                                String pay_num_e = param.pay_num_e;
                                String pay_password = param.pay_password;
                                //判断非空
                                if (TextUtils.isEmpty(pay_num_s)) {
                                    pay_num_s = "1";
                                }
                                if (TextUtils.isEmpty(pay_num_e)) {
                                    pay_num_e = "5";
                                }
                                if (TextUtils.isEmpty(pay_password)) {
                                    isNormal = false;
                                } else {
                                    isNormal = check(pay_num_s, pay_num_e, pay_password);//校验参数是否正确
                                }

                                if (isNormal) {
                                    operationPresenter.pushTask(30, pay_num_s, pay_num_e, pay_password);
                                } else {//任务执行失败。服务器参数不正确。
                                    StateRenwuBean stateRenwuBean2 = new StateRenwuBean(dataBean.getTask_id(), Integer.parseInt(dataBean.getLog_id()), "400", getDtae());
                                    stateDao.updatePerson(stateRenwuBean2);
                                }
                                break;
                        }
                        if (dataBean.getTask_id() == 50) {
                            StatuRequest_two(dataBean);
                            for (int a = 0; a < dataBeanList.size(); a++) {
                                if (dataBeanList.get(a).getLog_id().equals(dataBean.getLog_id())) {
                                    dataBeanList.remove(a);
                                }
                            }
                            LogUtils.d("TaskI的值zplh删除后还有" + dataBeanList.size() + "个任务");
                            wxUtils.saveTask(mContext, dataBeanList);//保存到本地
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ShowToast.show("正在生成循环任务,请稍后", getActivity());
                            getXunhuan(dataBean);
                            ShowToast.show("循环任务已经生成,请稍后", getActivity());
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else if (dataBean.getTask_id() <= 500 && dataBean.getTask_id() != 33 && dataBean.getTask_id() != 32 && dataBean.getTask_id() != 28 && dataBean.getTask_id() != 27 && dataBean.getTask_id() != 37 && dataBean.getTask_id() != 40) {
                            if (isLogin) {
                                StatuRequest(dataBean);
                                if (isNormal) {
                                    StateRenwuBean stateRenwuBean2 = new StateRenwuBean(dataBean.getTask_id(), Integer.parseInt(dataBean.getLog_id()), "200", getDtae());
                                    stateDao.updatePerson(stateRenwuBean2);
                                }
                                LogUtils.d("登录了微信");
                            } else {
                                LogUtils.d("未登录微信");
                                StateRenwuBean stateRenwuBean1 = new StateRenwuBean(dataBean.getTask_id(), Integer.parseInt(dataBean.getLog_id()), "400", getDtae());
                                stateDao.updatePerson(stateRenwuBean1);
                            }
                        } else if (dataBean.getTask_id() == 519 && SPUtils.getInt(mContext, "zfbAccountProblem", 0) == 1) {
                            StateRenwuBean stateRenwuBean4 = new StateRenwuBean(dataBean.getTask_id(), Integer.parseInt(dataBean.getLog_id()), "400", getDtae());
                            stateDao.updatePerson(stateRenwuBean4);
                            //                    StatuRequest_two(dataBean);
                        } else {
                            StatuRequest(dataBean);
                            StateRenwuBean stateRenwuBean2 = new StateRenwuBean(dataBean.getTask_id(), Integer.parseInt(dataBean.getLog_id()), "200", getDtae());
                            stateDao.updatePerson(stateRenwuBean2);
                        }

                        LogUtils.d("任务执行完成");
                        if (dataBean.getTask_id() != 27 && dataBean.isListTask()) {//设置任务间隔时间
                            //设置间隔时间
                            int start;
                            if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getTask_time_s())) {
                                start = 600;
                            } else {
                                start = Integer.valueOf(app.getWxGeneralSettingsBean().getTask_time_s());
                            }
                            int end;
                            if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getTask_time_e())) {
                                end = 3600;
                            } else {
                                end = Integer.valueOf(app.getWxGeneralSettingsBean().getTask_time_e());
                            }
                            int timeSleep = new Random().nextInt(end - start + 1) + start;
                            LogUtils.e("end=" + end + "__start=" + start + "___任务间隔随机数=" + timeSleep);
                            ShowToast.show("任务间隔时间：" + timeSleep + "秒", (Activity) mContext);
                            try {
                                Thread.sleep(timeSleep * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        //                wxUtils.adb("input keyevent KEYCODE_HOME");

                        SPUtils.putBoolean(mContext, "bindingtomain", true);
                        getActivity().startActivity(intent);
                        SPUtils.putBoolean(mContext, "chitchat", true);//双向互聊初始化
                        //                ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.zplh.zplh_android_yk/com.zplh.zplh_android_yk.ui.activity.BindingActivity");
                        //                android.os.Process.killProcess(android.os.Process.myPid());

                        //保存任务到数据库避免重复执行
                        TaskLogIdBean taskLogIdBean = new TaskLogIdBean();
                        taskLogIdBean.setLogIdTask(dataBean.getLog_id());
                        LogUtils.e("添加数据库" + taskLogIdBean.save() + "内容是:" + dataBean.getLog_id());
                    }

                });
            }

            /**
             * 校验参数是否正确！
             */
            private boolean check(String pay_num_s, String pay_num_e, String pay_password) {
                Integer s;
                Integer e;
                Integer password;
                try {
                    s = Integer.valueOf(pay_num_s); //3
                    e = Integer.valueOf(pay_num_e);//5
                    password = Integer.valueOf(pay_password);
                } catch (Exception ex) {
                    ShowToast.show("格式异常，密码、金额应该为数字", (Activity) mContext);
                    return false;
                }
                if (s > e || s < 1 || e > 5 || pay_password.length() != 6) {
                    ShowToast.show("金额范围错误、密码长度错误", (Activity) mContext);
                    return false;
                }

                return true;
            }


        }).run();


    }

    /**
     * 当前任务循环
     */
    public void getXunhuan(final MessageListBean.ContentBean.DataBean dataBean) {
        long conf;
        String add_log_id = "";
        Timer timer = new Timer();
        long time_damgqiam = timeUtil.getCurrentTimeMilies();//获取当前时间戳
        conf = Long.parseLong(dataBean.getParam().getInterval_time());//传递过来的间隔时间
        long new_time_start = conf * 60 * 60;//间隔时间的时间戳
        final long next_time = time_damgqiam + new_time_start;//下次执行的时间
        //SPUtils.getString(mContext,"newsLog_id","")
        if (!StringUtils.isEmpty(dataBean.getLog_id())) {
            add_log_id = dataBean.getLog_id();
        } else {
            add_log_id = SPUtils.getString(mContext, "newsLog_id", "");
        }
        RequestParams params = new RequestParams(zfb_urls.old_logid_new());
        params.addQueryStringParameter("log_id", add_log_id);
        LogUtils.d("以旧换新的log_id的接口是" + zfb_urls.old_logid_new() + "?log_id=" + dataBean.getLog_id());
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
            @Override
            public void onSuccess(LogidBean bean) {
                StateRenwuBean stateRenwuBean = new StateRenwuBean(50, Integer.parseInt(bean.getData()), "任务待执行", "周期任务:" + timeUtil.getTimesCuo(next_time * 1000));
                stateDao.addPerson(stateRenwuBean);
                newsLog_ids = bean.getData();
                SPUtils.putString(mContext, "newsLog_id", bean.getData());
                for (int a = 0; a < dataBeanList.size(); a++) {//先清空  在保存
                    if (dataBeanList.get(a).getTask_id() == 50) {
                        dataBeanList.remove(a);
                    }
                }
                dataBean.setLog_id(bean.getData());
                dataBean.setTodo_time(String.valueOf(next_time));
                BaseApplication.getDataBeanList().add(dataBean);//保存任务
                wxUtils.saveTask(mContext, BaseApplication.getDataBeanList());//保存到本地

                LogUtils.d("以旧换新换到新的log_id是：" + SPUtils.getString(mContext, "newsLog_id", ""));
                RequestParams params_updata = new RequestParams(URLS.updata_task_status());
                params_updata.addQueryStringParameter("log_id", bean.getData());
                params_updata.addQueryStringParameter("uid", uid);
                LogUtils.d(URLS.updata_task_status() + "?log_id=" + SPUtils.getString(mContext, "newsLog_id", "") + "&uid=" + uid);
                HttpManager.getInstance().sendRequest(params_updata, new HttpObjectCallback<CheckImei>() {
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

            @Override
            public void onFailure(int errorCode, String errorString) {
            }
        });

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                setBorad(dataBean);
            }
        };
        //new_time_start*1000
        timer.schedule(timerTask, new_time_start * 1000);//每次任务的等待时间
        LogUtils.d("目前该时间是" + new_time_start + "执行间隔时间是" + new_time_start * 1000 + "毫秒");

    }

    /**
     * 重复执行当前循环
     *
     * @param flag = 1 表示：没有收到任务，此时数据库没有该任务信息，添加数据
     *             flag = 2 表示：任务执行到一半卡死，此时数据库有该任务信息。修改任务
     */
    public void again(final MessageListBean.ContentBean.DataBean dataBean, int flag) {

        String add_log_id = dataBean.getLog_id();
        ;
        Timer timer = new Timer();

        long time_damgqiam = timeUtil.getCurrentTimeMilies();//获取当前时间戳
        long conf = Long.parseLong(dataBean.getParam().getInterval_time());//传递过来的间隔时间
        //long new_time_start=conf*60*60;//间隔时间的时间戳
        long new_time_start = 0;//不要时间间隔，立即执行
        final long next_time = time_damgqiam + new_time_start;//下次执行的时间
        //        if (StringUtils.isEmpty(SPUtils.getString(mContext,"newsLog_id",""))){
        //            add_log_id=dataBean.getLog_id();
        //        }else {
        //            add_log_id=SPUtils.getString(mContext,"newsLog_id","");
        //        }
        StateRenwuBean stateRenwuBean = new StateRenwuBean(50, Integer.parseInt(add_log_id), "任务待执行", "周期任务:" + timeUtil.getTimesCuo(next_time * 1000));
        if (flag == 1) {
            stateDao.addPerson(stateRenwuBean);
        } else if (flag == 2) {
            stateDao.updatePerson(stateRenwuBean);//此任务已经存在，应该是修改，不是添加
        }

        SPUtils.putString(mContext, "newsLog_id", add_log_id);
        for (int a = 0; a < dataBeanList.size(); a++) {//先清空  在保存
            if (dataBeanList.get(a).getTask_id() == 50) {
                dataBeanList.remove(a);
            }
        }
        dataBean.setLog_id(add_log_id);
        dataBean.setTodo_time(String.valueOf(next_time));
        BaseApplication.getDataBeanList().add(dataBean);//保存任务
        wxUtils.saveTask(mContext, BaseApplication.getDataBeanList());//保存到本地
        RequestParams params_updata = new RequestParams(URLS.updata_task_status());
        params_updata.addQueryStringParameter("log_id", add_log_id);
        params_updata.addQueryStringParameter("uid", uid);
        HttpManager.getInstance().sendRequest(params_updata, new HttpObjectCallback<CheckImei>() {
            @Override
            public void onSuccess(CheckImei bean) {
                LogUtils.d("收到任务后反馈成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("收到任务后反馈失败:" + errorString);
            }
        });
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                setBorad(dataBean);
            }
        };
        //new_time_start*1000
        timer.schedule(timerTask, new_time_start * 1000);//每次任务的等待时间
        LogUtils.d("目前该时间是" + new_time_start + "执行间隔时间是" + new_time_start * 1000 + "毫秒");


    }

    /**
     * 发送广播任务
     *
     * @param dataBeanList
     */
    private void setBorad(MessageListBean.ContentBean.DataBean dataBeanList) {
        Intent intent2 = new Intent();
        intent2.setAction(MyConstains.Broadcast_Task_TWO);
        Bundle bundle = new Bundle();
        bundle.putSerializable("messageBean", dataBeanList);
        intent2.putExtras(bundle);
        mContext.sendBroadcast(intent2);
        LogUtils.d("你发送了广播吗？？？？？？？？？？？？？？？？？？？？？");
    }

    int log_id = 0;

    /**
     * 支付宝加好友的专用加粉后反馈后台的请求方法
     *
     * @param dataBean
     */
    private void StatuRequest_two(final MessageListBean.ContentBean.DataBean dataBean) {
        final boolean isTag = SPUtils.getBoolean(mContext, "isTag", true);
        final int id = dataBean.getTask_id();
        if (StringUtils.isEmpty(SPUtils.getString(mContext, "newsLog_id", ""))) {
            log_id = Integer.parseInt(dataBean.getLog_id());
            LogUtils.d("你发送的是databean自带的log_id自带的值是:" + log_id);
        } else {
            log_id = Integer.parseInt(SPUtils.getString(mContext, "newsLog_id", ""));
            LogUtils.d("你发送的是databean自带的循环任务的值:" + log_id);
        }
        LogUtils.d("newsLog_id在本地存储的值是（刚开始进入到网络中）?" + SPUtils.getString(mContext, "newsLog_id", ""));
        RequestParams params = new RequestParams(urls.getResut());
        params.addQueryStringParameter("log_id", log_id + "");
        params.addQueryStringParameter("uid", uid);
        LogUtils.d("任务完成后提交到后台，返回的接口地址:" + urls.getResut() + "?log_id=" + log_id + "&uid=" + uid);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
            @Override
            public void onSuccess(LogidBean bean) {
                LogUtils.d("任务完成后返回的接口数据是:" + bean.getRet());
                if (isTag == true) {
                    StateRenwuBean stateRenwuBean = new StateRenwuBean(id, log_id, bean.getRet(), getDtae());
                    stateDao.updatePerson(stateRenwuBean);
                } else {
                    LogUtils.d("没有登录帐号的情况下:" + SPUtils.getBoolean(mContext, "isTag", true));
                    StateRenwuBean stateRenwuBean = new StateRenwuBean(id, log_id, "400", getDtae());
                    stateDao.updatePerson(stateRenwuBean);
                }
                //                StateRenwuBean stateRenwuBean = new StateRenwuBean(id, log_id, bean.getRet(), getDtae());
                //                stateDao.updatePerson(stateRenwuBean);
                alipay_statu = bean.getRet();
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                ShowToast.show("网络请求失败，请检测网络", (Activity) getContext());
                StateRenwuBean stateRenwuBean = new StateRenwuBean(id, log_id, errorString, getDtae());
                stateDao.updatePerson(stateRenwuBean);
            }
        });
        SPUtils.putString(mContext, "newsLog_id", "");

    }

    boolean isLogin = true;//是否登录微信

    @Override
    public void isLoginWx(boolean isLogin) {
        this.isLogin = isLogin;
    }


    private void StatuRequest(final MessageListBean.ContentBean.DataBean dataBean) {
        final int log_id = Integer.parseInt(dataBean.getLog_id());
        final int id = dataBean.getTask_id();
        RequestParams params = new RequestParams(urls.getResut());
        params.addQueryStringParameter("log_id", log_id + "");
        params.addQueryStringParameter("uid", uid);

        LogUtils.d("任务完成后提交到后台，返回的接口地址:" + urls.getResut() + "?log_id=" + log_id + "&uid=" + uid);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
            @Override
            public void onSuccess(LogidBean bean) {
                LogUtils.d("任务完成后返回的接口数据是:" + bean.getRet());
                if (isNormal) {
                    StateRenwuBean stateRenwuBean = new StateRenwuBean(id, log_id, bean.getRet(), getDtae());
                    stateDao.updatePerson(stateRenwuBean);
                }
                alipay_statu = bean.getRet();
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                //                ShowToast.show("网络请求失败，请检测网络", (Activity) getContext());
                //                StateRenwuBean stateRenwuBean = new StateRenwuBean(id, log_id, errorString, getDtae());
                //                stateDao.updatePerson(stateRenwuBean);
            }
        });

    }

    private String getDtae() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = df.format(new Date());
        return str;
    }

    private boolean uncaughtExceptionFlag = true;

    /**
     * 执行点击的任务
     *
     * @param taskId
     */
    public void task(final int taskId) {
        SingleThread.getInstance().threadPoolExecute(new Runnable() {
            @Override
            public void run() {
                if (uncaughtExceptionFlag)
                    uncaughtException();
                uncaughtExceptionFlag = false;
                switch (taskId) {
                    case 0:
                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        operationPresenter.task(taskId);//修改备注
                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        break;
                    case 1:
                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        operationPresenter.task(taskId);//拉群
                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        break;
                    case 3:
                        operationPresenter.task(taskId);
                        break;
                    case 4:
                        operationPresenter.task(taskId);
                        break;
                    case 5:
                        operationPresenter.task(taskId);
                        break;
                    case 6:
                        operationPresenter.task(taskId);
                        break;
                    case 7://自助加好友
                        ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.wanj.x007/com.wanj.x007.MainActivity");
                        operationPresenter.task(taskId);
                        break;
                    case 8://朋友圈发布
                        operationPresenter.task(taskId);
                        break;
                    case 9:
                        operationPresenter.task(taskId);
                        break;
                    case 10:
                        operationPresenter.task(taskId);
                        break;
                    case 11:
                        operationPresenter.task(taskId);
                        break;
                    case 25:
                        operationPresenter.task(taskId);
                        break;

                    //-------------------支付宝-----------------------------
                    case -1://加好友
                        operationAlipayPresenter.task(taskId);
                        break;
                    case -2://拉群
                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        operationAlipayPresenter.task(taskId);
                        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                        break;
                    case -3://统计群成员
                        operationAlipayPresenter.task(taskId);
                        break;
                }
            }
        });
    }

    /**
     * 崩溃是重启
     */
    public void uncaughtException() {
        if (BuildConfig.DEBUG) {
            return;
        }
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                LogUtils.d("崩溃了..................");
                // 复活
                PackageManager pm = getActivity().getApplication().getPackageManager();
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(getActivity().getApplication().getPackageName());
                startActivity(launchIntentForPackage);

                // 杀死
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }


    /**
     * 判断任务是否取消
     */
    private void HttpLog_id(final MessageListBean.ContentBean.DataBean data) {
        RequestParams params = new RequestParams(zfb_urls.isRenwuBoolean());
        params.addQueryStringParameter("log_id", data.getLog_id());//status
        LogUtils.d(zfb_urls.isRenwuBoolean() + "?log_id=" + data.getLog_id());
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {

            @Override
            public void onSuccess(LogidBean bean) {
                login_ret = bean.getRet();
                LogUtils.d("打印出来的login_ret是:" + login_ret);
                LogUtils.d(data.getLog_id() + "对应的任务已经被取消");

                for (int a = 0; a < dataBeanList.size(); a++) {
                    if (dataBeanList.get(a).getLog_id().equals(data.getLog_id())) {
                        //                        if(dataBean.getTask_id()!=50)
                        LogUtils.d("对应的取消任务已删除");
                        dataBeanList.remove(a);
                    }
                }
                LogUtils.d("zplh删除后还有" + dataBeanList.size() + "个任务");
                wxUtils.saveTask(mContext, dataBeanList);//保存到本地
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                version_ali = data.getParam().getVersion();
                task(data);//执行任务
            }
        });
    }


    /**
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

    private ProgressDialog pd;
    //______________________________ 关于更新____________________________________________

    /**
     * 检查版本是否需要更新
     */
    private void updata() {
        RequestParams params = new RequestParams(URLS.updata());
        LogUtils.d(URLS.updata());
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<CheckImei>() {

            @Override
            public void onSuccess(CheckImei bean) {
                String newVersion = bean.getData();
                double version = Double.valueOf(wxUtils.getVersionName(mContext));
                double ver = Double.valueOf(bean.getData());
                if (ver > version) {
                    pd = ProgressDialog.show(mContext, "提示", "等待更新中...", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int a = 5000;
                            while (true) {
                                if (version_update_go) {
                                    version_update_go();
                                } else {
                                    pd.dismiss();
                                    version_update_go = true;
                                    LogUtils.d("开始下载");
                                    handler.sendEmptyMessage(0x124);
                                    break;
                                }
                                LogUtils.d("20s判断一次");
                                try {
                                    Thread.sleep(a);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                a = 20000;
                            }
                        }
                    }).start();

                } else {
                    ShowToast.show("已经是最新版本", (Activity) mContext);
                    LogUtils.d("已经是最新版本...");
                }
            }

            @Override
            public void onFailure(int errorCode, String errorString) {

            }
        });
    }


    boolean version_update_go = true;
    //-----------------------------下载更新开始----------------------------------

    /**
     * 开始下载
     */
    public void version_update_go() {
        //        ShowToast.show("等待更新中...",(Activity) mContext);
        RequestParams params = new RequestParams(URLS.version_update_go());
        //        params.addQueryStringParameter("id", id);//手机4位数
        LogUtils.d(URLS.version_update_go());
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                version_update_go = false;
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
            }
        });
    }

    /**
     * 下载结束
     */
    public void version_update_back() {
        RequestParams params = new RequestParams(URLS.version_update_back());
        //        params.addQueryStringParameter("id", id);//手机4位数
        LogUtils.d(URLS.version_update_back());
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {

            }

            @Override
            public void onFailure(int errorCode, String errorString) {
            }
        });
    }

    /**
     * 下载更新
     */
    private void updataDown(final String path, final String pathUrl) {

        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(mContext);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("新版本下载中");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);

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
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("亲，努力下载中。。。");
                pd.show();
                pd.setMax((int) total);
                pd.setProgress((int) current);


                LogUtils.d((int) total + "下载了...");
                LogUtils.d((int) current + "下载...");
            }

            @Override
            public void onSuccess(File result) {
                LogUtils.d("下载完成开始安装");
                version_update_back();//下载结束反馈
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adb("pm install -r " + pathUrl);
                pd.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                LogUtils.d("x下载失败");
                pd.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private Handler handler = new Handler() {

        // 该方法运行在主线程中
        // 接收到handler发送的消息，对UI进行操作
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case 0x123:
                    //                    pd.setMessage("等待更新中");
                    break;
                case 0x124:
                    updataDown("http://103.94.20.102:8087/download/wxzs.apk", Environment.getExternalStorageDirectory().getAbsoluteFile() + "/wxykupdata.apk");
                    //                    downLoadApk("http://103.94.20.102:8087/download/wxzs.apk");
                    break;
            }


        }
    };

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk(final String apkUrl) {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(mContext);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("新版本下载中");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(apkUrl, pd);
                    version_update_back();//下载完成反馈
                    sleep(1000);
                    String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/wxykupdata.apk";
                    LogUtils.d("下载完成开始安装");
                    wxUtils.adb("pm install -r " + path);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * [下载APP]
     */
    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(25000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "wxykupdata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    //-----------------------------下载更新结束---------------------------------

}
