package com.zplh.zplh_android_yk.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wanj.x007_common.util.ShellUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseActivity;
import com.zplh.zplh_android_yk.bean.CheckImei;
import com.zplh.zplh_android_yk.bean.ImeiData;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.httpcallback.GsonUtil;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.NodeUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;
import com.zplh.zplh_android_yk.utils.StringUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import okhttp3.Call;

/**
 * Created by lichun on 2017/6/18.
 * Description:绑定设备
 */

public class BindingActivity extends BaseActivity implements View.OnClickListener {
    private EditText binding_et;
    private TextView binding_tv, imei_tv, version_tv;
    private Button btUpdata;
    private WxUtils wxUtils = new WxUtils();
    private String imei = "";

    private ProgressDialog pd;

    private boolean isOpen = true;
    @Override
    protected void initViews() {
        setContentView(R.layout.activity_binding);
        btUpdata = (Button) findViewById(R.id.bt_updata);
        binding_et = (EditText) findViewById(R.id.binding_et);
        binding_tv = (TextView) findViewById(R.id.binding_tv);
        version_tv = (TextView) findViewById(R.id.version_tv);
        imei_tv = (TextView) findViewById(R.id.imei_tv);

        btUpdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            updata();
            }
        });
    }

    @Override
    protected void initEvents() {
        binding_tv.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean b = SPUtils.getBoolean(mContext, "bindingtomain", false);
        LogUtils.d("直接打开" + b);
        if (b) {
            LogUtils.d("直接打开.........");
                        Intent intent = new Intent(BindingActivity.this, MainActivity.class);
                        startActivity(intent);
            SPUtils.putBoolean(mContext, "bindingtomain", false);
            isOpen = false;
                        finish();
        } else {
            if (SPUtils.getBoolean(mContext, "addshortcut", true)) {
                addShortcut(this.getString(R.string.app_name));//添加桌面图标
                SPUtils.putBoolean(mContext, "addshortcut", false);
            }
            version_tv.setText("version:" + wxUtils.getVersionName(mContext));
            SPUtils.putBoolean(mContext, "task", false);

            imei = SPUtils.getString(mContext, "imeiimei", "");

            if (StringUtils.isEmpty(imei)) {
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 222);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        NodeUtils.clickNode("com.android.packageinstaller:id/permission_allow_button");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        imei = wxUtils.getIMEI(mContext);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkBound();

                            }
                        });
                    }
                }).start();

            } else {
              checkBound();
            }
        }
    }



    private void checkBound() {
        pd = ProgressDialog.show(mContext, "提示", "数据初始化中...", true, false);

        if (imei != null && imei.length() > 0) {
            imei_tv.setText("imei:" + imei);
            imei_tv.setVisibility(View.VISIBLE);
        }

        isBoundImei(imei,  new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                CheckImei checkImei = GsonUtil.parseJsonWithGson(response,CheckImei.class);
                if (TextUtils.equals(checkImei.getRet(),"200")){
                    //绑定成功
                    pd.dismiss();
                    SPUtils.putBoolean(mContext, "imei", true);
                    SPUtils.putString(mContext, "uid", checkImei.getData());
                    Intent intent = new Intent(BindingActivity.this, MainActivity.class);
                    startActivity(intent);
                    isOpen = false;
                    finish();
                }else{
                    //未绑定 检查一下mis

                    String imsi = wxUtils.getImsi(BindingActivity.this);
                    if (TextUtils.isEmpty(imsi)) {
                        pd.dismiss();

                        return;
                    }

                    isBoundImei(imsi, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            pd.dismiss();
                            SPUtils.putBoolean(mContext, "imei", false);
                            showToast("网络异常，请稍后再试...");
                            binding_tv.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            CheckImei checkImei = GsonUtil.parseJsonWithGson(response,CheckImei.class);
                            if (TextUtils.equals(checkImei.getRet(),"200")) {
                                //绑定成功
                                pd.dismiss();
                                SPUtils.putBoolean(mContext, "imei", true);
                                SPUtils.putString(mContext, "uid", checkImei.getData());
                                Intent intent = new Intent(BindingActivity.this, MainActivity.class);
                                startActivity(intent);
                                isOpen = false;
                                finish();
                            }else {
                                pd.dismiss();
                            }
                        }
                    });

                }
            }

        });
    }


    /**
     * 创建快捷方式
     *
     * @param name
     */
    private void addShortcut(String name) {
        Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(BindingActivity.this,
                        R.mipmap.ic_launcher));

        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(BindingActivity.this, BindingActivity.class);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        sendBroadcast(addShortcutIntent);
    }




    /**
     * 检查版本是否需要更新
     */
    private void updata() {
        RequestParams params = new RequestParams(URLS.updata());
        LogUtils.d(URLS.updata());
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<CheckImei>() {

            @Override
            public void onSuccess(CheckImei bean) {
                String newVersion = bean.getData();
                double version = Double.valueOf(wxUtils.getVersionName(mContext));
                double ver = Double.valueOf(bean.getData());
                if (ver > version) {
                    pd = ProgressDialog.show(mContext, "提示", "等待更新中...", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int a = 5000;
                            while (true) {
                                if (version_update_go) {
                                    version_update_go();
                                } else {
                                    pd.dismiss();
                                    version_update_go = true;
                                    LogUtils.d("开始下载");
                                    handler.sendEmptyMessage(0x124);
                                    break;
                                }
                                LogUtils.d("20s判断一次");
                                try {
                                    Thread.sleep(a);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                a = 20000;
                            }
                        }
                    }).start();

                } else {
                    ShowToast.show("已经是最新版本", (Activity) mContext);
                    LogUtils.d("已经是最新版本...");
                }
            }

            @Override
            public void onFailure(int errorCode, String errorString) {

            }
        });
    }

    /**
     * 开始下载
     */
    public void version_update_go() {
        //        ShowToast.show("等待更新中...",(Activity) mContext);
        RequestParams params = new RequestParams(URLS.version_update_go());
        //        params.addQueryStringParameter("id", id);//手机4位数
        LogUtils.d(URLS.version_update_go());
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                version_update_go = false;
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
            }
        });
    }


    boolean version_update_go = true;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isOpen) {
            ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.zplh.zplh_android_yk/com.zplh.zplh_android_yk.ui.activity.BindingActivity");
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.binding_tv:
                String strBinding = binding_et.getText().toString().trim();
                if (TextUtils.isEmpty(strBinding) || strBinding.length() <= 5) {
                    showToast("请输入正确的激活码");
                } else {
                    binding_tv.setClickable(false);
//                    setBound(strBinding.substring(strBinding.length()-4),strBinding.substring(0,strBinding.length()-4));
                    setBound(strBinding.substring(strBinding.length() - 4), strBinding);
                }
                break;
        }
    }


    /**
     * 绑定设备
     *
     * @param id
     * @param code
     */
    public void setBound(final String id, String code) {
        //http://192.168.1.126:8087/yk/index.php/home/group/binding?id=159357457&imei=87975431324687132417
        pd = ProgressDialog.show(mContext, "提示", "设备绑定中", true, false);
        RequestParams params = new RequestParams(URLS.binding());
        params.addQueryStringParameter("id", id);//手机4位数
        params.addQueryStringParameter("code", code);
        params.addQueryStringParameter("imei", imei);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<ImeiData>() {

            @Override
            public void onSuccess(ImeiData bean) {
                pd.dismiss();
                showToast("绑定成功");
                SPUtils.putString(mContext, "uid", id);
                SPUtils.putBoolean(mContext, "imei", true);
                SPUtils.putString(mContext, "imeiimei", imei);
                Intent intent = new Intent(BindingActivity.this, MainActivity.class);
                startActivity(intent);
                isOpen = false;
                finish();
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                pd.dismiss();
                binding_tv.setClickable(true);
                showToast("绑定失败"+errorString);
            }
        });
    }

    /**
     * 判断设备是否绑定
     */
    public void isBoundImei(String imeivalue,StringCallback callback) {
        OkHttpUtils.get().url(URLS.isbinding())
                .addParams("imei",imeivalue)
                .addParams("status","1")
                .build().execute(callback);

    }





    /**
     * 下载更新
     */
    private void updataDown(final String path, final String pathUrl) {

        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(mContext);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("新版本下载中");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);

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
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("亲，努力下载中。。。");
                pd.show();
                pd.setMax((int) total);
                pd.setProgress((int) current);


                LogUtils.d((int) total + "下载了...");
                LogUtils.d((int) current + "下载...");
            }

            @Override
            public void onSuccess(File result) {
                LogUtils.d("下载完成开始安装");
                version_update_back();//下载结束反馈
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wxUtils.adb("pm install -r " + pathUrl);
                pd.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                LogUtils.d("x下载失败");
                pd.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";



    /**
     * 下载结束
     */
    public void version_update_back() {
        RequestParams params = new RequestParams(URLS.version_update_back());
        //        params.addQueryStringParameter("id", id);//手机4位数
        LogUtils.d(URLS.version_update_back());
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {

            }

            @Override
            public void onFailure(int errorCode, String errorString) {
            }
        });
    }


    private Handler handler = new Handler() {

        // 该方法运行在主线程中
        // 接收到handler发送的消息，对UI进行操作
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what){
                case 0x123:
                    pd.setMessage("等待更新中");
                    break;
                case 0x124:
                    updataDown("http://103.94.20.102:8087/download/wxzs.apk", Environment.getExternalStorageDirectory().getAbsoluteFile() + "/wxykupdata.apk");
                    break;
            }


        }
    };
}