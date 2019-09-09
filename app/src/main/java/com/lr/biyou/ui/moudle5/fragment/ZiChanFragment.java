package com.lr.biyou.ui.moudle5.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidkun.xtablayout.XTabLayout;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.pulltozoomview.PullToZoomScrollViewEx;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle5.activity.ChoseBiTypeActivity;
import com.lr.biyou.ui.moudle5.activity.HuaZhuanActivity;
import com.lr.biyou.ui.moudle5.activity.TiBiActivity;
import com.lr.biyou.ui.moudle5.adapter.ZiChanListAdapter;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class ZiChanFragment extends BasicFragment implements ReLoadingData, RequestView {

//    View headView;
//    View zoomView;
//    View contentView;

    @BindView(R.id.person_scroll_view)
    PullToZoomScrollViewEx mPersonScrollView;


    @BindView(R.id.back_view)
    ImageView mBackView;
    @BindView(R.id.titleText)
    TextView mTitleText;
    @BindView(R.id.personal_more_button)
    ImageView mPersonalMoreButton;

    @BindView(R.id.index_top_layout_person)
    LinearLayout mIndexTopLayoutPerson;
    Unbinder unbinder;
    @BindView(R.id.bank_money_tv)
    TextView bankMoneyTv;
    @BindView(R.id.bank_card_tv)
    TextView bankCardTv;
    @BindView(R.id.tab_layout)
    XTabLayout tabLayout;
    @BindView(R.id.money_tv)
    TextView moneyTv;
    @BindView(R.id.money_tv2)
    TextView moneyTv2;
    @BindView(R.id.refresh_list_view)
    LRecyclerView refreshListView;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.page_view)
    PageView pageView;
    @BindView(R.id.chongbi_tv)
    TextView chongbiTv;
    @BindView(R.id.tibi_tv)
    TextView tibiTv;
    @BindView(R.id.huazhuan_tv)
    TextView huazhuanTv;

    private LoadingWindow mLoadingWindow;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter1;
    private LRecyclerViewAdapter mLRecyclerViewAdapter2;
    private LRecyclerViewAdapter mLRecyclerViewAdapter3;
    private ZiChanListAdapter mAdapter;
    private ZiChanListAdapter mAdapter1;
    private ZiChanListAdapter mAdapter2;
    private ZiChanListAdapter mAdapter3;

    private ImageView mZoomImage;
    private CircleImageView mRoundImageView;
    private TextView mUserName;
    private TextView mBankMoneyTv;
    private TextView mBankCardTv;
    private RelativeLayout mShowYueLay;

    private ToggleButton mToggleButton;
    private TextView mShowMoneyTv;


    private String mRequestTag = "";


    private Map<String, Object> mBankInfo;
    private Map<String, Object> mMoneyInfo;
    private Map<String, Object> mShareMap;

    private List<Map<String, Object>> mDataList = new ArrayList<>();

    public ZiChanFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    public void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE);
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
        mLoadingWindow = new LoadingWindow(getActivity(), R.style.Dialog);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(getActivity()));
        mIndexTopLayoutPerson.setLayoutParams(layoutParams);
        mIndexTopLayoutPerson.setPadding(0, UtilTools.getStatusHeight2(getActivity()), 0, 0);
        mTitleText.setVisibility(View.GONE);


        //StatusBarUtil.setColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.transparent), 0);
//        headView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_head_view, null, false);
//        zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_zoom_view, null, false);
//        contentView = LayoutInflater.from(getActivity()).inflate(R.layout.zhanghu_money, null, false);
//        mZoomImage = (ImageView) zoomView.findViewById(R.id.iv_zoom);
//        //  Glide.with(this).load(R.drawable.per_bg).into(mZoomImage);
//        //Glide.with(this).load(R.drawable.per_bg).into(mZoomImage);
//
//        mPersonScrollView.setHeaderView(headView);
//        mPersonScrollView.setZoomView(zoomView);
//        mPersonScrollView.setScrollContentView(contentView);


        tabLayout.addTab(tabLayout.newTab().setText("币币账户"));
        tabLayout.addTab(tabLayout.newTab().setText("合约账户"));
        tabLayout.addTab(tabLayout.newTab().setText("法币账户"));
        tabLayout.addTab(tabLayout.newTab().setText("奖励金"));
        tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                //mViewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0: //币币账户
                        initData();

                        break;
                    case 1: //合约账户
                        initData1();
                        break;
                    case 2: //法币账户
                        initData2();
                        break;
                    case 3: //奖励金
                        initData3();
                        break;
                }


            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

        pageView.setContentView(content);
        pageView.showLoading();
        pageView.setReLoadingData(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        refreshListView.setLayoutManager(linearLayoutManager);

        refreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // getShouMoneyInfoList();
                refreshListView.refreshComplete(1, 10);
            }
        });

//
//        if (MbsConstans.USER_MAP == null) {
//            getFriendInfoAction();
//        } else {
//            String kind = MbsConstans.USER_MAP.get("firm_kind") + "";
////            if (kind.equals("1")) {//客户类型（0：车主，1：物流公司）
////                mUserName.setText(MbsConstans.USER_MAP.get("comname") + "");
////            } else {
////                mUserName.setText(MbsConstans.USER_MAP.get("name") + "");
////            }
//            initHeadPic();
//        }
        // getCardInfoAction();
        setBarTextColor();

        getShareData();

        initData();
    }

    public void setBarTextColor() {
        StatusBarUtil.setDarkMode(getActivity());
    }


    @OnClick({R.id.chongbi_tv, R.id.tibi_tv, R.id.huazhuan_tv})
    public void onViewClicked(View view) {
        Intent intent ;
        switch (view.getId()) {
            case R.id.chongbi_tv: //充币
                intent = new Intent(getActivity(), ChoseBiTypeActivity.class);
                startActivity(intent);
                break;
            case R.id.tibi_tv:   //提币
                intent = new Intent(getActivity(), TiBiActivity.class);
                startActivity(intent);
                break;
            case R.id.huazhuan_tv://划转
                intent = new Intent(getActivity(), HuaZhuanActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initData() {
        mDataList.clear();
        for (int i = 0; i < 9; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "USDT");
            map.put("money1", "100000");
            map.put("money2", "909010");
            map.put("money3", "608109291");
            mDataList.add(map);
        }

        responseData();
    }


    private void initData1() {
        mDataList.clear();
        for (int i = 0; i < 9; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "ADG");
            map.put("money1", "9000");
            map.put("money2", "9010");
            map.put("money3", "808109291");
            mDataList.add(map);
        }

        responseData1();
    }

    private void initData2() {
        mDataList.clear();
        for (int i = 0; i < 9; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "UDT");
            map.put("money1", "900000");
            map.put("money2", "709010");
            map.put("money3", "108109291");
            mDataList.add(map);
        }

        responseData2();
    }

    private void initData3() {

        mDataList.clear();
        for (int i = 0; i < 9; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "SDT");
            map.put("money1", "88000");
            map.put("money2", "");
            map.put("money3", "109291");
            mDataList.add(map);
        }

        responseData3();
    }


    /**
     * 获取用户信息
     */
    private void getUserInfoAction() {
        mRequestTag = MethodUrl.USER_INFO;
        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_INFO, map);
    }

    /**
     * 获取分享内容
     */
    public void getShareData() {
        mRequestTag = MethodUrl.shareUrl;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.shareUrl, map);
    }

    /**
     * 获取默认银行卡信息
     */
    private void getCardInfoAction() {
        mRequestTag = MethodUrl.bankCard;
        Map<String, String> map = new HashMap<>();
        map.put("isdefault", "1");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.bankCard, map);
    }

    /**
     * 查询余额
     */
    private void getCardMoney() {
        mRequestTag = MethodUrl.allZichan;
        Map<String, String> map = new HashMap<>();
        map.put("qry_type", "acct");
        map.put("accid", "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.allZichan, map);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE)) {
                getUserInfoAction();
            } else if (action.equals(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE)) {
            }
        }
    };

    private void initHeadPic() {
        String headUrl = MbsConstans.USER_MAP.get("head_pic") + "";
        if (headUrl.contains("http")) {
        } else {
            headUrl = MbsConstans.PIC_URL + headUrl;
        }
        GlideUtils.loadImage2(getActivity(), headUrl, mRoundImageView, R.drawable.head);

    }


    @Override
    public void showProgress() {
        mLoadingWindow.showView();
    }

    @Override
    public void disimissProgress() {
        mLoadingWindow.cancleView();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        switch (mType) {
            case MethodUrl.shareUrl:
                mShareMap = tData;
                break;
            case MethodUrl.USER_INFO://用户信息 //{auth=1, firm_kind=0, head_pic=default, name=刘英超, tel=151****3298, idno=4107****3616, cmpl_info=0}
                MbsConstans.USER_MAP = tData;
                SPUtils.put(getActivity(), MbsConstans.SharedInfoConstans.LOGIN_INFO, JSONUtil.getInstance().objectToJson(MbsConstans.USER_MAP));
                mUserName.setText(MbsConstans.USER_MAP.get("name") + "");
                initHeadPic();
                break;
            case MethodUrl.bankCard:
                String result = tData.get("result") + "";
                if (UtilTools.empty(result)) {
                } else {
                    List<Map<String, Object>> list = JSONUtil.getInstance().jsonToList(result);
                    if (list != null && list.size() > 0) {
                        mBankInfo = list.get(0);
                        mBankCardTv.setText("银行账户(" + mBankInfo.get("accid") + ")余额");
                    } else {

                    }
                }
                break;

            case MethodUrl.allZichan:
                String yue = UtilTools.getMoney(tData.get("use_amt") + "");
                mBankMoneyTv.setText(yue);
                mShowMoneyTv.setText("隐藏余额");

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.liveSubmit:
                        getUserInfoAction();
                        break;
                    case MethodUrl.bankCard:
                        getCardInfoAction();
                        break;
                    case MethodUrl.allZichan:
                        getCardMoney();
                        break;
                    case MethodUrl.shareUrl:
                        getShareData();
                        break;
                }
                break;
        }
    }

    private void responseData() {
        if (mAdapter == null) {
            mAdapter = new ZiChanListAdapter(getActivity());
            mAdapter.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mAdapter);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter.addHeaderView(headerView);
            refreshListView.setAdapter(mLRecyclerViewAdapter);
            refreshListView.setItemAnimator(new DefaultItemAnimator());
            refreshListView.setHasFixedSize(true);
            refreshListView.setNestedScrollingEnabled(false);

            refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            refreshListView.setPullRefreshEnabled(true);
            refreshListView.setLoadMoreEnabled(false);

            refreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            refreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mAdapter.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });


        } else {

           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mAdapter.clear();
            mAdapter.addAll(mDataList);
            refreshListView.setAdapter(mLRecyclerViewAdapter);
//            mAdapter.clear();
//            mAdapter.addAll(mDataList);
//            mAdapter.notifyDataSetChanged();
//            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            refreshListView.setNoMore(true);
        } else {
            refreshListView.setNoMore(false);
        }

        if (mAdapter.getDataList().size() <= 0) {
            pageView.showEmpty();
        } else {
            pageView.showContent();
        }
    }

    private void responseData1() {
        if (mAdapter1 == null) {
            mAdapter1 = new ZiChanListAdapter(getActivity());
            mAdapter1.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mAdapter1);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter1 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter.addHeaderView(headerView);
            refreshListView.setAdapter(mLRecyclerViewAdapter1);
            refreshListView.setItemAnimator(new DefaultItemAnimator());
            refreshListView.setHasFixedSize(true);
            refreshListView.setNestedScrollingEnabled(false);

            refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            refreshListView.setPullRefreshEnabled(true);
            refreshListView.setLoadMoreEnabled(false);

            refreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            refreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);

            mLRecyclerViewAdapter1.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mAdapter1.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });


        } else {

           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mAdapter1.clear();
            mAdapter1.addAll(mDataList);
            refreshListView.setAdapter(mLRecyclerViewAdapter1);
        }

        refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            refreshListView.setNoMore(true);
        } else {
            refreshListView.setNoMore(false);
        }

        if (mAdapter1.getDataList().size() <= 0) {
            pageView.showEmpty();
        } else {
            pageView.showContent();
        }
    }

    private void responseData2() {
        if (mAdapter2 == null) {
            mAdapter2 = new ZiChanListAdapter(getActivity());
            mAdapter2.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mAdapter2);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter2 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter.addHeaderView(headerView);
            refreshListView.setAdapter(mLRecyclerViewAdapter2);
            refreshListView.setItemAnimator(new DefaultItemAnimator());
            refreshListView.setHasFixedSize(true);
            refreshListView.setNestedScrollingEnabled(false);

            refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            refreshListView.setPullRefreshEnabled(true);
            refreshListView.setLoadMoreEnabled(false);

            refreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            refreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);

            mLRecyclerViewAdapter2.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mAdapter2.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });


        } else {

           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mAdapter2.clear();
            mAdapter2.addAll(mDataList);
            refreshListView.setAdapter(mLRecyclerViewAdapter2);
        }

        refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            refreshListView.setNoMore(true);
        } else {
            refreshListView.setNoMore(false);
        }

        if (mAdapter2.getDataList().size() <= 0) {
            pageView.showEmpty();
        } else {
            pageView.showContent();
        }
    }

    private void responseData3() {
        if (mAdapter3 == null) {
            mAdapter3 = new ZiChanListAdapter(getActivity());
            mAdapter3.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mAdapter3);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter3 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter.addHeaderView(headerView);
            refreshListView.setAdapter(mLRecyclerViewAdapter3);
            refreshListView.setItemAnimator(new DefaultItemAnimator());
            refreshListView.setHasFixedSize(true);
            refreshListView.setNestedScrollingEnabled(false);

            refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            refreshListView.setPullRefreshEnabled(true);
            refreshListView.setLoadMoreEnabled(false);

            refreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            refreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);

            mLRecyclerViewAdapter3.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mAdapter3.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });


        } else {

           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mAdapter3.clear();
            mAdapter3.addAll(mDataList);
            refreshListView.setAdapter(mLRecyclerViewAdapter3);
        }

        refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            refreshListView.setNoMore(true);
        } else {
            refreshListView.setNoMore(false);
        }

        if (mAdapter3.getDataList().size() <= 0) {
            pageView.showEmpty();
        } else {
            pageView.showContent();
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void reLoadingData() {

    }


}
