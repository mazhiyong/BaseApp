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
import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.SwipeMenuAdapter2;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.db.IndexData;
import com.lr.biyou.mywidget.dialog.MySelectDialog;
import com.lr.biyou.listener.MyClickableSpan;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.DataHolder;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.Permission;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * 申请额度 界面
 */
public class ApplyAmountActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.money_edit)
    EditText mMoneyEdit;
    @BindView(R.id.qixian_value_tv)
    TextView mQixianValueTv;
    @BindView(R.id.qixian_lay)
    CardView mQixianLay;
    @BindView(R.id.zhouqi_value_tv)
    TextView mZhouqiValueTv;
    @BindView(R.id.zhongqi_lay)
    CardView mZhongqiLay;
    @BindView(R.id.lilv_value_tv)
    TextView mLilvValueTv;
    @BindView(R.id.lilv_type_lay)
    CardView mLilvTypeLay;
    @BindView(R.id.daikuan_use_value_tv)
    TextView mDaikuanUseValueTv;
    @BindView(R.id.daikuan_use_lay)
    CardView mDaikuanUseLay;
    @BindView(R.id.daikuan_kind_value_tv)
    TextView mDaikuanKindValueTv;
    @BindView(R.id.daikuan_kind_lay)
    CardView mDaikuanKindLay;
    @BindView(R.id.pay_type_value_tv)
    TextView mPayTypeValueTv;
    @BindView(R.id.pay_type_lay)
    CardView mPayTypeLay;
    @BindView(R.id.fujian_lay)
    CardView mFujianLay;
    @BindView(R.id.add_same_people_lay)
    CardView mAddSamePeopleLay;
    @BindView(R.id.other_zhognqi_line)
    View mOtherZhognqiLine;
    @BindView(R.id.other_zhouqi_eddit)
    EditText mOtherZhouqiEddit;
    @BindView(R.id.other_zhonqi_lay)
    CardView mOtherZhonqiLay;
    @BindView(R.id.same_people_list)
    LRecyclerView mRefreshListView;
    @BindView(R.id.scrollView_content)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.but_submit)
    Button mButSubmit;
    @BindView(R.id.has_upload_tv)
    TextView mHasUploadTv;
    @BindView(R.id.add_file_tv)
    TextView mAddFileTv;
    @BindView(R.id.file_num_tv)
    TextView mFileNumTv;
    @BindView(R.id.add_fujian_title)
    TextView mAddFujianTitleTv;
    @BindView(R.id.xieyi_tv)
    TextView mXieyiTv;
    @BindView(R.id.cb_xieyi)
    CheckBox mXieyiCheckBox;
    @BindView(R.id.xieyi_lay)
    LinearLayout mXieyiLay;
    @BindView(R.id.add_gtpeople_Lay)
    LinearLayout mAddGtpeopleLay;

    @BindView(R.id.has_fujian_lay)
    CardView mHasFujianLay;
    @BindView(R.id.bulu_lay)
    LinearLayout mBuLuLay;
    @BindView(R.id.bulu_divide_view)
    View mBuluDivideView;
    @BindView(R.id.has_upload_tv2)
    TextView mHasUploadTv2;
    @BindView(R.id.add_file_tv2)
    TextView mAddFileTv2;
    @BindView(R.id.file_num_tv2)
    TextView mFileNumTv2;
    @BindView(R.id.add_fujian_lay)
    LinearLayout mAddFujinaLay;



    private Map<String, Object> mPayZhouQiMap;
    private Map<String, Object> mLilvMap;
    private Map<String, Object> mDaikuanUseMap;
    private Map<String, Object> mDaikuanZhonglMap;
    private Map<String, Object> mJieKuanQxianMap;

    private Map<String,Object> mHuanKuanTypeMap;

    private String mAuthCode = "";

    private Map<String, Object> mHezuoMap;
    private Map<String, Object> mConfigMap;

    private String mIsNOPeople = "";

    private List<Map<String, Object>> mPeopleList = new ArrayList<>();

    private SwipeMenuAdapter2 mSwipeMenuAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private String mRequestTag = "";

    private Map<String,Object> mDefaultMap ;

    private int mIsModify ;

    private String mPreId = "";

    private IndexData mIndexData;

    private String mKind = "";

    @Override
    public int getContentView() {
        return R.layout.activity_apply_amount;
    }


    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mHezuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
            if (bundle.containsKey("TYPE")){
                mIsModify = bundle.getInt("TYPE");
                mPreId = bundle.getString("precreid");

            }
        }
        mIndexData = IndexData.getInstance();
        mTitleText.setText(getResources().getString(R.string.get_my_num));
        mXieyiLay.setVisibility(View.GONE);
        UtilTools.setMoneyEdit(mMoneyEdit,0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.FILE_TIP_ACTION);
        registerReceiver(receiver, filter);

        mAddFileTv.setVisibility(View.VISIBLE);
        mHasUploadTv.setVisibility(View.GONE);
        mFileNumTv.setVisibility(View.GONE);

        mOtherZhonqiLay.setVisibility(View.GONE);
        mOtherZhognqiLine.setVisibility(View.GONE);
        initDialog();


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        if (MbsConstans.USER_MAP != null){
            mKind = MbsConstans.USER_MAP.get("firm_kind")+"";
        }
        if (mKind.equals("1")){
            mAddGtpeopleLay.setVisibility(View.GONE);
        }else {
            mAddGtpeopleLay.setVisibility(View.VISIBLE);
        }

        if (mIsModify == 1){
            getModifyAction();
            mBuLuLay.setVisibility(View.VISIBLE);
            mBuluDivideView.setVisibility(View.VISIBLE);
            mAddFujianTitleTv.setText(getResources().getString(R.string.upload_bulu));
        }else {
            getConfigAction();// 查询授信申请配置
            mBuLuLay.setVisibility(View.GONE);
            mBuluDivideView.setVisibility(View.GONE);
            mAddFujianTitleTv.setText(getResources().getString(R.string.upload_fuji));
        }

        mDaikuanKindLay.setEnabled(false);
        if (MbsConstans.USER_MAP != null ){
            String kind = MbsConstans.USER_MAP.get("firm_kind")+"";
            if (kind.equals("1")){//客户类型（0：车主，1：物流公司）
                mDaikuanZhonglMap = SelectDataUtil.getMap("101010",SelectDataUtil.getNameCodeByType("loanType"));
            }else if (kind.equals("0")){
                mDaikuanZhonglMap = SelectDataUtil.getMap("202010",SelectDataUtil.getNameCodeByType("loanType"));
            }else {
                mDaikuanKindLay.setEnabled(true);
            }
            mDaikuanKindValueTv.setText(mDaikuanZhonglMap.get("name")+"");
        }
        mLilvMap = SelectDataUtil.getMap("1",SelectDataUtil.getLilvType());
    }

    @Override
    public void viewEnable(){
        //mButSubmit.setEnabled(true);
    }

    /**
     * 得到预授信详情  修改申请授信信息  比如驳回了要修改
     */
    private void getModifyAction(){

        mRequestTag = MethodUrl.reqShouxinDetail;
        Map<String, String> map = new HashMap<>();
        map.put("precreid",mPreId+ "");//预授信申请ID
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.reqShouxinDetail, map);
    }

    /**
     * 查询授信申请配置
     */
    private void getConfigAction() {

        mRequestTag = MethodUrl.creConfig;
        Map<String, String> map = new HashMap<>();
        map.put("patncode",mHezuoMap.get("patncode")+"");
        map.put("zifangbho",mHezuoMap.get("zifangbho")+"");
//        map.put("patncode", "CSHEZUOF");
//        map.put("zifangbho", "BLOF1212");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.creConfig, map);
    }


    private void submitDataAction() {

        if (UtilTools.isEmpty(mMoneyEdit,"金额")){
            showToastMsg("请填写金额");
            mButSubmit.setEnabled(true);
            return;
        }

        if (mJieKuanQxianMap == null || mJieKuanQxianMap.isEmpty()) {
            UtilTools.isEmpty(mQixianValueTv, "借款期限");
            showToastMsg("请选择借款期限");
            mButSubmit.setEnabled(true);
            return;
        }
        if (mHuanKuanTypeMap == null || mHuanKuanTypeMap.isEmpty()) {
            UtilTools.isEmpty(mPayTypeValueTv, "还款方式");
            showToastMsg("请选择还款方式");
            mButSubmit.setEnabled(true);
            return;
        }
        if (mPayZhouQiMap == null || mPayZhouQiMap.isEmpty()) {
            UtilTools.isEmpty(mZhouqiValueTv, "还款周期");
            showToastMsg("请选择还款周期");
            mButSubmit.setEnabled(true);
            return;
        } else {
            if ((mPayZhouQiMap.get("code") + "").equals("19")) {
                if (UtilTools.isEmpty(mOtherZhouqiEddit, "还款周期")) {
                    showToastMsg("请输入还款周期内容");
                    mButSubmit.setEnabled(true);
                    return;
                }
            }
        }

        if (mLilvMap == null || mLilvMap.isEmpty()) {
            UtilTools.isEmpty(mLilvValueTv, "利率");
            showToastMsg("请选择利率");
            mButSubmit.setEnabled(true);
        }
        if (mDaikuanZhonglMap == null || mDaikuanZhonglMap.isEmpty()) {
            UtilTools.isEmpty(mDaikuanKindValueTv, "贷款种类");
            showToastMsg("请选择贷款种类");
            mButSubmit.setEnabled(true);
            return;
        }
        if (mDaikuanUseMap == null || mDaikuanUseMap.isEmpty()) {
            UtilTools.isEmpty(mDaikuanUseValueTv, "贷款用途");
            showToastMsg("请选择贷款用途");
            mButSubmit.setEnabled(true);
            return;
        }


        if (mIsNOPeople.equals("1")){//共同借款人校验（0：没有共借人 1：可以有共借人）
            if (mPeopleList == null || mPeopleList.size() != 1){
                showToastMsg("共同借款人必须添加一个");
                mButSubmit.setEnabled(true);
                return;
            }
        }else {
            if (mPeopleList == null ){
                mPeopleList = new ArrayList<>();
            }
        }



        mRequestTag = MethodUrl.applySubmit;
        Map<String, Object> map = new HashMap<>();
        map.put("patncode",mHezuoMap.get("patncode")+"");
        map.put("zifangbho",mHezuoMap.get("zifangbho")+"");
        map.put("precreid", mConfigMap.get("precreid") + "");//预授信申请ID precreid
        map.put("creditmoney", mMoneyEdit.getText() + "");//申请金额
        map.put("singlelimit", mJieKuanQxianMap.get("code") + "");//申请期限
        map.put("interestaccmode", mPayZhouQiMap.get("code") + "");//还款周期

        String otherZhouqi = "";
        if ((mPayZhouQiMap.get("code") + "").equals("19")) {
            otherZhouqi = mOtherZhouqiEddit.getText() + "";
            if (UtilTools.isEmpty(mOtherZhouqiEddit, "还款周期")) {
                showToastMsg("请输入还款周期内容");
                mButSubmit.setEnabled(true);
                return;
            } else {
                map.put("interestaccnm", otherZhouqi);//还款周期-其他
            }
        }
        map.put("lvtype", mLilvMap.get("code") + "");//利率方式 0：浮动 1：固定
        map.put("reqloantp", mDaikuanZhonglMap.get("code") + "");//贷款种类 Y01：个人综合消费贷款 Y02：个人经营性贷款 Y03：个人信用贷款
        map.put("loanuse", mDaikuanUseMap.get("code") + "");//贷款用途 0：个人经营 1：个人授信额度服务 2：个人综合消费 3：商品交易 4：资金周转
        map.put("hktype", mHuanKuanTypeMap.get("code") + "");//还款方式

        if (mIsModify == 1){

            map.put("gtList", mPeopleList);//共同借款人列表
            map.put("modflag", "1");//新增修改标识 （0：新增 1：修改）
            if (mFileList == null && mFileList.size()<=0){
                mFileList = new ArrayList<>();
                map.put("contList", mFileList);//附件列表
            }else {
                map.put("contList", mFileList);//附件列表
            }
        }else {
            map.put("modflag", "0");//新增修改标识 （0：新增 1：修改）
            map.put("gtList", mPeopleList);//共同借款人列表
            List<Map<String,Object>>  fileConfigList = (List<Map<String,Object>>) mConfigMap.get("contList");
            for (Map map1 : fileConfigList){
                String sign = map1.get("isreq")+"";//是否必传(0:否1是)
                String connpk = map1.get("connpk")+"";

                if (sign.equals("1")){
                    if (mFileList == null || mFileList.size()<=0){
                        mButSubmit.setEnabled(true);
                        showToastMsg("请上传必传的附件");
                        return;
                    }else {
                        for (Map map2 : mFileList){
                            String code = map2.get("connpk")+"";
                            List<Map<String,Object>> files = (List<Map<String,Object>>) map2.get("files");
                            if (code.equals(connpk)){
                                if (files == null || files.size()<=0){
                                    mButSubmit.setEnabled(true);
                                    showToastMsg("请上传必传的附件");
                                    return;
                                }

                            }
                        }
                    }

                }else {

                }
            }
            map.put("contList", mFileList);//附件列表
        }

        LogUtilDebug.i("打印log日志","申请额度提交参数"+map);

        if (!mXieyiCheckBox.isChecked()){
            showToastMsg(getResources().getString(R.string.xieyi_tips));
            return;
        }

        if (isCheck){//是否已经人脸认证

        }else {
            PermissionsUtils.requsetRunPermission(ApplyAmountActivity.this, new RePermissionResultBack() {
                @Override
                public void requestSuccess() {
                    netWorkWarranty();
                }

                @Override
                public void requestFailer() {
                    toast(R.string.failure);
                    mButSubmit.setEnabled(true);
                }
            },Permission.Group.CAMERA,Permission.Group.STORAGE);


            mButSubmit.setEnabled(true);
            return;
        }

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.applySubmit, map);
    }


    private void initFujianView(){
        List<Map<String,Object>> mList = (List<Map<String,Object>>) mConfigMap.get("contList") ;
        if (mList.size() == 0 ){
            mAddFujinaLay.setVisibility(View.GONE);
        }else {
            mAddFujinaLay.setVisibility(View.VISIBLE);
        }
    }


    private void initModifyValue(){

        mMoneyEdit.setText(UtilTools.getShuziMoney(mDefaultMap.get("creditmoney")+""));
        //mJieKuanQxianMap = SelectDataUtil.getMap(mDefaultMap.get("singlelimit")+"",SelectDataUtil.jieKuanLimit());
        mJieKuanQxianMap = SelectDataUtil.getMap(mDefaultMap.get("singlelimit")+"",SelectDataUtil.getNameCodeByType("loanLimit"));
        mQixianValueTv.setText(mJieKuanQxianMap.get("name")+"");//借款期限


        //mPayZhouQiMap  = SelectDataUtil.getMap(mDefaultMap.get("interestaccmode")+"",SelectDataUtil.getHkZhouqi());
        mPayZhouQiMap  = SelectDataUtil.getMap(mDefaultMap.get("interestaccmode")+"",SelectDataUtil.getNameCodeByType("repayCycle"));
        mZhouqiValueTv.setText(mPayZhouQiMap.get("name")+"");
        if ((mPayZhouQiMap.get("code")+"").equals("19")){
            mOtherZhouqiEddit.setText(mDefaultMap.get("interestaccnm")+"");
            mOtherZhonqiLay.setVisibility(View.VISIBLE);
            mOtherZhognqiLine.setVisibility(View.VISIBLE);
        }else {
            mOtherZhonqiLay.setVisibility(View.GONE);
            mOtherZhognqiLine.setVisibility(View.GONE);
        }

        mLilvMap = SelectDataUtil.getMap(mDefaultMap.get("lvtype")+"",SelectDataUtil.getLilvType());
        mLilvValueTv.setText(mLilvMap.get("name")+"");

        //mDaikuanZhonglMap = SelectDataUtil.getMap(mDefaultMap.get("reqloantp")+"",SelectDataUtil.getDaikuanType());
        mDaikuanZhonglMap = SelectDataUtil.getMap(mDefaultMap.get("reqloantp")+"", SelectDataUtil.getNameCodeByType("loanType"));
        mDaikuanKindValueTv.setText(mDaikuanZhonglMap.get("name")+"");

        //mDaikuanUseMap = SelectDataUtil.getMap(mDefaultMap.get("loanuse")+"",SelectDataUtil.getDaikuanUse());
        mDaikuanUseMap = SelectDataUtil.getMap(mDefaultMap.get("loanuse")+"",SelectDataUtil.getNameCodeByType("loanUse"));
        mDaikuanUseValueTv.setText(mDaikuanUseMap.get("name")+"");

        //mHuanKuanTypeMap = SelectDataUtil.getMap(mDefaultMap.get("hktype")+"",SelectDataUtil.getHkType());
        mHuanKuanTypeMap = SelectDataUtil.getMap(mDefaultMap.get("hktype")+"",SelectDataUtil.getNameCodeByType("repayWay"));
        mPayTypeValueTv.setText(mHuanKuanTypeMap.get("name")+"");


        if (mDefaultMap.get("gtList") != null){
            mPeopleList = new ArrayList<>((List<Map<String,Object>>) mDefaultMap.get("gtList")) ;
        }

        responseData();

        List<Map<String,Object>> mFileTypeList = (List<Map<String,Object>>) mDefaultMap.get("contList");

        List<Map<String,Object>> mHasFile = ( List<Map<String,Object>>) mDefaultMap.get("existFileList");
        int num = 0;
        if (mHasFile != null ){
            for (Map<String,Object> fileMap : mHasFile){
                List<Map<String,Object>> files = (List<Map<String,Object>>) fileMap.get("files");
                for (Map<String,Object> map:files){
                    List<Map<String,Object>> timeList = (List<Map<String,Object>>) map.get("optFiles");
                    num = num+timeList.size();
                }
            }
        }

        if (num != 0){
            mAddFileTv2.setVisibility(View.GONE);
            mHasUploadTv2.setVisibility(View.VISIBLE);
            mFileNumTv2.setVisibility(View.VISIBLE);

        }else {
            mAddFileTv2.setVisibility(View.GONE);
            mHasUploadTv2.setVisibility(View.VISIBLE);
            mFileNumTv2.setVisibility(View.VISIBLE);

        }
        mFileNumTv2.setText(num+"个");


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

        //{smstoken=sms_token@3948602038bb8ecf912e0ede4a577ebd, send_tel=151****3298}
        Intent intent;
        switch (mType) {
            case MethodUrl.reqShouxinDetail:

                mDefaultMap = tData;
                mConfigMap = tData;
                mConfigMap.put("precreid",mPreId);
                mIsNOPeople = mConfigMap.get("needgt")+"";
                mHezuoMap = new HashMap<>();
                mHezuoMap.put("patncode",mDefaultMap.get("patncode")+"");
                mHezuoMap.put("zifangbho",mDefaultMap.get("zifangbho")+"");
                initModifyValue();
                initFujianView();
                break;
            case MethodUrl.applySubmit:
                isCheck = false;
                mButSubmit.setEnabled(true);
                showToastMsg("申请成功");

                if (mIsModify == 1){//是驳回修改的
                    Intent intent1 = new Intent();
                    intent1.setAction(MbsConstans.BroadcastReceiverAction.DAIBAN_INFO_UPDATE);
                    sendBroadcast(intent1);
                }

                intent = new Intent(ApplyAmountActivity.this,SubmitResultActivity.class);
                intent.putExtra(MbsConstans.ResultType.RESULT_KEY,MbsConstans.ResultType.RESULT_APPLY_MONEY);
                startActivity(intent);
                finish();
                break;
            case MethodUrl.creConfig:
                mConfigMap = tData;
                initFujianView();
                mIsNOPeople = mConfigMap.get("needgt")+"";
                initXieyiView();
                mButSubmit.setEnabled(true);
                if (mIsModify == 1){
                    getModifyAction();
                }
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.creConfig:
                        getConfigAction();
                        break;
                    case MethodUrl.applySubmit:
                        submitDataAction();
                        break;
                    case MethodUrl.reqShouxinDetail:
                        getModifyAction();
                        break;
                }
                break;

        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        switch (mType) {
            case MethodUrl.applySubmit:
                isCheck = false;
                mButSubmit.setEnabled(true);
                break;
            case MethodUrl.creConfig:
                mButSubmit.setEnabled(false);
                break;
        }
        dealFailInfo(map,mType);
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {

        String value = map.get("name") + "";
        switch (type) {
            case 100:
                mPayZhouQiMap = map;
                mZhouqiValueTv.setText(value);
                if ((mPayZhouQiMap.get("code") + "").equals("19")) {
                    mOtherZhonqiLay.setVisibility(View.VISIBLE);
                    mOtherZhognqiLine.setVisibility(View.VISIBLE);
                } else {
                    mOtherZhonqiLay.setVisibility(View.GONE);
                    mOtherZhognqiLine.setVisibility(View.GONE);
                    mOtherZhouqiEddit.setError(null, null);
                }
                mZhouqiValueTv.setError(null, null);
                break;
            case 101:
                mLilvMap = map;
                mLilvValueTv.setText(value);
                mLilvValueTv.setError(null, null);

                break;
            case 102:
                mDaikuanUseMap = map;
                mDaikuanUseValueTv.setText(value);
                mDaikuanUseValueTv.setError(null, null);

                break;
            case 103:
                mDaikuanZhonglMap = map;
                mDaikuanKindValueTv.setText(value);
                mDaikuanKindValueTv.setError(null, null);
                break;
            case 104:
                mJieKuanQxianMap = map;
                mQixianValueTv.setText(value);
                mQixianValueTv.setError(null, null);
                break;
            case 105:
                mHuanKuanTypeMap = map;
                mPayTypeValueTv.setText(value);
                mPayTypeValueTv.setError(null, null);
                break;
        }
    }


    @OnClick({R.id.qixian_lay, R.id.zhongqi_lay, R.id.lilv_type_lay, R.id.daikuan_use_lay, R.id.daikuan_kind_lay,R.id.has_fujian_lay,
            R.id.pay_type_lay, R.id.back_img, R.id.fujian_lay, R.id.add_same_people_lay, R.id.but_submit,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.qixian_lay:
                mJieKuanLimitDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.zhongqi_lay:
                showPayZhouqiDialog();
                break;
            case R.id.lilv_type_lay:
                showLilvDialog();
                break;
            case R.id.daikuan_use_lay:
                showDaikuanUseDialog();
                break;
            case R.id.daikuan_kind_lay:
                showDaikuanKindDialog();
                break;
            case R.id.pay_type_lay:
                showHuanKuanDialog();
                break;
            case R.id.has_fujian_lay:
                if (mIsModify == 1 && mDefaultMap != null){
                    List<Map<String,Object>> mHasFile = ( List<Map<String,Object>>) mDefaultMap.get("existFileList");
                    if (mHasFile == null){
                        mHasFile = new ArrayList<>();
                    }
                    intent = new Intent(ApplyAmountActivity.this, ModifyFileActivity.class);
                    //intent.putExtra("DATA",(Serializable) mHasFile);
                    DataHolder.getInstance().save("fileList", mHasFile);
                    startActivity(intent);
                }else {
                }
                break;
            case R.id.fujian_lay:
                PermissionsUtils.requsetRunPermission(ApplyAmountActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        if (mConfigMap != null){
                            Intent intent = new Intent(ApplyAmountActivity.this, AddFileActivity.class);
                            intent.putExtra("DATA", (Serializable) mConfigMap);
                            intent.putExtra("TYPE","1");
                            startActivityForResult(intent, 300);
                        }
                    }

                    @Override
                    public void requestFailer() {

                    }
                },Permission.Group.CAMERA,Permission.Group.STORAGE);

                /*intent = new Intent(ApplyAmountActivity.this, AddFileActivity.class);
                intent.putExtra("DATA", (Serializable) mConfigMap);
                intent.putExtra("TYPE","1");
                startActivityForResult(intent, 300);*/
                break;
            case R.id.add_same_people_lay:
                intent = new Intent(ApplyAmountActivity.this, AddSamePeopleActivity.class);
                intent.putExtra("DATA", (Serializable) mHezuoMap);
                startActivityForResult(intent, 200);
                break;
            case R.id.but_submit:
/*
                netWorkWarranty();
*/
                mButSubmit.setEnabled(false);
                submitDataAction();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
               // case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                   // List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    //adapter.setList(selectList);
                    // adapter.notifyDataSetChanged();
               //     break;
                case 200:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String gtfirmname = bundle.getString("name");
                        String gtjkzjno = bundle.getString("idno");
                        String gtjkrel = bundle.getString("guanxi");
                        String gtcustid = bundle.getString("custid");
                        String gtjkrelnm = bundle.getString("other");
                        Map<String, Object> map = new HashMap<>();
                        map.put("gtfirmname", gtfirmname);
                        map.put("gtjkzjno", gtjkzjno);
                        map.put("gtjkrel", gtjkrel);
                        map.put("gtcustid", gtcustid);
                        if (gtjkrel.equals("3")){
                            map.put("gtjkrelnm", gtjkrelnm);//其它
                        }
                        mPeopleList.add(map);
                        responseData();

                        if (mPeopleList != null &&  mPeopleList.size() == 1){
                            mAddSamePeopleLay.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 300:

                    Bundle bundle2 = data.getExtras();
                    if (bundle2 != null) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) bundle2.getSerializable("resultList");
                    }
                    break;
            }
        }


        if (requestCode == 1 ) {
            switch (resultCode){//
                case MbsConstans.FaceType.FACE_CHECK_APPLY:
                    Bundle bundle = data.getExtras();
                    if (bundle == null){
                        mButSubmit.setEnabled(true);
                        isCheck = false;
                    }else {
                        mButSubmit.setEnabled(false);
                        isCheck = true;
                        submitDataAction();
                    }
                    break;
                default:
                    mButSubmit.setEnabled(true);
                    break;

            }

        }else if (requestCode == PAGE_INTO_LIVENESS){//人脸识别返回来的数据
            if (resultCode == RESULT_OK ) {
                Bundle bundle=data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_CHECK_APPLY);
                Intent intent = new Intent(ApplyAmountActivity.this,ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent,1);
            }else {
                mButSubmit.setEnabled(true);
            }
        }
    }


    private List<Map<String, Object>> mFileList = new ArrayList<>();
    private int mFileNum = 0;
    //广播监听
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (MbsConstans.BroadcastReceiverAction.FILE_TIP_ACTION.equals(action)) {

                if (b != null) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) b.getSerializable("DATA");
                    int num = 0;
                    if (list != null) {
                        mConfigMap.put("contList", list);
                        mFileList.clear();
                        for (Map<String, Object> map : list) {
                            Map<String, Object> resultMap = new HashMap<>();
                            resultMap.put("connpk", map.get("connpk") + "");
                            resultMap.put("name", map.get("name") + "");
                            List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("resultData");
                            if (files != null) {
                                resultMap.put("files",files );
                                num = num+files.size();
                            }else {
                                files = new ArrayList<>();
                                resultMap.put("files",files);
                            }
                            mFileList.add(resultMap);
                        }
                    }

                    if (num != 0){
                        mAddFileTv.setVisibility(View.GONE);
                        mHasUploadTv.setVisibility(View.VISIBLE);
                        mFileNumTv.setVisibility(View.VISIBLE);

                    }else {
                        mAddFileTv.setVisibility(View.VISIBLE);
                        mHasUploadTv.setVisibility(View.GONE);
                        mFileNumTv.setVisibility(View.GONE);

                    }
                    mFileNum = num;
                    mFileNumTv.setText(num+"个");
                    LogUtilDebug.i("打印log日志","###################################################" + mFileList);
                }
            }
        }
    };


    private void responseData() {

        if (mPeopleList != null &&  mPeopleList.size() == 1){
            mAddSamePeopleLay.setVisibility(View.GONE);
        }

        mSwipeMenuAdapter = new SwipeMenuAdapter2(this);
        mSwipeMenuAdapter.setDataList(mPeopleList);

        mSwipeMenuAdapter.setOnDelListener(new SwipeMenuAdapter2.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                //Toast.makeText(ApplyAmountActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();

                /*//RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528
                mSwipeMenuAdapter.getDataList().remove(pos);
                mSwipeMenuAdapter.notifyItemRemoved(pos);//推荐用这个
                if(pos != (mSwipeMenuAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                    mSwipeMenuAdapter.notifyItemRangeChanged(pos, mSwipeMenuAdapter.getDataList().size() - pos);
                }
                mSwipeMenuAdapter.notifyDataSetChanged();*/

                mPeopleList.remove(pos);
                responseData();

                if (mPeopleList .size()<= 0){
                    mAddSamePeopleLay.setVisibility(View.VISIBLE);
                }

                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
            }

            @Override
            public void onTop(int pos) {//置顶功能有bug，后续解决

            }
        });

        AnimationAdapter adapter = new ScaleInAnimationAdapter(mSwipeMenuAdapter);
        adapter.setFirstOnly(false);
        adapter.setDuration(500);
        adapter.setInterpolator(new OvershootInterpolator(.5f));


        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
        //mLRecyclerViewAdapter.addHeaderView(headerView);
        mRefreshListView.setAdapter(mLRecyclerViewAdapter);
        mRefreshListView.setItemAnimator(new DefaultItemAnimator());
        mRefreshListView.setHasFixedSize(true);
        mRefreshListView.setNestedScrollingEnabled(false);

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        mRefreshListView.setPullRefreshEnabled(false);
        mRefreshListView.setLoadMoreEnabled(false);

        mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
    }


    private MySelectDialog mPayZhouqiDialog;
    private MySelectDialog mLilvDialog;
    private MySelectDialog mDaiKuanUseDialog;
    private MySelectDialog mDaiKuanKindDialog;
    private MySelectDialog mJieKuanLimitDialog;
    private MySelectDialog mHkTypeDialog;


    private void initDialog() {
        //List<Map<String, Object>> list1 = SelectDataUtil.getHkZhouqi();
        List<Map<String, Object>> list1 = SelectDataUtil.getNameCodeByType("repayCycle");
        mPayZhouqiDialog = new MySelectDialog(this, true, list1, "还款周期", 100);
        mPayZhouqiDialog.setSelectBackListener(this);

        List<Map<String, Object>> list2 = SelectDataUtil.getLilvType();
        mLilvDialog = new MySelectDialog(this, true, list2, "利率", 101);
        mLilvDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list3 = SelectDataUtil.getDaikuanUse();
        List<Map<String, Object>> list3 = SelectDataUtil.getNameCodeByType("loanUse");
        mDaiKuanUseDialog = new MySelectDialog(this, true, list3, "贷款用途", 102);
        mDaiKuanUseDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list4 = SelectDataUtil.getDaikuanType();
        List<Map<String, Object>> list4 = SelectDataUtil.getNameCodeByType("loanType");
        mDaiKuanKindDialog = new MySelectDialog(this, true, list4, "贷款种类", 103);
        mDaiKuanKindDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list5 = SelectDataUtil.jieKuanLimit();
        List<Map<String, Object>> list5 = SelectDataUtil.getNameCodeByType("loanLimit");
        mJieKuanLimitDialog = new MySelectDialog(this, true, list5, "借款期限", 104);
        mJieKuanLimitDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list6 = SelectDataUtil.getHkType();
        List<Map<String, Object>> list6 = SelectDataUtil.getNameCodeByType("repayWay");
        mHkTypeDialog = new MySelectDialog(this, true, list6, "还款方式", 105);
        mHkTypeDialog.setSelectBackListener(this);
    }


    private void showPayZhouqiDialog() {
        mPayZhouqiDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }

    private void showLilvDialog() {

        mLilvDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }

    private void showDaikuanUseDialog() {

        mDaiKuanUseDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }

    private void showDaikuanKindDialog() {

        mDaiKuanKindDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }
    private void showHuanKuanDialog() {
        mHkTypeDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }




    private void initXieyiView(){
        mXieyiLay.setVisibility(View.VISIBLE);
        mXieyiCheckBox.setChecked(true);
        String xiyiStr = "已阅读并同意签署";
        if(mConfigMap != null){
            List<Map<String,Object>> xieyiList = (List<Map<String,Object>>) mConfigMap.get("signConts");
            if (xieyiList != null && xieyiList.size() > 0){
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

                for (final Map map : xieyiList){
                    final String str = map.get("pdfname")+"";
                    setClickableSpan(sp, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ApplyAmountActivity.this,PDFLookActivity.class);
                            intent.putExtra("id",map.get("pdfurl")+"");
                            startActivity(intent);
                        }
                    }, xiyiStr, "《"+str+"》");
                }
                mXieyiTv.setText(sp);
            }else {
                mXieyiTv.setText(xiyiStr);
            }

        }
        //添加点击事件时，必须设置
        mXieyiTv.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private SpannableString setClickableSpan(SpannableString sp, View.OnClickListener l, String str, String span) {
        sp.setSpan( new MyClickableSpan(0xff1c91ea, l), str.indexOf(span), str.indexOf(span) + span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }



    @Override
    public void finish() {
        super.finish();
        if (mFileNum > 0){
            FileUtils.deleteDir(MbsConstans.UPLOAD_PATH);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean isCheck = false;
    private static final int PAGE_INTO_LIVENESS = 101;
    private void enterNextPage() {
        //startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
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
//                Manager manager = new Manager(ApplyAmountActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(ApplyAmountActivity.this);
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
                    break;
            }
        }
    };



    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
