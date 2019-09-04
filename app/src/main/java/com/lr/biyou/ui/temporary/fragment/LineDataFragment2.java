package com.lr.biyou.ui.temporary.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.mywidget.line.DetailsMarkerView;
import com.lr.biyou.mywidget.line.MyLineChart;
import com.lr.biyou.mywidget.line.PositionMarker;
import com.lr.biyou.mywidget.line.RoundMarker;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.MyLineChartView;
import com.lr.biyou.ui.temporary.activity.MyShouMoneyActivity;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 折线统计 应收账款
 */
public class LineDataFragment2 extends BasicFragment implements RequestView, ReLoadingData, SelectBackListener {

    @BindView(R.id.linechaer_view)
    MyLineChartView mLinechaerView;
    @BindView(R.id.start_time_value_tv)
    TextView mStartTimeValueTv;
    @BindView(R.id.end_time_value_tv)
    TextView mEndTimeValueTv;
    @BindView(R.id.query_bt)
    TextView mQueryBt;
    @BindView(R.id.chart)
    MyLineChart mLineChart;
    @BindView(R.id.chart2)
    MyLineChart mLineChart2;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.rb_left)
    RadioButton mRbLeft;
    @BindView(R.id.rb_right)
    RadioButton mRbRight;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.date_lay)
    LinearLayout mDateLay;
    private LoadingWindow mLoadingWindow;

    private String mRequestTag = "";

    private DateSelectDialog mySelectDialog, mySelectDialog2;

    private String mStartTime = "";
    private String mEndTime = "";

    private String mSelectStartTime = "";
    private String mSelectEndTime = "";
    private String mSelectType = "";


    private List<String> xValues;   //x轴数据集合
    private List<Float> yValues;  //y轴数据集合

    public LineDataFragment2() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_linedata2;
    }


    @Override
    public void init() {
        mLoadingWindow = new LoadingWindow(getActivity(), R.style.Dialog);
        setBarTextColor();
        mLineChart.setVisibility(View.VISIBLE);
        mLineChart2.setVisibility(View.GONE);
        mLineChart.setNoDataText("暂无数据");
        mLineChart2.setNoDataText("暂无数据");
        Paint paint =  mLineChart.getPaint(Chart.PAINT_INFO);
        paint.setTextSize(60f);
        paint.setColor(ContextCompat.getColor(getActivity(),R.color.black_middle));
        Paint paint2 =  mLineChart2.getPaint(Chart.PAINT_INFO);
        paint2.setTextSize(60f);
        paint2.setColor(ContextCompat.getColor(getActivity(),R.color.black_middle));

        initData();
    }

    //查询应收账款池列表信息
    public void getShouMoneyInfoLine() {
        Map<String, Object> mapKehu = ((MyShouMoneyActivity) getActivity()).getSelectKehuMap();
        if (mapKehu == null) {
            ((MyShouMoneyActivity) getActivity()).showToastMsg("请选择付款方");
            mLoadingWindow.cancleView();
            return;
        }
        mRequestTag = MethodUrl.shoumoneyLine;
        Map<String, String> map = new HashMap<>();
        map.put("payfirmname", mapKehu.get("payfirmname") + ""); //购买方名称
        map.put("begindate", mStartTime); //开始时间
        map.put("enddate", mEndTime);  //结束时间
        map.put("qrytype", mShowType);  //结束时间
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.shoumoneyLine, map);
    }

    private void initData() {

        mySelectDialog = new DateSelectDialog(getActivity(), true, "选择日期", 21);
        mySelectDialog.setSelectBackListener(this);
        mySelectDialog2 = new DateSelectDialog(getActivity(), true, "选择日期", 22);
        mySelectDialog2.setSelectBackListener(this);


        //默认查询最近一周
        String sTime = UtilTools.getWeekAgo(new Date(), -6);
        String eTime = UtilTools.getStringFromDate(new Date(), "yyyyMMdd");

        mSelectStartTime = sTime;
        mSelectEndTime = eTime;
        mStartTime = mSelectStartTime;
        mEndTime = mSelectEndTime;

        mStartTimeValueTv.setText(UtilTools.getStringFromSting2(mStartTime, "yyyyMMdd", "yyyy-MM-dd"));
        mEndTimeValueTv.setText(UtilTools.getStringFromSting2(mEndTime, "yyyyMMdd", "yyyy-MM-dd"));

        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            xValues.add(UtilTools.dateTypeTo(UtilTools.getWeekAgo(new Date(), -1 * i)));
            yValues.add(0f);
        }
        Collections.reverse(xValues);

        mLinechaerView.setXValues(xValues);
        mLinechaerView.setYValues(yValues);

    }

    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        mLoadingWindow.cancleView();
        Intent intent;
        switch (mType) {
            case MethodUrl.shoumoneyLine:
                List<Map<String, Object>> list = (List<Map<String, Object>>) tData.get("pondinfoList");
                if (list != null && list.size() > 0) {
                    responseData(list);
                }

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.shoumoneyLine:
                        getShouMoneyInfoLine();
                        break;
                }
                break;
        }
    }

    private void responseData(List<Map<String, Object>> list) {
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        for (Map<String, Object> map : list) {
            xValues.add(UtilTools.dateTypeTo(map.get("acctime") + ""));
            yValues.add(UtilTools.fenToWanYuan(map.get("yszkcsw") + ""));
        }


        // xy轴集合自己添加数据
        mLinechaerView.setXValues(xValues);
        mLinechaerView.setYValues(yValues);

        mLinechaerView.updateUI();


        if (mShowType.equals("1")){
            initLine(list);
        }else {
            initLine2(list);
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        mLoadingWindow.cancleView();
        dealFailInfo(map, mType);
    }

    @Override
    public void reLoadingData() {
        mLoadingWindow.showView();
        getShouMoneyInfoLine();
    }


    private String mShowType = "1";
    @OnClick({R.id.start_time_value_tv, R.id.end_time_value_tv, R.id.query_bt,R.id.rb_left,R.id.rb_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_left:
                mShowType = "1";
                mDateLay.setVisibility(View.VISIBLE);
                mLineChart.setVisibility(View.VISIBLE);
                mLineChart2.setVisibility(View.GONE);
                getShouMoneyInfoLine();
                break;
            case R.id.rb_right:
                mShowType = "2";
                mDateLay.setVisibility(View.GONE);
                mLineChart.setVisibility(View.GONE);
                mLineChart2.setVisibility(View.VISIBLE);
                getShouMoneyInfoLine();
                break;
            case R.id.start_time_value_tv:
                mySelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.end_time_value_tv:
                mySelectDialog2.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.query_bt:

                String start = mStartTimeValueTv.getText().toString().trim();
                String end = mEndTimeValueTv.getText().toString().trim();

                mStartTime = UtilTools.getStringFromSting2(start, "yyyy-MM-dd", "yyyyMMdd");
                mEndTime = UtilTools.getStringFromSting2(end, "yyyy-MM-dd", "yyyyMMdd");

               /* long day = UtilTools.dateDiff(start, end, "yyyy-MM-dd");

                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
                long time = 0;
                long endTime = 0;

                Date date = new Date();
                try {
                    time = sd.parse(mStartTime).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (day < 6) {
                    ((MyShouMoneyActivity) getActivity()).showToastMsg("查询时间周期不能少于7日");
                    endTime = time + 6 * 24 * 60 * 60 * 1000;
                    date.setTime(endTime);
                    mEndTime = sd.format(date);
                }

                if (day > 14) {
                    ((MyShouMoneyActivity) getActivity()).showToastMsg("查询时间周期不能多于15日");
                    endTime = time + 14 * 24 * 60 * 60 * 1000;
                    date.setTime(endTime);
                    mEndTime = sd.format(date);

                }*/

                mStartTimeValueTv.setText(UtilTools.getStringFromSting2(mStartTime, "yyyyMMdd", "yyyy-MM-dd"));
                mEndTimeValueTv.setText(UtilTools.getStringFromSting2(mEndTime, "yyyyMMdd", "yyyy-MM-dd"));

                mLoadingWindow.showView();
                getShouMoneyInfoLine();

                break;
        }
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 21:
                mSelectStartTime = map.get("date") + "";
                mStartTimeValueTv.setText(map.get("year") + "-" + map.get("month") + "-" + map.get("day"));
                mStartTime = mSelectStartTime;

                break;
            case 22:
                mSelectEndTime = map.get("date") + "";
                mEndTimeValueTv.setText(map.get("year") + "-" + map.get("month") + "-" + map.get("day"));
                mEndTime = mSelectEndTime;
                break;

        }
    }

    private void initData1(){

    }

    private void initData2(){


    }


    private void initLine(List<Map<String, Object>> mDataList) {
        mLineChart.animateXY(1500,0);
        //1.设置x轴和y轴的点
        List<Entry> entries = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++){
            Map<String,Object> map = mDataList.get(i);
            entries.add(new Entry(i,UtilTools.fenToWanYuan(map.get("yszkcsw") + "") ));
            entries2.add(new Entry(i,UtilTools.fenToWanYuan(map.get("yjcsw") + "") ));
        }

        //2.把数据赋值到你的线条
        LineDataSet dataSet = new LineDataSet(entries, "1"); // add entries to dataset
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置折线图显示模式

        dataSet.setDrawCircles(true);//设置是否画圆
        dataSet.setDrawCircleHole(false);//设置是否空心圆
        dataSet.setColor(Color.parseColor("#7d7d7d"));//线条颜色
        dataSet.setColor(ContextCompat.getColor(getActivity(),R.color.line_color));//线条颜色
        dataSet.setCircleColor(ContextCompat.getColor(getActivity(),R.color.line_color));//圆点颜色
        dataSet.setLineWidth(1f);//线条宽度

        LineDataSet dataSet2 = new LineDataSet(entries2, "2"); // add entries to dataset
        dataSet2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置折线图显示模式
        dataSet2.setDrawCircles(true);//设置是否画圆
        dataSet2.setDrawCircleHole(false);//设置是否空心圆
        dataSet2.setColor(ContextCompat.getColor(getActivity(),R.color.line_color2));//线条颜色
        dataSet2.setCircleColor(ContextCompat.getColor(getActivity(),R.color.line_color2));//圆点颜色
        dataSet2.setLineWidth(1f);//线条宽度


        mLineChart.setScaleEnabled(true);//设置是否可以缩放
        mLineChart.setScaleXEnabled(true);//设置x轴可以缩放
        mLineChart.setScaleYEnabled(false);//设置y轴不可以缩放
        if (mDataList.size()>60){
//            mLineChart2.zoomToCenter(3f, 1f);
            mLineChart.zoom(3f, 1f,0,0);
            //mLineChart2.setScaleMinima(3f, 1f);
        }else  if (mDataList.size()>30) {
//            mLineChart2.zoomToCenter(1.6f, 1f);
            mLineChart.zoom(1.6f, 1f,0,0);

            //mLineChart2.setScaleMinima(1.6f, 1f);
        }else  if (mDataList.size()>15) {
//            mLineChart2.zoomToCenter(1.3f, 1f);
            mLineChart.zoom(1.3f, 1f,0,0);
            //mLineChart2.setScaleMinima(1.3f, 1f);
        }

//         mLineChart2.getViewPortHandler().setMaximumScaleX(5);
        mLineChart.getViewPortHandler().setMaximumScaleY(1);

        //mLineChart2.setGridBackgroundColor(R.color.colorAccent);//设置网格背景应与绘制的颜色。
        //mLineChart2.setDrawBorders(true);//启用/禁用绘制图表边框（chart周围的线）。
        //mLineChart2.setBorderColor(R.color.colorAccent);//设置chart边框线的颜色。


        //mLineChart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        //mLineChart2.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

        //mLineChart2.getLineData().getDataSets().get(0).setVisible(true);
        //设置样式
        YAxis rightAxis = mLineChart.getAxisRight();
        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);//设置y右边禁用
        rightAxis.setAxisMaximum(dataSet.getYMax() * 2);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        //是否绘制0所在的网格线
        leftAxis.setDrawZeroLine(false);
//        leftAxis.setSpaceBottom(10f);
        //设置图表左边的y轴禁用
        leftAxis.setEnabled(true);
        leftAxis.setAxisMaximum(dataSet.getYMax() * 1.4f);
        leftAxis.setAxisMinimum(0f);
        if (dataSet.getYMin()<0){
            leftAxis.setAxisMinimum(dataSet.getYMin() * 1.4f);
        }

        //设置x轴
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setGridColor(R.color.colorPrimary);
        xAxis.setGranularity(0.1f);//设置坐标轴间隔大小
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大x轴标签重绘
        xAxis.setAvoidFirstLastClipping(true);
        // 标签倾斜
        //xAxis.setLabelRotationAngle(45);
        List<String> list = new ArrayList<>();


        for (Map<String, Object> map : mDataList) {
            String s = UtilTools.getStringFromSting2(map.get("acctime") + "","yyyyMMdd","MM/dd");
            list.add(s);
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(list));

        //透明化图例
        Legend legend = mLineChart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);
        //legend.setYOffset(-2);

        //点击图表坐标监听
        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //查看覆盖物是否被回收
                if (mLineChart.isMarkerAllNull()) {
                    //重新绑定覆盖物
                    createMakerView(list);
                    //并且手动高亮覆盖物
                    //mLineChart.highlightValue(h);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        mLineChart.setDescription(description);

        //创建覆盖物
        createMakerView(list);

        //3.chart设置数据
        LineData lineData = new LineData(dataSet,dataSet2);
        //是否绘制线条上的文字
        lineData.setDrawValues(false);
        mLineChart.setData(lineData);
        mLineChart.invalidate();

    }
    private void initLine2(List<Map<String, Object>> mDataList) {

        mLineChart2.animateXY(1500,0);
        //1.设置x轴和y轴的点
        List<Entry> entries = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++){
            Map<String,Object> map = mDataList.get(i);
            entries.add(new Entry(i,UtilTools.fenToWanYuan(map.get("yszkcsw") + "") ));
            entries2.add(new Entry(i,UtilTools.fenToWanYuan(map.get("yjcsw") + "") ));
        }


        //2.把数据赋值到你的线条
        LineDataSet dataSet = new LineDataSet(entries, "2222"); // add entries to dataset
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置折线图显示模式

        dataSet.setDrawCircles(true);//设置是否画圆
        dataSet.setDrawCircleHole(false);//设置是否空心圆
        dataSet.setColor(Color.parseColor("#7d7d7d"));//线条颜色
        dataSet.setColor(ContextCompat.getColor(getActivity(),R.color.line_color));//线条颜色
        dataSet.setCircleColor(ContextCompat.getColor(getActivity(),R.color.line_color));//圆点颜色
        dataSet.setLineWidth(1f);//线条宽度

        LineDataSet dataSet2 = new LineDataSet(entries2, "2"); // add entries to dataset
        dataSet2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置折线图显示模式
        dataSet2.setDrawCircles(true);//设置是否画圆
        dataSet2.setDrawCircleHole(false);//设置是否空心圆
        dataSet2.setColor(ContextCompat.getColor(getActivity(),R.color.line_color2));//线条颜色
        dataSet2.setCircleColor(ContextCompat.getColor(getActivity(),R.color.line_color2));//圆点颜色
        dataSet2.setLineWidth(1f);//线条宽度


        mLineChart2.setScaleEnabled(true);//设置是否可以缩放
        mLineChart2.setScaleXEnabled(true);//设置x轴可以缩放
        mLineChart2.setScaleYEnabled(false);//设置y轴不可以缩放
        if (mDataList.size()>60){
//            mLineChart2.zoomToCenter(3f, 1f);
            mLineChart2.zoom(3f, 1f,0,0);
            //mLineChart2.setScaleMinima(3f, 1f);
        }else  if (mDataList.size()>30) {
//            mLineChart2.zoomToCenter(1.6f, 1f);
            mLineChart2.zoom(1.6f, 1f,0,0);

            //mLineChart2.setScaleMinima(1.6f, 1f);
        }else  if (mDataList.size()>15) {
//            mLineChart2.zoomToCenter(1.3f, 1f);
            mLineChart2.zoom(1.3f, 1f,0,0);
            //mLineChart2.setScaleMinima(1.3f, 1f);
        }

//         mLineChart2.getViewPortHandler().setMaximumScaleX(5);
        mLineChart2.getViewPortHandler().setMaximumScaleY(1);

        //mLineChart2.setGridBackgroundColor(R.color.colorAccent);//设置网格背景应与绘制的颜色。
        //mLineChart2.setDrawBorders(true);//启用/禁用绘制图表边框（chart周围的线）。
        //mLineChart2.setBorderColor(R.color.colorAccent);//设置chart边框线的颜色。


        // mLineChart2.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        //mLineChart2.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

        //mLineChart2.getLineData().getDataSets().get(0).setVisible(true);
        //设置样式
        YAxis rightAxis = mLineChart2.getAxisRight();
        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);//设置y右边禁用
        rightAxis.setAxisMaximum(dataSet.getYMax() * 2);

        YAxis leftAxis = mLineChart2.getAxisLeft();
        //     // 设置y轴数据偏移量
//        leftAxis.setXOffset(30);
//        leftAxis.setYOffset(-3);
        leftAxis.setAxisMaximum(dataSet.getYMax() * 1.4f);
        leftAxis.setAxisMinimum(0f);
        if (dataSet.getYMin()<0){
            leftAxis.setAxisMinimum(dataSet.getYMin() * 1.4f);
        }

        leftAxis.setSpaceBottom(10f);
        //设置图表左边的y轴禁用
        leftAxis.setEnabled(true);

        //设置x轴
        XAxis xAxis = mLineChart2.getXAxis();
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setGridColor(R.color.colorPrimary);
        xAxis.setGranularity(0.1f);//设置坐标轴间隔大小
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
//        xAxis.setGridColor(ContextCompat.getColor(getActivity(),R.color.divide_line));
//        xAxis.setGridLineWidth(1f);
        xAxis.setAxisLineColor(ContextCompat.getColor(getActivity(),R.color.yellow));
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大x轴标签重绘
        // 标签倾斜
        //xAxis.setLabelRotationAngle(45);
        List<String> list = new ArrayList<>();


        for (Map<String, Object> map : mDataList) {
            String dateStr = map.get("acctime")+"";
            if (dateStr.endsWith("01")){
                String s = UtilTools.getStringFromSting2(map.get("acctime") + "","yyyyMMdd","MM");
                list.add(s+"月");
            }else {
                String s = UtilTools.getStringFromSting2(map.get("acctime") + "","yyyyMMdd","MM/dd");
                list.add(s);
            }
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(list));

        //透明化图例
        Legend legend = mLineChart2.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);
        //legend.setYOffset(-2);

        //点击图表坐标监听
        mLineChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //查看覆盖物是否被回收
                if (mLineChart2.isMarkerAllNull()) {
                    //重新绑定覆盖物
                    createMakerView2(list);
                    //并且手动高亮覆盖物
                    mLineChart2.highlightValue(h);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        mLineChart2.setDescription(description);

        //创建覆盖物
        createMakerView2(list);

        //3.chart设置数据
        LineData lineData = new LineData(dataSet,dataSet2);
        //是否绘制线条上的文字
        lineData.setDrawValues(false);
        mLineChart2.setData(lineData);


        //一个页面显示的数据太多了，都不看清楚，怎么样设置一个页面显示固定条数的数据，如果数据太多需要手动滑动看到
//        mLineChart2.setVisibleXRangeMaximum(7.5f);

        mLineChart2.invalidate(); // refresh

    }

    /**
     * 创建覆盖物
     */
    public void createMakerView(List<String> list) {
        DetailsMarkerView detailsMarkerView = new DetailsMarkerView(getActivity());
        detailsMarkerView.setList(list);
        detailsMarkerView.setChartView(mLineChart);
        mLineChart.setDetailsMarkerView(detailsMarkerView);
        mLineChart.setPositionMarker(new PositionMarker(getActivity()));
        mLineChart.setRoundMarker(new RoundMarker(getActivity()));
    }
    /**
     * 创建覆盖物
     */
    public void createMakerView2(List<String> list) {
        DetailsMarkerView detailsMarkerView = new DetailsMarkerView(getActivity());
        detailsMarkerView.setList(list);
        detailsMarkerView.setChartView(mLineChart2);
        mLineChart2.setDetailsMarkerView(detailsMarkerView);
        mLineChart2.setPositionMarker(new PositionMarker(getActivity()));
        mLineChart2.setRoundMarker(new RoundMarker(getActivity()));
    }

}
