package com.lr.biyou.ui.moudle5.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加提币 地址
 */
public class AddAddressActivity extends BasicActivity implements RequestView {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.tv)
    TextView mTextView;

    Map<String, Object> mapData = new HashMap<>();
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.aviable_money_tv)
    TextView aviableMoneyTv;
    @BindView(R.id.address_et)
    EditText addressEt;
    @BindView(R.id.scan_iv)
    ImageView scanIv;
    @BindView(R.id.address_iv)
    ImageView addressIv;
    @BindView(R.id.number_et)
    EditText numberEt;
    @BindView(R.id.selectall_tv)
    TextView selectallTv;
    @BindView(R.id.middle_money_tv)
    TextView middleMoneyTv;
    @BindView(R.id.tibi_tv)
    TextView tibiTv;


    @Override
    public int getContentView() {
        return R.layout.activity_add_address;
    }


    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mapData = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        mTitleText.setText("添加地址");
        mTitleText.setCompoundDrawables(null, null, null, null);
        rightImg.setVisibility(View.GONE);
        rightImg.setImageResource(R.drawable.icon6_dingdan);
        mTextView.setText("温馨提示：\n" +
                "最小提币数量 10 USDT");
    }


    @OnClick({R.id.back_img, R.id.right_lay,R.id.address_iv,R.id.tibi_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.right_lay: //充提记录
                intent = new Intent(AddAddressActivity.this, TradeListActivity.class);
                startActivity(intent);
                break;

            case R.id.address_iv ://地址
                intent = new Intent(AddAddressActivity.this, TibiAddressActivity.class);
                startActivity(intent);
                break;

            case R.id.tibi_tv: //确定
                finish();
                break;

        }
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {


    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }


    /**---------------------------------------------------------------------以下代码申请权限---------------------------------------------
     * Request permissions.
     */


    /**
     * activity销毁前触发的方法
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * activity恢复时触发的方法
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
