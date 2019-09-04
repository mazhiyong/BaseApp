package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 企业基本信息展示  界面
 */
public class QiyeInfoShowActivity extends BasicActivity implements RequestView , ReLoadingData {


    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.qiye_name_tv)
    TextView mQiyeNameTv;
    @BindView(R.id.qiye_daima_tv)
    TextView mQiyeDaimaTv;
    @BindView(R.id.zhuce_hao_tv)
    TextView mZhuceHaoTv;
    @BindView(R.id.qiye_fading_name_tv)
    TextView mQiyeFadingNameTv;
    @BindView(R.id.qiye_zhuce_money_tv)
    TextView mQiyeZhuceMoneyTv;
    @BindView(R.id.qiye_really_money_tv)
    TextView mQiyeReallyMoneyTv;
    @BindView(R.id.qiye_jingying_status_tv)
    TextView mQiyeJingyingStatusTv;
    @BindView(R.id.qiye_kind_tv)
    TextView mQiyeKindTv;
    @BindView(R.id.qiye_hangye_tv)
    TextView mQiyeHangyeTv;
    @BindView(R.id.qiye_open_date_tv)
    TextView mQiyeOpenDateTv;
    @BindView(R.id.qiye_jingying_qixian_tv)
    TextView mQiyeJingyingQixianTv;
    @BindView(R.id.qiye_address_tv)
    TextView mQiyeAddressTv;
    @BindView(R.id.qiye_dengji_tv)
    TextView mQiyeDengjiTv;
    @BindView(R.id.qiye_all_pro_tv)
    TextView mQiyeAllProTv;
    @BindView(R.id.qiye_normal_pro_tv)
    TextView mQiyeNormalProTv;
    @BindView(R.id.qiye_jingying_fanwei_tv)
    TextView mQiyeJingyingFanweiTv;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.content)
    LinearLayout mContent;

    private String mRequestTag = "";

    private String mBackType = "";

    @Override
    public int getContentView() {
        return R.layout.activity_qiye_info_show;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.qiye_info));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mBackType = bundle.getString("backtype");
        }

        if (mBackType.equals("2")){
            mBtnSubmit.setText("返回");
        }

        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        getCompanyInfo();
    }


    private void getCompanyInfo() {
        mRequestTag = MethodUrl.companyInfo;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.companyInfo, map);
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
            case MethodUrl.companyInfo:

                initValueTv(tData);
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.companyInfo:
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mPageView.showNetworkError();
        dealFailInfo(map, mType);
    }


    //{
    //	"custid": "1910715000014349",
    //	"addr": "湖南省怀化市鹤城区锦溪南路岳麓欧城一期第17栋1904号",
    //	"busipermitem": "日用百货、五金交电、厨具、办公用品、服装、家用电器、电子产品、宠物用品、宠物食品、宠物药品、保健品的批发及零售。（依法须经批准的项目，经相关部门批准后方可开展经营活动）",
    //	"district": "431202",
    //	"eptype": null,
    //	"genebusiitem": "日用百货、五金交电、厨具、办公用品、服装、家用电器、电子产品、宠物用品、宠物食品、宠物药品、保健品的批发及零售。（依法须经批准的项目，经相关部门批准后方可开展经营活动）",
    //	"opendate": "20160601",
    //	"operateplace": "百货零售",
    //	"opercap": "在营（开业）",
    //	"opteend": null,
    //	"optescope": "日用百货、五金交电、厨具、办公用品、服装、家用电器、电子产品、宠物用品、宠物食品、宠物药品、保健品的批发及零售。（依法须经批准的项目，经相关部门批准后方可开展经营活动）",
    //	"optestart": "20160601",
    //	"paidmny": null,
    //	"registmny": "200.000000",
    //	"registno": "431200000079989",
    //	"regorg": "怀化市工商行政管理局鹤城分局",
    //	"regorgcode": "431202",
    //	"regorgprov": "湖南省"
    //}
    private void initValueTv(Map<String,Object> tData){

        //企业名称
        String firmname = tData.get("firmname")+"";
        if (UtilTools.empty(firmname)){
            firmname = "";
        }
        //统一社会信用代码
        String zuzhjgdm = tData.get("zuzhjgdm")+"";
        if (UtilTools.empty(zuzhjgdm)){
            zuzhjgdm = "";
        }
        //注册号
        String registno = tData.get("registno")+"";
        if (UtilTools.empty(registno)){
            registno = "";
        }
        //法定代表人名称
        String farnname = tData.get("farnname")+"";
        if (UtilTools.empty(farnname)){
            farnname = "";
        }
        //注册资本
        String registmny = tData.get("registmny")+"";
        if (UtilTools.empty(registmny)){
            registmny = "";
        }
        //实收资本
        String paidmny = tData.get("paidmny")+"";
        if (UtilTools.empty(paidmny)){
            paidmny = "";
        }
        //经营状态
        String opercap = tData.get("opercap")+"";
        if (UtilTools.empty(opercap)){
            opercap = "";
        }
        //企业类型
        String eptype = tData.get("eptype")+"";
        if (UtilTools.empty(eptype)){
            eptype = "";
        }

        //所属行业
        String operateplace = tData.get("operateplace")+"";
        if (UtilTools.empty(operateplace)){
            operateplace = "";
        }
        //开业日期
        String opendate = tData.get("opendate")+"";
        if (UtilTools.empty(opendate)){
            opendate = "";
        }
        //经营期限
        /*String eptype = tData.get("eptype")+"";
        if (UtilTools.empty(eptype)){
            eptype = "";
        }*/
        //企业地址
        String addr = tData.get("addr")+"";
        if (UtilTools.empty(addr)){
            addr = "";
        }
        //登记机关
        String regorg = tData.get("regorg")+"";
        if (UtilTools.empty(regorg)){
            regorg = "";
        }
        //许可经营项目
        String busipermitem = tData.get("busipermitem")+"";
        if (UtilTools.empty(busipermitem)){
            busipermitem = "";
        }
        //一般经营项目
        String genebusiitem = tData.get("genebusiitem")+"";
        if (UtilTools.empty(genebusiitem)){
            genebusiitem = "";
        }
        //经营范围
        String optescope = tData.get("optescope")+"";
        if (UtilTools.empty(optescope)){
            optescope = "";
        }

        mQiyeNameTv.setText(firmname);//企业名称
        mQiyeDaimaTv.setText(zuzhjgdm);//统一社会信用代码
        mZhuceHaoTv.setText(registno);//注册号
        mQiyeFadingNameTv.setText(farnname);//法定代表人名称
        mQiyeZhuceMoneyTv.setText(registmny);//注册资本
        mQiyeReallyMoneyTv.setText(paidmny);//实收资本
        mQiyeJingyingStatusTv.setText(opercap);//经营状态 -------------
        mQiyeKindTv.setText(eptype);//企业类型  --------
        mQiyeHangyeTv.setText(operateplace);//所属行业   -----------
        mQiyeOpenDateTv.setText(opendate);//开业日期
        mQiyeJingyingQixianTv.setText("");//经营期限  ------------------
        mQiyeAddressTv.setText(addr);//企业地址
        mQiyeDengjiTv.setText(regorg);//登记机关
        mQiyeAllProTv.setText(busipermitem);//许可经营项目
        mQiyeNormalProTv.setText(genebusiitem);//一般经营项目
        mQiyeJingyingFanweiTv.setText(optescope);//经营范围


        mPageView.showContent();

    }


    @OnClick({R.id.left_back_lay, R.id.btn_submit})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.btn_submit:
                if (mBackType.equals("2")){
                    finish();
                }else {
                    intent = new Intent(QiyeInfoShowActivity.this, QiyeCardInfoActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void reLoadingData() {
        getCompanyInfo();
    }
}
