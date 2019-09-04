package com.lr.biyou.utils.imageload;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {
    /**
     * 压缩图片之后保存为文件
     * 
     * @param filePath
     *            原始图片的完整路径
     * @param storeImgPath
     *            压缩之后要存储的图片的完整路径
     * @return boolean
     * @author Doraemon
     * @time 2014年6月27日下午5:10:19
     */
    public static boolean saveCompressImg(String filePath, String storeImgPath) {
        Bitmap bm = getSmallBitmap(filePath);
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(storeImgPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bm.compress(Bitmap.CompressFormat.JPEG, 20, stream);//质量压缩
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     * 
     * @return
     */
    private static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        //options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inSampleSize = 1;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}