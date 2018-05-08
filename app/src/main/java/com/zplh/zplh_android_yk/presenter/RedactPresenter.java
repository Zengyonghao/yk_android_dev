package com.zplh.zplh_android_yk.presenter;

import android.content.Context;

import com.zplh.zplh_android_yk.base.BasePresenter;
import com.zplh.zplh_android_yk.ui.view.RedactView;

/**
 * Created by lichun on 2017/6/21.
 * Description:
 */

public class RedactPresenter extends BasePresenter<RedactView> {

    private RedactView redactView;
    private Context context;

    public RedactPresenter(Context context, RedactView redactView) {
        this.redactView = redactView;
        this.context = context;
    }

}
