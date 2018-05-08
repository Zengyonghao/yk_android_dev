package com.zplh.zplh_android_yk.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseActivity;
import com.zplh.zplh_android_yk.service.ShellService;
import com.zplh.zplh_android_yk.ui.fragment.MessageFragment;
import com.zplh.zplh_android_yk.ui.fragment.OperationFragment;
import com.zplh.zplh_android_yk.ui.fragment.RecordFragment;
import com.zplh.zplh_android_yk.ui.fragment.RedactFragment;


/**
 * Created by lichun on 2017/5/24.
 * Description:
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private DrawerLayout mDrawerLayout;
    private MessageFragment messageFragment;
    private RecordFragment recordFragment;
    private OperationFragment operationFragment;
    private RedactFragment redactFragment;
    private TextView aTextView, bTextView, cTextView, dTextView;
//    private WToolSDK wToolSDK=new WToolSDK();


    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        aTextView = (TextView) findViewById(R.id.aTextView);
        bTextView = (TextView) findViewById(R.id.bTextView);
        cTextView = (TextView) findViewById(R.id.cTextView);
        dTextView = (TextView) findViewById(R.id.dTextView);
        clickA();
        Intent startIntent = new Intent(this, ShellService.class);
        startService(startIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(this, ShellService.class);
        stopService(stopIntent);
    }

    @Override
    protected void initEvents() {
        aTextView.setOnClickListener(this);
        bTextView.setOnClickListener(this);
        cTextView.setOnClickListener(this);
        dTextView.setOnClickListener(this);
    }
        WxUtils wxUtils=new WxUtils();
    @Override
    protected void initDatas() {

//      int x=  mContext.getResources().getDimensionPixelSize(R.dimen.x320);
//        int y=  mContext.getResources().getDimensionPixelSize(R.dimen.y400);
//      LogUtils.d(x+"zplh_pure"+y);


        //获取微信好友
 /*      String friends= wToolSDK.getFriends( 0,0);
        WxFriendsBean wxFriendsBean= new Gson().fromJson(friends, WxFriendsBean.class);
        List<WxFriendsBean.ContentBean> contentBeanList= wxFriendsBean.getContent();
        for (int a=0;a<contentBeanList.size();a++){
            LogUtils.d(wToolSDK.decodeValue(contentBeanList.get(a).getNickname())+"___"+wToolSDK.decodeValue(contentBeanList.get(a).getWxid()));
        }
*/

//        LogUtils.d(wToolSDK.sendText( "wxid_gvajbt1jtdvf21", "在干嘛啊"));

     //获取微信群列表
/*       String Chatrooms= wToolSDK.getChatrooms(0,0,true);
        WxChatroomsBean wxFriendsBean= new Gson().fromJson(Chatrooms, WxChatroomsBean.class);
        List<WxChatroomsBean.ContentBean> contentBeanList= wxFriendsBean.getContent();
        for (int a=0;a<contentBeanList.size();a++){
            LogUtils.d(wToolSDK.decodeValue(contentBeanList.get(a).getNickname())+"___"+wToolSDK.decodeValue(contentBeanList.get(a).getWxid()));
        }
        LogUtils.d(wToolSDK.sendText( "6796556249@chatroom", "你好"));*/

        //写入文件
/*    try {
        File file = new File(Environment.getExternalStorageDirectory(),
                "pure.txt");
        FileOutputStream fos = new FileOutputStream(file);
        String info = Chatrooms;
        fos.write(info.getBytes());
        fos.close();
        System.out.println("写入成功：");
    } catch (Exception e) {
        e.printStackTrace();
    }*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aTextView:
                clickA();
                break;
            case R.id.bTextView:
                clickB();
                break;
            case R.id.cTextView:
                clickC();
                break;
            case R.id.dTextView:
                clickD();
                break;
        }
    }

    public void clickA() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //打开手势滑动
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
            fragmentTransaction.add(R.id.fragmentLayout, messageFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.show(messageFragment);
            fragmentTransaction.commit();
        }
        aTextView.setEnabled(false);
    }

    public void clickB() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //打开手势滑动
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        if (recordFragment == null) {
            recordFragment = new RecordFragment();
            fragmentTransaction.add(R.id.fragmentLayout, recordFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.show(recordFragment);
            fragmentTransaction.commit();
        }
        bTextView.setEnabled(false);
    }

    private void clickC() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        if (operationFragment == null) {
            operationFragment = new OperationFragment();
            fragmentTransaction.add(R.id.fragmentLayout, operationFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.show(operationFragment);
            fragmentTransaction.commit();
        }
        cTextView.setEnabled(false);
    }


    private void clickD() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        if (redactFragment == null) {
            redactFragment = new RedactFragment();
            fragmentTransaction.add(R.id.fragmentLayout, redactFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.show(redactFragment);
            fragmentTransaction.commit();
        }
        dTextView.setEnabled(false);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (recordFragment != null) {
            transaction.hide(recordFragment);
        }

        if (operationFragment != null) {
            transaction.hide(operationFragment);
        }
        if (redactFragment != null) {
            transaction.hide(redactFragment);
        }

        aTextView.setEnabled(true);
        bTextView.setEnabled(true);
        cTextView.setEnabled(true);
        dTextView.setEnabled(true);
    }

}
