package com.lr.biyou.mywidget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;
import com.lr.biyou.mywidget.view.CustomerKeyboard;
import com.lr.biyou.mywidget.view.PasswordEditText;

/**
 *  交易密码弹框
 */
public class TradePassDialog extends BaseDialog implements CustomerKeyboard.CustomerKeyboardClickListener, PasswordEditText.PasswordFullListener{


    private Context mContext;

    public PasswordEditText mPasswordEditText;
    public CustomerKeyboard mCustomerKeyboard;
    public TextView mForgetPassTv;

    public PassFullListener getPassFullListener() {
        return mPassFullListener;
    }

    public void setPassFullListener(PassFullListener passFullListener) {
        mPassFullListener = passFullListener;
    }

    private PassFullListener mPassFullListener;


    public TradePassDialog(Context context) {
        super(context);
        mContext=context;
    }

    public TradePassDialog(Context context, boolean isPopupStyle) {
        super(context, isPopupStyle);
        mContext =context;
    }

    @Override
    public View onCreateView() {
        View view=View.inflate(mContext, R.layout.dialog_trade_pass_layout,null);

        mPasswordEditText = view.findViewById(R.id.password_edit_text);
        mCustomerKeyboard = view.findViewById(R.id.custom_key_board);
        mForgetPassTv = view.findViewById(R.id.forget_trade_pass);

        mCustomerKeyboard.setOnCustomerKeyboardClickListener(this);
        mPasswordEditText.setEnabled(false);
        mPasswordEditText.setOnPasswordFullListener(this);
        init();
        return view;
    }

    @SuppressLint("WrongConstant")
    private void init() {

    }

    @Override
    public void setUiBeforShow() {

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
                .dismissAnim(bas_out);
    }

    @Override
    public void click(String number) {
        mPasswordEditText.addPassword(number);
    }

    @Override
    public void delete() {
        mPasswordEditText.deleteLastPassword();
    }

    @Override
    public void passwordFull(String password) {
        // Toast.makeText(this,"密码输入完毕->"+password,Toast.LENGTH_LONG).show();
        if (mPassFullListener != null){
            mPassFullListener.onPassFullListener(password);
        }
    }

    public interface PassFullListener{
        void onPassFullListener(String pass);
    }

}
