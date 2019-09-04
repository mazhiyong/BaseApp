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

public class SureOrNoDialog extends BaseDialog {
    public Button tv_cancel;
    public Button tv_sure;

    public TextView mTitleTv;
    public TextView mContentTv;

    private String mTitle;
    private String mContent;



    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private View.OnClickListener mOnClickListener;

    public SureOrNoDialog(Context context) {
        super(context);
    }
    public SureOrNoDialog(Context context, boolean isb) {
        super(context,isb);
    }
    @Override
    public View onCreateView() {
        BaseAnimatorSet bas_in;
        BaseAnimatorSet bas_out;
        bas_in = new BounceEnter();
        bas_out = new ZoomOutExit();
        showAnim(bas_in);
        dismissAnim(bas_out);//
        widthScale(0.85f);
        View inflate = View.inflate(mContext, R.layout.sure_dialog, null);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"),dp2px(10)));
        tv_cancel = inflate.findViewById( R.id.cancel);
        tv_sure = inflate.findViewById( R.id.confirm);
        mTitleTv = inflate.findViewById(R.id.tv_one);
        mContentTv = inflate.findViewById(R.id.tv_two);


        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tv_cancel.setOnClickListener(mOnClickListener);
        tv_sure.setOnClickListener(mOnClickListener);
        mTitleTv.setText(mTitle);
        mContentTv.setText(mContent);


    }

    public void initValue(String title ,String content){
        mTitle = title;
        mContent = content;
    }
}