package com.zplh.zplh_android_yk.presenter;

import android.app.Activity;
import android.content.Context;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.ui.view.OperationView;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lichun on 2017/6/14.
 * Description:操作
 */

public class OperationPresenter extends BasePresenter<OperationView> {

    private OperationView operationView;
    private Context context;
    WxUtils wxUtils = new WxUtils();
    private String xmlData;
    private NodeXmlBean.NodeBean nodeBean;
    private List<Integer> listXY;
    private List<String> nodeList;
    private int 

    public OperationPresenter(Context context, OperationView operationView) {
        this.operationView = operationView;
        this.context = context;
    }


    /**
     * 执行任务.先判断
     *
     * @param task
     */
    public void task(int task) {
        wxUtils.openWx((Activity) context);//打开微信
        xmlData = wxUtils.getXmlData();
        if (xmlData.contains("登录") || xmlData.contains("注册") || xmlData.contains("忘记密码")) {//判断是否登录
            ShowToast.show("请先登录微信", (Activity) context);
        } else {
            if (xmlData.contains("通讯录") && xmlData.contains("发现")) {//判断是否在微信主界面
                wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y367, R.dimen.x160, R.dimen.y400);//点击通讯录
                xmlData = wxUtils.getXmlData();
                if (!xmlData.contains("新的朋友")) {//在通讯录界面，但是需要滑动到最顶端
                    wxUtils.adb("input swipe 200 300 200 1000 50");  //滑动到顶部
                    xmlData = wxUtils.getXmlData();
                }
                switch (task) {
                    case 0://修改备注
                        ShowToast.show("修改备注开始", (Activity) context);
                        startAlterName();
                        break;
                    case 1://拉群

                        ShowToast.show("开始拉群开始", (Activity) context);
                        addCrowd();
                        break;
                }
            } else {//不在微信主界面，跳转到主界面 TODO
                ShowToast.show("任务执行失败", (Activity) context);
            }
        }
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
            List<String> nodeList = new ArrayList<>();
            Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
            Matcher matcher = pattern.matcher(xmlData);
            while (matcher.find()) {
                nodeList.add(matcher.group() + "/>");
            }

            for (int a = 0; a < nodeList.size(); a++) {
                NodeXmlBean.NodeBean nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if ("com.tencent.mm:id/a1h".equals(nodeBean.getResourceid())) {
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
                        if ("com.tencent.mm:id/gh".equals(qunNameBean.getResourceid())) {
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
                                continue;
                            }
                        }
                    }
                    //_______________________________________________________________________________________________
                    wxUtils.adb("input keyevent 4");
                    wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y367, R.dimen.x160, R.dimen.y400);//点击通讯录
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
                ShowToast.show("拉群任务完成", (Activity) context);
                break;
            }
        }
        LogUtils.d(bogCount + "___girl" + girlCount);
        //拉完群改名
        ShowToast.show("拉群完，修改备注开始", (Activity) context);
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
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/i_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && nodeBean.getContentdesc().startsWith("ZZZ0")&&countStr.contains(nodeBean.getContentdesc())) {
                    if (nodeBean.getText().contains("A") && boyAlterCount >= bogCount) {
                        continue;
                    } else if (nodeBean.getText().contains("B") && girlAltCount >= girlCount) {
                        continue;
                    }
                    //筛选出好友
                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击好友修改备注
                    xmlData = wxUtils.getXmlData();//重新获取页面数据

                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y135, R.dimen.x320, R.dimen.y166);//点击设置备注和标签
                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字
                    String sexStr = "";
                    if (nodeBean.getText().length() > 4) {
                        char[] ch = nodeBean.getText().toCharArray();
                        ch[3] = '1';
                        sexStr = new String(ch);
                    }
                    wxUtils.adb("input text \"" + sexStr + "\"");
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    if (nodeBean.getText().contains("A")) {
                        boyAlterCount++;
                    } else if (nodeBean.getText().contains("B")) {
                        girlAltCount++;
                    }
                    LogUtils.d(nodeList.get(a));
                    wxUtils.adb("input keyevent 4");
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("发现")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                break w;
            }
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/i_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信")&& nodeBean.getContentdesc().startsWith("ZZZ0")&&countStr.contains(nodeBean.getContentdesc())) {
                    if (nodeBean.getText().startsWith("ZZZ0A") && boyAlterCount < bogCount) {
                        continue w;
                    } else if (nodeBean.getText().startsWith("ZZZ0B") && girlAltCount < girlCount) {
                        continue w;
                    }
                }
            }
            if (!bottom) {
                    wxUtils.adbUpSlide(context);//向上滑动
            }
            endData = xmlData;
            xmlData = wxUtils.getXmlData();//滑动后重新获取页面数据
            if (endData.equals(xmlData)) {
                ShowToast.show("修改备注完成,拉群任务完成", (Activity) context);
                LogUtils.d(boyAlterCount+"修改备注完成,拉群任务完成"+girlAltCount);
                break w;
            }
            if (xmlData.contains("位联系人")) {//判断是否到达底部
                bottom = true;
            }
        }
    }


    int bogCount = 0;//要改名的男好友
    int girlCount = 0;//要改名的男好友
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
                if ("com.tencent.mm:id/j2".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ0")) {
                    NodeXmlBean.NodeBean checkBox = wxUtils.getNodeXmlBean(addNameList.get(a + 1)).getNode();

                    if ("com.tencent.mm:id/n2".equals(checkBox.getResourceid()) && checkBox.isChecked()) {//isChecked true代表选中
                        if (!countStr.contains(nodeBean.getText())) {
                            if (nodeBean.getText().contains("B")) {
                                girlCount++;
                                LogUtils.d(girlCount + "girlCount+选中");
                                countStr = countStr + nodeBean.getText() + ",";
                            } else if (nodeBean.getText().contains("A")) {
                                bogCount++;
                                LogUtils.d(bogCount + "bogCount选中____________________");
                                countStr = countStr + nodeBean.getText() + ",";
                            }
                        }
                    }

                    if (sex == 0 && nodeBean.getText().contains("B")) {//女
                        if ("com.tencent.mm:id/n2".equals(checkBox.getResourceid()) && !checkBox.isChecked()) {
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
                    } else if (sex == 1 && nodeBean.getText().contains("A")) {//男
                        if ("com.tencent.mm:id/n2".equals(checkBox.getResourceid()) && !checkBox.isChecked()) {
                            if (countStr.contains(nodeBean.getText())) {
                                continue;
                            } else {
                                if (clickCount + qb < qunMaxNum) {
                                    listXY = wxUtils.getXY(nodeBean.getBounds());//获取好友坐标
                                    wxUtils.adbClick(listXY.get(0), listXY.get(1), listXY.get(2), listXY.get(3));//点击
                                    countStr = countStr + nodeBean.getText() + ",";
                                    clickCount++;
                                    bogCount++;
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
                    if ("com.tencent.mm:id/j2".equals(nodeBean.getResourceid()) && nodeBean.getText().startsWith("ZZZ1")) {
                        judgeGirl++;
                    }
                } else {//男
                    if ("com.tencent.mm:id/j2".equals(nodeBean.getResourceid()) && (nodeBean.getText().startsWith("ZZZ0B") || nodeBean.getText().startsWith("ZZZ1"))) {
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
        wxUtils.adbDimensClick(context, R.dimen.x80, R.dimen.y367, R.dimen.x160, R.dimen.y400);//点击通讯录
        wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y87, R.dimen.x320, R.dimen.y124);//群聊
    }


    /**
     * 修改备注.
     */
    private void startAlterName() {
        boolean bottom = false;//到了底部
        int sex = 0;//0代表女。   1代表男   2代表性别未知
        DecimalFormat df = new DecimalFormat("0000");
        int zzzNum = 0;//判断是否直接到#号修改
        String endData = "";
        String meName = "";
        w:
        while (true) {
            List<String> nodeList = wxUtils.getNodeList(xmlData);
            for (int a = 0; a < nodeList.size(); a++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(a)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/i_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().startsWith("ZZZ")&&!meName.equals(nodeBean.getContentdesc())) {
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
                    wxUtils.adbDimensClick(context, R.dimen.x1, R.dimen.y135, R.dimen.x320, R.dimen.y166);//点击设置备注和标签
                    wxUtils.adbDimensClick(context, R.dimen.x16, R.dimen.y89, R.dimen.x304, R.dimen.y115);//点击名字EditText
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y89, R.dimen.x312, R.dimen.y115);//清空名字

                    switch (sex) {//0代表女。   1代表男   2代表性别未知
                        case 0:
                            int wx_name_number_girl = (int) SPUtils.get(context, "wx_name_number_girl", 0);
                            String wx_nume_number_new_girl = df.format(wx_name_number_girl);
                            wxUtils.adb("input text \"ZZZ0B" + wx_nume_number_new_girl + "\"");
                            SPUtils.put(context, "wx_name_number_girl", wx_name_number_girl + 1);
                            break;
                        case 1:
                            int wx_name_number_boy = (int) SPUtils.get(context, "wx_name_number_boy", 0);
                            String wx_nume_number_new_boy = df.format(wx_name_number_boy);
                            wxUtils.adb("input text \"ZZZ0A" + wx_nume_number_new_boy + "\"");
                            SPUtils.put(context, "wx_name_number_boy", wx_name_number_boy + 1);
                            break;
                        case 2:
                            int wx_name_number_c = (int) SPUtils.get(context, "wx_name_number_c", 0);
                            String wx_nume_number_c = df.format(wx_name_number_c);
                            wxUtils.adb("input text \"ZZZ0C" + wx_nume_number_c + "\"");
                            SPUtils.put(context, "wx_name_number_c", wx_name_number_c + 1);
                            break;
                    }
                    wxUtils.adbDimensClick(context, R.dimen.x252, R.dimen.y23, R.dimen.x312, R.dimen.y44);//确定修改
                    LogUtils.d(nodeList.get(a));
                    wxUtils.adb("input keyevent 4");
                    break;
                }
            }
            xmlData = wxUtils.getXmlData();//修改完一个重新获取页面数据
            nodeList = wxUtils.getNodeList(xmlData);
            if (!xmlData.contains("发现")) {
                ShowToast.show("任务被中断，结束修改备注任务", (Activity) context);
                break w;
            }
            zzzNum = 0;
            for (int b = 0; b < nodeList.size(); b++) {
                nodeBean = wxUtils.getNodeXmlBean(nodeList.get(b)).getNode();
                if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/i_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && !nodeBean.getContentdesc().startsWith("微信") && !nodeBean.getContentdesc().startsWith("ZZZ") && !meName.equals(nodeBean.getContentdesc())) {
                    continue w;
                } else if (nodeBean.getResourceid() != null && nodeBean.getResourceid().equals("com.tencent.mm:id/i_") && nodeBean.getContentdesc() != null && nodeBean.getContentdesc() != "" && nodeBean.getContentdesc().startsWith("ZZZ")) {
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

}
