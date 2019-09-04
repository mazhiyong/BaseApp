package com.wanou.framelibrary.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wanou.framelibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by wodx521
 * Date on 2018/11/22.
 */

public class AutoChangeRowLayout extends ViewGroup {
    //自定义控件的初级
    //自定义控件高级 QQ5.0新特性

    public float mHorizontalSpacing;
    public float mVerticalSpacing;
    private List<Line> lineList = new ArrayList<>();

    public AutoChangeRowLayout(Context context) {
        this(context, null);
    }

    public AutoChangeRowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoChangeRowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int space = (int) (5 * context.getResources().getDisplayMetrics().density + 0.5f);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoChangeRowLayout, defStyleAttr, 0);
        mHorizontalSpacing = typedArray.getDimension(R.styleable.AutoChangeRowLayout_horizontal_spacing, space);
        mVerticalSpacing = typedArray.getDimension(R.styleable.AutoChangeRowLayout_vertical_spacing, space);
        typedArray.recycle();
    }

    public void setSpacing(float horizontalSpacing, float verticalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
        mVerticalSpacing = verticalSpacing;
        invalidate();
    }

    //onMeasure--->onLayout----->onDraw
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //获取距离左侧和顶端内间距,这2个内间距可以决定行中的第一个控件的位置,指定了第一个控件的位置
        //就指定了行对象的开始位置
        int left = getPaddingLeft();
        int top = getPaddingTop();
        //获取行对象集合中的每一个行,然后指定行对象所在屏幕的位置
        for (int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);
            line.onLayout(left, top);
            //top = top+行高度+竖直间距;
            top += line.lineHeight + mVerticalSpacing;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽高如何处理,宽高有何作用?
        lineList.clear();
        //1.先获取自定义控件的宽度,自定义控件的模块默认都是填充满屏幕的
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //2.获取自定义控件可用的宽度大小
        int invalidWidth = width - getPaddingLeft() - getPaddingRight();
        //3.创建行对象,让TextView能够添加在某一个行对象上
        Line line = new Line();
        //4.获取FlowLayout内部的每一个TextView,决定放置那个行对象上
        for (int i = 0; i < getChildCount(); i++) {
            View viewChild = getChildAt(i);
            //获取viewChild宽度,用于判断后续情况?满足
            viewChild.measure(0, 0);
            int childWidth = viewChild.getMeasuredWidth();

            if (line.getLineViewCount() == 0) {
                //情况一:当前行一个控件都没有,则直接将viewChlid添加进来
                line.addLineView(viewChild);
            } else if (line.lineWidth + childWidth + mHorizontalSpacing > invalidWidth) {
                //情况二:当前行已经有一些控件了,现在遍历的到的viewChild因为内部文字过多,导致此行放下,换行放置
                //如果现在要换行,则认为上一行控件已经放满了,则需要将行对象添加到行对象的集合中
                lineList.add(line);
                //创建一个新的行对象,去放置要换行的控件
                line = new Line();
                line.addLineView(viewChild);
            } else {
                //情况三:当前行已经有一些控件了,现在遍历的到的viewChild文字很少,在当前行可以放下
                line.addLineView(viewChild);
            }

            //最后一个行对象需要添加在行对象的集合中,最后一个条目一定在最后的行对象上
            if (i == getChildCount() - 1) {
                lineList.add(line);
            }
        }
        //计算自定义控件的高度
        int totalHeight = 0;
        //所有行的高度之和+所有行的内间距+顶部的内间距+底部的内间距
        for (int i = 0; i < lineList.size(); i++) {
            Line line1 = lineList.get(i);
            totalHeight += line1.lineHeight;
        }
        totalHeight += (lineList.size() - 1) * mVerticalSpacing;
        totalHeight += getPaddingTop() + getPaddingBottom();

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        //控件应该放置在何处??
    }

    //创建一个行对象所在的类
    public class Line {
        //指定行对象的高度,所有行对象的高度之和+行和行的间距,得到现有自定义控件的高度

        //行的高度,是由行中最高的控件决定的
        private int lineHeight;
        //行中已经使用的宽度
        private int lineWidth;

        //创建一个集合,用于记录此行中有多少个控件
        private List<View> lineViewList = new ArrayList<>();

        //返回行对象中有几个view,放置了几个控件
        public int getLineViewCount() {
            return lineViewList.size();
        }

        //提供一个向行中添加对象的方法
        public void addLineView(View view) {
            lineViewList.add(view);

            //每当调用此方法,行对象中的行高度和行已使用的宽度,都会变化

            view.measure(0, 0);
            int viewHeight = view.getMeasuredHeight();
            int viewWidth = view.getMeasuredWidth();
            //取最大的高度作为行的高度
            lineHeight = Math.max(lineHeight, viewHeight);

            /*
                addLineView(view1);  lineWidth = view1宽度+水平间距
                addLineView(view2);  lineWidth = lineWidth + view2宽度+水平间距
                addLineView(view3);  lineWidth = lineWidth + view3宽度+水平间距
                lineWidth += view3宽度+水平间距;
             */
            //行中已经使用的宽度
            lineWidth += viewWidth + mHorizontalSpacing;
        }

        //留白区域的处理??留白区域交由行对象进行平均划分给内部的所有控件
        public void onLayout(int left, int top) {
            //此方法不仅需要将留白区域平均分配给行对象中的所有的控件,还需要指定行对象中每一个控件的所在位置
            //获取自定义控件的宽度
            /*int 剩余的空白宽度 = getMeasuredWidth()-lineWidth-getPaddingLeft()-getPaddingRight();
            int 每一个控件还能分配到的空间 = 剩余的空白宽度/本行有多少个控件;
            int 此控件的最终的宽度 = 拿到控件的原有的宽度+每一个控件还能分配到的空间;*/
            int totalSurplusWidth = getMeasuredWidth() - lineWidth - getPaddingLeft() - getPaddingRight();

            //拿总剩余宽度除以行中有多少个控件得到结果为每一个控件还需要分配的宽度大小
            int surplusWidth = totalSurplusWidth / getLineViewCount();

            //给行对象中的每一个控件,补充剩余区域
            for (int i = 0; i < getLineViewCount(); i++) {
                View view = lineViewList.get(i);
                view.measure(0, 0);

                //平均分配剩余宽度给控件,如需平均分配去掉下行代码注释
                //int viewWidth = view.getMeasuredWidth()+surplusWidth;
                int viewWidth = view.getMeasuredWidth();
                int viewHeight = view.getMeasuredHeight();

                //要求view对象按照自己指定的宽高大小显示
                int width32 = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);
                int height32 = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);

                view.measure(width32, height32);

                //每一个循环遍历到的view应该在那个位置展示
                view.layout(left, top, left + viewWidth, top + viewHeight);
                left += viewWidth + mHorizontalSpacing;
            }
        }
    }
}

