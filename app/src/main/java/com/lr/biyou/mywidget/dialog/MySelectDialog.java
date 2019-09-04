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
import com.lr.biyou.ui.moudle.adapter.MyWheelAdapter;
import com.lr.biyou.listener.SelectBackListener;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySelectDialog extends BaseDialog {


    private WheelView mWheelView1;
    private WheelView mWheelView2;
    private WheelView mWheelView3;

    private TextView mCancleTv;
    private TextView mTitleTv;
    private TextView mSureTv;

    private Context context;

    private String mTitleStr = "";

    private int mType = 0;
    private SelectBackListener mSelectBackListener;

    public SelectBackListener getSelectBackListener() {
        return mSelectBackListener;
    }

    public void setSelectBackListener(SelectBackListener selectBackListener) {
        mSelectBackListener = selectBackListener;
    }


    private List<Map<String,Object>> mList = new ArrayList<>();

    public MySelectDialog(Context context) {
        super(context);
        this.context = context;
    }
    public MySelectDialog(Context context, boolean isStyle, List<Map<String,Object>> list,String title,int  type) {
        super(context,isStyle);
        this.context = context;
        mList = list;
        mTitleStr = title;
        mType = type;
    }

    @Override
    public View onCreateView() {

        View view = View.inflate(context, R.layout.dialog_select_layout, null);
        mWheelView1 = view.findViewById(R.id.one_wheelview);
           /* mWheelView2 = view.findViewById(R.id.two_wheelview);
            mWheelView3 = view.findViewById(R.id.three_wheelview);*/

        mSureTv = view.findViewById(R.id.sure_tv);
        mTitleTv = view.findViewById(R.id.title_tv);
        mCancleTv = view.findViewById(R.id.cancel_tv);

        mWheelView1.setWheelAdapter(new MyWheelAdapter(context));
        mWheelView1.setWheelSize(5);
        mWheelView1.setSkin(WheelView.Skin.Holo);

        try {
            mWheelView1.setWheelData(mList);
        }catch (Exception e){

        }
        //mWheelView1.setSelection(2);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        //style.holoBorderColor = Color.RED;
        style.textColor = Color.GRAY;
        // style.selectedTextSize = 20;
        mWheelView1.setStyle(style);

        return  view;
    }


    @Override
    public void setUiBeforShow() {
        mTitleTv.setText(mTitleStr);
        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = (Map<String, Object>) mWheelView1.getSelectionItem();
                if (mSelectBackListener != null){
                    mSelectBackListener.onSelectBackListener(map,mType);
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

        mWheelView1.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                Map<String,Object> map = (Map<String, Object>) o;
            }
        });

        BaseAnimatorSet bas_in;
        BaseAnimatorSet bas_out;
        bas_in = new SlideBottomEnter();
        bas_in.duration(400);
        bas_out = new SlideBottomExit();
        bas_out.duration(200);

        widthScale(1f);
        dimEnabled(true);
        // baseDialog.heightScale(1f);
        showAnim(bas_in)
                .dismissAnim(bas_out);//
    }
}
