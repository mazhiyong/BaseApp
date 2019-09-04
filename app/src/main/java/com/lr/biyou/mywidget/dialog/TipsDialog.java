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


/**
 滑动删除/注销   等操作提示框
 */
public class TipsDialog extends BaseDialog {

    private Button btSure;
    private Button btCancel;

    private TextView tvMessage;

    private String mMessage;

    private View.OnClickListener mClickListener;

    public TipsDialog(Context context, String message) {
        super(context);
        mMessage=message;
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

        View view=View.inflate(mContext, R.layout.tips_dialog,null);
        view.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"),dp2px(10)));

        btCancel=view.findViewById(R.id.bt_cancel);
        btSure=view.findViewById(R.id.bt_sure);
        tvMessage=view.findViewById(R.id.tv_message);

        return view;
    }

    @Override
    public void setUiBeforShow() {
        btCancel.setOnClickListener(mClickListener);
        btSure.setOnClickListener(mClickListener);
        tvMessage.setText(mMessage);
    }
}
