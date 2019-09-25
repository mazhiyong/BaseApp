package com.lr.biyou.utils.imageload;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.AsyncTaskUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.RotatePictureUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**

 * 描述：Glide工具类（glide 4.x）
 * 功能包括加载图片，圆形图片，圆角图片，指定圆角图片，模糊图片，灰度图片等等。
 * 目前我只加了这几个常用功能，其他请参考glide-transformations这个开源库。
 * https://github.com/wasabeef/glide-transformations
 */
public class GlideUtils {


    public static final int placeholderSoWhite = R.drawable.refresh_people_3;
    public static final int errorSoWhite = R.drawable.refresh_people_3;
    // public static final int soWhite = R.color.white;

    /**
     *加载图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        //url = "https://gagayi.oss-cn-beijing.aliyuncs.com/video/image.jpg";
       // url = "http://172.16.1.173:8082/image22.jpg";
       // url = "http://wx2.sinaimg.cn/mw690/b1072857ly1fm2xa2am75j20rsa8xqv8.jpg";



       /* Picasso
                .with(context)
                .load(url)
                .into(imageView);*/

        RequestOptions options = new RequestOptions()
                //.skipMemoryCache(true)
               // .diskCacheStrategy(DiskCacheStrategy.NONE)
               // .format(DecodeFormat.PREFER_RGB_565)////.format(DecodeFormat.PREFER_ARGB_8888)
                //.timeout(1*1000*60)
               // .centerCrop()
                .placeholder(R.color.body_bg) //占位图
                .error(R.color.body_bg);       //错误图
                //.disallowHardwareConfig()



               // .signature(new ObjectKey(UUID.randomUUID().toString())) ;
                // .priority(Priority.HIGH)

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("access_token",MbsConstans.ACCESS_TOKEN)
                .addHeader("refresh_token",MbsConstans.REFRESH_TOKEN)
                .build());
            Glide.with(context)
                    .load(url)
                    //.thumbnail(0.3f)
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            LogUtilDebug.i("打印log日志",e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
       /* GlideApp.with(context)
                .asBitmap()
                .centerCrop()
                .load(url)
                .into(imageView);*/

    }
    /**
     *加载图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView,int defaultPic) {

        RequestOptions options = new RequestOptions()
                .placeholder(defaultPic) //占位图
                .error(defaultPic);       //错误图

            Glide.with(context)
                    .load(url)
                    //.thumbnail(0.3f)
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            LogUtilDebug.i("打印log日志",e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);

    }

    /**
     *加载图片(默认)
     */
    public static void loadImage2(Context context, String url, ImageView imageView,int defaultPic) {

        RequestOptions options = new RequestOptions()
                .placeholder(defaultPic) //占位图
                .error(defaultPic)       //错误图
                .signature(new ObjectKey(UUID.randomUUID().toString())) ;



        Glide.with(context)
                .load(url)
                //.thumbnail(0.3f)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志",e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

    }

    /*
     *加载图片(默认)
     */
    public static void loadUUIDImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.body_bg) //占位图
                .error(R.color.body_bg)      //错误图
                .signature(new ObjectKey(UUID.randomUUID().toString())) ;

        Glide.with(context)
                .load(url)
                //.thumbnail(0.3f)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志",e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

    }


    /*
     *加载图片(默认)
     */
    public static void loadSmallImage(Context context, String url, ImageView imageView) {
        //url = "https://gagayi.oss-cn-beijing.aliyuncs.com/video/image.jpg";
       // url = "http://172.16.1.173:8082/image22.jpg";
       // url = "http://wx2.sinaimg.cn/mw690/b1072857ly1fm2xa2am75j20rsa8xqv8.jpg";



       /* Picasso
                .with(context)
                .load(url)
                .into(imageView);*/

        RequestOptions options = new RequestOptions()
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .format(DecodeFormat.PREFER_RGB_565)////.format(DecodeFormat.PREFER_ARGB_8888)
               // .timeout(1*1000*60)
               // .override(200,200)
                .placeholder(R.color.body_bg) //占位图
                .error(R.color.body_bg)       //错误图
//                .disallowHardwareConfig()
                .centerCrop();

               // .signature(new ObjectKey(UUID.randomUUID().toString())) ;
               // .priority(Priority.HIGH)

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("access_token",MbsConstans.ACCESS_TOKEN)
                .addHeader("refresh_token",MbsConstans.REFRESH_TOKEN)
                .build());
            Glide.with(context)
                    .load(glideUrl)
                    .thumbnail(0.2f)
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())//淡入淡出
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            LogUtilDebug.i("打印log日志",e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
       /* GlideApp.with(context)
                .asBitmap()
                .centerCrop()
                .load(url)
                .into(imageView);*/

    }


    /**
     * 指定图片大小;使用override()方法指定了一个图片的尺寸。
     * Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
     * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字----override(Target.SIZE_ORIGINAL)
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void loadImageSize(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderSoWhite) //占位图
                .error(placeholderSoWhite)       //错误图
                .override(width, height)
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 禁用内存缓存功能
     * diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收五种参数：
     * <p>
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     */

    public static void loadImageSizekipMemoryCache(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderSoWhite) //占位图
                .error(R.color.white)       //错误图S
                .skipMemoryCache(true)//禁用掉Glide的内存缓存功能
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(R.color.body_bg) //占位图
                .error(R.color.body_bg)       //错误图
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 预先加载图片
     * 在使用图片之前，预先把图片加载到缓存，调用了预加载之后，我们以后想再去加载这张图片就会非常快了，
     * 因为Glide会直接从缓存当中去读取图片并显示出来
     */
    public static void preloadImage(Context context, String url) {
        Glide.with(context)
                .load(url)
                .preload();

    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite)
                //.priority(Priority.HIGH)
                .bitmapTransform(new RoundedCornersTransformation(45, 0, RoundedCornersTransformation.CornerType.ALL))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     *
     * @param context
     * @param url
     * @param imageView
     * @param type
     */
    public static void loadCustRoundCircleImage(Context context, String url, ImageView imageView, RoundedCornersTransformation.CornerType type) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite)
                //.priority(Priority.HIGH)
                .bitmapTransform(new RoundedCornersTransformation(8, 0, type))
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(url).apply(options).into(imageView);
    }


    /**
     * 加载模糊图片（自定义透明度）
     *
     * @param context
     * @param url
     * @param imageView
     * @param blur      模糊度，一般1-100够了，越大越模糊
     */
    public static void loadBlurImage(Context context, String url, ImageView imageView, int blur) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite)
                //.priority(Priority.HIGH)
                .bitmapTransform(new BlurTransformation(blur))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /*
     *加载灰度(黑白)图片（自定义透明度）
     */
    public static void loadBlackImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite)
                //.priority(Priority.HIGH)
                .bitmapTransform(new GrayscaleTransformation())
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * Glide.with(this).asGif()    //强制指定加载动态图片
     * 如果加载的图片不是gif，则asGif()会报错， 当然，asGif()不写也是可以正常加载的。
     * 加入了一个asBitmap()方法，这个方法的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。
     * 如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
     *
     * @param context
     * @param url       例如：https://image.niwoxuexi.com/blog/content/5c0d4b1972-loading.gif
     * @param imageView
     */
    private void loadGif(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite);
        Glide.with(context)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(imageView);

    }


    public static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

  /*  public static void downloadImage(final Context context, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //String url = "http://www.guolin.tech/book.png";
                    FutureTarget<File> target = Glide.with(context)
                            .asFile()
                            .load(url)
                            .submit();
                    final File imageFile = target.get();
                    LogUtilDebug.i("show", "下载好的图片文件路径="+imageFile.getPath());
                  *//*  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, imageFile.getPath(), Toast.LENGTH_LONG).show();
                        }
                    });*//*
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
*/


    /**
     * 下载图片
     * @param context
     * @param url
     */

    private  static File file;
    public static void downloadImage(final Context context, final String url) {
        AsyncTaskUtil.excute(1, new AsyncTaskUtil.TaskCallback() {
            @Override
            public void initOnUI() {
               /* LogcatUtil.logI("UI线程初始化");
                RxToast.info("开始下载");*/
            }

            @Override
            public void cancellTask() {

            }

            @Override
            public Map<Object, Object> doTask(Map<Object, Object>... maps) {
                try {
                    FutureTarget<File> target = Glide.with(context)
                            .asFile()
                            .load(url)
                            .submit();
                    final File imageFile = target.get();
                    LogUtilDebug.i("show","下载好的图片文件所在缓存路径="+imageFile.getPath());


                    File saveFile = new File(MbsConstans.PIC_SAVE);
                    String path = MbsConstans.PIC_SAVE+UtilTools.getStringFromDate(new Date(),"yyyyMMddHHmmss")+".png";
                    file = new File(path);

                    if(!saveFile.exists()){
                        saveFile.mkdirs();
                    }

                    if(!file.exists()){
                        file.createNewFile();
                    }

                    //复制文件
                    copy(imageFile,file);


                    //适配Android 7.0 FileProvider
                   /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(context, "com.example.baseproject.fileProvider", file);
                        LogcatUtil.logW("uri:"+uri);
                    } else {
                        uri = Uri.fromFile(file);
                    }*/

                    LogUtilDebug.i("show","File路径="+file.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Map<Object,Object> map = new HashMap<>();
                map.put("result","success");
                return map;
            }



            @Override
            public void progressTask(Integer... values) {
                LogUtilDebug.i("show","完成进度："+values[0]);
            }

            @Override
            public void resultTask(Map<Object, Object> objectObjectMap) {
                if(objectObjectMap != null){
                    String result = objectObjectMap.get("result")+"";
                    if(result.equals("success")){
                        Toast.makeText(context,"保存成功",Toast.LENGTH_LONG).show();

                        //把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                    file.getAbsolutePath(), file.getName(), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        //更新图库
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        public void onScanCompleted(String path, Uri uri) {
                                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                            mediaScanIntent.setData(uri);
                                            context.sendBroadcast(mediaScanIntent);
                                        }
                                    });
                        } else {
                            String relationDir = file.getParent();
                            File file1 = new File(relationDir);
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.getAbsoluteFile())));
                        }

//                        if (file != null){
//                            Uri uri= Uri.fromFile(file);
//                            LogUtilDebug.i("show","uri>>"+uri);
//                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//                        }
                    }else {
                        Toast.makeText(context,"保存失败",Toast.LENGTH_LONG).show();

                    }
                }

            }
        });
    }

    public static void copy (File sourece, File target){
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(sourece);
            fos = new FileOutputStream(target);

            byte[] buffer = new byte[1024];
            while (fis.read(buffer)>0){
                fos.write(buffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null){
                    fis.close();
                }
                if(fos != null){
                    fos.close();
                }


            }catch (IOException e) {
                e.printStackTrace();
            }
        }


    }




    /**
     * 计算出图片初次显示需要放大倍数
     * @param imagePath 图片的绝对路径
     */
    public static float getImageScale(Context context, String imagePath){
        if(TextUtils.isEmpty(imagePath)) {
            return 2.0f;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }


        int i = RotatePictureUtil.getBitmapDegree(imagePath);
        bitmap = RotatePictureUtil.rotateBitmapByDegree(bitmap,i);

        if(bitmap == null) {
            return 2.0f;
        }

        // 拿到图片的宽和高
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();

       /* WindowManager wm = ((Activity)context).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();*/

        int width = UtilTools.getScreenWidth(context);
        int height = UtilTools.getScreenHeight(context);


        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;

        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;

        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }
        bitmap.recycle();
        return scale;
    }

}