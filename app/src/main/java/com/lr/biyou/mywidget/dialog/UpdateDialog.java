package com.lr.biyou.mywidget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;

public class UpdateDialog extends BaseDialog {
    public Button tv_cancel;
    public Button tv_exit;

    private TextView mTitleTv;
    private TextView mContentTv;

    private String mTitle;
    private String mContent;


    public LinearLayout getProgressLay() {
        return mProgressLay;
    }

    public void setProgressLay(LinearLayout progressLay) {
        mProgressLay = progressLay;
    }

    public TextView getPrgText() {
        return mPrgText;
    }

    public void setPrgText(TextView prgText) {
        mPrgText = prgText;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    private LinearLayout mProgressLay;
    private TextView mPrgText;
    private ProgressBar mProgressBar;


    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private View.OnClickListener mOnClickListener;

    public UpdateDialog(Context context) {
        super(context);
    }
    public UpdateDialog(Context context, boolean isb) {
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
        View inflate = View.inflate(mContext, R.layout.update_dialog, null);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"),dp2px(10)));
        tv_cancel = inflate.findViewById( R.id.cancel);
        tv_exit = inflate.findViewById( R.id.confirm);
        mTitleTv = inflate.findViewById(R.id.tv_one);
        mContentTv = inflate.findViewById(R.id.tv_two);

        mProgressBar= inflate.findViewById(R.id.progress);
        mPrgText= inflate.findViewById(R.id.progress_text);
        mProgressLay= inflate.findViewById(R.id.progress_lay);


        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tv_cancel.setOnClickListener(mOnClickListener);
        tv_exit.setOnClickListener(mOnClickListener);

        mTitleTv.setText(mTitle);
        mContentTv.setText(mContent);


    }

    public void initValue(String title ,String content){
        mTitle = title;
        mContent = content;
    }

    public void update(String max,String size,String progress){
        mProgressBar.setMax(Integer.parseInt(max));
        mProgressBar.setProgress(Integer.parseInt(size));
        mPrgText.setText(progress);
        if (max.equals(size)){
            dismiss();
        }

    }
}