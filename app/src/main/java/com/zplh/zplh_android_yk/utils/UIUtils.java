package com.zplh.zplh_android_yk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.base.BaseApplication;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @创建者	 Administrator
 * @创时间 	 2015-8-14 下午2:27:07
 * @描述	     和ui相关的一些静态工具方法
 *
 * @版本       $Rev: 36 $
 * @更新者     $Author: admin $
 * @更新时间    $Date: 2015-08-18 15:32:51 +0800 (Tue, 18 Aug 2015) $
 * @更新描述    TODO
 */
public class UIUtils {
	/**得到上下文*/
	public static Context getContext() {
		return BaseApplication.getContext();
	}

	/**得到resouce对象*/
	public static Resources getResources() {
		return getContext().getResources();
	}

	// string.xml-->string-->arr
	/**得到string.xml中的一个字符串*/
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/**得到string.xml中的一个字符串,带占位符情况*/
	public static String getString(int resId, Object... formatArgs) {
		return getResources().getString(resId, formatArgs);
	}

	/**得到string.xml中的一个字符串数组*/
	public static String[] getStringArr(int resId) {
		return getResources().getStringArray(resId);
	}

	/**得到color.xml中的颜色值*/
	public static int getColor(int colorId) {
		return getResources().getColor(colorId);
	}

	/**得到应用程序的包名*/
	public static String getPackageName() {
		return getContext().getPackageName();
	}

	/**得到主线程id*/
	public static long getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	/**得到一个主线程的handler*/
	public static Handler getMainThreadHandler() {
		return BaseApplication.getHandler();
	}

	/**安全的执行一个task*/
	public static void postTaskSafely(Runnable task) {
		long curThreadId = android.os.Process.myTid();
		long mainThreadId = getMainThreadId();
		// 如果当前线程是主线程
		if (curThreadId == mainThreadId) {
			task.run();
		} else {// 如果当前线程不是主线程
			getMainThreadHandler().post(task);
		}
	}

	/**在主线程执行一个延时的task*/
	public static void postTaskDelay(Runnable task, long delayMillis) {
		getMainThreadHandler().postDelayed(task, delayMillis);
	}

	/**移除主线程里面的一个task*/
	public static void removeTask(Runnable task) {
		getMainThreadHandler().removeCallbacks(task);
	}

	/**
	 * dp-->px
	 * @param dp
	 * @return
	 */
	public static int dip2Px(int dp) {
		// px/dp = density;
		float density = getResources().getDisplayMetrics().density;
//		System.out.println("density:" + density);
		int px = (int) (dp * density + .5f);
		return px;
	}

	/**
	 * px-->dp
	 * @param
	 * @return
	 */
	public static int px2Dp(int px) {
		// px/dp = density;
		float density = getResources().getDisplayMetrics().density;
//		System.out.println("density:" + density);
		int dp = (int) (px / density + .5f);
		return px;
	}
	//动态添加listview高度解决只显示一条listview的问题
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	private static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	/**
	 * 设置不带描边的用户头像
	 *
	 * @param path
	 * @param imageView
	 */
	public static void setStrokeHeadPic(String path, ImageView imageView, int stroke) {

		// 参数配置
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_default_head)
				.showImageForEmptyUri(R.drawable.icon_default_head)
				.showImageOnFail(R.drawable.icon_default_head)
				.imageScaleType(ImageScaleType.EXACTLY)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new CircleBitmapDisplayer(Color.WHITE, stroke)).build();
		// 设置图片
		if (TextUtils.isEmpty(path)) {

			imageView.setImageResource(R.drawable.icon_default_head);
		} else {

			ImageLoader.getInstance().displayImage(path, imageView, displayImageOptions, animateFirstListener);
		}
	}

	/**
	 * 设置用户头像
	 *
	 * @param path
	 * @param imageView
	 */
	public static void setHeadPic(String path, ImageView imageView) {

		setStrokeHeadPic(path, imageView, 2);

	}

	/**
	 * 设置图片
	 *
	 * @param path
	 * @param imageView
	 */
	public static void setPic(String path, ImageView imageView) {

		// 参数配置
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.loadingpic)
				.showImageForEmptyUri(R.mipmap.loadingpic)
				.showImageOnFail(R.mipmap.loadingpic)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Config.RGB_565)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
		// 设置图片
		if (TextUtils.isEmpty(path)) {

			imageView.setImageResource(R.mipmap.loadingpic);
		} else {

			ImageLoader.getInstance().displayImage(path, imageView, displayImageOptions, animateFirstListener);
		}
	}

	/**
	 * 设置列表图片
	 *
	 * @param path
	 * @param imageView
	 */
	public static void setListPic(String path, ImageView imageView) {

		// 参数配置
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.ic_launcher)
				.showImageForEmptyUri(R.mipmap.ic_launcher)
				.showImageOnFail(R.mipmap.ic_launcher)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Config.RGB_565)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.displayer(new FadeInBitmapDisplayer(100))
				.considerExifParams(true)
				.build();
		// 设置图片
		if (TextUtils.isEmpty(path)) {

			imageView.setImageResource(R.mipmap.ic_launcher);
		} else {

			ImageLoader.getInstance().displayImage(path, imageView, displayImageOptions, animateFirstListener);
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@SuppressLint("NewApi")
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			LogUtils.i("bitmap大小："+loadedImage.getByteCount());
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * 转换图片成圆形
	 *
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap toRoundBitmap(int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static void clarn()
	{
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}
	/**
	 * 校验银行卡卡号
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCard(String cardId) {
		char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
		if(bit == 'N'){
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}
	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * @param nonCheckCodeCardId
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId){
		if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			//如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if(j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
	}

}
