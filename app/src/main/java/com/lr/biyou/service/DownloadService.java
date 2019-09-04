package com.lr.biyou.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.lr.biyou.R;

import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.permission.InstallRationale;
import com.lr.biyou.utils.secret.MD5;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.AsyncTaskUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DownloadService extends Service {

	private static NotificationManager nm;
	private static Notification notification;
	private static boolean cancelUpdate = false;

	//private static ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
	public static Map<Integer,Integer> download = new HashMap<Integer, Integer>();
	public static Context context;
	private static RemoteViews views;

	public static  Context mActivity;


	public static TextView mTextView;
	public static ProgressBar mProgressBar;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		context = this;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public static void downNewFile(final String url, final int notificationId, final String name, final String code,Context activity){
		mActivity = activity;
		if(download.containsKey(notificationId))
			return;
		//notification = new Notification(R.drawable.ic_launcher,"0%", System.currentTimeMillis());

		String id = "my_channel_01";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上需要处理
			NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
			//Toast.makeText(context, mChannel.toString(), Toast.LENGTH_SHORT).show();

			/*mChannel.canBypassDnd();//是否绕过请勿打扰模式
			mChannel.enableLights(true);//闪光灯
			mChannel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
			mChannel.setLightColor(Color.RED);//闪关灯的灯光颜色
			mChannel.canShowBadge();//桌面launcher的消息角标
			mChannel.enableVibration(true);//是否允许震动
			mChannel.getAudioAttributes();//获取系统通知响铃声音的配置
			mChannel.getGroup();//获取通知取到组
			mChannel.setBypassDnd(true);//设置可绕过  请勿打扰模式
			mChannel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
			mChannel.shouldShowLights();//是否会有灯光*/

			mChannel.enableVibration(false);//是否允许震动
			mChannel.setVibrationPattern(new long[]{0});//设置震动模式
			mChannel.setSound(null, null);

			nm.createNotificationChannel(mChannel);

			notification = new Notification.Builder(context,"title")
					.setChannelId(id)
					.setContentTitle("5 new messages")
					.setContentText("hahaha")
					.setSmallIcon(R.mipmap.ic_launcher).build();
		} else {
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"title")
					.setContentTitle("5 new messages")
					.setContentText("hahaha")
					.setSmallIcon(R.mipmap.ic_launcher)
					.setOngoing(true)
					.setChannelId(id);//无效
			notification = notificationBuilder.build();

			notification.sound = null;
			notification.vibrate = null;

			/*	Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			 *//**
			 * sound属性是一个 Uri 对象。 可以在通知发出的时候播放一段音频，这样就能够更好地告知用户有通知到来.
			 * 如：手机的/system/media/audio/ringtones 目录下有一个 Basic_tone.ogg音频文件，
			 * 可以写成： Uri soundUri = Uri.fromFile(new
			 * File("/system/media/audio/ringtones/Basic_tone.ogg"));
			 * notification.sound = soundUri; 我这里为了省事，就去了手机默认设置的铃声
			 *//*
			notification.sound = uri;
			long[] vibrates = new long[]{100, 100, 200};
			notification.vibrate = vibrates;
		*/
		}
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		nm.notify(notificationId, notification);

		//设置任务栏中下载进程显示的views
		views=new RemoteViews(context.getPackageName(), R.layout.apkupdate);
		notification.contentView=views;

/*		notification.icon = android.R.drawable.stat_sys_download;
		// notification.icon=android.R.drawable.stat_sys_download_done;
		notification.tickerText = name + "开始下载";
		notification.when = System.currentTimeMillis();
		notification.defaults = Notification.DEFAULT_LIGHTS;

*/

		Intent notifyIntent = new Intent(Intent.ACTION_MAIN);
		notifyIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		notifyIntent.setComponent(new ComponentName(MainActivity.mInstance.getPackageName(),  MainActivity.mInstance.getLocalClassName()));
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式

		//显示在“正在进行中”
		//notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
		PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId,notifyIntent, 0);
		//notification.setLatestEventInfo(context, name, "0%", contentIntent);
		notification.contentIntent = contentIntent;
		download.put(notificationId, 0);
		//views.setOnClickPendingIntent(R.id.turn_next, contentIntent);
		// 将下载任务添加到任务栏中
		nm.notify(notificationId, notification);
		// 启动线程开始执行下载任务
		downFile(url,notificationId,name,code);
	}

	// 下载更新文件
	private static  void downFile(final String url, final int notificationId, final String name, final String md5Code) {

        AsyncTaskUtil.excute(5, new AsyncTaskUtil.TaskCallback() {
            int total = 0;
            @Override
            public void initOnUI() {
                if (mProgressBar != null ){
                    mProgressBar.setMax(100);
                }
            }
            @Override
            public void cancellTask() {
                //取消任务  移除通知栏
                download.remove(notificationId);
                nm.cancel(notificationId);

            }

            @Override
            public Map<Object, Object> doTask(Map<Object, Object>... maps) {
                Map<Object,Object> map =new HashMap<>();

                File tempFile = null;
                try {
                    HttpClient client = new DefaultHttpClient();
                    // params[0]代表连接的url
                    HttpGet get = new HttpGet(url);
                    HttpResponse response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    if (is != null) {
                        MbsConstans.APP_DOWN_PATH = UtilTools.getAppDownPath(context);
                        File rootFile=new File(MbsConstans.APP_DOWN_PATH);
                        if(!rootFile.exists()&&!rootFile.isDirectory())
                            rootFile.mkdirs();
                        //原有APK文件
                        tempFile = new File(MbsConstans.APP_DOWN_PATH+"/"+url.substring(url.lastIndexOf("/")+1));
                        if (!tempFile.exists()) {
                            tempFile.createNewFile();
                        }
                        if(tempFile.exists()){
                            //如果MD5值相等  已下载最新APK 退出防止反复下载
                            if(md5Code.equals(MD5.md5File(tempFile))){
								/*Message message=myHandler.obtainMessage(2,tempFile);
								message.arg1 = notificationId;
								myHandler.sendMessage(message);*/
                                AsyncTaskUtil.mcallback.updateProgress(-1);
                                map.put("total",""+100);
                                map.put("restlut",tempFile);
                                return map;
                            }else {
                                //删除无用文件
                                tempFile.delete();
                            }
                        }
                        // 已读出流作为参数创建一个带有缓冲的输出流
                        BufferedInputStream bis = new BufferedInputStream(is);

                        // 创建一个新的写入流，讲读取到的图像数据写入到文件中
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        // 已写入流作为参数创建一个带有缓冲的写入流
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        int read;
                        long count = 0;
                        int precent = 0;
                        byte[] buffer = new byte[1024];
                        while ((read = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, read);
                            count += read;
                            precent = (int) (((double) count / length) * 100);
                            total = precent;
                            //判断是否取消当前下载任务
                            if(cancelUpdate){
                                tempFile.delete();
                                //取消更新
                                cancellTask();

                            }
                            // 每下载完成1%就通知任务栏进行修改下载进度
                            if (precent - download.get(notificationId) >= 1) {
                                download.put(notificationId, precent);
                                AsyncTaskUtil.mcallback.updateProgress(precent);
                            }
                        }
                        bos.flush();
                        bos.close();
                        fos.flush();
                        fos.close();
                        is.close();
                        bis.close();
                    }


                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    if (tempFile.exists())
                        tempFile.delete();

                    total = 0;
                    AsyncTaskUtil.mcallback.updateProgress(-2);

                } catch (IOException e) {
                    e.printStackTrace();
                    if (tempFile.exists())
                        tempFile.delete();

                    total = 0;
                    AsyncTaskUtil.mcallback.updateProgress(-2);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (tempFile.exists())
                        tempFile.delete();

                    total = 0;
                    AsyncTaskUtil.mcallback.updateProgress(-2);

                }
                map.put("total",""+total);
                map.put("restlut",tempFile);
                return map;
            }

            @Override
            public void progressTask(Integer... values) {
                int precent =values[0];

                //下载失败
                if(precent == -2){
                    if (mProgressBar != null && mTextView != null){
                        mProgressBar.setProgress(0);
                        mTextView.setText("下载失败");
                    }
                    cancellTask();


                }
                //已下载
                if(precent == -1){
                    if(mProgressBar != null){
                        mProgressBar.setProgress(100);
                    }
                    cancellTask();

                }
                //正在下载
                if(precent > 0 && precent< 99){
                    if (mProgressBar != null && mTextView != null){
                        mProgressBar.setProgress(precent);
                        mTextView.setText("已下载"+precent+"%");
                    }
                    //通知栏信息
                    views.setTextViewText(R.id.apkDownTextView,"已下载"+precent+"%");
                    views.setProgressBar(R.id.apkDownProgressBar,100,precent,false);
                    notification.contentView=views;
                    nm.notify(notificationId, notification);
                }

                //下载完成
                if(precent > 99){
                    if (mProgressBar != null && mTextView != null){
                        mProgressBar.setProgress(100);
                        mTextView.setText("已下载"+100+"%");
                    }
                    views.setTextViewText(R.id.apkDownTextView,"已下载"+100+"%");
                    views.setProgressBar(R.id.apkDownProgressBar,100,100,false);
                    nm.notify(notificationId, notification);
                    // 下载完成后清除所有下载信息
                    cancellTask();
                }

            }

            @Override
            public void resultTask(Map<Object, Object> objectObjectMap) {
                //执行安装提示
                String total = objectObjectMap.get("total")+"";
                if(total.equals("100")){
                    File file= (File) objectObjectMap.get("restlut");
                    install(file,context);
                }
            }
        });
	}


	// 安装下载后的apk文件
	public static  void install(File file, Context context) {


        LogUtilDebug.i("---------------------------------------------------------------------------",file.getAbsolutePath());
		/**
		 * Install package.
		 */
        AndPermission.with(mActivity)
                .install()
                .file(file)
                .rationale(new InstallRationale())
                .onGranted(new Action<File>() {
                    @Override
                    public void onAction(File data) {

                    }
                })
                .onDenied(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                        // The user refused to install.
                    }
                })
                .start();


    }


}
