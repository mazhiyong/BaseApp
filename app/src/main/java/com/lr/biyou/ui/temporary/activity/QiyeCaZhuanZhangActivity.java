package com.lr.biyou.ui.temporary.activity;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mywidget.dialog.TipMsgDialog;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 企业证书转账界面
 */
public class QiyeCaZhuanZhangActivity extends BasicActivity implements RequestView {


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
    @BindView(R.id.qiye_receive_num_tv)
    TextView mQiyeReceiveNumTv;
    @BindView(R.id.qiye_receive_name_tv)
    TextView mQiyeReceiveNameTv;
    @BindView(R.id.qiye_receive_bank_tv)
    TextView mQiyeReceiveBankTv;
    @BindView(R.id.qiye_memo_tv)
    TextView mQiyeMemoTv;
    @BindView(R.id.qiye_pay_name_tv)
    TextView mQiyePayNameTv;
    @BindView(R.id.qiye_pay_num_tv)
    TextView mQiyePayNumTv;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    private String mRequestTag = "";

    private Map<String,Object> mPayInfo;

    @Override
    public int getContentView() {
        return R.layout.activity_qiye_ca_zhuanzhang;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.qiye_ca_money));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mPayInfo =(Map<String, Object>) bundle.getSerializable("DATA");
        }


        initValueTv();
    }

    /**
     * 检测是否已支付money  匹配来账信息
     */
    private void checkCa() {
        mRequestTag = MethodUrl.checkCa;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.checkCa, map);
    }
    /**
     * 获取证书信息
     */
    private void caInfo() {
        mRequestTag = MethodUrl.caConfig;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.caConfig, map);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }


    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        switch (mType) {
            case MethodUrl.checkCa:
                String s = tData.get("matchingsta")+"";
                if (s.equals("1")){//0未匹配，1已匹配

                    Intent intent = new Intent();
                    intent.setAction(MbsConstans.BroadcastReceiverAction.CAPAY_SUC);
                    sendBroadcast(intent);

                    String tip = "已支付费用，前往申请证书";
                    int dian ;
                    int end = tip.length();
                    if (tip.contains("，")) {
                        dian = tip.indexOf("，");
                    } else {
                        dian = tip.length();
                    }
                    SpannableString ss = new SpannableString(tip);
                    ss.setSpan(new TextSpanClick(false), dian, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.data_col)), 0, dian, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue1)), dian, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
                    showMsgDialog(ss,true);
                }else {
                    showMsgDialog("未查询到支付信息，请稍后重试",false);
                }
                mBtnSubmit.setEnabled(true);
                break;
            case MethodUrl.balanceAccount:

                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.checkCa:
                        checkCa();
                        break;
                    case MethodUrl.balanceAccount:
                        break;
                }
                break;

        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mBtnSubmit.setEnabled(true);
        dealFailInfo(map, mType);
    }

    private void initValueTv(){
        String receiveNum = mPayInfo.get("account")+"";
        receiveNum = UtilTools.getShowBankIdCard(receiveNum);

        String receiveName = mPayInfo.get("accountName")+"";

        String bankName = mPayInfo.get("branchName")+"";

        String payNum = mPayInfo.get("baseaccid")+"";
        payNum = UtilTools.getShowBankIdCard(payNum);
        String payName = mPayInfo.get("firmname")+"";


        mQiyeReceiveNumTv.setText(receiveNum);
        mQiyeReceiveNameTv.setText(receiveName);
        mQiyeReceiveBankTv.setText(bankName);
        mQiyeMemoTv.setText("证书支付费用");
        mQiyePayNameTv.setText(payName);
        mQiyePayNumTv.setText(payNum);
    }


    @OnClick({R.id.left_back_lay, R.id.btn_submit})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.btn_submit:
                mBtnSubmit.setEnabled(false);
                checkCa();
                /*int min=20;
                int max=22;
                Random random = new Random();
                int num = random.nextInt(max)%(max-min+1) + min;

                intent = new Intent(QiyeCaZhuanZhangActivity.this,QiyeCaResultActivity.class);
                intent.putExtra(MbsConstans.QiYeResultType.RESULT_KEY,num);
                startActivity(intent);*/
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            backTo(QiyeCaActivity.class,true);
        }
    }




    private TipMsgDialog mZhangDialog;
    private void showMsgDialog(Object msg,boolean isClose){
        mZhangDialog = new TipMsgDialog(this,true);
        mZhangDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                    dialog.dismiss();
                    if (isClose){
                        finish();
                    }
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
                        mZhangDialog.dismiss();
                        if (isClose){
                            finish();
                        }
                        break;
                    case R.id.confirm:
                        mZhangDialog.dismiss();
                        break;
                    case R.id.tv_right:
                        mZhangDialog.dismiss();
                        if (isClose){
                            finish();
                        }
                        break;
                }
            }
        };
        mZhangDialog.setCanceledOnTouchOutside(false);
        mZhangDialog.setCancelable(true);
        mZhangDialog.setOnClickListener(onClickListener);
        mZhangDialog.initValue("温馨提示",msg);
        mZhangDialog.show();
        mZhangDialog.tv_cancel.setText("确定");
    }

}
