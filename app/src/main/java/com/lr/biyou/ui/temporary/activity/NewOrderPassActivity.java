package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.view.CustomerKeyboard;
import com.lr.biyou.mywidget.view.PasswordEditText;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更换手机号  界面
 */
public class NewOrderPassActivity extends BasicActivity implements CustomerKeyboard.CustomerKeyboardClickListener, PasswordEditText.PasswordFullListener{

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.pass_tip)
    TextView mPassTip;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.password_edit_text)
    PasswordEditText mPasswordEditText;
    @BindView(R.id.custom_key_board)
    CustomerKeyboard mCustomKeyBoard;

    private String mNewPass="";

    private String mOnePass="";
    private int mType= 0;

    @Override
    public int getContentView() {
        return R.layout.activity_new_order_pass;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mType = bundle.getInt("type");
            if (mType == 2){
                mOnePass = bundle.getString("newPass");
                mPassTip.setText(getResources().getString(R.string.order_sure_pass));
                mButNext.setText(getResources().getString(R.string.but_finish));
            }
        }

        mTitleText.setText(getResources().getString(R.string.modify_order_pass_title));
        mCustomKeyBoard.setOnCustomerKeyboardClickListener(this);
        mPasswordEditText.setEnabled(false);
        mPasswordEditText.setOnPasswordFullListener(this);
        mButNext.setEnabled(false);
    }

    @OnClick({R.id.back_img, R.id.but_next,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
                if (mType == 2){
                    if (mNewPass.equals(mOnePass)){
                        showToastMsg("两次输入密码相同");
                    }else {
                        showToastMsg("两次输入密码不同");
                    }
                }else {
                    intent = new Intent(NewOrderPassActivity.this, NewOrderPassActivity.class);
                    intent.putExtra("newPass",mNewPass);
                    intent.putExtra("type",2);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void click(String number) {
        mPasswordEditText.addPassword(number);
    }

    @Override
    public void delete() {
        mPasswordEditText.deleteLastPassword();
        mButNext.setEnabled(false);
    }

    @Override
    public void passwordFull(String password) {
        //Toast.makeText(this,"密码输入完毕->"+password,Toast.LENGTH_LONG).show();
        mButNext.setEnabled(true);
        mNewPass = password;
    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {

    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {

    }

    /**
     * @param tData 数据类型
     * @param mType
     * @descriptoin 请求数据成功
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
