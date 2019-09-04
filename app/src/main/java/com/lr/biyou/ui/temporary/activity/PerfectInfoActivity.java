package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.dialog.AddressSelectDialog2;
import com.lr.biyou.mywidget.dialog.MySelectDialog;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PerfectInfoActivity extends BasicActivity implements SelectBackListener, RequestView {
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
    @BindView(R.id.name_value_tv)
    TextView mNameValueTv;
    @BindView(R.id.card_num_value_tv)
    TextView mCardNumValueTv;
    @BindView(R.id.country_value_tv)
    TextView mCountryValueTv;
    @BindView(R.id.country_lay)
    CardView mCountryLay;
    @BindView(R.id.huji_value_tv)
    TextView mHujiValueTv;
    @BindView(R.id.huji_lay)
    CardView mHujiLay;
    @BindView(R.id.other_lay)
    CardView mOtherLay;
    @BindView(R.id.other_value_eddit)
    EditText mOtherValueEddit;
    @BindView(R.id.marray_value_tv)
    TextView mMarrayValueTv;
    @BindView(R.id.marray_lay)
    CardView mMarrayLay;
    @BindView(R.id.juzhu_value_tv)
    TextView mJuzhuValueTv;
    @BindView(R.id.juzhu_lay)
    CardView mJuzhuLay;
    @BindView(R.id.juzhu_detail_value_edit)
    EditText mJuzhuDetailValueEdit;
    @BindView(R.id.zhufang_value_tv)
    TextView mZhufangValueTv;
    @BindView(R.id.zhufang_info_lay)
    CardView mZhufangInfoLay;
    @BindView(R.id.zujin_value_edit)
    EditText mZujinValueEdit;
    @BindView(R.id.other_value_edit2)
    EditText mOtherValueEdit2;
    @BindView(R.id.gdphone_value_edit)
    EditText mGdphoneValueEdit;
    @BindView(R.id.tx_address_value_tv)
    TextView mTxAddressValueTv;
    @BindView(R.id.tx_address_lay)
    CardView mTxAddressLay;
    @BindView(R.id.txdetail_value_edit)
    EditText mTxdetailValueEdit;
    @BindView(R.id.busines_name_lay)
    LinearLayout mBusinesNameLay;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.education_value_tv)
    TextView mEducationValueTv;
    @BindView(R.id.education_lay)
    CardView mEducationLay;
    @BindView(R.id.huji_line)
    View mHujiLine;
    @BindView(R.id.other_line)
    View mOtherLine;
    @BindView(R.id.juzhu_detail_lay)
    CardView mJuzhuDetailLay;
    @BindView(R.id.zujin_lay)
    CardView mZujinLay;
    @BindView(R.id.other_lay2)
    CardView mOtherLay2;
    @BindView(R.id.juzhu_detail_line)
    View mJuzhuDetailLine;
    @BindView(R.id.zujin_line)
    View mZujinLine;
    @BindView(R.id.other_line2)
    View mOtherLine2;
    @BindView(R.id.tx_detail_line)
    View mTxDetailLine;
    @BindView(R.id.tx_detail_lay)
    CardView mTxDetailLay;
    @BindView(R.id.minzu_value_tv)
    TextView mMinzuValueTv;
    @BindView(R.id.minzu_lay)
    CardView mMinzuLay;


    private MySelectDialog mGuojiDialog;
    private MySelectDialog mHunyinDialog;
    private MySelectDialog mXueliDialog;
    private MySelectDialog mZhufangDialog;

    private AddressSelectDialog2 mHuJiAddSelectDialog;
    private AddressSelectDialog2 mJuZhuAddSelectDialog;
    private AddressSelectDialog2 mTxAddSelectDialog;

    private String mRequestTag = "";

    private Map<String, Object> mData;

    private String mViewType = "";
    private Animation mShake;


    @Override
    public int getContentView() {
        return R.layout.activity_perfect_info;
    }

    @Override
    public void init() {
//        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 60);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.perfect_info));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mViewType = bundle.getString("type");
        }

        mShake = AnimationUtils.loadAnimation(this, R.anim.shake);//加载动画资源文件


        mHujiLay.setVisibility(View.GONE);
        mOtherLay.setVisibility(View.GONE);
        mHujiLine.setVisibility(View.GONE);
        mOtherLine.setVisibility(View.GONE);

        mJuzhuDetailLine.setVisibility(View.GONE);
        mJuzhuDetailLay.setVisibility(View.GONE);

        mZujinLine.setVisibility(View.GONE);
        mZujinLay.setVisibility(View.GONE);
        mOtherLine2.setVisibility(View.GONE);
        mOtherLay2.setVisibility(View.GONE);

        mTxDetailLay.setVisibility(View.GONE);
        mTxDetailLine.setVisibility(View.GONE);

        UtilTools.setMoneyEdit(mZujinValueEdit,0);

        initAllDialog();

        getMoreInfo();

    }

    /**
     * 获取用户更多资料信息
     */
    private void getMoreInfo() {

        mRequestTag = MethodUrl.userMoreInfo;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.userMoreInfo, map);
    }

    private void initValutView() {


        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("nation");
        mGuojiDialog = new MySelectDialog(this, true, list, "国籍", 1);
        mGuojiDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list2 = SelectDataUtil.getMarry();
        List<Map<String, Object>> list2 = SelectDataUtil.getNameCodeByType("marital");
        mHunyinDialog = new MySelectDialog(this, true, list2, "婚姻", 2);
        mHunyinDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list3 = SelectDataUtil.getEducation();
        List<Map<String, Object>> list3 = SelectDataUtil.getNameCodeByType("education");
        mXueliDialog = new MySelectDialog(this, true, list3, "学历", 3);
        mXueliDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list4 = SelectDataUtil.getHouse();
        List<Map<String, Object>> list4 = SelectDataUtil.getNameCodeByType("house");
        mZhufangDialog = new MySelectDialog(this, true, list4, "住房信息", 4);
        mZhufangDialog.setSelectBackListener(this);

        String minzuCode = mData.get("nation")+"";
        String minzuName = mData.get("nationname")+"";

        String name = mData.get("finame") + "";
        String idCardNum = mData.get("farnzjno") + "";
        String huji = mData.get("register") + "";
        String guoji = mData.get("nationality") + "";
        String hunyin = mData.get("marry") + "";
        String xueli = mData.get("education") + "";
        String juzhuShengName = mData.get("houprname") + "";
        String juzhuShengCode = mData.get("houprcode") + "";
        String juzhuCityName = mData.get("houciname") + "";
        String juzhuCityCode = mData.get("houcicode") + "";

        String juzhuAdd = juzhuShengName + "" + juzhuCityName;


        if (UtilTools.empty(juzhuShengCode) || UtilTools.empty(juzhuCityCode)) {
            mJuzhuDetailLay.setVisibility(View.GONE);
            mJuzhuDetailLine.setVisibility(View.GONE);
        } else {
            mJuzhuAddMap.put("proname", juzhuShengName);//居住地址  省份名称
            mJuzhuAddMap.put("procode", juzhuShengCode);//居住地址  省份code
            mJuzhuAddMap.put("cityname", juzhuCityName);//居住地址  城市名称
            mJuzhuAddMap.put("citycode", juzhuCityCode);//居住地址  城市code
            mJuzhuDetailLay.setVisibility(View.VISIBLE);
            mJuzhuDetailLine.setVisibility(View.VISIBLE);
            mJuzhuDetailValueEdit.setText(mData.get("houaddr") + "");

        }
        String txShengName = mData.get("cmnprname") + "";
        String txShengCode = mData.get("cmnprcode") + "";
        String txCityName = mData.get("cmnciname") + "";
        String txCityCode = mData.get("cmncicode") + "";
        String txAdd = txShengName + "" + txCityName;

        if (UtilTools.empty(txShengCode) || UtilTools.empty(txCityCode)) {
            mTxDetailLay.setVisibility(View.GONE);
            mTxDetailLine.setVisibility(View.GONE);

        } else {
            mTxAddMap.put("proname", txShengName);//通讯地址  省份名称
            mTxAddMap.put("procode", txShengCode);//通讯地址  省份code
            mTxAddMap.put("cityname", txCityName);//通讯地址  城市名称
            mTxAddMap.put("citycode", txCityCode);//通讯地址  城市code
            mTxDetailLay.setVisibility(View.VISIBLE);
            mTxDetailLine.setVisibility(View.VISIBLE);
            mTxdetailValueEdit.setText(mData.get("cmnaddr") + "");
        }


        String zhufangZk = mData.get("houproperty") + "";
        String guhua = mData.get("phone") + "";

        if (UtilTools.empty(name)) {
        } else {
            mNameValueTv.setText(name);
        }
        if (UtilTools.empty(idCardNum)) {
        } else {
           // mCardNumValueTv.setText(idCardNum);
            mCardNumValueTv.setText(UtilTools.getIDCardXing(idCardNum));
        }
        if (UtilTools.empty(huji)) {
        } else {
            mHujiValueTv.setText(huji);
            mHujiAddMap = new HashMap<>();
            mHujiAddMap.put("name", huji);
        }

        if (UtilTools.empty(minzuCode) || UtilTools.empty(minzuName)) {
        } else {
            mMinzuValueTv.setText(minzuName);
            mMinZuMap.put("code",minzuCode);
            mMinZuMap.put("name",minzuName);
        }
        if (UtilTools.empty(guhua)) {
        } else {
            mGdphoneValueEdit.setText(guhua);
        }

        if (UtilTools.empty(juzhuShengCode) || UtilTools.empty(juzhuCityCode)) {
        } else {
            mJuzhuValueTv.setText(juzhuAdd);
        }
        if (UtilTools.empty(txShengCode) || UtilTools.empty(txCityCode)) {
        } else {
            mTxAddressValueTv.setText(txAdd);
        }

        if (UtilTools.empty(guoji)) {
            mCountryValueTv.setText("");
        } else {
            if (guoji.equals("OTHER")) {
                mOtherLay.setVisibility(View.VISIBLE);
                mHujiLay.setVisibility(View.GONE);

                mOtherLine.setVisibility(View.VISIBLE);
                mHujiLine.setVisibility(View.GONE);
            } else {
                mOtherLay.setVisibility(View.GONE);
                mHujiLay.setVisibility(View.VISIBLE);

                mOtherLine.setVisibility(View.GONE);
                mHujiLine.setVisibility(View.VISIBLE);
            }
            mCountryMap = SelectDataUtil.getMap(guoji, SelectDataUtil.getNameCodeByType("nation"));
            if (mCountryMap != null) {
                mCountryValueTv.setText(mCountryMap.get("name") + "");
            }
        }

        if (UtilTools.empty(hunyin)) {
            mMarrayValueTv.setText("");
        } else {
            mMarryMap = SelectDataUtil.getMap(hunyin, SelectDataUtil.getNameCodeByType("marital"));
            if (mMarryMap != null) {
                mMarrayValueTv.setText(mMarryMap.get("name") + "");
            }
        }

        if (UtilTools.empty(xueli)) {
            mEducationValueTv.setText("");
        } else {
            mEducationMap = SelectDataUtil.getMap(xueli, SelectDataUtil.getNameCodeByType("education"));
            if (mEducationMap != null) {
                mEducationValueTv.setText(mEducationMap.get("name") + "");
            }
        }

        if (UtilTools.empty(zhufangZk)) {
            mZhufangValueTv.setText("");
        } else {
            if (zhufangZk.equals("6")) {
                mOtherLay2.setVisibility(View.VISIBLE);
                mZujinLay.setVisibility(View.GONE);
                mOtherLine2.setVisibility(View.VISIBLE);
                mZujinLine.setVisibility(View.GONE);
                mOtherValueEdit2.setText(mData.get("houmemo")+"");
            } else if (zhufangZk.equals("5")) {
                mOtherLay2.setVisibility(View.GONE);
                mZujinLay.setVisibility(View.VISIBLE);
                mOtherLine2.setVisibility(View.GONE);
                mZujinLine.setVisibility(View.VISIBLE);
                mZujinValueEdit.setText(mData.get("houmonthrent")+"");
            } else {
                mOtherLay2.setVisibility(View.GONE);
                mZujinLay.setVisibility(View.GONE);
                mOtherLine2.setVisibility(View.GONE);
                mZujinLine.setVisibility(View.GONE);
            }

            mZhuFangMap = SelectDataUtil.getMap(zhufangZk, SelectDataUtil.getNameCodeByType("house"));
            if (mZhuFangMap != null) {
                mZhufangValueTv.setText(mZhuFangMap.get("name") + "");
            }
        }
    }


    private void getItemValues() {

        String name = mData.get("finame") + "";
        String idCardNum = mData.get("farnzjno") + "";

        Map<String, String> valueMap = new HashMap<>();

        if (mCountryMap == null || mCountryMap.isEmpty()) {
            UtilTools.isEmpty(mCountryValueTv, "国籍");
            showToastMsg("请选择国籍");
            return;
        }

        if ((mCountryMap.get("code") + "").equals("OTHER")) {
            if (UtilTools.isEmpty(mOtherValueEddit, "其它")) {
                showToastMsg("请输入内容");
                return;
            }
        } else {
            if (mHujiAddMap == null || mHujiAddMap.isEmpty()) {
                UtilTools.isEmpty(mHujiValueTv, "户口所在地");
                showToastMsg("请选择户口所在地");
                return;
            } else {

            }
        }

        if (mMinZuMap == null || mMinZuMap.isEmpty()) {
            UtilTools.isEmpty(mMinzuValueTv, "民族");
            showToastMsg("请选择民族");
            return;
        }
        if (mMarryMap == null || mMarryMap.isEmpty()) {
            UtilTools.isEmpty(mMarrayValueTv, "婚姻");
            showToastMsg("请选择婚姻");
            return;
        }

        if (mEducationMap == null || mEducationMap.isEmpty()) {
            UtilTools.isEmpty(mEducationValueTv, "学历");

            showToastMsg("请选择学历");
            return;
        }
        if (mJuzhuAddMap == null || mJuzhuAddMap.isEmpty()) {
            UtilTools.isEmpty(mJuzhuValueTv, "居住地址");
            showToastMsg("请选择居住地址");
            return;
        }

        if (UtilTools.isEmpty(mJuzhuDetailValueEdit, "详细地址")) {
            showToastMsg("请输入详细地址");
            return;
        }

        /*if(!UtilTools.isContainsChinese(mJuzhuDetailValueEdit.getText()+"")){
            mJuzhuDetailLay.startAnimation(mShake);
            showToastMsg("详细地址不合法，请重新输入");
            return;
        }*/

        if (mZhuFangMap == null || mZhuFangMap.isEmpty()) {
            UtilTools.isEmpty(mZhufangValueTv, "住房信息");
            showToastMsg("请选择住房信息");
            return;
        }

        if ((mZhuFangMap.get("code") + "").equals("6")) {
            if (UtilTools.isEmpty(mOtherValueEdit2, "其它")) {
                showToastMsg("请输入内容");
                return;
            }
        } else if ((mZhuFangMap.get("code") + "").equals("5")) {
            if (UtilTools.isEmpty(mZujinValueEdit, "租金")) {
                showToastMsg("请输入内容");
                return;
            }
        }

        if (UtilTools.isEmpty(mGdphoneValueEdit, "电话")) {
            showToastMsg("请输入电话");
            return;
        }


        if (mTxAddMap == null || mTxAddMap.isEmpty()) {
            UtilTools.isEmpty(mTxAddressValueTv, "通讯地址");
            showToastMsg("请选择通讯地址");
            return;
        }

        if (UtilTools.isEmpty(mTxdetailValueEdit, "详细地址")) {
            showToastMsg("请输入详细地址");
            return;
        }

        /*if(!UtilTools.isContainsChinese(mTxdetailValueEdit.getText()+"")){
            mTxDetailLay.startAnimation(mShake);
            showToastMsg("详细地址不合法，请重新输入");
            return;
        }*/

        valueMap.put("finame", name);//姓名
        valueMap.put("farnzjno", idCardNum);//身份证号
        valueMap.put("nationality", mCountryMap.get("code") + "");//国籍
        valueMap.put("register", mHujiAddMap.get("name") + "");//户口所在地
        valueMap.put("nation", mMinZuMap.get("code") + "");//民族编码
        valueMap.put("marry", mMarryMap.get("code") + "");//婚姻
        valueMap.put("education", mEducationMap.get("code") + "");//学历
        //proname    procode  cityname  citycode  name
        valueMap.put("houprname", mJuzhuAddMap.get("proname") + "");//居住地址  省份名称
        valueMap.put("houprcode", mJuzhuAddMap.get("procode") + "");//居住地址  省份code
        valueMap.put("houciname", mJuzhuAddMap.get("cityname") + "");//居住地址  城市名称
        valueMap.put("houcicode", mJuzhuAddMap.get("citycode") + "");//居住地址  城市code
        valueMap.put("houaddr", mJuzhuDetailValueEdit.getText() + "");//居住地址  详细地址
        valueMap.put("houproperty", mZhuFangMap.get("code") + "");//住房状况  现在居住权

        if ((mZhuFangMap.get("code") + "").equals("6")) {
            valueMap.put("houmemo", mOtherValueEdit2.getText() + "");//住房状况  其它
        } else if ((mZhuFangMap.get("code") + "").equals("5")) {
            valueMap.put("houmonthrent", mZujinValueEdit.getText() + "");//住房状况  月租金
        }
        valueMap.put("phone", mGdphoneValueEdit.getText() + "");// 固定电话

        //proname    procode  cityname  citycode  name
        valueMap.put("cmnprname", mTxAddMap.get("proname") + "");//通讯地址  省份名称
        valueMap.put("cmnprcode", mTxAddMap.get("procode") + "");//通讯地址  省份code
        valueMap.put("cmnciname", mTxAddMap.get("cityname") + "");//通讯地址  城市名称
        valueMap.put("cmncicode", mTxAddMap.get("citycode") + "");//通讯地址  城市code
        valueMap.put("cmnaddr", mTxdetailValueEdit.getText() + "");//通讯地址  详细地址


        LogUtilDebug.i("打印log日志","最后的结果是" + valueMap);
        Intent intent = new Intent(this, WorkInfoActivity.class);
        intent.putExtra("DATA", (Serializable) valueMap);
        intent.putExtra("DEFAULT_DATA", (Serializable) mData);
        intent.putExtra("type",mViewType);
        startActivity(intent);

    }


    private void initAllDialog() {
        //List<Map<String, Object>> list = SelectDataUtil.getCountry();
        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("nation");
        mGuojiDialog = new MySelectDialog(this, true, list, "国籍", 1);
        mGuojiDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list2 = SelectDataUtil.getMarry();
        List<Map<String, Object>> list2 = SelectDataUtil.getNameCodeByType("marital");
        mHunyinDialog = new MySelectDialog(this, true, list2, "婚姻", 2);
        mHunyinDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list3 = SelectDataUtil.getEducation();
        List<Map<String, Object>> list3 = SelectDataUtil.getNameCodeByType("education");
        mXueliDialog = new MySelectDialog(this, true, list3, "学历", 3);
        mXueliDialog.setSelectBackListener(this);

        //List<Map<String, Object>> list4 = SelectDataUtil.getHouse();
        List<Map<String, Object>> list4 = SelectDataUtil.getNameCodeByType("house");
        mZhufangDialog = new MySelectDialog(this, true, list4, "住房信息", 4);
        mZhufangDialog.setSelectBackListener(this);

        mHuJiAddSelectDialog = new AddressSelectDialog2(this, true, "选择地址", 10);
        mHuJiAddSelectDialog.setSelectBackListener(this);

        mJuZhuAddSelectDialog = new AddressSelectDialog2(this, true, "选择地址", 11);
        mJuZhuAddSelectDialog.setSelectBackListener(this);

        mTxAddSelectDialog = new AddressSelectDialog2(this, true, "选择地址", 12);
        mTxAddSelectDialog.setSelectBackListener(this);

    }


    private void showCountryDialog() {
        mGuojiDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }

    private void showMarryDialog() {
        mHunyinDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }

    private void showEducationDialog() {
        mXueliDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }

    private void showZhufangDialog() {
        mZhufangDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }


    @OnClick({R.id.country_lay, R.id.huji_lay, R.id.marray_lay, R.id.juzhu_lay, R.id.zhufang_info_lay,
            R.id.tx_address_lay, R.id.btn_next, R.id.education_lay, R.id.back_img, R.id.left_back_lay, R.id.minzu_lay})
    public void onViewClicked(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.country_lay:
                showCountryDialog();
                break;
            case R.id.huji_lay:
                mHuJiAddSelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.minzu_lay:
               intent = new Intent(PerfectInfoActivity.this,NormalNameListActivity.class);
               intent.putExtra("type","1");
               startActivityForResult(intent,120);
                break;
            case R.id.marray_lay:
                showMarryDialog();
                break;
            case R.id.education_lay:
                showEducationDialog();
                break;
            case R.id.juzhu_lay:
                mJuZhuAddSelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.zhufang_info_lay:
                showZhufangDialog();
                break;
            case R.id.tx_address_lay:
                mTxAddSelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);

                break;
            case R.id.btn_next:
                getItemValues();
                /*intent = new Intent(PerfectInfoActivity.this,WorkInfoActivity.class);
                startActivity(intent);*/
                break;
        }
    }


    private Map<String, Object> mCountryMap = new HashMap<>();
    private Map<String, Object> mHujiAddMap = new HashMap<>();
    private Map<String, Object> mJuzhuAddMap = new HashMap<>();
    private Map<String, Object> mTxAddMap = new HashMap<>();
    private Map<String, Object> mMarryMap = new HashMap<>();
    private Map<String, Object> mEducationMap = new HashMap<>();
    private Map<String, Object> mZhuFangMap = new HashMap<>();
    private Map<String, Object> mMinZuMap = new HashMap<>();

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 1:
                mCountryMap = map;
                mCountryValueTv.setText(mCountryMap.get("name") + "");
                mCountryValueTv.setError(null, null);

                layShow();
                break;
            case 2:
                mMarryMap = map;
                mMarrayValueTv.setText(mMarryMap.get("name") + "");
                mMarrayValueTv.setError(null, null);
                break;
            case 3:
                mEducationMap = map;
                mEducationValueTv.setText(mEducationMap.get("name") + "");
                mEducationValueTv.setError(null, null);
                break;
            case 4:
                mZhuFangMap = map;
                mZhufangValueTv.setText(mZhuFangMap.get("name") + "");
                mZhufangValueTv.setError(null, null);
                layShow2();
                break;
            case 10:
                mHujiAddMap = map;
                mHujiValueTv.setText(mHujiAddMap.get("name") + "");
                mHujiValueTv.setError(null, null);
                break;
            case 11:
                mJuzhuAddMap = map;
                mJuzhuValueTv.setText(mJuzhuAddMap.get("name") + "");
                mJuzhuValueTv.setError(null, null);
                mJuzhuDetailLine.setVisibility(View.VISIBLE);
                mJuzhuDetailLay.setVisibility(View.VISIBLE);
                break;
            case 12:
                mTxAddMap = map;
                mTxAddressValueTv.setText(mTxAddMap.get("name") + "");
                mTxAddressValueTv.setError(null, null);
                mTxDetailLay.setVisibility(View.VISIBLE);
                mTxDetailLine.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void layShow() {
        String code = mCountryMap.get("code") + "";
        if (code.equals("OTHER")) {
            mOtherLay.setVisibility(View.VISIBLE);
            mHujiLay.setVisibility(View.GONE);

            mOtherLine.setVisibility(View.VISIBLE);
            mHujiLine.setVisibility(View.GONE);

        } else {
            mOtherLay.setVisibility(View.GONE);
            mHujiLay.setVisibility(View.VISIBLE);

            mOtherLine.setVisibility(View.GONE);
            mHujiLine.setVisibility(View.VISIBLE);
        }
    }

    private void layShow2() {
        String code = mZhuFangMap.get("code") + "";
        mOtherValueEdit2.setError(null, null);
        mZujinValueEdit.setError(null, null);
        if (code.equals("5")) {
            mOtherLay2.setVisibility(View.GONE);
            mZujinLay.setVisibility(View.VISIBLE);
            mOtherLine2.setVisibility(View.GONE);
            mZujinLine.setVisibility(View.VISIBLE);
        } else if (code.equals("6")) {
            mOtherLay2.setVisibility(View.VISIBLE);
            mZujinLay.setVisibility(View.GONE);
            mOtherLine2.setVisibility(View.VISIBLE);
            mZujinLine.setVisibility(View.GONE);

        } else {
            mOtherLay2.setVisibility(View.GONE);
            mZujinLay.setVisibility(View.GONE);
            mOtherLine2.setVisibility(View.GONE);
            mZujinLine.setVisibility(View.GONE);
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
        Intent intent;
        switch (mType) {
            case MethodUrl.userMoreInfo:
                mData = tData;
                initValutView();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.userMoreInfo:
                        getMoreInfo();
                        break;
                }
                break;
        }
    }




    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        dealFailInfo(map,mType);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 120:
                    bundle = data.getExtras();
                    if (bundle != null){
                        mMinZuMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mMinzuValueTv.setText(mMinZuMap.get("name")+"");
                        mMinzuValueTv.setError(null, null);

                    }
                    break;
            }
        }
    }

}
