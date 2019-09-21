package com.lr.biyou.ui.moudle.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.rongyun.common.ResultCallback;
import com.lr.biyou.rongyun.im.IMManager;
import com.lr.biyou.rongyun.model.UserCacheInfo;
import com.lr.biyou.rongyun.sp.UserCache;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.TextViewUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BasicActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.user_icon)
    ImageView mUserIcon;
    @BindView(R.id.bottom_image)
    ImageView mBottomImage;
    @BindView(R.id.edit_uid)
    EditText mEditUid;
    @BindView(R.id.img_login_clear_uid)
    ImageView mImgLoginClearUid;
    @BindView(R.id.arrow_view)
    ImageView mArrowView;
    @BindView(R.id.name_lay)
    RelativeLayout mNameLay;
    @BindView(R.id.img_login_clear_psw)
    ImageView mImgLoginClearPsw;
    @BindView(R.id.edit_psw)
    EditText mEditPsw;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.togglePwd)
    ToggleButton mTogglePwd;
    @BindView(R.id.forget_pass_tv)
    TextView mForgetPassTv;
    @BindView(R.id.code_register)
    TextView mCodeRegister;

    private SharedPreferences mShared;//存放配置信息的文件


    private String mAccount;
    private String mPassWord;
    private List<Map<String, Object>> mUserList = new ArrayList<Map<String, Object>>();//本地存储的多个登录账号的信息


    public static LoginActivity mInstance;

    private String mFirmKind = "";

    //存储当前最新一次登录的用户信息
    private UserCache userCache;
    private IMManager imManager;


    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

            }
        }
        super.onNewIntent(intent);
    }

    @Override
    public void init() {
        String content = mCodeRegister.getText().toString().trim();
        TextViewUtils textViewUtils = new TextViewUtils();
        textViewUtils.init(content,mCodeRegister);
        textViewUtils.setTextColor(content.indexOf("？")+1,content.length(), ContextCompat.getColor(this,R.color.blue1));
        textViewUtils.build();

        mInstance = this;

        userCache = new UserCache(getApplicationContext());
        imManager = IMManager.getInstance();

        //getNameCodeInfo();


        // StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 60);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        SPUtils.put(this, MbsConstans.SharedInfoConstans.LOGIN_OUT, true);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.MAIN_ACTIVITY);
        registerReceiver(mBroadcastReceiver,intentFilter);

        String account = SPUtils.get(this,MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT,"") +"";

        mEditUid.setText(account);
        if (!UtilTools.empty(account)){
            mEditUid.setSelection(account.length());
        }

        mEditUid.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditUid.getText().toString().length() > 0) {
                    mImgLoginClearUid.setVisibility(View.VISIBLE);
                    if (mEditPsw.getText().toString().length() > 0) {
                        mBtnLogin.setEnabled(true);
                    } else {
                        mEditPsw.setText("");
                        mBtnLogin.setEnabled(false);
                    }
                } else {
                    mEditPsw.setText("");
                    mBtnLogin.setEnabled(false);
                    mImgLoginClearUid.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditPsw.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditPsw.getText().toString().length() > 0) {
                    mImgLoginClearPsw.setVisibility(View.VISIBLE);
                    if (mEditUid.getText().toString().length() > 0) {
                        mBtnLogin.setEnabled(true);
                    } else {
                        mBtnLogin.setEnabled(false);
                    }
                } else {
                    mBtnLogin.setEnabled(false);
                    mImgLoginClearPsw.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mEditUid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (!UtilTools.empty( mEditUid.getText().toString())){
                        mImgLoginClearUid.setVisibility(View.VISIBLE);
                        mImgLoginClearPsw.setVisibility(View.GONE);
                    }else {
                        mImgLoginClearUid.setVisibility(View.GONE);
                    }
                }else {
                    mImgLoginClearUid.setVisibility(View.GONE);
                }
            }
        });
        mEditPsw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (!UtilTools.empty( mEditPsw.getText().toString())){
                        mImgLoginClearPsw.setVisibility(View.VISIBLE);
                        mImgLoginClearUid.setVisibility(View.GONE);
                    }else {
                        mImgLoginClearPsw.setVisibility(View.GONE);
                    }

                }else {
                    mImgLoginClearPsw.setVisibility(View.GONE);
                }
            }
        });


        mTogglePwd.setOnCheckedChangeListener(this);

    }


    /**
     * 获取全局字典配置信息
     */
    public void getNameCodeInfo() {
        Map<String, String> map = new HashMap<>();
        Map<String,String> mHeaderMap = new HashMap<String,String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.nameCode, map);
    }



    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.MAIN_ACTIVITY)){
                finish();
            }
        }
    };


    /**
     * 是否显示密码
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //显示密码
            mEditPsw.setTransformationMethod(
                    HideReturnsTransformationMethod.getInstance());
        } else {
            //隐藏密码
            mEditPsw.setTransformationMethod(
                    PasswordTransformationMethod.getInstance());
        }
    }


    /**
     * 清空控件文本
     */
    private void clearText(EditText edit) {
        edit.setText("");
    }

    private void loginAction() {

        if (UtilTools.isEmpty(mEditUid, getResources().getString(R.string.phone_number))) {
            mBtnLogin.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mEditPsw, getResources().getString(R.string.pass_word))) {
            mBtnLogin.setEnabled(true);
            return;
        }

        mAccount = mEditUid.getText() + "";
        mPassWord = mEditPsw.getText() + "";
//
//        if (!RegexUtil.isPhone(mAccount)){
//            showToastMsg("手机格式不正确");
//            return;
//        }


        //String pass = AESHelper.encrypt(mPassWord, AESHelper.password);
        //String pass = RSAUtils.encryptContent(mPassWord,RSAUtils.publicKey);
        Map<String, Object> map = new HashMap<>();
        map.put("account", mAccount);
        map.put("password", mPassWord);

        //String ss = AESHelper.decrypt(pass,AESHelper.password);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.LOGIN_ACTION, map);
    }


    /**
     * 获取refreshToken方法
     */
    private void getLoginRefreshToken() {
        Map<String,Object> map = new HashMap<>();
        map.put("access_token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.REFRESH_TOKEN, map);
    }

    /* *//**
     * 获取用户基本信息
     *//*
    private void getFriendInfoAction() {
        Map<String, String> map = new HashMap<>();
        Map<String,String> mHeaderMap = new HashMap<String,String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_INFO, map);
    }*/

    /**
     * 获取用户认证信息
     */
    private void getAuthInfoAction() {
        //
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.userAuthInfo, map);
//        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.userAuthInfo, map);
    }


    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @OnClick({R.id.img_login_clear_uid, R.id.arrow_view, R.id.img_login_clear_psw,
            R.id.code_register,R.id.btn_login,R.id.forget_pass_tv})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.code_register:
//                PermissionsUtils.requsetRunPermission(LoginActivity.this, new RePermissionResultBack() {
//                    @Override
//                    public void requestSuccess() {
//                        Intent intent = new Intent(LoginActivity.this,TestScanActivity.class);
//                        intent.putExtra("type","3");
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void requestFailer() {
//                        toast(R.string.failure);
//                    }
//                },Permission.Group.CAMERA,Permission.Group.STORAGE);

                intent = new Intent(LoginActivity.this, RegistActivity.class);
                intent.putExtra("type","3");
                startActivity(intent);


                break;
            case R.id.forget_pass_tv:
                intent = new Intent(LoginActivity.this, ResetLoginPassActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                loginAction();
                mBtnLogin.setEnabled(false);
//                CrashReport.testJavaCrash();
//                intent = new Intent(LoginActivity.this, ModifyFileActivity.class);
//                startActivity(intent);
                break;
            case R.id.img_login_clear_uid:    //清除用户名
                clearText(mEditUid);
                break;
            case R.id.img_login_clear_psw:    //清除密码
                clearText(mEditPsw);
                break;
            case R.id.arrow_view:
               /* if (mUserList.size() == 0) {
                    return;
                }
                if (mNameAdapter == null) {
                    mNameAdapter =  new LoginNameAdapter(this, mUserList);
                    mzListView.setAdapter(mNameAdapter);
                }
                if (mzPopupWindow == null) {
                    mzPopupWindow = new PopupWindow(mzView, mNameLay.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    mzPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                }

                if (mzPopupWindow.isShowing()) {
                    mzPopupWindow.dismiss();
                } else {
                    mzPopupWindow.showAsDropDown(mNameLay, 0, 0);
                }*/
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
        Intent intent;
        switch (mType) {
            case MethodUrl.nameCode:
                String result = tData.get("result")+"";
                SPUtils.put(LoginActivity.this, MbsConstans.SharedInfoConstans.NAME_CODE_DATA, result);
                break;
            case MethodUrl.LOGIN_ACTION://登录操作返回结果
                switch (tData.get("code")+""){
                    case "0":
                        MbsConstans.ACCESS_TOKEN = tData.get("data") + "";
                        //LogUtilDebug.i("show","token:"+tData.get("access_token"));
                      /*  if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
                            MbsConstans.ACCESS_TOKEN = "";
                        }*/
                        if (!UtilTools.empty(tData.get("ry_data")+"")){
                            MbsConstans.RONGYUN_MAP = (Map<String, Object>) tData.get("ry_data");
                            SPUtils.put(LoginActivity.this, MbsConstans.SharedInfoConstans.RONGYUN_DATA, JSONUtil.getInstance().objectToJson(MbsConstans.RONGYUN_MAP));
                        }


                        SPUtils.put(LoginActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, MbsConstans.ACCESS_TOKEN);
                        SPUtils.put(LoginActivity.this, MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT, mAccount+"");
                        SPUtils.put(LoginActivity.this, MbsConstans.SharedInfoConstans.LOGIN_PASSWORD,mPassWord+"");

                        LogUtilDebug.i("show","***********");
                        imManager.connectIM(MbsConstans.RONGYUN_MAP.get("token")+"", true, new ResultCallback<String>() {
                            @Override
                            public void onSuccess(String s) {
                                // 存储当前登录成功的用户信息
                                UserCacheInfo info = new UserCacheInfo( MbsConstans.RONGYUN_MAP.get("id")+"",
                                        MbsConstans.RONGYUN_MAP.get("token")+"",
                                        mAccount+"", mPassWord+"", "86", null);
                                userCache.saveUserCache(info);
                                LogUtilDebug.i("show","链接rong融云成功");
                        }

                            @Override
                            public void onFail(int errorCode) {
                                LogUtilDebug.i("show","链接rong融云失败");
                            }
                        });


                        intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;

                    case "1":
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }

                mBtnLogin.setEnabled(true);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                getAuthInfoAction();//获取用户认证信息
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mBtnLogin.setEnabled(true);
        String msg = map.get("errmsg") + "";
        showToastMsg(msg);
        dealFailInfo(map,mType);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }



    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
