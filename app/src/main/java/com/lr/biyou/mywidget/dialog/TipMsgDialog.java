package com.lr.biyou.mywidget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;

public class TipMsgDialog extends BaseDialog {
    public Button tv_cancel;
    public Button tv_exit;
    public ImageView tv_right;
    public TextView mTitleTv;
    public TextView mContentTv;
    public String mTitle;
    public Object mContent;

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;

    }

    private View.OnClickListener mOnClickListener;

    public TipMsgDialog(Context context) {
        super(context);
    }
    public TipMsgDialog(Context context, boolean isb) {
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
        View inflate = View.inflate(mContext, R.layout.dialog_msg_tip, null);
        inflate.setBackgroundDrawable(  CornerUtils.cornerDrawable(Color.parseColor("#ffffff"),dp2px(10)));
        tv_cancel = inflate.findViewById( R.id.cancel);
        tv_exit = inflate.findViewById( R.id.confirm);
        tv_right = inflate.findViewById( R.id.tv_right);
        mTitleTv = inflate.findViewById(R.id.tv_one);
        mContentTv = inflate.findViewById(R.id.tv_two);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mTitleTv.setText(mTitle);
        if (mContent instanceof SpannableString){
            mContentTv.setText((SpannableString)mContent);
            mContentTv.setMovementMethod(LinkMovementMethod.getInstance());
        }else if (mContent instanceof String){
            mContentTv.setText(mContent+"");
        }else {
            mContentTv.setText(mContent+"");
        }
        tv_cancel.setOnClickListener(mOnClickListener);
        tv_exit.setOnClickListener(mOnClickListener);
        tv_right.setOnClickListener(mOnClickListener);
    }


    public void initValue(String title ,Object content){
        mTitle = title;
        mContent = content;
    }
}