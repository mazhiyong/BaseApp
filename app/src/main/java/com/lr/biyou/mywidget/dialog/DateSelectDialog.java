package com.lr.biyou.mywidget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.utils.tool.UtilTools;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateSelectDialog extends BaseDialog {


    private int mCurrentYear ;

    private WheelView mYear ;
    private WheelView mMonth ;
    private WheelView mDay ;

    private TextView mCancleTv;
    private TextView mTitleTv;
    private TextView mSureTv;

    private Context context;

    private String mTitleStr = "";
    private String mSelectTime = "";

    private String mYearStr = "";
    private String mMonthStr = "";
    private String mDayStr = "";

    private int mType = 0;
    private SelectBackListener mSelectBackListener;

    public SelectBackListener getSelectBackListener() {
        return mSelectBackListener;
    }

    public void setSelectBackListener(SelectBackListener selectBackListener) {
        mSelectBackListener = selectBackListener;
    }



    public DateSelectDialog(Context context) {
        super(context);
        this.context = context;
    }
    //type  2000  2001（从业工作时间） 已经被占用，其他界面请选择其他数字
    public DateSelectDialog(Context context, boolean isStyle, String title, int  type) {
        super(context,isStyle);
        this.context = context;
        mTitleStr = title;
        mType = type;
    }


    @Override
    public View onCreateView() {

        Calendar calendar = Calendar.getInstance();
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) ;//如果需要用的话需要  calendar.get(Calendar.MONTH)+1
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);

        View view = View.inflate(context, R.layout.dialog_time_select, null);
        mYear = view.findViewById(R.id.one_wheelview);
        mMonth = view.findViewById(R.id.two_wheelview);
        mDay = view.findViewById(R.id.three_wheelview);

        mSureTv = view.findViewById(R.id.sure_tv);
        mTitleTv = view.findViewById(R.id.title_tv);
        mCancleTv = view.findViewById(R.id.cancel_tv);


        List<String> mYearsList = createYear();

        mYear.setWheelAdapter(new ArrayWheelAdapter(context));
        mYear.setSkin(WheelView.Skin.Holo);
        mYear.setWheelData(createYear());
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;
        mYear.setStyle(style);

        int index = mYear.getWheelCount()-1;
        for (int i = 0;i<mYearsList.size();i++){
            String ss = mYearsList.get(i);
            if (ss.equals(mCurrentYear+"")){
                index = i;
                break;
            }
        }
        mYear.setWheelSize(5);
        mYear.setSelection(index);
        mYear.setExtraText("年", Color.parseColor("#0288ce"), UtilTools.sp2px(mContext,15),UtilTools.dip2px(mContext,35));

        mMonth.setWheelAdapter(new ArrayWheelAdapter(context));
        mMonth.setSkin(WheelView.Skin.Holo);
        mMonth.setWheelData(createMonth());
        mMonth.setStyle(style);
        mMonth.setWheelSize(5);
        mMonth.setSelection(month);
        mMonth.setExtraText("月", Color.parseColor("#0288ce"), UtilTools.sp2px(mContext,15),UtilTools.dip2px(mContext,20));

        mDay.setWheelAdapter(new ArrayWheelAdapter(context));
        mDay.setSkin(WheelView.Skin.Holo);
        mDay.setWheelData(createDay(2016, 5));
        mDay.setStyle(style);
        mDay.setWheelSize(5);
        mDay.setSelection(day-1);
        mDay.setExtraText("日", Color.parseColor("#0288ce"), UtilTools.sp2px(mContext,15),UtilTools.dip2px(mContext,20));


        return view;
    }

    @Override
    public void setUiBeforShow() {
        mTitleTv.setText(mTitleStr);
        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectBackListener != null) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("year", mYearStr);
                    map.put("month", mMonthStr);
                    map.put("day", mDayStr);
                    map.put("date", mSelectTime);
                    mSelectBackListener.onSelectBackListener(map, mType);
                }
                dismiss();
            }
        });

        mCancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mYear.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                String ss = mMonth.getSelectionItem() + "";
                mDay.setWheelData(createDay(Integer.valueOf(o + ""), Integer.valueOf(ss)));
                getSelectTime();
            }
        });
        mMonth.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                String ss = mYear.getSelectionItem() + "";
                mDay.setWheelData(createDay(Integer.valueOf(ss), Integer.valueOf(o + "")));
                getSelectTime();
            }
        });
        mDay.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                getSelectTime();
            }
        });


        BaseAnimatorSet bas_in;
        BaseAnimatorSet bas_out;
        bas_in = new SlideBottomEnter();
        bas_in.duration(200);
        bas_out = new SlideBottomExit();
        bas_out.duration(300);

        widthScale(1f);
        dimEnabled(true);
        // baseDialog.heightScale(1f);
        showAnim(bas_in)
                .dismissAnim(bas_out);//
    }


    private void getSelectTime() {
        String ss1 = mYear.getSelectionItem() + "";
        String ss2 = mMonth.getSelectionItem() + "";
        String ss3 = mDay.getSelectionItem() + "";
        mYearStr = ss1;
        mMonthStr = ss2;
        mDayStr = ss3;
        mSelectTime = ss1 + ss2 + ss3;
    }


    private ArrayList<String> createYear() {
        ArrayList<String> list = new ArrayList<String>();

        Calendar c = Calendar.getInstance();
        int nowYear = c.get(Calendar.YEAR) ;
        mCurrentYear = nowYear;
        int startYear = nowYear - 2;
        if (mType == 2000){
            startYear = nowYear - 40;
        }else if (mType == 2001){
            startYear = nowYear - 20;
        }

        int nextYear = nowYear + 2;
        for (int i = startYear; i <= nextYear; i++) {
            list.add("" + i);
        }
        return list;
    }

    private ArrayList<String> createMonth() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> createDay(int mYear, int mMonth) {

        int day = getDay(mYear, mMonth);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= day; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }


    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

}
