package com.lr.biyou.basic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.widget.Toast;

import com.lr.biyou.BuildConfig;
import com.lr.biyou.R;
import com.lr.biyou.utils.tool.AppContextUtil;


import com.lr.biyou.utils.tool.LogUtilDebug;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.OkHttpClient;

import static com.wanou.framelibrary.okgoutil.OkGoUtils.TIMEOUT_SECOND;

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
/*		Context context = getApplicationContext();
		// 获取当前包名
		String packageName = context.getPackageName();
		// 获取当前进程名
		String processName = getProcessName(android.os.Process.myPid());
		// 设置是否为上报进程
		CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
		strategy.setUploadProcess(processName == null || processName.equals(packageName));

		CrashReport.initCrashReport(context, "186ece60d5", true, strategy);*/

		/**
		 * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
		 * 第一个参数：应用程序上下文
		 * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
		 */
		BGASwipeBackHelper.init(this,  null);

		Bugly.init(this, "5156cc29ea", false);

		RongIM.init(this);
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

		//连接融云
		//17319449662
		String token1 = "kcX1ye0YGBlG7iWZ5jHpq3Q66evRHTAQkKthDvKlCzd8eLCcwj5jgjOIbvNeHYutzyYe1ed3QZTcsIDyo3AmMdK3Z331/2kt";
		//15561400223
		String token2 = "AUrI48u7jSC9yV8XmieXLxOUx7H8bap2AjZHooKpeeWhNSrnoHFlS2nmFye32TQGajQ85yilCTlPrNGB16o4rBnyf9oopTJ+";

		RongIM.connect(token1, new RongIMClient.ConnectCallback() {

			/**
			 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
			 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
			 */
			@Override
			public void onTokenIncorrect() {

			}

			/**
			 * 连接融云成功
			 * @param userid 当前 token 对应的用户 id
			 */
			@Override
			public void onSuccess(String userid) {
				LogUtilDebug.i("show", "rongyun--onSuccess" + userid);
			}

			/**
			 * 连接融云失败
			 * @param errorCode 错误码，可到官网 查看错误码对应的注释
			 */
			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {
				LogUtilDebug.i("show", "rongyun--onError" + errorCode);
			}
		});
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
