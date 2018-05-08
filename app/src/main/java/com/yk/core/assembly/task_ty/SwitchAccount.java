package com.yk.core.assembly.task_ty;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.yk.core.assembly.AbsExecutionProcess;
import com.yk.core.utils.Tools;
import com.yk2_0.utils.AdbUtils;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.conf.ModelConstans;
import com.zplh.zplh_android_yk.utils.NodeUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;

import java.util.List;

/**
 * Author：liaogulong
 * Time: 2018/5/5/005   11:18
 * Description： 切换帐号
 */
public class SwitchAccount extends AbsExecutionProcess {


    public SwitchAccount(Context context, MessageListBean.ContentBean.DataBean _dataBean) {
        super(context, _dataBean, "切换帐号");
    }

    @Override
    public int ExecutionParsing() {
        NodeUtils.clickNode("我", "com.tencent.mm:id/c_z");//点击右下角的我
        xmlData = wxUtils.getXmlData();
        if (!xmlData.contains("相册") || !xmlData.contains("收藏")) {
            return EX_BACK; //重新执行
        }
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
        wxUtils.adbUpSlide(context);
        wxUtils.wxActivityJump("com.tencent.mm/com.tencent.mm.plugin.setting.ui.setting.SettingsUI");
        wxUtils.adbUpSlide(context);
        NodeUtils.clickNode("切换帐号", "android:id/title");
        Tools.ThreadTools.sleep(3); //休眠
        xmlData = wxUtils.getXmlData();
        NodeXmlBean.NodeBean nodeBean;
        List<String> nodeList = wxUtils.getNodeList(xmlData);
        List<Integer> listXY = null;
        int accountNum = 0;
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
        int sendAccountType = SPUtils.getInt(context, "is_accType", 1);//1为新号 2为老号 3为全部
        switch (Build.MODEL) {
            case ModelConstans.coolpad_8737:
                if (accountNum == 2) {
                    //老号在左边  新号在右边
                    //说明已经登录了两个帐号
                    setTaskLog("2个帐号");
                    if (listXY != null && listXY.get(0) == 187) {
                        //正在使用的帐号 在左边（老号）， 点击右边的帐号切换
                        if ((sendAccountType == 3) || (sendAccountType == 1)) {
                            SPUtils.putString(context, "WxAccountLocation", "1"); //目前的帐号在左边
                            SPUtils.putString(context, "SwitchAccountSuccess", "1"); // 没有切换
                            NodeUtils.clickNode("返回", "com.tencent.mm:id/d5i");//点击左上角的返回
                            return 1;
                        }
                        if (sendAccountType == 2) {
                            SPUtils.putString(context, "WxAccountLocation", "2"); //切换后的帐号在右边
                            switch (Build.MODEL) {
                                case ModelConstans.coolpad_8737:
                                    AdbUtils.click(464, 678);
                                    break;
                                case ModelConstans.tvyk:
                                    wxUtils.adbClick(288, 457, 384, 553);
                                    break;
                            }
                            SPUtils.putString(context, "SwitchAccountSuccess", "2"); // 切换成功
                        }
                    } else if (listXY != null && listXY.get(0) == 443) {
                        //正在使用的帐号 在右边(新号)， 点击左边的帐号切换
                        if ((sendAccountType == 3) || (sendAccountType == 2)) {
                            SPUtils.putString(context, "WxAccountLocation", "2"); //目前的帐号在右边
                            SPUtils.putString(context, "SwitchAccountSuccess", "1"); // 没有切换
                            NodeUtils.clickNode("返回", "com.tencent.mm:id/d5i");//点击左上角的返回
                            return EX_Next;
                        }
                        if (sendAccountType == 1) {
                            SPUtils.putString(context, "WxAccountLocation", "1"); //切换后的帐号在右边
                            switch (Build.MODEL) {
                                case ModelConstans.coolpad_8737:
                                    AdbUtils.click(234, 672);
                                    break;
                                case ModelConstans.tvyk:
                                    wxUtils.adbClick(96, 457, 192, 553);
                                    break;
                            }

                            SPUtils.putString(context, "SwitchAccountSuccess", "2"); // 切换成功
                        }
                    }
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (listXY != null && listXY.get(0) == 187) {
                        if (sendAccountType == 1) {
                            SPUtils.putString(context, "AccountIsOnlyOne", "2");
                            SPUtils.putString(context, "WxAccountLocation", "1"); //目前的帐号在左边
                        } else {
                            SPUtils.putString(context, "AccountIsOnlyOne", "1");  //失败
                            SPUtils.putString(context, "WxAccountLocation", "1"); //目前的帐号在右边
                        }
                    } else if (listXY != null && listXY.get(0) == 443) {
                        if (sendAccountType == 1) {
                            SPUtils.putString(context, "AccountIsOnlyOne", "1");//失败
                            SPUtils.putString(context, "WxAccountLocation", "2"); //目前的帐号在右边
                        } else {
                            SPUtils.putString(context, "AccountIsOnlyOne", "2");
                            SPUtils.putString(context, "WxAccountLocation", "2"); //目前的帐号在右边
                        }
                    }
                }
                break;
            case ModelConstans.tvyk:
                if (accountNum == 2) {  //老号在左边  新号在右边
                    //说明已经登录了两个帐号
                    if (listXY != null && listXY.get(0) == 187) {
                        //正在使用的帐号 在左边（老号）， 点击右边的帐号切换
                        if ((sendAccountType == 3) || (sendAccountType == 1)) {
                            SPUtils.putString(context, "WxAccountLocation", "1"); //目前的帐号在左边
                            SPUtils.putString(context, "SwitchAccountSuccess", "1"); // 没有切换
                            NodeUtils.clickNode("返回", "com.tencent.mm:id/d5i");//点击左上角的返回
                            return EX_Next;
                        }
                        if (sendAccountType == 2) {
                            SPUtils.putString(context, "WxAccountLocation", "2"); //切换后的帐号在右边
                            switch (Build.MODEL) {
                                case ModelConstans.coolpad_8737:
                                    AdbUtils.click(464, 678);
                                    break;
                                case ModelConstans.tvyk:
                                    wxUtils.adbClick(288, 457, 384, 553);
                                    break;
                            }
                            SPUtils.putString(context, "SwitchAccountSuccess", "2"); // 切换成功
                            return EX_Next;
                        }
                    } else if (listXY != null && listXY.get(0) == 443) {
                        //正在使用的帐号 在右边(新号)， 点击左边的帐号切换
                        if ((sendAccountType == 3) || (sendAccountType == 2)) {
                            SPUtils.putString(context, "WxAccountLocation", "2"); //目前的帐号在右边
                            SPUtils.putString(context, "SwitchAccountSuccess", "1"); // 没有切换
                            NodeUtils.clickNode("返回", "com.tencent.mm:id/d5i");//点击左上角的返回
                            return EX_Next;
                        }
                        if (sendAccountType == 1) {
                            SPUtils.putString(context, "WxAccountLocation", "1"); //切换后的帐号在右边
                            switch (Build.MODEL) {
                                case ModelConstans.coolpad_8737:
                                    AdbUtils.click(234, 672);
                                    break;
                                case ModelConstans.tvyk:
                                    wxUtils.adbClick(96, 457, 192, 553);
                                    break;
                            }
                            SPUtils.putString(context, "SwitchAccountSuccess", "2"); // 切换成功
                            return 1;
                        }
                    }
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (listXY != null && listXY.get(0) == 187) {
                        if (sendAccountType == 1) {
                            SPUtils.putString(context, "AccountIsOnlyOne", "2");
                            SPUtils.putString(context, "WxAccountLocation", "1"); //目前的帐号在左边
                        } else {
                            SPUtils.putString(context, "AccountIsOnlyOne", "1");  //失败
                            SPUtils.putString(context, "WxAccountLocation", "1"); //目前的帐号在右边
                        }
                    } else if (listXY != null && listXY.get(0) == 443) {
                        if (sendAccountType == 1) {
                            SPUtils.putString(context, "AccountIsOnlyOne", "1");//失败
                            SPUtils.putString(context, "WxAccountLocation", "2"); //目前的帐号在右边
                        } else {
                            SPUtils.putString(context, "AccountIsOnlyOne", "2");
                            SPUtils.putString(context, "WxAccountLocation", "2"); //目前的帐号在右边
                        }
                    }

                }
                break;
        }
        return EX_Next;
    }

    @Override
    public void ExecutionBack() {
        _exAlignMent.get(1).Execution();
    }
}