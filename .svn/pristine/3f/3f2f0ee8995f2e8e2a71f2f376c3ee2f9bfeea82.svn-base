package com.zplh.zplh_android_yk.utils;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by yong hao zeng on 2018/4/16/016.
 */
public class NetUtils {
    //同步网络请求  获取File
    public static File getApk(String server) throws IOException {

        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(server);
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
     * 异步get请求
     *
     * @param url
     * @param params
     */
    public static void get_excute(String url, Map<String, String> params, Callback callback) {
//        Logger.t("网络请求").d(url, params);
        GetBuilder url1 = OkHttpUtils.get().url(url);
        if (params == null)
            url1.build().execute(callback);
        else
            url1.params(params).build().execute(callback);
    }


    public static String get(String url,@Nullable Map<String, String> params) throws Exception {
        int number = 0;
        Response response = null;
        while (number < 5) {
            number++;
//            .t("网络请求").d(url, params);
            Thread.sleep(5000);
            GetBuilder url1 = OkHttpUtils.get().url(url);

            if (params == null)
                response = url1.build().execute();
            else
                response = url1.params(params).build().execute();
            if (response.code() == 200) {
                return response.body().string();
            }
        }
        return null;
    }


    public static String post(String url, Map<String, String> params) throws Exception {
        int number = 0;
        Response response = null;
        while (number < 5) {
            number++;
            Thread.sleep(5000);
//            Logger.t("网络请求").d(url, params);
             response =  OkHttpUtils.post().url(url).params(params).build().execute();
            if (response.code() == 200) {
                return response.body().string();
            }
        }
        return null;
    }
}
