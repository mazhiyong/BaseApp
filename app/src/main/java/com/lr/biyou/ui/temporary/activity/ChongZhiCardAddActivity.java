package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.AddressSelectDialog;
import com.lr.biyou.mywidget.view.BankCardTextWatcher;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.RegexUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加新的充值卡信息
 */
public class ChongZhiCardAddActivity extends BasicActivity implements RequestView, SelectBackListener {
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
    @BindView(R.id.bank_card_edit)
    EditText mBankCardEdit;
    @BindView(R.id.bank_name_tv)
    TextView mBankNameTv;
    @BindView(R.id.kaihu_bank_add_edit)
    EditText mKaihuBankAddEdit;
    @BindView(R.id.bank_card_phone_edit)
    EditText mBankCardPhoneEdit;
    @BindView(R.id.but_next)
    Button mButNext;

    @BindView(R.id.kaihuhang_lay)
    LinearLayout mKaiHuHangLay;
    @BindView(R.id.bank_dian_value_tv)
    TextView mBankDianValueTv;
    @BindView(R.id.bank_dian_lay)
    CardView mBankDianLay;
    @BindView(R.id.card_user_tv)
    TextView mCardUserTv;
    @BindView(R.id.card_num_tv)
    TextView mCardNumTv;
    @BindView(R.id.tip_name_tv)
    TextView mTipNameTv;
    @BindView(R.id.tip_num_tv)
    TextView mTipNumTv;
    @BindView(R.id.tv_bank_type)
    TextView mBankTypTv;
    @BindView(R.id.ll_view)
    View mView;
    @BindView(R.id.bank_card_account_edit)
    EditText mAccountEdit;


    private boolean mIsShow = false;

    private String mRequestTag = "";

    private Map<String, Object> mBankMap;
    private Map<String, Object> mBankWDMap;
    private Map<String, Object> mAddressMap;


    private AddressSelectDialog mAddressSelectDialog;

    private String mBackType = "";

    @Override
    public int getContentView() {
        return R.layout.activity_chongzhi_add;
    }

    @Override
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.bank_card_bind_title));

        String type = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
        if (type.equals("1")) {
            mAccountEdit.setVisibility(View.GONE);
            mCardUserTv.setVisibility(View.VISIBLE);
            mBankCardEdit.setHint("请输入您的银行卡号");
            mBankTypTv.setText("银行卡号");
        } else {
            mAccountEdit.setVisibility(View.GONE);
            mCardUserTv.setVisibility(View.VISIBLE);
            mBankCardEdit.setHint("请输入您的银行卡号");
            mBankTypTv.setText("银行卡号");
        }

        Intent intentData = getIntent();
        Bundle bundle = intentData.getExtras();
        if (bundle != null) {
            mBackType = bundle.getString("backtype");
        }

        if (MbsConstans.USER_MAP == null || MbsConstans.USER_MAP.isEmpty()) {
            showToastMsg("用户信息获取失败，请重新登录");
            closeActivity();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        } else {
            String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
            if (kind.equals("1")) {
                //mTipNameTv.setText("企业名称");
                mTipNumTv.setText("营业执照号");

                LogUtilDebug.i("--------------------",MbsConstans.USER_MAP);
                mCardUserTv.setText(MbsConstans.USER_MAP.get("name") + "");
                mCardNumTv.setText(MbsConstans.USER_MAP.get("clno") + "");
            } else {
                //mTipNameTv.setText("姓名");
                mTipNumTv.setText("身份证号");
                mCardUserTv.setText(MbsConstans.USER_MAP.get("name") + "");
                mCardNumTv.setText("");
            }
        }

        //键盘显示监听
        mBankCardEdit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                final Rect rect = new Rect();
                ChongZhiCardAddActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                final int screenHeight = ChongZhiCardAddActivity.this.getWindow().getDecorView().getRootView().getHeight();
                Log.e("TAG", rect.bottom + "#" + screenHeight);
                final int heightDifference = screenHeight - rect.bottom;
                boolean visible = heightDifference > screenHeight / 3;
                if (visible) {
                    mIsShow = true;
                } else {
                    if (mIsShow && mBankCardEdit.hasFocus()) {
                        String cardNum = mBankCardEdit.getText() + "";
                        cardNum = cardNum.replaceAll(" ", "");
                        if (!UtilTools.empty(cardNum)) {
                            if (RegexUtil.isBankCard(cardNum)) {
                                checkBankCard();
                            }
                        }
                    }
                    mIsShow = false;
                }
            }
        });
        BankCardTextWatcher.bind(mBankCardEdit);



        mBankCardEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBankNameTv.setText("");
                mBankDianValueTv.setText("");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void checkBankCard() {
        String num = mBankCardEdit.getText() + "";

        num = num.replaceAll(" ", "");
        if (UtilTools.empty(num)){
            showToastMsg("请输入银行卡号");
            return;
        }
        mRequestTag = MethodUrl.checkBankCard;
        Map<String, String> map = new HashMap<>();
        map.put("accid",num + "");
        map.put("scene", "ot");//使用场景（wd：提现卡查询使用 ot：其他场景使用）
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.checkBankCard, map);
    }


    private String cardNum = "";
    private String bankName = "";
    private String phoneNum = "";

    private void cardSubmit() {

        cardNum = mBankCardEdit.getText() + "";
        bankName = mBankNameTv.getText() + "";
        phoneNum = mBankCardPhoneEdit.getText() + "";
        cardNum = cardNum.replaceAll(" ", "");

        if (UtilTools.isEmpty(mBankCardEdit, "银行卡号")) {
            showToastMsg("银行卡号不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (!RegexUtil.isBankCard(cardNum)) {
            showToastMsg("银行卡号格式不正确");
            mButNext.setEnabled(true);
            return;
        }

        if (UtilTools.isEmpty(mBankNameTv, "开户银行")) {
            showToastMsg("开户银行不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mBankDianValueTv, "开户网点")) {
            showToastMsg("开户网点不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (mBankWDMap == null || mBankWDMap.isEmpty()) {
            showToastMsg("开户网点不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mBankCardPhoneEdit, "手机号码")) {
            showToastMsg("手机号码不能为空");
            mButNext.setEnabled(true);
            return;
        }

        if (!RegexUtil.isPhone(phoneNum)) {
            showToastMsg("手机号码格式不正确");
            mButNext.setEnabled(true);
            return;
        }

        getMsgCodeAction();
    }

    private void getMsgCodeAction() {
        mRequestTag = MethodUrl.bankCardSms;
        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        map.put("accid", cardNum);//卡号
        map.put("mobno", phoneNum);//银行预留手机号
        map.put("opnbnkid", mBankMap.get("opnbnkid") + "");//开户银行编号
        map.put("opnbnknm", bankName);//开户银行名称

        map.put("opnbnkwdcd", mBankWDMap.get("opnbnkwdcd") + "");//开户网点编号
        map.put("opnbnkwdnm", mBankWDMap.get("opnbnkwdnm") + "");//开户网点名称

        map.put("wdprovcd", mAddressMap.get("procode") + "");//开户网点地址-省份-编号
        map.put("wdprovnm", mAddressMap.get("proname") + "");//开户网点地址-省份-名称
        map.put("wdcitycd", mAddressMap.get("citycode") + "");//开户网点地址-城市-编号
        map.put("wdcitynm", mAddressMap.get("cityname") + "");//开户网点地址-城市-名称
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.bankCardSms, map);
    }


    @OnClick({R.id.back_img, R.id.but_next, R.id.kaihuhang_lay, R.id.left_back_lay, R.id.bank_dian_lay})
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
                mButNext.setEnabled(false);
                cardSubmit();
                break;
            case R.id.kaihuhang_lay:

                checkBankCard();
                /*intent = new Intent(ChongZhiCardAddActivity.this, BankNameListActivity.class);
                startActivityForResult(intent, 100);*/
                break;
            case R.id.bank_dian_lay:
                if (mBankMap == null) {
                    showToastMsg("银行卡信息不正确，请核实");
                } else {
                    intent = new Intent(ChongZhiCardAddActivity.this, ChoseBankAddActivity.class);
                    intent.putExtra("bankid", mBankMap.get("opnbnkid") + "");
                    startActivityForResult(intent, 100);
                   /* mAddressSelectDialog = new AddressSelectDialog(this, true, "选择地址", 11);
                    mAddressSelectDialog.setSelectBackListener(this);
                    mAddressSelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);*/
                }

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
        mButNext.setEnabled(true);
        switch (mType) {
            case MethodUrl.bankCardSms:
                showToastMsg(getResources().getString(R.string.get_msg_code_tip));
                Intent intent = new Intent(ChongZhiCardAddActivity.this, CodeMsgActivity.class);
                intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_CARD_CHONGZHI);
                intent.putExtra("accid", cardNum);//卡号
                intent.putExtra("mobno", phoneNum);//银行预留手机号
                intent.putExtra("opnbnkid", mBankMap.get("opnbnkid") + "");//开户银行编号
                intent.putExtra("opnbnknm", bankName);//开户银行名称

                intent.putExtra("opnbnkwdcd", mBankWDMap.get("opnbnkwdcd") + "");//开户网点编号
                intent.putExtra("opnbnkwdnm", mBankWDMap.get("opnbnkwdnm") + "");//开户网点名称
                //{name=北京市北京市, proname=北京市, citycode=110100, procode=110000, cityname=北京市}
                // {opnbnkwdcd=310100000149, opnbnkwdnm=上海浦东发展银行北京西直门支行}
                intent.putExtra("wdprovcd", mAddressMap.get("procode") + "");//开户网点地址-省份-编号
                intent.putExtra("wdprovnm", mAddressMap.get("proname") + "");//开户网点地址-省份-名称
                intent.putExtra("wdcitycd", mAddressMap.get("citycode") + "");//开户网点地址-城市-编号
                intent.putExtra("wdcitynm", mAddressMap.get("cityname") + "");//开户网点地址-城市-名称

                intent.putExtra("backtype", mBackType);

                intent.putExtra("DATA", (Serializable) tData);
                startActivity(intent);
                break;
            case MethodUrl.checkBankCard://
                mBankMap = tData;
                mBankNameTv.setText(mBankMap.get("opnbnknm") + "");
                mKaiHuHangLay.setVisibility(View.VISIBLE);
                mView.setVisibility(View.VISIBLE);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.checkBankCard:
                        checkBankCard();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mButNext.setEnabled(true);
        dealFailInfo(map, mType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case 100:
                        bundle = data.getExtras();
                        if (bundle != null) {
                            mBankWDMap = (Map<String, Object>) bundle.getSerializable("DATA");
                            mAddressMap = (Map<String, Object>) bundle.getSerializable("DATA2");
                            mBankDianValueTv.setText(mBankWDMap.get("opnbnkwdnm") + "");
                            mBankDianValueTv.setError(null, null);
                        }

                        LogUtilDebug.i("-------------------------------",mAddressMap+"           "+mBankWDMap);
                        break;
                    case 300:
                        bundle = data.getExtras();
                        if (bundle != null) {
                            mBankMap = (Map<String, Object>) bundle.getSerializable("DATA");
                            //mBankMap.put("bankid", mBankMap.get("opnbnkid") + "");
                            //mBankMap.put("bankname", mBankMap.get("opnbnknm") + "");
                            //mBankMap.put("logopath", mBankMap.get("logopath") + "");
                            mBankNameTv.setText(mBankMap.get("opnbnknm") + "");
                            mBankNameTv.setError(null, null);
                        }
                }
                break;
        }
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        Intent intent;
        switch (type) {
            case 11:
                intent = new Intent(ChongZhiCardAddActivity.this, BankWdActivity.class);
                intent.putExtra("bankid", mBankMap.get("opnbnkid") + "");
                intent.putExtra("citycode", map.get("citycode") + "");
                startActivityForResult(intent, 100);
                break;
        }
    }

}
