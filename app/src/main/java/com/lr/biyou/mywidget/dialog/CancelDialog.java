package com.lr.biyou.mywidget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;


/**
  取消 弹框
 */
public class CancelDialog extends BaseDialog {

    private TextView tvSure;
    private TextView tvCancel;
    private TextView tvTitle;
    private TextView tvMessageBlack;
    private TextView tvMessageRed;

    private String mMessageBlack;
    private String mMessageRed;
    private String mTitle;
    private String mCancel;
    private String mSure;

    private View.OnClickListener mClickListener;

    public CancelDialog(Context context,  String mTitle,String mMessageBlack, String mMessageRed, String mCancel, String mSure) {
        super(context);
        this.mMessageBlack = mMessageBlack;
        this.mMessageRed = mMessageRed;
        this.mTitle = mTitle;
        this.mCancel = mCancel;
        this.mSure = mSure;
    }

    public View.OnClickListener getClickListener() {
        return mClickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }



    @Override
    public View onCreateView() {
        BaseAnimatorSet bas_in;
        BaseAnimatorSet bas_out;
        bas_in=new BounceEnter();
        bas_out=new ZoomOutExit();
        showAnim(bas_in);
        dismissAnim(bas_out);
        widthScale(0.8f);

        View view=View.inflate(mContext, R.layout.cancel_dialog,null);
        view.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"),dp2px(10)));

        tvTitle=view.findViewById(R.id.title_tv);
        tvMessageBlack=view.findViewById(R.id.messge_black_tv);
        tvMessageRed=view.findViewById(R.id.messge_red_tv);
        tvCancel=view.findViewById(R.id.cancel_tv);
        tvSure=view.findViewById(R.id.sure_tv);

        return view;
    }

    @Override
    public void setUiBeforShow() {

        tvCancel.setText(mCancel);
        tvSure.setText(mSure);
        tvTitle.setText(mTitle);
        tvMessageRed.setText(mMessageRed);
        tvMessageBlack.setText(mMessageBlack);

        tvCancel.setOnClickListener(mClickListener);
        tvSure.setOnClickListener(mClickListener);

    }
}
