package com.lr.biyou.mywidget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.ArrayList;
import java.util.List;

/**
 *手势登录（小）
 */
public class ShouShiPatternViewSmall extends View  {
    private int i=0;

    private List<Integer> pause=new ArrayList<>();
    private static final int COUNT = 3;
    Cell [] cell;
    int [] selectedCell;
    int RADIUS ,OFFSET;
    int ScreenWidth,ScreenHeight;
    int startX,startY,selectedCount,lastX,lastY;
    boolean drawFinish ;

    Paint mPaint ;

    public ShouShiPatternViewSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
       init(context);
    }
    //将已选择的点，进行标记
    public  void  cellSeleced(List<Integer> list){
        for (i=0;i<9;i++){
            cell[i].setIsSelected(false);
        }
        for (i=0;i<list.size();i++){
            LogUtilDebug.i("show",">>>:"+list.get(i));
            int num = list.get(i);
            if (num<0){
                return;
            }
            cell[list.get(i)].setIsSelected(true);
        }


       /* int[] arr = new int[str.length()];
        for(int i=0; i<str.length(); i++){
           // arr[i] = Integer.parseInt(str.substring(i,i+1));
            cell[Integer.parseInt(str.substring(i,i+1))].setIsSelected(true);
        }
*/

    }
    private void initCell(){
        //初始化各点
        for(int i = 0 ; i < COUNT ; i++ )
            for (int j = 0 ; j < COUNT ; j++) {
                cell[i * COUNT + j].setIsSelected(false);
                cell[i * COUNT + j].setX(startX + OFFSET * j - RADIUS/2);
                cell[i * COUNT + j].setY(startY + OFFSET * i - RADIUS/2);
            }
    }
    private void init(Context context){

        cell = new Cell[COUNT * COUNT];
        selectedCell = new int[COUNT*COUNT];
        mPaint = new Paint();
        //获取屏幕的宽度和高度
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);

        ScreenWidth = dm.widthPixels/4;
        ScreenHeight = dm.heightPixels/6;

        this.setMinimumWidth(ScreenWidth);
        this.setMinimumHeight(ScreenHeight);

        drawFinish = false; //是否绘制完成
        selectedCount = 0; //已经选中的点个数
        RADIUS = ScreenWidth / 15; //半径
        OFFSET = ScreenWidth / 4 ; //点之间的间距
        startX = OFFSET*7; //起始点横坐标
        startY = (ScreenHeight - OFFSET * 2) / 4; //起始点纵坐标

        for(int i = 0 ; i < COUNT*COUNT ; i++){
            cell[i] = new Cell();
        }
        initCell();
    }

    @SuppressLint("ResourceAsColor")
    void drawCell(Canvas canvas){
       for(int i = 0 ; i < COUNT*COUNT ; i++){
            //选择画笔&&画圆
            if(cell[i].isSelected()){
                //mPaint.setColor(Color.RED);
                //mPaint.setStrokeWidth(10);
                //画圆
                //  canvas.drawCircle(cell[i].getX(),cell[i].getY(),RADIUS,mPaint);
                //mPaint.setStrokeWidth(20);
                mPaint.setColor(Color.RED);
                mPaint.setStyle(Paint.Style.FILL);
                //画点
                canvas.drawCircle(cell[i].getX(),cell[i].getY(), 20,mPaint);
               // canvas.drawPoint(cell[i].getX(),cell[i].getY(),mPaint);
            } else {
               // mPaint.setColor(Color.WHITE);
                //mPaint.setStrokeWidth(5);
                //画圆
               // canvas.drawCircle(cell[i].getX(),cell[i].getY(),RADIUS,mPaint);
                //画点
                mPaint.setColor(Color.GRAY);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cell[i].getX(),cell[i].getY(), 20,mPaint);
                //canvas.drawPoint(cell[i].getX(),cell[i].getY(),mPaint);
            }

        }
    }

    void drawLine(Canvas canvas) {
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(5);
        for(int i = 1 ; i < selectedCount ; i++){
            Cell lastCell = cell[selectedCell[i-1]],thisCell = cell[selectedCell[i]];
            canvas.drawLine(lastCell.getX(), lastCell.getY(), thisCell.getX(), thisCell.getY(), mPaint);
        }
        if(selectedCount !=0 &&(lastX !=0 || lastY != 0)){
            canvas.drawLine(cell[selectedCell[selectedCount - 1]].getX(), cell[selectedCell[selectedCount - 1]].getY(), lastX, lastY, mPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStyle(Paint.Style.STROKE);
        drawCell(canvas);
        drawLine(canvas);
    }


    public class Cell {
        private int x;
        private int y;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
