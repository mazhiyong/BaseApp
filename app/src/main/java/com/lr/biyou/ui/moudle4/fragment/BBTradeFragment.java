package com.lr.biyou.ui.moudle4.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidkun.xtablayout.XTabLayout;
import com.flyco.dialog.utils.CornerUtils;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.BasicRecycleViewAdapter;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.mywidget.dialog.SureOrNoDialog;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle3.activity.CoinInfoDetailActivity;
import com.lr.biyou.ui.moudle3.adapter.BuyAdapter;
import com.lr.biyou.ui.moudle3.adapter.SellAdapter;
import com.lr.biyou.ui.moudle3.adapter.TypeSelectAdapter;
import com.lr.biyou.ui.moudle4.activity.WeiTuoListActivity;
import com.lr.biyou.ui.moudle4.adapter.EntrustListAdapter;
import com.lr.biyou.ui.moudle4.adapter.WeiTuoListAdapter;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.BigDecimalUtils;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wanou.framelibrary.okgoutil.websocket.listener.WsStatusListener;
import com.xw.repo.BubbleSeekBar;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * OTC  币币交易
 */
public class BBTradeFragment extends BasicFragment implements RequestView, ReLoadingData, SelectBackListener {
    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.lay22)
    LinearLayout lay22;
    @BindView(R.id.rbBuy)
    RadioButton rbBuy;
    @BindView(R.id.rbSell)
    RadioButton rbSell;
    @BindView(R.id.rgBuySell)
    RadioGroup rgBuySell;
    @BindView(R.id.tvLimitPrice)
    TextView tvLimitPrice;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.ivLess)
    ImageView ivLess;
    @BindView(R.id.view18)
    View view18;
    @BindView(R.id.ivPlus)
    ImageView ivPlus;
    @BindView(R.id.clPrice)
    ConstraintLayout clPrice;
    @BindView(R.id.tvMarketPrice)
    TextView tvMarketPrice;
    @BindView(R.id.tvCnyPrice)
    TextView tvCnyPrice;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.clNumber)
    ConstraintLayout clNumber;
    @BindView(R.id.tvAvailable)
    TextView tvAvailable;
    @BindView(R.id.coinCoinSeekBar)
    RadioGroup coinCoinSeekBar;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.tvTransactionAmount)
    TextView tvTransactionAmount;
    @BindView(R.id.clTransactionAmount)
    ConstraintLayout clTransactionAmount;
    @BindView(R.id.tvOperateCoin)
    TextView tvOperateCoin;
    @BindView(R.id.clLeft)
    ConstraintLayout clLeft;
    @BindView(R.id.textView59)
    TextView textView59;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.rvSell)
    RecyclerView rvSell;
    @BindView(R.id.tvCurrentPrice)
    TextView tvCurrentPrice;
    @BindView(R.id.tvCurrentCny)
    TextView tvCurrentCny;
    @BindView(R.id.rvBuy)
    RecyclerView rvBuy;
    @BindView(R.id.clRight)
    ConstraintLayout clRight;
    @BindView(R.id.tlEntrust)
    LinearLayout tlEntrust;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.rvEntrustDay)
    RecyclerView rvEntrustDay;
    @BindView(R.id.hsv)
    HorizontalScrollView hsv;
    @BindView(R.id.textView64)
    TextView textView64;
    @BindView(R.id.ivEmptyContent)
    ImageView ivEmptyContent;
    @BindView(R.id.tvEmptyContent)
    TextView tvEmptyContent;
    @BindView(R.id.clView)
    ConstraintLayout clView;
    @BindView(R.id.select_iv)
    ImageView selectIv;
    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.iv_toCoinInfo)
    ImageView ivToCoinInfo;
    @BindView(R.id.rb_number1)
    RadioButton rbNumber1;
    @BindView(R.id.rb_number2)
    RadioButton rbNumber2;
    @BindView(R.id.rb_number3)
    RadioButton rbNumber3;
    @BindView(R.id.rb_number4)
    RadioButton rbNumber4;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.rcv)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.bundle_seekBar)
    BubbleSeekBar bundleSeekBar;
    @BindView(R.id.areaTv)
    TextView areaTv;


    public LoadingWindow mLoadingWindow;
    @BindView(R.id.smartRefreshLay)
    SmartRefreshLayout smartRefreshLay;


    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private WeiTuoListAdapter mWeiTuoListAdapter;
    private SellAdapter sellAdapter;
    private BuyAdapter buyAdapter;
    private EntrustListAdapter entrustListAdapter;
    private TypeSelectAdapter mAdapter;


    private List<Map<String, Object>> mDataList3 = new ArrayList<>();
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private List<List<String>> mDataListBuy = new ArrayList<>();
    private List<List<String>> mDataListSell = new ArrayList<>();
    private List<Map<String, Object>> mDatas = new ArrayList<>();
    private List<Map<String, Object>> mtabsData = new ArrayList<>();
    private Map<String,Object> mPairPrecisionData = new HashMap<>();
    private Map<String, Object> mPairPrecisionMap = new HashMap<>();


    List<Map<String,Object>> mapOneList = new ArrayList<>();

    private KindSelectDialog mDialog;

    private String mRequestTag = "";
    public String area ="USDT";
    public String symbol ="BTC";
    public String buysell = "1";
    private String buysell2 = "0";



    private DecimalFormat decimalFormat = new DecimalFormat();
    private DecimalFormat decimalFormat1 = new DecimalFormat();
    private DecimalFormat decimalFormat3 = new DecimalFormat();

    private int mPage = 1;
    private AnimUtil mAnimUtil;

    //private WsManager wsManager;
    //private Handler handler = new Handler();
    private String mSelectType = "0"; // 0 限价  1 市价
    private String mKindType = "0"; // 0 买入  1 卖出

    private String Symbol_Account = "0";
    private String Area_Account = "0";

    private final int QUEST_CODE = 100;


    private int pricePrecision = 2;
    private double precisionNum = 0;
    private double price;
    private double ratio = 1.0;


    private Handler handler = new Handler();

    //HTTP请求  轮询
    private Runnable cnyRunnable = new Runnable() {
        @Override
        public void run() {
            // 获取币当前价
            getCurrentPriceAction();

            //获取合约价格以及深度信息
            getPairDepthAction();

            handler.postDelayed(this, MbsConstans.SECOND_TIME_5000);
        }
    };

    private void getPairDepthAction() {
        mRequestTag = MethodUrl.COIN_DEPTH;
        Map<String, Object> map = new HashMap<>();
        /*if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(), MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);*/
        map.put("symbol", symbol);
        map.put("area", area);
        map.put("depth", "1");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.COIN_DEPTH, map);
    }

    private void getCurrentPriceAction() {
        mRequestTag = MethodUrl.CURRENT_PRICE;
        Map<String, Object> map = new HashMap<>();
       /* if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(CoinInfoDetailActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);*/
        map.put("area",area);
        map.put("symbol",symbol);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CURRENT_PRICE, map);
    }

    public BBTradeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_repayment;
    }


    /**
     * 只加载一次
     * -------------------------------------懒加载  start
     */
    boolean isViewInitiated;
    boolean isVisibleToUser;
    boolean isDataInitiated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public boolean prepareFetchData() {
        return prepareFetchData(true);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            //请求数据

            //查询交易区列表
            getAreaListAction();

            //查询交易区列表项
            getAreaListItemAction();

            //查询委托单
            //getEntrustListAction();

            //账户当前交易区交易币可用
            getCurAreaAccountAction();

            //查询价格和数量精度
            getPariPrecionAction();

            //setWebsocketListener();
            //handler.post(runnable);



            handler.post(cnyRunnable);
            LogUtilDebug.i("show", "BB懒加载数据,每次进入界面加载");
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    private void getPariPrecionAction() {
        mRequestTag = MethodUrl.PAIR_PRECISION;
        Map<String, Object> map = new HashMap<>();
        /*if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);*/
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.PAIR_PRECISION, map);

    }

    /**
     * --------------------------------------懒加载     end
     */


   /* //开辟线程 轮询
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Map<String, Object> map = new HashMap<>();
            map.put("area", "USDT");
            map.put("method", "queryCoinDepth");
            map.put("symbol", "BTC");
            map.put("type", "1");

            //ws 关闭 连接深度ws
            if (!wsManager.isWsConnected()) {
                wsManager.startConnect();
            }

            wsManager.sendMessage(GsonUtils.toJson(map));
            try {
//                if (getCoinObject() == null) {
//                    // 如果查到有对应的币对, 添加对应币对的订阅
//                    currenctPriceWsParams.setArea(area);
//                    currenctPriceWsParams.setSymbol(symbol);
//                   LogUtilDebug.i("show","currenctPriceWsParams:"+GsonUtils.toJson(currenctPriceWsParams));
//                    currenctPrice16.sendMessage(GsonUtils.toJson(currenctPriceWsParams));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //每隔 0.5s 发送一次
            handler.postDelayed(this, MbsConstans.SECOND_TIME_500);
        }
    };*/

    @Override
    public void init() {
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(getActivity()));
//        mTitleBarView.setLayoutParams(layoutParams);
//        mTitleBarView.setPadding(0, UtilTools.getStatusHeight2(getActivity()), 0, 0);
//        mTitleText.setText(getResources().getString(R.string.bottom_heyue));
//        mLeftBackLay.setVisibility(View.GONE);
        //wsManager = BasicApplication.getWsManager();

     /*   if (buysell.equals("1")){ //买入
            rbBuy.setChecked(true);
        }else {  //卖出
            rbSell.setChecked(true);
        }

        selectTv.setText(area + "/" + symbol);*/

        mAnimUtil = new AnimUtil();

        mLoadingWindow = new LoadingWindow(getActivity(), R.style.Dialog);
        //mLoadingWindow.showView();

        List<Map<String, Object>> mDataList2 = SelectDataUtil.getBBPriceType();
        mDialog = new KindSelectDialog(getActivity(), true, mDataList2, 10);
        mDialog.setSelectBackListener(this);

        initView();
        setBarTextColor();

        rgBuySell.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbSell:
                        mKindType = "1";
                        //bundleSeekBar.setProgress(0);
                        tvOperateCoin.setText("卖出" + symbol);
                        tvOperateCoin.setBackgroundResource(R.drawable.btn_next_red);
                        rbNumber1.setBackgroundResource(R.drawable.selector_open_close_house3);
                        rbNumber2.setBackgroundResource(R.drawable.selector_open_close_house3);
                        rbNumber3.setBackgroundResource(R.drawable.selector_open_close_house3);
                        rbNumber4.setBackgroundResource(R.drawable.selector_open_close_house3);

                        if (mSelectType.equals("0")) { //限价
                            clPrice.setVisibility(View.VISIBLE);
                            tvMarketPrice.setVisibility(View.GONE);
                            etPrice.setHint("价格");
                            tvCnyPrice.setVisibility(View.VISIBLE);
                        } else { //市价 卖
                            etNumber.setHint("数量");
                            tvUnit.setText(symbol);
                            clPrice.setVisibility(View.GONE);
                            tvMarketPrice.setVisibility(View.VISIBLE);
                            tvCnyPrice.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case R.id.rbBuy:
                        mKindType = "0";
                        //bundleSeekBar.setProgress(0);
                        tvOperateCoin.setText("买入" + symbol);
                        tvOperateCoin.setBackgroundResource(R.drawable.btn_next_green);
                        rbNumber1.setBackgroundResource(R.drawable.selector_open_close_house2);
                        rbNumber2.setBackgroundResource(R.drawable.selector_open_close_house2);
                        rbNumber3.setBackgroundResource(R.drawable.selector_open_close_house2);
                        rbNumber4.setBackgroundResource(R.drawable.selector_open_close_house2);

                        if (mSelectType.equals("0")) { //限价
                            clPrice.setVisibility(View.VISIBLE);
                            tvMarketPrice.setVisibility(View.GONE);
                            tvCnyPrice.setVisibility(View.VISIBLE);
                            etPrice.setHint("价格");
                        } else { //市价买
                           /* clPrice.setVisibility(View.VISIBLE);
                            tvCnyPrice.setVisibility(View.GONE);
                            etPrice.setHint("金额");*/
                            etNumber.setHint("金额");
                            tvUnit.setText(area);
                            clPrice.setVisibility(View.GONE);
                            tvMarketPrice.setVisibility(View.VISIBLE);
                            tvCnyPrice.setVisibility(View.INVISIBLE);

                        }
                        break;
                }
            }
        });

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0 && mSelectType.equals("0") && !UtilTools.empty(etPrice.getText().toString())) {//限价
                    tvTransactionAmount.setText(UtilTools.getNormalMoney(Double.parseDouble(s.toString()) * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                }else {
                    tvTransactionAmount.setText("--");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    tvCnyPrice.setText(UtilTools.formatDecimal(ratio*Double.parseDouble(s.toString())+"",2)+" CNY");
                    if (rbNumber1.isChecked()) {
                        if (mKindType.equals("0")) { //买入
                            if (mSelectType.equals("0")) { //限价
                                //交易区总可用额度/用户输入的价格-> 用户可买的最大数量
                                float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                                float number =  (maxNumber * 0.1f);
                                etNumber.setText(number + "");
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(s.toString()) + "") + "  " + area);
                            } else { //市价
                                // 用户任意输入数量
                                etNumber.setText("10");
                                tvTransactionAmount.setText("--");
                            }

                        } else {
                            //用户当前可卖的最大数量
                            float number =  (Float.parseFloat(Symbol_Account) * 0.1f);
                            etNumber.setText(number + "");
                            if (mSelectType.equals("0")) { //限价
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(s.toString()) + "") + "  " + area);
                            } else {
                                tvTransactionAmount.setText("--");
                            }
                        }
                    }

                    if (rbNumber2.isChecked()) {
                        if (mKindType.equals("0")) { //买入
                            if (mSelectType.equals("0")) { //限价
                                float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                                float number = (maxNumber * 0.2f);
                                etNumber.setText(number + "");
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(s.toString()) + "") + "  " + area);
                            } else { //市价
                                // 用户任意输入数量
                                etNumber.setText("20");
                                tvTransactionAmount.setText("--");
                            }

                        } else {
                            float number = (Float.parseFloat(Symbol_Account) * 0.2f);
                            etNumber.setText(number + "");
                            if (mSelectType.equals("0")) { //限价
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(s.toString()) + "") + "  " + area);
                            } else {
                                tvTransactionAmount.setText("--");
                            }
                        }
                    }

                    if (rbNumber3.isChecked()) {
                        if (mKindType.equals("0")) { //买入
                            if (mSelectType.equals("0")) { //限价
                                float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                                float number =  (maxNumber * 0.5f);
                                etNumber.setText(number + "");
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(s.toString()) + "") + "  " + area);
                            } else { //市价
                                etNumber.setText("50");
                                tvTransactionAmount.setText("--");
                            }

                        } else {
                            float number =  (Float.parseFloat(Symbol_Account) * 0.5f);
                            etNumber.setText(number + "");
                            if (mSelectType.equals("0")) { //限价
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(s.toString()) + "") + "  " + area);
                            } else {
                                tvTransactionAmount.setText("--");
                            }
                        }
                    }

                    if (rbNumber4.isChecked()) {
                        if (mKindType.equals("0")) { //买入
                            if (mSelectType.equals("0")) { //限价
                                float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                                float number =  (maxNumber * 1);
                                etNumber.setText(number + "");
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(s.toString()) + "") + "  " + area);
                            } else { //市价
                                etNumber.setText("100");
                                tvTransactionAmount.setText("--");
                            }

                        } else {
                            float number =  (Float.parseFloat(Symbol_Account));
                            etNumber.setText(number + "");
                            if (mSelectType.equals("0")) { //限价
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(s.toString()) + "") + "  " + area);
                            } else {
                                tvTransactionAmount.setText("--");
                            }
                        }
                    }


                } else {
                    etNumber.setText("");
                    tvCnyPrice.setText("0.00 CNY" );
                    tvTransactionAmount.setText("--");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        coinCoinSeekBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (mSelectType) {
                    case "0": //限价
                        if (UtilTools.empty(etPrice.getText().toString()) && UtilTools.empty(etPrice.getText().toString().replaceAll(",", "").trim())) {
                            showToastMsg("请输入价格");
                            etNumber.setText("");
                            return;
                        }
                        break;

                    case "1": //市价

                        break;
                }

                switch (checkedId) {
                    case R.id.rb_number1: //10
                        if (mKindType.equals("0")) { //买入
                            if (mSelectType.equals("0")) { //限价
                                float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                                float number =  (maxNumber * 0.1f);
                                etNumber.setText(number + "");
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                            } else { //市价
                                etNumber.setText("10");
                                tvTransactionAmount.setText("--");
                            }

                        } else { //卖出
                            float number =  (Float.parseFloat(Symbol_Account) * 0.1f);
                            etNumber.setText(number + "");
                            if (mSelectType.equals("0")) { //限价
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                            } else {
                                tvTransactionAmount.setText("--");
                            }
                        }
                        break;
                    case R.id.rb_number2: //20
                        if (mKindType.equals("0")) { //买入
                            if (mSelectType.equals("0")) { //限价
                                float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                                float number =  (maxNumber * 0.2f);
                                etNumber.setText(number + "");
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                            } else { //市价
                                etNumber.setText("20");
                                tvTransactionAmount.setText("--");
                            }
                        } else { //卖出
                            float number = (Float.parseFloat(Symbol_Account) * 0.2f);
                            etNumber.setText(number + "");
                            if (mSelectType.equals("0")) { //限价
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                            } else {
                                tvTransactionAmount.setText("--");
                            }
                        }
                        break;

                    case R.id.rb_number3: //50
                        if (mKindType.equals("0")) { //买入
                            if (mSelectType.equals("0")) { //限价
                                float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                                float number =  (maxNumber * 0.5f);
                                etNumber.setText(number + "");
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                            } else { //市价
                                etNumber.setText("50");
                                tvTransactionAmount.setText("--");
                            }
                        } else { //卖出
                            float number =  (Float.parseFloat(Symbol_Account) * 0.5f);
                            etNumber.setText(number + "");
                            if (mSelectType.equals("0")) { //限价
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                            } else {
                                tvTransactionAmount.setText("--");
                            }
                        }
                        break;

                    case R.id.rb_number4: //100
                        if (mKindType.equals("0")) { //买入
                            if (mSelectType.equals("0")) { //限价
                                float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                                float number =  (maxNumber * 1.0f);
                                etNumber.setText(number + "");
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                            } else { //市价
                                etNumber.setText("100");
                                tvTransactionAmount.setText("--");
                            }

                        } else { //卖出
                            float number = (Float.parseFloat(Symbol_Account) * 1.0f);
                            etNumber.setText(number + "");
                            if (mSelectType.equals("0")) { //限价
                                tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                            } else {
                                tvTransactionAmount.setText("--");
                            }
                        }
                        break;
                }
            }
        });


        bundleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                switch (mSelectType) {
                    case "0": //限价
                        if (UtilTools.empty(etPrice.getText().toString()) && UtilTools.empty(etPrice.getText().toString().replaceAll(",", "").trim())) {
                            showToastMsg("请输入价格");
                            etNumber.setText("");
                            return;
                        }
                        break;

                    case "1": //市价

                        break;
                }
                if (progress > 0) {



                    if (mKindType.equals("0")) { //买入
                        if (mSelectType.equals("0")) { //限价
                            float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(etPrice.getText().toString().replaceAll(",", "").trim());
                            float number = (maxNumber * progress / 100);
                            etNumber.setText(number + "");
                            tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                        } else { //市价

                            //etNumber.setText(progress + "");
                            //tvTransactionAmount.setText("--");

                            float maxNumber = Float.parseFloat(Area_Account) / Float.parseFloat(tvCurrentPrice.getText().toString().replaceAll(",", "").trim());
                            float number = (maxNumber * progress / 100);
                            etNumber.setText(number + "");
                            tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(tvCurrentPrice.getText().toString()) + "") + "  " + area);
                        }

                    } else { //卖出
                        float number = (Float.parseFloat(Symbol_Account) * progress / 100);
                        etNumber.setText(number + "");
                        if (mSelectType.equals("0")) { //限价
                            tvTransactionAmount.setText(UtilTools.getNormalMoney(number * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                        } else {
                            tvTransactionAmount.setText("--");
                        }
                    }

                   /* if ( number > 0 ){
                        etHand.setEnabled(false);
                    }else {
                        etHand.setEnabled(true);
                    }*/
                } else {
                    etNumber.setText("0");
                }
            }
        });


    }


//
//    @Override
//    public void onResume() {
//        super.onResume();
//       LogUtilDebug.i("show","onResume()");
//        if (getUserVisibleHint()) {
//            setWebsocketListener();
//            handler.post(runnable);
//        }
//    }

    private void setWebsocketListener() {
      /*  if (!wsManager.isWsConnected()) {
            wsManager.startConnect();
        }
        wsManager.setWsStatusListener(wsStatusListener);*/
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);

        LinearLayoutManager manager = new LinearLayoutManager(getParentFragment().getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        rvEntrustDay.setHasFixedSize(true);
        rvEntrustDay.setNestedScrollingEnabled(false);
        entrustListAdapter = new EntrustListAdapter(getActivity());
        rvEntrustDay.setAdapter(entrustListAdapter);


        entrustListAdapter.setOrderList(mDataList3);

        rvSell.setHasFixedSize(true);
        rvSell.setNestedScrollingEnabled(false);
        sellAdapter = new SellAdapter(getActivity());
        rvSell.setAdapter(sellAdapter);
        sellAdapter.setOnItemClickListener(new BasicRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (sellAdapter.getBuySell().size() - 1 < position) {
                    etPrice.setText("0");
                } else {
                    List<String> strings = sellAdapter.getBuySell().get(position);
                    etPrice.setText( UtilTools.formatDecimal(strings.get(0), pricePrecision));

                }
            }
        });

        //sellAdapter.setSellTradeInfos(mDataListSell,precision);

        rvBuy.setHasFixedSize(true);
        rvBuy.setNestedScrollingEnabled(false);
        buyAdapter = new BuyAdapter(getActivity());
        rvBuy.setAdapter(buyAdapter);

        buyAdapter.setOnItemClickListener(new BasicRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (buyAdapter.getBuySell().size() - 1 < position) {
                    etPrice.setText("0");
                } else {
                    List<String> strings = buyAdapter.getBuySell().get(position);
                    etPrice.setText( UtilTools.formatDecimal(strings.get(0), pricePrecision));
                }
            }
        });

        smartRefreshLay.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //mPage = 1;
                getEntrustListAction();
            }
        });

        // buyAdapter.setBuyTradeInfo(mDataListBuy,precision);


//        List<Map<String, Object>> tabs = SelectDataUtil.getTabValues3();
//        for (Map<String, Object> map : tabs) {
//            tlEntrust.addTab(tlEntrust.newTab().setText(map.get("name") + ""));
//        }
//        tlEntrust.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                // 委托信息
//                // getEntrustInfo(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                // 委托信息
//                //getEntrustInfo(tab.getPosition());
//            }
//        });

    }

    @OnClick({R.id.select_iv, R.id.select_tv,R.id.ivPlus,R.id.ivLess, R.id.iv_toCoinInfo, R.id.tv_all, R.id.tvLimitPrice, R.id.tvOperateCoin})
    public void onViewClicked(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.select_iv:
            case R.id.select_tv:
                initPopupWindow();
                break;
            case R.id.ivPlus:
                if (!UtilTools.empty(mPairPrecisionMap) && !UtilTools.empty(mPairPrecisionMap.get("price"))){
                    String pricePrecisionStr = mPairPrecisionMap.get("price")+"";
                    if (!UtilTools.empty(pricePrecisionStr)){
                        pricePrecision = Integer.parseInt(pricePrecisionStr);
                    }else {
                        pricePrecision = 2;
                    }
                }else {
                    pricePrecision = 2;
                }
                switch (pricePrecision){
                    case 2:
                        precisionNum = 0.01;
                        break;
                    case 4:
                        precisionNum = 0.0001;
                        break;
                    case 6:
                        precisionNum = 0.000001;
                        break;
                    case 8:
                        precisionNum = 0.00000001;
                        break;
                    case 10:
                        precisionNum = 0.0000000001;
                        break;
                }

                if (UtilTools.empty(etPrice.getText())){
                    price = 0;
                }else {
                    price = Double.parseDouble(etPrice.getText()+"");
                }
                price = price+precisionNum;
                etPrice.setText(UtilTools.formatDecimal(price+"",pricePrecision));
                break;
            case R.id.ivLess:
                if (!UtilTools.empty(mPairPrecisionMap) && !UtilTools.empty(mPairPrecisionMap.get("price"))){
                    String pricePrecisionStr = mPairPrecisionMap.get("price")+"";
                    if (!UtilTools.empty(pricePrecisionStr)){
                        pricePrecision = Integer.parseInt(pricePrecisionStr);
                    }else {
                        pricePrecision = 2;
                    }
                }else {
                    pricePrecision = 2;
                }
                switch (pricePrecision){
                    case 2:
                        precisionNum = 0.01;
                        break;
                    case 4:
                        precisionNum = 0.0001;
                        break;
                    case 6:
                        precisionNum = 0.000001;
                        break;
                    case 8:
                        precisionNum = 0.00000001;
                        break;
                    case 10:
                        precisionNum = 0.0000000001;
                        break;
                }
                if (UtilTools.empty(etPrice.getText())){
                    price = 0;
                }else {
                    price = Double.parseDouble(etPrice.getText()+"");
                    if (price > 0){
                        price = price-precisionNum;
                        etPrice.setText(UtilTools.formatDecimal(price+"",pricePrecision));
                    }
                }
                break;
            case R.id.iv_toCoinInfo:
                Intent intent1 = new Intent(getActivity(), CoinInfoDetailActivity.class);
                intent1.putExtra("symbol", symbol);
                intent1.putExtra("area", area);
                intent1.putExtra("from", "2");
                startActivityForResult(intent1, QUEST_CODE);
                break;
            case R.id.tv_all:
                intent = new Intent(getParentFragment().getActivity(), WeiTuoListActivity.class);
                startActivity(intent);
                break;
            case R.id.tvLimitPrice:
                mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tvOperateCoin:
                buyAndSellAction();
                break;
        }
    }

    private void buyAndSellAction() {
        mRequestTag = MethodUrl.GUADAN_TRADE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("area", area);
        map.put("symbol", symbol);
        map.put("number", etNumber.getText() + "");
        map.put("direction", mKindType);
        map.put("type", mSelectType);
        if (mSelectType.equals("1") && mKindType.equals("0")) {
            map.put("number", "");
            map.put("total", etNumber.getText() + ""); //市价买
        } else {
            map.put("price", etPrice.getText() + "");
        }

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.GUADAN_TRADE, map);
    }


    private void getEntrustListAction() {
        mRequestTag = MethodUrl.ENTRUST_LIST;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("area", area);
        map.put("symbol", symbol);
        map.put("status", "1");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.ENTRUST_LIST, map);

    }

    private void getCurAreaAccountAction() {
        mRequestTag = MethodUrl.AREA_COIN_ACCOUNT;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("area", area);
        map.put("symbol", symbol);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.AREA_COIN_ACCOUNT, map);

    }


    private void getAreaListAction() {
        mRequestTag = MethodUrl.COIN_AREAALL;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        //map.put("token",MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.COIN_AREAALL, map);

    }

    private void getAreaListItemAction() {
        mRequestTag = MethodUrl.AREA_ITEM;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("area", area);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.AREA_ITEM, map);

    }


    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    private XTabLayout tabLayout;
    private RecyclerView rcv;

    private void initPopupWindow() {

        if (mConditionDialog == null) {
            popView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_bitype, null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            initConditionDialog(popView);
            mConditionDialog.setClippingEnabled(false);

            int screenWidth = UtilTools.getScreenWidth(getActivity());
            int screenHeight = UtilTools.getScreenHeight(getActivity());
            mConditionDialog.setWidth((int) (screenWidth * 0.8));
            mConditionDialog.setHeight(WindowManager.LayoutParams.MATCH_PARENT);

            //设置background后在外点击才会消失
            mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(getActivity(), 5)));
            mConditionDialog.setOutsideTouchable(true);// 设置可允许在外点击消失
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

    private int tabPostion = 0;

    private void initConditionDialog(View view) {
        tabLayout = view.findViewById(R.id.tab_child);
        rcv = view.findViewById(R.id.rcv);
        LinearLayoutManager manager = new LinearLayoutManager(getParentFragment().getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcv.setLayoutManager(manager);
        if (mtabsData != null && mDatas != null) {
            for (Map<String, Object> map : mtabsData) {
                tabLayout.addTab(tabLayout.newTab().setText(map.get("name") + ""));
            }

            mAdapter = new TypeSelectAdapter(getParentFragment().getActivity(), mDatas, 10);
            rcv.setAdapter(mAdapter);
        }


        tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                tabPostion = tab.getPosition();
                if (mtabsData != null && mtabsData.size() > 0) {
                    area = mtabsData.get(tabPostion).get("name") + "";

                    getAreaListItemAction();
                }

            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });


        mAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
            @Override
            public void OnMyItemClickListener(View view, int position) {
                mConditionDialog.dismiss();
                symbol = mDatas.get(position).get("name") + "";
                selectTv.setText(symbol + "/" + area);
                if (!UtilTools.empty(mPairPrecisionData) && !UtilTools.empty(mPairPrecisionData.get((symbol+area).toLowerCase()))){
                    mPairPrecisionMap = (Map<String, Object>) mPairPrecisionData.get((symbol+area).toLowerCase());
                    if (!UtilTools.empty(mPairPrecisionMap) && !UtilTools.empty(mPairPrecisionMap.get("price"))){
                        String pricePrecisionStr = mPairPrecisionMap.get("price")+"";
                        if (!UtilTools.empty(pricePrecisionStr)){
                            pricePrecision = Integer.parseInt(pricePrecisionStr);
                        }else {
                            pricePrecision = 2;
                        }
                    }
                }
                areaTv.setText(area);

                if (!UtilTools.empty(etNumber.getText()) && !UtilTools.empty(etPrice.getText())) {
                    tvTransactionAmount.setText(UtilTools.getNormalMoney(Double.parseDouble(etNumber.getText().toString()) * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                } else {
                    tvTransactionAmount.setText("--");
                }
                if (buysell.equals("1")) { //买入
                    rbBuy.setChecked(true);
                    mKindType = "0";
                    tvUnit.setText(area);
                    tvOperateCoin.setText("买入" + symbol);
                    tvOperateCoin.setBackgroundResource(R.drawable.btn_next_green);
                    rbNumber1.setBackgroundResource(R.drawable.selector_open_close_house2);
                    rbNumber2.setBackgroundResource(R.drawable.selector_open_close_house2);
                    rbNumber3.setBackgroundResource(R.drawable.selector_open_close_house2);
                    rbNumber4.setBackgroundResource(R.drawable.selector_open_close_house2);
                } else {
                    rbSell.setChecked(true);
                    mKindType = "1";
                    tvUnit.setText(symbol);
                    tvOperateCoin.setText("卖出" + symbol);
                    tvOperateCoin.setBackgroundResource(R.drawable.btn_next_red);
                    rbNumber1.setBackgroundResource(R.drawable.selector_open_close_house3);
                    rbNumber2.setBackgroundResource(R.drawable.selector_open_close_house3);
                    rbNumber3.setBackgroundResource(R.drawable.selector_open_close_house3);
                    rbNumber4.setBackgroundResource(R.drawable.selector_open_close_house3);
                }

                getEntrustListAction();
                getCurAreaAccountAction();
                // 获取币当前价
                getCurrentPriceAction();
                //获取合约价格以及深度信息
                getPairDepthAction();
            }
        });


    }


    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
    }


    private void responseData() {
        if (mWeiTuoListAdapter == null) {
            mWeiTuoListAdapter = new WeiTuoListAdapter(getActivity());
            mWeiTuoListAdapter.addAll(mDataList);

            AnimationAdapter adapter = new ScaleInAnimationAdapter(mWeiTuoListAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mWeiTuoListAdapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(true);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
                    .setHeight(R.dimen.dp_10)
                    .setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.body_bg)
                    .build();
            mRefreshListView.addItemDecoration(divider2);


        } else {

           /* if (mPage == 1) {
                mWeiTuoListAdapter.clear();
            }*/
            mWeiTuoListAdapter.clear();
            mWeiTuoListAdapter.addAll(mDataList);
            mWeiTuoListAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");

        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
            mPage++;
        }
        mRefreshListView.refreshComplete(10);
        mWeiTuoListAdapter.notifyDataSetChanged();

        if (mWeiTuoListAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mWeiTuoListAdapter.setmCallBack(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                SureOrNoDialog sureOrNoDialog = new SureOrNoDialog(getActivity(), true);
                sureOrNoDialog.initValue("提示", "是否撤销当前委托？");
                sureOrNoDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cancel:
                                sureOrNoDialog.dismiss();
                                break;
                            case R.id.confirm:
                                sureOrNoDialog.dismiss();
                                //撤销委托
                                cancelWeituoAction(mParentMap);
                                break;
                        }
                    }
                });
                sureOrNoDialog.show();
                sureOrNoDialog.setCanceledOnTouchOutside(false);
                sureOrNoDialog.setCancelable(true);


            }
        });


    }

    private void cancelWeituoAction(Map<String, Object> mParentMap) {
        mRequestTag = MethodUrl.CANCEL_WEITUO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(), MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", mParentMap.get("id") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CANCEL_WEITUO, map);
    }


    @Override
    public void showProgress() {
        //mLoadingWindow.show();
    }

    @Override
    public void disimissProgress() {
        //mLoadingWindow.cancleView();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        //mPageView.showNetworkError();
        mLoadingWindow.cancleView();
        Intent intent;
        switch (mType) {
            case MethodUrl.CURRENT_PRICE:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String, Object> mapData = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(mapData)) {
                                tvCurrentPrice.setText(UtilTools.formatNumber(mapData.get("price") + "","#.########")+"");
                                tvCurrentCny.setText("≈"+UtilTools.formatDecimal(mapData.get("cny_number")+ "",2)+"CNY");
                                //tvCurrentCny.setText(getResources().getString(R.string.defaultCny).replace("%S", UtilTools.formatNumber(mapData.get("cny_number") + "", "#.##")));
                                if (!UtilTools.empty(mapData.get("price")) && !UtilTools.empty(mapData.get("cny_number"))){
                                    ratio = Double.parseDouble(mapData.get("cny_number")+"")/Double.parseDouble(mapData.get("price")+"");
                                }else {
                                    ratio = 1.0;
                                }
                            }
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.ENTRUST_LIST:
                switch (tData.get("code") + "") {
                    case "0":
                        mDataList = (List<Map<String, Object>>) tData.get("data");
                        if (mDataList != null && mDataList.size() > 0) {
                            for (Map<String, Object> map : mDataList) {
                                map.put("status", "1");
                            }
                            mPageView.showContent();
                            mapOneList.clear();
                            mapOneList.add(mDataList.get(0));
                            mDataList.clear();
                            mDataList.addAll(mapOneList);
                            responseData();
                        } else {
                            mPageView.showEmpty();
                        }
                        smartRefreshLay.finishRefresh();
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        mPageView.showNetworkError();
                        showToastMsg(tData.get("msg") + "");
                        break;
                }
                break;
            case MethodUrl.COIN_AREAALL:
                switch (tData.get("code") + "") {
                    case "0":
                        mtabsData = (List<Map<String, Object>>) tData.get("data");
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }
                break;

            case MethodUrl.PAIR_PRECISION:
                switch (tData.get("code") + "") {
                    case "0":
                        mPairPrecisionData = (Map<String, Object>) tData.get("data");
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }
                break;

            case MethodUrl.AREA_ITEM:
                switch (tData.get("code") + "") {
                    case "0":
                        mDatas = (List<Map<String, Object>>) tData.get("data");
                        if (mAdapter != null && mDatas != null && rcv != null) {
                            mAdapter.setDatas(mDatas);
                            rcv.setAdapter(mAdapter);
                        }
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }
                break;

            case MethodUrl.AREA_COIN_ACCOUNT:
                switch ((tData.get("code") + "")) {
                    case "0":
                        Map<String, Object> mapData = (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)) {
                            Symbol_Account = mapData.get("symbol") + "";
                            Area_Account = mapData.get("area") + "";
                            tvAvailable.setText("可用 "+Area_Account+" "+area);
                        }

                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }
                break;
            //mRefreshListView.refreshComplete(10);

            case MethodUrl.GUADAN_TRADE:
                switch ((tData.get("code") + "")) {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        //查询委托单
                        getEntrustListAction();
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }

                break;

            case MethodUrl.COIN_DEPTH:
                switch ((tData.get("code") + "")) {
                    case "0":
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String, Object> map1 = JSONUtil.getInstance().jsonMap(tData.get("data") + "");
                            if (!UtilTools.empty(map1)) {
                                List<List<String>> mListBuy = JSONUtil.getInstance().jsonToListStr2(map1.get("buy") + "");
                                List<List<String>> mListSell = JSONUtil.getInstance().jsonToListStr2(map1.get("sell") + "");

                                // 设置深度信息
                                if (!UtilTools.empty(mListBuy) && mListBuy.size() > 0) {
                                    mDataListBuy.clear();
                                    for (List<String> strings : mListBuy) {
                                        String price = strings.get(0);
                                        String volume = strings.get(1);
                                        decimalFormat.setRoundingMode(RoundingMode.CEILING);
                                        decimalFormat.setGroupingUsed(false);
                                        String format;
                                        if (pricePrecision >= 0) {
                                            decimalFormat.setMaximumFractionDigits(pricePrecision );
                                            format = decimalFormat.format(Double.parseDouble(price));
                                        } else {
                                            decimalFormat.setMaximumFractionDigits(0);
                                            format = BigDecimalUtils.mul(decimalFormat.format(Double.parseDouble(BigDecimalUtils.divide(price, RoundingMode.CEILING, 0 - pricePrecision ).toString())), 0 - pricePrecision ).toString();
                                        }

                                        if (mDataListBuy.size() > 0) {
                                            List<String> strings2 = mDataListBuy.get(mDataListBuy.size() - 1);
                                            String s = strings2.get(0);
                                            if (format.equals(s)) {
                                                strings2.add(1, BigDecimalUtils.add(strings2.get(1), volume).toString());
                                            } else {
                                                List<String> strings1 = new ArrayList<>();
                                                strings1.add(format);
                                                strings1.add(volume);
                                                mDataListBuy.add(strings1);
                                            }
                                        } else {
                                            List<String> strings1 = new ArrayList<>();
                                            strings1.add(format);
                                            strings1.add(volume);
                                            mDataListBuy.add(strings1);
                                        }
                                    }
                                    buyAdapter.setBuyTradeInfo(mListBuy, pricePrecision);
                                }

                                if (!UtilTools.empty(mListSell) && mListSell.size() > 0) {
                                    mDataListSell.clear();
                                    for (List<String> strings : mListSell) {
                                        String price = strings.get(0);
                                        String volume = strings.get(1);
                                        decimalFormat.setRoundingMode(RoundingMode.CEILING);
                                        decimalFormat.setGroupingUsed(false);
                                        String format;
                                        if (pricePrecision  >= 0) {
                                            decimalFormat.setMaximumFractionDigits(pricePrecision );
                                            format = decimalFormat.format(Double.parseDouble(price));
                                        } else {
                                            decimalFormat.setMaximumFractionDigits(0);
                                            format = BigDecimalUtils.mul(decimalFormat.format(Double.parseDouble(BigDecimalUtils.divide(price, RoundingMode.CEILING, 0 - pricePrecision ).toString())), 0 - pricePrecision ).toString();
                                        }

                                        if (mDataListSell.size() > 0) {
                                            List<String> strings2 = mDataListSell.get(mDataListSell.size() - 1);
                                            String s = strings2.get(0);
                                            if (format.equals(s)) {
                                                strings2.add(1, BigDecimalUtils.add(strings2.get(1), volume).toString());
                                            } else {
                                                List<String> strings1 = new ArrayList<>();
                                                strings1.add(format);
                                                strings1.add(volume);
                                                mDataListSell.add(strings1);
                                            }
                                        } else {
                                            List<String> strings1 = new ArrayList<>();
                                            strings1.add(format);
                                            strings1.add(volume);
                                            mDataListSell.add(strings1);
                                        }
                                    }
                                    sellAdapter.setSellTradeInfos(mListSell, pricePrecision );
                                }

                            }
                        }

                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }

                break;
            case MethodUrl.CANCEL_WEITUO:
                switch (tData.get("code") + "") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        //查询委托单
                        getEntrustListAction();
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null) {
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }

            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                mLoadingWindow.showView();
                switch (mRequestTag) {
                    case MethodUrl.repaymentList:

                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mLoadingWindow.cancleView();
        dealFailInfo(map, mType);
    }

    @Override
    public void reLoadingData() {
        mLoadingWindow.show();
        getEntrustListAction();
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10: //限价
                tvLimitPrice.setText(map.get("name") + "");
                if ((map.get("name") + "").equals("限价")) {
                    mSelectType = "0";
                    if (!UtilTools.empty(etNumber.getText()) && !UtilTools.empty(etPrice.getText())) {
                        tvTransactionAmount.setText(UtilTools.getNormalMoney(Double.parseDouble(etNumber.getText().toString()) * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
                    } else {
                        tvTransactionAmount.setText("--");
                    }
                    if (mKindType.equals("0")) { //买入
                        clPrice.setVisibility(View.VISIBLE);
                        tvMarketPrice.setVisibility(View.GONE);
                        tvCnyPrice.setVisibility(View.VISIBLE);
                        etPrice.setHint("价格");
                        etNumber.setHint("数量");
                        tvUnit.setText(symbol);
                    } else { //卖出
                        clPrice.setVisibility(View.VISIBLE);
                        tvMarketPrice.setVisibility(View.GONE);
                        tvCnyPrice.setVisibility(View.VISIBLE);
                        etPrice.setHint("价格");
                        etNumber.setHint("数量");
                        tvUnit.setText(symbol);
                    }


                } else {
                    mSelectType = "1"; //市价
                    tvTransactionAmount.setText("--");
                    if (mKindType.equals("0")) { //买入
                       /* clPrice.setVisibility(View.VISIBLE);
                        tvCnyPrice.setVisibility(View.GONE);
                        etPrice.setHint("金额");*/
                        etNumber.setHint("金额");
                        tvUnit.setText(area);
                        clPrice.setVisibility(View.GONE);
                        tvMarketPrice.setVisibility(View.VISIBLE);
                        tvCnyPrice.setVisibility(View.INVISIBLE);

                    } else { //卖出
                        etNumber.setHint("数量");
                        tvUnit.setText(symbol);
                        clPrice.setVisibility(View.GONE);
                        tvMarketPrice.setVisibility(View.VISIBLE);
                        tvCnyPrice.setVisibility(View.INVISIBLE);
                    }

                }
                break;

        }
    }

    //深度 ws 连接监听
    private WsStatusListener wsStatusListener = new WsStatusListener() {
        @Override
        public void onMessage(String text) {
/*
            if (wsManager.getCurrentStatus() == WsStatus.CONNECTED) {
                Log.i("TAG", "深度 ws Message :" + text);
                try {
                    Map<String, Object> map = JSONUtil.getInstance().jsonMap(text);
                    String statusCode = map.get("status") + "";
                    //成功
                    if (!UtilTools.empty(statusCode) && statusCode.equals("1")) {
                        Map<String, Object> map1 = JSONUtil.getInstance().jsonMap(map.get("data") + "");

                        List<List<String>> mListBuy = JSONUtil.getInstance().jsonToListStr2(map1.get("buy") + "");
                        List<List<String>> mListSell = JSONUtil.getInstance().jsonToListStr2(map1.get("sell") + "");

                        // 设置深度信息
                        if (!UtilTools.empty(mListBuy) && mListBuy.size() > 0) {
                            mDataListBuy.clear();
                            for (List<String> strings : mListBuy) {
                                String price = strings.get(0);
                                String volume = strings.get(1);
                                decimalFormat.setRoundingMode(RoundingMode.CEILING);
                                decimalFormat.setGroupingUsed(false);
                                String format;
                                if (precision >= 0) {
                                    decimalFormat.setMaximumFractionDigits(precision);
                                    format = decimalFormat.format(Double.parseDouble(price));
                                } else {
                                    decimalFormat.setMaximumFractionDigits(0);
                                    format = BigDecimalUtils.mul(decimalFormat.format(Double.parseDouble(BigDecimalUtils.divide(price, RoundingMode.CEILING, 0 - precision).toString())), 0 - precision).toString();
                                }

                                if (mDataListBuy.size() > 0) {
                                    List<String> strings2 = mDataListBuy.get(mDataListBuy.size() - 1);
                                    String s = strings2.get(0);
                                    if (format.equals(s)) {
                                        strings2.add(1, BigDecimalUtils.add(strings2.get(1), volume).toString());
                                    } else {
                                        List<String> strings1 = new ArrayList<>();
                                        strings1.add(format);
                                        strings1.add(volume);
                                        mDataListBuy.add(strings1);
                                    }
                                } else {
                                    List<String> strings1 = new ArrayList<>();
                                    strings1.add(format);
                                    strings1.add(volume);
                                    mDataListBuy.add(strings1);
                                }
                            }
                            buyAdapter.setBuyTradeInfo(mDataListBuy, precision);
                        }

                        if (!UtilTools.empty(mListSell) && mListSell.size() > 0) {
                            mDataListSell.clear();
                            for (List<String> strings : mListSell) {
                                String price = strings.get(0);
                                String volume = strings.get(1);
                                decimalFormat.setRoundingMode(RoundingMode.CEILING);
                                decimalFormat.setGroupingUsed(false);
                                String format;
                                if (precision >= 0) {
                                    decimalFormat.setMaximumFractionDigits(precision);
                                    format = decimalFormat.format(Double.parseDouble(price));
                                } else {
                                    decimalFormat.setMaximumFractionDigits(0);
                                    format = BigDecimalUtils.mul(decimalFormat.format(Double.parseDouble(BigDecimalUtils.divide(price, RoundingMode.CEILING, 0 - precision).toString())), 0 - precision).toString();
                                }

                                if (mDataListSell.size() > 0) {
                                    List<String> strings2 = mDataListSell.get(mDataListSell.size() - 1);
                                    String s = strings2.get(0);
                                    if (format.equals(s)) {
                                        strings2.add(1, BigDecimalUtils.add(strings2.get(1), volume).toString());
                                    } else {
                                        List<String> strings1 = new ArrayList<>();
                                        strings1.add(format);
                                        strings1.add(volume);
                                        mDataListSell.add(strings1);
                                    }
                                } else {
                                    List<String> strings1 = new ArrayList<>();
                                    strings1.add(format);
                                    strings1.add(volume);
                                    mDataListSell.add(strings1);
                                }
                            }
                            sellAdapter.setSellTradeInfos(mDataListSell, precision);
                        }


                    }
                } catch (Exception e) {

                }
            }
*/
        }
    };


    public void restartWs(String selectArea, String selectSymbol, String buySell) {
       /* handler.removeCallbacks(runnable);
        setWebsocketListener();
        handler.post(runnable);*/

       /*area = selectArea;
       symbol = selectSymbol;
*/
       /* if (buySell.equals("1")){ //买入
            rbBuy.setChecked(true);
        }else {  //卖出
            rbSell.setChecked(true);
        }*/

    /*    //查询交易区列表项
        getAreaListItemAction();

        //查询委托单
        getEntrustListAction();

        //账户当前交易区交易币可用
        getCurAreaAccountAction();*/

        if (handler != null && cnyRunnable != null) {
            handler.post(cnyRunnable);
        }
        LogUtilDebug.i("show", "BB轮询数据,每次一进入界面开始轮询");


    }


    public void stopWs() {
       /* handler.removeCallbacks(runnable);
        if (wsManager != null){
            if (wsManager.getWebSocket() != null) {
                wsManager.getWebSocket().cancel();
            }
            wsManager.stopConnect();
        }
        */

        if (handler != null && cnyRunnable != null) {
            handler.removeCallbacks(cnyRunnable);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtilDebug.i("show", "刷新查询委托单");

        if (!buysell2.equals("0")) {
            buysell = buysell2;
        }

        //查询委托单
        getEntrustListAction();

        LogUtilDebug.i("show", "buysell&&&&&&&&&&&:" + buysell);
        if (buysell.equals("1")) { //买入
            rbBuy.setChecked(true);
            mKindType = "0";
            tvOperateCoin.setText("买入" + symbol);
            tvOperateCoin.setBackgroundResource(R.drawable.btn_next_green);
            rbNumber1.setBackgroundResource(R.drawable.selector_open_close_house2);
            rbNumber2.setBackgroundResource(R.drawable.selector_open_close_house2);
            rbNumber3.setBackgroundResource(R.drawable.selector_open_close_house2);
            rbNumber4.setBackgroundResource(R.drawable.selector_open_close_house2);

            if (mSelectType.equals("0")) { //限价
                clPrice.setVisibility(View.VISIBLE);
                tvMarketPrice.setVisibility(View.GONE);
                tvCnyPrice.setVisibility(View.VISIBLE);
                etPrice.setHint("价格");
            } else { //市价买
                           /* clPrice.setVisibility(View.VISIBLE);
                            tvCnyPrice.setVisibility(View.GONE);
                            etPrice.setHint("金额");*/
                etNumber.setHint("金额");
                tvUnit.setText(area);
                clPrice.setVisibility(View.GONE);
                tvMarketPrice.setVisibility(View.VISIBLE);
                tvCnyPrice.setVisibility(View.INVISIBLE);
            }

        } else {  //卖出
            rbSell.setChecked(true);
            mKindType = "1";
            tvOperateCoin.setText("卖出" + symbol);
            tvOperateCoin.setBackgroundResource(R.drawable.btn_next_red);
            rbNumber1.setBackgroundResource(R.drawable.selector_open_close_house3);
            rbNumber2.setBackgroundResource(R.drawable.selector_open_close_house3);
            rbNumber3.setBackgroundResource(R.drawable.selector_open_close_house3);
            rbNumber4.setBackgroundResource(R.drawable.selector_open_close_house3);

            if (mSelectType.equals("0")) { //限价
                clPrice.setVisibility(View.VISIBLE);
                tvMarketPrice.setVisibility(View.GONE);
                tvCnyPrice.setVisibility(View.VISIBLE);
                etPrice.setHint("价格");
            } else { //市价 卖
                etNumber.setHint("数量");
                tvUnit.setText(symbol);
                clPrice.setVisibility(View.GONE);
                tvMarketPrice.setVisibility(View.VISIBLE);
                tvCnyPrice.setVisibility(View.INVISIBLE);
            }
        }

        selectTv.setText(symbol + "/" + area);
        if (!UtilTools.empty(mPairPrecisionData) && !UtilTools.empty(mPairPrecisionData.get((symbol+area).toLowerCase()))){
            mPairPrecisionMap = (Map<String, Object>) mPairPrecisionData.get((symbol+area).toLowerCase());
            if (!UtilTools.empty(mPairPrecisionMap) && !UtilTools.empty(mPairPrecisionMap.get("price"))){
                String pricePrecisionStr = mPairPrecisionMap.get("price")+"";
                if (!UtilTools.empty(pricePrecisionStr)){
                    pricePrecision = Integer.parseInt(pricePrecisionStr);
                }else {
                    pricePrecision = 2;
                }
            }
        }
        areaTv.setText(area);
        tvUnit.setText(symbol);
        if (!UtilTools.empty(etNumber.getText()) && !UtilTools.empty(etPrice.getText())) {
            tvTransactionAmount.setText(UtilTools.getNormalMoney(Double.parseDouble(etNumber.getText().toString()) * Double.parseDouble(etPrice.getText().toString()) + "") + "  " + area);
        } else {
            tvTransactionAmount.setText("--");
        }
        buysell2 = "0";
    }

    @Override
    public void onPause() {
        LogUtilDebug.i("show", "Pause()");
        super.onPause();
        //wsManager.stopConnect();

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        ((OTCFragment) getParentFragment()).setUserVisibleHint(false);
        if (hidden) {
            LogUtilDebug.i("show", "BBfragment 可见");
            if (handler != null && cnyRunnable != null) {
                handler.removeCallbacks(cnyRunnable);
            }
        } else {
            LogUtilDebug.i("show", "BBfragment 不可见");
            ((OTCFragment) getParentFragment()).setUserVisibleHint(true);
            if (handler != null && cnyRunnable != null) {
                handler.post(cnyRunnable);
            }
        }


    /*    handler.removeCallbacks(runnable);
        if (getUserVisibleHint()) {
            setWebsocketListener();
            handler.post(runnable);
        } else {
            if (wsManager.getWebSocket() != null) {
                wsManager.getWebSocket().cancel();
            }
            wsManager.stopConnect();
        }*/


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == QUEST_CODE) {
                LogUtilDebug.i("show", "BBTradeFragment onActivityResult()");
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    buysell2 = bundle.getString("buySell");
                    LogUtilDebug.i("show", "buysell2>>>" + buysell2);
                    //buysell = buySell2;
                  /*  if (buySell2.equals("1")){ //买入
                        rbBuy.setChecked(true);
                    }else {  //卖出
                        rbSell.setChecked(true);
                    }*/
                }

            }
        }
    }



}
