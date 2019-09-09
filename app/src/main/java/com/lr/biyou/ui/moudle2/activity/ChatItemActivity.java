package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
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
import com.lr.biyou.rongyun.ui.widget.switchbutton.SwitchButton;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

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


    private Map<String, Object> mShareMap;

    private KindSelectDialog mDialog;
    private String id;

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
        //getShareData();
        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle =intent.getExtras();
            if (bundle != null){
                id = bundle.getString("DATA");
            }
        }

        getFriendInfoAction();


        topSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateStatusAction("1",isChecked);
            }
        });

        quiteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateStatusAction("2",isChecked);
            }
        });
    }

    private void updateStatusAction(String type, boolean isChecked) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id",id);
        map.put("type",type);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_CHANAGE_STATUS, map);
    }


    @OnClick({R.id.left_back_lay, R.id.chat_history_lay, R.id.clear_chat_lay, R.id.tousu_lay, R.id.delete_tv})
    public void onViewClicked(View view) {
        Intent intent ;
        switch (view.getId()) {
            case R.id.left_back_lay:
                break;
            case R.id.chat_history_lay:
                break;
            case R.id.clear_chat_lay:
                break;
            case R.id.tousu_lay:
                intent = new Intent(ChatItemActivity.this,ChoseReasonTypeActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_tv:

                break;
        }
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
        map.put("id",id);
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
            case MethodUrl.CHAT_FRIEDN_INFO:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            nameTv.setText(map.get("name")+"");
                            GlideUtils.loadImage(ChatItemActivity.this,map.get("portrait")+"",headImage);
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
                        showToastMsg(tData.get("msg") + "");
                        getFriendInfoAction();
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
                        //getShareData();
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
