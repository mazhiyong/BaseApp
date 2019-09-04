package com.lr.biyou.utils.tool;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * bitmap对象操作工具类，最大可能性的降低OOM风险<br>
 * 提供的操作包括：获得一定宽高的缩略图、旋转图片、压缩保存图片、获得图片文件的旋转角度属性
 *
 *
 */
public class BitmapResizeUtil {

	/**
	 * 获得重置大小后的bitmap图片,同时解决OOM问题
	 *
	 * @param path
	 *            文件在本地的路径
	 * @param width
	 *            重置后的宽度
	 * @param height
	 *            重置后的高度
	 * @return bitmap 返回重置后的bmp图像
	 */
	public static Bitmap getResizeBitmap(String path, int width, int height) {

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeFile(path, opt);
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		opt.inSampleSize = 1;
		if (picWidth > picHeight) {
			if (picWidth > width)
				opt.inSampleSize = picWidth / width;
		} else {
			if (picHeight > height)
				opt.inSampleSize = picHeight / height;
		}
		opt.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, opt);
		return bm;
	}

	/**
	 * 获得重置大小后的bitmap图片,同时解决OOM问题
	 *
	 * @param path
	 *            文件在本地的路径
	 * @return bitmap 返回重置后的bmp图像
	 */
	public static Bitmap getResizeBitmap(String path) {

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeFile(path, opt);
		if (null != bm) {
			opt.inSampleSize = 2;
		} else {
			opt.inSampleSize = 1;
		}

		opt.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, opt);
		return bm;
	}

	/**
	 * 获得重置大小后的bitmap图片,同时解决OOM问题
	 *
	 * @param data
	 *            图片文件的字节数据
	 * @param width
	 *            重置后的宽度
	 * @param height
	 *            重置后的高度
	 * @return bitmap 返回重置后的bmp图像
	 */
	public static Bitmap getResizeBitmap(byte[] data, int width, int height) {

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		opt.inSampleSize = 1;
		if (picWidth > picHeight) {
			if (picWidth > width)
				opt.inSampleSize = picWidth / width;
		} else {
			if (picHeight > height)

				opt.inSampleSize = picHeight / height;
		}
		opt.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
		return bm;
	}

	public static String saveImg(ByteArrayOutputStream baos, String savePath,
                                 String name) {
		File mediaFile = new File(savePath + name);
		if (mediaFile.exists()) {
			mediaFile.delete();
		}
		if (!new File(savePath).exists()) {
			new File(savePath).mkdirs();
		}
		try {
			mediaFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(mediaFile);
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
			bitmap.compress(CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
			fos = null;
			bitmap = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mediaFile.getPath();
	}

	public static String getThumbUploadPath(String oldPath, String newPath,
                                            int bitmapMaxWidth, String name) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(oldPath, options);
		int height = options.outHeight;
		int width = options.outWidth;

		int reqHeight;
		int reqWidth;
		if (height > width) {
			reqHeight = bitmapMaxWidth;
			reqWidth = (width * bitmapMaxWidth) / height;
		} else {
			reqWidth = bitmapMaxWidth;
			reqHeight = (height * bitmapMaxWidth) / width;
		}

		// // 在内存中创建bitmap对象，这个对象按照缩放大小创建的
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;

		int degree = RotatePictureUtil.getBitmapDegree(oldPath);

		Bitmap bmpSelected = RotatePictureUtil.rotateBitmapByDegree(
				BitmapResizeUtil.getResizeBitmap(oldPath, reqWidth, reqHeight),
				degree);

		// Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
		ByteArrayOutputStream baos = compressImage(Bitmap.createScaledBitmap(
				bmpSelected, reqWidth, reqHeight, false));
		return saveImg(baos, newPath, name);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
											 int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}


	/**
	 * 质量压缩方法
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage2(Bitmap image) {

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;


		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset(); // 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

		opts.inJustDecodeBounds = false;
		return bitmap;
	}

	public static ByteArrayOutputStream compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ((baos.toByteArray().length / 1024) > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}

		if (null != image) {
			image = null;
		}
		// 把ByteArrayInputStream数据生成图片
		return baos;
	}

	// 将指定路径的图片压缩到200kb以内并存储在原路径
	public static void compressBitmap(String oldPath, String newPath,
                                      String filename) {

		Bitmap bitmap = null;
		try {
			File file = new File(newPath, filename);
			File path = new File(newPath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			// bitmap = BitmapFactory.decodeFile(path); //方式一,某些手机会OOM,如:S5
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(oldPath, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 960 * 960);
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(oldPath, opts); // 方式二,解决OOM问题

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 100不压缩;
			int options = 90;
			bitmap.compress(CompressFormat.JPEG, options, baos);// 质量压缩方法，这里options表示压缩，把数据存放到baos中
			while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				options -= 10;// 每次都减少10
				bitmap.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			}
			FileOutputStream fOut = new FileOutputStream(file);
			fOut.write(baos.toByteArray());
			fOut.flush();
			fOut.close();
			fOut = null;
			baos.flush();
			baos.close();
			baos = null;
			bitmap.recycle();
			bitmap = null;
			file = null;
			path = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	// 将指定路径的图片压缩到200kb以内并存储在原路径
	public static Bitmap compressBitmap2(String oldPath) {

		Bitmap bitmap = null;
		try {


			// bitmap = BitmapFactory.decodeFile(path); //方式一,某些手机会OOM,如:S5
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(oldPath, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 960 * 960);
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(oldPath, opts); // 方式二,解决OOM问题

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 100不压缩;
			int options = 90;
			bitmap.compress(CompressFormat.JPEG, options, baos);// 质量压缩方法，这里options表示压缩，把数据存放到baos中
			while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				options -= 10;// 每次都减少10
				bitmap.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			}
			baos.flush();
			baos.close();
			bitmap.recycle();
			bitmap = null;
			System.gc();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 图片压缩方法（该压缩方法能够最大限度的节省内存并最大限度的保证图片质量）<br>
	 * 压缩的最大压力测试:11张 8K分辨率的大图共603MB连续进行压缩通过
	 *
	 * @param oldPath
	 *            图片原始路径
	 * @param newPath
	 *            图片要保存的路径
	 * @param filename
	 *            图片的保存名称
	 * @param degree
	 *            图片的旋转角度
	 */
	public static void compressBitmap(String oldPath, String newPath,
                                      String filename, int degree) {

		Bitmap bitmap = null;
		try {
			File file = new File(newPath, filename);
			File path = new File(newPath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			// bitmap = BitmapFactory.decodeFile(path); //方式一,某些手机会OOM,如:S5
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(oldPath, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 1080 * 1920);
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(oldPath, opts); // 方式二,解决OOM问题

			// 旋转图片
			bitmap = rotateBitmapByDegree(bitmap, degree);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 100不压缩;
			int options = 90;
			bitmap.compress(CompressFormat.JPEG, options, baos);// 质量压缩方法，这里options表示压缩，把数据存放到baos中
			while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				options -= 10;// 每次都减少10
				bitmap.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			}
			FileOutputStream fOut = new FileOutputStream(file);
			fOut.write(baos.toByteArray());
			fOut.flush();
			fOut.close();
			fOut = null;
			baos.flush();
			baos.close();
			baos = null;
			bitmap.recycle();
			bitmap = null;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static Bitmap getBitmapFromFile(String path) {

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 960 * 960);
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, opts);
	}

	/**
	 * 将图片按照某个角度进行旋转
	 *
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
			bm = null;
		}
		return returnBm;
	}

	// Android提供了一种动态计算图片的inSampleSize方法
	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
												int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 读取图片的旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

}
