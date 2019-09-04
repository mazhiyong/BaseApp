package com.wanou.framelibrary.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.wanou.framelibrary.R;


/**
 * @author wodx521
 * @date on 2018/8/22
 */
public class RatioImageView extends androidx.appcompat.widget.AppCompatImageView {

    private float mRatio = 0f;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);

        mRatio = typedArray.getFloat(R.styleable.RatioImageView_image_ratio, 0f);
        typedArray.recycle();
    }

    /**
     * 设置ImageView的宽高比
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mRatio != 0) {
//            if (measureHeight > measureWidth) {
//                float width = measureHeight * mRatio;
//                widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY);
//            } else {
                float height = measureWidth / mRatio;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
//            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
