package com.zplh.zplh_android_yk.Threads;

import com.zplh.zplh_android_yk.utils.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by lichun on 2017/6/23.
 * Description:单例模式单线程任务啊啊
 */

public class SingleThread {
    private static final String TAG="SingleThread";
    private static SingleThread singleThread;
    public static synchronized SingleThread getInstance(){
        if(singleThread==null){
            singleThread=new SingleThread();
        }
        return singleThread;
    }
    ExecutorService regThreadPool= Executors.newSingleThreadExecutor();
    public void threadPoolExecute(Runnable runnable){
        if(runnable==null){
            LogUtils.e(TAG,"runnable is null");
            return;
        }
        try{
            regThreadPool.execute(runnable);
        }catch (RejectedExecutionException e){
            LogUtils.e(TAG,"regThreadPool execute fail.+ "+runnable);
        }
    }

    public void task( String num){
        final int i=Integer.valueOf(num);
        SingleThread.getInstance().threadPoolExecute(new Runnable() {
            @Override
            public void run() {
                switch (i){
                    case 15:
                        int i15=i;
                        while (i15!=0){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            LogUtils.d("i15="+i15--);
                        }
                        break;
                    case 14:
                        int i14=i;
                        while (i14!=0){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            LogUtils.d("i14="+i14--);
                        }
                        break;
                    case 13:
                        int i13=i;
                        while (i13!=0){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            LogUtils.d("i13="+i13--);
                        }
                        break;

                }
            }
        });
    }
}
