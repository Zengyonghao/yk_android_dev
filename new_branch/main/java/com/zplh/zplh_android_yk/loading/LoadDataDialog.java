package com.zplh.zplh_android_yk.loading;/*
package showsoft.lber.loading;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bamai.patient.R;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

*/
/**
 * @项目名: 	BaMaiPaitent
 * @包名:	com.bamai.patient.view
 * @类名:	MyRatingBar
 * @公司:	88my.com
 * @创建者:	wenkaichuang
 * @创建时间:	2015年11月14日	上午11:35:21 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 *//*

public class LoadDataDialog extends Dialog {
	private ImageView iv;
	private TextView tv_message;
	private Reference<Activity> contextRef;
	
	public LoadDataDialog(Activity context) {
		super(context,R.style.loadDialog);
		contextRef = new WeakReference<Activity>(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_load_data);
		tv_message = (TextView) findViewById(R.id.tv_loaddialog_message);
		setCanceledOnTouchOutside(false);
		initView();
	}

	private void initView() {
		iv = (ImageView) findViewById(R.id.iv1);
		AlphaAnimation aa = new AlphaAnimation(0.0f,1.0f);
		aa.setDuration(500);
		aa.setRepeatCount(1000);
		aa.setRepeatMode(Animation.REVERSE);
		iv.startAnimation(aa);
	}
	
	public void setMessage(String text){
		tv_message.setVisibility(View.VISIBLE);
		tv_message.setText(text);
	}

	@Override
	public void dismiss() {
		if (contextRef!= null && contextRef.get() != null && !contextRef.get().isFinishing()) {
			super.dismiss();
			contextRef.clear();
			contextRef = null;
		}
	}
}
*/
