package com.lr.biyou.mywidget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lr.biyou.R;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;

import java.math.BigDecimal;

import androidx.core.content.ContextCompat;

public class CircleProgress extends View {

    private static final String TAG = CircleProgress.class.getSimpleName();

    private Context mContext;
    //默认大小
    private int mDefaultSize;
    //是否开启抗锯齿
    private boolean antiAlias;
    //绘制提示
    private TextPaint mHintPaint;
    private CharSequence mHint;
    private int mHintColor;
    private float mHintSize;
    private float mHintOffset;

    //绘制单位
    private TextPaint mUnitPaint;
    private CharSequence mUnit;
    private int mUnitColor;
    private float mUnitSize;
    private float mUnitOffset;

    //绘制数值
    private TextPaint mValuePaint;
    private float mValue;
    private float mMaxValue;
    private float mValueOffset;
    private int mPrecision;
    private String mPrecisionFormat;
    private int mValueColor;
    private float mValueSize;

    //绘制圆弧
    private Paint mArcPaint;
    private float mArcWidth;
    private float mStartAngle, mSweepAngle;
    private RectF mRectF;
    //当前进度，[0.0f,1.0f]
    private float mPercent;
    //动画时间
    private long mAnimTime;
    //属性动画
    private ValueAnimator mAnimator;
    private ValueAnimator mAnimator2;

    private float mNum = 0.00f;

    //绘制背景圆弧
    private Paint mBgArcPaint;
    private int mBgArcColor;
    private int mArcColor;
    private float mBgArcWidth;

    //圆心坐标，半径
    private Point mCenterPoint;
    private float mRadius;
    private float mTextOffsetPercentInRadius;

    private int mArcCenterX;
    // 内部虚线的外部半径
    private float mExternalDottedLineRadius;
    // 内部虚线的内部半径
    private float mInsideDottedLineRadius;

    // 线条数
    private int mDottedLineCount = 100;
    // 圆弧跟虚线之间的距离
    private int mLineDistance = 20;
    // 线条宽度
    private float mDottedLineWidth = 40;
    //是否使用渐变
    protected boolean useGradient=true;
    //前景色起始颜色
    private int foreStartColor;
    //前景色结束颜色
    private int foreEndColcor;
    protected int mWidth;
    protected int mHeight;


    private Paint paint_3;
    private Paint paint_4;


    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mDefaultSize = dipToPx(mContext, 150);
        mAnimator = new ValueAnimator();
        mRectF = new RectF();
        mCenterPoint = new Point();
        initAttrs(attrs);
        initPaint();
        setValue(mValue);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        antiAlias = typedArray.getBoolean(R.styleable.CircleProgressBar_antiAlias, true);

        mHint = typedArray.getString(R.styleable.CircleProgressBar_hint);
        mHintColor = typedArray.getColor(R.styleable.CircleProgressBar_hintColor, Color.BLACK);
        mHintSize = typedArray.getDimension(R.styleable.CircleProgressBar_hintSize, 15);

        mValue = typedArray.getFloat(R.styleable.CircleProgressBar_value, 50);
        mMaxValue = typedArray.getFloat(R.styleable.CircleProgressBar_maxValue, 50);
        //内容数值精度格式
        mPrecision = typedArray.getInt(R.styleable.CircleProgressBar_precision, 0);
        mPrecisionFormat = getPrecisionFormat(mPrecision);
        mValueColor = typedArray.getColor(R.styleable.CircleProgressBar_valueColor, Color.BLACK);
        mValueSize = typedArray.getDimension(R.styleable.CircleProgressBar_valueSize, 15);

        mUnit = typedArray.getString(R.styleable.CircleProgressBar_unit);
        mUnitColor = typedArray.getColor(R.styleable.CircleProgressBar_unitColor, Color.BLACK);
        mUnitSize = typedArray.getDimension(R.styleable.CircleProgressBar_unitSize, 30);

        mArcWidth = typedArray.getDimension(R.styleable.CircleProgressBar_arcWidth, 15);
        mStartAngle = typedArray.getFloat(R.styleable.CircleProgressBar_startAngle, 270);
        mSweepAngle = typedArray.getFloat(R.styleable.CircleProgressBar_sweepAngle, 360);

        mBgArcColor = typedArray.getColor(R.styleable.CircleProgressBar_bgArcColor, Color.WHITE);
        mArcColor = typedArray.getColor(R.styleable.CircleProgressBar_arcColors, Color.RED);
        mBgArcWidth = typedArray.getDimension(R.styleable.CircleProgressBar_bgArcWidth, 15);
        mTextOffsetPercentInRadius = typedArray.getFloat(R.styleable.CircleProgressBar_textOffsetPercentInRadius, 0.33f);
        mAnimTime = typedArray.getInt(R.styleable.CircleProgressBar_animTime, 1000);
        mDottedLineCount = typedArray.getInteger(R.styleable.CircleProgressBar_dottedLineCount, mDottedLineCount);
        mLineDistance = typedArray.getInteger(R.styleable.CircleProgressBar_lineDistance, mLineDistance);
        mDottedLineWidth = typedArray.getDimension(R.styleable.CircleProgressBar_dottedLineWidth, mDottedLineWidth);
        foreStartColor = typedArray.getColor(R.styleable.CircleProgressBar_foreStartColor, Color.BLUE);
        foreEndColcor = typedArray.getColor(R.styleable.CircleProgressBar_foreEndColor, Color.BLUE);
        typedArray.recycle();
    }

    private void initPaint() {
        paint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);


        mHintPaint = new TextPaint();
        // 设置抗锯齿,会消耗较大资源，绘制图形速度会变慢。
        mHintPaint.setAntiAlias(antiAlias);
        // 设置绘制文字大小
        mHintPaint.setTextSize(mHintSize);
        // 设置画笔颜色
        mHintPaint.setColor(mHintColor);
        // 从中间向两边绘制，不需要再次计算文字
        mHintPaint.setTextAlign(Paint.Align.CENTER);

        mValuePaint = new TextPaint();
        mValuePaint.setAntiAlias(antiAlias);
        mValuePaint.setTextSize(mValueSize);
        mValuePaint.setColor(mValueColor);
        // 设置Typeface对象，即字体风格，包括粗体，斜体以及衬线体，非衬线体等
       // mValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mValuePaint.setTextAlign(Paint.Align.CENTER);

        mUnitPaint = new TextPaint();
        mUnitPaint.setAntiAlias(antiAlias);
        mUnitPaint.setTextSize(mUnitSize);
        mUnitPaint.setColor(mUnitColor);
        mUnitPaint.setTextAlign(Paint.Align.CENTER);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(antiAlias);
        // 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE
        mArcPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔粗细
        mArcPaint.setStrokeWidth(mArcWidth);
        // 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
        // Cap.ROUND(圆形样式)或Cap.SQUARE(方形样式)
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mBgArcPaint = new Paint();
        mBgArcPaint.setAntiAlias(antiAlias);
        mBgArcPaint.setColor(mBgArcColor);
        mBgArcPaint.setStyle(Paint.Style.STROKE);
        mBgArcPaint.setStrokeWidth(mBgArcWidth);
        // 设置画笔粗细
        mBgArcPaint.setStrokeWidth(mArcWidth);
        mBgArcPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureView(widthMeasureSpec, mDefaultSize),
                measureView(heightMeasureSpec, mDefaultSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcCenterX = (int) (w / 2.f);
        Log.d(TAG, "onSizeChanged: w = " + w + "; h = " + h + "; oldw = " + oldw + "; oldh = " + oldh);
        //求圆弧和背景圆弧的最大宽度
        float maxArcWidth = Math.max(mArcWidth, mBgArcWidth);
        //求最小值作为实际值
        int minSize = Math.min(w - getPaddingLeft() - getPaddingRight() - 2 * (int) maxArcWidth,
                h - getPaddingTop() - getPaddingBottom() - 2 * (int) maxArcWidth);
        //减去圆弧的宽度，否则会造成部分圆弧绘制在外围
        mRadius = minSize / 2;
        //获取圆的相关参数
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2+dipToPx(mContext,20);
        //绘制圆弧的边界
        mRectF.left = mCenterPoint.x - mRadius - maxArcWidth / 2;
        mRectF.top = mCenterPoint.y - mRadius - maxArcWidth / 2;
        mRectF.right = mCenterPoint.x + mRadius + maxArcWidth / 2;
        mRectF.bottom = mCenterPoint.y + mRadius + maxArcWidth / 2;
        //计算文字绘制时的 baseline
        //由于文字的baseline、descent、ascent等属性只与textSize和typeface有关，所以此时可以直接计算
        //若value、hint、unit由同一个画笔绘制或者需要动态设置文字的大小，则需要在每次更新后再次计算
        mValueOffset = mCenterPoint.y + (int)(1.5*getBaselineOffsetFromY(mValuePaint));

//        mHintOffset = mCenterPoint.y - mRadius * mTextOffsetPercentInRadius + getBaselineOffsetFromY(mHintPaint);
//        mUnitOffset = mCenterPoint.y + mRadius * mTextOffsetPercentInRadius + getBaselineOffsetFromY(mUnitPaint);

        mHintOffset = mCenterPoint.y +mValueOffset +getBaselineOffsetFromY(mValuePaint);
        mUnitOffset = mCenterPoint.y - 2*getBaselineOffsetFromY(mValuePaint)- getBaselineOffsetFromY(mUnitPaint);

        if (useGradient) {
            LinearGradient gradient = new LinearGradient(0, 0, w, h, foreEndColcor, foreStartColor, Shader.TileMode.CLAMP);
            mArcPaint.setShader(gradient);
        } else {
            mArcPaint.setColor(mArcColor);
        }

        Log.d(TAG, "onSizeChanged: 控件大小 = " + "(" + w + ", " + h + ")"
                + "圆心坐标 = " + mCenterPoint.toString()
                + ";圆半径 = " + mRadius
                + ";圆的外接矩形 = " + mRectF.toString());

        // 虚线的外部半径
        mExternalDottedLineRadius = (int) (mRectF.width() / 2)+mLineDistance;
        // 虚线的内部半径
        mInsideDottedLineRadius = mExternalDottedLineRadius - mDottedLineWidth;
    }

    private float getBaselineOffsetFromY(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return ((Math.abs(fontMetrics.ascent) - fontMetrics.descent))/ 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        drawArc(canvas);
    }

    /**
     * 绘制内容文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
       // canvas.drawText(String.format(mPrecisionFormat, mValue), mCenterPoint.x, mValueOffset, mValuePaint);

        String text = UtilTools.getNormalMoney(mValue+"");
//        Spannable spannable = UtilTools.getDianType2(mContext,text);
        canvas.drawText(text, mCenterPoint.x, mValueOffset, mValuePaint);
        if (mHint != null) {
            canvas.drawText(mHint.toString(), mCenterPoint.x, mHintOffset, mHintPaint);
        }

        if (mUnit != null) {
            canvas.drawText(mUnit.toString(), mCenterPoint.x, mUnitOffset, mUnitPaint);
        }
    }

    private void drawArc(Canvas canvas) {
        // 绘制背景圆弧
        // 从进度圆弧结束的地方开始重新绘制，优化性能
        canvas.save();

        // 360 * Math.PI / 180
        float evenryDegrees = (float) (2.0f * Math.PI / mDottedLineCount);
        float startDegress = (float) (135 * Math.PI / 180);
        float endDegress = (float) (225 * Math.PI / 180);
        for (int i = 0; i < mDottedLineCount; i++) {
            float degrees = i * evenryDegrees;
            // 过滤底部90度的弧长
            if (degrees > startDegress && degrees < endDegress) {
                continue;
            }
            float startX = mArcCenterX + (float) Math.sin(degrees) * mInsideDottedLineRadius;
            float startY = mArcCenterX - (float) Math.cos(degrees) * mInsideDottedLineRadius;

            float stopX = mArcCenterX + (float) Math.sin(degrees) * mExternalDottedLineRadius;
            float stopY = mArcCenterX - (float) Math.cos(degrees) * mExternalDottedLineRadius;

            //canvas.drawLine(startX, startY, stopX, stopY, mBgArcPaint);
        }

        canvas.drawArc(mRectF, mStartAngle, mSweepAngle, false, mBgArcPaint);




        canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);

        // 第一个参数 oval 为 RectF 类型，即圆弧显示区域
        // startAngle 和 sweepAngle  均为 float 类型，分别表示圆弧起始角度和圆弧度数
        // 3点钟方向为0度，顺时针递增
        // 如果 startAngle < 0 或者 > 360,则相当于 startAngle % 360
        // useCenter:如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形
        float currentAngle = mSweepAngle * mPercent;
        //canvas.drawArc(mRectF, 2, currentAngle, false, mArcPaint);
        canvas.drawArc(mRectF, 0, currentAngle, false, mArcPaint);


        float x = (float) ((mRadius+mArcWidth/2)*Math.cos(Math.toRadians(currentAngle)));
        float y = (float) ((mRadius+mArcWidth/2)*Math.sin(Math.toRadians(currentAngle)));
        paint_3.setStyle(Paint.Style.FILL);
        paint_3.setColor(ContextCompat.getColor(mContext,R.color.circle_start_color));
        // paint_3.setShader(shader);
        // paint_3.setColor(ContextCompat.getColor(context,R.color.red));
        paint_3.setMaskFilter(new BlurMaskFilter(dipToPx(mContext,3), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
       // canvas.drawCircle( mCenterPoint.x+mRadius+mArcWidth/2, mCenterPoint.y,dipToPx(mContext,3),paint_3);



        paint_3.setStrokeWidth(mArcWidth/2/3*4);
        paint_3.setStyle(Paint.Style.STROKE);
        paint_3.setMaskFilter(new BlurMaskFilter((float) (mArcWidth/3), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
        canvas.drawCircle( mCenterPoint.x+x, mCenterPoint.y+y,mArcWidth/2/3,paint_3);////圆形动态
//         canvas.drawCircle( mCenterPoint.x+mRadius+mArcWidth/2, mCenterPoint.y,mArcWidth/3,paint_3);


        paint_4.setStyle(Paint.Style.FILL);
        paint_4.setColor(ContextCompat.getColor(mContext,R.color.white));
        // paint_3.setShader(shader);
        // paint_3.setColor(ContextCompat.getColor(context,R.color.red));
        paint_4.setMaskFilter(new BlurMaskFilter((float) (mArcWidth/6), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
         canvas.drawCircle( mCenterPoint.x+x, mCenterPoint.y+y,mArcWidth/6,paint_4);  //圆形动态
//        canvas.drawCircle( mCenterPoint.x+mRadius+mArcWidth/2, mCenterPoint.y,mArcWidth/6,paint_4);


        canvas.restore();
    }

    public boolean isAntiAlias() {
        return antiAlias;
    }

    public CharSequence getHint() {
        return mHint;
    }

    public void setHint(CharSequence hint) {
        mHint = hint;
    }

    public CharSequence getUnit() {
        return mUnit;
    }

    public void setUnit(CharSequence unit) {
        mUnit = unit;
    }

    public float getValue() {
        return mValue;
    }

    /**
     * 设置当前值
     *
     * @param value
     */
    public void setValue(float value) {
        mNum = value;
        mPercent = 0;
        reset();
        if (value > mMaxValue) {
            value = mMaxValue;
        }
        float start = mPercent;
        double bi = UtilTools.divide(value,mMaxValue);
        BigDecimal bg = new BigDecimal(bi);
        float end = bg.floatValue();
        startAnimator(start, end, mAnimTime);
    }

    private void startAnimator(float start, float end, long animTime) {
        mAnimator = ValueAnimator.ofFloat(start, end);
        mAnimator.setDuration(animTime);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();

                /*double all = UtilTools.mul(mPercent,mMaxValue);
                BigDecimal bg = new BigDecimal(all);
                mValue = bg.floatValue();
                LogUtilDebug.i(TAG, "onAnimationUpdate: percent = " + mPercent
                        + ";currentAngle = " + (mSweepAngle * mPercent)
                        + ";value = " + mValue);*/
                invalidate();
            }
        });
        mAnimator.start();

        mAnimator2 = ValueAnimator.ofFloat(0, 1);
        mAnimator2.setDuration(animTime);
        mAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float p = (float) animation.getAnimatedValue();

                double all = UtilTools.mul(p,mNum);
                BigDecimal bg = new BigDecimal(all);
                mValue = bg.floatValue();
                LogUtilDebug.i(TAG, ";value = " + mValue);
                invalidate();
            }
        });
        mAnimator2.start();
    }
    /**
     * 获取最大值
     *
     * @return
     */
    public float getMaxValue() {
        return mMaxValue;
    }

    /**
     * 设置最大值
     *
     * @param maxValue
     */
    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    /**
     * 获取精度
     *
     * @return
     */
    public int getPrecision() {
        return mPrecision;
    }

    public void setPrecision(int precision) {
        mPrecision = precision;
        mPrecisionFormat = getPrecisionFormat(precision);
    }

    public long getAnimTime() {
        return mAnimTime;
    }

    public void setAnimTime(long animTime) {
        mAnimTime = animTime;
    }

    /**
     * 重置
     */
    public void reset() {
        startAnimator(mPercent, 0.0f, 1000L);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //释放资源
    }

    /**
     * 测量 View
     *
     * @param measureSpec
     * @param defaultSize View 的默认大小
     * @return
     */
    private static int measureView(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    public static int dipToPx(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 获取数值精度格式化字符串
     *
     * @param precision
     * @return
     */
    public static String getPrecisionFormat(int precision) {
        return "%." + precision + "f";
    }
}