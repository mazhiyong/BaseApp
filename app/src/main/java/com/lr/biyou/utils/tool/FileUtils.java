package com.lr.biyou.utils.tool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.basic.MbsConstans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

	public static String SDPATH = MbsConstans.PHOTO_PATH;

	public static String saveBitmap(Bitmap bm, String picName) {
		String filePath = "";
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			filePath = SDPATH + File.separator+picName + ".png";
			File f = new File(SDPATH, picName + ".png");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filePath;
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			LogUtilDebug.i("打印log日志","createSDDir:" + dir.getAbsolutePath());
			LogUtilDebug.i("打印log日志","createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir(String path) {

		try {
			File dir = new File(path);
			if (dir == null || !dir.exists() || !dir.isDirectory())
				return;

			for (File file : dir.listFiles()) {
				if (file.isFile()){
					file.delete();
//				updateFileFromdatabase(mContext,file);
					updateMedia(file.getAbsolutePath());
				}	else if (file.isDirectory()){
					deleteDir(path);
				}
			}
			dir.delete();
		}catch (Exception e){
			e.printStackTrace();
			LogUtilDebug.i("FileUtils删除缓存文件异常"+path,"没有给予权限捕获异常防止崩溃，不影响其他正常操作");
		}

	}



	public static void updateMedia(String path){

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//当大于等于Android 4.4时
			MediaScannerConnection.scanFile(BasicApplication.getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
				@Override
				public void onScanCompleted(String path, Uri uri) {
					Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					mediaScanIntent.setData(uri);
					BasicApplication.getContext().sendBroadcast(mediaScanIntent);
				}
			});

		}else{//Andrtoid4.4以下版本
			BasicApplication.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.fromFile((new File(path).getParentFile()))));
		}

	}



	//删除文件后更新数据库  通知媒体库更新文件夹
	public static void updateFileFromdatabase(Context context, File file){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
			MediaScannerConnection.scanFile(context, paths, null, null);
			MediaScannerConnection.scanFile(context, new String[] {
							file.getAbsolutePath()},
					null, new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri)
						{
						}
					});
		} else {
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		}
	}




	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}
	/**
	 * 获取指定文件大小
	 * @return
	 * @throws Exception 　　
	 */
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}
}
