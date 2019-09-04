package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.MyClickableSpan;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;




import com.yanzhenjie.permission.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 共同借款人 审核页面
 */
public class PeopleCheckActivity extends BasicActivity implements RequestView ,ReLoadingData{


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
    @BindView(R.id.jkren_value_tv)
    TextView mJkrenValueTv;
    @BindView(R.id.jkjine_value_tv)
    TextView mJkjineValueTv;
    @BindView(R.id.jkqixian_value_tv)
    TextView mJkqixianValueTv;
    @BindView(R.id.hkzhouqi_value_tv)
    TextView mHkzhouqiValueTv;
    @BindView(R.id.lilvfangshi_value_tv)
    TextView mLilvfangshiValueTv;
    @BindView(R.id.dkzhonglei_value_tv)
    TextView mDkzhongleiValueTv;
    @BindView(R.id.dkyongtu_value_tv)
    TextView mDkyongtuValueTv;
    @BindView(R.id.hkfangshi_value_tv)
    TextView mHkfangshiValueTv;
    @BindView(R.id.chujieren_value_tv)
    TextView mChujierenValueTv;
    @BindView(R.id.tongyi_value_switch)
    Switch mTongyiValueSwitch;
    @BindView(R.id.refuse_edit_lay)
    RelativeLayout mResuseEditLay;
    @BindView(R.id.resuse_des_edit)
    EditText mResuseDesEdit;
    @BindView(R.id.count)
    TextView mCount;
    @BindView(R.id.xieyi_tv)
    TextView mXieyiTv;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.other_value_tv)
    TextView mOtherValueTv;
    @BindView(R.id.other_lay)
    LinearLayout mOtherLay;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    @BindView(R.id.cb_xieyi)
    CheckBox mXieyiCheckBox;
    @BindView(R.id.xiyi_lay)
    LinearLayout mXieyiLay;

    private int MAX_COUNT = 100;

    private String mRequestTag = "";

    private Map<String, Object> mDataMap;

    private Map<String, Object> mValueMap;

    private Map<String,Object> mZhengshuMap;

    private String mIsNO = "2";

    @Override
    public int getContentView() {
        return R.layout.activity_people_check;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.shenhe));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        mXieyiLay.setVisibility(View.GONE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.ZHENGSHU_UPDATE);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE);
        registerReceiver(mBroadcastReceiver,intentFilter);
        isInstallCer();
        // 先去掉监听器，否则会出现栈溢出
        mResuseDesEdit.addTextChangedListener(mTextWatcher);
        setLeftCount();
        mResuseDesEdit.setSelection(mResuseDesEdit.length());


        mResuseEditLay.setVisibility(View.GONE);
        mTongyiValueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIsNO = "2";
                    mResuseEditLay.setVisibility(View.GONE);
                } else {
                    mIsNO = "4";
                    mResuseEditLay.setVisibility(View.VISIBLE);
                }
            }
        });

        mPageView.setContentView(mContent);
        //mPageView.showEmpty();
        mPageView.showLoading();
        mPageView.setReLoadingData(this);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.ZHENGSHU_UPDATE)){
                isInstallCer();
            }else if (action.equals(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE)){
                getUserInfoAction();
            }
        }
    };
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 审核详情信息
     */
    private void getInfoAction() {

        mRequestTag = MethodUrl.prePeopleCheck;
        Map<String, String> map = new HashMap<>();
        map.put("patncode", mDataMap.get("patncode") + "");//合作方编号
        map.put("zifangbho", mDataMap.get("zifangbho") + "");//资方编号
        map.put("precreid", mDataMap.get("precreid") + "");//预授信申请ID
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.prePeopleCheck, map);
    }

    /**
     * 是否安装证书
     */
    private void isInstallCer() {

        mRequestTag = MethodUrl.isInstallCer;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.isInstallCer, map);
    }

    /**
     * 获取用户基本信息
     */
    public void getUserInfoAction() {
        mRequestTag = MethodUrl.USER_INFO;

        Map<String,Object> map = new HashMap<>();
        Map<String,String> mHeaderMap = new HashMap<String,String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_INFO, map);
    }


    private void submitAction() {
        if (MbsConstans.USER_MAP != null){
            String ss =  MbsConstans.USER_MAP.get("cmpl_info")+"";
            String zhengshu = MbsConstans.USER_MAP.get("auth")+"";
            if (ss.equals("1")){//是否完善信息（0：未完善，1：已完善

                if (zhengshu.equals("1")){//是否认证（0：未认证，1：已认证

                }else {
                    if (mZhengshuMap != null && !mZhengshuMap.isEmpty()){
                        String state = mZhengshuMap.get("state")+"";
                        Intent intent = new Intent(PeopleCheckActivity.this,IdCardSuccessActivity.class);
                        intent.putExtra("verify_type",mZhengshuMap.get("verify_type")+"");
                        intent.putExtra(MbsConstans.FaceType.FACE_KEY,MbsConstans.FaceType.FACE_INSTALL);
                        startActivity(intent);
                        mBtnSubmit.setEnabled(true);
                        return;
                    }else {
                        showToastMsg("请先完善认证信息");
                        mBtnSubmit.setEnabled(true);
                        return;
                    }
                }
            }else {
                Intent intent = new Intent(this,PerfectInfoActivity.class);
                intent.putExtra("type","3");
                startActivity(intent);
                showToastMsg("请先完善信息");
                mBtnSubmit.setEnabled(true);
                return;
            }
        }else {
            MainActivity.mInstance.getUserInfoAction();
            showToastMsg("获取用户信息失败,请重新获取");
            mBtnSubmit.setEnabled(true);
            return;

        }


        mRequestTag = MethodUrl.peopleCheckSure;
        Map<String, Object> map = new HashMap<>();
        map.put("patncode", mDataMap.get("patncode") + "");//合作方编号
        map.put("zifangbho", mDataMap.get("zifangbho") + "");//资方编号
        map.put("precreid", mDataMap.get("precreid") + "");//预授信申请ID
        map.put("state", mIsNO);//审核状态 2：通过 4：驳回

        if (mIsNO.equals("2")){

        }else {
            if (UtilTools.isEmpty(mResuseDesEdit,"驳回理由")){
                showToastMsg("驳回理由不能为空");
                mBtnSubmit.setEnabled(true);
                return;
            }else {
                map.put("reason", mResuseDesEdit.getText()+"");//驳回原因
            }
        }
        if (!mXieyiCheckBox.isChecked()){
            showToastMsg(getResources().getString(R.string.xieyi_tips));
            mBtnSubmit.setEnabled(true);
            return;
        }

        if (isCheck){

        }else {

            PermissionsUtils.requsetRunPermission(PeopleCheckActivity.this, new RePermissionResultBack() {
                @Override
                public void requestSuccess() {
                    netWorkWarranty();
                }

                @Override
                public void requestFailer() {
                    toast(R.string.failure);
                    mBtnSubmit.setEnabled(true);
                }
            },Permission.Group.CAMERA,Permission.Group.STORAGE);


            mBtnSubmit.setEnabled(true);
            return;
        }

        LogUtilDebug.i("驳回参数",map);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.peopleCheckSure, map);
    }


    private void initValue() {
        mJkrenValueTv.setText(mValueMap.get("firmname") + "");
        mJkjineValueTv.setText(UtilTools.getRMBMoney(mValueMap.get("creditmoney") + ""));
        mJkqixianValueTv.setText(mValueMap.get("singlelimit") + " 月");

        //Map<String, Object> huanKuanType = SelectDataUtil.getMap(mValueMap.get("hktype") + "",SelectDataUtil.getHkType());
        Map<String, Object> huanKuanType = SelectDataUtil.getMap(mValueMap.get("hktype") + "",SelectDataUtil.getNameCodeByType("repayWay"));
        mHkfangshiValueTv.setText(huanKuanType.get("name") + "");

        //Map<String, Object> zhouqiMap = SelectDataUtil.getMap(mValueMap.get("interestaccmode") + "", SelectDataUtil.getHkZhouqi());
        Map<String, Object> zhouqiMap = SelectDataUtil.getMap(mValueMap.get("interestaccmode") + "",SelectDataUtil.getNameCodeByType("repayCycle"));
        mHkzhouqiValueTv.setText(zhouqiMap.get("name") + "");
        if ((mValueMap.get("interestaccmode")+"").equals("19")){
            mOtherLay.setVisibility(View.VISIBLE);
            mOtherValueTv.setText(mValueMap.get("interestaccnm")+"");
        }else {
            mOtherLay.setVisibility(View.GONE);
        }

        Map<String, Object> lilvMap = SelectDataUtil.getMap(mValueMap.get("lvtype") + "", SelectDataUtil.getLilvType());
        mLilvfangshiValueTv.setText(lilvMap.get("name") + "");

        //Map<String, Object> dkZhongleiMap = SelectDataUtil.getMap(mValueMap.get("reqloantp") + "",SelectDataUtil.getDaikuanType());
        Map<String, Object> dkZhongleiMap = SelectDataUtil.getMap(mValueMap.get("reqloantp") + "",SelectDataUtil.getNameCodeByType("loanType"));
        mDkzhongleiValueTv.setText(dkZhongleiMap.get("name") + "");

        //Map<String, Object> dkYongtuMap = SelectDataUtil.getMap(mValueMap.get("loanuse") + "", SelectDataUtil.getDaikuanUse());
        Map<String, Object> dkYongtuMap = SelectDataUtil.getMap(mValueMap.get("loanuse") + "",SelectDataUtil.getNameCodeByType("loanUse"));
        mDkyongtuValueTv.setText(dkYongtuMap.get("name") + "");

        mChujierenValueTv.setText(mValueMap.get("zifangnme") + "");

        initXieyiView();
    }

    private void initXieyiView() {
        mXieyiLay.setVisibility(View.VISIBLE);
        mXieyiCheckBox.setChecked(true);
        String xiyiStr = "已阅读并同意签署";
        if (mValueMap != null) {
            List<Map<String, Object>> xieyiList = (List<Map<String, Object>>) mValueMap.get("contList");
            for (int i =0;i<xieyiList.size();i++) {
                Map<String,Object> map = xieyiList.get(i);
                final String str = map.get("pdfname") + "";
                if (i == (xieyiList.size()-1)){
                    xiyiStr = xiyiStr + "《" + str + "》";
                }else {
                    xiyiStr = xiyiStr + "《" + str + "》、";
                }
            }
            SpannableString sp = new SpannableString(xiyiStr);

            for (final Map map : xieyiList) {
                final String str = map.get("pdfname") + "";
                setClickableSpan(sp, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PeopleCheckActivity.this, PDFLookActivity.class);
                        intent.putExtra("id", map.get("pdfurl") + "");
                        startActivity(intent);
                    }
                }, xiyiStr, "《" + str + "》");
            }
            mXieyiTv.setText(sp);
        }
        //添加点击事件时，必须设置
        mXieyiTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private SpannableString setClickableSpan(SpannableString sp, View.OnClickListener l, String str, String span) {
        sp.setSpan(new MyClickableSpan(0xff1c91ea, l), str.indexOf(span), str.indexOf(span) + span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    @OnClick({R.id.back_img, R.id.btn_submit,R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.btn_submit:
                mBtnSubmit.setEnabled(false);
                submitAction();
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
            case MethodUrl.USER_INFO://用户信息 //{auth=1, firm_kind=0, head_pic=default, name=刘英超, tel=151****3298, idno=4107****3616, cmpl_info=0}
                MbsConstans.USER_MAP = tData;
                SPUtils.put(PeopleCheckActivity.this, MbsConstans.SharedInfoConstans.LOGIN_INFO,   JSONUtil.getInstance().objectToJson(MbsConstans.USER_MAP));
                //showUpdateDialog();
                break;
            case MethodUrl.peopleCheckSure:
                mBtnSubmit.setEnabled(true);
                isCheck = false;
                showToastMsg("审核完成");
                Intent intent = new Intent();
                intent.setAction(MbsConstans.BroadcastReceiverAction.DAIBAN_INFO_UPDATE);
                sendBroadcast(intent);
                finish();
                break;
            case MethodUrl.isInstallCer://{verify_type=FACE, state=0}
                mZhengshuMap = tData;
                getInfoAction();
                break;
            case MethodUrl.prePeopleCheck://
                mPageView.showContent();
                mValueMap = tData;
                initValue();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.prePeopleCheck:
                        getInfoAction();
                        break;
                    case MethodUrl.isInstallCer:
                        isInstallCer();
                        break;
                    case MethodUrl.peopleCheckSure:
                        submitAction();
                        break;
                    case MethodUrl.USER_INFO:
                        getUserInfoAction();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.isInstallCer://{verify_type=FACE, state=0}
                mPageView.showNetworkError();
                break;
            case MethodUrl.prePeopleCheck://
                mPageView.showNetworkError();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                break;
            case MethodUrl.peopleCheckSure:
                isCheck = false;
                mBtnSubmit.setEnabled(true);
                break;
        }
        dealFailInfo(map,mType);
    }


    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;

        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = mResuseDesEdit.getSelectionStart();
            editEnd = mResuseDesEdit.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            mResuseDesEdit.removeTextChangedListener(mTextWatcher);

            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            mResuseDesEdit.setText(s);
            mResuseDesEdit.setSelection(editStart);

            // 恢复监听器
            mResuseDesEdit.addTextChangedListener(mTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    };


    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                //len++;
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 刷新剩余输入字数,最大值新浪微博是140个字，人人网是200个字
     */
    private void setLeftCount() {
        mCount.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }

    /**
     * 获取用户输入的分享内容字数
     *
     * @return
     */
    private long getInputCount() {
        return calculateLength(mResuseDesEdit.getText().toString());
    }

    @Override
    public void reLoadingData() {
        getInfoAction();
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
        if (requestCode == 1 ) {
            switch (resultCode){//通过短信验证码  安装证书
                case MbsConstans.FaceType.FACE_PEOPLE_CHECK:
                    bundle = data.getExtras();
                    if (bundle == null){
                        isCheck = false;
                        mBtnSubmit.setEnabled(true);
                    }else {
                        mBtnSubmit.setEnabled(false);
                        isCheck = true;
                        submitAction();
                    }
                    break;
                default:
                    mBtnSubmit.setEnabled(true);
                    break;

            }

        }else if (requestCode == PAGE_INTO_LIVENESS){//人脸识别返回来的数据
            if (resultCode == RESULT_OK ) {
                bundle=data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_PEOPLE_CHECK);
                intent = new Intent(PeopleCheckActivity.this,ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent,1);
            }else {
                mBtnSubmit.setEnabled(true);
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
//                Manager manager = new Manager(PeopleCheckActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(PeopleCheckActivity.this);
//                manager.registerLicenseManager(licenseManager);
//                manager.takeLicenseFromNetwork(uuid);
//                if (licenseManager.checkCachedLicense() > 0) {
//                    //授权成功
//                    mHandler.sendEmptyMessage(1);
//                }else {
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
                    mBtnSubmit.setEnabled(true);
                    break;
            }
        }
    };







    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
