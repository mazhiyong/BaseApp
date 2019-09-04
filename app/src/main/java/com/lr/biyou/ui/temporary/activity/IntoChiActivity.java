package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.PopuTipView;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 申请入池
 */
public class IntoChiActivity extends BasicActivity implements RequestView, ReLoadingData {


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
    @BindView(R.id.total_money_tv)
    TextView mTotalMoneyTv;
    @BindView(R.id.guanlian_lay)
    CardView mGuanlianLay;
    @BindView(R.id.number_et)
    EditText mNumberEt;
    @BindView(R.id.buyer_tv)
    TextView mBuyerTv;
    @BindView(R.id.date_tv)
    TextView mDateTv;
    @BindView(R.id.money_et)
    EditText mMoneyEt;
    @BindView(R.id.pay_type_value_tv)
    TextView mPayTypeValueTv;
    @BindView(R.id.pay_type_lay)
    CardView mPayTypeLay;
    @BindView(R.id.zhouqi_value_tv)
    TextView mZhouqiValueTv;
    @BindView(R.id.zhongqi_lay)
    CardView mZhongqiLay;
    @BindView(R.id.other_zhognqi_line)
    View mOtherZhognqiLine;
    @BindView(R.id.other_zhonqi_lay)
    CardView mOtherZhonqiLay;
    @BindView(R.id.zhaiyao_et)
    EditText mZhaiyaoEt;
    @BindView(R.id.lilv_type_lay)
    CardView mLilvTypeLay;
    @BindView(R.id.chujieren_tv)
    TextView mChujierenTv;
    @BindView(R.id.chujieren_lay)
    CardView mChujierenLay;
    @BindView(R.id.same_people_list)
    LRecyclerView mSamePeopleList;
    @BindView(R.id.add_same_people_lay)
    CardView mAddSamePeopleLay;
    @BindView(R.id.has_upload_tv2)
    TextView mHasUploadTv2;
    @BindView(R.id.add_file_tv2)
    TextView mAddFileTv2;
    @BindView(R.id.file_num_tv2)
    TextView mFileNumTv2;
    @BindView(R.id.has_fujian_lay)
    CardView mHasFujianLay;
    @BindView(R.id.bulu_divide_view)
    View mBuluDivideView;
    @BindView(R.id.bulu_lay)
    LinearLayout mBuluLay;
    @BindView(R.id.add_fujian_title)
    TextView mAddFujianTitle;
    @BindView(R.id.has_upload_tv)
    TextView mHasUploadTv;
    @BindView(R.id.add_file_tv)
    TextView mAddFileTv;
    @BindView(R.id.file_num_tv)
    TextView mFileNumTv;
    @BindView(R.id.fujian_lay)
    CardView mFujianLay;
    @BindView(R.id.cb_xieyi)
    CheckBox mCbXieyi;
    @BindView(R.id.xieyi_tv)
    TextView mXieyiTv;
    @BindView(R.id.xieyi_lay)
    LinearLayout mXieyiLay;
    @BindView(R.id.but_submit)
    Button mButSubmit;
    @BindView(R.id.scrollView_content)
    NestedScrollView mScrollViewContent;
    @BindView(R.id.tip_wu_view)
    ImageView mTipWuView;
    @BindView(R.id.tip_ri_view)
    ImageView mTipRiView;



    @Override
    public int getContentView() {
        return R.layout.activity_into_chi;
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

    @OnClick({R.id.left_back_lay, R.id.guanlian_lay, R.id.zhongqi_lay, R.id.but_submit,R.id.tip_wu_view, R.id.tip_ri_view,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.guanlian_lay:
                break;
            case R.id.zhongqi_lay:
                break;
            case R.id.but_submit:
                startActivity(new Intent(IntoChiActivity.this,IntoChiFinishActivity.class));
                break;
            case R.id.tip_wu_view:

                String s ="付款截止日=当月的发票日期+" + "\n" +
                        "结算周期（按月计算，每30天" + "\n" +
                        "算一个月）后的当月最后一天";

                PopuTipView mp= new PopuTipView(IntoChiActivity.this,s,R.layout.popu_lay_top);
                mp.show(mTipWuView,1);


                /*View inflate = View.inflate(IntoChiActivity.this, R.layout.popup_bubble_text, null);
                TextView mTextView = inflate.findViewById(R.id.tv_bubble);
                mTextView.setText("付款截止日=当月的发票日期+" + "\n" +
                        "结算周期（按月计算，每30天" + "\n" +
                        "算一个月）后的当月最后一天" );

                new BubblePopup(IntoChiActivity.this, inflate)
                        .anchorView(mTipWuView)
                        .bubbleColor(Color.parseColor("#A6000000"))
                        .gravity(Gravity.TOP)
                        .triangleWidth(5)
                        .triangleHeight(5)
                        .showAnim(null)
                        .dismissAnim(null)
                        .show();*/
                break;
            case R.id.tip_ri_view:


                String s1 ="1、若开票日期在当月结算日之前"+"\n"+
                        "（不含结算日），则付款截止日=当"+ "\n" +
                        "月结算日+结算周期（按月计算）"+ "\n" +
                        "2、若开票日期在当月结算日之后"+ "\n" +
                        "（含结算日），则付款截止日=下一"+ "\n" +
                        "月结算日+结算周期（按月计算）";

                PopuTipView mp1= new PopuTipView(IntoChiActivity.this,s1,R.layout.popu_lay_top);
                mp1.show(mTipRiView,2);

               /* View inflate2 = View.inflate(IntoChiActivity.this, R.layout.popup_bubble_text, null);
                TextView mTextView2 = inflate2.findViewById(R.id.tv_bubble);
                mTextView2.setText("1、若开票日期在当月结算日之前"+"\n"+
                        "（不含结算日），则付款截止日=当"+ "\n" +
                        "月结算日+结算周期（按月计算）"+ "\n" +
                        "2、若开票日期在当月结算日之后"+ "\n" +
                        "（含结算日），则付款截止日=下一"+ "\n" +
                        "月结算日+结算周期（按月计算）");


                new BubblePopup(IntoChiActivity.this, inflate2)
                        .anchorView(mTipRiView)
                        .bubbleColor(Color.parseColor("#A6000000"))
                        .gravity(Gravity.TOP)
                        .triangleWidth(5)
                        .triangleHeight(5)
                        .showAnim(null)
                        .dismissAnim(null)
                        .show();*/
                break;
        }
    }
}
