package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 身份证认证初始界面   界面
 */
public class IdCardCheckActivity extends BasicActivity implements RequestView{

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
    @BindView(R.id.but_checkl)
    Button mButCheck;

    private String mRequestTag = "";

    private Map<String,Object> mAuthTimesMap ;
    @Override
    public int getContentView() {
        return R.layout.activity_idcard_check;
    }



    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        //mBackImg.setVisibility(View.GONE);
        //mLeftBackLay.setVisibility(View.GONE);
        mTitleText.setText(getResources().getString(R.string.id_card_check));

    }

    /**
     * 获取用户认证信息
     */
    private void getAuthInfoAction() {
        mRequestTag = MethodUrl.userAuthInfo;

        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.userAuthInfo, map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAuthInfoAction();
    }
    @OnClick({R.id.back_img, R.id.but_checkl,R.id.left_back_lay})
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
                if (mAuthTimesMap == null || mAuthTimesMap.isEmpty()){
                    getAuthInfoAction();
                }else {
                    int authTimes = Integer.valueOf(mAuthTimesMap.get("auth_times") + "");
                    if (authTimes >=3){
                        intent = new Intent(IdCardCheckActivity.this,IdCardMyActivity.class);
                        startActivity(intent);
                    }else {
                        intent = new Intent(IdCardCheckActivity.this,IdCardPicActivity.class);
                        startActivity(intent);
                    }
                }

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
        Intent intent ;
        switch (mType){
            case MethodUrl.userAuthInfo:
                mAuthTimesMap = tData;
                int authTimes = Integer.valueOf(tData.get("auth_times") + "");
                mButCheck.setText("开始认证("+authTimes+"/3)");
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.userAuthInfo:
                        getAuthInfoAction();
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
