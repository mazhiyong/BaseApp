package com.lr.biyou.ui.moudle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 手动输入身份证界面   界面
 */
public class IdCardEditActivity extends BasicActivity implements RequestView {


    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.idcard_edit)
    EditText idcardEdit;
    @BindView(R.id.check_lay)
    LinearLayout checkLay;
    @BindView(R.id.result_check_lay)
    LinearLayout resultCheckLay;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.but_next)
    Button butNext;

    private String mOpType = "";

    private String mRequestTag = "";
    private String mIdNum = "";
    private String mName = "";

    @Override
    public int getContentView() {
        return R.layout.activity_idcard_edit;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mOpType = bundle.getString("TYPE");
                if (!UtilTools.empty(mOpType) && mOpType.equals("1")) {
                    resultCheckLay.setVisibility(View.VISIBLE);
                    checkLay.setVisibility(View.GONE);
                }
            }
        }


        mTitleText.setText(getResources().getString(R.string.id_card_check2));
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);
        mBackText.setText("");
    }



    @OnClick({R.id.back_img, R.id.left_back_lay, R.id.but_next})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
                mName = nameEdit.getText() + "";
                if (UtilTools.isEmpty(nameEdit, "姓名")) {
                    showToastMsg("姓名不能为空");
                    return;
                }
                mIdNum = idcardEdit.getText() + "";
                if (UtilTools.isEmpty(idcardEdit, "身份证号")) {
                    showToastMsg("身份证号不能为空");
                    return;
                }

                //身份认证
                identityActiveAction();
                butNext.setEnabled(false);

                break;
        }
    }

    private void identityActiveAction() {

        mRequestTag = MethodUrl.IDENTITY_ACTIVE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(IdCardEditActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("real_name", mName);
        map.put("identity", mIdNum);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.IDENTITY_ACTIVE, map);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = null;
        if (requestCode == 1) {
            switch (resultCode) {//通过短信验证码  安装证书
                case MbsConstans.CodeType.CODE_INSTALL:
                    String authCode = "";
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        authCode = bundle.getString("authcode");
                    }
                    intent = new Intent();
                    intent.putExtra("authcode", authCode);
                    setResult(MbsConstans.CodeType.CODE_INSTALL, intent);
                    finish();
                    break;

            }
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
            case MethodUrl.IDENTITY_ACTIVE:
                butNext.setEnabled(true);
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        resultCheckLay.setVisibility(View.VISIBLE);
                        checkLay.setVisibility(View.GONE);
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(IdCardEditActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.installCode:
                        identityActiveAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        butNext.setEnabled(true);
        dealFailInfo(map, mType);
    }

}
