package com.lr.biyou.mywidget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;

import java.util.Map;

public class ZZTipMsgDialog extends BaseDialog {
    public Button tv_cancel;
    public Button tv_sure;
    public TextView mNameTv;
    public TextView mAccountTv;
    public TextView mBankTv;
    public TextView mBankNumTv;
    public Map<String,Object> mMap;

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;

    }

    private View.OnClickListener mOnClickListener;

    public ZZTipMsgDialog(Context context) {
        super(context);
    }
    public ZZTipMsgDialog(Context context, boolean isb) {
        super(context,isb);
    }
    @Override
    public View onCreateView() {
        BaseAnimatorSet bas_in;
        BaseAnimatorSet bas_out;
        bas_in = new BounceEnter();
        bas_out = new ZoomOutExit();
        showAnim(bas_in);
        //dismissAnim(bas_out);//
        widthScale(0.85f);
        View inflate = View.inflate(mContext, R.layout.dialog_zz_msg_tip, null);
        inflate.setBackgroundDrawable(  CornerUtils.cornerDrawable(Color.parseColor("#ffffff"),dp2px(10)));
        tv_cancel = inflate.findViewById( R.id.cancel);
        tv_sure = inflate.findViewById( R.id.sure);
        mNameTv = inflate.findViewById( R.id.tv_name);
        mAccountTv = inflate.findViewById(R.id.tv_account);
        mBankTv = inflate.findViewById(R.id.tv_bank);
        mBankNumTv = inflate.findViewById(R.id.tv_bank_num);


        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        //mNameTv.setText(mMap.get("orgname")+"");
        mAccountTv.setText(mMap.get("accid")+"");
        mBankTv.setText(mMap.get("opnbnkwdnm")+"");
        mBankNumTv.setText("302100011552");
        tv_cancel.setOnClickListener(mOnClickListener);
        tv_sure.setOnClickListener(mOnClickListener);


    }


    public void initValue(Map<String,Object>  map){
        mMap = map;
    }
}