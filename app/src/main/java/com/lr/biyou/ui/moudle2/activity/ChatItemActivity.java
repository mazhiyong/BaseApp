package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.lr.biyou.mywidget.dialog.SureOrNoDialog;
import com.lr.biyou.rongyun.common.IntentExtra;
import com.lr.biyou.rongyun.ui.activity.SearchHistoryMessageActivity;
import com.lr.biyou.rongyun.ui.widget.switchbutton.SwitchButton;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 聊天详情  界面
 */
public class ChatItemActivity extends BasicActivity implements RequestView, SelectBackListener {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.head_image)
    ImageView headImage;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.chat_history_lay)
    LinearLayout chatHistoryLay;
    @BindView(R.id.clear_chat_lay)
    LinearLayout clearChatLay;
    @BindView(R.id.tousu_lay)
    LinearLayout tousuLay;
    @BindView(R.id.top_switch)
    SwitchButton topSwitch;
    @BindView(R.id.quite_switch)
    SwitchButton quiteSwitch;
    @BindView(R.id.delete_tv)
    TextView deleteTv;

    private String mRequestTag = "";
    private String mTempToken = "";
    private String mAuthCode = "";
    private String mSmsToken = "";

    private Conversation.ConversationType conversationType;
    private Map<String, Object> mShareMap;

    private KindSelectDialog mDialog;
    private String targId;
    private String Id;
    private String name;
    private String portraitUrl;

    @Override
    public int getContentView() {
        return R.layout.activity_chat_item;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText("聊天详情");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);
        mBackText.setText("");
        List<Map<String, Object>> mDataList2 = SelectDataUtil.getSetType();
        mDialog = new KindSelectDialog(ChatItemActivity.this, true, mDataList2, 30);
        mDialog.setSelectBackListener(this);
        //getZiChanDataAction();
        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle =intent.getExtras();
            if (bundle != null){
                targId = intent.getStringExtra(IntentExtra.STR_TARGET_ID);
                conversationType = (Conversation.ConversationType) intent.getSerializableExtra(IntentExtra.SERIA_CONVERSATION_TYPE);

            }else {
                finish();
            }
        }

        //根据rc_id查询用户本地id
        getidFromRcidAction();

        topSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (topSwitch.isChecked()){
                    topSwitch.setChecked(false);
                }else {
                   topSwitch.setChecked(true);
                }*/
                updateStatusAction("1");
            }
        });

        quiteSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (quiteSwitch.isChecked()){
                    quiteSwitch.setChecked(false);
                }else {
                    quiteSwitch.setChecked(true);
                }*/
                updateStatusAction("2");
            }
        });


    }

    private void updateStatusAction(String type) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", Id);
        map.put("type",type);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_CHANAGE_STATUS, map);
    }


    @OnClick({R.id.left_back_lay, R.id.chat_history_lay, R.id.clear_chat_lay, R.id.tousu_lay, R.id.delete_tv})
    public void onViewClicked(View view) {
        Intent intent ;
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.chat_history_lay:
                intent = new Intent(this, SearchHistoryMessageActivity.class);
                intent.putExtra(IntentExtra.STR_TARGET_ID, targId);
                intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, conversationType);
                intent.putExtra(IntentExtra.STR_CHAT_NAME, name);
                intent.putExtra(IntentExtra.STR_CHAT_PORTRAIT, portraitUrl);
                startActivity(intent);
                break;
            case R.id.clear_chat_lay:
                showTipDislog();
                break;
            case R.id.tousu_lay:
                intent = new Intent(ChatItemActivity.this,ChoseReasonTypeActivity.class);
                intent.putExtra("id",Id);
                startActivity(intent);
                break;
            case R.id.delete_tv:

                break;
        }
    }

    private void showTipDislog() {

        SureOrNoDialog sureOrNoDialog = new SureOrNoDialog(ChatItemActivity.this,true);
        sureOrNoDialog.initValue("提示","是否清除聊天记录？");
        sureOrNoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cancel:
                        sureOrNoDialog.dismiss();
                        break;
                    case R.id.confirm:
                        sureOrNoDialog.dismiss();
                        //清除聊天记录
                        RongIM.getInstance().clearMessages(conversationType, targId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                LogUtilDebug.i("show","清除成功");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                LogUtilDebug.i("show","清除失败"+errorCode);
                            }
                        });
                        // 清除远端消息
                        RongIMClient.getInstance().cleanRemoteHistoryMessages(
                                conversationType,
                                targId, System.currentTimeMillis(),
                                null);
                        break;
                }
            }
        });
        sureOrNoDialog.show();
        sureOrNoDialog.setCanceledOnTouchOutside(false);
        sureOrNoDialog.setCancelable(true);
    }

    /**
     * 查询id
     */
    public void getidFromRcidAction() {
        mRequestTag = MethodUrl.CHAT_QUERY_ID;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("rc_id", targId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_QUERY_ID, map);
    }


    /**
     * 获取用户信息
     */
    public void getFriendInfoAction() {
        mRequestTag = MethodUrl.CHAT_FRIEDN_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", Id);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_FRIEDN_INFO, map);
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
            case MethodUrl.CHAT_QUERY_ID:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Id = tData.get("data")+"";
                            getFriendInfoAction();
                        }


                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.CHAT_FRIEDN_INFO:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            name = map.get("name")+"";
                            portraitUrl = map.get("portrait")+"";
                            nameTv.setText(name);
                            GlideUtils.loadImage(ChatItemActivity.this,portraitUrl,headImage);
                            if ((map.get("top")+"").equals("0")) {
                                topSwitch.setChecked(false);
                            }else {
                                topSwitch.setChecked(true);
                            }

                            if ((map.get("disturb")+"").equals("0")) {
                                quiteSwitch.setChecked(false);
                            }else {
                                quiteSwitch.setChecked(true);
                            }
                        }


                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;
            case MethodUrl.CHAT_CHANAGE_STATUS:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        //showToastMsg(tData.get("msg") + "");
                        //getFriendInfoAction();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }


                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.shareUrl:
                        //getZiChanDataAction();
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

    @OnClick(R.id.divide_line)
    public void onViewClicked() {
    }



    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 30:
                String str = (String) map.get("name");

                break;

        }
    }



}
