package com.lr.biyou.ui.moudle5.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.TradePassDialog;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.ResetPayPassButActivity;
import com.lr.biyou.ui.moudle.activity.TestScanActivity;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.yanzhenjie.permission.Permission;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提币
 */
public class TiBiActivity extends BasicActivity implements RequestView ,TradePassDialog.PassFullListener{
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

    Map<String, Object> mapData;
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

    private String symbol = "";
    private String number = "";
    @Override
    public int getContentView() {
        return R.layout.activity_ti_bi;
    }


    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mapData = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        symbol = mapData.get("symbol") + "";
        mTitleText.setText("提币");
        mTitleText.setCompoundDrawables(null, null, null, null);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.icon6_dingdan);

        addressEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0 && !UtilTools.empty(numberEt.getText()+"")){
                    tibiTv.setEnabled(true);
                }else {
                    tibiTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        numberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0 && !UtilTools.empty(addressEt.getText()+"")){
                    tibiTv.setEnabled(true);
                }else {
                    tibiTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getBiMesssageAction();
    }

    private void getBiMesssageAction() {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(TiBiActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("symbol",symbol);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.TI_BI, map);
    }


    @OnClick({R.id.back_img, R.id.right_lay,R.id.address_iv,R.id.tibi_tv,R.id.scan_iv,R.id.selectall_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.right_lay: //充提记录
                intent = new Intent(TiBiActivity.this, TradeListActivity.class);
                startActivity(intent);
                break;

            case R.id.address_iv ://地址
                intent = new Intent(TiBiActivity.this, TibiAddressActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.scan_iv: //扫码二维码地址
                PermissionsUtils.requsetRunPermission(TiBiActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        Intent intent = new Intent(TiBiActivity.this, TestScanActivity.class);
                        intent.putExtra("type", "4");
                        startActivityForResult(intent,200);
                    }

                    @Override
                    public void requestFailer() {
                        Toast.makeText(TiBiActivity.this,"相机权限授权失败",Toast.LENGTH_LONG).show();
                    }
                }, Permission.Group.STORAGE,Permission.Group.CAMERA);


                break;
            case R.id.selectall_tv:
                numberEt.setText(number);
                break;
            case R.id.tibi_tv: //提币操作
                showPassDialog();
                break;

        }
    }
    private TradePassDialog mTradePassDialog;
    private void showPassDialog(){

        if (mTradePassDialog == null){
            mTradePassDialog = new TradePassDialog(this, true);
            mTradePassDialog.setPassFullListener(TiBiActivity.this);
            mTradePassDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
            mTradePassDialog.mPasswordEditText.setText(null);

            //忘记密码
            mTradePassDialog.mForgetPassTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TiBiActivity.this, ResetPayPassButActivity.class);
                    startActivity(intent);

                }
            });

        }else {
            mTradePassDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
            mTradePassDialog.mPasswordEditText.setText(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle != null){
                Map<String,Object> map = (Map<String, Object>) bundle.getSerializable("DATA");
                addressEt.setText(map.get("address")+"");
            }
        }

        if (requestCode == 200 && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle != null){
                String result = bundle.getString("result");
                addressEt.setText(result);
            }
        }

    }

    @Override
    public void onPassFullListener(String pass) {
        mTradePassDialog.mPasswordEditText.setText(null);
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(TiBiActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("symbol",symbol);
        map.put("number",numberEt.getText()+"");
        map.put("payment_password",pass);
        map.put("address",addressEt.getText()+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.TI_BI_DEAL, map);

        mTradePassDialog.dismiss();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType){
            case MethodUrl.TI_BI:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            aviableMoneyTv.setText("可用 "+UtilTools.getNormalNumber(map.get("balance")+"")+" "+symbol);
                            middleMoneyTv.setText("手续费: "+UtilTools.getNormalNumber(map.get("fee")+" ")+" "+symbol);
                            number = map.get("balance")+"";
                            mTextView.setText("温馨提示：\n" +
                                    "最小提币数量 "+UtilTools.getNormalNumber(number)+" "+symbol);
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(TiBiActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.TI_BI_DEAL:
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
                        intent = new Intent(TiBiActivity.this, LoginActivity.class);
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
