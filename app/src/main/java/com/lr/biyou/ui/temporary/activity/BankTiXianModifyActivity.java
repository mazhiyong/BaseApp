package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;

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
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.AddressSelectDialog;
import com.lr.biyou.mywidget.view.BankCardTextWatcher;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.tool.RegexUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改，变更提现卡   界面
 */
public class BankTiXianModifyActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.kaihu_arrow_view)
    ImageView mKaihuArrwoView;
    @BindView(R.id.bank_tip_tv)
    TextView mBankTipTv;

    private Map<String,Object> mAddressMap;

    private boolean mIsShow = false;

    private String mRequestTag = "";

    private Map<String, Object> mBankMap;
    private Map<String, Object> mBankWDMap;
    private AddressSelectDialog mAddressSelectDialog;

    private Map<String,Object> mHezuoMap ;

    private String mBackType = "";

    @Override
    public int getContentView() {
        return R.layout.activity_bank_tixian_modify;
    }

    @Override
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.bind_tixian_title));

        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();
        if (bundle != null) {
            mHezuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
            mBackType = bundle.getString("backtype")+"";
        }
        if (MbsConstans.USER_MAP == null || MbsConstans.USER_MAP.isEmpty()) {
            showToastMsg("用户信息获取失败，请重新登录");
            closeActivity();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            return;
        } else { ///{auth=1, firm_kind=1, head_pic=default, name=阿里巴巴, tel=158****9191, idno=410725****3616, cmpl_info=1}
            String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
            if (kind.equals("1")) {
                mCardUserTv.setText(MbsConstans.USER_MAP.get("comname") + "");
                mKaihuArrwoView.setVisibility(View.VISIBLE);
                mKaiHuHangLay.setEnabled(true);
                mBankNameTv.setHint("请选择");
                mBankTipTv.setText(getResources().getString(R.string.bank_card_title));
                mBankCardEdit.setHint(R.string.hint_bank_zhanghu);
            } else {
                mCardUserTv.setText(MbsConstans.USER_MAP.get("name") + "");
                mKaihuArrwoView.setVisibility(View.GONE);
                mKaiHuHangLay.setEnabled(true);
                mBankNameTv.setHint("点击校验");
                mBankTipTv.setText(getResources().getString(R.string.bank_card_title2));
                mBankCardEdit.setHint(R.string.bank_card_edit_tip);

                //键盘显示监听
                mBankCardEdit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    //当键盘弹出隐藏的时候会 调用此方法。
                    @Override
                    public void onGlobalLayout() {
                        final Rect rect = new Rect();
                        BankTiXianModifyActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                        final int screenHeight = BankTiXianModifyActivity.this.getWindow().getDecorView().getRootView().getHeight();
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
                                    }else {
                                        showToastMsg("请输入合法的银行卡号");
                                    }
                                }else {
                                    showToastMsg("银行账户不能为空");
                                }
                            }
                            mIsShow = false;
                        }
                    }
                });

            }
        }

        //mBankCardEdit.setText("6210812430032354652");
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
        mRequestPresenterImp = new RequestPresenterImp(this, this);
        String num = mBankCardEdit.getText() + "";
        num = num.replaceAll(" ","");
        if (UtilTools.empty(num)){
            showToastMsg("请输入银行卡号");
            return;
        }
        mRequestTag = MethodUrl.checkBankCard;
        Map<String, String> map = new HashMap<>();
        map.put("accid", num);
        map.put("scene", "wd");
        //map.put("patncode", mHezuoMap.get("patncode")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.checkBankCard, map);
    }


    private void cardSubmit() {

        String cardNum = mBankCardEdit.getText() + "";
        cardNum = cardNum.replaceAll(" ", "");


        String cardTip = "银行卡号";
        String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
        if (kind.equals("1")) {
            cardTip = getResources().getString(R.string.bank_card_title);
        }else {
            cardTip = getResources().getString(R.string.bank_card_title2);
        }

        if (UtilTools.isEmpty(mBankCardEdit, cardTip)) {
            showToastMsg(cardTip+"不能为空");
            mButNext.setEnabled(true);
            return;
        }

        if (kind.equals("1")) {
            if (!RegexUtil.isGongCard(cardNum)) {
                showToastMsg(cardTip+"格式不正确");
                mButNext.setEnabled(true);
                return;
            }
        }else {
            if (!RegexUtil.isBankCard(cardNum)) {
                showToastMsg(cardTip+"格式不正确");
                mButNext.setEnabled(true);
                return;
            }
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
        if (mBankWDMap == null || mBankWDMap.isEmpty()){
            showToastMsg("开户网点不能为空");
            mButNext.setEnabled(true);
            return;
        }


        mRequestPresenterImp = new RequestPresenterImp(this, this);
        mRequestTag = MethodUrl.bindCard;
        Map<String, Object> map = new HashMap<>();
        map.put("accid", cardNum);//银行卡号
        map.put("opnbnkid", mBankMap.get("opnbnkid")+"");//开户银行编号
        map.put("opnbnknm",mBankMap.get("opnbnknm")+"");//开户银行名称
        map.put("opnbnkwdcd", mBankWDMap.get("opnbnkwdcd")+"");//开户网点编号
        map.put("opnbnkwdnm", mBankWDMap.get("opnbnkwdnm")+"");//开户网点名称
//        map.put("wdprovcd", mAddressMap.get("procode")+"");//开户网点地址-省份-编号
//        map.put("wdprovnm", mAddressMap.get("proname")+"");//开户网点地址-省份-名称
//        map.put("wdcitycd",mAddressMap.get("citycode")+"");//开户网点地址-城市-编号
//        map.put("wdcitynm", mAddressMap.get("cityname")+"");//开户网点地址-城市-名称

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.bindCard, map);
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
                String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
                if (kind.equals("1")) {
                    intent = new Intent(BankTiXianModifyActivity.this, BankNameListActivity.class);
                    startActivityForResult(intent, 30);
                }else {
                    checkBankCard();
                }
                break;
            case R.id.bank_dian_lay:
                if (mBankMap == null) {
                    showToastMsg("银行卡信息不正确，请核实");
                } else {
                    intent = new Intent(BankTiXianModifyActivity.this,ChoseBankAddActivity.class);
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
            case MethodUrl.checkBankCard://
                mBankMap = tData;
                mBankNameTv.setText(mBankMap.get("opnbnknm")+"");
                break;
            case MethodUrl.bindCard:
                showToastMsg(tData.get("result")+"");
                sendBrodcast2();
                if (mBackType.equals("20")){
                    finish();
                }else {
                    backTo(MainActivity.class,true);
                }
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
        dealFailInfo(map,mType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle;
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case 30:
                        bundle = data.getExtras();
                        if (bundle != null){
                            mBankMap = (Map<String, Object>) bundle.getSerializable("DATA");
                            mBankNameTv.setText(mBankMap.get("opnbnknm")+"");
                            mBankNameTv.setError(null,null);
                        }
                        break;
                    case 100:
                        bundle = data.getExtras();
                        if (bundle != null) {
                            mBankWDMap = (Map<String, Object>) bundle.getSerializable("DATA");
                            mAddressMap = (Map<String, Object>) bundle.getSerializable("DATA2");
                            mBankDianValueTv.setText(mBankWDMap.get("opnbnkwdnm") + "");
                            mBankDianValueTv.setError(null, null);
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        Intent intent;
        switch (type) {
            case 11:
                mAddressMap = map;
                intent = new Intent(BankTiXianModifyActivity.this, BankWdActivity.class);
                intent.putExtra("bankid", mBankMap.get("opnbnkid") + "");
                intent.putExtra("citycode", map.get("citycode") + "");
                startActivityForResult(intent, 100);
                break;
        }
    }

    private void sendBrodcast2(){
        Intent intent = new Intent();
        intent.setAction(MbsConstans.BroadcastReceiverAction.BANKUPDATE_UPDATE);
        sendBroadcast(intent);
    }
}
