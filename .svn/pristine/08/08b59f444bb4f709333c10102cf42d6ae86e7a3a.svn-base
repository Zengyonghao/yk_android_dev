package com.zplh.zplh_android_yk.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.adb.WxUtils;
import com.zplh.zplh_android_yk.base.BaseFragment;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.presenter.OperationPresenter;
import com.zplh.zplh_android_yk.ui.view.OperationView;


/**
 * Created by lichun on 2017/5/31.
 * Description:操作
 */

public class OperationFragment extends BaseFragment implements View.OnClickListener,OperationView {

    private TextView aTextView, bTextView, cTextView, dTextView, eTextView, fTextView;
    private View view;
    private OperationPresenter operationPresenter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        operationPresenter = new OperationPresenter(mContext, this);
    }



    @Override
    protected View initViews() {
        view = View.inflate(mContext, R.layout.fragment_operation, null);
        aTextView = (TextView) view.findViewById(R.id.aTextView);
        bTextView = (TextView) view.findViewById(R.id.bTextView);
        cTextView = (TextView) view.findViewById(R.id.cTextView);
        dTextView = (TextView) view.findViewById(R.id.dTextView);
        eTextView = (TextView) view.findViewById(R.id.eTextView);
        fTextView = (TextView) view.findViewById(R.id.fTextView);
        return view;
    }

    @Override
    protected void initEvents() {
        aTextView.setOnClickListener(this);
        bTextView.setOnClickListener(this);
        cTextView.setOnClickListener(this);
        dTextView.setOnClickListener(this);
        eTextView.setOnClickListener(this);
        fTextView.setOnClickListener(this);
    }
    WxUtils wxUtils=new WxUtils();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aTextView://建群
                break;

            case R.id.bTextView://拉群
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        operationPresenter.task(1);//拉群
                    }
                }).start();

                break;

            case R.id.cTextView://判断男女，修改备注
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        operationPresenter.task(0);//修改备注
                    }
                }).start();

                break;
        }
    }

    @Override
    public void alterName() {//修改备注回调

    }




}
