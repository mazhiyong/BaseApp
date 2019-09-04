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
import com.lr.biyou.ui.temporary.adapter.AddressWheelAdapter;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mywidget.view.TipsToast;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressSelectDialog2 extends BaseDialog {


    private WheelView mProvince;
    private WheelView mCity;
    private WheelView mArea;



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

    private List<Map<String,Object>> mList = new ArrayList<>();

    public SelectBackListener getSelectBackListener() {
        return mSelectBackListener;
    }

    public void setSelectBackListener(SelectBackListener selectBackListener) {
        mSelectBackListener = selectBackListener;
    }



    public AddressSelectDialog2(Context context) {
        super(context);
        this.context = context;
    }
    public AddressSelectDialog2(Context context, boolean isStyle, String title, int  type) {
        super(context,isStyle);
        this.context = context;
        mTitleStr = title;
        mType = type;

       String ss = SelectDataUtil.getJson(context,"china_city_data2.json");
       mList =   JSONUtil.getInstance().jsonToList(ss);

    }

    @Override
    public View onCreateView() {

        View view = View.inflate(context, R.layout.dialog_address_select, null);
        mProvince = view.findViewById(R.id.one_wheelview);
        mCity = view.findViewById(R.id.two_wheelview);
        mArea = view.findViewById(R.id.three_wheelview);

        mSureTv = view.findViewById(R.id.sure_tv);
        mTitleTv = view.findViewById(R.id.title_tv);
        mCancleTv = view.findViewById(R.id.cancel_tv);

        mProvince.setWheelAdapter(new AddressWheelAdapter(context));
        mProvince.setSkin(WheelView.Skin.Holo);
        mProvince.setWheelData(mList);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        //style.holoBorderColor = Color.RED;
        style.textColor = Color.GRAY;
        mProvince.setStyle(style);
        mProvince.setWheelSize(5);

        mCity.setWheelAdapter(new AddressWheelAdapter(context));
        mCity.setSkin(WheelView.Skin.Holo);
        mCity.setWheelData(getCityData(0));
        mCity.setStyle(style);
        mCity.setWheelSize(5);

        mArea.setVisibility(View.GONE);

      /*  mArea.setWheelAdapter(new MyWheelAdapter(context));
        mArea.setSkin(WheelView.Skin.Holo);
        mArea.setWheelData(getAreaData(0,0));
        mArea.setStyle(style);
        mArea.setWheelSize(5);*/

        return  view;
    }

    @Override
    public void setUiBeforShow() {
        mTitleTv.setText(mTitleStr);
        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectBackListener != null){

                    if (getSelectAddress()){
                        mSelectBackListener.onSelectBackListener(resultMap,mType);
                        dismiss();

                    }else {
                        TipsToast.showToastMsg("我还在转，不要慌");
                    }
                }
            }
        });

        mCancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mProvince.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mCity.setWheelData(getCityData(position));

                // int mCityPosition = mCity.getCurrentPosition();
               // mArea.setWheelData(getAreaData(position,mCityPosition));
            }
        });
        mCity.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                int mCityPosition = mProvince.getCurrentPosition();
                //mArea.setWheelData(getAreaData(mCityPosition,position));
            }
        });
        mArea.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
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

    private Map<String,Object> resultMap = new HashMap<>();


    private boolean getSelectAddress(){
        Map<String,Object> mProMap = (Map<String, Object>) mProvince.getSelectionItem();
        Map<String,Object> mCityMap = (Map<String, Object>) mCity.getSelectionItem();

        List<Map<String,Object>> mCityList = (List<Map<String,Object>>)mProMap.get("children");
        if (mCityList.contains(mCityMap)){
            resultMap = new HashMap<>();
            resultMap.put("proname",mProMap.get("label")+"");
            resultMap.put("procode",mProMap.get("value")+"");
            resultMap.put("cityname",mCityMap.get("label")+"");
            resultMap.put("citycode",mCityMap.get("value")+"");
            resultMap.put("name",mProMap.get("label")+""+mCityMap.get("label")+"");
            return  true;
        }else {
            return false;
        }
    }



    private List<Map<String,Object>> getCityData(int position){
        Map<String,Object> mCityMap = (Map<String,Object>)mList.get(position);
        List<Map<String,Object>> mCityList = (List<Map<String,Object>>) mCityMap.get("children");

        return mCityList;
    }
   /* private List<Map<String,Object>> getAreaData(int i,int position){
        Map<String,Object> mCityMap = (Map<String,Object>)mList.get(i);
        List<Map<String,Object>> mCityList = (List<Map<String,Object>>) mCityMap.get("cityList");

        Map<String,Object> mAreaMap = (Map<String,Object>)mCityList.get(position);
        List<Map<String,Object>> mAreaList = (List<Map<String,Object>>) mAreaMap.get("cityList");

        return mAreaList;
    }*/

}
