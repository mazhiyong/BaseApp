package com.lr.biyou.manage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.Stack;
public class ActivityManager {
	//peek 不改变栈的值(不删除栈顶的值)，pop会把栈顶的值删除。
	/**
	 * 堆栈处理Activity
	 */
	private static Stack<Activity> activityStack;

	/**
	 * 实现单例模式
	 */
	private static ActivityManager instance = new ActivityManager();

	private ActivityManager() {

	}

	public static ActivityManager getInstance() {

		//		if (instance == null) {
		//
		//		}
		return instance;
	}

	/**
	 * 返回到指定的Activity中
	 * @param clazz
	 */
	public void backTo(Class<?> clazz,boolean b){
		if (clazz != null) {
			Log.i("popActivity", clazz.getName());
			//在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			String ps = clazz.getName();
			while(true){
				if(!activityStack.isEmpty()){
					Activity activity = activityStack.peek();

					if((activity.getLocalClassName()).equalsIgnoreCase(ps)){
						((BasicActivity)activity).mIsRefresh = b;
						break;
					}else {

						activityStack.pop().finish();
					}
				}else
					break;
			}
		}
	}

	/**
	 * 返回到指定的Activity中
	 * @param clazz
	 */
	public void backToMainActivity(Class<?> clazz, int i){
		if (clazz != null) {
			Log.i("popActivity", clazz.getName());
			//在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			String ps = clazz.getName();
			while(true){
				if(!activityStack.isEmpty()){
					Activity activity = activityStack.peek();
					if((activity.getPackageName()+"."+activity.getLocalClassName()).equalsIgnoreCase(ps)){
						if (activity instanceof MainActivity) {

						}
						break;
					}else {
						activityStack.pop().finish();
					}
				}else
					break;
			}
		}
	}

	public void removeActivity(Activity activity){
		if(activityStack!=null && !activityStack.isEmpty()){
			activityStack.remove(activity);
			LogUtilDebug.i("打印log日志","栈中的activity "+activityStack+"     已经移除的activity   "+activity);
		}
	}

	//退出栈顶Activity
	public void popActivity(Activity activity) {
		if (activity != null) {
			Log.i("popActivity", activity.getPackageName()+"."+activity.getLocalClassName());
			//在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	//退出栈顶Activity
	public void popActivity() {
		if(activityStack!=null && !activityStack.isEmpty()){
			//在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			Activity mActivity = activityStack.peek();
			popActivity(mActivity);
		}
	}

	public void popActivity(Context context) {
		if (context != null) {
			Log.i("popActivity", context.getPackageName());
			//在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			activityStack.pop().finish();
		}
	}

	public Activity peepActivity(){
		if(activityStack!=null && !activityStack.isEmpty()){
			return activityStack.peek();
		}
		return null;
	}


	//获得当前栈顶Activity
	public Activity currentActivity() {
		Activity activity = null;
		if(!activityStack.empty())
			activity= activityStack.lastElement();
		return activity;
	}

	//将当前Activity推入栈中
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	//退出栈中所有Activity
	public void popAllActivityExceptOne(Class<?> clazz) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(clazz)) {
				break;
			}
			popActivity(activity);
		}
	}

	//退出栈中所有Activity
	public void close() {
		Activity activity = currentActivity();
		activityStack.remove(activity);
		//Stack<Activity> stack2 = new Stack<Activity>();
		while (true) {

			if(!activityStack.isEmpty())
				//stack2.push(activityStack.pop());
				activityStack.pop().finish();
			else {
				break;
			}
		}
		//关闭项目
		activity.finish();
	}
}
