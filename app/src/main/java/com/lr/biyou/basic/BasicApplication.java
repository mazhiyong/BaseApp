package com.lr.biyou.basic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.lr.biyou.BuildConfig;
import com.lr.biyou.R;
import com.lr.biyou.rongyun.common.ErrorCode;
import com.lr.biyou.rongyun.contact.PhoneContactManager;
import com.lr.biyou.rongyun.im.IMManager;
import com.lr.biyou.rongyun.utils.SearchUtils;
import com.lr.biyou.rongyun.wx.WXManager;
import com.lr.biyou.utils.tool.AppContextUtil;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.wanou.framelibrary.okgoutil.websocket.WsManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import io.rong.imlib.ipc.RongExceptionHandler;
import okhttp3.OkHttpClient;

import static com.wanou.framelibrary.okgoutil.OkGoUtils.TIMEOUT_SECOND;
import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

public class BasicApplication extends MultiDexApplication {

	int appCount=0;

	private static Context mContext;
	public static Typeface typeFace;
	/*public void setTypeface(){
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/gagayi.ttf");
		try
		{
			Field field = Typeface.class.getDeclaredField("SERIF");
			field.setAccessible(true);
			field.set(null, typeFace);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}*/

	private static WsManager wsManager;
	private static WsManager wsManager1;
	private static WsManager wsManager2;
	private static WsManager wsManager3;

	public static WsManager getWsManager() {
		return wsManager;
	}
	public static WsManager getWsManager1() {
		return wsManager1;
	}

	public static WsManager getWsManager2() {
		return wsManager2;
	}

	public static WsManager getWsManager3() {
		return wsManager3;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this) ;
		// 安装tinker
		Beta.installTinker();

	}
	@Override
	public void onCreate() {
		super.onCreate();
		registerActivityListener();
		//setTypeface();
		AppContextUtil.init(this);

		/*Context context = getApplicationContext();
		// 获取当前包名
		String packageName = context.getPackageName();
		// 获取当前进程名
		String processName = getProcessName(android.os.Process.myPid());
		// 设置是否为上报进程
		CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
		strategy.setUploadProcess(processName == null || processName.equals(packageName));

		CrashReport.initCrashReport(context, "5f54eab52c", true, strategy);*/

		/**
		 * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
		 * 第一个参数：应用程序上下文
		 * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
		 */
		BGASwipeBackHelper.init(this,  null);

		Bugly.init(this, "5f54eab52c", true);

		//RongIM.init(this);
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				/*CrashHandler mUncaughtHandler= CrashHandler.getmContext();
				mUncaughtHandler.init(getApplicationContext());*/
				mContext = getApplicationContext();
			}
		});
		initWS();
		initWS1();
		initWS2();
		initWS3();


		// 初始化 bugly BUG 统计
		//CrashReport.initCrashReport(getApplicationContext());

		ErrorCode.init(this);

		/*
		 * 以上部分在所有进程中会执行
		 */
		if (!getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
			return;
		}
		/*
		 * 以下部分仅在主进程中进行执行
		 */
		// 初始化融云IM SDK，初始化 SDK 仅需要在主进程中初始化一次
		IMManager.getInstance().init(this);
		Stetho.initializeWithDefaults(this);

		SearchUtils.init(this);

		Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

		// 微信分享初始化
		WXManager.getInstance().init(this);

		PhoneContactManager.getInstance().init(this);
	}


	private void initWS() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("webSocket");
		if (BuildConfig.DEBUG) {
			loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
		}
		loggingInterceptor.setColorLevel(Level.WARNING);
		builder.addInterceptor(loggingInterceptor);
		builder.readTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.writeTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.connectTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.readTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.writeTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.connectTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		//cookie的缓存设置
		builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
		OkHttpClient websocketBuild = builder.build();
		wsManager = new WsManager.Builder(this)
				.wsUrl(MbsConstans.DEPTH_LEVER)
				.needReconnect(true)
				.client(websocketBuild)
				.build();


	}
	private void initWS1() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("webSocket");
		if (BuildConfig.DEBUG) {
			loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
		}
		loggingInterceptor.setColorLevel(Level.WARNING);
		builder.addInterceptor(loggingInterceptor);
		builder.readTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.writeTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.connectTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.readTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.writeTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.connectTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		//cookie的缓存设置
		builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
		OkHttpClient websocketBuild = builder.build();
		wsManager1 = new WsManager.Builder(this)
				.wsUrl(MbsConstans.CURRENT_PRICE_URL)
				.needReconnect(true)
				.client(websocketBuild)
				.build();


	}

	private void initWS2() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("webSocket");
		if (BuildConfig.DEBUG) {
			loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
		}
		loggingInterceptor.setColorLevel(Level.WARNING);
		builder.addInterceptor(loggingInterceptor);
		builder.readTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.writeTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.connectTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.readTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.writeTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.connectTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		//cookie的缓存设置
		builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
		OkHttpClient websocketBuild = builder.build();
		wsManager2 = new WsManager.Builder(this)
				.wsUrl(MbsConstans.KLINE_WEBSOCKET_URL)
				.needReconnect(true)
				.client(websocketBuild)
				.build();


	}


	private void initWS3() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("webSocket");
		if (BuildConfig.DEBUG) {
			loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
		}
		loggingInterceptor.setColorLevel(Level.WARNING);
		builder.addInterceptor(loggingInterceptor);
		builder.readTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.writeTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.connectTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.readTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.writeTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		builder.connectTimeout(TIMEOUT_SECOND, TimeUnit.MILLISECONDS);
		//cookie的缓存设置
		builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
		OkHttpClient websocketBuild = builder.build();
		wsManager3 = new WsManager.Builder(this)
				.wsUrl(MbsConstans.HUOBI_WEBSOCKET)
				.needReconnect(true)
				.client(websocketBuild)
				.build();


	}



	public static Context getContext() {
		return mContext;
	}


	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
	}



	/**
	 * 获取进程号对应的进程名
	 *
	 * @param pid 进程号
	 * @return 进程名
	 */
	private static String getProcessName(int pid) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
			String processName = reader.readLine();
			if (!TextUtils.isEmpty(processName)) {
				processName = processName.trim();
			}
			return processName;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		return null;
	}

	private void registerActivityListener() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			registerActivityLifecycleCallbacks(
					new ActivityLifecycleCallbacks() {

						@Override
						public void onActivityCreated(Activity activity, Bundle
								savedInstanceState) {
							// TODO Auto-generated method stub
						}
						@Override
						public void onActivityStarted(Activity activity) {
							// TODO Auto-generated method stub
							appCount++;
						}
						@Override
						public void onActivityResumed(Activity activity) {
							// TODO Auto-generated method stub
						}
						@Override
						public void onActivityPaused(Activity activity) {
							// TODO Auto-generated method stub
						}
						@Override
						public void onActivityStopped(Activity activity) {
							// TODO Auto-generated method stub
							appCount--;
							if(appCount==0){
								Toast.makeText(getApplicationContext(),
										getResources().getString(R.string.app_name_main)+"应用进入后台运行",
										Toast.LENGTH_LONG).show();
							}
						}
						@Override
						public void onActivitySaveInstanceState(Activity activity, Bundle
								outState) {
							// TODO Auto-generated method stub
						}
						@Override
						public void onActivityDestroyed(Activity activity) {
							// TODO Auto-generated method stub
						}
					}
			);
		}
	}
}
