package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lr.biyou.R;
import com.lr.biyou.utils.imageload.CircleProgressView;
import com.lr.biyou.utils.imageload.ProgressInterceptor;
import com.lr.biyou.utils.imageload.ProgressListener;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

/**
 */
public class ModifyShowImageAdapter extends
        RecyclerView.Adapter<ModifyShowImageAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;

    public OnMyItemClickListener getOnMyItemClickListener() {
        return mOnMyItemClickListener;
    }

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        mOnMyItemClickListener = onMyItemClickListener;
    }

    private OnMyItemClickListener mOnMyItemClickListener;

    private List<Map<String,Object>> mDataList;
    public ModifyShowImageAdapter(Context context, List<Map<String,Object>> mDataList) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mDataList = mDataList;
    }



    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_mofify_file_image,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Map<String,Object> map = mDataList.get(position);

        int w = UtilTools.getScreenWidth(context);
        int padd = UtilTools.dip2px(context,5);
        int picPadd = UtilTools.dip2px(context,2);
        w = w-(2*4*picPadd)-(2*padd);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w/4,w/4);
        layoutParams.bottomMargin = picPadd;
        layoutParams.topMargin = picPadd;
        layoutParams.leftMargin = picPadd;
        layoutParams.rightMargin = picPadd;
        viewHolder.mImg.setLayoutParams(layoutParams);

        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnMyItemClickListener != null){
                    mOnMyItemClickListener.OnMyItemClickListener(viewHolder.mImg,position);
                }
            }
        });


        String imgUrl = map.get("remotepath")+"";
        if (imgUrl.contains("http")){
            //GlideUtils.loadSmallImage(context,imgUrl,viewHolder.mImg);
        }else {
            imgUrl = MbsConstans.PIC_URL+imgUrl;
            //GlideUtils.loadSmallImage(context,imgUrl,viewHolder.mImg);
        }

        final String loadImgUrl = imgUrl;
        //viewHolder.mProgressBar.setProgress(20+position);

        ProgressInterceptor.addListener(loadImgUrl, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                viewHolder.mProgressBar.setProgress(progress);
//                viewHolder.mImg.setTag(R.id.glide_tag,progress);
//
//                CircleProgressView ccc =(CircleProgressView) viewHolder.mImg.getTag(R.id.glide_tag);
//                ccc.setProgress(progress);
            }
        });


        RequestOptions options = new RequestOptions()
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .format(DecodeFormat.PREFER_RGB_565)////.format(DecodeFormat.PREFER_ARGB_8888)
                // .timeout(1*1000*60)
                .override(160,160)
                .placeholder(R.color.body_bg) //占位图
                .error(R.color.body_bg)       //错误图
//                .disallowHardwareConfig()
                .centerCrop();

        // .signature(new ObjectKey(UUID.randomUUID().toString())) ;
        // .priority(Priority.HIGH)

        Glide.with(context)
                .load(imgUrl)
                //.thumbnail(0.3f)

                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())//淡入淡出
                .into(new DrawableImageViewTarget( viewHolder.mImg) {
                    public void onLoadFailed( Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        //隐藏加载的进度条
                        viewHolder.mProgressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> animation) {
                        super.onResourceReady(resource, animation);
                        ProgressInterceptor.removeListener(loadImgUrl);
                        viewHolder.mProgressBar.setVisibility(View.GONE);
                    }
                });

        LogUtilDebug.i("打印log日志",imgUrl);

    }


    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        ImageView imageView=holder.mImg;
        if (imageView!=null){
            Glide.with(context).clear(imageView);
            /*Drawable drawable = imageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable){
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }*/
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        CircleProgressView mProgressBar;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.image_view);
            mProgressBar = (CircleProgressView) view.findViewById(R.id.progressView);
        }
    }


}
