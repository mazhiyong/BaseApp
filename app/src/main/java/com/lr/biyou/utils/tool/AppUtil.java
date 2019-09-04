package com.lr.biyou.utils.tool;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.lr.biyou.basic.MbsConstans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 

 */
public class AppUtil {

	private static AppUtil appUtil;
	private Context mContext;
	private static PackageManager pmanager;
	private List<PackageInfo> pkgList = new ArrayList<PackageInfo>();
	private List<PackageInfo> packinfos = new ArrayList<PackageInfo>();

	public AppUtil(Context mContext){
		this.mContext = mContext;
		pmanager = mContext.getPackageManager();
		if(pmanager!=null){
			pkgList = pmanager.getInstalledPackages(0);
			packinfos = pmanager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
		}
	}

	public static AppUtil getInstance(Context context){
		if (appUtil == null) {
			appUtil = new AppUtil(context);
		}
		return appUtil;
	}


	/**
	 * 获取本地应用程序的版本号
	 * @return
	 */
	public int getAppVersion(){

		int versionCode = 1;
		try {  

			PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			// 当前应用的版本名称
			MbsConstans.UpdateAppConstans.VERSION_APP_NAME= info.versionName;
			// 当前版本的版本号
			versionCode = info.versionCode;
			MbsConstans.UpdateAppConstans.VERSION_APP_CODE = versionCode;
			// 当前版本的包名  
			//String packageNames = info.packageName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		return versionCode;
	}

	//	/**
	//	 * 展示更新信息
	//	 */
	//	public void showVersionMsg(){
	//
	//		if(ParamUtils.VERSION_NET_CODE>ParamUtils.VERSION_APP_CODE){
	//			MessageBox.Builder mBuilder = new MessageBox.Builder(mContext)
	//			.setTitle("软件升级") 
	//			.setMessage("发现新版本,建议立即更新使用.\n"+ParamUtils.VERSION_NET_UPDATE_MSG) 
	//			.setPositiveButton("更新", 
	//					new DialogInterface.OnClickListener() { 
	//				public void onClick(DialogInterface dialog, 
	//						int which) { 
	//					dialog.cancel();
	//				} 
	//			}) 
	//			.setNegativeButton("取消", 
	//					new DialogInterface.OnClickListener() { 
	//				public void onClick(DialogInterface dialog, 
	//						int which) { 
	//					dialog.cancel(); 
	//				} 
	//			}); 
	//			mBuilder.create().show(); 
	//		}
	//	}

	//	/**
	//	 * 是否安装apk
	//	 */
	//	public void isInstall(final String filePath){
	//
	//		MessageBox.Builder mBuilder = new MessageBox.Builder(mContext)
	//		.setTitle("安装提示") 
	//		.setMessage("新版本已经下载完毕是否安装?") 
	//		.setPositiveButton("安装", 
	//				new DialogInterface.OnClickListener() { 
	//			public void onClick(DialogInterface dialog, 
	//					int which) { 
	//				dialog.cancel();
	//				appUtil.installApk(filePath);
	//			} 
	//		}) 
	//		.setNegativeButton("取消", 
	//				new DialogInterface.OnClickListener() { 
	//			public void onClick(DialogInterface dialog, 
	//					int which) { 
	//				dialog.cancel(); 
	//			} 
	//		}); 
	//		mBuilder.create().show(); 
	//	}


	/**
	 * 进行apk安装
	 * @param filePath	需要安装的apk路径
	 */
	public void installApk(String filePath){

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	public boolean isMobile_spExist(String packageName) {

		for (int i = 0; i < pkgList.size(); i++) {

			PackageInfo pI = pkgList.get(i);
			if (pI.packageName.equalsIgnoreCase(packageName))//根据安装的应用的包名判断
				return true;
		}


		return false;
	}

	public List<Map<String,Object>> getInstallPackages(){
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		for(PackageInfo info : packinfos) {
			String[] premissions = info.requestedPermissions;
			if (premissions != null && premissions.length > 0) {
				for (String premission : premissions) {
					if ("android.permission.INTERNET".equals(premission)) {
						String processName = info.applicationInfo.processName;

						if(!processName.contains(":")){
							//获取应用程序名称
							String appName = info.applicationInfo.loadLabel(pmanager).toString();;
							String packageName = info.packageName;
						}
					}
				}
			}
		}

		return list;
	}

	/** 
	 * 用来判断服务是否运行. 
	 * @param
	 * @param className 判断的服务名字 
	 * @return true 在运行 false 不在运行 
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false; 
		ActivityManager activityManager = (ActivityManager)
				mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList  = activityManager.getRunningServices(30);
		if (!(serviceList.size()>0)) { 
			return false; 
		} 
		for (int i=0; i<serviceList.size(); i++) { 
			if (serviceList.get(i).service.getClassName().equals(className) == true) { 
				isRunning = true; 
				break; 
			} 
		} 
		return isRunning; 
	}


	/**
	 * 获取当前应用程序的包名
	 * @param context 上下文对象
	 * @return 返回包名
	 */
	public static String getAppProcessName(Context context) {
		//当前应用pid
		int pid = android.os.Process.myPid();
		//任务管理类
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//遍历所有应用
		List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo info : infos) {
			if (info.pid == pid)//得到当前应用
				return info.processName;//返回包名
		}
		return "";
	}


//	public void getNetAppInfo(){
//		Map<String, Object> postMap = new HashMap<String, Object>();
//		postMap.put("code", "delivery_h2y");
//
//		Map<String, Object> resultMap = mRequestMbs.getResultMap(postMap, CommandCode.App.value(), MethodCode.UpdateApp.value());
//		if (resultMap != null) {
//
//
//			SysApp sysApp =   JSONUtil.getObject(resultMap.get(MbsEnum.PostKey.OBJECT_DATA.value()), SysApp.class);
//			UpdateAppConstans.VERSION_NET_CODE =  (int) sysApp.getVersionCode();
//			UpdateAppConstans.VERSION_NET_APK_NAME = sysApp.getApkName();
//			UpdateAppConstans.VERSION_NET_UPDATE_MSG = sysApp.getVersionUpdateMsg();
//			UpdateAppConstans.VERSION_NET_APK_URL = sysApp.getDownUrl(); 
//			UpdateAppConstans.VERSION_MD5_CODE = sysApp.getMd5Code();
//		}
//	}

	//	@Override
	//	protected Map<String, Object> doInBackground(String... params) {
	//		// TODO Auto-generated method stub
	//		Map<String, Object> postMap = new HashMap<String, Object>();
	//		postMap.put("code", "delivery_h2y");
	//		
	//		Map<String, Object> resultMap = mRequestMbs.getResultMap(postMap, CommandCode.App.value(), MethodCode.UpdateApp.value());
	//		
	//		return resultMap;
	//	}
	//
	//	@Override
	//	protected void onPostExecute(Map<String, Object> resultMap) {
	//		super.onPostExecute(resultMap);
	//		Result result =   JSONUtil.getObject(resultMap.get(MbsEnum.PostKey.RESULT_DATA.value()),Result.class);
	//		if (result.getOpFlg() == ResultCode.getDataSuccess.value()) {
	//			SysApp sysApp =   JSONUtil.getObject(resultMap.get(MbsEnum.PostKey.OBJECT_DATA.value()), SysApp.class);
	//			UpdateAppConstans.VERSION_NET_CODE =  (int) sysApp.getVersionCode();
	//			UpdateAppConstans.VERSION_NET_APK_NAME = sysApp.getApkName();
	//		}
	//	}

}
