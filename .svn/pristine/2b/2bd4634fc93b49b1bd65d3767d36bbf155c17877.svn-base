package com.zplh.zplh_android_yk.loading;

import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zplh.zplh_android_yk.R;


public class LoadDataProgress extends FrameLayout {

	private ImageView mIndicationIm;

	private TextView mLoadTextView;

	public LoadDataProgress(Context context) {
		super(context);
		init();
	}

	public LoadDataProgress(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}

	public LoadDataProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	protected void init() {

		View view = LayoutInflater.from(getContext()).inflate(R.layout.load_view, null);

		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		layoutParams.gravity = Gravity.CENTER;

		mIndicationIm = (ImageView) view.findViewById(R.id.indication);
		mLoadTextView = (TextView) view.findViewById(R.id.promptTV);

		addView(view, layoutParams);
		startLoading(100);
	}

	private AnimatorSet mAnimatorSet = null;

	private Runnable mFreeFallRunnable = new Runnable() {
		@Override
		public void run() {
			AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
			aa.setDuration(800);
			aa.setRepeatCount(Integer.MAX_VALUE);
			aa.setRepeatMode(Animation.REVERSE);
			mIndicationIm.startAnimation(aa);
		}
	};

	private void startLoading(long delay) {
		if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
			return;
		}
		this.removeCallbacks(mFreeFallRunnable);
		if (delay > 0) {
			this.postDelayed(mFreeFallRunnable, delay);
		} else {
			this.post(mFreeFallRunnable);
		}
	}

	private void stopLoading() {
		if (mAnimatorSet != null) {
			if (mAnimatorSet.isRunning()) {
				mAnimatorSet.cancel();
			}
			mAnimatorSet = null;
		}
		this.removeCallbacks(mFreeFallRunnable);
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == View.VISIBLE) {
			startLoading(200);
		} else {
			stopLoading();
		}
	}

	public void setLoadingText(CharSequence loadingText) {

		mLoadTextView.setVisibility(VISIBLE);
		mLoadTextView.setText(loadingText);
	}

}
