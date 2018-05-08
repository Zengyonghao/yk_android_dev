package com.zplh.zplh_android_yk.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adpter.StateAdpter;
import com.zplh.zplh_android_yk.base.BaseFragment;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.bean.FiltrationBean;
import com.zplh.zplh_android_yk.bean.LogidBean;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.bean.StateRenwuBean;
import com.zplh.zplh_android_yk.conf.MyConstains;
import com.zplh.zplh_android_yk.conf.ZFB_URLS;
import com.zplh.zplh_android_yk.db.MyList_Dao;
import com.zplh.zplh_android_yk.db.StateDao;
import com.zplh.zplh_android_yk.httpcallback.GsonUtil;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.presenter.OldOperationAlipayPresenter;
import com.zplh.zplh_android_yk.ui.activity.MainActivity;
import com.zplh.zplh_android_yk.ui.view.CustomDialog;
import com.zplh.zplh_android_yk.ui.view.OperationView;
import com.zplh.zplh_android_yk.utils.LogUtils;
import com.zplh.zplh_android_yk.utils.SPUtils;
import com.zplh.zplh_android_yk.utils.ShowToast;
import com.zplh.zplh_android_yk.utils.TimeUtil;
import com.zplh.zplh_android_yk.utils.VibratorUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 * 记录
 */

public class RecordFragment extends BaseFragment implements View.OnClickListener,OperationView, AdapterView.OnItemLongClickListener {
    private ListView listview_recored;//listvie
    private int id=0;
    private String log_id;
    private List<StateRenwuBean>list;
    private StateDao dao;
    private StateAdpter adpter;
    private Button button_clean,button_refresh;//一键清除
    private List<String> arr_list=new ArrayList<String>();
    private ZFB_URLS zfb_urls;
    private String str_log_id="";
    private String wait_login_id;
    private int taskId=0;
    private TimeUtil timeUtil;
    private OldOperationAlipayPresenter oldOperationAlipayPresenter;
    private MyRecordFragmentReceiver recordFragemtn;
    private MyList_Dao myList_dao;
    private AudioManager audioManage;
    private MediaPlayer mPlayer;
    CustomDialog.Builder builder;

    @Override
    protected BasePresenter createPresenter() {
        return oldOperationAlipayPresenter;
    }
//   private NetworkChange.OnNetWorkChange MyWorknets=new NetworkChange.OnNetWorkChange() {
//        @Override
//        public void onChange(int wifi, final int mobile, final int none, int oldStatus, final int newStatus) {
//            if (newStatus == none){ //没有网络
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(12000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (newStatus==mobile) {//再次有网络 则继续执行
//                            stop();
//                            audioManage.setStreamMute(AudioManager.STREAM_SYSTEM, true);
//                            audioManage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                            Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
//                            vib.cancel();
//                        }
//                       if (newStatus==none){//没有网络 则开始叫
//                            VibratorUtil.Vibrate(getActivity(), new long[]{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000},true);
//                            audioManage.setStreamMute(AudioManager.STREAM_SYSTEM,true);
//                            audioManage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                            audioManage.adjustVolume(AudioManager.ADJUST_RAISE, 100);
//                            paly(mContext);
//                        }
//
//                     //   CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
//
//                    }
//                }).start();
//            }
//            if (newStatus == mobile){  //移动网络
//                LogUtils.d("该手机目前有网络");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        stop();
//                        audioManage.setStreamMute(AudioManager.STREAM_SYSTEM,true);
//                        audioManage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                        Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
//                        vib.cancel();
//                    }
//                }).start();
//            }
//            if (newStatus == wifi){//wifi网络
//                LogUtils.d("目前该手机没有无线网");
//                Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
//                stop();
//                audioManage.setStreamMute(AudioManager.STREAM_SYSTEM,true);
//                audioManage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                 vib.cancel();
//                if (oldStatus == mobile) {  //从移动网络切换到wifi网络
//                    stop();
//                    audioManage.setStreamMute(AudioManager.STREAM_SYSTEM,true);
//                    audioManage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                    vib.cancel();
//                }
//            }
//       }
//    };
//
//        //注册监听网络变化
//    public void registerReceiver() {
//        NetworkChange.getInstance().setOnNetWorkChange(MyWorknets);
//    }
    View view;
    @Override
    protected void initData() {
        audioManage= (AudioManager) mContext.getSystemService(Service.AUDIO_SERVICE);
       // registerReceiver();
        builder= new CustomDialog.Builder(getContext());
        dao=new StateDao(mContext);
        oldOperationAlipayPresenter=new OldOperationAlipayPresenter(mContext, this);
        timeUtil=new TimeUtil();
        zfb_urls=new ZFB_URLS();
        arr_list=dao.select_log_id();
        myList_dao=new MyList_Dao(mContext);
//        HttpSetLog_id();
        list=dao.selectPerson();
        adpter=new StateAdpter(list,mContext);
        adpter.notifyDataSetChanged();
        listview_recored.setAdapter(adpter);
        listview_recored.setOnItemLongClickListener(this);
        button_clean.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyConstains.Broadcast_Task);
        recordFragemtn=new MyRecordFragmentReceiver();
        mContext.registerReceiver(recordFragemtn,filter);//注册一个广播
    }

    @Override
    public void onResume() {
        super.onResume();
        //registerReceiver();

        arr_list=dao.select_log_id();
        LogUtils.d("myList_dao的长度是:"+myList_dao.selectPerson().size());
//        HttpSetLog_id();
        list=dao.selectPerson();
        adpter=new StateAdpter(list,mContext);
        adpter.notifyDataSetChanged();
        listview_recored.setAdapter(adpter);
    }

    @Override
    protected View initViews() {
        view = View.inflate(mContext, R.layout.fragment_record, null);
        listview_recored= (ListView) view.findViewById(R.id.listview_recored);
        button_clean= (Button) view.findViewById(R.id.button_clean);
        button_refresh=(Button) view.findViewById(R.id.button_refresh);
        return view;
    }

    @Override
    protected void initEvents() {
        button_refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_clean:
                if (list.size()<=0){
                    ShowToast.show("目前该手机的任务状态列表为0或者已被清空",getActivity());
                }else{
                    dialog();
                }
                break;
            case R.id.button_refresh:
                HttpSetLog_id();
                break;
        }
    }

    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定要删除该手机的所有任务状态吗？");
        builder.setTitle("提示");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dao.deleteAll();//删除表中所有数据
                list=dao.selectPerson();
                adpter=new StateAdpter(list,mContext);
                listview_recored.setAdapter(adpter);
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private ProgressDialog pd;
    /**
     * 过滤Log_id
     * @param
     */
    private void HttpSetLog_id(){

        if (arr_list.size()>0){
            pd = ProgressDialog.show(mContext, "提示", "正在刷新...", true, false);
            for (int i =0;i<arr_list.size();i++){
                str_log_id=str_log_id+arr_list.get(i)+",";
            }
            str_log_id=str_log_id.substring(0,str_log_id.length()-1);
            RequestParams params = new RequestParams(zfb_urls.FiltrationSet());
            params.addBodyParameter("logSets", str_log_id);//status
            LogUtils.d("过滤log_id的接口是"+zfb_urls.FiltrationSet() + "?logSets=" + str_log_id);
            HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<FiltrationBean>() {

                @Override
                public void onSuccess(FiltrationBean bean) {
                    pd.dismiss();
                    LogUtils.d("在列表中过滤的log_id的值是:"+bean.getData());
                    for (int i=0;i<bean.getData().size();i++){
                        dao.deletlog_id(bean.getData().get(i));
                    }
                    list=dao.selectPerson();
                    adpter=new StateAdpter(list,mContext);
                    adpter.notifyDataSetChanged();
                    listview_recored.setAdapter(adpter);
                    ShowToast.show("刷新成功",(Activity) mContext);
                }
                @Override
                public void onFailure(int errorCode, String errorString) {
                    pd.dismiss();
                    ShowToast.show("刷新失败，"+errorString,(Activity) mContext);
                }
            });
            str_log_id="";
        }else{
            ShowToast.show("刷新成功",(Activity) mContext);
        }
    }

    @Override
    public void isLoginWx(boolean isLogin) {

    }

    public void showAlertDialog() {
        VibratorUtil.Vibrate(getActivity(), new long[]{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000},true);
        String uid= SPUtils.getString(getContext(), "uid", "0000");
        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());

        builder.setMessage("你真是狠心，这么快你就把人家忘记了，今天终于找到我，可不能再次遗弃我哦！");
        builder.setTitle("手机UID是:"+uid);
        builder.setPositiveButton("亲 你点我可以关闭窗口哦", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
                vib.cancel();
                //设置你的操作事项
            }
        });
        builder.create().show();

    }

    //listview  item的长按事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (list.get(position).getId_task()!=50)  {
            ShowToast.show("只有循环任务，才能启用此功能", (Activity) mContext);
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("log_id值："+list.get(position).getLogin_id());
        builder.setCancelable(false);
        View item = View.inflate(mContext,R.layout.listview_item,null);
        Button again = (Button) item.findViewById(R.id.again_bt);//重复当前任务
        Button next = (Button) item.findViewById(R.id.next_bt);//进入下一个任务
        Button cancel = (Button) item.findViewById(R.id.cancel_bt);//取消
        builder.setView(item);

        final AlertDialog alertDialog = builder.create();
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final RequestParams params = new RequestParams(zfb_urls.getTaskByLogid());//url
                params.addQueryStringParameter("log_id", list.get(position).getLogin_id()+"");
                System.out.println("--");
                new Thread(){
                    @Override
                    public void run() {
                        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
                            @Override
                            public void onSuccess(LogidBean bean) {
                                String data = bean.getData();
                                MessageListBean.ContentBean.DataBean.ParamBean paramBean = GsonUtil.parseJsonWithGson(data, MessageListBean.ContentBean.DataBean.ParamBean.class);
                                MessageListBean.ContentBean.DataBean dataBean = new MessageListBean.ContentBean.DataBean();
                                dataBean.setLog_id(list.get(position).getLogin_id()+""); //设置logid
                                dataBean.setParam(paramBean);
                                dataBean.setTask_id(50);//只有支付宝循环加粉，需要这个需求。
                                MainActivity m = (MainActivity) getActivity();
                                m.operationFragment.again(dataBean,2);
                                ShowToast.show("请求成功", (Activity) mContext);
                                //alertDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int errorCode, String errorString) {
                                ShowToast.show("请求失败", (Activity) mContext);
                            }
                        });
                        alertDialog.dismiss();
                    }
                }.start();

            }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        RequestParams params = new RequestParams(zfb_urls.getTaskByLogid());//url
                        params.addQueryStringParameter("log_id", list.get(position).getLogin_id()+"");
                        HttpManager.getInstance().sendRequest(params, new HttpObjectCallback<LogidBean>() {
                            @Override
                            public void onSuccess(LogidBean bean) {
                                String data = bean.getData();
                                MessageListBean.ContentBean.DataBean.ParamBean paramBean = GsonUtil.parseJsonWithGson(data, MessageListBean.ContentBean.DataBean.ParamBean.class);
                                MessageListBean.ContentBean.DataBean dataBean = new MessageListBean.ContentBean.DataBean();
                                dataBean.setLog_id(list.get(position).getLogin_id()+""); //设置logid
                                dataBean.setParam(paramBean);
                                dataBean.setTask_id(50);//只有支付宝循环加粉，需要这个需求。
                                MainActivity m = (MainActivity) getActivity();
                                m.operationFragment.getXunhuan(dataBean);

                                //删除点击过的条目，防止用户多次点击。需要点击的条目，说明该任务已经失效。可以删除！
                                dao.deletlog_id(list.get(position).getLogin_id()+"");
                                ShowToast.show("请求成功", (Activity) mContext);
                                //alertDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int errorCode, String errorString) {
                                ShowToast.show("请求失败", (Activity) mContext);
                            }
                        });
                        alertDialog.dismiss();
                    }
                }.start();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        return true;
    }



    class MyRecordFragmentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strAction = intent.getAction();
            if (strAction.equals(MyConstains.Broadcast_Task)) {
                MessageListBean.ContentBean.DataBean dataBean = (MessageListBean.ContentBean.DataBean) intent.getSerializableExtra("messageBean");

                if (dataBean.getTask_id()==37||dataBean.getTask_id()==60){
                    LogUtils.d("我在RecordFragment中收到了广播 嘿嘿");
                    showAlertDialog();

                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(recordFragemtn);
    }
    /**
     * 播放
     */
    public void paly(Context c) {
        /**
         * 开头就调用stop()方法，可避免用户多次单机Play按钮创建多个MediaPlayer实例的情况发生。
         */
        stop();

        /**
         * 音频文件放在res/raw目录下。目录raw负责存放那些不需要Android编译系统特别处理的各类文件。
         */
        mPlayer = MediaPlayer.create(c, R.raw.jingbao);
       // mPlayer.setLooping(true);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });

        mPlayer.start();
    }
    /**
     * 停止
     */
    public void stop() {
        if (mPlayer != null) {
            /**
             * MediaPlayer.release()方法可销毁MediaPlayer的实例。销毁是“停止”的一种具有攻击意味的说法，
             * 但我们有充足的理由使用销毁一词。
             * 除非调用MediaPlayer.release()方法，否则MediaPlayer将一直占用着音频解码硬件及其它系统资源
             * 。而这些资源是由所有应用共享的。
             * MediaPlayer有一个stop()方法。该方法可使MediaPlayer实例进入停止状态，等需要时再重新启动
             * 。不过，对于简单的音频播放应用，建议 使用release()方法销毁实例，并在需要时进行重见。基于以上原因，有一个简单可循的规则：
             * 只保留一个MediaPlayer实例，保留时长即音频文件 播放的时长。
             */
            mPlayer.release();
            mPlayer = null;
        }
    }



    /**
     * 暂停
     */
    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }
}
