package com.zplh.zplh_android_yk.conf;

/**
 * Created by lichun on 2017/6/18.
 * Description:
 */

public class URLS {

//    public static final String url = "http://192.168.1.126:8087/yk/index.php/";内网
//      public static final String url = "http://103.94.20.102:8087/yk/index.php/";//正式
    public static final String url = "http://103.94.20.102:8087/yk_test/index.php/";//测试

    /**
     * 绑定设备
     */
    public static final String binding() {
            return url + "home/binding/index";
    }


    /**
     * 绑定设备
     */
    public static final String isbinding() {
        return url + "home/binding/check_imei";
    }
}
