package com.lr.biyou.ui.moudle5.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.mywidget.dialog.TradePassDialog;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 划转
 */
public class HuaZhuanActivity extends BasicActivity implements RequestView, TradePassDialog.PassFullListener, SelectBackListener {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    Map<String, Object> mapData = new HashMap<>();
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.from_tv)
    TextView fromTv;
    @BindView(R.id.from_lay)
    LinearLayout fromLay;
    @BindView(R.id.to_tv)
    TextView toTv;
    @BindView(R.id.change_iv)
    ImageView changeIv;
    @BindView(R.id.type_tv)
    TextView typeTv;
    @BindView(R.id.type_lay)
    LinearLayout typeLay;
    @BindView(R.id.number_et)
    EditText numberEt;
    @BindView(R.id.type2_tv)
    TextView type2Tv;
    @BindView(R.id.selectall_tv)
    TextView selectallTv;
    @BindView(R.id.aviable_tv)
    TextView aviableTv;
    @BindView(R.id.huzhuan_tv)
    TextView huzhuanTv;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.to_lay)
    LinearLayout toLay;
    @BindView(R.id.from_iv)
    ImageView fromIv;
    @BindView(R.id.to_iv)
    ImageView toIv;


    private KindSelectDialog mDialog;
    private KindSelectDialog mDialog2;

    private String fromStr;
    private String toStr;
    private String avaiableNumber;

    private String mFormToType = "0";

    @Override
    public int getContentView() {
        return R.layout.activity_hua_zhuan;
    }


    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mapData = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        mTitleText.setVisibility(View.GONE);
        divideLine.setVisibility(View.GONE);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.icon6_dingdan);

        toLay.setEnabled(false);
        initDialog();
    }


    private void initDialog() {
        List<Map<String, Object>> mDataList = SelectDataUtil.getAccoutType();
        mDialog = new KindSelectDialog(this, true, mDataList, 10);
        mDialog.setSelectBackListener(this);

    }


    @OnClick({R.id.back_img, R.id.right_lay, R.id.from_lay, R.id.to_lay, R.id.change_iv, R.id.type_lay, R.id.selectall_tv, R.id.huzhuan_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.right_lay: //充提记录
                intent = new Intent(HuaZhuanActivity.this, HuaZhuanListActivity.class);
                startActivity(intent);
                break;
            case R.id.from_lay:
                mFormToType = "0";
                mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.to_lay:
                mFormToType = "1";
                mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.change_iv: //划转
                fromStr = fromTv.getText().toString() + "";
                toStr = toTv.getText().toString() + "";
                fromTv.setText(toStr);
                toTv.setText(fromStr);
                if (fromTv.getText().toString().equals("币币账户")){
                    fromLay.setEnabled(false);
                    fromIv.setVisibility(View.GONE);
                    toLay.setEnabled(true);
                    toIv.setVisibility(View.VISIBLE);
                }else {
                    fromLay.setEnabled(true);
                    fromIv.setVisibility(View.VISIBLE);
                    toLay.setEnabled(false);
                    toIv.setVisibility(View.GONE);
                }

                if (!type2Tv.getText().toString().equals("请选择")){
                    getAviableMoneyAction(type2Tv.getText().toString());
                }

                break;
            case R.id.type_lay:
                String type = "";
                if (fromTv.getText().toString().equals("请选择") || toTv.getText().toString().equals("请选择")) {
                    showToastMsg("请完善划转账户信息");
                    return;
                }
                if ((fromTv.getText().toString().equals("币币账户") && (toTv.getText().toString().equals("法币账户")))) {
                    type = "1";
                }
                if ((fromTv.getText().toString().equals("币币账户") && (toTv.getText().toString().equals("合约账户")))) {
                    type = "2";
                }
                if ((fromTv.getText().toString().equals("法币账户") && (toTv.getText().toString().equals("币币账户")))) {
                    type = "3";
                }
                if ((fromTv.getText().toString().equals("法币账户") && (toTv.getText().toString().equals("合约账户")))) {
                    type = "4";
                }
                if ((fromTv.getText().toString().equals("合约账户") && (toTv.getText().toString().equals("币币账户")))) {
                    type = "5";
                }
                if ((fromTv.getText().toString().equals("合约账户") && (toTv.getText().toString().equals("法币账户")))) {
                    type = "6";
                }
                if ((!fromTv.getText().toString().equals("奖励金") && (toTv.getText().toString().equals("奖励金")))) {
                    type = "7";
                }
                if ((fromTv.getText().toString().equals("奖励金") && (!toTv.getText().toString().equals("奖励金")))) {
                    type = "7";
                }

                getTypeListAction(type);
                break;
            case R.id.selectall_tv:
                numberEt.setText(avaiableNumber);
                break;
            case R.id.huzhuan_tv:
                huazhuanSumbitAction();
                huzhuanTv.setEnabled(false);
                break;
        }
    }

    private void huazhuanSumbitAction() {
        if (UtilTools.empty(numberEt.getText()) || typeTv.getText().toString().equals("请选择")) {
            showToastMsg("请填写完善信息");
            huzhuanTv.setEnabled(true);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(HuaZhuanActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("number", numberEt.getText() + "");
        map.put("symbol", typeTv.getText() + "");

        if (fromTv.getText().toString().equals("请选择") || toTv.getText().toString().equals("请选择")) {
            showToastMsg("请完善划转账户信息");
            huzhuanTv.setEnabled(true);
            return;
        }
        if (fromTv.getText().toString().equals(toTv.getText().toString())) {
            showToastMsg("不能向相同账户类型划转");
            huzhuanTv.setEnabled(true);
            return;
        }

        if ((fromTv.getText().toString().equals("币币账户") && (toTv.getText().toString().equals("法币账户")))) {
            map.put("type", "1");
        }
        if ((fromTv.getText().toString().equals("币币账户") && (toTv.getText().toString().equals("合约账户")))) {
            map.put("type", "2");
        }
        if ((fromTv.getText().toString().equals("法币账户") && (toTv.getText().toString().equals("币币账户")))) {
            map.put("type", "3");
        }
        if ((fromTv.getText().toString().equals("法币账户") && (toTv.getText().toString().equals("合约账户")))) {
            map.put("type", "4");
        }
        if ((fromTv.getText().toString().equals("合约账户") && (toTv.getText().toString().equals("币币账户")))) {
            map.put("type", "5");
        }
        if ((fromTv.getText().toString().equals("合约账户") && (toTv.getText().toString().equals("法币账户")))) {
            map.put("type", "6");
        }
        if ((fromTv.getText().toString().equals("币币账户") && (toTv.getText().toString().equals("奖励金")))) {
            map.put("type", "7");
        }
        if ((fromTv.getText().toString().equals("法币账户") && (toTv.getText().toString().equals("奖励金")))) {
            map.put("type", "8");
        }
        if ((fromTv.getText().toString().equals("合约账户") && (toTv.getText().toString().equals("奖励金")))) {
            map.put("type", "9");
        }
        if ((fromTv.getText().toString().equals("奖励金") && (toTv.getText().toString().equals("币币账户")))) {
            map.put("type", "10");
        }
        if ((fromTv.getText().toString().equals("奖励金") && (toTv.getText().toString().equals("法币账户")))) {
            map.put("type", "11");
        }
        if ((fromTv.getText().toString().equals("奖励金") && (toTv.getText().toString().equals("合约账户")))) {
            map.put("type", "12");
        }
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.HUAZHUAN_DEAL, map);

    }

    private void getTypeListAction(String type) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(HuaZhuanActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("type", type);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.HUANZHUAN_BI_TYPE, map);
    }


    @Override
    public void onPassFullListener(String pass) {
        //mTradePassDialog.mPasswordEditText.setText(null);
        //mTradePass = pass;
        //mNextButton.setEnabled(false);
        //submitData();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType) {
            case MethodUrl.HUANZHUAN_BI_TYPE:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            List<Map<String, Object>> mDataList2 = (List<Map<String, Object>>) tData.get("data");
                            for (Map<String, Object> map : mDataList2) {
                                map.put("name", map.get("symbol") + "");
                            }
                            mDialog2 = new KindSelectDialog(this, true, mDataList2, 30);
                            mDialog2.setSelectBackListener(this);
                            mDialog2.showAtLocation(Gravity.BOTTOM, 0, 0);
                        }
                        //List<Map<String,Object>> mDataList2 = SelectDataUtil.getBiType();

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(HuaZhuanActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.ACCOUNT_AVAIABLE_MONEY:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            avaiableNumber = tData.get("data") + "";
                            aviableTv.setText("可用 " + UtilTools.formatDecimal(avaiableNumber,2) + " " + typeTv.getText().toString());
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(HuaZhuanActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.HUAZHUAN_DEAL:
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
                        intent = new Intent(HuaZhuanActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                huzhuanTv.setEnabled(true);
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        huzhuanTv.setEnabled(true);
        dealFailInfo(map, mType);
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10:
                String s = (String) map.get("name"); //选择账户
                if (mFormToType.equals("0")) { //from
                    fromTv.setText(s);
                } else {
                    toTv.setText(s);
                }

                break;
            case 30: //选择币种
                String str = (String) map.get("name"); //选择账户
                typeTv.setText(str);
                type2Tv.setText(str);
                getAviableMoneyAction(str);
                huzhuanTv.setEnabled(true);
                break;
        }
    }

    private void getAviableMoneyAction(String symbol) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(HuaZhuanActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("symbol", symbol);

        if (fromTv.getText().toString().equals("币币账户")) {
            map.put("type", "1");
        }
        if (fromTv.getText().toString().equals("法币账户")) {
            map.put("type", "2");
        }
        if (fromTv.getText().toString().equals("合约账户")) {
            map.put("type", "3");
        }
        if (fromTv.getText().toString().equals("奖励金")) {
            map.put("type", "4");
        }
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.ACCOUNT_AVAIABLE_MONEY, map);
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
