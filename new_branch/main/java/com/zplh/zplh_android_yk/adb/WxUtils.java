package com.zplh.zplh_android_yk.adb;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.XmlToJson.XmlToJson;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.bean.XmlBean;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.ShellUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lichun on 2017/6/5.
 * Description:操作微信工具类
 */

public class WxUtils {

    private List<String> commnandList;
    private List<Integer> listXY;
    private Gson gson = new Gson();
    List<String> ls=new ArrayList<String>();

    //返回node节点数据
    public List<String> getNodeList(String node){
            ls.clear();
            Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
            Matcher matcher = pattern.matcher(node);
            while(matcher.find()){
                ls.add(matcher.group()+"/>");
            }
        return  ls;
    }


    public XmlBean getXmlBean() {
        adbDump();
        return gson.fromJson(xml2JSON(readTxtFile()), XmlBean.class);
    }

    public NodeXmlBean getNodeXmlBean(String str){
        return gson.fromJson(xml2JSON(str), NodeXmlBean.class);
    }

    public String getXmlData() {
        adbDump();
       return readTxtFile();
    }

    /**
     * 从字符串中提取数字
     *
     * @param s
     * @return
     */
    public List<Integer> getXY(String s) {
        if (listXY != null) {
            listXY.clear();
        } else {
            listXY = new ArrayList<>();
        }
        for (String sss : s.replaceAll("[^0-9]", ",").split(",")) {
            if (sss.length() > 0)
                try {
                    Integer a = Integer.parseInt(sss);
                    listXY.add(a);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

        }
        return listXY;
    }

    /**
     * 点坐标
     *
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public void adbClick(int a, int b, int c, int d) {
        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }
        commnandList.add("input tap " + (a + c) / 2 + " " + (b + d) / 2);
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }

    /**
     * 点击坐标（屏幕适配）
     * @param context
     * @param aa
     * @param bb
     * @param cc
     * @param dd
     */
    public void adbDimensClick(Context context,int aa, int bb, int cc, int dd) {
        int a= context.getResources().getDimensionPixelSize(aa);
        int b= context.getResources().getDimensionPixelSize(bb);
        int c= context.getResources().getDimensionPixelSize(cc);
        int d= context.getResources().getDimensionPixelSize(dd);

        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }

        commnandList.add("input tap " + (a + c) / 2 + " " + (b + d) / 2);
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }
// wxUtils.adb("input swipe 200 630 200 200");//向下滑动
    /**
     * 向上滑动（屏幕适配）
     * @param context
     */
    public void adbUpSlide(Context context) {

        int a= context.getResources().getDimensionPixelSize(R.dimen.x134);
        int b= context.getResources().getDimensionPixelSize(R.dimen.y295);
        int c= context.getResources().getDimensionPixelSize(R.dimen.x134);
        int d= context.getResources().getDimensionPixelSize(R.dimen.y94);

        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }

        commnandList.add("input swipe " + a + " "+ b + " "+ c + " "+ d );
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }

    /**
     * 拉群向上滑动（屏幕适配）
     * @param context
     */
    public void adbQunUpSlide(Context context) {

        int a= context.getResources().getDimensionPixelSize(R.dimen.x134);
        int b= context.getResources().getDimensionPixelSize(R.dimen.y283);
        int c= context.getResources().getDimensionPixelSize(R.dimen.x134);
        int d= context.getResources().getDimensionPixelSize(R.dimen.y94);

        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }

        commnandList.add("input swipe " + a + " "+ b + " "+ c + " "+ d );
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }

    /**
     * 获取页面xml数据
     */
    public void adbDump() {
        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }
        commnandList.add("uiautomator dump /sdcard/uidump.xml");
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }

    /**
     * adb shell input tap 279 1897
     * input tap 279 1897
     * 执行adb命令
     *
     * @param str
     */
    public void adb(String str) {
        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }
        commnandList.add(str);
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }

    //跳转到主页面
    public void openHome(Activity activity) {
        Intent intent1 = new Intent(Intent.ACTION_MAIN);

        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识

        intent1.addCategory(Intent.CATEGORY_HOME);

        activity.startActivity(intent1);
    }

    /**
     * 跳转到微信
     *
     * @param activity
     */
    public void openWx(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName cn = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setComponent(cn);
        activity.startActivity(intent);
    }

    /**
     * xml转json
     *
     * @param xml
     * @return
     */
    public String xml2JSON(String xml) {
        try {
            XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();

            String newJson = xmlToJson.toJson().toString().replaceAll("\"node\":\\[", "\"node_list\":[");
            return newJson;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
       /* String imei="";
        for (int slot = 0; slot < telephonyManager.getPhoneCount(); slot++) {
             imei = telephonyManager.getDeviceId(slot);
        }*/
        return imei;
    }


    /**
     * 6.0权限获取
     * android.permission.ACCESS_SUPERUSER
     *
     * @param context
     * @param activity
     */
    public static void getOpenWRITE(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)  //打开相机权限
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)   //可读
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)  //可写
                        != PackageManager.PERMISSION_GRANTED||
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)  //可写
                != PackageManager.PERMISSION_GRANTED){
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_PHONE_STATE},
                    1);
        }
    }


    /**
     * 读取sd卡 xml数据
     *
     * @param
     * @return
     */
    public String readTxtFile() {
        String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/uidump.xml";
        ;
        StringBuilder builder = new StringBuilder();
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        builder.append(line + "\n");
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return builder.toString();
    }


    // 添加联系
    public void addContact(String name, String phoneNumber, Context context) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        // 联系人的Email地址
        values.put(ContactsContract.CommonDataKinds.Email.DATA, "zhangphil@xxx.com");
        // 电子邮件的类型
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        // 向联系人Email URI添加Email数据
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        Toast.makeText(context, "联系人数据添加成功", Toast.LENGTH_SHORT).show();
    }




    //写入文件
  /*  try {
        File file = new File(Environment.getExternalStorageDirectory(),
                "pure.txt");
        FileOutputStream fos = new FileOutputStream(file);
        String info = aa;
        fos.write(info.getBytes());
        fos.close();
        System.out.println("写入成功：");
    } catch (Exception e) {
        e.printStackTrace();
    }*/

}
