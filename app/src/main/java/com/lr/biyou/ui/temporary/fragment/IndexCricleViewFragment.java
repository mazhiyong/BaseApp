package com.lr.biyou.ui.temporary.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.cardview.widget.CardView;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.mywidget.dialog.TipMsgDialog;
import com.lr.biyou.mywidget.view.CircleProgress;
import com.lr.biyou.ui.temporary.activity.ApplyAmountActivity;
import com.lr.biyou.ui.temporary.activity.BankOpenActivity;
import com.lr.biyou.ui.temporary.activity.BankTiXianModifyActivity;
import com.lr.biyou.ui.temporary.activity.BorrowDetailActivity;
import com.lr.biyou.ui.temporary.activity.BorrowMoneyActivity;
import com.lr.biyou.ui.temporary.activity.BorrowMoneySelectActivity;
import com.lr.biyou.ui.temporary.activity.ChongZhiCardAddActivity;
import com.lr.biyou.ui.moudle4.activity.PayMoneyActivity;
import com.lr.biyou.ui.temporary.activity.IdCardSuccessActivity;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.ui.temporary.activity.PeopleCheckActivity;
import com.lr.biyou.ui.temporary.activity.PerfectInfoActivity;
import com.lr.biyou.ui.temporary.activity.SelectBankListActivity;
import com.lr.biyou.ui.temporary.activity.SignLoanActivity;
import com.lr.biyou.ui.temporary.activity.WaitDoWorkActivity;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.mywidget.dialog.HeZuoFangDialog;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.RoundIndicatorView;
import com.lr.biyou.mywidget.view.TipsToast;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.ParseTextUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.mywidget.view.PullScrollView;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class  IndexCricleViewFragment extends BasicFragment implements RequestView, SelectBackListener {


    @BindView(R.id.my_view)
    RoundIndicatorView mMyView;
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.btn_please)
    Button mBtnPlease;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.edu_value_tv)
    TextView mEduValueTv;
    @BindView(R.id.lilv_value_tv)
    TextView mLilvValueTv;

    @BindView(R.id.tips_lay)
    LinearLayout mTipsLay;
    @BindView(R.id.tips_tv)
    TextView mTipsTv;
    @BindView(R.id.refresh_layout)
    PullScrollView refreshLayout;
    @BindView(R.id.more_daiban_tv)
    TextView mMoreDaibanTv;
    @BindView(R.id.more_daiban_lay)
    CardView mMoreDaibanLay;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_bank)
    TextView mTvBank;
    @BindView(R.id.status_tv)
    TextView mStatusTv;
    @BindView(R.id.item_waitdo)
    CardView mItemWaitdo;
    @BindView(R.id.tv_money_bohui)
    TextView mTvMoneyBohui;
    @BindView(R.id.tv_bank_bohui)
    TextView mTvBankBohui;
    @BindView(R.id.tv_date_bohui)
    TextView mTvDateBohui;
    @BindView(R.id.but_xiuding)
    TextView mButXiuding;
    @BindView(R.id.item_bohui)
    CardView mItemBohui;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;

    @BindView(R.id.circle_progress)
    CircleProgress mCircleProgress;


    private Map<String, Object> mMoneyMap = new HashMap<>();
    private String mRequestTag = "";

    private Map<String,Object> mZhengshuMap;


    private HeZuoFangDialog mHeZuoFangDialog;

    private HeZuoFangDialog mJieKuanDialog;

    private Map<String, Object> mHezuoMap;

    private Map<String, Object> mJieKuanMap;

    private Map<String, Object> mApplyConfigMap;


    private double mTotalMoney;
    private double mLeftMoney;

    private ParseTextUtil mParseTextUtil;

    private LoadingWindow mLoadingWindow;

    private boolean mIsback = false;

    public IndexCricleViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.mInstance.mIsRefresh) {
            mApplyConfigMap = null;
            totleMoney();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_circle_view;
    }

    @Override
    public void init() {
        initView();
    }


    private void initView() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.ZHENGSHU_UPDATE);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.DAIBAN_INFO_UPDATE);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.SHOUXIN_UPDATE);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE);
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(getActivity()));
        mTitleBarView.setLayoutParams(layoutParams);
        mTitleBarView.setPadding(0, UtilTools.getStatusHeight2(getActivity()), 0, 0);
        mTitleText.setText("币友");

        mLeftBackLay.setVisibility(View.GONE);
        mBtnPlease.setEnabled(false);
        mMyView.setCurrentNumAnim(0);

        mParseTextUtil = new ParseTextUtil(getActivity());
        mLoadingWindow = new LoadingWindow(getActivity(), R.style.Dialog);

        mLoadingWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                refreshLayout.setRefreshCompleted();
                System.out.println("dialog消失了监听");
                //mBtnPlease.setEnabled(true);
            }
        });


      /*  sqCheck();//授权申请校验
        jiekuanCheck();//借款授信列表*/

        refreshLayout = (PullScrollView) mRootView.findViewById(R.id.refresh_layout);
        //设置头部加载颜色
        //refreshLayout.setHeaderViewColor(R.color.colorAccent, R.color.black ,android.R.color.white);
        // refreshLayout.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);

        refreshLayout.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        refreshLayout.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        //refreshLayout.setRefreshHeader(new MyRefreshHeader(getActivity()));


        refreshLayout.setRefreshListener(new PullScrollView.RefreshListener() {
            @Override
            public void onRefresh() {
               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshCompleted();
                    }
                }, 1000);*/
                mApplyConfigMap = null;
                totleMoney();
                doworkListAction();
            }
        });

        mLoadingWindow.showView();
        mMoreDaibanLay.setVisibility(View.GONE);
        mItemBohui.setVisibility(View.GONE);
        mItemWaitdo.setVisibility(View.GONE);
        isInstallCer();
        doworkListAction();
        totleMoney();
        //refreshLayout.refreshWithPull();
        setBarTextColor();

        mCircleProgress.setValue(0);
    }
    public void setBarTextColor(){
        StatusBarUtil.setLightMode(getActivity());
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE)){

            }else if (action.equals(MbsConstans.BroadcastReceiverAction.ZHENGSHU_UPDATE)){
                isInstallCer();
            }else if (action.equals(MbsConstans.BroadcastReceiverAction.DAIBAN_INFO_UPDATE)){
                doworkListAction();
            }else if (action.equals(MbsConstans.BroadcastReceiverAction.SHOUXIN_UPDATE)){
                mApplyConfigMap = null;
                totleMoney();
            }else if (action.equals(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE)){
                mApplyConfigMap = null;
                totleMoney();
                doworkListAction();
            }
        }
    };

    private void isInstallCer() {
        mLoadingWindow.showView();
        mRequestTag = MethodUrl.isInstallCer;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.isInstallCer, map);
    }

    //获取总额度
    private void totleMoney() {
        mRequestTag = MethodUrl.totleMoney;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.totleMoney, map);
    }

    //申请额度前， 授信申请校验  包括合作方列表  是否完善资料 等等信息
    private void sqCheck() {
        mRequestTag = MethodUrl.reqCheck;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.reqCheck, map);
    }

    //借款前的配置信息获取   借款前选择的合作方   合作方列表
    private void jiekuanCheck() {
        if (MbsConstans.USER_MAP != null) {//是否完善信息（1：已完善 0：未完善）
            String ss = MbsConstans.USER_MAP.get("cmpl_info") + "";
            if (ss.equals("1")) {
            } else {

                showMsgDialog(getResources().getString(R.string.perfect_info_tip),true);
                /*Intent intent = new Intent(getActivity(), PerfectInfoActivity.class);
                startActivity(intent);*/
            }

        } else {
            MainActivity.mInstance.getUserInfoAction();
            TipsToast.showToastMsg("获取用户基本信息失败,请重新获取");
            return;
        }
        refreshLayout.setRefreshCompleted();

        mRequestTag = MethodUrl.jiekuanSxList;
        Map<String, String> map = new HashMap<>();
        map.put("creditstate", "1");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.jiekuanSxList, map);
    }


    //二类户查询列表
    private void erLeiHuList() {
        mRequestTag = MethodUrl.erleiHuList;
        Map<String, String> map = new HashMap<>();
        map.put("patncode", mHezuoMap.get("patncode") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.erleiHuList, map);
    }

    private void dealApplyConfig() {

        String isPerfectInfo = mApplyConfigMap.get("iscmp") + "";
        String prestate = mApplyConfigMap.get("prestate") + "";
        //是否完善信息（1：已完善 0：未完善）
        mBtnPlease.setEnabled(true);
        if (isPerfectInfo.equals("1")){
            switch (prestate) {//预授信申请状态（10： 未申请 11：已申请 12：担保已审核 13：核心企业已审核 14：已生效 16：共同借款人审核 21 授信驳回）
                case "10":
                    mTipsLay.setVisibility(View.VISIBLE);
                    mTipsTv.setText(getResources().getString(R.string.no_edu_tip));
                    mBtnPlease.setEnabled(true);
                    mBtnPlease.setText(getResources().getString(R.string.edu_button));
                    break;
                case "11":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    mTipsLay.setVisibility(View.VISIBLE);
                    mTipsTv.setText(getResources().getString(R.string.shenhe_tip));
                    mBtnPlease.setEnabled(false);
                    break;
                case "12":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    mTipsLay.setVisibility(View.VISIBLE);
                    mTipsTv.setText(getResources().getString(R.string.shenhe_tip));
                    mBtnPlease.setEnabled(false);
                    break;
                case "13":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    mTipsLay.setVisibility(View.VISIBLE);
                    mTipsTv.setText(getResources().getString(R.string.shenhe_tip));
                    mBtnPlease.setEnabled(false);
                    break;
                case "14":
                    mTipsLay.setVisibility(View.GONE);
                    mTipsTv.setText(getResources().getString(R.string.shenhe_tg_tip));
                    if (mTotalMoney>0){
                        mTipsLay.setVisibility(View.GONE);
                        mBtnPlease.setText(getResources().getString(R.string.shenhe_tg_button));
                    }else {
                        mTipsLay.setVisibility(View.VISIBLE);
                        mTipsTv.setText(getResources().getString(R.string.shenhe_tip));
                        mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    }
                    mBtnPlease.setEnabled(true);
                    if (mLeftMoney > 0) {
                        mBtnPlease.setEnabled(true);
                    } else {
                        mBtnPlease.setEnabled(false);
                    }
                    break;
                case "16":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    mTipsLay.setVisibility(View.VISIBLE);
                    mTipsTv.setText(getResources().getString(R.string.shenhe_tip));
                    mBtnPlease.setEnabled(false);
                    break;
                case "21":
                    mTipsLay.setVisibility(View.VISIBLE);
                    mTipsTv.setText(getResources().getString(R.string.shouxin_bohui));
                    mBtnPlease.setEnabled(true);
                    break;
                default:
                    mTipsLay.setVisibility(View.VISIBLE);
                    mTipsTv.setText(getResources().getString(R.string.exception_info));
                    mBtnPlease.setEnabled(false);
                    break;
            }
        }else  if (isPerfectInfo.equals("0")){
            mTipsLay.setVisibility(View.VISIBLE);
            mTipsTv.setText(getResources().getString(R.string.no_edu_tip));
            mBtnPlease.setEnabled(true);
            mBtnPlease.setText(getResources().getString(R.string.edu_button));
        }else {
            mTipsLay.setVisibility(View.VISIBLE);
            mTipsTv.setText(getResources().getString(R.string.exception_info));
            mBtnPlease.setEnabled(false);
            mBtnPlease.setText(getResources().getString(R.string.edu_button));
        }
    }

    private void butPressAction() {
        String isPerfectInfo = mApplyConfigMap.get("iscmp") + "";
        String prestate = mApplyConfigMap.get("prestate") + "";
        //是否完善信息（1：已完善 0：未完善）
        if (isPerfectInfo.equals("1")) {
            switch (prestate) {//预授信申请状态（10： 未申请 11：已申请 12：担保已审核 13：核心企业已审核 14：已生效 16：共同借款人审核  21 授信驳回）
                case "21":
                case "10":
                    mBtnPlease.setText(getResources().getString(R.string.edu_button));
                    if (mZhengshuMap != null){
                        String ss = mZhengshuMap.get("state")+"";
                        if (ss.equals("0")){
                            Intent intent = new Intent(getActivity(),IdCardSuccessActivity.class);
                            intent.putExtra("verify_type",mZhengshuMap.get("verify_type")+"");
                            intent.putExtra(MbsConstans.FaceType.FACE_KEY,MbsConstans.FaceType.FACE_INSTALL);
                            startActivity(intent);
                        }else {
                            List<Map<String, Object>> list1 = (List<Map<String, Object>>) mApplyConfigMap.get("patnList");

                            if (list1 != null && list1.size() > 0) {
                                if (list1.size() == 1) {//当合作方是一个的情况下
                                    mHezuoMap = list1.get(0);
                                    String accid = mHezuoMap.get("accid") + "";
                                    String secstatus = mHezuoMap.get("secstatus")+"";
                                    if (UtilTools.empty(accid)) {
                                       /* mLoadingWindow.showView();
                                        erLeiHuList();*/

                                        String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
                                        if (kind.equals("1")) {
                                            Intent intent = new Intent(getActivity(), BankTiXianModifyActivity.class);
                                            intent.putExtra("DATA", (Serializable) mHezuoMap);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(getActivity(), ChongZhiCardAddActivity.class);
                                            intent.putExtra("backtype","100");
                                            startActivity(intent);
                                        }

                                    } else {
                                        /*if (secstatus.equals("2")){
                                            Intent intent = new Intent(getActivity(), BankQianyueActivity.class);
                                            intent.putExtra("DATA", (Serializable) mHezuoMap);
                                            startActivity(intent);
                                        }else {*/
                                            Intent intent = new Intent(getActivity(), ApplyAmountActivity.class);
                                            intent.putExtra("DATA", (Serializable) mHezuoMap);
                                            startActivity(intent);
                                       /* }*/

                                    }
                                } else if (list1.size() > 1) {
                                    mHeZuoFangDialog = new HeZuoFangDialog(getActivity(), true, list1, "选择合作方", 200);
                                    mHeZuoFangDialog.setSelectBackListener(this);
                                    mHeZuoFangDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                                    mBtnPlease.setEnabled(true);
                                    if (mTotalMoney == 0) {
                                        dealApplyConfig();
                                    }
                                }
                            } else {
                                //Intent intent = new Intent(getActivity(),ApplyAmountActivity.class);
                                //startActivity(intent);
                                TipsToast.showToastMsg("没有合作方");
                                mBtnPlease.setEnabled(true);
                            }
                        }
                    }else {
                        mLoadingWindow.showView();
                        isInstallCer();
                    }
                    break;
                case "11":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    break;
                case "12":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    break;
                case "13":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    break;
                case "14":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_tg_button));
                    if (mZhengshuMap != null){
                        String zhengshu = mZhengshuMap.get("state")+"";
                        if (zhengshu.equals("0")){
                            Intent intent = new Intent(getActivity(),IdCardSuccessActivity.class);
                            intent.putExtra("verify_type",mZhengshuMap.get("verify_type")+"");
                            intent.putExtra(MbsConstans.FaceType.FACE_KEY,MbsConstans.FaceType.FACE_INSTALL);
                            startActivity(intent);
                        }else {
                            /*mLoadingWindow.showView();
                            jiekuanCheck();*/
                            Intent intent = new Intent(getActivity(),BorrowMoneySelectActivity.class);
                            startActivity(intent);
                        }
                    }else {
                        mLoadingWindow.showView();
                        isInstallCer();
                    }
                    break;
                case "16":
                    mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
                    break;

                default:
                    mBtnPlease.setText(getResources().getString(R.string.edu_button));
                    TipsToast.showToastMsg(getResources().getString(R.string.exception_info));
                    break;
            }
        } else if (isPerfectInfo.equals("0")) {
            mBtnPlease.setText(getResources().getString(R.string.edu_button));
            mBtnPlease.setEnabled(true);
            showMsgDialog(getResources().getString(R.string.perfect_info_tip),true);

            /*Intent intent = new Intent(getActivity(), PerfectInfoActivity.class);
            startActivity(intent);*/
        }else {
            TipsToast.showToastMsg(getResources().getString(R.string.exception_info));
        }
    }

    @OnClick({R.id.back_img, R.id.btn_please, R.id.more_daiban_lay, R.id.item_waitdo, R.id.item_bohui,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                break;
            case R.id.btn_please:
                mBtnPlease.setEnabled(false);
                if (mMoneyMap == null) {
                    totleMoney();
                } else {
                    if (mTotalMoney > 0) {
                       // jiekuanCheck();
                        intent = new Intent(getActivity(),BorrowMoneySelectActivity.class);
                        startActivity(intent);
                        mBtnPlease.setEnabled(true);
                    } else {
                        /*if (mApplyConfigMap == null) {*/
                        mLoadingWindow.showView();
                        sqCheck();
                       /* } else {
                            butPressAction();
                        }*/
                    }
                }

//                intent = new Intent(getActivity(), PeopleCheckActivity.class);
//                intent = new Intent(getActivity(), PerfectInfoActivity.class);
//                intent = new Intent(getActivity(), BorrowMoneyActivity.class);
//                startActivity(intent);
                //    mHeZuoFangDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
//                mJieKuanDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;

            case R.id.more_daiban_lay:
                intent = new Intent(getActivity(), WaitDoWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.item_waitdo:
                break;

            case R.id.item_bohui:
                break;
            case R.id.left_back_lay:

                break;
        }
    }

    @Override
    public void showProgress() {
        //mLoadingWindow.showView();
    }

    @Override
    public void disimissProgress() {
       // mLoadingWindow.cancleView();

    }

    private void initHuiTuiButton(){
        if (mIsback){
            mTipsTv.setText(getResources().getString(R.string.shenhe_tip));
            mBtnPlease.setText(getResources().getString(R.string.shenhe_button));
            mBtnPlease.setEnabled(false);
        }
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        mLoadingWindow.cancleView();

        /*if (mRequestTagList != null && mRequestTagList.contains(mType)){
            mRequestTagList.remove(mType);
        }*/
        switch (mType) {
            case MethodUrl.isInstallCer://{verify_type=FACE, state=0}//安装证书信息
                mZhengshuMap = tData;
                break;
            case MethodUrl.erleiHuList://二类户列表
                List<Map<String, Object>> mList =   JSONUtil.getInstance().jsonToList(tData.get("result") + "");
                if (mList != null && mList.size() > 0) {
                    Intent intent = new Intent(getActivity(), SelectBankListActivity.class);
                    intent.putExtra("TYPE","2");
                    intent.putExtra("DATA", (Serializable) mList);
                    intent.putExtra("patncode",mHezuoMap.get("patncode")+"");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), BankOpenActivity.class);
                    intent.putExtra("DATA",(Serializable) mHezuoMap);
                    startActivity(intent);
                }
                break;
            case MethodUrl.totleMoney://{daiklilv=0, totalmoney=2695213200, leftmoney=2261923200}
                mMoneyMap = tData;
                circleView();
                break;
            case MethodUrl.reqCheck://{prestate=10, iscmp=1, patnList=[{patncode=NSHCSHZF, vaccid=1831216000013914, secstatus=2, accid=6235559020000001270, zifangnme=上海支行, zifangbho=NCBK6621}]}

                if (mApplyConfigMap == null){
                    mApplyConfigMap = tData;
                    dealApplyConfig();
                }else {
                    mApplyConfigMap = tData;
                    dealApplyConfig();
                    butPressAction();
                }

                break;
            case MethodUrl.jiekuanSxList:
                mBtnPlease.setEnabled(true);
                List<Map<String, Object>> jkSxList =(List<Map<String,Object>>) tData.get("creditList");
                if (jkSxList != null ){
                    if (jkSxList.size() == 1){
                        mJieKuanMap = jkSxList.get(0);
                        Intent intent = new Intent(getActivity(), BorrowMoneySelectActivity.class);
                        intent.putExtra("DATA", (Serializable) mJieKuanMap);
                        //intent.putExtra("MONEY", (Serializable) mMoneyMap);
                        startActivity(intent);
                        LogUtilDebug.i("打印log日志",mJieKuanMap);
                    }else if (jkSxList.size()>1){
                        mJieKuanDialog = new HeZuoFangDialog(getActivity(), true, jkSxList, "选择合作方", 210);
                        mJieKuanDialog.setSelectBackListener(this);
                        mJieKuanDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                    }else if (jkSxList.size() == 0){
                        TipsToast.showToastMsg(getResources().getString(R.string.no_borrow_config));
                    }
                }else {
                    TipsToast.showToastMsg(getResources().getString(R.string.exception_info));
                }


                break;
            case MethodUrl.workList://

                mDataList = new ArrayList<>();
                String result = tData.get("result") + "";
                if (UtilTools.empty(result)) {
                } else {
                    Map<String, Object> mm =   JSONUtil.getInstance().jsonMap(result);
                    //预授信回退列表
                    List<Map<String, Object>> list1 = (List<Map<String, Object>>) mm.get("prebackList");
                    if (list1 != null && list1.size() > 0) {
                        for (Map<String, Object> map : list1) {
                            map.put("type", "1");
                        }
                        mDataList.addAll(list1);
                         mIsback = true;
                    }else {
                        mIsback = false;
                    }

                    initHuiTuiButton();

                    //授信签署列表
                    List<Map<String, Object>> list2 = (List<Map<String, Object>>) mm.get("creditSignList");
                    if (list2 != null && list2.size() > 0) {
                        for (Map<String, Object> map : list2) {
                            map.put("type", "2");
                        }
                        mDataList.addAll(list2);

                    }

                    //借款进度列表
                    List<Map<String, Object>> list3 = (List<Map<String, Object>>) mm.get("loanPlanList");
                    if (list3 != null && list3.size() > 0) {
                        for (Map<String, Object> map : list3) {
                            map.put("type", "3");
                        }
                        mDataList.addAll(list3);
                    }

                    //待还款列表
                    List<Map<String, Object>> list4 = (List<Map<String, Object>>) mm.get("replayList");
                    if (list4 != null && list4.size() > 0) {
                        for (Map<String, Object> map : list4) {
                            map.put("type", "4");
                        }
                        mDataList.addAll(list4);
                    }
                    /*else {
                        Toast.makeText(getActivity(), "待还款列表为空", Toast.LENGTH_SHORT).show();
                    }*/
                    //共同借款人审核列表
                    List<Map<String, Object>> list5 = (List<Map<String, Object>>) mm.get("gtPreList");
                    if (list5 != null && list5.size() > 0) {
                        for (Map<String, Object> map : list5) {
                            map.put("type", "5");
                        }
                        mDataList.addAll(list5);
                    }
                }

                initDaibanView();

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                mLoadingWindow.showView();
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                for (String stag : mRequestTagList){
                    switch (stag) {
                        case MethodUrl.erleiHuList:
                            erLeiHuList();
                            break;
                        case MethodUrl.jiekuanSxList:
                            jiekuanCheck();
                            break;
                        case MethodUrl.totleMoney:
                            totleMoney();
                            break;
                        case MethodUrl.reqCheck:
                            sqCheck();
                            break;
                        case MethodUrl.workList:
                            doworkListAction();
                            break;
                        case MethodUrl.isInstallCer://{
                            isInstallCer();
                            break;
                    }
                    mRequestTagList = new ArrayList<>();
                }
                break;
        }

        initHuiTuiButton();
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mLoadingWindow.cancleView();

        switch (mType) {
            case MethodUrl.erleiHuList://二类户列表

                break;
            case MethodUrl.totleMoney://{daiklilv=0, totalmoney=2695213200, leftmoney=2261923200}
                mBtnPlease.setEnabled(true);
                refreshLayout.setRefreshCompleted();
                break;
            case MethodUrl.reqCheck://申请金额配置及合作方列表
                mBtnPlease.setEnabled(true);
                break;
            case MethodUrl.jiekuanSxList://借款授信列表
                mBtnPlease.setEnabled(true);
                break;
        }
      dealFailInfo(map,mType);
    }

    private void circleView() {
        refreshLayout.setRefreshCompleted();

        mEduValueTv.setText(UtilTools.getMoney(mMoneyMap.get("totalmoney") + ""));
        mLilvValueTv.setText(UtilTools.getlilv(mMoneyMap.get("daiklilv") + "") );

        if (UtilTools.empty(mMoneyMap.get("leftmoney"))) {
            mLeftMoney = 0;
        } else {
            mLeftMoney = Double.valueOf(mMoneyMap.get("leftmoney") + "");
        }

        if (UtilTools.empty(mMoneyMap.get("totalmoney"))) {
            mTotalMoney = 0;
        } else {
            mTotalMoney = Double.valueOf(mMoneyMap.get("totalmoney") + "");
        }

        if (mTotalMoney > 0) {
            double all = UtilTools.divide(mTotalMoney,100);
            mCircleProgress.setMaxValue(new BigDecimal(all).floatValue());

            mTipsLay.setVisibility(View.GONE);
            mBtnPlease.setText(getResources().getString(R.string.shenhe_tg_button));
            if (mLeftMoney > 0) {
                mBtnPlease.setEnabled(true);

            } else {
                mBtnPlease.setEnabled(false);
            }
        } else {
            sqCheck();
        }

        // int dd = new Double(totlalMoney/leftMoney).intValue();
        double dd = mTotalMoney / mLeftMoney;
        double i = 500 / dd;
        mMyView.setCurrentNumAnim(new Double(i).intValue());
        // mMyView.setCurrentNumAnim(0);
        mMyView.setMoneyStr(UtilTools.fromDouble(mLeftMoney));


        double le = UtilTools.divide(mLeftMoney,100);
        BigDecimal bg = new BigDecimal(le);
        mCircleProgress.setValue(bg.floatValue());


        //mMyView.setCurrentNumAnim(150);
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        Intent intent;
        switch (type) {
            case 200:
                mHezuoMap = map;
                String accid = mHezuoMap.get("accid") + "";
                String secstatus = mHezuoMap.get("secstatus")+"";

                if (UtilTools.empty(accid)) {

                    String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
                    if (kind.equals("1")) {
                        intent = new Intent(getActivity(), BankTiXianModifyActivity.class);
                        intent.putExtra("DATA", (Serializable) mHezuoMap);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getActivity(), ChongZhiCardAddActivity.class);
                        intent.putExtra("backtype","100");
                        startActivity(intent);
                    }
                    //erLeiHuList();
                } else {
                    /*if (secstatus.equals("2")){
                        intent = new Intent(getActivity(), BankQianyueActivity.class);
                        intent.putExtra("DATA", (Serializable) mHezuoMap);
                        startActivity(intent);
                    }else {*/
                        intent = new Intent(getActivity(), ApplyAmountActivity.class);
                        intent.putExtra("DATA", (Serializable) mHezuoMap);
                        startActivity(intent);
//                    }

                }
                break;
            case 210:
                mJieKuanMap = map;
                intent = new Intent(getActivity(), BorrowMoneyActivity.class);
                intent.putExtra("DATA", (Serializable) mJieKuanMap);
                startActivity(intent);
                LogUtilDebug.i("打印log日志",mJieKuanMap);
                break;
        }
    }

    private List<Map<String, Object>> mDataList;

    private void doworkListAction() {
        mRequestPresenterImp = new RequestPresenterImp(this, getActivity());
        mRequestTag = MethodUrl.workList;
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        Map map = new HashMap();
        mRequestPresenterImp.requestGetToRes(mHeaderMap,MethodUrl.workList,map);
    }


    private void initDaibanView() {
        if (mDataList != null && mDataList.size() > 0) {
            if (mDataList.size() == 1) {
                mMoreDaibanLay.setVisibility(View.GONE);
                final Map<String, Object> map = mDataList.get(0);
                String type = map.get("type") + "";

                String tvStatus = "";
                //根据列表类型进行不同的处理操作
                // 1 预授信回退列表  2授信签署列表 3借款进度列表 4待还款列表  5共同借款人审核列表
                switch (type) {
                    case "1":
                        tvStatus = "去修订";
                        mTvBankBohui.setText(map.get("zifangnme") + "");
                        mItemBohui.setVisibility(View.VISIBLE);
                        mItemWaitdo.setVisibility(View.GONE);
                        mTvDateBohui.setText(UtilTools.getStringFromSting2(map.get("flowdate") + "", "yyyyMMdd", "yyyy-MM-dd"));
                        mTvMoneyBohui.setText(UtilTools.getRMBMoney(map.get("creditmoney") + ""));
                        mItemBohui.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(getActivity(),ApplyAmountActivity.class);
                                intent.putExtra("TYPE",1);
                                intent.putExtra("precreid",map.get("precreid")+"");
                                startActivity(intent);
                            }
                        });
                        break;
                    case "2"://1 预授信回退列表  2授信签署列表 3借款进度列表 4待还款列表  5共同借款人审核列表

                        final  String status = map.get("qsstate")+"";
                        if (status.equals("0")){
                            tvStatus = "去签署";
                        }else if (status.equals("1")){
                            tvStatus = "处理中";
                        }
                        mItemBohui.setVisibility(View.GONE);
                        mItemWaitdo.setVisibility(View.VISIBLE);
                        mTvMoney.setText(UtilTools.getRMBMoney(map.get("creditmoney") + ""));
                        mTvBank.setText(map.get("zifangnme") + "");
                        mItemWaitdo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(getActivity(),SignLoanActivity.class);
                                intent.putExtra("DATA",(Serializable)map);
                                intent.putExtra("status",status);
                                startActivity(intent);
                            }
                        });

                        break;
                    case "3"://1 预授信回退列表  2授信签署列表 3借款进度列表 4待还款列表  5共同借款人审核列表
                        tvStatus = "放款审核中";
                        mItemBohui.setVisibility(View.GONE);
                        mItemWaitdo.setVisibility(View.VISIBLE);
                        mTvMoney.setText(UtilTools.getRMBMoney(map.get("reqmoney") + ""));
                        mTvBank.setText(map.get("loanstepdesc") + "");
                        mTvBank.setVisibility(View.GONE);
                        mItemWaitdo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(getActivity(),BorrowDetailActivity.class);
                                intent.putExtra("DATA",(Serializable) map);
                                startActivity(intent);
                            }
                        });
                        break;
                    case "4"://1 预授信回退列表  2授信签署列表 3借款进度列表 4待还款列表  5共同借款人审核列表
                        tvStatus = "去还款";
                        mItemBohui.setVisibility(View.GONE);
                        mItemWaitdo.setVisibility(View.VISIBLE);
                        mTvMoney.setText(UtilTools.getRMBMoney(map.get("repayamt") + ""));
                        String dateStr = map.get("repaydate")+"";

                        dateStr ="截止还款日"+UtilTools.getStringFromSting2(dateStr,"yyyyMMdd","yyyy年MM月dd日");
                        mTvBank.setText(dateStr);
                        mItemWaitdo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(getActivity(), PayMoneyActivity.class);
                                intent.putExtra("DATA",(Serializable) map);
                                startActivity(intent);
                            }
                        });
                        break;
                    case "5"://1 预授信回退列表  2授信签署列表 3借款进度列表 4待还款列表  5共同借款人审核列表
                        tvStatus = "待审核";
                        mItemBohui.setVisibility(View.GONE);
                        mItemWaitdo.setVisibility(View.VISIBLE);
                        mTvMoney.setText(UtilTools.getRMBMoney(map.get("creditmoney") + ""));
                        mTvBank.setText(map.get("firmname") + "");
                        mItemWaitdo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(getActivity(),PeopleCheckActivity.class);
                                intent.putExtra("DATA",(Serializable) map);
                                startActivity(intent);
                            }
                        });
                        break;
                }
                mStatusTv.setText(tvStatus);
            } else {
                mMoreDaibanLay.setVisibility(View.VISIBLE);
                mItemBohui.setVisibility(View.GONE);
                mItemWaitdo.setVisibility(View.GONE);
                String ss = "您有" + mDataList.size() + "个待办事项";
                SpannableString spannableString = mParseTextUtil.parseValueColor(ss, R.color.black);
                mMoreDaibanTv.setText(spannableString);
            }
        } else {
            mMoreDaibanLay.setVisibility(View.GONE);
            mItemBohui.setVisibility(View.GONE);
            mItemWaitdo.setVisibility(View.GONE);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }




    private TipMsgDialog mZhangDialog;
    private void showMsgDialog(Object msg,boolean isClose){
        mZhangDialog = new TipMsgDialog(getActivity(),true);
        mZhangDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                    dialog.dismiss();
                    if (isClose){
                        //finish();
                    }
                    return true;
                }
                else {
                    return false;
                }
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cancel:
                        Intent intent = new Intent(getActivity(), PerfectInfoActivity.class);
                        startActivity(intent);
                        mZhangDialog.dismiss();
                        if (isClose){
                            //finish();
                        }
                        break;
                    case R.id.confirm:
                        mZhangDialog.dismiss();
                        break;
                    case R.id.tv_right:
                        mZhangDialog.dismiss();
                        if (isClose){
                            //finish();
                        }
                        break;
                }
            }
        };
        mZhangDialog.setCanceledOnTouchOutside(false);
        mZhangDialog.setCancelable(true);
        mZhangDialog.setOnClickListener(onClickListener);
        mZhangDialog.initValue("温馨提示",msg);
        mZhangDialog.show();
        mZhangDialog.tv_right.setVisibility(View.VISIBLE);
        mZhangDialog.tv_cancel.setText("去完善资料");
        mZhangDialog.tv_cancel.setTextColor(ContextCompat.getColor(getActivity(),R.color.black));
    }


}
