package com.wanou.framelibrary.glidetools;

/**
 * Author by wodx521
 * Date on 2018/11/8.
 */
public class GlideUtils {
    /*@NonNull
    private static GlideRequest<Drawable> getRequest(Object object) {
        GlideRequest<Drawable> glideRequest;
        if (object instanceof String) {
            glideRequest = GlideApp.with(GlobalApplication.getContext())
                    .load((String) object);
        } else if (object instanceof Integer) {
            glideRequest = GlideApp.with(GlobalApplication.getContext())
                    .load((Integer) object);
        } else if (object instanceof Drawable) {
            glideRequest = GlideApp.with(GlobalApplication.getContext())
                    .load((Drawable) object);
        } else if (object instanceof Bitmap) {
            glideRequest = GlideApp.with(GlobalApplication.getContext())
                    .load((Bitmap) object);
        } else if (object instanceof File) {
            glideRequest = GlideApp.with(GlobalApplication.getContext())
                    .load((File) object);
        } else if (object instanceof Uri) {
            glideRequest = GlideApp.with(GlobalApplication.getContext())
                    .load((Uri) object);
        } else {
            glideRequest = GlideApp.with(GlobalApplication.getContext())
                    .load(object);
        }
        return glideRequest;
    }

    *//**
     * 设置图片给ImageView带圆角
     *
     * @param object      图片地址
     * @param placeImage  占位图 不设置传0
     * @param errorImage  加载错误图片 不设置传0
     * @param roundCorner 图片圆角大小 不设置传0
     * @param imageView   显示图片view
     *//*
    public static void intoImage(Object object, int placeImage, int errorImage, int roundCorner, ImageView imageView) {
        GlideRequest<Drawable> glideRequest = getRequest(object);
        if (placeImage > 0 && errorImage > 0 && roundCorner > 0) {
            glideRequest = glideRequest.placeholder(placeImage)
                    .error(errorImage)
                    .transform(new RoundedCorners(roundCorner));
        } else {
            if (placeImage > 0) {
                glideRequest = glideRequest.placeholder(placeImage);
            }
            if (errorImage > 0) {
                glideRequest = glideRequest.error(errorImage);
            }
            if (roundCorner > 0) {
                glideRequest = glideRequest.transform(new RoundedCorners(roundCorner));
            }
        }
        glideRequest.transition(DrawableTransitionOptions.withCrossFade(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)))
                .transition(GenericTransitionOptions.withNoTransition())
                .into(imageView);
    }


    *//**
     * 设置图片给ImageView带圆角
     *
     * @param object        图片地址
     * @param placeDrawable 占位图 不设置传0
     * @param errorDrawable 加载错误图片 不设置传0
     * @param roundCorner   图片圆角大小 不设置传0
     * @param imageView     显示图片view
     *//*
    public static void intoImage(Object object, Drawable placeDrawable, Drawable errorDrawable, int roundCorner, ImageView imageView) {
        GlideRequest<Drawable> glideRequest = getRequest(object);
        if (placeDrawable != null && errorDrawable != null && roundCorner > 0) {
            glideRequest = glideRequest.placeholder(placeDrawable)
                    .error(errorDrawable)
                    .transform(new RoundedCorners(roundCorner));
        } else {
            if (placeDrawable != null) {
                glideRequest = glideRequest.placeholder(placeDrawable);
            }
            if (errorDrawable != null) {
                glideRequest = glideRequest.error(errorDrawable);
            }
            if (roundCorner > 0) {
                glideRequest = glideRequest.transform(new RoundedCorners(roundCorner));
            }
        }
        glideRequest.transition(DrawableTransitionOptions.withCrossFade(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)))
                .transition(GenericTransitionOptions.withNoTransition())
                .into(imageView);
    }*/
}
