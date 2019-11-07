package com.lr.biyou.ui.moudle4.activity;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.CancelDialog;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.mywidget.dialog.KindSelectDialog2;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.ShowDetailPictrue;
import com.lr.biyou.ui.temporary.activity.PDFLookActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
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
    @BindView(R.id.background_color_lay)
    RelativeLayout bac_color_lay;
    @BindView(R.id.background_color_lay2)
    LinearLayout bac_color_lay2;
    @BindView(R.id.copy_tv)
    ImageView tvCopy;
    @BindView(R.id.contact_tv)
    TextView contactTv;


    private String mRequestTag = "";


    private Map<String, Object> mDataMap = new HashMap<>();
    private Map<String, Object> mPayConfig = new HashMap<>();
    private boolean mIsShow = false;


    private Map<String, Object> mPayCreateMap = new HashMap<>();


    private KindSelectDialog2 mDialog;
    private List<Map<String, Object>> mDataList;
    private List<Map<String, Object>> mImageList = new ArrayList<>();


    private KindSelectDialog mKindSelectDialog;

    private String mPayType = "";


    private String mID = "";
    //订单状态：0待支付，1已付款 2已完成 4超时取消 5卖家取消 6买家取消
    private String status = "0";

    private Map<String, Object> mapBank;
    private Map<String, Object> mapAliPay;
    private Map<String, Object> mapWeChat;


    private ClipboardManager mClipboardManager;
    private ClipData clipData;

    private TimeCount mTimeCount;

    @Override
    public int getContentView() {
        return R.layout.pay_money_layout;
    }

       @Override
    public void setBarTextColor() {
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

        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.white), MbsConstans.ALPHA);
        mBackImg.setImageResource(R.drawable.icon_back2);

        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE);
        registerReceiver(mBroadcastReceiver, intentFilter);

        if (bundle != null) {
            mID = bundle.getString("id");

             /* String kind = bundle.getString("kind");
             if (!UtilTools.empty(kind) && kind.equals("0")) {
                payTypeTv.setText("请付款");
                payTimeTv.setText("请在15:00内完成付款");
            } else {
                payTypeTv.setText("等待买家付款");
                payTimeTv.setText("15:00之后订单自动取消");
            }*/

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


    private void getDingdanInfoAction() {

        mRequestTag = MethodUrl.DING_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(PayMoneyActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", mID);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.DING_INFO, map);
    }

    /**
     * 获取利息信息  xxx
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

    }


    @OnClick({R.id.cancel, R.id.order, R.id.copy_tv, R.id.iv_code, R.id.back_img, R.id.left_back_lay, R.id.toggle_money, R.id.iv_select_pay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.cancel: //取消
                if (payTypeTv.getText().toString().equals("请付款")) {
                    showDialog("确认取消交易", "如果已向卖家付款,请不要取消交易", "", "1");
                }

                if (payTypeTv.getText().toString().equals("等待买家付款")) {
                    showDialog("确认取消交易", "", "如果买家已付款,恶意取消交易,查明后做封号处理!", "2");
                }


                if (payTypeTv.getText().toString().equals("买家已付款")) {
                    showDialog("确认取消交易", "", "买家已付款,您不能取消订单,请耐心等待,如果15分钟还未到账,请联系客服处理", "3");
                }
                break;
            case R.id.order://完成付款
                if (payTypeTv.getText().toString().equals("请付款")) {
                    showDialog("付款确认", "请确认您已完成付款.", "如未付款,点击确认,后台查明后做封号处理.", "4");
                }

                if (payTypeTv.getText().toString().equals("等待买家付款")) {
                    showDialog("确认收款", "确认之后,将会把币打给买家", "如果买家已付款,恶意取消交易,查明后做封号处理!", "5");
                }


                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.iv_select_pay:
                if (mDialog != null) {
                    mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.copy_tv:
                clipData = ClipData.newPlainText("币友", bankNumberTv.getText() + "");
                mClipboardManager.setPrimaryClip(clipData);
                showToastMsg("复制成功");
                break;

            case R.id.iv_code:
                intent = new Intent(PayMoneyActivity.this, ShowDetailPictrue.class);
                intent.putExtra("position", 0);
                intent.putExtra("DATA", (Serializable) mImageList);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
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
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        Map<String, Object> mapData = (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)) {
                            Map<String, Object> mapAppendInfo = (Map<String, Object>) mapData.get("appendinfo");
                            if (!UtilTools.empty(mapAppendInfo)) {
                                payTimeTv.setText(mapAppendInfo.get("tips") + "");
                                //启动定时器
                                String timeCount = mapAppendInfo.get("remaining_second") + "";
                                if (!UtilTools.empty(timeCount)) {
                                    int time = Integer.parseInt(timeCount);
                                    if (time > 0) {
                                        if (mTimeCount != null) {
                                            mTimeCount.cancel();
                                            mTimeCount = null;
                                        }
                                        mTimeCount = new TimeCount(time * 1000, 1000);
                                        mTimeCount.start();
                                    }
                                }


                                payTypeTv.setText(mapAppendInfo.get("title") + "");
                                payMoneyTv.setText(UtilTools.getRMBMoney(mapAppendInfo.get("total") + ""));

                                dingdanNumberTv.setText(mapAppendInfo.get("order") + "");
                                dingdanMoneyTv.setText(UtilTools.getRMBMoney(mapAppendInfo.get("price") + ""));
                                dingdanAmountTv.setText(mapAppendInfo.get("number") + "");
                                contactTv.setText(mapData.get("account")+"");

                                status = mapAppendInfo.get("status") + "";
                                switch (status) {
                                    //0待支付，1已付款 2已完成 4超时取消 5卖家取消 6买家取消
                                    case "2":
                                        bac_color_lay.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.an_black));
                                        bac_color_lay2.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.an_black));
                                        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.an_black), MbsConstans.ALPHA);
                                        break;
                                    case "4":
                                        bac_color_lay.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.nn_gray));
                                        bac_color_lay2.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.nn_gray));
                                        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.nn_gray), MbsConstans.ALPHA);
                                        break;
                                    case "5":
                                        bac_color_lay.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.nn_gray));
                                        bac_color_lay2.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.nn_gray));
                                        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.nn_gray), MbsConstans.ALPHA);
                                        break;
                                    case "6":
                                        bac_color_lay.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.nn_gray));
                                        bac_color_lay2.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.nn_gray));
                                        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.nn_gray), MbsConstans.ALPHA);
                                        break;

                                    default:
                                        bac_color_lay.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.deep_blue));
                                        bac_color_lay2.setBackgroundColor(ContextCompat.getColor(PayMoneyActivity.this, R.color.deep_blue));
                                        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.deep_blue), MbsConstans.ALPHA);
                                        break;
                                }

                                switch (mapAppendInfo.get("ico") + "") {
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

                            String btnData = mapData.get("btn") + "";
                            if (UtilTools.empty(btnData)) {
                                cancel.setVisibility(View.GONE);
                                order.setVisibility(View.GONE);
                            } else {
                                Map<String, Object> mapBtn = (Map<String, Object>) mapData.get("btn");
                                if (!UtilTools.empty(mapBtn)) {
                                    cancel.setText(mapBtn.get("cancel") + "");
                                    order.setText(mapBtn.get("confirm") + "");
                                }
                            }


                            Map<String, Object> mapPayWay = (Map<String, Object>) mapData.get("receivables");
                            if (!UtilTools.empty(mapPayWay)) {
                                if (!UtilTools.empty(mapPayWay.get("bank") + "")) {
                                    mapBank = (Map<String, Object>) mapPayWay.get("bank");
                                }
                                if (!UtilTools.empty(mapPayWay.get("alipay") + "")) {
                                    mapAliPay = (Map<String, Object>) mapPayWay.get("alipay");
                                }

                                if (!UtilTools.empty(mapPayWay.get("wechat") + "")) {
                                    mapWeChat = (Map<String, Object>) mapPayWay.get("wechat");
                                }


                                mDataList = new ArrayList<>();
                                if (!UtilTools.empty(mapBank)) {
                                    mapBank.put("code", "0");
                                    mapBank.put("type", "银行卡");
                                    mDataList.add(mapBank);

                                }

                                if (!UtilTools.empty(mapAliPay)) {
                                    mapAliPay.put("code", "1");
                                    mapAliPay.put("type", "支付宝");
                                    mDataList.add(mapAliPay);

                                }

                                if (!UtilTools.empty(mapWeChat)) {
                                    mapWeChat.put("code", "2");
                                    mapWeChat.put("type", "微信支付");
                                    mDataList.add(mapWeChat);
                                }

                                if (mDataList.size() > 0) {
                                    Map<String, Object> map = mDataList.get(0);
                                    switch (map.get("code") + "") {
                                        case "0": //银行卡
                                            ivIcon.setImageResource(R.drawable.icon4_bank);
                                            bankPayLay.setVisibility(View.VISIBLE);
                                            mobilePayLay.setVisibility(View.GONE);
                                            mZhifuNameTv.setText(map.get("type") + "");
                                            bankPeopleTv.setText(map.get("name") + "");
                                            bankNumberTv.setText(map.get("number") + "");
                                            bankTv.setText(map.get("bank_name") + "");

                                            mPayType = "bank";
                                            break;

                                        case "1": //支付宝
                                            ivIcon.setImageResource(R.drawable.icon4_alipay);
                                            bankPayLay.setVisibility(View.GONE);
                                            mobilePayLay.setVisibility(View.VISIBLE);
                                            mZhifuNameTv.setText(map.get("type") + "");
                                            bankPeopleTv.setText(map.get("name") + "");
                                            GlideUtils.loadImage(PayMoneyActivity.this, map.get("qrcode") + "", ivCode);

                                            mImageList.clear();
                                            Map<String, Object> mapImage = new HashMap<>();
                                            mapImage.put("url", map.get("qrcode") + "");
                                            mImageList.add(mapImage);

                                            mPayType = "alipay";
                                            break;

                                        case "2": //微信
                                            ivIcon.setImageResource(R.drawable.icon4_wechat);
                                            bankPayLay.setVisibility(View.GONE);
                                            mobilePayLay.setVisibility(View.VISIBLE);
                                            mZhifuNameTv.setText(map.get("type") + "");
                                            bankPeopleTv.setText(map.get("name") + "");
                                            GlideUtils.loadImage(PayMoneyActivity.this, map.get("qrcode") + "", ivCode);

                                            mImageList.clear();
                                            Map<String, Object> mapImage1 = new HashMap<>();
                                            mapImage1.put("url", map.get("qrcode") + "");
                                            mImageList.add(mapImage1);

                                            mPayType = "wechat";
                                            break;
                                    }

                                }
                                mDialog = new KindSelectDialog2(PayMoneyActivity.this, true, mDataList, 30);
                                mDialog.setSelectBackListener(this);

                            }
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(PayMoneyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;

                }
                break;

            case MethodUrl.DING_CANCEL:
                switch (tData.get("code") + "") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        getDingdanInfoAction();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(PayMoneyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;
            case MethodUrl.SELLER_SURE:
                switch (tData.get("code") + "") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        getDingdanInfoAction();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(PayMoneyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;
            case MethodUrl.BUYER_SURE:
                switch (tData.get("code") + "") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        getDingdanInfoAction();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
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
        switch (type) {
            case 30:
                mZhifuNameTv.setText(map.get("name") + "");
                switch (map.get("code") + "") {
                    case "0": //银行卡
                        ivIcon.setImageResource(R.drawable.icon4_bank);
                        bankPayLay.setVisibility(View.VISIBLE);
                        mobilePayLay.setVisibility(View.GONE);
                        mZhifuNameTv.setText(map.get("type") + "");
                        bankPeopleTv.setText(map.get("name") + "");
                        bankNumberTv.setText(map.get("number") + "");
                        bankTv.setText(map.get("bank_name") + "");

                        mPayType = "bank";
                        break;

                    case "1": //支付宝
                        ivIcon.setImageResource(R.drawable.icon4_alipay);
                        bankPayLay.setVisibility(View.GONE);
                        mobilePayLay.setVisibility(View.VISIBLE);
                        mZhifuNameTv.setText(map.get("type") + "");
                        bankPeopleTv.setText(map.get("name") + "");
                        GlideUtils.loadImage(PayMoneyActivity.this, map.get("qrcode") + "", ivCode);

                        mImageList.clear();
                        Map<String, Object> mapImage = new HashMap<>();
                        mapImage.put("url", map.get("qrcode") + "");
                        mImageList.add(mapImage);

                        mPayType = "alipay";
                        break;

                    case "2": //微信
                        ivIcon.setImageResource(R.drawable.icon4_wechat);
                        bankPayLay.setVisibility(View.GONE);
                        mobilePayLay.setVisibility(View.VISIBLE);
                        mZhifuNameTv.setText(map.get("type") + "");
                        bankPeopleTv.setText(map.get("name") + "");
                        GlideUtils.loadImage(PayMoneyActivity.this, map.get("qrcode") + "", ivCode);

                        mImageList.clear();
                        Map<String, Object> mapImage1 = new HashMap<>();
                        mapImage1.put("url", map.get("qrcode") + "");
                        mImageList.add(mapImage1);

                        mPayType = "wechat";
                        break;
                }
                break;
        }

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

    private void showDialog(String title, String message1, String message2, String type) {

        mTipsDialog = new CancelDialog(this, title, message1,
                message2, "我再想想", "确认");
        mTipsDialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel_tv:
                        mTipsDialog.dismiss();
                        break;
                    case R.id.sure_tv:
                        switch (type) {
                            case "1":
                                //取消订单
                                cancelDingDanAction();
                                break;
                            case "2":
                                //取消订单
                                cancelDingDanAction();
                                break;
                            case "3":
                                break;
                            case "4":
                                //付款确认
                                surePayMoneyAction();
                                break;
                            case "5":
                                //收款确认
                                sureShouKuanAction();
                                break;

                        }
                        mTipsDialog.dismiss();
                        break;
                }
            }
        });

        mTipsDialog.setCanceledOnTouchOutside(false);
        mTipsDialog.setCancelable(true);
        mTipsDialog.show();

    }

    private void surePayMoneyAction() {

        mRequestTag = MethodUrl.BUYER_SURE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(PayMoneyActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", mID);
        map.put("payType", mPayType);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.BUYER_SURE, map);
    }

    private void sureShouKuanAction() {

        mRequestTag = MethodUrl.SELLER_SURE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(PayMoneyActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", mID);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.SELLER_SURE, map);
    }

    private void cancelDingDanAction() {

        mRequestTag = MethodUrl.DING_CANCEL;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(PayMoneyActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", mID);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.DING_CANCEL, map);
    }


    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        if (mTimeCount != null) {
            mTimeCount.cancel();
            mTimeCount = null;
        }
    }


    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            //tvCod.setText(getResources().getString(R.string.msg_code_again));
            //tvCode.setClickable(true);
            //MbsConstans.CURRENT_TIME = 0;

            //刷新订单状态
            getDingdanInfoAction();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            //tvCode.setClickable(false);
            //tvCode.setText(millisUntilFinished / 1000 + "秒");
            Log.i("show", "格式化时间:" + UtilTools.getStandardDate2(millisUntilFinished));
            switch (status) {
                case "0": //请在 1天23:54:2 内完成付款
                    if (payTimeTv.getText().toString().contains("内完成付款")) {
                        payTimeTv.setText("请在 " + UtilTools.getStandardDate2(millisUntilFinished) + " 内完成付款");
                    }

                    if (payTimeTv.getText().toString().contains("后订单自动取消")) {
                        payTimeTv.setText(UtilTools.getStandardDate2(millisUntilFinished) + " 后订单自动取消");
                    }
                    break;

                case "1":

                    break;
            }


        }
    }


}
