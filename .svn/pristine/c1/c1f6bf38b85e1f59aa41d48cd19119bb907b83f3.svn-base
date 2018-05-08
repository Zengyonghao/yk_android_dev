package com.zplh.zplh_android_yk.conf;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * 支付宝URL接口
 */
public class ZFB_URLS {


        public static final String url = "http://103.94.20.102:8087/yk/index.php/";//正式
//    public static final String url = "http://103.94.20.102:8087/yk_test/index.php/";//测试
    public static final String zfb_url = "http://103.94.20.100/api/index.php?zh=";

//    public static final String url = "http://192.168.10.135/yk/index.php/";//周文鹏


    /**
     * 第一个添加手机联系人接口 如果接口用完则使用第二个
     *
     * @param
     * @param
     * @return
     */
    public String AddPhone() {
        //return "http://103.94.20.100:8087/api/index_uid.php?uid=" + uid + "&limit=" + limt + "";;
        return "http://103.94.20.101:8087/api/index_uid.php";
    }

    /**
     * 添加手机联系人的第二个接口 如果第一个已经没有联系人 则调用第二个接口
     *
     * @param name
     * @param limt
     * @return
     */
    public String AddPhoneTwo(String name, String limt) {
        String url_phone2 = zfb_url + name + "&limit=" + limt + "&sta=2";
        return url_phone2;
    }

    /**
     * 发送好友数量的请求接口
     *
     * @param
     * @param
     * @param
     * @return
     */
    public String SendFrendNum() {
        String urls = url + "Ali/ApiAndroid/updata_friend_num";
        return urls;
    }

    /**
     * 发送点击申请好友数量的接口
     *
     * @param
     * @param
     * @param
     * @return
     */
    public String SendShenQFrendNum() {
        String urls = url + "Ali/ApiAndroid/updata_friend_send_num";
        return urls;
    }

    /**
     * 上传故障列表
     *
     * @param
     * @param
     * @param
     * @return
     */
    public String SendStatue() {
        String urls = url + "Ali/ApiAndroid/updata_driver_status";
        return urls;
    }

    /**
     * 上传支付宝群成员数
     */
    public static final String updata_group_member_count() {
        return url + "Ali/ApiAndroid/updata_group_member_count";
    }

    /**
     * 上传支付宝（发单）群成员数
     */
    public static final String push_bill() {
        return url + "Ali/ApiAndroid/push_bill";
    }

    /**
     * 上传支付宝拉群次数
     */
    public static final String updata_group_rquest_count() {
        return url + "Ali/ApiAndroid/updata_group_rquest_count";
    }

    public static final String zfb_resut(String log_id, String uid) {
        // String path = "http://103.94.20.102:8087/yk_test/index.php/home/ApiAndroid/updata_task_status?log_id=" + logId + ":帐号" + zfbNames + "&uid=" + uid;
        return url + "home/ApiAndroid/updata_task_status?log_id=" + log_id + "&uid=" + uid;
    }

    /**
     * 用于判断任务是否取消
     *
     * @return
     */
    public static final String isRenwuBoolean() {
        return url + "home/ApiAndroid/is_abolish";
    }

    /**
     * 通过login——id以旧换新
     * @param
     * @return
     */
    public static final String old_logid_new() {
        //http://103.94.20.102:8087/yk_test/index.php/
        return url+"ali/ApiAndroid/re_new_logid";
    }
    //http://103.94.20.102:8087/yk_test/index.php/home/ApiAndroid/abolished_sets?logSets=

    //通过logid获取，任务基本信息
    public static final String getTaskByLogid() {
        return url+"ali/ApiAndroid/param_task";
    }

    public static final String FiltrationSet(){
        return url+"home/ApiAndroid/abolished_sets";
    }

    public static final String ZFBConferred(){
        return url+"ali/Account/hasExist";
    }

    /**
     * 发送被封号的支付宝帐号到后台
     * @return
     */
    public static final String Title_zfb_fenghao(){
        //http://103.94.20.102:8087/yk_test/index.php/ali/ApiAndroid/limited_account
        return url+"ali/ApiAndroid/limited_account";
    }

    /**
     * 判断上次加粉数是否是1（判断限权）
     */
    public static final String get_latest() {
        return url + "Ali/ApiAndroid/get_latest";
    }
    /**
     * 发送添加的支付宝好友信息请求接口
     *
     * @param
     * @param
     * @param
     * @return
     */
    public static final String SendAliFriendsMessage() {
        String urls = url + "ali/ApiAndroid/upload_apply_records";
        return urls;
    }
    /**
     * 通过转账页面来获取真实名字的请求接口
     *
     * @param
     * @param
     * @param
     * @return
     */
    public static final String SendAliFriendsMessage2() {
        String urls = url + "ali/ApiHome/runame";
        return urls;
    }

}
