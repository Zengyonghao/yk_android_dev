package com.zplh.zplh_android_yk.ui.activity;

import android.view.View;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.base.BaseSwipeBackActivity;

/**
 * Created by lichun on 2017/6/1.
 * Description:选择素材
 */

public class OperationActivity extends BaseSwipeBackActivity{


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_operation);
    }


    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }



}
