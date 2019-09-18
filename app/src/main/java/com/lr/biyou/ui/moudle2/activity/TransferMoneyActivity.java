package com.lr.biyou.ui.moudle2.activity;

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
import com.lr.biyou.rongyun.im.message.RongRedPacketMessage;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 转账
 */
public class TransferMoneyActivity extends BasicActivity implements RequestView, TradePassDialog.PassFullListener, SelectBackListener {
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
    @BindView(R.id.type2_tv)
    TextView type2Tv;
    @BindView(R.id.selectall_tv)
    TextView selectallTv;
    @BindView(R.id.aviable_tv)
    TextView huzhuanTv;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.head_iv)
    ImageView headIv;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.cny_tv)
    TextView cnyTv;


    private KindSelectDialog mDialog;
    private KindSelectDialog mDialog2;

    private String fromStr;
    private String toStr;
    private String tarid ="";
    private String type = "";
    private String id = "";

    private String rate;
    private String total;

    @Override
    public int getContentView() {
        return R.layout.activity_transfer_money;
    }


    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tarid = bundle.getString("tarid");
            type = bundle.getString("type");
            id = bundle.getString("id");
        }
        mTitleText.setText("转账");
        mTitleText.setCompoundDrawables(null,null,null,null);
        divideLine.setVisibility(View.GONE);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.icon6_dingdan);


        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (UtilTools.empty(rate)){
                    showToastMsg("请选择币种");
                }else {
                    if (s.toString().length()>0){
                        total = Float.parseFloat(rate)*Integer.parseInt(s.toString())+"";
                        cnyTv.setText("≈ "+total+"CNY");
                    }else {
                        cnyTv.setText("≈ 0.00CNY");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //请求币种列表数据
        typeListAction();


    }

    private void typeListAction() {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(TransferMoneyActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_ZHUANZHANG_TYPE, map);
    }




    @OnClick({R.id.back_img, R.id.right_lay, R.id.from_lay, R.id.change_iv, R.id.type_lay, R.id.selectall_tv, R.id.huzhuan_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.right_lay: //转账记录
                intent = new Intent(TransferMoneyActivity.this, RedRecordListActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
                break;
            case R.id.from_lay:
                mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.change_iv:
                fromStr = fromTv.getText().toString() + "";
                toStr = toTv.getText().toString() + "";
                fromTv.setText(toStr);
                toTv.setText(fromStr);
                break;
            case R.id.type_lay:
                mDialog2.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.selectall_tv:
                break;
            case R.id.huzhuan_tv:
                if (UtilTools.empty(rate)){
                    showToastMsg("请选择币种");
                    return;
                }

                if (UtilTools.empty(etMoney.getText())){
                    showToastMsg("请输入转账金额");
                    return;
                }

                showPassDialog();

                break;
        }
    }

    private TradePassDialog mTradePassDialog;
    private void showPassDialog(){

        if (mTradePassDialog == null){
            mTradePassDialog = new TradePassDialog(this, true);
            mTradePassDialog.setPassFullListener(TransferMoneyActivity.this);
            mTradePassDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
            mTradePassDialog.mPasswordEditText.setText(null);

            //忘记密码
            mTradePassDialog.mForgetPassTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getMsgCodeAction();
                }
            });

        }else {
            mTradePassDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
            mTradePassDialog.mPasswordEditText.setText(null);
        }
    }

    @Override
    public void onPassFullListener(String pass) {
        mTradePassDialog.mPasswordEditText.setText(null);
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(TransferMoneyActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("symbol",typeTv.getText()+"");
        map.put("total",etMoney.getText()+"");
        map.put("id",id);
        map.put("payment_password",pass);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_SEND_ZHUANZHANG, map);
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
        switch (mType){
            case MethodUrl.CHAT_SEND_ZHUANZHANG:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        mTradePassDialog.dismiss();
                        String red_id = tData.get("data")+"";
                        finish();
                        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                            RongRedPacketMessage rongRedPacketMessage = RongRedPacketMessage.obtain("2",red_id, "转账"+etMoney.getText()+"USDT,请查收");
                            RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, tarid, rongRedPacketMessage, null, null, new RongIMClient.SendMessageCallback() {
                                @Override
                                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                                    LogUtilDebug.i("show", "-----onError--" + errorCode);
                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    LogUtilDebug.i("show", "-----onScuess--" );
                                    sendMessageAction();
                                }
                            });
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(TransferMoneyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;
            case MethodUrl.CHAT_ZHUANZHANG_TYPE:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        List<Map<String, Object>> mDataList2;
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            mDataList2 = (List<Map<String, Object>>) tData.get("data");
                            for (Map<String,Object> map :mDataList2){
                                map.put("name",map.get("symbol")+"");
                            }
                        } else {
                            mDataList2 = new ArrayList<>();
                        }
                        mDialog2 = new KindSelectDialog(this, true, mDataList2, 30);
                        mDialog2.setSelectBackListener(this);

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(TransferMoneyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case MethodUrl.CHAT_ZHUANZHANG_PAGE:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data")+"")){
                            Map<String,Object> mapData = (Map<String, Object>) tData.get("data");
                            GlideUtils.loadImage(TransferMoneyActivity.this,mapData.get("portrait")+"",headIv);
                            rate = mapData.get("rate")+"";
                            if (etMoney.getText().toString().length()>0){
                                total = Float.parseFloat(rate)*Integer.parseInt(etMoney.getText().toString())+"";
                                cnyTv.setText("≈ "+total+"CNY");
                            }
                        }

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(TransferMoneyActivity.this, LoginActivity.class);
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


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10:
                String s = (String) map.get("name"); //选择账户
                fromTv.setText(s);
                break;
            case 30: //选择币种
                String str = (String) map.get("name"); //选择账户
                typeTv.setText(str);
                getMoneyAction(str);
                break;
        }
    }

    public void sendMessageAction(){
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(TransferMoneyActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("type", "3");
        map.put("content","转账");
        map.put("receiver_id",id);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_SEND_NEWS, map);
    }


    private void getMoneyAction(String symbol) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(TransferMoneyActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("symbol", symbol);
        map.put("id", id);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_ZHUANZHANG_PAGE, map);
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
