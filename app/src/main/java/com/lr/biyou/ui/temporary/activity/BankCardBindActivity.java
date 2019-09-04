package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定银行卡  界面
 */
public class BankCardBindActivity extends BasicActivity implements RequestView{

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
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.card_num_tv)
    TextView mCardNumTv;
    @BindView(R.id.open_bank_tv)
    TextView mOpenBankTv;
    @BindView(R.id.bank_card_value_tv)
    EditText mBankCardValueTv;
    @BindView(R.id.edit_parent_lay)
    LinearLayout mEditLay;
    @BindView(R.id.root_lay)
    RelativeLayout root_lay;

    private boolean mIsShow = false;

    private String mRequestTag = "";

    private String mPatncode = "";

    private Map<String,Object> mBankMap;
    private Map<String,Object> mOldBankMap;

    @Override
    public int getContentView() {
        return R.layout.activity_bank_bind;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            if (bundle.containsKey("patncode")){
                mPatncode = bundle.getString("patncode");
            }else if(bundle.containsKey("DATA")){
                mOldBankMap = (Map<String, Object>) bundle.getSerializable("DATA");
            }
        }


        mTitleText.setText(getResources().getString(R.string.bank_card_open_title));
        mNameTv.setText(MbsConstans.USER_MAP.get("name")+"");
        mCardNumTv.setText(MbsConstans.USER_MAP.get("idno")+"");

        if (mOldBankMap != null){
            mBankCardValueTv.setText("****************"+mOldBankMap.get("accid"));
            mOpenBankTv.setText(mOldBankMap.get("bankname")+"");
            mPatncode = mOldBankMap.get("patncode")+"";
        }

        //键盘显示监听
        mEditLay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    //当键盘弹出隐藏的时候会 调用此方法。
                    @Override
                    public void onGlobalLayout () {
                        final Rect rect = new Rect();
                        BankCardBindActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                        final int screenHeight = BankCardBindActivity.this.getWindow().getDecorView().getRootView().getHeight();
                        Log.e("TAG", rect.bottom + "#" + screenHeight);
                        final int heightDifference = screenHeight - rect.bottom;
                        boolean visible = heightDifference > screenHeight / 3;
                        if (visible) {
                            showToastMsg("软键盘弹出");
                            mIsShow = true;
                        } else {
                            if(mIsShow){
                                checkBankCard();
                            }
                            showToastMsg("软键盘隐藏");
                            mIsShow = false;
                        }
                    }
                });
    }

    private void checkBankCard(){

        String num = mBankCardValueTv.getText()+"";
        mRequestTag = MethodUrl.checkBankCard;
        Map<String, String> map = new HashMap<>();
        map.put("accid","6226203000645932");
        map.put("ptncode",mPatncode);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.checkBankCard, map);
    }
    private void setBankCard(){
        String num = mBankCardValueTv.getText()+"";
        mRequestTag = MethodUrl.bankCard;
        Map<String, Object> map = new HashMap<>();
        map.put("accid","6226203000645932");
        map.put("ptncode",mPatncode);
        map.put("bankid",mBankMap.get("bankid")+"");
        map.put("bankname",mBankMap.get("bankname")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.bankCard, map);
    }

    @OnClick({R.id.back_img, R.id.but_next,R.id.left_back_lay})
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
                intent = new Intent(BankCardBindActivity.this, BankCardActivity.class);
                startActivity(intent);
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
            case MethodUrl.checkBankCard://{"bank_same":"0","bankid":"305","accid":"6226203000645932","bankname":"民生银行","card_type":"1"}
                mBankMap = tData;
                String sameType = mBankMap.get("bank_same")+"";
                if (sameType.equals("0")){

                }else {

                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";                 mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.checkBankCard:
                        checkBankCard();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        dealFailInfo(map,mType);
    }
}
