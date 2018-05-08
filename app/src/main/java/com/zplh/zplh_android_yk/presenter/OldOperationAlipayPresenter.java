package com.zplh.zplh_android_yk.presenter;

/**
 * Created by Administrator on 2017/7/29.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.text.ClipboardManager;

import com.google.gson.Gson;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.Threads.ReadThread;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.bean.AlipayAccountFlockClickBean;
import com.zplh.zplh_android_yk.bean.AlipayAccountFlockNumBean;
import com.zplh.zplh_android_yk.bean.AlipayAlterNameSqliteBean;
import com.zplh.zplh_android_yk.bean.LogidBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.bean.PhoneBean;
import com.zplh.zplh_android_yk.bean.StateRenwuBean;
import com.zplh.zplh_android_yk.bean.WxFlockMessageBean;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.conf.ZFB_URLS;
import com.zplh.zplh_android_yk.db.StateDao;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.ui.view.OperationView;
import com.zplh.zplh_android_yk.utils.FileUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.PhoneUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;
import com.zplh.zplh_android_yk.utils.StringUtils;
import com.zplh.zplh_android_yk.utils.TimeUtil;

import org.litepal.crud.DataSupport;
import org.xutils.http.RequestParams;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 支付宝老版本
 */

public class OldOperationAlipayPresenter extends BasePresenter<OperationView> {
    private OperationView operationView;
    private Context context;
    private WxUtils wxUtils = new WxUtils();
    private String xmlData;
    private List<Integer> listXY;
    private ZFB_URLS zfb_urls;//支付宝接口
    private String zfb_phone = "";//添加联系人获取的手机号码
    private String zfbNames = "";//支付宝名称
    private String zfb_friend_num;//支付宝好友数量
    private int send_friend_num = 0;//每一个账号每天的发送好友申请的数量
    private String statue = "";//任务出现故障原因
    private int account = 1;//第几个支付宝账号
    private String logId = "0";
    private int taskId = 0;
    private StateDao stateDao;
    private PackageManager mPackageManager;
    private String ali_add_num = "";//支付宝每次任务请求数量
    private List<AlipayAccountFlockNumBean.AccountBean> accountBeanList;//群成员
    private List<AlipayAccountFlockClickBean.AccountBean> clickAccountBenaList;//拉群点击数据
    Gson gson = new Gson();
    private int ali_add_num_s;//支付宝开始
    private int ali_add_num_e;//支付宝结束
    private boolean isAstrictRemark = false;
    String messageData;
    private String uid;
    private String add_friends = "";
    private String contact_verify_msg = "";//搜索加好友申请内容//可以作为通讯录加好友申请内容
    private PhoneUtils phoneUtils;
    private boolean is_tag = true;
    private ReadThread readThread = new ReadThread(Thread.class.getName());
    private boolean is_username = true;
    private int chen_limit = 0;//请求次数

    public boolean isAstrictRemark() {
        return isAstrictRemark;
    }

    public void setAstrictRemark(boolean astrictRemark) {
        isAstrictRemark = astrictRemark;
    }

//
//   private NetworkChange.OnNetWorkChange MyWorknets=new NetworkChange.OnNetWorkChange() {
//        @Override
//        public void onChange(int wifi, int mobile, int none, int oldStatus, int newStatus) {
//            LogUtils.d("你是否回调了亲戚气你亲");
//            if (newStatus == none){ //没有网络
//                LogUtils.d("暂时没有了网络呢");
//                LogUtils.d("任务正在等待哦");
//                 new Thread(new Runnable() {
//                     @Override
//                     public void run() {
//                             synchronized (this){
//                                 while (true){
//                                     try {
//                                         wait();
//                                     } catch (InterruptedException e) {
//                                         e.printStackTrace();
//                                     }
//                                 }
//                             }
//                     }
//                 }).start();
//                // readThread.suspend();
//                ShowToast.show("哎真是可怜 连网络都没有的人", (Activity) context);
//            }
//            if (newStatus == mobile){  //移动网络
//                LogUtils.d("该手机目前有网络");
//                ShowToast.show("我是有网络的人 哈哈你就是么有网络", (Activity) context);
//                LogUtils.d("开始了任务哦");
//
//
//            }
//            if (newStatus == wifi){//wifi网络
//
//
//                if (oldStatus == mobile) {  //从移动网络切换到wifi网络
//
//                }
//            }
//       }
//    };

    //    //注册监听网络变化
//    public void registerReceiver() {
//        NetworkChange.getInstance().setOnNetWorkChange(MyWorknets);
//    }
    public OldOperationAlipayPresenter(Context context, OperationView operationView) {
        this.operationView = operationView;
        this.context = context;
        zfb_urls = new ZFB_URLS();
        stateDao = new StateDao(context);
        mPackageManager = context.getPackageManager();
        phoneUtils = new PhoneUtils(context);
        uid = SPUtils.getString(context, "uid", "0000");

    }

    private int time;

    /**
     * 支付宝执行任务.先判断
     *
     * @param task
     */
    public void task(int task) {
        SPUtils.putBoolean(context,"isTag",true);
        is_username = true;
        //registerReceiver();//监听网络的状态
        backNum = 0;
        statue = "";
        if (isInstallApp(context, "com.eg.android.AlipayGphone"))  {//判断支付宝是否安装
            if (time == 0) {
                wxUtils.openAliPay();//打开支付宝
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("忘记密码") || (xmlData.contains("登录") && xmlData.contains("新用户注册") && xmlData.contains("语言")) || (xmlData.contains("你的手机号") && xmlData.contains("注册"))||
                    xmlData.contains("语言")&&xmlData.contains("登录")&&xmlData.contains("没有账号? 请注册")||xmlData.contains("登录")&&xmlData.contains("登录遇到问题？")&&xmlData.contains("注册")) {//判断是否登录
                time = 0;
                LogUtils.e("请先登录支付宝");
                statue = "已安装支付宝需加号";
                String uid = SPUtils.getString(context, "uid", "0000");
                getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
                ShowToast.show("请先登录支付宝", (Activity) context);
//                StateRenwuBean stateRenwuBean=new StateRenwuBean(taskId,Integer.parseInt(logId),"400",TimeUtil.getDtae());
//                stateDao.updatePerson(stateRenwuBean);
                SPUtils.putBoolean(context,"isTag",false);
                return;
            } else {
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {//判断是否在支付宝主界面
                    LogUtils.d("已经登录支付宝");
                    time = 0;
                    boolean switchFlag = true;
                    astrict = true;
                    switch (task) {
                        case -1://加好友
                            wxUtils.DeletPhone(context);
                            int aliPayNum = 0;
                            while (switchFlag || switchAccount()) {
                                aliPayNum++;
                                wxUtils.DeletPhone(context);
                                switchFlag = false;
                                ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                //wxUtils.DeletPhone(context);
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                SendFrendsNum();//将好友数量发送至后台在进行好友申请添加
                                if (is_username) {
                                    ZFBHttpPhone();//请求网络获取电话号码并且添加联系人
                                    if (wxUtils.getContactCount(context) < 1) {
                                        ShowToast.show("目前该手机没有电话联系人，需重新请求一次", (Activity) context);
                                        ZFBHttpPhone();//请求网络获取电话号码并且添加联系人
                                    } else {
                                        AddFriend();
                                    }
                                }

                            }
                            if (aliPayNum != 5) {
                                statue = "支付宝账号不足5个";
                                String uid = SPUtils.getString(context, "uid", "0000");
                                getGuzhang(statue + ":log_id的值是:" + logId + "：故障与账号无关", zfbNames, uid);
                            }
                            //wxUtils.adb("input keyevent 4");
                            break;
                        case -2://拉群
                            LogUtils.d("群上限："+qunMaxNum+"_______"+"拉群数上限"+pullMax);

                            AlipayAccountFlockClickBean alipayAccountFlockClickBean = new AlipayAccountFlockClickBean();
                            alipayAccountFlockClickBean.setUid(SPUtils.getString(context, "uid", "0000"));
                            clickAccountBenaList = new ArrayList<>();
                            while (switchFlag || switchAccount()) {
                                switchFlag = false;
                                astrict = true;//重置支付宝是否被被限制
                                getName_zfb();//获取支付宝账号
                                if (isAstrictRemark) {//分男女
                                    startAlterNameMark();//修改备注
                                    if (astrict)//判断支付宝账号是否被限制
                                        addCrowdMark();//拉群
                                } else {//不分男女
                                    startAlterName();//修改备注
                                    addCrowd();//拉群
                                }
                            }
                            alipayAccountFlockClickBean.setAccount(clickAccountBenaList);
                            String strClick = gson.toJson(alipayAccountFlockClickBean);
                            LogUtils.e(strClick);
                            updata_group_member_count(0, strClick);//上传群点击数
                            break;
                        case -3://获取群人数
                            AlipayAccountFlockNumBean alipayAccountFlockNumBean = new AlipayAccountFlockNumBean();
                            alipayAccountFlockNumBean.setUid(SPUtils.getString(context, "uid", "0000"));
                            accountBeanList = new ArrayList<>();
                            while (switchFlag || switchAccount()) {//上传群成员数量
                                switchFlag = false;
                                getName_zfb();//获取支付宝账号
                                getFlock();//群数量
                            }
                            alipayAccountFlockNumBean.setAccount(accountBeanList);
                            String str = gson.toJson(alipayAccountFlockNumBean);
                            LogUtils.e(str);
                            updata_group_member_count(1, str);
                            break;
                        case -4://好友统计
                            while (switchFlag || switchAccount()) {
                                switchFlag = false;
                                SendFrendsNum();
                            }
                            break;
                        case -5://群里发消息
                            while (switchFlag || switchAccount()) {//上传群成员数量
                                switchFlag = false;
                                getName_zfb();//获取支付宝账号
                                sendMessageFlock(messageData);//群里发消息
                            }
                            break;
                        case -6:
                            wxUtils.DeletPhone(context);
                            int aliPayNums = 0;
                            while (switchFlag || switchAccount()) {
                                aliPayNums++;
                                wxUtils.DeletPhone(context);
                                switchFlag = false;
                                ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                SendFrendsNum();//将好友数量发送至后台在进行好友申请添加
                                ZFBHttpPhone();//请求网络获取电话号码并且添加联系人
                                if (wxUtils.getContactCount(context) < 1) {
                                    ShowToast.show("目前该手机没有电话联系人，需重新请求一次", (Activity) context);
                                    ZFBHttpPhone();//请求网络获取电话号码并且添加联系人
                                } else {
                                    AddFriend();
                                }
                            }
                            if (aliPayNums != 5) {
                                statue = "支付宝账号不足5个";
                                String uid = SPUtils.getString(context, "uid", "0000");
                                getGuzhang(statue + ":log_id的值是:" + logId + "：故障与账号无关", zfbNames, uid);
                            }
                            break;
                    }
                } else {//不在微信主界面，跳转到主界面 TODO
                    backHome();
                    time = 1000;
                    task(task);
                }
            }
            backHome();
        } else {//支付宝没有安装，下载安装支付宝
            downAlipay();
            statue = "已安装支付宝需加号";
            String uid = SPUtils.getString(context, "uid", "0000");
            getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
        }
        // StatuRequest_ZFB();
    }


    /**
     * 下载支付宝
     */
    private void downAlipay() {
        String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alipay.apk";
        if (!new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "alipay.apk").exists()) {//不存在，下载
            try {
                LogUtils.d("开始下载支付宝任务");
                File filr = wxUtils.getFileAliPay("http://103.94.20.102:8087/download/alipay.apk", "alipay.apk");

                LogUtils.d("下载完成开始安装");
                wxUtils.adb("pm install -r " + path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogUtils.d("开始安装支付宝");
            wxUtils.adb("pm install -r " + path);
        }
    }

    /**
     * 下载apk
     */
    public void downApk(String url) {
        if (StringUtils.isEmpty(url)) {
            statue = "apk下载失败，下载链接错误";
            LogUtils.e("apk下载失败，下载链接错误");
            String uid = SPUtils.getString(context, "uid", "0000");
            getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
            return;
        }

        String fileUrl = url.replace("\\", "/");
        LogUtils.e(fileUrl);
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")).replace("/", "").replace(" ", "");
        LogUtils.e(fileName);

        String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + fileName;
        if (!new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), fileName).exists()) {//不存在，下载
            File file = null;
            try {
                LogUtils.d("开始下载apk任务");
                file = wxUtils.getFileAliPay(fileUrl, fileName);

                LogUtils.d("下载完成开始安装");
                wxUtils.adb("pm install -r " + path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (file == null) {
                statue = "apk下载失败，下载链接错误";
                LogUtils.e("apk下载失败，下载链接错误");
                String uid = SPUtils.getString(context, "uid", "0000");
                getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
            }
        } else {
            LogUtils.d("开始安装");
            wxUtils.adb("pm install -r " + path);
            LogUtils.d("安装完成");
        }

    }

    /**
     * 关闭支付宝广告页面
     */
    private void ClosedImage() {
        xmlData = wxUtils.getXmlData();
        List<String> image_list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < image_list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(image_list.get(i)).getNode();
            if ("com.alipay.android.phone.discovery.o2ohome:id/image_close".equals(nodeBean.getResourceid())) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击广告×按钮
            }

        }

    }

    /**
     * 统计好友数量
     */
    private void StatisticsFriend() {
        backHome();
        wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//通讯录按钮
        w:
        while (true) {
            xmlData = wxUtils.getXmlData();
            String TAG = xmlData;
            List<String> friend_num = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < friend_num.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(friend_num.get(i)).getNode();
                if ("com.alipay.mobile.socialsdk:id/tv_total_count".equals(nodeBean.getResourceid())) {
                    zfb_friend_num = nodeBean.getText();//获取支付宝好友数量的接口
                    zfb_friend_num = zfb_friend_num.replace("个朋友", "");
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回
                    LogUtils.d("支付宝好友的数量" + zfb_friend_num);
                    break w;
                }
            }
            wxUtils.adbUpSlide(context);
            xmlData = wxUtils.getXmlData();
            if (TAG.equals(xmlData)) {
                break w;
            }
        }
    }

    /**
     * 发送申请好友的数量
     */
    private void SendSenQFrendsNum() {
        final String uid = SPUtils.getString(context, "uid", "0000");
        RequestParams params = new RequestParams(zfb_urls.SendShenQFrendNum());
        params.addQueryStringParameter("account", zfbNames);
        params.addQueryStringParameter("num", send_friend_num + "");
        params.addQueryStringParameter("uid", uid);
        LogUtils.d("支付宝将好友数量发送到后台的请求地址是" + zfb_urls.SendShenQFrendNum() + "?account=" + zfbNames + "&num=" + send_friend_num + "&uid=" + uid);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
            @Override
            public void onSuccess(LogidBean bean) {
                LogUtils.d("支付宝申请添加好友数量上传后台所返回的结果" + bean.getRet());
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
//                if (errorCode!=200){
//                    statue = "发送申请好友数量网络出现故障";
//                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
//                    ShowToast.show("网络请求失败，请检测网络", (Activity) context);
//                }
            }
        });
    }

    /**
     * 将好友数量发送到后台
     */
    private void SendFrendsNum() {
        getName_zfb();//获取支付宝账户号
        StatisticsFriend();//获取好友的数量
        final String uid = SPUtils.getString(context, "uid", "0000");
        RequestParams params = new RequestParams(zfb_urls.SendFrendNum());
        params.addQueryStringParameter("account", zfbNames);
        params.addQueryStringParameter("num", zfb_friend_num + "");
        params.addQueryStringParameter("uid", uid);
        LogUtils.d("支付宝将好友数量发送到后台的请求地址是" + zfb_urls.SendFrendNum() + "?account=" + zfbNames + "&num=" + zfb_friend_num + "&uid=" + uid);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
            @Override
            public void onSuccess(LogidBean bean) {
                LogUtils.d("支付宝将好友数量上传到后台的请求结果返回的是" + bean.getRet());
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
//                if (errorCode!=200){
//                    statue = "将好友数量发送到后台出现故障";
//                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
//                    ShowToast.show("网络请求失败，请检测网络", (Activity) context);
//                }
            }
        });
    }

    /**
     * 检测多个账号
     */
    private void CheckDuo() {
        xmlData = wxUtils.getXmlData();
        List<String> shangxian = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < shangxian.size(); a++) {
            NodeXmlBean.NodeBean nodeBean_shangxian = wxUtils.getNodeXmlBean(shangxian.get(a)).getNode();
            if ("com.alipay.mobile.contactsapp:id/user_account".equals(nodeBean_shangxian.getResourceid())) {//如果有多个账号 则点击第一个账号进行添加
                listXY = wxUtils.getXY(nodeBean_shangxian.getBounds());//获取添加坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加
            }
        }
    }

    /**
     * 检测是否添加好友后直接添加不需要验证
     */
    private void Check_send() {
        xmlData = wxUtils.getXmlData();
        List<String> list_send = wxUtils.getNodeList(xmlData);
        for (int s = 0; s < list_send.size(); s++) {
            NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(list_send.get(s)).getNode();
            if ("com.alipay.mobile.socialsdk:id/btn_send_message".equals(nodeBean1.getResourceid())) {
                if ("发消息".equals(nodeBean1.getText())) {
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回
                }
            }
        }
    }

    /**
     * 添加支付宝好友
     */
    private void AddFriend() {
        send_friend_num = 0;
        boolean is_tag = true;
        String str_name = "";
        String sendName = "";
        String neirong = "";//验证信息
        if (phoneUtils.getAPNType() == 0) {

            ShowToast.show("目前该手机暂无网络，无法完成操作", (Activity) context);
        }
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y363, R.dimen.x80, R.dimen.y400);//点底部支付宝按钮
        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击添加按钮
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y51, R.dimen.x320, R.dimen.y91);//点击添加朋友
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y94, R.dimen.x320, R.dimen.y139);//d点击添加手机联系人按钮
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        boolean mark = true;
        while (mark) {
            List<String> zfb_friend = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("添加")) {
                for (int i = 0; i < zfb_friend.size(); i++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(zfb_friend.get(i)).getNode();
                    if ("com.alipay.mobile.contactsapp:id/contact_item_name".equals(nodeBean.getResourceid())) {
                        str_name = nodeBean.getText();
                    }

                    if ("com.alipay.mobile.contactsapp:id/contact_item_name".equals(nodeBean.getResourceid())) {
                        if (!sendName.contains(str_name)) {
                            if (chen_limit > send_friend_num) {
                                LogUtils.d("chen_limit的值是" + chen_limit + ":senD_friend_num的值是" + send_friend_num);
                                sendName = sendName + str_name + ",";
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击用户
                                CheckDuo();//检测是否包含多个账号
                                xmlData = wxUtils.getXmlData();
                                List<String> zfb_add = wxUtils.getNodeList(xmlData);
                                for (int a = 0; a < zfb_add.size(); a++) {
                                    NodeXmlBean.NodeBean nodeBean_add = wxUtils.getNodeXmlBean(zfb_add.get(a)).getNode();
                                    if ("com.alipay.mobile.socialsdk:id/btn_add_to_contact".equals(nodeBean_add.getResourceid())) {
                                        if ("添加到通讯录".equals(nodeBean_add.getText())) {
                                            listXY = wxUtils.getXY(nodeBean_add.getBounds());//获取添加到通讯罗
                                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加到通讯录按钮
                                            Check_send();
                                            is_tag = true;
                                            xmlData = wxUtils.getXmlData();
                                            int ps = 0;
                                            List<String> duoci_s = wxUtils.getNodeList(xmlData);
                                            for (int k = 0; k < duoci_s.size(); k++) {
                                                NodeXmlBean.NodeBean nodeBean_duoci = wxUtils.getNodeXmlBean(duoci_s.get(k)).getNode();
                                                if (xmlData.contains("请求已达上限")) {
                                                    if (ps < 2) {
                                                        if ("android:id/button1".equals(nodeBean_duoci.getResourceid())) {
                                                            if ("确定".equals(nodeBean_duoci.getText())) {
                                                                listXY = wxUtils.getXY(nodeBean_duoci.getBounds());//获取添加坐标
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击确定
                                                                statue = "添加好友次数达到上限";
                                                                String uid = SPUtils.getString(context, "uid", "0000");
                                                                getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
                                                                ps++;
                                                                is_tag = false;
                                                                SendSenQFrendsNum();
                                                                send_friend_num = 0;
                                                                sendName = "";
                                                                backHome();
                                                                return;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (is_tag == true) {
                                                LogUtils.d("你进来了吗？？？？？？？");
                                                if (StringUtils.isEmpty(contact_verify_msg)) {
                                                    neirong = "你好，加一下你可以吗";
                                                } else {
                                                    neirong = contact_verify_msg.replaceAll("《name》", str_name);
                                                    LogUtils.d("你所发送的验证消息是" + neirong);
                                                }
                                                wxUtils.adbDimensClick(context, R.dimen.x279, R.dimen.y95, R.dimen.x304, R.dimen.y112);//点击清除
                                                ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                                cm.setText(neirong);
                                                int x = context.getResources().getDimensionPixelSize(R.dimen.y160);
                                                int y = context.getResources().getDimensionPixelSize(R.dimen.y98);//EdiText
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 2500);  //长按EdiText
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                send_friend_num++;
                                                wxUtils.adbDimensClick(context, R.dimen.x15, R.dimen.y59, R.dimen.x61, R.dimen.y84);//粘贴
                                                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y22, R.dimen.x308, R.dimen.y45);//点击发送
                                                xmlData = wxUtils.getXmlData();
                                                int p = 0;
                                                boolean duocis_tag = true;
                                                List<String> duoci = wxUtils.getNodeList(xmlData);
                                                for (int k = 0; k < duoci.size(); k++) {
                                                    NodeXmlBean.NodeBean nodeBean_duoci = wxUtils.getNodeXmlBean(duoci.get(k)).getNode();
                                                    if (xmlData.contains("今天已经发送太多好友申请了，明天再来吧。")) {
                                                        if (p < 2) {
                                                            if ("android:id/button1".equals(nodeBean_duoci.getResourceid())) {
                                                                if ("确定".equals(nodeBean_duoci.getText())) {
                                                                    listXY = wxUtils.getXY(nodeBean_duoci.getBounds());//获取添加坐标
                                                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击确定
                                                                    statue = "添加好友次数达到上限";
                                                                    String uid = SPUtils.getString(context, "uid", "0000");
                                                                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
                                                                    p++;
//                                                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);
//                                                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);
//                                                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);
//                                                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);
                                                                    duocis_tag = false;
                                                                    SendSenQFrendsNum();
                                                                    send_friend_num = 0;
                                                                    sendName = "";
                                                                    backHome();
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                try {
                                                    Thread.sleep(3000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                ZfbFenghao();//如果支付宝已经被封号 进行处理
                                                if (duocis_tag == true) {
                                                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);
                                                }
                                            }
                                            //  }
                                        }
                                    }
                                }
                                LogUtils.s("发送了多少个添加账号的请求。" + send_friend_num);
                                int max = 3;
                                int min = 1;
                                Random random = new Random();
                                int s = random.nextInt(max) % (max - min + 1) + min;
                                try {
                                    Thread.sleep(s * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ShowToast.show("添加好友，数量达到上限", (Activity) context);
                                mark = false;
                                return;
                            }
                        }
                    }
                }
                if (send_friend_num == 0) {
                    statue = "获取的手机号码无对应的支付宝账户或该账号访问联系人被限制";
                    String uid = SPUtils.getString(context, "uid", "0000");
                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
                }
                String TAG = xmlData;
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (TAG.equals(xmlData)) {
                    break;
                }
            } else {
                mark = false;
                ShowToast.show("没有可添加的好友，当前账号任务结束", (Activity) context);
                backHome();
                return;

            }
        }
        SendSenQFrendsNum();
        send_friend_num = 0;
        sendName = "";
        backHome();
    }

    /**
     * 支付宝账号被封号几天的情况
     */
    private void ZfbFenghao() {
        String times_fenghao = "";
        String neitong = "";
        boolean mark = true;
        if (xmlData.contains("你需要发送验证申请，等对方通过") && xmlData.contains("朋友验证")) {
            statue = "该账号已经被封号";
            LogUtils.d("作出账号被封号的处理");
            String uid = SPUtils.getString(context, "uid", "0000");
            getGuzhang(statue + ":log_id的值是:" + logId + ":故障账号是:" + zfbNames, zfbNames, uid);
            backHome();
            wxUtils.adbDimensClick(context, R.dimen.x160, R.dimen.y363, R.dimen.x240, R.dimen.y400);//点击朋友按钮
            while (mark) {
                xmlData = wxUtils.getXmlData();
                List<String> nodeList = wxUtils.getNodeList(xmlData);
                for (int i = 0; i < nodeList.size(); i++) {
                    NodeXmlBean.NodeBean nodeBean_duoci = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
                    if ("com.alipay.mobile.socialwidget:id/item_name".equals(nodeBean_duoci.getResourceid())) {
                        if ("消息中心".equals(nodeBean_duoci.getText())) {
                            listXY = wxUtils.getXY(nodeBean_duoci.getBounds());//点击消息中心
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击确定
                            xmlData = wxUtils.getXmlData();
                            List<String> node_time = wxUtils.getNodeList(xmlData);
                            for (int k = 0; k < node_time.size(); k++) {
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(node_time.get(k)).getNode();
                                if ("com.alipay.android.phone.messageboxapp:id/tv_system_todo_date".equals(nodeBean.getResourceid())) {
                                    times_fenghao = nodeBean.getText();//获取封号时间
                                    LogUtils.d("封号的时间是" + times_fenghao);
                                }
                                if ("com.alipay.android.phone.messageboxapp:id/tv_system_todo_content".equals(nodeBean.getResourceid())) {
                                    neitong = nodeBean.getText();
                                    neitong = neitong.substring(13, 14);
                                    LogUtils.d("封号的天数是" + neitong);
                                    RequestParams params = new RequestParams(zfb_urls.Title_zfb_fenghao());
                                    params.addQueryStringParameter("uid", uid);
                                    params.addQueryStringParameter("account", zfbNames);
                                    params.addQueryStringParameter("frozen_time", times_fenghao);
                                    params.addQueryStringParameter("frozen_day", neitong);
                                    LogUtils.d("支付宝账号被封号返回的接口是:" + zfb_urls.Title_zfb_fenghao() + "?uid=" + uid + "&account=" + zfbNames + "&frozen_time=" + times_fenghao + "&frozen_day=" + neitong);
                                    HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
                                        @Override
                                        public void onSuccess(LogidBean bean) {
                                            LogUtils.d("支付宝被封号请求的数据结果是:" + bean.getRet());
                                        }

                                        @Override
                                        public void onFailure(int errorCode, String errorString) {

                                        }
                                    });
                                }
                            }

                        }
                    }
                }
                String TAG = xmlData;
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (TAG.equals(xmlData)) {
                    break;
                }
            }
            return;
        }
    }

    /**
     * 支付宝账号请求电话号码
     */
    private void ZFBHttpPhone() {
        String uid = SPUtils.getString(context, "uid", "0000");
        String phone_name = "";
        int limit = 0;
        String add_phone = "";
        if (ali_add_num_s == 0 || ali_add_num_e == 0) {
            ali_add_num_s = 30;
            ali_add_num_e = 30;
            limit = 30;
        } else {
            int max = ali_add_num_e;
            int min = ali_add_num_s;
            Random random = new Random();
            limit = random.nextInt(max) % (max - min + 1) + min;
            chen_limit = limit;
            LogUtils.d("limit*1.2:" + limit * 1.2);
            LogUtils.d("(int) Math.ceil(limit*1.2):" + (int) Math.ceil(limit * 1.2));
            limit = (int) Math.ceil(limit * 1.2);
        }
        RequestParams params = new RequestParams(zfb_urls.AddPhone());
        params.addQueryStringParameter("uid", uid);//uid
        params.addQueryStringParameter("limit", limit + "");//limit
        LogUtils.d("支付宝请求的接口地址是:" + zfb_urls.AddPhone() + "?uid=" + uid + "&limit=" + limit);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<PhoneBean>() {
            @Override
            public void onSuccess(PhoneBean phoneBean) {
                for (int i = 0; i < phoneBean.getData().size(); i++) {
                    wxUtils.addContact(phoneBean.getData().get(i).getAlipay_account(), phoneBean.getData().get(i).getContact(), context);
                    LogUtils.d("电话号码为" + phoneBean.getData().get(i).getContact());
                    ShowToast.show("电话号码" + phoneBean.getData().get(i).getContact(), (Activity) context);
                }
            }

            @Override
            public void onFailure(int errorCode, String errorString) {

            }
        });
        ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
        ShowToast.show("添加手机号码成功,请稍后20s...", (Activity) context);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adb("input keyevent KEYCODE_HOME");//回到home
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.openAliPay();//回到支付宝页面
    }


    /**
     * 将任务故障发送到后台进行请求
     *
     * @param statue   错误的故障状态
     * @param zfbNames 支付宝用户名
     * @param uid      手机uid
     */
    private void getGuzhang(String statue, String zfbNames, String uid) {
        RequestParams params = new RequestParams(zfb_urls.SendStatue());
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("info", statue);
        params.addQueryStringParameter("account", zfbNames);
        LogUtils.d("支付宝故障列表返回接口是:" + zfb_urls.SendStatue() + "?&uid=" + uid + "&info=" + statue + "&account" + zfbNames);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
            @Override
            public void onSuccess(LogidBean bean) {
                LogUtils.d("故障列表返回结果:" + bean.getRet());
            }

            @Override
            public void onFailure(int errorCode, String errorString) {

            }
        });
    }

    /**
     * 判断app是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    private boolean isInstallApp(Context context, String packageName) {
        try {
            mPackageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    //----------------------------------------转过来的-------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 上传群数据
     *
     * @param json
     * @param type 0上传群点击数  1上传群成员
     */
    private void updata_group_member_count(int type, String json) {
        RequestParams params = null;
        if (type == 0) {
            params = new RequestParams(ZFB_URLS.updata_group_rquest_count());
            params.addQueryStringParameter("json", json);
            LogUtils.d(ZFB_URLS.updata_group_rquest_count() + "?json=" + json);
        } else if (type == 1) {
            params = new RequestParams(ZFB_URLS.updata_group_member_count());
            params.addQueryStringParameter("json", json);
            LogUtils.d(ZFB_URLS.updata_group_member_count() + "?json=" + json);
        }

        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                LogUtils.d("群信息上传成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("群信息上传失败");
            }
        });
    }


    /**
     * 设置logId
     *
     * @param logId
     */
    public void setLogId(String logId, int taskId, String ali_add_num) {
        this.logId = logId;
        this.taskId = taskId;
        this.ali_add_num = ali_add_num;
    }

    public void setLogId(int taskId, String logId, int ali_add_num_s, int ali_add_num_e, String contact_verify_msg) {
        this.ali_add_num_s = ali_add_num_s;
        this.ali_add_num_e = ali_add_num_e;
        this.logId = logId;
        this.taskId = taskId;
        this.contact_verify_msg = contact_verify_msg;
    }

    /**
     * 获取群成员数量
     */
    private void getFlock() {
        //初始化数据
        bogCount = 0;//要改名的男好友
        girlCount = 0;//要改名的女好友
        countStr = "";//判断是否已经选择
        refuseAdd = "";//初始化拒绝拉群的人
        boyEnd = true;//如果是flase带表男群拉完
        girlEnd = true;//如果是flase带表女群拉完

        List<AlipayAccountFlockNumBean.AccountBean.FlockBean> flockBeanList = new ArrayList<>();
        //_________________________________________

        String qunClickMark = "";
        List<String> nodeList;
        wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮

        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
        //进入了群列表
        w:
        while (true) {
            while (true) {//必须跳转到通讯录

                if (xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
                    status = 0;
                    wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                    return;
                }

                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (xmlData.contains("通讯录") && xmlData.contains("新的朋友") && xmlData.contains("群聊")) {
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
                } else if (!xmlData.contains("群聊") || !xmlData.contains("返回") || !xmlData.contains("搜索")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }

            if (!(xmlData.contains("群聊") && xmlData.contains("返回") && xmlData.contains("搜索"))) {
//                ShowToast.show("任务被中断...", (Activity) context);
                continue w;
            }

            if (xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
                wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                return;
            }

            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid())) {//"A000101"

                    if (nodeBean.getText().length() < 7) {
                        continue;
                    }
                    if (!(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B"))) {
                        continue;
                    }

                    String qunFlockName = "";
                    if (qunClickMark.contains(nodeBean.getText())) {//进过的群
                        continue;
                    } else {
                        qunFlockName = nodeBean.getText();
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        qunClickMark = qunClickMark + nodeBean.getText() + ",";

                    }

                    //_______________________________________________________________________________________________
                    String qunName = "";
                    //获取群人数，男女群信息
                    String qunNameData = wxUtils.getXmlData();
                    List<String> qunNameDataList = wxUtils.getNodeList(qunNameData);

                    if (!(qunNameData.contains("com.alipay.mobile.chatapp:id/voiceSwitchBtn") && qunNameData.contains("com.alipay.mobile.chatapp:id/chat_expression_ctr_btn"))) {//判断是否在拉群页面

//                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                        continue w;
                    }


                    for (int c = 0; c < qunNameDataList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
//                        LogUtils.d(qunNameBean.toString());
                        if ("com.alipay.mobile.ui:id/title_bar_title".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }
                    if (qunName.length() >= 10) {
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(qunName.substring(qunName.length() - 3));
                        int qb = Integer.parseInt(m.replaceAll("").trim());//群人数
                        if (true) {//超过群人数
                            int sex = 2;//0代表女群
                            if ((qunName.contains("a") || qunName.contains("A")) && boyEnd) {//a代表男群
                                sex = 1;
                            } else if ((qunName.contains("b") || qunName.contains("B")) && girlEnd) {
                                sex = 0;
                            } else {
                                sex = 2; //TODO
                            }
                            if (sex == 0 || sex == 1||sex==2) {
//                                addMember(sex, qb);
                                AlipayAccountFlockNumBean.AccountBean.FlockBean flockBean = new AlipayAccountFlockNumBean.AccountBean.FlockBean();
                                flockBean.setFlock_name(qunFlockName);
                                flockBean.setFlock_num(qb + "");
                                flockBeanList.add(flockBean);
                                wxUtils.adb("input keyevent 4");
                                continue;
                            }
                        }
                    }
                    //_______________________________________________________________________________________________
                    wxUtils.adb("input keyevent 4");
                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData)) {
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                break;
            }
        }
        AlipayAccountFlockNumBean.AccountBean accountBean = new AlipayAccountFlockNumBean.AccountBean();
        accountBean.setAlipay_account(zfbNames);
        accountBean.setFlock(flockBeanList);
        accountBeanList.add(accountBean);
        LogUtils.d(bogCount + "___girl" + girlCount);
    }

    private int status = 5;//0没群 1男群满  2女群满  3男女都满  4失败 5正常
    public int qunMaxNum = 50;//设置群人数
    public int pullMax=1000;
    boolean boyEnd = true;//如果是flase带表男群拉完
    boolean girlEnd = true;//如果是flase带表女群拉完
    boolean neutralEnd = true;//如果是flase带表群拉完

    /**
     * 拉群
     */
    private void addCrowd() {
        //初始化数据
        bogCount = 0;//要改名的男好友
        girlCount = 0;//要改名的女好友
        neutralCount = 0;//要改名的总数
//        countAddStr = "";//判断是否已经勾选过
        countStr = "";//判断是否已经选择
        boyEnd = true;//如果是flase带表男群拉完
        girlEnd = true;//如果是flase带表女群拉完
        neutralEnd = true;
        List<AlipayAccountFlockClickBean.AccountBean.FlockBean> flockBeanList = new ArrayList<>();
        //_________________________________________

        String qunClickMark = "";
        List<String> nodeList;

        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
        //进入了群列表
        w:
        while (true) {
            while (true) {//必须跳转到通讯录
                xmlData = wxUtils.getXmlData();

                if (xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
                    status = 0;
                    wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                    return;
                }


                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (xmlData.contains("通讯录") && xmlData.contains("新的朋友") && xmlData.contains("群聊")) {
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
                } else if (!xmlData.contains("群聊") || !xmlData.contains("返回") || !xmlData.contains("搜索")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }

            if (!(xmlData.contains("群聊") && xmlData.contains("返回") && xmlData.contains("搜索"))) {
//                ShowToast.show("任务被中断...", (Activity) context);
                continue w;
            }

            if (xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
                status = 0;
                wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                return;
            }


            List<String> nodeListA = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeListA.size(); a++) {

                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeListA.get(a)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid())) {//"A000101"
                    if (nodeBean.getText().length() < 7) {
                        continue;
                    }
                    if (!(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B"))) {
                        continue;
                    }
                    LogUtils.e("neutralEnd=" + neutralEnd);
                    if (!neutralEnd) {//拉群任务完成
                        wxUtils.adb("input keyevent 4");
                        ShowToast.show("拉群任务完成", (Activity) context);
                        LogUtils.e("拉群任务完成");
                        break w;
                    }


                    String qunFlockName = "";
                    if (qunClickMark.contains(nodeBean.getText())) {//进过的群
                        continue;
                    } else {
                        qunFlockName = nodeBean.getText();
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        qunClickMark = qunClickMark + nodeBean.getText() + ",";
                    }

                    //_______________________________________________________________________________________________
                    String qunName = "";
                    //获取群人数，男女群信息
                    String qunNameData = wxUtils.getXmlData();
                    List<String> qunNameDataList = wxUtils.getNodeList(qunNameData);
                    if (!(qunNameData.contains("com.alipay.mobile.chatapp:id/voiceSwitchBtn") && qunNameData.contains("com.alipay.mobile.chatapp:id/chat_expression_ctr_btn"))) {//判断是否在拉群页面

//                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                        continue w;
                    }


                    for (int c = 0; c < qunNameDataList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
//                        LogUtils.d(qunNameBean.toString());
                        if ("com.alipay.mobile.ui:id/title_bar_title".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }
                    if (qunName.length() >= 10) {
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(qunName.substring(qunName.length() - 3));
                        int qb = Integer.parseInt(m.replaceAll("").trim());//群人数
                        if (qb < qunMaxNum) {//超过群人数
                            int sex = 2;//0代表女群
                            if ((qunName.contains("a") || qunName.contains("A")) && boyEnd) {//a代表男群
                                sex = 1;
                            } else if ((qunName.contains("b") || qunName.contains("B")) && girlEnd) {
                                sex = 0;
                            } else {
                                sex = 2; //TODO
                            }
                            if (sex == 0 || sex == 1 || sex == 2) {
                                come_num = 0;
                                addMember(sex, qb);//拉群选择成员


                                AlipayAccountFlockClickBean.AccountBean.FlockBean flockBean = new AlipayAccountFlockClickBean.AccountBean.FlockBean();
                                flockBean.setFlock_name(qunFlockName);
                                flockBean.setClick_num(clickCount + "");
                                flockBean.setCome_num("" + (come_num - qb));
                                flockBeanList.add(flockBean);
                                LogUtils.e(come_num - qb + "__come_num:" + come_num + "qb:" + qb);
                                continue;
                            }
                        }
                    }
                    //_______________________________________________________________________________________________
                    wxUtils.adb("input keyevent 4");
                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            LogUtils.e("向上滑动a");
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData)) {
                wxUtils.adb("input keyevent 4");
                LogUtils.e("拉群任务完成");
                ShowToast.show("拉群任务完成", (Activity) context);
                break;
            }
        }

        AlipayAccountFlockClickBean.AccountBean accountBean = new AlipayAccountFlockClickBean.AccountBean();
        accountBean.setAlipay_account(zfbNames);
        accountBean.setFlock(flockBeanList);
        clickAccountBenaList.add(accountBean);

        LogUtils.d( "改名总数：" + neutralCount);
        LogUtils.d("拒绝拉群的:" + refuseAdd);
        //拉完群改名
        ShowToast.show("拉群完，修改备注开始", (Activity) context);
        qunEndAlterNameRefuseAdd();//修改拒绝的人
        wxUtils.adb("input keyevent 4");
        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
        qunEndAlterName();
    }

    /**
     * 拉完群改名修改备注. 不分男女
     */
    private void qunEndAlterName() {
        int neutralAlterCount = 0;
        boolean bottom = false;//到了底部
        DecimalFormat df = new DecimalFormat("0000");
        String endData = "";
        String alterName = "";
        xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
        w:
        while (true) {

            if (neutralAlterCount >= neutralCount) {
                ShowToast.show("拉群后改名完成", (Activity) context);
                break w;
            }

            while (true) {//必须跳转到通讯录
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }

            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && countStr.contains(nodeBean.getText()) && !alterName.contains(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);

                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
//                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (xmlData.contains("设置备注")) {
                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y235, R.dimen.x280, R.dimen.y235);//设置备注
                    } else {
                        continue;
                    }


                    //判断是否在修改备注界面
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("备注信息") && xmlData.contains("备注名"))) {
                        continue w;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y106, R.dimen.x320, R.dimen.y106);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x294, R.dimen.y106, R.dimen.x294, R.dimen.y106);//清空名字

                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '1';
                        sexStr = new String(ch);
                    }
                    LogUtils.e("input text \"" + sexStr + "\"");
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    LogUtils.d("修改后删除数据库"+DataSupport.deleteAll(AlipayAlterNameSqliteBean.class, "alipayname = ? and newName = ?", zfbNames,name));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (true){
                        xmlData = wxUtils.getXmlData();
                        if(xmlData.contains("备注信息")&&xmlData.contains("备注名")){
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            break ;
                        }
                    }



                    alterName = alterName + name + ",";
                    neutralAlterCount++;
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains(name)) {//判断修改后是否名字没有改变
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }

                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("通讯录")) {
//                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && countStr.contains(nodeBean.getText())) {
                    if (neutralAlterCount < neutralCount) {
                        continue w;
                    }
                }
            }
            if (!bottom) {
                wxUtils.adbUpSlide(context);//向上滑动aaaadrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                ShowToast.show("修改备注完成,拉群任务完成", (Activity) context);
                LogUtils.d(neutralAlterCount + "修改备注完成,拉群任务完成" + neutralCount);
                break w;
            }
            if (xmlData.contains("个朋友")) {//判断是否到达底部
                bottom = true;
            }
        }
    }

    /**
     * 拉完群修改拒绝添加的人
     */
    private void qunEndAlterNameRefuseAdd() {
        if (StringUtils.isEmpty(refuseAdd)) {
            return;
        }
        String[] refuseAdds = refuseAdd.split(",");
        int refuseAddCount = 0;
        boolean bottom = false;//到了底部
        DecimalFormat df = new DecimalFormat("0000");
        String endData = "";

        xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
        w:
        while (true) {
            if (refuseAddCount >= refuseAdds.length) {
//                ShowToast.show("拒绝加群的改名完成", (Activity) context);
                break w;
            }

            while (true) {//必须跳转到通讯录
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }

            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && refuseAdd.contains(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);
                    if (refuseAddCount >= refuseAdds.length) {
                        break w;
                    }
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (xmlData.contains("设置备注")) {
                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y235, R.dimen.x280, R.dimen.y235);//设置备注
                    } else {
                        continue;
                    }
                    //判断是否在修改备注界面
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("备注信息") && xmlData.contains("备注名"))) {
                        continue w;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y106, R.dimen.x320, R.dimen.y106);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x294, R.dimen.y106, R.dimen.x294, R.dimen.y106);//清空名字


                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '9';
                        sexStr = new String(ch);
                    }
                    LogUtils.e("input text \"" + sexStr + "\"");
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改

                    LogUtils.d("修改后删除数据库"+DataSupport.deleteAll(AlipayAlterNameSqliteBean.class, "alipayname = ? and newName = ?", zfbNames,name));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (true){
                        xmlData = wxUtils.getXmlData();
                        if(xmlData.contains("备注信息")&&xmlData.contains("备注名")){
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            break ;
                        }
                    }

                    refuseAddCount++;
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains(name)) {//判断修改后是否名字没有改变
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }


                    break;
                }
            }

            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("通讯录") && !xmlData.contains("返回")) {
//                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }

            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && refuseAdd.contains(nodeBean.getText())) {
                    if (refuseAddCount >= refuseAdds.length) {
                        break w;
                    }
                }
            }
            if (!bottom) {
                wxUtils.adbUpSlide(context);//向上滑动aaaadrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
//                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y367, R.dimen.x80, R.dimen.y400);//点击微信
//                ShowToast.show("修改拒绝进群完成", (Activity) context);
                LogUtils.d(refuseAdd + "修改备注完成,拉群任务完成");
                break w;
            }
            if (xmlData.contains("个朋友")) {//判断是否到达底部
                bottom = true;
            }
        }
        refuseAdd = "";
    }

    int bogCount = 0;//要改名的男好友
    int girlCount = 0;//要改名的男好友
    int neutralCount = 0;//要改名的总数

    String countStr = "";//判断是否已经勾选过
    String refuseAdd = "";//拒绝添加群

    int clickCount = 0;

    /**
     * 拉群添加成员
     *
     * @param qb  群当前人数
     * @param sex 性别
     */
    private void addMember(int sex, int qb) {
        clickCount = 0;//选中次数
        List<String> qunNameDataList = new ArrayList<>();
        wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//确定
            String qunNameData = wxUtils.getXmlData();
            if (!qunNameData.contains("聊天信息")) {
                return;
            }

            if (qunNameData.contains("群聊名称")) {
                qunNameDataList = wxUtils.getNodeList(qunNameData);
                for (int a = 0; a < qunNameDataList.size(); a++) {

                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(qunNameDataList.get(a)).getNode();
//                    if (nodeBean != null && nodeBean.getContentdesc() != null && "添加成员".equals(nodeBean.getContentdesc())) {
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().contains("com.alipay.mobile.chatapp:id/item_")) {
                        NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(qunNameDataList.get(a + 2)).getNode();

                        if (StringUtils.isEmpty(nodeBean1.getText())) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//添加
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加

                            xmlData = wxUtils.getXmlData();
                            if (!(xmlData.contains("选择朋友") && xmlData.contains("确定") && xmlData.contains("返回"))) {
                                wxUtils.adb("input keyevent 4");
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }

                            break;
                        }

                    }

                }

            }

        xmlData = wxUtils.getXmlData();
        if (!(xmlData.contains("选择朋友") && xmlData.contains("确定") && xmlData.contains("返回"))) {
            return;
        }


        String addNameData = wxUtils.getXmlData();
        List<String> addNameList = new ArrayList<>();
        addNameList = wxUtils.getNodeList(addNameData);
        int flagGirlCount = 0;
        int flagBoyCount = 0;
        String flagCountStr = "";
        int flagClickCount = 0;
        int flagNeutralCount = 0;

        String flagSendCount="";
        w:
        while (true) {

            for (int a = 0; a < addNameList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(addNameList.get(a)).getNode();

                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ0") && !refuseAdd.contains(nodeBean.getText())) {
                    if(a + 2>addNameList.size()-1){
                        continue ;
                    }

                    NodeXmlBean.NodeBean checkBox = wxUtils.getNodeXmlBean(addNameList.get(a + 2)).getNode();

                    if ("com.alipay.mobile.socialsdk:id/selected_check_box".equals(checkBox.getResourceid()) && checkBox.isChecked()) {//isChecked true代表选中
                        if (!countStr.contains(nodeBean.getText()) && !flagCountStr.contains(nodeBean.getText())) {
                            neutralCount++;
                            flagNeutralCount++;
                            LogUtils.d(neutralCount + "neutralCount+选中");
                            flagCountStr = flagCountStr + nodeBean.getText() + ",";
                        }
                    }

                    if ("com.alipay.mobile.socialsdk:id/selected_check_box".equals(checkBox.getResourceid()) && !checkBox.isChecked()) {
                        if (countStr.contains(nodeBean.getText()) || flagCountStr.contains(nodeBean.getText())) {
                            continue;
                        } else {
                            if (clickCount + qb < qunMaxNum) {
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
//                                    countStr = countStr + nodeBean.getText() + ",";
                                flagCountStr = flagCountStr + nodeBean.getText() + ",";
                                clickCount++;
                                flagClickCount++;
                                neutralCount++;
                                flagNeutralCount++;
                                LogUtils.d(neutralCount + "girlCount+点击");

                                flagSendCount=flagSendCount+ nodeBean.getText() + ",";
                                if(neutralCount>=pullMax){
                                    break w;
                                }
                            } else {
                                break w;
                            }
                        }
                    }
                }
            }
            String oldAddNameData = addNameData;
            wxUtils.adbQunUpSlide(context);//向上滑动
            addNameData = wxUtils.getXmlData();
            addNameList = wxUtils.getNodeList(addNameData);

            if (oldAddNameData.equals(addNameData)) {
                neutralEnd = false;
                break;
            }
            int judgeGirl = 0;
            int judgeBoy = 0;
            int judgeType = 0;
            for (int a = 0; a < addNameList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(addNameList.get(a)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ1")) {
                    judgeType++;
                }
            }
            if (judgeType >= 9) {
                neutralEnd = false;
            }
        }
        if (clickCount > 0) {//确认添加
            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定

            boolean send=true;
            wh:
            while (true) {
                String qunLiaoBack = wxUtils.getXmlData();//修改完一个重新获取页面数据

                if(qunLiaoBack.contains("当前群聊人数较多。为减少打扰，对方同意邀请后才会进入群聊，现在发送邀请？")){
                    wxUtils.adbDimensClick(context, R.dimen.x234, R.dimen.y244, R.dimen.x234, R.dimen.y244);//邀请
                }

                if(qunLiaoBack.contains("添加参与人失败")){
                    send=false;
                }

                if (qunLiaoBack.contains("添加参与人失败") && !qunLiaoBack.contains("取消")) {//拒绝添加
                    wxUtils.adb("input keyevent 4");


                    List<String> nodeList = wxUtils.getNodeList(qunLiaoBack);

                    for (int b = 0; b < nodeList.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.antui:id/message")) {
                            refuseAdd = refuseAdd + nodeBean.getText().substring(0, 9) + ",";//添加失败的人
//                            girlCount = girlCount - flagGirlCount;//添加失败，回滚
//                            bogCount = bogCount - flagBoyCount;
                            neutralCount = neutralCount - flagNeutralCount;
                            clickCount = clickCount - flagClickCount;
                            wxUtils.adb("input keyevent 4");
                            addMember(sex, qb);
                            return;
                        }
                    }
                } else {
                    countStr = countStr + flagCountStr + ",";

                    if(neutralCount>=pullMax){
                        neutralEnd = false;
                    }


                }

                if ((qunLiaoBack.contains("com.alipay.mobile.chatapp:id/voiceSwitchBtn") && qunLiaoBack.contains("com.alipay.mobile.chatapp:id/chat_expression_ctr_btn"))) {//判断是否在拉群页面

                    String qunName = "";
                    List<String> qunNameDataNewList = wxUtils.getNodeList(qunLiaoBack);
                    for (int c = 0; c < qunNameDataNewList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataNewList.get(c)).getNode();
//                        LogUtils.d(qunNameBean.toString());
                        if ("com.alipay.mobile.ui:id/title_bar_title".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }
                    if (qunName.length() >= 10) {
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(qunName.substring(qunName.length() - 3));
                        come_num = Integer.parseInt(m.replaceAll("").trim());//群人数
                    }
                    //@选中的人，发送消息----------------------------------------------
                    List<AlipayAlterNameSqliteBean> alipayAlterNameSqliteBeanList = DataSupport.where("alipayname = ?", zfbNames).find(AlipayAlterNameSqliteBean.class);
                    String message = "";
                    for (AlipayAlterNameSqliteBean alipayAlterNameSqliteBeanFor : alipayAlterNameSqliteBeanList) {
                        if (flagSendCount.contains(alipayAlterNameSqliteBeanFor.getNewName())) {
                            message = message + "@" + alipayAlterNameSqliteBeanFor.getName() + " ";
                        }
                    }
                    message = message + context.getString(R.string.alipay_message);
                    LogUtils.e(message);
                    // 将文本内容放到系统剪贴板里。
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    cm.setText(message);
                    int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                    int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//支付宝发消息

                    if(send) {
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1500);  //长按输入框
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y180, R.dimen.x62, R.dimen.y180);//粘贴
                        wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y176, R.dimen.x314, R.dimen.y198);//发送
                        wxUtils.adb("input keyevent 4");
                    }

                    wxUtils.adb("input keyevent 4");
                    break;
                } else {
                    wxUtils.adb("input keyevent 4");
                }
            }

        } else {//没有选中.返回
            come_num = qb;//群人数
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
        }
    }

    private int come_num;

    private NodeXmlBean.NodeBean nodeBean;
    private boolean astrict = true;

    /**
     * 修改备注.  不分男女
     */
    private void startAlterName() {

        while (true) {//必须跳转到通讯录
            xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {//判断是否在支付宝主界面
                backHome();
            } else {
                wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮

                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("通讯录") && xmlData.contains("新的朋友")) {
                    break;
                }
            }
        }


        boolean bottom = false;//到了底部
        int sex = 2;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("0000");
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        String meName = "";

        xmlData = wxUtils.getXmlData();
        w:
        while (true) {

            while (true) {//必须跳转到通讯录
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }


            if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
//                ShowToast.show("任务被中断...", (Activity) context);
                continue w;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            boolean hideMark = false;//判断是否隐藏真实姓名
            a:
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && !nodeBean.getText().startsWith("ZZZ") && !meName.equals(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝好友名:" + name);
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (xmlData.contains("设置备注")) {
                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y235, R.dimen.x280, R.dimen.y235);//设置备注
                    } else {
                        meName = name;
                        continue;
                    }
                    xmlData = wxUtils.getXmlData();
                    //判断是否在修改备注界面
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("备注信息") && xmlData.contains("备注名"))) {
                        continue w;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y106, R.dimen.x320, R.dimen.y106);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x294, R.dimen.y106, R.dimen.x294, R.dimen.y106);//清空名字

                    AlipayAlterNameSqliteBean alipayAlterNameSqliteBean = new AlipayAlterNameSqliteBean();//创建数据库对象
                    alipayAlterNameSqliteBean.setAlipayName(zfbNames);
                    alipayAlterNameSqliteBean.setName(name);


                    switch (sex) {//0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, zfbNames + "wx_name_number_girl_alipay", 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                            wxUtils.adb("input text \"ZZZ0B" + wx_nume_number_new_girl + "\"");
                            SPUtils.put(context, zfbNames + "wx_name_number_girl_alipay", wx_name_number_girl + 1);
                            alipayAlterNameSqliteBean.setNewName("ZZZ0B" + wx_nume_number_new_girl);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, zfbNames + "wx_name_number_boy_alipay", 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                            wxUtils.adb("input text \"ZZZ0A" + wx_nume_number_new_boy + "\"");
                            SPUtils.put(context, zfbNames + "wx_name_number_boy_alipay", wx_name_number_boy + 1);
                            alipayAlterNameSqliteBean.setNewName("ZZZ0A" + wx_nume_number_new_boy);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, zfbNames + "wx_name_number_c_alipay", 0);
                            String wx_nume_number_c = df.format(wx_name_number_c + 1);
                            wxUtils.adb("input text \"ZZZ0C" + wx_nume_number_c + "\"");
                            SPUtils.put(context, zfbNames + "wx_name_number_c_alipay", wx_name_number_c + 1);
                            alipayAlterNameSqliteBean.setNewName("ZZZ0C" + wx_nume_number_c);
                            break;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    LogUtils.e("添加数据库" + alipayAlterNameSqliteBean.save() + "内容是:" + alipayAlterNameSqliteBean.toString());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (true){
                        xmlData = wxUtils.getXmlData();
                        if(xmlData.contains("备注信息")&&xmlData.contains("备注名")){
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            break ;
                        }
                    }

                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains(name)) {//判断修改后是否名字没有改变
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }

                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
//                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && !nodeBean.getText().startsWith("ZZZ") && !meName.equals(nodeBean.getText())) {
                    continue w;
                } else if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText() != "" && nodeBean.getText().startsWith("ZZZ")) {
                    zzzNum++;
                }
            }

            if (!bottom) {
                if (zzzNum >= 10) {
                    wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y387, R.dimen.x320, R.dimen.y395);
                } else {
                    wxUtils.adbUpSlide(context);//向上滑动
                }
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                wxUtils.adb("input keyevent 4");
                wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮

                ShowToast.show("修改备注完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("个朋友")) {//判断是否到达底部
                bottom = true;
            }
        }

    }

    int backNum = 0;//返回次数

    /**
     * 返回到支付宝主页面
     */
    private void backHome() {
        backNum++;
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("新版本") || xmlData.contains("版本更新")) {//关闭版本更新
            LogUtils.d("您进来了吗啊啊啊");
            Clean_Update();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            backHome();
        } else if (xmlData.contains("com.alipay.android.phone.discovery.o2ohome:id/image_close")) {//关闭广告
            ClosedImage();
            backHome();
        } else if (!(xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {//判断是否在支付宝主界面
            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);
            if (backNum < 100) {
                backHome();
            }
        }

    }


    /**
     * 关闭版本更新
     */
    private void Clean_Update() {
        List<String> version_list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < version_list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(version_list.get(i)).getNode();
            if ("稍后再说".equals(nodeBean.getText())) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                break;
            }
        }
    }

    /**
     * 切换账号
     *
     * @return
     */
    private boolean switchAccount() {
        LogUtils.d("切换账号你进来了吗");
        xmlData = wxUtils.getXmlData();
        backHome();//返回到home
        wxUtils.adbDimensClick(context, R.dimen.x240, R.dimen.y363, R.dimen.x320, R.dimen.y400);//我的
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y51, R.dimen.x320, R.dimen.y114);//个人主页
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y60, R.dimen.x320, R.dimen.y91);//账户详情
        wxUtils.adbDimensClick(context, R.dimen.x284, R.dimen.y25, R.dimen.x308, R.dimen.y42);//账号切换
        xmlData = wxUtils.getXmlData();
        List<String> datalist = wxUtils.getNodeList(xmlData);
        List<NodeXmlBean.NodeBean> nodeBeanList = new ArrayList<>();
        int s = nodeBeanList.size();
        for (int a = 0; a < datalist.size(); a++) {//获取支付宝账号id
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(datalist.get(a)).getNode();
            if ("com.alipay.mobile.securitybiz:id/table_left_text".equals(nodeBean.getResourceid())) {
                nodeBeanList.add(nodeBean);
            }
        }
        LogUtils.e(nodeBeanList.size() + "个账号");

        if (nodeBeanList.size() > account) {
            LogUtils.d("accout的值是" + account);
            LogUtils.d("nodeBeanList的值是:" + nodeBeanList.size());
            listXY = wxUtils.getXY(nodeBeanList.get(nodeBeanList.size() - 1).getBounds());//获取最下面一个坐标
            LogUtils.d("账号" + nodeBeanList.get(nodeBeanList.size() - 1).getText());
            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击坐标
            account++;
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            LogUtils.d("nodeBeanList的值是:" + nodeBeanList.size());
            LogUtils.d("accout的值是" + account);
            account = 1;
            LogUtils.d("账号切换完毕...");
            backHome();//返回到home
        }

        return false;
    }

    String pan_ret = "";

    /**
     * 获取支付宝账号
     */
    private void getName_zfb() {//先判断是否在主页面 TODO
        backHome();//返回到home
        xmlData = wxUtils.getXmlData();
        String tag = xmlData;
        wxUtils.adbDimensClick(context, R.dimen.x240, R.dimen.y363, R.dimen.x320, R.dimen.y400);//点击我的按钮
        xmlData = wxUtils.getXmlData();
        if (tag.equals(xmlData)) {
            LogUtils.d("你进入到点击弹出窗口的取消按钮没有呀");
            wxUtils.adbDimensClick(context, R.dimen.x33, R.dimen.y77, R.dimen.x63, R.dimen.y98);//点击红包的小x
            wxUtils.adbDimensClick(context, R.dimen.x240, R.dimen.y363, R.dimen.x320, R.dimen.y400);//点击我的按钮
        }
        backHome();
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        xmlData = wxUtils.getXmlData();
        List<String> zfb_name = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < zfb_name.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(zfb_name.get(i)).getNode();
            if ("com.alipay.android.phone.wealth.home:id/user_account".equals(nodeBean.getResourceid())) {
                zfbNames = nodeBean.getText();
                LogUtils.d("用户名称是" + zfbNames);
                HttpUserName(zfbNames);

            }
        }
    }

    private void HttpUserName(String account) {
        RequestParams params = new RequestParams(zfb_urls.ZFBConferred());
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("account", account);
        LogUtils.d(zfb_urls.ZFBConferred() + "?uid=" + uid + "&account=" + account);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
            @Override
            public void onSuccess(LogidBean bean) {
                LogUtils.d("查看支付宝账号是否被限制的返回结果" + bean.getRet());
                pan_ret = bean.getRet();
                if ("200".equals(bean.getRet())) {
                    is_username = false;
                    LogUtils.d("该账号已被限制权限");
                }

            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("查看支付宝账号是否被限制的返回失败结果:" + errorCode);
                if (errorCode == 400) {
                    LogUtils.d("该账号没有被限制权限");
                    is_username = true;
                }

            }
        });
    }

    /**
     * 支付宝群里发消息
     */
    private void sendMessageFlock(String messageData) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        String qunClickMark = "";
        List<String> nodeList;

        String fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
        WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);

        if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans != null && wxFlockMessageBeans.length > 0) {

        } else {
            ShowToast.show("数据有误", (Activity) context);
            return;
        }

        wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮

        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
        //进入了群列表
        w:
        while (true) {
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("群聊")) {
                ShowToast.show("任务被中断...", (Activity) context);
                break;
            }

            if (xmlData.contains("新群聊") && xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
                wxUtils.adb("input keyevent 4");
                break;
            }
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if ("com.alipay.mobile.antui:id/item_left_text".equals(nodeBean.getResourceid())) {

                    if (nodeBean.getText().length() < 7) {
                        continue;
                    }
                    if (!(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B"))) {
                        continue;
                    }
                    if ((nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !boyEnd) {
                        continue;
                    } else if ((nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B")) && !girlEnd) {
                        continue;
                    }
                    if (!nodeBean.getText().contains(uid)) {//只给自己的群发消息
                        continue;
                    }

                    if (qunClickMark.contains(nodeBean.getText())) {//进过的群
                        continue;
                    } else {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        qunClickMark = qunClickMark + nodeBean.getText() + ",";

                    }

                    //_______________________________________________________________________________________________
                    String qunName = "";
                    //获取群人数，男女群信息
                    String qunNameData = wxUtils.getXmlData();
                    List<String> qunNameDataList = wxUtils.getNodeList(qunNameData);

                    if (!qunNameData.contains("语音") && !qunNameData.contains("更多")) {
                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                        break w;
                    }
                    for (int c = 0; c < qunNameDataList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
//                        LogUtils.d(qunNameBean.toString());
                        if ("com.alipay.mobile.ui:id/title_bar_title".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }
                    if (qunName.length() >= 10) {
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(qunName.substring(qunName.length() - 3));
                        int qb = Integer.parseInt(m.replaceAll("").trim());//群人数
                        if (true) {//超过群人数
                            int sex = 2;//0代表女群
                            if ((qunName.contains("a") || qunName.contains("A")) && boyEnd) {//a代表男群
                                sex = 1;
                            } else if ((qunName.contains("b") || qunName.contains("B")) && girlEnd) {
                                sex = 0;
                            } else {
                                sex = 2; //TODO
                            }
                            if (sex == 0 || sex == 1 || sex == 2) {
                                xmlData = wxUtils.getXmlData();
                                if (xmlData.contains("群公告") && xmlData.contains("群聊设置")) {

                                    //操作群
                                    LogUtils.e("发送消息");
                                    xmlData = wxUtils.getXmlData();
                                    if (xmlData.contains("文本")) {
                                        wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                                        wxUtils.adb("input keyevent 4");
                                    }

                                    for (int b = 0; b < wxFlockMessageBeans.length; b++) {
                                        int wCount = 0;
                                        switch (wxFlockMessageBeans[b].getType()) {
                                            case "txt":

                                                // 将文本内容放到系统剪贴板里。
                                                cm.setText(wxFlockMessageBeans[b].getData());
                                                int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                                                int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1500);  //长按EdiText
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y180, R.dimen.x62, R.dimen.y180);//粘贴
//                                                wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y176, R.dimen.x314, R.dimen.y198);//发送

                                                xmlData = wxUtils.getXmlData();
                                                List<String> nodeList1 = wxUtils.getNodeList(xmlData);
                                                for (int s = 0; s < nodeList1.size(); s++) {
                                                    NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(nodeList1.get(s)).getNode();
                                                    if (nodeBean1 != null && "com.alipay.mobile.chatapp:id/sendBtn".equals(nodeBean1.getResourceid())) {
                                                        listXY = wxUtils.getXY(nodeBean1.getBounds());//获取好友坐标
                                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发送
                                                        LogUtils.d(nodeBean.getBounds() + "发送坐标");
                                                        break;
                                                    }
                                                }

                                                wxUtils.adb("input keyevent 4");

                                                break;
                                            case "img":
                                                downFlockImg(wxFlockMessageBeans[b].getData(), 1);
                                                try {
                                                    Thread.sleep(2000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册

                                                a = 0;
                                                while (a < 5) {
                                                    a++;
                                                    xmlData = wxUtils.getXmlData();
                                                    if (!xmlData.contains("图片和视频")) {
                                                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册
                                                    } else {
                                                        break;
                                                    }
                                                }

                                                if (xmlData.contains("图片和视频")) {
                                                    wxUtils.adbClick(274, 123, 304, 153);
                                                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                                    try {
                                                        Thread.sleep(500);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                }

                                                break;
                                        }

                                    }


                                } else {
                                    wxUtils.adb("input keyevent 4");
                                    break w;
                                }


                                wxUtils.adb("input keyevent 4");
                                continue;
                            }
                        }
                    }
                    //_______________________________________________________________________________________________
                    wxUtils.adb("input keyevent 4");
                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData)) {
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                break;
            }
        }

    }

    /**
     * 微信群消息发图文  图片下载
     *
     * @param messageData
     * @return
     */
    private boolean downFlockImg(String messageData, int type) {
        String path = "";
        String strMark = "";
        String fileName = "";
        String filePath = "";
        String text = "";
        String fileUrl = "";

        if (!StringUtils.isEmpty(messageData)) {//判断请求地址是否为空
            text = messageData;
        } else {
            LogUtils.d("朋友圈图文地址为空");
            return false;
        }
        if (type == 1) {//群里发图文
            path = URLS.pic_vo_flock + text.replace("\\", "/");
        } else if (type == 0) {//朋友圈
            path = URLS.pic_vo + text.replace("\\", "/");
        }
        LogUtils.d("文件url__" + path);
        strMark = text.replace("\\", "/");
        fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");
        LogUtils.d("a" + fileName);
        filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + fileName;
        LogUtils.d("b" + filePath);
        LogUtils.d("c" + FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages"));

        if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载
            LogUtils.d("存在");
        } else {
            LogUtils.d("不存在");
            File f = null;
            try {
                f = wxUtils.getFileDown(path, fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            if (f == null) {
                return false;
            }
        }
        fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
        LogUtils.e("aa文件写入:" + FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false));//改名把文件添加到第一个
        wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), context);

        return true;
    }
    //----------------------------------拉群分男女开始---------------------------------------------------------------

    /**
     * 修改备注.
     */
    private void startAlterNameMark() {

        while (true) {//必须跳转到通讯录
            xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {//判断是否在支付宝主界面
                backHome();
            } else {
                wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮

                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("通讯录") && xmlData.contains("新的朋友")) {
                    break;
                }
            }
        }


        boolean bottom = false;//到了底部
        int sex = 2;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("0000");
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        String meName = "";

        w:
        while (true) {

            while (true) {//必须跳转到通讯录
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }


            if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
//                ShowToast.show("任务被中断...", (Activity) context);
                continue w;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            boolean hideMark = false;//判断是否隐藏真实姓名
            a:
            for (int a = 0; a < nodeList.size(); a++) {
//                LogUtils.e(nodeList.get(a));
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && !nodeBean.getText().startsWith("ZZZ") && !meName.equals(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注

                    String hideStr = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode().getText();
                    if ("对方已隐藏真实姓名 ".contains(hideStr)) {
                        hideMark = true;
                    }

                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    xmlData = wxUtils.getXmlData();//重新获取页面数据  获取两次

                    if (xmlData.contains("该功能暂未对您开放")) {//支付宝被限制 TODO
                        wxUtils.adb("input keyevent 4");
                        LogUtils.e("支付宝账号被限制");
                        astrict = false;
                        break w;
                    }

                    if (xmlData.contains("修改我的信息")) {//自己的账号返回
                        wxUtils.adb("input keyevent 4");
                        meName = nodeBean.getText();
                        continue;
                    }

                    if (!(xmlData.contains("详细资料") && xmlData.contains("支付宝账户"))) {
                        LogUtils.d("不在详细资料页面");
                        continue w;
                    }

                    List<String> nodeListColor = wxUtils.getNodeList(xmlData);
                    for (int c = 0; c < nodeListColor.size(); c++) {//判断男女
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeListColor.get(c)).getNode();
                        if ("com.alipay.mobile.socialsdk:id/tv_display_name".equals(nodeBean.getResourceid())) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
//                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                            int x = listXY.get(2) + 25;
                            LogUtils.d("xx:" + x);
                            wxUtils.adb("/system/bin/screencap -p /sdcard/screenshot.png");
                            //        adb shell /system/bin/screencap -p /sdcard/screenshot.png（保存到SDCard）
                            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/screenshot.png");//filePath
                            //        Bitmap bitmap= getWindow().getDecorView().getDrawingCache();

                            if (null != bitmap) {
                                int pixel = bitmap.getPixel(x, 147);
                                //获取颜色
                                int redValue = Color.red(pixel);
                                int greenValue = Color.green(pixel);
                                int blueValue = Color.blue(pixel);
                                String col = Integer.toHexString(pixel).toUpperCase();
                                LogUtils.d("R:" + redValue + "__G:" + greenValue + "__B:" + blueValue);
                                LogUtils.d("【颜色值】 #" + col);
                                bitmap.recycle();
                                bitmap = null;

                                switch (col) {
                                    case "FFF37E7D":
                                        sex = 0;
                                        break;
                                    case "FF00AAEE":
                                        sex = 1;
                                        break;
                                    default:
                                        sex = 2;
                                        break;
                                }
                            }
                            break;
                        }
                    }

                    wxUtils.adbDimensClick(context, R.dimen.x297, R.dimen.y33, R.dimen.x297, R.dimen.y33);//点击更多
                    wxUtils.adbDimensClick(context, R.dimen.x167, R.dimen.y66, R.dimen.x167, R.dimen.y66);//点击修改备注
                    //判断是否在修改备注界面
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("备注信息") && xmlData.contains("备注名"))) {
                        continue w;
                    }


                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y106, R.dimen.x320, R.dimen.y106);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x294, R.dimen.y106, R.dimen.x294, R.dimen.y106);//清空名字

                    AlipayAlterNameSqliteBean alipayAlterNameSqliteBean = new AlipayAlterNameSqliteBean();//创建数据库对象
                    alipayAlterNameSqliteBean.setAlipayName(zfbNames);
                    alipayAlterNameSqliteBean.setName(name);


                    switch (sex) {//0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, zfbNames + "wx_name_number_girl_alipay", 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                            wxUtils.adb("input text \"ZZZ0B" + wx_nume_number_new_girl + "\"");
                            SPUtils.put(context, zfbNames + "wx_name_number_girl_alipay", wx_name_number_girl + 1);
                            alipayAlterNameSqliteBean.setNewName("ZZZ0B" + wx_nume_number_new_girl);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, zfbNames + "wx_name_number_boy_alipay", 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                            wxUtils.adb("input text \"ZZZ0A" + wx_nume_number_new_boy + "\"");
                            SPUtils.put(context, zfbNames + "wx_name_number_boy_alipay", wx_name_number_boy + 1);
                            alipayAlterNameSqliteBean.setNewName("ZZZ0A" + wx_nume_number_new_boy);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, zfbNames + "wx_name_number_c_alipay", 0);
                            String wx_nume_number_c = df.format(wx_name_number_c + 1);
                            wxUtils.adb("input text \"ZZZ0C" + wx_nume_number_c + "\"");
                            SPUtils.put(context, zfbNames + "wx_name_number_c_alipay", wx_name_number_c + 1);
                            alipayAlterNameSqliteBean.setNewName("ZZZ0C" + wx_nume_number_c);
                            break;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    LogUtils.e("添加数据库" + alipayAlterNameSqliteBean.save() + "内容是:" + alipayAlterNameSqliteBean.toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (true){
                        xmlData = wxUtils.getXmlData();
                        if(xmlData.contains("备注信息")&&xmlData.contains("备注名")){
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else if(xmlData.contains("保存本次编辑？")){
                            wxUtils.adbDimensClick(context, R.dimen.x234, R.dimen.y241, R.dimen.x234, R.dimen.y241);//保存
                        }else{
                            break ;
                        }
                    }

                    wxUtils.adb("input keyevent 4");

                    while (true) {
                        xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("保存本次编辑？")) {
                            wxUtils.adbDimensClick(context, R.dimen.x234, R.dimen.y241, R.dimen.x234, R.dimen.y241);//保存
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (xmlData.contains("备注信息") && xmlData.contains("备注名")) {
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            wxUtils.adb("input keyevent 4");
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            break ;
                        }

                    }

                    xmlData = wxUtils.getXmlData();


                    while (xmlData.contains("详细资料") || xmlData.contains("支付宝账户")) {
                        wxUtils.adb("input keyevent 4");
                        xmlData = wxUtils.getXmlData();
                    }


                   /* if (hideMark) {//判断是否隐藏姓名
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }*/
                    if (xmlData.contains(name)) {//判断修改后是否名字没有改变
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }

                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("通讯录") && !xmlData.contains("搜索") && !xmlData.contains("添加朋友")) {
//                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && !nodeBean.getText().startsWith("ZZZ") && !meName.equals(nodeBean.getText())) {
                    continue w;
                } else if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText() != "" && nodeBean.getText().startsWith("ZZZ")) {
                    zzzNum++;
                }
            }

            if (!bottom) {
                if (zzzNum >= 10) {
                    wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y387, R.dimen.x320, R.dimen.y395);
                } else {
                    wxUtils.adbUpSlide(context);//向上滑动
                }
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                wxUtils.adb("input keyevent 4");
                wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                ShowToast.show("修改备注完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("个朋友")) {//判断是否到达底部
                bottom = true;
            }
        }

    }


    /**
     * 拉群  分男女
     */
    private void addCrowdMark() {
        //初始化数据
        bogCount = 0;//要改名的男好友
        girlCount = 0;//要改名的女好友
//        countAddStr = "";//判断是否已经勾选过
        countStr = "";//判断是否已经选择
        boyEnd = true;//如果是flase带表男群拉完
        girlEnd = true;//如果是flase带表女群拉完

        List<AlipayAccountFlockClickBean.AccountBean.FlockBean> flockBeanList = new ArrayList<>();
        //_________________________________________
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
        String qunClickMark = "";
        List<String> nodeList;

        //进入了群列表
        w:
        while (true) {


            while (true) {//必须跳转到通讯录

                if (xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
                    status = 0;
                    wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                    return;
                }

                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (xmlData.contains("通讯录") && xmlData.contains("新的朋友") && xmlData.contains("群聊")) {
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
                } else if (!xmlData.contains("群聊") || !xmlData.contains("返回") || !xmlData.contains("搜索")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }

            if (!(xmlData.contains("群聊") && xmlData.contains("返回") && xmlData.contains("搜索"))) {
//                ShowToast.show("任务被中断...", (Activity) context);
                continue w;
            }

            if (xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
                status = 0;
                wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                return;
            }
            List<String> nodeListA = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeListA.size(); a++) {

                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeListA.get(a)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid())) {//"A000101"
                    if (nodeBean.getText().length() < 7) {
                        continue;
                    }
                    if (!(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B"))) {
                        continue;
                    }
                    if ((nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !boyEnd) {
                        continue;
                    } else if ((nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B")) && !girlEnd) {
                        continue;
                    }
                    LogUtils.e("boyEnd=" + boyEnd + "_______girlEnd=" + girlEnd);
                    if (!boyEnd && !girlEnd) {//拉群任务完成
                        wxUtils.adb("input keyevent 4");
                        ShowToast.show("拉群任务完成", (Activity) context);
                        LogUtils.e("拉群任务完成");
                        break w;
                    }
                    String qunFlockName = "";
                    if (qunClickMark.contains(nodeBean.getText())) {//进过的群
                        continue;
                    } else {
                        qunFlockName = nodeBean.getText();
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        qunClickMark = qunClickMark + nodeBean.getText() + ",";
                    }

                    //_______________________________________________________________________________________________
                    String qunName = "";
                    //获取群人数，男女群信息
                    String qunNameData = wxUtils.getXmlData();
                    List<String> qunNameDataList = wxUtils.getNodeList(qunNameData);
                    if (!(qunNameData.contains("com.alipay.mobile.chatapp:id/voiceSwitchBtn") && qunNameData.contains("com.alipay.mobile.chatapp:id/chat_expression_ctr_btn"))) {//判断是否在拉群页面

//                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                        continue w;
                    }

                    for (int c = 0; c < qunNameDataList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
//                        LogUtils.d(qunNameBean.toString());
                        if ("com.alipay.mobile.ui:id/title_bar_title".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }
                    if (qunName.length() >= 10) {
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(qunName.substring(qunName.length() - 3));
                        int qb = Integer.parseInt(m.replaceAll("").trim());//群人数
                        if (qb < qunMaxNum) {//超过群人数
                            int sex = 2;//0代表女群
                            if ((qunName.contains("a") || qunName.contains("A")) && boyEnd) {//a代表男群
                                sex = 1;
                            } else if ((qunName.contains("b") || qunName.contains("B")) && girlEnd) {
                                sex = 0;
                            } else {
                                sex = 2; //TODO
                            }
                            if (sex == 0 || sex == 1) {
                                come_num = 0;
                                addMemberMark(sex, qb);//拉群选择成员


                                AlipayAccountFlockClickBean.AccountBean.FlockBean flockBean = new AlipayAccountFlockClickBean.AccountBean.FlockBean();
                                flockBean.setFlock_name(qunFlockName);
                                flockBean.setClick_num(clickCount + "");
                                flockBean.setCome_num("" + (come_num - qb));
                                flockBeanList.add(flockBean);
                                LogUtils.e(come_num - qb + "__come_num:" + come_num + "qb:" + qb);
                                continue;
                            }
                        }
                    }
                    //_______________________________________________________________________________________________
                    wxUtils.adb("input keyevent 4");
                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            LogUtils.e("向上滑动a");
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData)) {
                wxUtils.adb("input keyevent 4");
                LogUtils.e("拉群任务完成");
                ShowToast.show("拉群任务完成", (Activity) context);
                break;
            }
        }

        AlipayAccountFlockClickBean.AccountBean accountBean = new AlipayAccountFlockClickBean.AccountBean();
        accountBean.setAlipay_account(zfbNames);
        accountBean.setFlock(flockBeanList);
        clickAccountBenaList.add(accountBean);

        LogUtils.d("男改名"+bogCount + "___女改名" + girlCount);
        LogUtils.d("拒绝拉群的:" + refuseAdd);
        //拉完群改名
        ShowToast.show("拉群完，修改备注开始", (Activity) context);
        qunEndAlterNameRefuseAddMark();//修改拒绝的人
        wxUtils.adb("input keyevent 4");
        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
        qunEndAlterNameMark();
    }

    /**
     * 拉完群改名修改备注.  分男女
     */
    private void qunEndAlterNameMark() {
        int boyAlterCount = 0;
        int girlAltCount = 0;
        boolean bottom = false;//到了底部
        DecimalFormat df = new DecimalFormat("0000");
        String endData = "";

        xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
        w:
        while (true) {
            if (boyAlterCount >= bogCount && girlAltCount >= girlCount) {
                ShowToast.show("拉群后改名完成", (Activity) context);
                break w;
            }

            while (true) {//必须跳转到通讯录
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }

            LogUtils.d("待改名" + countStr);
            boolean hideMark = false;
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && countStr.contains(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);
                    if (nodeBean.getText().contains("A") && boyAlterCount >= bogCount) {
                        continue;
                    } else if (nodeBean.getText().contains("B") && girlAltCount >= girlCount) {
                        continue;
                    }
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注


                    xmlData = wxUtils.getXmlData();
                    wxUtils.adbDimensClick(context, R.dimen.x297, R.dimen.y33, R.dimen.x297, R.dimen.y33);//点击更多
                    wxUtils.adbDimensClick(context, R.dimen.x167, R.dimen.y66, R.dimen.x167, R.dimen.y66);//点击修改备注
                    //判断是否在修改备注界面
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("备注信息") && xmlData.contains("备注名"))) {
                        continue w;
                    }

                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y106, R.dimen.x320, R.dimen.y106);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x294, R.dimen.y106, R.dimen.x294, R.dimen.y106);//清空名字

                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '1';
                        sexStr = new String(ch);
                    }
                    LogUtils.e("input text \"" + sexStr + "\"");
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改

                    LogUtils.d("修改后删除数据库"+DataSupport.deleteAll(AlipayAlterNameSqliteBean.class, "alipayname = ? and newName = ?", zfbNames,name));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (true){
                        xmlData = wxUtils.getXmlData();
                        if(xmlData.contains("备注信息")&&xmlData.contains("备注名")){
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else if(xmlData.contains("保存本次编辑？")){
                            wxUtils.adbDimensClick(context, R.dimen.x234, R.dimen.y241, R.dimen.x234, R.dimen.y241);//保存
                        }else{
                            break ;
                        }
                    }

                    wxUtils.adb("input keyevent 4");

                    while (true) {
                        xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("保存本次编辑？")) {
                            wxUtils.adbDimensClick(context, R.dimen.x234, R.dimen.y241, R.dimen.x234, R.dimen.y241);//保存
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (xmlData.contains("备注信息") && xmlData.contains("备注名")) {
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            wxUtils.adb("input keyevent 4");
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            break ;
                        }
                    }



                    xmlData = wxUtils.getXmlData();
                    while (xmlData.contains("详细资料") || xmlData.contains("支付宝账户")) {
                        wxUtils.adb("input keyevent 4");
                        xmlData = wxUtils.getXmlData();
                    }
                    if (xmlData.contains(name)) {//判断修改后是否名字没有改变
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }

                    if (nodeBean.getText().contains("A")) {
                        boyAlterCount++;
                    } else if (nodeBean.getText().contains("B")) {
                        girlAltCount++;
                    }

                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("通讯录")) {
//                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && countStr.contains(nodeBean.getText())) {
                    if (nodeBean.getText().startsWith("ZZZ0A") && boyAlterCount < bogCount) {
                        continue w;
                    } else if (nodeBean.getText().startsWith("ZZZ0B") && girlAltCount < girlCount) {
                        continue w;
                    }
                }
            }
            if (!bottom) {
                wxUtils.adbUpSlide(context);//向上滑动aaaadrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
//                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y367, R.dimen.x80, R.dimen.y400);//点击微信
                ShowToast.show("修改备注完成,拉群任务完成", (Activity) context);
                LogUtils.d(boyAlterCount + "修改备注完成,拉群任务完成" + girlAltCount);
                break w;
            }
            if (xmlData.contains("个朋友")) {//判断是否到达底部
                bottom = true;
            }
        }
    }

    /**
     * 拉完群修改拒绝添加的人
     */
    private void qunEndAlterNameRefuseAddMark() {
        if (StringUtils.isEmpty(refuseAdd)) {
            return;
        }
        String[] refuseAdds = refuseAdd.split(",");
        int refuseAddCount = 0;
        boolean bottom = false;//到了底部
        DecimalFormat df = new DecimalFormat("0000");
        String endData = "";


        w:
        while (true) {
            if (refuseAddCount >= refuseAdds.length) {
//                ShowToast.show("拒绝加群的改名完成", (Activity) context);
                break w;
            }

            while (true) {//必须跳转到通讯录
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("支付宝") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {
                    wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
                    wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                } else if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    wxUtils.openAliPay();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
                    wxUtils.adb("input keyevent 4");
                } else {
                    break;
                }
            }


            if (!xmlData.contains("通讯录") || !xmlData.contains("返回")) {
//                ShowToast.show("任务被中断...", (Activity) context);
                continue w;
            }

            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && refuseAdd.contains(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);
                    if (refuseAddCount >= refuseAdds.length) {
                        break w;
                    }
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注

                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("详细资料") && xmlData.contains("支付宝账户"))) {
                        LogUtils.d("不在详细资料页面");
                        continue w;
                    }


                    wxUtils.adbDimensClick(context, R.dimen.x297, R.dimen.y33, R.dimen.x297, R.dimen.y33);//点击更多
                    wxUtils.adbDimensClick(context, R.dimen.x167, R.dimen.y66, R.dimen.x167, R.dimen.y66);//点击修改备注
                    //判断是否在修改备注界面
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("备注信息") && xmlData.contains("备注名"))) {
                        continue w;
                    }


                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y106, R.dimen.x320, R.dimen.y106);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x294, R.dimen.y106, R.dimen.x294, R.dimen.y106);//清空名字

                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '9';
                        sexStr = new String(ch);
                    }
                    LogUtils.e("input text \"" + sexStr + "\"");
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改

                    LogUtils.d("修改后删除数据库"+DataSupport.deleteAll(AlipayAlterNameSqliteBean.class, "alipayname = ? and newName = ?", zfbNames,name));
                    refuseAddCount++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    while (true){
                        xmlData = wxUtils.getXmlData();
                        if(xmlData.contains("备注信息")&&xmlData.contains("备注名")){
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else if(xmlData.contains("保存本次编辑？")){
                            wxUtils.adbDimensClick(context, R.dimen.x234, R.dimen.y241, R.dimen.x234, R.dimen.y241);//保存
                        }else{
                            break ;
                        }
                    }

                    wxUtils.adb("input keyevent 4");

                    while (true) {
                        xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("保存本次编辑？")) {
                            wxUtils.adbDimensClick(context, R.dimen.x234, R.dimen.y241, R.dimen.x234, R.dimen.y241);//保存
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (xmlData.contains("备注信息") && xmlData.contains("备注名")) {
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            wxUtils.adb("input keyevent 4");
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            break ;
                        }

                    }

                    xmlData = wxUtils.getXmlData();
                    while (xmlData.contains("详细资料") || xmlData.contains("支付宝账户")) {
                        wxUtils.adb("input keyevent 4");
                        xmlData = wxUtils.getXmlData();
                    }
                    if (xmlData.contains(name)) {//判断修改后是否名字没有改变
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.x176, R.dimen.y17, R.dimen.x224, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }


                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("通讯录")) {
//                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && refuseAdd.contains(nodeBean.getText())) {
                    if (refuseAddCount >= refuseAdds.length) {
                        break w;
                    }//TODO
                }
            }
            if (!bottom) {
                wxUtils.adbUpSlide(context);//向上滑动aaaadrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
//                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y367, R.dimen.x80, R.dimen.y400);//点击微信
//                ShowToast.show("修改拒绝进群完成", (Activity) context);
                LogUtils.d(refuseAdd + "修改备注完成,拉群任务完成");
                break w;
            }
            if (xmlData.contains("个朋友")) {//判断是否到达底部
                bottom = true;
            }
        }
        refuseAdd = "";
    }


    /**
     * 拉群添加成员
     *
     * @param qb  群当前人数
     * @param sex 性别
     */
    private void addMemberMark(int sex, int qb) {
        clickCount = 0;//选中次数
        List<String> qunNameDataList = new ArrayList<>();
        wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//确定

        String qunNameData = wxUtils.getXmlData();

        if (!qunNameData.contains("聊天信息")) {
            return;
        }
        qunNameDataList = wxUtils.getNodeList(qunNameData);
        for (int a = 0; a < qunNameDataList.size(); a++) {

            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(qunNameDataList.get(a)).getNode();
//                    if (nodeBean != null && nodeBean.getContentdesc() != null && "添加成员".equals(nodeBean.getContentdesc())) {
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().contains("com.alipay.mobile.chatapp:id/item_")) {
                NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(qunNameDataList.get(a + 2)).getNode();

                if (StringUtils.isEmpty(nodeBean1.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//添加
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加

                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("选择朋友") && xmlData.contains("确定") && xmlData.contains("返回"))) {
                        wxUtils.adb("input keyevent 4");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    break;
                }

            }

        }

        xmlData = wxUtils.getXmlData();
        if (!(xmlData.contains("选择朋友") && xmlData.contains("确定") && xmlData.contains("返回"))) {
            return;
        }


        String addNameData = wxUtils.getXmlData();
        List<String> addNameList = new ArrayList<>();
        addNameList = wxUtils.getNodeList(addNameData);
        int flagGirlCount = 0;
        int flagBoyCount = 0;
        String flagCountStr = "";
        int flagClickCount = 0;

        String flagSendCount="";
        w:
        while (true) {

            for (int a = 0; a < addNameList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(addNameList.get(a)).getNode();
                if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ0") && !refuseAdd.contains(nodeBean.getText())) {
                    if(a + 2>addNameList.size()-1){
                        continue ;
                    }
                    NodeXmlBean.NodeBean checkBox = wxUtils.getNodeXmlBean(addNameList.get(a + 2)).getNode();   //TODO IndexOutOfBoundsException

                    if ("com.alipay.mobile.socialsdk:id/selected_check_box".equals(checkBox.getResourceid()) && checkBox.isChecked()) {//isChecked true代表选中

                        if (!countStr.contains(nodeBean.getText()) && !flagCountStr.contains(nodeBean.getText())) {
                            if (nodeBean.getText().contains("B")) {
                                girlCount++;
                                flagGirlCount++;
                                LogUtils.d(girlCount + "girlCount+选中");
//                                countStr = countStr + nodeBean.getText() + ",";
                                flagCountStr = flagCountStr + nodeBean.getText() + ",";
                            } else if (nodeBean.getText().contains("A")) {
                                bogCount++;
                                flagBoyCount++;
                                LogUtils.d(bogCount + "bogCount选中____________________");
//                                countStr = countStr + nodeBean.getText() + ",";
                                flagCountStr = flagCountStr + nodeBean.getText() + ",";
                            }
                        }
                    }

                    if (sex == 0 && nodeBean.getText().contains("B")) {//女
                        if ("com.alipay.mobile.socialsdk:id/selected_check_box".equals(checkBox.getResourceid()) && !checkBox.isChecked()) {
                            if (countStr.contains(nodeBean.getText()) || flagCountStr.contains(nodeBean.getText())) {
                                continue;
                            } else {
                                if (clickCount + qb < qunMaxNum) {
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
//                                    countStr = countStr + nodeBean.getText() + ",";
                                    flagCountStr = flagCountStr + nodeBean.getText() + ",";
                                    clickCount++;
                                    flagClickCount++;
                                    girlCount++;
                                    flagGirlCount++;
                                    LogUtils.d(girlCount + "girlCount+点击");

                                    flagSendCount=flagSendCount +nodeBean.getText() + ",";

                                    if(bogCount+girlCount>=pullMax){
                                        break w;
                                    }
                                } else {
                                    break w;
                                }
                            }
                        }
                    } else if (sex == 1 && nodeBean.getText().contains("A")) {//男
                        if ("com.alipay.mobile.socialsdk:id/selected_check_box".equals(checkBox.getResourceid()) && !checkBox.isChecked()) {
                            if (countStr.contains(nodeBean.getText()) || flagCountStr.contains(nodeBean.getText())) {
                                continue;
                            } else {
                                if (clickCount + qb < qunMaxNum) {
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
//                                    countStr = countStr + nodeBean.getText() + ",";
                                    flagCountStr = flagCountStr + nodeBean.getText() + ",";
                                    clickCount++;
                                    flagClickCount++;
                                    bogCount++;
                                    flagBoyCount++;
                                    LogUtils.d(bogCount + "bogCount点击____________________");

                                    flagSendCount=flagSendCount +nodeBean.getText() + ",";

                                    if(bogCount+girlCount>=pullMax){
                                        break w;
                                    }
                                } else {
                                    break w;
                                }
                            }
                        }
                    }
                }
            }
            String oldAddNameData = addNameData;
            wxUtils.adbQunUpSlide(context);//向上滑动
            addNameData = wxUtils.getXmlData();
            addNameList = wxUtils.getNodeList(addNameData);
            if (oldAddNameData.equals(addNameData)) {
                LogUtils.e("sex" + sex);
                if (sex == 0) {
                    girlEnd = false;
                } else {
                    boyEnd = false;
                }
                break;
            }
            int judgeGirl = 0;
            int judgeBoy = 0;
            for (int a = 0; a < addNameList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(addNameList.get(a)).getNode();
                if (sex == 0) {//女
                    if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ1")) {
                        judgeGirl++;
                    }
                } else {//男
                    if ("com.alipay.mobile.socialsdk:id/list_item_title".equals(nodeBean.getResourceid()) && (nodeBean.getText().startsWith("ZZZ0B") || nodeBean.getText().startsWith("ZZZ1"))) {
                        judgeBoy++;
                    }
                }
            }
            if (judgeGirl >= 9) {//女生拉完
                girlEnd = false;
                break;
            } else if (judgeBoy >= 9) {//男生拉完
                boyEnd = false;
                break;
            }
        }
        if (clickCount > 0) {//确认添加
            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
            boolean send=true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wh: while (true) {
                String qunLiaoBack = wxUtils.getXmlData();//修改完一个重新获取页面数据

                if(qunLiaoBack.contains("当前群聊人数较多。为减少打扰，对方同意邀请后才会进入群聊，现在发送邀请？")){
                    wxUtils.adbDimensClick(context, R.dimen.x234, R.dimen.y244, R.dimen.x234, R.dimen.y244);//邀请
                }

                if(qunLiaoBack.contains("添加参与人失败")){
                    send=false;
                }

                if (qunLiaoBack.contains("添加参与人失败") && !qunLiaoBack.contains("取消")) {//拒绝添加
                    wxUtils.adb("input keyevent 4");


                    List<String> nodeList = wxUtils.getNodeList(qunLiaoBack);

                    for (int b = 0; b < nodeList.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.antui:id/message")) {
                            refuseAdd = refuseAdd + nodeBean.getText().substring(0, 9) + ",";//添加失败的人
                            girlCount = girlCount - flagGirlCount;//添加失败，回滚
                            bogCount = bogCount - flagBoyCount;
                            clickCount = clickCount - flagClickCount;
                            wxUtils.adb("input keyevent 4");
                            addMemberMark(sex, qb);
                            return;
                        }
                    }
                } else {
                    countStr = countStr + flagCountStr + ",";

                    if(bogCount+girlCount>=pullMax){
                        boyEnd=false;
                        girlEnd=false;
                    }
                }
                if ((qunLiaoBack.contains("com.alipay.mobile.chatapp:id/voiceSwitchBtn") && qunLiaoBack.contains("com.alipay.mobile.chatapp:id/chat_expression_ctr_btn"))) {//判断是否在拉群页面

                    String qunName = "";
                    List<String> qunNameDataNewList = wxUtils.getNodeList(qunLiaoBack);
                    for (int c = 0; c < qunNameDataNewList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataNewList.get(c)).getNode();
//                        LogUtils.d(qunNameBean.toString());
                        if ("com.alipay.mobile.ui:id/title_bar_title".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }
                    if (qunName.length() >= 10) {
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(qunName.substring(qunName.length() - 3));
                        come_num = Integer.parseInt(m.replaceAll("").trim());//群人数
                    }
                    //@选中的人，发送消息----------------------------------------------
                    List<AlipayAlterNameSqliteBean> alipayAlterNameSqliteBeanList = DataSupport.where("alipayname = ?", zfbNames).find(AlipayAlterNameSqliteBean.class);
                    String message = "";
                    for (AlipayAlterNameSqliteBean alipayAlterNameSqliteBeanFor : alipayAlterNameSqliteBeanList) {
                        if (flagSendCount.contains(alipayAlterNameSqliteBeanFor.getNewName())) {
                            message = message + "@" + alipayAlterNameSqliteBeanFor.getName() + " ";
                        }
                    }
                    message = message + context.getString(R.string.alipay_message);
                    LogUtils.e(message);
                    // 将文本内容放到系统剪贴板里。
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    cm.setText(message);
                    int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                    int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//支付宝发消息
                    if(send) {
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1500);  //长按输入框
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y180, R.dimen.x62, R.dimen.y180);//粘贴
                        wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y176, R.dimen.x314, R.dimen.y198);//发送
                        wxUtils.adb("input keyevent 4");
                    }
                    wxUtils.adb("input keyevent 4");
                    break;
                } else {
                    wxUtils.adb("input keyevent 4");
                }
            }

        } else {//没有选中.返回
            come_num = qb;//群人数
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
        }
    }
}
