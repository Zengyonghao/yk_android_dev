package com.zplh.zplh_android_yk.Threads;

import com.zplh.zplh_android_yk.utils.LogUtils;

/**
 * Created by Administrator on 2017/8/10.
 */

public class ReadThread implements Runnable{
    public Thread t;
    private String threadName;
    boolean suspended=false;

    public ReadThread(String threadName){
        this.threadName=threadName;
        LogUtils.d("线程名称:"+threadName);
    }
    @Override
    public void run() {
        for (int i=10;i>0;i--){
            LogUtils.d("Thread:"+threadName+","+i);
            try {
                Thread.sleep(300);
                synchronized (this){
                    while (suspended){
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                LogUtils.d("Thread："+threadName+"interrupted");
                e.printStackTrace();
            }
            LogUtils.d("Thread："+threadName+"exiting.");
        }

    }

    /**
     * 开始执行
     */
    public void start(){
        LogUtils.d("start线程："+threadName);
        if (t==null){
            t=new Thread(this,threadName);
            t.start();
        }

    }

    /**
     * 暂停
     */
   public void suspend(){
        suspended=true;

    }

    /**
     * 继续
     */
    synchronized void resume(){
        suspended=false;
        notify();
    }
}
