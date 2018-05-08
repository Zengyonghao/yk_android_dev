package com.zplh.zplh_android_yk.ui.fragment;

import android.view.View;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.base.BaseFragment;
import com.zplh.zplh_android_yk.base.BasePresenter;

/**
 * Created by lichun on 2017/5/31.
 * Description:编辑
 */

public class RedactFragment extends BaseFragment {

    private View view;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected View initViews() {
        view = View.inflate(mContext, R.layout.fragment_redact,null);
        return view;
    }

    @Override
    protected void initEvents() {

    }
}
