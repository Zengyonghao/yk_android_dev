package com.zplh.zplh_android_yk.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePalApplication;
import org.xutils.x;

import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * @创建者 Administrator
 * @创时间 2015-8-14 下午2:19:53
 * @描述 全局盒子, 里面放置一些全局的变量或者方法, Application其实是一个单例
 * @版本 $Rev: 6 $
 * @更新者 $Author: admin $
 * @更新时间 $Date: 2015-08-14 14:38:24 +0800 (Fri, 14 Aug 2015) $
 * @更新描述 TODO
 */
public class BaseApplication extends LitePalApplication {

    private static Context mContext;
    private static Handler mHandler;
    private static long mMainThreadId;
    private static Thread mMainThread;
    public CookieStore cookieStore;
    private String userId;
    private String mobile;//getMobile
    private String name;
    private String customerCode;//getLastlogindate
    private String byInfo;

    /**
     * 是否刷新和账号有关的数据
     **/
    public boolean isRefreshAccount = false;
    /**
     * 是否刷新订单
     **/
    public boolean isRefreshOrder = false;
    private int needRefresh;

    public int getNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(int needRefresh) {
        this.needRefresh = needRefresh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * 购物车数据
     **/
    public List<Activity> activityList = new ArrayList<Activity>();

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    /** 添加界面 **/
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    @Override
    public void onCreate() {// 程序入口方法

        // 1.上下文
        mContext = getApplicationContext();

        // 2.创建一个handler
        mHandler = new Handler();

        // 3.得到一个主线程id
        mMainThreadId = android.os.Process.myTid();

        // 4.得到主线程
        mMainThread = Thread.currentThread();

        // 5.捕获异常
        // uncaughtException();

        // 6.配置imageLoder 使用环境
        ImageLoaderConfigur();

        // 7.初始化xUtil
        x.Ext.init(this);

        // 8.初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // 9.初始化xposed
//         new WToolSDK().init("0279C8C340306804E57499CD112EB094CB13037A");

        //初始化内存泄露检查
//		refWatcher = LeakCanary.install(this);

        super.onCreate();

        //百度地图初始化
     /*   SDKInitializer.initialize(getApplicationContext());
        mobile= SPUtils.getString(this,"mobile","");
        userId=SPUtils.getString(this,"userid","");
        name=SPUtils.getString(this,"name","");*/
    }
    public String getByInfo() {
        return byInfo;
    }
    public void setByInfo(String byInfo) {
        this.byInfo = byInfo;
    }

//	public static RefWatcher getRefWatcher(Context context) {
//		BaseApplication application = (BaseApplication) context.getApplicationContext();
//		return application.refWatcher;
//	}

//	private RefWatcher refWatcher;

    /**
     * 配置imageLoder 使用环境
     */
    private void ImageLoaderConfigur() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())

                .diskCacheSize(512 * 1024 * 1024)
				.diskCacheExtraOptions(720, 1280, null)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密

                .memoryCacheSizePercentage(14)
                .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheExtraOptions(720, 1280)
                .memoryCache(new WeakMemoryCache())

                .threadPoolSize(5)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

    }

    /**
     * 崩溃是重启
     */
    private void uncaughtException() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                // 复活
                PackageManager pm = getPackageManager();
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(getPackageName());
                startActivity(launchIntentForPackage);

                // 杀死
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

}
