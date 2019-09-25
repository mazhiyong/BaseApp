package com.lr.biyou.ui.moudle1.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.db.CustomViewsInfo;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.ContactKefuActivity;
import com.lr.biyou.ui.moudle.activity.IdCardEditActivity;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.ui.moudle.activity.SecurityActivity;
import com.lr.biyou.ui.moudle.activity.SettingActivity;
import com.lr.biyou.ui.moudle1.activity.HelpListActivity;
import com.lr.biyou.ui.moudle1.activity.NoticeDetialActivity;
import com.lr.biyou.ui.moudle1.activity.NoticeListActivity;
import com.lr.biyou.ui.moudle1.activity.PayListActivity;
import com.lr.biyou.ui.moudle1.activity.UserInfoActivity;
import com.lr.biyou.ui.moudle1.activity.YaoQingActivity;
import com.lr.biyou.ui.moudle1.adapter.MainCoinAdapter;
import com.lr.biyou.ui.moudle1.adapter.MoreTypeAdapter;
import com.lr.biyou.ui.moudle2.activity.RedRecordListActivity;
import com.lr.biyou.ui.moudle3.activity.CoinInfoDetailActivity;
import com.lr.biyou.ui.moudle5.activity.ChoseBiTypeActivity;
import com.lr.biyou.ui.moudle5.activity.HuaZhuanActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.stx.xhb.xbanner.XBanner;
import com.sunfusheng.marqueeview.MarqueeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BasicFragment implements RequestView, ReLoadingData {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.back_text)
    TextView backText;
    @BindView(R.id.left_back_lay)
    LinearLayout leftBackLay;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_text_tv)
    TextView rightTextTv;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.top_layout)
    LinearLayout titleBarView;
    @BindView(R.id.clTopView)
    View clTopView;
    @BindView(R.id.xBanner)
    XBanner xBanner;
    @BindView(R.id.ivHomeNews)
    ImageView ivHomeNews;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.tvMore)
    TextView tvMore;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.tvCoinPrice)
    TextView tvCoinPrice;
    @BindView(R.id.tvCoinRatio)
    TextView tvCoinRatio;
    @BindView(R.id.tvCoinConvert)
    TextView tvCoinConvert;
    @BindView(R.id.vpQuotesInfo)
    ViewPager vpQuotesInfo;
    @BindView(R.id.tlRank)
    TabLayout tlRank;
    @BindView(R.id.textView56)
    TextView textView56;
    @BindView(R.id.textView57)
    TextView textView57;
    @BindView(R.id.textView58)
    TextView textView58;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvArea)
    TextView tv24HVolume;
    @BindView(R.id.tvCurrentPrice)
    TextView tvCurrentPrice;
    @BindView(R.id.tvCurrentPriceCny)
    TextView tvCurrentPriceCny;
    @BindView(R.id.tvRiseFallRatio)
    TextView tvRiseFallRatio;
    @BindView(R.id.clRise)
    ConstraintLayout clRise;
    @BindView(R.id.guideline10)
    Guideline guideline10;
    @BindView(R.id.guideline11)
    Guideline guideline11;
    //    @BindView(R.id.llRankPlace)
//    LinearLayout llRankPlace;
    @BindView(R.id.nsvAllView)
    NestedScrollView nsvAllView;
    @BindView(R.id.tv_loading_content)
    TextView tvLoadingContent;
    @BindView(R.id.ivError)
    ImageView ivError;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.fast_buy_lay)
    LinearLayout fastLay;
    @BindView(R.id.to_bb_lay)
    LinearLayout bbLay;
    @BindView(R.id.to_hy_lay)
    LinearLayout hyLay;


    private String mRequestTag = "";

    private MainCoinAdapter mainCoinAdapter;
    private MoreTypeAdapter moreTypeAdapter;

    private List<CustomViewsInfo> localImageInfos = new ArrayList<>();
    private List<Map<String, Object>> listUp = new ArrayList<>();
    private List<Map<String, Object>> noticeList;

    private AnimUtil mAnimUtil;
    private String biType = "1";

    private int mPage = 1;
    private Handler handler = new Handler();

    //HTTP请求  轮询
    private Runnable cnyRunnable = new Runnable() {
        @Override
        public void run() {
            //获取实时虚拟货币数据
            getBiInfoAction();
            handler.postDelayed(this, MbsConstans.SECOND_TIME_5000);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(getActivity()));
        titleBarView.setLayoutParams(layoutParams);
        titleBarView.setPadding(0, UtilTools.getStatusHeight2(getActivity()), 0, 0);
        setBarTextColor();

        mAnimUtil = new AnimUtil();

        backImg.setVisibility(View.VISIBLE);
        backImg.setImageResource(R.drawable.icon1_touxiang);
        backText.setVisibility(View.GONE);

        titleText.setText("MFEX Global");
//        Resources resources = getActivity().getResources();
//        Drawable drawable = resources.getDrawable(R.drawable.icon0_logo);
//        titleText.setCompoundDrawables(drawable,null,null,null);


        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        mPageView.showEmpty();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rvList.setLayoutManager(manager);
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);

        moreTypeAdapter = new MoreTypeAdapter(getActivity());
        rvList.setAdapter(moreTypeAdapter);

        mainCoinAdapter = new MainCoinAdapter(getActivity(), listUp);
        vpQuotesInfo.setAdapter(mainCoinAdapter);


        List<Map<String, Object>> maps = SelectDataUtil.getTabValues();
        for (Map<String, Object> map : maps) {
            tlRank.addTab(tlRank.newTab().setText(map.get("name") + ""));
        }
        tlRank.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        biType = "1";
                        getBiInfoAction();
                        break;
                    case 1:
                        biType = "USDT";
                        getBiInfoAction();
                        break;
                    case 2:
                        biType = "BTC";
                        getBiInfoAction();
                        break;
                    case 3:
                        biType = "ETH";
                        getBiInfoAction();
                        break;
                    case 4:
                        biType = "2";
                        getBiInfoAction();
                        break;

                }
//                if (rankList != null && rankList.size() > 0) {
//                    if (tab.getPosition() == 0) {
//                        Collections.sort(rankList, new RatioRankObj());
//                    } else {
//                        Collections.sort(rankList, new TradeVolumeRank());
//                    }
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Intent intent = new Intent(getActivity(), NoticeDetialActivity.class);
                intent.putExtra("DATA", (Serializable) noticeList.get(position));
                startActivity(intent);
            }
        });

        //获取轮播页数据
        getBannerInfoAction();
        //获取实时虚拟货币数据
        //getBiInfoAction();
        //handler.post(cnyRunnable);
    }

    private void getBannerInfoAction() {
        mRequestTag = MethodUrl.BANNNER_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.BANNNER_INFO, map);
    }

    private void getBiInfoAction() {
        mRequestTag = MethodUrl.LIST_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("type", biType);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.LIST_INFO, map);
    }


    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent = null;
        switch (mType) {
            case MethodUrl.BANNNER_INFO:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        Map<String, Object> mapData = (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)) {
                            List<String> bannerList = (List<String>) mapData.get("banner");
                            if (!UtilTools.empty(bannerList)) {
                                localImageInfos.clear();
                                for (String str : bannerList) {
                                    CustomViewsInfo customViewsInfo = new CustomViewsInfo(str);
                                    localImageInfos.add(customViewsInfo);
                                }
                                xBanner.setBannerData(localImageInfos);
                                xBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(XBanner banner, Object model, View view, int position) {
//                              bundle.clear();
//                              bundle.putString("url", "https://www.baidu.com/");
//                              startActivity(HomeFragment.this, bundle, NewsDetailActivity.class);
                                    }
                                });

                                xBanner.loadImage(new XBanner.XBannerAdapter() {
                                    @Override
                                    public void loadBanner(XBanner banner, Object model, View view, int position) {
                                        GlideUtils.loadRoundCircleImage(getActivity(), ((CustomViewsInfo) model).getXBannerUrl(), (ImageView) view);
                                    }
                                });
                            }
                        }

                        noticeList = (List<Map<String, Object>>) mapData.get("notice");
                        if (!UtilTools.empty(noticeList)) {
                            List<String> list1 = new ArrayList<>();
                            for (Map<String, Object> map : noticeList) {
                                list1.add(map.get("title") + "");
                            }
                            marqueeView.startWithList(list1);
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        if (getActivity() != null){
                            getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;

            case MethodUrl.LIST_INFO:
                switch (tData.get("code") + "") {
                    case "0":
                        Map<String, Object> mapData = (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)) {
                            List<Map<String, Object>> rankList = new ArrayList<>();
                            Map<String, Object> mapMarket = null;
                            switch (biType) {
                                case "1":
                                    if (!UtilTools.empty(mapData.get("market_tickers")+"")){
                                        rankList = (List<Map<String, Object>>) mapData.get("market_tickers");
                                    }
                                    break;
                                case "USDT":
                                    if (!UtilTools.empty(mapData.get("market_tickers")+"")){
                                        rankList = (List<Map<String, Object>>) mapData.get("market_tickers");
                                    }
                                    break;
                                case "BTC":
                                    if (!UtilTools.empty(mapData.get("market_tickers")+"")){
                                        rankList = (List<Map<String, Object>>) mapData.get("market_tickers");
                                    }

                                    break;
                                case "ETH":
                                    if (!UtilTools.empty(mapData.get("market_tickers")+"")){
                                        rankList = (List<Map<String, Object>>) mapData.get("market_tickers");
                                    }
                                    break;
                                case "2":
                                    if (!UtilTools.empty(mapData.get("market_tickers")+"")){
                                        rankList = (List<Map<String, Object>>) mapData.get("market_tickers");
                                    }
                                    break;
                            }

                            //设置数据
                            if (!UtilTools.empty(rankList) && rankList.size() > 0) {
                                mPageView.showContent();
                                moreTypeAdapter.setRankList(rankList);
                                List<Map<String, Object>> finalRankList = rankList;
                                moreTypeAdapter.setOnItemClickListener(new MoreTypeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClickListener(View view, int position) {
                                        Intent intent1 = new Intent(getActivity(), CoinInfoDetailActivity.class);
                                        intent1.putExtra("symbol", finalRankList.get(position).get("name")+"");
                                        intent1.putExtra("area", finalRankList.get(position).get("area")+"");
                                        intent1.putExtra("from","1");
                                        startActivity(intent1);
                                    }
                                });

                            } else {
                                mPageView.showEmpty();
                            }
                        } else {
                            mPageView.showEmpty();
                        }


                        if (!UtilTools.empty(mapData) && !UtilTools.empty(mapData.get("trade_block")+"")) {
                            listUp = (List<Map<String, Object>>) mapData.get("trade_block");
                            if (!UtilTools.empty(listUp)) {
                                mainCoinAdapter.setData(listUp, vpQuotesInfo.getCurrentItem());
                                mainCoinAdapter.setItemClickListener(new MainCoinAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClickListener(Map<String, Object> listUpBean) {

                                        Intent intent1 = new Intent(getActivity(), CoinInfoDetailActivity.class);
                                        intent1.putExtra("symbol", listUpBean.get("name")+"");
                                        intent1.putExtra("area", listUpBean.get("area")+"");
                                        intent1.putExtra("from","1");
                                        startActivity(intent1);
                                    }
                                });
                            }

                        }

                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        mPageView.showNetworkError();
                        break;
                    case "1":
                        if (getActivity() != null){
                            getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        mPageView.showEmpty();
                        break;
                }

                break;
            case  MethodUrl.IS_IDENTITY:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        Map<String,Object> mapData = (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)){
                            intent = new Intent(getActivity(), IdCardEditActivity.class);
                            if ((mapData.get("is_identity")+"").equals("1")){
                                intent.putExtra("TYPE","1");
                            }else {
                                intent.putExtra("TYPE","0");
                            }
                            startActivity(intent);
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        if (getActivity() != null){
                            getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;

        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }

    @OnClick({R.id.left_back_lay, R.id.tvMore, R.id.to_hy_lay,R.id.to_bb_lay,R.id.fast_buy_lay})
    public void onViewClicked(View view) {
        Intent intent;
        MainActivity activity = (MainActivity) getActivity();
        switch (view.getId()) {
            case R.id.left_back_lay: //个人中心
                initPopupWindow();
                break;
            case R.id.tvMore: //更多
                intent = new Intent(getActivity(), NoticeListActivity.class);
                startActivity(intent);
                break;
            case R.id.to_hy_lay: //合约交易
                if (activity != null){
                    activity.toHeYueFragment();
                }
                break;
            case R.id.to_bb_lay: //币币交易
                if (activity != null){
                    activity.toBBFragment();
                }
                break;
            case R.id.fast_buy_lay: //快捷购买
                if (activity != null){
                    activity.toFBFragment();
                }
                break;
        }
    }


    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    private TextView tvPhone;
    private TextView tvUID;
    private LinearLayout layInfo;
    private LinearLayout layChongbi;
    private LinearLayout layTibi;
    private LinearLayout layHuazhuan;
    private LinearLayout layHongbao;
    private LinearLayout layShiming;
    private LinearLayout layYaoqing;
    private LinearLayout layShoukuan;
    private LinearLayout layAnquan;
    private LinearLayout layKefu;
    private LinearLayout layBangzhu;
    private LinearLayout layShezhi;


    private void initPopupWindow() {

        if (mConditionDialog == null) {
            popView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_person_message, null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            initConditionDialog(popView);
            mConditionDialog.setClippingEnabled(false);

            int screenWidth = UtilTools.getScreenWidth(getActivity());
            int screenHeight = UtilTools.getScreenHeight(getActivity());
            mConditionDialog.setWidth((int) (screenWidth * 0.8));
            mConditionDialog.setHeight(WindowManager.LayoutParams.MATCH_PARENT);

            //设置background后在外点击才会消失
            // mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(getActivity(),5)));
            //mConditionDialog.setOutsideTouchable(true);// 设置可允许在外点击消失
            //自定义动画
            mConditionDialog.setAnimationStyle(R.style.PopupAnimation);
            //mConditionDialog.setAnimationStyle(android.R.style.Animation_Activity);//使用系统动画
            mConditionDialog.update();
            mConditionDialog.setTouchable(true);
            mConditionDialog.setFocusable(true);
            //popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
            //			mConditionDialog.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
            mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),
                    Gravity.TOP | Gravity.LEFT, 0, 0);
            toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggleBright();
                }
            });
        } else {
            mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),
                    Gravity.TOP | Gravity.LEFT, 0, 0);
            toggleBright();
        }
    }


    private void toggleBright() {
        //三个参数分别为： 起始值 结束值 时长 那么整个动画回调过来的值就是从0.5f--1f的
        mAnimUtil.setValueAnimator(0.7f, 1f, 300);
        mAnimUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                //此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                float bgAlpha = bright ? progress : (1.7f - progress);//三目运算，应该挺好懂的。
                //bgAlpha = progress;//三目运算，应该挺好懂的。
                bgAlpha(bgAlpha);//在此处改变背景，这样就不用通过Handler去刷新了。
            }
        });
        mAnimUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                //在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        mAnimUtil.startAnimator();
    }

    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) getActivity()).getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private void initConditionDialog(View view) {

        tvPhone = view.findViewById(R.id.tv_phone);
        tvUID = view.findViewById(R.id.tv_uid);

        layInfo = view.findViewById(R.id.lay_info);
        layChongbi = view.findViewById(R.id.lay_chongbi);
        layTibi = view.findViewById(R.id.lay_tibi);
        layHuazhuan = view.findViewById(R.id.lay_huazhuan);
        layHongbao = view.findViewById(R.id.lay_hongbao);
        layShiming = view.findViewById(R.id.lay_shiming);
        layYaoqing = view.findViewById(R.id.lay_yaoqing);
        layShoukuan = view.findViewById(R.id.lay_shoukuan);
        layAnquan = view.findViewById(R.id.lay_anquan);
        layKefu = view.findViewById(R.id.lay_kefu);
        layBangzhu = view.findViewById(R.id.lay_bangzhu);
        layShezhi = view.findViewById(R.id.lay_shezhi);

        if (UtilTools.empty(MbsConstans.USER_MAP)) {
            String s = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.LOGIN_INFO, "").toString();
            MbsConstans.USER_MAP = JSONUtil.getInstance().jsonMap(s);

        }

        tvPhone.setText(MbsConstans.USER_MAP.get("account") + "");
        tvUID.setText("UID: "+MbsConstans.USER_MAP.get("id") );



        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.lay_info:
                        intent = new Intent(getActivity(), UserInfoActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.lay_chongbi:
                        intent = new Intent(getActivity(), ChoseBiTypeActivity.class);
                        intent.putExtra("TYPE","1");
                        startActivity(intent);
                        break;
                    case R.id.lay_tibi:
                        intent = new Intent(getActivity(), ChoseBiTypeActivity.class);
                        intent.putExtra("TYPE","2");
                        startActivity(intent);
                        break;
                    case R.id.lay_huazhuan:
                        intent = new Intent(getActivity(), HuaZhuanActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.lay_hongbao:
                        intent = new Intent(getActivity(), RedRecordListActivity.class);
                        intent.putExtra("type","1");
                        startActivity(intent);
                        break;
                    case R.id.lay_shiming:
                        //是否实名认证
                        getIsIdentityAction();

                        break;
                    case R.id.lay_yaoqing:
                        intent = new Intent(getActivity(), YaoQingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.lay_shoukuan:
                        intent = new Intent(getActivity(), PayListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.lay_anquan:
                        intent = new Intent(getActivity(), SecurityActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.lay_kefu:
                        intent = new Intent(getActivity(), ContactKefuActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.lay_bangzhu:
                        intent = new Intent(getActivity(), HelpListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.lay_shezhi:
                        intent = new Intent(getActivity(), SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        layInfo.setOnClickListener(onClickListener);
        layChongbi.setOnClickListener(onClickListener);
        layTibi.setOnClickListener(onClickListener);
        layHuazhuan.setOnClickListener(onClickListener);
        layHongbao.setOnClickListener(onClickListener);
        layShiming.setOnClickListener(onClickListener);
        layYaoqing.setOnClickListener(onClickListener);
        layShoukuan.setOnClickListener(onClickListener);
        layAnquan.setOnClickListener(onClickListener);
        layKefu.setOnClickListener(onClickListener);
        layBangzhu.setOnClickListener(onClickListener);
        layShezhi.setOnClickListener(onClickListener);
    }

    private void getIsIdentityAction() {
        mRequestTag = MethodUrl.IS_IDENTITY;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.IS_IDENTITY, map);

    }

    @Override
    public void reLoadingData() {
        getBiInfoAction();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (handler != null && cnyRunnable != null){
            handler.removeCallbacks(cnyRunnable);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            if (handler != null && cnyRunnable != null) {
                handler.post(cnyRunnable);
                LogUtilDebug.i("show", "&&&&&&&&HomeFragment visible");
            }else {
                LogUtilDebug.i("show", "&&&&&&&&HomeFragment gone");
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            if (handler != null && cnyRunnable != null){
                handler.removeCallbacks(cnyRunnable);
            }
            setUserVisibleHint(false);
            LogUtilDebug.i("show","onHiddenChanged()*******HomeFragment不可见");
        }else {
            if (handler != null && cnyRunnable != null){
                handler.post(cnyRunnable);
            }
            setUserVisibleHint(true);
            LogUtilDebug.i("show", "onHiddenChanged()*******HomeFragment可见");
        }
    }
}
