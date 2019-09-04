package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.RegexUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.mywidget.view.BankCardTextWatcher;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开户  界面
 */
public class BankOpenActivity extends BasicActivity implements RequestView{

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
    @BindView(R.id.open_bank_tips)
    TextView mOpenBankTipsTv;
    @BindView(R.id.jiaoyan)
    TextView mJiaoYanTv;

    @BindView(R.id.kaihuhang_lay)
    LinearLayout mKaiHuHangLay;
    @BindView(R.id.kaihuhang_line)
    View mKaiHuHangLine;


    private boolean mIsShow = false;

    private String mRequestTag = "";

    private Map<String,Object> mBankMap;

    private Map<String,Object> mHeZuoMap;


    @Override
    public int getContentView() {
        return R.layout.activity_bank_open;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.bank_card_open_title));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.OPEN_BANK);
        registerReceiver(mBroadcastReceiver,intentFilter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mHeZuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        /*{
            "patncode": "13065230",
                "vaccid": "1834913000002786",
                "secstatus": "3",
                "accid": "6235559020000001338",
                "zifangnme": "天津分行",
                "zifangbho": "043362"
        }*/
      /*  mHeZuoMap = new HashMap<>();
        mHeZuoMap.put("patncode","13065230");
        mHeZuoMap.put("vaccid","1834913000002786");
        mHeZuoMap.put("secstatus","");
        mHeZuoMap.put("accid","6235559020000001338");
        mHeZuoMap.put("zifangnme","天津分行");
        mHeZuoMap.put("zifangbho","043362");
*/
      if (MbsConstans.USER_MAP == null){
            showToastMsg(getResources().getString(R.string.exception_info));
            finish();
            return;
        }

        String ss = MbsConstans.USER_MAP.get("name")+"";
        ss = ss.substring(1,ss.length());
        mOpenBankTipsTv.setText("请填写*"+ss+"的银行卡信息");

        //键盘显示监听
        mBankCardEdit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout () {
                final Rect rect = new Rect();
                BankOpenActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                final int screenHeight = BankOpenActivity.this.getWindow().getDecorView().getRootView().getHeight();
                Log.e("TAG", rect.bottom + "#" + screenHeight);
                final int heightDifference = screenHeight - rect.bottom;
                boolean visible = heightDifference > screenHeight / 3;
                if (visible) {
                    mIsShow = true;
                } else {
                    if (mIsShow && mBankCardEdit.hasFocus()) {
                        String cardNum = mBankCardEdit.getText()+"";
                        cardNum = cardNum.replaceAll(" ","");
                        if (!UtilTools.empty(cardNum)){
                            if (RegexUtil.isBankCard(cardNum)){
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
                mBankNameTv.setHint(getResources().getString(R.string.bank_jiaoyan));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //mKaiHuHangLay.setEnabled(false);
        //mKaiHuHangLay.setVisibility(View.GONE);
        //mKaiHuHangLine.setVisibility(View.GONE);


        mButNext.setEnabled(false);
    }

    private void checkBankCard(){

        String num = mBankCardEdit.getText()+"";
        num = num.replaceAll(" ","");
        if (UtilTools.empty(num)){
            showToastMsg("请输入银行卡号");
            return;
        }
        mRequestTag = MethodUrl.checkBankCard;
        Map<String, String> map = new HashMap<>();
        map.put("accid",num+"");
        map.put("ptncode",mHeZuoMap.get("patncode")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.checkBankCard, map);
    }


    private  void  cardSubmit(){
        String cardNum = mBankCardEdit.getText()+"";
        String bankName = mBankNameTv.getText()+"";
        String phoneNum = mBankCardPhoneEdit.getText()+"";
        cardNum = cardNum.replaceAll(" ","");

        if (UtilTools.isEmpty(mBankCardEdit,"银行卡号")){
            showToastMsg("银行卡号不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (!RegexUtil.isBankCard(cardNum)){
            showToastMsg("银行卡格式不正确");
            mButNext.setEnabled(true);
            return;
        }

        if (UtilTools.empty(bankName)){
            showToastMsg("开户银行不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mBankCardPhoneEdit,"手机号码")){
            showToastMsg("手机号码不能为空");
            mButNext.setEnabled(true);
            return;
        }

        if (!RegexUtil.isPhone(phoneNum)){
            showToastMsg("手机号码格式不正确");
            mButNext.setEnabled(true);
            return;
        }



        mRequestTag = MethodUrl.bankFourCheck;
        Map<String, String> map = new HashMap<>();
        map.put("accno",cardNum+"");
        map.put("patncode",mHeZuoMap.get("patncode")+"");
        map.put("phone",phoneNum+"");
        map.put("bankid",mBankMap.get("bankid")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.bankFourCheck, map);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.OPEN_BANK)){
                finish();
            }
        }
    };

    @OnClick({R.id.back_img, R.id.but_next,R.id.kaihuhang_lay,R.id.left_back_lay,R.id.jiaoyan})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.jiaoyan:
                checkBankCard();
                break;
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
               /* intent = new Intent(BankOpenActivity.this,BankNameListActivity.class);
                startActivityForResult(intent,100);*/
                checkBankCard();
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
        switch (mType){
            case MethodUrl.bankFourCheck:
                mButNext.setEnabled(true);
                showToastMsg(tData.get("result")+"");
                Intent intent = new Intent(BankOpenActivity.this,BankOpenXieyiActivity.class);
                String cardNum = mBankCardEdit.getText()+"";
                cardNum = cardNum.replaceAll(" ","");
                intent.putExtra("accno",cardNum+"");//银行卡
                intent.putExtra("patncode",mHeZuoMap.get("patncode")+"");//合作方
                intent.putExtra("opnbnknm",mBankMap.get("bankname")+""+"");//开户行名称
                intent.putExtra("opnbnkid",mBankMap.get("bankid")+"");//开户行编号
                intent.putExtra("logopath",mBankMap.get("logopath")+"");//银行头像
                startActivity(intent);
                break;
            case MethodUrl.checkBankCard://{"bank_same":"0","bankid":"305","accid":"6226203000645932","bankname":"民生银行","card_type":"1"}

                mBankMap = tData;
                mKaiHuHangLay.setVisibility(View.VISIBLE);
                mKaiHuHangLine.setVisibility(View.VISIBLE);
                mBankNameTv.setText(mBankMap.get("bankname")+"");

                mJiaoYanTv.setVisibility(View.GONE);

                String sameType = mBankMap.get("bank_same")+"";
                String cardType = mBankMap.get("card_type")+"";
                if (cardType.equals("1")){
                    if (sameType.equals("0")){
                        //showToastMsg("此卡不是本行银行卡，请更换银行卡");
                    }else {

                    }
                    mButNext.setEnabled(true);
                }else {
                    showToastMsg("此卡为信用卡，请填写储蓄卡信息");
                    mButNext.setEnabled(false);
                }

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.checkBankCard:
                        checkBankCard();
                        break;
                    case MethodUrl.bankFourCheck:
                        cardSubmit();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.bankFourCheck:
                mButNext.setEnabled(true);
                break;
        }
        dealFailInfo(map,mType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle;
        switch (resultCode){
            case RESULT_OK:
                switch (requestCode){
                    case 100:
                        bundle = data.getExtras();
                        if (bundle != null){
                            mBankMap = (Map<String, Object>) bundle.getSerializable("DATA");
                            mBankNameTv.setText(mBankMap.get("opnbnknm")+"");
                            mBankNameTv.setError(null,null);
                            mButNext.setEnabled(true);
                        }
                        break;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
