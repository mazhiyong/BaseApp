package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.view.CustomerKeyboard;
import com.lr.biyou.mywidget.view.PasswordEditText;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  提现（输入密码）
 */
public class TiXianTestActivity extends BasicActivity implements CustomerKeyboard.CustomerKeyboardClickListener,PasswordEditText.PasswordFullListener{

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.password_edit_text)
    PasswordEditText mPasswordEditText;
    @BindView(R.id.custom_key_board)
    CustomerKeyboard mKeyboard;
    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.font_c), 60);
        mTitleText.setText(getResources().getString(R.string.tixian));
        mKeyboard.setOnCustomerKeyboardClickListener(this);
        mKeyboard.setEnabled(false);
        mPasswordEditText.setOnPasswordFullListener(this);
        mPasswordEditText.setEnabled(false);

    }
    @OnClick({R.id.back_img,R.id.left_back_lay})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_ti_xian_test;
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
        Intent intent=new Intent(this,StateFeedbackActivity.class);
        startActivity(intent);
        finish();
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
