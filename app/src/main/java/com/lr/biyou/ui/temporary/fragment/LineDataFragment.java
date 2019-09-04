package com.lr.biyou.ui.temporary.fragment;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.MyLineChartView;
import com.lr.biyou.ui.temporary.activity.MyShouMoneyActivity;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 折线统计 应收账款
 */
public class LineDataFragment extends BasicFragment implements RequestView, ReLoadingData , SelectBackListener {

    @BindView(R.id.linechaer_view)
    MyLineChartView mLinechaerView;
    @BindView(R.id.start_time_value_tv)
    TextView mStartTimeValueTv;
    @BindView(R.id.end_time_value_tv)
    TextView mEndTimeValueTv;
    @BindView(R.id.query_bt)
    Button mQueryBt;
    @BindView(R.id.date_tv)
    TextView mDateTv;
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

    public LineDataFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_linedata;
    }


    @Override
    public void init() {
        mLoadingWindow = new LoadingWindow(getActivity(), R.style.Dialog);
        setBarTextColor();
        initData();
    }

    //查询应收账款池列表信息
    public void getShouMoneyInfoLine() {
        Map<String,Object> mapKehu= ((MyShouMoneyActivity)getActivity()).getSelectKehuMap();
        if (mapKehu == null){
            ((MyShouMoneyActivity) getActivity()).showToastMsg("请选择付款方");
            mLoadingWindow.cancleView();
            return;
        }
        mRequestTag = MethodUrl.shoumoneyLine;
        Map<String, String> map = new HashMap<>();
        map.put("payfirmname",mapKehu.get("payfirmname")+""); //购买方名称
        map.put("begindate",mStartTime); //开始时间
        map.put("autoid",mEndTime);  //结束时间
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.shoumoneyLine, map);
    }



    private void initData() {

        mySelectDialog = new DateSelectDialog(getActivity(), true, "选择日期", 21);
        mySelectDialog.setSelectBackListener(this);
        mySelectDialog2 = new DateSelectDialog(getActivity(), true, "选择日期", 22);
        mySelectDialog2.setSelectBackListener(this);


        //默认查询最近一周
        String sTime = UtilTools.getWeekAgo(new Date(),-6);
        String eTime = UtilTools.getStringFromDate(new Date(),"yyyyMMdd");

        mSelectStartTime = sTime;
        mSelectEndTime = eTime;
        mStartTime = mSelectStartTime;
        mEndTime = mSelectEndTime;

        mStartTimeValueTv.setText(UtilTools.getStringFromSting2(mStartTime,"yyyyMMdd","yyyy-MM-dd"));
        mEndTimeValueTv.setText(UtilTools.getStringFromSting2(mEndTime,"yyyyMMdd","yyyy-MM-dd"));

        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            xValues.add(UtilTools.dateTypeTo(UtilTools.getWeekAgo(new Date(),-1*i)));
            yValues.add(0f);
        }
        Collections.reverse(xValues);

        mDateTv.setText(xValues.get(0)+"至"+xValues.get(xValues.size()-1)+"应收账款走势图");
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
            case  MethodUrl.shoumoneyLine:
                List<Map<String,Object>> list = (List<Map<String, Object>>) tData.get("pondinfoList");
               if (list != null && list.size()>0){
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

    private void responseData(List<Map<String,Object>> list) {
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        for (Map<String,Object> map :list){
            xValues.add(UtilTools.dateTypeTo(map.get("acctime")+""));
            yValues.add(UtilTools.fenToWanYuan(map.get("yszkcsw")+""));
        }

        mDateTv.setText(xValues.get(0)+"至"+xValues.get(xValues.size()-1)+"应收账款走势图");

        // xy轴集合自己添加数据
        mLinechaerView.setXValues(xValues);
        mLinechaerView.setYValues(yValues);

        mLinechaerView.updateUI();

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

    @OnClick({ R.id.start_time_value_tv, R.id.end_time_value_tv, R.id.query_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_time_value_tv:
                mySelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);

                break;
            case R.id.end_time_value_tv:
                mySelectDialog2.showAtLocation(Gravity.BOTTOM, 0, 0);

                break;
            case R.id.query_bt:

                String start=mStartTimeValueTv.getText().toString().trim();
                String end= mEndTimeValueTv.getText().toString().trim();

                mStartTime = UtilTools.getStringFromSting2(start,"yyyy-MM-dd","yyyyMMdd");
                mEndTime = UtilTools.getStringFromSting2(end,"yyyy-MM-dd","yyyyMMdd");

                long day = UtilTools.dateDiff(start, end, "yyyy-MM-dd");

                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
                long time = 0;
                long endTime = 0;

                Date date = new Date();
                try {
                    time = sd.parse(mStartTime).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (day < 6){
                    ((MyShouMoneyActivity)getActivity()).showToastMsg("查询时间周期不能少于7日");
                     endTime= time + 6*24*60*60*1000;
                     date.setTime(endTime);
                     mEndTime = sd.format(date);
                }

                if (day > 14){
                    ((MyShouMoneyActivity)getActivity()).showToastMsg("查询时间周期不能多于15日");
                      endTime= time + 14*24*60*60*1000;
                      date.setTime(endTime);
                      mEndTime = sd.format(date);

                }


                mStartTimeValueTv.setText(UtilTools.getStringFromSting2(mStartTime,"yyyyMMdd","yyyy-MM-dd"));
                mEndTimeValueTv.setText(UtilTools.getStringFromSting2(mEndTime,"yyyyMMdd","yyyy-MM-dd"));

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
                mStartTimeValueTv.setText(map.get("year")+"-"+map.get("month")+"-"+map.get("day"));
                mStartTime = mSelectStartTime;

                break;
            case 22:
                mSelectEndTime = map.get("date") + "";
                mEndTimeValueTv.setText(map.get("year")+"-"+map.get("month")+"-"+map.get("day"));
                mEndTime = mSelectEndTime;
                break;

        }
    }
}
