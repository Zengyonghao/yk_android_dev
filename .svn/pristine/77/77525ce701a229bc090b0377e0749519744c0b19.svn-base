package com.yk.core.assembly.task_xgbz;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.yk.core.assembly.AbsExecutionProcess;
import com.yk.core.utils.Tools;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.bean.WxFriendsMessageBean;
import com.zplh.zplh_android_yk.conf.ModelConstans;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.utils.AdbBoundsUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.NodeUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;
import com.zplh.zplh_android_yk.utils.StringUtils;

import org.xutils.http.RequestParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Author：liaogulong
 * Time: 2018/5/5/005   11:15
 * Description： 修改备注
 */
public class SuperModifyRemark extends AbsExecutionProcess {
    List<String> nodeList;
    List<Integer> listXY;
    Random random = new Random();// 定义随机类
    private String yunYingMark = "";//运营号

    public SuperModifyRemark(Context context, MessageListBean.ContentBean.DataBean dataBean) {
        super(context, dataBean, "开始修改备注");
    }

    @Override
    public int ExecutionParsing() {
        NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("新的朋友")) {//在通讯录界面，但是需要滑动到最顶端
            NodeUtils.clickNode("通讯录", "com.tencent.mm:id/c_z");//点击通讯录
        }
        if (!TextUtils.isEmpty(dataBean.getParam().getPreRemark()) && dataBean.getParam().getPreRemark().length() == 1)
            return superXiuGaiBeiZhu1(dataBean.getParam().getPreRemark());

        if (!TextUtils.isEmpty(dataBean.getParam().getPreRemark()) && dataBean.getParam().getPreRemark().length() == 2)
            return superXiuGaiBeiZhu0(dataBean.getParam().getPreRemark());

        if (!TextUtils.isEmpty(dataBean.getParam().getPreRemark()) && dataBean.getParam().getPreRemark().length() >= 4 && dataBean.getParam().getRemark().length() >= 4)
            return superXiuGaiBeiZhu2(dataBean.getParam().getPreRemark(), dataBean.getParam().getRemark());

        return EX_FAIL;
    }

    @Override
    public void ExecutionBack() {

    }
    //超级自定义修改备注

    private int superXiuGaiBeiZhu0(String arg0) {
        String meName = "";
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("000");
        String Type = "0000";
        AdbBoundsUtils.searchAndPaste(context, arg0);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUtils.adb("input keyevent 4");//返回
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains(arg0)) {
            ShowToast.show("没有对应的老粉丝帐号", (Activity) context);
            return EX_FAIL;
        }
        xmlData = wxUtils.getXmlData();
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
            if (xmlData.contains(arg0)) {
                for (int a = 0; a < nodeList.size(); a++) {
                    NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                    if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/kq")
                            && nodeBean.getText() != null && nodeBean.getText().startsWith(arg0) && !meName.contains(nodeBean.getText())) {
                        String oldName = "";
                        listXY = wxUtils.getXY(nodeBean.getBounds());//获取  的坐标
                        wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                        bb = 1;
                        meName = meName + nodeBean.getText();
                        oldName = nodeBean.getText();
                        NodeUtils.clickNode("", "com.tencent.mm:id/he"); //右上角人头像
                        Tools.ThreadTools.sleep(1);
                        NodeUtils.clickNode("com.tencent.mm:id/d05"); //左上角人头像
                        Tools.ThreadTools.sleep(1);
                        xmlData = wxUtils.getXmlData();//重新获取页面数据
                        List<String> remarkList = wxUtils.getNodeList(xmlData);
                        if (xmlData.contains("女")) {
                            sex = 0;
                        } else if (xmlData.contains("男")) {
                            sex = 1;
                        } else {
                            sex = 2;
                        }
                        NodeUtils.clickNode("设置备注和标签", "com.tencent.mm:id/anw");
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
                        AdbBoundsUtils.clearRemark();//清空备注
//                            wxUtils.adb("input text " + friendsName + "");
                        wxUtils.adb("input text " + friendsName);
                        NodeUtils.clickNode("完成", "com.tencent.mm:id/hd");
                        //  LogUtils.d(nodeList.get(a));
                        wxUtils.adb("input keyevent 4");//返回
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
        return EX_Next;
    }

    private int superXiuGaiBeiZhu1(String arg0) {
        String oldXmlData = "";
        String zzz = "ZZZ" + arg0 + "0000";
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
            NodeXmlBean.NodeBean nodeBean;
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
                    AdbBoundsUtils.clearRemark();//清空备注
                    Calendar calendar = Calendar.getInstance();
                    String year = (calendar.get(Calendar.YEAR) + "").substring(2, 4);
                    String month = calendar.get(Calendar.MONTH) + 1 + "";
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
                    if (day != null && day.length() == 1) {
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
                    NodeUtils.clickNode("完成", "com.tencent.mm:id/hd");
                    wxUtils.adb("input keyevent 4");
                    xmlData = wxUtils.getXmlData();
                    if (!(xmlData.contains("微信") && xmlData.contains("通讯录") && xmlData.contains("发现") && xmlData.contains("我"))) {
                        wxUtils.adb("input keyevent 4");
                    }

                    //设置间隔时间
                    int start;
                    if (StringUtils.isEmpty(dataBean.getParam().getRemark_interval_time_s())) {
                        start = 3;
                    } else {
                        start = Integer.valueOf(dataBean.getParam().getRemark_interval_time_s());
                    }
                    int end;
                    if (StringUtils.isEmpty(dataBean.getParam().getRemark_interval_time_e())) {
                        end = 6;
                    } else {
                        end = Integer.valueOf(dataBean.getParam().getRemark_interval_time_e());
                    }
                    int timeSleep = random.nextInt(3 - 1 + 1) + start;
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
                    switch (Build.MODEL) {
                        case ModelConstans.tvyk:
                            wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                            break;
                        case ModelConstans.coolpad_8737:
                            wxUtils.adbWxClick(705, 1020);
                            break;

                    }
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
                        switch (Build.MODEL) {
                            case ModelConstans.tvyk:
                                wxUtils.adbClick(460, 772, 460, 772); //直接点击右边侧滑的 #
                                break;
                            case ModelConstans.coolpad_8737:
                                wxUtils.adbWxClick(705, 1058);
                                break;

                        }
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
                    switch (Build.MODEL) {
                        case ModelConstans.tvyk:
                            wxUtils.adbClick(460, 750, 460, 750); //直接点击右边侧滑的 Z
                            break;
                        case ModelConstans.coolpad_8737:
                            wxUtils.adbWxClick(705, 1020);
                            break;

                    }
                }
            }
        }

        return EX_Next;
    }

    private int superXiuGaiBeiZhu2(String arg0, String arg1) {
        String special_id = arg1.substring(3, 4);
        String meName = "";
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("000");
        String Type = "0000";
        AdbBoundsUtils.searchAndPaste(context, arg0);//搜索框长按并粘贴
        Tools.ThreadTools.sleep(3);
        wxUtils.adb("input keyevent 4");//返回
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains(arg0)) {
            ShowToast.show("没有对应的老粉丝帐号", (Activity) context);
            return EX_FAIL;
        }
        xmlData = wxUtils.getXmlData();
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
                        NodeUtils.clickNode("com.tencent.mm:id/he");//酷派
                        NodeUtils.clickNode("com.tencent.mm:id/d05");
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
                        AdbBoundsUtils.clearRemark(); //清空名称
                        Calendar calendar = Calendar.getInstance();
                        String year = (calendar.get(Calendar.YEAR) + "").substring(2, 4);
                        String month = calendar.get(Calendar.MONTH) + 1 + "";
                        if (month.length() == 1) {
                            month = "0" + month;
                        }
                        String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
                        if (day != null && day.length() == 1) {
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
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        NodeUtils.clickNode(null, "com.tencent.mm:id/hd");//确定修改

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
        return EX_Next;
    }


    private void StatisticsWxFriends(String xmlData) {
        List<String> wxFriendsMessage = wxUtils.getNodeList(xmlData);
        String wx_number = null; //微信号
        String wx_name = null;  //昵称
        String wx_location = null;//目前所在地区
        String wx_phone_number = null;//手机电话号码
        String wx_phone_name = null;//手机联系人名称
        String uid = SPUtils.getString(context, "uid", "0000");
        NodeXmlBean.NodeBean nodeBean;
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


}
