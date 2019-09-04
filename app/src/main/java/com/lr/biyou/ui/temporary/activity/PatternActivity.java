package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SPUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 开始手势登录 界面
 */
public class PatternActivity extends BasicActivity {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.switch_shoushi_login)
    Switch mSwitchLogin;
    @BindView(R.id.switch_shoushi_show)
    Switch mSwitchShow;
    @BindView(R.id.iv_shoushi_update)
    ImageView iv_Shoushi;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.ll_shoushi_update)
    LinearLayout mLlShoushiUpdate;
    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.shoushi_login));
    }

    @Override
    public int getContentView() {
        return R.layout.activity_pattern;
    }

    @OnClick({R.id.back_img, R.id.switch_shoushi_login, R.id.switch_shoushi_show, R.id.iv_shoushi_update,
            R.id.ll_shoushi_update,R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.switch_shoushi_login:
                if (mSwitchLogin.isChecked()) {
                    ll.setVisibility(View.VISIBLE);
                } else {
                    ll.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.switch_shoushi_show:
                break;
            case R.id.ll_shoushi_update:
                if (mSwitchShow.isChecked()) {
                    SPUtils.put(PatternActivity.this, MbsConstans.SharedInfoConstans.SHOW_SHOUSHI, "ture");
                } else {
                    SPUtils.put(PatternActivity.this, MbsConstans.SharedInfoConstans.SHOW_SHOUSHI, "false");
                }
                Intent intent = new Intent(this, DrawPatternActivity.class);
                startActivity(intent);
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
