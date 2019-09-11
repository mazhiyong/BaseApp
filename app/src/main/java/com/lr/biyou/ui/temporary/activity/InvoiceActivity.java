package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.activity.TestScanActivity;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.TextViewUtils;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.Permission;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发票   界面
 */
public class InvoiceActivity extends BasicActivity implements RequestView {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.my_image)
    ImageView mMyImage;
    @BindView(R.id.login_phone_tv)
    TextView mLoginPhoneTv;

    @BindView(R.id.shoudong_tv)
    TextView mShouDongTv;

    private String mRequestTag = "";


    @Override
    public int getContentView() {
        return R.layout.activity_invoice;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mLoginPhoneTv.setText(MbsConstans.USER_MAP.get("tel")+"");
        mTitleText.setText(getResources().getString(R.string.invoice_title));


        String content = mShouDongTv.getText().toString().trim();
        TextViewUtils textViewUtils = new TextViewUtils();
        textViewUtils.init(content,mShouDongTv);
        textViewUtils.setTextColor(content.indexOf("？")+1,content.length(), ContextCompat.getColor(this,R.color.font_c));
        textViewUtils.build();
    }

    private void getMsgCodeAction() {

        mRequestTag = MethodUrl.changePhoneMsgCode;
        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.changePhoneMsgCode, map);
    }


    @OnClick({R.id.back_img, R.id.but_checkl,R.id.left_back_lay,R.id.shoudong_tv})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_checkl:
                PermissionsUtils.requsetRunPermission(InvoiceActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        Intent intent = new Intent(InvoiceActivity.this, TestScanActivity.class);
                        intent.putExtra("type","2");
                        startActivity(intent);
                    }
                    @Override
                    public void requestFailer() {

                    }
                }, Permission.Group.STORAGE,Permission.Group.CAMERA);
                break;
            case R.id.shoudong_tv:
                intent = new Intent(InvoiceActivity.this,InvoiceAddActivity.class);
                startActivity(intent);
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
        switch (mType){
            case  MethodUrl.changePhoneMsgCode:
                showToastMsg("获取验证码成功");
                intent = new Intent(InvoiceActivity.this,CodeMsgActivity.class);
                intent.putExtra("DATA",(Serializable) tData);
                intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_PHONE_CHANGE);
                intent.putExtra("showPhone", MbsConstans.USER_MAP.get("tel")+"");
                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.changePhoneMsgCode:
                        getMsgCodeAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }
}
