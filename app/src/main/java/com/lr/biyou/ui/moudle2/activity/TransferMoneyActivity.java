package com.lr.biyou.ui.moudle2.activity;

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
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.mywidget.dialog.TradePassDialog;
import com.lr.biyou.rongyun.im.message.RongRedPacketMessage;
import com.lr.biyou.ui.moudle5.activity.HuaZhuanListActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SelectDataUtil;

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
        }
        mTitleText.setText("转账");
        mTitleText.setCompoundDrawables(null,null,null,null);
        divideLine.setVisibility(View.GONE);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.icon6_dingdan);

        initDialog();
    }

    private void initDialog() {
        List<Map<String, Object>> mDataList = SelectDataUtil.getAccoutType();
        mDialog = new KindSelectDialog(this, true, mDataList, 10);
        mDialog.setSelectBackListener(this);

        List<Map<String, Object>> mDataList2 = SelectDataUtil.getBiType();
        mDialog2 = new KindSelectDialog(this, true, mDataList2, 30);
        mDialog2.setSelectBackListener(this);

    }


    @OnClick({R.id.back_img, R.id.right_lay, R.id.from_lay, R.id.change_iv, R.id.type_lay, R.id.selectall_tv, R.id.huzhuan_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.right_lay: //充提记录
                intent = new Intent(TransferMoneyActivity.this, HuaZhuanListActivity.class);
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
                if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                    RongRedPacketMessage rongRedPacketMessage = RongRedPacketMessage.obtain(tarid, "转账100.21USDT","待收款");

                    RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, tarid, rongRedPacketMessage, null, null, new RongIMClient.SendMessageCallback() {
                        @Override
                        public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                            LogUtilDebug.i("show", "-----onError--" + errorCode);
                        }

                        @Override
                        public void onSuccess(Integer integer) {
                            LogUtilDebug.i("show", "-----onScuess--" );
                        }
                    });
                }
                break;
        }
    }

//    private TradePassDialog mTradePassDialog;
//
//    private void showPassDialog() {
//
//        if (mTradePassDialog == null) {
//            mTradePassDialog = new TradePassDialog(this, true);
//            mTradePassDialog.setPassFullListener(HuaZhuanActivity.this);
//            mTradePassDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
//            mTradePassDialog.mPasswordEditText.setText(null);
//
//            //忘记密码
//            mTradePassDialog.mForgetPassTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //getMsgCodeAction();
//                }
//            });
//
//        } else {
//            mTradePassDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
//            mTradePassDialog.mPasswordEditText.setText(null);
//
//        }
//    }

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
                type2Tv.setText(str);
                break;
        }
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
