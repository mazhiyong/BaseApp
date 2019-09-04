package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.dialog.AddressSelectDialog2;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择银行网点   界面
 */
public class ChoseBankAddActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.address_value_tv)
    TextView mAddressValueTv;
    @BindView(R.id.choose_city_lay)
    CardView mChooseCityLay;
    @BindView(R.id.bank_dian_value_tv)
    TextView mBankDianValueTv;
    @BindView(R.id.bank_dian_lay)
    CardView mBankDianLay;
    @BindView(R.id.but_sure)
    Button mButSure;

    private AddressSelectDialog2 mAddressSelectDialog2;
    private String mRequestTag = "";
    private Map<String, Object> mAddressMap;

    private String mBankId = "";

    private Map<String,Object> mWangDianMap ;

    @Override
    public int getContentView() {
        return R.layout.activity_chose_bankadd;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mBankId = bundle.getString("bankid");
        }
        mTitleText.setText(getResources().getString(R.string.chose_bank_address));
        mAddressSelectDialog2 = new AddressSelectDialog2(this, true, "选择地址", 10);
        mAddressSelectDialog2.setSelectBackListener(this);

    }

    /**
     */
    private void submitInstall() {

        mRequestTag = MethodUrl.submitUserInfo;

        Map<String, Object> map = new HashMap<>();

        map.put("opnbnkid", "");
        map.put("citycode", "");

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.installCerSubmit, map);
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
            case MethodUrl.submitUserInfo:

                showToastMsg("提交成功");
                backTo(MainActivity.class, true);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.submitUserInfo:
                        submitInstall();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }


    private void sureButAction(){
        if (mAddressMap == null || mAddressMap.isEmpty()){
            UtilTools.isEmpty(mAddressValueTv,"城市");
            return;
        }

        if (mWangDianMap == null || mWangDianMap.isEmpty()){
            UtilTools.isEmpty(mBankDianValueTv,"银行网点");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("DATA",(Serializable)mWangDianMap);
        intent.putExtra("DATA2",(Serializable)mAddressMap);
        setResult(RESULT_OK,intent);

        finish();
    }

    @OnClick({R.id.choose_city_lay, R.id.bank_dian_lay, R.id.back_img,R.id.but_sure,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.but_sure:
                sureButAction();
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.choose_city_lay:
                mAddressSelectDialog2.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.bank_dian_lay:
                if (mAddressMap == null || mAddressMap.isEmpty()){
                    showToastMsg("请选择城市");
                }else {
                    intent = new Intent(ChoseBankAddActivity.this, BankWdActivity.class);
                    intent.putExtra("bankid", mBankId);
                    intent.putExtra("citycode", mAddressMap.get("citycode") + "");
                    startActivityForResult(intent,111);
                }
                break;
        }
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10:
                mAddressValueTv.setError(null,null);
                if(mAddressMap == null){
                    mAddressMap = map;
                }else {
                    LogUtilDebug.i("打印log日志",(mAddressMap == map)+"  "+mAddressMap.equals(map));
                    if(mAddressMap .equals(map)){

                    }else {
                        mAddressMap = map;
                        mWangDianMap = null;
                        mBankDianValueTv.setText("");
                    }
                }
                mAddressValueTv.setText(mAddressMap.get("name") + "");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 111:
                    bundle = data.getExtras();
                    if (bundle != null){//{"opnbnkwdnm":"南洋商业银行（中国）有限公司北京分行","opnbnkwdcd":"503100000015"}
                        mWangDianMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mBankDianValueTv.setText(mWangDianMap.get("opnbnkwdnm")+"");
                        mBankDianValueTv.setError(null,null);
                    }
                    break;
            }
        }
    }

}
