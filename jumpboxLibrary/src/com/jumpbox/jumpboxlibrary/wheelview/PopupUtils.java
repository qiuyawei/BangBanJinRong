package com.jumpbox.jumpboxlibrary.wheelview;

import java.util.Calendar;

import com.jumpbox.jumpboxlibrary.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class PopupUtils {
	private static DateArrayAdapter adapter;

	private static OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;//
			int n_month = month.getCurrentItem() + 1;//
			day.setViewAdapter(new NumericWheelAdapter(wheel.getContext(), 1, getDay(n_year, n_month), "%02d"));
		}
	};

	public static void show(Context context, final LinearLayout parent, final onSelectFinishListener listener) {
		final PopupWindow popup = new PopupWindow(context);
		popup.setHeight(LayoutParams.WRAP_CONTENT);
		popup.setWidth(LayoutParams.MATCH_PARENT);
		View view = View.inflate(context, R.layout.layout_date, null);
		final WheelView start_time = (WheelView) view.findViewById(R.id.start_time);
		final WheelView end_time = (WheelView) view.findViewById(R.id.end_time);
		final TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
		final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);

		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.setFocusable(true);

		final String startTimes[] = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
				"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };
		adapter = new DateArrayAdapter(context, startTimes);
		start_time.setViewAdapter(adapter);
		start_time.setCurrentItem(6);

		final String[] strs = new String[59];
		for (int i = 0; i < 59; i++) {
			String t = i + 1 + "";
			if (i + 1 < 10) {
				t = "0" + (i + 1);
			}
			strs[i] = t;
		}
		adapter = new DateArrayAdapter(context, strs);
		end_time.setViewAdapter(adapter);
		end_time.setCurrentItem(30);

		start_time.setVisibleItems(7);
		end_time.setVisibleItems(7);

		popup.setContentView(view);
		popup.setAnimationStyle(R.style.PopupAnimation);
		popup.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}

		});
		tv_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
				String endTime = strs[end_time.getCurrentItem()];
				String startTime = startTimes[start_time.getCurrentItem()];
				if (listener != null) {
					listener.onSelectFinish(startTime, endTime, "", "");
				}
			}
		});
	}

	/**
	 * 鍒濆鍖杙opupWindow
	 * 
	 * @param view
	 */
	public static PopupWindow menuWindow;
	private static WheelView year;
	private static WheelView month;
	private static WheelView day;

	private static Dialog dialog;

	private static Dialog myDialog;

	private static Dialog cityDialog;

	public static void showPopwindow(Context context, View view) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setContentView(view, new
		// LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		// 璁剧疆鏄剧ず鍔ㄧ敾
		window.setWindowAnimations(R.style.PopupAnimation);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 浠ヤ笅杩欎袱鍙ユ槸涓轰簡淇濊瘉鎸夐挳鍙互姘村钩婊″睆
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 璁剧疆鏄剧ず浣嶇疆
		dialog.onWindowAttributesChanged(wl);
		// 璁剧疆鐐瑰嚮澶栧洿瑙ｆ暎
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public static View getDataPick(final Context context, final onSelectFinishListener listener) {
		Calendar c = Calendar.getInstance();
		final int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 閫氳繃Calendar绠楀嚭鐨勬湀鏁拌+1
		int curDate = c.get(Calendar.DATE);
		final View view = View.inflate(context, R.layout.dialog_datapick, null);

		year = (WheelView) view.findViewById(R.id.year);
		year.setViewAdapter(new NumericWheelAdapter(context, curYear, curYear + 10));
		// year.setLabel("骞�");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setViewAdapter(new NumericWheelAdapter(context, 1, 12));
		// month.setLabel("鏈�");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		day.setViewAdapter(new NumericWheelAdapter(context, 1, getDay(curYear, curMonth), "%02d"));

		// day.setLabel("鏃�");
		day.setCyclic(true);

		year.setVisibleItems(7);
		month.setVisibleItems(7);
		day.setVisibleItems(7);
		year.setCurrentItem(0);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (listener != null) {
					listener.onSelectFinish(curYear + year.getCurrentItem() + "", month.getCurrentItem() + 1 + "",
							day.getCurrentItem() + 1 + "");
				}
			}
		});
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
		return view;
	}

	public static View getDataPick2(final Context context, final onSelectFinishListener listener) {
		Calendar c = Calendar.getInstance();
		final int curYear = c.get(Calendar.YEAR) - 100;
		int curMonth = c.get(Calendar.MONTH) + 1;// 閫氳繃Calendar绠楀嚭鐨勬湀鏁拌+1
		int curDate = c.get(Calendar.DATE);
		final View view = View.inflate(context, R.layout.dialog_datapick, null);

		year = (WheelView) view.findViewById(R.id.year);
		year.setViewAdapter(new NumericWheelAdapter(context, curYear, curYear + 100));
		// year.setLabel("骞�");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setViewAdapter(new NumericWheelAdapter(context, 1, 12));
		// month.setLabel("鏈�");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		day.setViewAdapter(new NumericWheelAdapter(context, 1, getDay(curYear, curMonth), "%02d"));

		// day.setLabel("鏃�");
		day.setCyclic(true);

		Calendar calendar = Calendar.getInstance();
		int mCurYear = calendar.get(Calendar.YEAR);
		int item = 100 - (mCurYear - 1980);
		year.setCurrentItem(item);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		year.setVisibleItems(7);
		month.setVisibleItems(7);
		day.setVisibleItems(7);

		TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
		tv_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (listener != null) {
					listener.onSelectFinish(curYear + year.getCurrentItem() + "", month.getCurrentItem() + 1 + "",
							day.getCurrentItem() + 1 + "");
				}
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
		return view;
	}

	public interface onSelectFinishListener {
		public void onSelectFinish(String year, String month, String day);

		public void onSelectFinish(String startDate, String startTime, String endDate, String endTime);
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	public static class DateArrayAdapter extends ArrayWheelAdapter<String> {

		public DateArrayAdapter(Context context, String[] items) {
			super(context, items);
		}

		@Override
		public CharSequence getItemText(int index) {
			// TODO Auto-generated method stub
			return super.getItemText(index);
		}

		@Override
		public int getItemsCount() {
			return super.getItemsCount();
		}

	}

	public static void selectCity(Context context, final OnFinishListener listener) {
		CityUtils cityUtils = CityUtils.getInstance();
		cityUtils.showSelectDialog(context);
	}

	/**
	 * 閫氱敤鐨凞ialog
	 * 
	 * @param context
	 * @param view
	 */
	public static void setDialog(Context context, View view) {
		myDialog = new Dialog(context);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(view);
		Window window = myDialog.getWindow();
		// 璁剧疆鏄剧ず鍔ㄧ敾
		window.setWindowAnimations(R.style.PopupAnimation);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 浠ヤ笅杩欎袱鍙ユ槸涓轰簡淇濊瘉鎸夐挳鍙互姘村钩婊″睆
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 璁剧疆鏄剧ず浣嶇疆
		myDialog.onWindowAttributesChanged(wl);
		// 璁剧疆鐐瑰嚮澶栧洿瑙ｆ暎
		myDialog.setCanceledOnTouchOutside(true);
		myDialog.show();
	}

	public static void showView(Context context, String[] content, final OnFinishListener listener) {
		View view = View.inflate(context, R.layout.custom_select_view, null);
		final WheelView wv_view = (WheelView) view.findViewById(R.id.wv_view);
		TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
		TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(dialog != null){
					dialog.dismiss();
				}
			}
		});
		tv_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (listener != null) {
					listener.onFinish(wv_view);
				}
			}
		});

		wv_view.setViewAdapter(new ArrayWheelAdapter<String>(context, content));
		wv_view.setCurrentItem(1);
		wv_view.setVisibleItems(7);

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setContentView(view, new
		// LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		// 璁剧疆鏄剧ず鍔ㄧ敾
		window.setWindowAnimations(R.style.PopupAnimation);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 浠ヤ笅杩欎袱鍙ユ槸涓轰簡淇濊瘉鎸夐挳鍙互姘村钩婊″睆
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 璁剧疆鏄剧ず浣嶇疆
		dialog.onWindowAttributesChanged(wl);
		// 璁剧疆鐐瑰嚮澶栧洿瑙ｆ暎
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

	}

	public interface OnFinishListener {
		public void onFinish(WheelView view);

		public void onFinish(String country, String city, String ccity);
	}
}
