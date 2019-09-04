package com.lr.biyou.mywidget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.lr.biyou.R;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2016/12/24.
 * Version 1.0
 * Description: 自定义输入密码框
 */
public class PasswordEditText extends EditText {
    // 画笔
    private Paint mPaint;
    // 一个密码所占的宽度
    private int mPasswordItemWidth;
    // 密码的个数默认为6位数
    private int mPasswordNumber = 6;
    // 背景边框颜色
    private int mBgColor = Color.parseColor("#d1d2d6");
    // 背景边框大小
    private int mBgSize = 1;
    // 背景边框圆角大小
    private int mBgCorner = 0;
    // 分割线的颜色
    private int mDivisionLineColor = mBgColor;
    // 分割线的大小
    private int mDivisionLineSize = 1;
    // 密码圆点的颜色
    private int mPasswordColor = mDivisionLineColor;
    // 密码圆点的半径大小
    private int mPasswordRadius = 4;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
        initPaint();
        // 默认只能够设置数字和字母
        setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
    }

    /**
     * 初始化属性
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        // 获取大小
        mDivisionLineSize = (int) array.getDimension(R.styleable.PasswordEditText_divisionLineSize, dip2px(mDivisionLineSize));
        mPasswordRadius = (int) array.getDimension(R.styleable.PasswordEditText_passwordRadius, dip2px(mPasswordRadius));
        mBgSize = (int) array.getDimension(R.styleable.PasswordEditText_bgSize, dip2px(mBgSize));
        mBgCorner = (int) array.getDimension(R.styleable.PasswordEditText_bgCorner, 0);
        // 获取颜色
        mBgColor = array.getColor(R.styleable.PasswordEditText_bgColor, mBgColor);
        mDivisionLineColor = array.getColor(R.styleable.PasswordEditText_divisionLineColor, mDivisionLineColor);
        mPasswordColor = array.getColor(R.styleable.PasswordEditText_passwordColor, mDivisionLineColor);
        array.recycle();
    }

    /**
     * dip 转 px
     */
    private float dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 一个密码的宽度
        mPasswordItemWidth = (getWidth() - 2 * mBgSize - (mPasswordNumber - 1) * mDivisionLineSize) / mPasswordNumber;
        // 画背景
        drawBg(canvas);
        // 画分割线
        drawDivisionLine(canvas);
        // 画密码
        drawPassword(canvas);

        // 当前密码是不是满了
        if(mListener != null){
            String password = getText().toString().trim();
            if(password.length()>=mPasswordNumber){
                mListener.passwordFull(password);
            }
        }
    }

    /**
     * 绘制密码
     */
    private void drawPassword(Canvas canvas) {
        // 密码绘制是实心
        mPaint.setStyle(Paint.Style.FILL);
        // 设置密码的颜色
        mPaint.setColor(mPasswordColor);
        // 获取当前text
        String text = getText().toString().trim();
        // 获取密码的长度
        int passwordLength = text.length();
        // 不断的绘制密码
        for (int i = 0; i < passwordLength; i++) {
            int cy = getHeight() / 2;
            int cx = mBgSize + i * mPasswordItemWidth + i * mDivisionLineSize + mPasswordItemWidth / 2;
            canvas.drawCircle(cx, cy, mPasswordRadius, mPaint);
        }
    }

    /**
     * 绘制分割线
     */
    private void drawDivisionLine(Canvas canvas) {
        // 给画笔设置大小
        mPaint.setStrokeWidth(mDivisionLineSize);
        // 设置分割线的颜色
        mPaint.setColor(mDivisionLineColor);
        for (int i = 0; i < mPasswordNumber - 1; i++) {
            int startX = mBgSize + (i + 1) * mPasswordItemWidth + i * mDivisionLineSize;
            int startY = mBgSize;
            int endX = startX;
            int endY = getHeight() - mBgSize;
            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }
    }

    /**
     * 绘制背景
     */
    private void drawBg(Canvas canvas) {
        RectF rect = new RectF(mBgSize, mBgSize, getWidth() - mBgSize, getHeight() - mBgSize);
        // 给画笔设置大小
        mPaint.setStrokeWidth(mBgSize);
        // 设置背景的颜色
        mPaint.setColor(mBgColor);
        // 画空心
        mPaint.setStyle(Paint.Style.STROKE);

        // 绘制背景  drawRect , drawRoundRect  ,
        // 如果有圆角那么就绘制drawRoundRect，否则绘制drawRect
        if (mBgCorner == 0) {
            canvas.drawRect(rect, mPaint);
        } else {
            canvas.drawRoundRect(rect, mBgCorner, mBgCorner, mPaint);
        }
    }

    /**
     * 添加一个密码
     */
    public void addPassword(String number) {
        // 把之前的密码取出来
        String password = getText().toString().trim();
        if (password.length() >= mPasswordNumber) {
            // 密码不能超过当前密码个输
            return;
        }
        // 密码叠加
        password += number;
        setText(password);
    }

    /**
     * 删除最后一位密码
     */
    public void deleteLastPassword() {
        String password = getText().toString().trim();
        // 判断当前密码是不是空
        if (TextUtils.isEmpty(password)) {
            return;
        }
        password = password.substring(0, password.length() - 1);
        setText(password);
    }

    // 设置当前密码是否已满的接口回掉
    private PasswordFullListener mListener;
    public void setOnPasswordFullListener(PasswordFullListener listener){
        this.mListener = listener;
    }
    /**
     * 密码已经全部填满
     */
    public interface PasswordFullListener {
        public void passwordFull(String password);
    }
}
