package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import com.lr.biyou.mywidget.dialog.MySelectDialog;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.RegexUtil;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.mywidget.view.BankCardTextWatcher;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加收款人   界面
 */
public class AddSKPeopleActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.name_tv)
    EditText mNameTv;
    @BindView(R.id.sk_zhanghu_eidt)
    EditText mSkZhanghuEidt;
    @BindView(R.id.kaihu_bank_value_tv)
    TextView mKaihuBankValueTv;
    @BindView(R.id.name_bank_lay)
    CardView mNameBankLay;
    @BindView(R.id.kaihu_bank_lay)
    CardView mKaihuBankLay;
    @BindView(R.id.jine_value_edit)
    EditText mJineValueEdit;
    @BindView(R.id.zhaiyao_value_edit)
    EditText mZhaiyaoValueEdit;
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.people_image_view)
    ImageView mSelectImageView;
    @BindView(R.id.kaihu_bank_dian_tv)
    TextView mKaihuBankDianTv;
    @BindView(R.id.gong_si_value_tv)
    TextView mGongSiValueTv;
    @BindView(R.id.gong_si_lay)
    CardView mGongSiLay;
    @BindView(R.id.bank_name_arrow_view)
    ImageView mBankArrowView;

    private MySelectDialog mGSDialog;

    private Map<String, Object> mGSMap;

    private String mRequestTag = "";

    private String mBankNum = "";
    private String mName = "";

    private Map<String, Object> mHezuoMap;

    private boolean mIsShow = false;

    private Map<String, Object> mBankMap;
    private Map<String, Object> mWangDianMap;
    private Map<String, Object> mAddMap;

    @Override
    public int getContentView() {
        return R.layout.activity_add_skp;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mHezuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }

        //"patncode": "SKFH2018",
        //		"vaccid": "1832618000002590",
        //		"secstatus": "",
        //		"accid": "6235559020000001965",
        //		"zifangnme": "上海分行",
        //		"zifangbho": "SHFH2018"
       /* mHezuoMap = new HashMap<>();
        mHezuoMap.put("patncode","SKFH2018");
        mHezuoMap.put("vaccid","1832618000002590");
        mHezuoMap.put("secstatus","");
        mHezuoMap.put("accid","6235559020000001965");
        mHezuoMap.put("zifangnme","上海分行");
        mHezuoMap.put("zifangbho","SHFH2018");*/

        mTitleText.setText(getResources().getString(R.string.add_money_people));

        List<Map<String, Object>> list = SelectDataUtil.gongsi();
        mGSDialog = new MySelectDialog(this, true, list, "选择类型", 30);
        mGSDialog.setSelectBackListener(this);

        mGSMap = list.get(0);
        mGongSiValueTv.setText(mGSMap.get("name")+"");
        mNameTv.setHint(R.string.hint_shoukuan_ren2);


        isSelectBankName();
        BankCardTextWatcher.bind(mSkZhanghuEidt);
        UtilTools.setMoneyEdit(mJineValueEdit,0);

        mSkZhanghuEidt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mKaihuBankValueTv.setText("");
                mKaihuBankDianTv.setText("");
                showBankTip();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /*mSkZhanghuEidt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!UtilTools.empty( mSkZhanghuEidt.getText().toString())){
                        checkBankCard();
                    }else {
                    }
                }
            }
        });*/

        //键盘显示监听
        mSkZhanghuEidt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                final Rect rect = new Rect();
                AddSKPeopleActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                final int screenHeight = AddSKPeopleActivity.this.getWindow().getDecorView().getRootView().getHeight();
                Log.e("TAG", rect.bottom + "#" + screenHeight);
                final int heightDifference = screenHeight - rect.bottom;
                boolean visible = heightDifference > screenHeight / 3;
                if (visible) {
                    //showToastMsg("软键盘弹出");
                    mIsShow = true;
                } else {
                    if (mIsShow && mSkZhanghuEidt.hasFocus()) {
                        if (mGSMap != null){
                            String code = mGSMap.get("code")+"";
                            if (code.equals("1")){// 账户类型(1: 对公; 2: 对私
                            }else if (code.equals("2")){
                                checkBankCard();
                            }
                        }
                    }
                    //showToastMsg("软键盘隐藏");
                    mIsShow = false;
                }
            }
        });
    }

    private void isSelectBankName(){
        String code = mGSMap.get("code")+"";
        if (code.equals("1")){// 账户类型(1: 对公; 2: 对私
            mNameBankLay.setEnabled(true);
            mBankArrowView.setVisibility(View.VISIBLE);
            mKaihuBankValueTv.setHint(getResources().getString(R.string.please_choose));
            mNameTv.setHint(R.string.hint_shoukuan_ren2);

        }else if (code.equals("2")){//2: 对私
            mNameBankLay.setEnabled(true);
            mBankArrowView.setVisibility(View.GONE);
            mKaihuBankValueTv.setHint(getResources().getString(R.string.bank_jiaoyan));
            mNameTv.setHint(R.string.hint_shoukuan_ren);

        }
    }
    private void showBankTip(){
        String code = mGSMap.get("code")+"";
        if (code.equals("1")){// 账户类型(1: 对公; 2: 对私
            mKaihuBankValueTv.setHint(getResources().getString(R.string.please_choose));
        }else if (code.equals("2")){//2: 对私
            mKaihuBankValueTv.setHint(getResources().getString(R.string.bank_jiaoyan));
        }
    }

    private void checkBankCard() {

        String num = mSkZhanghuEidt.getText() + "";
        num = num.replaceAll(" ","");

        mRequestTag = MethodUrl.checkBankCard;
        Map<String, String> map = new HashMap<>();
        if (UtilTools.empty(num)){
            showToastMsg("请输入合法的银行卡号");
            return;
        }
        //---------------------------------这个地方是要校验银行账户信息的
        //---------------------------------对私14-19位校验  对公4-32位校验，对私使用接口匹配开户行，对公选择开户行
        String code = mGSMap.get("code")+"";
        if (code.equals("1")){// 账户类型(1: 对公; 2: 对私
            boolean b = RegexUtil.isGongCard(num);
            if (!b){
                showToastMsg("请输入合法的银行卡号");
                return;
            }
        }else if (code.equals("2")){//2: 对私
            boolean b = RegexUtil.isSiCard(num);
            if (!b){
                showToastMsg("请输入合法的银行卡号");
                return;
            }
        }


        map.put("accid", num);
        map.put("ptncode", mHezuoMap.get("patncode") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.checkBankCard, map);
    }


    private void submitAction() {

        if (mGSMap == null || mGSMap.isEmpty()) {
            UtilTools.isEmpty(mGongSiValueTv,"账户类型");
            showToastMsg("账户类型不能为空");
            mButNext.setEnabled(true);
            return;
        }

        if (UtilTools.isEmpty(mNameTv,"收款人")){
            showToastMsg("收款人不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mSkZhanghuEidt,"收款账户")){
            showToastMsg("收款账户不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mKaihuBankValueTv,"开户银行")){
            showToastMsg("开户银行不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mKaihuBankDianTv,"开户网点")){
            showToastMsg("开户网点不能为空");
            mButNext.setEnabled(true);
            return;
        }

        mBankNum = mSkZhanghuEidt.getText() + "";
        mBankNum = mBankNum.replaceAll(" ","");
        //---------------------------------这个地方是要校验银行账户信息的
        //---------------------------------对私14-19位校验  对公4-32位校验，对私使用接口匹配开户行，对公选择开户行
        String code = mGSMap.get("code")+"";
        if (code.equals("1")){// 账户类型(1: 对公; 2: 对私
            boolean b = RegexUtil.isGongCard(mBankNum);
            if (!b){
                showToastMsg("请输入合法的银行卡号");
                mButNext.setEnabled(true);
                return;
            }
        }else if (code.equals("2")){
            boolean b = RegexUtil.isSiCard(mBankNum);
            if (!b){
                showToastMsg("请输入合法的银行卡号");
                mButNext.setEnabled(true);
                return;
            }
        }

        if (UtilTools.isEmpty(mJineValueEdit,"金额")){
            showToastMsg("金额不能为空");
            mButNext.setEnabled(true);
            return;
        }

        if (UtilTools.isEmpty(mZhaiyaoValueEdit,"摘要")){
            showToastMsg("摘要不能为空");
            mButNext.setEnabled(true);
            return;
        }

        mName = mNameTv.getText() + "";

        mButNext.setEnabled(true);

        mRequestTag = MethodUrl.skPeopleAdd;
        mAddMap = new HashMap<>();

        mAddMap.put("acctype",mGSMap.get("code")+"");//账户类型(1: 对公; 2: 对私)
        mAddMap.put("accname",mName);//户名
        mAddMap.put("accid",mBankNum);//帐号
        mAddMap.put("bankid",mBankMap.get("bankid")+"");//开户行ID
        mAddMap.put("bankname",mBankMap.get("bankname")+"");//开户行名称
        // mAddMap.put("crossmark",mBankMap.get("crossmark")+"");//跨行标识（1 本行 2 跨行）
        mAddMap.put("wdcode",mWangDianMap.get("opnbnkwdcd")+"");//开户网点编号
        mAddMap.put("wdname",mWangDianMap.get("opnbnkwdnm")+"");//开户网点名称
        mAddMap.put("amt",mJineValueEdit.getText()+"");//金额
        mAddMap.put("memo",mZhaiyaoValueEdit.getText()+"");//摘要

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.skPeopleAdd, mAddMap);
    }


    @OnClick({R.id.kaihu_bank_lay, R.id.but_next, R.id.back_img, R.id.people_image_view,R.id.name_bank_lay,R.id.gong_si_lay,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.kaihu_bank_lay:
                if (mBankMap == null){
                    showToastMsg("请先填写银行卡信息");
                }else {
                    intent = new Intent(AddSKPeopleActivity.this, ChoseBankAddActivity.class);
                    intent.putExtra("bankid",mBankMap.get("bankid")+"");
                    startActivityForResult(intent, 200);
                }
                break;
            case R.id.but_next:
                mButNext.setEnabled(false);
                submitAction();
                break;
            case R.id.people_image_view:
                intent = new Intent(AddSKPeopleActivity.this, SkPeopleListActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.gong_si_lay:
                mGSDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.name_bank_lay:
                String code = mGSMap.get("code")+"";
                if (code.equals("1")){// 账户类型(1: 对公; 2: 对私
                    intent = new Intent(AddSKPeopleActivity.this,BankNameListActivity.class);
                    startActivityForResult(intent,300);
                }else if (code.equals("2")){//2: 对私
                    checkBankCard();
                }

                break;
        }
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 30:

                if (mGSMap == null){

                }else {
                    String oldCode = mGSMap.get("code")+"";
                    String code = map.get("code")+"";
                    if (oldCode.equals(code)){
                        mGSMap = map;
                        mGongSiValueTv.setText(mGSMap.get("name") + "");
                        mGongSiValueTv.setError(null,null);
                    }else {
                        mGSMap = map;
                        mGongSiValueTv.setText(mGSMap.get("name") + "");
                        mGongSiValueTv.setError(null,null);

                        mKaihuBankValueTv.setText("");
                        mKaihuBankValueTv.setError(null,null);

                        mKaihuBankDianTv.setText("");
                        mKaihuBankDianTv.setError(null,null);

                        mSkZhanghuEidt.setText("");
                        mSkZhanghuEidt.setError(null,null);

                        mNameTv.setText("");
                        mNameTv.setError(null,null);
                    }
                    isSelectBankName();// 账户类型(1: 对公; 2: 对私
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
        switch (mType) {
            case MethodUrl.skPeopleAdd://{custid=null}
                showToastMsg("添加成功");
                Intent intent = new Intent();
                intent.putExtra("DATA",(Serializable)mAddMap);
                setResult(RESULT_OK,intent);
                mButNext.setEnabled(true);
                finish();
                break;
            case MethodUrl.checkBankCard://{"bank_same":"0","bankid":"305","accid":"6226203000645932","bankname":"民生银行","card_type":"1"}
                mBankMap = tData;
                String tt = mBankMap.get("bank_same")+"";
                if (tt.equals("0")){//是否同行（0：不是，1：是）
                    mBankMap.put("crossmark","2");//跨行标识（1 本行 2 跨行
                }else {
                    mBankMap.put("crossmark","1");
                }
                mKaihuBankValueTv.setText(mBankMap.get("bankname")+"");
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.checkBankCard:
                        checkBankCard();
                        break;
                    case MethodUrl.skPeopleAdd:
                        submitAction();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.skPeopleAdd://{custid=null}
                mButNext.setEnabled(true);
                break;
            case MethodUrl.checkBankCard://{"bank_same":"0","bankid":"305","accid":"6226203000645932","bankname":"民生银行","card_type":"1"}
                break;
        }
        dealFailInfo(map,mType);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Bundle bundle;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    bundle = data.getExtras();
                    if (bundle != null) {
                        /**
                         * 	"bankid": "3002",
                         "crossmark": "1",
                         "accid": "6224101646431613",
                         "wdcode": "503290004522",
                         "bankname": "南洋商业银行",
                         "wdname": "南洋商业银行（中国）有限公司上海分行",
                         "accname": "我的卡1",
                         "acctype": "1"
                         */
                        mBankMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mSkZhanghuEidt.setText(mBankMap.get("accid")+"");
                        mKaihuBankValueTv.setText(mBankMap.get("bankname")+"");
                        mNameTv.setText(mBankMap.get("accname")+"");
                        mNameTv.setError(null,null);
                        mSkZhanghuEidt.setError(null,null);
                        mKaihuBankValueTv.setError(null,null);

                        mWangDianMap = new HashMap<>();
                        mWangDianMap.put("opnbnkwdcd",mBankMap.get("wdcode")+"");//开户网点编号
                        mWangDianMap.put("opnbnkwdnm",mBankMap.get("wdname")+"");//开户网点名称
                        mKaihuBankDianTv.setText(mBankMap.get("wdname")+"");


                        List<Map<String, Object>> list = SelectDataUtil.gongsi();
                        String acctype = mBankMap.get("acctype")+"";
                        if (acctype.equals("1")){//账户类型(1: 对公; 2: 对私)
                            mGSMap = list.get(0);
                            mGongSiValueTv.setText(mGSMap.get("name")+"");
                            mNameTv.setHint(R.string.hint_shoukuan_ren2);
                        }else {
                            mGSMap = list.get(1);
                            mGongSiValueTv.setText(mGSMap.get("name")+"");
                            mNameTv.setHint(R.string.hint_shoukuan_ren);

                        }
                        mGongSiValueTv.setError(null,null);
                    }
                    break;
                case 200:
                    bundle = data.getExtras();
                    if (bundle != null) { //{"opnbnkwdnm":"南洋商业银行（中国）有限公司北京分行","opnbnkwdcd":"503100000015"}
                        mWangDianMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mKaihuBankDianTv.setText(mWangDianMap.get("opnbnkwdnm")+"");
                        mKaihuBankDianTv.setError(null,null);
                    }
                    break;
                case 300:
                    bundle = data.getExtras();
                    if (bundle != null){
                        mBankMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mBankMap.put("bankid",mBankMap.get("opnbnkid")+"");
                        mBankMap.put("bankname",mBankMap.get("opnbnknm")+"");
                        mBankMap.put("logopath",mBankMap.get("logopath")+"");
                        mKaihuBankValueTv.setText(mBankMap.get("opnbnknm")+"");
                        mKaihuBankValueTv.setError(null,null);
                    }
                    break;
            }
        }
    }



}
