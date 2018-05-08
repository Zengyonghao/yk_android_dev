package com.zplh.zplh_android_yk.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.bean.AlipayAccountFlockClickBean;
import com.zplh.zplh_android_yk.bean.AlipayAccountFlockNumBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.conf.ZFB_URLS;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.ui.view.OperationView;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;
import com.zplh.zplh_android_yk.utils.StringUtils;

import org.xutils.http.RequestParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lichun on 2017/7/3.
 * Description:操作支付宝
 */

public class OperationAlipayTestPresenter extends BasePresenter<OperationView> {
    private OperationView operationView;
    private Context context;
    private WxUtils wxUtils = new WxUtils();
    private String xmlData;
    private List<Integer> listXY;
    private List<AlipayAccountFlockNumBean.AccountBean> accountBeanList;//群成员
    private List<AlipayAccountFlockClickBean.AccountBean> clickAccountBenaList;//拉群点击数据
    Gson gson = new Gson();
    private PackageManager mPackageManager;

    public OperationAlipayTestPresenter(Context context, OperationView operationView) {
        this.operationView = operationView;
        this.context = context;
        mPackageManager = context.getPackageManager();
        LogUtils.e(isInstallApp(context,"com.eg.android.AlipayGphone")+"支付宝是否安装");
    }

   private boolean isInstallApp(Context context,String packageName)
    {
        try {
            mPackageManager.getApplicationInfo(packageName,PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO: handle exception
            return false;
        }
    }

    private int time;

    /**
     * 支付宝执行任务.先判断
     *
     * @param task
     */
    public void task(int task) {

        if(!isInstallApp(context,"com.eg.android.AlipayGphone")){

            return;
        }

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
            ShowToast.show("请先登录支付宝", (Activity) context);
        } else {
            if (xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的")) {//判断是否在微信主界面
                time = 0;
                boolean switchFlag = true;
                astrict = true;
                switch (task) {
                    case -1://加好友
                        break;
                    case -2://拉群
                        AlipayAccountFlockClickBean alipayAccountFlockClickBean = new AlipayAccountFlockClickBean();
                        alipayAccountFlockClickBean.setUid(SPUtils.getString(context, "uid", "0000"));
                        clickAccountBenaList = new ArrayList<>();
                        while (switchFlag || switchAccount()) {
                            switchFlag = false;
                            getName_zfb();//获取支付宝账号
                            startAlterName();//修改备注
                            if (astrict)//判断支付宝账号是否被限制
                                addCrowd();//拉群
                        }
                        alipayAccountFlockClickBean.setAccount(clickAccountBenaList);
                        String strClick = gson.toJson(alipayAccountFlockClickBean);
                        LogUtils.e(strClick);
                        updata_group_member_count(0,strClick);
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
                        updata_group_member_count(1,str);
                        break;


                }
            } else {//不在微信主界面，跳转到主界面 TODO
                /*wxUtils.adb("am force-stop " + "com.eg.android.AlipayGphone");
                ShowToast.show("支付宝重启中...", (Activity) context);
                wxUtils.openAliPay();//打开支付宝
                time = time + 6000;
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task(task);*/

                backHome();
                time = 1000;
                task(task);
            }
        }
    }

    /**
     * 上传群数据
     * @param json
     * @param type 0上传群点击数  1上传群成员
     */
    private void updata_group_member_count(int type,String json) {
        RequestParams params=null;
        if (type==0){
            params = new RequestParams(ZFB_URLS.updata_group_rquest_count());
            params.addQueryStringParameter("json", json);
            LogUtils.d(ZFB_URLS.updata_group_rquest_count() + "?json=" + json);
        }else if(type==1){
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

    public void downApk(String s){

    }

    String logId = "0";
    int taskId = 0;
    String ali_add_num;
    /**
     * 设置logId
     *
     * @param logId
     */
    public void setLogId(String logId, int taskId,String ali_add_num) {
        this.logId = logId;
        this.taskId = taskId;
        this.ali_add_num=ali_add_num;
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
                            if (sex == 0 || sex == 1) {
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
    int qunMaxNum = 5;//设置群人数
    boolean boyEnd = true;//如果是flase带表男群拉完
    boolean girlEnd = true;//如果是flase带表女群拉完

    /**
     * 拉群
     */
    private void addCrowd() {
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
                                addMember(sex, qb);

                                AlipayAccountFlockClickBean.AccountBean.FlockBean flockBean = new AlipayAccountFlockClickBean.AccountBean.FlockBean();
                                flockBean.setFlock_name(qunFlockName);
                                flockBean.setClick_num(clickCount + "");
                                flockBeanList.add(flockBean);
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
        refuseAdd="";
    }


    int bogCount = 0;//要改名的男好友
    int girlCount = 0;//要改名的男好友
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
                            addMember(sex, qb);
                            break wh;
                        }
                    }
                } else {
                    countStr = countStr + flagCountStr + ",";
                }

                if (qunLiaoBack.contains("群公告") && qunLiaoBack.contains("群聊设置")) {
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
    }


    private NodeXmlBean.NodeBean nodeBean;
    private boolean astrict = true;

    /**
     * 修改备注.
     */
    private void startAlterName() {
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

                    if (xmlData.contains("该功能暂未对您开放")) {//支付宝被限制 TODO
                        wxUtils.adb("input keyevent 4");
                        LogUtils.e("支付宝账号被限制");
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

                    switch (sex) {//0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl_alipay", 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl + 1);
                            wxUtils.adb("input text \"ZZZ0B" + wx_nume_number_new_girl + "\"");
                            SPUtils.put(context, "wx_name_number_girl_alipay", wx_name_number_girl + 1);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy_alipay", 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy + 1);
                            wxUtils.adb("input text \"ZZZ0A" + wx_nume_number_new_boy + "\"");
                            SPUtils.put(context, "wx_name_number_boy_alipay", wx_name_number_boy + 1);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c_alipay", 0);
                            String wx_nume_number_c = df.format(wx_name_number_c + 1);
                            wxUtils.adb("input text \"ZZZ0C" + wx_nume_number_c + "\"");
                            SPUtils.put(context, "wx_name_number_c_alipay", wx_name_number_c + 1);
                            break;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
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
     * 返回到支付宝主页面
     */
    private void backHome() {
        xmlData = wxUtils.getXmlData();
        if (!(xmlData.contains("首页") && xmlData.contains("口碑") && xmlData.contains("朋友") && xmlData.contains("我的"))) {//判断是否在微信主界面
            wxUtils.adb("input keyevent 4");
            backHome();
        }
    }

    private int account = 1;

    /**
     * 切换账号
     *
     * @return
     */
    private boolean switchAccount() {
        xmlData = wxUtils.getXmlData();
        backHome();//返回到home
        wxUtils.adbDimensClick(context, R.dimen.x240, R.dimen.y363, R.dimen.x320, R.dimen.y400);//我的
        wxUtils.adbDimensClick(context, R.dimen.x264, R.dimen.y17, R.dimen.x320, R.dimen.y51);//设置
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y65, R.dimen.x320, R.dimen.y99);//账号管理
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y65, R.dimen.x320, R.dimen.y99);//账号切换
        xmlData = wxUtils.getXmlData();
        List<String> datalist = wxUtils.getNodeList(xmlData);
        List<NodeXmlBean.NodeBean> nodeBeanList = new ArrayList<>();
        for (int a = 0; a < datalist.size(); a++) {//获取支付宝账号id
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(datalist.get(a)).getNode();
            if ("com.alipay.mobile.securitybiz:id/table_left_text".equals(nodeBean.getResourceid())) {
                nodeBeanList.add(nodeBean);
            }
        }
        LogUtils.e(nodeBeanList.size() + "个账号");
        if (nodeBeanList.size() > account) {
            listXY = wxUtils.getXY(nodeBeanList.get(nodeBeanList.size() - 1).getBounds());//获取最下面一个坐标
            LogUtils.d(nodeBeanList.get(nodeBeanList.size() - 1).getText());
            wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击坐标
            account++;
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            account = 1;
            LogUtils.d("账号切换完毕...");
            backHome();//返回到home
        }

        return false;
    }

    private String zfbNames = "";//支付宝名称

    private void getName_zfb() {//先判断是否在主页面 TODO
        backHome();//返回到home

        wxUtils.adbDimensClick(context, R.dimen.x267, R.dimen.y367, R.dimen.x293, R.dimen.y395);//点击首页按钮
        wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
        xmlData = wxUtils.getXmlData();
        List<String> zfb_name = wxUtils.getNodeList(xmlData);
        for (int i = 0; i < zfb_name.size(); i++) {
            NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(zfb_name.get(i)).getNode();
            if ("com.alipay.android.phone.wealth.home:id/user_account".equals(nodeBean.getResourceid())) {
                zfbNames = nodeBean.getText();
                LogUtils.d("用户名称是" + zfbNames);
            }
        }
    }

}
