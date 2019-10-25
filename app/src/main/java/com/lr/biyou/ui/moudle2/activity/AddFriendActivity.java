package com.lr.biyou.ui.moudle2.activity;

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
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.listener.SoftKeyBoardListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.im.message.ContactNotificationMessage;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.ShowDetailPictrue;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;

/**
 * 添加朋友 界面
 */
public class AddFriendActivity extends BasicActivity implements RequestView, SelectBackListener {
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
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.my_qrcode_iv)
    ImageView myQrcodeIv;
    @BindView(R.id.user_icon_iv)
    ImageView userIconIv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.add_tv)
    TextView addTv;


    private String mRequestTag = "";

    private KindSelectDialog mDialog;
    private Map<String, Object> mapData;

    private String friendId ="";
    private String Id ="";
    private String groupId ="";
    private String groupName ="";

    private List<Map<String, Object>> mImageList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String result = bundle.getString("DATA");
                if (!UtilTools.empty(result)){
                    //根据二维码信息,获取用户信息 点击查看群成员加好友
                    getUserInfoAccordQrAction(result);

                }
                String targetId = bundle.getString(IntentExtra.STR_TARGET_ID);
                LogUtilDebug.i("show","targid:"+targetId);
                if (!UtilTools.empty(targetId)){
                    //根据rc_id 获取用户的信息
                    getUserInfoAction(targetId);
                    //根据rc_id 获取用户的id   扫码/分享名片加好友
                    //getidFromRcidAction(targetId);
                }

                groupId = bundle.getString(IntentExtra.STR_GROUP_ID);
                if (!UtilTools.empty(groupId)){
                    getUserInfoAccordQrAction("2,"+groupId);
                    addTv.setText("申请入群");
                }

            }
        }


        mTitleText.setText("添加好友");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);


        //获取我的二维码信息
        //getQrCodeAction();

        SoftKeyBoardListener.setListener(AddFriendActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //Toast.makeText(getActivity(), "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void keyBoardHide(int height) {
                //Toast.makeText(getActivity(), "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
                searchFriendAction();
            }
        });

    }


    @OnClick({R.id.back_img, R.id.left_back_lay,R.id.iv_search, R.id.my_qrcode_iv, R.id.add_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.iv_search:
                searchFriendAction();
                break;
            case R.id.my_qrcode_iv:
                intent = new Intent(AddFriendActivity.this, ShowDetailPictrue.class);
                intent.putExtra("position",0);
                intent.putExtra("DATA",(Serializable) mImageList);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.add_tv:
                if (addTv.getText().toString().equals("申请入群")){
                    addMemberAction();
                }else {
                    addFriendAction();
                }
                addTv.setEnabled(false);
                break;
        }
    }


    /**
     * 查询用户信息
     */
    public void getUserInfoAction(String targetId) {
        mRequestTag = MethodUrl.CHAT_QUERY_USERINFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(AddFriendActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("rc_id", targetId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_QUERY_USERINFO, map);
    }


    /**
     * 查询id
     */
    public void getidFromRcidAction(String targetId) {
        mRequestTag = MethodUrl.CHAT_QUERY_ID;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(AddFriendActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("rc_id", targetId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_QUERY_ID, map);
    }

    private void getUserInfoAccordQrAction(String text) {
        mRequestTag = MethodUrl.CHAT_SWEEP_CODE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddFriendActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("text",text);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_SWEEP_CODE, map);
    }


    private void getQrCodeAction() {
        mRequestTag = MethodUrl.CHAT_QRCODE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddFriendActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_QRCODE, map);
    }



    private void  searchFriendAction() {
        if (UtilTools.empty(etSearch.getText().toString())){
            showToastMsg("请输入检索信息");
            return;
        }

        mRequestTag = MethodUrl.CHAT_SEARCH_FRIEND;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddFriendActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("keywords", etSearch.getText()+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_SEARCH_FRIEND, map);
    }



    private void  addMemberAction() {
        mRequestTag = MethodUrl.CHAT_GROUP_ADD_MEMBER;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddFriendActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id", groupId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_ADD_MEMBER, map);
    }

    private void  addFriendAction() {
      /*  if (UtilTools.empty(etSearch.getText().toString())){
            showToastMsg("请输入检索信息");
            return;
        }*/

        mRequestTag = MethodUrl.CHAT_ADD_FRIEND;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddFriendActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", friendId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_ADD_FRIEND, map);
    }


    /**
     * 获取用户信息
     */
    public void getFriendInfoAction() {
        mRequestTag = MethodUrl.CHAT_FRIEDN_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(AddFriendActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
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
        Intent intent;
        switch (mType) {
            case MethodUrl.CHAT_QUERY_ID:
                /*switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Id = tData.get("data") + "";
                            //getFriendInfoAction();
                            friendId = Id;
                            addFriendAction();
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, com.lr.biyou.ui.moudle.activity.LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;*/
            case MethodUrl.CHAT_GROUP_ADD_MEMBER:
                switch (tData.get("code") + "") {
                    case "0": //请求成功 //进入群聊
                        RongIM.getInstance().startGroupChat(AddFriendActivity.this, groupId, groupName);

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, com.lr.biyou.ui.moudle.activity.LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                addTv.setEnabled(true);
                break;
            case MethodUrl.CHAT_FRIEDN_INFO:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String, Object> map = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(map)){
                                userNameTv.setText(map.get("name")+"");
                                GlideUtils.loadImage(AddFriendActivity.this,map.get("portrait")+"",userIconIv);
                                friendId = map.get("id")+"";
                            }
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;

            case MethodUrl.CHAT_QRCODE:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        String url = tData.get("data")+"";
                        Map<String,Object> mapImage= new HashMap<>();
                        mapImage.put("url",url);
                        mImageList.add(mapImage);
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case  MethodUrl.CHAT_QUERY_USERINFO:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data")+"")){
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            userNameTv.setText(map.get("name")+"");
                            GlideUtils.loadImage(AddFriendActivity.this,map.get("portrait")+"",userIconIv);
                            friendId = map.get("id")+"";
                        }else {
                            showToastMsg("未检索到相关用户信息");
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }
                break;
            case  MethodUrl.CHAT_SEARCH_FRIEND:
                String code = tData.get("code") + "";
                switch (code) {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data")+"")){
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            userNameTv.setText(map.get("name")+"");
                            GlideUtils.loadImage(AddFriendActivity.this,map.get("portrait")+"",userIconIv);
                            friendId = map.get("id")+"";
                        }else {
                            showToastMsg("未检索到相关用户信息");
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }
                break;
            case MethodUrl.CHAT_ADD_FRIEND:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        showToastMsg("添加好友申请,发送成功");
                        userIconIv.setImageResource(R.drawable.wait2);
                        userNameTv.setText("等待好友通过申请验证");

                        //发送融云message
                        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                            MessageContent message = ContactNotificationMessage.obtain("添加好友", Id, friendId, "你们已经是好友啦!");
                            RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, friendId, message, null, null, new RongIMClient.SendMessageCallback() {
                                @Override
                                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                                    LogUtilDebug.i("show", "-----onError--" + errorCode);
                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    LogUtilDebug.i("show", "-----onScuess--");

                                }
                            });

                        }
                        addTv.setEnabled(true);
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        /*if((tData.get("msg") + "").equals("他已经是你的好友了")){
                            getFriendInfoAction();
                        }*/
                        addTv.setEnabled(true);
                        break;
                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }
                break;

            case MethodUrl.CHAT_SWEEP_CODE:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data")+"")){
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            userNameTv.setText(map.get("name")+"");
                            GlideUtils.loadImage(AddFriendActivity.this,map.get("portrait")+"",userIconIv);
                            friendId = map.get("id")+"";
                            groupName = map.get("name")+"";
                        }else {
                            showToastMsg("未检索到相关用户信息");
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }
                break;

        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        addTv.setEnabled(true);
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


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 30:
                String str = (String) map.get("name");

                break;

        }
    }





}
