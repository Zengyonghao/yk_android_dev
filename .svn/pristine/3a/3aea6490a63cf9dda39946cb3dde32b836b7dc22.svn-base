package com.zplh.zplh_android_yk.ui.fragment;

import android.content.Context;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.TextView;

import com.easy.wtool.sdk.WToolSDK;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseApplication;
import com.zplh.zplh_android_yk.base.BaseFragment;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.bean.MessageListBean;
import com.zplh.zplh_android_yk.conf.ZFB_URLS;
import com.zplh.zplh_android_yk.db.MyList_Dao;
import com.zplh.zplh_android_yk.db.StateDao;
import com.zplh.zplh_android_yk.httpcallback.HttpManager;
import com.zplh.zplh_android_yk.httpcallback.HttpObjectCallback;
import com.zplh.zplh_android_yk.presenter.RedactPresenter;
import com.zplh.zplh_android_yk.ui.view.RedactView;
import com.zplh.zplh_android_yk.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by lichun on 2017/5/31.
 * Description:素材
 */

public class RedactFragment extends BaseFragment implements View.OnClickListener, RedactView {

    private View view;
    private TextView dTextView;
    RedactPresenter redactPresenter;
    private TextView text_signature;//个性签名
    List<MessageListBean.ContentBean.DataBean> dataBeanList;//保存任务，关闭app打开后执行剩下的任务
    private BaseApplication app;

    @Override
    protected BasePresenter createPresenter() {
        return redactPresenter;
    }

    @Override
    protected void initData() {
        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("你好");
        redactPresenter = new RedactPresenter(mContext, this);
        app = (BaseApplication) getActivity().getApplication();
        dataBeanList= app.getDataBeanList();//初始化任务数据
    }

    @Override
    protected View initViews() {
       view = View.inflate(mContext, R.layout.fragment_redact, null);
        //text_signature= (TextView) view.findViewById(R.id.text_signature);
        dTextView= (TextView) view.findViewById(R.id.dTextView);

        return view;
    }

    @Override
    protected void initEvents() {

        dTextView.setOnClickListener(this);
        view.findViewById(R.id.fTextView).setOnClickListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    WToolSDK wToolSDK = new WToolSDK();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dTextView:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getSignature();
                    }
                }).start();
                break;
            case R.id.fTextView:
                StateDao stateDao = new StateDao(getContext());
                stateDao.deleteAll();
                MyList_Dao myList_dao = new MyList_Dao(getContext());
                myList_dao.deleteAll();
                dataBeanList.clear();
                LogUtils.d("zplh删除后还有"+dataBeanList.size()+"个任务");
                new WxUtils().saveTask(mContext,dataBeanList);//保存到本地
                getActivity().finish();
                /*AlipayBillStatisticsBean alipayBillStatisticsBean=new AlipayBillStatisticsBean();
                alipayBillStatisticsBean.setAli_account("fjdskl@163.com");
                alipayBillStatisticsBean.setUid("7110");

                List<AlipayBillStatisticsBean.FlockBean> flock=new ArrayList<>();
                for(int a=0;a<350;a++){
                    flock.add(new AlipayBillStatisticsBean.FlockBean("今日特惠"+a,new Random().nextInt(300)+1+""));
                }

                alipayBillStatisticsBean.setFlock(flock);

                String strFlockBeanList = new Gson().toJson(alipayBillStatisticsBean);
                LogUtils.e(strFlockBeanList);
                push_bill(strFlockBeanList);//统计
                new WxUtils().write(strFlockBeanList);*/

                break;

        }
    }
    private void getSignature(){
        String content;
        String phone_url = "http://103.94.20.102:8087/yk_test/index.php/home/ApiAndroid/text_materials";
        try {
            URL url = new URL(phone_url);
            // 2.建立一个http连接
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            // 3.设置一些请求方式
            conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");

            int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
            // // 503服务器内部错误
            if (code == 200) {
                InputStream is = conn.getInputStream();
                // 把is的内容转换为字符串
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());
                LogUtils.d("个性签名请求的结果是" + result);
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                    JSONArray jsonArray = json.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        content = js.getString("content");
                        LogUtils.d("打印出来的content"+content);
                        ClipboardManager cmb = (ClipboardManager) getContext()
                                .getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText(content.trim());
                        //text_signature.setText(content.trim());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                is.close();
            } else {
                InputStream is = conn.getErrorStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());
                LogUtils.d("错误返回的结果" + result);
            }

        } catch (Exception e) {
            e.printStackTrace();


        }

    }


    /**
     * 上传群数据
     *
     * @param json  发单统计群信息
     */
    private void push_bill(String json) {
        RequestParams params  = new RequestParams(ZFB_URLS.push_bill());
//            params.addQueryStringParameter("json", json);
//            params.setBodyContent(json);
        params.addBodyParameter("json", json.replace("\\", ""));
        LogUtils.d(json.replace("\\", ""));
        LogUtils.d("--------------");
        LogUtils.showLargeLog(json.replace("\\", ""),500,"zplh");

        HttpManager.getInstance().sendPostRequest(params, new HttpObjectCallback<Object>() {

            @Override
            public void onSuccess(Object bean) {
                LogUtils.d("发单群信息上传成功");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                LogUtils.d("发单群信息上传失败");
            }
        });
    }


}
