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
import com.lr.biyou.ui.temporary.activity.ApplyAmountActivity;
import com.lr.biyou.ui.temporary.activity.PDFLookActivity;
import com.lr.biyou.ui.temporary.activity.ResultMoneyActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
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
        //bankCardInfoAction();
        //getConfigAction();
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
     * 服务协议的显示信息
     */
    private void setXieyi() {

        String tip = "已阅读并同意《还款申请书》";
        int dian = tip.length();
        if (tip.contains("《")) {
            dian = tip.indexOf("《");
        } else {
            dian = tip.length();
        }

       /* 用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
        Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)
        Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)*/
        SpannableString ss = new SpannableString(tip);
        ss.setSpan(new TextSpanClick(false), dian, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.data_col)), 0, dian, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.xiey_color)), dian, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mXieyiTv.setText(ss);
        //添加点击事件时，必须设置
        mXieyiTv.setMovementMethod(LinkMovementMethod.getInstance());

        mXieyiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // mButNext.setEnabled(true);
                } else {
                    //mButNext.setEnabled(false);
                }
            }
        });
        mArrowView.setVisibility(View.INVISIBLE);

    }


    /**
     * 获取还款申请配置信息
     */
    private void getConfigAction() {
        // mButNext.setEnabled(false);

        mRequestTag = MethodUrl.repayConfig;
        Map<String, String> map = new HashMap<>();
        map.put("loansqid", mDataMap.get("loansqid") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.repayConfig, map);
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

    /**
     * 生成还款申请书
     */
    private void createRepay() {

        if (UtilTools.isEmpty(mTvRepayCapital, "金额")) {
            showToastMsg("金额不能为空");
            //mButNext.setEnabled(true);
            return;
        }
        String money = mTvRepayCapital.getText() + "";

        double shuru = Double.valueOf(money);
        if (shuru > mMoney) {
            showToastMsg("输入金额不能大于" + UtilTools.fromDouble(mMoney) + "元");
            //mButNext.setEnabled(true);
            return;
        }
        /*double lx = UtilTools.divide(mRequestLixi,100);

        double allMoney = UtilTools.add(shuru,lx);
        if(allMoney > mYuEMoney){
            showToastMsg("账户余额不足");
            mButNext.setEnabled(true);
            return;
        }*/
        if (shuru == 0) {
            showToastMsg("输入余额不能为0");
            //mButNext.setEnabled(true);
            return;
        }

        if (UtilTools.empty(mZhifuNameTv.getText().toString().trim())) {
            showToastMsg("请选择支付方式");
            //mButNext.setEnabled(true);
            return;
        }


        mYuanyin = mYuanyinET.getText().toString().trim();

        mRequestTag = MethodUrl.repayCreate;
        Map<String, Object> map = new HashMap<>();
        map.put("loansqid", mDataMap.get("loansqid") + "");//借款编号
        map.put("backbejn", mTvRepayCapital.getText() + "");//归还本金
        map.put("memo", mYuanyin);  //还款原因
        double l = UtilTools.divide(Double.valueOf(mRequestLixi), 100);
        map.put("backlixi", l + "");//归还利息
        map.put("backtype", mPayType);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.repayCreate, map);
        showProgressDialog();
    }

    /**
     * 获取用户银行卡列表
     */
    private void bankCardInfoAction() {
        mRequestTag = MethodUrl.bankCard;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeadermap = new HashMap<>();
        mRequestPresenterImp.requestGetToRes(mHeadermap, MethodUrl.bankCard, map);
        showProgressDialog();
    }

    /**
     * 还款提交申请
     */
    private void submitData() {

        if (!mXieyiCheckBox.isChecked()) {
            //mButNext.setEnabled(true);
            showToastMsg(getResources().getString(R.string.xieyi_tips));
            return;
        }

        mRequestTag = MethodUrl.repayApply;
        Map<String, Object> map = new HashMap<>();
        map.put("rtnbillid", mPayCreateMap.get("rtnbillid") + "");//还款申请编号
        map.put("loansqid", mDataMap.get("loansqid") + "");//借款编号
        map.put("backbejn", mBenjin + "");//归还本金
        map.put("memo", mYuanyin);  //还款原因
        double l = UtilTools.divide(Double.valueOf(mLixi), 100);
        map.put("backlixi", l + "");//归还利息
        map.put("backtype", mPayType);//还款账户类型(1：结算账户还款;2：资金账户还款
        mHuankuanMap = map;
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        LogUtilDebug.i("还款参数打印", map);
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.repayApply, map);
        showProgressDialog();
    }

    /**
     * 余额查询
     */
    private void YueMoney() {
        mRequestTag = MethodUrl.allZichan;
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        Map<String, String> map = new HashMap<>();
        //还款账户类型(1：结算账户还款;2：资金账户还款
        if ((mZhifuNameTv.getText().toString().trim()).equals("结算账户还款")) {
            // map.put("qry_type","accid");
            // map.put("accid",mBankMap.get("accid")+"");
        } else {
            map.put("qry_type", "acct");
        }
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.allZichan, map);
        showProgressDialog();
    }


    //二类户余额查询
    private void erLeiHuMoney() {
        if (mBankMap == null || mBankMap.isEmpty()) {
            showToastMsg("请选择支付方式");
            return;
        }

        mRequestTag = MethodUrl.erleiMoney;
        Map<String, String> map = new HashMap<>();
        map.put("patncode", mBankMap.get("patncode") + "");
        map.put("crdno", mBankMap.get("accid") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.erleiMoney, map);
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

    /**
     * 显示银行卡信息
     */
    private void showCard(String str) {
       /* Spannable mSpannableString = new SpannableString(str);

        //匹配小括号里面内容
        List<Map<String, Object>> values = this.getBeginAndEnd(
                Pattern.compile("(?<=\\()[^\\)]+"), str);

        if (values == null || values.size() <= 0) {
            values = this.getBeginAndEnd(Pattern.compile("(?<=\\()[^\\)]+"), str);
        }


        for (Map<String, Object> value : values) {
            int begin = Integer.parseInt(value.get("BEGIN").toString());
            int end = Integer.parseInt(value.get("END").toString());
             mSpannableString.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(this,12)), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // mSpannableString.setSpan(new
            // StyleSpan(android.graphics.Typeface.BOLD), begin, end,
            // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗体
            mSpannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.black)), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

        }
        mZhifuNameTv.setText(mSpannableString);*/


        Spannable mSpan = new SpannableString(str);
        List<Map<String, Object>> values2 = this.getBeginAndEnd(Pattern.compile("\\d+"), str);
        for (Map<String, Object> value : values2) {
            int begin = Integer.parseInt(value.get("BEGIN").toString());
            int end = Integer.parseInt(value.get("END").toString());
            mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(this, 16)), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.gray_text2)), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        }
        mZhifuNameTv.setText(mSpan);

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

            case R.id.toggle_money:
                boolean b = mToggleMoney.isSelected();
                if (!b) {
                    //erLeiHuMoney();
                    YueMoney();
                    mYueTv.setText(MbsConstans.RMB + " " + UtilTools.getNormalMoney("0"));
                    mToggleMoney.setSelected(true);

                } else {
                    mToggleMoney.setSelected(false);
                    mYueTv.setText("****");
                }
                break;
            case R.id.iv_select_pay:
               /* if (mDialog != null){
                    mDialog.showAtLocation(Gravity.BOTTOM,0,0);
                }else {
                    bankCardInfoAction();
                    mIsShow = true;
                }*/
                //选择支付方式   还款账户类型(0：银行卡; 1 支付宝 2 微信支付  )
                List<Map<String, Object>> mTypeList = SelectDataUtil.getPayWayValues();


                mKindSelectDialog = new KindSelectDialog(this, true, mTypeList, 10);
                mKindSelectDialog.setSelectBackListener(this);
                mKindSelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
//            case R.id.but_next:
//                //mButNext.setEnabled(false);
//                if (mType.equals("1")) {
//                    //submitData();
//                    if (!mXieyiCheckBox.isChecked()) {
//                       // mButNext.setEnabled(true);
//                        showToastMsg(getResources().getString(R.string.xieyi_tips));
//                        return;
//                    }
//                    if (isCheck) {
//                        submitData();
//                    } else {
//
//                        PermissionsUtils.requsetRunPermission(PayMoneyActivity.this, new RePermissionResultBack() {
//                            @Override
//                            public void requestSuccess() {
//                                netWorkWarranty();
//                            }
//
//                            @Override
//                            public void requestFailer() {
//                                toast(R.string.failure);
//                                //mButNext.setEnabled(true);
//                            }
//                        }, Permission.Group.CAMERA, Permission.Group.STORAGE);
//
//
//                    }
//                    //enterNextPage();
//                    // submitData();
//                } else {
//                    createRepay();
//                }
//                break;
            case R.id.back_img:
                finish();
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

        //{smstoken=sms_token@3948602038bb8ecf912e0ede4a577ebd, send_tel=151****3298}
        Intent intent;
        switch (mType) {
            case MethodUrl.erleiMoney:
                double yue = Double.valueOf(tData.get("acctbal") + "");
                mYuEMoney = UtilTools.divide(yue, 100);
                mYueTv.setText(MbsConstans.RMB + " " + UtilTools.getNormalMoney(tData.get("acctbal") + ""));
                mToggleMoney.setSelected(true);
                break;
            case MethodUrl.bankCard:
                String result = tData.get("result") + "";
                if (UtilTools.empty(result)) {

                } else {
                    mDataList = JSONUtil.getInstance().jsonToList(result);
                }
                if (mDataList != null && mDataList.size() > 0) {

                    mBankList = new ArrayList<>(mDataList);
                    /*for (Map<String,Object> mm : mDataList){
                        String accid = mm.get("accid")+"";
                        String isDefault = mm.get("isdefault")+"";
                        if (!UtilTools.empty(accid) && isDefault.equals("1")){

                            mBankList.add(mm);
                        }
                    }*/
                    mBankMap = mBankList.get(0);
                    mDialog = new BankCardSelectDialog(this, true, mBankList, 30);
                    mDialog.setSelectBackListener(this);

                    String accid = mBankMap.get("accid") + "";
                    String weihao = accid.substring(accid.length() - 4, accid.length());
                    String s = mBankMap.get("bankname") + "(" + weihao + ")";
                    showCard(s);

                    erLeiHuMoney();

                    GlideUtils.loadImage(PayMoneyActivity.this, mBankMap.get("logopath") + "", mBankImageView);
                    if (mIsShow) {
                        mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                    }
                } else {
                    showToastMsg("暂无银行卡信息");
                }
                mIsShow = false;
                break;
            case MethodUrl.repayApply://还款提交数据
                isCheck = false;
                showToastMsg("提交成功");
                intent = new Intent(this, ResultMoneyActivity.class);
                intent.putExtra(MbsConstans.ResultType.RESULT_KEY, MbsConstans.ResultType.RESULT_HUANKUAN);
                intent.putExtra("DATA", (Serializable) mHuankuanMap);
                startActivity(intent);
                finish();
                break;
            case MethodUrl.repayCreate://生成还款申请书
                intent = new Intent(PayMoneyActivity.this, PayMoneyActivity.class);
                intent.putExtra("DATA", (Serializable) mDataMap);
                intent.putExtra("DATA2", (Serializable) tData);
                intent.putExtra("TYPE", "1");
                intent.putExtra("LIXI", mRequestLixi + "");
                intent.putExtra("BENJIN", mTvRepayCapital.getText() + "");
                intent.putExtra("MAXMONEY", mMoney);
                intent.putExtra("PAYTYPE", mPayType + "");
                startActivity(intent);
                //mButNext.setEnabled(true);
                break;
            case MethodUrl.repayLixi://利息

                double lixi2 = Double.valueOf(tData.get("repaylx") + "");
                mRequestLixi = lixi2;
                double d2 = UtilTools.divide(lixi2, 100);
                mTvPrincipal.setText(UtilTools.fromDouble(d2) + "");
                break;
            case MethodUrl.repayConfig://配置信息
                //bankCardInfoAction();
                //mButNext.setEnabled(true);

                mPayConfig = tData;
                double dd = Double.valueOf(mPayConfig.get("backbejn") + "");
                mMoney = UtilTools.divide(dd, 100);
                mTvRepayCapital.setText(UtilTools.fromDouble(mMoney) + "");

                UtilTools.setMoneyEdit(mTvRepayCapital, mMoney);

                double lixi = Double.valueOf(mPayConfig.get("repaylx") + "");
                mRequestLixi = lixi;
                double d = UtilTools.divide(lixi, 100);
                mTvPrincipal.setText(UtilTools.fromDouble(d) + "");
                mTvLoanBalance.setText(UtilTools.getRMBMoney(mPayConfig.get("backbejn") + ""));


                String s = mPayConfig.get("zftype") + "";

                // 还款账户类型(1：结算账户还款;2：资金账户还款)
                if (s.equals("0")) {
                    mPayType = "1";
                } else if (s.equals("1")) {
                    mPayType = "2";
                } else {
                    mPayType = "2";
                }

                if (mPayType.equals("1")) {
                    mZhanghuYueLay.setVisibility(View.GONE);
                }

                Map<String, Object> mTypeMap = SelectDataUtil.getMapByKey(mPayType, SelectDataUtil.getNameCodeByType("repayAcct"));
                mZhifuNameTv.setText(mTypeMap.get(mPayType) + "");
                break;
            case MethodUrl.allZichan: //查询余额
                mYueTv.setText(UtilTools.getRMBMoney(tData.get("use_amt") + ""));
                mToggleMoney.setSelected(true);
                break;

            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                for (String stag : mRequestTagList) {
                    switch (stag) {
                        case MethodUrl.bankCard:
                            //bankCardInfoAction();
                            break;
                        case MethodUrl.repayConfig:
                            getConfigAction();
                            break;
                        case MethodUrl.repayApply://还款提交数据
                            submitData();
                            break;
                        case MethodUrl.repayCreate://生成还款申请书
                            createRepay();
                            break;
                        case MethodUrl.repayLixi://利息
                            getLixiAction();
                            break;
                        case MethodUrl.erleiMoney:
                            erLeiHuMoney();
                            break;
                        case MethodUrl.allZichan:
                            YueMoney();
                            break;
                    }
                }
                mRequestTagList = new ArrayList<>();
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        switch (mType) {
            case MethodUrl.allZichan:
                mToggleMoney.setSelected(false);
                mYueTv.setText("****");
                break;
            case MethodUrl.erleiMoney:
                mToggleMoney.setSelected(false);
                mYueTv.setText("****");
                break;
            case MethodUrl.repayApply://还款提交数据
                isCheck = false;
                //mButNext.setEnabled(true);
                finish();
                break;
            case MethodUrl.repayCreate://生成还款申请书
                //mButNext.setEnabled(true);

                break;
            case MethodUrl.repayLixi://利息
                break;
            case MethodUrl.repayConfig://配置信息
                //mButNext.setEnabled(false);
                String msg = map.get("errmsg") + "";

                break;
        }
        dealFailInfo(map, mType);
        //mButNext.setEnabled(true);
    }


   /* private TipMsgDialog mTipMsgDialog;
    private void showZhangDialog(String msg){
        if (mTipMsgDialog == null){
            mTipMsgDialog = new TipMsgDialog(this,true);
            mTipMsgDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                        dialog.dismiss();
                        finish();
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.cancel:
                            mTipMsgDialog.dismiss();
                            finish();
                            break;
                        case R.id.confirm:
                            mTipMsgDialog.dismiss();
                            break;
                        case R.id.tv_right:
                            mTipMsgDialog.dismiss();
                            finish();
                            break;
                    }
                }
            };
            mTipMsgDialog.setCanceledOnTouchOutside(false);
            mTipMsgDialog.setCancelable(true);
            mTipMsgDialog.setOnClickListener(onClickListener);
        }
        mTipMsgDialog.initValue("温馨提示",msg);
        mTipMsgDialog.show();
    }*/


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 30:
                mBankMap = map;
                erLeiHuMoney();
                String accid = mBankMap.get("accid") + "";
                String weihao = accid.substring(accid.length() - 4, accid.length());
                String s = mBankMap.get("bankname") + "(" + weihao + ")";
                showCard(s);
                GlideUtils.loadImage(PayMoneyActivity.this, map.get("logopath") + "", mBankImageView);
                break;
            case 10:
                mPayType = map.get("code") + "";
                mZhifuNameTv.setText(map.get("name") + "");
                if (mPayType.equals("0")){
                    bankPayLay.setVisibility(View.VISIBLE);
                    mobilePayLay.setVisibility(View.GONE);
                    ivIcon.setImageResource(R.drawable.icon4_bank);
                }
                if (mPayType.equals("1")){
                    bankPayLay.setVisibility(View.GONE);
                    mobilePayLay.setVisibility(View.VISIBLE);
                    ivIcon.setImageResource(R.drawable.icon4_alipay);
                }
                if (mPayType.equals("2")){
                    bankPayLay.setVisibility(View.GONE);
                    mobilePayLay.setVisibility(View.VISIBLE);
                    ivIcon.setImageResource(R.drawable.icon4_wechat);
                }


                break;
        }
    }


    private boolean isCheck = false;
    private static final int PAGE_INTO_LIVENESS = 101;

    private void enterNextPage() {
        //startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = null;
        Bundle bundle;
        if (requestCode == 1) {
            switch (resultCode) {//人脸识别还款
                case MbsConstans.FaceType.FACE_CHECK_HUANKUAN:
                    bundle = data.getExtras();
                    if (bundle == null) {
                        isCheck = false;
                        //mButNext.setEnabled(true);
                    } else {
                        showProgress();
                        isCheck = true;
                        //mButNext.setEnabled(false);
                        submitData();
                    }
                    break;
                default:
                    //mButNext.setEnabled(true);
                    break;

            }

        } else if (requestCode == PAGE_INTO_LIVENESS) {//人脸识别返回来的数据
            if (resultCode == RESULT_OK) {
                bundle = data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_CHECK_HUANKUAN);
                intent = new Intent(PayMoneyActivity.this, ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent, 1);
            } else {
                //mButNext.setEnabled(true);
            }
        }
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {

//        final String uuid = ConUtil.getUUIDString(this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Manager manager = new Manager(PayMoneyActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(PayMoneyActivity.this);
//                manager.registerLicenseManager(licenseManager);
//                manager.takeLicenseFromNetwork(uuid);
//                if (licenseManager.checkCachedLicense() > 0) {
//                    //授权成功
//                    mHandler.sendEmptyMessage(1);
//                } else {
//                    //授权失败
//                    mHandler.sendEmptyMessage(2);
//                }
//            }
//        }).start();
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    enterNextPage();
                    break;
                case 2:
                    showToastMsg("人脸验证授权失败");
                    //mButNext.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
