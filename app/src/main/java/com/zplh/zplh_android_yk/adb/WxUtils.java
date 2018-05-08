package com.zplh.zplh_android_yk.adb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.XmlToJson.XmlToJson;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.NodeXmlBean;
import com.zplh.zplh_android_yk.bean.XmlBean;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.utils.FileUtils;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShellUtils;
import com.zplh.zplh_android_yk.utils.StringUtils;
import com.zplh.zplh_android_yk.utils.ViewCheckUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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


    //返回node节点数据
    public List<String> getNodeList(String node) {
        List<String> ls = new ArrayList<String>();
        ls.clear();
        Pattern pattern = Pattern.compile("<node.*?text=\"(.*?)\".*?resource-id=\"(.*?)\" class=\"(.*?)\" package=\"(.*?)\".*?content-desc=\"(.*?)\".*?checked=\"(.*?)\".*?enabled=\"(.*?)\".*?selected=\"(.*?)\".*?bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"");
        Matcher matcher = pattern.matcher(node);
        while (matcher.find()) {
            ls.add(matcher.group() + "/>");
        }
        return ls;
    }

    public int[] getLinkedXY(String s) {
        int[] linked = new int[4];
        int i = 0;
        for (String sss : s.replaceAll("[^0-9]", ",").split(",")) {
            if (sss.length() > 0) {
                try {
                    Integer a = Integer.parseInt(sss);
                    linked[i] = a;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } finally {
                    i++;
                }
            }
        }
        return linked;
    }

    public XmlBean getXmlBean() {
        adbDump();
        return gson.fromJson(xml2JSON(readTxtFile()), XmlBean.class);
    }

    public NodeXmlBean getNodeXmlBean(String str) {
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
     * 点坐标
     *
     * @param a
     * @param b
     */
    public void adbWxClick(int a, int b ) {
        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }
        commnandList.add("input tap " + a+ " " + b);
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }


    /**
     * 点击坐标（屏幕适配）
     *
     * @param context
     * @param aa
     * @param bb
     * @param cc
     * @param dd
     */
    public void adbDimensClick(Context context, int aa, int bb, int cc, int dd) {
        int a = context.getResources().getDimensionPixelSize(aa);
        int b = context.getResources().getDimensionPixelSize(bb);
        int c = context.getResources().getDimensionPixelSize(cc);
        int d = context.getResources().getDimensionPixelSize(dd);

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
     *
     * @param context
     */
    public void adbUpSlide(Context context) {

        int a = context.getResources().getDimensionPixelSize(R.dimen.x134);

        int b = context.getResources().getDimensionPixelSize(R.dimen.y295);
        int c = context.getResources().getDimensionPixelSize(R.dimen.x134);
        int d = context.getResources().getDimensionPixelSize(R.dimen.y94);

        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }

        commnandList.add("input swipe " + a + " " + b + " " + c + " " + d);
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }


    /**
     * 拉群向上滑动（屏幕适配）
     *
     * @param context
     */
    public void adbQunUpSlide(Context context) {

        int a = context.getResources().getDimensionPixelSize(R.dimen.x134);
        int b = context.getResources().getDimensionPixelSize(R.dimen.y283);
        int c = context.getResources().getDimensionPixelSize(R.dimen.x134);
        int d = context.getResources().getDimensionPixelSize(R.dimen.y94);

        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }

        commnandList.add("input swipe " + a + " " + b + " " + c + " " + d);
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
     * 跳转到支付宝
     */
    public void openAliPay() {
        com.wanj.x007_common.util.ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.eg.android.AlipayGphone/com.eg.android.AlipayGphone.AlipayLogin");

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
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getIMEI(Context context) {
/*        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();*/

        String imeiList = "";
        imeiList = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_GSM);



        return imeiList;
    }

    /**
     * 获取手机IMsi号
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getImsi(Context context) {
/*        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();*/

        String imeiList = "";
        imeiList = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_CDMA);



        return imeiList;
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
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)  //可写
                        != PackageManager.PERMISSION_GRANTED  ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)  //可写
                        != PackageManager.PERMISSION_GRANTED  ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)  //可写
                        != PackageManager.PERMISSION_GRANTED

                ) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},
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

    /**
     * 删除全部联系人
     *
     * @return
     */
    public HashMap<String, Object> delAllContacts(ContentResolver resolver) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation op = null;
        Uri uri = null;
        HashMap<String, Object> delResult = new HashMap<String, Object>();
        int num = 0;//删除影响的行数
        resolver.delete(Uri.parse(ContactsContract.RawContacts.CONTENT_URI.toString() + "?"
                        + ContactsContract.CALLER_IS_SYNCADAPTER + "=true"),
                ContactsContract.RawContacts._ID + ">0", null);
        //删除Data表的数据
        uri = Uri.parse(ContactsContract.Data.CONTENT_URI.toString() + "?" + ContactsContract.CALLER_IS_SYNCADAPTER + "=true");
        op = ContentProviderOperation.newDelete(uri)
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + ">0", null)
                .withYieldAllowed(true)
                .build();
        ops.add(op);
        //删除RawContacts表的数据
        uri = Uri.parse(ContactsContract.RawContacts.CONTENT_URI.toString() + "?" + ContactsContract.CALLER_IS_SYNCADAPTER + "=true");
        op = ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts._ID + ">0", null)
                .withYieldAllowed(true)
                .build();
        ops.add(op);
        //删除Contacts表的数据
        uri = Uri.parse(ContactsContract.Contacts.CONTENT_URI.toString() + "?" + ContactsContract.CALLER_IS_SYNCADAPTER + "=true");
        op = ContentProviderOperation.newDelete(uri)
                .withSelection(ContactsContract.Contacts._ID + ">0", null)
                .withYieldAllowed(true)
                .build();
        ops.add(op);
        //执行批量删除
        try {
            ContentProviderResult[] results = resolver.applyBatch(ContactsContract.AUTHORITY, ops);
            for (ContentProviderResult result : results) {
                num += result.count;
                LogUtils.d("影响的行数" + result.count);
            }
            delResult.put("result", "1");
            delResult.put("obj", num);
        } catch (Exception e) {
            LogUtils.d("影响的行数" + e.getMessage());
            delResult.put("result", "-1");
            delResult.put("obj", "删除失败！" + e.getMessage());
        }
        if (delResult.size() == 0) {
            delResult.put("result", "0");
            delResult.put("obj", "无效删除，联系人信息不正确！");
        }
        return delResult;
    }

    /**
     * 清除所有联系人
     *
     * @param context
     */
    public void DeletPhone(Context context) {
        //ShowToast.show("正在清理联系人", (Activity) context);
        try {
            ViewCheckUtils.check();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (cur.moveToNext()) {
            try {
                String lookupKey = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.
                        Contacts.CONTENT_LOOKUP_URI, lookupKey);
                System.out.println("The uri is " + uri.toString());
                cr.delete(uri, null, null);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
        cur.close();
        // ShowToast.show("手机联系人清理完成", (Activity) context);
    }

    /**
     * 获取手机通讯录的联系人的数量
     *
     * @param context
     * @return
     */
    public int getContactCount(Context context) {
        try {
            ViewCheckUtils.check();
        } catch (Exception e) {

        }
        Cursor c = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._COUNT}, null, null, null);
        try {
            c.moveToFirst();
            return c.getInt(0);
        } catch (Exception e) {
            return 0;
        } finally {
            c.close();
        }
    }

    // 添加联系
    public void addContact(String name, String phoneNumber, Context context) {
        try {
            ViewCheckUtils.check();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//        values.put(ContactsContract.CommonDataKinds.Email.DATA, "zhangphil@xxx.com");
        // 电子邮件的类型
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        // 向联系人Email URI添加Email数据
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

//        Toast.makeText(context, "联系人数据添加成功", Toast.LENGTH_SHORT).show();
    }

   /*   new Thread(new Runnable() {
        @Override
        public void run() {
            String s="18671522513,18671519721,18671522931,18671595527,18671519307,18671522167,\n" +
                    "18696091451,18671512397,18671167573,18671547261";
            String[] strList=s.split(",");
            for (int a=0;a<strList.length;a++){
                new WxUtils().addContact(strList[a],strList[a],mContext);
            }
        }
    }).start();*/

    /**
     * [下载APP]
     */
    public static File getFileFromServer(String path) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(25000);
            // 获取到文件的大小
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
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * [下载文件]
     */
    public static File getFileDown(String path, String name) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(25000);
            // 获取到文件的大小
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory() + "/ykimages", name);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * [下载app]
     */
    public static File getFileAliPay(String path, String name) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(25000);
            // 获取到文件的大小
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), name);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * 添加到相册
     *
     * @param result
     * @param context
     */
    public static void addimages(File result, Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(result);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开应用. 应用在前台不处理,在后台就直接在前台展示当前界面, 未开启则重新启动
     */
    public static void openApplicationFromBackground(Context context) {
        Intent intent;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (!list.isEmpty() && list.get(0).topActivity.getPackageName().equals(context.getPackageName())) {
            //此时应用正在前台, 不作处理
            LogUtils.d("zplh在前台不处理a");
            return;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(context.getPackageName())) {
                intent = new Intent();
                intent.setComponent(info.topActivity);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
                LogUtils.d("zplh在运行后台运行处理a");
                return;
            }
        }
        LogUtils.d("zplh在重新打开a");
        intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        context.startActivity(intent);

    }

    //😄,😷,😂,😝,😲,😳,😱,😔,😉,😌,😒,👿,👻,💝,🙏,💪,💰,🎂
//    public String imageData="[微笑],[撇嘴],[色],[发呆],[得意],[流泪],[害羞],[闭嘴],[睡],[大哭],[尴尬],[发怒],[调皮],[呲牙],[惊讶],[难过],[酷],[冷汗],[抓狂],[吐],[偷笑],[愉快],[白眼],[傲慢],[饥饿],[困],[惊恐],[流汗],[憨笑],[悠闲],[奋斗],[咒骂],[疑问],[嘘],[晕],[疯了],[衰],[骷髅],[敲打],[再见],[擦汗],[抠鼻],[鼓掌],[糗大了],[坏笑],[左哼哼],[右哼哼],[哈欠],[鄙视],[委屈],[快哭了],[阴险],[亲亲],[吓],[可怜],[菜刀],[西瓜],[啤酒],[篮球],[乒乓],[咖啡],[饭],[猪头],[玫瑰],[凋谢],[嘴唇],[爱心],[心碎],[蛋糕],[闪电],[炸弹],[刀],[足球],[瓢虫],[便便],[月亮],[太阳],[礼物],[拥抱],[强],[弱],[握手],[胜利],[抱拳],[勾引],[拳头],[差劲],[NO],[OK],[爱情],[飞吻],[跳跳],[发抖],[怄火],[转圈],[磕头],[回头],[跳绳],[投降]";
    //筛选后的
    public String imageData = "[微笑],[撇嘴],[色],[发呆],[得意],[流泪],[害羞],[闭嘴],[睡],[大哭],[尴尬],[调皮],[呲牙],[惊讶],[难过],[酷],[冷汗],[吐],[偷笑],[愉快],[白眼],[傲慢],[饥饿],[困],[惊恐],[流汗],[憨笑],[悠闲],[奋斗],[疑问],[嘘],[晕],[疯了],[敲打],[再见],[擦汗],[抠鼻],[鼓掌],[糗大了],[坏笑],[左哼哼],[右哼哼],[哈欠],[鄙视],[委屈],[快哭了],[阴险],[亲亲],[吓],[可怜],[西瓜],[啤酒],[篮球],[乒乓],[咖啡],[饭],[猪头],[玫瑰],[凋谢],[嘴唇],[爱心],[心碎],[蛋糕],[闪电],[足球],[瓢虫],[月亮],[太阳],[礼物],[拥抱],[强],[握手],[胜利],[抱拳],[勾引],[拳头],[差劲],[NO],[OK],[爱情],[飞吻],[跳跳],[发抖],[怄火],[转圈],[磕头],[回头],[跳绳],[投降]";

    Random random = new Random();// 定义随机类

    /**
     * string添加随机添加表情
     *
     * @param data
     * @return
     */
    public String getFaceText(String data) {

//        String data="我爱哭的时候便哭，想笑的时候便笑，只要这一切出于自然。";
        String[] strings = imageData.split(",");
        int count = 0;
        String[] texts = data.split("，");

        for (int a = 0; a < texts.length; a++) {
            if (count >= 4) {
                break;
            } else {

                if (random.nextInt(texts.length) == 0) {
                    int num = random.nextInt(3);
                    switch (num) {
                        case 0:
                            texts[a] = texts[a] + strings[random.nextInt(strings.length - 1)];
                            break;
                        case 1:
                            texts[a] = texts[a] + strings[random.nextInt(strings.length - 1)] + strings[random.nextInt(strings.length - 1)];
                            break;
                        case 2:
                            texts[a] = texts[a] + strings[random.nextInt(strings.length - 1)] + strings[random.nextInt(strings.length - 1)] + strings[random.nextInt(strings.length - 1)];
                            break;
                    }
                    count++;
                }
            }
        }

        StringBuffer stringBuffer = new StringBuffer("");
        for (int b = 0; b < texts.length; b++) {
            if (count == 0) {
                int num = random.nextInt(3);
                switch (num) {
                    case 0:
                        stringBuffer.append(strings[random.nextInt(strings.length - 1)]);
                        break;
                    case 1:
                        stringBuffer.append(strings[random.nextInt(strings.length - 1)]).append(strings[random.nextInt(strings.length - 1)]);
                        break;
                    case 2:
                        stringBuffer.append(strings[random.nextInt(strings.length - 1)]).append(strings[random.nextInt(strings.length - 1)]).append(strings[random.nextInt(strings.length - 1)]);
                        break;
                }
                count = 1;
            }
            if (b == 0) {
                stringBuffer.append(texts[b].toString());
            } else {
                stringBuffer.append(",").append(texts[b].toString());
            }

        }
        return stringBuffer.toString();
    }


    /**
     * 判断app是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isInstallApp(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 保存任务
     */
    public void saveTask(Context mContext, List<MessageListBean.ContentBean.DataBean> dataBeanList) {
        String saveTaskData = gson.toJson(dataBeanList);

        SPUtils.putString(mContext, "saveTaskData", saveTaskData);
        LogUtils.d("保存saveTaskData:" + saveTaskData);
    }


    /**
     * 写入文件到本地
     *
     * @param info
     */
    public void write(String info) {
        //写入文件
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "testlog.txt");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(info.getBytes());
            fos.close();
            System.out.println("写入成功：");
        } catch (Exception e) {
            System.out.println("写入失败：");
            e.printStackTrace();
        }
    }

    /**
     * 图片下载
     *
     * @param messageData
     */
    public void xDownloadFile(String messageData) {

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
        path = URLS.pic_vo + text.replace("\\", "/");
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


    /**
     * 截图
     */
    public void screenShot() {
        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }
        int count = random.nextInt(10);
        FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/BBB");
        String command = "/system/bin/screencap -p /sdcard/" + "BBB/" + "ScreenShot.png";
        commnandList.add(command);
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }

    /**
     *  杀微信进程
     */
    public void killWx() {
        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }
        int count = random.nextInt(10);
        FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/BBB");
        String command = "am force-stop com.tencent.mm";
        commnandList.add(command);
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);
    }

    /**
     *  Wx 通用页面跳转
     */
    public void wxActivityJump( String str ) {
        if (commnandList != null) {
            commnandList.clear();
        } else {
            commnandList = new ArrayList<>();
        }
        int count = random.nextInt(10);
        FileUtils.createDirs(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/BBB");
        String command = "  am start  "+str ;
        commnandList.add(command);
        ShellUtils.CommandResult result = ShellUtils.execCommand(commnandList, true);
        LogUtils.d(result.result + "adb" + result.successMsg);

    }



}
