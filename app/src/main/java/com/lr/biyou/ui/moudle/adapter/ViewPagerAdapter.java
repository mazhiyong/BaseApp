package com.lr.biyou.ui.moudle.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lr.biyou.R;
import com.lr.biyou.utils.imageload.CircleProgressView;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.imageload.ProgressInterceptor;
import com.lr.biyou.utils.imageload.ProgressListener;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.luck.picture.lib.config.PictureMimeType;
import com.lr.biyou.mywidget.photoview.PhotoView;

import java.io.File;
import java.util.List;
import java.util.Map;



public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<View> mViews;
    private List<Map<String, Object>> mImgList;

    public ViewPagerAdapter(Context mContext, List<View> mDataList, List<Map<String, Object>> mImgList){
        this.mContext = mContext;
        this.mViews = mDataList;
        this.mImgList = mImgList;
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int i = position % 4;
        View mRootView = mViews.get(i);

        CircleProgressView  mCircleProgressView = mRootView.findViewById(R.id.progressView);
        SubsamplingScaleImageView view = (SubsamplingScaleImageView) mRootView.findViewById(R.id.big_image_view);
        PhotoView mPhotoView =  mRootView.findViewById(R.id.big_photo_image_view);

        //在此次回收图片

        //........回收代码
        Glide.with(view.getContext()).clear(view);
        view.recycle();
        //Runtime.getRuntime().gc();
        //回收图片
        ((ViewPager) container).removeView(mRootView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        int i = position % 4;
        View mRootView = mViews.get(i);

        final CircleProgressView  mCircleProgressView = mRootView.findViewById(R.id.progressView);
        final SubsamplingScaleImageView view =  mRootView.findViewById(R.id.big_image_view);
        final PhotoView mPhotoView =  mRootView.findViewById(R.id.big_photo_image_view);
        //view.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
        view.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        view.setDoubleTapZoomScale(2.0f);
        view.setMinScale(0.3F);//最小显示比例
        view.setMaxScale(5.0F);//最大显示比例（太大了图片显示会失真，因为一般微博长图的宽度不会太宽）
        //ViewCompat.setTransitionName(view, "detail:header:image" + ":" + position);
        Map<String, Object> map = mImgList.get(position);
        ((ViewPager) container).addView(mRootView, 0);

        String imgUrl = map.get("url")+"";
       /* if (imgUrl.contains("http")){
        }else {
            imgUrl = MbsConstans.PIC_URL+imgUrl;
        }*/

        ProgressInterceptor.addListener(imgUrl, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                mCircleProgressView.setProgress(progress);
            }
        });

        Glide
                .with(view.getContext())
                .asFile()
                .load(imgUrl)
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onLoadFailed( Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        //隐藏加载的进度条
                        mCircleProgressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        //显示加载进度条
                        //viewHolder.mPd.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        String filePath = resource.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(filePath, options);
                        int bmpWidth = options.outWidth;
                        int bmpHeight = options.outHeight;



                        //outMimeType是以--”image/png”、”image/jpeg”、”image/gif”…….这样的方式返回的
                        String mimeType = options.outMimeType;
                        LogUtilDebug.i("图片类型:", "" + mimeType);
                        boolean isGif = false;
                        if (UtilTools.empty(mimeType)){
                            isGif = false;
                        }else {
                            isGif =  PictureMimeType.isGif(mimeType);
                        }
                        //自定义的判断是否为GIF的工具类

                        float scale = GlideUtils.getImageScale(mContext,resource.getAbsolutePath());
                        if (isGif){
                            view.setVisibility(View.GONE);
                            mPhotoView.setVisibility(View.VISIBLE);
                            Glide.with(view.getContext())
                                    .asGif()
                                    .load(resource)
                                    //监听器是RequestListener<GifDrawable>类型的，其中控制了加载进度条——loadingProgress的隐藏
                                    //imageView即 PhotoView对象
                                    .into(mPhotoView);

                        }else {

                            view.setVisibility(View.VISIBLE);
                            mPhotoView.setVisibility(View.GONE);
                            view.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(scale, new PointF(0, 0), SubsamplingScaleImageView.ORIENTATION_USE_EXIF));
//                            view.setImage(ImageSource.uri(Uri.fromFile(resource)));
                        }
                        mCircleProgressView.setVisibility(View.GONE);
                    }
                });


	/*	Glide.with(mContext)
				.load(map.get("remotepath")+"")
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
				.into(new SimpleTarget<Drawable>() {
					@Override
					public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
						BitmapDrawable bd = (BitmapDrawable) resource;
						Bitmap bm = bd.getBitmap();

						view.setImage(ImageSource.bitmap(bm),new ImageViewState(2.0F, new PointF(0, 0), 0));
					}
				});*/

        return mRootView;
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}
