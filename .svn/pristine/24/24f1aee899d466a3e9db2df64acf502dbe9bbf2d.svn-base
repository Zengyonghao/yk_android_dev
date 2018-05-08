package com.zplh.zplh_android_yk.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.zplh.zplh_android_yk.conf.Constants;
import com.zplh.zplh_android_yk.conf.URLS;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/6/26.
 */

public class XutilDown {
    private ProgressDialog progressDialog;
    private Context context;
    private ContentResolver localContentResolver;
    public XutilDown(Context context){
        this.context=context;
        progressDialog = new ProgressDialog(context);


    }
    public void downloadFile(String url,String path){
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
                ShowToast.show("视频素材等待下载中，请稍等！！！", (Activity) context);
            }

            @Override
            public void onStarted() {
                ShowToast.show("开始下载素材", (Activity) context);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("亲，正在努力下载中。。。不要着急");
                progressDialog.show();
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
             public void onSuccess(File result) {
               ShowToast.show("从后台获取数据成功，开始执行任务", (Activity) context);
                progressDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(result);
                intent.setData(uri);
                context.sendBroadcast(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                ShowToast.show("下载失败，请检查网络或者SDcard", (Activity) context);
                progressDialog.dismiss();
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
     * 将视频文件保存到相册
      * @param paramContext
     * @param paramFile
     * @param paramLong
     * @return
     */

    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong)
    {
         ContentValues localContentValues = new ContentValues();
         localContentValues.put("title", paramFile.getName());
         localContentValues.put("_display_name", paramFile.getName());
         localContentValues.put("mime_type", "video/3gp");
         localContentValues.put("datetaken", Long.valueOf(paramLong));
         localContentValues.put("date_modified", Long.valueOf(paramLong));
         localContentValues.put("date_added", Long.valueOf(paramLong));
         localContentValues.put("_data", paramFile.getAbsolutePath());
         localContentValues.put("_size", Long.valueOf(paramFile.length()));
              return localContentValues;
    }




}
