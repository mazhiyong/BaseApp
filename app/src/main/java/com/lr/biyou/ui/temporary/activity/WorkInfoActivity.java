package com.lr.biyou.ui.temporary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.widget.popup.base.BasePopup;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.dialog.AddressSelectDialog2;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.mywidget.dialog.MySelectDialog;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.PopuTipView;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 工作信息  界面
 */
public class WorkInfoActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.work_name_edit)
    EditText mWorkNameEdit;
    @BindView(R.id.work_phone_value_edit)
    EditText mWorkPhoneValueEdit;
    @BindView(R.id.work_address_value_tv)
    TextView mWorkAddressValueTv;
    @BindView(R.id.work_detail_value_edit)
    EditText mWorkDetailValueEdit;
    @BindView(R.id.work_detail_line)
    View mWorkDetailLine;
    @BindView(R.id.zhiwei_value_edit)
    TextView mZhiweiValueEdit;
    @BindView(R.id.zhiwei_lay)
    CardView mZhiweiLay;
    @BindView(R.id.work_xingzhi_value_tv)
    TextView mWorkXingzhiValueTv;
    @BindView(R.id.work_xingzhi_lay)
    CardView mWorkXingzhiLay;
    @BindView(R.id.renzhi_value_tv)
    TextView mRenzhiValueTv;
    @BindView(R.id.renzhi_time_lay)
    CardView mRenzhiTimeLay;
    @BindView(R.id.congye_value_tv)
    TextView mCongyeValueTv;
    @BindView(R.id.congye_time_lay)
    CardView mCongyeTimeLay;
    @BindView(R.id.suoshuhy_edit)
    TextView mSuoshuhyEdit;
    @BindView(R.id.work_address_lay)
    CardView mWorkAddressLay;
    @BindView(R.id.work_address_detail_lay)
    CardView mWorkAddressDetailLay;
    @BindView(R.id.btn_submit)
    Button mSubmitButton;
    @BindView(R.id.tips_icon1)
    ImageView mTipsIcon1;
    @BindView(R.id.tips_icon2)
    ImageView mTipsIcon2;
    @BindView(R.id.yue_shouru_value_tv)
    EditText mYueShouruValueTv;
    @BindView(R.id.yue_shouru_lay)
    CardView mYueShouruLay;
    @BindView(R.id.hangye_lay)
    CardView mHangyeLay;


    private Map<String, Object> mXingzhiMap;
    private Map<String, Object> mZhiyeMap;

    private String mRequestTag = "";

    private Map<String, Object> mValueMap;

    private Map<String, Object> mDefaultMap;


    private String mGongzuoTime = "";
    private String mCongyeTime = "";

    private Map<String, Object> mWorkAddressMap = new HashMap<>();
    private SimpleCustomPop mQuickCustomPopup;

    private Map<String,Object> mHangyeMap;
    private String mViewType = "";

    private List<Map<String,Object>> mZhiyeList = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_work_info;
    }


    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mValueMap = (Map<String, Object>) bundle.getSerializable("DATA");
            mDefaultMap = (Map<String, Object>) bundle.getSerializable("DEFAULT_DATA");
            mViewType = bundle.getString("type");
        }

        mTitleText.setText(getResources().getString(R.string.perfect_info));

        UtilTools.setMoneyEdit(mYueShouruValueTv,0);
        initDiaog();
        initValutView();
    }


    private void initValutView() {
        mQuickCustomPopup = new SimpleCustomPop(this);
        String hangye = mDefaultMap.get("cmptrades") + "";
        if (!UtilTools.empty(hangye)){
            String ss = SelectDataUtil.getJson(this,"hangye_work.json");
            List<Map<String,Object>> mHangyeList =   JSONUtil.getInstance().jsonToList(ss);
            for (Map mm : mHangyeList){
                String code = mm.get("code")+"";
                if (code.equals(hangye)){
                    mHangyeMap = mm;
                    mZhiyeList = (List<Map<String,Object>>) mHangyeMap.get("typeList");
                    break;
                }
            }
        }
        if (mHangyeMap != null ){
            mSuoshuhyEdit.setText(mHangyeMap.get("name") + "");

            String zhiweiCode = mDefaultMap.get("posicode")+"";
            String zhiwei = mDefaultMap.get("posiname")+"";
            if (!UtilTools.empty(zhiweiCode) ){
                mZhiyeMap = new HashMap<>();
                mZhiyeMap.put("code",zhiweiCode);
                mZhiyeMap.put("name",zhiwei);
                mZhiweiValueEdit.setText(mDefaultMap.get("posiname") + "");
            }

        }else {
            mZhiyeMap = null;
            mZhiweiValueEdit.setText("");
            mZhiweiValueEdit.setError(null,null);
        }


        if (UtilTools.empty(mDefaultMap.get("cmpname") + "")){
            mWorkNameEdit.setText("");
        }else {
            mWorkNameEdit.setText(mDefaultMap.get("cmpname") + "");
        }

        if (UtilTools.empty(mDefaultMap.get("cmptel") + "")){

        }else {
            mWorkPhoneValueEdit.setText(mDefaultMap.get("cmptel") + "");
        }



        String workShengName = mDefaultMap.get("cmpprname") + "";

        String workShengCode = mDefaultMap.get("cmpprcode") + "";
        String workCityName = mDefaultMap.get("cmpciname") + "";
        String workCityCode = mDefaultMap.get("cmpcicode") + "";
        if (UtilTools.empty(workShengCode) || UtilTools.empty(workCityCode)) {
            mWorkAddressDetailLay.setVisibility(View.GONE);
            mWorkDetailLine.setVisibility(View.GONE);

        } else {
            mWorkAddressMap.put("proname", workShengName);//通讯地址  省份名称
            mWorkAddressMap.put("procode", workShengCode);//通讯地址  省份code
            mWorkAddressMap.put("cityname", workCityName);//通讯地址  城市名称
            mWorkAddressMap.put("citycode", workCityCode);//通讯地址  城市code
            mWorkAddressDetailLay.setVisibility(View.VISIBLE);
            mWorkDetailLine.setVisibility(View.VISIBLE);
            mWorkAddressValueTv.setText(workShengName + "" + workCityName);
            mWorkDetailValueEdit.setText(mDefaultMap.get("cmpaddr") + "");
        }

       // mZhiweiValueEdit.setText(mDefaultMap.get("position") + "");
        mGongzuoTime = mDefaultMap.get("jobstartdate") + "";
        mCongyeTime = mDefaultMap.get("tradesstartdate") + "";

        mRenzhiValueTv.setText(UtilTools.getStringFromSting2(mGongzuoTime,"yyyyMMdd","yyyy-MM-dd"));
        mCongyeValueTv.setText(UtilTools.getStringFromSting2(mCongyeTime,"yyyyMMdd","yyyy-MM-dd"));


        String yueMoney = mDefaultMap.get("income")+"";
        if (UtilTools.empty(yueMoney)){
            mYueShouruValueTv.setText("");
        }else {
            mYueShouruValueTv.setText(yueMoney);
        }


        String xingzhi = mDefaultMap.get("jobnature") + "";
        mXingzhiMap = SelectDataUtil.getMap(xingzhi, SelectDataUtil.getNameCodeByType("job"));
        if (mXingzhiMap != null) {
            mWorkXingzhiValueTv.setText(mXingzhiMap.get("name") + "");
        }
    }

    /**
     *
     */
    private void submitAction() {

        if (UtilTools.isEmpty(mWorkNameEdit, "单位名称")) {
            showToastMsg("请输入单位名称");
            return;
        }
        if (mHangyeMap == null || mHangyeMap.isEmpty()) {
            UtilTools.isEmpty(mSuoshuhyEdit, "所属行业");
            showToastMsg("请选择所属行业");
            return;
        }
        if (UtilTools.isEmpty(mWorkPhoneValueEdit, "单位电话")) {
            showToastMsg("请输入单位电话");

            return;
        }
        if (mWorkAddressMap == null || mWorkAddressMap.isEmpty()) {
            UtilTools.isEmpty(mWorkAddressValueTv, "单位地址");
            showToastMsg("请选择单位地址");
            return;
        }
        if (UtilTools.isEmpty(mWorkDetailValueEdit, "详细地址")) {
            showToastMsg("请输入详细地址");
            return;
        }
        if (UtilTools.isEmpty(mZhiweiValueEdit, "职位")) {
            showToastMsg("请选择职位");
            return;
        }

        if (mXingzhiMap == null || mXingzhiMap.isEmpty()) {
            UtilTools.isEmpty(mWorkXingzhiValueTv, "工作性质");
            showToastMsg("请选择工作性质");
            return;
        }
        if ( UtilTools.isEmpty(mYueShouruValueTv, "月收入")) {
            showToastMsg("请输入月收入");
            return;
        }
        if (UtilTools.empty(mGongzuoTime)) {
            UtilTools.isEmpty(mRenzhiValueTv, "任职时间");
            showToastMsg("请选择时间");
            return;
        }
        if (UtilTools.empty(mCongyeTime)) {
            UtilTools.isEmpty(mCongyeValueTv, "从业时间");
            showToastMsg("请选择时间");
            return;
        }

        mValueMap.put("cmpname", mWorkNameEdit.getText() + "");//公司名称
        mValueMap.put("cmptrades", mHangyeMap.get("code") + "");//公司行业
        mValueMap.put("cmptel", mWorkPhoneValueEdit.getText() + "");//公司电话
        //proname    procode  cityname  citycode  name
        mValueMap.put("cmpprname", mWorkAddressMap.get("proname") + "");//公司地址 省份名称
        mValueMap.put("cmpprcode", mWorkAddressMap.get("procode") + "");//公司地址 省份code
        mValueMap.put("cmpciname", mWorkAddressMap.get("cityname") + "");//公司地址 城市名称
        mValueMap.put("cmpcicode", mWorkAddressMap.get("citycode") + "");//公司地址 城市code
        mValueMap.put("cmpaddr", mWorkDetailValueEdit.getText() + "");//公司详细地址
        //mValueMap.put("position", mZhiweiValueEdit.getText() + "");//职位
        mValueMap.put("posicode", mZhiyeMap.get("code") + "");//职位
        mValueMap.put("jobnature", mXingzhiMap.get("code") + "");//工作性质
        mValueMap.put("income", mYueShouruValueTv.getText() + "");//月收入
        mValueMap.put("jobstartdate", mGongzuoTime);//现职开始时间
        mValueMap.put("tradesstartdate", mCongyeTime);//所在行业开始时间(格式：yyyymmdd)


        LogUtilDebug.i("打印log日志",mValueMap);
        mRequestTag = MethodUrl.submitUserInfo;

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.submitUserInfo, mValueMap);
    }

    MySelectDialog mWorkDiaog;
    DateSelectDialog mGongZuoDialog;
    DateSelectDialog mCongyeDialog;
    AddressSelectDialog2 mWorkAddressDialog;

    private void initDiaog() {
        //List<Map<String, Object>> list = SelectDataUtil.getJobType();
        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("job");
        mWorkDiaog = new MySelectDialog(this, true, list, "工作性质", 11);
        mWorkDiaog.setSelectBackListener(this);

        mGongZuoDialog = new DateSelectDialog(this, true, "选择日期", 2000);
        mCongyeDialog = new DateSelectDialog(this, true, "选择日期", 2001);
        mGongZuoDialog.setSelectBackListener(this);
        mCongyeDialog.setSelectBackListener(this);


        mWorkAddressDialog = new AddressSelectDialog2(this, true, "选择地址", 31);
        mWorkAddressDialog.setSelectBackListener(this);
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

        switch (mType) {
            case MethodUrl.submitUserInfo:
                Intent intent = new Intent();
                intent.setAction(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE);
                sendBroadcast(intent);
                showToastMsg("提交成功");
                if (mViewType.equals("2")){
                    backTo(MoreInfoManagerActivity.class,false);
                }else if (mViewType.equals("3")){
                    finish();
                }else{
                    backTo(MainActivity.class, true);
                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.submitUserInfo:
                        submitAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        dealFailInfo(map,mType);
    }


    @OnClick({R.id.back_img, R.id.work_xingzhi_lay, R.id.renzhi_time_lay, R.id.congye_time_lay, R.id.work_address_lay,
            R.id.btn_submit, R.id.tips_icon1, R.id.tips_icon2, R.id.left_back_lay,R.id.zhiwei_lay,R.id.hangye_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.hangye_lay:
                intent = new Intent(WorkInfoActivity.this,HangYeActivity.class);
                intent.putExtra("TYPE","1");
                startActivityForResult(intent,60);
                break;
            case R.id.zhiwei_lay:

                if (mHangyeMap == null || mHangyeMap.isEmpty()){
                    showToastMsg("请先选择行业信息");
                }else {
                    if (mZhiyeList == null || mZhiyeList.size() == 0){
                        showToastMsg("职业信息异常，请联系管理员");
                    }else {
                        intent = new Intent(WorkInfoActivity.this,NormalNameListActivity.class);
                        intent.putExtra("type","2");
                        intent.putExtra("DATA",(Serializable) mZhiyeList);
                        startActivityForResult(intent,130);
                    }
                }

                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.work_xingzhi_lay:
                mWorkDiaog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.renzhi_time_lay:
                mGongZuoDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.congye_time_lay:
                mCongyeDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.work_address_lay:
                mWorkAddressDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_submit:
                submitAction();
                break;
            case R.id.tips_icon1:
               /* mQuickCustomPopup.initTvValue("在本单位任职的开始时间");
                mQuickCustomPopup
                        .anchorView(mTipsIcon1)
                        .offset(10, 0)
                        .gravity(Gravity.TOP)
                        .showAnim(null)
                        .dismissAnim(null)
                        .dimEnabled(false)
                        .show();*/
                String s1 ="在本单位任职的开始时间";

                PopuTipView mp1= new PopuTipView(WorkInfoActivity.this,s1,R.layout.popu_lay_top);
                mp1.show(mTipsIcon1,1);

                /*  View inflate = View.inflate(WorkInfoActivity.this, R.layout.popup_bubble_text, null);
                TextView mTextView = inflate.findViewById(R.id.tv_bubble);
                mTextView.setText("在本单位任职的开始时间" );


              new BubblePopup(WorkInfoActivity.this, inflate)
                        .anchorView(mTipsIcon1)
                        .bubbleColor(Color.parseColor("#A6000000"))
                        .gravity(Gravity.TOP)
                        .margin(UtilTools.px2dip(WorkInfoActivity.this,mTipsIcon1.getLeft()),8)
                        .triangleWidth(5)
                        .triangleHeight(5)
                        .showAnim(null)
                        .dismissAnim(null)
                        .show();*/
                break;
            case R.id.tips_icon2:
                /*mQuickCustomPopup.initTvValue("从事本行业的开始时间");

                mQuickCustomPopup
                        .anchorView(mTipsIcon2)
                        .offset(10, 0)
                        .gravity(Gravity.TOP)
                        .showAnim(null)
                        .dismissAnim(null)
                        .dimEnabled(false)
                        .show();*/

                String s2 = "从事本行业的开始时间";

                PopuTipView mp2= new PopuTipView(WorkInfoActivity.this,s2,R.layout.popu_lay_top);
                mp2.show(mTipsIcon2,1);

              /*View inflate2 = View.inflate(WorkInfoActivity.this, R.layout.popup_bubble_text, null);
                TextView mTextView2 = inflate2.findViewById(R.id.tv_bubble);
                mTextView2.setText("" );


             new BubblePopup(WorkInfoActivity.this, inflate2)
                        .anchorView(mTipsIcon2)
                        .bubbleColor(Color.parseColor("#A6000000"))
                        .gravity(Gravity.TOP)
                        .margin(UtilTools.px2dip(WorkInfoActivity.this,mTipsIcon2.getLeft()),8)
                        .triangleWidth(5)
                        .triangleHeight(5)
                        .showAnim(null)
                        .dismissAnim(null)
                        .show();*/
                break;
        }
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 11:
                mXingzhiMap = map;
                mWorkXingzhiValueTv.setText(mXingzhiMap.get("name") + "");
                mWorkXingzhiValueTv.setError(null,null);
                break;
            case 2000:
                mGongzuoTime = map.get("date") + "";
                mRenzhiValueTv.setText(map.get("year") + "-" + map.get("month") + "-" + map.get("day") + "");
                mRenzhiValueTv.setError(null,null);
                break;
            case 2001:
                mCongyeTime = map.get("date") + "";
                mCongyeValueTv.setText(map.get("year") + "-" + map.get("month") + "-" + map.get("day") + "");
                mCongyeValueTv.setError(null,null);
                break;
            case 31:
                mWorkAddressMap = map;
                mWorkAddressValueTv.setText(mWorkAddressMap.get("name") + "");
                mWorkAddressDetailLay.setVisibility(View.VISIBLE);
                mWorkAddressValueTv.setError(null,null);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 60:
                    bundle = data.getExtras();
                    if (bundle != null){
                        mHangyeMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mZhiyeList = (List<Map<String,Object>>) mHangyeMap.get("typeList");
                        mSuoshuhyEdit.setText(mHangyeMap.get("name")+"");
                        mSuoshuhyEdit.setError(null,null);
                        mZhiyeMap = null;
                        mZhiweiValueEdit.setText("");
                        mZhiweiValueEdit.setError(null,null);

                    }
                    break;
                case 130:
                    bundle = data.getExtras();
                    if (bundle != null){
                        mZhiyeMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mZhiweiValueEdit.setText(mZhiyeMap.get("name")+"");
                        mZhiweiValueEdit.setError(null,null);
                    }
                    break;
            }
        }
    }


    private class SimpleCustomPop extends BasePopup<SimpleCustomPop> {


        private TextView mTextView;

        public SimpleCustomPop(Context context) {
            super(context);
//            setCanceledOnTouchOutside(false);
        }

        @Override
        public View onCreatePopupView() {
            View inflate = View.inflate(mContext, R.layout.popup_bubble_text, null);
            mTextView = inflate.findViewById(R.id.tv_bubble);
            return inflate;
        }

        @Override
        public void setUiBeforShow() {
        }

        public void initTvValue(String str) {
            mTextView.setText(str);
        }
    }
}
