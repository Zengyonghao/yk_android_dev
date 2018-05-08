package com.zplh.zplh_android_yk.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseActivity;
import com.zplh.zplh_android_yk.bean.ImeiData;
import com.zplh.zplh_android_yk.conf.URLS;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;

import org.xutils.http.RequestParams;

/**
 * Created by lichun on 2017/6/18.
 * Description:绑定设备
 */

public class BindingActivity extends BaseActivity implements View.OnClickListener {
    private EditText binding_et;
    private TextView binding_tv, imei_tv;
    private WxUtils wxUtils = new WxUtils();
    private String imei = "";
    private ProgressDialog pd;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_binding);
        binding_et = (EditText) findViewById(R.id.binding_et);
        binding_tv = (TextView) findViewById(R.id.binding_tv);
        imei_tv = (TextView) findViewById(R.id.imei_tv);
    }

    @Override
    protected void initEvents() {
        binding_tv.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                wxUtils.adbDump();
                wxUtils.getOpenWRITE(mContext, (BindingActivity) mContext);//6.0获取权限
            }
        }).start();
        imei = wxUtils.getIMEI(mContext);
        LogUtils.d("____imei:" + imei);
        isBound();

        if (imei.length() > 0) {
            imei_tv.setText("imei:" + imei);
            imei_tv.setVisibility(View.VISIBLE);
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
    public void setBound(String id, String code) {
        //http://192.168.1.126:8087/yk/index.php/home/group/binding?id=159357457&imei=87975431324687132417
        pd = ProgressDialog.show(mContext, "提示", "设备绑定中", true, false);
        RequestParams params = new RequestParams(URLS.binding());
        params.addQueryStringParameter("id", id);//手机4位数
        params.addQueryStringParameter("code", code);
        params.addQueryStringParameter("imei", imei);
        LogUtils.d(URLS.binding() + "?id=" + id + "&code=" + code + "&imei=" + imei);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<ImeiData>() {

            @Override
            public void onSuccess(ImeiData bean) {
                pd.dismiss();
                showToast("绑定成功");
                SPUtils.putBoolean(mContext, "imei", true);
                Intent intent = new Intent(BindingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                pd.dismiss();
                binding_tv.setClickable(true);
                showToast("绑定失败");
            }
        });
    }

    /**
     * 判断设备是否绑定
     */
    public void isBound() {
        pd = ProgressDialog.show(mContext, "提示", "数据初始化中...", true, false);
        RequestParams params = new RequestParams(URLS.isbinding());
        params.addQueryStringParameter("imei", imei);
        LogUtils.d(URLS.isbinding() + "?imei=" + imei);
        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<ImeiData>() {

            @Override
            public void onSuccess(ImeiData bean) {
                pd.dismiss();
                SPUtils.putBoolean(mContext, "imei", true);
                Intent intent = new Intent(BindingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                pd.dismiss();
                SPUtils.putBoolean(mContext, "imei", false);
//                showToast("请绑定设备");
            }
        });
    }
}
