package com.lr.biyou.ui.moudle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 安全中心  界面
 */
public class SecurityActivity extends BasicActivity implements RequestView, SelectBackListener {
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
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.phone_lay)
    LinearLayout phoneLay;
    @BindView(R.id.email_tv)
    TextView emailTv;
    @BindView(R.id.email_lay)
    LinearLayout emailLay;
    @BindView(R.id.code_lay)
    LinearLayout codeLay;
    @BindView(R.id.exit_tv)
    TextView exitTv;
    @BindView(R.id.type_tv)
    TextView typeTv;


    private String mRequestTag = "";
    private String mTempToken = "";
    private String mAuthCode = "";
    private String mSmsToken = "";


    private Map<String, Object> mShareMap;

    private KindSelectDialog mDialog;

    private String account = "";

    @Override
    public int getContentView() {
        return R.layout.activity_security;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText("安全中心");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UtilTools.empty(MbsConstans.USER_MAP)) {
            String s = SPUtils.get(SecurityActivity.this, MbsConstans.SharedInfoConstans.LOGIN_INFO, "").toString();
            MbsConstans.USER_MAP = JSONUtil.getInstance().jsonMap(s);

        }
        account =MbsConstans.USER_MAP.get("account") + "";
        emailTv.setText(account);

        if (account.contains("@")){
            typeTv.setText("邮箱");
        }else {
            typeTv.setText("手机号");
        }
    }

    @OnClick({R.id.back_img, R.id.left_back_lay, R.id.phone_lay, R.id.email_lay, R.id.code_lay, R.id.exit_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.phone_lay:
                break;
            case R.id.email_lay:

                intent = new Intent(SecurityActivity.this, SecurityCheckActivity.class);
                if (account.contains("@")){
                    intent.putExtra("TYPE", "0");
                }else {
                    intent.putExtra("TYPE", "1");
                }
                intent.putExtra("DATA",account);
                startActivity(intent);
                break;
            case R.id.code_lay:
                intent = new Intent(SecurityActivity.this, ResetPayPassButActivity.class);
                intent.putExtra("TYPE", "1");
                startActivity(intent);
                break;
            case R.id.exit_tv:
                break;
        }
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
