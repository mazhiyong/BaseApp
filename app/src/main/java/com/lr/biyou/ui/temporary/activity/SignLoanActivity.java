package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.SignHeTongAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;




import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 合同签署界面
 */
public class SignLoanActivity extends BasicActivity implements RequestView , ReLoadingData {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.tv_loaner)
    TextView mLoanerText;
    @BindView(R.id.tv_jiekuan_timelitmit)
    TextView mTimeLitmitText;
    @BindView(R.id.tv_shouxin_money)
    TextView mShouxinMoneyText;
    @BindView(R.id.tv_rate_year)
    TextView mRateYearText;
    @BindView(R.id.tv_shouxin_date)
    TextView mShouxinDate;
    @BindView(R.id.rcv_conlist)
    RecyclerView mRecyclerView;
    @BindView(R.id.bt_read_agree)
    Button mButton;
    @BindView(R.id.lilv_type_tv)
    TextView mLilvTypeTv;
    @BindView(R.id.huankuan_type_tv)
    TextView mHuankuanTypeTv;
    @BindView(R.id.danbao_type_tv)
    TextView mDanbaoTypeTv;

    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    private String mRequestTag = "";

    private Map<String, Object> mDataMap;
    private Map<String, Object> mDefaultMap;


    private SignHeTongAdapter mSignHeTongAdapter;
    private List<Map<String, Object>> mHeTongList = new ArrayList<>();
    private String mStatus = "";

    @Override
    public int getContentView() {
        return R.layout.activity_sign_loan;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.hetong_sign));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
            mStatus = bundle.getString("status");
        }

        LogUtilDebug.i("打印日志",mDataMap+"    mstatus"+mStatus);
        if (mStatus.equals("0")) {//未签署
            //tvStatus = "去签署";
            signInfoAction();
            mButton.setVisibility(View.VISIBLE);

        } else if (mStatus.equals("1")) {//已签署
            //tvStatus = "处理中";
            hasSignInfoAction();
            mButton.setVisibility(View.GONE);
        }

        mPageView.setContentView(mContent);
        //mPageView.showEmpty();
        mPageView.showLoading();
        mPageView.setReLoadingData(this);

        mRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 去签署 要获取的信息
     */
    private void signInfoAction() {

        mRequestTag = MethodUrl.signDetail;
        Map<String, String> map = new HashMap<>();
        map.put("flowdate", mDataMap.get("flowdate") + "");//业务日期
        map.put("flowid", mDataMap.get("flowid") + "");//业务流水
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.signDetail, map);
    }

    /**
     * 已经签署  要获取的信息
     */
    private void hasSignInfoAction() {

        mRequestTag = MethodUrl.shouxinDetail;
        Map<String, String> map = new HashMap<>();
        map.put("creditfile", mDataMap.get("creditfile") + "");//授信合同编号
        map.put("flowdate", mDataMap.get("flowdate") + "");//业务日期
        map.put("flowid", mDataMap.get("flowid") + "");//业务流水
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.shouxinDetail, map);
    }

    /**
     * 签署提交的信息
     */
    private void submitInfoAction() {

        mRequestTag = MethodUrl.signSubmit;
        Map<String, Object> map = new HashMap<>();
        map.put("flowdate", mDataMap.get("flowdate") + "");//业务日期
        map.put("flowid", mDataMap.get("flowid") + "");//业务流水
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.signSubmit, map);
    }

    /**
     * 未签署合同详情信息
     */
    private void initDefaultValue() {
        if (mDefaultMap == null || mDefaultMap.isEmpty()) {
            return;
        }
        String danwei = mDefaultMap.get("singleunit") + "";
        //Map<String, Object> qixianDW = SelectDataUtil.getMap(danwei, SelectDataUtil.getQixianDw());
        Map<String, Object> qixianDW = SelectDataUtil.getMap(danwei, SelectDataUtil.getNameCodeByType("limitUnit"));

        mLoanerText.setText(mDefaultMap.get("zifangnme") + "");//出借人
        mTimeLitmitText.setText(mDefaultMap.get("singlelimit") + "" + qixianDW.get("name"));//借款期限
        mShouxinMoneyText.setText(UtilTools.getRMBMoney(mDefaultMap.get("creditmoney") + ""));//授信额度
        mRateYearText.setText(UtilTools.getlilv(mDefaultMap.get("daiklilv")+"") );//年化利率

        mLilvTypeTv.setText(mDefaultMap.get("lvtypenm")+"");//利率方式
        mHuankuanTypeTv.setText(mDefaultMap.get("hktypenm")+"");//还款方式
        //Map<String, Object> danbaoMap = SelectDataUtil.getMap(mDefaultMap.get("assutype")+"", SelectDataUtil.getDanbaoType());
        Map<String, Object> danbaoMap = SelectDataUtil.getMap(mDefaultMap.get("assutype")+"",SelectDataUtil.getNameCodeByType("assuType"));
        mDanbaoTypeTv.setText(danbaoMap.get("name")+"");//担保类型

        String showDate = UtilTools.getStringFromSting2(mDefaultMap.get("enddate") + "", "yyyyMMdd", "yyyy-MM-dd");
        mShouxinDate.setText(showDate);//授信截止日

        List<Map<String, Object>> hLists = (List<Map<String, Object>>) mDefaultMap.get("contList");
        if (hLists != null) {
            mHeTongList.clear();
            mHeTongList.addAll(hLists);
        }
        if (mSignHeTongAdapter == null) {
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(RecyclerView.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            mSignHeTongAdapter = new SignHeTongAdapter(this, mHeTongList);
            mRecyclerView.setAdapter(mSignHeTongAdapter);

            mSignHeTongAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {
                    Map<String, Object> map = mSignHeTongAdapter.getDatas().get(position);
                    Intent intent = new Intent(SignLoanActivity.this, PDFLookActivity.class);
                    intent.putExtra("id", map.get("pdfurl") + "");
                    startActivity(intent);
                }
            });

        } else {
            mSignHeTongAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 已经签署合同详情信息
     */
    private void initHasDefaultValue() {
        if (mDefaultMap == null || mDefaultMap.isEmpty()) {
            return;
        }

        String danwei = mDefaultMap.get("singleunit") + "";
        //Map<String, Object> qixianDW = SelectDataUtil.getMap(danwei, SelectDataUtil.getQixianDw());
        Map<String, Object> qixianDW = SelectDataUtil.getMap(danwei, SelectDataUtil.getNameCodeByType("limitUnit"));

        mLoanerText.setText(mDefaultMap.get("zifangnme") + "");//出借人
        mTimeLitmitText.setText(mDefaultMap.get("singlelimit") + "" + qixianDW.get("name"));//借款期限 singleunit
        mShouxinMoneyText.setText(UtilTools.getRMBMoney(mDefaultMap.get("creditmoney") + ""));//授信额度
        mRateYearText.setText(UtilTools.getlilv(mDefaultMap.get("daiklilv")+""));//年化利率
        mShouxinDate.setText(UtilTools.getStringFromSting2(mDefaultMap.get("enddate") + "", "yyyyMMdd", "yyyy年MM月dd日"));//授信截止日


        mLilvTypeTv.setText(mDefaultMap.get("lvtypenm")+"");//利率方式
        mHuankuanTypeTv.setText(mDefaultMap.get("hktypenm")+"");//还款方式
        //Map<String, Object> danbaoMap = SelectDataUtil.getMap(mDefaultMap.get("assutype")+"", SelectDataUtil.getDanbaoType());
        Map<String, Object> danbaoMap = SelectDataUtil.getMap(mDefaultMap.get("assutype")+"",SelectDataUtil.getNameCodeByType("assuType"));

        mDanbaoTypeTv.setText(danbaoMap.get("name")+"");//担保类型


        List<Map<String, Object>> hLists = (List<Map<String, Object>>) mDefaultMap.get("contList");
        if (hLists != null) {
            mHeTongList.clear();
            mHeTongList.addAll(hLists);
        }
        if (mSignHeTongAdapter == null) {
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(RecyclerView.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            mSignHeTongAdapter = new SignHeTongAdapter(this, mHeTongList);
            mRecyclerView.setAdapter(mSignHeTongAdapter);

            mSignHeTongAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {
                    Map<String, Object> map = mSignHeTongAdapter.getDatas().get(position);
                    Intent intent = new Intent(SignLoanActivity.this, PDFLookActivity.class);
                    intent.putExtra("id", map.get("pdfurl") + "");
                    startActivity(intent);
                }
            });

        } else {
            mSignHeTongAdapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.back_img, R.id.bt_read_agree, R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.bt_read_agree:
                mButton.setEnabled(false);

                PermissionsUtils.requsetRunPermission(SignLoanActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        netWorkWarranty();
                    }

                    @Override
                    public void requestFailer() {
                        toast(R.string.failure);
                        mButton.setEnabled(true);
                    }
                },Permission.Group.CAMERA,Permission.Group.STORAGE);
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
        switch (mType) {
            case MethodUrl.shouxinDetail:

                mDefaultMap = tData;
                initHasDefaultValue();
                mPageView.showContent();
                break;
            case MethodUrl.signDetail://
                mDefaultMap = tData;
                initDefaultValue();
                mPageView.showContent();
                break;
            case MethodUrl.signSubmit:
                showToastMsg("签署成功");
                mButton.setEnabled(true);
                Intent intent = new Intent();
                intent.setAction(MbsConstans.BroadcastReceiverAction.DAIBAN_INFO_UPDATE);
                sendBroadcast(intent);
                finish();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.signDetail:
                        signInfoAction();
                        break;
                    case MethodUrl.signSubmit:
                        submitInfoAction();
                        break;
                    case MethodUrl.shouxinDetail:
                        hasSignInfoAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.signDetail:
                mPageView.showNetworkError();
                break;
            case MethodUrl.shouxinDetail:
                mPageView.showNetworkError();
                break;
        }
        mButton.setEnabled(true);
        dealFailInfo(map, mType);
    }


    private boolean isCheck = false;
    private static final int PAGE_INTO_LIVENESS = 101;

    private void enterNextPage() {
        //startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = null;
        Bundle bundle;
        if (requestCode == 1) {
            switch (resultCode) {//通过短信验证码
                case MbsConstans.FaceType.FACE_SIGN_HETONG:
                    bundle = data.getExtras();
                    if (bundle == null) {
                        isCheck = false;
                        mButton.setEnabled(true);
                    } else {
                        mButton.setEnabled(false);
                        isCheck = true;
                        submitInfoAction();
                    }
                    break;
                default:
                    mButton.setEnabled(true);
                    break;

            }

        } else if (requestCode == PAGE_INTO_LIVENESS) {//人脸识别返回来的数据
            if (resultCode == RESULT_OK) {
                bundle = data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_SIGN_HETONG);
                intent = new Intent(SignLoanActivity.this, ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent, 1);
            } else {
                mButton.setEnabled(true);
            }
        }
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {

//        final String uuid = ConUtil.getUUIDString(this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Manager manager = new Manager(SignLoanActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(SignLoanActivity.this);
//                manager.registerLicenseManager(licenseManager);
//                manager.takeLicenseFromNetwork(uuid);
//                if (licenseManager.checkCachedLicense() > 0) {
//                    //授权成功
//                    mHandler.sendEmptyMessage(1);
//                } else {
//                    //授权失败
//                    mHandler.sendEmptyMessage(2);
//                }
//            }
//        }).start();
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    enterNextPage();
                    break;
                case 2:
                    showToastMsg("人脸验证授权失败");
                    mButton.setEnabled(true);
                    break;
            }
        }
    };






    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reLoadingData() {
        if (mStatus.equals("0")) {//未签署
            //tvStatus = "去签署";
            signInfoAction();
            mButton.setVisibility(View.VISIBLE);
        } else if (mStatus.equals("1")) {//已签署
            //tvStatus = "处理中";
            hasSignInfoAction();
            mButton.setVisibility(View.GONE);
        }
        mPageView.showLoading();
    }
}