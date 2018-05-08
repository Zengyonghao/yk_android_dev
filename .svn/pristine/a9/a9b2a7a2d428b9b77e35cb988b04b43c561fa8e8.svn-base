package com.zplh.zplh_android_yk.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;

import com.easy.wtool.sdk.WToolSDK;
import com.google.gson.Gson;
import com.wanj.x007_common.CommunicationDefine;
import com.wanj.x007_common.pb.Common;
import com.wanj.x007_common.util.ShellUtils;
import com.yk2_0.utils.AdbUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseApplication;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.bean.AddFriendsMobileBean;
import com.zplh.zplh_android_yk.bean.CheckImei;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.bean.PublicMarkBean;
import com.zplh.zplh_android_yk.bean.QunMessageBean;
import com.zplh.zplh_android_yk.bean.WxChatBean;
import com.zplh.zplh_android_yk.bean.WxChatroomsBean;
import com.zplh.zplh_android_yk.bean.WxFlockMessageBean;
import com.zplh.zplh_android_yk.bean.WxFriendsBean;
import com.zplh.zplh_android_yk.bean.WxFriendsMessageBean;
import com.zplh.zplh_android_yk.bean.WxFriendsMessageCultivate;
import com.zplh.zplh_android_yk.bean.WxNewFriendsToQunBean;
import com.zplh.zplh_android_yk.bean.WxNumBean;
import com.zplh.zplh_android_yk.bean.WxPhone;
import com.zplh.zplh_android_yk.bean.WxPhoneNumeAskBean;
import com.zplh.zplh_android_yk.bean.wxLockBean;
import com.zplh.zplh_android_yk.bean.wxReplyMessageBean;
import com.zplh.zplh_android_yk.callback.PullNewFriendCallback;
import com.zplh.zplh_android_yk.conf.Constants;
import com.zplh.zplh_android_yk.conf.ModelConstans;
import com.zplh.zplh_android_yk.conf.MyConstains;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.httpcallback.GsonUtil;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.ui.view.OperationView;
import com.zplh.zplh_android_yk.utils.AdbBoundsUtils;
import com.zplh.zplh_android_yk.utils.FileUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.NetUtils;
import com.zplh.zplh_android_yk.utils.NodeUtils;
import com.zplh.zplh_android_yk.utils.PhoneUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;
import com.zplh.zplh_android_yk.utils.StringUtils;
import com.zplh.zplh_android_yk.utils.TimeUtil;
import com.zplh.zplh_android_yk.utils.XutilDown;

import org.json.JSONException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by lichun on 2017/6/14.
 * Description:操作
 */

public class OperationPresenter extends BasePresenter<OperationView> {

    //private int standingTime; //进入微信游戏，京东购物界面，停留的时间

    private String reply_msg;//微信通讯录加好友成功之后，发送的一条消息
    public String[] materia_ssList5 = null;
    private int standingTimeShop;//进入京东购物界面，停留的时间
    private int standingTimeGame;//进入微信游戏,停留的时间
    private OperationView operationView;
    private Context context;
    WxUtils wxUtils = new WxUtils();
    private String xmlData;
    private NodeXmlBean.NodeBean nodeBean;
    private List<Integer> listXY;
    private List<String> nodeList;
    private int status = 5;//0没群 1男群满  2女群满  3男女都满  4失败 5正常
    private int groupPeopleNumber = 0;
    private XutilDown xutilDown;
    private PhoneUtils phoneUtils;

    /**
     * 以下是接受广播传递过来的值，并且进行相关的处理
     */
    private String open_s_game = null;
    private String open_e_game = null;
    private String open_s_shop = null;
    private String open_e_shop = null;
    private String dz_sum = "";//朋友圈点赞次数

    private int phone_add_num = 0;//一次任务每部手机最多请求加好友次数

    private String wx_add_num = "";//一次任务每个微信最多请求加好友次数

    private String materia_pic = "";//图片素材ID//也可以成为视频素材ID

    private String is_remind = "";    // 设置朋友圈发布查看权限


    private String one_add_num = "";	/*13*///一个微信号每次任务最多请求加好友次数(通讯录加好友)

    private String day_add_num = "";	/*10*///一个微信号每天最多请求加好友次数:(通讯录加好友)

    private String is_verify = "";	/*1*///添加认证信息(通讯录加好友)

    private String sniffing_type = "";	/*1*///嗅探加好友 好友来源方式

    private String materia_vedio = "";//微信群发视频地址

    private String is_mass = "";//好友发消息
    private String sdPath = "/sdcard/xUtils/" + System.currentTimeMillis() + "pengyou.mp4";

    private final OperationPresenterBrocaset m_client1;

    private List<String> materia_phone;


    private String add_interval_time_s;//单词加好友间隔时间 开始

    private String add_interval_time_e;//单次加好友结束间隔时间


    private String agree_interval_time_s = "";//自动通过好友开始时间

    private String agree_interval_time_e = "";//自动通过好友结束时间

    private String wx_add_num_e = "";//微信搜索加好友 结束

    private String wx_add_num_s = "";//微信搜索加好友 开始

    private String contact_verify_msg = "";//申请添加好友的发送内容

    private String one_add_num_s = "";//通讯录加好友 开始

    private String one_add_num_e = "";//通讯录加好友 结束
    private BaseApplication app;
    private String phone_name = "";
    private Map<String, String> map_sousuo = new HashMap<String, String>();//搜索添加好友键值对集合
    //private Map<String, String> map_tongxunlu = new HashMap<String, String>();//通讯录添加好友键值对集合
    private String phoneRadio;//手机设置1是手机重启 2是手机关机 3是清理手机内存 4手机微信手机 5是结束当前任务 6是结束全部任务
    private List<String> myCheck_massage = new ArrayList<String>();
    private String args2 = "";
    private String[] sex_key;
    private String wx_Sex = "3";
    private boolean QunIsFull = true;
    private int pic_id = 0;
    private int sendAccountType = 0;
    private String newQunName = "";

    public void createSDCardDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath() + "/pengyouImages/";
            File path1 = new File(path);
            if (!path1.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                path1.mkdirs();
                //  ShowToast.show("没有指定文件夹，直接创建", (Activity) context);
            }
        } else {
            //  ShowToast.show("有文件夹，可直接保存", (Activity) context);
            return;

        }
    }

    public OperationPresenter(Context context, OperationView operationView) {
        this.operationView = operationView;
        this.context = context;
        xutilDown = new XutilDown(context);
        m_client1 = new OperationPresenterBrocaset();
        phoneUtils = new PhoneUtils(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyConstains.Broadcast_Task);
        context.registerReceiver(m_client1, filter);

        m_client = new ClientBroadcastRecv();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(CommunicationDefine.Broadcast_WeichatToApp);
        context.registerReceiver(m_client, filter1);
        Activity activity = (Activity) context;
        app = (BaseApplication) activity.getApplication();
    }

    private int time = 0;
    Random random = new Random();// 定义随机类

    /**
     * 执行任务.先判断
     *
     * @param task
     */
    public void task(int task) {
        backNum = 0;
        if (wxUtils.isInstallApp(context, "com.tencent.mm")) {//判断支付宝是否安装
            if (time == 0) {
                wxUtils.openWx((Activity) context);//打开微信
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("安全警告") && xmlData.contains("wx助手 正在尝试") && xmlData.contains("记住我的选择。")) {
               /* wxUtils.adbClick(61, 472, 61, 520); //记住选择
                wxUtils.adbClick(342, 544, 438, 616);//确定*/

                wxUtils.adbDimensClick(context, R.dimen.x41, R.dimen.y232, R.dimen.x41, R.dimen.y232);//记住选择
                wxUtils.adbDimensClick(context, R.dimen.x260, R.dimen.y272, R.dimen.x260, R.dimen.y272);//确定
                xmlData = wxUtils.getXmlData();
            }

            if (xmlData.contains("更新") && xmlData.contains("取消") && xmlData.contains("立刻安装")) {
                /*wxUtils.adbClick(156, 658, 276, 703); //取消安装
                wxUtils.adbClick(300, 490, 396, 535);//确定*/

                //                wxUtils.adbDimensClick(context, R.dimen.x144, R.dimen.y308, R.dimen.x144, R.dimen.y329);//取消安装
                //                wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y230, R.dimen.x264, R.dimen.y251);//确定

                List<String> ud = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < ud.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ud.get(a)).getNode();
                    if (nodeBean.getText() != null && nodeBean.getText().contains("取消")) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//取消
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//取消
                        wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y230, R.dimen.x264, R.dimen.y251);//确定
                        break;
                    }
                }

                xmlData = wxUtils.getXmlData();
            }

            if (xmlData.contains("忘记密码") || (xmlData.contains("登录") && xmlData.contains("注册") && xmlData.contains("语言")) || (xmlData.contains("你的手机号码") && xmlData.contains("密码"))) {//判断是否登录
                ShowToast.show("请先登录微信", (Activity) context);
                status = 4;
                operationView.isLoginWx(false);
                return;
            } else {
                if (xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我") && !(xmlData.contains("聊天信息"))) {//判断是否在微信主界面
                    time = 0;
                    if (task == 0 || task == 1) {
                        wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y367, R.dimen.x160, R.dimen.y400);//点击通讯录
                        xmlData = wxUtils.getXmlData();
                        if (!xmlData.contains("新的朋友")) {//在通讯录界面，但是需要滑动到最顶端
                            //                            wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                            wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y367, R.dimen.x160, R.dimen.y400);//点击通讯录
                            xmlData = wxUtils.getXmlData();
                        }
                    }
                    if (task == 8 || task == 9) {
                        wxUtils.adbDimensClick(context, R.dimen.x160, R.dimen.y368, R.dimen.x240, R.dimen.y400);//发现
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y65, R.dimen.x320, R.dimen.y98);//朋友圈
                        //                        wxUtils.adbClick(0, 138, 5, 210);//朋友圈

                        xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("朋友圈") && xmlData.contains("扫一扫") && xmlData.contains("游戏")) {
                            //                            wxUtils.adbClick(0, 138, 5, 210);//朋友圈
                            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y65, R.dimen.x320, R.dimen.y98);//朋友圈
                        }
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (task == 10 || task == 11) {
                        wxUtils.adbDimensClick(context, R.dimen.x160, R.dimen.y368, R.dimen.x240, R.dimen.y400);//发现
                    }

                    switch (task) {
                        case 0://修改备注
                            ShowToast.show("修改备注开始", (Activity) context);
                            startAlterName("");
                            break;
                        case 1://拉群
                            ShowToast.show("开始拉群开始", (Activity) context);
                            addCrowd();
                            upddateGroup();//拉完群反馈信息给服务器
                            break;
                        case 2://打开附近人
                            OpendFujin();
                            break;
                        case 3://自动通过好友申请
                            AddFriend();
                            break;
                        case 4://添加通讯录为好友
                            //wxUtils.DeletPhone(context);
                            while (true) {
                                ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                wxUtils.DeletPhone(context);
                                if (wxUtils.getContactCount(context) < 1) {
                                    break;
                                } else {
                                    LogUtils.d("通讯录的好友数量是" + wxUtils.getContactCount(context));
                                    wxUtils.DeletPhone(context);
                                }
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            getPhoneAdd();
                            addFriendsReturn();
                            break;
                        case 5://搜索添加好友
                            StatuRequest();
                            break;
                        case 6://微信群保存到通讯录
                            SaveFlockToBook();
                            break;
                        case 7://自助加好友
                            //3:来自微信号搜索 6:通过好友同意  13:来自手机通讯录 14:群聊 15:来自手机号搜索 17:通过名片分享添加  18:来自附近人 30:通过扫一扫添加 39:搜索公众号来源
                            String[] sniffing = new String[]{"3", "6", "13", "14", "15", "17", "18", "30", "39"};
                            sniffing_type = sniffing[random.nextInt(8)];//0-8的随机数

                            contact_verify_msg = "您好,《name》，很高兴认识你。";
                            sniffingAddFriendsDatas(5);
                            break;
                        case 8://朋友圈发布 type 0文字    1图文   2视频
                            FriendsRing(0, "原来是你呀，好久都没联系了!");

                            break;
                        case 9://朋友圈点赞
                            if (StringUtils.isEmpty(dz_sum)) {
                                clickLike(5);
                            } else {
                                clickLike(Integer.parseInt(dz_sum));
                            }
                            break;
                        case 10://朋友圈购物
                            boolean isTrueStandingTimeS = checkStandingTime(open_s_shop, open_e_shop, 2);
                            if (!isTrueStandingTimeS) {//时间格式错误
                                break;
                            }
                            xmlData = wxUtils.getXmlData();
                            if (!xmlData.contains("购物")) {
                                ShowToast.show("请先添加购物的功能", (Activity) context);
                                break;
                            }

                            try {
                                Thread.sleep(1500);
                                backHome();
                                goToShopping();
                                LogUtils.d("停留时间" + standingTimeShop + "秒");
                                Thread.sleep(standingTimeShop * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            wxUtils.adb("input keyevent 4");
                            xmlData = wxUtils.getXmlData();
                            break;
                        case 11://朋友圈游戏
                            boolean isTrueStandingTimeG = checkStandingTime(open_s_game, open_e_game, 1);
                            if (!isTrueStandingTimeG) {//时间格式错误
                                break;
                            }
                            xmlData = wxUtils.getXmlData();
                            if (!xmlData.contains("游戏")) {
                                ShowToast.show("请先添加游戏的功能", (Activity) context);
                                break;
                            }
                            try {
                                Thread.sleep(1500);
                                backHome();
                                goToPlayGame();
                                LogUtils.d("停留时间" + standingTimeGame + "秒");
                                Thread.sleep(standingTimeGame * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            wxUtils.adb("input keyevent 4");

                            break;
                        case 12://微信好友发视频
                            WSendVido();
                            break;
                        case 13://微信好友发图片素材
                            WSendPic();
                        case 14://后台传递的手机号码
                            //wxUtils.DeletPhone(context);
                            while (true) {
                                //                                wxUtils.adb("am force-stop " + "com.tencent.mm");//关闭微信

                                ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                //wxUtils.delAllContacts(context.getContentResolver());
                                wxUtils.DeletPhone(context);
                                if (wxUtils.getContactCount(context) < 1) {
                                    break;
                                } else {
                                    LogUtils.d("通讯录的好友数量是" + wxUtils.getContactCount(context));
                                    // wxUtils.delAllContacts(context.getContentResolver());
                                    wxUtils.DeletPhone(context);
                                }
                            }
                            ShowToast.show("联系人清理完成", (Activity) context);
                            for (int i = 0; i < materia_phone.size(); i++) {
                                LogUtils.d("添加联系人" + materia_phone.get(i));
                                wxUtils.addContact(materia_phone.get(i), materia_phone.get(i), context);
                            }
                            ShowToast.show("正在添加电话号码到联系人，请等待...", (Activity) context);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ShowToast.show("联系人添加完成，开始执行添加好友任务...", (Activity) context);
                            AddCommunication(wx_Sex);
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


        } else {
            LogUtils.d("微信未安装");
        }

    }


    /**
     * @param isSend true发送方 false接受方
     */
    public void goChat(boolean isSend) {//语音互聊
        //先睡一下 保证界面进入微信了
        backHome();
        wxUtils.adbClick(36, 780, 84, 822);//点击微信
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //先获得第一个语音通话的item
        String xmlData = wxUtils.getXmlData();
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        for (String s : nodeList) {
            if (s.contains("语音通话") || s.contains("语音聊天")) {
                NodeXmlBean nodeXmlBean = wxUtils.getNodeXmlBean(s);
                String bounds = nodeXmlBean.getNode().getBounds();
                List<Integer> xy = wxUtils.getXY(bounds);
                wxUtils.adbClick(xy.get(0), xy.get(1), xy.get(2), xy.get(3));//进入互相聊天界面
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //先判断一下是否有edittext 先排除在语音状态;

                String xmlData1 = wxUtils.getXmlData();
                if (xmlData1.contains("按住 说话")) {
                    wxUtils.adbClick(6, 782, 78, 854);
                }

                String[] strings = {"你好", "吃饭了吗", "在哪儿呢", "有空吗？", "出去玩啊"};
                String[] strings1 = {"你好", "吃了", "在家里", "有空", "不去"};
                int number = 0;
                while (number < 5) {

                    //开始聊天
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    if (isSend) {
                        cm.setText(strings[number]);
                    } else {
                        cm.setText(strings1[number]);
                    }

                    wxUtils.adb("input swipe " + 130 + " " + 794 + " " + 318 + " " + 842 + " " + 4000);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //                    [8,562][104,658]
                    wxUtils.adbClick(120, 719, 154, 749);//点击粘贴
                    //                    NodeUtils.clickNode();
                    wxUtils.adbClick(405, 794, 471, 842);//点击发送

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    number++;
                }
                backHome();
                break;
            }
        }
        String wxAccount = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
        String fiendsAccount = SPUtils.getString(context, "fiendsWxNumber", ""); // 获取之前的好友信息
        eachChatUnlock(fiendsAccount);


    }

    private void eachChatUnlock(String fiendsAccount) {
        String Path = URLS.url + "home/ApiAndroid/eachChatUnlock" + "?account=" + fiendsAccount;
        URL url = null;
        try {
            url = new URL(Path);
            // 2.建立一个http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 3.设置一些请求方式
            conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
            // // 503服务器内部错误
            if (code == 200) {
                Log.d("解锁成功", "");
            } else {
                Log.d("解锁失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查服务器传递的时间格式是否正确（秒为单位）
     *
     * @param flag 1 表示 游戏，2 表示 京东购物
     */
    private boolean checkStandingTime(String open_s, String open_e, int flag) {
        Integer temp_s = 60;
        Integer temp_e = 120;
        try {
            if (!TextUtils.isEmpty(open_s)) {
                temp_s = Integer.valueOf(open_s);
            }
            if (!TextUtils.isEmpty(open_e)) {
                temp_e = Integer.valueOf(open_e);
            }
        } catch (Exception ex) {
            return false;
        }
        if (temp_s > temp_e) {
            return false;
        }
        int tempNum = random.nextInt(temp_e - temp_s + 1);//
        //standingTime = temp_s+tempNum;//s 到 e 之间的随机值 （单位为秒）
        if (flag == 1) {
            standingTimeGame = temp_s + tempNum;
        } else {
            standingTimeShop = temp_s + tempNum;
        }
        return true;
    }

    public Boolean backMark = true;

    int backNum = 0;//返回次数

    /**
     * 返回到微信主页面
     */
    private void backHome() {
        backNum++;
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
            wxUtils.openWx((Activity) context);
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            backHome();
        } else if (xmlData.contains("更新") && xmlData.contains("取消") && xmlData.contains("立刻安装")) {
            List<String> ud = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < ud.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ud.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().contains("取消")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//取消
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//取消
                    wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y230, R.dimen.x264, R.dimen.y251);//确定
                    break;
                }
            }

        } else if (xmlData.contains("你要关闭购物页面?")) {
            wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y230, R.dimen.x264, R.dimen.y250);
        } else if (xmlData.contains("忘记密码") || (xmlData.contains("登录") && xmlData.contains("注册") && xmlData.contains("语言")) || (xmlData.contains("你的手机号码") && xmlData.contains("密码"))) {//判断是否登录
            backMark = false;
        } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我") && !(xmlData.contains("聊天信息")))) {//判断是否在微信主界面
            wxUtils.adb("input keyevent 4");
            if (backNum < 100) {
                backHome();
            }
        }
    }

    /**
     * 推送任务
     * 执行任务.先判断
     *
     * @param task
     */
    public void pushTask(final int task, final String arg0, final String arg1, final String arg2) {

        backNum = 0;
        if (wxUtils.isInstallApp(context, "com.tencent.mm")) {//判断支付宝是否安装
            if (time == 0) {
                wxUtils.openWx((Activity) context);//打开微信
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            xmlData = wxUtils.getXmlData();

            if (xmlData.contains("安全警告") && xmlData.contains("wx助手 正在尝试") && xmlData.contains("记住我的选择。")) {
                            /* wxUtils.adbClick(61, 472, 61, 520); //记住选择    TODO copy
                wxUtils.adbClick(342, 544, 438, 616);//确定*/

                wxUtils.adbDimensClick(context, R.dimen.x41, R.dimen.y232, R.dimen.x41, R.dimen.y232);//记住选择
                wxUtils.adbDimensClick(context, R.dimen.x260, R.dimen.y272, R.dimen.x260, R.dimen.y272);//确定
            }

            if (xmlData.contains("更新") && xmlData.contains("取消") && xmlData.contains("立刻安装")) {
                List<String> ud = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < ud.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ud.get(a)).getNode();
                    if (nodeBean != null && nodeBean.getText() != null && nodeBean.getText().contains("取消")) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//取消
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//取消
                        wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y230, R.dimen.x264, R.dimen.y251);//确定
                        break;
                    }
                }
                xmlData = wxUtils.getXmlData();
            }

            if (xmlData.contains("忘记密码") || (xmlData.contains("登录") && xmlData.contains("注册") && xmlData.contains("语言")) || (xmlData.contains("你的手机号码") && xmlData.contains("密码"))) {//判断是否登录
                ShowToast.show("请先登录微信", (Activity) context);
                status = 4;
                operationView.isLoginWx(false);
                return;
            } else {
                if (xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我") && !(xmlData.contains("聊天信息"))) {//判断是否在微信主界面
                    time = 0;
                    LogUtils.d("taskid:" + task);
                    if (task == 0 || task == 1 || task == 15 || task == 16 || task == 17 || task == 18 || task == 19 || task == 20 || task == 21 || task == 22 || task == 23 || task == 24 || task == 25 || task == 26) {
                        //                        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                        xmlData = wxUtils.getXmlData();
                        if (!xmlData.contains("新的朋友")) {//在通讯录界面，但是需要滑动到最顶端
                            //                            wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                            //                            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                            xmlData = wxUtils.getXmlData();
                        }
                    }
                    if (task == 10 || task == 11) {
                        //                        NodeUtils.clickNode("发现", "com.tencent.mm:id/c_z");//发现
                        NodeUtils.clickNode("发现", "com.tencent.mm:id/c_z");
                    }
                    SPUtils.putString(context, "AccountIsOnlyOne", "0");
                    switch (task) {
                        case 0://修改备注
                            // TODO: 2018/4/28/028  切换账号双号尚未修改
                            switchWxAccount1();
                            // TODO: 2018/4/28/028 检查到号被封了
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录

                            ShowToast.show("修改备注开始", (Activity) context);
                            startAlterName(arg0);
                            break;
                        case 1://拉群
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                            ShowToast.show("开始拉群开始", (Activity) context);
                            addCrowd();
                            upddateGroup();//拉完群反馈信息给服务器
                            break;
                        case 2://打开附近人
                            OpendFujin();
                            break;
                        case 3://自动通过好友申请
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                            AddFriend();

                            break;
                        case 4://添加通讯录为好友
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            getUsingWxAccount();
                            backHome();
                            //wxUtils.DeletPhone(context);
                            while (true) {
                                ShowToast.show("正在清理手机联系人请稍后1...", (Activity) context);
                                //wxUtils.delAllContacts(context.getContentResolver());
                                wxUtils.DeletPhone(context);
                                if (wxUtils.getContactCount(context) < 1) {
                                    break;
                                } else {
                                    LogUtils.d("通讯录的好友数量是" + wxUtils.getContactCount(context));
                                    // wxUtils.delAllContacts(context.getContentResolver());
                                    wxUtils.DeletPhone(context);
                                }
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            while (true) {
                                ShowToast.show("正在清理手机联系人请稍后2...", (Activity) context);
                                //wxUtils.delAllContacts(context.getContentResolver());
                                wxUtils.DeletPhone(context);
                                if (wxUtils.getContactCount(context) < 1) {
                                    break;
                                } else {
                                    LogUtils.d("通讯录的好友数量是" + wxUtils.getContactCount(context));
                                    // wxUtils.delAllContacts(context.getContentResolver());
                                    wxUtils.DeletPhone(context);
                                }
                            }
                            getPhoneAdd();
                            addFriendsReturn();
                            toForUnRead();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                getUsingWxAccount();
                                backHome();
                                //wxUtils.DeletPhone(context);
                                while (true) {
                                    //关闭微信进程
                                    //                                wxUtils.adb("am force-stop " + "com.tencent.mm");//关闭微信
                                    ShowToast.show("正在清理手机联系人请稍后1...", (Activity) context);
                                    wxUtils.DeletPhone(context);
                                    if (wxUtils.getContactCount(context) < 1) {
                                        break;
                                    } else {
                                        LogUtils.d("通讯录的好友数量是" + wxUtils.getContactCount(context));
                                        wxUtils.DeletPhone(context);
                                    }
                                }
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                while (true) {
                                    ShowToast.show("正在清理手机联系人请稍后2...", (Activity) context);
                                    //wxUtils.delAllContacts(context.getContentResolver());
                                    wxUtils.DeletPhone(context);
                                    if (wxUtils.getContactCount(context) < 1) {
                                        break;
                                    } else {
                                        LogUtils.d("通讯录的好友数量是" + wxUtils.getContactCount(context));
                                        // wxUtils.delAllContacts(context.getContentResolver());
                                        wxUtils.DeletPhone(context);
                                    }
                                }
                                getPhoneAdd();
                                addFriendsReturn();
                                toForUnRead();
                            }
                            break;
                        case 5://搜索添加好友
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            StatuRequest();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                StatuRequest();
                            }
                            break;
                        case 6://微信群保存到通讯录
                            SaveFlockToBook();
                            break;
                        case 7://自助加好友
                            switchWxAccount1();
                            backHome();
                            //3:来自微信号搜索 6:通过好友同意  13:来自手机通讯录 14:群聊 15:来自手机号搜索 17:通过名片分享添加  18:来自附近人 30:通过扫一扫添加 39:搜索公众号来源
                            String[] sniffing = new String[]{"3", "6", "13", "14", "15", "17", "18", "30", "39"};
                            if (!StringUtils.isEmpty(arg0)) {
                                sniffing_type = arg0;
                            } else {
                                sniffing_type = sniffing[random.nextInt(8)];//0-8的随机数
                            }

                            if (StringUtils.isEmpty(arg2)) {
                                contact_verify_msg = "您好,《name》，很高兴认识你。";
                            } else {
                                contact_verify_msg = arg2;
                            }

                            if (!StringUtils.isEmpty(arg1)) {
                                //                            HttpPhoneDataXT(Integer.valueOf(arg1));
                                sniffingAddFriendsDatas(Integer.valueOf(arg1));
                            } else {
                                //                            HttpPhoneDataXT(5);
                                sniffingAddFriendsDatas(5);
                            }
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                backHome();
                                //3:来自微信号搜索 6:通过好友同意  13:来自手机通讯录 14:群聊 15:来自手机号搜索 17:通过名片分享添加  18:来自附近人 30:通过扫一扫添加 39:搜索公众号来源
                                sniffing = new String[]{"3", "6", "13", "14", "15", "17", "18", "30", "39"};
                                if (!StringUtils.isEmpty(arg0)) {
                                    sniffing_type = arg0;
                                } else {
                                    sniffing_type = sniffing[random.nextInt(8)];//0-8的随机数
                                }

                                if (StringUtils.isEmpty(arg2)) {
                                    contact_verify_msg = "您好,《name》，很高兴认识你。";
                                } else {
                                    contact_verify_msg = arg2;
                                }

                                if (!StringUtils.isEmpty(arg1)) {
                                    //                            HttpPhoneDataXT(Integer.valueOf(arg1));
                                    sniffingAddFriendsDatas(Integer.valueOf(arg1));
                                } else {
                                    //                            HttpPhoneDataXT(5);
                                    sniffingAddFriendsDatas(5);
                                }
                            }
                            break;
                        case 8: //朋友圈发布
                            // type 0文字    1图文   2视频
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!StringUtils.isEmpty(arg1)) {
                                FriendsRing(Integer.valueOf(arg0), arg1);
                            } else {
                                LogUtils.d("朋友圈图文发布参数不全");
                            }
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!StringUtils.isEmpty(arg1)) {
                                    FriendsRing(Integer.valueOf(arg0), arg1);
                                } else {
                                    LogUtils.d("朋友圈图文发布参数不全");
                                }
                            }
                            break;
                        case 9://朋友圈点赞
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (StringUtils.isEmpty(dz_sum)) {
                                clickLike(5);
                            } else {
                                clickLike(Integer.parseInt(dz_sum));
                            }

                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (StringUtils.isEmpty(dz_sum)) {
                                    clickLike(5);
                                } else {
                                    clickLike(Integer.parseInt(dz_sum));
                                }
                            }
                            break;
                        case 10://朋友圈购物
                            boolean isTrueStandingTimeS = checkStandingTime(open_s_shop, open_e_shop, 2);
                            if (!isTrueStandingTimeS) {//时间格式错误
                                break;
                            }
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            goToShopping();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                goToShopping();
                            }
                            break;
                        case 11://朋友圈游戏
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            goToPlayGame();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                goToPlayGame();
                            }
                            break;
                        case 12://微信好友发视频
                            WSendVido();
                            break;
                        case 13://微信好友发图片素材
                            WSendPic();
                            break;
                        case 14://后台传递的手机号码
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            getUsingWxAccount();
                            backHome();
                            //wxUtils.DeletPhone(context);
                            while (true) {
                                ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                //wxUtils.delAllContacts(context.getContentResolver());
                                wxUtils.DeletPhone(context);
                                if (wxUtils.getContactCount(context) < 1) {
                                    break;
                                } else {
                                    LogUtils.d("通讯录的好友数量是" + wxUtils.getContactCount(context));
                                    // wxUtils.delAllContacts(context.getContentResolver());
                                    wxUtils.DeletPhone(context);
                                }
                            }
                            ShowToast.show("联系人清理完成", (Activity) context);
                            for (int i = 0; i < materia_phone.size(); i++) {
                                LogUtils.d("添加联系人" + materia_phone.get(i));
                                wxUtils.addContact(materia_phone.get(i), materia_phone.get(i), context);
                            }
                            ShowToast.show("正在添加电话号码到联系人，请等待...", (Activity) context);
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ShowToast.show("联系人添加完成，开始执行添加好友任务...", (Activity) context);
                            AddCommunication(wx_Sex);

                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                getUsingWxAccount();
                                backHome();
                                //wxUtils.DeletPhone(context);
                                while (true) {
                                    ShowToast.show("正在清理手机联系人请稍后...", (Activity) context);
                                    //wxUtils.delAllContacts(context.getContentResolver());
                                    wxUtils.DeletPhone(context);
                                    if (wxUtils.getContactCount(context) < 1) {
                                        break;
                                    } else {
                                        LogUtils.d("通讯录的好友数量是" + wxUtils.getContactCount(context));
                                        // wxUtils.delAllContacts(context.getContentResolver());
                                        wxUtils.DeletPhone(context);
                                    }
                                }
                                ShowToast.show("联系人清理完成", (Activity) context);
                                for (int i = 0; i < materia_phone.size(); i++) {
                                    LogUtils.d("添加联系人" + materia_phone.get(i));
                                    wxUtils.addContact(materia_phone.get(i), materia_phone.get(i), context);
                                }
                                ShowToast.show("正在添加电话号码到联系人，请等待...", (Activity) context);
                                try {
                                    Thread.sleep(20000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ShowToast.show("联系人添加完成，开始执行添加好友任务...", (Activity) context);
                                AddCommunication(wx_Sex);


                            }
                            break;
                        case 15://群里发图文
                            //                            sendFlockMessageClickTo(arg0, false);
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            if (billing(arg0, arg1)) {
                                WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(arg0.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
                                transmitMessageFlock(arg1, wxFlockMessageBeans.length, true);
                            }
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                if (billing(arg0, arg1)) {
                                    WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(arg0.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);
                                    transmitMessageFlock(arg1, wxFlockMessageBeans.length, true);
                                }
                            }
                            break;
                        case 16://养号互撩
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            if (StringUtils.isEmpty(arg0)) {
                                //                                sendFriendsMessageCultivateDatas(0);
                                return;
                            } else {
                                if (arg0.equals("3")) {
                                    sendFriendsMessageCultivate(Integer.valueOf(arg0), new ArrayList<String>());
                                } else {
                                    if (sendFriendsMessageCultivateDatasList != null) {
                                        sendFriendsMessageCultivateDatasList = null;
                                    }
                                    sendFriendsMessageCultivateDatas(Integer.valueOf(arg0));

                                    try {
                                        Thread.sleep(10000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if (sendFriendsMessageCultivateDatasList != null && sendFriendsMessageCultivateDatasList.size() > 0) {
                                        sendFriendsMessageCultivate(Integer.valueOf(arg0), sendFriendsMessageCultivateDatasList);
                                    } else {
                                        return;
                                    }

                                }
                            }
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                if (StringUtils.isEmpty(arg0)) {
                                    //                                sendFriendsMessageCultivateDatas(0);
                                    return;
                                } else {
                                    if (arg0.equals("3")) {
                                        sendFriendsMessageCultivate(Integer.valueOf(arg0), new ArrayList<String>());
                                    } else {
                                        if (sendFriendsMessageCultivateDatasList != null) {
                                            sendFriendsMessageCultivateDatasList = null;
                                        }
                                        sendFriendsMessageCultivateDatas(Integer.valueOf(arg0));

                                        try {
                                            Thread.sleep(10000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if (sendFriendsMessageCultivateDatasList != null && sendFriendsMessageCultivateDatasList.size() > 0) {
                                            sendFriendsMessageCultivate(Integer.valueOf(arg0), sendFriendsMessageCultivateDatasList);
                                        } else {
                                            return;
                                        }

                                    }
                                }
                            }
                            //----------------------------------------------------------------------


                            break;
                        case 17://朋友圈分享链接
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            if (StringUtils.isEmpty(arg1)) {
                                shareUrl(arg0, arg2);//分享到朋友圈
                            } else {
                                if (arg1.equals("0")) {
                                    shareUrl(arg0, arg2);//分享到朋友圈
                                } else {
                                    shareFriendsUrl(arg0); //分享给好友
                                }
                            }
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                if (StringUtils.isEmpty(arg1)) {
                                    shareUrl(arg0, arg2);//分享到朋友圈
                                } else {
                                    if (arg1.equals("0")) {
                                        shareUrl(arg0, arg2);//分享到朋友圈
                                    } else {
                                        shareFriendsUrl(arg0); //分享给好友
                                    }
                                }
                            }
                            break;
                        //                    sendFriendsMessage
                        case 18://好友发消息
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            sendFriendsMessage(1, arg0, arg1, arg2);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                sendFriendsMessage(1, arg0, arg1, arg2);
                            }
                            break;
                        case 19://好友发图片
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            sendFriendsMessage(0, arg0, arg1, arg2);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                sendFriendsMessage(0, arg0, arg1, arg2);
                            }
                            break;
                        case 20://好友发视频
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            sendFriendsMessage(2, arg0, arg1, arg2);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                sendFriendsMessage(2, arg0, arg1, arg2);
                            }

                            break;
                        case 21://群发消息
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            sendFlockMessage(1, arg0);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                sendFlockMessage(1, arg0);
                            }
                            break;
                        case 22://群发图片
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            sendFlockMessage(0, arg0);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                sendFlockMessage(0, arg0);
                            }

                            break;
                        case 23://群发视频
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录

                            sendFlockMessage(2, arg0);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录

                                sendFlockMessage(2, arg0);
                            }

                            break;
                        case 24://统计好友数和群信息
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            getUsingWxAccount();
                            backHome();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            wxUtils.adbWxClick(187, 839);//点击通讯录
                            statistics();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                getUsingWxAccount();
                                backHome();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbWxClick(187, 839);//点击通讯录
                                statistics();
                            }
                            //                            statistics();
                            break;
                        case 25://语音聊天
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            intelligentChat2(1);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                intelligentChat2(1);
                            }
                            break;
                        case 26://视频聊天
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            intelligentChat2(0);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                intelligentChat2(0);
                            }
                            break;
                        case 27://关注公众号
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            if (arg0.equals("1")) {
                                addPublicMark(arg1);
                            } else {
                                addPublicMarkElseFlock(arg1);
                            }
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                if (arg0.equals("1")) {
                                    addPublicMark(arg1);
                                } else {
                                    addPublicMarkElseFlock(arg1);
                                }
                            }

                            break;
                        case 28://浏览公众号
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            publicMarkRead();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                publicMarkRead();
                            }
                            break;
                        case 30://发红包
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            sendRedPacket(arg0, arg1, arg2);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                sendRedPacket(arg0, arg1, arg2);
                            }
                            break;
                        case 31://统计微信好友信息
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            statisticsMessage();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                statisticsMessage();
                            }
                            break;
                        case 32://微信群  好友转发
                            transmitWxFriendsMessageFlock(arg0, arg1, arg2);
                            break;
                        case 33://微信群  群转发
                            transmitWxQunMessageFlock(arg0, arg1, arg2);
                            break;
                        case 34://把新增好友拉入群中
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, " AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            laNewFriendsToQun(new PullNewFriendCallback() {
                                @Override
                                public void onSucess(int user) {
                                    sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                                    if (sendAccountType == 3) {
                                        switchWxAccount2();
                                        backHome();
                                        wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                                        laNewFriendsToQun(new PullNewFriendCallback() {
                                            @Override
                                            public void onSucess(int user) {
                                                ShowToast.show("  二维码拉好友进群1号 完成！", (Activity) context);
                                                //任务结束
                                                backHome();
                                            }
                                        }, 2);
                                    } else {
                                        ShowToast.show("  二维码拉好友进群2号 完成！", (Activity) context);
                                        backHome();
                                    }
                                }
                            }, 0);
                            break;
                        case 35: //自定义拉群
                            wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            ziDingYiLaQun(arg0, arg1, arg2);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                ziDingYiLaQun(arg0, arg1, arg2);
                            }
                            break;
                        case 36: //语音互聊 智能聊天
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            intelligentChat();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                intelligentChat();
                            }
                            break;
                        case 37: // 阅读未读信息并回复
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            toForUnRead();
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                toForUnRead();
                            }
                            break;
                        case 38: //删除指定朋友圈
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            deleteWxPengYouContent(arg0);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                deleteWxPengYouContent(arg0);
                            }
                            break;
                        case 39: //超级自定义修改备注
                            wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            superXiuGaiBeiZhu(arg0, arg1);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                superXiuGaiBeiZhu(arg0, arg1);
                            }
                            break;
                        case 40: // B账号 语音互聊
                            wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            ChatB(arg0);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                ChatB(arg0);
                            }

                            break;
                        case 41: // B账号 语音互聊
                            wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            ChatC(arg0);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                ChatC(arg0);
                            }

                            break;
                        case 42: // B账号 语音互聊
                            wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            ChatC(arg0);
                            sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
                            if (sendAccountType == 3) {
                                switchWxAccount2();
                                if (!getIsAccountIsOk()) {  // 检查到号被封了
                                    break;
                                }
                                backHome();
                                ChatC(arg0);
                            }

                            break;
                        case 43:
                            wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            OldFansToQun(arg0, arg1, arg2);
                            break;
                        case 44:
                            wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            backHome();
                            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
                            newFansToQun(arg0, arg1, arg2);
                            break;
                        case 45: // 添加养号（A账户）
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            getUsingWxAccount();
                            backHome();
                            String firendsWxName = arg0;     //好友信息
                            String myWxName = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
                            String myLocation = SPUtils.getString(context, "WxAccountLocation", "");
                            String firendsLocation = arg2;
                            String myUid = SPUtils.getString(context, "uid", "0000");
                            String firendsUid = arg1;
                            addOurFriends(myWxName, myUid, myLocation, firendsWxName, firendsUid, firendsLocation);
                            break;
                        case 46: // B账号  通过好友认证
                            switchWxAccount1();
                            if (!getIsAccountIsOk()) {  // 检查到号被封了
                                break;
                            }
                            if (SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("1")) {
                                break;
                            }
                            getUsingWxAccount();
                            backHome();
                            String firendsWxName2 = arg0;     //好友信息
                            String myWxName2 = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
                            String myLocation2 = SPUtils.getString(context, "WxAccountLocation", "");
                            String firendsLocation2 = arg2;
                            String myUid2 = SPUtils.getString(context, "uid", "0000");
                            String firendsUid2 = arg1;
                            //                          addOurFriends(myWxName,myUid,myLocation,firendsWxName,firendsUid,firendsLocation);
                            passFriendsApply(myWxName2, myUid2, myLocation2, firendsWxName2, firendsUid2, firendsLocation2);//通过好友认证
                            break;
                    }
                    backHome();
                } else {//不在微信主界面，跳转到主界面 TODO
                    if (backMark) {
                        backHome();
                        time = 1000;
                        pushTask(task, arg0, arg1, arg2);
                    }

                }
            }
        } else

        {
            LogUtils.d("没有安装微信");
        }
    }


    //  通过好友认证
    private void passFriendsApply(String myWxName2, String myUid2, String myLocation2, String firendsWxName2, String firendsUid2, String firendsLocation2) {
        backHome();
        wxUtils.adbClick(306, 36, 378, 108);//搜索
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        cm.setText(firendsWxName2);
        wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
        wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
        wxUtils.adb("input keyevent 4");
        xmlData = wxUtils.getXmlData();
        Boolean Flag = true;
        if (xmlData.contains("微信号: " + firendsWxName2)) {
            //微信号: zhangxiaoh564
            List<String> friendsList = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < friendsList.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(friendsList.get(i)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/ql") && nodeBean.getText().startsWith("微信号")) {
                    //此号已经是你的好友了
                    listXY = wxUtils.getXY(nodeBean.getBounds());//点击好友
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友
                    Flag = false;
                    break;
                }
            }
            if (!Flag) {
                NodeUtils.clickNode("com.tencent.mm:id/he");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                NodeUtils.clickNode("com.tencent.mm:id/d09");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                NodeUtils.clickNode("com.tencent.mm:id/anw");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                NodeUtils.clickNode("com.tencent.mm:id/aoz");
                int xx = 0;
                int yy = 0;//删除
                switch (Build.MODEL) {
                    case ModelConstans.coolpad_8737:
                        xx = 683;
                        yy = 1020;
                        break;
                    case ModelConstans.tvyk:
                        //                                wxUtils.adbWxClick(461, 731);
                        xx = 461;
                        yy = 731;
                        break;
                }
                wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 5000);  //删除
                wxUtils.adb("input text " + "ZZZ9" + "D0001" + "_" + firendsUid2 + "_" + firendsLocation2);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
                NodeUtils.clickNode("完成", "com.tencent.mm:id/hd");

            }
        }

        // 如果不包含，就说明没有此人好友，需验证通过
        String meName = "";
        for (int j = 0; j < 7; j++) {
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {

            }
            wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
            wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI");
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {

            }
            int aaa = 0;
            xmlData = wxUtils.getXmlData();
            String friendsInputName = "";
            List<String> meWxFriend = wxUtils.getNodeList(xmlData);
            for (int i = 3; i < meWxFriend.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(meWxFriend.get(i)).getNode();
                NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(meWxFriend.get(i - 2)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/b8k") && nodeBean.getText() != null && nodeBean.getText().equals("接受")) {
                    if (nodeBean3 != null && nodeBean3.getText() != null && nodeBean3.getText().contains("ZZZ9D") && !meName.contains(nodeBean3.getText())) {
                        meName = meName + nodeBean3.getText();
                        listXY = wxUtils.getXY(nodeBean.getBounds());//接受
                        friendsInputName = nodeBean3.getText();
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击接受
                        aaa = 1;
                        try {
                            Thread.sleep(3 * 1000);
                        } catch (InterruptedException e) {

                        }
                        break;
                    }

                }
            }
            if (aaa == 1) {
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("对方存在异常行为")) {
                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                    wxUtils.adb("input text " + friendsInputName);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                    wxUtils.adbWxClick(415, 75); // 点击完成
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                    ShowToast.show("我已经点击完成了", (Activity) context);
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {

                    }
                } else {
                    NodeUtils.clickNode("去验证", "com.tencent.mm:id/all");
                    NodeUtils.clickNode("设置备注和标签", "com.tencent.mm:id/anw");
                    int xx = 0;
                    int yy = 0;//删除
                    switch (Build.MODEL) {
                        case ModelConstans.coolpad_8737:
                            xx = 683;
                            yy = 1020;
                            break;
                        case ModelConstans.tvyk:
                            //                                wxUtils.adbWxClick(461, 731);
                            xx = 461;
                            yy = 731;
                            break;
                    }
                    wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 5000);  //删除
                    //                    wxUtils.adbWxClick(461, 220); //点击最右边
                    //                    for (int i = 0; i < 30; i++) {
                    //                        wxUtils.adb("input keyevent 67");// 删除
                    //                    }
                    //                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    //                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                    wxUtils.adb("input text " + friendsInputName);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                    NodeUtils.clickNode("保存", "com.tencent.mm:id/hc");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    wxUtils.adbUpSlide(context);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    NodeUtils.clickNode("通过验证", "com.tencent.mm:id/an2");
                    NodeUtils.clickNode("完成", "com.tencent.mm:id/hc");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
                wxUtils.adb("input keyevent 4");

            }
        }
    }

    //  只拉养号
    private void addOurFriends(String myWxName, String myUid, String myLocation, String firendsWxName, String firendsUid, String firendsLocation) {
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        DecimalFormat df = new DecimalFormat("000");
        boolean flag0 = true;
        while (flag0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag0 = false;
            }
        }
        wxUtils.adbClick(306, 36, 378, 108);//搜索
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        cm.setText(firendsWxName);
        wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
        wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
        wxUtils.adb("input keyevent 4");
        Boolean Flag = true;
        if (xmlData.contains("微信号: " + firendsWxName)) {
            //微信号: zhangxiaoh564
            List<String> friendsList = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < friendsList.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(friendsList.get(i)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/ql") && nodeBean.getText().startsWith("微信号")) {
                    //此号已经是你的好友了
                    listXY = wxUtils.getXY(nodeBean.getBounds());//点击好友
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友
                    Flag = false;
                    break;
                }
            }
            if (!Flag) {
                NodeUtils.clickNode("com.tencent.mm:id/he");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                NodeUtils.clickNode("com.tencent.mm:id/d09");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                NodeUtils.clickNode("com.tencent.mm:id/anw");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                NodeUtils.clickNode("com.tencent.mm:id/aoz");
                int xx = 0;
                int yy = 0;//删除
                switch (Build.MODEL) {
                    case ModelConstans.coolpad_8737:
                        xx = 683;
                        yy = 1020;
                        break;
                    case ModelConstans.tvyk:
                        xx = 461;
                        yy = 731;
                        break;
                }
                wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 5000);  //删除
                wxUtils.adb("input text " + "ZZZ9" + "D0001" + "_" + firendsUid + "_" + firendsLocation);
                NodeUtils.clickNode("完成", "com.tencent.mm:id/hd");

            }
        }

        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("查找微信号")) {
            wxUtils.adbUpSlide(context);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (xmlData.contains("查找微信号")) {
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/baz")
                        && nodeBean.getText() != null && nodeBean.getText().contains("查找微信号")
                        ) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 查找微信号 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  查找微信号
                    break;
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("确定") && xmlData.contains("用户不存在")) {
            NodeUtils.clickNode("确定", "com.tencent.mm:id/all");
            return;
        }

        if (xmlData.contains("发消息")) {
            return;
        }
        NodeUtils.clickNode("添加到通讯录", "com.tencent.mm:id/an_");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int xx = 0;
        int yy = 0;//删除
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("验证申请")) {
            NodeUtils.clickNode("设置备注和标签", "com.tencent.mm:id/anw");

            switch (Build.MODEL) {
                case ModelConstans.coolpad_8737:
                    xx = 683;
                    yy = 1020;
                    break;
                case ModelConstans.tvyk:
                    xx = 461;
                    yy = 731;
                    break;
            }
            wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 7000);  //删除
            wxUtils.adb("input text " + "ZZZ9" + "D" + "0001" + "_" + firendsUid + "_" + firendsLocation);
            NodeUtils.clickNode("保存", "com.tencent.mm:id/hc");
            backHome();
        } else {

            switch (Build.MODEL) {
                case ModelConstans.coolpad_8737:
                    xx = 683;
                    yy = 1020;
                    break;
                case ModelConstans.tvyk:
                    xx = 461;
                    yy = 731;
                    break;
            }
            wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 7000);  //删除
            wxUtils.adb("input text " + "ZZZ9D0001" + "_" + firendsUid + "_" + firendsLocation);
            NodeUtils.clickNode("发送", "com.tencent.mm:id/hd");
            backHome();
        }
    }
    //  只拉新粉丝

    private void newFansToQun(String arg0, String arg1, String arg2) {

        String fanType = arg2;
        String mid_fourth = "";
        //获取中间的四位
        if (fanType.equals("1")) {
            mid_fourth = "1000";

        } else if (fanType.equals("2")) {
            mid_fourth = "0100";

        } else if (fanType.equals("3")) {
            mid_fourth = "0010";

        } else if (fanType.equals("4")) {
            mid_fourth = "0001";
        }
        String personNum = SPUtils.getString(context, "QunPersonNum", "0");
        SPUtils.putString(context, "groupName_newFriends", "");  // 记录修改过的名字的合集
        int personNum_int = Integer.valueOf(personNum);
        String wxQunFriendsName = "";
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        boolean flag0 = true;
        while (flag0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag0 = false;
            }
        }
        SPUtils.putInt((Activity) context, "NewFriendsCount", 0);
        SPUtils.putInt(context, "TimeOver", 1);
        wxUtils.openWx((Activity) context);//打开微信
        wxUtils.adbClick(153, 822, 207, 847);
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        //        startAlterName2("YYY0");

        superXiuGaiBeiZhu_newFans1(arg0, arg1);  //修改新粉丝的坐标
        backHome();
        wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
        wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
        wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
        int newfriendsaccount = 0;
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("YY")) {
            ShowToast.show("没有新好友!", (Activity) context);
        } else {
            ShowToast.show("有新好友!", (Activity) context);
            newfriendsaccount = 1;
        }
        boolean flag_0 = true;
        while (flag_0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag_0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(282, 822, 318, 847); //点击发现
                flag_0 = false;
            }
        }
        boolean QunIsFull = true;
        while (QunIsFull && (newfriendsaccount != 0)) {
            SPUtils.putString(context, "groupName", "");

            String url = URLS.wxNewFriendsToQun();
            HashMap<String, String> params = new HashMap<>();
            params.put("number", personNum_int + "");
            String batch = SPUtils.getString(context, "QunBatch", "00");
            boolean netFlag = true;
            int number = 0;
            while (netFlag) {
                try {

                    number++;
                    Thread.sleep(20000);
                    if (number > 30) {
                        //                        superXiuGaiBeiZhu_newFans2(arg0, mid_fourth,group);
                        superXiuGaiBeiZhu_newFans2(arg0, mid_fourth, wxQunFriendsName);
                        return;
                    }
                    Response execute = OkHttpUtils.get().params(params).url(url).addParams("batch", batch).build().execute();
                    if (execute.code() == 200) {
                        String string = execute.body().string();
                        WxNewFriendsToQunBean wxNewFriendsToQunBean = GsonUtil.parseJsonWithGson(string, WxNewFriendsToQunBean.class);
                        if (wxNewFriendsToQunBean != null) {
                            ShowToast.show(wxNewFriendsToQunBean.getMsg(), (Activity) context);
                            pic_id = Integer.parseInt(wxNewFriendsToQunBean.getData().getId());//获取到图片id
                            if (!TextUtils.isEmpty(wxNewFriendsToQunBean.getData().getQr_code_address()))
                                if (downFlockImg(wxNewFriendsToQunBean.getData().getQr_code_address(), 0))
                                    netFlag = false;
                            SPUtils.putString(context, "groupName", wxNewFriendsToQunBean.getData().getWx_group_name());
                            SPUtils.putInt(context, "picDownloadSuccess", 1);
                        }
                    } else {
                        ShowToast.show("网络异常未获取到二维码图片！", (Activity) context);
                    }
                } catch (Exception e) {
                    ShowToast.show("程序异常未获取到二维码图片！", (Activity) context);
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            backHome();
            wxUtils.adbClick(282, 822, 318, 847); //点击发现
            wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
            wxUtils.adbClick(400, 550, 400, 550);
            wxUtils.adbClick(400, 550, 400, 550);//连续点击两次个人头像
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().equals("发消息")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  发消息
                    break;
                }
            }
            wxUtils.adbClick(402, 782, 474, 854);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("相册") && !xmlData.contains("拍摄")) {
                wxUtils.adbClick(402, 782, 474, 854);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            wxUtils.adbClick(61, 612, 95, 635);
            wxUtils.adbClick(50, 820, 50, 820);//点击左下角
            boolean ccc = true;
            boolean ddd = true;

            while (ccc) {
                String xmlData_picture = wxUtils.getXmlData();
                if (xmlData_picture.contains("com.tencent.mm:id/d1r") && xmlData_picture.contains("ykimages")) {
                    List<String> pictureList = wxUtils.getNodeList(xmlData_picture);
                    for (int c = 0; c < pictureList.size(); c++) {
                        NodeXmlBean.NodeBean pictureBean = wxUtils.getNodeXmlBean(pictureList.get(c)).getNode();
                        if (pictureBean != null && pictureBean.getResourceid() != null && "com.tencent.mm:id/d1r".equals(pictureBean.getResourceid())
                                && pictureBean.getText() != null && pictureBean.getText().equals("ykimages")) {
                            listXY = wxUtils.getXY(pictureBean.getBounds());//获取坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击ykimages 文件夹
                            ccc = false;
                            break;
                        }
                    }
                } else {
                    wxUtils.adbUpSlide(context);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();
                    if (!xmlData_picture.contains("com.tencent.mm:id/d1r") || !xmlData_picture.contains("ykimages")) {
                        ccc = false;
                        ddd = false;
                    }
                }
            }
            if (!ddd) {
                ShowToast.show("未下载到图片，重新开始下载", (Activity) context);
                List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                String uid = SPUtils.getString(context, "uid", "0000");
                QunMessageBean messageBean = new QunMessageBean(0 + "", pic_id + "", uid, "" + personNum_int);
                mQunMessageBeanList.add(messageBean);
                String str = new Gson().toJson(mQunMessageBeanList);
                LogUtils.d("JSON" + str.toString());
                //                sendWxQunMessage(str);
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("json", str);
                try {
                    String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("图片和视频")) {
                wxUtils.adbClick(78, 119, 108, 149);//选中图片
                wxUtils.adbClick(378, 49, 468, 94);//点击发送
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                continue;
            }
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("com.tencent.mm:id/aec")) {
                continue;
            }
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size() - 1; a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/aec"))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 群名片图片 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击   群名片图片
                    break;
                }
            }
            wxUtils.adb("input swipe " + 240 + " " + 400 + " " + 200 + " " + 400 + " " + 4000);  //长按EdiText
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size() - 1; a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && (nodeBean.getText().equals("识别图中二维码"))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 群名片图片 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击识别图中二维码
                    break;
                }
            }
            if (!xmlData.contains("识别图中二维码")) {
                List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                String uid = SPUtils.getString(context, "uid", "0000");
                QunMessageBean messageBean = new QunMessageBean(0 + "", pic_id + "", uid + "", personNum_int + "");
                mQunMessageBeanList.add(messageBean);
                String str = new Gson().toJson(mQunMessageBeanList);
                LogUtils.d("JSON" + str.toString());
                groupPeopleNumber = 0;
                //               sendWxQunMessage(str);
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("json", str);
                try {
                    String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            wxUtils.adbClick(90, 481, 258, 514);
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //无法确定具体坐标，大概在四个范围
            wxUtils.adbClick(240, 600, 240, 600);//点击加入群聊
            wxUtils.adbClick(240, 550, 240, 550);//点击加入群聊
            wxUtils.adbClick(240, 500, 240, 500);//点击加入群聊
            wxUtils.adbClick(240, 650, 240, 650);//点击加入群聊
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean Flag = true;
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("(") || !xmlData.contains(")") || !xmlData.contains("加入群聊")) {
                LogUtils.d("二维码群图片没下载下来");
                Flag = false;
                continue;
            }
            int kkk = 0;
            while (Flag) {
                wxUtils.adbClick(408, 36, 480, 108);//点击右上角的人头像
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("(") || !xmlData.contains(")") || !xmlData.contains("聊天信息")) {
                    LogUtils.d("二维码群图片没下载下来");
                    Flag = false;
                    continue;
                }
                int i = 0;
                Boolean flag1 = true;
                while (flag1) {
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size() - 1; a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getText() != null && nodeBean.getText().contains("聊天信息")) {
                            String qunPersonNum = nodeBean.getText();
                            String Num = qunPersonNum.substring(5, qunPersonNum.length() - 1);
                            i = Integer.parseInt(Num);//获取到目前群内成员的人数
                            kkk++;
                            break;
                        }
                    }
                    if (i < personNum_int) {
                        for (int a = 0; a < nodeList.size() - 1; a++) {//寻找 添加成员的按钮
                            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                            if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/d05"))
                                    && nodeBean.getContentdesc() != null && nodeBean.getContentdesc().equals("添加成员")) {
                                flag1 = false;
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取 添加成员 按钮 的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  添加成员 开始拉人
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbWxClick(465, 790);  //点击右侧Y
                                break;
                            }
                        }
                        if (flag1 == true) {//如果页面没有添加成员 “+”存在
                            wxUtils.adbUpSlide(context);
                        }
                    } else if (i >= personNum_int) {
                        flag1 = false;
                    }
                }
                if (i >= personNum_int) {
                    QunIsFull = true;
                    Flag = false; //说明已经满40个人了
                    //                    wxUtils.adbUpSlide(context);
                    Boolean qunNameIsVisibled = false;
                    while (!qunNameIsVisibled) {
                        String newXmlData = wxUtils.getXmlData();
                        if (newXmlData.contains("群聊名称") && newXmlData.contains("群二维码")) {
                            qunNameIsVisibled = true;
                        } else {
                            wxUtils.adbUpSlide(context);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    wxUtils.screenShot(); //截图
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbUpSlide(context);
                    wxUtils.adbUpSlide(context);
                    wxUtils.adbUpSlide(context); //滑到最底部了
                    List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                    String uid = SPUtils.getString(context, "uid", "0000");
                    QunMessageBean messageBean = new QunMessageBean(i - 1 + "", pic_id + "", uid, personNum_int + "");
                    mQunMessageBeanList.add(messageBean);
                    String str = new Gson().toJson(mQunMessageBeanList);
                    LogUtils.d("JSON" + str.toString());
                    groupPeopleNumber = i - 1;
                    //                    sendWxQunMessage(str);
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("json", str);
                    try {
                        String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //                    wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                    //                    try {
                    //                        Thread.sleep(500);
                    //                    } catch (InterruptedException e) {
                    //                        e.printStackTrace();
                    //                    }
                    //                    wxUtils.adbClick(300, 490, 396, 535); //点击确定
                    //                    try {
                    //                        Thread.sleep(2000);
                    //                    } catch (InterruptedException e) {
                    //                        e.printStackTrace();
                    //                    }

                    NodeUtils.clickNode("删除并退出", "android:id/title");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String newXmlData = wxUtils.getXmlData();
                    if (newXmlData.contains("删除并退出后")) {
                        NodeUtils.clickNode("确定", "com.tencent.mm:id/all");
                    } else {
                        NodeUtils.clickNode("删除并退出", "android:id/title");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        NodeUtils.clickNode("确定", "com.tencent.mm:id/all");
                    }
                    sendQunSrceenShot();
                    backHome();
                }

                boolean flag = true;
                if (i >= personNum_int) {// 满40个人后就不用拉人了
                    flag = false;
                }
                int aaaa = 0;
                Boolean flag3 = false;
                while (flag) {
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size() - 1; a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode();
                        if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/kq")) && nodeBean2.isEnabled() == true
                                && nodeBean.getText() != null && !wxQunFriendsName.contains(nodeBean.getText()) && nodeBean2.isChecked() == false) {
                            if (nodeBean.getText().startsWith("YYZZZ")) {
                                String wxName = nodeBean.getText();
                                aaaa++;
                                wxQunFriendsName = wxQunFriendsName + wxName;
                                listXY = wxUtils.getXY(nodeBean2.getBounds());// 获取选中框的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击选中框
                                if (aaaa >= personNum_int - i) {
                                    flag = false;
                                    wxUtils.adbClick(368, 49, 468, 94);//点击确定
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    String xmlData3 = wxUtils.getXmlData();
                                    if (xmlData3.contains("确定")) {
                                        nodeList = wxUtils.getNodeList(xmlData3);
                                        for (int b = nodeList.size() - 1; b > 0; b--) {
                                            NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                            if (nodeBean3.getText() != null && nodeBean3.getText().equals("确定")) {
                                                listXY = wxUtils.getXY(nodeBean3.getBounds());
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击确定
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            } else if (nodeBean.getText().startsWith("ZZZ") || nodeBean.getText().startsWith("zzz")) {
                                flag3 = true;//已经检测到ZZZ了
                                break;
                            }

                        }
                    }
                    if (flag == true && aaaa < personNum_int - i) {
                        wxUtils.adbUpSlide(context);//向上滑动
                        String xmlData2 = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData2);

                        if (xmlData2.equals(xmlData) || (flag3 == true)) {
                            QunIsFull = true;
                            flag = false;
                            // 已经全部拉完了
                            if (aaaa == 0) {
                                wxUtils.adb("input keyevent 4");//返回
                            } else {
                                wxUtils.adbClick(378, 49, 468, 94);//点击确定
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String xmlData3 = wxUtils.getXmlData();
                            if (xmlData3.contains("确定")) {
                                nodeList = wxUtils.getNodeList(xmlData3);
                                for (int b = nodeList.size() - 1; b > 0; b--) {
                                    NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                    if (nodeBean3.getText() != null && nodeBean3.getText().equals("确定")) {
                                        listXY = wxUtils.getXY(nodeBean3.getBounds());
                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击确定
                                        break;
                                    }
                                }
                            }
                            xmlData = wxUtils.getXmlData();
                            nodeList = wxUtils.getNodeList(xmlData);
                            for (int a = 0; a < nodeList.size() - 1; a++) {
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                                if (nodeBean.getText() != null && nodeBean.getText().contains("聊天信息")) {
                                    String qunPersonNum = nodeBean.getText();
                                    String Num = qunPersonNum.substring(5, qunPersonNum.length() - 1);
                                    i = Integer.parseInt(Num);//获取到目前群内成员的人数
                                    Log.d("群内人数", "群内人数:" + i);
                                    // 将群 人数上传到服务器，再退出该群
                                    List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                                    String uid = SPUtils.getString(context, "uid", "0000");
                                    QunMessageBean messageBean = new QunMessageBean((i - 1) + "", pic_id + "", uid + "", personNum_int + "");
                                    mQunMessageBeanList.add(messageBean);
                                    String str = new Gson().toJson(mQunMessageBeanList);
                                    LogUtils.d("JSON" + str.toString());
                                    groupPeopleNumber = i - 1;
                                    //                                  sendWxQunMessage(str);
                                    Map<String, String> messageMap = new HashMap<>();
                                    messageMap.put("json", str);
                                    try {
                                        String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                            if (i <= personNum_int) {
                                QunIsFull = false;// 群没有满，就不用再申请二维码了
                                Flag = false;
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context); //滑到最底部了
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                                SPUtils.putInt(context, "FristFull", 1);
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbClick(300, 490, 396, 535); //点击确定
                                String newXmlData = wxUtils.getXmlData();
                                if (newXmlData.contains("删除并退出")) {
                                    wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    wxUtils.adbClick(300, 490, 396, 535); //点击确定
                                }
                            }
                        }
                    }
                }
            }
        }
        String str_groupName_newFriends = SPUtils.getString(context, "groupName_newFriends", "");
        superXiuGaiBeiZhu_newFans2(arg0, mid_fourth, str_groupName_newFriends);
    }

    private void superXiuGaiBeiZhu_newFans2(String arg0, String mid_fourth, String wxQunFriendsName) {
        String arg1 = "YYZZZ" + arg0;
        if (arg1.length() >= 4) {
            String meName = "";
            int sex = 0;//0代表女。   1代表男   2代表性别未知
            DecimalFormat df = new DecimalFormat("000");
            String Type = "0000";
            boolean flag0 = true;
            while (flag0) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    flag0 = false;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                    flag0 = false;
                }
            }
            //            wxUtils.adbClick(306, 36, 378, 108);//搜索
            //            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            //            cm.setText(arg1);
            //            wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
            //            wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
            AdbBoundsUtils.searchAndPaste(context, arg1);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.adb("input keyevent 4");//返回
            xmlData = wxUtils.getXmlData();
            if (!xmlData.toLowerCase().contains(arg1.toLowerCase())) {
                ShowToast.show("没有对应的老粉丝账号", (Activity) context);
                return;
            }
            xmlData = wxUtils.getXmlData();
            int aa = 0;
            if (xmlData.contains("更多联系人")) {
                NodeUtils.clickNode("更多联系人", "com.tencent.mm:id/in");
            } else {
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("更多联系人")) {

                    NodeUtils.clickNode("更多联系人", "com.tencent.mm:id/in");
                } else {
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                }
            }
            Boolean Flag = true;
            while (Flag) {
                xmlData = wxUtils.getXmlData();
                nodeList = wxUtils.getNodeList(xmlData);
                int bb = 0;
                if (xmlData.contains(arg1)) {
                    for (int a = 0; a < nodeList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/kq")
                                && nodeBean.getText() != null && nodeBean.getText().toLowerCase().startsWith(arg1.toLowerCase()) && !meName.toLowerCase().contains(nodeBean.getText().toLowerCase())
                                ) {
                            String oldName = "";
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取  的坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                            bb = 1;
                            meName = meName + nodeBean.getText().toLowerCase();
                            oldName = nodeBean.getText();
                            wxUtils.adbClick(396, 36, 480, 108);//点击右上角头像
                            xmlData = wxUtils.getXmlData();
                            wxUtils.adbClick(21, 168, 105, 286); //点击左上角的人头像
                            xmlData = wxUtils.getXmlData();//重新获取页面数据
                            List<String> remarkList = wxUtils.getNodeList(xmlData);
                            if (xmlData.contains("女")) {
                                sex = 0;
                            } else if (xmlData.contains("男")) {
                                sex = 1;
                            } else {
                                sex = 2;
                            }
                            for (int r = 0; r < remarkList.size(); r++) {
                                nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                    //筛选出好友
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                    break;
                                }
                            }
                            //                            wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                            //                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                            AdbBoundsUtils.clearRemark();
                            if (wxQunFriendsName.contains(oldName)) {

                                Calendar calendar = Calendar.getInstance();
                                String year = (calendar.get(Calendar.YEAR) + "").substring(2, 4);
                                String month = calendar.get(Calendar.MONTH) + 1 + "";
                                if (month.length() == 1) {
                                    month = "0" + month;
                                }
                                String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
                                if (day.length() == 1) {
                                    day = "0" + day;
                                }
                                switch (sex) {//0代表女。   1代表男   2代表性别未知
                                    case 0:
                                        int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl" + arg0, 0);
                                        String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                        wxUtils.adb("input text " + arg1.replace("YY", "") + mid_fourth + "B" + wx_nume_number_new_girl + "_" + year + month + day);
                                        SPUtils.put(context, "wx_name_number_girl" + arg0, wx_name_number_girl + 1);
                                        break;
                                    case 1:
                                        int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy" + arg0, 0);
                                        String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                        wxUtils.adb("input text " + arg1.replace("YY", "") + mid_fourth + "A" + wx_nume_number_new_boy + "_" + year + month + day);
                                        SPUtils.put(context, "wx_name_number_boy" + arg0, wx_name_number_boy + 1);
                                        break;
                                    case 2:
                                        int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c" + arg0, 0);
                                        String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                        wxUtils.adb("input text " + arg1.replace("YY", "") + mid_fourth + "C" + wx_nume_number_c + "_" + year + month + day);
                                        SPUtils.put(context, "wx_name_number_c" + arg0, wx_name_number_c + 1);
                                        break;
                                }

                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            NodeUtils.clickNode("com.tencent.mm:id/hd");
                            wxUtils.adb("input keyevent 4");
                            wxUtils.adb("input keyevent 4");//返回
                            wxUtils.adb("input keyevent 4");//返回
                            int timeSleep = random.nextInt(3 - 1 + 1) + 1;
                            LogUtils.e("end=" + 3 + "__start=" + 1 + "___间隔随机数=" + timeSleep);
                            ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                            try {
                                Thread.sleep(timeSleep * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (bb == 0) {
                    String oldXml = xmlData;
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    if (oldXml.equals(xmlData)) {
                        ShowToast.show("滑到底部了", (Activity) context);
                        Flag = false;
                        continue;
                    }
                }
            }

        }
    }

    private void superXiuGaiBeiZhu_newFans1(String arg0, String arg1) {
        String groupName_newFriends = ""; //记录修改后名字合集
        backHome();
        wxUtils.adbWxClick(180, 840);
        String oldXmlData = "";
        String zzz = "YYZZZ" + arg0 + "0000";
        boolean bottom = false;//到了底部
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("000");
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        String meName = "";
        int kkk_GaiBeiZhu = 0;
        int xiuGaiNum = 0;

        if (arg1.contains("@")) {
            String[] str_num = arg1.split("@");
            int num_start = Integer.valueOf(str_num[0]);
            int num_end = Integer.valueOf(str_num[1]);
            xiuGaiNum = random.nextInt(num_end - num_start + 1) + num_start;
        } else {
            xiuGaiNum = 2000;
        }

        w:
        while (true) {
            while (true) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                    break w;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else {
                    break;
                }
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            a:
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != ""
                        && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手")
                        && !nodeBean.getContentdesc().startsWith("YY") && !nodeBean.getContentdesc().startsWith("ZZZ")
                        && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc())) {
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        meName = nodeBean.getContentdesc();
                        continue;
                    }
                    StatisticsWxFriends(xmlData);//统计新增好友的信息
                    List<String> meWxIdList = wxUtils.getNodeList(xmlData);
                    if (xmlData.contains("女")) {
                        sex = 0;
                    } else if (xmlData.contains("男")) {
                        sex = 1;
                    } else {
                        sex = 2;
                    }
                    //                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    //                    List<String> remarkList = wxUtils.getNodeList(xmlData);
                    //                    for (int r = 0; r < remarkList.size(); r++) {
                    //                        nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                    //                        if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                    //                            //筛选出好友
                    //                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                    //                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                    //                            break;
                    //                        }
                    //                    }
                    NodeUtils.clickNode("com.tencent.mm:id/anw"); //修改备注
                    xmlData = wxUtils.getXmlData();

                    if (xmlData.contains("备注信息") && xmlData.contains("完成")) {

                    } else {
                        continue w;
                    }
                    //                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    //                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                    AdbBoundsUtils.clearRemark();
                    Calendar calendar = Calendar.getInstance();
                    String year = (calendar.get(Calendar.YEAR) + "").substring(2, 4);
                    String month = calendar.get(Calendar.MONTH) + 1 + "";
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    groupName_newFriends = SPUtils.getString(context, "groupName_newFriends", "");
                    switch (sex) {//0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl" + arg0, 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                            wxUtils.adb("input text " + zzz + "B" + wx_nume_number_new_girl + "_" + year + month + day);
                            SPUtils.put(context, "wx_name_number_girl" + arg0, wx_name_number_girl + 1);
                            groupName_newFriends = groupName_newFriends + zzz + "B" + wx_nume_number_new_girl + "_" + year + month + day;
                            SPUtils.putString(context, "groupName_newFriends", groupName_newFriends);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy" + arg0, 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                            wxUtils.adb("input text " + zzz + "A" + wx_nume_number_new_boy + "_" + year + month + day);
                            SPUtils.put(context, "wx_name_number_boy" + arg0, wx_name_number_boy + 1);
                            groupName_newFriends = groupName_newFriends + zzz + "A" + wx_nume_number_new_boy + "_" + year + month + day;
                            SPUtils.putString(context, "groupName_newFriends", groupName_newFriends);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c" + arg0, 0);
                            String wx_nume_number_c = df.format(wx_name_number_c + 1);
                            wxUtils.adb("input text " + zzz + "C" + wx_nume_number_c + "_" + year + month + day);
                            SPUtils.put(context, "wx_name_number_c" + arg0, wx_name_number_c + 1);
                            groupName_newFriends = groupName_newFriends + zzz + "C" + wx_nume_number_c + "_" + year + month + day;
                            SPUtils.putString(context, "groupName_newFriends", groupName_newFriends);
                            break;
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    NodeUtils.clickNode("完成", "com.tencent.mm:id/hd");
                    wxUtils.adb("input keyevent 4");
                    kkk_GaiBeiZhu++;
                    if (kkk_GaiBeiZhu >= xiuGaiNum) {
                        return;
                    }
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                        wxUtils.adb("input keyevent 4");
                    }

                    //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_s())) {
                        start = 3;
                    } else {
                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_e())) {
                        end = 6;
                    } else {
                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_e());
                    }
                    int timeSleep = random.nextInt(end - start + 1) + start;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("发现")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                        && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("YY")
                        && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())) {
                    continue w;
                } else if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                        && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && (nodeBean.getContentdesc().startsWith("YY")
                        || nodeBean.getContentdesc().startsWith("ZZZ") || nodeBean.getContentdesc().startsWith("zzz")) && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())
                        ) {
                    zzzNum++;
                }
            }
            int aaaaa = 0;
            if (!bottom) {
                if (zzzNum >= 8) {
                    //                    wxUtils.adbDimensClick(context, 460, 768,460, 768);
                    wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                    String xmlData2 = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData2);
                    for (int b = 0; b < nodeList.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                        if ((nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                            String xmlData3 = wxUtils.getXmlData();
                            //                            meName =meName+nodeBean.getContentdesc();
                            if (xmlData3.contains("备注和标签")) {
                                aaaaa++;
                            } else {
                                meName = meName + nodeBean.getContentdesc();
                            }
                            wxUtils.adb("input keyevent 4");//返回
                        }
                    }
                    if (aaaaa == 0) {
                        //说明全是ZZZ或者zzz开头的了
                        wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                        wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                        xmlData = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData);
                        int ccc = 0;
                        for (int b = 0; b < nodeList.size(); b++) {
                            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                            if ((nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                    && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                                ccc++;
                            }
                        }
                        if (ccc == 0) {
                            ShowToast.show("修改备注完成", (Activity) context);
                            break w;
                        }
                    }
                } else {
                    oldXmlData = wxUtils.getXmlData();
                    wxUtils.adbUpSlide(context);//向上滑动
                }

                //            endData = xmlData;
                xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
                if (oldXmlData.equals(xmlData)) {
                    ShowToast.show("修改备注完成", (Activity) context);
                    break w;
                }
                nodeList = wxUtils.getNodeList(xmlData);
                int bbb = 0;
                for (int b = 0; b < nodeList.size(); b++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                    if ((nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                            && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                        bbb++;
                    }
                }
                if (bbb == 0) {
                    wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                    wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                }
            }
        }
    }

    //  只拉老粉丝
    private void OldFansToQun(String arg0, String arg1, String arg2) {
        String oldFriendsName = ""; //被修改过备注的名字
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        String personNum = SPUtils.getString(context, "QunPersonNum", "0");
        int personNum_int = Integer.valueOf(personNum);
        String wxQunFriendsName = "";
        String meName = "";
        DecimalFormat df = new DecimalFormat("000");
        int sex = 0; //0 代表女。   1代表男   2代表性别未知
        String fanId = "";
        String mid_fouth = ""; //第4位
        String first_fouth = "";//前四位
        if (arg0 != null && arg0.length() >= 4) {
            mid_fouth = arg0.substring(3, 4);
            first_fouth = arg0.substring(0, 4);
        }
        fanId = arg0;
        boolean flag_gaibeizhu = true;
        while (flag_gaibeizhu) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag_gaibeizhu = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag_gaibeizhu = false;
            }
        }

        wxUtils.adbClick(306, 36, 378, 108);//搜索
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        cm.setText(fanId);
        wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
        wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adb("input keyevent 4");//返回
        xmlData = wxUtils.getXmlData();
        if (!xmlData.toLowerCase().contains(arg0.toLowerCase())) {
            ShowToast.show("没有对应的老粉丝账号", (Activity) context);
            return;
        }
        xmlData = wxUtils.getXmlData();
        int aa = 0;
        if (xmlData.contains("更多联系人")) {
            aa = 1;
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                        && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                        ) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                    break;
                }
            }
        } else {
            wxUtils.adbUpSlide(context);
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("更多联系人")) {
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                            && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                            ) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                        break;
                    }
                }
            } else {
                wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
            }
        }
        Boolean Flag_GaiBeiZhu = true;
        int kkk_GaiBeiZhu = 0;
        int xiuGaiNum = 0;
        if (arg1.contains("@")) {
            String[] str_num = arg1.split("@");
            int num_start = Integer.valueOf(str_num[0]);
            int num_end = Integer.valueOf(str_num[1]);
            xiuGaiNum = random.nextInt(num_end - num_start + 1) + num_start;
        } else {
            xiuGaiNum = 1000;
        }
        while (Flag_GaiBeiZhu) {
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            int bb = 0;
            if (xmlData.contains(fanId)) {
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    /**
                     * 在没有更多联系人的情况下 点击最上面的联系人列表
                     */
                    if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/kq")
                            && nodeBean.getText() != null && nodeBean.getText().toLowerCase().startsWith(fanId.toLowerCase()) && !meName.contains(nodeBean.getText().toLowerCase())
                            ) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取  的坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                        bb = 1;
                        meName = meName + nodeBean.getText().toLowerCase();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbClick(396, 36, 480, 108);//点击右上角头像
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbClick(21, 168, 105, 286); //点击左上角的人头像
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        List<String> remarkList = wxUtils.getNodeList(xmlData);
                        if (xmlData.contains("女")) {
                            sex = 0;
                        } else if (xmlData.contains("男")) {
                            sex = 1;
                        } else {
                            sex = 2;
                        }
                        for (int r = 0; r < remarkList.size(); r++) {
                            nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                            if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                //筛选出好友
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                break;
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        String friendsName = "";
                        List<String> remarkList2 = wxUtils.getNodeList(xmlData);
                        for (int r = 0; r < remarkList2.size(); r++) {
                            nodeBean = wxUtils.getNodeXmlBean(remarkList2.get(r)).getNode();
                            if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/aoz"))) {
                                //筛选出好友
                                friendsName = nodeBean.getText();
                                break;
                            }
                        }

                        Calendar calendar = Calendar.getInstance();
                        String year = (calendar.get(Calendar.YEAR) + "").substring(2, 4);
                        String month = calendar.get(Calendar.MONTH) + 1 + "";
                        if (month.length() == 1) {
                            month = "0" + month;
                        }
                        String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
                        if (day.length() == 1) {
                            day = "0" + day;
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                        String arg3 = "";

                        if (friendsName.length() == 19) {
                            arg3 = "YY" + friendsName;
                            oldFriendsName = oldFriendsName + arg3;
                            wxUtils.adb("input text " + arg3);
                        } else {
                            switch (sex) {//0代表女。   1代表男   2代表性别未知
                                case 0:
                                    int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl" + mid_fouth, 0);
                                    String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                    wxUtils.adb("input text " + "YY" + first_fouth + "0000" + "B" + wx_nume_number_new_girl + "_" + year + month + day);
                                    oldFriendsName = oldFriendsName + "YY" + first_fouth + "0000" + "B" + wx_nume_number_new_girl + "_" + year + month + day;
                                    SPUtils.put(context, "wx_name_number_girl" + mid_fouth, wx_name_number_girl + 1);
                                    break;
                                case 1:
                                    int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy" + mid_fouth, 0);
                                    String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                    wxUtils.adb("input text " + "YY" + first_fouth + "0000" + "A" + wx_nume_number_new_boy + "_" + year + month + day);
                                    oldFriendsName = oldFriendsName + "YY" + first_fouth + "0000" + "A" + wx_nume_number_new_boy + "_" + year + month + day;
                                    SPUtils.put(context, "wx_name_number_boy" + mid_fouth, wx_name_number_boy + 1);
                                    break;
                                case 2:
                                    int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c" + mid_fouth, 0);
                                    String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                    wxUtils.adb("input text " + "YY" + first_fouth + "0000" + "A" + wx_nume_number_c + "_" + year + month + day);
                                    oldFriendsName = oldFriendsName + "YY" + first_fouth + "0000" + "A" + wx_nume_number_c + "_" + year + month + day;
                                    SPUtils.put(context, "wx_name_number_c" + mid_fouth, wx_name_number_c + 1);
                                    break;
                            }
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                        wxUtils.adb("input keyevent 4");//返回
                        wxUtils.adb("input keyevent 4");//返回
                        wxUtils.adb("input keyevent 4");//返回
                        kkk_GaiBeiZhu++;
                        if (kkk_GaiBeiZhu >= xiuGaiNum) {
                            Flag_GaiBeiZhu = false;
                            break;
                        }
                        //                        int timeSleep = random.nextInt(5 - 3 + 1) + 3;
                        //                        LogUtils.e("end=" + 10 + "__start=" + 5 + "___间隔随机数=" + timeSleep);
                        //                        ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                        try {
                            Thread.sleep(1 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (bb == 0) {
                String oldXml = xmlData;
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (oldXml.equals(xmlData)) {
                    ShowToast.show("滑到底部了", (Activity) context);
                    Flag_GaiBeiZhu = false;
                    continue;
                }
            }
        }
        //  准备拉群
        backHome();
        wxUtils.openWx((Activity) context);//打开微信
        SPUtils.putInt((Activity) context, "NewFriendsCount", 0);
        SPUtils.putInt(context, "TimeOver", 1);
        wxUtils.openWx((Activity) context);//打开微信
        wxUtils.adbClick(153, 822, 207, 847);
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
        wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
        int newfriendsaccount = 0;
        xmlData = wxUtils.getXmlData();
        if (!xmlData.toLowerCase().contains(("YY" + fanId).toLowerCase())) {
            ShowToast.show("没有新好友!", (Activity) context);
        } else {
            ShowToast.show("有新好友!", (Activity) context);
            newfriendsaccount = 1;
        }
        boolean flag_0 = true;
        while (flag_0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag_0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(282, 822, 318, 847); //点击发现
                flag_0 = false;
            }
        }
        boolean QunIsFull = true;
        SPUtils.putString(context, "groupName", "");
        String batch = SPUtils.getString(context, "QunBatch", "00");
        while (QunIsFull && (newfriendsaccount != 0)) {
            ShowToast.show("等待10秒获取群名片", (Activity) context);
            String url = URLS.wxNewFriendsToQun();
            HashMap<String, String> params = new HashMap<>();
            params.put("number", personNum_int + "");

            boolean netFlag = true;
            int number = 0;
            while (netFlag) {
                try {
                    number++;
                    Thread.sleep(20000);
                    if (number > 30) {
                        oldFans_startAlterName(fanId, arg2, wxQunFriendsName);
                        return;
                    }
                    Response execute = OkHttpUtils.get().params(params).url(url).addParams("batch", batch).build().execute();
                    if (execute.code() == 200) {
                        String string = execute.body().string();
                        WxNewFriendsToQunBean wxNewFriendsToQunBean = GsonUtil.parseJsonWithGson(string, WxNewFriendsToQunBean.class);
                        if(wxNewFriendsToQunBean !=null ){
                            pic_id = Integer.parseInt(wxNewFriendsToQunBean.getData().getId());//获取到图片id
                            if (!TextUtils.isEmpty(wxNewFriendsToQunBean.getData().getQr_code_address()))
                                if (downFlockImg(wxNewFriendsToQunBean.getData().getQr_code_address(), 0))
                                    netFlag = false;
                            SPUtils.putString(context, "groupName", wxNewFriendsToQunBean.getData().getWx_group_name());
                            SPUtils.putInt(context, "picDownloadSuccess", 1);
                        }
                    } else {
                        ShowToast.show("网络异常未获取到二维码图片！", (Activity) context);
                    }
                } catch (Exception e) {
                    ShowToast.show("网络异常未获取到二维码图片！", (Activity) context);
                    e.printStackTrace();
                }

            }
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            backHome();
            // TODO: 2018/4/23/023 跳进去
            wxUtils.adbClick(282, 822, 318, 847); //点击发现
            wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.adbClick(400, 550, 400, 550);
            wxUtils.adbClick(400, 550, 400, 550);//连续点击两次个人头像
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().equals("发消息")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  发消息
                    break;
                }
            }
            wxUtils.adbClick(402, 782, 474, 854);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("相册") && !xmlData.contains("拍摄")) {
                wxUtils.adbClick(402, 782, 474, 854);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            wxUtils.adbClick(61, 612, 95, 635);
            wxUtils.adbClick(50, 820, 50, 820);//点击左下角
            boolean ccc = true;
            boolean ddd = true;

            while (ccc) {
                String xmlData_picture = wxUtils.getXmlData();
                if (xmlData_picture.contains("com.tencent.mm:id/d1r") && xmlData_picture.contains("ykimages")) {
                    List<String> pictureList = wxUtils.getNodeList(xmlData_picture);
                    for (int c = 0; c < pictureList.size(); c++) {
                        NodeXmlBean.NodeBean pictureBean = wxUtils.getNodeXmlBean(pictureList.get(c)).getNode();
                        if (pictureBean != null && pictureBean.getResourceid() != null && "com.tencent.mm:id/d1r".equals(pictureBean.getResourceid())
                                && pictureBean.getText() != null && pictureBean.getText().equals("ykimages")) {
                            listXY = wxUtils.getXY(pictureBean.getBounds());//获取坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击ykimages 文件夹
                            ccc = false;
                            break;
                        }
                    }
                } else {
                    wxUtils.adbUpSlide(context);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();
                    if (!xmlData_picture.contains("com.tencent.mm:id/d1r") || !xmlData_picture.contains("ykimages")) {
                        ccc = false;
                        ddd = false;
                    }
                }

            }
            if (!ddd) {
                ShowToast.show("未下载到图片，重新开始下载", (Activity) context);
                continue;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("图片和视频")) {
                wxUtils.adbClick(78, 119, 108, 149);//选中图片
                wxUtils.adbClick(378, 49, 468, 94);//点击发送
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                continue;
            }
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("com.tencent.mm:id/aec")) {
                continue;
            }
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size() - 1; a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/aec"))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 群名片图片 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击   群名片图片
                    break;
                }
            }
            wxUtils.adb("input swipe " + 240 + " " + 400 + " " + 200 + " " + 400 + " " + 4000);  //长按EdiText
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size() - 1; a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean != null && nodeBean.getText() != null && (nodeBean.getText().equals("识别图中二维码"))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 群名片图片 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击识别图中二维码
                    break;
                }
            }
            if (!xmlData.contains("识别图中二维码")) {
                List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                String uid = SPUtils.getString(context, "uid", "0000");
                QunMessageBean messageBean = new QunMessageBean(0 + "", pic_id + "", uid, "" + personNum_int);
                mQunMessageBeanList.add(messageBean);
                String str = new Gson().toJson(mQunMessageBeanList);
                LogUtils.d("JSON" + str.toString());
                //                sendWxQunMessage(str);
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("json", str);
                try {
                    String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            wxUtils.adbClick(90, 481, 258, 514);
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //无法确定具体坐标，大概在四个范围
            wxUtils.adbClick(240, 600, 240, 600);//点击加入群聊
            wxUtils.adbClick(240, 550, 240, 550);//点击加入群聊
            wxUtils.adbClick(240, 500, 240, 500);//点击加入群聊
            wxUtils.adbClick(240, 650, 240, 650);//点击加入群聊
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Boolean Flag = true;
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("(") || !xmlData.contains(")") || !xmlData.contains("加入群聊")) {
                LogUtils.d("二维码群图片没下载下来");
                List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                String uid = SPUtils.getString(context, "uid", "0000");
                QunMessageBean messageBean = new QunMessageBean(0 + "", pic_id + "", uid, "" + personNum_int);
                mQunMessageBeanList.add(messageBean);
                String str = new Gson().toJson(mQunMessageBeanList);
                LogUtils.d("JSON" + str.toString());
                //                sendWxQunMessage(str);
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("json", str);
                try {
                    String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Flag = false;
                continue;
            }
            int kkk = 0;
            while (Flag) {
                wxUtils.adbClick(408, 36, 480, 108);//点击右上角的人头像
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("(") || !xmlData.contains(")") || !xmlData.contains("聊天信息")) {
                    LogUtils.d("二维码群图片没下载下来");
                    List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                    String uid = SPUtils.getString(context, "uid", "0000");
                    QunMessageBean messageBean = new QunMessageBean(0 + "", pic_id + "", uid, "" + personNum_int);
                    mQunMessageBeanList.add(messageBean);
                    String str = new Gson().toJson(mQunMessageBeanList);
                    LogUtils.d("JSON" + str.toString());
                    //                    sendWxQunMessage(str);
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("json", str);
                    try {
                        String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Flag = false;
                    continue;
                }
                int i = 0;
                Boolean flag1 = true;
                while (flag1) {
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size() - 1; a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean != null && nodeBean.getText() != null && nodeBean.getText().contains("聊天信息")) {
                            String qunPersonNum = nodeBean.getText();
                            String Num = qunPersonNum.substring(5, qunPersonNum.length() - 1);
                            i = Integer.parseInt(Num);//获取到目前群内成员的人数
                            kkk++;
                            break;
                        }
                    }
                    if (i < personNum_int) {
                        for (int a = 0; a < nodeList.size() - 1; a++) {//寻找 添加成员的按钮
                            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                            if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/d05"))
                                    && nodeBean.getContentdesc() != null && nodeBean.getContentdesc().equals("添加成员")) {
                                flag1 = false;
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取 添加成员 按钮 的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  添加成员 开始拉人
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbWxClick(465, 790);  //点击右侧Y
                                break;
                            }
                        }
                        if (flag1 == true) {//如果页面没有添加成员 “+”存在
                            wxUtils.adbUpSlide(context);
                        }
                    } else if (i >= personNum_int) {
                        flag1 = false;
                    }
                }
                if (i >= personNum_int) {
                    QunIsFull = true;
                    Flag = false; //说明已经满40个人了
                    //                  wxUtils.adbUpSlide(context);
                    Boolean qunNameIsVisibled = false;
                    while (!qunNameIsVisibled) {
                        String newXmlData = wxUtils.getXmlData();
                        if (newXmlData.contains("群聊名称") && newXmlData.contains("群二维码")) {
                            qunNameIsVisibled = true;
                        } else {
                            wxUtils.adbUpSlide(context);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    wxUtils.screenShot(); //截图
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbUpSlide(context);
                    wxUtils.adbUpSlide(context);
                    wxUtils.adbUpSlide(context);
                    wxUtils.adbUpSlide(context); //滑到最底部了
                    List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                    String uid = SPUtils.getString(context, "uid", "0000");
                    QunMessageBean messageBean = new QunMessageBean(i - 1 + "", pic_id + "", uid, "" + personNum_int);
                    mQunMessageBeanList.add(messageBean);
                    String str = new Gson().toJson(mQunMessageBeanList);
                    LogUtils.d("JSON" + str.toString());
                    groupPeopleNumber = i - 1;
                    //                    sendWxQunMessage(str);
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("json", str);
                    try {
                        String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    NodeUtils.clickNode("删除并退出", "android:id/title");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String newXmlData = wxUtils.getXmlData();
                    if (newXmlData.contains("删除并退出后")) {
                        NodeUtils.clickNode("确定", "com.tencent.mm:id/all");
                    } else {
                        NodeUtils.clickNode("删除并退出", "android:id/title");
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        NodeUtils.clickNode("确定", "com.tencent.mm:id/all");
                    }
                    sendQunSrceenShot();
                    backHome();
                }
                boolean flag = true;
                if (i >= personNum_int) {// 满40个人后就不用拉人了
                    flag = false;
                }
                int aaaa = 0;
                Boolean flag3 = false;
                while (flag) {
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size() - 1; a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode();
                        if (nodeBean != null && nodeBean2 != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/kq")) && nodeBean2.isEnabled() == true
                                && nodeBean.getText() != null && !wxQunFriendsName.toLowerCase().contains(nodeBean.getText().toLowerCase()) && nodeBean2.isChecked() == false) {
                            if (nodeBean.getText().toLowerCase().startsWith(("YY" + fanId).toLowerCase())) {
                                String wxName = nodeBean.getText().toLowerCase();
                                aaaa++;
                                wxQunFriendsName = wxQunFriendsName + wxName;
                                listXY = wxUtils.getXY(nodeBean2.getBounds());// 获取选中框的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击选中框
                                if (aaaa >= personNum_int - i) {
                                    flag = false;
                                    wxUtils.adbClick(368, 49, 468, 94);//点击确定
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            } else if (nodeBean.getText().startsWith("ZZZ")) {
                                flag3 = true;//已经检测到AAZZZ了
                                break;
                            }

                        }
                    }
                    if (flag == true && aaaa < personNum_int - i) {
                        wxUtils.adbUpSlide(context);//向上滑动
                        String xmlData2 = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData2);

                        if (xmlData2.equals(xmlData) || (flag3 == true)) {
                            QunIsFull = true;
                            flag = false;
                            // 已经全部拉完了
                            if (aaaa == 0) {
                                wxUtils.adb("input keyevent 4");//返回
                            } else {
                                wxUtils.adbClick(378, 49, 468, 94);//点击确定
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String xmlData3 = wxUtils.getXmlData();
                            if (xmlData3.contains("确定")) {
                                nodeList = wxUtils.getNodeList(xmlData3);
                                for (int b = nodeList.size() - 1; b > 0; b--) {
                                    NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                    if (nodeBean3 != null && nodeBean3.getText() != null && nodeBean3.getText().equals("确定")) {
                                        listXY = wxUtils.getXY(nodeBean3.getBounds());
                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击确定
                                        break;
                                    }
                                }
                            }
                            xmlData = wxUtils.getXmlData();
                            nodeList = wxUtils.getNodeList(xmlData);
                            for (int a = 0; a < nodeList.size() - 1; a++) {
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                                if (nodeBean != null && nodeBean.getText() != null && nodeBean.getText().contains("聊天信息")) {
                                    String qunPersonNum = nodeBean.getText();
                                    String Num = qunPersonNum.substring(5, qunPersonNum.length() - 1);
                                    i = Integer.parseInt(Num);//获取到目前群内成员的人数
                                    Log.d("群内人数", "群内人数:" + i);
                                    // 将群 人数上传到服务器，再退出该群
                                    List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                                    String uid = SPUtils.getString(context, "uid", "0000");
                                    //                                    String  personNum =        SPUtils.getString(context,"QunPersonNum","0");
                                    QunMessageBean messageBean = new QunMessageBean((i - 1) + "", pic_id + "", uid + "", personNum_int + "");
                                    mQunMessageBeanList.add(messageBean);
                                    String str = new Gson().toJson(mQunMessageBeanList);
                                    LogUtils.d("JSON" + str.toString());
                                    groupPeopleNumber = i - 1;
                                    //                                    sendWxQunMessage(str);
                                    Map<String, String> messageMap = new HashMap<>();
                                    messageMap.put("json", str);
                                    try {
                                        String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                            if (i <= personNum_int - 1) {
                                QunIsFull = false;// 群没有满，就不用再申请二维码了
                                Flag = false;
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context); //滑到最底部了
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                                SPUtils.putInt(context, "FristFull", 1);
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbClick(300, 490, 396, 535); //点击确定
                                String newXmlData = wxUtils.getXmlData();
                                if (newXmlData.contains("删除并退出")) {
                                    wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    wxUtils.adbClick(300, 490, 396, 535); //点击确定
                                }
                            }
                        } else {

                        }
                    }
                }
            }
        }
        oldFans_startAlterName(fanId, arg2, wxQunFriendsName);
    }

    private void oldFans_startAlterName(String fanId, String groupType, String friendNameStr) {
        backHome();
        wxUtils.adbClick(153, 822, 207, 847); //点击通讯录
        wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
        wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("YY")) {
            return;
        }
        DecimalFormat df = new DecimalFormat("000");
        String meName = "";
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        boolean Flag = true;
        int bbb = 0;
        while (Flag) {
            int aaa = 0;
            backHome();
            wxUtils.adbClick(153, 822, 207, 847); //点击通讯录
            wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
            wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
            xmlData = wxUtils.getXmlData();
            //            if (!xmlData.contains("YY")) {
            //                Flag = false;
            //                continue;
            //            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_"))
                        && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信")
                        && !nodeBean.getContentdesc().equals("文件传输助手") && nodeBean.getContentdesc().startsWith("YY") && !meName.equals(nodeBean.getContentdesc().toLowerCase())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友
                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        meName = meName + nodeBean.getContentdesc().toLowerCase();
                        continue;
                    }
                    if (xmlData.contains("女")) {
                        sex = 0;
                    } else if (xmlData.contains("男")) {
                        sex = 1;
                    } else {
                        sex = 2;
                    }
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    List<String> remarkList = wxUtils.getNodeList(xmlData);
                    for (int r = 0; r < remarkList.size(); r++) {
                        nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                        if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                            //筛选出好友
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                            aaa++;
                            break;
                        }
                    }
                    xmlData = wxUtils.getXmlData();
                    String friendsName = "";
                    List<String> remarkList2 = wxUtils.getNodeList(xmlData);
                    for (int r = 0; r < remarkList2.size(); r++) {
                        nodeBean = wxUtils.getNodeXmlBean(remarkList2.get(r)).getNode();
                        if (nodeBean != null && nodeBean.getText() != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/aoz") && nodeBean.getText().startsWith("YY")) {
                            friendsName = nodeBean.getText().replace("YY", "");
                            break;
                        }
                    }
                    if (friendsName != null && friendsName.length() >= 19) {
                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                        if (friendNameStr.toLowerCase().contains(friendsName.toLowerCase()) && friendsName.toLowerCase().contains(fanId.toLowerCase())) {
                            String str_1 = friendsName.substring(0, 4);  // 截取前4位
                            String str_2 = friendsName.substring(4, 8);  // 截取中间5-8
                            String str_3 = friendsName.substring(8, 19); // 截取最后的那几位
                            int aaaaa = 0;
                            if (groupType.equals("1")) {
                                aaaaa = Integer.valueOf(str_2) + 1000;
                            } else if (groupType.equals("2")) {
                                aaaaa = Integer.valueOf(str_2) + 100;
                            } else if (groupType.equals("3")) {
                                aaaaa = Integer.valueOf(str_2) + 10;
                            } else {
                                aaaaa = Integer.valueOf(str_2) + 1;
                            }
                            String bbbb = aaaaa + "";
                            if (bbbb.length() == 1) {
                                bbbb = "000" + bbbb;
                            } else if (bbbb.length() == 2) {
                                bbbb = "00" + bbbb;
                            } else if (bbbb.length() == 3) {
                                bbbb = "0" + bbbb;
                            }
                            wxUtils.adb("input text " + str_1 + bbbb + str_3);
                        } else {
                            wxUtils.adb("input text " + friendsName);
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    } else {
                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                        wxUtils.adb("input text " + friendsName);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                        wxUtils.adb("input keyevent 4");
                    }
                    //  LogUtils.d(nodeList.get(a));
                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                        wxUtils.adb("input keyevent 4");
                    }

                    break;
                }

            }
            if (aaa == 0) {
                wxUtils.killWx();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.openWx((Activity) context);
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backHome();
                wxUtils.adbClick(153, 822, 207, 847); //点击通讯录
                wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
                wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("YY")) {
                    Flag = false;
                }
            }
        }
    }

    private Boolean getIsAccountIsOk() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("紧急冻结") && xmlData.contains("找回密码") && xmlData.contains("微信安全中心")) {
            operationView.isLoginWx(false);
            String currentLocation = SPUtils.getString(context, "WxAccountLocation", "0");
            wxUtils.adb("input keyevent 4");//返回
            if (currentLocation.equals("1")) {
                wxUtils.adbClick(288, 457, 384, 553);

            } else {
                wxUtils.adbClick(96, 457, 192, 553);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    private Boolean getIsAccountIsOk2() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("紧急冻结") && xmlData.contains("找回密码") && xmlData.contains("微信安全中心")) {
            wxUtils.adb("input keyevent 4");//返回
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 0;
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size() - 1; a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null) {
                    Log.d("zhangshuai", nodeBean.getResourceid());
                }
                if (nodeBean != null && nodeBean.getText() != null) {
                    Log.d("zhangshuai", nodeBean.getText());
                }
                if (nodeBean != null) {
                    if (nodeBean.getResourceid() != null) {
                        if (nodeBean.getText() != null) {
                            if (nodeBean.getText().contains("切换帐号")) {
                                if (nodeBean.getResourceid().equals("com.tencent.mm:id/d5s")) {
                                    i = 1;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (i == 1) {
                operationView.isLoginWx(true);
            } else {
                operationView.isLoginWx(false);
            }
            String currentLocation = SPUtils.getString(context, "WxAccountLocation", "0");

            if (currentLocation.equals("1")) {
                wxUtils.adbClick(96, 457, 192, 553);
            } else {
                wxUtils.adbClick(288, 457, 384, 553);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    private void ChatC(String arg0) {
        ShowToast.show("语音互聊或者视频互聊", (Activity) context);
        try {
            Thread.sleep(240000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // B 账号 语音聊天
    private void ChatB(String arg0) {
        String wxName = "";
        String uid = SPUtils.getString(context, "uid", "0000");
        String wxAccount = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
        String accountLocation = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
        // 将文本内容放到系统剪贴板里。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        cm.setText(arg0);
        String meName = "";
        backHome();
        wxUtils.adbDimensClick(context, R.dimen.x204, R.dimen.y17, R.dimen.x252, R.dimen.y51);//搜索
        int x = context.getResources().getDimensionPixelSize(R.dimen.x167);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y33);//EdiText
        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
        wxUtils.adbDimensClick(context, R.dimen.x118, R.dimen.y85, R.dimen.x118, R.dimen.y85);//粘贴
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y85, R.dimen.x320, R.dimen.y130);//文件传输助手
        wxUtils.adb("input keyevent 4");//返回

        xmlData = wxUtils.getXmlData();
        List<String> chaZhaoXmlList = wxUtils.getNodeList(xmlData);
        for (int k = 0; k < chaZhaoXmlList.size(); k++) {
            NodeXmlBean.NodeBean nodeBeanChaZhao = wxUtils.getNodeXmlBean(chaZhaoXmlList.get(k)).getNode();
            if (nodeBeanChaZhao != null && nodeBeanChaZhao.getText() != null && nodeBeanChaZhao.getText().contains(arg0) && nodeBeanChaZhao.getResourceid() != null && nodeBeanChaZhao.getResourceid().equals("com.tencent.mm:id/ql")) {
                listXY = wxUtils.getXY(nodeBeanChaZhao.getBounds());//
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击 指定联系人坐标
                int x1 = context.getResources().getDimensionPixelSize(R.dimen.x136);
                int y1 = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                Random random = new Random();
                String[] str_news =
                        {"火锅不好吃呀，太辣了，我们去吃烧烤",
                                "周末去春熙路买衣服，好不好？",
                                "什么？？马蓉又出轨了？？",
                                "没时间去呀， 忙着约妹子",
                                "天王盖地虎， 提莫一米五",
                                "拼多多假货真多， 改名假多多算了",
                                "买买买，这不买还是中国人吗？",
                                "上海这边的夜景真不错，晚上出来散步真是太爽了！",
                                "最近好忙的，忙得饭都没时间吃",
                                "摩拜单车被收购关我屁事，我只关心我每个月能剩多少钱",
                                "无论刮风还是下雨，哥一直穿短袖"};
                for (int i = 0; i < 4; i++) {
                    int tttt = random.nextInt(3);// 0为文字  1为图片  2为语音
                    int ttt2 = random.nextInt(11);
                    int ttt_0 = random.nextInt(3);
                    int ttt_1 = random.nextInt(3);
                    String xmlData5 = wxUtils.getXmlData();
                    List<String> nodeList5 = wxUtils.getNodeList(xmlData5);
                    for (int a = 0; a < nodeList5.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean5 = wxUtils.getNodeXmlBean(nodeList5.get(a)).getNode();
                        if (nodeBean5 != null && nodeBean5.getResourceid() != null && nodeBean5.getResourceid().equals("com.tencent.mm:id/aag")) {
                            listXY = wxUtils.getXY(nodeBean5.getBounds());//  点击+ 号
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                            break;
                        }

                    }
                    if (ttt_0 == 0 && ttt_1 == 0) {
                        //发语音
                        wxUtils.adbWxClick(49, 450);//点击切换到语音
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //录制时间
                        int start;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRecord_time_s())) {
                            start = 5;
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
                        wxUtils.adb("input swipe " + x1 + " " + y1 + " " + x1 + " " + y1 + " " + timeSleep * 1000);  //长按EdiText
                        ShowToast.show("休息90秒", (Activity) context);
                        try {
                            Thread.sleep(90000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else if (ttt_0 == 1 && ttt_1 == 1) {
                        int ttt_pic = random.nextInt(3);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        wxUtils.adbWxClick(78, 550);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (ttt_pic == 1) {
                            wxUtils.adbClick(78, 119, 108, 149);//选择图片
                        } else if (ttt_pic == 2) {
                            wxUtils.adbClick(198, 119, 228, 149);//选择图片
                        } else if (ttt_pic == 3) {
                            wxUtils.adbClick(318, 119, 348, 149);//选择图片
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                        ShowToast.show("休息90秒", (Activity) context);
                        try {
                            Thread.sleep(90000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else { // 发文字
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        cm.setText(str_news[ttt2]);
                        wxUtils.adb("input swipe " + 185 + " " + 450 + " " + 185 + " " + 450 + " " + 1000);  //长按EdiText
                        //                            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                        wxUtils.adbWxClick(105, 395);
                        //                            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String xmlData6 = wxUtils.getXmlData();
                        List<String> nodeList6 = wxUtils.getNodeList(xmlData6);
                        for (int a = 0; a < nodeList6.size(); a++) {
                            NodeXmlBean.NodeBean nodeBean6 = wxUtils.getNodeXmlBean(nodeList6.get(a)).getNode();
                            if (nodeBean6 != null && nodeBean6.getResourceid() != null && nodeBean6.getResourceid().equals("com.tencent.mm:id/aah") && nodeBean6.getText() != null && nodeBean6.getText().equals("发送")) {
                                listXY = wxUtils.getXY(nodeBean6.getBounds());//  点击发送
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                break;
                            }
                        }
                        ShowToast.show("休息90秒", (Activity) context);
                        try {
                            Thread.sleep(90000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ShowToast.show("消息发送完毕", (Activity) context);
                try {
                    Response data = OkHttpUtils.get().url(URLS.url + "home/ApiAndroid/eachChatUnlock").addParams("account", wxAccount).build().execute();
                    if (data.code() == 200) {
                        ShowToast.show("解锁成功", (Activity) context);
                    } else {
                        ShowToast.show("解锁失败", (Activity) context);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                backHome();
                break;
            }
        }
    }


    //超级自定义修改备注

    private void superXiuGaiBeiZhu(String arg0, String arg1) {
        //   例子 由 ZZZ1  改成 ZZZ2
        //   修改 YYY0
        if (arg0 != null && arg0.length() == 2) {
            String meName = "";
            int sex = 0;//0代表女。   1代表男   2代表性别未知
            DecimalFormat df = new DecimalFormat("000");
            String Type = "0000";
            boolean flag0 = true;
            while (flag0) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    flag0 = false;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                    flag0 = false;
                }
            }
            wxUtils.adbClick(306, 36, 378, 108);//搜索
            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            cm.setText(arg0);
            //        wxUtils.adb("input swipe " + 100 + " " + 80 + " " + 100 + " " + 80 + " " + 5000);  //长按EdiText
            wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
            wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.adb("input keyevent 4");//返回
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains(arg0)) {
                ShowToast.show("没有对应的老粉丝账号", (Activity) context);
                return;
            }
            xmlData = wxUtils.getXmlData();
            int aa = 0;
            if (xmlData.contains("更多联系人")) {
                aa = 1;
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                            && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                            ) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                        break;
                    }
                }
            } else {
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("更多联系人")) {
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                                && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                                ) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                            break;
                        }
                    }
                } else {
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                }
            }
            Boolean Flag = true;
            while (Flag) {
                xmlData = wxUtils.getXmlData();
                nodeList = wxUtils.getNodeList(xmlData);
                int bb = 0;
                if (xmlData.contains(arg0)) {
                    for (int a = 0; a < nodeList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/kq")
                                && nodeBean.getText() != null && nodeBean.getText().startsWith(arg0) && !meName.contains(nodeBean.getText())
                                ) {
                            String oldName = "";
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取  的坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                            bb = 1;
                            meName = meName + nodeBean.getText();
                            oldName = nodeBean.getText();
                            wxUtils.adbClick(396, 36, 480, 108);//点击右上角头像
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            wxUtils.adbClick(21, 168, 105, 286); //点击左上角的人头像
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            xmlData = wxUtils.getXmlData();//重新获取页面数据
                            List<String> remarkList = wxUtils.getNodeList(xmlData);
                            if (xmlData.contains("女")) {
                                sex = 0;
                            } else if (xmlData.contains("男")) {
                                sex = 1;
                            } else {
                                sex = 2;
                            }
                            for (int r = 0; r < remarkList.size(); r++) {
                                nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                    //筛选出好友
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                    break;
                                }
                            }
                            xmlData = wxUtils.getXmlData();
                            String friendsName = "";
                            List<String> remarkList2 = wxUtils.getNodeList(xmlData);
                            for (int r = 0; r < remarkList2.size(); r++) {
                                nodeBean = wxUtils.getNodeXmlBean(remarkList2.get(r)).getNode();
                                if (nodeBean != null && nodeBean.getText() != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/aoz") && nodeBean.getText().startsWith(arg0)) {
                                    friendsName = nodeBean.getText().replace(arg0, "");
                                    break;
                                }
                            }
                            wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                            //                            wxUtils.adb("input text " + friendsName + "");
                            wxUtils.adb("input swipe " + 250 + " " + 220 + " " + 250 + " " + 220 + " " + 2000);  //长按EdiText
                            cm.setText(friendsName + "");
                            wxUtils.adbWxClick(82, 160);
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            //  LogUtils.d(nodeList.get(a));
                            wxUtils.adb("input keyevent 4");
                            wxUtils.adb("input keyevent 4");//返回
                            wxUtils.adb("input keyevent 4");//返回
                            int timeSleep = random.nextInt(3 - 1 + 1) + 1;
                            LogUtils.e("end=" + 3 + "__start=" + 1 + "___间隔随机数=" + timeSleep);
                            ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                            try {
                                Thread.sleep(timeSleep * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (bb == 0) {
                    String oldXml = xmlData;
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    if (oldXml.equals(xmlData)) {
                        ShowToast.show("滑到底部了", (Activity) context);
                        Flag = false;
                        continue;
                    }
                }

            }
        } else if (arg0 != null && arg0.length() == 1) {
            backHome();
            wxUtils.adbWxClick(180, 840);
            String oldXmlData = "";
            String zzz = "ZZZ" + arg0 + "0000";
            boolean bottom = false;//到了底部
            int sex = 0;//0代表女。   1代表男   2代表性别未知
            DecimalFormat df = new DecimalFormat("000");
            int zzzNum = 0;//判断是否直接到#号修改
            String endData = "";
            String meName = "";
            w:
            while (true) {
                while (true) {
                    xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                    if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                        ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                        break w;
                    } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                        wxUtils.adb("input keyevent 4");//返回
                    } else {
                        break;
                    }
                }
                List<String> nodeList = wxUtils.getNodeList(xmlData);
                a:
                for (int a = 0; a < nodeList.size(); a++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != ""
                            && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手")
                            && !nodeBean.getContentdesc().startsWith("YY") && !nodeBean.getContentdesc().startsWith("ZZ")
                            && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc())) {
                        //筛选出好友
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                        LogUtils.d("点击进入");
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        if (!xmlData.contains("标签")) {
                            wxUtils.adb("input keyevent 4");
                            meName = nodeBean.getContentdesc();
                            continue;
                        }
                        StatisticsWxFriends(xmlData);//统计新增好友的信息
                        List<String> meWxIdList = wxUtils.getNodeList(xmlData);
                        if (xmlData.contains("女")) {
                            sex = 0;
                        } else if (xmlData.contains("男")) {
                            sex = 1;
                        } else {
                            sex = 2;
                        }
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        List<String> remarkList = wxUtils.getNodeList(xmlData);
                        for (int r = 0; r < remarkList.size(); r++) {
                            nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                            if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                //筛选出好友
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                break;
                            }
                        }
                        xmlData = wxUtils.getXmlData();

                        if (xmlData.contains("备注信息") && xmlData.contains("完成")) {

                        } else {
                            continue w;
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                        Calendar calendar = Calendar.getInstance();
                        String year = (calendar.get(Calendar.YEAR) + "").substring(2, 4);
                        String month = calendar.get(Calendar.MONTH) + 1 + "";
                        if (month.length() == 1) {
                            month = "0" + month;
                        }
                        String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
                        if (day.length() == 1) {
                            day = "0" + day;
                        }

                        switch (sex) {//0代表女。   1代表男   2代表性别未知
                            case 0:
                                int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl" + arg0, 0);
                                String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                wxUtils.adb("input text " + zzz + "B" + wx_nume_number_new_girl + "_" + year + month + day);
                                SPUtils.put(context, "wx_name_number_girl" + arg0, wx_name_number_girl + 1);
                                break;
                            case 1:
                                int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy" + arg0, 0);
                                String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                wxUtils.adb("input text " + zzz + "A" + wx_nume_number_new_boy + "_" + year + month + day);
                                SPUtils.put(context, "wx_name_number_boy" + arg0, wx_name_number_boy + 1);
                                break;
                            case 2:
                                int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c" + arg0, 0);
                                String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                wxUtils.adb("input text " + zzz + "C" + wx_nume_number_c + "_" + year + month + day);
                                SPUtils.put(context, "wx_name_number_c" + arg0, wx_name_number_c + 1);
                                break;
                        }
                        try {
                            Thread.sleep(5 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                        wxUtils.adb("input keyevent 4");
                        xmlData = wxUtils.getXmlData();
                        if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                            wxUtils.adb("input keyevent 4");
                        }

                        //设置间隔时间
                        int start;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_s())) {
                            start = 3;
                        } else {
                            start = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_s());
                        }
                        int end;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_e())) {
                            end = 6;
                        } else {
                            end = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_e());
                        }
                        int timeSleep = random.nextInt(end - start + 1) + start;
                        LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                        ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                        try {
                            Thread.sleep(timeSleep * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                nodeList = wxUtils.getNodeList(xmlData);
                if (!xmlData.contains("发现")) {
                    ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                    continue w;
                }
                zzzNum = 0;
                for (int b = 0; b < nodeList.size(); b++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                    if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                            && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("YYZ")
                            && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())) {
                        continue w;
                    } else if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                            && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && (nodeBean.getContentdesc().startsWith("YYZ")
                            || nodeBean.getContentdesc().startsWith("ZZZ") || nodeBean.getContentdesc().startsWith("zzz")) && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())
                            ) {
                        zzzNum++;
                    }
                }
                int aaaaa = 0;
                if (!bottom) {
                    if (zzzNum >= 8) {
                        //                    wxUtils.adbDimensClick(context, 460, 768,460, 768);
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                        String xmlData2 = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData2);
                        for (int b = 0; b < nodeList.size(); b++) {
                            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                            if ((nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                    && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYZ") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                                String xmlData3 = wxUtils.getXmlData();
                                //                            meName =meName+nodeBean.getContentdesc();
                                if (xmlData3.contains("备注和标签")) {
                                    aaaaa++;
                                } else {
                                    meName = meName + nodeBean.getContentdesc();
                                }
                                wxUtils.adb("input keyevent 4");//返回
                            }
                        }
                        if (aaaaa == 0) {
                            //说明全是ZZZ或者zzz开头的了
                            wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                            wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                            xmlData = wxUtils.getXmlData();
                            nodeList = wxUtils.getNodeList(xmlData);
                            int ccc = 0;
                            for (int b = 0; b < nodeList.size(); b++) {
                                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                if ((nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                        && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYZ") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                                    ccc++;
                                }
                            }
                            if (ccc == 0) {
                                ShowToast.show("修改备注完成", (Activity) context);
                                break w;
                            }
                        }
                    } else {
                        oldXmlData = wxUtils.getXmlData();
                        wxUtils.adbUpSlide(context);//向上滑动
                    }

                    //            endData = xmlData;
                    xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
                    if (oldXmlData.equals(xmlData)) {
                        ShowToast.show("修改备注完成", (Activity) context);
                        break w;
                    }
                    nodeList = wxUtils.getNodeList(xmlData);
                    int bbb = 0;
                    for (int b = 0; b < nodeList.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                        if ((nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYZ") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                            bbb++;
                        }
                    }
                    if (bbb == 0) {
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                    }
                }
            }

        } else if (arg0 != null && arg0.length() >= 4) {
            if (arg1.length() >= 4) {
                String special_id = arg1.substring(3, 4);
                String meName = "";
                int sex = 0;//0代表女。   1代表男   2代表性别未知
                DecimalFormat df = new DecimalFormat("000");
                String Type = "0000";
                boolean flag0 = true;
                while (flag0) {
                    xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                    if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                        flag0 = false;
                    } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                        wxUtils.adb("input keyevent 4");//返回
                    } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                        wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                        flag0 = false;
                    }
                }
                wxUtils.adbClick(306, 36, 378, 108);//搜索
                ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                cm.setText(arg0);
                wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
                wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adb("input keyevent 4");//返回
                xmlData = wxUtils.getXmlData();
                if (!xmlData.toLowerCase().contains(arg0.toLowerCase())) {
                    ShowToast.show("没有对应的老粉丝账号", (Activity) context);
                    return;
                }
                xmlData = wxUtils.getXmlData();
                int aa = 0;
                if (xmlData.contains("更多联系人")) {
                    aa = 1;
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                                && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                                ) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                            break;
                        }
                    }
                } else {
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("更多联系人")) {
                        nodeList = wxUtils.getNodeList(xmlData);
                        for (int a = 0; a < nodeList.size(); a++) {
                            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                                    && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                                    ) {
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                                break;
                            }
                        }
                    } else {
                        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    }
                }
                Boolean Flag = true;
                while (Flag) {
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    int bb = 0;
                    if (xmlData.contains(arg0)) {
                        for (int a = 0; a < nodeList.size(); a++) {
                            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/kq")
                                    && nodeBean.getText() != null && nodeBean.getText().toLowerCase().startsWith(arg0.toLowerCase()) && !meName.toLowerCase().contains(nodeBean.getText().toLowerCase())
                                    ) {
                                String oldName = "";
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取  的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                                bb = 1;
                                meName = meName + nodeBean.getText().toLowerCase();
                                oldName = nodeBean.getText();
                                wxUtils.adbClick(396, 36, 480, 108);//点击右上角头像
                                xmlData = wxUtils.getXmlData();
                                if (!xmlData.contains("添加成员")) {
                                    superXiuGaiBeiZhu(arg0, arg1);
                                    return;
                                }
                                wxUtils.adbClick(21, 168, 105, 286); //点击左上角的人头像
                                xmlData = wxUtils.getXmlData();//重新获取页面数据
                                List<String> remarkList = wxUtils.getNodeList(xmlData);
                                if (xmlData.contains("女")) {
                                    sex = 0;
                                } else if (xmlData.contains("男")) {
                                    sex = 1;
                                } else {
                                    sex = 2;
                                }
                                for (int r = 0; r < remarkList.size(); r++) {
                                    nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                                    if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                        //筛选出好友
                                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                        break;
                                    }
                                }
                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                                Calendar calendar = Calendar.getInstance();
                                String year = (calendar.get(Calendar.YEAR) + "").substring(2, 4);
                                String month = calendar.get(Calendar.MONTH) + 1 + "";
                                if (month.length() == 1) {
                                    month = "0" + month;
                                }
                                String day = calendar.get(Calendar.DAY_OF_MONTH) + "";

                                if (day.length() == 1) {
                                    day = "0" + day;
                                }
                                switch (sex) {//0代表女。   1代表男   2代表性别未知
                                    case 0:
                                        int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl" + special_id, 0);
                                        String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                        wxUtils.adb("input text " + arg1 + "B" + wx_nume_number_new_girl + "_" + year + month + day);
                                        SPUtils.put(context, "wx_name_number_girl" + special_id, wx_name_number_girl + 1);
                                        break;
                                    case 1:
                                        int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy" + special_id, 0);
                                        String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                        wxUtils.adb("input text " + arg1 + "A" + wx_nume_number_new_boy + "_" + year + month + day);
                                        SPUtils.put(context, "wx_name_number_boy" + special_id, wx_name_number_boy + 1);
                                        break;
                                    case 2:
                                        int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c" + special_id, 0);
                                        String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                        wxUtils.adb("input text " + arg1 + "C" + wx_nume_number_c + "_" + year + month + day);
                                        SPUtils.put(context, "wx_name_number_c" + special_id, wx_name_number_c + 1);
                                        break;
                                }
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                                wxUtils.adb("input keyevent 4");
                                wxUtils.adb("input keyevent 4");//返回
                                wxUtils.adb("input keyevent 4");//返回
                                try {
                                    Thread.sleep(1 * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    if (bb == 0) {
                        String oldXml = xmlData;
                        wxUtils.adbUpSlide(context);
                        xmlData = wxUtils.getXmlData();
                        if (oldXml.equals(xmlData)) {
                            ShowToast.show("滑到底部了", (Activity) context);
                            Flag = false;
                            continue;
                        }
                    }
                }

            }

        }
    }

    private void intelligentChat2(int ag0) {
        String wxName = "";
        String uid = SPUtils.getString(context, "uid", "0000");
        String wxAccount = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
        String accountLocation = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
        List<WxChatBean> mWxChatBeanList = new ArrayList<>();
        WxChatBean mWxChatBean = new WxChatBean(uid, wxAccount + "", 0 + "");
        mWxChatBeanList.add(mWxChatBean);
        String str = new Gson().toJson(mWxChatBeanList);
        LogUtils.d("JSON" + str.toString());
        //        eachAnotherChatTask(wxAccount);
        // 将文本内容放到系统剪贴板里。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        cm.setText("ZZZ9");
        String meName = "";
        backHome();
        wxUtils.adbDimensClick(context, R.dimen.x204, R.dimen.y17, R.dimen.x252, R.dimen.y51);//搜索
        int x = context.getResources().getDimensionPixelSize(R.dimen.x167);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y33);//EdiText
        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
        wxUtils.adbDimensClick(context, R.dimen.x118, R.dimen.y85, R.dimen.x118, R.dimen.y85);//粘贴
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y85, R.dimen.x320, R.dimen.y130);//文件传输助手
        wxUtils.adb("input keyevent 4");//返回

        xmlData = wxUtils.getXmlData();

        if (xmlData.contains("更多联系人")) {
            List<String> personXmlList = wxUtils.getNodeList(xmlData);
            for (int k = 0; k < personXmlList.size(); k++) {
                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(personXmlList.get(k)).getNode();
                if (nodeBean2.getText() != null && nodeBean2.getText().equals("更多联系人")) {
                    listXY = wxUtils.getXY(nodeBean2.getBounds());//点击更多联系人
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击更多联系人
                    break;
                }
            }

        } else {
            wxUtils.adbUpSlide(context);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlData = wxUtils.getXmlData();
            List<String> personXmlList = wxUtils.getNodeList(xmlData);
            for (int k = 0; k < personXmlList.size(); k++) {
                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(personXmlList.get(k)).getNode();
                if (nodeBean2.getText() != null && nodeBean2.getText().equals("更多联系人")) {
                    listXY = wxUtils.getXY(nodeBean2.getBounds());//获取更多联系人位置
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击更多联系人
                    break;
                }
            }
        }
        Boolean Flag = true;
        while (Flag) {
            int kkk = 0;
            xmlData = wxUtils.getXmlData();
            List<String> personBeanXmlList = wxUtils.getNodeList(xmlData);
            for (int k = 0; k < personBeanXmlList.size(); k++) {
                NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(personBeanXmlList.get(k)).getNode();
                if (nodeBean3.getText() != null && nodeBean3.getText().startsWith("ZZZ9") && nodeBean3.getResourceid() != null && nodeBean3.getResourceid().equals("com.tencent.mm:id/kq")
                        && !meName.contains(nodeBean3.getText())) {
                    listXY = wxUtils.getXY(nodeBean3.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击 指定联系人坐标
                    meName = meName + nodeBean3.getText();
                    kkk++;
                    wxUtils.adbClick(408, 36, 480, 108); //点击右上角的人头像
                    wxUtils.adbClick(80, 200, 80, 200);  //点击人头像
                    break;
                }
            }
            if (kkk == 0) {
                String oldXmlData = wxUtils.getXmlData();

                wxUtils.adbUpSlide(context);
                String newXmlData = wxUtils.getXmlData();
                if (newXmlData.equals(oldXmlData)) {
                    //证明已经到底了
                    Flag = false;
                    continue;
                }
                continue;
            }
            xmlData = wxUtils.getXmlData();
            List<String> personalBeanXmlList = wxUtils.getNodeList(xmlData);
            for (int k = 0; k < personalBeanXmlList.size(); k++) {
                NodeXmlBean.NodeBean nodeBean4 = wxUtils.getNodeXmlBean(personalBeanXmlList.get(k)).getNode();
                if (nodeBean4.getText() != null && nodeBean4.getText().startsWith("微信号")) {
                    String wxFullName = nodeBean4.getText();
                    //                String str = nodeBean.getText();
                    wxName = wxFullName.substring(4, wxFullName.length()).trim();
                    SPUtils.putString(context, "fiendsWxNumber", wxName);
                    break;
                }
            }
            String taskId = SPUtils.getString(context, "taskId", "");
            try {
                Response data = OkHttpUtils.get().url(URLS.url + "home/ApiAndroid/eachChatFree").addParams("taskId", taskId).addParams("accountB", wxName).
                        addParams("accountA", wxAccount).build().execute();
                if (data.code() == 200) {
                    ShowToast.show("等待120秒", (Activity) context);
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getText() != null && nodeBean.getText().contains("视频通话") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/anb")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  发消息
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (ag0 == 0) {
                                wxUtils.adbClick(120, 750, 120, 750); //点击视频聊天
                            } else if (ag0 == 1) {
                                wxUtils.adbClick(120, 820, 120, 820); //点击语音聊天
                            }

                            xmlData = wxUtils.getXmlData();
                            if (xmlData.contains("在移动网络环境下会影响视频和音频质量，并产生手机流量。")) {
                                wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y243, R.dimen.x264, R.dimen.y264);//确定
                            }
                            Flag = false;
                            break;
                        }
                    }
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbWxClick(251, 727);//点击挂断
                } else {
                    ShowToast.show("联网失败！", (Activity) context);
                    wxUtils.adb("input keyevent 4");
                    wxUtils.adb("input keyevent 4");
                    wxUtils.adb("input keyevent 4");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /*
     *   去朋友圈
     * */
    private void goToPengYou() {
        wxUtils.openWx((Activity) context);
        backHome();
        wxUtils.adbClick(282, 822, 318, 847); //发现
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("android:id/title") && nodeBean.getText() != null && nodeBean.getText().equals("朋友圈")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取朋友圈坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));
                break;
            }
        }
    }

    // 删除朋友圈指定内容
    private void deleteWxPengYouContent(String arg0) {
        TimeUtil timeUtil = new TimeUtil();
        String cuurentTime = timeUtil.getDtae();
        String[] str1 = cuurentTime.split(" ");
        String[] str2 = str1[0].split("-");
        String str3 = str2[2];
        //        goToPengYou();
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adbClick(400, 500, 400, 500);//点击人头像
        wxUtils.adbUpSlide(context);
        Boolean flag = true;
        while (flag) {
            Boolean Flag = true;
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains(arg0)) {
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/ql") || nodeBean.getResourceid().equals("com.tencent.mm:id/dda")
                            || nodeBean.getResourceid().equals("com.tencent.mm:id/a9h")) && nodeBean.getText() != null && nodeBean.getText().contains(arg0)) {

                        listXY = wxUtils.getXY(nodeBean.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));
                        break;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("删除")) {
                    String str_day = "0";
                    if (xmlData.contains("com.tencent.mm:id/dbc")) {
                        List<String> nodeList_ask = wxUtils.getNodeList(xmlData);
                        for (int b = 0; b < nodeList_ask.size(); b++) {
                            NodeXmlBean.NodeBean nodeBean_ask = wxUtils.getNodeXmlBean(nodeList_ask.get(b)).getNode();
                            if (nodeBean_ask.getText() != null && nodeBean_ask.getResourceid() != null && nodeBean_ask.getResourceid().equals("com.tencent.mm:id/dbc")) {
                                String str_data = nodeBean_ask.getText();
                                if (str_data.contains("日")) {
                                    String[] str_data1 = str_data.split("日");
                                    String[] str_data2 = str_data1[0].split("月");
                                    str_day = str_data2[1];
                                } else {
                                    str_day = str3;
                                }
                                break;
                            }
                        }
                    }
                    if ((Integer.valueOf(str3) == Integer.valueOf(str_day)) || (Integer.valueOf(str3) == (Integer.valueOf(str_day) + 1))) {
                        List<String> nodeList2 = wxUtils.getNodeList(xmlData);
                        for (int b = 0; b < nodeList2.size(); b++) {
                            NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList2.get(b)).getNode();
                            if (nodeBean2.getText() != null && nodeBean2.getText().equals("删除") && nodeBean2.getResourceid() != null && nodeBean2.getResourceid().equals("com.tencent.mm:id/dbi")) {
                                List<Integer> listXY2 = wxUtils.getXY(nodeBean2.getBounds());//
                                wxUtils.adbClick(listXY2.get(0), listXY2.get(1), listXY2.get(2), listXY2.get(3));
                                break;
                            }
                        }
                        wxUtils.adbClick(350, 520, 350, 520);//点击了确定
                    } else {
                        wxUtils.adb("input keyevent 4");//返回
                    }

                } else {
                    if (xmlData.contains("评论")) {
                        String str_day = "0";
                        //                        String xmlData_ask = wxUtils.getXmlData();
                        if (xmlData.contains("android:id/text1")) {
                            List<String> nodeList_ask = wxUtils.getNodeList(xmlData);
                            for (int b = 0; b < nodeList_ask.size(); b++) {
                                NodeXmlBean.NodeBean nodeBean_ask = wxUtils.getNodeXmlBean(nodeList_ask.get(b)).getNode();
                                if (nodeBean_ask.getText() != null && nodeBean_ask.getResourceid() != null && nodeBean_ask.getResourceid().equals("android:id/text1")) {
                                    String str_data = nodeBean_ask.getText();
                                    if (str_data.contains("日")) {
                                        String[] str_data1 = str_data.split("日");
                                        String[] str_data2 = str_data1[0].split("月");
                                        str_day = str_data2[1];
                                    } else {
                                        str_day = str3;
                                    }
                                }
                            }
                        }
                        if ((Integer.valueOf(str3) == Integer.valueOf(str_day)) || (Integer.valueOf(str3) == (Integer.valueOf(str_day) + 1))) {
                            wxUtils.adbClick(450, 60, 450, 60);//点击右上角的更多
                            wxUtils.adbClick(50, 820, 50, 820);//点击删除
                            wxUtils.adbClick(350, 520, 350, 520);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            wxUtils.adb("input keyevent 4");//返回
                        } else {
                            wxUtils.adb("input keyevent 4");//返回
                            flag = false;
                            continue;
                        }

                    } else {
                        wxUtils.adb("input keyevent 4");//返回
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String str_day = "0";
                        String xmlData_ask = wxUtils.getXmlData();
                        if (xmlData_ask.contains("android:id/text1")) {
                            List<String> nodeList_ask = wxUtils.getNodeList(xmlData_ask);
                            for (int b = 0; b < nodeList_ask.size(); b++) {
                                NodeXmlBean.NodeBean nodeBean_ask = wxUtils.getNodeXmlBean(nodeList_ask.get(b)).getNode();
                                if (nodeBean_ask.getText() != null && nodeBean_ask.getResourceid() != null && nodeBean_ask.getResourceid().equals("android:id/text1")) {
                                    String str_data = nodeBean_ask.getText();
                                    if (str_data.contains("日")) {
                                        String[] str_data1 = str_data.split("日");
                                        String[] str_data2 = str_data1[0].split("月");
                                        str_day = str_data2[1];
                                    } else {
                                        str_day = str3;
                                    }
                                }
                            }
                        }
                        if ((Integer.valueOf(str3) == Integer.valueOf(str_day)) || (Integer.valueOf(str3) == (Integer.valueOf(str_day) + 1))) {
                            wxUtils.adbClick(450, 60, 450, 60);//点击右上角的更多
                            wxUtils.adbClick(50, 820, 50, 820);//点击删除
                            wxUtils.adbClick(350, 520, 350, 520);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            wxUtils.adb("input keyevent 4");//返回
                        } else {
                            wxUtils.adb("input keyevent 4");//返回
                            flag = false;
                            continue;
                        }
                    }

                }
            } else {
                flag = false;
            }
        }

    }

    /**
     * 获得当前页面的集合数据
     *
     * @return
     */
    private List<String> getNodeList() {
        return wxUtils.getNodeList(wxUtils.getXmlData());
    }

    /**
     * 遍历微信第一个聊天列表页面，找出未读消息
     */
    private void toForUnRead() {
        wxUtils.openWx((Activity) context);
        backHome();
        wxUtils.adbClick(42, 822, 78, 847);//点击微信
        String meName = "";
        int kkk = 0;
        SPUtils.putInt(context, "HuaDongCiShu", 0);
        Boolean Flag = true;
        while (Flag) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("com.tencent.mm:id/j4")) {
                nodeList = wxUtils.getNodeList(xmlData);
                for (int b = 0; b < nodeList.size() - 8; b++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();   // 角标
                    NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(b + 4)).getNode();  //名称
                    NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b + 8)).getNode(); // 内容
                    if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/j4")) {
                        kkk = 1;
                        listXY = wxUtils.getXY(nodeBean.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                        if (nodeBean2 != null && nodeBean3 != null && nodeBean2.getText() != null && nodeBean3.getText() != null) {
                            wxReply(nodeBean3.getText(), nodeBean2.getText());
                        }
                        backHome();
                        break;
                    }
                }
            } else {
                ShowToast.show("该页面没有新消息了！", (Activity) context);
                Flag = false;
            }

        }
        int page = random.nextInt(2) + 1;// 随机滑动1-2页
        for (int i = 0; i < page; i++) {
            wxUtils.adbUpSlide(context);
            Boolean Flag1 = true;
            while (Flag1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("com.tencent.mm:id/j4")) {
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < nodeList.size() - 8; b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();   // 角标
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(b + 4)).getNode();  //名称
                        NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b + 8)).getNode(); // 内容
                        if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/j4")) {
                            kkk = 1;
                            listXY = wxUtils.getXY(nodeBean.getBounds());//
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                            if (nodeBean2 != null && nodeBean3 != null && nodeBean2.getText() != null && nodeBean3.getText() != null) {
                                wxReply(nodeBean3.getText(), nodeBean2.getText());
                            }
                            backHome();
                            break;
                        }
                    }
                } else {
                    ShowToast.show("该页面没有新消息了！", (Activity) context);
                    Flag1 = false;
                }

            }
        }
    }

    private void wxReply(String str, String str2) {
        //   内容 和  名称
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("com.tencent.mm:id/he")) {
            backHome();
            return;
        }
        SPUtils.putString(context, "WxReplyMessage", "");
        xmlData = wxUtils.getXmlData();
        List<String> qunNameDataList = wxUtils.getNodeList(xmlData);
        for (int c = 0; c < qunNameDataList.size(); c++) {
            NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
            if (qunNameBean != null && qunNameBean.getResourceid() != null && "android:id/text1".equals(qunNameBean.getResourceid()) && qunNameBean.getText() != null && qunNameBean.getText().contains("腾讯新闻")) {
                wxUtils.adbClick(45, 401, 435, 482);//点击腾讯新闻 阅读
                ShowToast.show("阅读新闻 停留一分钟！", (Activity) context);
                wxUtils.adbUpSlide(context);
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adbUpSlide(context);
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adbUpSlide(context);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backHome();
                break;
            }
            if (qunNameBean != null && qunNameBean.getResourceid() != null && "com.tencent.mm:id/hj".equals(qunNameBean.getResourceid()) && qunNameBean.getText() != null) {
                if ((qunNameBean.getText().contains("(") && qunNameBean.getText().contains(")"))) {
                    backHome();
                    break;
                } else {
                    if (qunNameBean.getText().equals(str2)) {
                        String newNeiRong = "";
                        if (str.contains("我通过了你的朋友验证请求")) {
                            backHome();
                            return;
                        }
                        if (str.contains("谁")) {
                            newNeiRong = "谁";
                        } else if (str.contains("哪个")) {
                            newNeiRong = "哪个";
                        } else if (str.contains("那个")) {
                            newNeiRong = "那个";
                        } else if (str.contains("电话")) {
                            newNeiRong = "你电话多少";
                        } else if (str.contains("约")) {
                            newNeiRong = "约吗";
                        } else if (str.contains("你好")) {
                            newNeiRong = "你好啊";
                        } else if (str.contains("名字")) {
                            newNeiRong = "你怎么知道我的名字？";
                        } else if (str.contains("认识")) {
                            newNeiRong = "我们认识吗？";
                        } else if (str.contains("你叫什么")) {
                            newNeiRong = "我不告诉你";
                        } else if (str.contains("那位")) {
                            newNeiRong = "谁";
                        } else if (str.contains("哪位")) {
                            newNeiRong = "哪位";
                        } else if (str.contains("有事")) {
                            newNeiRong = "没事呀";
                        } else if (str.contains("你叫")) {
                            newNeiRong = "哪位";
                        } else if (str.contains("哪位")) {
                            newNeiRong = "哪位";
                        } else if (str.contains("什么")) {
                            newNeiRong = "没什么";
                        } else if (str.contains("你是")) {
                            newNeiRong = "你是";
                        } else {
                            newNeiRong = str;
                        }
                        sendReply(newNeiRong);
                        ShowToast.show("相同就回复 ！", (Activity) context);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String[] imageData1 = {"[微笑]", "[得意]", "[呲牙]", "[嘿哈]", "[捂脸]", "[机智]", "[奸笑]", "[耶]", "[爱情]"};
                        Random rand = new Random();
                        int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                        int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                        int randNum1 = rand.nextInt(9);
                        xmlData = wxUtils.getXmlData();
                        if (!xmlData.contains("切换到按住说话")) {
                            wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                        } else {
                            wxUtils.adbClick(100, 820, 100, 820); //点击输入框
                        }
                        wxUtils.adb("input swipe " + 190 + " " + 450 + " " + 190 + " " + 450 + " " + 5000);  //长按3秒
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                        String replyMessageData = SPUtils.getString(context, "WxReplyMessage", "");
                        cm.setText(imageData1[randNum1] + replyMessageData);
                        //                        wxUtils.adbClick(110, 385, 110, 385);//点击粘贴
                        wxUtils.adbWxClick(100, 408);
                        //                        wxUtils.adbClick(405, 411, 471, 459); //点击发送
                        wxUtils.adbWxClick(437, 460);
                        backHome();
                        break;

                    } else {
                        ShowToast.show("不相同 不回复！", (Activity) context);
                        backHome();
                        break;
                    }
                }
            }
        }

        backHome();

    }


    private void sendReply(String newStr) {
        String Path = URLS.ziDongReply() + "?str=" + newStr;
        URL url = null;
        try {
            url = new URL(Path);
            // 2.建立一个http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 3.设置一些请求方式
            conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
            // // 503服务器内部错误
            if (code == 200) {
                InputStream is = conn.getInputStream();
                // 把is的内容转换为字符串
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());
                LogUtils.d("返回的结果是" + result);
                wxReplyMessageBean mWxReplyMessageBean = GsonUtil.parseJsonWithGson(result, wxReplyMessageBean.class);
                SPUtils.putString(context, "WxReplyMessage", mWxReplyMessageBean.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     *   语音互聊 智能聊天
     * */
    private void intelligentChat() {
        String wxName = "";
        String uid = SPUtils.getString(context, "uid", "0000");
        String wxAccount = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
        String accountLocation = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
        List<WxChatBean> mWxChatBeanList = new ArrayList<>();
        WxChatBean mWxChatBean = new WxChatBean(uid, wxAccount + "", 0 + "");
        mWxChatBeanList.add(mWxChatBean);
        String str = new Gson().toJson(mWxChatBeanList);
        LogUtils.d("JSON" + str.toString());
        //        eachAnotherChatTask(wxAccount);
        // 将文本内容放到系统剪贴板里。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        cm.setText("ZZZ9");
        String meName = "";
        backHome();
        wxUtils.adbDimensClick(context, R.dimen.x204, R.dimen.y17, R.dimen.x252, R.dimen.y51);//搜索
        int x = context.getResources().getDimensionPixelSize(R.dimen.x167);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y33);//EdiText
        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
        wxUtils.adbDimensClick(context, R.dimen.x118, R.dimen.y85, R.dimen.x118, R.dimen.y85);//粘贴
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y85, R.dimen.x320, R.dimen.y130);//文件传输助手
        wxUtils.adb("input keyevent 4");//返回

        xmlData = wxUtils.getXmlData();

        if (xmlData.contains("更多联系人")) {
            List<String> personXmlList = wxUtils.getNodeList(xmlData);
            for (int k = 0; k < personXmlList.size(); k++) {
                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(personXmlList.get(k)).getNode();
                if (nodeBean2.getText() != null && nodeBean2.getText().equals("更多联系人")) {
                    listXY = wxUtils.getXY(nodeBean2.getBounds());//点击更多联系人
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击更多联系人
                    break;
                }
            }

        } else {
            wxUtils.adbUpSlide(context);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlData = wxUtils.getXmlData();
            List<String> personXmlList = wxUtils.getNodeList(xmlData);
            for (int k = 0; k < personXmlList.size(); k++) {
                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(personXmlList.get(k)).getNode();
                if (nodeBean2.getText() != null && nodeBean2.getText().equals("更多联系人")) {
                    listXY = wxUtils.getXY(nodeBean2.getBounds());//获取更多联系人位置
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击更多联系人
                    break;
                }
            }
        }
        Boolean Flag = true;
        while (Flag) {
            int kkk = 0;
            xmlData = wxUtils.getXmlData();
            List<String> personBeanXmlList = wxUtils.getNodeList(xmlData);
            for (int k = 0; k < personBeanXmlList.size(); k++) {
                NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(personBeanXmlList.get(k)).getNode();
                if (nodeBean3.getText() != null && nodeBean3.getText().startsWith("ZZZ9") && nodeBean3.getResourceid() != null && nodeBean3.getResourceid().equals("com.tencent.mm:id/kq")
                        && !meName.contains(nodeBean3.getText())) {
                    listXY = wxUtils.getXY(nodeBean3.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击 指定联系人坐标
                    meName = meName + nodeBean3.getText();
                    kkk++;
                    wxUtils.adbClick(408, 36, 480, 108); //点击右上角的人头像
                    wxUtils.adbClick(80, 200, 80, 200);  //点击人头像
                    break;
                }
            }
            if (kkk == 0) {
                String oldXmlData = wxUtils.getXmlData();

                wxUtils.adbUpSlide(context);
                String newXmlData = wxUtils.getXmlData();
                if (newXmlData.equals(oldXmlData)) {
                    //证明已经到底了
                    Flag = false;
                    continue;
                }
                continue;
            }
            xmlData = wxUtils.getXmlData();
            List<String> personalBeanXmlList = wxUtils.getNodeList(xmlData);
            for (int k = 0; k < personalBeanXmlList.size(); k++) {
                NodeXmlBean.NodeBean nodeBean4 = wxUtils.getNodeXmlBean(personalBeanXmlList.get(k)).getNode();
                if (nodeBean4.getText() != null && nodeBean4.getText().startsWith("微信号")) {
                    String wxFullName = nodeBean4.getText();
                    //                String str = nodeBean.getText();
                    wxName = wxFullName.substring(4, wxFullName.length()).trim();
                    SPUtils.putString(context, "fiendsWxNumber", wxName);
                    break;
                }
            }
            String taskId = SPUtils.getString(context, "taskId", "");
            try {
                Response data = OkHttpUtils.get().url(URLS.url + "home/ApiAndroid/eachChatFree").addParams("taskId", taskId).addParams("accountB", wxName).
                        addParams("accountA", wxAccount).build().execute();
                if (data.code() == 200) {
                    ShowToast.show("等待10秒", (Activity) context);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getText() != null && nodeBean.getText().contains("发消息") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/ana")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  发消息
                            break;
                        }
                    }
                    int x1 = context.getResources().getDimensionPixelSize(R.dimen.x136);
                    int y1 = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                    Random random = new Random();
                    String[] str_news = {"旁边开了个火锅店，炒鸡好吃，美团还能减钱，一起去吃吧",
                            "周末一起去网吧玩LOL开黑,我安琪拉玩的贼6",
                            "等哈我们去吃油焖大虾还是纸上烤肉呀，看到速度回复",
                            "你们上班累不累呀，晚上出来Happy下",
                            "都是你们这群人，马化腾亏成中国首富了",
                            "我们去健身房锻炼下吧，身上的膘又涨了几斤",
                            "最近工作忙不忙呀，我TM周末都加班",
                            "摩拜单车被收购，以后股东是腾讯和美团呀!",
                            "这鬼天气，昨天是夏天，今天是冬天"};

                    for (int i = 0; i < 4; i++) {
                        int tttt = random.nextInt(3);// 0为文字  1为图片  2为语音
                        int ttt2 = random.nextInt(9);
                        int ttt_0 = random.nextInt(3);
                        int ttt_1 = random.nextInt(3);
                        String xmlData5 = wxUtils.getXmlData();
                        List<String> nodeList5 = wxUtils.getNodeList(xmlData5);
                        for (int a = 0; a < nodeList5.size(); a++) {
                            NodeXmlBean.NodeBean nodeBean5 = wxUtils.getNodeXmlBean(nodeList5.get(a)).getNode();
                            if (nodeBean5 != null && nodeBean5.getResourceid() != null && nodeBean5.getResourceid().equals("com.tencent.mm:id/aag")) {
                                listXY = wxUtils.getXY(nodeBean5.getBounds());//  点击+ 号
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                break;
                            }

                        }
                        if (ttt_0 == 0 && ttt_1 == 0) {
                            //发语音
                            wxUtils.adbWxClick(49, 450);//点击切换到语音
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //录制时间
                            int start;
                            if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRecord_time_s())) {
                                start = 5;
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
                            wxUtils.adb("input swipe " + x1 + " " + y1 + " " + x1 + " " + y1 + " " + timeSleep * 1000);  //长按EdiText
                            ShowToast.show("休息90秒", (Activity) context);
                            try {
                                Thread.sleep(90000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else if (ttt_0 == 1 && ttt_1 == 1) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            int ttt_pic = random.nextInt(3);
                            wxUtils.adbWxClick(78, 550);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (ttt_pic == 1) {
                                wxUtils.adbClick(78, 119, 108, 149);//选择图片
                            } else if (ttt_pic == 2) {
                                wxUtils.adbClick(198, 119, 228, 149);//选择图片
                            } else if (ttt_pic == 3) {
                                wxUtils.adbClick(318, 119, 348, 149);//选择图片
                            }
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                            ShowToast.show("休息90秒", (Activity) context);
                            try {
                                Thread.sleep(90000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else { // 发文字
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            cm.setText(str_news[ttt2]);
                            wxUtils.adb("input swipe " + 185 + " " + 450 + " " + 185 + " " + 450 + " " + 1000);  //长按EdiText
                            //                            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                            wxUtils.adbWxClick(105, 395);
                            //                            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String xmlData6 = wxUtils.getXmlData();
                            List<String> nodeList6 = wxUtils.getNodeList(xmlData6);
                            for (int a = 0; a < nodeList6.size(); a++) {
                                NodeXmlBean.NodeBean nodeBean6 = wxUtils.getNodeXmlBean(nodeList6.get(a)).getNode();
                                if (nodeBean6 != null && nodeBean6.getResourceid() != null && nodeBean6.getResourceid().equals("com.tencent.mm:id/aah") && nodeBean6.getText() != null && nodeBean6.getText().equals("发送")) {
                                    listXY = wxUtils.getXY(nodeBean6.getBounds());//  点击发送
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                    break;
                                }
                            }
                            ShowToast.show("休息90秒", (Activity) context);
                            try {
                                Thread.sleep(90000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    wxUtils.adb("input keyevent 4");
                    wxUtils.adb("input keyevent 4");
                    Flag = false;

                } else {
                    ShowToast.show("联网失败！", (Activity) context);
                    wxUtils.adb("input keyevent 4");
                    wxUtils.adb("input keyevent 4");
                    wxUtils.adb("input keyevent 4");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    //    private void checkFree(String str) {
    //        //成功是空闲 1  失败是繁忙 2
    //        String Path = URLS.url + "home/ApiAndroid/eachChatFree" + "?account=" + str;
    //        URL url = null;
    //        try {
    //            url = new URL(Path);
    //            // 2.建立一个http连接
    //            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //            // 3.设置一些请求方式
    //            conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
    //            conn.setRequestProperty(
    //                    "User-Agent",
    //                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
    //            int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
    //            // // 503服务器内部错误
    //            if (code == 200) {
    //                SPUtils.putString(context, "DeviceIsBusy", "1");
    //            } else {
    //                SPUtils.putString(context, "DeviceIsBusy", "2");
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //
    //    }


    private void eachChatLock(String str) {
        String Path = URLS.url + "home/ApiAndroid/eachChatLock";
        String Taskid = SPUtils.getString(context, "Taskid", "");
        List<wxLockBean> mWxLockBeanList = new ArrayList<>();
        wxLockBean mWxLockBean = new wxLockBean(str, Taskid);
        mWxLockBeanList.add(mWxLockBean);
        String str2 = new Gson().toJson(mWxLockBeanList);
        LogUtils.d("JSON" + str2.toString());
        RequestParams params = new RequestParams(Path);
        params.addBodyParameter("json", str2.replace("\\", ""));

        HttpManager.getInstance().sendPostRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                LogUtils.d("上锁成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("上锁失败");
            }
        });
    }

    /*
     *   自定义拉群
     *   思路： 先改备注，再拉群 1,1,2@2@京东@1
     * */
    private void ziDingYiLaQun(String arg0, String arg1, String arg2) {//1 c 4@4@王者荣耀游戏群@2
        String wxQunFriendsName = "";
        String fanType = arg0;             //粉丝类型  1为新粉丝  2为老粉丝
        String fanId = arg1;         //  粉丝分类 比如ZZZ9
        String addGroupNum = "";     //拉群次数
        String groupType = "";         //群类型
        String preGrpName = "";       // 群名前缀
        String personNum = "";          // 群人数
        String[] str = arg2.split("@");
        String meName = "";
        int i = 0;
        SPUtils.putString(context, "StrLastFourth", "");//每修改一次记录最后的四位
        if (str.length >= 4) {
            addGroupNum = str[0];    //拉群次数
            groupType = str[1];         //群类型
            preGrpName = str[2];      // 群名前缀
            personNum = str[3];          // 群人数
        }
        Log.d("接收到的数据：", "粉丝类型:" + fanType + " 粉丝ID:" + fanId + " 拉群次数:" + addGroupNum + " 群类型:" + groupType + " 群名前缀:" + preGrpName + " 群人数:" + personNum + "");
        //修改完备注之后开始拉群
        backHome();
        wxUtils.openWx((Activity) context);
        boolean flag0 = true;
        while (flag0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag0 = false;
            }
        }
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("群聊")) {
            wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        } else {
            wxUtils.adbClick(93, 208, 444, 241);//点击群聊
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (personNum.equals("2")) {//大于40人
            xmlData = wxUtils.getXmlData();
            List<String> ud = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < ud.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ud.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/a9u") && nodeBean.getText() != null && nodeBean.getText().contains(preGrpName)) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                    break;
                }
            }
            wxUtils.adbClick(408, 36, 480, 108);//点击右上角的人头像
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size() - 1; a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().contains("聊天信息")) {
                    String qunPersonNum = nodeBean.getText();
                    String Num = qunPersonNum.substring(5, qunPersonNum.length() - 1);
                    i = Integer.parseInt(Num);//获取到目前群内成员的人数
                    break;
                }
            }
            Boolean flag1 = true;
            while (flag1) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("添加成员") && xmlData.contains("com.tencent.mm:id/d05")) {
                    for (int a = 0; a < nodeList.size() - 1; a++) {//寻找 添加成员的按钮
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/d05"))
                                && nodeBean.getContentdesc() != null && nodeBean.getContentdesc().equals("添加成员")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取 添加成员 按钮 的坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  添加成员 开始拉人
                            flag1 = false;
                            break;
                        }
                    }
                } else {
                    wxUtils.adbUpSlide(context);
                }
            }
            wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
            wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
            Boolean flag2 = true;
            int ccc = 0;
            while (flag2) {
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("ZZZ" + fanId)) {
                    String oldXml = xmlData;
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.equals(oldXml)) {
                        flag2 = false;
                        flag1 = false;
                        continue;
                    }
                } else {
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size() - 1; a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode();
                        if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/kq")) && nodeBean2.isEnabled() == true
                                && nodeBean.getText() != null && !wxQunFriendsName.contains(nodeBean.getText()) && nodeBean2.isChecked() == false
                                && !meName.contains(nodeBean.getText())) {
                            meName = meName + nodeBean.getText();
                            if (nodeBean.getText().length() != 12) {
                                continue;
                            } else {
                                String str_type = nodeBean.getText().substring(3 + Integer.valueOf(groupType), 4 + Integer.valueOf(groupType));
                                if (Integer.valueOf(str_type) < Integer.valueOf(addGroupNum)) {
                                    listXY = wxUtils.getXY(nodeBean2.getBounds());// 获取选中框的坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击选中框
                                    ccc++;
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                    xmlData = wxUtils.getXmlData();
                    wxUtils.adbUpSlide(context);
                    String oldXmlData = xmlData;
                    String newXmlData = wxUtils.getXmlData();
                    if (oldXmlData.equals(newXmlData)) {
                        flag2 = false;
                        continue;
                    }
                }
            }
            if (ccc != 0) {
                wxUtils.adbClick(378, 49, 468, 94);//点击确定
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String xmlData3 = wxUtils.getXmlData();
                if (xmlData3.contains("确定")) {
                    nodeList = wxUtils.getNodeList(xmlData3);
                    for (int b = nodeList.size() - 1; b > 0; b--) {
                        NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                        if (nodeBean3.getText() != null && nodeBean3.getText().equals("确定")) {
                            listXY = wxUtils.getXY(nodeBean3.getBounds());
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击确定
                            break;
                        }
                    }
                }
            }
        } else {  //只拉40个人
            Boolean Flag = true;
            String oldQunName = "";
            String oldFansName = "";
            while (Flag) {
                Boolean flag_qunIsOver = true;
                backHome();
                wxUtils.adbClick(153, 822, 207, 847); //点击通讯录
                wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                wxUtils.adbClick(93, 208, 444, 241);//点击群聊
                xmlData = wxUtils.getXmlData();
                List<String> ud = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < ud.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ud.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/a9u") && nodeBean.getText() != null
                            && !oldQunName.contains(nodeBean.getText()) && nodeBean.getText().contains(preGrpName)) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                        oldQunName = oldQunName + nodeBean.getText();
                        flag_qunIsOver = false;
                        break;
                    }
                }
                if (flag_qunIsOver) {
                    ShowToast.show("没有多余的群了", (Activity) context);
                    Flag = false;
                    continue;
                }
                wxUtils.adbClick(408, 36, 480, 108);//点击右上角的人头像
                Boolean Flag1 = true;
                while (Flag1) {
                    int kkk = 0;
                    xmlData = wxUtils.getXmlData();
                    if (!xmlData.contains("聊天信息")) {
                        Flag1 = false;
                        ShowToast.show("界面没有聊天信息，不在拉群的界面", (Activity) context);
                        continue;
                    }
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size() - 1; a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getText() != null && nodeBean.getText().contains("聊天信息")) {
                            String qunPersonNum = nodeBean.getText();
                            String Num = qunPersonNum.substring(5, qunPersonNum.length() - 1);
                            i = Integer.parseInt(Num);//获取到目前群内成员的人数
                            break;
                        }
                    }
                    if (i >= 40) {//群人数已经满了
                        Flag1 = false;
                        continue;
                    }

                    Boolean Flag2 = true;
                    while (Flag2) {
                        xmlData = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData);
                        if (xmlData.contains("添加成员") && xmlData.contains("com.tencent.mm:id/d05")) {
                            for (int a = 0; a < nodeList.size() - 1; a++) {//寻找 添加成员的按钮
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/d05"))
                                        && nodeBean.getContentdesc() != null && nodeBean.getContentdesc().equals("添加成员")) {
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 添加成员 按钮 的坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  添加成员 开始拉人
                                    Flag2 = false;
                                    break;
                                }
                            }
                        } else {
                            wxUtils.adbUpSlide(context);
                        }
                    }
                    int bbb = 0;
                    if (kkk >= 40 - i) {
                        wxUtils.adbClick(378, 49, 468, 94);//点击右上角的确定
                    } else {
                        Boolean Flag3 = true;
                        while (Flag3) {
                            bbb = 0;
                            xmlData = wxUtils.getXmlData();
                            if (xmlData.contains("搜索")) {
                                wxUtils.adbClick(384, 132, 462, 186);//点击搜索
                                wxUtils.adb("input text " + "ZZZ" + fanId);
                                wxUtils.adb("input keyevent 4");
                            }
                            xmlData = wxUtils.getXmlData();
                            nodeList = wxUtils.getNodeList(xmlData);
                            for (int a = 0; a < nodeList.size() - 1; a++) {
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode();
                                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/kq")) && nodeBean2.isEnabled() == true
                                        && nodeBean.getText() != null && !wxQunFriendsName.contains(nodeBean.getText()) && nodeBean2.isChecked() == false
                                        && !oldFansName.contains(nodeBean.getText())) {
                                    oldFansName = oldFansName + nodeBean.getText();
                                    if (nodeBean.getText().length() != 12) {
                                        continue;
                                    } else {
                                        String str_type = nodeBean.getText().substring(3 + Integer.valueOf(groupType), 4 + Integer.valueOf(groupType));
                                        if (Integer.valueOf(str_type) < Integer.valueOf(addGroupNum)) {
                                            listXY = wxUtils.getXY(nodeBean2.getBounds());// 获取选中框的坐标
                                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击选中框
                                            bbb = 1;
                                            kkk++;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (kkk >= 40 - i) {
                                Flag3 = false;
                                wxUtils.adbClick(378, 49, 468, 94);//点击右上角的确定
                                xmlData = wxUtils.getXmlData();
                                if (xmlData.contains("选择联系人") && xmlData.contains("确定")) {
                                    kkk = 0;
                                    wxUtils.adb("input keyevent 4");
                                }
                                break;
                            }
                            if (bbb == 0) {
                                xmlData = wxUtils.getXmlData();
                                String oldXmlData = xmlData;
                                wxUtils.adbUpSlide(context);
                                String newXmlData = wxUtils.getXmlData();
                                if (oldXmlData.equals(newXmlData) || kkk >= 40 - i) {
                                    Flag3 = false;
                                    wxUtils.adbClick(378, 49, 468, 94);//点击右上角的确定
                                }

                            }

                        }

                        if (bbb == 0) {
                            if (kkk < 40 - i) {
                                //已经全部拉完，但群人数还是没满
                                Flag = false;
                            }
                            wxUtils.adbClick(378, 49, 468, 94);//点击右上角的确定
                            kkk = 0;
                            if (xmlData.contains("选择联系人") && xmlData.contains("确定")) {
                                wxUtils.adb("input keyevent 4");
                            }
                            Flag2 = false;
                            Flag1 = false;
                            Flag3 = false;
                        }
                        String xmlData3 = wxUtils.getXmlData();
                        if (xmlData3.contains("确定") && !xmlData3.contains("选择联系人")) {
                            nodeList = wxUtils.getNodeList(xmlData3);
                            for (int b = nodeList.size() - 1; b > 0; b--) {
                                NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                if (nodeBean3.getText() != null && nodeBean3.getText().equals("确定")) {
                                    listXY = wxUtils.getXY(nodeBean3.getBounds());
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击确定

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    /*
     *   老粉丝修改备注
     * */

    private void startAlterName_OldFans(String fanId, String addGroupNum, String
            groupType) {
        String meName = "";
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("000");
        String Type = "";
        boolean bottom = false;//到了底部
        SPUtils.putString(context, "OldFansNameLastfourth", "");//倒数第4位
        String newName = "";//修改完备注的名字
        String newStr = "";//获取到修改备注后的后四位
        SPUtils.putString(context, "StrLastFourth", "");//最后四位
        boolean flag0 = true;
        while (flag0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag0 = false;
            }
        }
        wxUtils.adbClick(306, 36, 378, 108);//搜索
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        cm.setText("ZZZ" + fanId);
        //        wxUtils.adb("input swipe " + 100 + " " + 80 + " " + 100 + " " + 80 + " " + 5000);  //长按EdiText
        wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
        wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adb("input keyevent 4");//返回
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("ZZZ" + fanId)) {
            ShowToast.show("没有对应的老粉丝账号", (Activity) context);
            return;
        }
        //        wxUtils.adb("input keyevent 4");//返回
        xmlData = wxUtils.getXmlData();
        int aa = 0;
        if (xmlData.contains("更多联系人")) {
            aa = 1;
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                        && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                        ) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                    break;
                }
            }
        } else {
            wxUtils.adbUpSlide(context);
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("更多联系人")) {
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                            && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                            ) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                        break;
                    }
                }
            } else {
                wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
            }
        }

        Boolean Flag = true;
        while (Flag) {
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);

            int bb = 0;
            if (xmlData.contains("ZZZ" + fanId)) {
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/kq")
                            && nodeBean.getText() != null && nodeBean.getText().startsWith("ZZZ" + fanId) && !meName.contains(nodeBean.getText())
                            ) {
                        String oldName = "";
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取  的坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                        bb = 1;
                        meName = meName + nodeBean.getText();
                        oldName = nodeBean.getText();
                        wxUtils.adbClick(396, 36, 480, 108);//点击右上角头像
                        xmlData = wxUtils.getXmlData();
                        if (!xmlData.contains("添加成员")) {
                            wxUtils.adbClick(396, 36, 480, 108);//点击右上角头像
                        }
                        wxUtils.adbClick(21, 168, 105, 286);
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        String subStr = "";
                        if (groupType.equals("1")) {
                            Type = "1000";

                        } else if (groupType.equals("2")) {
                            Type = "0100";

                        } else if (groupType.equals("3")) {
                            Type = "0010";

                        } else if (groupType.equals("4")) {
                            Type = "0001";
                        }

                        List<String> remarkList = wxUtils.getNodeList(xmlData);
                        if (xmlData.contains("女")) {
                            sex = 0;
                        } else if (xmlData.contains("男")) {
                            sex = 1;
                        } else {
                            sex = 2;
                        }

                        for (int r = 0; r < remarkList.size(); r++) {
                            nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                            if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                //筛选出好友
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                break;
                            }
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                        if (oldName.length() == 12) {
                            String str_first_four = oldName.substring(0, 4);//截取前4位
                            String str_four = oldName.substring(4, 8);//截取中间四位

                            String str_five = str_four.charAt(0) + "";
                            String str_six = str_four.charAt(1) + "";
                            String str_seven = str_four.charAt(2) + "";
                            String str_eight = str_four.charAt(3) + "";

                            String str_last_four = oldName.substring(8, 12);//截图最后的四位
                            if (groupType.equals("1")) {
                                subStr = oldName.substring(4, 5); //获取到第5位的数据
                            } else if (groupType.equals("2")) {
                                subStr = oldName.substring(5, 6);  // 获取到第6位的数据
                            } else if (groupType.equals("3")) {
                                subStr = oldName.substring(6, 7);  // 获取到第7位的数据

                            } else if (groupType.equals("4")) {
                                subStr = oldName.substring(7, 8);  // 获取到第8位的数据
                            }
                            if (Integer.valueOf(subStr) >= Integer.valueOf(addGroupNum)) {
                                ShowToast.show("超过次数了", (Activity) context);
                                wxUtils.adb("input keyevent 4");//返回
                                wxUtils.adb("input keyevent 4");//返回
                                wxUtils.adb("input keyevent 4");//返回
                                continue;
                            }

                            if (groupType.equals("1")) {
                                Type = String.valueOf(Integer.valueOf(str_five) + 1) + str_six + str_seven + str_eight;

                            } else if (groupType.equals("2")) {
                                Type = str_five + String.valueOf(Integer.valueOf(str_six) + 1) + str_seven + str_eight;

                            } else if (groupType.equals("3")) {
                                Type = str_five + str_six + String.valueOf(Integer.valueOf(str_seven) + 1) + str_eight;

                            } else if (groupType.equals("4")) {
                                Type = str_five + str_six + str_seven + String.valueOf(Integer.valueOf(str_eight) + 1);
                            }
                            wxUtils.adb("input text " + str_first_four + Type + str_last_four);
                            String str12 = SPUtils.getString(context, "StrLastFourth", "");
                            SPUtils.putString(context, "StrLastFourth", str12 + str_last_four);//每修改一次记录最后的四位
                        } else {
                            switch (sex) { //0代表女。   1代表男   2代表性别未知
                                case 0:
                                    int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl" + fanId, 0);
                                    String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                    wxUtils.adb("input text " + "ZZZ" + fanId + Type + "B" + wx_nume_number_new_girl);
                                    SPUtils.put(context, "wx_name_number_girl" + fanId, wx_name_number_girl + 1);
                                    newStr = ("ZZZ" + fanId + Type + "B" + wx_nume_number_new_girl).substring(8, 12); //获取最后四位
                                    String str0 = SPUtils.getString(context, "StrLastFourth", "");
                                    SPUtils.putString(context, "StrLastFourth", str0 + newStr);//每修改一次记录最后的四位
                                    break;
                                case 1:
                                    int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy" + fanId, 0);
                                    String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                    wxUtils.adb("input text " + "ZZZ" + fanId + Type + "A" + wx_nume_number_new_boy);
                                    SPUtils.put(context, "wx_name_number_boy" + fanId, wx_name_number_boy + 1);
                                    newStr = ("ZZZ" + fanId + Type + "A" + wx_nume_number_new_boy).substring(8, 12); //获取最后四位
                                    String str1 = SPUtils.getString(context, "StrLastFourth", "");
                                    SPUtils.putString(context, "StrLastFourth", str1 + newStr);//每修改一次记录最后的四位
                                    break;
                                case 2:
                                    int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c" + fanId, 0);
                                    String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                    wxUtils.adb("input text " + "ZZZ" + fanId + Type + "C" + wx_nume_number_c);
                                    SPUtils.put(context, "wx_name_number_c" + fanId, wx_name_number_c + 1);
                                    newStr = ("ZZZ" + fanId + Type + "C" + wx_nume_number_c).substring(8, 12); //获取最后四位
                                    String str2 = SPUtils.getString(context, "StrLastFourth", "");
                                    SPUtils.putString(context, "StrLastFourth", str2 + newStr);//每修改一次记录最后的四位
                                    break;
                            }
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                        wxUtils.adb("input keyevent 4");//返回
                        wxUtils.adb("input keyevent 4");//返回
                        wxUtils.adb("input keyevent 4");//返回
                    }
                }
            }

            if (bb == 0) {
                String oldXml = xmlData;
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (oldXml.equals(xmlData)) {
                    ShowToast.show("滑到底部了", (Activity) context);
                    Flag = false;
                    continue;
                }
            }
        }

    }


    /*
     *   新粉丝修改备注
     * */
    private void startAlterName_NewFans(String fanId, String groupType) {
        String meName = "";
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("000");
        String Type = "";
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        boolean bottom = false;//到了底部
        if (groupType.equals("1")) {
            Type = "1000";
        } else if (groupType.equals("2")) {
            Type = "0100";
        } else if (groupType.equals("3")) {
            Type = "0010";
        } else if (groupType.equals("4")) {
            Type = "0001";
        }
        boolean flag0 = true;
        while (flag0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag0 = false;
            }
        }
        w:
        while (true) {
            while (true) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                    break w;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else {
                    break;
                }
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            a:
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc())) {
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注

                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        meName = nodeBean.getContentdesc();
                        continue;
                    }
                    if (xmlData.contains("女")) {
                        sex = 0;
                    } else if (xmlData.contains("男")) {
                        sex = 1;
                    } else {
                        sex = 2;
                    }
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    List<String> remarkList = wxUtils.getNodeList(xmlData);
                    for (int r = 0; r < remarkList.size(); r++) {
                        nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                        if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                            //筛选出好友
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                            break;
                        }
                    }
                    xmlData = wxUtils.getXmlData();

                    if (xmlData.contains("备注信息") && xmlData.contains("完成")) {

                    } else {
                        continue w;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                    switch (sex) { //0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl" + fanId, 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                            wxUtils.adb("input text " + "ZZZ" + fanId + Type + "B" + wx_nume_number_new_girl);
                            SPUtils.put(context, "wx_name_number_girl" + fanId, wx_name_number_girl + 1);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy" + fanId, 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                            wxUtils.adb("input text " + "ZZZ" + fanId + Type + "A" + wx_nume_number_new_boy);
                            SPUtils.put(context, "wx_name_number_boy" + fanId, wx_name_number_boy + 1);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c" + fanId, 0);
                            String wx_nume_number_c = df.format(wx_name_number_c + 1);
                            wxUtils.adb("input text " + "ZZZ" + fanId + Type + "C" + wx_nume_number_c);
                            SPUtils.put(context, "wx_name_number_c" + fanId, wx_name_number_c + 1);
                            break;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    //  LogUtils.d(nodeList.get(a));
                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                        wxUtils.adb("input keyevent 4");
                    }
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("发现")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc())) {
                    continue w;
                } else if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && nodeBean.getContentdesc().startsWith("ZZZ")) {
                    zzzNum++;
                }
            }

            if (!bottom) {
                if (zzzNum >= 9) {
                    wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y357, R.dimen.x320, R.dimen.y365);
                } else {
                    wxUtils.adbUpSlide(context);//向上滑动
                }
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                ShowToast.show("修改备注完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("位联系人")) {//判断是否到达底部
                bottom = true;
            }
        }

    }

    //进入朋友圈的购物页面
    private void goToShopping() {
        wxUtils.adbClick(282, 822, 318, 847);//发现
        wxUtils.adbUpSlide(context);
        wxUtils.adbUpSlide(context);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        nodeList = wxUtils.getNodeList(xmlData);
        for (int b = 0; b < nodeList.size(); b++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
            if (nodeBean.getText() != null && nodeBean.getText().equals("购物") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("android:id/title")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取购物坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                break;
            }
        }
        //购物界面停留
        try {
            Thread.sleep(1500);
            LogUtils.d("停留时间" + standingTimeShop + "秒");
            Thread.sleep(standingTimeShop * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adb("input keyevent 4");
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("你要关闭购物页面?")) {
            wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y230, R.dimen.x264, R.dimen.y250);
        }
    }

    //进入朋友圈的游戏页面
    private void goToPlayGame() {
        wxUtils.adbClick(282, 822, 318, 847);//发现
        wxUtils.adbUpSlide(context);
        wxUtils.adbUpSlide(context);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        nodeList = wxUtils.getNodeList(xmlData);
        for (int b = 0; b < nodeList.size(); b++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
            if (nodeBean.getText() != null && nodeBean.getText().equals("游戏") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("android:id/title")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取购物坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                break;
            }
        }
        //购物界面停留
        try {
            Thread.sleep(1500);
            LogUtils.d("停留时间" + standingTimeGame + "秒");
            Thread.sleep(standingTimeGame * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adb("input keyevent 4");
    }

    //把新增好友拉入群中

    /**
     * @param callback 完成回调
     * @param user     第几个用户
     */
    private void laNewFriendsToQun(final PullNewFriendCallback callback, final int user) {
        String personNum = SPUtils.getString(context, "QunPersonNum", "0");
        int personNum_int = Integer.valueOf(personNum);
        String wxQunFriendsName = "";
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        boolean flag0 = true;
        while (flag0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag0 = false;
            }
        }
        SPUtils.putInt((Activity) context, "NewFriendsCount", 0);
        SPUtils.putInt(context, "TimeOver", 1);
        wxUtils.openWx((Activity) context);//打开微信
        wxUtils.adbClick(153, 822, 207, 847);
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        startAlterName2("YYY0");
        wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
        wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
        int newfriendsaccount = 0;
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("YYY0")) {
            ShowToast.show("没有新好友!", (Activity) context);
        } else {
            ShowToast.show("有新好友!", (Activity) context);
            newfriendsaccount = 1;
        }
        boolean flag_0 = true;
        while (flag_0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag_0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(282, 822, 318, 847); //点击发现
                flag_0 = false;
            }
        }
        boolean QunIsFull = true;
        while (QunIsFull && (newfriendsaccount != 0)) {
            SPUtils.putString(context, "groupName", "");

            String url = URLS.wxNewFriendsToQun();
            HashMap<String, String> params = new HashMap<>();
            params.put("number", personNum_int + "");

            boolean netFlag = true;
            int number = 0;
            while (netFlag)
                try {
                    number++;
                    Thread.sleep(20000);
                    if (number > 30) return;
                    Response execute = OkHttpUtils.get().params(params).url(url).build().execute();
                    if (execute.code() == 200) {
                        String string = execute.body().string();
                        WxNewFriendsToQunBean wxNewFriendsToQunBean = GsonUtil.parseJsonWithGson(string, WxNewFriendsToQunBean.class);
                        pic_id = Integer.parseInt(wxNewFriendsToQunBean.getData().getId());//获取到图片id
                        if (!TextUtils.isEmpty(wxNewFriendsToQunBean.getData().getQr_code_address()))
                            if (downFlockImg(wxNewFriendsToQunBean.getData().getQr_code_address(), 0))
                                netFlag = false;
                        SPUtils.putString(context, "groupName", wxNewFriendsToQunBean.getData().getWx_group_name());
                        SPUtils.putInt(context, "picDownloadSuccess", 1);
                    } else {
                        ShowToast.show("网络异常未获取到二维码图片！", (Activity) context);
                    }
                } catch (Exception e) {
                    ShowToast.show("网络异常未获取到二维码图片！", (Activity) context);
                    e.printStackTrace();
                }

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            backHome();
            wxUtils.adbClick(282, 822, 318, 847); //点击发现
            wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsTimeLineUI");
            wxUtils.adbClick(400, 550, 400, 550);
            wxUtils.adbClick(400, 550, 400, 550);//连续点击两次个人头像
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().equals("发消息")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  发消息
                    break;
                }
            }
            wxUtils.adbClick(402, 782, 474, 854);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("相册") && !xmlData.contains("拍摄")) {
                wxUtils.adbClick(402, 782, 474, 854);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            wxUtils.adbClick(61, 612, 95, 635);
            wxUtils.adbClick(50, 820, 50, 820);//点击左下角
            boolean ccc = true;
            boolean ddd = true;

            while (ccc) {
                String xmlData_picture = wxUtils.getXmlData();
                if (xmlData_picture.contains("com.tencent.mm:id/d1r") && xmlData_picture.contains("ykimages")) {
                    List<String> pictureList = wxUtils.getNodeList(xmlData_picture);
                    for (int c = 0; c < pictureList.size(); c++) {
                        NodeXmlBean.NodeBean pictureBean = wxUtils.getNodeXmlBean(pictureList.get(c)).getNode();
                        if (pictureBean != null && pictureBean.getResourceid() != null && "com.tencent.mm:id/d1r".equals(pictureBean.getResourceid())
                                && pictureBean.getText() != null && pictureBean.getText().equals("ykimages")) {
                            listXY = wxUtils.getXY(pictureBean.getBounds());//获取坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击ykimages 文件夹
                            ccc = false;
                            break;
                        }
                    }
                } else {
                    wxUtils.adbUpSlide(context);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();
                    if (!xmlData_picture.contains("com.tencent.mm:id/d1r") || !xmlData_picture.contains("ykimages")) {
                        ccc = false;
                        ddd = false;
                    }
                }
            }
            if (!ddd) {
                ShowToast.show("未下载到图片，重新开始下载", (Activity) context);
                List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                String uid = SPUtils.getString(context, "uid", "0000");
                QunMessageBean messageBean = new QunMessageBean(0 + "", pic_id + "", uid, "" + personNum_int);
                mQunMessageBeanList.add(messageBean);
                String str = new Gson().toJson(mQunMessageBeanList);
                LogUtils.d("JSON" + str.toString());
                //                sendWxQunMessage(str);
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("json", str);
                try {
                    String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("图片和视频")) {
                wxUtils.adbClick(78, 119, 108, 149);//选中图片
                wxUtils.adbClick(378, 49, 468, 94);//点击发送
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                continue;
            }
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("com.tencent.mm:id/aec")) {
                continue;
            }
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size() - 1; a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/aec"))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 群名片图片 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击   群名片图片
                    break;
                }
            }
            wxUtils.adb("input swipe " + 240 + " " + 400 + " " + 200 + " " + 400 + " " + 4000);  //长按EdiText
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size() - 1; a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && (nodeBean.getText().equals("识别图中二维码"))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 群名片图片 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击识别图中二维码
                    break;
                }
            }
            if (!xmlData.contains("识别图中二维码")) {
                List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                String uid = SPUtils.getString(context, "uid", "0000");
                QunMessageBean messageBean = new QunMessageBean(0 + "", pic_id + "", uid + "", personNum_int + "");
                mQunMessageBeanList.add(messageBean);
                String str = new Gson().toJson(mQunMessageBeanList);
                LogUtils.d("JSON" + str.toString());
                groupPeopleNumber = 0;
                //               sendWxQunMessage(str);

                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("json", str);
                try {
                    String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            wxUtils.adbClick(90, 481, 258, 514);
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //无法确定具体坐标，大概在四个范围
            wxUtils.adbClick(240, 600, 240, 600);//点击加入群聊
            wxUtils.adbClick(240, 550, 240, 550);//点击加入群聊
            wxUtils.adbClick(240, 500, 240, 500);//点击加入群聊
            wxUtils.adbClick(240, 650, 240, 650);//点击加入群聊
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Boolean Flag = true;
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("(") || !xmlData.contains(")") || !xmlData.contains("加入群聊")) {
                LogUtils.d("二维码群图片没下载下来");
                Flag = false;
                continue;
            }
            int kkk = 0;
            while (Flag) {
                wxUtils.adbClick(408, 36, 480, 108);//点击右上角的人头像
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("(") || !xmlData.contains(")") || !xmlData.contains("聊天信息")) {
                    LogUtils.d("二维码群图片没下载下来");
                    Flag = false;
                    continue;
                }
                int i = 0;
                Boolean flag1 = true;
                while (flag1) {
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size() - 1; a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getText() != null && nodeBean.getText().contains("聊天信息")) {
                            String qunPersonNum = nodeBean.getText();
                            String Num = qunPersonNum.substring(5, qunPersonNum.length() - 1);
                            i = Integer.parseInt(Num);//获取到目前群内成员的人数
                            kkk++;
                            break;
                        }
                    }
                    if (i < personNum_int) {
                        for (int a = 0; a < nodeList.size() - 1; a++) {//寻找 添加成员的按钮
                            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                            if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/d05"))
                                    && nodeBean.getContentdesc() != null && nodeBean.getContentdesc().equals("添加成员")) {
                                flag1 = false;
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取 添加成员 按钮 的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  添加成员 开始拉人
                                break;
                            }
                        }
                        if (flag1 == true) {//如果页面没有添加成员 “+”存在
                            wxUtils.adbUpSlide(context);
                        }
                    } else if (i >= personNum_int) {
                        flag1 = false;
                    }
                }
                if (i >= personNum_int) {
                    QunIsFull = true;
                    Flag = false; //说明已经满40个人了
                    wxUtils.adbUpSlide(context);
                    Boolean qunNameIsVisibled = false;
                    while (!qunNameIsVisibled) {
                        String newXmlData = wxUtils.getXmlData();
                        if (newXmlData.contains("群聊名称") && newXmlData.contains("群二维码")) {
                            qunNameIsVisibled = true;
                        } else {
                            wxUtils.adbUpSlide(context);
                        }
                    }
                    wxUtils.screenShot(); //截图
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbUpSlide(context);
                    wxUtils.adbUpSlide(context);
                    wxUtils.adbUpSlide(context); //滑到最底部了
                    List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                    String uid = SPUtils.getString(context, "uid", "0000");
                    QunMessageBean messageBean = new QunMessageBean(i - 1 + "", pic_id + "", uid, personNum_int + "");
                    mQunMessageBeanList.add(messageBean);
                    String str = new Gson().toJson(mQunMessageBeanList);
                    LogUtils.d("JSON" + str.toString());
                    groupPeopleNumber = i - 1;
                    //                    sendWxQunMessage(str);
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("json", str);
                    try {
                        String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbClick(300, 490, 396, 535); //点击确定
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String newXmlData = wxUtils.getXmlData();
                    if (newXmlData.contains("删除并退出")) {
                        wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbClick(300, 490, 396, 535); //点击确定
                    }
                    sendQunSrceenShot();
                    backHome();
                }

                boolean flag = true;
                if (i >= personNum_int) {// 满40个人后就不用拉人了
                    flag = false;
                }
                int aaaa = 0;
                Boolean flag3 = false;
                while (flag) {
                    xmlData = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size() - 1; a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(a + 1)).getNode();
                        if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/kq")) && nodeBean2.isEnabled() == true
                                && nodeBean.getText() != null && !wxQunFriendsName.contains(nodeBean.getText()) && nodeBean2.isChecked() == false) {
                            if (nodeBean.getText().startsWith("YYY0")) {
                                String wxName = nodeBean.getText();
                                aaaa++;
                                wxQunFriendsName = wxQunFriendsName + wxName;
                                listXY = wxUtils.getXY(nodeBean2.getBounds());// 获取选中框的坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击选中框
                                if (aaaa >= personNum_int - i) {
                                    flag = false;
                                    wxUtils.adbClick(368, 49, 468, 94);//点击确定
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    String xmlData3 = wxUtils.getXmlData();
                                    if (xmlData3.contains("确定")) {
                                        nodeList = wxUtils.getNodeList(xmlData3);
                                        for (int b = nodeList.size() - 1; b > 0; b--) {
                                            NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                            if (nodeBean3.getText() != null && nodeBean3.getText().equals("确定")) {
                                                listXY = wxUtils.getXY(nodeBean3.getBounds());
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击确定
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            } else if (nodeBean.getText().startsWith("ZZZ") || nodeBean.getText().startsWith("zzz") || nodeBean.getText().startsWith("YYY1")) {
                                flag3 = true;//已经检测到ZZZ了
                                break;
                            }

                        }
                    }
                    if (flag == true && aaaa < personNum_int - i) {
                        wxUtils.adbUpSlide(context);//向上滑动
                        String xmlData2 = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData2);

                        if (xmlData2.equals(xmlData) || (flag3 == true)) {
                            QunIsFull = true;
                            flag = false;
                            // 已经全部拉完了
                            if (aaaa == 0) {
                                wxUtils.adb("input keyevent 4");//返回
                            } else {
                                wxUtils.adbClick(378, 49, 468, 94);//点击确定
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String xmlData3 = wxUtils.getXmlData();
                            if (xmlData3.contains("确定")) {
                                nodeList = wxUtils.getNodeList(xmlData3);
                                for (int b = nodeList.size() - 1; b > 0; b--) {
                                    NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                    if (nodeBean3.getText() != null && nodeBean3.getText().equals("确定")) {
                                        listXY = wxUtils.getXY(nodeBean3.getBounds());
                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));// 点击确定
                                        break;
                                    }
                                }
                            }
                            xmlData = wxUtils.getXmlData();
                            nodeList = wxUtils.getNodeList(xmlData);
                            for (int a = 0; a < nodeList.size() - 1; a++) {
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                                if (nodeBean.getText() != null && nodeBean.getText().contains("聊天信息")) {
                                    String qunPersonNum = nodeBean.getText();
                                    String Num = qunPersonNum.substring(5, qunPersonNum.length() - 1);
                                    i = Integer.parseInt(Num);//获取到目前群内成员的人数
                                    Log.d("群内人数", "群内人数:" + i);
                                    // 将群 人数上传到服务器，再退出该群
                                    List<QunMessageBean> mQunMessageBeanList = new ArrayList<>();
                                    String uid = SPUtils.getString(context, "uid", "0000");
                                    QunMessageBean messageBean = new QunMessageBean((i - 1) + "", pic_id + "", uid + "", personNum_int + "");
                                    mQunMessageBeanList.add(messageBean);
                                    String str = new Gson().toJson(mQunMessageBeanList);
                                    LogUtils.d("JSON" + str.toString());
                                    groupPeopleNumber = i - 1;
                                    //                                  sendWxQunMessage(str);
                                    Map<String, String> messageMap = new HashMap<>();
                                    messageMap.put("json", str);
                                    try {
                                        String response = NetUtils.post(URLS.wxNewFriendsToQunUpData(), messageMap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                            if (i <= personNum_int) {
                                QunIsFull = false;// 群没有满，就不用再申请二维码了
                                Flag = false;
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context);
                                wxUtils.adbUpSlide(context); //滑到最底部了
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                                SPUtils.putInt(context, "FristFull", 1);
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adbClick(300, 490, 396, 535); //点击确定
                                String newXmlData = wxUtils.getXmlData();
                                if (newXmlData.contains("删除并退出")) {
                                    wxUtils.adbClick(18, 734, 462, 806); //点击 删除并退出
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    wxUtils.adbClick(300, 490, 396, 535); //点击确定
                                }
                            }
                        }
                    }
                }
            }
        }
        startAlterName3("", wxQunFriendsName);
    }

    /**
     * 发送截图到指定群
     *
     * @return
     */

    private void sendQunSrceenShot() {
        Boolean Flag = true;
        while (Flag) {
            backHome();
            wxUtils.adbClick(153, 822, 207, 847);//点击通讯录
            wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
            wxUtils.adbClick(93, 208, 444, 241);//点击群聊
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("群聊")) {
                Flag = false;
            }
        }
        String qunScreenShotXmlData = wxUtils.getXmlData();
        List<String> qunScreenShotXmlDataList = wxUtils.getNodeList(qunScreenShotXmlData);
        for (int iii = 0; iii < qunScreenShotXmlDataList.size() - 1; iii++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(qunScreenShotXmlDataList.get(iii)).getNode();
            if (nodeBean != null && nodeBean.getText() != null && nodeBean.getText().contains("群截图") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/a9u")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3)); //点击进群
                break;
            }
        }
        wxUtils.adbClick(402, 782, 474, 854);//点击加号
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        wxUtils.adbClick(50, 550, 50, 550);//点击相册
        for (int k = 0; k < 5; k++) {
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("图片和视频")) {
                wxUtils.adbClick(50, 550, 50, 550);//点击相册
            } else {
                break;
            }
        }
        wxUtils.adbClick(100, 820, 100, 820);//点击左下角
        boolean ccc = true;
        while (ccc) {
            String xmlData_picture = wxUtils.getXmlData();
            if (xmlData_picture.contains("com.tencent.mm:id/d1r") && xmlData_picture.contains("BBB")) {
                List<String> pictureList = wxUtils.getNodeList(xmlData_picture);
                for (int c = 0; c < pictureList.size(); c++) {
                    NodeXmlBean.NodeBean pictureBean = wxUtils.getNodeXmlBean(pictureList.get(c)).getNode();
                    if (pictureBean != null && pictureBean.getResourceid() != null && "com.tencent.mm:id/d1r".equals(pictureBean.getResourceid())
                            && pictureBean.getText() != null && pictureBean.getText().equals("BBB")) {
                        listXY = wxUtils.getXY(pictureBean.getBounds());//获取坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击BBB 文件夹
                        ccc = false;
                        break;
                    }
                }
            } else {
                wxUtils.adbUpSlide(context);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                xmlData = wxUtils.getXmlData();
                if (!xmlData_picture.contains("com.tencent.mm:id/d1r") || !xmlData_picture.contains("BBB")) {
                    ccc = false;
                }
            }
        }
        wxUtils.adbWxClick(92, 129);  //选择图片
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("发送(1/9)")) {
            wxUtils.adbClick(360, 49, 468, 94);
        } else {
            wxUtils.adbWxClick(92, 129);  //选择图片
            wxUtils.adbClick(360, 49, 468, 94);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        wxUtils.adbClick(100, 820, 100, 820); //点击EditText输入框
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        String groupName = SPUtils.getString(context, "groupName", "");
        cm.setText(groupName);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        wxUtils.adb("input swipe " + 200 + " " + 430 + " " + 200 + " " + 430 + " " + 1000);  //长按EdiText
        wxUtils.adbClick(90, 360, 90, 360); //点击粘贴
        wxUtils.adbClick(405, 411, 471, 459); //点击发送
        FileUtils.deleteDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/BBB");
        Log.d("文件删除成功", "");
    }

    /**
     * 将拉群后的 将之前的拉过群的YYY开头的，再改成ZZZ开头，YYY开头但没拉进群的不改备注
     *
     * @return
     */
    private void startAlterName3(String zzz, String wxQunFriendsName) {
        DecimalFormat df = new DecimalFormat("0000");
        String meName = "";
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        zzz = "YYY1";
        boolean Flag = true;
        int bbb = 0;
        while (Flag) {
            int aaa = 0;
            boolean flag0 = true;
            while (flag0) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    flag0 = false;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                    flag0 = false;
                }
            }
            wxUtils.adbClick(153, 822, 207, 847); //点击通讯录
            wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("YYY")) {
                wxUtils.adbClick(460, 728, 460, 728);
                xmlData = wxUtils.getXmlData();
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_"))
                        && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信")
                        && !nodeBean.getContentdesc().equals("文件传输助手") && nodeBean.getContentdesc().startsWith("YYY0") && wxQunFriendsName.contains(nodeBean.getContentdesc()) && !meName.equals(nodeBean.getContentdesc())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        meName = meName + nodeBean.getContentdesc();
                        continue;
                    }
                    if (xmlData.contains("女")) {
                        sex = 0;
                    } else if (xmlData.contains("男")) {
                        sex = 1;
                    } else {
                        sex = 2;
                    }
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    List<String> remarkList = wxUtils.getNodeList(xmlData);
                    for (int r = 0; r < remarkList.size(); r++) {
                        nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                        if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                            //筛选出好友
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                            aaa++;
                            break;
                        }
                    }
                    xmlData = wxUtils.getXmlData();
                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                    switch (sex) {//0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl", 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                            wxUtils.adb("input text " + zzz + "B" + wx_nume_number_new_girl);
                            SPUtils.put(context, "wx_name_number_girl", wx_name_number_girl + 1);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy", 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                            wxUtils.adb("input text " + zzz + "A" + wx_nume_number_new_boy);
                            SPUtils.put(context, "wx_name_number_boy", wx_name_number_boy + 1);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c", 0);
                            String wx_nume_number_c = df.format(wx_name_number_c + 1);
                            wxUtils.adb("input text " + zzz + "C" + wx_nume_number_c);
                            SPUtils.put(context, "wx_name_number_c", wx_name_number_c + 1);
                            break;
                    }
                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    //  LogUtils.d(nodeList.get(a));
                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                        wxUtils.adb("input keyevent 4");
                    }

                    break;
                }

            }
            if (aaa == 0) { //证明已经全部修改成功了
                wxUtils.adb("input keyevent KEYCODE_HOME");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.openWx((Activity) context);//打开微信
                wxUtils.adb("input keyevent KEYCODE_HOME");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.openWx((Activity) context);//打开微信
                wxUtils.adbClick(153, 822, 207, 847); //点击通讯录
                wxUtils.adbClick(460, 725, 460, 725); //直接点击右边侧滑的 Y
                bbb++;
                if (bbb == 3) {
                    Flag = false;
                }
            }
        }
    }

    private void sendWxQunMessage(String str) {
        Map<String, String> params = new HashMap<>();
        params.put("json", str.replace("\\", ""));
        try {
            Response execute = OkHttpUtils.post().params(params).url(URLS.wxNewFriendsToQunUpData()).build().execute();
            if (execute.code() == 200) {
                String string = execute.body().string();
                if (string.contains("200")) {
                    SPUtils.putInt(context, "commitSuccess", 1);
                    return;
                }
            }
            SPUtils.putInt(context, "commitSuccess", 2);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void transmitWxFriendsMessageFlock(String arg0, String crowd, String arg2) {
        if (arg2 == null) {
            arg2 = "1";
        }
        String qunName = "" + crowd;
        boolean flag0 = true;
        while (flag0) {
            boolean flag2 = true;
            boolean flag = true;
            while (flag) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我")) {
                    // 证明已经在微信主页了
                    wxUtils.adbClick(153, 822, 207, 847); // 点击通讯录
                    flag = false;
                } else {
                    wxUtils.adb("input keyevent 4");//返回
                }
            }

            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().equals("群聊")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                        wxUtils.adb("input keyevent 4");
                        ShowToast.show("没有设备群，任务中断", (Activity) context);
                        return;
                    }
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();
            //进入指定群发消息
            boolean isExist = false;
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/a9u"))) {
                    LogUtils.d(crowd + "当前群=" + nodeBean.getText());
                    if (!(crowd != null && crowd.equals(nodeBean.getText()))) {
                        continue;
                    }
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                ShowToast.show("没有设备群，任务中断", (Activity) context);
                return;
            }
            wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
            xmlData = wxUtils.getXmlData();
            List<String> wxQunMessageList = wxUtils.getNodeList(xmlData);
            for (int i = wxQunMessageList.size() - 1; i > 0; i--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(wxQunMessageList.get(i)).getNode();
                if (nodeBean.getResourceid() != null && ((nodeBean.getResourceid().equals("com.tencent.mm:id/if")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a7k")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a6d")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a5c")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a5u")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a5r")))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取最后一条消息的坐标
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    break;
                }
            }
            int count = Integer.parseInt(arg2);
            int aaa = 0;
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size(); a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().equals("更多") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/fa")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//  点击更多
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));
                    break;
                }
            }

            if (count > 1) {
                while (flag2) {
                    xmlData = wxUtils.getXmlData();
                    List<String> wxQunMessageList2 = wxUtils.getNodeList(xmlData);
                    for (int i = wxQunMessageList2.size() - 1; i > 0; i--) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(wxQunMessageList2.get(i)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/o") && !nodeBean.isChecked()) {
                            //选择没有被选择的
                            listXY = wxUtils.getXY(nodeBean.getBounds());//  选中框
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));
                            aaa++;

                        }
                        if (aaa == count - 1) {
                            break;
                        }
                    }
                    if (aaa < count - 1) {
                        wxUtils.adb("input swipe 200 630 200 200");//向下滑动
                    } else {
                        flag2 = false;
                    }
                }
            }

            wxUtils.adbClick(42, 822, 78, 847); //点击左下角的转发
            wxUtils.adbClick(90, 391, 186, 424);// 点击逐条转发
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("多选")) {
                wxUtils.adbClick(378, 49, 468, 94);//点击右上角切换到 单选
            }
            wxUtils.adbClick(18, 201, 444, 279); //点击 更多联系人
            wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
            int wxQunCount = 0;
            boolean flag3 = true;
            while (flag3) {
                xmlData = wxUtils.getXmlData();
                List<String> wxQunMessageList3 = wxUtils.getNodeList(xmlData);
                for (int i = 0; i < wxQunMessageList3.size() - 1; i++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(wxQunMessageList3.get(i)).getNode(); //名字
                    NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(wxQunMessageList3.get(i + 1)).getNode();//勾选框
                    if (nodeBean.getText() != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/j2") && !nodeBean2.isChecked()
                            && !qunName.contains(nodeBean.getText())) {
                        qunName = qunName + nodeBean.getText();
                        listXY = wxUtils.getXY(nodeBean.getBounds());//  选中框
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3)); //点击选中框
                        wxQunCount++;
                    }
                    if (wxQunCount == 9) {
                        break;
                    }
                }
                String oldXmlData = xmlData;
                if (wxQunCount < 9) {
                    wxUtils.adbUpSlide(context);//向上滑动
                }
                xmlData = wxUtils.getXmlData();
                if (xmlData.equals(oldXmlData) || wxQunCount == 9) {
                    flag3 = false;
                }
            }

            if (wxQunCount == 0) {
                //证明没有勾选一个群,没有满足要求的群了
                flag0 = false;//4个返回
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                ShowToast.show("群发好友任务完成", (Activity) context);
            }
            wxUtils.adbClick(378, 49, 468, 94);//点击确定
            wxUtils.adbClick(378, 49, 468, 94);//点击发送
            xmlData = wxUtils.getXmlData();
            List<String> wxQunMessageList4 = wxUtils.getNodeList(xmlData);
            for (int i = wxQunMessageList4.size() - 1; i > 3; i--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(wxQunMessageList4.get(i)).getNode(); //
                if (nodeBean.getText() != null && nodeBean.getText().equals("发送") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/abz")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3)); //点击发送
                    break;
                }
            }
        }
    }

    private void transmitWxQunMessageFlock(String arg0, String crowd, String arg2) {
        if (arg2 == null) {
            arg2 = "1";
        }
        String qunName = "" + crowd;
        boolean flag0 = true;
        while (flag0) {
            boolean flag2 = true;
            boolean flag = true;
            while (flag) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我")) {
                    // 证明已经在微信主页了
                    wxUtils.adbClick(153, 822, 207, 847); // 点击通讯录
                    flag = false;
                } else {
                    wxUtils.adb("input keyevent 4");//返回
                }
            }

            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().equals("群聊")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                        wxUtils.adb("input keyevent 4");
                        ShowToast.show("没有设备群，任务中断", (Activity) context);
                        return;
                    }
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();
            //进入指定群发消息
            boolean isExist = false;
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/a1") || nodeBean.getResourceid().equals("com.tencent.mm:id/a1y"))) {
                    LogUtils.d(crowd + "当前群=" + nodeBean.getText());
                    if (!(crowd != null && crowd.equals(nodeBean.getText()))) {
                        continue;
                    }
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                ShowToast.show("没有设备群，任务中断", (Activity) context);
                return;
            }
            wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
            xmlData = wxUtils.getXmlData();
            List<String> wxQunMessageList = wxUtils.getNodeList(xmlData);
            for (int i = wxQunMessageList.size() - 1; i > 0; i--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(wxQunMessageList.get(i)).getNode();
                if (nodeBean.getResourceid() != null && ((nodeBean.getResourceid().equals("com.tencent.mm:id/if")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a7k")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a6d")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a5c")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a5u")
                        || nodeBean.getResourceid().equals("com.tencent.mm:id/a5r")))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取最后一条消息的坐标
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    break;
                }
            }
            int count = Integer.parseInt(arg2);
            int aaa = 0;
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size(); a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                if (nodeBean.getText() != null && nodeBean.getText().equals("更多") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/fa")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//  点击更多
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));
                    break;
                }
            }

            if (count > 1) {
                while (flag2) {
                    xmlData = wxUtils.getXmlData();
                    List<String> wxQunMessageList2 = wxUtils.getNodeList(xmlData);
                    for (int i = wxQunMessageList2.size() - 1; i > 0; i--) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(wxQunMessageList2.get(i)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/o") && !nodeBean.isChecked()) {
                            //选择没有被选择的
                            listXY = wxUtils.getXY(nodeBean.getBounds());//  选中框
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));
                            aaa++;

                        }
                        if (aaa == count - 1) {
                            break;
                        }
                    }
                    if (aaa < count - 1) {
                        wxUtils.adb("input swipe 200 630 200 200");//向下滑动
                    } else {
                        flag2 = false;
                    }
                }
            }

            wxUtils.adbClick(42, 822, 78, 847); //点击左下角的转发
            wxUtils.adbClick(90, 391, 186, 424);// 点击逐条转发
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("多选")) {
                wxUtils.adbClick(378, 49, 468, 94);//点击右上角切换到 单选
            }
            wxUtils.adbClick(18, 201, 444, 279); //点击 更多联系人
            wxUtils.adbClick(18, 204, 444, 282);// 点击 选择群聊

            wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
            xmlData = wxUtils.getXmlData();
            int wxQunCount = 0;
            List<String> wxQunMessageList3 = wxUtils.getNodeList(xmlData);
            for (int i = wxQunMessageList3.size() - 1; i > 3; i--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(wxQunMessageList3.get(i)).getNode(); //多选框
                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(wxQunMessageList3.get(i - 2)).getNode();//名字
                if (nodeBean2.getText()
                        != null && nodeBean2.getResourceid() != null && nodeBean2.getResourceid().equals("com.tencent.mm:id/b6e") && !nodeBean.isChecked()
                        && !qunName.contains(nodeBean2.getText())) {
                    qunName = qunName + nodeBean2.getText();
                    listXY = wxUtils.getXY(nodeBean.getBounds());//  选中框
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3)); //点击选中框
                    wxQunCount++;
                }
                if (wxQunCount == 9) {
                    break;
                }
            }
            if (wxQunCount == 0) {
                //证明没有勾选一个群,没有满足要求的群了
                flag0 = false;//4个返回
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input keyevent 4");
                ShowToast.show("群发任务任务完成", (Activity) context);
            }
            wxUtils.adbClick(378, 49, 468, 94);//点击确定
            wxUtils.adbClick(378, 49, 468, 94);//点击确定
            wxUtils.adbClick(378, 49, 468, 94);//点击发送
            xmlData = wxUtils.getXmlData();
            List<String> wxQunMessageList4 = wxUtils.getNodeList(xmlData);
            for (int i = wxQunMessageList4.size() - 1; i > 3; i--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(wxQunMessageList4.get(i)).getNode(); //
                if (nodeBean.getText() != null && nodeBean.getText().equals("发送") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/abz")) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3)); //点击发送
                    break;
                }
            }


        }
    }

    private void statisticsMessage() {
        //zhangshuai
        xmlData = wxUtils.getXmlData();
        boolean bottom = false;//到了底部
        String endData = "";
        String meName = "";

        String path = "";
        String strMark = "";
        String fileName = "";
        String filePath = "";
        String text = "";
        String fileUrl = "";
        if (!xmlData.contains("通讯录")) {
            wxUtils.adb("input keyevent KEYCODE_HOME");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.openWx((Activity) context);//打开微信
        }
        wxUtils.adbClick(150, 800, 200, 820);//点击通讯录
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("新的朋友")) {//在通讯录界面，但是需要滑动到最顶端
            wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        }
        xmlData = wxUtils.getXmlData();
        w:
        while (true) {
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            a:
            for (int a = 0; a < nodeList.size(); a++) {

                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/j_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !meName.contains(nodeBean.getContentdesc())) {

                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    meName = meName + nodeBean.getContentdesc();//点击之后就记录当前点击的内容，下次进来就不会重复点击了
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        continue;
                    }
                    StatisticsWxFriends(xmlData);
                    wxUtils.adb("input keyevent 4");
                    //                    wxUtils.adbClick(150, 800, 200, 820);//点击通讯录
                    //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_s())) {
                        start = 60;
                    } else {
                        //start = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_s());
                        start = 5;
                        LogUtils.e("start2" + start);
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_e())) {
                        end = 200;
                    } else {
                        //                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_e());
                        end = 10;


                        LogUtils.e("end2" + end);
                    }
                    int timeSleep = random.nextInt(end - start + 1) + start;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (!xmlData.contains("发现") && !xmlData.contains("com.tencent.mm:id/j_")) {
                ShowToast.show("任务被中断，发消息任务", (Activity) context);
                break w;
            }


            if (!bottom) {
                LogUtils.d("向上滑动了");
                wxUtils.adbUpSlide(context);//向上滑动
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                //                ShowToast.show("发消息完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("位联系人")) {//判断是否到达底部
                bottom = true;
            }
        }


    }

    private void StatisticsWxFriends(String xmlData) {
        List<String> wxFriendsMessage = wxUtils.getNodeList(xmlData);
        String wx_number = null; //微信号
        String wx_name = null;  //昵称
        String wx_location = null;//目前所在地区
        String wx_phone_number = null;//手机电话号码
        String wx_phone_name = null;//手机联系人名称
        String uid = SPUtils.getString(context, "uid", "0000");
        for (int a = 0; a < wxFriendsMessage.size(); a++) {
            nodeBean = wxUtils.getNodeXmlBean(wxFriendsMessage.get(a)).getNode();
            if ((xmlData.contains("com.tencent.mm:id/anq"))) {//信息里面已经有备注了的时候
                if (("com.tencent.mm:id/anq").equals(nodeBean.getResourceid())) {
                    wx_name = nodeBean.getText();
                    break;
                }
            } else {//信息里面没有备注的时候
                if (("com.tencent.mm:id/pl").equals(nodeBean.getResourceid())) {
                    wx_name = nodeBean.getText();
                    break;
                }
            }
        }
        for (int a = 0; a < wxFriendsMessage.size(); a++) {
            nodeBean = wxUtils.getNodeXmlBean(wxFriendsMessage.get(a)).getNode();
            if ((wx_location == null) && xmlData.contains("地区") && ("android:id/summary".equals(nodeBean.getResourceid()))) {
                wx_location = nodeBean.getText();
            } else if (nodeBean != null && xmlData.contains("微信号") && ("com.tencent.mm:id/ang").equals(nodeBean.getResourceid())) {
                wx_number = nodeBean.getText();
            } else if (nodeBean != null && xmlData.contains("电话号码") && ("com.tencent.mm:id/cp2").equals(nodeBean.getResourceid())) {
                wx_phone_number = wxUtils.getNodeXmlBean(wxFriendsMessage.get(a + 2)).getNode().getText();
            }
        }
        if (wx_number != null && wx_number.contains(":")) {
            int start = wx_number.indexOf(":");
            String wx_number2 = wx_number.substring(start + 1, wx_number.length());
            wx_number = wx_number2.trim();
        }

        if (wx_name != null && wx_name.contains(":")) {
            int start = wx_name.indexOf(":");
            String wx_name2 = wx_name.substring(start + 1, wx_name.length());
            wx_name = wx_name2.trim();
        }
        if (wx_name != null) {
            for (int i = 0; i < wx_name.length(); i++) {
                char codePoint = wx_name.charAt(i);
                if (!((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                        (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                        ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                        && (codePoint <= 0x10FFFF)))) {
                    wx_name = (wx_name.substring(0, i) + wx_name.substring(i + 1)).trim();
                }
            }
        }
        Log.d("获取到的信息", "微信号: " + wx_number + "微信名字： " + wx_name + "微信所在地区： " + wx_location + " 微信手机号：" + wx_phone_number + " 手机联系人名： " + wx_phone_name + "设备的ID ： " + uid);

        List<WxFriendsMessageBean> mWxFriendsMessageBean = new ArrayList<>();
        WxFriendsMessageBean messageBean = new WxFriendsMessageBean(wx_number, wx_name, wx_phone_number, wx_phone_name, wx_location, uid);
        //       JSON[{"wx_location":"安道尔","wx_name":"女人如烟 ","wx_phone_name":"李霞","wx_phone_number":"13801522864","wx_uid":"1122"}]
        mWxFriendsMessageBean.add(messageBean);
        String str = new Gson().toJson(mWxFriendsMessageBean);
        LogUtils.d("JSON" + str.toString());
        sendWxFriendsMessage(str);
        return;
    }

    public void sendWxFriendsMessage(String str) {
        RequestParams params = new RequestParams(URLS.statictis_wx_message_store());
        params.addBodyParameter("json", str.replace("\\", ""));

        HttpManager.getInstance().sendPostRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                LogUtils.d("好友个人信息上传成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                //                LogUtils.d("好友数量上传失败");
            }
        });

    }


    /**
     * 给测试号（zzz9开头的用户）发送红包
     */
    public void sendRedPacket(String pay_num_s, String pay_num_e, String pay_password) {
        Integer s = Integer.valueOf(pay_num_s); //3
        Integer e = Integer.valueOf(pay_num_e);//5
        int tempNum = random.nextInt(e - s + 1);// [0,2]
        tempNum = s + tempNum;//s 到 e 之间的随机值 （单位为分）
        float result = 0.01f * tempNum;
        String money = result + "";

        zzzChatList.clear();
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录,
        //页面本来停留在微信主页，通讯录界面上
        boolean bottom = false;//到了底部
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        String meName = "";
        String endData = "";
        int x = context.getResources().getDimensionPixelSize(R.dimen.x180);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y82);//EditText(金额输入框)


        //判断有多少zzz9用户
        xmlData = wxUtils.getXmlData();
        while (true) {
            if (!(xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                return;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/i_") || nodeBean.getResourceid().equals("com.tencent.mm:id/ig")) && !StringUtils.isEmpty(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("微信") && (nodeBean.getContentdesc().startsWith("ZZZ9") || nodeBean.getContentdesc().startsWith("zzz9"))) {
                    if (!zzzChatList.contains(nodeBean.getText())) {
                        zzzChatList.add(nodeBean.getText());
                    }
                }
            }
            String endXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            xmlData = wxUtils.getXmlData();
            if (endXmlData.equals(xmlData)) {
                break;
            }
        }
        int size = zzzChatList.size(); //好友中满足条件的用户个数。在这些人中选出一个
        if (size == 0) {
            ShowToast.show("没有满足条件的好友", (Activity) context);
            return;
        }
        int randomTrue = random.nextInt(size);//随机

        String temp = zzzChatList.get(randomTrue);//nodeBean.getText() == temp的用户，就是随机出来的用户对象

        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录,
        xmlData = wxUtils.getXmlData();//重新获取页面数据
        w:
        while (true) {
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            f:
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                //筛选出好友。
                if (nodeBean.getText() != null && nodeBean.getText().equals(temp) && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/i_") || nodeBean.getResourceid().equals("com.tencent.mm:id/ig")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && (nodeBean.getContentdesc().startsWith("ZZZ9") || nodeBean.getContentdesc().startsWith("zzz9")) && !nodeBean.getContentdesc().startsWith("微信") && !meName.contains(nodeBean.getContentdesc())) {

                   /* int randomTrue = random.nextInt(3);//随机1-3条
                    if (randomTrue != 0) {//任务随机执行
                        continue;
                    }*/

                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {//只有好友界面。才有”标签“
                        wxUtils.adb("input keyevent 4");
                        meName = meName + nodeBean.getContentdesc();
                        continue;
                    }
                    //打开好友界面后。
                    List<String> messageList = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < messageList.size(); b++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(messageList.get(b)).getNode();
                        if ("com.tencent.mm:id/adj".equals(nodeBean.getResourceid())) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发消息
                            //开始发红包的操作。
                            wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                            wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y304, R.dimen.x88, R.dimen.y368);//点击红包
                            while (true) {
                                SystemClock.sleep(1000);
                                xmlData = wxUtils.getXmlData();//重新获取页面数据
                                if (!xmlData.contains("com.tencent.mm:id/adj")) { //说明已经跳出了原来的界面
                                    break;
                                }

                            }
                            //SystemClock.sleep(8000);//睡眠8秒防止红包界面还没有打开
                            cm.setText(wxUtils.getFaceText(money));
                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                            wxUtils.adbDimensClick(context, R.dimen.x240, R.dimen.y54, R.dimen.x240, R.dimen.y54);//点击粘贴
                            wxUtils.adbDimensClick(context, R.dimen.x20, R.dimen.y169, R.dimen.x300, R.dimen.y199);//点击一下金额，隐藏输入法
                            wxUtils.adbDimensClick(context, R.dimen.x20, R.dimen.y218, R.dimen.x300, R.dimen.y252);//点击  塞进红包
                            int tempCount = 0;
                            while (true) {
                                if (tempCount > 15) {
                                    return; //超过15秒，可能是没有网络。直接结束方法。
                                }
                                SystemClock.sleep(1000);//睡眠1秒防止支付界面还没有打开
                                //判断弹出系统繁忙的界面
                                xmlData = wxUtils.getXmlData();//重新获取页面数据
                                if (xmlData.contains("系统繁忙，请稍后再试")) {
                                    wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y230, R.dimen.x264, R.dimen.y251);//点击确定300,490,396,535
                                    wxUtils.adbDimensClick(context, R.dimen.x20, R.dimen.y218, R.dimen.x300, R.dimen.y252);//点击  塞进红包
                                }
                                if (xmlData.contains("请输入支付密码") || xmlData.contains("立即支付")) {//已经进入到下一个界面，就跳出循环
                                    break;
                                }
                                tempCount = tempCount++;
                            }


                            xmlData = wxUtils.getXmlData();//重新获取页面数据
                            if (xmlData.contains("请输入支付密码")) {//输入密码，红包发送成功
                                ShowToast.show("依次输入密码", (Activity) context);
                                String password = pay_password;
                                for (int i = 0; i < password.length(); i++) {
                                    String c = password.charAt(i) + "";
                                    Integer pass = Integer.valueOf(c);
                                    enterPassword(pass);
                                    SystemClock.sleep(100);
                                }

                                //判断支付密码错误界面，判断支付密码输入错误过多账户已被锁定
                                xmlData = wxUtils.getXmlData();//重新获取页面数据
                                if (xmlData.contains("支付密码错误，请重试") || xmlData.contains("支付密码输入错误过多账户已被锁定")) {//页面包含支付密码错误
                                    ShowToast.show("支付密码错误,或错误次数过多", (Activity) context);
                                    //点击请重试
                                    wxUtils.adbDimensClick(context, R.dimen.x120, R.dimen.y230, R.dimen.x184, R.dimen.y251);//点击重试180,490,276,535
                                    //wxUtils.adb("input keyevent 4");
                                    //wxUtils.adb("input keyevent 4");
                                }
                                //回到微信主页
                                //wxUtils.adb("input keyevent 4");
                                //wxUtils.adb("input keyevent 4");
                            } else {//提示用户绑定支付方式
                                ShowToast.show("请绑定支付方式", (Activity) context);
                                //回到微信主页
                                //wxUtils.adb("input keyevent 4");
                                //wxUtils.adb("input keyevent 4");
                                //wxUtils.adb("input keyevent 4");
                                //wxUtils.adb("input keyevent 4");
                            }
                            //NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                            return;
                        }
                    }

                    meName = meName + nodeBean.getContentdesc();

                }
            }

            xmlData = wxUtils.getXmlData();//重新获取页面数据
            if (!xmlData.contains("发现") && !xmlData.contains("com.tencent.mm:id/i")) {
                ShowToast.show("任务被中断，发消息任务", (Activity) context);
                break w;
            }
            if (!bottom) {
                LogUtils.d("向上滑动了");
                wxUtils.adbUpSlide(context);//向上滑动
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                break w;
            }
            if (xmlData.contains("位联系人")) {//判断是否到达底部
                bottom = true;
            }
        }

    }

    /**
     * 微信红包支付，模拟键盘录入密码
     *
     * @param number 数字
     */
    public void enterPassword(int number) {
        switch (number) {
            case 0:
                wxUtils.adbDimensClick(context, R.dimen.x107, R.dimen.y358, R.dimen.x213, R.dimen.y400);//0(160,765)(320,854)
                break;
            case 1:
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y233, R.dimen.x107, R.dimen.y275);//1(0,497)(160,586)
                break;
            case 2:
                wxUtils.adbDimensClick(context, R.dimen.x107, R.dimen.y233, R.dimen.x213, R.dimen.y275);//2(160,497)(320,586)
                break;
            case 3:
                wxUtils.adbDimensClick(context, R.dimen.x213, R.dimen.y233, R.dimen.x320, R.dimen.y275);//3(320,497)(480,586)
                break;
            case 4:
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y275, R.dimen.x107, R.dimen.y317);//4(0,586)(160,675)
                break;
            case 5:
                wxUtils.adbDimensClick(context, R.dimen.x107, R.dimen.y275, R.dimen.x213, R.dimen.y317);//5(160,586)(320,675)
                break;
            case 6:
                wxUtils.adbDimensClick(context, R.dimen.x213, R.dimen.y275, R.dimen.x320, R.dimen.y317);//6(320,586)(480,675)
                break;
            case 7:
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y317, R.dimen.x107, R.dimen.y359);//7(0,675)(160,765)
                break;
            case 8:
                wxUtils.adbDimensClick(context, R.dimen.x107, R.dimen.y317, R.dimen.x213, R.dimen.y359);//8(160,675)(320,765)
                break;
            case 9:
                wxUtils.adbDimensClick(context, R.dimen.x213, R.dimen.y317, R.dimen.x320, R.dimen.y359);//9(320,675)(480,765)
                break;

        }
    }

    /**
     * 朋友圈分享链接
     */
    private void shareUrl(String url, String sendText) {
        if (!StringUtils.isEmpty(url)) {
            // 将文本内容放到系统剪贴板里。
            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            cm.setText("文件传输助手");
            wxUtils.adbDimensClick(context, R.dimen.x204, R.dimen.y17, R.dimen.x252, R.dimen.y51);//搜索
            int x = context.getResources().getDimensionPixelSize(R.dimen.x167);
            int y = context.getResources().getDimensionPixelSize(R.dimen.y33);//EdiText
            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
            wxUtils.adbDimensClick(context, R.dimen.x118, R.dimen.y85, R.dimen.x118, R.dimen.y85);//粘贴
            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y85, R.dimen.x320, R.dimen.y130);//文件传输助手

            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("文件传输助手") && xmlData.contains("添加到通讯录")) {
                wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y192, R.dimen.x306, R.dimen.y225);//添加到通讯录
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y85, R.dimen.x320, R.dimen.y130);//文件传输助手
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("发消息")) {
                    wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y240, R.dimen.x306, R.dimen.y272);//发消息
                    SystemClock.sleep(3000);
                }

            }

            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("当前所在页面,与文件传输助手的聊天")) {
                return;
            }

            if (xmlData.contains("按住 说话")) {
                wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                wxUtils.adb("input keyevent 4");
                xmlData = wxUtils.getXmlData();
            }

            List<String> copyList = wxUtils.getNodeList(xmlData);
            for (int c = 0; c < copyList.size(); c++) {
                NodeXmlBean.NodeBean copyBean = wxUtils.getNodeXmlBean(copyList.get(c)).getNode();
                if ("com.tencent.mm:id/aab".equals(copyBean.getResourceid())) {
                    if (!StringUtils.isEmpty(copyBean.getText())) {
                        int xx = context.getResources().getDimensionPixelSize(R.dimen.x296);
                        int yy = context.getResources().getDimensionPixelSize(R.dimen.y343);//删除
                        wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 7000);  //删除
                        wxUtils.adb("input keyevent 4");
                        break;
                    }
                }
            }


            cm.setText(url);
            x = context.getResources().getDimensionPixelSize(R.dimen.x136);
            y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送


            xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
            List<String> urlData = wxUtils.getNodeList(xmlData);
            for (int i = urlData.size(); i > 0; i--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(urlData.get(i - 1)).getNode();
                if ("com.tencent.mm:id/ji".equals(nodeBean.getResourceid())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取消息坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//消息
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /*xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
                    if (!xmlData.contains("QQ浏览器X5内核提供技术支持")) {
                        return;
                    }*/

                    wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//更多功能
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();
                    if (!xmlData.contains("发送给朋友")) {
                        return;
                    }

                    while (true) {
                        //                                    wxUtils.adbDimensClick(context, R.dimen.x85, R.dimen.y185, R.dimen.x160, R.dimen.y243);//分享到朋友圈
                        x = context.getResources().getDimensionPixelSize(R.dimen.x120);
                        y = context.getResources().getDimensionPixelSize(R.dimen.y214);//EdiText
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 2000);  //长按EdiText
                        xmlData = wxUtils.getXmlData();
                        if (!xmlData.contains("发送给朋友")) {
                            break;
                        }
                    }
                    SystemClock.sleep(5000);
                    if (!StringUtils.isEmpty(sendText)) {
                        cm.setText(wxUtils.getFaceText(sendText));
                        x = context.getResources().getDimensionPixelSize(R.dimen.x160);
                        y = context.getResources().getDimensionPixelSize(R.dimen.y74);//EdiText
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                        SystemClock.sleep(3000);
                        wxUtils.adbDimensClick(context, R.dimen.x48, R.dimen.y77, R.dimen.x48, R.dimen.y124);//粘贴
                    }

                    wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//点击发送
                    break;
                }
            }

        }
    }

    /**
     * 好友分享链接
     */
    private void shareFriendsUrl(String url) {
        if (!StringUtils.isEmpty(url)) {
            // 将文本内容放到系统剪贴板里。
            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            cm.setText("文件传输助手");
            wxUtils.adbDimensClick(context, R.dimen.x204, R.dimen.y17, R.dimen.x252, R.dimen.y51);//搜索
            int x = context.getResources().getDimensionPixelSize(R.dimen.x167);
            int y = context.getResources().getDimensionPixelSize(R.dimen.y33);//EdiText
            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
            wxUtils.adbDimensClick(context, R.dimen.x118, R.dimen.y85, R.dimen.x118, R.dimen.y85);//粘贴
            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y85, R.dimen.x320, R.dimen.y130);//文件传输助手

            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("文件传输助手") && xmlData.contains("添加到通讯录")) {
                wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y192, R.dimen.x306, R.dimen.y225);//添加到通讯录
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y85, R.dimen.x320, R.dimen.y130);//文件传输助手
            }

            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("当前所在页面,与文件传输助手的聊天")) {
                return;
            }


            cm.setText(url);
            x = context.getResources().getDimensionPixelSize(R.dimen.x136);
            y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送


            xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
            List<String> urlData = wxUtils.getNodeList(xmlData);
            for (int i = urlData.size(); i > 0; i--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(urlData.get(i - 1)).getNode();
                if ("com.tencent.mm:id/ji".equals(nodeBean.getResourceid())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取消息坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//消息
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
                    if (xmlData.contains("文件传输助手")) {
                        return;
                    } else if (xmlData.contains("地理位置授权")) {
                        //                        wxUtils.adbClick(300, 517, 396, 562);      TODO copy   修改
                        wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y243, R.dimen.x264, R.dimen.y263);//更多功能
                    }

                    wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//更多功能
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //------------------------------------------------------------------------------------------------------

                    wxUtils.adbDimensClick(context, R.dimen.x10, R.dimen.y185, R.dimen.x85, R.dimen.y243);//发送给朋友

                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("选择") && xmlData.contains("多选"))) {
                        wxUtils.adbDimensClick(context, R.dimen.x10, R.dimen.y185, R.dimen.x85, R.dimen.y243);//发送给朋友
                    }

                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("选择") && xmlData.contains("多选"))) {
                        return;
                    }

                    wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//多选
                    wxUtils.adbDimensClick(context, R.dimen.x48, R.dimen.y60, R.dimen.x308, R.dimen.y86);//输入框

                    wxUtils.adb("input text zzz9");
                    wxUtils.adb("input keyevent 4");//关闭键盘
                    xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
                    List<String> nodeList1 = wxUtils.getNodeList(xmlData);

                    int cc = 0;
                    ;
                    for (int f = 0; f < nodeList1.size() - 2; f++) {
                        //                        LogUtils.e(nodeList1.get(f));
                        NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(nodeList1.get(f)).getNode();
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList1.get(f + 1)).getNode();
                        if (nodeBean1.getText() != null && (nodeBean1.getText().startsWith("zzz9") || nodeBean1.getText().startsWith("ZZZ9"))) {
                            int r = random.nextInt(3);
                            if (r != 0) {
                                listXY = wxUtils.getXY(nodeBean2.getBounds());//点击zzz9
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击zzz9
                                cc++;
                                break;
                            }
                        }
                    }
                    if (cc > 0) {//判断是否选中

                        wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//发送

                        xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
                        List<String> nodeList2 = wxUtils.getNodeList(xmlData);

                        for (int f = 0; f < nodeList2.size(); f++) {
                            NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList2.get(f)).getNode();
                            if (nodeBean3.getText() != null && nodeBean3.getText().equals("发送")) {
                                listXY = wxUtils.getXY(nodeBean3.getBounds());//点击zzz9
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//确认发送
                                break;
                            }
                        }
                    }

                    break;
                }
            }

        }
    }


    /**
     * 统计好友 群数量
     */
    public void sendWxNum() {
        if (SPUtils.getInt(context, "wtoolsdk", -1) == 0) {
            JSONObject jsonTask = new JSONObject();
            try {
                jsonTask.put("type", 6);
                jsonTask.put("taskid", System.currentTimeMillis());
                jsonTask.put("content", new JSONObject());
                jsonTask.getJSONObject("content").put("pageindex", 0);
                jsonTask.getJSONObject("content").put("pagecount", 0);
                jsonTask.getJSONObject("content").put("ismembers", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String content = wToolSDK.sendTask(jsonTask.toString());
            LogUtils.d("wxqunfa" + content);

            if (!content.contains("非授权用户")) {//aaaaaaaaaaaaaaaaaaaaaa
                WxNumBean wxNumBean = new WxNumBean();
                WxNumBean.ContentBean contentBean = new WxNumBean.ContentBean();
                List<WxNumBean.ContentBean.FlockBean> flockBeanList = new ArrayList<>();

                WxChatroomsBean wxFriendsBean = new Gson().fromJson(content, WxChatroomsBean.class);
                if (wxFriendsBean != null && wxFriendsBean.getResult() == 0) {//判断微信是否登录
                    String wxAccount = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
                    String accountLocation = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置

                    List<WxChatroomsBean.ContentBean> contentBeanList = wxFriendsBean.getContent();
                    for (int a = 0; a < contentBeanList.size(); a++) {
                        WxNumBean.ContentBean.FlockBean flockBean = new WxNumBean.ContentBean.FlockBean();
                        flockBean.setFlock_name(wToolSDK.decodeValue(contentBeanList.get(a).getNickname()));
                        flockBean.setFlock_num(contentBeanList.get(a).getMembers().size() + "");
                        flockBeanList.add(flockBean);
                        LogUtils.d(wToolSDK.decodeValue(contentBeanList.get(a).getNickname()) + "___" + wToolSDK.decodeValue(contentBeanList.get(a).getWxid()) + "群好友数量" + contentBeanList.get(a).getMembers().size());
                    }
                    contentBean.setFriends_num(getFriends() + "");
                    contentBean.setUid(SPUtils.getString(context, "uid", "0000"));
                    contentBean.setAccount(wxAccount);
                    contentBean.setLocation(accountLocation);
                    contentBean.setFlock(flockBeanList);
                    wxNumBean.setContent(contentBean);
                    String str = new Gson().toJson(wxNumBean);
                    LogUtils.d("JSON" + str.toString());
                    setWxnum(str);
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (SPUtils.getInt(context, "WxnumUploadSuccess", 0) == 2) {
                        setWxnum(str);
                    }
                } else if (wxFriendsBean != null && wxFriendsBean.getResult() == -1012) {//-1012代表没有群
                    String wxAccount = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
                    String accountLocation = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
                    contentBean.setFriends_num(getFriends() + "");
                    contentBean.setUid(SPUtils.getString(context, "uid", "0000"));
                    contentBean.setAccount(wxAccount);
                    contentBean.setLocation(accountLocation);
                    contentBean.setFlock(flockBeanList);
                    wxNumBean.setContent(contentBean);
                    String str = new Gson().toJson(wxNumBean);
                    LogUtils.d("JSON" + str.toString());
                    setWxnum(str);
                }
            }
        }
    }

    /**
     * 微信统计
     *
     * @param jsonStr
     */
    public void setWxnum(String jsonStr) {
        RequestParams params = new RequestParams(URLS.wxNewstatictis_crowd());
        //        params.addQueryStringParameter("", jsonStr);//
        //        params.setBodyContent(jsonStr);
        params.addBodyParameter("data", jsonStr.replace("\\", ""));

        HttpManager.getInstance().sendPostRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                LogUtils.d("好友数量上传成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("好友数量上传失败");
            }
        });
    }


    /**
     * 获取好友数量
     *
     * @return
     */
    private int getFriends() {
        if (SPUtils.getInt(context, "wtoolsdk", -1) == 0) {
            JSONObject jsonTask = new JSONObject();
            try {
                jsonTask.put("type", 5);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String content = wToolSDK.sendTask(jsonTask.toString());
            LogUtils.d("wxqunfa" + content);
            if (!content.contains("非授权用户")) {
                WxFriendsBean wxFriendsBean = new Gson().fromJson(content, WxFriendsBean.class);
                List<WxFriendsBean.ContentBean> contentBeanList = wxFriendsBean.getContent();
                LogUtils.d(contentBeanList.size() + "好友数");
                return contentBeanList.size();
            }

        }

        return 0;
    }


    WToolSDK wToolSDK = new WToolSDK();

    /**
     * @param friends 0好友  1群
     * @param type    1文字  2图片  3视频
     * @param test    内容
     */
    public void friendsSendMessage(int friends, int type, String test) {
        LogUtils.d("发送内容..." + test);

        if (SPUtils.getInt(context, "wtoolsdk", -1) == 0) {

            JSONObject jsonTask = new JSONObject();
            try {
                switch (friends) {
                    case 0://好友
                        jsonTask.put("type", 5);
                        break;
                    case 1:
                        jsonTask.put("type", 6);
                        jsonTask.put("taskid", System.currentTimeMillis());
                        jsonTask.put("content", new JSONObject());
                        jsonTask.getJSONObject("content").put("pageindex", 0);
                        jsonTask.getJSONObject("content").put("pagecount", 0);
                        jsonTask.getJSONObject("content").put("ismembers", 0);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String content = wToolSDK.sendTask(jsonTask.toString());
            LogUtils.d("wxqunfa" + content);
            if (!content.contains("非授权用户")) {
                WxFriendsBean wxFriendsBean = new Gson().fromJson(content, WxFriendsBean.class);
                List<WxFriendsBean.ContentBean> contentBeanList = wxFriendsBean.getContent();
                for (int a = 0; a < contentBeanList.size(); a++) {
                    LogUtils.d(wToolSDK.decodeValue(contentBeanList.get(a).getNickname()) + "___" + wToolSDK.decodeValue(contentBeanList.get(a).getWxid()));
                    sendSDKMessage(type, wToolSDK.decodeValue(contentBeanList.get(a).getWxid()), test);//群发

                    //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_s())) {
                        start = 60;
                    } else {
                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_e())) {
                        end = 200;
                    } else {
                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_e());
                    }
                    int timeSleep = random.nextInt(end - start + 1) + start;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 发消息
     *
     * @param type   1发文字  2发图片  4视频
     * @param talker
     * @param text
     */
    private void sendSDKMessage(int type, String talker, String text) {
        String path = "";
        String strMark = "";
        String fileName = "";
        String filePath = "";

        if (type == 2 || type == 4) {
            path = URLS.pic_vo + text.replace("\\", "/");
            LogUtils.d("文件url__" + path);
            strMark = text.replace("\\", "/");
            fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");
            LogUtils.d("a" + fileName);
            filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + fileName;
            LogUtils.d("b" + filePath);
            LogUtils.d("c" + FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages"));
        }


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", type);
            jsonObject.put("taskid", System.currentTimeMillis());
            jsonObject.put("content", new JSONObject());
            jsonObject.getJSONObject("content").put("talker", talker);
            jsonObject.getJSONObject("content").put("timeout", 60);


            switch (type) {


                case 1:
                    jsonObject.getJSONObject("content").put("text", wToolSDK.encodeValue(text));
                    break;
                case 2:

                    if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载

                        LogUtils.d("存在");
                        jsonObject.getJSONObject("content").put("imagefile", filePath);
                    } else {
                        LogUtils.d("不存在");
                        File f = wxUtils.getFileDown(path, fileName);
                        LogUtils.d(f.getAbsolutePath());
                        if (f != null) {
                            jsonObject.getJSONObject("content").put("imagefile", filePath);
                        }
                    }
                    break;
                case 4:

                    //                    /Uploads/Picture/2017-06-30/videologo.png
                    if (!new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "videologo").exists()) {
                        String videoLogoPath = URLS.pic_vo + "/Uploads/Picture/2017-06-30/videologo.png";
                        wxUtils.getFileDown(videoLogoPath, "videologo.png");
                    }

                    if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载
                        LogUtils.d("存在");
                        jsonObject.getJSONObject("content").put("videofile", filePath);
                        jsonObject.getJSONObject("content").put("videothumbfile", Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + "videologo.png");
                        jsonObject.getJSONObject("content").put("duration", 60);
                    } else {
                        LogUtils.d("不存在");
                        File f = wxUtils.getFileDown(path, fileName);
                        LogUtils.d(f.getAbsolutePath());
                        if (f != null) {
                            jsonObject.getJSONObject("content").put("videofile", filePath);
                            jsonObject.getJSONObject("content").put("videothumbfile", Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + "videologo.png");
                            jsonObject.getJSONObject("content").put("duration", 60);
                        }
                    }

                    break;

            }


            String result = wToolSDK.sendTask(jsonObject.toString());
            LogUtils.d("json" + jsonObject.toString());
            LogUtils.d("发送结果" + result);
            ShowToast.show("已发送", (Activity) context);
            try {
                JSONObject jsonObject1 = new JSONObject(result);
                if (jsonObject1.getInt("result") == 0) {

                } else {

                }
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }
    }

    /**
     * 给微信好友发图片素材
     */
    private void WSendPic() {
        createSDCardDir();
        if (StringUtils.isEmpty(materia_pic)) {
            ShowToast.show("发图片的素材地址有误或为空请重新选择请求地址", (Activity) context);
        } else {
            String rul = URLS.pic_vo + materia_pic.replaceAll("\\s", "");
            xutilDown.downloadFile(rul, Constants.sdPath_pic + System.currentTimeMillis() + "fp.jpg");
        }
        wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y362, R.dimen.x160, R.dimen.y400);//好友逐个发消息视频
        xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
        List<String> sen = wxUtils.getNodeList(xmlData);
        while (true) {
            for (int i = 0; i < sen.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(sen.get(i)).getNode();
                if ("com.tencent.mm:id/i_".equals(nodeBean.getResourceid())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击坐标
                    xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
                    List<String> sen_xiaoxi = wxUtils.getNodeList(xmlData);
                    for (int k = 0; k < sen_xiaoxi.size(); k++) {
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(sen.get(k)).getNode();
                        if ("com.tencent.mm:id/adj".equals(nodeBean2.getResourceid())) {
                            if ("发消息".equals(nodeBean2.getText())) {
                                LogUtils.d("你你进来了没有亲?????");
                                listXY = wxUtils.getXY(nodeBean2.getBounds());//获取添加坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击坐标
                                wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y366, R.dimen.x316, R.dimen.y400);//弹出选择按钮
                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y239, R.dimen.x88, R.dimen.y303);//选择相册
                                wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y239, R.dimen.x88, R.dimen.y303);//选择相册
                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y52, R.dimen.x105, R.dimen.y126);//选择相片
                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//点击发送
                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回

                            }

                        }
                    }

                }

            }
        }

    }

    /**
     * 给微信好友发视频
     */
    private void WSendVido() {
        if (StringUtils.isEmpty(materia_vedio)) {//判断请求地址是否为空
            ShowToast.show("选择的任务请求地址为空,请更换地址", (Activity) context);
        } else {
            String rul = URLS.pic_vo + materia_vedio.replaceAll("\\s", "");
            LogUtils.d("请求过来的视屏地址2" + rul);
            xutilDown.downloadFile(rul, Constants.sdPath_vido + System.currentTimeMillis() + "sp.mp4");
        }
        wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y362, R.dimen.x160, R.dimen.y400);//好友逐个发消息视频


    }

    public void setGender(String sex) {
        wx_Sex = sex;
    }

    public void checkoutDevice(String uid) {

        String path = "http://121.40.68.4:8080/update_status/reportDriversStatus.php";
        RequestParams params = new RequestParams(path);
        params.addQueryStringParameter("uid", uid);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<Object>() {
            @Override
            public void onSuccess(Object bean) {
                Log.d("上传成功", "");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                Log.d("上传失败", "");
            }
        });
    }

    /**
     * 接收推送过来的广播
     */
    class OperationPresenterBrocaset extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strAction = intent.getAction();
            if (strAction.equals(MyConstains.Broadcast_Task)) {
                MessageListBean.ContentBean.DataBean messageBean = (MessageListBean.ContentBean.DataBean) intent.getSerializableExtra("messageBean");
                LogUtils.d("我在Operationpresenter里面接收到了接受到了广播,data的值是:" + messageBean.toString());
                materia_pic = messageBean.getParam().getMateria_pic();//素材ID
                if (!StringUtils.isEmpty(messageBean.getParam().getDz_num_e()) && !StringUtils.isEmpty(messageBean.getParam().getDz_num_s())) {
                    int dz_num_e = Integer.valueOf(messageBean.getParam().getDz_num_e());
                    int dz_num_s = Integer.valueOf(messageBean.getParam().getDz_num_s());
                    dz_sum = random.nextInt(dz_num_e - dz_num_s + 1) + dz_num_s + "";
                    LogUtils.d("点赞次数：" + dz_sum);
                } else {
                    dz_sum = 5 + "";//点赞次数0
                }
                materia_vedio = messageBean.getParam().getMateria_vedio();//视频地址
                one_add_num = messageBean.getParam().getOne_add_num();//通讯录每次最多请求的好友次数
                day_add_num = messageBean.getParam().getDay_add_num();//通讯录每天最多的请求次数
                wx_add_num = messageBean.getParam().getWx_add_num();//获取床底过啦id次数
                materia_phone = messageBean.getParam().getMateria_phone();
                add_interval_time_s = messageBean.getParam().getAdd_interval_time_s();//通讯录添加好友间隔时间
                add_interval_time_e = messageBean.getParam().getAdd_interval_time_e();//通讯录添加好友结束时间
                one_add_num_s = messageBean.getParam().getOne_add_num_s();//通讯录加好友的开始（次数）
                one_add_num_e = messageBean.getParam().getOne_add_num_e();//通讯录加好友的结束(次数)
                contact_verify_msg = messageBean.getParam().getContact_verify_msg();//自定义添加好友申请内容
                wx_add_num_e = messageBean.getParam().getWx_add_num_e();//搜索添加好友结束
                wx_add_num_s = messageBean.getParam().getWx_add_num_s();//搜索添加好友开始
                agree_interval_time_s = messageBean.getParam().getAgree_interval_time_s();//自动通过好友申请开始时间
                agree_interval_time_e = messageBean.getParam().getAgree_interval_time_e();//自动通过好友结束时间
                phoneRadio = messageBean.getParam().getPhoneRadio();//参数设置
                open_s_game = messageBean.getParam().game_s;
                open_e_game = messageBean.getParam().game_e;
                open_s_shop = messageBean.getParam().shopping_s;
                open_e_shop = messageBean.getParam().shopping_e;

                reply_msg = messageBean.getParam().reply_msg;
                args2 = messageBean.getParam().getSs_comment();

                LogUtils.d("传过来的微信搜索加好友次数是" + wx_add_num_e + "qian " + wx_add_num_s);
            }

        }
    }

    /**
     * 保存群聊到通讯录
     */
    private void SaveFlockToBook() {
        xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
        boolean Tag = true;
        List<String> flockbook = wxUtils.getNodeList(xmlData);
        while (true) {
            for (int i = 0; i < flockbook.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(flockbook.get(i)).getNode();
                if ("com.tencent.mm:id/af9".equals(nodeBean.getResourceid())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击坐标
                    wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//
                    xmlData = wxUtils.getXmlData();//重新获取页面
                    if (xmlData.contains("聊天信息(")) {
                        if (xmlData.contains("保存到通讯录")) {
                            //    xmlData = wxUtils.getXmlData();//获取主页面的gson、格式数据
                            List<String> biaoqian = wxUtils.getNodeList(xmlData);
                            for (int k = 0; k < biaoqian.size(); k++) {//循环当前页面的标签
                                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(flockbook.get(k)).getNode();
                                if ("com.tencent.mm:id/fp".equals(nodeBean2.getResourceid())) {
                                    if (!StringUtils.isEmpty(nodeBean2.getContentdesc())) {
                                        if ("已关闭".equals(nodeBean2.getContentdesc())) {
                                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击坐标
                                            AdbUtils.back();
                                            AdbUtils.back();
                                            //                                            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                            //                                            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                        } else {
                                            AdbUtils.back();
                                            AdbUtils.back();
                                            //                                            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                            //                                            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                        }
                                    } else {
                                        AdbUtils.back();
                                        AdbUtils.back();
                                        //                                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                        //                                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                    }
                                }
                            }
                        } else {
                            wxUtils.adbUpSlide(context);
                            Log.d("我来保存选择通讯录的哦", "hah weqwewq ");
                        }
                    } else {
                        AdbUtils.back();
                        AdbUtils.back();
                        //                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                        //                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                    }
                } else {

                }

            }

        }
    }


    String phone = "";
    String name = "";

    private void StatuRequest() {
        int num = 1;
        int add_num;
        int min = 0;
        int max = 0;
        if (StringUtils.isEmpty(SPUtils.getString(context, "wxcishu", ""))) {
            add_num = 5;
            LogUtils.d("你是进来了几次a  搜索加好友" + add_num);
        } else {
            add_num = Integer.parseInt(SPUtils.getString(context, "wxcishu", "").trim());
            LogUtils.d("你是进来了几次zheli 搜索加好友" + add_num);
        }
        String uid = SPUtils.getString(context, "uid", "0000");
        int wxcishu = 0;
        if (StringUtils.isEmpty(wx_add_num_e) && StringUtils.isEmpty(wx_add_num_s)) {
            wxcishu = 5;
        } else {
            Random random = new Random();
            int maxs = Integer.parseInt(wx_add_num_e);
            int mins = Integer.parseInt(wx_add_num_s);
            wxcishu = random.nextInt(maxs) % (maxs - mins + 1) + mins;
            SPUtils.putString(context, "wxcishu", wxcishu + "");
        }

        String phone_url = "http://103.94.20.101:8087/api_wechat/index.php?zh=" + uid + "&limit=" + wxcishu + "";
        try {
            URL url = new URL(phone_url);
            // 2.建立一个http连接
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            // 3.设置一些请求方式
            conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");

            int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
            // // 503服务器内部错误
            if (code == 200) {
                InputStream is = conn.getInputStream();
                // 把is的内容转换为字符串
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());
                LogUtils.d("搜索加好友返回的结果是" + result);
                WxPhone phoneBean = GsonUtil.parseJsonWithGson(result, WxPhone.class);
                for (int i = 0; i < phoneBean.getData().size(); i++) {
                    phone = phoneBean.getData().get(i).getPhone();
                    name = phoneBean.getData().get(i).getName();
                    if (num <= add_num) {
                        num++;
                        LogUtils.d("哈哈这是我进来的第" + num + "次");
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.x17, R.dimen.x320, R.dimen.y51);//
                        wxUtils.adbDimensClick(context, R.dimen.x112, R.dimen.y84, R.dimen.x308, R.dimen.y118);//
                        wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y73, R.dimen.x306, R.dimen.y94);//
                        wxUtils.adb("input text " + phone);
                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y51, R.dimen.x320, R.dimen.y96);//
                        xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("该用户不存在")) {
                            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//如果该用户不存在 则返回
                        } else {
                            wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y73, R.dimen.x306, R.dimen.y94);//输入框
                            wxUtils.adb("input text " + phone);//填写账号
                            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y51, R.dimen.x320, R.dimen.y96);//点击搜索
                            xmlData = wxUtils.getXmlData();//重新获取页面json
                            List<String> TongxunList = wxUtils.getNodeList(xmlData);
                            for (int k = 0; k < TongxunList.size(); k++) {
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(TongxunList.get(k)).getNode();
                                if (nodeBean.getResourceid() != null && "com.tencent.mm:id/an_".equals(nodeBean.getResourceid())) {
                                    if (nodeBean.getText() != null && "添加到通讯录".equals(nodeBean.getText())) {
                                        String neirong = "";
                                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标

                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加
                                        Log.d("我的坐标是", listXY.get(0) + "," + listXY.get(1) + "," + listXY.get(2) + "," + listXY.get(3));
                                        if (StringUtils.isEmpty(contact_verify_msg)) {
                                            neirong = "你好";
                                        } else {
                                            neirong = contact_verify_msg.replaceAll("《name》", phone_name);
                                        }
                                        //                                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y80, R.dimen.x312, R.dimen.y107);//点击清除按钮
                                        wxUtils.adbClick(420, 200, 420, 200);//点击清除按钮
                                        wxUtils.adbClick(420, 200, 420, 200);//点击清除按钮
                                        int x = context.getResources().getDimensionPixelSize(R.dimen.x160);
                                        int y = context.getResources().getDimensionPixelSize(R.dimen.y93);//EdiText
                                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                        cm.setText(neirong);
                                        wxUtils.adbDimensClick(context, R.dimen.x24, R.dimen.y51, R.dimen.x96, R.dimen.y80);//点击复制 黏贴

                                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//发送、

                                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                        if (StringUtils.isEmpty(add_interval_time_s) && StringUtils.isEmpty(add_interval_time_e)) {

                                        } else {
                                            SPUtils.putString(context, "add_interval_time_s", add_interval_time_s);
                                            SPUtils.putString(context, "add_interval_time_e", add_interval_time_e);
                                        }
                                        if (StringUtils.isEmpty(SPUtils.getString(context, "add_interval_time_s", "")) && StringUtils.isEmpty(SPUtils.getString(context, "add_interval_time_e", ""))) {
                                            min = 30;
                                            max = 120;
                                        } else {
                                            min = Integer.parseInt(SPUtils.getString(context, "add_interval_time_s", "").trim());
                                            max = Integer.parseInt(SPUtils.getString(context, "add_interval_time_e", "").trim());
                                        }
                                        Random random = new Random();
                                        int s = random.nextInt(max) % (max - min + 1) + min;
                                        LogUtils.d("通讯录添加好友的休眠的时间为" + s + "秒");
                                        try {
                                            Thread.sleep(s * 1000);
                                        } catch (InterruptedException e) {
                                        }
                                    } else if ("发消息".equals(nodeBean.getText())) {
                                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回到页面
                                    }
                                }
                            }
                        }
                    } else {
                        ShowToast.show("任务执行完毕", (Activity) context);
                        break;
                    }
                }
                //SerchAddFriend();
                is.close();

            } else {
                InputStream is = conn.getErrorStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());


                LogUtils.d("错误返回的结果" + result);
                ShowToast.show("网络请求失败，请检测网络", (Activity) context);
            }

        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    /**
     * 添加手机联系人通讯录
     */
    private void AddCommunication(String sex) {
        if (sex.equals("1")) {
            sex_key = new String[1];
            sex_key[0] = "男";// 只加男
        } else if (sex.equals("2")) {
            sex_key = new String[1];
            sex_key[0] = "女"; // 只加女
        } else if (sex.equals("3")) {
            sex_key = new String[1];
            sex_key[0] = "全加"; // 全部都加
        }
        wxUtils.adb("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        switchWxAccount();
        if (!getIsAccountIsOk2()) {
            Random random321 = new Random();
            int rsm = random321.nextInt(3);
            String[] str0 = new String[3];
            str0[0] = "0";
            str0[1] = "1";
            str0[2] = "3";
            String str2 = "1";
            str2 = str0[rsm];
            backHome();
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
            if (StringUtils.isEmpty(str2)) {
                //                                sendFriendsMessageCultivateDatas(0);
                return;
            } else {
                if (str2.equals("3")) {
                    sendFriendsMessageCultivate(Integer.valueOf(str2), new ArrayList<String>());
                } else {
                    if (sendFriendsMessageCultivateDatasList != null) {
                        sendFriendsMessageCultivateDatasList = null;
                    }
                    sendFriendsMessageCultivateDatas(Integer.valueOf(str2));

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (sendFriendsMessageCultivateDatasList != null && sendFriendsMessageCultivateDatasList.size() > 0) {
                        sendFriendsMessageCultivate(Integer.valueOf(str2), sendFriendsMessageCultivateDatasList);
                    } else {
                        return;
                    }

                }
            }
        } else {
            Random random321 = new Random();
            int rsm = random321.nextInt(3);
            String[] str0 = new String[3];
            str0[0] = "0";
            str0[1] = "1";
            str0[2] = "3";
            String str2 = "1";
            str2 = str0[rsm];
            backHome();
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
            if (StringUtils.isEmpty(str2)) {
                //                                sendFriendsMessageCultivateDatas(0);
                return;
            } else {
                if (str2.equals("3")) {
                    sendFriendsMessageCultivate(Integer.valueOf(str2), new ArrayList<String>());
                } else {
                    if (sendFriendsMessageCultivateDatasList != null) {
                        sendFriendsMessageCultivateDatasList = null;
                    }
                    sendFriendsMessageCultivateDatas(Integer.valueOf(str2));

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (sendFriendsMessageCultivateDatasList != null && sendFriendsMessageCultivateDatasList.size() > 0) {
                        sendFriendsMessageCultivate(Integer.valueOf(str2), sendFriendsMessageCultivateDatasList);
                    } else {
                        return;
                    }

                }
            }
            switchWxAccount();
            if (!SPUtils.getString(context, "AccountIsOnlyOne", "0").equals("2")) {
                backHome();
                NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                if (StringUtils.isEmpty(str2)) {
                    //                                sendFriendsMessageCultivateDatas(0);
                    return;
                } else {
                    if (str2.equals("3")) {
                        sendFriendsMessageCultivate(Integer.valueOf(str2), new ArrayList<String>());
                    } else {
                        if (sendFriendsMessageCultivateDatasList != null) {
                            sendFriendsMessageCultivateDatasList = null;
                        }
                        sendFriendsMessageCultivateDatas(Integer.valueOf(str2));

                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (sendFriendsMessageCultivateDatasList != null && sendFriendsMessageCultivateDatasList.size() > 0) {
                            sendFriendsMessageCultivate(Integer.valueOf(str2), sendFriendsMessageCultivateDatasList);
                        } else {
                            return;
                        }

                    }
                }
            }
        }
        backHome();
        wxUtils.adbWxClick(438, 827);//点击我
        String wxAccount = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains(wxAccount)) {  //目前的账号，不是要加的账号

            switchWxAccount();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.ui.bindmobile.MobileFriendUI");
        ShowToast.show("等待20秒刷新联系人界面", (Activity) context);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.ui.bindmobile.MobileFriendUI");
        ShowToast.show("等待20秒再次刷新联系人界面", (Activity) context);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean tag = true;
        xmlData = wxUtils.getXmlData();
        String name = "";
        int zuiduo_num = 0;
        int meici_num = 0;
        xmlData = wxUtils.getXmlData();
        int max = 0;
        int min = 0;
        String str_name = "";
        String meName = "";
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        SPUtils.putString(context, "MeiCiNum", "");
        while (tag) {
            xmlData = wxUtils.getXmlData();
            List<String> meWxFriend = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("添加")) {
                for (int i = 5; i < meWxFriend.size(); i++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(meWxFriend.get(i)).getNode();
                    if (nodeBean != null && nodeBean.getText() != null && "添加".equals(nodeBean.getText())) {
                        str_name = wxUtils.getNodeXmlBean(meWxFriend.get(i - 3)).getNode().getText();
                        LogUtils.d("通讯录好友名称是" + str_name);
                        if (nodeBean.getResourceid() != null && ("com.tencent.mm:id/b_k".equals(nodeBean.getResourceid()))) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取添加坐标
                            x1 = listXY.get(0);
                            y1 = listXY.get(1);
                            x2 = listXY.get(2);
                            y2 = listXY.get(3);
                            //在添加之前，我们先点击通讯录的个人信息，统计微信好友信息
                            if (str_name != null && !meName.contains(str_name)) {//统计一次之后记录之前的名字，下次就不要统计了
                                meName = meName + str_name;
                                if (!StatisticsWxFriendsMessage(listXY, str_name, sex_key)) {
                                    continue;
                                }
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                continue;
                            }
                            if (StringUtils.isEmpty(add_interval_time_s) && StringUtils.isEmpty(add_interval_time_e)) {
                            } else {
                                SPUtils.putString(context, "add_interval_time_s", add_interval_time_s);
                                SPUtils.putString(context, "add_interval_time_e", add_interval_time_e);
                            }
                            if (StringUtils.isEmpty(SPUtils.getString(context, "add_" +
                                    "+interval_time_s", "")) && StringUtils.isEmpty(SPUtils.getString(context, "add_interval_time_e", ""))) {
                                min = 30;
                                max = 60;
                                //                                min = 10;
                                //                                max = 15;
                            } else {
                                min = Integer.parseInt(SPUtils.getString(context, "add_interval_time_s", "").trim());
                                max = Integer.parseInt(SPUtils.getString(context, "add_interval_time_e", "").trim());
                            }

                            if (!name.contains(wxUtils.getNodeXmlBean(meWxFriend.get(i - 2)).getNode().getText())) {
                                Random random = new Random();
                                int s = random.nextInt(max) % (max - min + 1) + min;
                                LogUtils.d("通讯录添加好友的休眠的时间为" + s + "秒");
                                ShowToast.show("休眠" + s + "秒后再继续", (Activity) context);
                                try {
                                    Thread.sleep(s * 1000);
                                } catch (InterruptedException e) {

                                }
                                wxUtils.adbClick(x1, y1, x2, y2);//点击添加

                                meici_num = meici_num + 1;
                                SPUtils.putString(context, "MeiCiNum", "" + meici_num);
                                try {
                                    Thread.sleep(3 * 1000);
                                } catch (InterruptedException e) {

                                }

                                xmlData = wxUtils.getXmlData();
                                if (!xmlData.contains("验证申请")) {
                                    continue;//证明无需验证，自动通过了
                                }
                                List<String> sendneirong = wxUtils.getNodeList(xmlData);
                                for (int a = 0; a < sendneirong.size(); a++) {
                                    NodeXmlBean.NodeBean nodeBeans = wxUtils.getNodeXmlBean(sendneirong.get(a)).getNode();
                                    if (nodeBeans != null && "com.tencent.mm:id/d0c".equals(nodeBeans.getResourceid())) {
                                        String neirong = "";
                                        if (StringUtils.isEmpty(contact_verify_msg)) {
                                            neirong = "你好";
                                        } else {
                                            if (str_name.length() == 11) {
                                                neirong = contact_verify_msg.replaceAll("《name》", "");
                                                String newNeirong = neirong.trim();
                                                if (newNeirong.startsWith(",") || newNeirong.startsWith("，")) {
                                                    neirong = newNeirong.substring(1, newNeirong.length());
                                                }
                                            } else {
                                                neirong = contact_verify_msg.replaceAll("《name》", str_name);
                                            }

                                        }
                                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y80, R.dimen.x312, R.dimen.y107);//点击清除按钮
                                        int x = context.getResources().getDimensionPixelSize(R.dimen.x160);
                                        int y = context.getResources().getDimensionPixelSize(R.dimen.y93);//EdiText
                                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                        cm.setText(neirong);
                                        wxUtils.adbDimensClick(context, R.dimen.x24, R.dimen.y51, R.dimen.x96, R.dimen.y80);//点击复制 黏贴
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                //                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//发送
                                wxUtils.adbWxClick(431, 72);
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Boolean Flag = true;
                                while (Flag) {
                                    String xmlData2 = wxUtils.getXmlData();

                                    if (xmlData2.contains("你需要发送验证申请")) {
                                        ShowToast.show("还处于发送验证申请界面", (Activity) context);
                                        wxUtils.adbWxClick(431, 72);
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Flag = false;
                                    }
                                }
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                name = name + wxUtils.getNodeXmlBean(meWxFriend.get(i - 2)).getNode().getText() + ",";

                                if (StringUtils.isEmpty(day_add_num)) {

                                } else {
                                    if (zuiduo_num == Integer.parseInt(day_add_num)) {
                                        backHome();
                                        LogUtils.d("第一个位置");
                                        //CheckMessage();
                                        break;
                                    }
                                }
                                if (StringUtils.isEmpty(one_add_num)) {

                                } else {
                                    if (meici_num == Integer.parseInt(one_add_num)) {
                                        SPUtils.put(context, "meici_num", meici_num);
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        ShowToast.show("任务完成...", (Activity) context);
                                        backHome();
                                        LogUtils.d("第二个位置");
                                        //CheckMessage();
                                        break;
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
                    backHome();
                    LogUtils.d("第三个位置");
                    // CheckMessage();
                    break;
                }
            } else {
                tag = false;

            }
        }
        SPUtils.putString(context, "sendAccountApplySuccess", "0");
        ShowToast.show("准备上传数据 请稍后20s...", (Activity) context);
        String uid = SPUtils.getString(context, "uid", "0000");
        String flag_ram = (random.nextInt(99999) + 100000) + "";
        String accountLocation = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
        List<WxPhoneNumeAskBean> mWxPhoneNumeAskBeanList = new ArrayList<>();
        String meiCiNum = SPUtils.getString(context, "MeiCiNum", "");
        WxPhoneNumeAskBean wxPhoneNumeAskBean = new WxPhoneNumeAskBean(uid, wxAccount, accountLocation, meiCiNum + "", flag_ram);
        mWxPhoneNumeAskBeanList.add(wxPhoneNumeAskBean);
        String str = new Gson().toJson(mWxPhoneNumeAskBeanList);
        SPUtils.putString(context, "upLoadMessageTimeOut", "0");
        LogUtils.d("JSON" + str.toString());
        try {
            Response data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
            if (data.code() == 200) {
                String string = data.body().string();
                ShowToast.show("好友申请数据上传成功", (Activity) context);
                Log.d("zs1", string);
            } else {
                ShowToast.show("失败了，继续上传", (Activity) context);
                data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
                if (data.code() == 200) {
                    String string = data.body().string();
                    Log.d("zs2", string);
                    ShowToast.show("好友申请数据上传成功", (Activity) context);
                } else {
                    ShowToast.show("又失败了，继续上传", (Activity) context);
                    data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
                    if (data.code() == 200) {
                        String string = data.body().string();
                        Log.d("zs3", string);
                        ShowToast.show("好友申请数据上传成功", (Activity) context);
                    } else {
                        ShowToast.show("又又失败了，继续上传", (Activity) context);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            SPUtils.putString(context, "upLoadMessageTimeOut", "1");
        }
        if (SPUtils.getString(context, "upLoadMessageTimeOut", "").equals("1")) {
            try {
                Response data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
                SPUtils.putString(context, "upLoadMessageTimeOut", "0");
                if (data.code() == 200) {
                    String string = data.body().string();
                    ShowToast.show("好友申请数据上传成功", (Activity) context);
                    Log.d("zs1", string);
                } else {
                    ShowToast.show("失败了，继续上传", (Activity) context);
                    data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
                    if (data.code() == 200) {
                        String string = data.body().string();
                        Log.d("zs2", string);
                        ShowToast.show("好友申请数据上传成功", (Activity) context);
                    } else {
                        ShowToast.show("又失败了，继续上传", (Activity) context);
                        data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
                        if (data.code() == 200) {
                            String string = data.body().string();
                            Log.d("zs3", string);
                            ShowToast.show("好友申请数据上传成功", (Activity) context);
                        } else {
                            ShowToast.show("又又失败了，继续上传", (Activity) context);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                SPUtils.putString(context, "upLoadMessageTimeOut", "1");
            }
        }
        if (SPUtils.getString(context, "upLoadMessageTimeOut", "").equals("1")) {
            try {
                Response data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
                SPUtils.putString(context, "upLoadMessageTimeOut", "0");
                if (data.code() == 200) {
                    String string = data.body().string();
                    ShowToast.show("好友申请数据上传成功", (Activity) context);
                    Log.d("zs1", string);
                } else {
                    ShowToast.show("失败了，继续上传", (Activity) context);
                    data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
                    if (data.code() == 200) {
                        String string = data.body().string();
                        Log.d("zs2", string);
                        ShowToast.show("好友申请数据上传成功", (Activity) context);
                    } else {
                        ShowToast.show("又失败了，继续上传", (Activity) context);
                        data = OkHttpUtils.post().url(URLS.wxAccountApply()).addParams("data", str.replace("\\", "")).build().execute();
                        if (data.code() == 200) {
                            String string = data.body().string();
                            Log.d("zs3", string);
                            ShowToast.show("好友申请数据上传成功", (Activity) context);
                        } else {
                            ShowToast.show("又又失败了，继续上传", (Activity) context);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                SPUtils.putString(context, "upLoadMessageTimeOut", "1");
            }
        }
    }

    //通讯录加好友  专用切换手机联系人

    private void switchWxAccount() {
        backHome();
        boolean flag0 = true;
        int accountNum = 0;
        while (flag0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag0 = false;
            }
        }
        wxUtils.adbClick(411, 822, 429, 847);//点击我
        wxUtils.adbUpSlide(context);
        wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.setting.ui.setting.SettingsUI"); //进入设置界面
        wxUtils.adbUpSlide(context);
        wxUtils.adbClick(21, 681, 459, 714); //点击切换账号
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < nodeList.size() - 1; i++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
            if (nodeBean != null && nodeBean.getText() != null && !nodeBean.getText().equals("切换帐号") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/d5s")) {
                accountNum++;
            }
        }
        for (int i = 0; i < nodeList.size() - 1; i++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
            if (nodeBean != null && nodeBean.getText() != null && nodeBean.getText().equals("当前使用") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/d5v")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取 当前使用的坐标
                break;
            }
        }
        if (accountNum == 2) {  //老号在左边  新号在右边

            //说明已经登录了两个账号
            if (listXY.get(0) == 110) {
                //正在使用的账号 在左边（新号）， 点击右边的账号切换
                wxUtils.adbClick(288, 457, 384, 553);
            } else if (listXY.get(0) == 302) {
                //正在使用的账号 在右边(老号)， 点击左边的账号切换
                wxUtils.adbClick(96, 457, 192, 553);
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        wxUtils.adbClick(0, 36, 90, 108);//点击左上角的返回
        backHome();
    }

    private boolean StatisticsWxFriendsMessage(List<Integer> listXY, String wx_phone_name, String[] sex) {
        //        List<WxFriendsMessageBean.ContactMessageBean.WxContactBean> wxContactBeanList=new ArrayList<>();
        List<WxFriendsMessageBean> mWxFriendsMessageBean = new ArrayList<>();
        wxUtils.adbClick(listXY.get(0) - 200, listXY.get(1), listXY.get(2) - 200, listXY.get(3));
        xmlData = wxUtils.getXmlData();
        String wx_number = "";
        String wx_name = "";
        String wx_location = "";
        String wx_phone_number = "";
        int aaa = 0;
        if (xmlData.contains("男")) {
            aaa = 1;
        } else if (xmlData.contains("女")) {
            aaa = 2;
        } else {
            aaa = 3;
        }
        if ((sex[0].equals("男") && (aaa == 1)) || (sex[0].equals("女") && (aaa == 2)) || (sex[0].equals("全加"))) {
            List<String> meWxFriendsMessageList = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < meWxFriendsMessageList.size(); i++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(meWxFriendsMessageList.get(i)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && "com.tencent.mm:id/pl".equals(nodeBean.getResourceid())) {
                    // 为空的时候才获取，这样下次有数据只有就不会 重复获取了
                    wx_name = nodeBean.getText();
                }
                if (xmlData.contains("地区") && ("android:id/summary".equals(nodeBean.getResourceid())) &&
                        wxUtils.getNodeXmlBean(meWxFriendsMessageList.get(i - 3)).getNode().getText() != null &&
                        wxUtils.getNodeXmlBean(meWxFriendsMessageList.get(i - 3)).getNode().getText().equals("地区")
                        ) {
                    wx_location = nodeBean.getText();
                }
                if (xmlData.contains("社交资料") && ("com.tencent.mm:id/ga".equals(nodeBean.getResourceid()))) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取消息
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                    xmlData = wxUtils.getXmlData();
                    List<String> phoneMessageList = wxUtils.getNodeList(xmlData);
                    for (int j = 0; j < phoneMessageList.size(); j++) {
                        NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(phoneMessageList.get(j)).getNode();
                        if (nodeBean2 != null && "android:id/summary".equals(nodeBean2.getResourceid()) && xmlData.contains("手机")) {
                            String wx_phone_numer2 = nodeBean2.getText();
                            String[] wx_message = wx_phone_numer2.split("\\s+");
                            wx_phone_number = wx_message[1];
                            wxUtils.adb("input keyevent 4");
                            break;
                        }
                    }
                }
            }
            Log.d("获取到的信息", "微信名字： " + wx_name + "微信所在地区： " + wx_location + " 微信手机号：" + wx_phone_number + " 手机联系人名： " + wx_phone_name);
            ShowToast.show("统计微信好友信息", (Activity) context);
            wxUtils.adb("input keyevent 4");
            String uid = SPUtils.getString(context, "uid", "0000");
            WxFriendsMessageBean messageBean = new WxFriendsMessageBean(wx_number, wx_name, wx_phone_number, wx_phone_name, wx_location, uid);
            mWxFriendsMessageBean.add(messageBean);
            String str = new Gson().toJson(mWxFriendsMessageBean);
            LogUtils.d("JSON" + str.toString());
            sendWxFriendsMessage(str);
            return true;
        }
        wxUtils.adb("input keyevent 4");
        return false;
    }

    /**
     * 检测添加好友以后 是否接收到了消息
     */
    private void CheckMessage() {
        String biaoqian = "";
        boolean mark = true;
        wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y362, R.dimen.x160, R.dimen.y400);//通讯录
        xmlData = wxUtils.getXmlData();
        List<String> ch_list = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < ch_list.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ch_list.get(i)).getNode();
            if ("com.tencent.mm:id/avq".equals(nodeBean.getResourceid())) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取消息
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击消息
                xmlData = wxUtils.getXmlData();
                //while (mark){
                List<String> message_xiang = wxUtils.getNodeList(xmlData);
                if (xmlData.contains("添加") || xmlData.contains("等待验证")) {
                    for (int k = 0; k < message_xiang.size(); k++) {
                        //LogUtils.d("设置助手大撒打算的萨达www"+xmlData);
                        NodeXmlBean.NodeBean message_bean = wxUtils.getNodeXmlBean(message_xiang.get(k)).getNode();
                        if ("com.tencent.mm:id/aw4".equals(message_bean.getResourceid())) {
                            biaoqian = message_bean.getText();
                            if (!"手机联系人".contains(biaoqian)) {
                                // LogUtils.d("设置助手大撒打算的萨达" + wxUtils.getNodeXmlBean(message_xiang.get(k - 1)).getNode().getText());
                                if (!myCheck_massage.contains(wxUtils.getNodeXmlBean(message_xiang.get(k - 1)).getNode().getText())) {
                                    listXY = wxUtils.getXY(message_bean.getBounds());//获取消息
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击消息
                                    xmlData = wxUtils.getXmlData();
                                    List<String> lie_bean = wxUtils.getNodeList(xmlData);
                                    for (int s = 0; s < lie_bean.size(); s++) {//遍历该界面
                                        NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(lie_bean.get(s)).getNode();
                                        if ("com.tencent.mm:id/awf".equals(nodeBean1.getResourceid())) {
                                            if ("回复".equals(nodeBean1.getText())) {
                                                listXY = wxUtils.getXY(nodeBean1.getBounds());//获取消息回复
                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击消息回复
                                                int x = context.getResources().getDimensionPixelSize(R.dimen.x160);
                                                int y = context.getResources().getDimensionPixelSize(R.dimen.y119);//EdiText
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                                ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                                cm.setText("您是京东挑选的优质用户，我们是免费为您发放京东优惠券的！");
                                                wxUtils.adbDimensClick(context, R.dimen.x64, R.dimen.y80, R.dimen.x104, R.dimen.y96);//点击复制 黏贴
                                                xmlData = wxUtils.getXmlData();
                                                List<String> myOk = wxUtils.getNodeList(xmlData);
                                                for (int p = 0; p < myOk.size(); p++) {
                                                    NodeXmlBean.NodeBean nodeBean_myok = wxUtils.getNodeXmlBean(myOk.get(p)).getNode();
                                                    if ("com.tencent.mm:id/abz".equals(nodeBean_myok.getResourceid())) {
                                                        if ("确定".equals(nodeBean_myok.getText())) {
                                                            listXY = wxUtils.getXY(nodeBean_myok.getBounds());//获取消息
                                                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击消息
                                                            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回

                                                        }
                                                    }
                                                }
                                            }
                                            //                                            } else {
                                            //                                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y51);//返回
                                            //                                            }
                                        }
                                        //
                                    }
                                }
                            }
                        }
                        if ("com.tencent.mm:id/aw3".equals(nodeBean.getResourceid())) {
                            myCheck_massage.add(nodeBean.getText());//将好友名称加入到集合中
                        }
                    }
                }

                // }


            }
        }

    }

    /**
     * 检测在不在微信的首页
     */

    String add_phone = "";

    /**
     * 手机通讯录添加好友 搜索将手机号码添加到手机本地联系人中 再去进行添加
     */
    private void getPhoneAdd() {
        //"http://103.94.20.101:8087/api_wechat/index.php";
        // 模拟http请求，提交数据到服务器
        String uid = SPUtils.getString(context, "uid", "0000");
        int add_nums = 0;
        int max = 0;
        int min = 0;
        LogUtils.d("进来的第一次");
        if (StringUtils.isEmpty(one_add_num_s) || StringUtils.isEmpty(one_add_num_e)) {
            add_nums = 5;
        } else {
            max = Integer.parseInt(one_add_num_e);
            min = Integer.parseInt(one_add_num_s);
            Random random = new Random();
            add_nums = random.nextInt(max) % (max - min + 1) + min;
            LogUtils.d("通讯录添加好友的次数为" + add_nums);
        }
        SPUtils.putString(context, "TimeOut", "0");
        String account = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
        String accountLocation = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
        LogUtils.d("微信搜索添加好友的接口是:" + URLS.phone_url + "?zh=" + uid + "&limit=" + add_nums + "&account=" + account);
        try {
            Response data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
            if (data.code() == 200) {
                String string = data.body().string();
                Log.d("zs1", string);
                WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                for (int i = 0; i < phoneBean.getData().size(); i++) {
                    wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                    LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                    ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                }
                AddCommunication(wx_Sex);
            } else {
                ShowToast.show("第一次获取号码失败", (Activity) context);
                data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
                if (data.code() == 200) {
                    String string = data.body().string();
                    Log.d("zs1", string);
                    WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                    ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                    for (int i = 0; i < phoneBean.getData().size(); i++) {
                        wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                        LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                        ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                    }
                    AddCommunication(wx_Sex);
                } else {
                    ShowToast.show("第二次获取号码失败", (Activity) context);
                    data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
                    if (data.code() == 200) {
                        String string = data.body().string();
                        Log.d("zs1", string);
                        WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                        ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                        for (int i = 0; i < phoneBean.getData().size(); i++) {
                            wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                            LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                            ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                        }
                        AddCommunication(wx_Sex);
                    } else {
                        ShowToast.show("第三次获取号码失败", (Activity) context);
                        String uid2 = SPUtils.getString(context, "uid", "0000");
                        String wxAccount2 = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
                        String accountLocation2 = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
                        upload(uid2, wxAccount2, accountLocation2);
                    }
                }
            }

        } catch (IOException e) {
            SPUtils.putString(context, "TimeOut", "1");
            e.printStackTrace();
        }
        if (SPUtils.getString(context, "TimeOut", "").equals("1")) {
            try {
                Response data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
                SPUtils.putString(context, "TimeOut", "0");
                if (data.code() == 200) {
                    String string = data.body().string();
                    Log.d("zs1", string);
                    WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                    ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                    for (int i = 0; i < phoneBean.getData().size(); i++) {
                        wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                        LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                        ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                    }
                    AddCommunication(wx_Sex);
                } else {
                    ShowToast.show("第一次获取号码失败", (Activity) context);
                    data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
                    if (data.code() == 200) {
                        String string = data.body().string();
                        Log.d("zs1", string);
                        WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                        ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                        for (int i = 0; i < phoneBean.getData().size(); i++) {
                            wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                            LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                            ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                        }
                        AddCommunication(wx_Sex);
                    } else {
                        ShowToast.show("第二次获取号码失败", (Activity) context);
                        data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
                        if (data.code() == 200) {
                            String string = data.body().string();
                            Log.d("zs1", string);
                            WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                            ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                            for (int i = 0; i < phoneBean.getData().size(); i++) {
                                wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                                LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                                ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                            }
                            AddCommunication(wx_Sex);
                        } else {
                            ShowToast.show("第三次获取号码失败", (Activity) context);
                            String uid2 = SPUtils.getString(context, "uid", "0000");
                            String wxAccount2 = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
                            String accountLocation2 = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
                            upload(uid2, wxAccount2, accountLocation2);
                        }
                    }
                }

            } catch (IOException e) {

                SPUtils.putString(context, "TimeOut", "1");
                e.printStackTrace();
            }
        }
        if (SPUtils.getString(context, "TimeOut", "").equals("1")) {
            try {
                Response data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
                SPUtils.putString(context, "TimeOut", "0");
                if (data.code() == 200) {
                    if (data.body() != null) {
                        String string = data.body().string();
                        Log.d("zs1", string);
                        WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                        ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                        for (int i = 0; i < phoneBean.getData().size(); i++) {
                            wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                            LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                            ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                        }
                        AddCommunication(wx_Sex);
                    } else {
                        ShowToast.show("服务器没数据了！", (Activity) context);
                    }

                } else {
                    ShowToast.show("第一次获取号码失败", (Activity) context);
                    data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
                    if (data.code() == 200) {
                        if (data.body() != null) {
                            String string = data.body().string();
                            Log.d("zs1", string);
                            WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                            ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                            for (int i = 0; i < phoneBean.getData().size(); i++) {
                                wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                                LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                                ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                            }
                            AddCommunication(wx_Sex);
                        } else {
                            ShowToast.show("服务器没数据了！", (Activity) context);
                        }
                    } else {
                        ShowToast.show("第二次获取号码失败", (Activity) context);
                        data = OkHttpUtils.get().url(URLS.phone_url).addParams("zh", uid).addParams("limit", add_nums + "").addParams("account", account).build().execute();
                        if (data.code() == 200) {
                            if (data.body() != null) {
                                String string = data.body().string();
                                Log.d("zs1", string);
                                WxPhone phoneBean = GsonUtil.parseJsonWithGson(string, WxPhone.class);
                                ShowToast.show("正在添加手机联系人请稍后...", (Activity) context);
                                for (int i = 0; i < phoneBean.getData().size(); i++) {
                                    wxUtils.addContact(phoneBean.getData().get(i).getName(), phoneBean.getData().get(i).getPhone(), context);
                                    LogUtils.d("电话号码为" + phoneBean.getData().get(i).getPhone());
                                    ShowToast.show("电话号码" + phoneBean.getData().get(i).getPhone(), (Activity) context);
                                }
                                AddCommunication(wx_Sex);
                            } else {
                                ShowToast.show("服务器没数据了！", (Activity) context);
                            }
                        } else {
                            ShowToast.show("第三次获取号码失败", (Activity) context);
                            String uid2 = SPUtils.getString(context, "uid", "0000");
                            String wxAccount2 = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
                            String accountLocation2 = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
                            upload(uid2, wxAccount2, accountLocation2);
                        }
                    }
                }

            } catch (IOException e) {

                SPUtils.putString(context, "TimeOut", "1");
                e.printStackTrace();
            }
        }
        if (SPUtils.getString(context, "TimeOut", "").equals("1")) {
            String uid2 = SPUtils.getString(context, "uid", "0000");
            String wxAccount2 = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
            String accountLocation2 = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
            upload(uid2, wxAccount2, accountLocation2);

        }
    }

    private void upload(final String uid, final String wxAccount, final String accountLocation) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response data = OkHttpUtils.get().url(URLS.url + "home/ApiAndroid/wx_exceptions_upload").addParams("uid", uid).addParams("account", wxAccount + "").addParams("location", accountLocation).build().execute();
                    if (data.code() == 200) {
                        ShowToast.show("第三次获取号码失败 异常信息上传成功！", (Activity) context);
                    } else {
                        data = OkHttpUtils.get().url(URLS.url + "home/ApiAndroid/wx_exceptions_upload").addParams("uid", uid).addParams("account", wxAccount + "").addParams("location", accountLocation).build().execute();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 自动通过好友申请
     */
    private void AddFriend() {
        int max = 0;
        int min = 0;
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//通讯录
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//通讯录
        xmlData = wxUtils.getXmlData();
        boolean mark = true;
        while (mark) {
            List<String> meWxFriend = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("接受")) {
                for (int i = 0; i < meWxFriend.size(); i++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(meWxFriend.get(i)).getNode();
                    if ("com.tencent.mm:id/b8k".equals(nodeBean.getResourceid())) {
                        if ("接受".equals(nodeBean.getText())) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//接受
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//完成
                            xmlData = wxUtils.getXmlData();
                            wxUtils.adb("input keyevent 4");
                            if (StringUtils.isEmpty(agree_interval_time_s) && StringUtils.isEmpty(agree_interval_time_e)) {
                                SPUtils.putString(context, "agree_interval_time_s", "");
                                SPUtils.putString(context, "agree_interval_time_e", "");
                            } else {
                                SPUtils.putString(context, "agree_interval_time_s", agree_interval_time_s);
                                SPUtils.putString(context, "agree_interval_time_e", agree_interval_time_e);
                            }
                            if (StringUtils.isEmpty(SPUtils.getString(context, agree_interval_time_s, "")) && StringUtils.isEmpty(SPUtils.getString(context, agree_interval_time_e, ""))) {
                                max = 120;
                                min = 30;
                            } else {
                                max = Integer.parseInt(SPUtils.getString(context, agree_interval_time_e, "").trim());
                                min = Integer.parseInt(SPUtils.getString(context, agree_interval_time_s, "").trim());
                            }
                            Random random = new Random();
                            int s = random.nextInt(max) % (max - min + 1) + min;
                            LogUtils.d("通讯录添加好友的休眠的时间为" + s + "秒");
                            try {
                                Thread.sleep(s * 1000);
                            } catch (InterruptedException e) {

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
            } else {
                mark = false;
                ShowToast.show("没有可以通过请求的好友", (Activity) context);
                wxUtils.adb("input keyevent 4");

            }

        }
    }

    /**
     * 打开附近人
     */
    private void OpendFujin() {
        //        NodeUtils.clickNode("发现", "com.tencent.mm:id/c_z");//发现
        NodeUtils.clickNode("发现", "com.tencent.mm:id/c_z");//发现
        //        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y194, R.dimen.x320, R.dimen.y228);//附近人
        NodeUtils.clickNode("附近的人", "android:id/title");//附近人
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("你可以在此看到附近的人，同时你也会被他们看到。你在此留下的位置信息可以随时手动清除。")) {
            List<String> meWxIdList = wxUtils.getNodeList(xmlData);
            b:
            for (int b = 0; b < meWxIdList.size(); b++) {
                NodeXmlBean.NodeBean meWxIdBean = wxUtils.getNodeXmlBean(meWxIdList.get(b)).getNode();
                if ("com.tencent.mm:id/bv6".equals(meWxIdBean.getResourceid())) {
                    listXY = wxUtils.getXY(meWxIdBean.getBounds());//添加
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加
                }
            }
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("查看附近的人功能将获取你的位置信息，你的位置信息会被保留一段时间。通过列表右上角的清除功能可随时手动清除位置信息。")) {
            //            wxUtils.adbDimensClick(context, R.dimen.y45, R.dimen.y238, R.dimen.x148, R.dimen.y252);//第一次进入
            NodeUtils.clickNode("下次不提示", "com.tencent.mm:id/bx8");//第一次进入
            //            wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y263, R.dimen.x264, R.dimen.y284);//确定按钮
            NodeUtils.clickNode("确定", "com.tencent.mm:id/all");//确定按钮
            xmlData = wxUtils.getXmlData();
        }
        boolean boo = true;
        while (boo) {
            List<String> meWxIdList = wxUtils.getNodeList(xmlData);
            for (int i = 0; i < meWxIdList.size(); i++) {
                NodeXmlBean.NodeBean meWxIdBean = wxUtils.getNodeXmlBean(meWxIdList.get(i)).getNode();
                if ("com.tencent.mm:id/ali".equals(meWxIdBean.getResourceid())) {
                    listXY = wxUtils.getXY(meWxIdBean.getBounds());//添加
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击添加
                    if ("com.tencent.mm:id/acw".equals(meWxIdBean.getResourceid())) {
                        if ("打招呼".equals(meWxIdBean.getText())) {
                            listXY = wxUtils.getXY(meWxIdBean.getBounds());//添加
                            wxUtils.adb("input keyevent 4");
                        }
                    }

                } else {
                    boo = false;
                }
                String TAG = xmlData;
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (TAG.equals(xmlData)) {
                    break;
                }
            }
        }
    }


    /**
     * 拉完群反馈信息给服务器
     */
    private void upddateGroup() {//status 0没群 1男群满   2女群满  3男女都满  4正常 //
        if (status != 0 && status != 4) {
            if (boyEnd && girlEnd) {
                status = 3;
            } else if (boyEnd) {//flase 代表有剩余群  true代表群不够
                status = 1;
            } else if (girlEnd) {
                status = 2;
            }
        }
        RequestParams params = new RequestParams(URLS.upddateGroup());
        params.addQueryStringParameter("uid", SPUtils.getString(context, "uid", "0000"));
        params.addQueryStringParameter("status", status + "");
        LogUtils.d(URLS.upddateGroup() + "?uid=" + SPUtils.getString(context, "uid", "0000") + "&status=" + status);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<CheckImei>() {

            @Override
            public void onSuccess(CheckImei bean) {

            }

            @Override
            public void onFailure(int errorCode, String errorString) {

            }
        });

    }

    int qunMaxNum = 40;//设置群人数
    boolean boyEnd = true;//如果是flase带表男群拉完
    boolean girlEnd = true;//如果是flase带表女群拉完

    /**
     * 拉群
     */
    private void addCrowd() {
        //初始化数据
        bogCount = 0;//要改名的男好友
        girlCount = 0;//要改名的男好友
        unknownCount = 0;//未知性别
        //        countAddStr = "";//判断是否已经勾选过
        countStr = "";//判断是否已经选择
        boyEnd = true;//如果是flase带表男群拉完
        girlEnd = true;//如果是flase带表女群拉完

        //_________________________________________

        String qunClickMark = "";
        boolean isOneSlide = false;
        int count = 0;
        boolean bottom = false;//到了底部
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if ("群聊".equals(nodeBean.getText())) {//获取群聊node节点
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群聊坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击群聊
                break;
            }
        }
        //进入了群列表
        w:
        while (true) {
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                status = 0;
                wxUtils.adb("input keyevent 4");
                //                ShowToast.show("没有群...", (Activity) context);
                break;
            }
            List<String> nodeList = new ArrayList<>();
            Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
            Matcher matcher = pattern.matcher(xmlData);
            while (matcher.find()) {
                nodeList.add(matcher.group() + "/>");
            }
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().contains("com.tencent.mm:id/a9u"))) {
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

                    if (!boyEnd && !girlEnd) {//拉群任务完成
                        wxUtils.adb("input keyevent 4");
                        ShowToast.show("拉群任务完成", (Activity) context);
                        break w;
                    }

                    if (qunClickMark.contains(nodeBean.getText())) {
                        continue;
                    } else {
                        if (!isOneSlide) {
                            for (int b = 0; b < count; b++) {
                                wxUtils.adbUpSlide(context);//向上滑动
                            }
                        }
                        isOneSlide = false;
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        qunClickMark = qunClickMark + nodeBean.getText() + ",";
                    }

                    //_______________________________________________________________________________________________
                    String qunName = "";
                    //获取群人数，男女群信息
                    String qunNameData = wxUtils.getXmlData();
                    List<String> qunNameDataList = new ArrayList<String>();
                    Matcher matcherA = pattern.matcher(qunNameData);
                    while (matcherA.find()) {
                        qunNameDataList.add(matcherA.group() + "/>");
                    }
                    if (!(qunNameData.contains("当前所在页面,与"))) {
                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                        break w;
                    }

                    for (int c = 0; c < qunNameDataList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
                        LogUtils.d(qunNameBean.toString());
                        if ("com.tencent.mm:id/hj".equals(qunNameBean.getResourceid())) {
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
                                addMember(sex, qb);

                                //设置间隔时间
                                int start;
                                if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_s())) {
                                    start = 3;
                                } else {
                                    start = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_s());
                                }
                                int end;
                                if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_e())) {
                                    end = 6;
                                } else {
                                    end = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_e());
                                }
                                int timeSleep = random.nextInt(end - start + 1) + start;
                                LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                                ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                                try {
                                    Thread.sleep(timeSleep * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }
                        }
                    }
                    //_______________________________________________________________________________________________
                    wxUtils.adb("input keyevent 4");
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");
                    //                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    //                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊
                    NodeUtils.clickNode("群聊", "com.tencent.mm:id/j5");
                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            count++;
            isOneSlide = true;
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData)) {
                wxUtils.adb("input keyevent 4");
                ShowToast.show("拉群任务完成", (Activity) context);
                break;
            }
        }
        LogUtils.d(bogCount + "___girl" + girlCount + "未知____" + unknownCount);
        //拉完群改名
        ShowToast.show("拉群完，修改备注开始", (Activity) context);
        qunEndAlterName();
    }

    /**
     * 拉完群改名修改备注.
     */
    private void qunEndAlterName() {
        String zzzXmlData = "";
        int boyAlterCount = 0;
        int girlAltCount = 0;
        int unknownAltCount = 0;
        boolean bottom = false;//到了底部
        DecimalFormat df = new DecimalFormat("0000");
        String endData = "";
        String alterName = "";

        w:
        while (true) {
            while (true) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                    break w;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else {
                    break;
                }
            }

            if (boyAlterCount >= bogCount && girlAltCount >= girlCount && unknownAltCount >= unknownCount) {
                ShowToast.show("拉群后改名完成", (Activity) context);
                break w;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && nodeBean.getContentdesc().startsWith("ZZZ0") && countStr.contains(nodeBean.getContentdesc()) && !alterName.contains(nodeBean.getContentdesc())) {
                    String friendsName = nodeBean.getContentdesc();
                    if (nodeBean.getText().contains("A") && boyAlterCount >= bogCount) {
                        continue;
                    } else if (nodeBean.getText().contains("B") && girlAltCount >= girlCount) {
                        continue;
                    } else if (nodeBean.getText().contains("C") && unknownAltCount >= unknownCount) {
                        continue;
                    }
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    xmlData = wxUtils.getXmlData();//重新获取页面数据

                    //                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y135, R.dimen.x320, R.dimen.y166);//点击设置备注和标签

                    List<String> remarkList = wxUtils.getNodeList(xmlData);
                    for (int r = 0; r < remarkList.size(); r++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                        if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                            //筛选出好友
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                            break;
                        }
                    }
                    xmlData = wxUtils.getXmlData();

                    if (xmlData.contains("备注信息") && xmlData.contains("完成")) {

                    } else {
                        continue w;
                    }

                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '1';
                        sexStr = new String(ch);
                    }
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    LogUtils.e("设置备注==" + "input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    alterName = alterName + friendsName + ",";
                    if (nodeBean.getText().contains("A")) {
                        boyAlterCount++;
                    } else if (nodeBean.getText().contains("B")) {
                        girlAltCount++;
                    } else if (nodeBean.getText().contains("C")) {
                        unknownAltCount++;
                    }
                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                        wxUtils.adb("input keyevent 4");
                    }

                    //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_s())) {
                        start = 3;
                    } else {
                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_e())) {
                        end = 6;
                    } else {
                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_e());
                    }
                    int timeSleep = random.nextInt(end - start + 1) + start;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("发现")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && nodeBean.getContentdesc().startsWith("ZZZ0") && countStr.contains(nodeBean.getContentdesc())) {
                    if (nodeBean.getText().startsWith("ZZZ0A") && boyAlterCount < bogCount) {
                        if (xmlData.equals(zzzXmlData)) {
                            wxUtils.adbUpSlide(context);//向上滑动
                        }
                        zzzXmlData = wxUtils.getXmlData();
                        continue w;
                    } else if (nodeBean.getText().startsWith("ZZZ0B") && girlAltCount < girlCount) {
                        if (xmlData.equals(zzzXmlData)) {
                            wxUtils.adbUpSlide(context);//向上滑动
                        }
                        zzzXmlData = wxUtils.getXmlData();
                        continue w;
                    } else if (nodeBean.getText().startsWith("ZZZ0C") && unknownAltCount < unknownCount) {
                        if (xmlData.equals(zzzXmlData)) {
                            wxUtils.adbUpSlide(context);//向上滑动
                        }
                        zzzXmlData = wxUtils.getXmlData();
                        continue w;
                    }
                }
            }
            if (!bottom) {
                wxUtils.adbUpSlide(context);//向上滑动aaaadrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData) && xmlData.contains("位联系人")) {
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y367, R.dimen.x80, R.dimen.y400);//点击微信
                ShowToast.show("修改备注完成,拉群任务完成", (Activity) context);
                LogUtils.d(boyAlterCount + "修改备注完成,拉群任务完成" + girlAltCount);
                break w;
            }
            if (xmlData.contains("位联系人")) {//判断是否到达底部
                bottom = true;
            }
        }
    }


    int bogCount = 0;//要改名的男好友
    int girlCount = 0;//要改名的男好友
    int unknownCount = 0;
    //    String countAddStr = "";//判断是否已经勾选过
    String countStr = "";//判断是否已经勾选过

    /**
     * 拉群添加成员
     *
     * @param qb  群当前人数
     * @param sex 性别
     */
    private void addMember(int sex, int qb) {
        int clickCount = 0;//选中次数
        List<String> qunNameDataList = new ArrayList<>();
        wxUtils.adbDimensClick(context, R.dimen.x272, R.dimen.y17, R.dimen.x320, R.dimen.y51);//确定
        w:
        while (true) {//获取添加按钮并点击
            String qunNameData = wxUtils.getXmlData();
            if (qunNameData.contains("添加")) {

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


        w:
        while (true) {
            for (int a = 0; a < addNameList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(addNameList.get(a)).getNode();
                if ("com.tencent.mm:id/kq".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ0")) {
                    NodeXmlBean.NodeBean checkBox = wxUtils.getNodeXmlBean(addNameList.get(a + 1)).getNode();

                    if (checkBox.getResourceid() != null && ("com.tencent.mm:id/sf".equals(checkBox.getResourceid())) && checkBox.getClassX() != null && checkBox.getClassX().equals("android.widget.CheckBox") && checkBox.isChecked()) {//isChecked true代表选中
                        if (!countStr.contains(nodeBean.getText())) {
                            if (nodeBean.getText().contains("B")) {
                                girlCount++;
                                LogUtils.d(girlCount + "girlCount+选中");
                                countStr = countStr + nodeBean.getText() + ",";
                            } else if (nodeBean.getText().contains("A")) {
                                bogCount++;
                                LogUtils.d(bogCount + "bogCount选中____________________");
                                countStr = countStr + nodeBean.getText() + ",";
                            } else if (nodeBean.getText().contains("C")) {
                                unknownCount++;
                                LogUtils.d(unknownCount + "unknownCount选中____________________");
                                countStr = countStr + nodeBean.getText() + ",";
                            }
                        }
                    }

                    if (sex == 0 && nodeBean.getText().contains("B")) {//女
                        if (checkBox.getResourceid() != null && ("com.tencent.mm:id/sf".equals(checkBox.getResourceid())) && checkBox.getClassX() != null && checkBox.getClassX().equals("android.widget.CheckBox") && !checkBox.isChecked()) {
                            if (countStr.contains(nodeBean.getText())) {
                                continue;
                            } else {
                                if (clickCount + qb < qunMaxNum) {
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                                    countStr = countStr + nodeBean.getText() + ",";
                                    clickCount++;
                                    girlCount++;
                                    LogUtils.d(girlCount + "girlCount+点击");
                                } else {
                                    break w;
                                }
                            }
                        }
                    } else if (sex == 1 && (nodeBean.getText().contains("A") || nodeBean.getText().contains("C"))) {//男
                        if (checkBox.getResourceid() != null && ("com.tencent.mm:id/sf".equals(checkBox.getResourceid())) && checkBox.getClassX() != null && checkBox.getClassX().equals("android.widget.CheckBox") && !checkBox.isChecked()) {
                            if (countStr.contains(nodeBean.getText())) {
                                continue;
                            } else {
                                if (clickCount + qb < qunMaxNum) {
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                                    countStr = countStr + nodeBean.getText() + ",";
                                    clickCount++;
                                    if (nodeBean.getText().contains("A")) {
                                        bogCount++;
                                        LogUtils.d(bogCount + "bogCount点击____________________");
                                    } else if (nodeBean.getText().contains("C")) {
                                        unknownCount++;
                                        LogUtils.d(unknownCount + "unknownCount点击____________________");
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
                    if (("com.tencent.mm:id/kq".equals(nodeBean.getResourceid())) && nodeBean.getText().startsWith("ZZZ1")) {
                        judgeGirl++;
                    }
                } else {//男
                    if (("com.tencent.mm:id/kq".equals(nodeBean.getResourceid())) && nodeBean.getText().startsWith("ZZZ1")) {
                        judgeBoy++;
                    }
                }
            }
            if (judgeGirl >= 10) {//女生拉完
                girlEnd = false;
                break;
            } else if (judgeBoy >= 10) {//男生拉完
                boyEnd = false;
                break;
            }
        }
        if (clickCount > 0) {//确认添加
            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
            while (true) {
                String qunLiaoBack = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (qunLiaoBack.contains("切换到")) {
                    wxUtils.adb("input keyevent 4");
                    break;
                } else {
                    wxUtils.adb("input keyevent 4");
                }
            }

        } else {//没有选中.返回
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
            wxUtils.adb("input keyevent 4");
        }
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊
    }

    private String yunYingMark = "";//运营号

    /**
     * 修改备注.
     */
    private void startAlterName(String zzz) {
        if (StringUtils.isEmpty(zzz)) {
            zzz = "ZZZ0";
        } else if (zzz != null && zzz.length() > 10) {
            zzz = "ZZZ9";
        }
        boolean bottom = false;//到了底部
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("0000");
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        String meName = "";
        w:
        while (true) {


            while (true) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                    break w;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else {
                    break;
                }
            }


            List<String> nodeList = wxUtils.getNodeList(xmlData);
            a:
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手")
                        && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())) {
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注

                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        meName = nodeBean.getContentdesc();
                        continue;
                    }
                    StatisticsWxFriends(xmlData);//统计新增好友的信息
                    List<String> meWxIdList = wxUtils.getNodeList(xmlData);
                    if (xmlData.contains("女")) {
                        sex = 0;
                    } else if (xmlData.contains("男")) {
                        sex = 1;
                    } else {
                        sex = 2;
                    }
                    xmlData = wxUtils.getXmlData();//重新获取页面数据

                    //                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y135, R.dimen.x320, R.dimen.y166);//点击设置备注和标签

                    List<String> remarkList = wxUtils.getNodeList(xmlData);
                    for (int r = 0; r < remarkList.size(); r++) {
                        nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                        if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                            //筛选出好友
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                            break;
                        }
                    }
                    xmlData = wxUtils.getXmlData();

                    if (xmlData.contains("备注信息") && xmlData.contains("完成")) {

                    } else {
                        continue w;
                    }
                    //                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    NodeUtils.clickNode(null, "com.tencent.mm:id/aoy");

                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                    switch (sex) {//0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl", 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                            wxUtils.adb("input text " + zzz + "B" + wx_nume_number_new_girl);
                            SPUtils.put(context, "wx_name_number_girl", wx_name_number_girl + 1);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy", 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                            wxUtils.adb("input text " + zzz + "A" + wx_nume_number_new_boy);
                            SPUtils.put(context, "wx_name_number_boy", wx_name_number_boy + 1);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c", 0);
                            String wx_nume_number_c = df.format(wx_name_number_c + 1);
                            wxUtils.adb("input text " + zzz + "C" + wx_nume_number_c);
                            SPUtils.put(context, "wx_name_number_c", wx_name_number_c + 1);
                            break;
                    }
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    //  LogUtils.d(nodeList.get(a));
                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                        wxUtils.adb("input keyevent 4");
                    }

                    //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_s())) {
                        start = 3;
                    } else {
                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRemark_interval_time_e())) {
                        end = 6;
                    } else {
                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getRemark_interval_time_e());
                    }
                    int timeSleep = random.nextInt(5 - 3 + 1) + 3;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("发现")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc())) {
                    continue w;
                } else if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && nodeBean.getContentdesc().startsWith("ZZZ")) {
                    zzzNum++;
                }
            }

            if (!bottom) {
                if (zzzNum >= 9) {
                    wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y357, R.dimen.x320, R.dimen.y365);
                } else {
                    wxUtils.adbUpSlide(context);//向上滑动
                }
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                ShowToast.show("修改备注完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("位联系人")) {//判断是否到达底部
                bottom = true;
            }
        }
    }

    /**
     * 修改备注.
     */
    private void startAlterName2(String zzz) {
        if (StringUtils.isEmpty(zzz)) {
            zzz = "YYY0";
        } else if (zzz != null && zzz.length() > 10) {
            zzz = "YYY9";
        }
        boolean bottom = false;//到了底部
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("0000");
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        String meName = "";
        int newFriendsCount = 0;
        w:
        while (true) {


            while (true) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                    break w;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else {
                    break;
                }
            }
            String oldXmlData = "";

            List<String> nodeList = wxUtils.getNodeList(xmlData);
            a:
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz")
                        && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("XX") && !nodeBean.getContentdesc().startsWith("AA")
                        && !meName.equals(nodeBean.getContentdesc())) {
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注

                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        meName = nodeBean.getContentdesc();
                        continue;
                    }
                    //                    StatisticsWxFriends(xmlData);//统计新增好友的信息
                    List<String> meWxIdList = wxUtils.getNodeList(xmlData);
                    if (xmlData.contains("女")) {
                        sex = 0;
                    } else if (xmlData.contains("男")) {
                        sex = 1;
                    } else {
                        sex = 2;
                    }
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    List<String> remarkList = wxUtils.getNodeList(xmlData);
                    for (int r = 0; r < remarkList.size(); r++) {
                        nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                        if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                            //筛选出好友
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                            newFriendsCount++;
                            SPUtils.putInt(context, "NewFriendsCount", newFriendsCount);
                            break;
                        }
                    }
                    xmlData = wxUtils.getXmlData();

                    if (xmlData.contains("备注信息") && xmlData.contains("完成")) {

                    } else {
                        continue w;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                    switch (sex) {//0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl", 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                            wxUtils.adb("input text " + zzz + "B" + wx_nume_number_new_girl);
                            SPUtils.put(context, "wx_name_number_girl", wx_name_number_girl + 1);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy", 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                            wxUtils.adb("input text " + zzz + "A" + wx_nume_number_new_boy);
                            SPUtils.put(context, "wx_name_number_boy", wx_name_number_boy + 1);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c", 0);
                            String wx_nume_number_c = df.format(wx_name_number_c + 1);
                            wxUtils.adb("input text " + zzz + "C" + wx_nume_number_c);
                            SPUtils.put(context, "wx_name_number_c", wx_name_number_c + 1);
                            break;
                    }
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    //  LogUtils.d(nodeList.get(a));
                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                        wxUtils.adb("input keyevent 4");
                    }
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("发现")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                continue w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                        && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("XX") && !nodeBean.getContentdesc().startsWith("AA")
                        && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())) {
                    continue w;
                } else if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                        && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && (nodeBean.getContentdesc().startsWith("YYY")
                        || nodeBean.getContentdesc().startsWith("ZZZ") || nodeBean.getContentdesc().startsWith("zzz")) && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())
                        ) {
                    zzzNum++;
                }
            }
            int aaaaa = 0;
            if (!bottom) {
                if (zzzNum >= 8) {
                    //                    wxUtils.adbDimensClick(context, 460, 768,460, 768);
                    wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                    String xmlData2 = wxUtils.getXmlData();
                    nodeList = wxUtils.getNodeList(xmlData2);
                    for (int b = 0; b < nodeList.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                        if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                            String xmlData3 = wxUtils.getXmlData();
                            //                            meName =meName+nodeBean.getContentdesc();
                            if (xmlData3.contains("备注和标签")) {
                                aaaaa++;
                            } else {
                                meName = meName + nodeBean.getContentdesc();
                            }
                            wxUtils.adb("input keyevent 4");//返回
                        }
                    }
                    if (aaaaa == 0) {
                        //说明全是ZZZ或者zzz开头的了
                        wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                        wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                        xmlData = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData);
                        int ccc = 0;
                        for (int b = 0; b < nodeList.size(); b++) {
                            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                            if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                    && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY0") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                                ccc++;
                            }
                        }
                        if (ccc == 0) {
                            ShowToast.show("修改备注完成", (Activity) context);
                            break w;
                        }
                    }
                } else {
                    oldXmlData = wxUtils.getXmlData();
                    wxUtils.adbUpSlide(context);//向上滑动
                }

                //            endData = xmlData;
                xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
                if (oldXmlData.equals(xmlData)) {
                    ShowToast.show("修改备注完成", (Activity) context);
                    break w;
                }
                nodeList = wxUtils.getNodeList(xmlData);
                int bbb = 0;
                for (int b = 0; b < nodeList.size(); b++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                    if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                            && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                        bbb++;
                    }
                }
                if (bbb == 0) {
                    wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                    wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                }
            }
        }
    }


    int wxCount = 0;
    boolean wxAddMark = true;
    //    String[] phoneList;
    List<AddFriendsMobileBean.DataBean> dataBeanList;

    /**
     * 自助加好友
     *
     * @param num
     */
    private void sniffingAddFriendsDatas(int num) {
        String uid = SPUtils.getString(context, "uid", "0000");
        RequestParams params = new RequestParams(URLS.phone_url);
        params.addQueryStringParameter("zh", uid);
        params.addQueryStringParameter("limit", num + "");
        LogUtils.d(URLS.phone_url + "?zh=" + uid + "&limit=" + num);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<AddFriendsMobileBean>() {

            @Override
            public void onSuccess(AddFriendsMobileBean bean) {
                dataBeanList = bean.getData();
                if (dataBeanList.size() > 0) {
                    findWxId(dataBeanList.get(wxCount).getPhone());
                }
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("错误返回的结果" + errorString);
                ShowToast.show("网络请求失败，请检测网络", (Activity) context);
            }
        });
    }


    /**
     * 嗅探2.通过wxid加好友
     */
    private void sniffingAddFriends(String wxid, String introduce) {
        ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.tencent.mm/com.tencent.mm.ui.LauncherUI");

        Common.XPhone_AddIDFriendReq.Builder task = Common.XPhone_AddIDFriendReq.newBuilder();
        Common.XPhone_MsgBase.Builder base = Common.XPhone_MsgBase.newBuilder();
        base.setMsgType(Common.XPhone_MsgType.MsgType_AddIDFriendReq);
        task.setBase(base);
        //3:来自微信号搜索 6:通过好友同意  13:来自手机通讯录 14:群聊 15:来自手机号搜索 17:通过名片分享添加  18:来自附近人 30:通过扫一扫添加 39:搜索公众号来源
        task.setISencse(Integer.valueOf(sniffing_type));
        LogUtils.d("加好友来源" + Integer.valueOf(sniffing_type));
        if (StringUtils.isEmpty(introduce)) {
            task.setStrIntroduce("你好，朋友推荐的");
        } else {
            task.setStrIntroduce(introduce);
        }
        task.setStrIntroduce("你好，朋友推荐的");
        task.setIAddIntrval(random.nextInt(40) + 60); //添加间隔 //60-100的随机数
        task.setIAddCount(2);
/*        for (int a = 0; a < wxidList.length; a++) {
            task.addIds(wxidList[a]);//搜索出来的微信号
        }*/
        task.addIds(wxid);//搜索出来的微信号
        //        task.addIds("wxid_dfaf");
        ;//搜索出来的微信号


        Common.XPhone_TaskReq.Builder builder = Common.XPhone_TaskReq.newBuilder();
        base = Common.XPhone_MsgBase.newBuilder();
        base.setMsgType(Common.XPhone_MsgType.MsgType_TaskReq);
        builder.setBase(base);
        builder.setITaskID(2);//设置一个任务ID，任务完成的时候会带上这个ID
        builder.setTaskType(task.getBase().getMsgType());
        builder.setTaskInfo(task.build().toByteString());

        Intent intent2 = new Intent();
        intent2.setAction(CommunicationDefine.Broadcast_AppToWeichat);
        intent2.putExtra("data", builder.build().toByteArray());
        context.sendBroadcast(intent2);
    }

    /**
     * 嗅探1.查询wxid
     */
    private void findWxId(String phone) {
        wxAddMark = false;
        ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.tencent.mm/com.tencent.mm.ui.LauncherUI");

        Common.XPhone_AccountSearchReq.Builder task = Common.XPhone_AccountSearchReq.newBuilder();
        Common.XPhone_MsgBase.Builder base = Common.XPhone_MsgBase.newBuilder();
        base.setMsgType(Common.XPhone_MsgType.MsgType_AccountSearchReq);
        task.setBase(base);
        task.setSearchIntrval(random.nextInt(40) + 60);//设置搜索时间间隔。单位秒。 避免频繁搜索，时间长一点 60-100
  /*      for (int a = 0; a < phoneList.length; a++) {
            task.addAccounts(phoneList[a]);//手机号，可以调用多次，添加多个
        }*/
        task.addAccounts(phone);//手机号，可以调用多次，添加多个
        //        task.addAccounts("18565895962");//手机号，可以调用多次，添加多个

        Common.XPhone_TaskReq.Builder builder = Common.XPhone_TaskReq.newBuilder();
        base = Common.XPhone_MsgBase.newBuilder();
        base.setMsgType(Common.XPhone_MsgType.MsgType_TaskReq);
        builder.setBase(base);
        builder.setITaskID(1);//设置一个任务ID，任务完成的时候会带上这个ID
        builder.setTaskType(task.getBase().getMsgType());
        builder.setTaskInfo(task.build().toByteString());

        Intent intent2 = new Intent();
        intent2.setAction(CommunicationDefine.Broadcast_AppToWeichat);
        intent2.putExtra("data", builder.build().toByteArray());
        context.sendBroadcast(intent2);
    }

    ClientBroadcastRecv m_client;

    /**
     * 嗅探
     */
    class ClientBroadcastRecv extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strAction = intent.getAction();
            if (strAction.equals(CommunicationDefine.Broadcast_WeichatToApp)) {
                byte[] data = intent.getByteArrayExtra("data");
                handleWeichatBroadcast(data);
            }
        }

    }

    /**
     * 嗅探
     *
     * @param data
     */
    protected void handleWeichatBroadcast(byte[] data) {
        try {
            Common.XPhone_ReqHead reqHead = Common.XPhone_ReqHead.parseFrom(data);
            switch (reqHead.getBase().getMsgType()) {
                case MsgType_TaskRsp: {
                    Common.XPhone_TaskRsp request = Common.XPhone_TaskRsp.parseFrom(data);
                    LogUtils.d("askRsp" + request.toString());
                    if (request.getITaskID() == 2) {//2加完好友
                        if (wxCount < dataBeanList.size()) {
                            Thread.sleep(25000);
                            findWxId(dataBeanList.get(wxCount).getPhone());
                        }
                        LogUtils.d("嗅探加好友完成");
                    }

                }
                break;
                case MsgType_AccountSearchRsp: {
                    Common.XPhone_AccountSearchRsp request = Common.XPhone_AccountSearchRsp.parseFrom(data);
                    LogUtils.d("AccountSearchRsp" + request.toString());
                    String[] wxid = new String[request.getUserInfoCount()];
                    for (int i = 0; i < request.getUserInfoCount(); i++) {
                        wxid[i] = request.getUserInfo(i).getStrUserName();
                        LogUtils.d(request.getUserInfo(i).getStrUserName());
                    }
                    wxCount++;
                    LogUtils.d("wxCount" + wxCount);
                    if (wxid.length > 0) {
                        Thread.sleep(25000);
                        sniffingAddFriends(wxid[0], contact_verify_msg.replace("《name》", dataBeanList.get(wxCount - 1).getName()));
                        LogUtils.d(contact_verify_msg.replace("《name》", dataBeanList.get(wxCount - 1).getName()));
                        ShowToast.show(contact_verify_msg.replace("《name》", dataBeanList.get(wxCount - 1).getName()), (Activity) context);
                    } else {
                        if (wxCount < dataBeanList.size()) {
                            Thread.sleep(25000);
                            findWxId(dataBeanList.get(wxCount).getPhone());
                        }
                    }
                }
                break;
                default:
                    break;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 点赞
     *
     * @param likeNum 点赞次数
     */
    private void clickLike(int likeNum) {
        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        int count = 0;
        String oldXmlData = "";
        ShowToast.show("点赞开始", (Activity) context);
        xmlData = wxUtils.getXmlData();
        w:
        while (true) {
            if (!(xmlData.contains("朋友圈") && xmlData.contains("更多功能按钮"))) {
                ShowToast.show("任务中断，结束点赞任务！", (Activity) context);
                break;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (("com.tencent.mm:id/dbk".equals(nodeBean.getResourceid())))) {
                    int num = random.nextInt(2);
                    if (num == 0) {
                        continue;
                    }

                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//打开点赞
                    String xmlLikeData = wxUtils.getXmlData();

                    if (!(xmlLikeData.contains("朋友圈") && xmlLikeData.contains("更多功能按钮"))) {
                        //                        ShowToast.show("任务中断，结束点赞任务！", (Activity) context);
                        wxUtils.adb("input keyevent 4");
                        continue;
                    }

                    if (xmlLikeData.contains("取消")) {
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//打开点赞
                        continue;
                    }
                    List<String> nodeLikeList = wxUtils.getNodeList(xmlLikeData);
                    for (int b = 0; b < nodeLikeList.size(); b++) {
                        NodeXmlBean.NodeBean nodeLikeBean = wxUtils.getNodeXmlBean(nodeLikeList.get(b)).getNode();
                        if ("com.tencent.mm:id/dah".equals(nodeLikeBean.getResourceid())) {
                            listXY = wxUtils.getXY(nodeLikeBean.getBounds());//
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//打开点赞
                            count++;
                            if (count >= likeNum) {
                                break w;
                            }
                            //设置间隔时间
                            int start;
                            if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getDz_interval_s())) {
                                start = 10;
                            } else {
                                start = Integer.valueOf(app.getWxGeneralSettingsBean().getDz_interval_s());
                            }
                            int end;
                            if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getDz_interval_e())) {
                                end = 180;
                            } else {
                                end = Integer.valueOf(app.getWxGeneralSettingsBean().getDz_interval_e());
                            }
                            LogUtils.e("end=" + end + "__start=" + start);
                            int timeSleep = new Random().nextInt(end - start + 1) + start;
                            LogUtils.e("end=" + end + "__start=" + start + "___任务间隔随机数=" + timeSleep);
                            ShowToast.show("点赞间隔时间：" + timeSleep + "秒", (Activity) context);
                            try {
                                Thread.sleep(timeSleep * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            xmlData = wxUtils.getXmlData();
                            continue w;
                        }
                    }


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
     * 朋友圈发布内容
     *
     * @param type 0文字    1图文     2视频
     */
    private void FriendsRing(int type, String args) {//args0 文字   1图片链接或者视频链接

       /* String path = "";
        String strMark = "";
        String fileName = "";
        String filePath = "";
        String text = "";*/
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


            /*path = URLS.pic_vo + text.replace("\\", "/");
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
                }

                if (f == null) {
                    return;
                }
            }
            fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
            FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false);//改名把文件添加到第一个
            wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), context);*/

        }

        boolean mark = true;
        int x = context.getResources().getDimensionPixelSize(R.dimen.x282);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y33);//朋友圈发文字坐标

        if (type == 1 || type == 2) {//1.图文 2.视频
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y209, R.dimen.x280, R.dimen.y243);//从相册选择
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据

            if (xmlData.contains("你拍的照片，你的微信朋友可以看到并且评论。")) {
                wxUtils.adbDimensClick(context, R.dimen.x184, R.dimen.y269, R.dimen.x264, R.dimen.y289);//我知道了
                //                wxUtils.adbClick(276, 573, 396, 618);//我知道了
            }

            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据

            if (xmlData.contains("你拍的照片，你的微信朋友可以看到并且评论。")) {
                wxUtils.adb("input keyevent 4");

                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y209, R.dimen.x280, R.dimen.y243);//从相册选择

            }

        }
        int a = 0;
        switch (type) {
            case 0://文字
                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //点击发布
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("长按相机按钮发文字，为内部体验功能。后续版本可能取消，也有可能保留，请勿过于依赖此方法。")) {
                    wxUtils.adbDimensClick(context, R.dimen.x100, R.dimen.y338, R.dimen.x220, R.dimen.y372);//我知道了
                }

                break;

            case 1://朋友圈图文

                //                wxUtils.adbDimensClick(context, R.dimen.x48, R.dimen.y87, R.dimen.x48, R.dimen.y87);//选择图片
                if (downList != null && downList.size() > 0 && downList.size() <= 9) {
                    LogUtils.d(downList.size() + "张下载成功___________" + imgList.length);
                    a = 0;
                    while (a < 5) {
                        a++;
                        xmlData = wxUtils.getXmlData();
                        if (xmlData.contains("图片和视频")) {
                            wxUtils.adbClick(24, 801, 144, 834);//点击 左下角 的图片和视频
                            boolean ccc = true;
                            while (ccc) {
                                String xmlData_picture = wxUtils.getXmlData();
                                List<String> pictureList = wxUtils.getNodeList(xmlData_picture);
                                for (int c = 0; c < pictureList.size(); c++) {
                                    NodeXmlBean.NodeBean pictureBean = wxUtils.getNodeXmlBean(pictureList.get(c)).getNode();
                                    if (pictureBean != null && pictureBean.getResourceid() != null && "com.tencent.mm:id/d1r".equals(pictureBean.getResourceid())
                                            && pictureBean.getText() != null && pictureBean.getText().equals("ykimages")) {
                                        listXY = wxUtils.getXY(pictureBean.getBounds());//获取坐标
                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击ykimages 文件夹
                                        ccc = false;
                                        break;
                                    }
                                }
                                if (ccc == true) {
                                    wxUtils.adbUpSlide(context);//向上滑动
                                }
                            }
                            switch (downList.size()) {
                                case 1:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1         TODO 老廖

                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    //                                    NodeUtils.clickIndex("com.tencent.mm:id/a9n", 0);

                                    break;
                                case 2:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                    //                                    wxUtils.adbClick(280, 119, 310, 149);//2

                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                    //                                    NodeUtils.clickIndex("com.tencent.mm:id/a9n", 0);
                                    //                                    NodeUtils.clickIndex("com.tencent.mm:id/a9n", 1);

                                    break;
                                case 3:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                    //                                    wxUtils.adbClick(280, 119, 310, 149);//2
                                    //                                    wxUtils.adbClick(441, 119, 471, 149);//3
                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                    wxUtils.adbClick(318, 119, 348, 149);//选择图片

                                    break;
                                case 4:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                    //                                    wxUtils.adbClick(280, 119, 310, 149);//2
                                    //                                    wxUtils.adbClick(441, 119, 471, 149);//3
                                    //                                    wxUtils.adbClick(119, 280, 149, 310);//4

                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                    wxUtils.adbClick(318, 119, 348, 149);//选择图片
                                    wxUtils.adbClick(438, 119, 468, 149);//选择图片
                                    break;
                                case 5:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                    //                                    wxUtils.adbClick(280, 119, 310, 149);//2
                                    //                                    wxUtils.adbClick(441, 119, 471, 149);//3
                                    //                                    wxUtils.adbClick(119, 280, 149, 310);//4
                                    //                                    wxUtils.adbClick(280, 280, 310, 310);//5

                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                    wxUtils.adbClick(318, 119, 348, 149);//选择图片
                                    wxUtils.adbClick(438, 119, 468, 149);//选择图片
                                    wxUtils.adbClick(78, 239, 108, 269);//选择图片
                                    break;
                                case 6:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                    //                                    wxUtils.adbClick(280, 119, 310, 149);//2
                                    //                                    wxUtils.adbClick(441, 119, 471, 149);//3
                                    //                                    wxUtils.adbClick(119, 280, 149, 310);//4
                                    //                                    wxUtils.adbClick(280, 280, 310, 310);//5
                                    //                                    wxUtils.adbClick(441, 280, 471, 310);//6


                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                    wxUtils.adbClick(318, 119, 348, 149);//选择图片
                                    wxUtils.adbClick(438, 119, 468, 149);//选择图片
                                    wxUtils.adbClick(78, 239, 108, 269);//选择图片
                                    wxUtils.adbClick(198, 239, 228, 269);//选择图片
                                    break;
                                case 7:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                    //                                    wxUtils.adbClick(280, 119, 310, 149);//2
                                    //                                    wxUtils.adbClick(441, 119, 471, 149);//3
                                    //                                    wxUtils.adbClick(119, 280, 149, 310);//4
                                    //                                    wxUtils.adbClick(280, 280, 310, 310);//5
                                    //                                    wxUtils.adbClick(441, 280, 471, 310);//6
                                    //                                    wxUtils.adbClick(119, 441, 149, 471);//7

                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                    wxUtils.adbClick(318, 119, 348, 149);//选择图片
                                    wxUtils.adbClick(438, 119, 468, 149);//选择图片
                                    wxUtils.adbClick(78, 239, 108, 269);//选择图片
                                    wxUtils.adbClick(198, 239, 228, 269);//选择图片

                                    wxUtils.adbClick(318, 239, 348, 269);//选择图片
                                    break;
                                case 8:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                    //                                    wxUtils.adbClick(280, 119, 310, 149);//2
                                    //                                    wxUtils.adbClick(441, 119, 471, 149);//3
                                    //                                    wxUtils.adbClick(119, 280, 149, 310);//4
                                    //                                    wxUtils.adbClick(280, 280, 310, 310);//5
                                    //                                    wxUtils.adbClick(441, 280, 471, 310);//6
                                    //                                    wxUtils.adbClick(119, 441, 149, 471);//7
                                    //                                    wxUtils.adbClick(280, 441, 310, 471);//8

                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                    wxUtils.adbClick(318, 119, 348, 149);//选择图片
                                    wxUtils.adbClick(438, 119, 468, 149);//选择图片
                                    wxUtils.adbClick(78, 239, 108, 269);//选择图片
                                    wxUtils.adbClick(198, 239, 228, 269);//选择图片

                                    wxUtils.adbClick(318, 239, 348, 269);//选择图片
                                    wxUtils.adbClick(438, 239, 468, 269);//选择图片
                                    break;
                                case 9:
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                    //                                    wxUtils.adbClick(280, 119, 310, 149);//2
                                    //                                    wxUtils.adbClick(441, 119, 471, 149);//3
                                    //                                    wxUtils.adbClick(119, 280, 149, 310);//4
                                    //                                    wxUtils.adbClick(280, 280, 310, 310);//5
                                    //                                    wxUtils.adbClick(441, 280, 471, 310);//6
                                    //                                    wxUtils.adbClick(119, 441, 149, 471);//7
                                    //                                    wxUtils.adbClick(280, 441, 310, 471);//8
                                    //                                    wxUtils.adbClick(441, 441, 471, 471);//9

                                    wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                    wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                    wxUtils.adbClick(318, 119, 348, 149);//选择图片
                                    wxUtils.adbClick(438, 119, 468, 149);//选择图片
                                    wxUtils.adbClick(78, 239, 108, 269);//选择图片
                                    wxUtils.adbClick(198, 239, 228, 269);//选择图片

                                    wxUtils.adbClick(318, 239, 348, 269);//选择图片
                                    wxUtils.adbClick(438, 239, 468, 269);//选择图片
                                    wxUtils.adbClick(78, 359, 108, 389);//选择图片
                                    break;
                            }
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
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
                //                wxUtils.adbDimensClick(context, R.dimen.x48, R.dimen.y87, R.dimen.x48, R.dimen.y87);//选择图片
                a = 0;
                while (a < 5) {
                    a++;
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("图片和视频")) {
                        //                        wxUtils.adbClick(119, 119, 149, 149);   TODO copy 修改
                        wxUtils.adbClick(78, 119, 108, 149);//选择图片;
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                        try {
                            Thread.sleep(7000);
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
                while (true) {
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("这一刻的想法...")) {
                        break;
                    }
                }
                // 将文本内容放到系统剪贴板里。
                ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                cm.setText(wxUtils.getFaceText(args));

                x = context.getResources().getDimensionPixelSize(R.dimen.x160);
                y = context.getResources().getDimensionPixelSize(R.dimen.y74);//EdiText
                //            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y65, R.dimen.x320, R.dimen.y124);//点击EdiText
                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                wxUtils.adbDimensClick(context, R.dimen.x48, R.dimen.y122, R.dimen.x48, R.dimen.y124);//粘贴
            } else {
                while (true) {
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("这一刻的想法...")) {
                        break;
                    }
                }
                // 将文本内容放到系统剪贴板里。
                ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                cm.setText(args_comment);

                x = context.getResources().getDimensionPixelSize(R.dimen.x160);
                y = context.getResources().getDimensionPixelSize(R.dimen.y74);//EdiText
                //            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y65, R.dimen.x320, R.dimen.y124);//点击EdiText
                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                wxUtils.adbDimensClick(context, R.dimen.x48, R.dimen.y77, R.dimen.x48, R.dimen.y124);//粘贴

            }
            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//发送
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (type == 1) {
            wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
            wxUtils.adbClick(350, 520, 400, 550);//点击进入相册
            //                wxUtils.adbDimensClick(context,R.dimen.x224,R.dimen.y155,R.dimen.x308,R.dimen.y262);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.adbClick(134, 840, 347, 854);//进入发布的图文
            wxUtils.adbDimensClick(context, R.dimen.x224, R.dimen.y155, R.dimen.x308, R.dimen.y262);
            for (int i = 0; i < 5; i++) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("评论")) {
                    wxUtils.adbClick(160, 800, 180, 820);//坐下角点击评论
                    break;
                }
                wxUtils.adb("input keyevent 4");
                wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adbClick(134, 840, 347, 854);//进入发布的图文
            }
            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            for (int j = 0; j < materia_ssList5.length; j++) {
                if (j == 0) {
                    cm.setText(materia_ssList5[j]);
                }
                if (j == 1) {
                    cm.setText("/:v" + materia_ssList5[j]);
                }
                if (j == 2) {
                    cm.setText("/::B" + materia_ssList5[j]);
                }
                if (j == 3) {
                    cm.setText("/:share" + materia_ssList5[j]);
                }
                if (j == 4) {
                    cm.setText("/:rose" + materia_ssList5[j]);
                }
                if (j == 5) {
                    cm.setText("/:ok" + materia_ssList5[j]);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adb("input swipe " + 40 + " " + 150 + " " + 50 + " " + 150 + " " + 2000);  //长按EdiText
                wxUtils.adbClick(40, 125, 50, 125);
                if (materia_ssList5.length > 1) {
                    //                    wxUtils.adb("input keyevent 66");
                    wxUtils.adbClick(440, 830, 460, 840);
                }
            }

            wxUtils.adbClick(400, 60, 450, 80);//发送
            ShowToast.show("朋友圈评论结束", (Activity) context);

        }


        if (type == 1 || type == 2) {

            //            wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName), context);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int b = 0; b < downList.size(); b++) {
                String strMark = downList.get(b).replace("\\", "/");
                String fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");

                FileUtils.delete(fileUrl + "/aa" + fileName);//删除复制的文件
            }

        }

    }

    public void setApp(BaseApplication app) {
        this.app = app;
    }


    public void unReceiver() {
        context.unregisterReceiver(m_client);
        context.unregisterReceiver(m_client1);
    }

    //-----------------------------------好友发消息和群发消息-----------------------------------------------------------------------------------------

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
     * 给微信群发消息
     * 模拟点击发消息  发多组图文需求  （做废）
     *
     * @param messageData
     */
    public void sendFlockMessageClick(String messageData, boolean isAll) {
        String uid = SPUtils.getString(context, "uid", "0000");
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);//
        String qunClickMark = "";//进过的群
        boolean isOneSlide = false;
        int count = 0;
        List<String> imgList = new ArrayList<>();
        String fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
        WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);//aaaa 啊啊aa

        if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans != null && wxFlockMessageBeans.length > 0) {

        } else {
            ShowToast.show("数据有误", (Activity) context);
            return;
        }

        //进入群聊
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {//
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if ("群聊".equals(nodeBean.getText())) {//获取群聊node节点
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群聊坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击群聊
                break;
            }
        }

        //进入了群列表
        w:
        while (true) {
            while (true) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                    wxUtils.adb("input keyevent 4");
                    break w;
                }

                if ((xmlData.contains("应用") && xmlData.contains("主屏幕")) || xmlData.contains("wx助手")) {
                    break w;
                } else if (!(xmlData.contains("新群聊") && xmlData.contains("返回"))) {
                    backHome();
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//群聊
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("新群聊") && xmlData.contains("返回"))) {
                        continue w;
                    } else {
                        break;
                    }

                } else {
                    break;
                }
            }


            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().contains("com.tencent.mm:id/a1")) {
                    if (!isAll) {//给
                        if (nodeBean.getText().length() < 7) {
                            continue;
                        }
                        if (!(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B"))) {
                            continue;
                        }
                       /* if (!nodeBean.getText().contains(uid)) {//只给自己的群发消息  //TODO  发单要注释
                            continue;
                        }*/
                    }


                    if (qunClickMark.contains(nodeBean.getText())) {
                        continue;
                    } else {
                        if (!isOneSlide) {
                            for (int b = 0; b < count; b++) {
                                wxUtils.adbUpSlide(context);//向上滑动
                            }
                        }
                        isOneSlide = false;
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        qunClickMark = qunClickMark + nodeBean.getText() + ",";
                    }

                    //_______________________________________________________________________________________________
                    String qunName = "";
                    //获取群人数，男女群信息
                    String qunNameData = wxUtils.getXmlData();
                    List<String> qunNameDataList = wxUtils.getNodeList(xmlData);

                    if (!(qunNameData.contains("当前所在页面,与"))) {
                        //                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                        continue w;
                    }

                    for (int c = 0; c < qunNameDataList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
                        if ("com.tencent.mm:id/gh".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }

                    //操作群
                    LogUtils.e("发送消息");
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("切换到键盘")) {
                        wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                        wxUtils.adb("input keyevent 4");
                    }

                    for (int b = 0; b < wxFlockMessageBeans.length; b++) {
                        switch (wxFlockMessageBeans[b].getType()) {
                            case "txt":

                                // 将文本内容放到系统剪贴板里。
                                String[] cms = wxFlockMessageBeans[b].getData().split("==");
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int c = 0; c < cms.length; c++) {
                                    if (c != cms.length - 1) {
                                        stringBuilder.append(cms[c] + "\n");
                                    } else {
                                        stringBuilder.append(cms[c]);
                                    }
                                }
                                cm.setText(stringBuilder.toString());
                                //                                cm.setText(wxFlockMessageBeans[b].getData());
                                int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                                int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送

                                int ran = random.nextInt(5) + 5;

                                try {
                                    if (b != wxFlockMessageBeans.length - 1) {
                                        ShowToast.show("间隔" + ran + "秒", (Activity) context);
                                        Thread.sleep(ran * 1000);
                                    }

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

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
                                    //                                    wxUtils.adbClick(119, 119, 149, 149);   TODO copy
                                    wxUtils.adbDimensClick(context, R.dimen.x79, R.dimen.y56, R.dimen.x99, R.dimen.y69);//选择图片
                                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                    try {
                                        if (b != wxFlockMessageBeans.length - 1) {
                                            ShowToast.show("间隔5秒", (Activity) context);
                                            Thread.sleep(5000);
                                        } else {
                                            Thread.sleep(500);
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }

                                break;
                        }

                    }
                    //-------------------------------------------------------------------------------
            /*        int wCount = 0;
                    switch (type) {
                        case 0://发消息
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(wxUtils.getFaceText(messageData));
                            int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                            int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                            break;
                        case 1://发图片
                            wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                            wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册
                            wCount = 0;
                            while (wCount < 5) {
                                wCount++;
                                xmlData = wxUtils.getXmlData();
                                if (xmlData.contains("图片和视频")) {
                                    wxUtils.adbClick(119, 119, 149, 149);
                                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }

                            break;
                        case 2://发视频
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
                                wxUtils.adbClick(119, 119, 149, 149);
                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            break;
                    }*/
                    //-------------------------------------------------------------------------------
                    xmlData = wxUtils.getXmlData();
                    if (!xmlData.contains("当前所在页面,与")) {
                        continue w;
                    }

                    //返回
                    wxUtils.adb("input keyevent 4");
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊

                    //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getCrowd_ad_time_s())) {
                        start = 5;
                    } else {
                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getCrowd_ad_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getCrowd_ad_time_e())) {
                        end = 15;
                    } else {
                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getCrowd_ad_time_e());
                    }
                    LogUtils.d(end + "___" + start + "_____________________________");
                    int timeSleep = random.nextInt(end - start + 1) + start;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            count++;
            isOneSlide = true;
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData) && xmlData.contains("个群聊")) {
                wxUtils.adb("input keyevent 4");
                ShowToast.show("群消息发送完成", (Activity) context);
                break w;
            }


        }

       /* try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int a = 0; a < imgList.size(); a++) {
            FileUtils.delete(fileUrl + "/aa" + imgList.get(a));//删除复制的文件
        }*/

        //        ShowToast.show("群消息发送完毕", (Activity) context);

    }


    /**
     * 给微信群发消息
     * 模拟点击发消息  发多组图文需求2
     *
     * @param messageData
     */
    public void sendFlockMessageClickTo(String messageData, boolean isAll) {
        String uid = SPUtils.getString(context, "uid", "0000");
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);//
        String qunClickMark = "";//进过的群
        int count = 0;
        List<String> imgList = new ArrayList<>();
        String fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
        WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);//aaaa 啊啊aa

        if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans != null && wxFlockMessageBeans.length > 0) {

        } else {
            ShowToast.show("数据有误", (Activity) context);
            return;
        }

        //进入群聊
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {//
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if ("群聊".equals(nodeBean.getText())) {//获取群聊node节点
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群聊坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击群聊
                break;
            }
        }

        //进入了群列表
        w:
        while (true) {


            while (true) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                    wxUtils.adb("input keyevent 4");
                    break w;
                }

                if ((xmlData.contains("应用") && xmlData.contains("主屏幕")) || xmlData.contains("wx助手")) {
                    break w;
                } else if (!(xmlData.contains("新群聊") && xmlData.contains("返回"))) {
                    backHome();
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y88, R.dimen.x296, R.dimen.y123);//群聊
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("新群聊") && xmlData.contains("返回"))) {
                        continue w;
                    } else {
                        break;
                    }

                } else {
                    break;
                }
            }

            for (int b = 0; b < count; b++) {
                LogUtils.d("滑动第" + count + "下");
                wxUtils.adbUpSlide(context);//向上滑动
            }


            wh:
            while (true) {
                xmlData = wxUtils.getXmlData();
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getText() != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().contains("com.tencent.mm:id/a1")) {
                        if (!isAll) {//给
                            if (nodeBean.getText().length() < 7) {
                                continue;
                            }
                            if (!(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B"))) {
                                continue;
                            }
                            if (!nodeBean.getText().contains(uid)) {//只给自己的群发消息  //TODO  发单要注释
                                continue;
                            }
                        }

                        if (qunClickMark.contains(nodeBean.getText())) {
                            continue;
                        } else {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                            qunClickMark = qunClickMark + nodeBean.getText() + ",";
                            break wh;
                        }
                    }
                }

                String strXmlData = xmlData;
                wxUtils.adbUpSlide(context);//向上滑动
                count++;
                xmlData = wxUtils.getXmlData();
                if (xmlData.equals(strXmlData) && xmlData.contains("个群聊")) {
                    wxUtils.adb("input keyevent 4");
                    ShowToast.show("群消息发送完成", (Activity) context);
                    return;
                }
            }

            //_______________________________________________________________________________________________
            String qunName = "";
            //获取群人数，男女群信息
            String qunNameData = wxUtils.getXmlData();
            List<String> qunNameDataList = wxUtils.getNodeList(xmlData);

            if (!(qunNameData.contains("当前所在页面,与"))) {
                //                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                continue w;
            }

            for (int c = 0; c < qunNameDataList.size(); c++) {
                NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
                if ("com.tencent.mm:id/gh".equals(qunNameBean.getResourceid())) {
                    qunName = qunNameBean.getText();
                    LogUtils.d(qunName + "qunName");
                    break;
                }
            }

            //操作群
            LogUtils.e("发送消息");
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("切换到键盘")) {
                wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                wxUtils.adb("input keyevent 4");
            }

            for (int b = 0; b < wxFlockMessageBeans.length; b++) {
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("当前所在页面,与")) {//一遍判断不出来
                    xmlData = wxUtils.getXmlData();
                    if (!xmlData.contains("当前所在页面,与")) {
                        continue w;
                    }
                }
                switch (wxFlockMessageBeans[b].getType()) {
                    case "txt":

                        // 将文本内容放到系统剪贴板里。
                        String[] cms = wxFlockMessageBeans[b].getData().split("==");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int c = 0; c < cms.length; c++) {
                            if (c != cms.length - 1) {
                                stringBuilder.append(cms[c] + "\n");
                            } else {
                                stringBuilder.append(cms[c]);
                            }
                        }
                        cm.setText(stringBuilder.toString());
                        //                                cm.setText(wxFlockMessageBeans[b].getData());
                        int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                        int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                        wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                        wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送

                        int ran = random.nextInt(5) + 5;

                        try {
                            if (b != wxFlockMessageBeans.length - 1) {
                                ShowToast.show("间隔" + ran + "秒", (Activity) context);
                                Thread.sleep(ran * 1000);
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

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
                            //                            wxUtils.adbClick(119, 119, 149, 149);         TODO copy

                            wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y56, R.dimen.x99, R.dimen.y69);//确定
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                            try {
                                if (b != wxFlockMessageBeans.length - 1) {
                                    ShowToast.show("间隔5秒", (Activity) context);
                                    Thread.sleep(5000);
                                } else {
                                    Thread.sleep(500);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        break;
                }

            }
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains("当前所在页面,与")) {
                continue w;
            }

            //返回
            wxUtils.adb("input keyevent 4");
            xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                continue w;
            }
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯
            xmlData = wxUtils.getXmlData();
            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊

            //设置间隔时间
            int start;
            if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getCrowd_ad_time_s())) {
                start = 5;
            } else {
                start = Integer.valueOf(app.getWxGeneralSettingsBean().getCrowd_ad_time_s());
            }
            int end;
            if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getCrowd_ad_time_e())) {
                end = 15;
            } else {
                end = Integer.valueOf(app.getWxGeneralSettingsBean().getCrowd_ad_time_e());
            }
            LogUtils.d(end + "___" + start + "_____________________________");
            int timeSleep = random.nextInt(end - start + 1) + start;
            LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
            ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
            try {
                Thread.sleep(timeSleep * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }


    private void xiuGaiBeiZhu_erWeiMa(String arg0, String arg1, String groupType) {
        // 例子 由ZZZ1改成 ZZZ2
        if (arg0.length() == 4) {
            String str_Last = "";
            if (arg1.length() >= 4) {
                str_Last = arg1.substring(3, 4);
            }
            String meName = "";
            int sex = 0;//0代表女。   1代表男   2代表性别未知
            DecimalFormat df = new DecimalFormat("000");
            String Type = "0000";
            boolean bottom = false;//到了底部
            SPUtils.putString(context, "OldFansNameLastfourth", "");//倒数第4位
            String newName = "";//修改完备注的名字
            String newStr = "";//获取到修改备注后的后四位
            SPUtils.putString(context, "StrLastFourth", "");//最后四位
            boolean flag0 = true;
            while (flag0) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                    flag0 = false;
                } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adb("input keyevent 4");//返回
                } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                    wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                    flag0 = false;
                }
            }
            wxUtils.adbClick(306, 36, 378, 108);//搜索
            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            cm.setText(arg0);
            wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
            wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.adb("input keyevent 4");//返回
            xmlData = wxUtils.getXmlData();
            if (!xmlData.contains(arg0)) {
                ShowToast.show("没有对应的老粉丝账号", (Activity) context);
                return;
            }
            xmlData = wxUtils.getXmlData();
            int aa = 0;
            if (xmlData.contains("更多联系人")) {
                aa = 1;
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                            && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                            ) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                        break;
                    }
                }
            } else {
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("更多联系人")) {
                    nodeList = wxUtils.getNodeList(xmlData);
                    for (int a = 0; a < nodeList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                                && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                                ) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                            break;
                        }
                    }
                } else {
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                }
            }
            Boolean Flag = true;
            while (Flag) {
                xmlData = wxUtils.getXmlData();
                nodeList = wxUtils.getNodeList(xmlData);
                int bb = 0;
                if (xmlData.contains(arg0)) {
                    for (int a = 0; a < nodeList.size(); a++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                        if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/kq")
                                && nodeBean.getText() != null && nodeBean.getText().startsWith(arg0) && !meName.contains(nodeBean.getText())
                                ) {
                            String oldName = "";
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取  的坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                            bb = 1;
                            meName = meName + nodeBean.getText();
                            oldName = nodeBean.getText();
                            wxUtils.adbClick(396, 36, 480, 108);//点击右上角头像
                            xmlData = wxUtils.getXmlData();
                            if (!xmlData.contains("添加成员")) {
                                superXiuGaiBeiZhu(arg0, arg1);
                                return;
                            }
                            wxUtils.adbClick(21, 168, 105, 286); //点击左上角的人头像
                            xmlData = wxUtils.getXmlData();//重新获取页面数据
                            List<String> remarkList = wxUtils.getNodeList(xmlData);
                            if (xmlData.contains("女")) {
                                sex = 0;
                            } else if (xmlData.contains("男")) {
                                sex = 1;
                            } else {
                                sex = 2;
                            }
                            for (int r = 0; r < remarkList.size(); r++) {
                                nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                    //筛选出好友
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                    break;
                                }
                            }
                            wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                            switch (sex) { //0代表女。   1代表男   2代表性别未知
                                case 0:
                                    int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl" + str_Last, 0);
                                    String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                    wxUtils.adb("input text " + arg1 + Type + "B" + wx_nume_number_new_girl);
                                    SPUtils.put(context, "wx_name_number_girl" + str_Last, wx_name_number_girl + 1);
                                    break;
                                case 1:
                                    int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy" + str_Last, 0);
                                    String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                    wxUtils.adb("input text " + arg1 + Type + "A" + wx_nume_number_new_boy);
                                    SPUtils.put(context, "wx_name_number_boy" + str_Last, wx_name_number_boy + 1);
                                    break;
                                case 2:
                                    int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c" + str_Last, 0);
                                    String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                    wxUtils.adb("input text " + arg1 + Type + "C" + wx_nume_number_c);
                                    SPUtils.put(context, "wx_name_number_c" + str_Last, wx_name_number_c + 1);
                                    break;
                            }
                            try {
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                            wxUtils.adb("input keyevent 4");//返回
                            wxUtils.adb("input keyevent 4");//返回
                            wxUtils.adb("input keyevent 4");//返回
                            try {
                                Thread.sleep(2 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (bb == 0) {
                    String oldXml = xmlData;
                    wxUtils.adbUpSlide(context);
                    xmlData = wxUtils.getXmlData();
                    if (oldXml.equals(xmlData)) {
                        ShowToast.show("滑到底部了", (Activity) context);
                        Flag = false;
                        continue;
                    }
                }

            }
        }


    }

    private void startAlterName_ZiDingYiErWeiMaLaQun(String zzz, String str2) {
        if (str2.equals("")) {
            // 修改全部粉丝备注
            boolean bottom = false;//到了底部
            int sex = 0;//0代表女。   1代表男   2代表性别未知
            DecimalFormat df = new DecimalFormat("0000");
            int zzzNum = 0;//判断是否直接到#号修改
            String endData = "";
            String meName = "";
            int newFriendsCount = 0;
            w:
            while (true) {
                while (true) {
                    xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                    if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                        ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                        break w;
                    } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                        wxUtils.adb("input keyevent 4");//返回
                    } else {
                        break;
                    }
                }
                String oldXmlData = "";

                List<String> nodeList = wxUtils.getNodeList(xmlData);
                a:
                for (int a = 0; a < nodeList.size(); a++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz")
                            && !nodeBean.getContentdesc().startsWith("YYY")
                            && !meName.equals(nodeBean.getContentdesc())) {
                        //筛选出好友
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注

                        LogUtils.d("点击进入");
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        if (!xmlData.contains("标签")) {
                            wxUtils.adb("input keyevent 4");
                            meName = nodeBean.getContentdesc();
                            continue;
                        }
                        List<String> meWxIdList = wxUtils.getNodeList(xmlData);
                        if (xmlData.contains("女")) {
                            sex = 0;
                        } else if (xmlData.contains("男")) {
                            sex = 1;
                        } else {
                            sex = 2;
                        }
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        List<String> remarkList = wxUtils.getNodeList(xmlData);
                        for (int r = 0; r < remarkList.size(); r++) {
                            nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                            if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                //筛选出好友
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                newFriendsCount++;
                                SPUtils.putInt(context, "NewFriendsCount", newFriendsCount);
                                break;
                            }
                        }
                        xmlData = wxUtils.getXmlData();

                        if (xmlData.contains("备注信息") && xmlData.contains("完成")) {

                        } else {
                            continue w;
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                        switch (sex) {//0代表女。   1代表男   2代表性别未知
                            case 0:
                                int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl", 0);
                                String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                wxUtils.adb("input text " + zzz + "B" + wx_nume_number_new_girl);
                                SPUtils.put(context, "wx_name_number_girl", wx_name_number_girl + 1);
                                break;
                            case 1:
                                int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy", 0);
                                String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                wxUtils.adb("input text " + zzz + "A" + wx_nume_number_new_boy);
                                SPUtils.put(context, "wx_name_number_boy", wx_name_number_boy + 1);
                                break;
                            case 2:
                                int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c", 0);
                                String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                wxUtils.adb("input text " + zzz + "C" + wx_nume_number_c);
                                SPUtils.put(context, "wx_name_number_c", wx_name_number_c + 1);
                                break;
                        }
                        try {
                            Thread.sleep(5 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                        //  LogUtils.d(nodeList.get(a));
                        wxUtils.adb("input keyevent 4");
                        xmlData = wxUtils.getXmlData();
                        if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                            wxUtils.adb("input keyevent 4");
                        }
                        break;
                    }
                }
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                nodeList = wxUtils.getNodeList(xmlData);
                if (!xmlData.contains("发现")) {
                    ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                    continue w;
                }
                zzzNum = 0;
                for (int b = 0; b < nodeList.size(); b++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                    if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                            && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("YYY")
                            && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())) {
                        continue w;
                    } else if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                            && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && (nodeBean.getContentdesc().startsWith("YYY")
                            || nodeBean.getContentdesc().startsWith("ZZZ") || nodeBean.getContentdesc().startsWith("zzz")) && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())
                            ) {
                        zzzNum++;
                    }
                }
                int aaaaa = 0;
                if (!bottom) {
                    if (zzzNum >= 8) {
                        //                    wxUtils.adbDimensClick(context, 460, 768,460, 768);
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                        String xmlData2 = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData2);
                        for (int b = 0; b < nodeList.size(); b++) {
                            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                            if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                    && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                                String xmlData3 = wxUtils.getXmlData();
                                //                            meName =meName+nodeBean.getContentdesc();
                                if (xmlData3.contains("备注和标签")) {
                                    aaaaa++;
                                } else {
                                    meName = meName + nodeBean.getContentdesc();
                                }
                                wxUtils.adb("input keyevent 4");//返回
                            }
                        }
                        if (aaaaa == 0) {
                            //说明全是ZZZ或者zzz开头的了
                            wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                            wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                            xmlData = wxUtils.getXmlData();
                            nodeList = wxUtils.getNodeList(xmlData);
                            int ccc = 0;
                            for (int b = 0; b < nodeList.size(); b++) {
                                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                        && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY0") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                                    ccc++;
                                }
                            }
                            if (ccc == 0) {
                                ShowToast.show("修改备注完成", (Activity) context);
                                break w;
                            }
                        }
                    } else {
                        oldXmlData = wxUtils.getXmlData();
                        wxUtils.adbUpSlide(context);//向上滑动
                    }
                    xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
                    if (oldXmlData.equals(xmlData)) {
                        ShowToast.show("修改备注完成", (Activity) context);
                        break w;
                    }
                    nodeList = wxUtils.getNodeList(xmlData);
                    int bbb = 0;
                    for (int b = 0; b < nodeList.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                        if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                            bbb++;
                        }
                    }
                    if (bbb == 0) {
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                    }
                }
            }


        } else {
            // 修改几个全部备注
            boolean bottom = false;//到了底部
            int sex = 0;//0代表女。   1代表男   2代表性别未知
            DecimalFormat df = new DecimalFormat("0000");
            int zzzNum = 0;  //判断是否直接到#号修改
            String endData = "";
            String meName = "";
            int kkk = 0;
            int newFriendsCount = 0;
            w:
            while (true) {
                while (true) {
                    xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                    if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                        ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                        break w;
                    } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                        wxUtils.adb("input keyevent 4");//返回
                    } else {
                        break;
                    }
                }
                String oldXmlData = "";

                List<String> nodeList = wxUtils.getNodeList(xmlData);
                a:
                for (int a = 0; a < nodeList.size(); a++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz")
                            && !nodeBean.getContentdesc().startsWith("YYY")
                            && !meName.equals(nodeBean.getContentdesc())) {
                        //筛选出好友
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注

                        LogUtils.d("点击进入");
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        if (!xmlData.contains("标签")) {
                            wxUtils.adb("input keyevent 4");
                            meName = nodeBean.getContentdesc();
                            continue;
                        }
                        List<String> meWxIdList = wxUtils.getNodeList(xmlData);
                        if (xmlData.contains("女")) {
                            sex = 0;
                        } else if (xmlData.contains("男")) {
                            sex = 1;
                        } else {
                            sex = 2;
                        }
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        List<String> remarkList = wxUtils.getNodeList(xmlData);
                        for (int r = 0; r < remarkList.size(); r++) {
                            nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
                            if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                                //筛选出好友
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                newFriendsCount++;
                                SPUtils.putInt(context, "NewFriendsCount", newFriendsCount);
                                break;
                            }
                        }
                        xmlData = wxUtils.getXmlData();

                        if (xmlData.contains("备注信息") && xmlData.contains("完成")) {
                        } else {
                            continue w;
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                        switch (sex) {//0代表女。   1代表男   2代表性别未知
                            case 0:
                                int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl", 0);
                                String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                wxUtils.adb("input text " + zzz + "B" + wx_nume_number_new_girl);
                                SPUtils.put(context, "wx_name_number_girl", wx_name_number_girl + 1);
                                break;
                            case 1:
                                int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy", 0);
                                String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                wxUtils.adb("input text " + zzz + "A" + wx_nume_number_new_boy);
                                SPUtils.put(context, "wx_name_number_boy", wx_name_number_boy + 1);
                                break;
                            case 2:
                                int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c", 0);
                                String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                wxUtils.adb("input text " + zzz + "C" + wx_nume_number_c);
                                SPUtils.put(context, "wx_name_number_c", wx_name_number_c + 1);
                                break;
                        }
                        try {
                            Thread.sleep(5 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                        kkk++;
                        //  LogUtils.d(nodeList.get(a));
                        wxUtils.adb("input keyevent 4");
                        xmlData = wxUtils.getXmlData();
                        if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                            wxUtils.adb("input keyevent 4");
                        }
                        break;
                    }
                }
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                nodeList = wxUtils.getNodeList(xmlData);
                if (kkk >= Integer.valueOf(str2)) {
                    ShowToast.show("修改备注完成", (Activity) context);
                    break w;
                }
                if (!xmlData.contains("发现")) {
                    ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                    continue w;
                }
                zzzNum = 0;
                for (int b = 0; b < nodeList.size(); b++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                    if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                            && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && !nodeBean.getContentdesc().startsWith("YYY")
                            && !nodeBean.getContentdesc().startsWith("ZZZ") && !nodeBean.getContentdesc().startsWith("zzz") && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())) {
                        continue w;
                    } else if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队")
                            && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().equals("文件传输助手") && (nodeBean.getContentdesc().startsWith("YYY")
                            || nodeBean.getContentdesc().startsWith("ZZZ") || nodeBean.getContentdesc().startsWith("zzz")) && !meName.equals(nodeBean.getContentdesc()) && !yunYingMark.contains(nodeBean.getContentdesc())
                            ) {
                        zzzNum++;
                    }
                }
                int aaaaa = 0;
                if (!bottom) {
                    if (zzzNum >= 8) {
                        //                    wxUtils.adbDimensClick(context, 460, 768,460, 768);
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                        String xmlData2 = wxUtils.getXmlData();
                        nodeList = wxUtils.getNodeList(xmlData2);
                        for (int b = 0; b < nodeList.size(); b++) {
                            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                            if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                    && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                                listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入
                                String xmlData3 = wxUtils.getXmlData();
                                //                            meName =meName+nodeBean.getContentdesc();
                                if (xmlData3.contains("备注和标签")) {
                                    aaaaa++;
                                } else {
                                    meName = meName + nodeBean.getContentdesc();
                                }
                                wxUtils.adb("input keyevent 4");//返回
                            }
                        }
                        if (aaaaa == 0) {
                            //说明全是ZZZ或者zzz开头的了
                            wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                            wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                            xmlData = wxUtils.getXmlData();
                            nodeList = wxUtils.getNodeList(xmlData);
                            int ccc = 0;
                            for (int b = 0; b < nodeList.size(); b++) {
                                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                                if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                        && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY0") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                                    ccc++;
                                }
                            }
                            if (ccc == 0) {
                                ShowToast.show("修改备注完成", (Activity) context);
                                break w;
                            }
                        }
                    } else {
                        oldXmlData = wxUtils.getXmlData();
                        wxUtils.adbUpSlide(context);//向上滑动
                    }
                    xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
                    if (oldXmlData.equals(xmlData)) {
                        ShowToast.show("修改备注完成", (Activity) context);
                        break w;
                    }
                    nodeList = wxUtils.getNodeList(xmlData);
                    int bbb = 0;
                    for (int b = 0; b < nodeList.size(); b++) {
                        nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                        if (nodeBean != null && (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信团队"))
                                && !meName.equals(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("zzz") && !nodeBean.getContentdesc().startsWith("YYY") && !nodeBean.getContentdesc().startsWith("ZZZ")) {
                            bbb++;
                        }
                    }
                    if (bbb == 0) {
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                        wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                    }
                }
            }
        }
    }

    /**
     * @param type 0视频聊天 1语音聊天
     */
    public void VideoChat(int type) {

        boolean bottom = false;//到了底部
        String endData = "";
        String meName = "";
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText

        wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y346, R.dimen.x320, R.dimen.y346);//点击Z
        xmlData = wxUtils.getXmlData();//重新获取页面数据
        boolean sendMark = true;
        w:
        while (true) {
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            a:
            for (int a = 0; a < nodeList.size(); a++) {

                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && (nodeBean.getContentdesc().startsWith("ZZZ9") || nodeBean.getContentdesc().startsWith("zzz9")) && !meName.contains(nodeBean.getContentdesc())) {
                    int r = random.nextInt(3);
                    if (r != 1) {//随机视频
                        continue;
                    }
                    sendMark = false;
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        meName = meName + nodeBean.getContentdesc();
                        continue;
                    }
                    //发送标记


                    List<String> messageListMessage = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < messageListMessage.size(); b++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(messageListMessage.get(b)).getNode();
                        if ("com.tencent.mm:id/ana".equals(nodeBean.getResourceid())) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//发消息
                            cm.setText("你好现在方便吗");
                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }


                    /*xmlData = wxUtils.getXmlData();//重新获取页面数据
                    //视频聊天
                    List<String> messageList = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < messageList.size(); b++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(messageList.get(b)).getNode();
                        if ("com.tencent.mm:id/adk".equals(nodeBean.getResourceid())||(nodeBean.getText()!=null&&nodeBean.getText().equals("视频聊天"))) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//视频聊天
                            break;
                        }
                    }*/
                    wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                    wxUtils.adbDimensClick(context, R.dimen.x160, R.dimen.y235, R.dimen.x232, R.dimen.y298);//视频聊天
                    switch (type) {
                        case 0://视频聊天
                            wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y175, R.dimen.x280, R.dimen.y209);//视频聊天

                            break;
                        case 1://语音聊天
                            wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y209, R.dimen.x280, R.dimen.y243);//语音聊天
                            break;
                    }
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("在移动网络环境下会影响视频和音频质量，并产生手机流量。")) {
                        wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y243, R.dimen.x264, R.dimen.y264);//确定
                    }
                    if (true) {//视频聊天后返回
                        try {
                            Thread.sleep(1000 * 40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        LogUtils.d("等待视频结束");
                        return;
                    }

                    meName = meName + nodeBean.getContentdesc();
                    wxUtils.adb("input keyevent 4");
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录

                    //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_s())) {
                        start = 60;
                    } else {
                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_e())) {
                        end = 200;
                    } else {
                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_e());
                    }
                    int timeSleep = random.nextInt(end - start + 1) + start;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (!xmlData.contains("发现") && !xmlData.contains("com.tencent.mm:id/i")) {
                ShowToast.show("任务被中断，发消息任务", (Activity) context);
                break w;
            }


            if (!bottom) {
                LogUtils.d("向上滑动了");
                wxUtils.adbUpSlide(context);//向上滑动
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                //                ShowToast.show("发消息完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("位联系人")) {//判断是否到达底部
                bottom = true;
            }
        }

        if (sendMark && (xmlData.contains("zzz9") || xmlData.contains("ZZZ9"))) {
            VideoChat(type);
        } else {
            ShowToast.show("发消息完成", (Activity) context);
        }


    }

    List<String> zzzChatList = new ArrayList<>();

    /**
     * 双向互聊
     */
    private void chitChat() {
        zzzChatList.clear();
        backHome();
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
        wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y345, R.dimen.x320, R.dimen.y350);//点击Z

        //判断有多少zzz9用户
        xmlData = wxUtils.getXmlData();
        while (true) {
            if (!(xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                chitChat();
                return;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && !StringUtils.isEmpty(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("微信") && (nodeBean.getContentdesc().startsWith("ZZZ9") || nodeBean.getContentdesc().startsWith("zzz9"))) {
                    if (!zzzChatList.contains(nodeBean.getText())) {
                        zzzChatList.add(nodeBean.getText());
                    }
                }
            }
            String endXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            xmlData = wxUtils.getXmlData();
            if (endXmlData.equals(xmlData)) {
                break;
            }
        }

        int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
        getChatData();
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);

        if (zzzChatList.size() > 0) {
            wh:
            while (true) {
                xmlData = wxUtils.getXmlData();
                boolean first = true;
                int messageNum = random.nextInt(5) + 1;
                int messageCuont = 0;
                boolean sendTow = false;
                if (!(xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我")) || !(xmlData.contains("com.tencent.mm:id/j_"))) {
                    backHome();
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y345, R.dimen.x320, R.dimen.y350);//点击Z
                    continue;
                }
                List<String> nodeList = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < nodeList.size(); a++) {

                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && !StringUtils.isEmpty(nodeBean.getContentdesc()) && !nodeBean.getContentdesc().startsWith("微信") && (nodeBean.getContentdesc().startsWith("ZZZ9") || nodeBean.getContentdesc().startsWith("zzz9"))) {
                        int randomNum = random.nextInt(zzzChatList.size());
                        if (randomNum != 0) {
                            continue;
                        } else {
                            //筛选出好友  开始双向互聊
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友

                            xmlData = wxUtils.getXmlData();
                            if (!xmlData.contains("详细资料")) {
                                continue wh;
                            }
                            List<String> messageList = wxUtils.getNodeList(xmlData);
                            for (int b = 0; b < messageList.size(); b++) {
                                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(messageList.get(b)).getNode();
                                if ("com.tencent.mm:id/ana".equals(nodeBean.getResourceid())) {
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发消息
                                    break;
                                }
                            }
                            //进入到好友聊天界面-------------------------------------------------------
                            w:
                            while (true) {
                                LogUtils.d("第:" + messageCuont + "条消息___总消息数:" + messageNum);

                                xmlData = wxUtils.getXmlData();
                                //判断是否在与好友聊天界面
                                if (xmlData.contains("当前所在页面,与") && xmlData.contains("聊天信息") && xmlData.contains("切换到") && xmlData.contains("更多功能按钮，已") && !xmlData.contains("微信(")) {
                                    if (xmlData.contains("按住 说话")) {
                                        wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                                        wxUtils.adb("input keyevent 4");
                                        xmlData = wxUtils.getXmlData();
                                    }

                                    List<String> copyList = wxUtils.getNodeList(xmlData);
                                    for (int c = 0; c < copyList.size(); c++) {
                                        NodeXmlBean.NodeBean copyBean = wxUtils.getNodeXmlBean(copyList.get(c)).getNode();
                                        if (copyBean != null && copyBean.getResourceid() != null && "com.tencent.mm:id/aab".equals(copyBean.getResourceid())) {
                                            if (!StringUtils.isEmpty(copyBean.getText())) {
                                                int xx = context.getResources().getDimensionPixelSize(R.dimen.x296);
                                                int yy = context.getResources().getDimensionPixelSize(R.dimen.y343);//删除
                                                wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 7000);  //删除
                                                wxUtils.adb("input keyevent 4");
                                            }
                                            break;
                                        }
                                    }
                                    int randomNumType = random.nextInt(3);
                                    if (first || messageCuont >= messageNum) {
                                        randomNumType = 0;
                                    }
                                    switch (randomNumType) {
                                        case 0://文字
                                            // 将文本内容放到系统剪贴板里。
                                            if (messageCuont >= messageNum) {
                                                cm.setText("下次再聊，拜拜");
                                            } else if (first) {
                                                cm.setText("你好。。");
                                            } else {
                                                if (textList.size() > 0) {
                                                    cm.setText(wxUtils.getFaceText(textList.get(0)));
                                                    textList.remove(0);
                                                }
                                            }
                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                                            if (messageCuont >= messageNum) {
                                                wxUtils.adb("input keyevent 4");
                                                wxUtils.adb("input keyevent 4");
                                                ShowToast.show("双向互聊任务完成", (Activity) context);
                                                return;
                                            }
                                            break;
                                        case 1://图片
                                            if (imageList.size() > 0) {
                                                downFlockImgAliPay(imageList.get(0), 0);
                                                imageList.remove(0);
                                            }
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                                            wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册

                                            int aa = 0;
                                            while (aa < 5) {
                                                aa++;
                                                xmlData = wxUtils.getXmlData();
                                                if (!xmlData.contains("图片和视频")) {
                                                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册
                                                } else {
                                                    break;
                                                }
                                            }

                                            if (xmlData.contains("图片和视频")) {
                                                wxUtils.adbClick(78, 119, 108, 149);//确定
                                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
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
                                                wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                                            }
                                            //录制时间
                                            int start;
                                            if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRecord_time_s())) {
                                                start = 5;
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
                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + timeSleep * 1000);  //长按EdiText

                                            break;
                                    }
                                    if (!first)
                                        messageCuont++;

                                    if (sendTow)
                                        first = false;
                                    f:
                                    for (int b = 0; b < (first ? 15 : 3); b++) {
                                        try {
                                            Thread.sleep(20000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        xmlData = wxUtils.getXmlData();
                                        xmlData = wxUtils.getXmlData();
                                        if (!xmlData.contains("当前所在页面,与") || !xmlData.contains("聊天信息") || !xmlData.contains("切换到") || !xmlData.contains("更多功能按钮，已") || (xmlData.contains("微信("))) {
                                            LogUtils.d("跑到其他页面去了");
                                            backHome();
                                            continue wh;
                                        }
                                        nodeList = wxUtils.getNodeList(xmlData);
                                        for (int c = nodeList.size(); c > 0; c--) {
                                            //                                            LogUtils.d(nodeList.get(c-1));
                                            if (nodeList.get(c - 1).contains(" />")) {
                                                continue;
                                            }
                                            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(c - 1)).getNode();
                                            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/jg") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc().contains("头像")) {
                                                if (nodeBean.getContentdesc().startsWith("zzz9") || nodeBean.getContentdesc().startsWith("ZZZ9")) {
                                                    LogUtils.d("回复了消息");
                                                    first = false;
                                                    NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(nodeList.get(c + 1)).getNode();
                                                    if (nodeBean1.getResourceid() != null) {
                                                        switch (nodeBean1.getResourceid()) {
                                                            case "com.tencent.mm:id/a7k":
                                                                listXY = wxUtils.getXY(nodeBean1.getBounds());//点击语音消息
                                                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                                                break;
                                                        }
                                                    }

                                                    continue w;
                                                } else {
                                                    LogUtils.d("还没有回复消息");
                                                    continue f;
                                                }
                                            }
                                        }

                                    }
                                    if (!first) {
                                        LogUtils.d("超时，任务结束");
                                        break w;
                                    } else {
                                        sendTow = true;
                                    }
                                } else {
                                    continue wh;
                                }
                            }
                            //互聊结束-----------------------------------------------------------------
                            break wh;
                        }
                    }
                }
                String endXmlData = xmlData;
                wxUtils.adbUpSlide(context);//向上滑动
                xmlData = wxUtils.getXmlData();
                if (endXmlData.equals(xmlData)) {
                    wxUtils.adb("input keyevent KEYCODE_HOME");
                    continue wh;
                }
            }
        }

    }

    /**
     * 获取双向互聊数据
     */
    private void getChatData() {
        httpChatData(1);
        httpChatData(0);
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

                        if (imageList.size() > 0) {
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
     * 养号发消息
     *
     * @param type 0图片 1文字 2视频  3语音
     */
    public void sendFriendsMessageCultivate(int type, List<String> dataBeanList) {
        LogUtils.d("养号互聊" + type);
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText

        boolean bottom = false;//到了底部
        String endData = "";
        String meName = "";

        String path = "";
        String strMark = "";
        String fileName = "";
        String filePath = "";
        String text = "";
        String fileUrl = "";
        List<String> imgList = new ArrayList<>();

        if (!(type == 0 || type == 1 || type == 3)) {
            return;
        }

        if (type == 0 || type == 2) {
            if (dataBeanList != null && dataBeanList.size() > 0) {//判断请求地址是否为空
                LogUtils.d("a" + FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages"));
                fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
                for (int a = 0; a < dataBeanList.size(); a++) {
                    text = dataBeanList.get(a);

                    path = URLS.pic_vo + text.replace("\\", "/");
                    LogUtils.d("文件url__" + path);
                    strMark = text.replace("\\", "/");
                    fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");
                    LogUtils.d("b" + fileName);
                    filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + fileName;
                    LogUtils.d("c" + filePath);


                    if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载
                        LogUtils.d("存在");
                    } else {
                        LogUtils.d("不存在");
                        File f = null;
                        try {
                            f = wxUtils.getFileDown(path, fileName);
                        } catch (Exception e) {
                            LogUtils.e("下载失败");
                            e.printStackTrace();
                        }

                        if (f == null) {
                            //                            return;
                        } else {
                            imgList.add(fileName);//添加到集合
                        }
                    }

                    FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false);//改名把文件添加到第一个
                    wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), context);

                }


            } else {
                LogUtils.d("朋友圈图文地址为空");
                return;
            }


        }

        wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y346, R.dimen.x320, R.dimen.y346);//点击Z aaa
        xmlData = wxUtils.getXmlData();//重新获取页面数据
        //        int s = random.nextInt(max) % (max - min + 1) + min;
        int randomFriends = random.nextInt(3) % 3 + 1;//随机3-4条
        LogUtils.e("好友人数:" + (randomFriends + 1));
        ShowToast.show("好友人数:" + (randomFriends + 1), (Activity) context);
        int messageCount = 0;
        w:
        while (true) {
            if (messageCount > randomFriends) {
                break w;
            }
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            a:
            for (int a = 0; a < nodeList.size(); a++) {

                if (messageCount > randomFriends) {
                    break w;
                }
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/j_")) && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && (nodeBean.getContentdesc().startsWith("ZZZ9") || nodeBean.getContentdesc().startsWith("zzz9")) && !meName.contains(nodeBean.getContentdesc())) {
                    int randomTrue1 = random.nextInt(2);//随机0-1
                    int randomTrue2 = random.nextInt(2);//随机0-1
                    if (randomTrue1 == 0 && randomTrue2 == 0) {//任务随机执行
                        continue;
                    }

                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友
                    LogUtils.d("点击进入");
                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    if (!xmlData.contains("标签")) {
                        wxUtils.adb("input keyevent 4");
                        meName = meName + nodeBean.getContentdesc();
                        continue;
                    }
                    XiuGaiBeiZhu("ZZZ9");
                    //                    xmlData = wxUtils.getXmlData();//重新获取页面数据
                    List<String> messageList = wxUtils.getNodeList(xmlData);
                    for (int b = 0; b < messageList.size(); b++) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(messageList.get(b)).getNode();
                        if (nodeBean != null && "com.tencent.mm:id/ana".equals(nodeBean.getResourceid())) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发消息
                            int wCount = 0;

                            int randomNum = random.nextInt(3);
                            LogUtils.e(randomNum + 1 + "条消息");
                            ShowToast.show(randomNum + 1 + "条消息", (Activity) context);

                            xmlData = wxUtils.getXmlData();

                            if (!xmlData.contains("当前所在页面,与")) {
                                return;
                            }


                            List<String> copyList = wxUtils.getNodeList(xmlData);
                            for (int c = 0; c < copyList.size(); c++) {
                                NodeXmlBean.NodeBean copyBean = wxUtils.getNodeXmlBean(copyList.get(c)).getNode();
                                if (copyBean != null && copyBean.getResourceid() != null && "com.tencent.mm:id/aab".equals(copyBean.getResourceid())) {
                                    if (!StringUtils.isEmpty(copyBean.getText())) {
                                        int xx = context.getResources().getDimensionPixelSize(R.dimen.x296);
                                        int yy = context.getResources().getDimensionPixelSize(R.dimen.y343);//删除
                                        wxUtils.adb("input swipe " + xx + " " + yy + " " + xx + " " + yy + " " + 7000);  //删除
                                        wxUtils.adb("input keyevent 4");
                                        break;
                                    }
                                }
                            }

                            switch (type) {
                                case 1://文字
                                    xmlData = wxUtils.getXmlData();
                                    if (xmlData.contains("切换到键盘")) {
                                        wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                                        wxUtils.adb("input keyevent 4");
                                    }

                                    switch (randomNum) {
                                        case 0:
                                            if (dataBeanList.size() >= 1) {
                                                // 将文本内容放到系统剪贴板里。
                                                cm.setText(wxUtils.getFaceText(dataBeanList.get(0)));
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                                wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                                wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                                            }
                                            break;
                                        case 1:
                                            if (dataBeanList.size() >= 2) {
                                                // 将文本内容放到系统剪贴板里。
                                                cm.setText(wxUtils.getFaceText(dataBeanList.get(0)));
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                                wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                                wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送

                                                int ran = random.nextInt(5) + 3;
                                                ShowToast.show("间隔" + ran + "秒", (Activity) context);
                                                try {
                                                    Thread.sleep(ran * 1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                // 将文本内容放到系统剪贴板里。
                                                cm.setText(wxUtils.getFaceText(dataBeanList.get(1)));
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                                wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                                wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                                            }
                                            break;
                                        case 2:
                                            if (dataBeanList.size() >= 3) {
                                                // 将文本内容放到系统剪贴板里。
                                                cm.setText(wxUtils.getFaceText(dataBeanList.get(0)));
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                                wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                                wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送

                                                int ran = random.nextInt(5) + 3;
                                                ShowToast.show("间隔" + ran + "秒", (Activity) context);
                                                try {
                                                    Thread.sleep(ran * 1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                // 将文本内容放到系统剪贴板里。
                                                cm.setText(wxUtils.getFaceText(dataBeanList.get(1)));
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                                wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                                wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送

                                                int ran1 = random.nextInt(5) + 3;
                                                ShowToast.show("间隔" + ran1 + "秒", (Activity) context);
                                                try {
                                                    Thread.sleep(ran1 * 1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                // 将文本内容放到系统剪贴板里。
                                                cm.setText(wxUtils.getFaceText(dataBeanList.get(2)));
                                                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                                wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                                wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                                            }
                                            break;
                                    }

                                    break;
                                case 0://发图片
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
                                        wxUtils.adbClick(24, 801, 144, 834);
                                        boolean ccc = true;
                                        while (ccc) {
                                            String xmlData_picture = wxUtils.getXmlData();
                                            List<String> pictureList = wxUtils.getNodeList(xmlData_picture);
                                            for (int c = 0; c < pictureList.size(); c++) {
                                                NodeXmlBean.NodeBean pictureBean = wxUtils.getNodeXmlBean(pictureList.get(c)).getNode();
                                                if (pictureBean != null && pictureBean.getResourceid() != null && "com.tencent.mm:id/d1r".equals(pictureBean.getResourceid())
                                                        && pictureBean.getText() != null && pictureBean.getText().equals("ykimages")) {
                                                    listXY = wxUtils.getXY(pictureBean.getBounds());//获取坐标
                                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击ykimages 文件夹
                                                    ccc = false;
                                                    break;
                                                }
                                            }
                                            if (ccc == true) {
                                                wxUtils.adbUpSlide(context);//向上滑动
                                            }
                                        }
                                        switch (randomNum) {
                                            case 0:

                                                //                                    wxUtils.adbClick(119, 119, 149, 149);//1         TODO copy

                                                wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                                break;
                                            case 1:
                                                //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                                //                                    wxUtils.adbClick(280, 119, 310, 149);//2

                                                wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                                wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                                //                                                NodeUtils.clickIndex("com.tencent.mm:id/a9n", 0);
                                                //                                                NodeUtils.clickIndex("com.tencent.mm:id/a9n", 1);
                                                break;
                                            case 2:
                                                //                                    wxUtils.adbClick(119, 119, 149, 149);//1
                                                //                                    wxUtils.adbClick(280, 119, 310, 149);//2
                                                //                                    wxUtils.adbClick(441, 119, 471, 149);//3
                                                wxUtils.adbClick(78, 119, 108, 149);//选择图片
                                                wxUtils.adbClick(198, 119, 228, 149);//选择图片
                                                wxUtils.adbClick(318, 119, 348, 149);//选择图片

                                                //                                                NodeUtils.clickIndex("com.tencent.mm:id/a9n", 0);
                                                //                                                NodeUtils.clickIndex("com.tencent.mm:id/a9n", 1);
                                                //                                                NodeUtils.clickIndex("com.tencent.mm:id/a9n", 2);
                                                break;
                                        }

                                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }

                                    break;
                                case 3://发送语音
                                    xmlData = wxUtils.getXmlData();
                                    if (xmlData.contains("切换到按住说话")) {
                                        wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                                    }
                                    //录制时间
                                    int start;
                                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRecord_time_s())) {
                                        start = 2;
                                    } else {
                                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getRecord_time_s());
                                    }
                                    int end;
                                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getRecord_time_e())) {
                                        end = 59;
                                    } else {
                                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getRecord_time_e());
                                    }
                                    int timeSleep = random.nextInt(end - start + 1) + start;
                                    int timeSleep1 = random.nextInt(end - start + 1) + start;
                                    int timeSleep2 = random.nextInt(end - start + 1) + start;
                                    switch (randomNum) {
                                        case 0:


                                            LogUtils.e("end=" + end + "__start=" + start + "___语音时间=" + timeSleep);
                                            ShowToast.show("语音录音时间：" + timeSleep + "秒", (Activity) context);

                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + timeSleep * 1000);  //长按EdiText
                                            break;
                                        case 1:
                                            LogUtils.e("end=" + end + "__start=" + start + "___语音时间=" + timeSleep);
                                            ShowToast.show("语音录音时间：" + timeSleep + "秒", (Activity) context);

                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + timeSleep * 1000);  //长按EdiText
                                            int ran = random.nextInt(3) + 3;
                                            ShowToast.show("间隔" + ran + "秒", (Activity) context);
                                            try {
                                                Thread.sleep(ran * 1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            LogUtils.e("end=" + end + "__start=" + start + "___语音时间=" + timeSleep1);
                                            ShowToast.show("语音录音时间：" + timeSleep1 + "秒", (Activity) context);

                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + timeSleep1 * 1000);  //长按EdiText
                                            break;
                                        case 2:

                                            LogUtils.e("end=" + end + "__start=" + start + "___语音时间=" + timeSleep);
                                            ShowToast.show("语音录音时间：" + timeSleep + "秒", (Activity) context);

                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + timeSleep * 1000);  //长按EdiText
                                            int ran1 = random.nextInt(3) + 3;
                                            ShowToast.show("间隔" + ran1 + "秒", (Activity) context);
                                            try {
                                                Thread.sleep(ran1 * 1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            LogUtils.e("end=" + end + "__start=" + start + "___语音时间=" + timeSleep1);
                                            ShowToast.show("语音录音时间：" + timeSleep1 + "秒", (Activity) context);

                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + timeSleep1 * 1000);  //长按EdiText
                                            int ran2 = random.nextInt(3) + 3;
                                            ShowToast.show("间隔" + ran2 + "秒", (Activity) context);
                                            try {
                                                Thread.sleep(ran2 * 1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            LogUtils.e("end=" + end + "__start=" + start + "___语音时间=" + timeSleep2);
                                            ShowToast.show("语音录音时间：" + timeSleep2 + "秒", (Activity) context);

                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + timeSleep2 * 1000);  //长按EdiText
                                            break;
                                    }

                                    break;
                            }

                            break;
                        }
                    }
                    messageCount++;
                    meName = meName + nodeBean.getContentdesc();
                    wxUtils.adb("input keyevent 4");
                    wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y367, R.dimen.x160, R.dimen.y400);//点击通讯录

                    //                    设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_s())) {
                        start = 10;
                    } else {
                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_e())) {
                        end = 20;
                    } else {
                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_e());
                    }
                    int timeSleep = random.nextInt(5) + 1;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (!xmlData.contains("发现") && !xmlData.contains("com.tencent.mm:id/i")) {
                ShowToast.show("任务被中断，发消息任务", (Activity) context);
                break w;
            }


            if (!bottom) {
                LogUtils.d("向上滑动了");
                wxUtils.adbUpSlide(context);//向上滑动
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                //                ShowToast.show("发消息完成", (Activity) context);
                break w;
            }
            if (xmlData.contains("位联系人")) {//判断是否到达底部
                bottom = true;
            }
        }
        ShowToast.show("发消息完成", (Activity) context);
        if (type == 0 || type == 2) {
            //            wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName), context);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int a = 0; a < imgList.size(); a++) {
                FileUtils.delete(fileUrl + "/aa" + imgList.get(a));//删除复制的文件
            }

        }
    }

    /**
     * 养号专用
     * 修改备注
     */
    private void XiuGaiBeiZhu(String str) {
        ShowToast.show("修改备注开始", (Activity) context);
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("0000");
        xmlData = wxUtils.getXmlData();//重新获取页面数据
        //        if (xmlData.contains("女")) {
        //            sex = 0;
        //        } else if (xmlData.contains("男")) {
        //            sex = 1;
        //        } else {
        //            sex = 2;
        //        }
        List<String> remarkList = wxUtils.getNodeList(xmlData);
        for (int r = 0; r < remarkList.size(); r++) {
            nodeBean = wxUtils.getNodeXmlBean(remarkList.get(r)).getNode();
            if (nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/anw"))) {
                //筛选出好友
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取修改备注标签
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                break;
            }
        }
        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
        int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl_D", 0);
        String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
        wxUtils.adb("input text " + str + "D" + wx_nume_number_new_girl);
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SPUtils.put(context, "wx_name_number_girl_D", wx_name_number_girl + 1);
        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
        //  LogUtils.d(nodeList.get(a));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    List<String> sendFriendsMessageCultivateDatasList;

    /**
     * 养号互撩数据
     * 0图片   1文字
     */
    private void sendFriendsMessageCultivateDatas(final int type) {
        String uid = SPUtils.getString(context, "uid", "0000");
        RequestParams params = new RequestParams(URLS.wechat_list());
        params.addQueryStringParameter("type", type + "");
        LogUtils.d(URLS.wechat_list() + "?type=" + type);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<WxFriendsMessageCultivate>() {

            @Override
            public void onSuccess(final WxFriendsMessageCultivate bean) {
                if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
                    sendFriendsMessageCultivateDatasList = new ArrayList<>();
                    sendFriendsMessageCultivateDatasList.addAll(bean.getData());
                } else {
                    LogUtils.d("养号互聊数据有误");
                    ShowToast.show("数据有误", (Activity) context);
                }

            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("错误返回的结果" + errorString);
                ShowToast.show("网络请求失败，请检测网络", (Activity) context);
            }
        });
    }


    /**
     * 普通好友发消息
     *
     * @param type 0图片 1文字 2视频
     */
    public void sendFriendsMessage(int type, String messageData, String designated, String Is_newAdd) {
        if (Is_newAdd != null) {// 勾选了新增发送类型
            if (StringUtils.isEmpty(messageData)) {
                LogUtils.d("发消息内容null");
                return;
            }

            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
            int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText

            boolean bottom = false;//到了底部
            String endData = "";
            String meName = "";

            String path = "";
            String strMark = "";
            String fileName = "";
            String filePath = "";
            String text = "";
            String fileUrl = "";
            int sex = 0;//0代表女。   1代表男   2代表性别未知
            DecimalFormat df = new DecimalFormat("0000");

            if (Is_newAdd.equals("1")) {
                sex_key = new String[1];
                sex_key[0] = "男";
            } else if (Is_newAdd.equals("2")) {
                sex_key = new String[1];
                sex_key[0] = "女";
            } else if (Is_newAdd.equals("3")) {
                sex_key = new String[1];
                sex_key[0] = "";//未知
            } else if (Is_newAdd.equals("12")) {
                sex_key = new String[2];
                sex_key[0] = "男";
                sex_key[1] = "女";
            } else if (Is_newAdd.equals("23")) {
                sex_key = new String[2];
                sex_key[0] = "女";
                sex_key[1] = "";
            } else if (Is_newAdd.equals("13")) {
                sex_key = new String[2];
                sex_key[0] = "男";
                sex_key[1] = "";
            } else if (Is_newAdd.equals("123")) {
                sex_key = new String[3];
                sex_key[0] = "男";
                sex_key[1] = "女";
                sex_key[2] = "";
            }
            if (type == 0 || type == 2) {
                if (!StringUtils.isEmpty(messageData)) {//判断请求地址是否为空
                    LogUtils.d("a" + FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages"));
                    fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
                    text = messageData;
                    String[] str_text = text.split("\\|");
                    text = str_text[0];

                    path = URLS.pic_vo + text.replace("\\", "/");
                    String[] str_path = path.split("\\|");
                    path = str_path[0];

                    LogUtils.d("文件url__" + path);
                    strMark = text.replace("\\", "/");
                    fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");
                    LogUtils.d("b" + fileName);
                    filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + fileName;
                    LogUtils.d("c" + filePath);


                    if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载
                        LogUtils.d("存在");
                    } else {
                        LogUtils.d("不存在");
                        File f = null;
                        try {
                            f = wxUtils.getFileDown(path, fileName);
                        } catch (Exception e) {
                            LogUtils.e("下载失败");
                            e.printStackTrace();
                        }

                        if (f == null) {
                            return;
                        }
                    }

                    FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false);//改名把文件添加到第一个
                    wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), context);


                } else {
                    LogUtils.d("发消息图片或视频地址为空");
                    return;
                }


            }

            //        wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y346, R.dimen.x320, R.dimen.y346);//点击Z
            xmlData = wxUtils.getXmlData();//重新获取页面数据

            w:
            while (true) {
                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                List<String> nodeList = wxUtils.getNodeList(xmlData);
                a:
                for (int a = 0; a < nodeList.size(); a++) {

                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/j_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().startsWith("ZZZ") && !meName.contains(nodeBean.getContentdesc())) {
                        //筛选出好友
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                        LogUtils.d("点击进入");
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        if (!xmlData.contains("标签")) {
                            wxUtils.adb("input keyevent 4");
                            meName = meName + nodeBean.getContentdesc();
                            continue;
                        }
                        List<String> messageList = wxUtils.getNodeList(xmlData);


                        for (int k1 = 0; k1 < sex_key.length; k1++) {
                            //首先修改备注
                            if (xmlData.contains(sex_key[k1])) {
                                if (sex_key[k1].equals("女") && xmlData.contains("女")) {
                                    sex = 0;
                                } else if (sex_key[k1].equals("男") && xmlData.contains("男")) {
                                    sex = 1;
                                } else if (!xmlData.contains("男") && !xmlData.contains("女")) {
                                    sex = 2;

                                } else {
                                    sex = 3;
                                    break;
                                }
                                for (int b = 0; b < messageList.size(); b++) {
                                    NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(messageList.get(b)).getNode();
                                    if (nodeBean2.getText() != null && nodeBean2.getText().equals("设置备注和标签") && !xmlData.contains("ZZZ")) {
                                        //筛选出好友
                                        listXY = wxUtils.getXY(nodeBean2.getBounds());//获取修改备注标签的坐标
                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击修改备注
                                        wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        switch (sex) {//0代表女。   1代表男   2代表性别未知
                                            case 0:
                                                int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl", 0);
                                                String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                                                wxUtils.adb("input text " + "ZZZ0" + "B" + wx_nume_number_new_girl);
                                                SPUtils.put(context, "wx_name_number_girl", wx_name_number_girl + 1);
                                                break;
                                            case 1:
                                                int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy", 0);
                                                String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                                                wxUtils.adb("input text " + "ZZZ0" + "A" + wx_nume_number_new_boy);
                                                SPUtils.put(context, "wx_name_number_boy", wx_name_number_boy + 1);
                                                break;
                                            case 2:
                                                int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c", 0);
                                                String wx_nume_number_c = df.format(wx_name_number_c + 1);
                                                wxUtils.adb("input text " + "ZZZ0" + "C" + wx_nume_number_c);
                                                SPUtils.put(context, "wx_name_number_c", wx_name_number_c + 1);
                                                break;
                                        }
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                                        break;
                                        //  LogUtils.d(nodeList.get(a));
                                    }
                                }

                            } else {
                                sex = 3;
                                continue;

                            }
                            break;
                        }
                        if (sex != 3) {
                            wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
                            xmlData = wxUtils.getXmlData();
                            messageList = wxUtils.getNodeList(xmlData); //修改备注之后会多一行，发消息的位置 变成了视频聊天，后面点到视频聊天而中断
                            for (int c = 0; c < messageList.size(); c++) {
                                NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(messageList.get(c)).getNode();
                                if (nodeBean3.getResourceid() != null && nodeBean3.getText() != null && nodeBean3.getText().equals("发消息") && ("com.tencent.mm:id/ana".equals(nodeBean3.getResourceid()))) {
                                    listXY = wxUtils.getXY(nodeBean3.getBounds());//
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发消息,防止点到视频聊天上


                                    switch (type) {
                                        case 1://文字
                                            xmlData = wxUtils.getXmlData();
                                            if (xmlData.contains("切换到键盘")) {
                                                wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                                                wxUtils.adb("input keyevent 4");
                                            }

                                            cm.setText(wxUtils.getFaceText(messageData));
                                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                                            break;
                                        case 0://发图片
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
                                                wxUtils.adbClick(24, 801, 144, 834);//点击左下角
                                                String newXmlData = wxUtils.getXmlData();
                                                nodeList = wxUtils.getNodeList(newXmlData);
                                                for (int aaaa = 0; aaaa < nodeList.size(); aaaa++) {
                                                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(aaaa)).getNode();
                                                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/ccb") && nodeBean.getText() != null && nodeBean.getText().equals("ykimages")) {
                                                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  发消息
                                                        break;
                                                    }
                                                }
                                                //                                        wxUtils.adbClick(119, 119, 149, 149);//       TODO copy
                                                wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y56, R.dimen.x99, R.dimen.y69);//确定
                                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                                try {
                                                    Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            break;
                                        case 2://发视频
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
                                                //                                        wxUtils.adbClick(119, 119, 149, 149);   TODO copy
                                                //                                              wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y56, R.dimen.x99, R.dimen.y69);//确定
                                                wxUtils.adbClick(78, 119, 108, 149);
                                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                                xmlData = wxUtils.getXmlData();
                                                if (xmlData.contains("你现在不在无线局域网环境，继续操作会消耗较多流量。")) {
                                                    //                                            wxUtils.adbClick(300, 517, 396, 562);   TODO copy
                                                    wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y243, R.dimen.x264, R.dimen.y263);//确定
                                                }
                                                try {
                                                    Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            break;
                                    }

                                }

                            }
                            wxUtils.adb("input keyevent 4");
                            //                          wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y367, R.dimen.x160, R.dimen.y400);//点击通讯录
                            wxUtils.adbClick(150, 800, 200, 850);//通讯录调小了，防止点不到的情况


                            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                            nodeList = wxUtils.getNodeList(xmlData);

                        } else {

                            wxUtils.adb("input keyevent 4");

                        }
                        meName = meName + nodeBean.getContentdesc();
                        //设置间隔时间
                        int start;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_s())) {
                            start = 5;
                        } else {
                            //                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_s());
                            start = 5;
                            LogUtils.e("start2" + start);
                        }
                        int end;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_e())) {
                            end = 10;
                        } else {
                            //                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_e());
                            end = 10;
                            LogUtils.e("end2" + end);
                        }
                        int timeSleep = random.nextInt(end - start + 1) + start;
                        LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                        ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                        try {
                            Thread.sleep(timeSleep * 1000);


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;//zhangshuai
                    }

                }

                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                nodeList = wxUtils.getNodeList(xmlData);
                for (int c = 0; c < nodeList.size(); c++) {
                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(c)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/j_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().startsWith("ZZZ") && !meName.contains(nodeBean.getContentdesc())) {
                        continue w;// 重新获取界面后 发现 依然存在满足条件的， 就继续执行w
                    }
                }

                if (!xmlData.contains("发现") && !xmlData.contains("com.tencent.mm:id/j_")) {
                    ShowToast.show("任务被中断，发消息任务", (Activity) context);
                    break w;
                }
                if (!bottom) {
                    LogUtils.d("向上滑动了");
                    wxUtils.adbUpSlide(context);//向上滑动
                }
                endData = xmlData;
                xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
                if (endData.equals(xmlData)) {
                    //                ShowToast.show("发消息完成", (Activity) context);
                    break w;
                }

            }
            ShowToast.show("发消息完成", (Activity) context);
            if (type == 0 || type == 2) {
                //            wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName), context);
                try {
                    Thread.sleep(10000);
                    FileUtils.delete(fileUrl + "/aa" + fileName);//删除复制的文件
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        } else {//不勾选新增
            if (StringUtils.isEmpty(messageData)) {
                LogUtils.d("发消息内容null");
                return;
            }

            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
            int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText

            boolean bottom = false;//到了底部
            String endData = "";
            String meName = "";

            String path = "";
            String strMark = "";
            String fileName = "";
            String filePath = "";
            String text = "";
            String fileUrl = "";

            if (type == 0 || type == 2) {
                if (!StringUtils.isEmpty(messageData)) {//判断请求地址是否为空
                    LogUtils.d("a" + FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages"));
                    fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
                    text = messageData;
                    String[] str_text = text.split("\\|");
                    text = str_text[0];

                    path = URLS.pic_vo + text.replace("\\", "/");
                    String[] str_path = path.split("\\|");
                    path = str_path[0];

                    LogUtils.d("文件url__" + path);
                    strMark = text.replace("\\", "/");
                    fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");
                    LogUtils.d("b" + fileName);
                    filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + fileName;
                    LogUtils.d("c" + filePath);


                    if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载
                        LogUtils.d("存在");
                    } else {
                        LogUtils.d("不存在");
                        File f = null;
                        try {
                            f = wxUtils.getFileDown(path, fileName);
                        } catch (Exception e) {
                            LogUtils.e("下载失败");
                            e.printStackTrace();
                        }

                        if (f == null) {
                            return;
                        }
                    }

                    FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false);//改名把文件添加到第一个
                    wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), context);


                } else {
                    LogUtils.d("发消息图片或视频地址为空");
                    return;
                }


            }

            //        wxUtils.adbDimensClick(context, R.dimen.x296, R.dimen.y346, R.dimen.x320, R.dimen.y346);//点击Z
            xmlData = wxUtils.getXmlData();//重新获取页面数据

            w:
            while (true) {
                List<String> nodeList = wxUtils.getNodeList(xmlData);
                a:
                for (int a = 0; a < nodeList.size(); a++) {

                    nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/j_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !meName.contains(nodeBean.getContentdesc())) {
                    /*int randomTrue = random.nextInt(3);//随机1-3条
                    if (randomTrue != 0) {//任务随机执行
                        continue;
                    }*/
                        if (!StringUtils.isEmpty(designated)) {
                            if (!nodeBean.getContentdesc().startsWith(designated)) {
                                continue;
                            }
                        }

                        //筛选出好友
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                        LogUtils.d("点击进入");
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        if (!xmlData.contains("标签")) {
                            wxUtils.adb("input keyevent 4");
                            meName = meName + nodeBean.getContentdesc();
                            continue;
                        }

                        List<String> messageList = wxUtils.getNodeList(xmlData);
                        for (int b = 0; b < messageList.size(); b++) {
                            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(messageList.get(b)).getNode();
                            if ("com.tencent.mm:id/ana".equals(nodeBean.getResourceid())) {
                                listXY = wxUtils.getXY(nodeBean.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发消息

                                switch (type) {
                                    case 1://文字
                                        xmlData = wxUtils.getXmlData();
                                        if (xmlData.contains("切换到键盘")) {
                                            wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                                            wxUtils.adb("input keyevent 4");
                                        }

                                        cm.setText(wxUtils.getFaceText(messageData));
                                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                                        wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                                        wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                                        break;
                                    case 0://发图片
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
                                            wxUtils.adbClick(24, 801, 144, 834);//点击左下角
                                            String newXmlData = wxUtils.getXmlData();
                                            nodeList = wxUtils.getNodeList(newXmlData);
                                            for (int aaaa = 0; aaaa < nodeList.size(); aaaa++) {
                                                NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(nodeList.get(aaaa)).getNode();
                                                if (nodeBean2.getResourceid() != null && nodeBean2.getResourceid().equals("com.tencent.mm:id/ccb") && nodeBean2.getText() != null && nodeBean2.getText().equals("ykimages")) {
                                                    listXY = wxUtils.getXY(nodeBean2.getBounds());//获取 发消息 的坐标
                                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  发消息
                                                    break;
                                                }
                                            }
                                            //                                        wxUtils.adbClick(119, 119, 149, 149);//       TODO copy
                                            //                                            wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y56, R.dimen.x99, R.dimen.y69);//确定
                                            wxUtils.adbClick(78, 119, 108, 149);
                                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                            try {
                                                Thread.sleep(500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        break;
                                    case 2://发视频
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
                                            //                                        wxUtils.adbClick(119, 119, 149, 149);   TODO copy
                                            //                                            wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y56, R.dimen.x99, R.dimen.y69);//确定
                                            wxUtils.adbClick(78, 119, 108, 149);
                                            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                            xmlData = wxUtils.getXmlData();
                                            if (xmlData.contains("你现在不在无线局域网环境，继续操作会消耗较多流量。")) {
                                                //                                            wxUtils.adbClick(300, 517, 396, 562);   TODO copy
                                                wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y243, R.dimen.x264, R.dimen.y263);//确定
                                            }
                                            try {
                                                Thread.sleep(500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        break;
                                }

                            }
                        }
                        meName = meName + nodeBean.getContentdesc();
                        wxUtils.adb("input keyevent 4");
                        wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y367, R.dimen.x160, R.dimen.y400);//点击通讯录

                        //设置间隔时间
                        int start;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_s())) {
                            start = 60;
                        } else {
                            //start = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_s());
                            start = 5;
                            LogUtils.e("start2" + start);
                        }
                        int end;
                        if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_e())) {
                            end = 200;
                        } else {
                            //                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_e());
                            end = 10;


                            LogUtils.e("end2" + end);
                        }
                        int timeSleep = random.nextInt(end - start + 1) + start;
                        LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                        ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                        try {
                            Thread.sleep(timeSleep * 1000);


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
                if (!xmlData.contains("发现") && !xmlData.contains("com.tencent.mm:id/i")) {
                    ShowToast.show("任务被中断，发消息任务", (Activity) context);
                    break w;
                }


                if (!bottom) {
                    LogUtils.d("向上滑动了");
                    wxUtils.adbUpSlide(context);//向上滑动
                }
                endData = xmlData;
                xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
                if (endData.equals(xmlData)) {
                    //                ShowToast.show("发消息完成", (Activity) context);
                    break w;
                }
                if (xmlData.contains("位联系人")) {//判断是否到达底部
                    bottom = true;
                }
            }
            ShowToast.show("发消息完成", (Activity) context);
            if (type == 0 || type == 2) {
                //            wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName), context);
                try {
                    Thread.sleep(10000);
                    FileUtils.delete(fileUrl + "/aa" + fileName);//删除复制的文件
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    /**
     * 给微信群发消息
     *
     * @param messageData
     */
    public void sendFlockMessage(int type, String messageData) {
        //0 位图片  2为视频 1为文字
        if (StringUtils.isEmpty(messageData)) {
            return;
        }

        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        String qunClickMark = "";//进过的群
        boolean isOneSlide = false;
        int count = 0;
        int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
        int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText

        String path = "";
        String strMark = "";
        String fileName = "";
        String filePath = "";
        String text = "";
        String fileUrl = "";

        if (type == 0 || type == 2) {
            if (!StringUtils.isEmpty(messageData)) {//判断请求地址是否为空
                LogUtils.d("a" + FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages"));
                fileUrl = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages";
                text = messageData;

                path = URLS.pic_vo + text.replace("\\", "/");
                if (type == 2) {
                    String[] Str = path.split("\\|");
                    path = Str[0];
                }
                LogUtils.d("文件url__" + path);
                strMark = text.replace("\\", "/");
                fileName = strMark.substring(strMark.lastIndexOf("/")).replace("/", "").replace(" ", "");
                if (type == 2) {
                    String[] str = fileName.split("\\|");
                    fileName = str[0];
                }
                LogUtils.d("b" + fileName);
                filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages/" + fileName;
                LogUtils.d("c" + filePath);


                if (new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", fileName).exists()) {//不存在，下载
                    LogUtils.d("存在");
                } else {
                    LogUtils.d("不存在");
                    File f = null;
                    try {
                        f = wxUtils.getFileDown(path, fileName);
                    } catch (Exception e) {
                        LogUtils.e("下载失败");
                        e.printStackTrace();
                    }

                    if (f == null) {
                        return;
                    }
                }

                FileUtils.copy(fileUrl + "/" + fileName, fileUrl + "/aa" + fileName, false);//改名把文件添加到第一个
                wxUtils.addimages(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ykimages", "aa" + fileName), context);


            } else {
                LogUtils.d("发消息图片或视频地址为空");
                return;
            }


        }


        //进入群聊
        xmlData = wxUtils.getXmlData();
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if ("群聊".equals(nodeBean.getText())) {//获取群聊node节点
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群聊坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击群聊
                break;
            }
        }

        //进入了群列表
        w:
        while (true) {
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                wxUtils.adb("input keyevent 4");
                //                ShowToast.show("没有群...", (Activity) context);
                break;
            }

            List<String> nodeList = new ArrayList<>();
            Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
            Matcher matcher = pattern.matcher(xmlData);
            while (matcher.find()) {
                nodeList.add(matcher.group() + "/>");
            }

            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().contains("com.tencent.mm:id/a9u")) {
                    if (qunClickMark.contains(nodeBean.getText())) {
                        continue;
                    } else {
                        if (!isOneSlide) {
                            for (int b = 0; b < count; b++) {
                                wxUtils.adbUpSlide(context);//向上滑动
                            }
                        }
                        isOneSlide = false;
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        qunClickMark = qunClickMark + nodeBean.getText() + ",";
                    }

                    //_______________________________________________________________________________________________
                    String qunName = "";
                    //获取群人数，男女群信息
                    String qunNameData = wxUtils.getXmlData();
                    List<String> qunNameDataList = new ArrayList<String>();
                    Matcher matcherA = pattern.matcher(qunNameData);
                    while (matcherA.find()) {
                        qunNameDataList.add(matcherA.group() + "/>");
                    }
                    if (!(qunNameData.contains("当前所在页面,与"))) {
                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                        break w;
                    }

                    for (int c = 0; c < qunNameDataList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
                        if ("com.tencent.mm:id/gh".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }

                    //操作群
                    LogUtils.e("发送消息");


                    //-------------------------------------------------------------------------------
                    int wCount = 0;
                    switch (type) {
                        case 1://发消息
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(wxUtils.getFaceText(messageData));
                            x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                            y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                            wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                            wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                            break;
                        case 0://发图片
                            wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y235, R.dimen.x88, R.dimen.y298);//相册
                            wCount = 0;
                            while (wCount < 5) {
                                wCount++;
                                xmlData = wxUtils.getXmlData();
                                if (xmlData.contains("图片和视频")) {
                                    wxUtils.adbClick(78, 119, 108, 149);
                                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }

                            break;
                        case 2://发视频
                            wxUtils.adbDimensClick(context, R.dimen.x268, R.dimen.y367, R.dimen.x316, R.dimen.y400);//更多功能
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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
                                wxUtils.adbClick(78, 119, 108, 149);
                                wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                xmlData = wxUtils.getXmlData();
                                if (xmlData.contains("你现在不在无线局域网环境，继续操作会消耗较多流量。")) {
                                    //                                            wxUtils.adbClick(300, 517, 396, 562);   TODO copy
                                    wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y243, R.dimen.x264, R.dimen.y263);//确定
                                }
                                break;
                            }
                            break;
                    }
                    //-------------------------------------------------------------------------------

                    //返回
                    wxUtils.adb("input keyevent 4");
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊

                  /*  //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_s())) {
                        start = 60;
                    } else {
                        start = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(app.getWxGeneralSettingsBean().getMsg_interval_time_e())) {
                        end = 200;
                    } else {
                        end = Integer.valueOf(app.getWxGeneralSettingsBean().getMsg_interval_time_e());
                    }
                    int timeSleep = random.nextInt(end - start + 1) + start;
                    LogUtils.e("end=" + end + "__start=" + start + "___间隔随机数=" + timeSleep);
                    ShowToast.show("间隔时间：" + timeSleep + "秒", (Activity) context);
                    try {
                        Thread.sleep(timeSleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            count++;
            isOneSlide = true;
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData)) {
                wxUtils.adb("input keyevent 4");
                ShowToast.show("群消息发送完成", (Activity) context);
                break;
            }


        }

     /*   try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int a = 0; a < imgList.size(); a++) {
            FileUtils.delete(fileUrl + "/aa" + imgList.get(a));//删除复制的文件
        }*/

        ShowToast.show("群消息发送完毕", (Activity) context);

    }

    /**
     * 统计好友数和群信息
     */
    private void statistics() {
        backHome();
        wxUtils.adbClick(306, 36, 378, 108);//搜索
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        cm.setText("ZZZ9");
        wxUtils.adb("input swipe " + 300 + " " + 80 + " " + 300 + " " + 80 + " " + 2000);
        wxUtils.adbClick(160, 200, 160, 200);//点击粘贴
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adb("input keyevent 4");//返回
        xmlData = wxUtils.getXmlData();
        int aa = 0;
        if (xmlData.contains("更多联系人")) {
            aa = 1;
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                        && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                        ) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                    break;
                }
            }
        } else {
            wxUtils.adbUpSlide(context);
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("更多联系人")) {
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/in")
                            && nodeBean.getText() != null && nodeBean.getText().equals("更多联系人")
                            ) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取 发消息 的坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击  更多联系人
                        break;
                    }
                }
            } else {
                wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
            }
        }
        String meName_ZZZ9 = "";
        int count_ZZZ9 = 0;
        Boolean flag_ZZZ9 = true;
        while (flag_ZZZ9) {
            xmlData = wxUtils.getXmlData();
            List<String> nodeList_ZZZ9 = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList_ZZZ9.size(); a++) {
                NodeXmlBean.NodeBean nodeBean_ZZZ9 = wxUtils.getNodeXmlBean(nodeList_ZZZ9.get(a)).getNode();
                if (nodeBean_ZZZ9 != null && nodeBean_ZZZ9.getText() != null && (nodeBean_ZZZ9.getText().startsWith("ZZZ9") || nodeBean_ZZZ9.getText().startsWith("zzz9")) &&
                        nodeBean_ZZZ9.getResourceid() != null && nodeBean_ZZZ9.getResourceid().equals("com.tencent.mm:id/kq") && !meName_ZZZ9.contains(nodeBean_ZZZ9.getText())) {
                    count_ZZZ9++;
                    meName_ZZZ9 = meName_ZZZ9 + nodeBean_ZZZ9.getText();

                }
            }
            if (count_ZZZ9 > 3) {
                String oldXmlData = xmlData;
                wxUtils.adbUpSlide(context);
                xmlData = wxUtils.getXmlData();
                if (xmlData.equals(oldXmlData)) {
                    flag_ZZZ9 = false;
                }
            } else {
                flag_ZZZ9 = false;
            }

        }
        String zzz9_friend_count = count_ZZZ9 + "";

        backHome();
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("详细资料")) {
            wxUtils.adb("input keyevent 4");
        }
        WxNumBean wxNumBean = new WxNumBean();
        WxNumBean.ContentBean contentBean = new WxNumBean.ContentBean();
        List<WxNumBean.ContentBean.FlockBean> flockBeanList = new ArrayList<>();
        while (true) {
            wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("位联系人")) {
                break;
            }
        }
        List<String> strings = wxUtils.getNodeList(xmlData);

        //设置好友数
        for (String s : strings) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(s).getNode();
            if ("com.tencent.mm:id/amy".equals(nodeBean.getResourceid())) {
                contentBean.setFriends_num(nodeBean.getText().replace("位联系人", ""));
                LogUtils.d("联系人" + nodeBean.getText().replace("位联系人", ""));
                break;
            }
        }

        //设置uid
        contentBean.setUid(SPUtils.getString(context, "uid", "0000"));


        //设置群信息
        //        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊

/*        //进入群聊
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if ("群聊".equals(nodeBean.getText())) {//获取群聊node节点
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群聊坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击群聊
                break;
            }
        }*/

        String qunClickMark = "";//进过的群
        boolean isOneSlide = false;
        int count = 0;

        //进入了群列表
        w:
        while (true) {
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                wxUtils.adb("input keyevent 4");
                //                ShowToast.show("没有群...", (Activity) context);
                break;
            }

            List<String> nodeList = new ArrayList<>();
            Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
            Matcher matcher = pattern.matcher(xmlData);
            while (matcher.find()) {
                nodeList.add(matcher.group() + "/>");
            }

            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().contains("com.tencent.mm:id/a9u")) {
                    String flockName = nodeBean.getText();
                   /* if (true) {//给自己群发
                        if (nodeBean.getText().length() < 7) {
                            continue;
                        }
                        if (!(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("a") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("A")) && !(nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("b") || nodeBean.getText().substring(nodeBean.getText().length() - 7).startsWith("B"))) {
                            continue;
                        }
                    }*/


                    if (qunClickMark.contains(nodeBean.getText())) {
                        continue;
                    } else {
                        if (!isOneSlide) {
                            for (int b = 0; b < count; b++) {
                                wxUtils.adbUpSlide(context);//向上滑动
                            }
                        }
                        isOneSlide = false;
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        qunClickMark = qunClickMark + nodeBean.getText() + ",";
                    }

                    //_______________________________________________________________________________________________
                    String qunName = "";
                    //获取群人数，男女群信息
                    String qunNameData = wxUtils.getXmlData();
                    List<String> qunNameDataList = new ArrayList<String>();
                    Matcher matcherA = pattern.matcher(qunNameData);
                    while (matcherA.find()) {
                        qunNameDataList.add(matcherA.group() + "/>");
                    }
                    if (!(qunNameData.contains("当前所在页面,与"))) {
                        ShowToast.show("任务被中断，结束拉群任务", (Activity) context);
                        break w;
                    }

                    for (int c = 0; c < qunNameDataList.size(); c++) {
                        NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
                        if ("com.tencent.mm:id/hj".equals(qunNameBean.getResourceid())) {
                            qunName = qunNameBean.getText();
                            LogUtils.d(qunName + "qunName");
                            break;
                        }
                    }

                    //操作群

                    if (qunName.length() >= 10) {
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(qunName.substring(qunName.length() - 3));
                        int qb = Integer.parseInt(m.replaceAll("").trim());//群人数

                        flockBeanList.add(new WxNumBean.ContentBean.FlockBean(flockName, qb + ""));
                    }

                    //--------------------------------------------------------------------------------------------------------------------------------------

                    //返回
                    wxUtils.adb("input keyevent 4");//fdsfasfsfjksfjlsdjfkl
                    NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊


                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            count++;
            isOneSlide = true;
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData)) {
                wxUtils.adb("input keyevent 4");
                ShowToast.show("群消息发送完成", (Activity) context);
                break;
            }
        }
        //        ShowToast.show("群信息统计完毕", (Activity) context);
        //添加
        contentBean.setFlock(flockBeanList);
        String wxAccount = SPUtils.getString(context, "wxAccount", ""); //目前的微信号
        String accountLocation = SPUtils.getString(context, "WxAccountLocation", ""); //获取目前的位置
        contentBean.setAccount(wxAccount);
        contentBean.setLocation(accountLocation);
        contentBean.setZzz9_friend_count(zzz9_friend_count);
        wxNumBean.setContent(contentBean);
        String str = new Gson().toJson(wxNumBean);
        LogUtils.d("JSON" + str.toString());
        //        ShowToast.show(str.toString(), (Activity) context);
        //        setWxnum(str);
        try {
            Response data = OkHttpUtils.post().url(URLS.wxNewstatictis_crowd()).addParams("data", str.replace("\\", "")).build().execute();
            if (data.code() == 200) {
                String string = data.body().string();
                Log.d("zs1", string);
            } else {
                data = OkHttpUtils.post().url(URLS.wxNewstatictis_crowd()).addParams("data", str.replace("\\", "")).build().execute();
                if (data.code() == 200) {
                    String string = data.body().string();
                    Log.d("zs2", string);
                } else {
                    data = OkHttpUtils.post().url(URLS.wxNewstatictis_crowd()).addParams("data", str.replace("\\", "")).build().execute();
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

    /**
     * 微信发单发到自己群
     *
     * @param messageData
     * @param crowd
     */
    private boolean billing(String messageData, String crowd) {

        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        List<String> nodeList;
        WxFlockMessageBean[] wxFlockMessageBeans = new Gson().fromJson(messageData.replace("&quot", "\"").replace(";", ""), WxFlockMessageBean[].class);

        if (!StringUtils.isEmpty(messageData) && wxFlockMessageBeans != null && wxFlockMessageBeans.length > 0) {
        } else {
            ShowToast.show("数据有误", (Activity) context);
            return false;
        }

        //进入群聊
        while (true) {
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                wxUtils.adb("input keyevent 4");
                return false;
            }

            if ((xmlData.contains("应用") && xmlData.contains("主屏幕")) || xmlData.contains("wx助手")) {
                return false;
            } else if (!(xmlData.contains("新群聊") && xmlData.contains("返回"))) {
                backHome();
                NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y88, R.dimen.x296, R.dimen.y123);//群聊
            } else {
                break;
            }
        }


        //进入指定群发消息
        boolean isExist = false;
        nodeList = wxUtils.getNodeList(xmlData);
        for (int a = 0; a < nodeList.size(); a++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
            if (nodeBean.getResourceid() != null && nodeBean.getResourceid().contains("com.tencent.mm:id/a9u")) {

                LogUtils.d(crowd + "当前群=" + nodeBean.getText());
                if (!(crowd != null && crowd.equals(nodeBean.getText()))) {
                    continue;
                }

                listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            ShowToast.show("没有设备群，任务中断", (Activity) context);
            return false;
        }

        //_______________________________________________________________________________________________
        String qunName = "";
        //获取群人数，男女群信息
        String qunNameData = wxUtils.getXmlData();
        List<String> qunNameDataList = wxUtils.getNodeList(qunNameData);

        if (!(qunNameData.contains("当前所在页面,与") && qunNameData.contains("聊天信息"))) {
            return false;
        }
        for (int c = 0; c < qunNameDataList.size(); c++) {
            NodeXmlBean.NodeBean qunNameBean = wxUtils.getNodeXmlBean(qunNameDataList.get(c)).getNode();
            //                        LogUtils.d(qunNameBean.toString());
            if ("com.tencent.mm:id/hj".equals(qunNameBean.getResourceid())) {
                qunName = qunNameBean.getText();
                LogUtils.d(qunName + "qunName");
                break;
            }
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("当前所在页面,与") && xmlData.contains("聊天信息")) {
            //操作群
            LogUtils.e("发送消息");
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("按住 说话")) {
                wxUtils.adbDimensClick(context, R.dimen.x4, R.dimen.y367, R.dimen.x52, R.dimen.y400);//切换到键盘
                wxUtils.adb("input keyevent 4");
                xmlData = wxUtils.getXmlData();
            }

            List<String> copyList = wxUtils.getNodeList(xmlData);
            for (int c = 0; c < copyList.size(); c++) {
                NodeXmlBean.NodeBean copyBean = wxUtils.getNodeXmlBean(copyList.get(c)).getNode();
                if ("com.tencent.mm:id/aab".equals(copyBean.getResourceid())) {
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

                LogUtils.d(wxFlockMessageBeans[b].toString());
                switch (wxFlockMessageBeans[b].getType()) {
                    case "txt":


                        // 将文本内容放到系统剪贴板里。
                                /*cm.setText(wxFlockMessageBeans[b].getData());
                                cm.setText("淘口令:￥E8jW01PdrJP￥\n" +
                                        "精准计价，超长待机90天");*/

                        cm.setText(wxFlockMessageBeans[b].getData());
                        int x = context.getResources().getDimensionPixelSize(R.dimen.x136);
                        int y = context.getResources().getDimensionPixelSize(R.dimen.y383);//EdiText
                        wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 2000);  //长按EdiText
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x52, R.dimen.y345, R.dimen.x52, R.dimen.y345);//点击粘贴
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        wxUtils.adbDimensClick(context, R.dimen.x270, R.dimen.y372, R.dimen.x314, R.dimen.y395);//点击发送
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "img":
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

                        if (!downFlockImgAliPay(wxFlockMessageBeans[b].getData(), 1)) {
                            return false;
                        }

                        try {
                            Thread.sleep(1000);
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
                            //                            wxUtils.adbClick(119, 119, 149, 149);
                            wxUtils.adbClick(24, 801, 144, 834);//点击左下角
                            boolean ccc = true;
                            while (ccc) {
                                String xmlData_picture = wxUtils.getXmlData();
                                List<String> pictureList = wxUtils.getNodeList(xmlData_picture);
                                for (int c = 0; c < pictureList.size(); c++) {
                                    NodeXmlBean.NodeBean pictureBean = wxUtils.getNodeXmlBean(pictureList.get(c)).getNode();
                                    if (pictureBean != null && pictureBean.getResourceid() != null && "com.tencent.mm:id/d1r".equals(pictureBean.getResourceid())
                                            && pictureBean.getText() != null && pictureBean.getText().equals("ykimages")) {
                                        listXY = wxUtils.getXY(pictureBean.getBounds());//获取坐标
                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击ykimages 文件夹
                                        ccc = false;
                                        break;
                                    }
                                }
                                if (ccc == true) {
                                    wxUtils.adbUpSlide(context);//向上滑动
                                }
                            }
                            wxUtils.adbClick(78, 119, 108, 149); //选择图片
                            wxUtils.adbClick(360, 49, 468, 94);  //发送
                            break;
                        }
                        break;
                }
            }
        }


        return true;
    }

    /**
     * 微信账号切换 6.6.3 版本  只有发送 新号或者老号中的一个
     */

    private void switchWxAccount1() {
        SPUtils.putString(context, "SwitchAccountSuccess", "0");
        int sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
        wxUtils.openWx((Activity) context);
        int accountNum = 0;
        //        wxUtils.adbClick(411, 822, 429, 847);// 点击右下角的我
        NodeUtils.clickNode("我", "com.tencent.mm:id/c_z");//点击右下角的我
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("相册") || !xmlData.contains("收藏")) {
            return;
        } else {
            List<String> ud = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < ud.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ud.get(a)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/cdh")) && nodeBean.getText() != null && nodeBean.getText().contains("微信号")) {
                    String str = nodeBean.getText();
                    String wxAccount = str.replaceAll("微信号：", "");
                    SPUtils.putString(context, "wxAccount", wxAccount);
                    break;
                }
            }
        }
        wxUtils.adbUpSlide(context);
        wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.setting.ui.setting.SettingsUI");
        wxUtils.adbUpSlide(context);
        wxUtils.adbClick(21, 681, 459, 714); //点击切换账号
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < nodeList.size() - 1; i++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
            if (nodeBean != null && nodeBean.getText() != null && !nodeBean.getText().equals("切换账号") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/d5s")) {
                accountNum++;
            }
        }
        for (int i = 0; i < nodeList.size() - 1; i++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
            if (nodeBean != null && nodeBean.getText() != null && nodeBean.getText().equals("当前使用") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/d5v")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取 当前使用的坐标
                break;
            }
        }
        if (accountNum == 2) {  //老号在左边  新号在右边

            //说明已经登录了两个账号
            if (listXY.get(0) == 110) {
                //正在使用的账号 在左边（老号）， 点击右边的账号切换

                if ((sendAccountType == 3) || (sendAccountType == 1)) {
                    SPUtils.putString(context, "WxAccountLocation", "1"); //目前的账号在左边
                    //                    wxUtils.adbClick(0, 36, 90, 108);//点击左上角的返回
                    NodeUtils.clickNode("返回", "com.tencent.mm:id/d5i");
                    SPUtils.putString(context, "SwitchAccountSuccess", "1"); // 没有切换
                    //                    wxUtils.adbClick(0, 36, 90, 108);//点击左上角的返回
                    NodeUtils.clickNode("返回", "com.tencent.mm:id/d5i");//点击左上角的返回
                    return;
                }
                if (sendAccountType == 2) {
                    SPUtils.putString(context, "WxAccountLocation", "2"); //切换后的账号在右边
                    wxUtils.adbClick(288, 457, 384, 553);
                    SPUtils.putString(context, "SwitchAccountSuccess", "2"); // 切换成功
                }
            } else if (listXY.get(0) == 302) {
                //正在使用的账号 在右边(新号)， 点击左边的账号切换

                if ((sendAccountType == 3) || (sendAccountType == 2)) {
                    SPUtils.putString(context, "WxAccountLocation", "2"); //目前的账号在右边
                    //                    wxUtils.adbClick(0, 36, 90, 108);//点击左上角的返回
                    NodeUtils.clickNode("返回", "com.tencent.mm:id/d5i");//点击左上角的返回
                    SPUtils.putString(context, "SwitchAccountSuccess", "1"); // 没有切换
                    //                    wxUtils.adbClick(0, 36, 90, 108);//点击左上角的返回
                    NodeUtils.clickNode("返回", "com.tencent.mm:id/d5i");//点击左上角的返回
                    return;
                }
                if (sendAccountType == 1) {
                    SPUtils.putString(context, "WxAccountLocation", "1"); //切换后的账号在右边
                    wxUtils.adbClick(96, 457, 192, 553);
                    SPUtils.putString(context, "SwitchAccountSuccess", "2"); // 切换成功
                }
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            if (listXY.get(0) == 110) {
                if (sendAccountType == 1) {
                    SPUtils.putString(context, "AccountIsOnlyOne", "2");
                    SPUtils.putString(context, "WxAccountLocation", "1"); //目前的账号在左边
                } else {
                    SPUtils.putString(context, "AccountIsOnlyOne", "1");  //失败
                    SPUtils.putString(context, "WxAccountLocation", "1"); //目前的账号在右边
                }
            } else if (listXY.get(0) == 302) {
                if (sendAccountType == 1) {
                    SPUtils.putString(context, "AccountIsOnlyOne", "1");//失败
                    SPUtils.putString(context, "WxAccountLocation", "2"); //目前的账号在右边
                } else {
                    SPUtils.putString(context, "AccountIsOnlyOne", "2");
                    SPUtils.putString(context, "WxAccountLocation", "2"); //目前的账号在右边
                }
            }

        }

    }

    // 获取目前的微信账号
    private void getUsingWxAccount() {
        backHome();
        wxUtils.openWx((Activity) context);
        int accountNum = 0;
        wxUtils.adbClick(411, 822, 429, 847);// 点击右下角的我
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("相册") || !xmlData.contains("收藏")) {
            return;
        } else {
            List<String> ud = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < ud.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ud.get(a)).getNode();
                if (nodeBean != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().equals("com.tencent.mm:id/cdh")) && nodeBean.getText() != null && nodeBean.getText().contains("微信号")) {
                    String str = nodeBean.getText();
                    String wxAccount = str.replaceAll("微信号：", "");
                    SPUtils.putString(context, "wxAccount", wxAccount);
                    break;
                }
            }
        }

    }


    /**
     * 微信账号切换 6.6.3 版本  新号老号都发送
     */

    private void switchWxAccount2() {
        SPUtils.putString(context, "SwitchAccountSuccess", "0");
        int sendAccountType = SPUtils.getInt(context, "is_accType", 0);//1为新号 2为老号 3为全部
        if (sendAccountType != 3) {
            return;
        }
        wxUtils.openWx((Activity) context);
        boolean flag0 = true;
        int accountNum = 0;
        while (flag0) {
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            if (xmlData.contains("wx助手") || (xmlData.contains("应用") && xmlData.contains("主屏幕"))) {
                flag0 = false;
            } else if (!(xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adb("input keyevent 4");//返回
            } else if ((xmlData.contains("通讯录") && xmlData.contains("发现"))) {
                wxUtils.adbClick(200, 800, 200, 800);//点击通讯录
                flag0 = false;
            }
        }
        wxUtils.adbClick(411, 822, 429, 847);//点击我
        wxUtils.adbUpSlide(context);
        wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.setting.ui.setting.SettingsUI");
        wxUtils.adbUpSlide(context);
        wxUtils.adbClick(21, 681, 459, 714); //点击切换账号
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < nodeList.size() - 1; i++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
            if (nodeBean != null && nodeBean.getText() != null && !nodeBean.getText().isEmpty() && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/d5s")) {
                accountNum++;
            }
        }
        for (int i = 0; i < nodeList.size() - 1; i++) {
            nodeBean = wxUtils.getNodeXmlBean(nodeList.get(i)).getNode();
            if (nodeBean != null && nodeBean.getText() != null && nodeBean.getText().equals("当前使用") && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/d5v")) {
                listXY = wxUtils.getXY(nodeBean.getBounds());//获取 当前使用的坐标
                break;
            }
        }
        if (accountNum == 2) {  //老号在左边  新号在右边

            //说明已经登录了两个账号
            if (listXY.get(0) == 110) {
                //正在使用的账号 在左边（新号）， 点击右边的账号切换
                wxUtils.adbClick(288, 457, 384, 553);
                SPUtils.putString(context, "WxAccountLocation", "2"); //点击切换的账号后在右边
                SPUtils.putString(context, "SwitchAccountSuccess", "2");
            } else if (listXY.get(0) == 302) {
                //正在使用的账号 在右边(老号)， 点击左边的账号切换
                wxUtils.adbClick(96, 457, 192, 553);
                SPUtils.putString(context, "WxAccountLocation", "1"); //点击切换后的账号在左边
                SPUtils.putString(context, "SwitchAccountSuccess", "2");
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        wxUtils.adbClick(0, 36, 90, 108);//点击左上角的返回
        //        backHome();
        //        wxUtils.adbClick(411, 822, 429, 847);// 点击右下角的我
        //        xmlData = wxUtils.getXmlData();
        //        if (!xmlData.contains("相册") || !xmlData.contains("收藏")) {
        //            return;
        //        } else {
        //            List<String> ud = wxUtils.getNodeList(xmlData);
        //            for (int a = 0; a < ud.size(); a++) {
        //                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(ud.get(a)).getNode();
        //                if (nodeBean != null && nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/cdh") && nodeBean.getText() != null && nodeBean.getText().contains("微信号")) {
        //                    String str = nodeBean.getText();
        //                    String wxAccount = str.replaceAll("微信号：", "");
        //                    SPUtils.putString(context, "wxAccount", wxAccount);
        //                    break;
        //
        //                }
        //            }
        //        }
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
        }
    }

    String markData = "";

    /**
     * 微信发单转发
     *
     * @param flockName
     * @param messageNum
     * @param isInit
     */
    private void transmitMessageFlock(String flockName, int messageNum, boolean isInit) {
        if (StringUtils.isEmpty(flockName) || !(flockName != null && messageNum > 0)) {
            return;
        }
        if (isInit) {
            markData = "";
        } else {
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y88, R.dimen.x296, R.dimen.y123);//群聊


            xmlData = wxUtils.getXmlData();
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().contains("com.tencent.mm:id/a9u") && flockName.equals(nodeBean.getText())) {//到指定群取数据
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    break;
                }
            }
        }


        w:
        while (true) {

            while (true) {
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    //                    ShowToast.show("任务被中断", (Activity) context);
                    wxUtils.openWx((Activity) context);
                    backHome();
                    transmitMessageFlock(flockName, messageNum, false);
                    return;
                } else if (xmlData.contains("微信无响应。要将其关闭吗？")) {
                    wxUtils.adbDimensClick(context, R.dimen.x220, R.dimen.y219, R.dimen.x284, R.dimen.y252);//确定
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (!(xmlData.contains("当前所在页面,与") && xmlData.contains("聊天信息"))) {
                    wxUtils.adb("input keyevent 4");
                } else if ((xmlData.contains("当前所在页面,与") && xmlData.contains("聊天信息") && xmlData.contains("发送邮件") && xmlData.contains("分享"))) {
                    wxUtils.adb("input keyevent 4");
                } else if (xmlData.contains("当前所在页面,与") && xmlData.contains("聊天信息") && !xmlData.contains(flockName)) {
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
                if ("com.tencent.mm:id/aec".equals(nodeBean.getResourceid())) {//消息是图片
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    break;
                } else if ("com.tencent.mm:id/ji".equals(nodeBean.getResourceid())) {//消息是文字
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adb("input swipe " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + ((listXY.get(0) + listXY.get(2)) / 2) + " " + ((listXY.get(1) + listXY.get(3)) / 2) + " " + 1000);  //长按EdiText
                    //                    wxUtils.adb("input swipe 200 607 200 315");//向下滑动
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("复制") && xmlData.contains("收藏") && xmlData.contains("更多")) {
                for (int a = nodeList.size(); a > 0; a--) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                    if ("更多".equals(nodeBean.getText())) {//更多
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
                if (xmlData.contains("分享") && xmlData.contains("发送邮件")) {//判断是否在转发页面
                    for (int a = nodeList.size(); a > 0; a--) {
                        if (count >= messageNum) {
                            break wh;
                        }
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                        if ("com.tencent.mm:id/x".equals(nodeBean.getResourceid()) && !nodeBean.isChecked()) {//选中....
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
            if (xmlData.contains("分享") && xmlData.contains("发送邮件")) {//转发id
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y367, R.dimen.x80, R.dimen.y400);//转发
                wxUtils.adbDimensClick(context, R.dimen.x40, R.dimen.y175, R.dimen.x280, R.dimen.y209);//转发

            } else {
                LogUtils.d("不在转发页面");
                continue w;
            }
            while (true) {//判断是否或
                xmlData = "";
                xmlData = wxUtils.getXmlData();
                if (StringUtils.isEmpty(xmlData)) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else if (xmlData.contains("逐条转发") || xmlData.contains("你当前的网络可能存在问题") || xmlData.contains("wx助手") || (xmlData.contains("主屏幕") && xmlData.contains("应用"))) {
                    //                    LogUtils.showLargeLog(xmlData,2000,"zplh");
                    continue w;
                } else if (xmlData.contains("创建新聊天")) {
                    break;
                }
            }


            if (xmlData.contains("创建新聊天")) {
                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//多选
                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("更多联系人")) {
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
            nodeList = wxUtils.getNodeList(xmlData);
            if (xmlData.contains("选择群聊") && xmlData.contains("确定")) {
                for (int a = nodeList.size(); a > 0; a--) {
                    if (countClick >= 9 || a <= 4) {
                        break;
                    }
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                    NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(nodeList.get(a - 3)).getNode();
                    if ("com.tencent.mm:id/sf".equals(nodeBean.getResourceid()) && !nodeBean.isChecked() && nodeBean1.getText() != null && isFlockMark(nodeBean1.getText()) && !markData.contains(nodeBean1.getText()) && !flockName.equals(nodeBean1.getText())) {//更多
                        listXY = wxUtils.getXY(nodeBean.getBounds());
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击选择
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
                if (!xmlData.contains("选择群聊")) {
                    continue w;
                }

                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//确认
                xmlData = wxUtils.getXmlData();
                if (!(xmlData.contains("更多联系人") && xmlData.contains("发送"))) {
                    continue w;
                }
                wxUtils.adbDimensClick(context, R.dimen.x244, R.dimen.y23, R.dimen.x308, R.dimen.y45);//发送

            } else if (countClick == 0) {
                LogUtils.e("发单任务完成");
                //                SPUtils.putString(context, "alipaySendMessageData", "");//保存数据
                break w;
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("发送给") && xmlData.contains("com.tencent.mm:id/all")) { //TODO
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = nodeList.size(); a > 0; a--) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                    if ("com.tencent.mm:id/all".equals(nodeBean.getResourceid())) {//发送
                        listXY = wxUtils.getXY(nodeBean.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击发送

                        markData = markData + countData + ",";//记录发过的
                        SPUtils.putString(context, "alipaySendMessageData", markData);//保存数据

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        while (true) {//判断是否在转发群页面
                            xmlData = wxUtils.getXmlData();
                            if (xmlData.contains("当前所在页面,与") && xmlData.contains("聊天信息")) {
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
        /*Pattern p = Pattern.compile("^[\u4e00-\u9fa5]+[a-zA-Z][0-9]{6}$");//中文开头 +  1个英文  +  6个数字结尾
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;*/

        return true;
    }

    private int article_num_e;
    private int article_num_s;
    private int slip_time_e;
    private int slip_time_s;
    private int read_time_e;
    private int read_time_s;

    /**
     * 设置公众号数据
     *
     * @param article_num_e
     * @param article_num_s
     * @param slip_time_e
     */
    public void setPublicMark(int article_num_e, int article_num_s, int slip_time_e, int slip_time_s, int read_time_e, int read_time_s) {
        this.article_num_e = article_num_e;
        this.article_num_s = article_num_s;
        this.slip_time_e = slip_time_e;
        this.slip_time_s = slip_time_s;
        this.read_time_e = read_time_e;
        this.read_time_s = read_time_s;
    }

    /**
     * 添加公众号 (搜索添加)
     *
     * @param publicMark
     */
    private void addPublicMark(String publicMark) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        cm.setText(publicMark);

        while (true) {
            wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y17, R.dimen.x320, R.dimen.y50);//更多
            wxUtils.adbDimensClick(context, R.dimen.x108, R.dimen.y85, R.dimen.x308, R.dimen.y118);//添加朋友
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("添加朋友") && xmlData.contains("公众号")) {
                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y322, R.dimen.x320, R.dimen.y366);//公众号
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int x = context.getResources().getDimensionPixelSize(R.dimen.x197);
                int y = context.getResources().getDimensionPixelSize(R.dimen.y34);//EdiText
                wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1000);  //长按EdiText
                wxUtils.adbDimensClick(context, R.dimen.x91, R.dimen.y83, R.dimen.x91, R.dimen.y83);//粘贴
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y64, R.dimen.x320, R.dimen.y64);//点击搜索出来的

                wxUtils.adbDimensClick(context, R.dimen.x293, R.dimen.y380, R.dimen.x293, R.dimen.y380);//点击搜索出来的
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y114, R.dimen.x320, R.dimen.y114);//点击搜索出来的
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部

                xmlData = wxUtils.getXmlData();
                if (xmlData.contains("关注")) {
                    List<String> list = wxUtils.getNodeList(xmlData);
                    for (int a = list.size(); a > 0; a--) {
                        NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(a - 1)).getNode();
                        if ("关注".equals(nodeBean.getText())) {
                            listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//关注
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                } else {
                    return;
                }

            } else {
                backHome();
                continue;
            }
        }
    }

    /**
     * 添加公众号 (其他添加)
     *
     * @param publicMark
     */
    private void addPublicMarkElseFlock(String publicMark) {

        //进入群聊
        while (true) {
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊
            xmlData = wxUtils.getXmlData();

            if (xmlData.contains("新群聊") && xmlData.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                backHome();
                return;
            } else if (xmlData.contains("新群聊")) {
                break;
            } else {
                backHome();
                continue;
            }
        }

        //进入指定群
        w:
        while (true) {
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getResourceid() != null && (nodeBean.getResourceid().contains("com.tencent.mm:id/a9u"))) {
                    if (publicMark.equals(nodeBean.getText())) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                        break w;
                    }

                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData) && xmlData.contains("个群聊")) {
                wxUtils.adb("input keyevent 4");
                ShowToast.show("没有对应群，任务结束", (Activity) context);
                return;
            }
        }

        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("当前所在页面,与" + publicMark)) {
            backHome();
            addPublicMarkElseFlock(publicMark);
            return;
        } else {

            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = nodeList.size(); a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();

                if ("com.tencent.mm:id/ad8".equals(nodeBean.getResourceid())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//点击最后一条信息
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    break;
                }

            }
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("当前所在页面,与" + publicMark)) {
            return;
        } else if (xmlData.contains("进入公众号")) {//点击了名片进入到了  关注页面（关注过）
            ShowToast.show("已关注过，任务结束", (Activity) context);
            return;
        } else if (xmlData.contains("关注")) {//点击了名片进入到了  关注页面（未关注）
        } else if (xmlData.contains("QQ浏览器X5内核提供技术支持")) {//点击了链接
            wxUtils.adbDimensClick(context, R.dimen.x136, R.dimen.y100, R.dimen.x136, R.dimen.y100);//点击公众号
        } else if (xmlData.contains("com.tencent.mm:id/ws")) {//点击了图片

            int x = context.getResources().getDimensionPixelSize(R.dimen.x160);
            int y = context.getResources().getDimensionPixelSize(R.dimen.y200);//长按图片
            wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 2000);  //长按图片
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlData = wxUtils.getXmlData();
            if (xmlData.contains("识别图中二维码")) {
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = nodeList.size(); a > 0; a--) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();

                    if ("识别图中二维码".equals(nodeBean.getText())) {
                        listXY = wxUtils.getXY(nodeBean.getBounds());//点击识别图中二维码
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击识别图中二维码
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            } else {
                return;
            }
        }

        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("关注") && xmlData.contains("详细资料")) {
            List<String> list = wxUtils.getNodeList(xmlData);
            for (int a = list.size(); a > 0; a--) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(list.get(a - 1)).getNode();
                if ("关注".equals(nodeBean.getText())) {
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//关注
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        } else {
            return;
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());

    /**
     * 浏览公众号
     */
    private void publicMarkRead() {

        String day = sdf.format(System.currentTimeMillis());
        LogUtils.d(day);

        //清空不是今天的数据
        LogUtils.d("删除数据库" + DataSupport.deleteAll(PublicMarkBean.class, "publicDay != ?", day));

        //查询今天的数据
        List<PublicMarkBean> publicMarkBeanList = DataSupport.where("publicDay = ?", day).find(PublicMarkBean.class);
        LogUtils.d("公众号长度" + publicMarkBeanList.size());


        //进入公众号
        while (true) {
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
            wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y161, R.dimen.x320, R.dimen.y196);//群聊
            xmlData = wxUtils.getXmlData();

            if (xmlData.contains("0个公众号")) {
                backHome();
                return;
            } else if (xmlData.contains("当前所在页面,公众号")) {
                break;
            } else {
                backHome();
                continue;
            }
        }

        int pmNum = 0;//公众号总数

        //查看有多少
        w:
        while (true) {
            wxUtils.adb("input swipe 200 700 200 200 50");//滑动到底部
            xmlData = wxUtils.getXmlData();

            if (!xmlData.contains("当前所在页面,公众号")) {
                return;
            }

            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (!StringUtils.isEmpty(nodeBean.getText()) && nodeBean.getText().contains("公众号") && "com.tencent.mm:id/ag4".equals(nodeBean.getResourceid())) {
                    pmNum = Integer.valueOf(nodeBean.getText().replace("个公众号", ""));
                    LogUtils.d("公众号数量:" + pmNum);
                    break w;
                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            xmlData = wxUtils.getXmlData();

            if (xmlData.equals(strXmlData) && xmlData.contains("个公众号")) {
                wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
                break w;
            }
        }

        //判断有没有公众号和判断是否所有公众号今天都浏览了
        if (pmNum <= 0 || publicMarkBeanList.size() >= pmNum) {
            return;
        }

        wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部


        //查看有多少
        w:
        while (true) {
            xmlData = wxUtils.getXmlData();
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getText() != null && nodeBean.getResourceid().contains("com.tencent.mm:id/sx")) {//公众号id

                    int r = random.nextInt(pmNum);
                    LogUtils.d(nodeBean.getText() + "随机数" + r);
                    if (r != 0) {
                        continue;
                    }
                    //查询当前公众号今天是否浏览过
                    List<PublicMarkBean> publicMarkBeans = DataSupport.where("publicMark = ? and publicDay = ?", nodeBean.getText(), day).find(PublicMarkBean.class);
                    if (publicMarkBeans.size() > 0) {
                        LogUtils.d("今天浏览过了" + publicMarkBeans.size());
                        continue;
                    }

                    //保存公众号和浏览日期
                    PublicMarkBean publicMarkBean = new PublicMarkBean();
                    publicMarkBean.setPublicMark(nodeBean.getText());
                    publicMarkBean.setPublicDay(day);
                    LogUtils.e("添加数据库" + publicMarkBean.save() + "公众号是:" + nodeBean.getText() + "___日期是" + day);


                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取群坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击进入群
                    xmlData = wxUtils.getXmlData();
                    if (xmlData.contains("微信不能确定你的位置") || xmlData.contains("要求使用你的地理位置")) {
                        wxUtils.adb("input keyevent 4");
                    }
                    break w;
                }
            }

            String strXmlData = xmlData;
            wxUtils.adbUpSlide(context);//向上滑动
            xmlData = wxUtils.getXmlData();
            if (xmlData.equals(strXmlData) && xmlData.contains("个公众号")) {
                wxUtils.adb("input swipe 200 300 200 700 50");//滑动到顶部
            }
        }
        int count = 0;
        int readNum = random.nextInt(article_num_e - article_num_s + 1) + article_num_s;
        LogUtils.d("浏览" + readNum + "篇");
        ShowToast.show("浏览" + readNum + "篇", (Activity) context);
        xmlData = wxUtils.getXmlData();
        wh:
        while (true) {
            if (count >= readNum) {
                break wh;
            }
            if (xmlData.contains("聊天信息") && (xmlData.contains("服务按钮") || xmlData.contains("消息")) && (xmlData.contains("com.tencent.mm:id/a43") || xmlData.contains("com.tencent.mm:id/fa"))) {
                nodeList = wxUtils.getNodeList(xmlData);
                for (int a = nodeList.size(); a > 0; a--) {
                    if (count >= readNum) {
                        break wh;
                    }
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a - 1)).getNode();
                    if ("com.tencent.mm:id/a43".equals(nodeBean.getResourceid()) || "com.tencent.mm:id/fa".equals(nodeBean.getResourceid())) {

                        listXY = wxUtils.getXY(nodeBean.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//进入公众号
                        count++;
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //设置滑动次数
                        if (slip_time_e <= 0 || ((slip_time_e - slip_time_s) <= 0)) {
                            slip_time_e = 3;
                            slip_time_s = 1;
                        }
                        int slip_time = random.nextInt(slip_time_e - slip_time_s + 1) + slip_time_s;
                        LogUtils.d("end=" + slip_time_e + "__start=" + slip_time_s + "___间隔随机数=" + slip_time);
                        ShowToast.show("滑动" + slip_time + "次", (Activity) context);
                        LogUtils.d("滑动次数" + slip_time);
                        for (int b = 0; b < slip_time; b++) {
                            wxUtils.adb("input swipe 200 630 200 200");//向下滑动

                            //设置滑动次数
                            if (read_time_e <= 0 || ((read_time_e - read_time_s) <= 0)) {
                                read_time_e = 60;
                                read_time_s = 10;
                            }
                            int timeSleep = random.nextInt(read_time_e - read_time_s + 1) + read_time_s;
                            LogUtils.d("end=" + read_time_e + "__start=" + read_time_s + "___间隔随机数=" + timeSleep);
                            ShowToast.show("阅读" + timeSleep + "秒", (Activity) context);
                            try {
                                Thread.sleep(timeSleep * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //TODO 点击广告
                        wxUtils.adbDimensClick(context, R.dimen.x15, R.dimen.y329, R.dimen.x304, R.dimen.y387);//点击广告
                        try {
                            Thread.sleep(random.nextInt(5) + 5 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        w:
                        while (true) {//回到公众号
                            xmlData = wxUtils.getXmlData();

                            if (xmlData.contains("地理位置授权")) {
                                List<String> ud = wxUtils.getNodeList(xmlData);
                                for (int aa = 0; aa < ud.size(); aa++) {
                                    NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(ud.get(aa)).getNode();
                                    if (nodeBean1.getText() != null && nodeBean1.getText().contains("取消")) {
                                        listXY = wxUtils.getXY(nodeBean1.getBounds());//取消
                                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//取消
                                        continue w;
                                    }
                                }
                            }


                            if (!(xmlData.contains("聊天信息") && (xmlData.contains("服务按钮") || xmlData.contains("消息")) && (xmlData.contains("com.tencent.mm:id/a43") || xmlData.contains("com.tencent.mm:id/fa")))) {
                                //                               wxUtils.adb("input keyevent 4");
                                wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y17, R.dimen.x48, R.dimen.y50);//点击左上角返回键
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //先关闭，广告弹窗
                                if (xmlData.contains("你要关闭购物页面?")) {
                                    //wxUtils.adbClick(300, 490, 396, 535);

                                    wxUtils.adbDimensClick(context, R.dimen.x200, R.dimen.y230, R.dimen.x264, R.dimen.y250);//点击关闭
                                }
                                SystemClock.sleep(2000);
                                //关闭 浏览器X5内核  安装请求
                                if (xmlData.contains("浏览器X5内核")) {
                                    wxUtils.adbDimensClick(context, R.dimen.x136, R.dimen.y230, R.dimen.x200, R.dimen.y251);//点击取消 204，490,300,535
                                }
                                SystemClock.sleep(2000);
                            } else {
                                break;
                            }
                        }

                    }
                }

            } else {
                ShowToast.show("任务结束", (Activity) context);
                LogUtils.d("没有文章");
                count++;
                wxUtils.adb("input keyevent 4");
                continue;
            }

            String xd = xmlData;
            wxUtils.adb("input swipe 200 200 200 630");//向下滑动
            xmlData = wxUtils.getXmlData();
            if (xd.equals(xmlData)) {
                break;
            }
        }

        backHome();

    }

    /**
     * 加粉回复
     */
    private void addFriendsReturn() {
        System.out.println("" + reply_msg);
        wxUtils.openWx((Activity) context);
        backHome();
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
        wxUtils.adbDimensClick(context, R.dimen.x14, R.dimen.y51, R.dimen.x296, R.dimen.y87);//新的朋友
        int count = 0;
        while (count < 3) {
            count++;
            xmlData = wxUtils.getXmlData();
            if (!(xmlData.contains("添加朋友") && xmlData.contains("新的朋友"))) {
                ShowToast.show("任务完成", (Activity) context);
                return;
            }
            nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                if (a < 4) {
                    continue;
                }
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                NodeXmlBean.NodeBean nodeBean1 = wxUtils.getNodeXmlBean(nodeList.get(a - 2)).getNode();
                if (nodeBean.getResourceid() != null && "com.tencent.mm:id/b8j".equals(nodeBean.getResourceid()) && nodeBean.getText() != null && nodeBean.getText().equals("添加")) {
                    if (!StringUtils.isEmpty(nodeBean1.getText()) && !nodeBean1.getText().contains("您是京东挑选的优质用户") && !nodeBean1.getText().contains("手机联系人")) {
                        listXY = wxUtils.getXY(nodeBean1.getBounds());//
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//

                        //进入详细资料界面
                        xmlData = wxUtils.getXmlData();
                        if (reply_msg == null) {
                            reply_msg = "你好";
                        }
                        if (!xmlData.contains("详细资料") && !xmlData.contains("设置备注和标签") || xmlData.contains(reply_msg) || xmlData.contains("滚") || xmlData.contains("病")
                                || xmlData.contains("傻逼") || xmlData.contains("妈") || xmlData.contains("死") || xmlData.contains("智障") || xmlData.contains("白痴")
                                ) {//判断下是否已经回复了，回复了就返回
                            wxUtils.adb("input keyevent 4");//返回
                            continue;
                        }
                        List<String> returnList = wxUtils.getNodeList(xmlData);
                        String newFriends = "";
                        for (int ccc = 0; ccc < returnList.size(); ccc++) {
                            NodeXmlBean.NodeBean nodeBean_ccc = wxUtils.getNodeXmlBean(returnList.get(ccc)).getNode();
                            if (nodeBean_ccc != null && nodeBean_ccc.getText() != null && nodeBean_ccc.getResourceid() != null && nodeBean_ccc.getResourceid().equals("com.tencent.mm:id/pl")) {
                                newFriends = nodeBean_ccc.getText();
                                break;
                            }
                        }

                        int jjj = 0;
                        for (int eee = returnList.size() - 5; eee > 0; eee--) {
                            NodeXmlBean.NodeBean nodeBean_eee = wxUtils.getNodeXmlBean(returnList.get(eee)).getNode();
                            if (nodeBean_eee != null && nodeBean_eee.getText() != null && nodeBean_eee.getResourceid() != null
                                    && nodeBean_eee.getResourceid().equals("com.tencent.mm:id/b8z")) {
                                if (nodeBean_eee.getText().equals(newFriends)) {
                                    jjj++;
                                }
                            }
                        }

                        if (jjj == 2 || jjj == 3) {
                            wxUtils.adb("input keyevent 4");//返回
                            continue;
                        }
                        int kkk = 0;
                        for (int ddd = 0; ddd < returnList.size(); ddd++) {
                            NodeXmlBean.NodeBean nodeBean_ddd = wxUtils.getNodeXmlBean(returnList.get(ddd)).getNode();
                            if (nodeBean_ddd != null && nodeBean_ddd.getText() != null && nodeBean_ddd.getText().contains("我:")) {
                                kkk++;
                                break;
                            }
                        }
                        if (kkk == 2 || kkk == 3) {
                            wxUtils.adb("input keyevent 4");//返回
                            continue;
                        }
                        String newFriends2 = "";
                        for (int eee = returnList.size() - 5; eee > 0; eee--) {
                            NodeXmlBean.NodeBean nodeBean_eee = wxUtils.getNodeXmlBean(returnList.get(eee)).getNode();
                            if (nodeBean_eee != null && nodeBean_eee.getText() != null && nodeBean_eee.getResourceid() != null
                                    && nodeBean_eee.getResourceid().equals("com.tencent.mm:id/b8z")) {
                                newFriends2 = nodeBean_eee.getText();
                                break;
                            }
                        }
                        if (newFriends2.contains("我")) {
                            wxUtils.adb("input keyevent 4");//返回
                            continue;
                        }

                        for (int b = 0; b < returnList.size(); b++) {
                            NodeXmlBean.NodeBean nodeBean2 = wxUtils.getNodeXmlBean(returnList.get(b)).getNode();
                            if (nodeBean2.getResourceid() != null && "com.tencent.mm:id/b90".equals(nodeBean2.getResourceid()) && nodeBean2.getText() != null && nodeBean2.getText().equals("回复")) {
                                listXY = wxUtils.getXY(nodeBean2.getBounds());//
                                wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//
                                //点击了回复
                                ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                //cm.setText("您是京东挑选的优质用户，我们是免费为您发放京东优惠券的！");
                                if (!TextUtils.isEmpty(reply_msg)) {//不为null，服务器给了 数据。需要设置，然后回复给刚刚加的好友
                                    cm.setText(reply_msg);
                                    //                                    int x = context.getResources().getDimensionPixelSize(R.dimen.x160);
                                    //                                    int y = context.getResources().getDimensionPixelSize(R.dimen.y122);//EdiText
                                    //                                    wxUtils.adb("input swipe " + x + " " + y + " " + x + " " + y + " " + 1500);  //长按EdiText
                                    wxUtils.adb("input swipe " + 150 + " " + 260 + " " + 200 + " " + 265 + " " + 2000);  //长按EdiText
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    wxUtils.adbClick(100, 215, 110, 220);//点击粘贴
                                    //重新加载页面，计算出 确定 的位置
                                    xmlData = wxUtils.getXmlData();
                                    List<String> nodeList3 = wxUtils.getNodeList(xmlData);
                                    //                                    Log.d("zhangshuai", "nodeList3" + nodeList3);
                                    for (int c = 0; c < nodeList3.size(); c++) {
                                        NodeXmlBean.NodeBean nodeBean3 = wxUtils.getNodeXmlBean(nodeList3.get(c)).getNode();
                                        if (nodeBean3.getResourceid() != null && "com.tencent.mm:id/all".equals(nodeBean3.getResourceid()) && nodeBean3.getText() != null && nodeBean3.getText().equals("确定")) {
                                            listXY = wxUtils.getXY(nodeBean3.getBounds());
                                            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));
                                            break;
                                        }

                                    }
                                }
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wxUtils.adb("input keyevent 4");
                                break;
                            }
                        }

                    }
                }
            }
            wxUtils.adbUpSlide(context);//向上滑动
        }
    }

}
