package com.zplh.zplh_android_yk.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wanj.x007_common.CommunicationDefine;
import com.wanj.x007_common.pb.Common;
import com.wanj.x007_common.util.ShellUtils;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseActivity;
import com.zplh.zplh_android_yk.utils.LogUtils;

/**
 * Created by lichun on 2017/6/23.
 * Description:嗅探测试
 */

public class WxSdkActivity extends BaseActivity implements View.OnClickListener {


    class ClientBroadcastRecv extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strAction = intent.getAction();
            if (strAction.equals(CommunicationDefine.Broadcast_WeichatToApp)) {
                byte[] data = intent.getByteArrayExtra("data");
                handleWeichatBroadcast(data);
            }
        }
    }

    protected void handleWeichatBroadcast(byte[] data) {
        try {
            Common.XPhone_ReqHead reqHead = Common.XPhone_ReqHead.parseFrom(data);
            switch (reqHead.getBase().getMsgType()) {
                case MsgType_TaskRsp: {
                    Common.XPhone_TaskRsp request = Common.XPhone_TaskRsp.parseFrom(data);
                    Log.d("askRsp", request.toString());
                }
                break;
                case MsgType_AccountSearchRsp: {
                    Common.XPhone_AccountSearchRsp request = Common.XPhone_AccountSearchRsp.parseFrom(data);
                    Log.d("AccountSearchRsp", request.toString());
                    for(int i=0;i<request.getUserInfoCount();i++){
                        LogUtils.d(request.getUserInfo(i).getStrUserName());
                    }
                }
                break;
                default:
                    break;
            }
        } catch (Exception e) {

        }
    }

    ClientBroadcastRecv m_client;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_wxsdk_test);
        ((Button)findViewById(R.id.convert)).setOnClickListener(this);
        ((Button)findViewById(R.id.addidfriend)).setOnClickListener(this);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new WxUtils().getOpenWRITE(mContext, (WxSdkActivity) mContext);//6.0获取权限
            }
        }).start();
        m_client = new ClientBroadcastRecv();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommunicationDefine.Broadcast_WeichatToApp);
        registerReceiver(m_client,filter);
    }
    java.util.Random random=new java.util.Random();// 定义随机类
    String phone="18572832400";
    @Override
    public void onClick(View v) {
        ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.wanj.x007/com.wanj.x007.MainActivity");

        ShellUtils.myExecCommand("am start -a android.intent.action.MAIN -n com.tencent.mm/com.tencent.mm.ui.LauncherUI");

        switch (v.getId())
        {
            //一定要记得打开SDK 的辅助功能
            case R.id.convert:
            {
                Common.XPhone_AccountSearchReq.Builder task = Common.XPhone_AccountSearchReq.newBuilder();
                Common.XPhone_MsgBase.Builder base = Common.XPhone_MsgBase.newBuilder();
                base.setMsgType(Common.XPhone_MsgType.MsgType_AccountSearchReq);
                task.setBase(base);

                task.setSearchIntrval(20);//设置搜索时间间隔。单位秒。 避免频繁搜索，时间长一点
                task.addAccounts(phone);//手机号，可以调用多次，添加多个

//                task.addAccounts("18565895962");//手机号，可以调用多次，添加多个

                Common.XPhone_TaskReq.Builder builder = Common.XPhone_TaskReq.newBuilder();
                base = Common.XPhone_MsgBase.newBuilder();
                base.setMsgType(Common.XPhone_MsgType.MsgType_TaskReq);
                builder.setBase(base);
                builder.setITaskID(1);//设置一个任务ID，任务完成的时候会带上这个ID
                builder.setTaskType(task.getBase().getMsgType());
                builder.setTaskInfo(task.build().toByteString());

                Intent intent2= new Intent();
                intent2.setAction(CommunicationDefine.Broadcast_AppToWeichat);
                intent2.putExtra("data",builder.build().toByteArray());
                sendBroadcast(intent2);

            }
            break;
            case R.id.addidfriend:
            {
                Common.XPhone_AddIDFriendReq.Builder task = Common.XPhone_AddIDFriendReq.newBuilder();
                Common.XPhone_MsgBase.Builder base = Common.XPhone_MsgBase.newBuilder();
                base.setMsgType(Common.XPhone_MsgType.MsgType_AddIDFriendReq);
                task.setBase(base);
                //3:来自微信号搜索 6:通过好友同意  13:来自手机通讯录 14:群聊 15:来自手机号搜索 17:通过名片分享添加  18:来自附近人 30:通过扫一扫添加 39:搜索公众号来源

                String[] sniffing=new String[]{"3","6","13","14","15","17","18","30","39"};
                String sniffing_type=sniffing[random.nextInt(8)];//0-8的随机数
                task.setISencse(Integer.valueOf(sniffing_type));//设置来源
                task.setStrIntroduce("戴老师推荐4");

                int result=random.nextInt(40)+60; //60-100的随机数
                task.setIAddIntrval(result); //添加间隔
                task.setIAddCount(2);
//                task.addIds("wxid_gvajbt1jtdvf21");//搜索出来的微信号 愣头青
//                task.addIds("wxid_dfaf");;//搜索出来的微信号

                task.addIds("wxid_sax3ir46dj1m22");
                task.addIds("wxid_t8djn1pzdzbm22");
                task.addIds("wxid_o7aht7kyx5yk22");
                task.addIds("wxid_jjsip2mx2tio22");
                task.addIds("wxid_arnta7596nx422");
                task.addIds("wxid_u06m3phi6jd122");
                task.addIds("wxid_6qi2hwtxrk6222");
                task.addIds("wxid_hnwhuohi6isy22");
                task.addIds("wxid_kbd1gbpgewfj22");
                task.addIds("wxid_8b0mgvjg6i4x22");



                Common.XPhone_TaskReq.Builder builder = Common.XPhone_TaskReq.newBuilder();
                base = Common.XPhone_MsgBase.newBuilder();
                base.setMsgType(Common.XPhone_MsgType.MsgType_TaskReq);
                builder.setBase(base);
                builder.setITaskID(2);//设置一个任务ID，任务完成的时候会带上这个ID
                builder.setTaskType(task.getBase().getMsgType());
                builder.setTaskInfo(task.build().toByteString());

                Intent intent2= new Intent();
                intent2.setAction(CommunicationDefine.Broadcast_AppToWeichat);
                intent2.putExtra("data",builder.build().toByteArray());
                sendBroadcast(intent2);
            }
            break;
        }
    }
}
