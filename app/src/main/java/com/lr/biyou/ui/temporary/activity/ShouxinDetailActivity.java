package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
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

/**
 * 我的额度  -- 点击列表  -- 授信详情界面
 */
public class ShouxinDetailActivity extends BasicActivity implements RequestView{

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.more_info_lay)
    LinearLayout mMoreInfoLay;
    @BindView(R.id.more_info_but)
    ImageView mMoreInfoBut;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.shouxin_edu_tv)
    TextView mShouxinEduTv;
    @BindView(R.id.shouxin_hetong_tv)
    TextView mShouxinHetongTv;
    @BindView(R.id.hetong_name_tv)
    TextView mHetongNameTv;
    @BindView(R.id.chujieren_tv)
    TextView mChujierenTv;
    @BindView(R.id.jiekuan_ren_tv)
    TextView mJiekuanRenTv;
    @BindView(R.id.shouxin_yue_tv)
    TextView mShouxinYueTv;
    @BindView(R.id.nian_lilv_tv)
    TextView mNianLilvTv;
    @BindView(R.id.danbi_jiekuan_tv)
    TextView mDanbiJiekuanTv;
    @BindView(R.id.huankuan_type_tv)
    TextView mHuankuanTypeTv;
    @BindView(R.id.danbao_type_tv)
    TextView mDanbaoTypeTv;
    @BindView(R.id.danbao_ren_tv)
    TextView mDanbaoRenTv;
    @BindView(R.id.hasno_zhuisuo_tv)
    TextView mHasnoZhuisuoTv;
    @BindView(R.id.baoli_yewu_tv)
    TextView mBaoliYewuTv;
    @BindView(R.id.chuzhuang_type_tv)
    TextView mChuzhuangTypeTv;
    @BindView(R.id.shouxin_stop_tv)
    TextView mShouxinStopTv;
    @BindView(R.id.jiekuan_xinxi_lay)
    CardView mJiekuanXinxiLay;
    @BindView(R.id.fujian_lay)
    CardView mFujianLay;
    @BindView(R.id.hetong_lay)
    CardView mHetongLay;
    @BindView(R.id.divide_jiekuan)
    View mDivideJiekuan;
    @BindView(R.id.divide_jiekuan2)
    View mDivideJiekuan2;

    private String mRequestTag = "";

    private Map<String,Object> mDataMap;


    @Override
    public int getContentView() {
        return R.layout.activity_shouxin_detail;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataMap =(Map<String, Object>) bundle.getSerializable("DATA");
        }

        mTitleText.setText(getResources().getString(R.string.my_larger_num));

        mMoreInfoLay.setVisibility(View.GONE);

        detailAction();
    }

    private void detailAction(){

        mRequestTag = MethodUrl.shouxinDetail;
        Map<String, String> map = new HashMap<>();
        map.put("creditfile",mDataMap.get("creditfile")+"");
        map.put("flowdate",mDataMap.get("flowdate")+"");
        map.put("flowid",mDataMap.get("flowid")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.shouxinDetail, map);
    }

    private void initValue(){

        String status = mDataMap.get("creditstate")+"";
        switch (status){
            case "0":
                //viewHolder.mStatusTv.setText("未开通");
                break;
            case "1":
                //viewHolder.mStatusTv.setText("已开通");
                break;
            case "2":
                //viewHolder.mStatusTv.setText("暂停");
                break;
            case "3":
                //viewHolder.mStatusTv.setText("终止");
                mJiekuanXinxiLay.setVisibility(View.GONE);
                mDivideJiekuan.setVisibility(View.GONE);
                mDivideJiekuan2.setVisibility(View.GONE);
                break;
            case "4":
                //viewHolder.mStatusTv.setText("已签署");
                break;
        }


        //Map<String,Object> hkType = SelectDataUtil.getMap(mDataMap.get("hktypenm")+"",SelectDataUtil.getHkType());
        Map<String,Object> hkType = SelectDataUtil.getMap(mDataMap.get("hktypenm")+"",SelectDataUtil.getNameCodeByType("repayWay"));
        //Map<String,Object> danboType = SelectDataUtil.getMap(mDataMap.get("assutype")+"",SelectDataUtil.getDanbaoType());
        Map<String,Object> danboType = SelectDataUtil.getMap(mDataMap.get("assutype")+"",SelectDataUtil.getNameCodeByType("assuType"));
        Map<String,Object> hasZhuisuo = SelectDataUtil.getMap(mDataMap.get("blzhsoqn")+"",SelectDataUtil.getHasZhuisuo());
        Map<String,Object> baoliType = SelectDataUtil.getMap(mDataMap.get("blyewufs")+"",SelectDataUtil.getBaoliType());
        Map<String,Object> chuzhuangType = SelectDataUtil.getMap(mDataMap.get("vrtacct")+"",SelectDataUtil.getChuzhangType());

        //Map<String,Object> danwei = SelectDataUtil.getMap(mDataMap.get("singleunit")+"",SelectDataUtil.getQixianDw());
        Map<String,Object> danwei = SelectDataUtil.getMap(mDataMap.get("singleunit")+"",SelectDataUtil.getNameCodeByType("limitUnit"));

        mShouxinEduTv.setText(UtilTools.getRMBMoney(mDataMap.get("creditmoney")+""));
        mShouxinHetongTv.setText(mDataMap.get("creditfile")+"");
        mHetongNameTv.setText(UtilTools.empty(mDataMap.get("creditfilename")+"") ? "" : (mDataMap.get("creditfilename")+""));
        mChujierenTv.setText(mDataMap.get("zifangnme")+"");
        mJiekuanRenTv.setText("");//借款人--------------------------
        mShouxinYueTv.setText(UtilTools.getRMBMoney(mDataMap.get("leftmoney")+""));
        mNianLilvTv.setText(UtilTools.getlilv(mDataMap.get("daiklilv")+""));
        mDanbiJiekuanTv.setText(mDataMap.get("singlelimit")+""+danwei.get("name"));
        //mHuankuanTypeTv.setText(hkType.get("name")+"");
        mHuankuanTypeTv.setText(UtilTools.empty(mDataMap.get("hktypenm")+"") ?  "" : (mDataMap.get("hktypenm")+""));
        mDanbaoTypeTv.setText(danboType.get("name")+"");
        mDanbaoRenTv.setText("");
        mHasnoZhuisuoTv.setText(hasZhuisuo.get("name")+"");
        mBaoliYewuTv.setText(baoliType.get("name")+"");
        mChuzhuangTypeTv.setText(chuzhuangType.get("name")+"");


        String time = UtilTools.getStringFromSting2(mDataMap.get("enddate")+"","yyyyMMdd","yyyy-MM-dd");
        mShouxinStopTv.setText(time);

    }


    @OnClick({R.id.back_img, R.id.more_info_but,R.id.jiekuan_xinxi_lay, R.id.fujian_lay, R.id.hetong_lay,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.more_info_but:
                if (mMoreInfoLay.isShown()) {
                    mMoreInfoLay.setVisibility(View.GONE);
                    mMoreInfoBut.setImageResource(R.drawable.android_list_down);
                } else {
                    mMoreInfoLay.setVisibility(View.VISIBLE);
                    mMoreInfoBut.setImageResource(R.drawable.android_list_up);
                }
                break;
            case R.id.jiekuan_xinxi_lay:
                intent = new Intent(ShouxinDetailActivity.this,ShouxinJkListActivity.class);
                intent.putExtra("DATA",(Serializable) mDataMap);
                startActivity(intent);
                break;
            case R.id.fujian_lay:
                break;
            case R.id.hetong_lay:
                intent = new Intent(ShouxinDetailActivity.this,HeTongShowActivity.class);
                List<Map<String,Object>> mHetongList = (List<Map<String,Object>>) mDataMap.get("contList");
                intent.putExtra("DATA",(Serializable) mHetongList);
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
        Intent intent ;
        switch (mType) {
            case MethodUrl.shouxinDetail://
                mDataMap = tData;
                initValue();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.shouxinDetail:
                        detailAction();
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
