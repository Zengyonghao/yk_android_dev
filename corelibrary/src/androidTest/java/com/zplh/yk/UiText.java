package com.zplh.yk;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.test.InstrumentationTestCase;


/**
 * Author：liaogulong
 * Time: 2018/5/3/003   17:57
 * Description：
 */
public class UiText extends InstrumentationTestCase {


    //开始
    protected void setUp() throws Exception {
        super.setUp();
        //初始化
        BackApp();
    }

    //进行一系列用例操作
    public void testDemo1() throws UiObjectNotFoundException {
//        //点击拨号应用
//        open("拨号");
//        //输入电话号码
//        input("1");
//        input("0");
//        input("0");
//        input("8");
//        input("6");
//        //按拨号按钮
//        CallAndEnd("com.yulong.coolmessage:id/single_card_call_icon");
//        //通话状态
//        sleep(2000);
//        //挂断电话
//        CallAndEnd("com.android.dialer:id/floating_end_call_action_button");


//        while (true) {
////            UiObject uiObject = UiDevice.getInstance(getInstrumentation()).findObject(new UiSelector().text("微信"));
////            Log.e("TAG",uiObject+"");
////            if (uiObject != null) {
////                uiObject.click();
////                return;
////            }
//            try {
//                if (UITools.open("微信"))
//                    break;
//            } catch (UiObjectNotFoundException e) {
//                e.printStackTrace();
//            }
//            UiDevice.getInstance(getInstrumentation()).swipe(696, 529, 696 - 300, 529, 50);
//            UITools.sleep(1);
//        }
//        if (UITools.isPage("com.tencent.mm")) {
//            try {
//                UITools.open("通讯录");
//            } catch (UiObjectNotFoundException e) {
//                e.printStackTrace();
//            }
//            UITools.getListUiObject("com.tencent.mm:id/iq", "android.widget.LinearLayout").get(1).clickAndWaitForNewWindow();
//        }

    }


//    protected void tearDown() throws Exception {
//        super.tearDown();
//        //结束
//        BackApp();
//    }

    //从此处开始，下面的代码都可以copy到另外一个类中这样看起来就更直观了
    //初始化和退出操作
    public void BackApp() {
        UiDevice.getInstance(getInstrumentation()).pressBack();
        UiDevice.getInstance(getInstrumentation()).pressBack();
        UiDevice.getInstance(getInstrumentation()).pressBack();
        UiDevice.getInstance(getInstrumentation()).pressHome();
    }


}
