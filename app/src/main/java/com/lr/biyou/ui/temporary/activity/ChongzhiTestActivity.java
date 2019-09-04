package com.lr.biyou.ui.temporary.activity;

import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mywidget.dialog.BankCardSelectDialog;
import com.lr.biyou.mywidget.dialog.ZZTipMsgDialog;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值 校验(短信验证码)
 */
public class ChongzhiTestActivity extends BasicActivity implements SelectBackListener {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.tv_card)
    TextView mCardText;
    @BindView(R.id.tv_tixin_maxmoney)
    TextView mMaxMoneyText;
    @BindView(R.id.tv_money)
    EditText mMoneyText;
    @BindView(R.id.bt_chongzhi)
    Button   mChongzhiButton;
    @BindView(R.id.rl_layout)
    RelativeLayout mLayout;

    private String mRequestTag = "";
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private Map<String, Object> mSelectBank;


    private Map<String,Object> mCardConfig;


    @Override
    public int getContentView() {
        return R.layout.activity_chongzhi_test;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.chongzhi));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.BANKUPDATE_UPDATE);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE);
        registerReceiver(mBroadcastReceiver,intentFilter);

        UtilTools.setMoneyEdit(mMoneyText,0);

        bankCardInfoAction();
        cardConfig();

    }


    /**
     查询资金托管配置
     */
    private void cardConfig() {
        mRequestTag = MethodUrl.supervisionConfig;
        Map<String, String> map = new HashMap<>();
        map.put("accsn","A");
        Map<String, String> mHeadermap = new HashMap<>();
        mRequestPresenterImp.requestGetToMap(mHeadermap, MethodUrl.supervisionConfig, map);
    }
    /**
     获取用户银行卡列表
     */
    private void bankCardInfoAction() {
        mRequestTag = MethodUrl.bankCardList;
        Map<String, String> map = new HashMap<>();
        map.put("accsn","A");
        map.put("isdefault","");
        Map<String, String> mHeadermap = new HashMap<>();
        mRequestPresenterImp.requestGetToRes(mHeadermap, MethodUrl.bankCardList, map);
    }


    /**
     *  提交
     */
    private void submitData() {
        String money = mMoneyText.getText()+"";

        if (!UtilTools.CheckMoneyValid(money)){
            showToastMsg("请输入金额");
            mChongzhiButton.setEnabled(true);
            return;
        }
        mRequestTag = MethodUrl.chongzhiSubmit;
        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        map.put("amount",money);
        map.put("accid", mSelectBank.get("accid")+"");
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.chongzhiSubmit, map);
    }

    @OnClick({R.id.back_img,R.id.bt_chongzhi,R.id.left_back_lay,R.id.rl_layout})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.rl_layout:
                showDialog();
                break;
            case R.id.bt_chongzhi:
                mChongzhiButton.setEnabled(false);
                butPress();
                break;
        }
    }


    private void butPress(){
        if (mSelectBank  == null){
            mChongzhiButton.setEnabled(true);
            showToastMsg("暂无可选择的充值方式");
            return;
        }

        String accsn = mSelectBank.get("accsn")+"";
        //accsn   业务类型(1:提现账户;A充值卡<快捷支付>; D电子账户)
        switch (accsn){
            case "1":
                submitData();
                break;
            case "A":
                submitData();
                break;
            case "D"://电子账户直接转账
                //showBaseMsgDialog("请向"+mSelectBank.get("accid")+"转账",false);
                showTipMsgDialog(mSelectBank,false);
                mChongzhiButton.setEnabled(true);
                break;
        }
    }


    private BankCardSelectDialog mDialog;

    private void showDialog(){
        int mType = 200;

        //是否支持银商入金（0：不支持 1：支持）-暂不支持 不需要调试            bankSup
        //是否支持快捷入金（0：不支持 1：支持                   fastSup
        //是否支持跨行转账（0：不支持 1：支持）                 obankSup

        if (mCardConfig == null || mCardConfig.isEmpty()){
            mType = 30;
            if (mDataList == null || mDataList.size() == 0){
                showToastMsg("暂无数据可选择，请联系客服");
                return;
            }
        }else {
            String fastSup = mCardConfig.get("fastSup")+"";
            String obankSup = mCardConfig.get("obankSup")+"";
            LogUtilDebug.i("---------------",mCardConfig+"    "+fastSup+"    "+obankSup);
            if (fastSup.equals("0")){
                mType = 30;
                if (obankSup.equals("0")){
                    showToastMsg("不支持绑卡业务，请联系客服");
                    return;
                }else if (obankSup.equals("1") && (mDataList == null || mDataList.size() == 0)){
                    showToastMsg("数据信息异常，请联系客服");
                    return;
                }
            }
        }



        mDialog = new BankCardSelectDialog(this, true, mDataList, mType);
        mDialog.setSelectBackListener(this);
        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_card_lay:
                        Intent intent = new Intent(ChongzhiTestActivity.this, ChongZhiCardAddActivity.class);
                        intent.putExtra("backtype", "30");
                        startActivity(intent);
                        mDialog.dismiss();
                        break;
                    case R.id.tv_cancel:
                        mDialog.dismiss();
                        break;
                }
            }
        });
        mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }
    private void addOtherPay(){

        /*if (mDataList != null && mDataList.size()>0){
            for (Map<String,Object> map : mDataList){
                map.put("type","300");
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("type","11");
        map.put("opnbnknm","微信");
        map.put("accid","");
        mDataList.add(map);

        map = new HashMap<>();
        map.put("type","12");
        map.put("opnbnknm","支付宝");
        map.put("accid","");
        mDataList.add(map);*/
    }



    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {
        showProgressDialog();
    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {
        dismissProgressDialog();
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

        Intent intent;
        switch (mType){
            //是否支持银商入金（0：不支持 1：支持）-暂不支持 不需要调试            bankSup
            //是否支持快捷入金（0：不支持 1：支持                   fastSup
            //是否支持跨行转账（0：不支持 1：支持）                 obankSup
            case MethodUrl.supervisionConfig://{"obankSup":"0","bankSup":"1","fastSup":"0"}
                mCardConfig = tData;
                break;
            case MethodUrl.bankCardList://
                mCardText.setText("--");
                String result = tData.get("result") + "";
                if (UtilTools.empty(result)) {
                    //mCardText.setText("暂无可用的银行卡");
                } else {
                    mDataList = JSONUtil.getInstance().jsonToList(result);
                    if (mDataList != null && mDataList.size() > 0) {
                        mSelectBank = mDataList.get(0);
                       /* for (Map<String, Object> map : mDataList) {
                            String s = map.get("accsn") + "";
                            if (s.equals("2") || s.equals("3")) {
                                mSelectBank = map;
                                break;
                            }
                        }*/
                        if (mSelectBank != null && !mSelectBank.isEmpty()) {
                            String accsn = mSelectBank.get("accsn")+"";
                            String bankName = mSelectBank.get("opnbnknm")+"";
                            if (UtilTools.empty(bankName) ){
                                if (accsn.equals("D")){
                                    bankName = "交易账户";
                                }else {
                                    bankName = "";
                                }
                            }
                            mCardText.setText(bankName + "(" + UtilTools.getIDCardXing(mSelectBank.get("accid") + "") + ")");
                        } else {
                            mCardText.setText("暂无可用的银行卡");
                        }
                    } else {
                        mCardText.setText("暂无可用的银行卡");
                    }
                }

                addOtherPay();
                break;

            case MethodUrl.chongzhiSubmit:
                mChongzhiButton.setEnabled(true);
                String tt = tData.get("type")+"";//1：银商入金 2：快捷入金
                String serialno  = tData.get("serialno")+"";//快捷支付流水号
                LogUtilDebug.i("-----------------------------------------------------------",tData+"     "+tt);
                switch (tt){
                    case "1":
                        intent = new Intent(ChongzhiTestActivity.this,FuKuanFinishActivity.class);
                        intent.putExtra("type","1");
                        intent.putExtra("money",mMoneyText.getText()+"");
                        intent.putExtra("accid", mSelectBank.get("accid")+"");
                        intent.putExtra("bankName", mSelectBank.get("opnbnknm")+"");
                        startActivity(intent);
                        sendBrodcast();
                        finish();
                        break;
                    case "2":
                        showToastMsg("获取短信验证码成功");
                        intent = new Intent(this, CodeMsgActivity.class);
                        intent.putExtra(MbsConstans.CodeType.CODE_KEY,MbsConstans.CodeType.CODE_CHONGZHI_MONEY);
                        intent.putExtra("amount",mMoneyText.getText()+"");
                        intent.putExtra("accid", mSelectBank.get("accid")+"");
                        intent.putExtra("bankName", mSelectBank.get("opnbnknm")+"");
                        intent.putExtra("phone",mSelectBank.get("mobno")+"");
                        intent.putExtra("DATA",(Serializable)tData);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                for (String stag : mRequestTagList) {
                    switch (stag) {
                        case MethodUrl.bankCardList:
                            bankCardInfoAction();
                            break;
                        case MethodUrl.supervisionConfig:
                            cardConfig();
                            break;
                        case MethodUrl.chongzhiSubmit:
                            submitData();
                            break;
                    }
                }
                mRequestTagList = new ArrayList<>();
                break;
        }
    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mChongzhiButton.setEnabled(true);
        dealFailInfo(map,mType);
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type){
            case 30://不显示添加银行卡时候 点击返回的数据
                mSelectBank = map;
                break;
            case 200://显示 添加银行卡   点击返回的列表数据
                mSelectBank = map;
                break;
        }

        if (mSelectBank != null && !mSelectBank.isEmpty()) {
            String accsn = mSelectBank.get("accsn")+"";
            String bankName = mSelectBank.get("opnbnknm")+"";
            if (UtilTools.empty(bankName) ){
                if (accsn.equals("D")){
                    bankName = "交易账户";
                }else {
                    bankName = "";
                }
            }
            mCardText.setText(bankName + "(" + UtilTools.getIDCardXing(mSelectBank.get("accid") + "") + ")");
            //accsn   业务类型(1:提现账户;A充值卡<快捷支付>; D电子账户)
            switch (accsn){
                case "1":
                    break;
                case "A":
                    break;
                case "D"://电子账户直接转账
                    //showBaseMsgDialog("请向"+mSelectBank.get("accid")+"转账",false);
                    showTipMsgDialog(mSelectBank,false);
                    mChongzhiButton.setEnabled(true);
                    break;
            }
        }
    }
    public ZZTipMsgDialog mZhangDialog;
    private void showTipMsgDialog(Map<String,Object> map,boolean isClose) {
        if (mZhangDialog == null){
            mZhangDialog = new ZZTipMsgDialog(this,true);
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
                        case R.id.sure:
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
        }
        mZhangDialog.initValue(map);
        mZhangDialog.show();

        mZhangDialog.mNameTv.setText(MbsConstans.USER_MAP.get("comname") + "");

    }


    private void sendBrodcast(){
        Intent intent = new Intent();
        intent.setAction(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE);
        sendBroadcast(intent);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.BANKUPDATE_UPDATE)){
                bankCardInfoAction();
            }else if (action.equals(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE)){
                finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
