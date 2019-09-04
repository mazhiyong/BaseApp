package com.lr.biyou.ui.temporary.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 应收账款详情界面
 */
public class ShouMoneyItemActivity extends BasicActivity implements RequestView, ReLoadingData {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.divide_line)
    View mDivideLine;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.pz_number_tv)
    TextView mPzNumberTv;
    @BindView(R.id.pz_money_tv)
    TextView mPzMoneyTv;
    @BindView(R.id.fukuan_fang_tv)
    TextView mFukuanFangTv;
    @BindView(R.id.shoukuan_fang_tv)
    TextView mShoukuanFangTv;
    @BindView(R.id.date_tv)
    TextView mDateTv;
    @BindView(R.id.zhaiyao_tv)
    TextView mZhaiyaoTv;
    @BindView(R.id.tv_money)
    TextView mMoenyTv;

    @Override
    public int getContentView() {
        return R.layout.activity_shou_money_item;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText("应收账款详情");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
           Map<String,Object> map= (Map<String, Object>) bundle.getSerializable("DATA");
           mPzNumberTv.setText(map.get("billid")+"");
           mMoenyTv.setText(UtilTools.getRMBMoney(map.get("vouchmny")+""));
           mPzMoneyTv.setText(UtilTools.getRMBMoney(map.get("vouchmny")+""));
           mFukuanFangTv.setText(map.get("payfirmname")+"");
           mShoukuanFangTv.setText("暂无");
           mDateTv.setText(map.get("paydate")+"");
           mZhaiyaoTv.setText("暂无");
        }




    }

    @Override
    public void reLoadingData() {

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
    @OnClick(R.id.left_back_lay)
    public void onViewClicked() {
        finish();
    }
}
