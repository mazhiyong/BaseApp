package com.lr.biyou.utils.tool;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * 富文本  TextView
 */
public class TextViewUtils {
    public TextViewUtils() {
    }

    private    TextView mTextView1;
    private    SpannableString ss;
    public  void init(String content, TextView mTextView){
        mTextView1 = mTextView;
        ss = new SpannableString(content);
    }

    /**
     * 设置点击事件
     * @param start
     * @param end
     */
    public  void setTextClick(int start, int end,ClickCallBack callBack){
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if(callBack != null){
                    callBack.onClick();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {

            }

        }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTextView1.setMovementMethod(LinkMovementMethod.getInstance());
    }



    /**
     * 设置颜色
     * @param start
     * @param end
     * @param color
     */
    public  void setTextColor(int start, int end, int color){
        ss.setSpan(new ForegroundColorSpan(color),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    /**
     * 设置大小
     * @param start
     * @param end
     * @param size
     */
    public  void setTextSize(int start, int end,float size){
        ss.setSpan(new RelativeSizeSpan(size),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }



    /**
     * 设置字体风格
     * @param start
     * @param end
     */
    public  void setTextStyle(int start, int end, int style){
        ss.setSpan(new StyleSpan(style),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

    }


    /**
     * 设置字体背景
     * @param start
     * @param end
     */
    public  void setTextBackgound(int start, int end, int color){
        ss.setSpan(new BackgroundColorSpan(color),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }


    /**
     * 设置删除线
     * @param start
     * @param end
     */
    public  void setTextDeleteLine(int start, int end){
        ss.setSpan(new StrikethroughSpan(),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    /**
     * 设置下划线
     * @param start
     * @param end
     */
    public  void setTextUnderLine(int start, int end){
        ss.setSpan(new UnderlineSpan(),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    /**
     * 设置左上角上标
     * @param start
     * @param end
     */
    public  void setTextTopTip(int start, int end){
        ss.setSpan(new SuperscriptSpan(),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }


    /**
     * 设置右下角下标
     * @param start
     * @param end
     */
    public  void setTextUnderTip(int start, int end){
        ss.setSpan(new SubscriptSpan(),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }


    /**
     * 向文本中添加表情
     * @param start
     * @param end
     */
    public void setTextAddEmoji(int start, int end, Drawable drawable){
        ss.setSpan(new ImageSpan(drawable),start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }





    public  interface ClickCallBack{
        void onClick();
    }


    /**
     * 为TextView 设置效果
     */
    public void build(){
        mTextView1.setText(ss);
    }


}
