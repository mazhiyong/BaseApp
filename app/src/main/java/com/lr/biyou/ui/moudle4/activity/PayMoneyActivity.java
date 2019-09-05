package com.lr.biyou.ui.moudle4.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.BankCardSelectDialog;
import com.lr.biyou.mywidget.dialog.CancelDialog;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.ResetPayPassButActivity;
import com.lr.biyou.ui.temporary.activity.ApplyAmountActivity;
import com.lr.biyou.ui.temporary.activity.PDFLookActivity;
import com.lr.biyou.ui.temporary.activity.ResultMoneyActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;





import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 付款界面  界面
 */
public class PayMoneyActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.tv_repay_capital)
    EditText mTvRepayCapital;
    @BindView(R.id.tv_principal)
    TextView mTvPrincipal;
    @BindView(R.id.tv_loan_balance)
    TextView mTvLoanBalance;
    @BindView(R.id.zhifu_name_tv)
    TextView mZhifuNameTv;
    @BindView(R.id.image_view)
    ImageView mBankImageView;
    @BindView(R.id.xieyi_tv)
    TextView mXieyiTv;
    @BindView(R.id.iv_select_pay)
    LinearLayout mIvSelectPay;
    @BindView(R.id.xiyi_lay)
    LinearLayout mXieyiLay;
    @BindView(R.id.check_box_xieyi)
    CheckBox mXieyiCheckBox;
    @BindView(R.id.zhifu_arrow_view)
    ImageView mArrowView;
    @BindView(R.id.yue_tv)
    TextView mYueTv;

    @BindView(R.id.toggle_money)
    ImageView mToggleMoney;
    @BindView(R.id.zhanghu_yue_lay)
    LinearLayout mZhanghuYueLay;
    @BindView(R.id.zhanghu_yue_line)
    View mZhanghuYueLine;

    @BindView(R.id.yy_edit)
    EditText mYuanyinET;
    @BindView(R.id.yuanyin_lay)
    LinearLayout mYuanLay;
    @BindView(R.id.pay_type_tv)
    TextView payTypeTv;
    @BindView(R.id.pay_money_tv)
    TextView payMoneyTv;
    @BindView(R.id.pay_time_tv)
    TextView payTimeTv;
    @BindView(R.id.dingdan_number_tv)
    TextView dingdanNumberTv;
    @BindView(R.id.dingdan_money_tv)
    TextView dingdanMoneyTv;
    @BindView(R.id.dingdan_amount_tv)
    TextView dingdanAmountTv;
    @BindView(R.id.bank_people_tv)
    TextView bankPeopleTv;
    @BindView(R.id.bank_number_tv)
    TextView bankNumberTv;
    @BindView(R.id.bank_num_lay)
    LinearLayout bankNumLay;
    @BindView(R.id.bank_tv)
    TextView bankTv;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.order)
    TextView order;
    @BindView(R.id.bank_pay_lay)
    LinearLayout bankPayLay;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.mobile_pay_lay)
    LinearLayout mobilePayLay;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.zhanghu_ine)
    View zhanghuIne;
    @BindView(R.id.count_tv)
    TextView countTv;
    @BindView(R.id.icon)
    ImageView Icon;


    private String mRequestTag = "";


    private Map<String, Object> mDataMap = new HashMap<>();
    private Map<String, Object> mPayConfig = new HashMap<>();
    private boolean mIsShow = false;

    private String mType = "";
    private String mYuanyin = "";

    private Map<String, Object> mPayCreateMap = new HashMap<>();
    private String mLixi = "";
    private String mBenjin = "";

    private BankCardSelectDialog mDialog;
    private List<Map<String, Object>> mDataList;
    private List<Map<String, Object>> mBankList;

    private Map<String, Object> mBankMap;

    private boolean mIsShowDialog = false;


    private double mMoney = 0;
    private double mYuEMoney = 0;

    private Map<String, Object> mHuankuanMap;

    private double mRequestLixi;


    private KindSelectDialog mKindSelectDialog;

    private String mPayType = "2";


    private String mID = "";
    //订单状态：0待支付，1已付款 2已完成 4超时取消 5卖家取消 6买家取消
    private String status = "0";

    @Override
    public int getContentView() {
        return R.layout.pay_money_layout;
    }

    @Override
    public void setBarTextColor(){
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StatusBarUtil.setDarkMode(this);
        } else {
            StatusBarUtil.setLightMode(this);
        }*/

        StatusBarUtil.setDarkMode(this);
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.deep_blue), MbsConstans.ALPHA);
        mBackImg.setImageResource(R.drawable.icon_back2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE);
        registerReceiver(mBroadcastReceiver, intentFilter);

        if (bundle != null) {
            String kind = bundle.getString("kind");
            mID = bundle.getString("id");
            if (!UtilTools.empty(kind) && kind.equals("0")) {
                payTypeTv.setText("请付款");
                payTimeTv.setText("请在15:00内完成付款");
            } else {
                payTypeTv.setText("等待买家付款");
                payTimeTv.setText("15:00之后订单自动取消");
            }


            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
            if (bundle.containsKey("TYPE")) {
                mPayCreateMap = (Map<String, Object>) bundle.getSerializable("DATA2");
                mType = bundle.getString("TYPE");
                mLixi = bundle.getString("LIXI");
                mBenjin = bundle.getString("BENJIN");
                mYuEMoney = bundle.getDouble("MAXMONEY");
                mPayType = bundle.getString("PAYTYPE");
            }
        }

        UtilTools.setMoneyEdit(mTvRepayCapital, 0);

        //键盘显示监听
        mTvRepayCapital.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                final Rect rect = new Rect();
                PayMoneyActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                final int screenHeight = PayMoneyActivity.this.getWindow().getDecorView().getRootView().getHeight();
                Log.e("TAG", rect.bottom + "#" + screenHeight);
                final int heightDifference = screenHeight - rect.bottom;
                boolean visible = heightDifference > screenHeight / 3;
                if (visible) {
                    mIsShow = true;
                } else {
                    if (mIsShow) {
                        getLixiAction();
                        //showToastMsg("软键盘隐藏");
                    }
                    mIsShow = false;
                }
            }
        });

        showProgressDialog();
        getDingdanInfoAction();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE)) {
                finish();
            }

        }
    };



    /**
     * 获取还款申请配置信息
     */
    private void getDingdanInfoAction() {

        mRequestTag = MethodUrl.DING_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(PayMoneyActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("id",mID);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.DING_INFO, map);
    }

    /**
     * 获取利息信息
     */
    private void getLixiAction() {

        String money = mTvRepayCapital.getText() + "";
        if (UtilTools.empty(money)) {
            return;
        }
       /* double shuri = Double.valueOf(money);
        if (shuri > mMoney){
            return;
        }*/


        mRequestTag = MethodUrl.repayLixi;
        Map<String, String> map = new HashMap<>();
        map.put("loansqid", mDataMap.get("loansqid") + "");
        map.put("backbejn", mTvRepayCapital.getText() + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.repayLixi, map);
        showProgressDialog();
    }





    @Override
    public void viewEnable() {
        if (mRequestTag.equals(MethodUrl.repayConfig)) {

        } else {

        }

    }


    public List<Map<String, Object>> getBeginAndEnd(Pattern pattern,
                                                    String content) {
        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            Map<String, Object> value = new HashMap<String, Object>();
            value.put("VALUE", matcher.group());
            value.put("BEGIN", Integer.valueOf(matcher.start()));
            value.put("END", Integer.valueOf(matcher.end()));
            values.add(value);
        }
        return values;
    }


    @OnClick({R.id.cancel, R.id.order, R.id.back_img, R.id.iv_select_pay, R.id.left_back_lay, R.id.toggle_money})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.cancel: //取消
                showDialog();
                break;
            case R.id.order://完成付款

                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;

        }
    }

    @Override
    public void showProgress() {
        //showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType) {
            case MethodUrl.DING_INFO:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        Map<String,Object> mapData = (Map<String, Object>) tData.get("");
                        if (!UtilTools.empty(mapData)){
                            Map<String,Object> mapAppendInfo = (Map<String, Object>) mapData.get("appendinfo");
                            if (!UtilTools.empty(mapAppendInfo)){
                                payTimeTv.setText(mapAppendInfo.get("tips")+"");
                                payTypeTv.setText(mapAppendInfo.get("title")+"");
                                payMoneyTv.setText(UtilTools.getRMBMoney(mapAppendInfo.get("total")+""));

                                dingdanNumberTv.setText(mapAppendInfo.get("order")+"");
                                dingdanMoneyTv.setText(UtilTools.getRMBMoney(mapAppendInfo.get("price")+""));
                                dingdanAmountTv.setText(mapAppendInfo.get("number")+"");

                                status = mapAppendInfo.get("status")+"";
                                switch (mapAppendInfo.get("ico")+""){
                                    case "1": //闹钟
                                        Icon.setImageResource(R.drawable.wait);
                                        break;
                                    case "2": //对号
                                        Icon.setImageResource(R.drawable.icon4_1yes);
                                        break;
                                    case "3": //叉号
                                        Icon.setImageResource(R.drawable.icon4_1no);

                                        break;
                                }

                            }

                            Map<String,Object> mapBtn= (Map<String, Object>) mapData.get("btn");
                            if (!UtilTools.empty(mapBtn)){
                                cancel.setText(mapBtn.get("cancel")+"");
                                order.setText(mapBtn.get("confirm")+"");
                            }


                            Map<String,Object> mapPayWay= (Map<String, Object>) mapData.get("receivables");




                        }


                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(PayMoneyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;

                }


                break;


            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
        //mButNext.setEnabled(true);
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {

    }


    private final class TextSpanClick extends ClickableSpan {
        private boolean status;

        public TextSpanClick(boolean status) {
            this.status = status;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);//取消下划线false
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PayMoneyActivity.this, PDFLookActivity.class);
            intent.putExtra("id", mPayCreateMap.get("pdfurl") + "");
            startActivity(intent);
        }
    }


    private CancelDialog mTipsDialog;

    private void showDialog() {
        if (mTipsDialog == null) {
            mTipsDialog = new CancelDialog(this, "付款确认", "请确认您已完成付款。",
                    "如未付款，点击确认，后台查明后做封号处理。", "我再想想", "确认");
            mTipsDialog.setClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.cancel_tv:
                            mTipsDialog.dismiss();
                            break;
                        case R.id.sure_tv:
                            mTipsDialog.dismiss();
                            break;
                    }
                }
            });

            mTipsDialog.setCanceledOnTouchOutside(false);
            mTipsDialog.setCancelable(true);
        }
        mTipsDialog.show();

    }

    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
