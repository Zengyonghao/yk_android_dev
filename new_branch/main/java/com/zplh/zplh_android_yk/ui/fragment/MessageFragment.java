package com.zplh.zplh_android_yk.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.base.BaseFragment;
import com.zplh.zplh_android_yk.base.BasePresenter;

/**
 * Created by Administrator on 2017/5/26.
 * 消息
 */

public class MessageFragment extends BaseFragment{

    View view;
    private TextView textView;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected View initViews() {
        view = View.inflate(mContext, R.layout.fragment_message, null);
        textView = (TextView) view.findViewById(R.id.textView);
        return view;
    }

    @Override
    protected void initEvents() {

    }
}
