package com.zplh.zplh_android_yk.base;

import android.os.Bundle;
import android.view.View;

import com.zplh.zplh_android_yk.eventbus.EventCenter;


/**
 * Author:  Tau.Chen
 * Email:   1076559197@qq.com | tauchen1990@gmail.com
 * Date:    15/7/21
 * Description:
 */
public abstract class BaseSwipeBackActivity<V,T extends BasePresenter<V>> extends BaseSwipeBackFragmentActivity implements BaseView {

    private T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter =createPresenter();
        if (mPresenter != null)
        {
            mPresenter.attachView((V)this);
        }
        if (isApplyKitKatTranslucency()) {
            //setTranslucentStatus(true);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
        {
            mPresenter.detachView();
        }
    }

    protected abstract T createPresenter();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected BaseApplication getBaseApplication() {
        return (BaseApplication) getApplication();
    }

    @Override
    public void showError(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showNetError(View.OnClickListener listener) {
        toggleNetworkError(true,listener);
    }

    @Override
    public void showLoading(String msg) {
        toggleShowLoading(true, null);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public void showErrorMsg(String msg) {
        showToast(msg);
    }

}
