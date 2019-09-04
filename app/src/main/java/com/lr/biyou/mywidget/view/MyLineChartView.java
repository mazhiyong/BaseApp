package com.lr.biyou.mywidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.lr.biyou.R;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class MyLineChartView extends View {

    private int originX; // 原点x坐标

    private int originY; // 原点y坐标

    private int firstPointX; //第一个点x坐标
    private int firstPointY; //第一个点y坐标

    private int firstMinX; // 移动时第一个点的最小x值

    private int firstMaxX; //移动时第一个点的最大x值

    private int intervalX ; // x坐标刻度的间隔

    private int intervalY ; // y轴刻度的间隔

    private List<String> xValues;

    private List<Float> yValues;

    private int mWidth; // 控件宽度

    private int mHeight; // 控件高度

    private int startX; // 滑动时上一次手指的x坐标值

    private int xyTextSize ; //xy轴文字大小

    private int paddingTop ;// 默认上下左右的padding

    private int paddingLeft ;

    private int paddingRight ;

    private int paddingDown ;

    private int scaleHeight ; // x轴刻度线高度
    private int scaleLength ; // Y轴刻度线长度

    private int textToXYAxisGap ; // xy轴的文字距xy线的距离

    private int leftRightExtra ; //x轴左右向外延伸的长度

    private int lableCountY = 6; // Y轴刻度个数

    private int bigCircleR ;

    private int smallCircleR ;

    private float minValueY; // y轴最小值

    private float maxValueY = 0; // y轴最大值

    private int shortLine = 34; // 比例图线段长度

    private Paint paintWhite, paintBlue,paintPoint, paintRed, paintBack, paintText, p;

    private int backGroundColor = Color.parseColor("#ffffff"); // view的背景颜色

    private GestureDetector gestureDetector;

    private String legendTitle = "";

    private int addDashPath = 0; //平行于Y轴虚线突出长度

    public MyLineChartView(Context context) {
        this(context, null);
    }

    public MyLineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(context, new MyOnGestureListener());

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LogUtilDebug.i("show","屏幕宽度："+width);
        int interva= (width-100)/7;
        LogUtilDebug.i("show","刻度间隔："+interva);
        intervalX = interva; // x坐标刻度的间隔
        intervalY = interva; // y轴刻度的间隔
        leftRightExtra = intervalX / 3;

        xyTextSize = UtilTools.dip2px(context,11);//xy轴文字大小

        paddingTop = UtilTools.dip2px(context,5);// 默认上下左右的padding

        paddingLeft = UtilTools.dip2px(context,50);

        paddingRight = UtilTools.dip2px(context,15);

        paddingDown = UtilTools.dip2px(context,20);

        scaleHeight = UtilTools.dip2px(context,5); // x轴刻度线高度
        scaleLength =UtilTools.dip2px(context,40); // Y轴刻度线长度

        textToXYAxisGap = UtilTools.dip2px(context,10); // xy轴的文字距xy线的距离
        bigCircleR = UtilTools.dip2px(context,3);
        smallCircleR = UtilTools.dip2px(context,2);

        addDashPath = UtilTools.dip2px(context,10);

        initPaint(context);
    }

    private void initPaint(Context context) {
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(ContextCompat.getColor(context,R.color.line_background));
        p.setStyle(Paint.Style.STROKE);

        paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintWhite.setColor(Color.BLACK);
        paintWhite.setStyle(Paint.Style.STROKE);


        paintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBlue.setColor(ContextCompat.getColor(context,R.color.font_c));
        paintBlue.setStrokeWidth(3f);
        paintBlue.setStyle(Paint.Style.STROKE);

        paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPoint.setColor(ContextCompat.getColor(context,R.color.font_c));
        paintPoint.setStrokeWidth(3f);
        paintPoint.setStyle(Paint.Style.FILL);


        paintBack = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBack.setColor(Color.parseColor("#ffffff"));
        paintBack.setStyle(Paint.Style.FILL);

        paintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRed.setColor(ContextCompat.getColor(context,R.color.font_c));
        paintRed.setStrokeWidth(1f);
        paintRed.setTextSize(UtilTools.sp2px(context,12));
        paintRed.setStyle(Paint.Style.FILL);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(xyTextSize);
        paintText.setStrokeWidth(2f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();

        originX = paddingLeft - leftRightExtra;
        originY = mHeight - paddingDown;
        LogUtilDebug.i("show","originY:"+originY+"  mheight:"+mHeight);

        firstPointX = paddingLeft;
        firstMinX = mWidth - originX - (xValues.size() - 1) * intervalX - leftRightExtra;
        // 滑动时，第一个点x值最大为paddingLeft，在大于这个值就不能滑动了
        firstMaxX = firstPointX;
        setBackgroundColor(backGroundColor);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        drawBiaoGeLine(canvas);
        //绘制折线
        drawBrokenLine(canvas);
        //绘制折线中的原点
        drawPoint(canvas);
        //绘制X轴
        drawX(canvas);
        //drawLegend(canvas);
        //绘制Y轴
        drawY(canvas);


    }

    private void drawPoint(Canvas canvas) {
        canvas.save();
        // 折线中的圆点
        float aver = (lableCountY - 1) * intervalY / (maxValueY - minValueY);
        for (int i = 0; i < yValues.size(); i++) {
//            canvas.drawCircle(firstPointX + i * intervalX,
//                    mHeight - paddingDown - yValues.get(i) * aver + minValueY * aver, bigCircleR, paintPoint);
          /*  canvas.drawCircle(firstPointX + i * intervalX,
                    mHeight - paddingDown  - yValues.get(i) * aver + minValueY * aver, smallCircleR, paintBack);*/
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.HALF_DOWN);
            canvas.drawText(df.format(yValues.get(i))+"",firstPointX + i * intervalX+10,mHeight - paddingDown  - yValues.get(i) * aver + minValueY * aver,paintRed);

        }
        //将折线超出x轴坐标的部分截取掉（左边）
        paintBack.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        RectF rectF = new RectF(0, 0, originX, mHeight);
        canvas.drawRect(rectF, paintBack);
        canvas.restore();
    }

    /**
     * 画x轴
     *
     * @param canvas
     */
    private void drawX(Canvas canvas) {
        Path path = new Path();
        path.moveTo(originX, originY);
        for (int i = 0; i < xValues.size(); i++) {
            // x轴线
            path.lineTo(mWidth - paddingRight, originY);  // 写死不变
            // x轴箭头
            canvas.drawLine(mWidth - paddingRight, originY, mWidth - paddingRight - 15, originY + 10, paintWhite);
            canvas.drawLine(mWidth - paddingRight, originY, mWidth - paddingRight - 15, originY + 10, p);
            canvas.drawLine(mWidth - paddingRight, originY, mWidth - paddingRight - 15, originY - 10, paintWhite);
            canvas.drawLine(mWidth - paddingRight, originY, mWidth - paddingRight - 15, originY - 10, p);

            // x轴线上的刻度线
            //firstPointX x轴起始点

            canvas.drawLine(firstPointX + i * intervalX, originY, firstPointX + i * intervalX, originY - scaleHeight, paintWhite);
            // x轴上的文字
            canvas.drawText(xValues.get(i), firstPointX + i * intervalX - getTextWidth(paintText, "17.01") / 2,
                    originY + textToXYAxisGap + getTextHeight(paintText, "17.01"), paintText);
        }
        canvas.drawPath(path, paintWhite);



//        // 平行于 x轴虚线
//        Path path1 = new Path();
//      DashPathEffect dash = new DashPathEffect(new float[]{8, 10, 8, 10}, 0);
//        p.setPathEffect(dash);
//        for (int i = 0; i < lableCountY; i++) {
//            path1.moveTo(originX, mHeight - paddingDown  - i * intervalY);
//            path1.lineTo(mWidth - paddingRight, mHeight - paddingDown  - i * intervalY);
//        }
//        canvas.drawPath(path1, p);
    }

    /**
     * 画折线
     *
     * @param canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        canvas.save();
        // y轴文字
        minValueY = 0;
        for (int i = 0; i < yValues.size(); i++) {
            // 找出y轴的最大最小值
            if (yValues.get(i) > maxValueY) {
                maxValueY = yValues.get(i);
            }
            if (yValues.get(i) < minValueY) {
                minValueY = yValues.get(i);
            }
        }


        if (maxValueY < 1.0f || maxValueY ==1.0f){
            maxValueY = 1.0f;

        }else if (maxValueY <5.0f || maxValueY == 5.0f){
            maxValueY = 5.0f;

        }else if (maxValueY < 10.f || maxValueY ==10.f){

            maxValueY = 10.0f;
        }



        // 画折线
        float aver = (lableCountY - 1) * intervalY / (maxValueY - minValueY);
        Path path = new Path();

        //path.moveTo(firstPointX, originY);
        float endx = 0;
        float endy = 0;

        //背景色填充
        for (int i = 0; i < yValues.size(); i++) {
            if (i == 0) {
                path.moveTo(firstPointX, mHeight - paddingDown - yValues.get(i) * aver + minValueY * aver);
            } else {
                path.lineTo(firstPointX + i * intervalX, mHeight - paddingDown  - yValues.get(i) * aver + minValueY * aver);
                endx = firstPointX + i * intervalX;
                endy = mHeight - paddingDown  - yValues.get(i) * aver + minValueY * aver;
            }



        }

    /*   //背景色渐变
        Shader mShader = new LinearGradient(endx,endy,endx,originY,new int[] {Color.RED,Color.TRANSPARENT},null,Shader.TileMode.CLAMP);
        //新建一个线性渐变，
        // 前两个参数是渐变开始的点坐标，
        // 第三四个参数是渐变结束的点的坐标。
        // 连接这2个点就拉出一条渐变线了，玩过PS的都懂。
        // 然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，
        // 如果为空，每个颜色就是均匀分布的。
        // 最后是模式，这里设置的是循环渐变

        paintBlue.setShader(mShader);
*/
        /*path.lineTo(firstPointX + (yValues.size()-1) * intervalX,originY);
        path.close();
        paintBlue.setStyle(Paint.Style.FILL);*/
        //getShadeColorPaint();
        canvas.drawPath(path, paintBlue);

        //将折线超出x轴坐标的部分截取掉（左边）
        paintBack.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        RectF rectF = new RectF(0, 0, originX, mHeight);
        canvas.drawRect(rectF, paintBack);
        canvas.restore();
    }



    /**
     * 画y轴
     *
     * @param canvas
     */
    private void drawY(Canvas canvas) {
        canvas.save();
        Path path = new Path();
        path.moveTo(originX, originY);

        for (int i = 0; i < lableCountY; i++) {
            // y轴线
            if (i == 0) {
                path.lineTo(originX, mHeight - paddingDown );
            } else {
                path.lineTo(originX, mHeight - paddingDown  - i * intervalY);
            }

            //Y轴刻度线
            int lastPointY = mHeight - paddingDown  - i * intervalY;
            if (i == lableCountY - 1) {
                int lastY = lastPointY - leftRightExtra-leftRightExtra / 2;
                // y轴最后一个点后，需要额外加上一小段，就是一个半leftRightExtra的长度
                canvas.drawLine(originX, lastPointY, originX, lastY, paintWhite);
                canvas.drawLine(originX, lastPointY, originX, lastY, p);
                // y轴箭头
                canvas.drawLine(originX, lastY, originX - 10, lastY + 15, paintWhite);
                canvas.drawLine(originX, lastY, originX - 10, lastY + 15, p);
                canvas.drawLine(originX, lastY, originX + 10, lastY + 15, paintWhite);
                canvas.drawLine(originX, lastY, originX + 10, lastY + 15, p);
            }


            Path path1 = new Path();
            path1.moveTo(originX, mHeight - paddingDown  - i * intervalY);
            path1.lineTo(scaleLength, mHeight - paddingDown  - i * intervalY);
            canvas.drawPath(path1, paintWhite);
            canvas.drawPath(path1, p);

        }
        canvas.drawPath(path, paintWhite);
        canvas.drawPath(path, p);


        List<String> yTitles = new ArrayList<>() ;
        if (maxValueY < 1.0f || maxValueY ==1.0f){

            yTitles = new ArrayList<>();
            for (int i = 0; i < lableCountY; i++) {
                String s = 0.2*i+"";
                yTitles.add(s.substring(0,3));
                //LogUtilDebug.i("show","num:"+decimalFormat.format(minValueY + i * space));
            }

        }else if (maxValueY <5.0f || maxValueY == 5.0f){
            yTitles = new ArrayList<>();
            for (int i = 0; i < lableCountY; i++) {
                yTitles.add(1*i+".0");
                //LogUtilDebug.i("show","num:"+decimalFormat.format(minValueY + i * space));
            }


        }else if (maxValueY < 10.0f || maxValueY ==10.0f){

            yTitles = new ArrayList<>();
            for (int i = 0; i < lableCountY; i++) {
                yTitles.add(2*i+".0");
                //LogUtilDebug.i("show","num:"+decimalFormat.format(minValueY + i * space));
            }


        }else {
            // y轴文字
            int space = (int) ((maxValueY - minValueY) / (lableCountY - 1));
            LogUtilDebug.i("show","spece:"+space);
            // 设置Y轴刻度为 5或10 的倍数
            //判断个位是否是以5或者是0
            //10的倍数
            int douTen = (int) (space/10);
            //5的倍数
            int douFive = (int) (space/5);
            int digst =  (int)space % 10;

            if (digst>0 && digst <5){
                //以10的倍数为Y轴单位值
                if (digst < 3){
                    space = (douTen+1)*10;
                }else { //以5的倍数为Y轴单位值
                    space = (douFive+1)*5;
                }
            }
            if (digst > 5 && digst<9){
                //以5的倍数为Y轴单位值
                if (digst < 8){
                    space = (douFive+1)*5;
                }else { //以10的倍数为Y轴单位值
                    space = (douTen+1)*10;
                }
            }
            LogUtilDebug.i("show","spece2:"+space);

            DecimalFormat decimalFormat = new DecimalFormat("");
            yTitles = new ArrayList<>();
            for (int i = 0; i < lableCountY; i++) {
                yTitles.add(decimalFormat.format(minValueY + i * space));
                //LogUtilDebug.i("show","num:"+decimalFormat.format(minValueY + i * space));
            }
        }
      /*
        //设置开始单位为5 或 10的倍数
        //判断个位是否是以5或者是0
        //10的倍数
        int minTen = (int) (minValueY/10);
        //5的倍数
        int minFive = (int) (minValueY/5);
        int minDigst =  (int)minValueY % 10;


        // 小于10
        if (minTen == 0){
            if (minDigst < 5){
                minValueY = 0;
            }else {
                minValueY = 5;
            }
        }else {
            if (minDigst < 5){
                minValueY = minTen * 10;
            }else {
                minValueY = minFive*5;
            }
        }
*/



        //绘制Y轴数值
        for (int i = 0; i < yTitles.size(); i++) {
            canvas.drawText(yTitles.get(i), originX  - getTextWidth(paintText, "00.00"),
                    mHeight - paddingDown  - i * intervalY + getTextHeight(paintText, "00.00") / 2, paintText);
        }
        // 截取折线超出部分（右边）
        paintBack.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        RectF rectF = new RectF(mWidth, 0, mWidth, mHeight);
        canvas.drawRect(rectF, paintBack);
        canvas.restore();


        // 平行于y轴虚线


//        Path path1 = new Path();
//       /* DashPathEffect dash = new DashPathEffect(new float[]{8, 10, 8, 10}, 0);
//        p.setPathEffect(dash);*/
//        for (int i = 0; i < xValues.size(); i++) {
//            path1.moveTo(firstPointX + i * intervalX, originY);
//            path1.lineTo(firstPointX + i * intervalX, mHeight - paddingDown  - (lableCountY-1)* intervalY - addDashPath );
//        }
//        canvas.drawPath(path1, p);
    }

    private void drawBiaoGeLine(Canvas canvas){

        // 平行于 x轴虚线
        Path path1 = new Path();
    /*  DashPathEffect dash = new DashPathEffect(new float[]{8, 10, 8, 10}, 0);
        p.setPathEffect(dash);*/
        for (int i = 0; i < lableCountY; i++) {
            path1.moveTo(originX, mHeight - paddingDown  - i * intervalY);
            path1.lineTo(mWidth - paddingRight, mHeight - paddingDown  - i * intervalY);
        }
        canvas.drawPath(path1, p);

        //drawLegend(canvas);
        //绘制折线中的原点
        Path path2 = new Path();
       /* DashPathEffect dash = new DashPathEffect(new float[]{8, 10, 8, 10}, 0);
        p.setPathEffect(dash);*/
        for (int i = 0; i < xValues.size(); i++) {
            path2.moveTo(firstPointX + i * intervalX, originY);
            path2.lineTo(firstPointX + i * intervalX, mHeight - paddingDown  - (lableCountY-1)* intervalY - addDashPath );
        }
        canvas.drawPath(path2, p);
    }

    /**
     * 画图例
     */
    private void drawLegend(Canvas canvas) {
        // 开始点的坐标
        int x = 350;
        int y = mHeight - (paddingDown - textToXYAxisGap - getTextHeight(paintText, "06.00")) / 2;
        canvas.save();
        canvas.drawLine(x, y, x + 2 * shortLine, y, paintBlue);
        canvas.drawCircle(x + shortLine, y, bigCircleR, paintBlue);
        canvas.drawCircle(x + shortLine, y, smallCircleR, paintBack);
        //canvas.drawText(legendTitle, x + 2 * shortLine + 10, y + getTextHeight(paintText, legendTitle) / 2 - 2, paintText);

        canvas.drawLine(x + 2 * shortLine + getTextWidth(paintText, legendTitle) + 20,
                y, x + 2 * shortLine + getTextWidth(paintText, legendTitle) + 20 + 2 * shortLine, y, paintRed);
        canvas.drawCircle(x + 2 * shortLine + getTextWidth(paintText, legendTitle) + 20 + shortLine, y, bigCircleR, paintRed);
        canvas.drawCircle(x + 2 * shortLine + getTextWidth(paintText, legendTitle) + 20 + shortLine, y, smallCircleR, paintBack);
        /*canvas.drawText("护士填写", x + 2 * shortLine + getTextWidth(paintText, legendTitle) + 30 + 2 * shortLine,
                y + getTextHeight(paintText, legendTitle) / 2 - 2, paintText);*/
        canvas.restore();
    }


    /**
     * 手势事件
     */
    class MyOnGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) { // 按下事件
            return false;
        }

        // 按下停留时间超过瞬时，并且按下时没有松开或拖动，就会执行此方法
        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) { // 单击抬起
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e1.getX() > originX && e1.getX() < mWidth - paddingRight &&
                    e1.getY() > paddingTop && e1.getY() < mHeight - paddingDown) {
                //注意：这里的distanceX是e1.getX()-e2.getX()
                distanceX = -distanceX;
                if (firstPointX + distanceX > firstMaxX) {
                    firstPointX = firstMaxX;
                } else if (firstPointX + distanceX < firstMinX) {
                    firstPointX = firstMinX;
                } else {
                    firstPointX = (int) (firstPointX + distanceX);
                }
                invalidate();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        } // 长按事件

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (yValues.size() < 7) {
            return false;
        }
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void setXValues(List<String> values) {
        this.xValues = values;
    }

    public void setYValues(List<Float> values) {
        this.yValues = values;
    }

    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextWidth(Paint paint, String text) {
        return (int) paint.measureText(text);
    }

    /**
     * 获取文字的高度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }


    public void updateUI(){
        requestLayout();//执行onMeasure()方法和onLayout()方法
        invalidate();//执行onDraw()方法
    }



    // 修改笔的颜色
    private void getShadeColorPaint() {
        paintBlue.setStyle(Paint.Style.FILL);
        Shader mShader = new LinearGradient(300, 50, 300, 400,
                new int[] { Color.parseColor("#55FF7A00"), Color.TRANSPARENT }, null, Shader.TileMode.CLAMP);
        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        paintBlue.setShader(mShader);
    }
}