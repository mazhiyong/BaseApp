package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle4.activity.PayMoneyActivity;
import com.lr.biyou.utils.tool.DataHolder;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.ParseTextUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 借款记录详情   界面
 */
public class BorrowDetailActivity extends BasicActivity implements RequestView, ReLoadingData {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.borrow_info_lay)
    CardView mBorrowInfoLay;
    @BindView(R.id.borrow_file_lay)
    CardView mBorrowFileLay;
    @BindView(R.id.borrow_hetong_lay)
    CardView mBorrowHetongLay;
    @BindView(R.id.borrow_pay_lay)
    CardView mBorrowPayLay;
    @BindView(R.id.borrow_repayplan_lay)
    CardView mBorrowRepayplanLay;
    @BindView(R.id.borrow_repayshistory_lay)
    CardView mBorrowRepayshistoryLay;
    @BindView(R.id.borrow_card_lay)
    CardView mBorrowCardLay;
    @BindView(R.id.money_tv)
    TextView mLeftMoneyTv;
    @BindView(R.id.jiekuan_zonge_tv)
    TextView mZongMoneyTv;
    @BindView(R.id.tiqian_huankuan_tv)
    TextView mTqHuanKuanTv;
    @BindView(R.id.status_image_view)
    ImageView mStatusImageView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.bohui_yuanyin_lay)
    LinearLayout mBohuiYyLay;
    @BindView(R.id.bohui_yuanyin_tv)
    TextView mBohuiYyTv;
    @BindView(R.id.chexiao_sq_button)
    Button mChexiaoButton;
    @BindView(R.id.shoutuo_pay_line)
    View mShoutuoPayLine;
    @BindView(R.id.huankuan_plan_line)
    View mHuankuanPlanLine;
    @BindView(R.id.huankuan_his_line)
    View mHuankuanHisLine;
    @BindView(R.id.jiekuan_use_line)
    View mJiekuanUseLine;
    @BindView(R.id.bottom_line)
    View mBottomLine;

    private String mRequestTag = "";

    private Map<String, Object> mDataMap;
    private Map<String, Object> mResultMap;

    @Override
    public int getContentView() {
        return R.layout.activity_borrow_detail;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        mTitleText.setText(getResources().getString(R.string.my_borrow_title));
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        mStatusImageView.setVisibility(View.GONE);
        borrowDetail();

        //mTqHuanKuanTv.getBackground().setAlpha((int)(0.1*255));
        //mTqHuanKuanTv.setTextColor(ContextCompat.getColor(this,R.color.red3));

    }

    /**
     * 借款详情
     */
    private void borrowDetail() {

        mRequestTag = MethodUrl.borrowDetail;
        Map<String, String> map = new HashMap<>();
        map.put("loansqid", mDataMap.get("loansqid") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.borrowDetail, map);
    }

    /**
     * 获取借款信息  贷后详情
     */
    private void getModifyAction() {

        mRequestTag = MethodUrl.daihouDetail;
        Map<String, String> map = new HashMap<>();
        map.put("loansqid", mDataMap.get("loansqid") + "");//借款申请编号
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.daihouDetail, map);
    }

    private void initStatus() {

        if (mResultMap != null) {
            mStatusImageView.setVisibility(View.VISIBLE);

            //借款状态（1：放款中 2：还款中 3：已结清 4：已驳回）  loanstate
            String status = mResultMap.get("loanstate") + "";
            LogUtilDebug.i("show","status:"+status);
            switch (status) {
                case "1":
                    mStatusImageView.setImageResource(R.drawable.shenhe);
                    mTqHuanKuanTv.setVisibility(View.GONE);
                    mLeftMoneyTv.setVisibility(View.VISIBLE);
                    mZongMoneyTv.setVisibility(View.GONE);
                    mBohuiYyLay.setVisibility(View.GONE);
                    // mChexiaoButton.setVisibility(View.VISIBLE);
                    mChexiaoButton.setVisibility(View.GONE);

                    mHuankuanPlanLine.setVisibility(View.GONE);
                    mBorrowRepayplanLay.setVisibility(View.GONE);

                    mHuankuanHisLine.setVisibility(View.GONE);
                    mBorrowRepayshistoryLay.setVisibility(View.GONE);

                    mJiekuanUseLine.setVisibility(View.GONE);
                    mBorrowCardLay.setVisibility(View.GONE);

                    mBottomLine.setVisibility(View.GONE);
                    break;
                case "2":
                    mStatusImageView.setImageResource(R.drawable.huankuan);
                    mTqHuanKuanTv.setVisibility(View.VISIBLE);
                    mLeftMoneyTv.setVisibility(View.VISIBLE);
                    mZongMoneyTv.setVisibility(View.VISIBLE);
                    mBohuiYyLay.setVisibility(View.GONE);
                    mChexiaoButton.setVisibility(View.GONE);
                    break;
                case "3":
                    mStatusImageView.setImageResource(R.drawable.jieqing);
                    mTqHuanKuanTv.setVisibility(View.GONE);
                    mLeftMoneyTv.setVisibility(View.VISIBLE);
                    mZongMoneyTv.setVisibility(View.VISIBLE);
                    mBohuiYyLay.setVisibility(View.GONE);
                    mChexiaoButton.setVisibility(View.GONE);
                    break;
                case "4":
                    mStatusImageView.setImageResource(R.drawable.bohui);
                    mTqHuanKuanTv.setVisibility(View.GONE);
                    mLeftMoneyTv.setVisibility(View.VISIBLE);
                    mZongMoneyTv.setVisibility(View.GONE);
                    mBohuiYyLay.setVisibility(View.VISIBLE);
                    mChexiaoButton.setVisibility(View.GONE);
                    if (UtilTools.empty(mResultMap.get("lnfailrsn"))){
                        mBohuiYyTv.setText("驳回原因:无" );
                    }else {
                        mBohuiYyTv.setText("驳回原因:" + mResultMap.get("lnfailrsn"));
                    }


                    mHuankuanPlanLine.setVisibility(View.GONE);
                    mBorrowRepayplanLay.setVisibility(View.GONE);

                    mHuankuanHisLine.setVisibility(View.GONE);
                    mBorrowRepayshistoryLay.setVisibility(View.GONE);

                    mJiekuanUseLine.setVisibility(View.GONE);
                    mBorrowCardLay.setVisibility(View.GONE);

                    mBottomLine.setVisibility(View.GONE);
                    //mShoutuoPayLine.setVisibility(View.GONE);

                    break;
                default:
                    mBohuiYyLay.setVisibility(View.GONE);
                    mChexiaoButton.setVisibility(View.GONE);
                    mTqHuanKuanTv.setVisibility(View.GONE);
                    break;
            }

        }
    }

    @OnClick({R.id.back_img, R.id.borrow_info_lay, R.id.borrow_file_lay, R.id.borrow_hetong_lay,
            R.id.borrow_pay_lay, R.id.borrow_repayplan_lay, R.id.borrow_repayshistory_lay,
            R.id.borrow_card_lay, R.id.tiqian_huankuan_tv, R.id.left_back_lay, R.id.chexiao_sq_button})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.borrow_info_lay://借款信息
                intent = new Intent(BorrowDetailActivity.this, BorrowInfoActivity.class);
                //intent.putExtra("DATA",(Serializable) mDataMap);
                intent.putExtra("DATA", (Serializable) mResultMap);
                startActivity(intent);
                break;
            case R.id.borrow_file_lay://附件信息
                intent = new Intent(BorrowDetailActivity.this, FujianShowActivity.class);
                //intent.putExtra("DATA", (Serializable) ((List<Map<String, Object>>) mResultMap.get("imgList")));
                DataHolder.getInstance().save("fileList",((List<Map<String, Object>>) mResultMap.get("imgList")));
                startActivity(intent);
                break;
            case R.id.borrow_hetong_lay://合同信息
                List<Map<String, Object>> mHetongList = (List<Map<String, Object>>) mResultMap.get("contList");
                if (mHetongList != null && mHetongList.size() > 0) {
                    if (mHetongList.size() == 1) {
                        Map<String, Object> mHetongMap = mHetongList.get(0);
                        intent = new Intent(BorrowDetailActivity.this, PDFLookActivity.class);
                        //intent.putExtra("DATA",(Serializable) mDataMap);
                        intent.putExtra("id", mHetongMap.get("pdfurl") + "");
                        startActivity(intent);
                    } else {
                        intent = new Intent(BorrowDetailActivity.this, HeTongShowActivity.class);
                        intent.putExtra("DATA", (Serializable) ((List<Map<String, Object>>) mResultMap.get("contList")));
                        startActivity(intent);
                    }
                } else {
                    showToastMsg("暂无合同信息");
                }
                break;
            case R.id.borrow_pay_lay://受托支付信息
                intent = new Intent(BorrowDetailActivity.this, PayTheInfoActivity.class);
                intent.putExtra("DATA", (Serializable) mDataMap);
                startActivity(intent);
                break;
            case R.id.borrow_repayplan_lay://还款计划
                intent = new Intent(BorrowDetailActivity.this, PayPlanActivity.class);
                intent.putExtra("DATA", (Serializable) mDataMap);
                startActivity(intent);
                break;
            case R.id.borrow_repayshistory_lay://还款记录
                intent = new Intent(BorrowDetailActivity.this, PayHistoryActivity.class);
                intent.putExtra("DATA", (Serializable) mDataMap);
                startActivity(intent);
                break;
            case R.id.borrow_card_lay://借款用途凭证
                getModifyAction();
                break;
            case R.id.tiqian_huankuan_tv:
                intent = new Intent(BorrowDetailActivity.this, PayMoneyActivity.class);
                intent.putExtra("DATA", (Serializable) mDataMap);
                startActivity(intent);
                break;
            case R.id.chexiao_sq_button:
                showDialog();
                break;
        }
    }


    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        switch (mType) {
            case MethodUrl.borrowDetail://
                mPageView.showContent();
                mResultMap = tData;

                double all = Double.valueOf(mResultMap.get("reqmoney") + "");
                double backmoney = Double.valueOf(mResultMap.get("backmoney") + "");
                double leftMoney = UtilTools.sub(all, backmoney);
                String leftStr = UtilTools.getRMBMoney(leftMoney + "");

                ParseTextUtil m = new ParseTextUtil(this);
                Spannable spannable = m.getDianType(leftStr);
                mLeftMoneyTv.setText(spannable);

                String money = UtilTools.getMoney(mResultMap.get("reqmoney") + "");
                mZongMoneyTv.setText("借款总额(元) " + money);
                initStatus();
                break;

            case MethodUrl.daihouDetail:
                List<Map<String, Object>> mHasFile = (List<Map<String, Object>>) tData.get("existFileList");
                if (mHasFile == null || mHasFile.size() == 0) {
                    showToastMsg("暂无借款用途凭证");
                } else {
                    Intent intent = new Intent(BorrowDetailActivity.this, ModifyFileActivity.class);
                    //intent.putExtra("DATA", (Serializable) mHasFile);
                    DataHolder.getInstance().save("fileList", mHasFile);
                    startActivity(intent);
                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.borrowDetail:
                        borrowDetail();
                        break;
                    case MethodUrl.daihouDetail:
                        getModifyAction();
                        break;

                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        switch (mType) {
            case MethodUrl.borrowDetail://
                mStatusImageView.setVisibility(View.GONE);

                mPageView.showNetworkError();
                break;
        }


        dealFailInfo(map, mType);
    }

    @Override
    public void reLoadingData() {
        borrowDetail();
    }

    private BaseDialog mEditDialog;
    private Button mDSureBut;
    private Button mDCancleBut;
    private EditText mDEditText;
    private TextView mDCountText;
    private int MAX_COUNT = 100;

    public void showDialog() {
        BaseAnimatorSet bas_in;
        BaseAnimatorSet bas_out;
        bas_in = new SlideBottomEnter();
        bas_in.duration(200);
        bas_out = new SlideBottomExit();
        bas_out.duration(300);

        if (mEditDialog == null) {
            mEditDialog = new BaseDialog(this, true) {
                @Override
                public View onCreateView() {
                    View view = View.inflate(BorrowDetailActivity.this, R.layout.dialog_edit_reson, null);
                    initDialog(view);
                    return view;
                }

                @Override
                public void setUiBeforShow() {

                }
            };
            mEditDialog.dimEnabled(true);
            mEditDialog.widthScale(1f);
            mEditDialog.showAnim(bas_in)//
                    .dismissAnim(bas_out);//
            mEditDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
        } else {
            mEditDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
        }
    }

    private void initDialog(View view) {
        mDSureBut = view.findViewById(R.id.submit_but);
        mDCancleBut = view.findViewById(R.id.cancle_but);
        mDEditText = view.findViewById(R.id.resuse_des_edit);
        mDCountText = view.findViewById(R.id.count);
        mDEditText.addTextChangedListener(mTextWatcher);
        setLeftCount();
        mDEditText.setSelection(mDEditText.length());
        mDSureBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditDialog.dismiss();
            }
        });

        mDCancleBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditDialog.dismiss();
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;

        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = mDEditText.getSelectionStart();
            editEnd = mDEditText.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            mDEditText.removeTextChangedListener(mTextWatcher);

            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            mDEditText.setText(s);
            mDEditText.setSelection(editStart);

            // 恢复监听器
            mDEditText.addTextChangedListener(mTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    };


    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                //len++;
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 刷新剩余输入字数,最大值新浪微博是140个字，人人网是200个字
     */
    private void setLeftCount() {
        mDCountText.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }

    /**
     * 获取用户输入的分享内容字数
     *
     * @return
     */
    private long getInputCount() {
        return calculateLength(mDEditText.getText().toString());
    }
}
