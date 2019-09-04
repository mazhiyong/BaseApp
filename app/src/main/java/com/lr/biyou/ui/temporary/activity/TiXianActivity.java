package com.lr.biyou.ui.temporary.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.core.content.ContextCompat;

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
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.BankCardSelectDialog;
import com.lr.biyou.mywidget.dialog.TipMsgDialog;
import com.lr.biyou.mywidget.dialog.TradePassDialog;
import com.lr.biyou.utils.secret.RSAUtils;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
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
 *  提现
 */
public class TiXianActivity extends BasicActivity implements RequestView, SelectBackListener , TradePassDialog.PassFullListener {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.tv_card)
    TextView mCardText;
    @BindView(R.id.tv_tixin_maxmoney)
    TextView mMaxMoneyText;
    @BindView(R.id.iv_morecard)
    ImageView mCardArrowIamge;
    @BindView(R.id.but_next)
    Button mNextButton;
    @BindView(R.id.money_edit)
    EditText mMoneyEdit;
    @BindView(R.id.tixian_tip_tv)
    TextView mTixianTipTv;
    @BindView(R.id.tv_tips)
    TextView mTipText;
    @BindView(R.id.tv_toaccout_time)
    TextView mTimeText;
    @BindView(R.id.tv_yue_money)
    TextView mYueText;
    @BindView(R.id.ll_layout)
    LinearLayout mLayout2;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.rl_layout)
    RelativeLayout mRlLayout;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private List<Map<String, Object>> mDataList2 = new ArrayList<>();
    private String mRequestTag = "";
    private BankCardSelectDialog mDialog;
    int type;
    //合作方编号
    String patncode;

    private TradePassDialog mTradePassDialog;


    private Map<String,Object> mSetTiXianBank;


    private boolean mIsShow = false;

    private Map<String,Object> mTradeStateMap;

    private String mTradePass = "";

    private Map<String,Object> mCardConfig;

    @Override
    public int getContentView() {
        return R.layout.activity_ti_xian;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.tixian));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.BANKUPDATE_UPDATE);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.TRADE_PASS_UPDATE);
        registerReceiver(mBroadcastReceiver,intentFilter);

        UtilTools.setMoneyEdit(mMoneyEdit,0);
        type = getIntent().getIntExtra("TYPE", 0);

        mNextButton.setEnabled(false);
        //获取用户所有的银行卡
        bankCardInfoAction();
        getMoneyAction();
        tradePassState();

        String kind = MbsConstans.USER_MAP.get("firm_kind")+"";//客户类型（0：个人，1：企业）
        if (kind.equals("0")){//个人的情况下  添加提现卡  转成 添加快捷卡  然后选择快捷卡把这个快捷卡变成提现卡
            cardConfig();
        }
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
     * 请求资产信息（账号资金，可用资金，冻结资金）
     */
    private void getMoneyAction() {
        showProgressDialog();
        //??????
        mRequestTag = MethodUrl.allZichan;
        Map<String, String> map = new HashMap<>();
        map.put("qry_type","acct");
        map.put("accid","");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.allZichan, map);
    }


    /**
     获取 提现 银行卡列表
     */
    private void bankCardInfoAction() {
        showProgressDialog();
        mRequestTag = MethodUrl.bankCardList;
        Map<String, String> map = new HashMap<>();
        String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
        if (kind.equals("1")) {
            map.put("accsn", "1");
        } else {

        }

        map.put("accsn","1");
        map.put("isdefault","");
        Map<String, String> mHeadermap = new HashMap<>();
        mRequestPresenterImp.requestGetToRes(mHeadermap, MethodUrl.bankCardList, map);
    }

    /**
     * 是否设置交易密码
     */
    private void tradePassState() {
        mRequestTag = MethodUrl.isTradePass;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.isTradePass, map);
    }

    /**
     获取 个 人 银行卡列表
     */
    private void bankCardInfoAction2() {
        showProgressDialog();
        mRequestTag = MethodUrl.bankCardList2;
        Map<String, String> map = new HashMap<>();
        String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
        Map<String, String> mHeadermap = new HashMap<>();
        mRequestPresenterImp.requestGetToRes(mHeadermap, MethodUrl.bankCardList2, map);
    }

    private void setTiXianAction() {
        showProgressDialog();
        mRequestTag = MethodUrl.bindCard;
        Map<String, Object> map = new HashMap<>();


        map.put("accid", mSetTiXianBank.get("accid")+"");//银行卡号
        map.put("opnbnkid", mSetTiXianBank.get("opnbnkid")+"");//开户银行编号
        map.put("opnbnknm",mSetTiXianBank.get("opnbnknm")+"");//开户银行名称
        map.put("opnbnkwdcd", mSetTiXianBank.get("opnbnkwdcd")+"");//开户网点编号
        map.put("opnbnkwdnm", mSetTiXianBank.get("opnbnkwdnm")+"");//开户网点名称
//        map.put("wdprovcd", mSetTiXianBank.get("wdprovcd")+"");//开户网点地址-省份-编号
//        map.put("wdprovnm", mSetTiXianBank.get("wdprovnm")+"");//开户网点地址-省份-名称
//        map.put("wdcitycd",mSetTiXianBank.get("wdcitycd")+"");//开户网点地址-城市-编号
//        map.put("wdcitynm", mSetTiXianBank.get("wdcitynm")+"");//开户网点地址-城市-名称

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.bindCard, map);
    }


    private void buttonPress(){

        String tradeState = mTradeStateMap.get("trd_pwd_state")+"";
        if (tradeState.equals("0")){//交易密码状态（0：未设置，1：已设置）
            showMsgDialog("暂未设置交易密码，前往设置交易密码",false);
            mNextButton.setEnabled(true);
            return;
        }

        if (mSetTiXianBank == null || mSetTiXianBank.isEmpty()){
            mNextButton.setEnabled(true);
            showToastMsg("暂无可选的银行卡信息");
            return;
        }


        String money = mMoneyEdit.getText()+"";
        if (UtilTools.isEmpty(mMoneyEdit,"金额")){
            showToastMsg("金额不能为空");
            mNextButton.setEnabled(true);
            return;
        }

        if(!UtilTools.CheckMoneyValid(money)){
            showToastMsg("请输入有效金额");
            mNextButton.setEnabled(true);
            return;
        }
        showPassDialog();
        mNextButton.setEnabled(true);
    }


    private void submitData(){

        if (mSetTiXianBank == null || mSetTiXianBank.isEmpty()){
            showToastMsg("暂无可选的银行卡信息");
            mNextButton.setEnabled(true);
            return;
        }


        String money = mMoneyEdit.getText()+"";
        if (UtilTools.isEmpty(mMoneyEdit,"金额")){
            showToastMsg("金额不能为空");
            mNextButton.setEnabled(true);
            return;
        }

        if(!UtilTools.CheckMoneyValid(money)){
            showToastMsg("请输入有效金额");
            mNextButton.setEnabled(true);
            return;
        }
        mRequestTag = MethodUrl.tixianSubmit;
        Map<String, Object> map = new HashMap<>();
        map.put("amount", money);//银行卡号
        map.put("accid", mSetTiXianBank.get("accid")+"");//银行卡号
        map.put("trd_pwd", RSAUtils.encryptContent(mTradePass,RSAUtils.publicKey));//交易密码
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.tixianSubmit, map);
    }

    private void showPassDialog(){

        if (mTradePassDialog == null){
            mTradePassDialog = new TradePassDialog(this, true);
            mTradePassDialog.setPassFullListener(this);
            mTradePassDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
            mTradePassDialog.mPasswordEditText.setText(null);

            mTradePassDialog.mForgetPassTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMsgCodeAction();
                }
            });

        }else {
            mTradePassDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
            mTradePassDialog.mPasswordEditText.setText(null);

        }
    }

    private void getMsgCodeAction() {
        mRequestTag = MethodUrl.forgetTradePass;
        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        String tel = SPUtils.get(this,MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT,"")+"";
        map.put("tel", tel);//手机账号
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.forgetTradePass, map);
    }



    @Override
    protected void onResume(){
        super.onResume();

        if (mIsRefresh){
            //bankCardInfoAction();
            tradePassState();
        }
        mIsRefresh = false;
    }

    @OnClick({R.id.left_back_lay, R.id.back_img, R.id.title_text,
            R.id.but_next,R.id.rl_layout})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
                mNextButton.setEnabled(false);
                buttonPress();
                //mBackText.setEnabled(false);
               // submitData();
                break;
            case R.id.rl_layout:
                String kind = MbsConstans.USER_MAP.get("firm_kind")+"";//客户类型（0：个人，1：企业）
                if (kind.equals("1")){
                    //企业没有提现卡的时候  去绑定提现卡界面
                    if (mSetTiXianBank == null || mSetTiXianBank.isEmpty()){
                        intent = new Intent(this, BankTiXianModifyActivity.class);
                        intent.putExtra("backtype","20");
                        startActivity(intent);
                    }else {
                        //如果企业有提现卡的时候  弹出来选择
                        if (mDataList == null ){
                            mDataList = new ArrayList<>();
                        }
                        BankCardSelectDialog  mAddDialog = new BankCardSelectDialog(this, true,mDataList, 30);
                        mAddDialog.setSelectBackListener(this);
                        mAddDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()){
                                    case R.id.add_card_lay:
                                        Intent intent = new Intent(TiXianActivity.this, BankTiXianModifyActivity.class);
                                        intent.putExtra("backtype","20");
                                        startActivity(intent);
                                        mAddDialog.dismiss();
                                        break;
                                    case R.id.tv_cancel:
                                        mAddDialog.dismiss();
                                        break;
                                }
                            }
                        });
                        mAddDialog.showAtLocation(Gravity.BOTTOM,0,0);
                    }
                }else {
                    if (mDataList2 == null || mDataList2.size() == 0){
                        mIsShow = true;
                        //获取个人 银行卡列表
                        bankCardInfoAction2();
                    }else {
                        //dialog展示个人银行卡列表
                        showBankSelectPer();
                    }
                }
                break;
        }
    }


    private void showBankSelectPer(){

        int isShow = 200;

        if (mCardConfig == null || mCardConfig.isEmpty()) {
            cardConfig();
            return;
        } else {
            String fastSup = mCardConfig.get("fastSup") + "";
            //是否支持银商入金（0：不支持 1：支持）-暂不支持 不需要调试            bankSup
            //是否支持快捷入金（0：不支持 1：支持                   fastSup
            //是否支持跨行转账（0：不支持 1：支持）                 obankSup
            if (fastSup.equals("1")) {
                isShow = 200;
            }else {
                isShow = 30;
            }
        }


        mDialog = new BankCardSelectDialog(this, true, mDataList2, isShow);
        mDialog.setSelectBackListener(this);
        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.add_card_lay:
                        /*Intent intent = new Intent(TiXianActivity.this, BankTiXianModifyActivity.class);
                        intent.putExtra("backtype","20");
                        startActivity(intent);*/
                        Intent intent = new Intent(TiXianActivity.this, ChongZhiCardAddActivity.class);
                        intent.putExtra("backtype","110");
                        startActivity(intent);
                        mDialog.dismiss();
                        break;
                    case R.id.tv_cancel:
                        mDialog.dismiss();
                        break;
                }
            }
        });
        mDialog.showAtLocation(Gravity.BOTTOM,0,0);
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
        mNextButton.setEnabled(true);
        Intent intent;
        switch (mType) {
            case MethodUrl.supervisionConfig://{"obankSup":"0","bankSup":"1","fastSup":"0"}
                mCardConfig = tData;
                //是否支持银商入金（0：不支持 1：支持）-暂不支持 不需要调试            bankSup
                //是否支持快捷入金（0：不支持 1：支持                   fastSup
                //是否支持跨行转账（0：不支持 1：支持）                 obankSup
                if (mIsShow){
                    showBankSelectPer();
                    mIsShow = false;
                }
                break;
            case MethodUrl.forgetTradePass:
                intent = new Intent(TiXianActivity.this,CodeMsgActivity.class);
                String tel = SPUtils.get(this,MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT,"")+"";
                intent.putExtra("DATA",(Serializable) tData);
                intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_TRADE_PASS);
                intent.putExtra("showPhone", tel+"");
                startActivity(intent);
                mTradePassDialog.dismiss();
                break;
            //交易密码状态
            case MethodUrl.isTradePass:
                mNextButton.setEnabled(true);
                mTradeStateMap = tData;
                break;
            case MethodUrl.allZichan:
                if(tData!=null && !tData.isEmpty()){
                    String money = UtilTools.getRMBMoney(tData.get("bal_amt")+"");
                    String yue = UtilTools.getRMBMoney(tData.get("use_amt")+"");
                    String dongjie = UtilTools.getRMBMoney(tData.get("frz_amt")+"");
                    mTixianTipTv.setText("可提现余额"+yue);
                }
                break;
            case MethodUrl.tixianSubmit:
                mTradePassDialog.dismiss();
                dismissProgressDialog();
                sendBrodcast();
                intent = new Intent(TiXianActivity.this,FuKuanFinishActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("money", mMoneyEdit.getText()+"");
                intent.putExtra("accid",mSetTiXianBank.get("accid")+"");
                intent.putExtra("bankName",mSetTiXianBank.get("opnbnknm")+"");
                startActivity(intent);
                break;
            case  MethodUrl.bindCard:
                dismissProgressDialog();
                mCardText.setText(mSetTiXianBank.get("opnbnknm") + "(" + UtilTools.getIDCardXing(mSetTiXianBank.get("accid")+"")+ ")");
                break;
            case MethodUrl.bankCardList:
                dismissProgressDialog();
                String result = tData.get("result") + "";
                if (UtilTools.empty(result)) {
                    mCardText.setText("当前无可用的银行卡");
                } else {
                    LogUtilDebug.i("show", "银行卡列表：" + result);
                    mDataList = JSONUtil.getInstance().jsonToList(result);
                    if (mDataList != null && mDataList.size() > 0) {

                        for (Map<String,Object> map : mDataList){
                            String s = map.get("accsn")+"";
                            if (s.equals("1") || s.equals("3")){
                                mSetTiXianBank = map;
                            }
                        }
                        if (mSetTiXianBank != null && !mSetTiXianBank.isEmpty()){
                            mCardText.setText(mSetTiXianBank.get("opnbnknm")+ "(" + UtilTools.getIDCardXing(mSetTiXianBank.get("accid")+"")+ ")");
                        }else{
                            String kind = MbsConstans.USER_MAP.get("kind") + "";//客户类型（0：个人，1：企业）
                            if (kind.equals("1")) {
                                mCardText.setText("当前无可用的银行卡");
                            } else {
                                mCardText.setText("请选择");
                            }
                        }
                    } else {
                        mCardText.setText("当前无可用的银行卡");
                    }
                }
                break;
            case MethodUrl.bankCardList2:
                dismissProgressDialog();
                mDataList2 = JSONUtil.getInstance().jsonToList(tData.get("result") + "");

                if (mIsShow){
                    showBankSelectPer();
                    mIsShow = false;
                }
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken= false;
                showProgressDialog();
                for (String stag : mRequestTagList) {
                    switch (stag) {
                        case MethodUrl.isTradePass:
                            tradePassState();
                            break;
                        case MethodUrl.forgetTradePass:
                            getMsgCodeAction();
                            break;
                        case MethodUrl.bankCardList2:
                            bankCardInfoAction2();
                            break;
                        case MethodUrl.bankCardList:
                            bankCardInfoAction();
                            break;
                        case MethodUrl.tixianSubmit:
                            submitData();
                            break;
                        case MethodUrl.bindCard:
                            setTiXianAction();
                            break;
                        case MethodUrl.allZichan:
                            getMoneyAction();
                            break;
                        case MethodUrl.supervisionConfig:
                            cardConfig();
                            break;
                    }
                }
                mRequestTagList = new ArrayList<>();
                break;

        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mNextButton.setEnabled(true);
        dismissProgressDialog();
        switch (mType){
            case MethodUrl.tixianSubmit:
                //mTradePassDialog.dismiss();
                break;
        }
        dealFailInfo(map,mType);
    }

    //更新选择后的结果
    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        mSetTiXianBank = map;
        switch (type){
            case 30://不显示添加银行卡时候 点击返回的数据
                mSetTiXianBank = map;
                mCardText.setText(mSetTiXianBank.get("opnbnknm") + "(" + UtilTools.getIDCardXing(mSetTiXianBank.get("accid")+"")+ ")");
                break;
            case 200://显示 添加银行卡   点击返回的列表数据
                mSetTiXianBank = map;
                setTiXianAction();
                break;
        }

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.BANKUPDATE_UPDATE)){

                String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
                if (kind.equals("1")) {
                    bankCardInfoAction();
                } else {
                    bankCardInfoAction();
                    bankCardInfoAction2();
                }
            }else if (action.equals(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE)){
                finish();
            }else if (action.equals(MbsConstans.BroadcastReceiverAction.TRADE_PASS_UPDATE)){
                tradePassState();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void sendBrodcast(){
        Intent intent = new Intent();
        intent.setAction(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE);
        sendBroadcast(intent);
    }

    @Override
    public void onPassFullListener(String pass) {

        mTradePassDialog.mPasswordEditText.setText(null);
        mTradePass = pass;
        mNextButton.setEnabled(false);
        submitData();
    }




    private TipMsgDialog mZhangDialog;
    private void showMsgDialog(Object msg,boolean isClose){
        mZhangDialog = new TipMsgDialog(TiXianActivity.this,true);
        mZhangDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                    dialog.dismiss();
                    if (isClose){
                        //finish();
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
                        Intent intent = new Intent(TiXianActivity.this, ModifyOrderPassActivity.class);
                        intent.putExtra("BACK_TYPE","2");
                        intent.putExtra("TYPE","1");
                        startActivity(intent);
                        mZhangDialog.dismiss();
                        if (isClose){
                            //finish();
                        }
                        break;
                    case R.id.confirm:
                        mZhangDialog.dismiss();
                        break;
                    case R.id.tv_right:
                        mZhangDialog.dismiss();
                        if (isClose){
                            //finish();
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
        mZhangDialog.tv_right.setVisibility(View.VISIBLE);
        mZhangDialog.tv_cancel.setText("设置交易密码");
        mZhangDialog.tv_cancel.setTextColor(ContextCompat.getColor(TiXianActivity.this,R.color.black));
    }
}
