package com.zplh.zplh_android_yk.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.ClipboardManager;
import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.bean.AliFriendsMessageBean;
import com.zplh.zplh_android_yk.bean.AliPayNewResultBean;
import com.zplh.zplh_android_yk.bean.AliPayResultBean;
import com.zplh.zplh_android_yk.bean.AlipayAccountFlockClickBean;
import com.zplh.zplh_android_yk.bean.AlipayAccountFlockNumBean;
import com.zplh.zplh_android_yk.bean.AlipayAlterNameSqliteBean;
import com.zplh.zplh_android_yk.bean.AlipayBillStatisticsBean;
import com.zplh.zplh_android_yk.bean.AlipayTransferMessageBean;
import com.zplh.zplh_android_yk.bean.LogidBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.bean.PhoneBean;
import com.zplh.zplh_android_yk.bean.StateRenwuBean;
import com.zplh.zplh_android_yk.bean.WxFlockMessageBean;
import com.zplh.zplh_android_yk.bean.WxFriendsMessageBean;
import com.zplh.zplh_android_yk.bean.XmlBean;
import com.zplh.zplh_android_yk.bean.ZfbNameCodeCommitBean;
import com.zplh.zplh_android_yk.bean.ZfbPhoneNumberBean;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.conf.ZFB_URLS;
import com.zplh.zplh_android_yk.db.StateDao;
import com.zplh.zplh_android_yk.httpcallback.GsonUtil;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpManager2;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.ui.view.OperationView;
import com.zplh.zplh_android_yk.utils.FileUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.NodeUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;
import com.zplh.zplh_android_yk.utils.StringUtils;
import com.zplh.zplh_android_yk.utils.TimeUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.xutils.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by lichun on 2017/7/3.
 * Description:操作支付宝
 */

public class OperationAlipayPresenter extends BasePresenter<OperationView> {
    private OperationView operationView;
    private Context context;
    private WxUtils wxUtils = new WxUtils();
    private String xmlData;
    private List<Integer> listXY;
    private ZFB_URLS zfb_urls;//支付宝接口
    private String zfb_phone = "";//添加联系人获取的手机号码
    private String zfbNames = "";//支付宝名称
    private String zfb_friend_num;//支付宝好友数量
    private int send_friend_num = 0;//每一个帐号每天的发送好友申请的数量
    private String statue = "";//任务出现故障原因
    private int account = 1;//第几个支付宝帐号
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
    private final String uid;
    private String add_friends = "";
    private Map<String, String> map_tongxunlu = new HashMap<String, String>();//通讯录添加好友键值对集合
    private String contact_verify_msg = "";
    private String crowd;
    private String is_statistic;
    private String sendMore;
    private int messageNum;
    private boolean is_username = true;
    public String[] materia_ssList5 = null;
    private boolean zfbQunState = true;
    private String zfbLodId;
    private int zfbNamesCount;
    private String zfbNamesTable;
    private int zfbNamesCountCommit = 0;
    private int logid = 0; //主要用于
    private String zfb_code="";
    private String zfb_logid="";
    List<String> phoneNumList = new ArrayList<>();
    private int zfb_success_flag =0;

    public void setAliNameCount(int zfbNamesCount, String zfbNamesTable,String zfb_logid) {
        this.zfbNamesCount = zfbNamesCount;
        this.zfbNamesTable = zfbNamesTable;
        this.zfb_logid=zfb_logid;
    }


    public String getMessageData() {
        return messageData;
    }

    public void setMessageData(String messageData, String crowd, String is_statistic, String sendMore, String zfbLodId) {
        this.messageData = messageData;
        this.crowd = crowd;
        this.is_statistic = is_statistic;
        this.sendMore = sendMore;
        this.zfbLodId = zfbLodId;
    }

    public boolean isAstrictRemark() {
        return isAstrictRemark;
    }

    public void setAstrictRemark(boolean astrictRemark) {
        isAstrictRemark = astrictRemark;
    }


    public OperationAlipayPresenter(Context context, OperationView operationView) {
        this.operationView = operationView;
        this.context = context;
        zfb_urls = new ZFB_URLS();
        stateDao = new StateDao(context);
        mPackageManager = context.getPackageManager();
        uid = SPUtils.getString(context, "uid", "0000");
    }

    private int time;
    List<AlipayBillStatisticsBean.FlockBean> flockBeanList = new ArrayList<>();

    /**
     * 支付宝执行任务.先判断
     *
     * @param task
     */
    public void task(int task) {
        SPUtils.putBoolean(context, "isTag", true);
        backMark = true;
        backNum = 0;
        statue = "";
        if (isInstallApp(context, "com.eg.android.AlipayGphone")) {//判断支付宝是否安装
            if (time == 0) {
                wxUtils.openAliPay();//打开支付宝
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("忘记密码") || (xmlData.contains("登录") && xmlData.contains("新用户注册") && xmlData.contains("语言")) || (xmlData.contains("你的手机号") && xmlData.contains("注册"))) {//判断是否登录
                time = 0;
                LogUtils.e("请先登录支付宝");
                statue = "已安装支付宝需加号";
                String uid = SPUtils.getString(context, "uid", "0000");
                getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
                ShowToast.show("请先登录支付宝", (Activity) context);
                SPUtils.putBoolean(context, "isTag", false);
                return;
            } else {
                if (xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {//判断是否在支付宝主界面
                    LogUtils.d("已经登录支付宝");
                    time = 0;
                    boolean switchFlag = true;
                    astrict = true;
                    switch (task) {
                        case -1://加好友
                            logid = 1;
                            AlipayAccountFlockClickBean alipayAccountFlockClickBean = new AlipayAccountFlockClickBean();
                            alipayAccountFlockClickBean.setUid(SPUtils.getString(context, "uid", "0000"));
                            clickAccountBenaList = new ArrayList<>();

                            // backHome_tow_zfb();
//                            wxUtils.DeletPhone(context);
                            int aliPayNum = 0;
                            while (switchFlag || switchAccount()) {
                                backHome_tow_zfb();
                                SendFrendsNum();//将好友数量发送至后台在进行好友申请添加

                                aliPayNum++;
                                switchFlag = false;
                                /*wxUtils.DeletPhone(context);
                                ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }*/
                                //wxUtils.DeletPhone(context);

                                if (is_username) {
                                    wxUtils.adb("am force-stop " + "com.eg.android.AlipayGphone");//关闭支付宝
                                    wxUtils.DeletPhone(context);
                                    ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    ZFBHttpPhone();//请求网络获取电话号码并且添加联系人
                                    wxUtils.openAliPay();//打开支付宝
                                    try {
                                        Thread.sleep(10000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if (wxUtils.getContactCount(context) < 1) {
                                        ShowToast.show("目前该手机没有电话联系人，需重新请求一次", (Activity) context);

                                        wxUtils.adb("am force-stop " + "com.eg.android.AlipayGphone");//关闭支付宝

                                        wxUtils.DeletPhone(context);
                                        ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        ZFBHttpPhone();//请求网络获取电话号码并且添加联系人
                                        wxUtils.openAliPay();//打开支付宝
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        wxUtils.openAliPay();//打开支付宝
                                        AddFriend();
                                    }
                                }
                                //AddFriend();//搜索添加支付宝

                                if (!StringUtils.isEmpty(crowd) && crowd.equals("1")) {
                                    backHome_tow_zfb();
                                    astrict = true;//重置支付宝是否被被限制
                                    getName_zfb();//获取支付宝帐号
                                    isAstrictRemark = false;
                                    if (isAstrictRemark) {//分男女
                                        startAlterNameMark();//修改备注
                                        if (astrict)//判断支付宝帐号是否被限制
                                            addCrowdMark();//拉群
                                    } else {//不分男女
                                        startAlterName();//修改备注
                                        addCrowd();//拉群
                                    }
                                }

                            }
                            if (!StringUtils.isEmpty(crowd) && crowd.equals("1")) {
                                alipayAccountFlockClickBean.setAccount(clickAccountBenaList);
                                String strClick = gson.toJson(alipayAccountFlockClickBean);
                                LogUtils.e(strClick);
                                updata_group_member_count(0, strClick);
                            }


                            if (aliPayNum != 5) {
                                statue = "支付宝帐号不足5个或者加粉时切换帐号异常";
                                String uid = SPUtils.getString(context, "uid", "0000");
                                getGuzhang(statue + ":log_id的值是:" + logId + "：故障与帐号无关", zfbNames, uid);
                            }
                            break;
                        case -2://拉群
                            LogUtils.d("群上限：" + qunMaxNum + "_______" + "拉群数上限" + pullMax);

                            AlipayAccountFlockClickBean alipayAccountFlockClickBeanTo = new AlipayAccountFlockClickBean();
                            alipayAccountFlockClickBeanTo.setUid(SPUtils.getString(context, "uid", "0000"));
                            clickAccountBenaList = new ArrayList<>();
                            while (switchFlag || switchAccount()) {
                                switchFlag = false;
                                astrict = true;//重置支付宝是否被被限制
                                getName_zfb();//获取支付宝帐号
                                if (isAstrictRemark) {//分男女
                                    startAlterNameMark();//修改备注
                                    if (astrict)//判断支付宝帐号是否被限制
                                        addCrowdMark();//拉群
                                } else {//不分男女
                                    startAlterName();//修改备注
                                    addCrowd();//拉群
                                }
                            }
                            alipayAccountFlockClickBeanTo.setAccount(clickAccountBenaList);
                            String strClickTo = gson.toJson(alipayAccountFlockClickBeanTo);
                            LogUtils.e(strClickTo);
                            updata_group_member_count(0, strClickTo);
                            break;
                        case -3://获取群人数

                            AlipayAccountFlockNumBean alipayAccountFlockNumBean = new AlipayAccountFlockNumBean();
                            alipayAccountFlockNumBean.setUid(SPUtils.getString(context, "uid", "0000"));
                            accountBeanList = new ArrayList<>();
                            while (switchFlag || switchAccount()) {//上传群成员数量
                                switchFlag = false;
                                getName_zfb();//获取支付宝帐号
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
                            isExist = false;
                            getName_zfb();//获取支付宝帐号

                            flockBeanList.clear();
                            AlipayBillStatisticsBean alipayBillStatisticsBean = new AlipayBillStatisticsBean();
                            alipayBillStatisticsBean.setAli_account(zfbNames);
                            alipayBillStatisticsBean.setUid(uid);
                            sendMessageFlockMark(messageData, crowd);//群里发消息
                            if (isExist) {
                                backHome();
                                WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
                                if (StringUtils.isEmpty(sendMore) || sendMore == null) {
                                    messageNum = wxFlockMessageBeans.length;
                                } else {
                                    messageNum = wxFlockMessageBeans.length + Integer.parseInt(sendMore);
                                }
                                alipayBillStatisticsBean.setFlock(flockBeanList);

                                String strFlockBeanList = gson.toJson(alipayBillStatisticsBean);
                                LogUtils.e(strFlockBeanList);
                                if ("1".equals(is_statistic))
                                    push_bill(strFlockBeanList);//统计
                            }
                            break;
                        case -6://支付宝群里转发消息
                            WxFlockMessageBean[] wxFlockMessageBeans1 = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
                            if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans1 != null && wxFlockMessageBeans1.length > 0) {
                                getName_zfb();
                                transmitMessageFlock(crowd, wxFlockMessageBeans1.length, true);

                            } else {
                                LogUtils.d("数据有误");
                                ShowToast.show("数据有误", (Activity) context);
                                return;
                            }
                            break;
                        case -7:
                            while (switchFlag || switchAccount()) {
                                switchFlag = false;
                                isExist = false;
                                getName_zfb();//获取支付宝帐号

                                flockBeanList.clear();
                                AlipayBillStatisticsBean alipayBillStatisticsBean2 = new AlipayBillStatisticsBean();
                                alipayBillStatisticsBean2.setAli_account(zfbNames);
                                alipayBillStatisticsBean2.setUid(uid);

                                sendMessageFlockMark(messageData, crowd);//群里发消息
                                if (isExist) {
                                    backHome();
                                    WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
                                    if (StringUtils.isEmpty(sendMore) || sendMore == null) {
                                        messageNum = wxFlockMessageBeans.length;
                                    } else {
                                        messageNum = wxFlockMessageBeans.length + Integer.parseInt(sendMore);
                                    }
                                    alipayBillStatisticsBean2.setFlock(flockBeanList);

                                    String strFlockBeanList = gson.toJson(alipayBillStatisticsBean2);
                                    LogUtils.e(strFlockBeanList);
                                    if ("1".equals(is_statistic))
                                        push_bill(strFlockBeanList);//统计
                                }
                            }
                            break;
                        case -8://指定群转发图文给好友
                            while (switchFlag || switchAccount()) {
                                switchFlag = false;
                                isExist = false;
                                getName_zfb();//获取支付宝帐号
                                flockBeanList.clear();
                                AlipayBillStatisticsBean alipayBillStatisticsBean2 = new AlipayBillStatisticsBean();
                                alipayBillStatisticsBean2.setAli_account(zfbNames);
                                alipayBillStatisticsBean2.setUid(uid);
                                backHome();
                                if (StringUtils.isEmpty(sendMore) || sendMore == null) {
                                    messageNum = 1;
                                } else {
                                    messageNum = 1 + Integer.parseInt(sendMore);
                                }
                                transmitMessageFlock2(crowd, messageNum, true);
                                alipayBillStatisticsBean2.setFlock(flockBeanList);
                                String strFlockBeanList = gson.toJson(alipayBillStatisticsBean2);
                                LogUtils.e(strFlockBeanList);
                                if ("1".equals(is_statistic))
                                    push_bill(strFlockBeanList);//统计
                            }

                            break;
                        case -9://指定群转发图文给群
                            while (switchFlag || switchAccount()) {
                                switchFlag = false;
                                isExist = false;
                                getName_zfb();//获取支付宝帐号
                                flockBeanList.clear();
                                AlipayBillStatisticsBean alipayBillStatisticsBean2 = new AlipayBillStatisticsBean();
                                alipayBillStatisticsBean2.setAli_account(zfbNames);
                                alipayBillStatisticsBean2.setUid(uid);
                                backHome();
                                if (StringUtils.isEmpty(sendMore) || sendMore == null) {
                                    messageNum = 1;
                                } else {
                                    messageNum = 1 + Integer.parseInt(sendMore);
                                }

                                transmitMessageFlock(crowd, messageNum, true);//转发消息
                                alipayBillStatisticsBean2.setFlock(flockBeanList);
                                String strFlockBeanList = gson.toJson(alipayBillStatisticsBean2);
                                LogUtils.e(strFlockBeanList);
                                if ("1".equals(is_statistic))
                                    push_bill(strFlockBeanList);//统计
                            }
                            break;
                        case -10://转账 获取名字
                            switchFlag = true;
                            while (switchFlag || switchAccount()) {
//                                getName_zfb();//获取支付宝帐号
                                SPUtils.putInt(context, "AliCountIsOver", 0);
                                switchFlag = false;
                                zfbNamesCountCommit = 0;
                                wxUtils.openAliPay();
                                sendAliCount(zfbNamesCount);
                            }
                            break;
                    }
                    backHome();
                } else {//不在微信主界面，跳转到主界面 TODO

                    if (backMark) {
                        backHome();
                        time = 1000;
                        task(task);
                    }

                }
            }
        } else {//支付宝没有安装，下载安装支付宝
            downAlipay();
            statue = "已安装支付宝需加号";
            String uid = SPUtils.getString(context, "uid", "0000");
            getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
        }
        // StatuRequest_ZFB();
    }

    /**
     * 推送任务
     * 执行任务.先判断
     *
     * @param task
     */
    public void pushTask(int task, String arg0, String arg1, String arg2) {
        boolean flag = true;
        if (isInstallApp(context, "com.eg.android.AlipayGphone")) {//判断支付宝是否安装
            wxUtils.openAliPay();//打开支付宝
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("忘记密码") || (xmlData.contains("登录") && xmlData.contains("新用户注册") && xmlData.contains("语言")) || (xmlData.contains("你的手机号") && xmlData.contains("注册"))) {//判断是否登录
                time = 0;
                LogUtils.e("请先登录支付宝");
                statue = "已安装支付宝需加号";
                String uid = SPUtils.getString(context, "uid", "0000");
                getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
                ShowToast.show("请先登录支付宝", (Activity) context);
                SPUtils.putBoolean(context, "isTag", false);
                return;
            } else {
                //证明有支付宝帐号了
                while (flag) {
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {
                        ShowToast.show("不在支付宝主界面", (Activity) context);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adb("input keyevent 4");//返回
                    } else {
                        flag = false;
                    }
                }
                switch (task) {
                    case -10:
                        if (arg0.equals("1")) {
                            Log.d("直接搜索生活号关注", "");
                            attentionAliPublicNumber(arg1);//直接搜索生活号关注

                        } else if (arg0.equals("0")) {
                            Log.d("通过群分享的名片关注", "");
                            attentionQunAliPublicNumber(arg1);//通过群分享的名片关注
                        }
                        break;
                    case -11:
                        int max = Integer.parseInt(arg1);
                        int min = Integer.parseInt(arg0);
                        int likeNum = random.nextInt(max) % (max - min + 1) + min;
                        clickZfbLike(likeNum);
                        break;
                    case -12:
                        FriendsAliRing(Integer.valueOf(arg0), arg1);
                        break;
                }
            }

        }

    }

    /**
     * 支付宝生活圈发布内容
     *
     * @param type 0文字    1图文     2视频
     */
    private void FriendsAliRing(int type, String args) {//args0 文字   1图片链接或者视频链接

        boolean flag = true;
        boolean aaa = true;
        boolean bbb = true;
        wxUtils.openAliPay();//打开支付宝
        while (flag) {
            xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {
                ShowToast.show("不在支付宝界面", (Activity) context);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adb("input keyevent 4");//返回
            } else {
                flag = false;
            }
        }

        String args_comment = null;
        String fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
        String[] imgList = null;
        List<String> downList = new ArrayList<>();
        if (type == 1 || type == 2) {
            if (!StringUtils.isEmpty(args)) {//判断请求地址是否为空
                if (type == 1) {
                    String[] materia_ssList = args.split("\\|");
                    if (materia_ssList != null) {
                        args = materia_ssList[0];
                        String[] materia_ssList2 = new String[materia_ssList.length - 1];
                        System.arraycopy(materia_ssList, 1, materia_ssList2, 0, materia_ssList2.length);
                        String[] materia_ssList3 = materia_ssList2[0].split(",");
                        String[] materia_ssList4 = materia_ssList3[materia_ssList3.length - 1].split("@");
                        materia_ssList3[materia_ssList3.length - 1] = materia_ssList4[0];
                        materia_ssList5 = new String[materia_ssList4.length - 1];
                        for (int bb = 0; bb < materia_ssList5.length; bb++) {
                            if (bb == 0) {
                                materia_ssList5[0] = materia_ssList4[1];
                            } else {
                                materia_ssList5[bb] = materia_ssList4[bb + 1];
                            }
                        }
                        imgList = materia_ssList3;
                    } else if (materia_ssList != null && materia_ssList.length == 1) {
//                    text = materia_ssList[0];
                        args = "";
                    }
                } else {//视频
                    String[] materia_ssList = args.split("\\|");
                    args = materia_ssList[0];
                    args_comment = materia_ssList[1];
                    imgList = new String[1];
                    imgList[0] = args;
                    args = "";
                }
            } else {
                LogUtils.d("朋友圈图文地址为空");
                return;
            }

            if (imgList != null && imgList.length > 0) {//下载图片
                for (int a = 0; a < imgList.length; a++) {
                    if (downFlockImg(imgList[a], 0)) {
                        downList.add(imgList[a]);
                    }
                }
            }


            if (downList.size() == 0) {
                type = 0;
            }
        }

        boolean mark = true;
        wxUtils.adbClick(280, 783, 320, 844); //点击 支付宝 主页 朋友 按钮
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        while (aaa) {
            xmlData = wxUtils.getXmlData();
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < nodeList.size(); i++) {
                NodeXmlBean.NodeBean nodeBean_zfb = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
                if (nodeBean_zfb.getResourceid() != null && "com.alipay.mobile.socialwidget:id/item_name".equals(nodeBean_zfb.getResourceid()) && nodeBean_zfb.getText() != null && nodeBean_zfb.getText().equals("朋友动态")) {
                    listXY = wxUtils.getXY(nodeBean_zfb.getBounds());//获取 支付宝朋友动态 坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                    aaa = false;
                    break;
                }
            }
            if (aaa == true) {
                wxUtils.adbUpSlide(context);//向上滑动
            }

        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("部分内容没有上传成功")) {
            ShowToast.show("该帐号不是实名号", (Activity) context);
            wxUtils.adbClick(276, 146, 357, 187);//点击忽略
            return;

        }
        wxUtils.adbClick(426, 57, 462, 87);//点击又上角的相机按钮

        int a = 0;
        switch (type) {
            case 0://文字
                wxUtils.adbClick(96, 355, 384, 388);//点击记录生活

                break;

            case 1://朋友圈图文
                wxUtils.adbClick(96, 501, 384, 534);//点击 从相册选择

//                wxUtils.adbDimensClick(context, R.dimen.x48, R.dimen.y87, R.dimen.x48, R.dimen.y87);//选择图片
                if (downList != null && downList.size() > 0 && downList.size() <= 9) {
                    LogUtils.d(downList.size() + "张下载成功___________" + imgList.length);
                    a = 0;
                    while (a < 5) {
                        a++;
                        xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("图片和视频")) {
                            switch (downList.size()) {
                                case 1:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    break;
                                case 2:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    wxUtils.adbClick(435, 123, 465, 153);//选择图片
                                    break;
                                case 3:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    wxUtils.adbClick(435, 123, 465, 153);//选择图片
                                    wxUtils.adbClick(113, 284, 143, 314);//选择图片
                                    break;
                                case 4:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    wxUtils.adbClick(435, 123, 465, 153);//选择图片
                                    wxUtils.adbClick(113, 284, 143, 314);//选择图片
                                    wxUtils.adbClick(274, 284, 304, 314);//选择图片
                                    break;
                                case 5:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    wxUtils.adbClick(435, 123, 465, 153);//选择图片
                                    wxUtils.adbClick(113, 284, 143, 314);//选择图片
                                    wxUtils.adbClick(274, 284, 304, 314);//选择图片
                                    wxUtils.adbClick(435, 284, 465, 314);//选择图片
                                    break;
                                case 6:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    wxUtils.adbClick(435, 123, 465, 153);//选择图片
                                    wxUtils.adbClick(113, 284, 143, 314);//选择图片
                                    wxUtils.adbClick(274, 284, 304, 314);//选择图片
                                    wxUtils.adbClick(435, 284, 465, 314);//选择图片
                                    wxUtils.adbClick(113, 445, 143, 475);//选择图片
                                    break;
                                case 7:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    wxUtils.adbClick(435, 123, 465, 153);//选择图片
                                    wxUtils.adbClick(113, 284, 143, 314);//选择图片
                                    wxUtils.adbClick(274, 284, 304, 314);//选择图片
                                    wxUtils.adbClick(435, 284, 465, 314);//选择图片
                                    wxUtils.adbClick(113, 445, 143, 475);//选择图片
                                    wxUtils.adbClick(275, 445, 304, 475);//选择图片
                                    break;
                                case 8:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    wxUtils.adbClick(435, 123, 465, 153);//选择图片
                                    wxUtils.adbClick(113, 284, 143, 314);//选择图片
                                    wxUtils.adbClick(274, 284, 304, 314);//选择图片
                                    wxUtils.adbClick(435, 284, 465, 314);//选择图片
                                    wxUtils.adbClick(113, 445, 143, 475);//选择图片
                                    wxUtils.adbClick(275, 445, 304, 475);//选择图片
                                    wxUtils.adbClick(435, 445, 465, 475);//选择图片
                                    break;
                                case 9:
                                    wxUtils.adbClick(274, 123, 304, 153);//选择图片
                                    wxUtils.adbClick(435, 123, 465, 153);//选择图片
                                    wxUtils.adbClick(113, 284, 143, 314);//选择图片
                                    wxUtils.adbClick(274, 284, 304, 314);//选择图片
                                    wxUtils.adbClick(435, 284, 465, 314);//选择图片
                                    wxUtils.adbClick(113, 445, 143, 475);//选择图片
                                    wxUtils.adbClick(275, 445, 304, 475);//选择图片
                                    wxUtils.adbClick(435, 445, 465, 475);//选择图片
                                    wxUtils.adbClick(113, 606, 143, 636);//选择图片
                                    break;
                            }
                            wxUtils.adbClick(320, 49, 463, 94);//确定
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                } else {
                    wxUtils.adb("input keyevent 4");
                }
                break;
            case 2://视频
                wxUtils.adbClick(96, 501, 384, 534);//点击 从相册选择
                a = 0;
                while (a < 5) {
                    a++;
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("图片和视频")) {
//                        wxUtils.adbClick(119, 119, 149, 149);   TODO copy 修改
                        wxUtils.adbClick(274, 123, 304, 153);//选择图片
                        wxUtils.adbClick(320, 49, 463, 94);//确定
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                break;
        }
        if (mark) {
            if (type != 2) {
//                while (true) {
//                    xmlData = wxUtils.getXmlData();
//                    if (xmlData.contains("这一刻的想法...")) {
//                        break;
//                    }
//                }
                // 将文本内容放到系统剪贴板里。
                ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                cm.setText(wxUtils.getFaceText(args));

//                x = context.getResources().getDimensionPixelSize(R.dimen.x160);
//                y = context.getResources().getDimensionPixelSize(R.dimen.y74);//EdiText
////            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y65, R.dimen.x320, R.dimen.y124);//点击EdiText
//                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                wxUtils.adb("input swipe " + 30 + " " + 250 + " " + 30 + " " + 250 + " " + 1000);  //长按EdiText
                wxUtils.adbClick(110, 200, 112, 202);//粘贴
            } else {

                // 将文本内容放到系统剪贴板里。
                ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                cm.setText(args_comment);

//                x = context.getResources().getDimensionPixelSize(R.dimen.x160);
//                y = context.getResources().getDimensionPixelSize(R.dimen.y74);//EdiText
////            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y65, R.dimen.x320, R.dimen.y124);//点击EdiText
//                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                wxUtils.adb("input swipe " + 30 + " " + 250 + " " + 30 + " " + 250 + " " + 1000);  //长按EdiText
                wxUtils.adbClick(110, 200, 112, 202);//粘贴

            }
            wxUtils.adbClick(366, 48, 462, 96);//发送
        }
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("部分内容没有上传成功")) {
            ShowToast.show("该帐号不是实名号", (Activity) context);
            wxUtils.adbClick(276, 146, 357, 187);//点击忽略
            return;
        }

        if (type == 1 && materia_ssList5 != null && materia_ssList5.length != 0 && materia_ssList5.toString().trim().length() != 0) {
            while (bbb) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("评论")) {
                    List<String> nodeList2 = wxUtils.getNodeList(xmlData);
                    for (int i = 0; i < nodeList2.size(); i++) {
                        NodeXmlBean.NodeBean nodeBean_comment = wxUtils.getNodeXmlBean(nodeList2.get(i)).getNode();
                        if (nodeBean_comment.getText() != null && nodeBean_comment.getText().equals("评论") && nodeBean_comment.getResourceid() != null && nodeBean_comment.getResourceid().equals("com.alipay.mobile.beehive:id/card_option_item_title")) {
                            listXY = wxUtils.getXY(nodeBean_comment.getBounds());//获取添加坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击评论坐标
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                            cm.setText(materia_ssList5[0]);
                            wxUtils.adb("input swipe " + 50 + " " + 420 + " " + 50 + " " + 420 + " " + 2000);  //长按EdiText
                            wxUtils.adbClick(80, 340, 80, 340);//点击粘贴
                            bbb = false;
                            break;
                        }
                    }
                } else {
                    wxUtils.adbUpSlide(context);//向上滑动
                }
            }

            xmlData = wxUtils.getXmlData();
            List<String> nodeList3 = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < nodeList3.size(); i++) {
                NodeXmlBean.NodeBean nodeBean_send = wxUtils.getNodeXmlBean(nodeList3.get(i)).getNode();
                if (nodeBean_send.getResourceid() != null && nodeBean_send.getResourceid().equals("com.alipay.android.phone.wallet.socialfeedsmob:id/feed_detail_comment_submit")
                        && nodeBean_send.getText() != null && nodeBean_send.getText().equals("发送")) {
                    listXY = wxUtils.getXY(nodeBean_send.getBounds());//获取 发送 按钮坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发送
                    break;
                }
            }
        }
    }

    /**
     * 支付宝生活圈点赞
     * <p>
     * TODO----------------------------------------------------------------------------
     */
    private void clickZfbLike(int likeNum) {
        boolean flag = true;
        boolean aaa = true;
        wxUtils.openAliPay();//打开支付宝
        while (flag) {
            xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {
                ShowToast.show("不在支付宝界面", (Activity) context);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adb("input keyevent 4");//返回
            } else {
                flag = false;
            }
        }
        wxUtils.adbClick(280, 783, 320, 844); //点击 支付宝 主页 朋友 按钮
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        while (aaa) {
            xmlData = wxUtils.getXmlData();
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < nodeList.size(); i++) {
                NodeXmlBean.NodeBean nodeBean_zfb = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
                if (nodeBean_zfb.getResourceid() != null && "com.alipay.mobile.socialwidget:id/item_name".equals(nodeBean_zfb.getResourceid()) && nodeBean_zfb.getText() != null && nodeBean_zfb.getText().equals("朋友动态")) {
                    listXY = wxUtils.getXY(nodeBean_zfb.getBounds());//获取 支付宝朋友动态 坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                    aaa = false;
                    break;
                }
            }
            if (aaa == true) {
                wxUtils.adbUpSlide(context);//向上滑动
            }

        }
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        int count = 0;
        String oldXmlData = "";
        ShowToast.show("点赞开始", (Activity) context);
        ShowToast.show("点赞次数" + likeNum + "次", (Activity) context);
        xmlData = wxUtils.getXmlData();
        w:
        while (true) {
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && "com.alipay.mobile.beehive:id/card_option_item_title".equals(nodeBean.getResourceid()) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc().equals("赞")
                        && nodeBean.getText() != null && nodeBean.getText().equals("赞")) {

                    listXY = wxUtils.getXY(nodeBean.getBounds());// 获取点赞的坐标

                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//打开点赞
                    count++;
                    if (count >= likeNum) {
                        break w;
                    }
                    xmlData = wxUtils.getXmlData();
                    continue w;
                }
            }
            wxUtils.adbUpSlide(context);//向上滑动
            oldXmlData = xmlData;
            xmlData = wxUtils.getXmlData();
            if (oldXmlData.equals(xmlData)) {
                break;
            }
        }
        ShowToast.show("点赞结束", (Activity) context);
    }


    /**
     * 支付宝通过群分享的名片关注
     * <p>
     * TODO----------------------------------------------------------------------------
     */
    private void attentionQunAliPublicNumber(String qunName) {
        boolean aaa = true;
        xmlData = wxUtils.getXmlData();
        if (!(xmlData.contains("朋友") || xmlData.contains("首页") || xmlData.contains("口碑") || xmlData.contains("我的"))) {
            ShowToast.show("不在支付宝主界面", (Activity) context);
            wxUtils.adb("input keyevent KEYCODE_HOME");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.openAliPay();//打开支付宝
        }
        wxUtils.adbClick(280, 783, 320, 844);
        wxUtils.adbClick(356, 36, 428, 108);
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        wxUtils.adbClick(93, 213, 456, 240);//群聊
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        while (aaa) {
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains(qunName)) {
                wxUtils.adbUpSlide(context);//向上滑动
                String xmlData2 = wxUtils.getXmlData();
                if (xmlData2.equals(xmlData)) {
                    ShowToast.show("已经到底部了，依然没找到群名", (Activity) context);
                    aaa = false;
                }
            } else {
                aaa = false;
            }
        }
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.antui:id/item_left_text") && nodeBean.getText() != null && nodeBean.getText().equals(qunName)) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取当前群的节点
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                break;
            }
        }
        xmlData = wxUtils.getXmlData();
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = nodeList.size() - 1; a > 0; a--) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if (nodeBean.getText() != null && nodeBean.getText().equals("生活号分享") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.chatapp:id/biz_title")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取当前群的节点
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                break;

            }

        }
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("关注")) {
            ShowToast.show("已经关注了", (Activity) context);
            return;

        }
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.pubsvc:id/life_follow_checkbox") && nodeBean.getText() != null && nodeBean.getText().equals("关注")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取当前节点
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                break;
            }
        }


    }

    /**
     * 支付宝直接搜索生活号关注
     * <p>
     */
    private void attentionAliPublicNumber(String aLiPublicNumberName) {
        xmlData = wxUtils.getXmlData();
        if (!(xmlData.contains("朋友") || xmlData.contains("首页") || xmlData.contains("口碑") || xmlData.contains("我的"))) {
            ShowToast.show("不在支付宝主界面", (Activity) context);
            wxUtils.adb("input keyevent KEYCODE_HOME");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.openAliPay();//打开支付宝
            xmlData = wxUtils.getXmlData();
        }
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialwidget:id/social_tab_text") && nodeBean.getText() != null && nodeBean.getText().equals("朋友")) {
                wxUtils.adbClick(280, 783, 320, 844);
                break;
            }
        }
        wxUtils.adbClick(434, 48, 462, 96);//点击右上角的+号
        wxUtils.adbClick(352, 185, 444, 217);//点击添加朋友
        wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
        xmlData = wxUtils.getXmlData();
        nodeList = wxUtils.getNodeList(xmlData);
        for (int b = 0; b < nodeList.size(); b++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.antui:id/item_left_text") && nodeBean.getText() != null && nodeBean.getText().equals("生活号")) {
                wxUtils.adbClick(111, 679, 183, 712);
                break;
            }
        }
        wxUtils.adbClick(426, 54, 462, 90);//点击右上角的搜索
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        cm.setText(aLiPublicNumberName);
        wxUtils.adb("input swipe " + 200 + " " + 90 + " " + 200 + " " + 90 + " " + 3000);  //长按EdiText
//        xmlData =wxUtils.getXmlData();
//        nodeList = wxUtils.getNodeList(xmlData);
        wxUtils.adbClick(70, 184, 75, 213);//点击粘贴
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adbClick(396, 36, 480, 108);//点击搜索
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("没有找到") && xmlData.contains("抱歉")) {
            ShowToast.show("搜索的公众号存在", (Activity) context);
            return;
        }
        nodeList = wxUtils.getNodeList(xmlData);
        for (int c = 0; c < nodeList.size(); c++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(c)).getNode();
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.android.phone.businesscommon.globalsearch:id/title") && nodeBean.getText() != null && nodeBean.getText().equals(aLiPublicNumberName)) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));
                break;
            }
        }
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        nodeList = wxUtils.getNodeList(xmlData);
        for (int d = 0; d < nodeList.size(); d++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(d)).getNode();
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.pubsvc:id/life_follow_checkbox") && nodeBean.getText() != null && nodeBean.getText().equals("关注")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取坐标
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击关注
                ShowToast.show("关注支付宝公众号成功", (Activity) context);
                break;
            }
        }

    }

    /**
     * 支付宝群发给好友
     * <p>
     * (发单)   TODO----------------------------------------------------------------------------
     */
    private void transmitMessageFlock2(String flockName, int messageNum, boolean isInit) {
        String countData = "";
        boolean aaa = false;
        //zhangshuai
        if (StringUtils.isEmpty(flockName) || !(flockName != null && messageNum > 0)) {
            return;
        }
        wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊

        if (isInit) {
            markData = "";
//            String markData = SPUtils.getString(context, "alipaySendMessageData", "");
//            LogUtils.d("markData:" + markData);
        }
        xmlData = wxUtils.getXmlData();
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if ("com.alipay.mobile.antui:id/item_left_text".equals(nodeBean.getResourceid()) && flockName.equals(nodeBean.getText())) {//到指定群取数据
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                aaa = true;
                break;
            }
        }
        if (!aaa) {
            ShowToast.show("找不到指定群", (Activity) context);
            statue = "找不到指定群名异常";
            String uid = SPUtils.getString(context, "uid", "0000");
            getGuzhang(statue + ":log_id的值是:" + zfbLodId + ":故障帐号是:" + zfbNames, zfbNames, uid);
//            zfbQunState =false;
//            SPUtils.putBoolean(context, "zfbQunState", zfbQunState);//保存数据
            return;
        } else {
//           SPUtils.putBoolean(context, "zfbQunState", zfbQunState);//保存数据
            int qunAccount = SPUtils.getInt(context, "zfbQunAccount", 0);
            qunAccount++;
            SPUtils.putInt(context, "zfbQunAccount", qunAccount);
            //将获取到满足条件的群 的 帐号的总数记录下来，比支付宝帐号总数比较，小于支付宝帐号就说明有部分帐号，指定的群没找到，任务失败
        }

        w:
        while (true) {
            /*xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("群公告") && xmlData.contains("群聊设置"))) {
                return;
            }*/
            while (true) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    ShowToast.show("任务被中断", (Activity) context);
                    return;
                } else if (!(xmlData.contains("群公告") && xmlData.contains("群聊设置"))) {
                    wxUtils.adb("input keyevent 4");
                } else if (xmlData.contains("群公告") && xmlData.contains("群聊设置") && !xmlData.contains(flockName)) {
                    backHome();
                    transmitMessageFlock2(flockName, messageNum, false);
                    return;
                } else {
                    wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
                    break;
                }
            }

            int count = 0;
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size(); a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                if ("com.alipay.mobile.chatapp:id/chat_msg_template_4_rl".equals(nodeBean.getResourceid())) {//更多
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
//                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    break;
                } else if ("com.alipay.mobile.chatapp:id/chat_msg_text".equals(nodeBean.getResourceid())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    wxUtils.adb("input swipe 200 607 200 315");//向下滑动
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("删除") && xmlData.contains("收藏") && xmlData.contains("更多")) {
                for (int a = nodeList.size(); a > 0; a--) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                    if ("com.alipay.mobile.antui:id/item_name".equals(nodeBean.getResourceid()) && "更多".equals(nodeBean.getText())) {//更多
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击更多
                        count++;
                        break;
                    }
                }
            } else {
                continue w;
            }
            xmlData = wxUtils.getXmlData();
            wh:
            while (count < messageNum) {//选择转发数据
                nodeList = wxUtils.getNodeList(xmlData);
                if (xmlData.contains("com.alipay.mobile.chatapp:id/bottom_chat_op_forward")) {//判断是否在转发页面
                    for (int a = nodeList.size(); a > 0; a--) {
                        if (count >= messageNum) {
                            break wh;
                        }
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                        if ("com.alipay.mobile.chatapp:id/chat_msg_checkbox".equals(nodeBean.getResourceid()) && !nodeBean.isChecked()) {//选中....
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击更多
                            count++;
                        }
                    }
                    String oldXmlData = xmlData;
                    wxUtils.adb("input swipe 200 200 200 630");//向下滑动
                    xmlData = wxUtils.getXmlData();
                    if (oldXmlData.equals(xmlData)) {
                        break w;
                    }

                } else {
                    continue w;
                }

                if (count != messageNum) {
                    LogUtils.d(count + "转发数=" + messageNum);
                }
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("com.alipay.mobile.chatapp:id/bottom_chat_op_forward")) {//转发id
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y374, R.dimen.x107, R.dimen.y391);//转发
            } else {
                LogUtils.d("不在转发页面");
                continue w;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("创建新聊天")) {
                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//多选
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("从通讯录选择")) {
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y123);//从通讯录选择
                } else {
                    continue w;
                }

            } else {
                LogUtils.d("不在创建新聊天页面");
                continue w;
            }
            xmlData = wxUtils.getXmlData();

            while (true) {//滑动到底部
                String oldXmlData = xmlData;
                wxUtils.adb("input swipe 200 700 200 200 40");//滑动到底部
                xmlData = wxUtils.getXmlData();
                if (oldXmlData.equals(xmlData))
                    break;
            }
            int countClick = 0;
            wh2:
            while (true) {
                markFlockBeans.clear();
                xmlData = wxUtils.getXmlData();
                nodeList = wxUtils.getNodeList(xmlData);
                //到了选择联系人的界面
                if (xmlData.contains("选择联系人") && xmlData.contains("确定")) {
                    for (int a = nodeList.size(); a > 0; a--) {
                        if (countClick >= 9 || a <= 4) {
                            break;
                        }
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();//单选框
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(a - 3)).getNode();//联系人人名
                        if ("com.alipay.mobile.socialcontactsdk:id/selected_check_box".equals(nodeBean.getResourceid()) && !nodeBean.isChecked() && nodeBean2.getText() != null && !countData.contains(nodeBean2.getText())) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击选择
                            countData = countData + nodeBean2.getText() + ",";//记录发过的
                            countClick++;
                        }
                    }
                    if (countClick == 0) {
                        wxUtils.adb("input swipe 200 223   200 600  ");
                    }
                }
                String oldXmlData = xmlData;
                xmlData = wxUtils.getXmlData();
                if (countClick != 0) {
                    break wh2;
                }
                if (countClick == 0 && oldXmlData.contains("选择群聊")) {
                    break wh2;
                }
            }

            if (countClick > 0) {
//                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//确认
                wxUtils.adbClick(380, 60, 420, 80);//确定
                wxUtils.adbClick(380, 60, 420, 80);//发送
//                xmlData = wxUtils.getXmlData();
//                if (!xmlData.contains("选择联系人")) {
//                    continue w;
//                }
//
//                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//确认
//                xmlData = wxUtils.getXmlData();
//                if (!(xmlData.contains("选择") && xmlData.contains("发送"))) {
//                    continue w;
//                }
//                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//发送

            } else if (countClick == 0) {
                LogUtils.e("发单任务完成");
                SPUtils.putString(context, "alipaySendMessageData", "");//保存数据
                break w;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("发送给") && xmlData.contains("com.alipay.mobile.socialshare:id/positive")) { //TODO
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = nodeList.size(); a > 0; a--) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                    if ("com.alipay.mobile.socialshare:id/positive".equals(nodeBean.getResourceid())) {//发送
                        listXY = wxUtils.getXY(nodeBean.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发送
                        flockBeanList.addAll(markFlockBeans);
                        markData = markData + countData + ",";//记录发过的
                        SPUtils.putString(context, "alipaySendMessageData", markData);//保存数据

                        while (true) {//判断是否在转发群页面
                            xmlData = wxUtils.getXmlData();
                            if (xmlData.contains("群公告") && xmlData.contains("群聊设置")) {
                                break;
                            }
                        }
                        break;
                    }
                }

            } else {
                LogUtils.d("不在发送给页面");
                continue w;
            }


        }
    }


    /**
     * 下载支付宝
     */
    private void downAlipay() {
        final String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alipay.apk";
        new Thread(new Runnable() {
            @Override
            public void run() {

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
        }).start();

    }

//    http://103.94.20.102:8087/download/sougoushurufa_650.apk  搜狗输入法

    /**
     * 下载apk
     */
    public void downApk(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(url)) {
                    statue = "apk下载失败，下载链接错误";
                    LogUtils.e("apk下载失败，下载链接错误");
                    String uid = SPUtils.getString(context, "uid", "0000");
                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
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
                        getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
                    }
                } else {
                    LogUtils.d("开始安装");
                    wxUtils.adb("pm install -r " + path);
                    LogUtils.d("安装完成");
                }

            }
        }).start();

    }

    /**
     * 关闭支付宝广告页面
     */
    private void ClosedImage() {
        List<String> image_list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < image_list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(image_list.get(i)).getNode();
            //if ("com.alipay.android.phone.discovery.o2ohome:id/imageView_mask".equals(nodeBean.getResourceid())){
            if ("com.alipay.android.phone.discovery.o2ohome:id/image_close".equals(nodeBean.getResourceid())) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击广告×按钮
                // }
            }
        }

    }

    /**
     * 统计好友数量
     */
    private void StatisticsFriend() {
        backHome_tow_zfb();
        wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击t通讯录按钮
        w:
        while (true) {
            xmlData = wxUtils.getXmlData();
            String TAG = xmlData;
            List<String> friend_num = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < friend_num.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(friend_num.get(i)).getNode();
                if ("com.alipay.mobile.socialcontactsdk:id/tv_total_count".equals(nodeBean.getResourceid())) {
                    zfb_friend_num = nodeBean.getText();//获取支付宝好友数量的接口
                    zfb_friend_num = zfb_friend_num.replace("个朋友", "");
                    wxUtils.adb("input keyevent 4");//返回
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
                if (errorCode != 200) {
                    statue = "发送申请好友数量网络出现故障";
                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
                    ShowToast.show("网络请求失败，请检测网络", (Activity) context);
                }
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
                if (errorCode != 200) {
                    statue = "将好友数量发送到后台出现故障";
                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
                    ShowToast.show("网络请求失败，请检测网络", (Activity) context);
                }
            }
        });
    }

    private void Checkaccount() {
        LogUtils.d("检测帐号第一步");
        xmlData = wxUtils.getXmlData();
        List<String> zfb_friend = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < zfb_friend.size(); i++) {
            LogUtils.d("检测帐号第二步");
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(zfb_friend.get(i)).getNode();
            if ("com.alipay.mobile.contactsapp:id/add_text".equals(nodeBean.getResourceid())) {
                LogUtils.d("检测帐号第三步");
                if (!"已添加".equals(nodeBean.getText()) || !"等待验证".equals(nodeBean.getText())) {
                    LogUtils.d("检测帐号第四步");
                    ZfbFenghao();
                }
            }
        }
    }

    Random random = new Random();
    private int chen_limit = 0;//请求次数
    int send_frend_sum = 0;

    /**
     * 添加支付宝好友
     */
    private void AddFriend() {
        // ZFBHttpPhone();//请求网络获取电话号码并且添加联系人
        send_frend_sum = 0;
        send_friend_num = 0;
        int flag = 0;
        String zfb_personalCount = "";
        backHome_tow_zfb();
        //先点击 有右下角 我的，获取当前的帐号
        wxUtils.adbClick(400, 783, 440, 844);
        xmlData = wxUtils.getXmlData();
        List<String> personalMessage = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < personalMessage.size(); a++) {
            NodeXmlBean.NodeBean personalMessageBean = wxUtils.getNodeXmlBean(personalMessage.get(a)).getNode();
            if (personalMessageBean.getText() != null && personalMessageBean.getResourceid() != null && personalMessageBean.getResourceid().equals("com.alipay.android.phone.wealth.home:id/user_account")) {
                zfb_personalCount = personalMessageBean.getText();
                break;
            }
        }
        wxUtils.adbDimensClick(context, R.dimen.x27, R.dimen.y367, R.dimen.x53, R.dimen.y395);//点击首页按钮
        wxUtils.adbDimensClick(context, R.dimen.x289, R.dimen.y23, R.dimen.x308, R.dimen.y45);//进入支付宝 点击添加好友按钮x235
        wxUtils.adbDimensClick(context, R.dimen.x235, R.dimen.y87, R.dimen.x296, R.dimen.y102);//点击添加朋友
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y227, R.dimen.x320, R.dimen.y271);//d点击添加手机联系人按钮
        try {
            Thread.sleep(10000);//等待5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        String TAGS = xmlData;
        boolean mark = true;
        while (mark) {
            List<String> zfb_friend = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("添加")) {
                for (int i = 0; i < zfb_friend.size(); i++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(zfb_friend.get(i)).getNode();
                    if ("com.alipay.mobile.contactsapp:id/re_operation".equals(nodeBean.getResourceid())) {
                        if ("添加".equals(nodeBean.getText())) {
                            if (chen_limit > send_frend_sum) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                LogUtils.d("chen_limit的值是" + chen_limit + ":send_frend_sum的值是" + send_frend_sum);
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加
                                // Checkaccount();//检测支付宝帐号是否已经被封号
//                                    try {
//                                        Thread.sleep(5000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
                                if (TAGS.equals(wxUtils.getXmlData())) {
                                    LogUtils.d("帐号好像被限制权限");
                                    ZfbFenghao();


                                    break;
                                }
                                send_friend_num++;
                                send_frend_sum++;//用于比较
                                xmlData = wxUtils.getXmlData();
                                if (xmlData.contains("该手机号对应多个支付宝账户，请核实后选择") || xmlData.contains("确定")) {
                                    List<String> shangxian = wxUtils.getNodeList(xmlData);
                                    for (int a = 0; a < shangxian.size(); a++) {
                                        NodeXmlBean.NodeBean nodeBean_shangxian = wxUtils.getNodeXmlBean(shangxian.get(a)).getNode();
                                        LogUtils.d("saasaasd" + nodeBean_shangxian.getResourceid());
                                        if (xmlData.contains("该手机号对应多个支付宝账户，请核实后选择")) {
                                            if ("com.alipay.mobile.contactsapp:id/user_name".equals(nodeBean_shangxian)) {//如果有多个帐号 则点击第一个帐号进行添加
                                                listXY = wxUtils.getXY(nodeBean_shangxian.getBounds());//获取添加坐标
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击第一个帐号
                                            }
                                        }
                                        if ("确定".equals(nodeBean_shangxian.getText())) {
                                            if ("android:id/button1".equals(nodeBean_shangxian.getResourceid())) {
                                                listXY = wxUtils.getXY(nodeBean_shangxian.getBounds());//获取确认坐标
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击确认按
                                                statue = "添加好友次数达到上限";
                                                String uid = SPUtils.getString(context, "uid", "0000");
                                                getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
                                                //wxUtils.adb("input keyevent 4");//返回
                                                mark = false;
                                                backHome();
                                                LogUtils.d("第一个发送的啊啊啊");
                                                SendSenQFrendsNum();
                                            }
                                        }

                                    }
                                }
                                LogUtils.s("发送了所少个添加帐号的请求。" + send_friend_num);
                                    /*int s = random.nextInt(2)+1;
                                    try {
                                        LogUtils.s("你到底休眠了多少时间" + s);
                                        Thread.sleep(s * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }*/

                            } else {
                                ShowToast.show("添加好友次数达到输入值的次数。", (Activity) context);
                                LogUtils.d("第二个发送的啊啊啊");
                                SendSenQFrendsNum();
                                String xmlData2 = wxUtils.getXmlData();
                                SendFrendsMessaages(zfb_personalCount, xmlData2);
                                return;
                            }
                        }
                    }
                }
                if (send_friend_num == 0) {
                    statue = "获取的手机号码无对应的支付宝账户或该帐号访问联系人被限制";
                    String uid = SPUtils.getString(context, "uid", "0000");
                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);

                }
                String TAG = xmlData;
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (TAG.equals(xmlData)) {
                    LogUtils.d("第三个发送的啊啊啊");
                    SendSenQFrendsNum();
                    break;
                }
            } else {
                mark = false;
                ShowToast.show("没有可添加的好友，任务结束", (Activity) context);
                LogUtils.d("第四个发送的啊啊啊");
                SendSenQFrendsNum();
                long s = System.currentTimeMillis() / 1000 + 86400;
                LogUtils.d("启动的时间戳是" + s);
                backHome();
            }
        }


    }

    private void SendFrendsMessaages(String zfb_personalCount, String xmlData) {

        if (xmlData.contains("已添加") || xmlData.contains("等待验证")) {
            //证明申请加好友成功了
            List<String> friendsNodeLists = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < friendsNodeLists.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(friendsNodeLists.get(a)).getNode();
                if (nodeBean.getResourceid() != null && "com.alipay.mobile.contactsapp:id/add_text".equals(nodeBean.getResourceid())
                        && nodeBean.getText() != null && (nodeBean.getText().equals("等待验证") || nodeBean.getText().equals("已添加"))) {//获取 添加按钮的位置的节点
                    String friends_name = wxUtils.getNodeXmlBean(friendsNodeLists.get(a - 1)).getNode().getText();
                    String[] strs = friends_name.split(":");
                    friends_name = strs[1];
                    String friends_number = wxUtils.getNodeXmlBean(friendsNodeLists.get(a - 2)).getNode().getText();
                    StatisticsAliNewFrinedsMessages(friends_name, friends_number, zfb_personalCount);
                }
            }
        }
    }

    /**
     * 上传支付宝新增好友的信息
     */
    private void StatisticsAliNewFrinedsMessages(String friends_name, String friends_number, String zfb_count) {
        String zfb_Phone = null;
        String[] zfb_name1 = null;
        String zfb_name = null;
        xmlData = wxUtils.getXmlData();
        int sum = 0;
        String uid = SPUtils.getString(context, "uid", "0000");
        List<AliFriendsMessageBean> mAliFriendsMessageBean = new ArrayList<>();
        AliFriendsMessageBean aliFriendsMessageBean = new AliFriendsMessageBean(uid, zfb_count, friends_number, friends_name);
        mAliFriendsMessageBean.add(aliFriendsMessageBean);
        String str = new Gson().toJson(mAliFriendsMessageBean);
        LogUtils.d("JSON" + str.toString());
        SendAliFriendsMessage(str);
    }

    private void SendAliFriendsMessage(String str) {
        RequestParams params = new RequestParams(ZFB_URLS.SendAliFriendsMessage());
        params.addBodyParameter("json", str.replace("\\", ""));

        HttpManager.getInstance().sendPostRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                LogUtils.d("支付宝好友信息上传成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
//                LogUtils.d("好友数量上传失败");
            }
        });
    }

    /**
     * 支付宝帐号被封号几天的情况
     */
    private void ZfbFenghao() {
        boolean limit = false;//是否有消息中心

        boolean havaMessage = false;//是否有限权消息
        String times_fenghao = "";
        String neitong = "";
        boolean mark = true;
        statue = "该帐号已经被封号";
        LogUtils.d("作出帐号被封号的处理");
        String uid = SPUtils.getString(context, "uid", "0000");
        backHome();
        wxUtils.adbDimensClick(context, R.dimen.x160, R.dimen.y363, R.dimen.x240, R.dimen.y400);//点击朋友按钮
        w:
        while (mark) {
            xmlData = wxUtils.getXmlData();
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < nodeList.size(); i++) {
                NodeXmlBean.NodeBean nodeBean_duoci = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
                if ("com.alipay.mobile.socialwidget:id/item_name".equals(nodeBean_duoci.getResourceid())) {
                    if ("消息中心".equals(nodeBean_duoci.getText())) {
                        limit = true;
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
                            if ("com.alipay.android.phone.messageboxapp:id/tv_system_todo_content".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText().contains("当前账户涉嫌发布违规信息")) {
                                havaMessage = true;
                                neitong = nodeBean.getText();
                                neitong = neitong.substring(13, 14);
                                LogUtils.d("封号的天数是" + neitong);
                                RequestParams params = new RequestParams(zfb_urls.Title_zfb_fenghao());
                                params.addQueryStringParameter("uid", uid);
                                params.addQueryStringParameter("account", zfbNames);
                                params.addQueryStringParameter("frozen_time", times_fenghao);
                                params.addQueryStringParameter("frozen_day", neitong);
                                LogUtils.d("支付宝帐号被封号返回的接口是:" + zfb_urls.Title_zfb_fenghao() + "?uid=" + uid + "&account=" + zfbNames + "&frozen_time=" + times_fenghao + "&frozen_day=" + neitong);
                                HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
                                    @Override
                                    public void onSuccess(LogidBean bean) {
                                        LogUtils.d("支付宝被封号请求的数据结果是:" + bean.getRet());
                                    }

                                    @Override
                                    public void onFailure(int errorCode, String errorString) {

                                    }
                                });
                                break w;
                            }

                        }

                        break w;
                    }


                }
            }

            String TAG = xmlData;
            wxUtils.adbUpSlide(context);
            xmlData = wxUtils.getXmlData();
            if (TAG.equals(xmlData)) {
                mark = false;
                break;
            }
        }

        //有消息中心并且有封号信息
        if (limit && havaMessage) {
            getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
        } else if (!limit) {
            //没有消息中心
            SendSenQFrendsNum();//上次传点击申请数
        }

        //有消息中心并且没有封号信息
        if (limit && !havaMessage) {
            //判断上次加粉数量
            get_latest();
        }

    }

    /**
     * 上次加粉数
     */
    private void get_latest() {
        RequestParams params1 = new RequestParams(zfb_urls.SendStatue());
        params1.addQueryStringParameter("account", zfbNames);
        LogUtils.d("支付宝上次加粉数:" + zfb_urls.get_latest() + "?account=" + zfbNames);
        HttpManager.getInstance().sendRequest(params1, new HttpObjectCallback<LogidBean>() {

            @Override
            public void onSuccess(LogidBean bean) {
                if ("1".equals(bean.getData())) {
                    //上次限权信息
                    getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);

                    //上传封号时间
                    RequestParams params = new RequestParams(zfb_urls.Title_zfb_fenghao());
                    params.addQueryStringParameter("uid", uid);
                    params.addQueryStringParameter("account", zfbNames);
                    params.addQueryStringParameter("frozen_time", TimeUtil.currentDatetime());
                    params.addQueryStringParameter("frozen_day", "7");
                    LogUtils.d("支付宝帐号被封号返回的接口是:" + zfb_urls.Title_zfb_fenghao() + "?uid=" + uid + "&account=" + zfbNames + "&frozen_time=" + TimeUtil.currentDatetime() + "&frozen_day=7");
                    HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
                        @Override
                        public void onSuccess(LogidBean bean) {
                            LogUtils.d("支付宝被封号请求的数据结果是:" + bean.getRet());
                        }

                        @Override
                        public void onFailure(int errorCode, String errorString) {

                        }
                    });

                } else {
                    SendSenQFrendsNum();//上次传点击申请数
                }
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("获取上次加粉数量失败" + errorString);
                SendSenQFrendsNum();//上次传点击申请数
            }
        });
    }

    /**
     * 支付宝请求网络的电话号码，然后将电话号码添加到手机联系人 ，执行前进行清除
     */
    private void ZFBHttpPhone() {
        Map<String, String> map = new HashMap<String, String>();
        final String uid = SPUtils.getString(context, "uid", "0000");
        String phone_name = "";
        int limit = 0;
        String add_phone = "";
        if (ali_add_num_s == 0 || ali_add_num_e == 0) {
            ali_add_num_s = 30;
            ali_add_num_e = 30;
            limit = 30;
            chen_limit = limit;
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
                statue = "网络请求后台电话号码出现故障";
                getGuzhang(statue + ":log_id的值是:" + logId + ":故障帐号是:" + zfbNames, zfbNames, uid);
                ShowToast.show("网络请求失败，请检测网络", (Activity) context);
            }
        });
        ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
        ShowToast.show("添加手机号码成功,请稍后20s...", (Activity) context);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShowToast.show("开始添加好友...", (Activity) context);
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
//            params.addQueryStringParameter("json", json);
            params.addBodyParameter("json", json.replace("\\", ""));
            LogUtils.d(ZFB_URLS.updata_group_rquest_count() + "?json=" + json);
        } else if (type == 1) {
            params = new RequestParams(ZFB_URLS.updata_group_member_count());
//            params.addQueryStringParameter("json", json);
            params.addBodyParameter("json", json.replace("\\", ""));
            LogUtils.d(ZFB_URLS.updata_group_member_count() + "?json=" + json);
        }

        HttpManager.getInstance().sendPostRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                LogUtils.d("群信息上传成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("群信息上传失败" + errorString);
            }
        });
    }

    /**
     * 上传群数据
     *
     * @param json 发单统计群信息
     */
    private void push_bill(String json) {
        RequestParams params = new RequestParams(ZFB_URLS.push_bill());
//            params.addQueryStringParameter("json", json);
//            params.setBodyContent(json);
        params.addBodyParameter("json", json.replace("\\", ""));
        LogUtils.d(json.replace("\\", ""));
        LogUtils.d("--------------");
        LogUtils.showLargeLog(json.replace("\\", ""), 500, "zplh");
        wxUtils.write(json);
        HttpManager.getInstance().sendPostRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                ShowToast.show("统计信息上传成功", (Activity) context);
                LogUtils.d("发单群信息上传成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                ShowToast.show("统计失败" + errorString, (Activity) context);
                LogUtils.d("发单群信息上传失败");
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

    public void setLogId(int taskId, String logId, int ali_add_num_s, int ali_add_num_e, String contact_verify_msg, String crowd) {
        this.ali_add_num_s = ali_add_num_s;
        this.ali_add_num_e = ali_add_num_e;
        this.logId = logId;
        this.taskId = taskId;
        this.contact_verify_msg = contact_verify_msg;
        this.crowd = crowd;
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
                status = 0;
                wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                break;
            }
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if ("com.alipay.mobile.antui:id/item_left_text".equals(nodeBean.getResourceid())) {//"A000101"

                    /*if (nodeBean.getText().length() < 7) {
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

                    if (!boyEnd && !girlEnd) {//拉群任务完成
                        wxUtils.adb("input keyevent 4");
                        ShowToast.show("拉群任务完成", (Activity) context);
                        break w;
                    }*/
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
//              ShowToast.show("拉群任务完成", (Activity) context);
                break;
            }
        }
        AlipayAccountFlockNumBean.AccountBean accountBean = new AlipayAccountFlockNumBean.AccountBean();
        accountBean.setAlipay_account(zfbNames);
        accountBean.setFlock(flockBeanList);
        accountBeanList.add(accountBean);
        LogUtils.d(bogCount + "___girl" + girlCount);

        //拉完群改名
//        ShowToast.show("拉群完，修改备注开始", (Activity) context);
//        qunEndAlterName();
    }

    private int status = 5;//0没群 1男群满  2女群满  3男女都满  4失败 5正常
    public int qunMaxNum = 50;//设置群人数
    public int pullMax = 1000;
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
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("群聊")) {
                ShowToast.show("任务被中断...", (Activity) context);
                break;
            }

            if (xmlData.contains("新群聊") && xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
                status = 0;
                wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                break;
            }
            List<String> nodeListA = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeListA.size(); a++) {

                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeListA.get(a)).getNode();
                if ("com.alipay.mobile.antui:id/item_left_text".equals(nodeBean.getResourceid())) {//"A000101"

                    if (nodeBean.getText().length() < 7) {
                        continue;
                    }
                    if (!(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B"))) {
                        continue;
                    }
                    /*if ((nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !boyEnd) {
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
                    }*/
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

        LogUtils.d(bogCount + "___girl__" + girlCount);
        LogUtils.d("拒绝拉群的:" + refuseAdd);
        //拉完群改名
        ShowToast.show("拉群完，修改备注开始", (Activity) context);
        qunEndAlterNameRefuseAdd();//修改拒绝的人
        wxUtils.adb("input keyevent 4");
        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
        qunEndAlterName();
    }

    /**
     * 拉完群改名修改备注.
     */
    private void qunEndAlterName() {
//        int boyAlterCount = 0;
//        int girlAltCount = 0;
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

            /*if (boyAlterCount >= bogCount && girlAltCount >= girlCount) {
                ShowToast.show("拉群后改名完成", (Activity) context);
                break w;
            }*/

            boolean hideMark = false;
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialcontactsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && countStr.contains(nodeBean.getText()) && !alterName.contains(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);
                    /*if (nodeBean.getText().contains("A") && boyAlterCount >= bogCount) {
                        continue;
                    } else if (nodeBean.getText().contains("B") && girlAltCount >= girlCount) {
                        continue;
                    }*/
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
//                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (xmlData.contains("设置备注")) {
                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y192, R.dimen.x280, R.dimen.y226);//设置备注
                    } else {
                        continue;
                    }

                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y60, R.dimen.x320, R.dimen.y96);//点击名字EditText
//                    wxUtils.adbDimensClick(context, R.dimen.x288, R.dimen.y58, R.dimen.x305, R.dimen.y72);//清空名字1920*1080
//                    wxUtils.adbDimensClick(context, R.dimen.x284, R.dimen.y71, R.dimen.x304, R.dimen.y85);//清空名字480*854
                    wxUtils.adbDimensClick(context, R.dimen.x288, R.dimen.y71, R.dimen.x304, R.dimen.y71);//清空名字

                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '1';
                        sexStr = new String(ch);
                    }
                    LogUtils.e("input text \"" + sexStr + "\"");
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    LogUtils.d("修改后删除数据库" + DataSupport.deleteAll(AlipayAlterNameSqliteBean.class, "alipayname = ? and newName = ?", zfbNames, name));


                    alterName = alterName + name + ",";
                    neutralAlterCount++;
//                    wxUtils.adb("input keyevent 4");
//
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
            if (!xmlData.contains("通讯录")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                break w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialcontactsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && countStr.contains(nodeBean.getText())) {
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
//                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y367, R.dimen.x80, R.dimen.y400);//点击微信
                ShowToast.show("修改备注完成,拉群任务完成", (Activity) context);
                LogUtils.d(neutralAlterCount + "修改备注完成,拉群任务完成" + neutralCount);
//                LogUtils.d(boyAlterCount + "修改备注完成,拉群任务完成" + girlAltCount);
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

            boolean hideMark = false;
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialcontactsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && refuseAdd.contains(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);
                    if (refuseAddCount >= refuseAdds.length) {
                        break w;
                    }
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
//                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (xmlData.contains("设置备注")) {
                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y192, R.dimen.x280, R.dimen.y226);//设置备注
                    } else {
                        continue;
                    }

                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y60, R.dimen.x320, R.dimen.y96);//点击名字EditText
//                    wxUtils.adbDimensClick(context, R.dimen.x288, R.dimen.y58, R.dimen.x305, R.dimen.y72);//清空名字1920*1080
//                    wxUtils.adbDimensClick(context, R.dimen.x284, R.dimen.y71, R.dimen.x304, R.dimen.y85);//清空名字480*854
                    wxUtils.adbDimensClick(context, R.dimen.x288, R.dimen.y71, R.dimen.x304, R.dimen.y71);//清空名字

                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '9';
                        sexStr = new String(ch);
                    }
                    LogUtils.e("input text \"" + sexStr + "\"");
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    LogUtils.d("修改后删除数据库" + DataSupport.deleteAll(AlipayAlterNameSqliteBean.class, "alipayname = ? and newName = ?", zfbNames, name));

                    refuseAddCount++;
//                    wxUtils.adb("input keyevent 4");

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
            if (!xmlData.contains("通讯录")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                break w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialcontactsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && refuseAdd.contains(nodeBean.getText())) {
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


        w:
        while (true) {//获取添加按钮并点击
            String qunNameData = wxUtils.getXmlData();

            if (!qunNameData.contains("聊天信息")) {
                break;
            }

            if (qunNameData.contains("添加成员")) {

                Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
                Matcher matcher = pattern.matcher(qunNameData);
                while (matcher.find()) {
                    qunNameDataList.add(matcher.group() + "/>");
                }

                for (int a = 0; a < qunNameDataList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(qunNameDataList.get(a)).getNode();
                    if (nodeBean != null && nodeBean.getContentdesc() != null && "添加成员".equals(nodeBean.getContentdesc())) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//添加
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加
                        break w;
                    }
                }

            }
            wxUtils.adbUpSlide(context);//向上滑动
        }
        String addNameData = wxUtils.getXmlData();
        List<String> addNameList = new ArrayList<>();
        Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
        Matcher matcher = pattern.matcher(addNameData);
        while (matcher.find()) {
            addNameList.add(matcher.group() + "/>");
        }

        int flagGirlCount = 0;
        int flagBoyCount = 0;
        String flagCountStr = "";
        int flagClickCount = 0;
        int flagNeutralCount = 0;

        String flagSendCount = "";

        w:
        while (true) {

            for (int a = 0; a < addNameList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(addNameList.get(a)).getNode();

                if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ0") && !refuseAdd.contains(nodeBean.getText())) {
                    NodeXmlBean.NodeBean checkBox = wxUtils.getNodeXmlBean(addNameList.get(a + 2)).getNode();

                    if ("com.alipay.mobile.socialcontactsdk:id/selected_check_box".equals(checkBox.getResourceid()) && checkBox.isChecked()) {//isChecked true代表选中

                        if (!countStr.contains(nodeBean.getText()) && !flagCountStr.contains(nodeBean.getText())) {
                            neutralCount++;
                            flagNeutralCount++;
                            LogUtils.d(neutralCount + "neutralCount+选中");
                            flagCountStr = flagCountStr + nodeBean.getText() + ",";
                        }
                    }

                    if ("com.alipay.mobile.socialcontactsdk:id/selected_check_box".equals(checkBox.getResourceid()) && !checkBox.isChecked()) {
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

                                flagSendCount = flagSendCount + nodeBean.getText() + ",";
                                if (neutralCount >= pullMax) {
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
            Matcher matcherAdd = pattern.matcher(addNameData);
            while (matcher.find()) {
                addNameList.add(matcherAdd.group() + "/>");
            }

            if (oldAddNameData.equals(addNameData)) {
                neutralEnd = false;
                break;
            }
            int judgeGirl = 0;
            int judgeBoy = 0;
            int judgeType = 0;
            for (int a = 0; a < addNameList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(addNameList.get(a)).getNode();
                if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ1")) {
                    judgeType++;
                }
            }
            if (judgeType >= 9) {
                neutralEnd = false;
            }

        }
        if (clickCount > 0) {//确认添加
            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
            wh:
            while (true) {
                String qunLiaoBack = wxUtils.getXmlData();//修改完一个重新获取页面数据

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
                            break wh;
                        }
                    }
                } else {
                    countStr = countStr + flagCountStr + ",";

                    if (neutralCount >= pullMax) {
                        neutralEnd = false;
                    }
                }

                if (qunLiaoBack.contains("群公告") && qunLiaoBack.contains("群聊设置")) {

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
                    wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1500);  //长按输入框
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y180, R.dimen.x62, R.dimen.y180);//粘贴
                    wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y176, R.dimen.x314, R.dimen.y198);//发送

                    wxUtils.adb("input keyevent 4");
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
     * 修改备注.
     */
    private void startAlterName() {
        wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
        wxUtils.adbDimensClick(context, R.dimen.x238, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
        boolean bottom = false;//到了底部
        int sex = 2;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("0000");
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        String meName = "";

        xmlData = wxUtils.getXmlData();
        w:
        while (true) {
            if (!xmlData.contains("通讯录") && !xmlData.contains("搜索") && !xmlData.contains("添加朋友")) {
                ShowToast.show("任务被中断...", (Activity) context);
                break;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            boolean hideMark = false;//判断是否隐藏真实姓名
            a:
            for (int a = 0; a < nodeList.size(); a++) {
//                LogUtils.e(nodeList.get(a));
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && !nodeBean.getText().startsWith("ZZZ") && !meName.equals(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
//                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (xmlData.contains("设置备注")) {
                        wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y192, R.dimen.x280, R.dimen.y226);//设置备注
                    } else {
                        meName = name;
                        continue;
                    }


                   /* String hideStr = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode().getText();
                    if ("对方已隐藏真实姓名 ".contains(hideStr)) {
                        hideMark = true;
                    }

                    xmlData = wxUtils.getXmlData();//重新获取页面数据

                    xmlData = wxUtils.getXmlData();//重新获取页面数据  获取两次
                    if (xmlData.contains("该功能暂未对您开放")) {//支付宝被限制 TODO
                        wxUtils.adb("input keyevent 4");
                        LogUtils.e("支付宝帐号被限制");
                        astrict = false;
                        break w;
                    }

                    if (xmlData.contains("记录我的生活")) {
                        wxUtils.adb("input keyevent 4");
                        meName = nodeBean.getText();
                        continue;
                    }

                    if (xmlData.contains("备注她的信息")) {
                        sex = 0;
                    } else if (xmlData.contains("备注他的信息")) {
                        sex = 1;
                    } else {
                        sex = 2;
                    }
                    xmlData = wxUtils.getXmlData();
                    List<String> nodeListMarkInfo = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < nodeListMarkInfo.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeListMarkInfo.get(b)).getNode();
                        if ("com.alipay.android.phone.wallet.profileapp:id/tv_remark_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText().contains("备注")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                            break;
                        }
                    }*/
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y60, R.dimen.x320, R.dimen.y96);//点击名字EditText
//                    wxUtils.adbDimensClick(context, R.dimen.x288, R.dimen.y58, R.dimen.x305, R.dimen.y72);//清空名字1920*1080
//                    wxUtils.adbDimensClick(context, R.dimen.x284, R.dimen.y71, R.dimen.x304, R.dimen.y85);//清空名字480*854
                    wxUtils.adbDimensClick(context, R.dimen.x288, R.dimen.y71, R.dimen.x304, R.dimen.y71);//清空名字

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


//                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
//                    while (xmlData.contains("备注他的信息") || xmlData.contains("备注她的信息")) {
//                        wxUtils.adb("input keyevent 4");
//                        xmlData = wxUtils.getXmlData();
//                    }
                   /* if (hideMark) {//判断是否隐藏姓名
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }*/
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
            if (!xmlData.contains("通讯录") && !xmlData.contains("搜索") && !xmlData.contains("添加朋友")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                break w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && !nodeBean.getText().startsWith("ZZZ") && !meName.equals(nodeBean.getText())) {
                    continue w;
                } else if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText() != "" && nodeBean.getText().startsWith("ZZZ")) {
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
                wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮

                ShowToast.show("修改备注完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("个朋友")) {//判断是否到达底部
                bottom = true;
            }
        }

    }

    /**
     * 此方法专用支付宝执行任务的时候页面错乱 再次返回到支付宝执行任务页面
     */
    private void backHome_tow_zfb() {
        backNum++;
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
            wxUtils.openAliPay();//打开支付宝
        }
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
            backHome_tow_zfb();
        } else if(xmlData.contains("全屏广告")&&xmlData.contains("推荐广告")){
            wxUtils.adbClick(400,170,400,170);
            backHome_tow_zfb();
        }
        else if (!(xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {//判断是否在支付宝主界面
            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);
            if (backNum < 100) {
                backHome_tow_zfb();
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

    boolean backMark = true;
    int backNum = 0;//返回次数

    /**
     * 返回到支付宝主页面
     */
    private void backHome() {
        backMark = true;
        backNum++;
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
          /*  // 复活
            PackageManager pm = ((Activity)context).getApplication().getPackageManager();
            Intent launchIntentForPackage = pm.getLaunchIntentForPackage(((Activity)context).getApplication().getPackageName());
            ((Activity)context).startActivity(launchIntentForPackage);

            // 杀死
            android.os.Process.killProcess(android.os.Process.myPid());*/

            backMark = false;
            return;
        } else if (xmlData.contains("版本更新")) {//关闭版本更新
            LogUtils.d("aaaaaaaaaaaaa");
            closeVersionUndata();
            backHome();
        } else if (xmlData.contains("com.alipay.android.phone.discovery.o2ohome:id/image_close")) {//关闭广告
            LogUtils.d("bbbbbbbbbbbb");
            ClosedImage();
            backHome();
        } else if (!(xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {//判断是否在支付宝主界面
            LogUtils.d("cccccccccccccc");
            wxUtils.adb("input keyevent 4");
            if (backNum < 100) {
                backHome();
            }
        }

    }

    /**
     * 关闭版本更新
     */
    private void closeVersionUndata() {
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

    private int count_qiehuan = 0;

    /**
     * 切换帐号
     *
     * @return
     */
    private boolean switchAccount() {
        wxUtils.openAliPay();
        int zfbAccount = 0;
        backHome_tow_zfb();//返回到home
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        wxUtils.adbClick(400, 783, 440, 844);//我的
//        wxUtils.adbClick(411, 59, 459, 84);//设置
//        wxUtils.adbClick(21, 160, 117, 188);//帐号管理
//        wxUtils.adbClick(21, 157, 117, 190);//帐号切换
         NodeUtils.clickNode("我的","com.alipay.android.phone.wealth.home:id/tab_description");
        // TODO: 2018/4/29
        wxUtils.adbClick(628,79,692,112);//点击设置

        NodeUtils.clickNode("帐号管理","com.alipay.android.phone.openplatform:id/table_left_text");
        NodeUtils.clickNode("帐号切换","com.alipay.mobile.antui:id/item_left_text");
        xmlData =wxUtils.getXmlData();
        List<String> datalist = wxUtils.getNodeList(xmlData);
        List<NodeXmlBean.NodeBean> nodeBeanList = new ArrayList<>();
        for (int a = 0; a < datalist.size(); a++) {//获取支付宝帐号id
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(datalist.get(a)).getNode();
            if (nodeBean!=null && "com.alipay.mobile.securitybiz:id/table_left_text".equals(nodeBean.getResourceid())) {
                nodeBeanList.add(nodeBean);
                zfbAccount++;
            }
        }
        LogUtils.e(nodeBeanList.size() + "个帐号");
        SPUtils.putInt(context, "zfbAccount", zfbAccount); //将支付宝的帐号总数存到数据库
        count_qiehuan = nodeBeanList.size();
        LogUtils.d("count_qiehuan的值是" + count_qiehuan);
        if (nodeBeanList.size() > account) {
            listXY = wxUtils.getXY(nodeBeanList.get(nodeBeanList.size() - 1).getBounds());//获取最下面一个坐标
            LogUtils.d(nodeBeanList.get(nodeBeanList.size() - 1).getText());
            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击坐标
            account++;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String xmlData2 = wxUtils.getXmlData();
            if (xmlData2.contains("短信验证码")|| xmlData2.contains("刷脸登录")|| xmlData2.contains("密码登录")) {
                statue = "支付宝切换帐号异常";
                String uid = SPUtils.getString(context, "uid", "0000");
                getGuzhang(statue + ":log_id的值是:" + logId + "：故障与帐号有关", nodeBeanList.get(nodeBeanList.size() - 1).getText(), uid);
                SPUtils.putInt(context, "zfbAccountProblem", 1);
                return false;
            }
            return true;
        } else {
            account = 1;
            LogUtils.d("帐号切换完毕...");
            backHome();//返回到home
        }

        return false;
    }


    /**
     * 获取支付宝帐号
     */
    private void getName_zfb() {//先判断是否在主页面 TODO
        backHome_tow_zfb();
//        wxUtils.adbDimensClick(context, R.dimen.x267, R.dimen.y367, R.dimen.x293, R.dimen.y395);//点击首页按钮
//        xmlData = wxUtils.getXmlData();
//        String tag = xmlData;
//        xmlData = wxUtils.getXmlData();
//        if (tag.equals(xmlData)) {
//            LogUtils.d("你进入到点击弹出窗口的取消按钮没有呀");
//            wxUtils.adbDimensClick(context, R.dimen.x33, R.dimen.y77, R.dimen.x63, R.dimen.y98);//点击红包的小x
//            wxUtils.adbDimensClick(context, R.dimen.x240, R.dimen.y363, R.dimen.x320, R.dimen.y400);//点击我的按钮
//        }
//        backHome_tow_zfb();
        NodeUtils.clickNode("我的","com.alipay.android.phone.wealth.home:id/tab_description"); // 点击我的
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
//        if (xmlData.contains("全屏广告") && xmlData.contains("推荐广告")){
//            wxUtils.adbClick(400,170,400,170);
//            xmlData = wxUtils.getXmlData();
//        }
        List<String> zfb_name = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < zfb_name.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(zfb_name.get(i)).getNode();
            if (nodeBean!=null && nodeBean.getText()!=null && nodeBean.getResourceid() != null && "com.alipay.android.phone.wealth.home:id/user_account".equals(nodeBean.getResourceid())) {
                zfbNames = nodeBean.getText();
                LogUtils.d("用户名称是" + zfbNames);
                if (logid == 1) {
                    HttpUserName(zfbNames);
                }
//                if ("200".equals(pan_ret)) {
//                    return;
//                }
            }
        }
        logid = 0;
    }

    String pan_ret = "";

    private void HttpUserName(String account) {
        RequestParams params = new RequestParams(zfb_urls.ZFBConferred());
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("account", account);
        LogUtils.d(zfb_urls.ZFBConferred() + "?uid=" + uid + "&account=" + account);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {

            @Override
            public void onSuccess(LogidBean bean) {
                LogUtils.d("查看支付宝帐号是否被限制的返回结果" + bean.getRet());
                pan_ret = bean.getRet();
                if ("200".equals(bean.getRet())) {
                    is_username = false;
                    LogUtils.d("该帐号已被限制权限");
                }

            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("查看支付宝帐号是否被限制的返回失败结果:" + errorCode);
                if (errorCode == 400) {
                    LogUtils.d("该帐号未被限制权限");
                    is_username = true;
                }

            }
        });
    }

    String markData = "";
    List<AlipayBillStatisticsBean.FlockBean> markFlockBeans = new ArrayList<>();

    /**
     * 支付宝群里转发消息(发单)   TODO----------------------------------------------------------------------------
     */
    private void transmitMessageFlock(String flockName, int messageNum, boolean isInit) {
        boolean aaa = false;
        if (StringUtils.isEmpty(flockName) || !(flockName != null && messageNum > 0)) {
            return;
        }
        wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊

        if (isInit) {
            markData = "" + flockName;
//            String markData = SPUtils.getString(context, "alipaySendMessageData", "");
//            LogUtils.d("markData:" + markData);
        }
        xmlData = wxUtils.getXmlData();
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if ("com.alipay.mobile.antui:id/item_left_text".equals(nodeBean.getResourceid()) && flockName.equals(nodeBean.getText())) {//到指定群取数据
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                aaa = true;
                break;
            }
        }
        if (!aaa) {
            ShowToast.show("找不到指定群", (Activity) context);
            statue = "找不到指定群名异常";
            String uid = SPUtils.getString(context, "uid", "0000");
            getGuzhang(statue + ":log_id的值是:" + zfbLodId + ":故障帐号是:" + zfbNames, zfbNames, uid);
            return;
        } else {
            int qunAccount = SPUtils.getInt(context, "zfbQunAccount", 0);
            qunAccount++;
            SPUtils.putInt(context, "zfbQunAccount", qunAccount);
            //将获取到满足条件的群 的 帐号的总数记录下来，比支付宝帐号总数比较，小于支付宝帐号就说明有部分帐号，指定的群没找到，任务失败
        }

        w:
        while (true) {
            /*xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("群公告") && xmlData.contains("群聊设置"))) {
                return;
            }*/
            while (true) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    ShowToast.show("任务被中断", (Activity) context);
                    return;
                } else if (!(xmlData.contains("群公告") && xmlData.contains("群聊设置"))) {
                    wxUtils.adb("input keyevent 4");
                } else if (xmlData.contains("群公告") && xmlData.contains("群聊设置") && !xmlData.contains(flockName)) {
                    backHome();
                    transmitMessageFlock(flockName, messageNum, false);
                    return;
                } else {
                    wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
                    break;
                }
            }

            int count = 0;
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size(); a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                if ("com.alipay.mobile.chatapp:id/chat_msg_template_4_rl".equals(nodeBean.getResourceid())) {//更多
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
//                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    break;
                } else if ("com.alipay.mobile.chatapp:id/chat_msg_text".equals(nodeBean.getResourceid())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    wxUtils.adb("input swipe 200 607 200 315");//向下滑动
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("删除") && xmlData.contains("收藏") && xmlData.contains("更多")) {
                for (int a = nodeList.size(); a > 0; a--) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                    if ("com.alipay.mobile.antui:id/item_name".equals(nodeBean.getResourceid()) && "更多".equals(nodeBean.getText())) {//更多
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击更多
                        count++;
                        break;
                    }
                }
            } else {
                continue w;
            }
            xmlData = wxUtils.getXmlData();
            wh:
            while (count < messageNum) {//选择转发数据
                nodeList = wxUtils.getNodeList(xmlData);
                if (xmlData.contains("com.alipay.mobile.chatapp:id/bottom_chat_op_forward")) {//判断是否在转发页面
                    for (int a = nodeList.size(); a > 0; a--) {
                        if (count >= messageNum) {
                            break wh;
                        }
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                        if ("com.alipay.mobile.chatapp:id/chat_msg_checkbox".equals(nodeBean.getResourceid()) && !nodeBean.isChecked()) {//选中....
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击更多
                            count++;
                        }
                    }
                    String oldXmlData = xmlData;
                    wxUtils.adb("input swipe 200 200 200 630");//向下滑动
                    xmlData = wxUtils.getXmlData();
                    if (oldXmlData.equals(xmlData)) {
                        break w;
                    }

                } else {
                    continue w;
                }

                if (count != messageNum) {
                    LogUtils.d(count + "转发数=" + messageNum);
                }
            }


            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("com.alipay.mobile.chatapp:id/bottom_chat_op_forward")) {//转发id
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y374, R.dimen.x107, R.dimen.y391);//转发
            } else {
                LogUtils.d("不在转发页面");
                continue w;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("创建新聊天")) {
                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//多选
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("从通讯录选择")) {
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y123);//从通讯录选择
                } else {
                    continue w;
                }

            } else {
                LogUtils.d("不在创建新聊天页面");
                continue w;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("选择群聊")) {
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y123);//选择群聊
            } else {
                LogUtils.d("不在选择群聊页面");
                continue w;
            }
            xmlData = wxUtils.getXmlData();

            while (true) {//滑动到底部
                String oldXmlData = xmlData;
                wxUtils.adb("input swipe 200 700 200 200 40");//滑动到底部
                xmlData = wxUtils.getXmlData();
                if (oldXmlData.equals(xmlData))
                    break;
            }
            String countData = "";
            int countClick = 0;
            markFlockBeans.clear();
            nodeList = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("选择群聊") && xmlData.contains("确定")) {
                for (int a = nodeList.size(); a > 0; a--) {
                    if (countClick >= 9 || a <= 4) {
                        break;
                    }
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();//群名
                    NodeXmlBean.NodeBean nodeBean0 = wxUtils.getNodeXmlBean(nodeList.get(a - 2)).getNode();//群人数
                    NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(nodeList.get(a - 3)).getNode();//单选框
//                    LogUtils.e(nodeList.get(a - 1));
                    if ("com.alipay.mobile.socialshare:id/check_box".equals(nodeBean.getResourceid()) && !nodeBean.isChecked() && nodeBean1.getText() != null && !markData.contains(nodeBean1.getText())) {//更多
                        listXY = wxUtils.getXY(nodeBean.getBounds());
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击选择
                        ShowToast.show(nodeBean1.getText() + nodeBean0.getText(), (Activity) context);
                        markFlockBeans.add(new AlipayBillStatisticsBean.FlockBean(nodeBean1.getText(), nodeBean0.getText().replace("人)", "").replace("(", "")));
                        LogUtils.d(nodeBean1.getText() + "____" + nodeBean0.getText().replace("人)", "").replace("(", ""));
                        countData = countData + nodeBean1.getText() + ",";//记录发过的
                        LogUtils.d(countData);
                        countClick++;
                    }
                }
            } else {
                LogUtils.d("不在选择群界面");
                continue w;
            }
            if (countClick > 0) {
                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//确认
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("选择联系人")) {
                    continue w;
                }

                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//确认
                xmlData = wxUtils.getXmlData();
                if (!(xmlData.contains("选择") && xmlData.contains("发送"))) {
                    continue w;
                }
                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//发送

            } else if (countClick == 0) {
                LogUtils.e("发单任务完成");
                SPUtils.putString(context, "alipaySendMessageData", "");//保存数据
                break w;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("发送给") && xmlData.contains("com.alipay.mobile.socialshare:id/positive")) { //TODO
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = nodeList.size(); a > 0; a--) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                    if ("com.alipay.mobile.socialshare:id/positive".equals(nodeBean.getResourceid())) {//发送
                        listXY = wxUtils.getXY(nodeBean.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发送
                        flockBeanList.addAll(markFlockBeans);
                        markData = markData + countData + ",";//记录发过的
                        SPUtils.putString(context, "alipaySendMessageData", markData);//保存数据

                        while (true) {//判断是否在转发群页面
                            xmlData = wxUtils.getXmlData();
                            if (xmlData.contains("群公告") && xmlData.contains("群聊设置")) {
                                break;
                            }
                        }
                        break;
                    }
                }

            } else {
                LogUtils.d("不在发送给页面");
                continue w;
            }

        }
    }

    /**
     * 正则匹配转发的群
     *
     * @param str
     * @return
     */
    public static boolean isFlockMark(String str) {
        Pattern p = Pattern.compile("^[\u4e00-\u9fa5]+[a-zA-Z][0-9]{6}$");//中文开头 +  1个英文  +  6个数字结尾
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    boolean isExist = false;//判断有没a+uid群

    /**
     * 支付宝群里发消息(发单)
     */
    private void sendMessageFlockMark(String messageData, String crowd) {

        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        List<String> nodeList;

        WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);

        if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans != null && wxFlockMessageBeans.length > 0) {

        } else {
            ShowToast.show("数据有误", (Activity) context);
            isExist = false;
            return;
        }


        while (true) {
            xmlData = wxUtils.getXmlData();

            if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                ShowToast.show("任务被中断", (Activity) context);
                isExist = false;
                return;
            }

            if (!(xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {//判断是否在支付宝主界面
                backHome();
                continue;
            }
            wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
            wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮

            xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("通讯录") && xmlData.contains("新的朋友") && xmlData.contains("群聊"))) {
                backHome();
                continue;
            }
            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y88, R.dimen.x320, R.dimen.y125);//群聊
            if (!(xmlData.contains("搜索") && xmlData.contains("群聊"))) {
                backHome();
                continue;
            } else {
                break;
            }
        }


        //进入了群列表
        xmlData = wxUtils.getXmlData();
        if (!(xmlData.contains("搜索") && xmlData.contains("群聊"))) {
//            ShowToast.show("任务被中断...", (Activity) context);
            sendMessageFlockMark(messageData, crowd);
            return;
        }

        if (xmlData.contains("新群聊") && xmlData.contains("你可通过群聊中“保存到通讯录”选项，将其保存到这里")) {
            wxUtils.adb("input keyevent 4");
            LogUtils.e("没有群");
            isExist = false;
            return;
        }
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if ("com.alipay.mobile.antui:id/item_left_text".equals(nodeBean.getResourceid())) {

                LogUtils.d(crowd + "当前群=" + nodeBean.getText());
                if (!(crowd != null && crowd.equals(nodeBean.getText()))) {
                    continue;
                }

                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            ShowToast.show("没有设备群，任务中断", (Activity) context);
            return;
        }
        //_______________________________________________________________________________________________
        String qunName = "";
        //获取群人数，男女群信息
        String qunNameData = wxUtils.getXmlData();
        List<String> qunNameDataList = wxUtils.getNodeList(qunNameData);

        if (!(qunNameData.contains("群公告") && qunNameData.contains("群聊设置"))) {
//                    ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
            backHome();
            sendMessageFlockMark(messageData, crowd);
            return;
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
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("群公告") && xmlData.contains("群聊设置")) {
            //操作群
            LogUtils.e("发送消息");
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("文本")) {
                wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                wxUtils.adb("input keyevent 4");
                xmlData = wxUtils.getXmlData();
            }

            List<String> copyList = wxUtils.getNodeList(xmlData);
            for (int c = 0; c < copyList.size(); c++) {
                NodeXmlBean.NodeBean copyBean = wxUtils.getNodeXmlBean(copyList.get(c)).getNode();
                if ("com.alipay.mobile.chatapp:id/chat_msg_edit".equals(copyBean.getResourceid())) {
                    if (!StringUtils.isEmpty(copyBean.getText())) {
                        int x = context.getResources().getDimensionPixelSize(R.dimen.x296);
                        int y = context.getResources().getDimensionPixelSize(R.dimen.y343);//删除
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 7000);  //删除
                        wxUtils.adb("input keyevent 4");
                        break;
                    }
                }
            }

            LogUtils.d(wxFlockMessageBeans.length + "个信息");
            for (int b = 0; b < wxFlockMessageBeans.length; b++) {
//                        if()
                LogUtils.d(wxFlockMessageBeans[b].toString());
                switch (wxFlockMessageBeans[b].getType()) {
                    case "txt":


                        // 将文本内容放到系统剪贴板里。
                                /*cm.setText(wxFlockMessageBeans[b].getData());
                                cm.setText("淘口令:￥E8jW01PdrJP￥\n" +
                                        "精准计价，超长待机90天");*/
                        String[] cms = wxFlockMessageBeans[b].getData().split("__");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int c = 0; c < cms.length; c++) {
                            if (c != cms.length - 1) {
                                stringBuilder.append(cms[c] + "\n");
                            } else {
                                stringBuilder.append(cms[c]);
                            }
                        }
                        cm.setText(stringBuilder.toString());
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
                                listXY = wxUtils.getXY(nodeBean1.getBounds());//获取发送按钮的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发送
                                LogUtils.d(nodeBean1.getBounds() + "发送坐标");
                                break;
                            }
                        }

                        wxUtils.adb("input keyevent 4");

                        break;
                    case "img":
                        if (!downFlockImgAliPay(wxFlockMessageBeans[b].getData(), 1)) {
                            isExist = false;
                            return;
                        }

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册

                        int a = 0;
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
        }

        if (wxFlockMessageBeans[wxFlockMessageBeans.length - 1].getType().equals("txt")) {
            xmlData = wxUtils.getXmlData();
            if (wxFlockMessageBeans[wxFlockMessageBeans.length - 1].getData().length() >= 2) {
                LogUtils.d(wxFlockMessageBeans[wxFlockMessageBeans.length - 1].getData().substring(wxFlockMessageBeans[wxFlockMessageBeans.length - 1].getData().length() - 1));
                if (!xmlData.contains(wxFlockMessageBeans[wxFlockMessageBeans.length - 1].getData().substring(wxFlockMessageBeans[wxFlockMessageBeans.length - 1].getData().length() - 1))) {
                    isExist = false;
                    ShowToast.show("数据错误，任务中断了", (Activity) context);
                    return;
                }
            }
        }


        if (FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages")) {//删除aa开头文件文件
            String fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
            File folder = new File(fileUrl);
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.getName().startsWith("aa")) {
                    LogUtils.d("删除了:" + file.getName());
                    file.delete();
                }
            }
        }
    }


    /**
     * 支付宝群里发消息
     */
    private void sendMessageFlock(String messageData) {

        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        String qunClickMark = "";
        List<String> nodeList;

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


    /**
     * 支付宝群消息发图文  图片下载
     *
     * @param messageData
     * @return
     */
    private boolean downFlockImgAliPay(String messageData, int type) {
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
            fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
            LogUtils.e("aa文件写入:" + FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false));//改名把文件添加到第一个
            wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), context);
            return true;
        } else {
            LogUtils.d("不存在");
            return false;


           /* File f = null;
            try {
                f = wxUtils.getFileDown(path, fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            if (f == null) {
                return false;
            }*/
        }
//        fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
//        LogUtils.e("aa文件写入:" + FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false));//改名把文件添加到第一个
//        wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), context);
//        return true;
    }

    //----------------------------------拉群分男女开始---------------------------------------------------------------

    /**
     * 修改备注.
     */
    private void startAlterNameMark() {
        wxUtils.adbDimensClick(context, R.dimen.x187, R.dimen.y367, R.dimen.x213, R.dimen.y395);//点击底部朋友按钮
        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
        boolean bottom = false;//到了底部
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("0000");
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        String meName = "";

        xmlData = wxUtils.getXmlData();
        w:
        while (true) {
            if (!xmlData.contains("通讯录") && !xmlData.contains("搜索") && !xmlData.contains("添加朋友")) {
                ShowToast.show("任务被中断...", (Activity) context);
                break;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            boolean hideMark = false;//判断是否隐藏真实姓名
            a:
            for (int a = 0; a < nodeList.size(); a++) {
//                LogUtils.e(nodeList.get(a));
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && !nodeBean.getText().startsWith("ZZZ") && !meName.equals(nodeBean.getText())) {
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

                    if (xmlData.contains("通讯录") && xmlData.contains("搜索") && xmlData.contains("添加朋友")) {
                        continue;
                    }


                    if (xmlData.contains("该功能暂未对您开放")) {//支付宝被限制 TODO
                        wxUtils.adb("input keyevent 4");
                        LogUtils.e("支付宝帐号被限制");
                        astrict = false;
                        break w;
                    }

                    if (xmlData.contains("记录我的生活")) {
                        wxUtils.adb("input keyevent 4");
                        meName = nodeBean.getText();
                        continue;
                    }

                    if (xmlData.contains("备注她的信息")) {
                        sex = 0;
                    } else if (xmlData.contains("备注他的信息")) {
                        sex = 1;
                    } else {
                        sex = 2;
                    }
                    xmlData = wxUtils.getXmlData();
                    List<String> nodeListMarkInfo = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < nodeListMarkInfo.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeListMarkInfo.get(b)).getNode();
                        if ("com.alipay.android.phone.wallet.profileapp:id/tv_remark_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText().contains("备注")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                            break;
                        }
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y60, R.dimen.x320, R.dimen.y96);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x290, R.dimen.x110, R.dimen.x290, R.dimen.x110);//清空名字

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


                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                    while (xmlData.contains("备注他的信息") || xmlData.contains("备注她的信息")) {
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
                        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }

                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("通讯录") && !xmlData.contains("搜索") && !xmlData.contains("添加朋友")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                break w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && !nodeBean.getText().startsWith("ZZZ") && !meName.equals(nodeBean.getText())) {
                    continue w;
                } else if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText() != "" && nodeBean.getText().startsWith("ZZZ")) {
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
                wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮

                ShowToast.show("修改备注完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("个朋友")) {//判断是否到达底部
                bottom = true;
            }
        }

    }


    /**
     * 拉群
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

        String qunClickMark = "";
        List<String> nodeList;

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
                status = 0;
                wxUtils.adb("input keyevent 4");
//                ShowToast.show("没有群...", (Activity) context);
                break;
            }
            List<String> nodeListA = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeListA.size(); a++) {

                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeListA.get(a)).getNode();
                if ("com.alipay.mobile.antui:id/item_left_text".equals(nodeBean.getResourceid())) {//"A000101"

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

        LogUtils.d(bogCount + "___girl__" + girlCount);
        LogUtils.d("拒绝拉群的:" + refuseAdd);
        //拉完群改名
        ShowToast.show("拉群完，修改备注开始", (Activity) context);
        qunEndAlterNameRefuseAddMark();//修改拒绝的人
        wxUtils.adb("input keyevent 4");
        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
        qunEndAlterNameMark();
    }

    /**
     * 拉完群改名修改备注.
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

            boolean hideMark = false;
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialcontactsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && countStr.contains(nodeBean.getText())) {
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

                    String hideStr = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode().getText();
                    if ("对方已隐藏真实姓名 ".contains(hideStr)) {
                        hideMark = true;
                    }

                    xmlData = wxUtils.getXmlData();
                    List<String> nodeListMarkInfo = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < nodeListMarkInfo.size(); b++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeListMarkInfo.get(b)).getNode();
                        if ("com.alipay.android.phone.wallet.profileapp:id/tv_remark_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText().contains("备注")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                            break;
                        }
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y60, R.dimen.x320, R.dimen.y96);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x290, R.dimen.x110, R.dimen.x290, R.dimen.x110);//清空名字

                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '1';
                        sexStr = new String(ch);
                    }
                    LogUtils.e("input text \"" + sexStr + "\"");
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    LogUtils.d("修改后删除数据库" + DataSupport.deleteAll(AlipayAlterNameSqliteBean.class, "alipayname = ? and newName = ?", zfbNames, name));

                    wxUtils.adb("input keyevent 4");

                    xmlData = wxUtils.getXmlData();
                    while (xmlData.contains("备注他的信息") || xmlData.contains("备注她的信息")) {
                        wxUtils.adb("input keyevent 4");
                        xmlData = wxUtils.getXmlData();
                    }

                   /* if (hideMark) {
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }*/
                    if (xmlData.contains(name)) {//判断修改后是否名字没有改变
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
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
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                break w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialcontactsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && countStr.contains(nodeBean.getText())) {
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

        xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
        w:
        while (true) {
            if (refuseAddCount >= refuseAdds.length) {
//                ShowToast.show("拒绝加群的改名完成", (Activity) context);
                break w;
            }

            boolean hideMark = false;
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialcontactsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && refuseAdd.contains(nodeBean.getText())) {
                    String name = nodeBean.getText();//好友名字
                    LogUtils.e("支付宝名:" + name);
                    if (refuseAddCount >= refuseAdds.length) {
                        break w;
                    }
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注

                    String hideStr = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode().getText();
                    if ("对方已隐藏真实姓名 ".contains(hideStr)) {
                        hideMark = true;
                    }

                    xmlData = wxUtils.getXmlData();
                    List<String> nodeListMarkInfo = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < nodeListMarkInfo.size(); b++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeListMarkInfo.get(b)).getNode();
                        if ("com.alipay.android.phone.wallet.profileapp:id/tv_remark_title".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText().contains("备注")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击备注他的信息
                            break;
                        }
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y60, R.dimen.x320, R.dimen.y96);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x290, R.dimen.x110, R.dimen.x290, R.dimen.x110);//清空名字

                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '9';
                        sexStr = new String(ch);
                    }
                    LogUtils.e("input text \"" + sexStr + "\"");
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    LogUtils.d("修改后删除数据库" + DataSupport.deleteAll(AlipayAlterNameSqliteBean.class, "alipayname = ? and newName = ?", zfbNames, name));

                    refuseAddCount++;
                    wxUtils.adb("input keyevent 4");

                    xmlData = wxUtils.getXmlData();
                    while (xmlData.contains("备注他的信息") || xmlData.contains("备注她的信息")) {
                        wxUtils.adb("input keyevent 4");
                        xmlData = wxUtils.getXmlData();
                    }

                    /*if (hideMark) {
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.y167, R.dimen.y17, R.dimen.x285, R.dimen.y51);//点击通讯录按钮
                        xmlData = wxUtils.getXmlData();
                        continue w;
                    }*/

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
            if (!xmlData.contains("通讯录")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                break w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.socialcontactsdk:id/list_item_title") && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ0") && refuseAdd.contains(nodeBean.getText())) {
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


        w:
        while (true) {//获取添加按钮并点击
            String qunNameData = wxUtils.getXmlData();

            if (!qunNameData.contains("聊天信息")) {
                break;
            }

            if (qunNameData.contains("添加成员")) {

                Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
                Matcher matcher = pattern.matcher(qunNameData);
                while (matcher.find()) {
                    qunNameDataList.add(matcher.group() + "/>");
                }

                for (int a = 0; a < qunNameDataList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(qunNameDataList.get(a)).getNode();
                    if (nodeBean != null && nodeBean.getContentdesc() != null && "添加成员".equals(nodeBean.getContentdesc())) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//添加
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加
                        break w;
                    }
                }

            }
            wxUtils.adbUpSlide(context);//向上滑动
        }
        String addNameData = wxUtils.getXmlData();
        List<String> addNameList = new ArrayList<>();
        Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
        Matcher matcher = pattern.matcher(addNameData);
        while (matcher.find()) {
            addNameList.add(matcher.group() + "/>");
        }

        int flagGirlCount = 0;
        int flagBoyCount = 0;
        String flagCountStr = "";
        int flagClickCount = 0;

        String flagSendCount = "";
        w:
        while (true) {

            for (int a = 0; a < addNameList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(addNameList.get(a)).getNode();

                if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ0") && !refuseAdd.contains(nodeBean.getText())) {
                    NodeXmlBean.NodeBean checkBox = wxUtils.getNodeXmlBean(addNameList.get(a + 2)).getNode();

                    if ("com.alipay.mobile.socialcontactsdk:id/selected_check_box".equals(checkBox.getResourceid()) && checkBox.isChecked()) {//isChecked true代表选中

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
                        if ("com.alipay.mobile.socialcontactsdk:id/selected_check_box".equals(checkBox.getResourceid()) && !checkBox.isChecked()) {
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

                                    if (bogCount + girlCount >= pullMax) {
                                        break w;
                                    }
                                } else {
                                    break w;
                                }
                            }
                        }
                    } else if (sex == 1 && nodeBean.getText().contains("A")) {//男
                        if ("com.alipay.mobile.socialcontactsdk:id/selected_check_box".equals(checkBox.getResourceid()) && !checkBox.isChecked()) {
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

                                    if (bogCount + girlCount >= pullMax) {
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
            Matcher matcherAdd = pattern.matcher(addNameData);
            while (matcher.find()) {
                addNameList.add(matcherAdd.group() + "/>");
            }

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
                    if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ1")) {
                        judgeGirl++;
                    }
                } else {//男
                    if ("com.alipay.mobile.socialcontactsdk:id/list_item_title".equals(nodeBean.getResourceid()) && (nodeBean.getText().startsWith("ZZZ0B") || nodeBean.getText().startsWith("ZZZ1"))) {
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
            wh:
            while (true) {
                String qunLiaoBack = wxUtils.getXmlData();//修改完一个重新获取页面数据

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
                            break wh;
                        }
                    }
                } else {
                    countStr = countStr + flagCountStr + ",";

                    if (bogCount + girlCount >= pullMax) {
                        boyEnd = false;
                        girlEnd = false;
                    }
                }

                if (qunLiaoBack.contains("群公告") && qunLiaoBack.contains("群聊设置")) {

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
                    wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1500);  //长按输入框
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x62, R.dimen.y180, R.dimen.x62, R.dimen.y180);//粘贴
                    wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y176, R.dimen.x314, R.dimen.y198);//发送

                    wxUtils.adb("input keyevent 4");
                    wxUtils.adb("input keyevent 4");
                    break;
                } else {
                    wxUtils.adb("input keyevent 4");
                }
            }

        } else {//没有选中.返回
            come_num = qb;
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
        }
    }

    public void sendAliCount(final int num) {
        final String uid = SPUtils.getString(context, "uid", "0000");
        xmlData =wxUtils.getXmlData();
        if (xmlData.contains("任务正在执行中")){
            wxUtils.openAliPay();
        }
        wxUtils.openAliPay();
        getName_zfb();
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        //支付宝跑名字  获取地址
        String url = "http://103.94.20.101:8087/52xm/get_more.php?database=53_sj&batch=" + "u" + uid + "_" + zfbNames + "&table=" + zfbNamesTable + "&num=" + num;
        boolean netFlag = true;
        int number = 0;
        while (netFlag){
            try {
                number++;
                Thread.sleep(5000);
                if (number > 3) return;
                Response execute = OkHttpUtils.get().url(url).build().execute();
                if (execute.code() == 200) {
                    String string = execute.body().string();
                    ZfbPhoneNumberBean zfbPhoneNumberBean = GsonUtil.parseJsonWithGson(string, ZfbPhoneNumberBean.class);
                    if (zfbPhoneNumberBean != null) {
                        for (int i = 0; i < zfbPhoneNumberBean.getData().size(); i++) {
                            Log.d("bean:", "" + zfbPhoneNumberBean.getData().toString());
                            String phone_num = zfbPhoneNumberBean.getData().get(i);
                            Log.d("phone_num:", "" + phone_num);
                            phoneNumList.add(phone_num);
                            Log.d("phoneNumList:", "" + phoneNumList);
                        }
                        netFlag = false;
                    }
                } else {
                    ShowToast.show("网络异常未能获取到电话号码！", (Activity) context);
                }
            } catch (Exception e) {
                ShowToast.show("网络异常未能获取到电话号码！", (Activity) context);
                e.printStackTrace();
            }
        }
        wxUtils.openAliPay();
        backHome_tow_zfb();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData =wxUtils.getXmlData();
        if (xmlData.contains("账户不存在")||xmlData.contains("帐号不存在")||xmlData.contains("操作太频繁了")) {
            NodeUtils.clickNode("确定","com.alipay.mobile.antui:id/ensure"); //点击确定
            NodeUtils.clickIndex("com.alipay.mobile.ui:id/clearButton",2); //点击清空号码
//            wxUtils.adbClick(426, 126, 456, 176);//点击清空号码
            backHome_tow_zfb();
        }
        NodeUtils.clickNode("首页","com.alipay.android.phone.openplatform:id/tab_description"); // 点击首页
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
//        wxUtils.adbClick(42, 280, 78, 280);//点击转账
        NodeUtils.clickNode("转账","com.alipay.android.phone.openplatform:id/app_text");
        ShowToast.show("请等待5秒", (Activity) context);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
           NodeUtils.clickText("转到支付宝账户",1);

        if (phoneNumList!=null && phoneNumList.size()!=0){
            for (int i = 0; i < phoneNumList.size(); i++) {
                String phone_num = phoneNumList.get(i);
                if (SPUtils.getInt(context, "AliCountIsOver", 0) == 0) {
                    wxUtils.openAliPay();
                    zfbTrtransfer(phone_num, zfbNames, uid, cm);
                }
            }
            if (num == zfbNamesCountCommit &&zfb_code != "" ) {
                List<ZfbNameCodeCommitBean> zfbNameCodeCommitBeanList = new ArrayList<>();
                ZfbNameCodeCommitBean zfbNameCodeCommitBean = new ZfbNameCodeCommitBean(zfbNamesTable, zfb_code);
                zfbNameCodeCommitBeanList.add(zfbNameCodeCommitBean);
                String str1 = new Gson().toJson(zfbNameCodeCommitBeanList);
//                str1="{\"res\":"+str1+"}";
                LogUtils.d("JSON" + str1.toString());
                zfbNamesTableCommit(str1, zfb_code);
            }
            phoneNumList.clear();
        }

        return;
    }


    private void zfbNamesTableCommit(String countStr, String zfb_code) {
        String url = "http://103.94.20.101:8087/52xm/confirm.php?database=53_sj&table=" + zfbNamesTable;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("code", zfb_code);
        HttpManager2.getInstance().sendPostRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                LogUtils.d("好友个人信息上传成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("好友数量上传失败");
            }
        });

    }

    /**
     * 支付宝转账获取名字
     */
    private void zfbTrtransfer(String phone_num, String zfbAcount, String uid, ClipboardManager cm) {
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        String zfbPhoneName = "";
        int isRealName = 3;
        String zfbRealname = "";
        String table = zfbNamesTable + "_u" + uid;
        wxUtils.openAliPay();
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("任务正在执行中")){
            wxUtils.openAliPay();
        }
        if (xmlData.contains("账户不存在")||xmlData.contains("帐号不存在")||xmlData.contains("操作太频繁了")) {
            NodeUtils.clickNode("确定","com.alipay.mobile.antui:id/ensure"); //点击确定
            NodeUtils.clickIndex("com.alipay.mobile.ui:id/clearButton",0); //点击清空号码
//            wxUtils.adb("input swipe " + 150 + " " + 150 + " " + 150 + " " + 150 + " " + 1000);  //长按EdiText
            NodeUtils.clickNode("支付宝账户/手机号码","com.alipay.mobile.ui:id/content"); //点击EditText
        }
        if (xmlData.contains("对方账户") && xmlData.contains("钱将实时转")) {
            NodeUtils.clickNode("支付宝账户/手机号码","com.alipay.mobile.ui:id/content"); //点击EditText
        }else {
            wxUtils.openAliPay();
            backHome_tow_zfb();
            NodeUtils.clickNode("首页","com.alipay.android.phone.openplatform:id/tab_description"); // 点击首页
            wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
//            wxUtils.adbClick(42, 332, 78, 353);//点击转账
            NodeUtils.clickNode("转账","com.alipay.android.phone.openplatform:id/app_text");
//            NodeUtils.clickText("转到支付宝账户",1);
            return;
        }
        wxUtils.adb("input text " + phone_num);
        NodeUtils.clickNode("下一步","com.alipay.mobile.transferapp:id/tf_toAccountNextBtn");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("操作太频繁了") || xmlData.contains("人气太旺啦")) {
            wxUtils.adbClick(350, 500, 350, 500);
            SPUtils.putInt(context, "AliCountIsOver", 1);
            try {
                Response data = OkHttpUtils.get().url("http://103.94.20.101:8087/api/update_error_account.php").addParams("account", zfbAcount).addParams("uid",uid).build().execute();
                if (data.code() == 200){
                    ShowToast.show("频繁帐号上传成功...", (Activity) context);
                }else {
                    ShowToast.show("频繁帐号第一次上传失败，等待再次上传...", (Activity) context);
                     data = OkHttpUtils.get().url(URLS.wxAccountApply()).addParams("acount", zfbAcount).addParams("uid",uid).build().execute();
                     if ( data.code() ==  200 ){
                         ShowToast.show("频繁帐号上传成功...", (Activity) context);
                     }else {
                         ShowToast.show("频繁帐号第二次上传失败，等待再次上传..", (Activity) context);
                         data = OkHttpUtils.get().url(URLS.wxAccountApply()).addParams("acount", zfbAcount).addParams("uid",uid).build().execute();
                         if ( data.code() ==  200 ){
                             ShowToast.show("频繁帐号上传成功...", (Activity) context);
                         }else {
                             ShowToast.show("频繁帐号第三次上传失败，结束上传", (Activity) context);
                         }
                     }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (xmlData.contains("帐号不存在")||xmlData.contains("账户不存在")  || xmlData.contains("对方尚未通过认证")) {
            List<AlipayTransferMessageBean.ResBean> resBeanList = new ArrayList<>();
            AlipayTransferMessageBean.ResBean resBean = new AlipayTransferMessageBean.ResBean(phone_num.trim(), "", 3, "", uid, zfbAcount, table);
            resBeanList.add(resBean);
            String str1 = new Gson().toJson(resBeanList);
            str1 = "{\"res\":" + str1 + "}";
            LogUtils.d("JSON" + str1.toString());
            sendAlipayTransferMessage(str1);
            zfbNamesCountCommit++;
            NodeUtils.clickNode("确定","com.alipay.mobile.antui:id/ensure"); //点击确定
            NodeUtils.clickIndex("com.alipay.mobile.ui:id/clearButton",0); //点击清空号码
            return;
        }
        if (xmlData.contains("该手机号对应多个支付宝账户")) {
            NodeUtils.clickIndex("com.alipay.mobile.socialcontactsdk:id/user_account",0);
//            wxUtils.adbClick(112, 445, 280, 474);
        }
        wxUtils.adb("input keyevent 4");//返回
        xmlData = wxUtils.getXmlData();
        List<String> zfbMessageList = wxUtils.getNodeList(xmlData);
        if (zfbMessageList.size() > 15) {
            for (int i = 15; i < zfbMessageList.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(zfbMessageList.get(i)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.alipay.mobile.transferapp:id/tf_receiveNameTextView") &&  nodeBean.getText()!=null ) {
                    zfbPhoneName = nodeBean.getText();
                    if (!zfbPhoneName.contains("（") && !zfbPhoneName.contains("）")) {
                        //说明是未实名的
                        isRealName = 2;
                        List<AlipayTransferMessageBean.ResBean> resBeanList = new ArrayList<>();
                        AlipayTransferMessageBean.ResBean resBean = new AlipayTransferMessageBean.ResBean(phone_num.trim(), zfbPhoneName, isRealName, "", uid, zfbAcount, table);
                        resBeanList.add(resBean);
                        String str1 = new Gson().toJson(resBeanList);
                        str1 = "{\"res\":" + str1 + "}";
                        LogUtils.d("JSON" + str1.toString());
                        sendAlipayTransferMessage(str1);
                        zfbNamesCountCommit++;
                    } else {
                        isRealName = 1;
                        int a = zfbPhoneName.indexOf("（");
                        int b = zfbPhoneName.indexOf("）");
                        zfbRealname = zfbPhoneName.substring(a + 1, b);
                        zfbRealname = zfbRealname.substring(1);
                        zfbPhoneName = zfbPhoneName.substring(0, a);
                        List<AlipayTransferMessageBean.ResBean> resBeanList = new ArrayList<>();
                        AlipayTransferMessageBean.ResBean resBean = new AlipayTransferMessageBean.ResBean(phone_num.trim(), zfbPhoneName, isRealName, zfbRealname, uid, zfbAcount, table);
                        resBeanList.add(resBean);
                        String str2 = new Gson().toJson(resBeanList);
                        str2 = "{\"res\":" + str2 + "}";
                        LogUtils.d("JSON" + str2.toString());
                        sendAlipayTransferMessage(str2);
                        zfbNamesCountCommit++;
                    }
                    break;
                }
            }
        }
        if (xmlData.contains("账户不存在") || xmlData.contains("对方尚未通过认证")) {
            NodeUtils.clickNode("确定","com.alipay.mobile.antui:id/ensure"); //点击确定

        }
        if (!xmlData.contains("下一步")) {
            wxUtils.adb("input keyevent 4");//返回
        }
        NodeUtils.clickIndex("com.alipay.mobile.ui:id/clearButton",0); //点击清空号码
        return;
    }

    private void sendAlipayTransferMessage(String str) {
        String url = "http://103.94.20.101:8087/52xm/upload.php?database=53_xm&table=52_xm";
        try {
            Response data = OkHttpUtils.post().url(url).addParams("json", str).build().execute();
            if (data.code() == 200) {
                String string = data.body().string();
                Log.d("zs1", string);
            } else {
                 data = OkHttpUtils.post().url(url).addParams("json", str).build().execute();
                if (data.code() == 200) {
                    String string = data.body().string();
                    Log.d("zs2", string);
                } else {
                     data = OkHttpUtils.post().url(url).addParams("json", str).build().execute();
                    if (data.code() == 200) {
                        String string = data.body().string();
                        Log.d("zs3", string);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------拉群分男女结束---------------------------------------------------------------
}
