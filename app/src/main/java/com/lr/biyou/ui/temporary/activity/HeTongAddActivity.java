package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.CompanySelectDialog;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.mywidget.dialog.MySelectDialog;
import com.lr.biyou.mywidget.dialog.PopuTipView;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加完善贸易信息合同
 */
public class HeTongAddActivity extends BasicActivity implements RequestView, SelectBackListener {

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

    @BindView(R.id.has_upload_tv)
    TextView mHasUploadTv;
    @BindView(R.id.add_file_tv)
    TextView mAddFileTv;
    @BindView(R.id.file_num_tv)
    TextView mFileNumTv;
    @BindView(R.id.hetong_code_edit)
    EditText mHetongCodeEdit;//合同编号
    @BindView(R.id.gmf_value_tv)
    TextView mGmfValueTv;//购买方
    @BindView(R.id.gmf_lay)
    CardView mGmfLay;//购买方
    @BindView(R.id.qd_date_value_tv)
    TextView mQdDateValueTv;//签订日期
    @BindView(R.id.qd_date)
    CardView mQdDate;//签订日期
    @BindView(R.id.money_edit)
    EditText mMoneyEdit;//金额
    @BindView(R.id.js_type_value_tv)
    TextView mJsTypeValueTv;//结算方式
    @BindView(R.id.js_type_lay)
    CardView mJsTypeLay;//结算方式
    @BindView(R.id.js_zhouqi_edit)
    EditText mJsZhouqiEdit;//结算周期
    @BindView(R.id.wu_checkbox)
    CheckBox mWuCheckbox;//无
    @BindView(R.id.wu_lay)
    LinearLayout mWuLay;//无
    @BindView(R.id.myue_checkbox)
    CheckBox mMyueCheckbox;//每月
    @BindView(R.id.myue_lay)
    LinearLayout mMyueLay;//每月
    @BindView(R.id.myue_edit)
    EditText mMyueEdit;//每月
    @BindView(R.id.zy_edit)
    EditText mZyEdit;//摘要
    @BindView(R.id.count_tv)
    TextView mCountTv;//字数限制 摘要
    @BindView(R.id.cjr_value_tv)
    TextView mCjrValueTv;//出借人
    @BindView(R.id.cjr_lay)
    CardView mCjrLay;//出借人
    @BindView(R.id.but_submit)
    Button mButSubmit;//下一步
    @BindView(R.id.tip_wu_view)
    ImageView mTipWuView;
    @BindView(R.id.tip_ri_view)
    ImageView mTipRiView;
    @BindView(R.id.danwei_lay)
    LinearLayout mDanweiLay;
    @BindView(R.id.danwei_value_tv)
    TextView mDanweiValueTv;
    @BindView(R.id.jiesuan_date_line)
    View mJiesuanDateLine;
    @BindView(R.id.jiesuan_date_value)
    TextView mJiesuanDateValue;
    @BindView(R.id.jiesuan_date_lay)
    CardView mJiesuanDateLay;
    @BindView(R.id.zhouqiJs_lay)
    LinearLayout mZhouqiJsLay;


    // private Map<String, Object> mMoneyMap;
    private Map<String, Object> mHezuoMap;
    private Map<String, Object> mConfigMap = new HashMap<>();
    private Map<String, Object> mQixianMap;


    private Map<String, Object> mParamMap;
    private String mType = "";

    private List<Map<String, Object>> mPeopleList = new ArrayList<>();


    private String mRequestTag = "";

    private DateSelectDialog mQdDateDialog;
    private MySelectDialog mJsTypeDialog;
    private MySelectDialog mJsZhouqiDwDialog;

    private DateSelectDialog mJsDateDialog;

    private CompanySelectDialog mCompanySelectDialog;

    private Map<String, Object> mJsTypeMap;
    private Map<String, Object> mJsZhouqiDwMap;
    private Map<String, Object> mPayCompanyMap;

    private String mQdDateStr = ""; //签订日期
    private String mZhouqiStr = ""; //定期结算日


    private String mPayCompayName = "";
    private String mPaycustid = "";

    private boolean mPayLayPress = false;

    @Override
    public int getContentView() {
        return R.layout.activity_hetong_add;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.hetong_title));
        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.FILE_TIP_ACTION);
        registerReceiver(receiver, filter);
        initDialog();
        mAddFileTv.setVisibility(View.VISIBLE);
        mHasUploadTv.setVisibility(View.GONE);
        mFileNumTv.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mPayCompayName = bundle.getString("payfirmname");
            mPaycustid = bundle.getString("paycustid");
        }

        mWuCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mMyueCheckbox.setChecked(false);
                    mMyueEdit.setEnabled(false);
                } else {
                    if (!mMyueCheckbox.isChecked()) {
                        mWuCheckbox.setChecked(true);
                        mMyueEdit.setEnabled(false);
                    }
                }
            }
        });

        mMyueCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mWuCheckbox.setChecked(false);
                    mMyueEdit.setEnabled(true);
                } else {
                    if (!mWuCheckbox.isChecked()) {
                        mMyueCheckbox.setChecked(true);
                        mMyueEdit.setEnabled(true);
                    } else {
                        mMyueEdit.setEnabled(false);
                    }
                }
            }
        });

        UtilTools.setMoneyEdit(mMoneyEdit, 0);

        mJiesuanDateLine.setVisibility(View.GONE);
        mJiesuanDateLay.setVisibility(View.GONE);

        getCompanyList();

        initContList();
    }


    private void initDialog() {
        //签订日期
        mQdDateDialog = new DateSelectDialog(this, true, "选择日期", 2001);
        mQdDateDialog.setSelectBackListener(this);

        //结算日期选择   定期方式
        mJsDateDialog = new DateSelectDialog(this, true, "选择日期", 2002);
        mJsDateDialog.setSelectBackListener(this);

        List<Map<String, Object>> list = new ArrayList<>();//结算方式 0周期结算 1定期结算
        Map<String, Object> map = new HashMap<>();
        map.put("name", "周期结算");
        map.put("code", "0");
        list.add(map);
        map = new HashMap<>();
        map.put("name", "定期结算");
        map.put("code", "1");
        list.add(map);
        mJsTypeDialog = new MySelectDialog(this, true, list, "结算方式", 11);
        mJsTypeDialog.setSelectBackListener(this);

        List<Map<String, Object>> jsZhouqiDwList = new ArrayList<>();//期限单位(1: 年, 2: 月, 3: 日)
        Map<String, Object> zhouqiMap = new HashMap<>();
        zhouqiMap.put("name", "年");
        zhouqiMap.put("code", "1");
        jsZhouqiDwList.add(zhouqiMap);
        zhouqiMap = new HashMap<>();
        zhouqiMap.put("name", "月");
        zhouqiMap.put("code", "2");
        jsZhouqiDwList.add(zhouqiMap);
        zhouqiMap = new HashMap<>();
        zhouqiMap.put("name", "日");
        zhouqiMap.put("code", "3");
        jsZhouqiDwList.add(zhouqiMap);
        mJsZhouqiDwDialog = new MySelectDialog(this, true, jsZhouqiDwList, "结算周期单位", 12);
        mJsZhouqiDwDialog.setSelectBackListener(this);
    }


    /**
     * 查询分子公司信息
     */
    private void getCompanyList() {
        mRequestTag = MethodUrl.childfirm;
        Map<String, String> map = new HashMap<>();
        map.put("firmname", mPayCompayName);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.childfirm, map);
    }


    private void addHetong() {

        mRequestTag = MethodUrl.addHetongInfo;

        String htCode = mHetongCodeEdit.getText() + "";//合同编号
        if (UtilTools.isEmpty(mHetongCodeEdit, getString(R.string.hetong_code))) {
            mButSubmit.setEnabled(true);
            return;
        }

        String gmf = "";//购买方
        if (UtilTools.isEmpty(mGmfValueTv, getString(R.string.pay_people))) {
            mButSubmit.setEnabled(true);
            return;
        }

        //签订日期
        if (UtilTools.isEmpty(mQdDateValueTv, getString(R.string.qian_date))) {
            mButSubmit.setEnabled(true);
            return;
        }

        String money = mMoneyEdit.getText() + "";//金额
        if (UtilTools.isEmpty(mMoneyEdit, getString(R.string.jine))) {
            mButSubmit.setEnabled(true);
            return;
        }


        if (UtilTools.isEmpty(mJsTypeValueTv, getString(R.string.js_type))) {
            mButSubmit.setEnabled(true);
            return;
        }



       /* String zhaiyao = mZyEdit.getText() + "";//摘要
        if (UtilTools.isEmpty(mZyEdit, getString(R.string.trade_des))) {
            mButSubmit.setEnabled(true);
            return;
        }*/

       /* String cjr = "";//出借人
        if (UtilTools.isEmpty(mCjrValueTv, getString(R.string.borrow_pay_man))) {
            mButSubmit.setEnabled(true);
            return;
        }*/

      /*  String isJsDate = "0";
        if (mWuCheckbox.isChecked()) {
            isJsDate = "0";
        } else {
            isJsDate = "1";
        }
*/

        String settdateflg = "";
        String jsType = "";//结算方式
        if (mJsTypeValueTv.getText().toString().trim().equals("周期结算")) {
            jsType = "0";  //周期结算

            //结算周期
            if (UtilTools.isEmpty(mJsZhouqiEdit, getString(R.string.js_zhouqi))) {
                mButSubmit.setEnabled(true);
                return;
            }

            if (!mWuCheckbox.isChecked() && !mMyueCheckbox.isChecked()){
                showToastMsg("请选择有无结算日");
                mButSubmit.setEnabled(true);
                return;
            }

            //无结算日
            if (mWuCheckbox.isChecked()){
                settdateflg = "0";
            }

            //有结算日
            if (mMyueCheckbox.isChecked()){

                settdateflg = "1";
                if (UtilTools.isEmpty(mMyueEdit, getString(R.string.js_date))) {
                    mButSubmit.setEnabled(true);
                    return;
                }
            }

        }else {
            jsType = "1"; //定期结算

            if (UtilTools.isEmpty(mJiesuanDateValue, getString(R.string.jiesuan_date))) {
                mButSubmit.setEnabled(true);
                return;
            }

        }



        String jsZhouqi = mJsZhouqiEdit.getText() + "";//结算周期
        String jsZhouqiDanWei = "";//结算周期单位
        if (mDanweiValueTv.getText().toString().trim().equals("年")){
            jsZhouqiDanWei ="1";
        }else if (mDanweiValueTv.getText().toString().trim().equals("月")){
            jsZhouqiDanWei ="2";
        }else {
            jsZhouqiDanWei ="3";
        }

        String jsDate = mMyueEdit.getText()+"";//结算日

        String beizhu = mZyEdit.getText()+ "";
        mParamMap = new HashMap<>();
        mParamMap.put("contno", htCode);//合同编号
        mParamMap.put("contmny", money);//金额
        mParamMap.put("contsgndt", mQdDateStr);//合同签订日期
        mParamMap.put("settype", jsType);//结算方式
        mParamMap.put("settcycle", jsZhouqi);//结算周期
        mParamMap.put("settunit", jsZhouqiDanWei);//期限单位(1: 年, 2: 月, 3: 日)
        mParamMap.put("settdateflg", settdateflg);//是否有结算日(0：无 1：有)
        mParamMap.put("settdate", jsDate);//结算日
        mParamMap.put("dqsettdate", mZhouqiStr);//定期结算日期
        mParamMap.put("memo", beizhu);//备注
        mParamMap.put("paycustid", mPaycustid);//付款方客户号
        mParamMap.put("payfirmname",mPayCompayName);//付款方名称

        mParamMap.put("contList", "");//附件列表

        List<Map<String, Object>> fileConfigList = (List<Map<String, Object>>) mConfigMap.get("contList");
        if (fileConfigList != null){
            for (Map map1 : fileConfigList) {
                String sign = map1.get("isreq") + "";//是否必传(0:否1是)
                String filetype = map1.get("filetype") + "";

                if (sign.equals("1")) {
                    if (mFileList == null || mFileList.size() <= 0) {
                        showToastMsg("请上传必传的附件");
                        return;
                    } else {
                        for (Map map2 : mFileList) {
                            String code = map2.get("filetype") + "";
                            List<Map<String, Object>> files = (List<Map<String, Object>>) map2.get("files");
                            if (code.equals(filetype)) {
                                if (files == null || files.size() <= 0) {
                                    showToastMsg("请上传必传的附件");
                                    return;
                                }

                            }
                        }
                    }
                } else {

                }
            }
        }


        mParamMap.put("contList", mFileList);
        LogUtilDebug.i("打印log日志", "提交借款申请的参数" + mParamMap);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.addHetongInfo, mParamMap);

    }

    private void submitDataAction() {
        LogUtilDebug.i("打印log日志", "提交借款申请的参数" + mParamMap);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.jiekuanSubmit, mParamMap);
    }

    private void  initContList(){

        mConfigMap = new HashMap<>();

        List<Map<String,Object>> mContList = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        map.put("filetype","01");
        map.put("name","贸易合同");
        mContList.add(map);

        map = new HashMap<>();
        map.put("filetype","02");
        map.put("name","发票");
        mContList.add(map);

        map = new HashMap<>();
        map.put("filetype","03");
        map.put("name","物流单据");
        mContList.add(map);

        map = new HashMap<>();
        map.put("filetype","04");
        map.put("name","收货单");
        mContList.add(map);

        map = new HashMap<>();
        map.put("filetype","05");
        map.put("name","校验单");
        mContList.add(map);

        mConfigMap.put("contList",mContList);
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
            case MethodUrl.childfirm:

                List<Map<String,Object>> list = (List<Map<String, Object>>) tData.get("firmlist");
                if (list != null && list.size()>0){
                    mCompanySelectDialog = new CompanySelectDialog(HeTongAddActivity.this, true, list,10);
                    mCompanySelectDialog.setSelectBackListener(this);
                    if (mPayLayPress){
                        mCompanySelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                    }
                }else {
                    if (mPayLayPress){
                        showToastMsg("暂无付款方");
                    }
                }
                break;
            case MethodUrl.jiekuanSubmit:
                showToastMsg("借款申请成功");
                intent = new Intent(this, ResultMoneyActivity.class);
                intent.putExtra(MbsConstans.ResultType.RESULT_KEY, MbsConstans.ResultType.RESULT_JIEKUAN);
                intent.putExtra("DATA", (Serializable) mParamMap);
                startActivity(intent);
                finish();
                break;
            case MethodUrl.jiekuanConfig:

                break;

            case MethodUrl.addHetongInfo:
                showToastMsg("添加合同成功");
                intent = new Intent();
                intent.setAction(MbsConstans.BroadcastReceiverAction.HTONGUPDATE);
                sendBroadcast(intent);
                finish();
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.childfirm:
                        getCompanyList();
                        break;
                    case MethodUrl.jiekuanHetong:
                        //addHetong();
                        break;
                    case MethodUrl.jiekuanSubmit:
                        submitDataAction();
                        break;
                    case MethodUrl.addHetongInfo:
                        addHetong();
                        break;
                }
                break;

        }
    }


    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.jiekuanHetong:
                break;
            case MethodUrl.jiekuanSubmit:
                break;
            case MethodUrl.jiekuanConfig:
                break;
        }

        dealFailInfo(map, mType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        String value = map.get("name") + "";
        switch (type) {
            case 10:
                mPayCompanyMap = map;
                mGmfValueTv.setText(mPayCompanyMap.get("firmname")+"");
                break;
            case 11:
                mJsTypeMap = map;
                mJsTypeValueTv.setText(mJsTypeMap.get("name") + "");
                String code = mJsTypeMap.get("code")+"";
                if (code.equals("0")){
                    mZhouqiJsLay.setVisibility(View.VISIBLE);
                    mJiesuanDateLay.setVisibility(View.GONE);
                    mJiesuanDateLine.setVisibility(View.GONE);
                }else if (code.equals("1")){
                    mZhouqiJsLay.setVisibility(View.GONE);
                    mJiesuanDateLine.setVisibility(View.VISIBLE);
                    mJiesuanDateLay.setVisibility(View.VISIBLE);
                }
                break;
            case 12:
                mJsZhouqiDwMap = map;
                mDanweiValueTv.setText(mJsZhouqiDwMap.get("name") + "");
                break;
            case 2001:
                mQdDateStr = map.get("date") + "";
                mQdDateValueTv.setText(map.get("year") + "-" + map.get("month") + "-" + map.get("day") + "");
                mQdDateValueTv.setError(null, null);
                break;
            case 2002:
                mZhouqiStr = map.get("date") + "";
                mJiesuanDateValue.setText(map.get("year") + "-" + map.get("month") + "-" + map.get("day") + "");
                mJiesuanDateValue.setError(null, null);
                break;
        }
    }

    @OnClick({R.id.back_img, R.id.fujian_lay, R.id.left_back_lay, R.id.gmf_lay, R.id.tip_wu_view, R.id.tip_ri_view,
            R.id.qd_date, R.id.js_type_lay, R.id.wu_lay, R.id.myue_lay, R.id.cjr_lay, R.id.but_submit, R.id.danwei_lay,R.id.jiesuan_date_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.jiesuan_date_lay:
                mJsDateDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.danwei_lay:
                mJsZhouqiDwDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.fujian_lay:

                PermissionsUtils.requsetRunPermission(HeTongAddActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        Intent intent = new Intent(HeTongAddActivity.this, AddFileActivity.class);
                        intent.putExtra("DATA", (Serializable) mConfigMap);
                        intent.putExtra("TYPE", "2");
                        startActivityForResult(intent, 300);
                    }

                    @Override
                    public void requestFailer() {
                        showToastMsg(R.string.failure);
                    }
                }, Permission.Group.CAMERA, Permission.Group.STORAGE);
                break;

            case R.id.gmf_lay://选择购买方
                if (mCompanySelectDialog != null){
                    mCompanySelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                }else {
                    mPayLayPress = true;
                    getCompanyList();
                }
                break;
            case R.id.qd_date://选择签订日期
                mQdDateDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.js_type_lay://选择结算方式
                mJsTypeDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.wu_lay://无
                break;
            case R.id.myue_lay://每月
                break;
            case R.id.cjr_lay://选择出借人
                break;
            case R.id.but_submit:
                LogUtilDebug.i("show","0000000");
                addHetong();
               // finish();
                break;
            case R.id.tip_wu_view:


                String s2 = "付款截止日=当月的发票日期+" + "\n" +
                        "结算周期（按月计算，每30天" + "\n" +
                        "算一个月）后的当月最后一天";

                PopuTipView mp2 = new PopuTipView(HeTongAddActivity.this, s2, R.layout.popu_lay_top);
                mp2.show(mTipWuView, 1);

               /* View inflate = View.inflate(HeTongAddActivity.this, R.layout.popup_bubble_text, null);
                TextView mTextView = inflate.findViewById(R.id.tv_bubble);
                mTextView.setText("付款截止日=当月的发票日期+" + "\n" +
                        "结算周期（按月计算，每30天" + "\n" +
                        "算一个月）后的当月最后一天" );



                new EasyDialog(HeTongAddActivity.this)
                        .setLayout(inflate)
                        .setBackgroundColor(HeTongAddActivity.this.getResources().getColor(R.color.black_light))
                        .setLocationByAttachedView(mTipWuView)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(10, 10)
                        .show();*/

               /* new BubblePopup(HeTongAddActivity.this, inflate)
                        .anchorView(mTipWuView)
                        .bubbleColor(Color.parseColor("#A6000000"))
                        .gravity(Gravity.TOP)
                        .triangleWidth(5)
                        .triangleHeight(5)
                        .showAnim(null)
                        .dismissAnim(null)
                        .show();*/
                break;
            case R.id.tip_ri_view:

                /*View inflate2 = View.inflate(HeTongAddActivity.this, R.layout.popup_bubble_text, null);
                TextView mTextView2 = inflate2.findViewById(R.id.tv_bubble);
                mTextView2.setText("1、若开票日期在当月结算日之前"+"\n"+
                        "（不含结算日），则付款截止日=当"+ "\n" +
                        "月结算日+结算周期（按月计算）"+ "\n" +
                        "2、若开票日期在当月结算日之后"+ "\n" +
                        "（含结算日），则付款截止日=下一"+ "\n" +
                        "月结算日+结算周期（按月计算）");*/
                String s = "1、若开票日期在当月结算日之前" + "\n" +
                        "（不含结算日），则付款截止日=当" + "\n" +
                        "月结算日+结算周期（按月计算）" + "\n" +
                        "2、若开票日期在当月结算日之后" + "\n" +
                        "（含结算日），则付款截止日=下一" + "\n" +
                        "月结算日+结算周期（按月计算）";

                PopuTipView mp = new PopuTipView(HeTongAddActivity.this, s, R.layout.popu_lay_top);
                mp.show(mTipRiView, 2);


             /*   new BubblePopup(HeTongAddActivity.this, inflate2)
                        .anchorView(mTipRiView)
                        .bubbleColor(Color.parseColor("#A6000000"))
                        .gravity(Gravity.TOP)
                        .triangleWidth(5)
                        .triangleHeight(5)
                        .showAnim(null)
                        .dismissAnim(null)
                        .show();*/
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


    private View popView;
    private PopupWindow mPopupWindow;

    private void initPopupWindow(View view) {
        popView = View.inflate(this, R.layout.popu_lay, null);
        popView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        TextView mTextView2 = popView.findViewById(R.id.tv_bubble);
        mTextView2.setText("从事本行业的开始时间");


        LinearLayout mLlContent = (LinearLayout) popView.findViewById(com.flyco.dialog.R.id.ll_content);

        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mLlContent.getLayoutParams();
        mLlContent.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#A6000000"), 15));

        //mLayoutParams.setMargins(10, 0, 10, 0);
        mLlContent.setLayoutParams(mLayoutParams);


        //让mOnCreateView充满父控件,防止ViewHelper.setXY导致点击事件无效



       /* popView = LayoutInflater.from(this).inflate(R.layout.popup_bubble_text,null);
        TextView mTextView2 = popView.findViewById(R.id.tv_bubble);
        mTextView2.setText("从事本行业的开始时间" );
        mTextView2.setTextColor(ContextCompat.getColor(this,R.color.black));*/


        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (mPopupWindow != null && mPopupWindow.isShowing() == false) {
            int screenWidth = UtilTools.getScreenWidth(this);
            int screenHeight = UtilTools.getScreenHeight(this);
            //mPopupWindow.setWidth(screenWidth/2);
            // mPopupWindow.setHeight((int)(screenHeight/1.5));
            //设置background后在外点击才会消失
            //mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_corners));
            //mPopupWindow.setOutsideTouchable(true);// 设置可允许在外点击消失
            //自定义动画
            // mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
            //mPopupWindow.setAnimationStyle(android.R.style.Animation_Activity);//使用系统动画
            mPopupWindow.update();
            mPopupWindow.setTouchable(true);
            mPopupWindow.setFocusable(true);
            //popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
            //			mPopupWindow.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
            // mPopupWindow.showAsDropDown(mCityLay);
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            popView.measure(w, h);
            int height = popView.getMeasuredHeight();
            int width = popView.getMeasuredWidth();
            System.out.println("measure width=" + width + " height=" + height);
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            mPopupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, location[0] - width + view.getWidth(), location[1] - height);

            //mPopupWindow.showAtLocation(view,Gravity.TOP,location[0],location[1]-height);
        }

        // mLoadingView.cancleView();
    }

}
