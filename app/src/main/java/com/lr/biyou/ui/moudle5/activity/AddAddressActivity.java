package com.lr.biyou.ui.moudle5.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加提币 地址
 */
public class AddAddressActivity extends BasicActivity implements RequestView {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.tv)
    TextView mTextView;

    Map<String, Object> mapData = new HashMap<>();
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.aviable_money_tv)
    TextView aviableMoneyTv;
    @BindView(R.id.address_et)
    EditText addressEt;
    @BindView(R.id.scan_iv)
    ImageView scanIv;
    @BindView(R.id.address_iv)
    ImageView addressIv;
    @BindView(R.id.number_et)
    EditText numberEt;
    @BindView(R.id.selectall_tv)
    TextView selectallTv;
    @BindView(R.id.middle_money_tv)
    TextView middleMoneyTv;
    @BindView(R.id.tibi_tv)
    TextView tibiTv;

    private String id;

    @Override
    public int getContentView() {
        return R.layout.activity_add_address;
    }


    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mapData = (Map<String, Object>) bundle.getSerializable("DATA");
            if (!UtilTools.empty(mapData)){
                id = mapData.get("id")+"";
                addressEt.setText(mapData.get("address")+"");
                numberEt.setText(mapData.get("text")+"");
                mTitleText.setText("修改地址");
            }else {
                mTitleText.setText("添加地址");
            }
        }
        mTitleText.setCompoundDrawables(null, null, null, null);
        rightImg.setVisibility(View.GONE);
        rightImg.setImageResource(R.drawable.icon6_dingdan);
    }


    @OnClick({R.id.back_img, R.id.right_lay,R.id.address_iv,R.id.tibi_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.right_lay:
                break;
            case R.id.address_iv :
                break;
            case R.id.tibi_tv: //确定
                addAddressSumbitAction();
                break;

        }
    }

    private void addAddressSumbitAction() {
        String address = addressEt.getText().toString();
        String text = numberEt.getText().toString();
        if (UtilTools.empty(address)){
            showToastMsg("请输入地址信息");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddAddressActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("address",address);
        map.put("text",text);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        if (UtilTools.empty(id)){
            mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.ADDRESS_ADD, map);
        }else {
            map.put("id",id);
            mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.ADDRESS_EDIT, map);
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
        Intent intent;
        switch (mType){
            case MethodUrl.ADDRESS_ADD:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        showToastMsg(tData.get("msg") + "");
                        finish();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddAddressActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.ADDRESS_EDIT:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        showToastMsg(tData.get("msg") + "");
                        finish();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddAddressActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }


    /**---------------------------------------------------------------------以下代码申请权限---------------------------------------------
     * Request permissions.
     */


    /**
     * activity销毁前触发的方法
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * activity恢复时触发的方法
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }



}
