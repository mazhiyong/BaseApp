package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 审核失败后，自己手动提交审核
 */
public class IdCardMyActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.name_edit)
    EditText mNameEdit;
    @BindView(R.id.id_num_edit)
    EditText mIdNumEdit;

    private String mRequestTag = "";


    @Override
    public int getContentView() {
        return R.layout.activity_idcard_my;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.id_card_check));
        mLeftBackLay.setVisibility(View.GONE);
        getAuthInfoAction();
    }


    /**
     * 最近一次认证信息
     */
    private void getAuthInfoAction() {

        mRequestTag = MethodUrl.laseAuthInfo;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.laseAuthInfo, map);
    }

    private void butAction(){
        if (UtilTools.isEmpty(mNameEdit,"姓名")){
            showToastMsg("姓名不能为空");
            return;
        }
        if (UtilTools.isEmpty(mIdNumEdit,"身份证号")){
            showToastMsg("身份证号不能为空");
            return;
        }

        Intent intent = new Intent(IdCardMyActivity.this, IdCardMyPicActivity.class);
        intent.putExtra("idname",mNameEdit.getText()+"");
        intent.putExtra("idno",mIdNumEdit.getText()+"");
        startActivity(intent);
    }


    @OnClick({R.id.back_img, R.id.but_next,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
                butAction();
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
    public void loadDataSuccess(Map<String, Object> tData, String mType) {//
        Intent intent;
        switch (mType) {
            case MethodUrl.laseAuthInfo://{idname=刘英超, idno=410725199103263616}
                String name = tData.get("idname")+"";
                String idNum = tData.get("idno")+"";
                mNameEdit.setText(name);
                mIdNumEdit.setText(idNum);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";                 mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.laseAuthInfo:
                        getAuthInfoAction();
                        break;
                }
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
       dealFailInfo(map,mType);
    }

}
