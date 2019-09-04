package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.api.ErrorHandler;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.MyClickableSpan;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.MySelectDialog;
import com.lr.biyou.mywidget.dialog.PopuTipView;
import com.lr.biyou.mywidget.dialog.TipMsgDialog;
import com.lr.biyou.ui.temporary.adapter.JkHetongAdapter;
import com.lr.biyou.ui.temporary.adapter.ShouKuanRenAdapter;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;




import com.yanzhenjie.permission.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * 我要借款 界面
 */
public class BorrowMoneyActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.lixi_valut_tv)
    TextView mLixiValutTv;
    @BindView(R.id.qixian_lay)
    CardView mQixianLay;
    @BindView(R.id.jiekuan_yongtu_edit)
    EditText mJiekuanYongtuEdit;
    @BindView(R.id.shoukuan_list)
    LRecyclerView mShoukuanList;
    @BindView(R.id.add_shoukuan_people_lay)
    CardView mAddShoukuanPeopleLay;
    @BindView(R.id.has_upload_tv)
    TextView mHasUploadTv;
    @BindView(R.id.add_file_tv)
    TextView mAddFileTv;
    @BindView(R.id.file_num_tv)
    TextView mFileNumTv;
    @BindView(R.id.qixian_edit)
    EditText mQixianEidt;
    @BindView(R.id.fujian_lay)
    CardView mFujianLay;
    @BindView(R.id.but_submit)
    Button mButSubmit;
    @BindView(R.id.scrollView_content)
    NestedScrollView mScrollViewContent;
    @BindView(R.id.hetong_recycleview)
    RecyclerView mHetongRceycleView;
    @BindView(R.id.hetong_lay)
    LinearLayout mHetongLay;
    @BindView(R.id.shoukanren_lay)
    LinearLayout mShouKuanrenLay;
    @BindView(R.id.but_agree)
    Button mButAgree;

    @BindView(R.id.money_tips_tv)
    TextView mMoneyTipsTv;
    @BindView(R.id.lilv_value_tv)
    TextView mLilvValueTv;

    @BindView(R.id.qixian_arrow_view)
    ImageView mQixianArrowView;

    @BindView(R.id.fujian_content_lay)
    LinearLayout mFujianContentLay;

    @BindView(R.id.tv_add_amount)
    TextView mAddTv;
    @BindView(R.id.danwei_tv)
    TextView mDanweiTv;
    @BindView(R.id.cjr_tv)
    TextView mCjrTv;
    @BindView(R.id.nian_lilv_tv)
    TextView mNianLilvTv;
    @BindView(R.id.dd_money_tv)
    TextView mDdMoneyTv;
    @BindView(R.id.xx_fwf_tv)
    TextView mXxFwfTv;
    @BindView(R.id.tips_view)
    ImageView mTipsView;
    @BindView(R.id.dd_jie_lay)
    LinearLayout mDingdanLay;
    @BindView(R.id.fuwufei_lay)
    LinearLayout mFuwufeiLay;

    @BindView(R.id.xieyi_tv)
    TextView mXieyiTv;
    @BindView(R.id.cb_xieyi)
    CheckBox mXieyiCheckBox;
    @BindView(R.id.xieyi_lay)
    LinearLayout mXieyiLay;

    // private Map<String, Object> mMoneyMap;
    private Map<String, Object> mHezuoMap;
    private Map<String, Object> mConfigMap;
    private Map<String, Object> mQixianMap;


    private Map<String, Object> mParamMap;
    private String mType = "";

    private List<Map<String, Object>> mPeopleList = new ArrayList<>();

    private ShouKuanRenAdapter mSwipeMenuAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private String mRequestTag = "";
    private MySelectDialog mQixianDialog;


    private int mQianxianMax = 0;
    private String mPayCompayName = "";
    private String mPaycustid = "";

    private String mZifangnme = "";
    private String mRongziKind = "";
    private String mDdJine = "";


    private String mBillid = "";//订单编号（预付款必传）


    @Override
    public int getContentView() {
        return R.layout.activity_borrow_money;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //mMoneyMap = (Map<String, Object>) bundle.getSerializable("MONEY");
            if (bundle.containsKey("TYPE")) {
                mConfigMap = (Map<String, Object>) bundle.getSerializable("CONFIG");
                mHetongList = (List<Map<String, Object>>) bundle.getSerializable("HETONG");
                mParamMap = (Map<String, Object>) bundle.getSerializable("PARAM");
                mType = bundle.getString("TYPE");
                mZifangnme = bundle.getString("zifangnme");
                mRongziKind = bundle.getString("creditcd");
                mDdJine = bundle.getString("totalamt","");
            } else {
                mHezuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
                mRongziKind = mHezuoMap.get("creditcd") + "";
            }
            mPayCompayName = bundle.getString("payfirmname");
            mPaycustid = bundle.getString("paycustid", "");
        }
        mTitleText.setText(getResources().getString(R.string.tikuan_title));

        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.FILE_TIP_ACTION);
        filter.addAction(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE);

        registerReceiver(receiver, filter);


        initView();

    }


    private void initView(){

        LogUtilDebug.i("-----------------------------------------------------------",mHezuoMap);
        switch (mRongziKind){
            case "L03":
                //"应收账款池"
                mShouKuanrenLay.setVisibility(View.VISIBLE);
                mDingdanLay.setVisibility(View.GONE);
                break;
            case "L11":
                //"信用融资"
                mShouKuanrenLay.setVisibility(View.VISIBLE);
                mDingdanLay.setVisibility(View.GONE);
                break;
            case "L08":
                mShouKuanrenLay.setVisibility(View.GONE);
                //"预付款融资"
                mDingdanLay.setVisibility(View.VISIBLE);
                if (mType.equals("1")) {
                    mDdMoneyTv.setText(UtilTools.getRMBMoney(mDdJine)+"");
                }else {
                    mDdMoneyTv.setText(UtilTools.getRMBMoney(mHezuoMap.get("totalamt")+""));
                    mBillid = mHezuoMap.get("billid")+"";
                }

                break;
            default:
                mShouKuanrenLay.setVisibility(View.VISIBLE);
                mDingdanLay.setVisibility(View.GONE);
                break;
        }


        UtilTools.setMoneyEdit(mMoneyEdit, 0);

        //List<Map<String, Object>> list = SelectDataUtil.jieKuanLimit();
        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("loanLimit");
        mQixianDialog = new MySelectDialog(this, true, list, "选择期限", 10);
        mQixianDialog.setSelectBackListener(this);
        mAddFileTv.setVisibility(View.VISIBLE);
        mHasUploadTv.setVisibility(View.GONE);
        mFileNumTv.setVisibility(View.GONE);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mShoukuanList.setLayoutManager(manager);



        if (mType.equals("1")) {
            mMoneyTipsTv.setText("最多可借款:" + UtilTools.getRMBMoney(mConfigMap.get("maxamt") + ""));
            mLilvValueTv.setText(getResources().getString(R.string.borrow_lilu) + UtilTools.getlilv(mConfigMap.get("daiklilv") + ""));

            String danwei = mParamMap.get("limitunit") + "";
            //Map<String,Object> danWeiMap = SelectDataUtil.getMap(danwei,SelectDataUtil.getQixianDw());
            Map<String, Object> danWeiMap = SelectDataUtil.getMap(danwei, SelectDataUtil.getNameCodeByType("limitUnit"));

            //mParamMap.put("limitunit", danwei);//借款期限单位
            mMoneyEdit.setText(mParamMap.get("reqmoney") + "");
            mQixianValueTv.setText(mParamMap.get("loanlimit") + "" + danWeiMap.get("name"));
            mJiekuanYongtuEdit.setText(mParamMap.get("loanuse") + "");

            mQixianMap = new HashMap<>();
            mQixianMap.put("code",mParamMap.get("loanlimit")+"");

            mPeopleList = (List<Map<String, Object>>) mParamMap.get("stitems");
            if (mPeopleList == null || mPeopleList.size() == 0) {
                mShouKuanrenLay.setVisibility(View.GONE);
            } else {
                mShouKuanrenLay.setVisibility(View.VISIBLE);
            }
            responseData();

            List<Map<String, Object>> fileList = (List<Map<String, Object>>) mParamMap.get("contList");

            int num = 0;
            for (Map mm : fileList) {
                List<Map<String, Object>> fileMap = (List<Map<String, Object>>) mm.get("files");
                if (fileMap != null) {
                    num = num + fileMap.size();
                }
            }
            if (num != 0) {
                mFujianContentLay.setVisibility(View.VISIBLE);
                mAddFileTv.setVisibility(View.GONE);
                mHasUploadTv.setVisibility(View.VISIBLE);
                mFileNumTv.setVisibility(View.VISIBLE);
            } else {
                mFujianContentLay.setVisibility(View.GONE);
                mAddFileTv.setVisibility(View.VISIBLE);
                mHasUploadTv.setVisibility(View.GONE);
                mFileNumTv.setVisibility(View.GONE);

            }
            mFileNumTv.setText(num + "个");
            LogUtilDebug.i("打印log日志", "上传文件列表" + mFileList);

            initHetongList();
            initXieyiView();

            mMoneyEdit.setEnabled(false);
            mQixianLay.setEnabled(false);
            mJiekuanYongtuEdit.setEnabled(false);
            mShoukuanList.setEnabled(false);
            mAddShoukuanPeopleLay.setEnabled(false);
            mFujianLay.setEnabled(false);
            mQixianEidt.setVisibility(View.GONE);
            mButSubmit.setVisibility(View.GONE);
            mAddShoukuanPeopleLay.setVisibility(View.GONE);
            mButAgree.setVisibility(View.VISIBLE);
            mQixianArrowView.setVisibility(View.GONE);

            mCjrTv.setText(mZifangnme);
            mNianLilvTv.setText(UtilTools.getlilv(mConfigMap.get("daiklilv") + ""));

            isShowFuwuFei();
            getXxFuwuFei();
            //fuwuFeiMoney();
        } else {
            mMoneyEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getXxFuwuFei();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mQixianEidt.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getXxFuwuFei();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            mCjrTv.setText(mHezuoMap.get("zifangnme")+"");
            mNianLilvTv.setText(UtilTools.getlilv(mHezuoMap.get("daiklilv") + ""));
            getConfigAction();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsRefresh) {
            getConfigAction();
            mIsRefresh = false;
        }
    }


    @Override
    public void viewEnable() {
        mButSubmit.setEnabled(true);
        mButAgree.setEnabled(true);
    }

    /**
     * 查询借款申请配置
     */
    private void getConfigAction() {
        showProgressDialog();
        mRequestTag = MethodUrl.jiekuanConfig;
        Map<String, String> map = new HashMap<>();
        map.put("patncode", mHezuoMap.get("patncode") + "");
        map.put("zifangbho", mHezuoMap.get("zifangbho") + "");
        map.put("creditno", mHezuoMap.get("creditno") + "");
        map.put("paycustid", mPaycustid);
        map.put("billid", mBillid);

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.jiekuanConfig, map);
    }
    /**
     * 调用信息服务费
     */
    private void getXxFuwuFei() {

        mXxFwfTv.setText(MbsConstans.RMB+" 0.00");
        if (mConfigMap == null || mConfigMap.isEmpty()){
            return;
        }

        String money = mMoneyEdit.getText()+"";
        if (UtilTools.empty(money)) {
            return;
        }
        String maxMStr = mConfigMap.get("maxamt") + "";
        double maxMoney = Double.valueOf(maxMStr);
        maxMoney = UtilTools.divide(maxMoney, 100);
        double editMoney = Double.valueOf(money);
        if (editMoney > maxMoney) {

        } else if (editMoney == 0) {
            return;
        }

        if (mQixianMap == null || mQixianMap.isEmpty()){
            return;
        }

        String danwei = mConfigMap.get("limitunit") + "";
        String qixian = "";
        switch (danwei) {
            case "1"://借款期限单位（1：年 2：月 3：日）
                qixian = mQixianMap.get("code") + "";
                break;
            case "2":
                qixian = mQixianMap.get("code") + "";
                break;
            case "3":
                qixian = mQixianEidt.getText() + "";
                break;
        }

        if (UtilTools.empty(qixian)){
            return;
        }


        mRequestTag = MethodUrl.xxFuwuFei;
        Map<String, String> map = new HashMap<>();
        map.put("reqmoney", mMoneyEdit.getText() + "");
        map.put("xxgrate", mConfigMap.get("xxgrate") + "");
        map.put("loanlimit", qixian);
        map.put("limitunit", danwei);

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.xxFuwuFei, map);
    }


    private void createHetong() {

        if (UtilTools.isEmpty(mMoneyEdit, "金额")) {
            showToastMsg("金额不能为空");
            mButSubmit.setEnabled(true);
            return;
        }


        String maxMStr = mConfigMap.get("maxamt") + "";
        double maxMoney = Double.valueOf(maxMStr);
        maxMoney = UtilTools.divide(maxMoney, 100);
        double editMoney = Double.valueOf(mMoneyEdit.getText() + "");
        if (editMoney > maxMoney) {
            showToastMsg("输入金额不能大于" + UtilTools.fromDouble(maxMoney));
            mButSubmit.setEnabled(true);
            return;
        } else if (editMoney == 0) {
            showToastMsg("输入金额不能为 0");
            mButSubmit.setEnabled(true);
            return;
        }


        String dw = mConfigMap.get("limitunit") + "";//借款期限单位（1：年 2：月 3：日）
        switch (dw) {
            case "1"://借款期限单位（1：年 2：月 3：日）
            case "2":
                if (mQixianMap == null || mQixianMap.isEmpty()) {
                    UtilTools.isEmpty(mQixianValueTv, "期限");
                    showToastMsg("期限不能为空");
                    mButSubmit.setEnabled(true);
                    return;
                }
                break;
            case "3":
                if (UtilTools.isEmpty(mQixianEidt, "期限")) {
                    showToastMsg("期限不能为空");
                    mButSubmit.setEnabled(true);
                    return;
                }
                break;
        }
        if (UtilTools.isEmpty(mJiekuanYongtuEdit, "用途")) {
            showToastMsg("用途不能为空");
            mButSubmit.setEnabled(true);
            return;
        }


        mRequestTag = MethodUrl.jiekuanHetong;
        mParamMap = new HashMap<>();
        mParamMap.put("patncode", mHezuoMap.get("patncode") + "");
        mParamMap.put("zifangbho", mHezuoMap.get("zifangbho") + "");
        mParamMap.put("reqmoney", mMoneyEdit.getText() + "");//借款金额（单位：元

        String danwei = mConfigMap.get("limitunit") + "";
        String qixian = "";
        switch (danwei) {
            case "1"://借款期限单位（1：年 2：月 3：日）
                qixian = mQixianMap.get("code") + "";
                break;
            case "2":
                qixian = mQixianMap.get("code") + "";
                break;
            case "3":
                qixian = mQixianEidt.getText() + "";
                break;
        }

        String qixianStr = mConfigMap.get("loanlimit") + "";

        mParamMap.put("loanlimit", qixian);//借款期限
        mParamMap.put("limitunit", danwei);//借款期限单位

        mParamMap.put("loanuse", mJiekuanYongtuEdit.getText() + "");//借款用途
        mParamMap.put("stitems", mPeopleList);//受托支付信息
        //mParamMap.put("loansqid", mConfigMap.get("loansqid")+"");// 借款编号

        mParamMap.put("loancode", mHezuoMap.get("creditcd") + "");// 借款品种

        mParamMap.put("paycustid", mPaycustid);
        mParamMap.put("billid", mBillid);

        List<Map<String, Object>> fileConfigList = (List<Map<String, Object>>) mConfigMap.get("contList");
        for (Map map1 : fileConfigList) {
            String sign = map1.get("isreq") + "";//是否必传(0:否1是)
            String filetype = map1.get("filetype") + "";

            if (sign.equals("1")) {
                if (mFileList == null || mFileList.size() <= 0) {
                    mButSubmit.setEnabled(true);
                    showToastMsg("请上传必传的附件");
                    return;
                } else {
                    for (Map map2 : mFileList) {
                        String code = map2.get("filetype") + "";
                        List<Map<String, Object>> files = (List<Map<String, Object>>) map2.get("files");
                        if (code.equals(filetype)) {
                            if (files == null || files.size() <= 0) {
                                mButSubmit.setEnabled(true);
                                showToastMsg("请上传必传的附件");
                                return;
                            }

                        }
                    }
                }
            } else {

            }
        }

        //map.put("contList", mFileList);//附件列表
        showProgressDialog();
        LogUtilDebug.i("打印log日志", "提交借款申请的参数" + mParamMap);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.jiekuanHetong, mParamMap);
    }

    private void submitDataAction() {

        showProgressDialog();

        /*if (UtilTools.isEmpty(mMoneyEdit,"金额")){
            showToastMsg("金额不能为空");
            return;
        }
        String dw = mConfigMap.get("limitunit")+"";
        switch (dw){
            case "1"://借款期限单位（1：年 2：月 3：日）
            case "2":
                if (mQixianMap == null || mQixianMap.isEmpty()){
                    UtilTools.isEmpty(mQixianValueTv,"期限");
                    return;
                }
                break;
            case "3":
                if (UtilTools.isEmpty(mQixianEidt,"期限")){
                    showToastMsg("期限不能为空");
                    return;
                }
                break;
        }
        if (UtilTools.isEmpty(mMoneyEdit,"金额")){
            showToastMsg("金额不能为空");
            return;
        }*/


        mRequestTag = MethodUrl.jiekuanSubmit;
       /* Map<String, Object> map = new HashMap<>();
        map.put("patncode", mHezuoMap.get("patncode")+"");
        map.put("zifangbho", mHezuoMap.get("zifangbho")+"");
        map.put("reqmoney", mMoneyEdit.getText()+"");//借款金额（单位：元

        String danwei = mConfigMap.get("limitunit")+"";
        String qixian = "";
        List<Map<String, Object>> list;
        switch (danwei){
            case "1"://借款期限单位（1：年 2：月 3：日）
                qixian = mQixianMap.get("code")+"";
                break;
            case "2":
                qixian = mQixianMap.get("code")+"";
                break;
            case "3":
                qixian = mQixianEidt.getText()+"";
                break;
        }
        map.put("loanlimit", qixian);//借款期限
        map.put("limitunit", danwei);//借款期限单位

        map.put("loanuse", mJiekuanYongtuEdit.getText()+"");//借款用途
        map.put("stitems", mPeopleList);//受托支付信息
        map.put("loansqid", mConfigMap.get("loansqid")+"");// 借款编号


        List<Map<String, Object>> fileConfigList = (List<Map<String, Object>>) mConfigMap.get("conts");
        for (Map map1 : fileConfigList) {
            String sign = map1.get("isreq") + "";//是否必传(0:否1是)
            String filetype = map1.get("filetype") + "";

            if (sign.equals("1")) {
                if (mFileList == null || mFileList.size() <= 0) {
                    showToastMsg("请上传附件");
                    return;
                } else {
                    for (Map map2 : mFileList) {
                        String code = map2.get("filetype") + "";
                        List<Map<String, Object>> files = (List<Map<String, Object>>) map2.get("files");
                        if (code.equals(filetype)) {
                            if (files == null || files.size() <= 0) {
                                showToastMsg("请上传附件");
                                return;
                            }

                        }
                    }
                }
            }
        }

        map.put("contList", mFileList);//附件列表*/

        LogUtilDebug.i("打印log日志", "提交借款申请的参数" + mParamMap);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.jiekuanSubmit, mParamMap);
    }


    private List<Map<String, Object>> mHetongList;
    private JkHetongAdapter mJkHetongAdapter;

    private void initHetongList() {

        if (mHetongList == null || mHetongList.size() == 0) {
            mHetongList = new ArrayList<>();
            mHetongLay.setVisibility(View.GONE);
            return;
        }else {
            mHetongLay.setVisibility(View.VISIBLE);
        }
        mJkHetongAdapter = new JkHetongAdapter(this, mHetongList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mHetongRceycleView.setLayoutManager(manager);
        mHetongRceycleView.setAdapter(mJkHetongAdapter);

        mHetongLay.setVisibility(View.GONE);

    }

    private void initXieyiView(){
        mXieyiCheckBox.setChecked(false);
        if (mHetongList == null || mHetongList.size() == 0){
            mXieyiLay.setVisibility(View.GONE);
            return;
        }
        mXieyiLay.setVisibility(View.VISIBLE);
        String xiyiStr = "已阅读并同意签署";
        if(mConfigMap != null){
            if (mHetongList != null && mHetongList.size() > 0){
                for (int i =0;i<mHetongList.size();i++) {
                    Map<String,Object> map = mHetongList.get(i);
                    final String str = map.get("pdfname") + "";
                    if (i == (mHetongList.size()-1)){
                        xiyiStr = xiyiStr + "《" + str + "》";
                    }else {
                        xiyiStr = xiyiStr + "《" + str + "》、";
                    }
                }
                SpannableString sp = new SpannableString(xiyiStr);

                for (final Map map : mHetongList){
                    final String str = map.get("pdfname")+"";
                    setClickableSpan(sp, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BorrowMoneyActivity.this,PDFLookActivity.class);
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




    private void initFujianView() {
        List<Map<String, Object>> mList = (List<Map<String, Object>>) mConfigMap.get("contList");
        if (mList.size() == 0) {
            mFujianContentLay.setVisibility(View.GONE);
        } else {
            mFujianContentLay.setVisibility(View.VISIBLE);
        }
    }


    private double getLixi() {

        return 0;
    }

    @Override
    public void showProgress() {
        //showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

        Intent intent;
        switch (mType) {
            case MethodUrl.xxFuwuFei:
                mXxFwfTv.setText(MbsConstans.RMB+" "+UtilTools.getNormalMoney(tData.get("fwmny")+""));
                break;
            case MethodUrl.jiekuanHetong:
                mHetongList = (List<Map<String, Object>>) tData.get("conts");

                mParamMap.put("loansqid", tData.get("loansqid") + "");// 借款编号

                intent = new Intent(BorrowMoneyActivity.this, BorrowMoneyActivity.class);
                intent.putExtra("creditcd",  mHezuoMap.get("creditcd")+"");
                intent.putExtra("zifangnme",  mHezuoMap.get("zifangnme")+"");
                intent.putExtra("totalamt",  mHezuoMap.get("totalamt")+"");
                mParamMap.put("contList", mFileList);
                intent.putExtra("HETONG", (Serializable) mHetongList);
                intent.putExtra("CONFIG", (Serializable) mConfigMap);
                intent.putExtra("PARAM", (Serializable) mParamMap);
                intent.putExtra("TYPE", "1");
                startActivity(intent);
                mButSubmit.setEnabled(true);
                break;
            case MethodUrl.jiekuanSubmit:
                showToastMsg("借款申请成功");
                backTo(MainActivity.class, false);
                intent = new Intent(this, ResultMoneyActivity.class);
                intent.putExtra(MbsConstans.ResultType.RESULT_KEY, MbsConstans.ResultType.RESULT_JIEKUAN);
                intent.putExtra("DATA", (Serializable) mParamMap);
                startActivity(intent);
                finish();
                break;
            case MethodUrl.jiekuanConfig:
                mConfigMap = tData;
                initLoanConfig();
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.jiekuanConfig:
                        getConfigAction();
                        break;
                    case MethodUrl.jiekuanHetong:
                        createHetong();
                        break;
                    case MethodUrl.jiekuanSubmit:
                        submitDataAction();
                        break;
                    case MethodUrl.xxFuwuFei:
                        getXxFuwuFei();
                        break;
                }
                break;

        }
    }



    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.jiekuanHetong:
                mButSubmit.setEnabled(true);
                break;
            case MethodUrl.jiekuanSubmit:
                mButAgree.setEnabled(true);
                break;
            case MethodUrl.jiekuanConfig:

                int errorCode = -1;
                String errcodeStr = map.get("errcode") + "";
                try {
                    errorCode = Double.valueOf(errcodeStr).intValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtilDebug.i("打印log日志", "这里出现异常了" + e.getMessage());
                }
                if (errorCode != ErrorHandler.REFRESH_TOKEN_DATE_CODE
                        && errorCode != ErrorHandler.ACCESS_TOKEN_DATE_CODE
                        && errorCode != ErrorHandler.PHONE_NO_ACTIVE) {
                    finish();
                }

                break;
        }

        dealFailInfo(map, mType);
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        String value = map.get("name") + "";
        switch (type) {
            case 11:
                mQixianMap = map;
                mQixianValueTv.setText(mQixianMap.get("name") + "");
                mQixianValueTv.setError(null, null);
                mQixianEidt.setError(null, null);
                //fuwuFeiMoney();
                getXxFuwuFei();
                break;
            case 10:
                mQixianMap = map;
                double d = getLixi();
                String ss = UtilTools.fromDouble(d);
                mQixianValueTv.setText(mQixianMap.get("name") + "");
                mLixiValutTv.setText(ss);
                mQixianValueTv.setError(null, null);
                mQixianEidt.setError(null, null);
                //fuwuFeiMoney();
                getXxFuwuFei();
                break;
        }
    }


    private void initLoanConfig(){
        isShowFuwuFei();
        //fuwuFeiMoney();
        getXxFuwuFei();

        String danwei = mConfigMap.get("limitunit") + ""; //借款期限单位（1：年 2：月 3：日）
        String qixianStr = mConfigMap.get("loanlimit") + "";

        mQianxianMax = Integer.valueOf(mConfigMap.get("loanlimit") + "");

        String maxMStr = mConfigMap.get("maxamt") + "";
        double maxMoney = Double.valueOf(maxMStr);
        maxMoney = UtilTools.divide(maxMoney, 100);

        UtilTools.setMoneyEdit(mMoneyEdit, maxMoney);

        mMoneyTipsTv.setText("最多可借款:" + UtilTools.getRMBMoney(maxMStr));
        mLilvValueTv.setText(getResources().getString(R.string.borrow_lilu) + UtilTools.getlilv(mConfigMap.get("daiklilv") + ""));

        //Map<String,Object> qixianMap = SelectDataUtil.getMap(danwei,SelectDataUtil.getQixianDw());
        //mQixianValueTv.setText(qixianStr+""+qixianMap.get("name")+"");

        initFujianView();

        List<Map<String, Object>> list;
        switch (danwei) {
            case "1"://借款期限单位（1：年 2：月 3：日）
                list = SelectDataUtil.getMaxQixian(mQianxianMax, danwei);
                mQixianDialog = new MySelectDialog(this, true, list, "选择期限", 11);
                mQixianDialog.setSelectBackListener(this);
                mQixianEidt.setVisibility(View.GONE);
                mQixianLay.setEnabled(true);
                mQixianArrowView.setVisibility(View.VISIBLE);
                mDanweiTv.setVisibility(View.GONE);
                break;
            case "2":
                list = SelectDataUtil.getMaxQixian(mQianxianMax, danwei);
                mQixianDialog = new MySelectDialog(this, true, list, "选择期限", 10);
                mQixianDialog.setSelectBackListener(this);
                mQixianEidt.setVisibility(View.GONE);
                mQixianLay.setEnabled(true);
                mQixianArrowView.setVisibility(View.VISIBLE);
                mDanweiTv.setVisibility(View.GONE);

                break;
            case "3":
                mQixianEidt.setVisibility(View.VISIBLE);
                mQixianValueTv.setVisibility(View.GONE);
                mQixianLay.setEnabled(false);
                mQixianArrowView.setVisibility(View.GONE);
                mDanweiTv.setVisibility(View.VISIBLE);

                break;
        }

        String yongtu = mConfigMap.get("usage") + "";
        if (UtilTools.empty(yongtu)) {
            mJiekuanYongtuEdit.setText("");
            mJiekuanYongtuEdit.setEnabled(true);
        } else {
            mJiekuanYongtuEdit.setText(yongtu);
            mJiekuanYongtuEdit.setEnabled(false);
        }


        if (mHezuoMap != null) {
            String type = mHezuoMap.get("creditcd") + "";
            switch (type) {
                case "L03"://应收账款池
                    String total = mConfigMap.get("totalmny") + "";
                    if (total.equals("0")) {
                        showMsgDialog("当前可借额度为零，请关联相关合同，申请数据入池", true);
                        mAddTv.setVisibility(View.VISIBLE);
                    }
                    break;
                case "L11"://信用融资
                    break;
                default://未知
                    break;
            }
        }
    }


    private void isShowFuwuFei(){
        String isFuwufei = mConfigMap.get("paytype")+"";

        if (isFuwufei.equals("1")){
            mFuwufeiLay.setVisibility(View.VISIBLE);
        }else {
            mFuwufeiLay.setVisibility(View.GONE);
        }
    }


    private void fuwuFeiMoney(){
        String money = mMoneyEdit.getText()+"";
        String qixian = "0";
        String dw =  "0";//借款期限单位（1：年 2：月 3：日）
        String lilv = "0";
        if (mConfigMap != null && !mConfigMap.isEmpty()){
            dw = mConfigMap.get("limitunit") + "";//借款期限单位（1：年 2：月 3：日）
            lilv =mConfigMap.get("xxgrate")+"";
        }
        if (UtilTools.empty(dw)){
            dw = "0";
        }
        if (UtilTools.empty(lilv)){
            lilv = "0";
        }

        switch (dw) {
            case "1"://借款期限单位（1：年 2：月 3：日）
                if (mQixianMap!= null && !mQixianMap.isEmpty()) {
                    qixian = mQixianMap.get("code") + "";
                    qixian = (Integer.valueOf(qixian)*360)+"";
                }
                break;
            case "2":
                if (mQixianMap != null && !mQixianMap.isEmpty()) {
                    qixian = mQixianMap.get("code") + "";
                    qixian = (Integer.valueOf(qixian)*30)+"";
                }
                break;
            case "3":
                qixian = mQixianEidt.getText() + "";
                break;
        }

        if (UtilTools.empty(money)){
            money = "0";
        }
        if (UtilTools.empty(qixian)){
            qixian = "0";
        }



        double d1 = UtilTools.mul(Double.valueOf(money),Double.valueOf(lilv));//金额乘以利率   没有带百分号的利率  利率单位是年
        d1 = UtilTools.divide(d1,10000);//上次结果除以一百   转成带百分号的结果
        d1 = UtilTools.mul(d1,Double.valueOf(qixian));//结果乘以借款期限  以天为单位的借款期限
        d1 = UtilTools.divide(d1,360);//因为利率单位是年   要除以360  把天转成年

              /*  String s1 ="信息服务费=借款金额×信息服务费率×借款期限\n" +
                        "即：80000.00元×3.00%×120天÷360";*/
        String s1 ="信息服务费=借款金额×信息服务费率×借款期限\n" +
                "即："+money+"元×"+lilv+"×"+qixian+"天÷360";

        mXxFwfTv.setText(UtilTools.getNormalMoney(d1+""));
    }

    @OnClick({R.id.qixian_lay, R.id.back_img, R.id.fujian_lay, R.id.add_shoukuan_people_lay,
            R.id.but_submit, R.id.but_agree, R.id.left_back_lay, R.id.tv_add_amount,R.id.tips_view})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.but_agree:
                if (!mXieyiCheckBox.isChecked()){
                    showToastMsg("请先阅读协议");
                    return;
                }
                mButAgree.setEnabled(false);
                // submitDataAction();
                PermissionsUtils.requsetRunPermission(BorrowMoneyActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        netWorkWarranty();
                    }

                    @Override
                    public void requestFailer() {
                        toast(R.string.failure);
                        mButAgree.setEnabled(true);
                    }
                }, Permission.Group.CAMERA, Permission.Group.STORAGE);

                break;
            case R.id.qixian_lay:
                mQixianDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.fujian_lay:
                PermissionsUtils.requsetRunPermission(BorrowMoneyActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        Intent intent = new Intent(BorrowMoneyActivity.this, AddFileActivity.class);
                        intent.putExtra("DATA", (Serializable) mConfigMap);
                        intent.putExtra("TYPE", "2");
                        startActivityForResult(intent, 300);
                    }
                    @Override
                    public void requestFailer() {
                        toast(R.string.failure);
                    }
                }, Permission.Group.CAMERA, Permission.Group.STORAGE);

               /* intent = new Intent(BorrowMoneyActivity.this, AddFileActivity.class);
                intent.putExtra("DATA", (Serializable) mConfigMap);
                intent.putExtra("TYPE","2");
                startActivityForResult(intent, 300);*/
                break;
            case R.id.add_shoukuan_people_lay:
                intent = new Intent(BorrowMoneyActivity.this, AddSKPeopleActivity.class);
                intent.putExtra("DATA", (Serializable) mHezuoMap);
                startActivityForResult(intent, 400);
                break;
            case R.id.but_submit:
                mButSubmit.setEnabled(false);
                createHetong();
                break;
            case R.id.tv_add_amount: //添加入池
                intent = new Intent(BorrowMoneyActivity.this, HeTongSelectActivity.class);
                intent.putExtra("payfirmname", mPayCompayName);
                intent.putExtra("paycustid", mPaycustid);
                intent.putExtra("DATA", (Serializable) mHezuoMap);
                startActivity(intent);
                break;
            case R.id.tips_view:
                String money = mMoneyEdit.getText()+"";
                String qixian = "0";

                String dw = mConfigMap.get("limitunit") + "";//借款期限单位（1：年 2：月 3：日）
                switch (dw) {
                    case "1"://借款期限单位（1：年 2：月 3：日）
                        if (mQixianMap!= null && !mQixianMap.isEmpty()) {
                            qixian = mQixianMap.get("code") + "";
                            qixian = (Integer.valueOf(qixian)*360)+"";
                        }
                        break;
                    case "2":
                        if (mQixianMap != null && !mQixianMap.isEmpty()) {
                            qixian = mQixianMap.get("code") + "";
                            qixian = (Integer.valueOf(qixian)*30)+"";
                        }
                        break;
                    case "3":
                        qixian = mQixianEidt.getText() + "";
                        break;
                }

                if (UtilTools.empty(money)){
                    money = "0";
                }
                if (UtilTools.empty(qixian)){
                    qixian = "0";
                }

                String lilv = UtilTools.getlilv(UtilTools.divide(Double.valueOf(mConfigMap.get("xxgrate")+""),100)+"");
              /*  String s1 ="信息服务费=借款金额×信息服务费率×借款期限\n" +
                        "即：80000.00元×3.00%×120天÷360";*/
                String s1 ="信息服务费=借款金额×信息服务费率×借款期限\n" +
                        "即："+money+"元×"+lilv+"×"+qixian+"天÷360";
                PopuTipView mp1= new PopuTipView(this,s1,R.layout.popu_lay_top);
                mp1.show(mTipsView,2);
                break;

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
                            resultMap.put("filetype", map.get("filetype") + "");
                            resultMap.put("name", map.get("name") + "");
                            List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("resultData");
                            if (files != null) {
                                resultMap.put("files", files);
                                num = num + files.size();
                            } else {
                                files = new ArrayList<>();
                                resultMap.put("files", files);
                            }
                            mFileList.add(resultMap);
                        }
                    }

                    if (num != 0) {
                        mAddFileTv.setVisibility(View.GONE);
                        mHasUploadTv.setVisibility(View.VISIBLE);
                        mFileNumTv.setVisibility(View.VISIBLE);

                    } else {
                        mAddFileTv.setVisibility(View.VISIBLE);
                        mHasUploadTv.setVisibility(View.GONE);
                        mFileNumTv.setVisibility(View.GONE);

                    }
                    mFileNum = num;
                    mFileNumTv.setText(num + "个");
                    LogUtilDebug.i("打印log日志", "上传文件列表" + mFileList);
                }
            } else if (action.equals(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE)) {
                finish();
            }
        }
    };


    private void responseData() {
        if (mPeopleList == null) {
            return;
        }
        mSwipeMenuAdapter = new ShouKuanRenAdapter(this);

        if (mType.equals("1")) {
            mSwipeMenuAdapter.setSwipeEnable(false);
        } else {
            mSwipeMenuAdapter.setSwipeEnable(true);
        }

        mSwipeMenuAdapter.setDataList(mPeopleList);

        mSwipeMenuAdapter.setOnDelListener(new ShouKuanRenAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                //Toast.makeText(BorrowMoneyActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();

                /*//RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528
                mSwipeMenuAdapter.getDataList().remove(pos);
                mSwipeMenuAdapter.notifyItemRemoved(pos);//推荐用这个
                if(pos != (mSwipeMenuAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                    mSwipeMenuAdapter.notifyItemRangeChanged(pos, mSwipeMenuAdapter.getDataList().size() - pos);
                }
                mSwipeMenuAdapter.notifyDataSetChanged();*/

                mPeopleList.remove(pos);
                responseData();

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
        mShoukuanList.setAdapter(mLRecyclerViewAdapter);
        mShoukuanList.setItemAnimator(new DefaultItemAnimator());
        mShoukuanList.setHasFixedSize(true);
        mShoukuanList.setNestedScrollingEnabled(false);

        mShoukuanList.setPullRefreshEnabled(false);
        mShoukuanList.setLoadMoreEnabled(false);

        DividerDecoration divider2 = new DividerDecoration.Builder(this)
                .setHeight(2f)
                .setColorResource(R.color.divide_line)
                .build();
        mShoukuanList.addItemDecoration(divider2);
        mShoukuanList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mShoukuanList.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        mShoukuanList.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
    }

    @Override
    public void finish() {
        super.finish();
        if (mFileNum > 0) {
            FileUtils.deleteDir(MbsConstans.UPLOAD_PATH);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                Toast.makeText(BorrowMoneyActivity.this, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    //adapter.setList(selectList);
                    // adapter.notifyDataSetChanged();
                    break;
                case 400:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Map<String, Object> map = (Map<String, Object>) bundle.getSerializable("DATA");
                        mPeopleList.add(map);
                        responseData();
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

        Intent intent = null;
        Bundle bundle;
        if (requestCode == 1) {//人脸识别过来的状态码
            switch (resultCode) {//通过短信验证码
                case MbsConstans.FaceType.FACE_BORROW_MONEY:
                    bundle = data.getExtras();
                    if (bundle == null) {
                        isCheck = false;
                        mButAgree.setEnabled(true);
                    } else {
                        mButAgree.setEnabled(false);
                        isCheck = true;
                        submitDataAction();
                    }
                    break;
                default:
                    mButAgree.setEnabled(true);
                    break;

            }

        } else if (requestCode == PAGE_INTO_LIVENESS) {//人脸识别返回来的数据
            if (resultCode == RESULT_OK) {
                bundle = data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_BORROW_MONEY);
                intent = new Intent(BorrowMoneyActivity.this, ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent, 1);
            } else {
                mButAgree.setEnabled(true);
            }
        }
    }

    /**
     * -----------------------------------  人脸识别  ------------------------------------------------
     */


    private boolean isCheck = false;
    private static final int PAGE_INTO_LIVENESS = 101;

    private void enterNextPage() {
        //startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {
//
//        final String uuid = ConUtil.getUUIDString(this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Manager manager = new Manager(BorrowMoneyActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(BorrowMoneyActivity.this);
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
                    mButSubmit.setEnabled(true);
                    break;
            }
        }
    };

    private static final int REQUEST_CODE_SETTING = 10011;

    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private TipMsgDialog mTotalMoneyDialog;

    private void showMsgDialog(Object msg, boolean isClose) {
        mTotalMoneyDialog = new TipMsgDialog(this, true);
        mTotalMoneyDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                    if (isClose) {
                        finish();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel:
                        mTotalMoneyDialog.dismiss();
                        break;
                    case R.id.confirm:
                        Intent intent = new Intent(BorrowMoneyActivity.this, HeTongSelectActivity.class);
                        intent.putExtra("payfirmname", mPayCompayName);
                        intent.putExtra("paycustid", mPaycustid);
                        intent.putExtra("DATA", (Serializable) mHezuoMap);
                        startActivity(intent);
                        mTotalMoneyDialog.dismiss();
                        break;
                    case R.id.tv_right:
                        mTotalMoneyDialog.dismiss();
                        break;
                }
            }
        };
        mTotalMoneyDialog.setCanceledOnTouchOutside(true);
        mTotalMoneyDialog.setCancelable(true);
        mTotalMoneyDialog.setOnClickListener(onClickListener);
        mTotalMoneyDialog.initValue("温馨提示", msg);
        mTotalMoneyDialog.show();
        mTotalMoneyDialog.tv_cancel.setText("取消");
        mTotalMoneyDialog.tv_exit.setVisibility(View.VISIBLE);
        mTotalMoneyDialog.tv_exit.setText("前往");
    }
}
