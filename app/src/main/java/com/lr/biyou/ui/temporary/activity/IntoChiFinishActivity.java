package com.lr.biyou.ui.temporary.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class IntoChiFinishActivity extends BasicActivity implements RequestView, ReLoadingData {

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
    @BindView(R.id.my_image)
    ImageView mMyImage;
    @BindView(R.id.submit_result_tv)
    TextView mSubmitResultTv;
    @BindView(R.id.submit_tip_tv)
    TextView mSubmitTipTv;
    @BindView(R.id.title_tv1)
    TextView mTitleTv1;
    @BindView(R.id.value_tv1)
    TextView mValueTv1;
    @BindView(R.id.title_lay1)
    LinearLayout mTitleLay1;
    @BindView(R.id.title_tv2)
    TextView mTitleTv2;
    @BindView(R.id.value_tv2)
    TextView mValueTv2;
    @BindView(R.id.title_lay2)
    LinearLayout mTitleLay2;
    @BindView(R.id.line2)
    View mLine2;
    @BindView(R.id.title_tv3)
    TextView mTitleTv3;
    @BindView(R.id.value_tv3)
    TextView mValueTv3;
    @BindView(R.id.title_lay3)
    LinearLayout mTitleLay3;
    @BindView(R.id.line3)
    View mLine3;
    @BindView(R.id.but_back)
    Button mButBack;

    @Override
    public int getContentView() {
        return R.layout.activity_into_chi_finish;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText("申请入池");
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

    @OnClick({R.id.left_back_lay, R.id.but_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_back:
                finish();
                break;
        }
    }
}
