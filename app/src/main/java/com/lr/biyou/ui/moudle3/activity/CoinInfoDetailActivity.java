package com.lr.biyou.ui.moudle3.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineChartAdapter;
import com.github.fujianlian.klinechart.KLineChartView;
import com.github.fujianlian.klinechart.draw.Status;
import com.github.fujianlian.klinechart.entity.KLineEntity;
import com.github.fujianlian.klinechart.formatter.DateMiddleTimeFormatter;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.db.IsCollectBean;
import com.lr.biyou.mywidget.dialog.IndexChooseDialog;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.utils.websocketparams.CoinCoinKlineParams;
import com.lr.biyou.utils.websocketparams.CurrenctPriceWsParams;
import com.lr.biyou.utils.websocketparams.PongParams;
import com.lr.biyou.utils.websocketparams.QuotesDataParams;
import com.lr.biyou.utils.websocketparams.ReqKlineDataParams;
import com.lr.biyou.utils.websocketparams.SubKlineDataParams;
import com.lr.biyou.utils.websocketparams.UnsubKlineDataParams;
import com.wanou.framelibrary.bean.SimpleResponse;
import com.wanou.framelibrary.okgoutil.OkGoUtils;
import com.wanou.framelibrary.okgoutil.websocket.WsManager;
import com.wanou.framelibrary.okgoutil.websocket.WsStatus;
import com.wanou.framelibrary.okgoutil.websocket.listener.WsStatusListener;
import com.wanou.framelibrary.utils.GsonUtils;
import com.wanou.framelibrary.utils.UiTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

public class CoinInfoDetailActivity extends BasicActivity implements View.OnClickListener {
    private ImageView ivBack, ivKlineSet, ivSelf;
    private Toolbar toolBar;
    private TextView tvCoinName, tvCoinPrice, tvCnyPrice, tvUpRatio, tvHighPrice, tvLowPrice, tv24H,
            tvBuyIn, tvSellOut;
    private DrawerLayout drawerLayout;
    private TabLayout tlLeftCoinTitle;
    private ActionBarDrawerToggle toggle;
    private List<KLineEntity> kLineDataList = new ArrayList<>();
    private CoinCoinKlineParams coinCoinKlineParams = new CoinCoinKlineParams();
    private CurrenctPriceWsParams currenctPriceWsParams = new CurrenctPriceWsParams();
    private TabLayout tlTimeChoose;
    private KLineChartView kLineChartView;
    private String symbol, area,period="60";
    private String[] timeChoose, klineTimeParams, timeChooseParams;
    private KLineChartAdapter kLineChartAdapter;
    private Handler handler = new Handler();
    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();
    private QuotesDataParams homeDataParams = new QuotesDataParams();
    private WsManager wsManage3, wsManage2, wsManage1;
    private IndexChooseDialog indexChooseDialog;
    // 主图指标下标
    private int mainIndex = 0;
    // 副图指标下标
    private int subIndex = -1;
    private TextView tvDataNotice;
    private boolean isFirstLoad = true;
    private LinearLayout llOperate;
    private ScrollView nestedScrollView;
    private boolean isConnect;
    private boolean isFirst = true;
    private PongParams pongParams = new PongParams();
    private SubKlineDataParams subKlineDataParams = new SubKlineDataParams();
    private SubKlineDataParams subKlineDataParams1 = new SubKlineDataParams();
    private ReqKlineDataParams reqKlineDataParams = new ReqKlineDataParams();
    private boolean isLogin = false;
    private String mRequestTag = "";
    protected Bundle mBundle;


    private  String areaID = "";
    private  String areaOptional = "";


    private  String colorType ="0";



   /* private Runnable currentPriceRunnable = new Runnable() {
        @Override
        public void run() {
            // 获取币当前价
            Map<String,String> map = new HashMap<>();
            map.put("area","USDT");
            map.put("method","coinCurrent");
            map.put("symbol","BTC");
            wsManage1.sendMessage(JSONUtil.getInstance().objectToJson(map));
            handler1.postDelayed(this, MbsConstans.SECOND_TIME_500);
        }
    };*/
    /*private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 获取平台币K线
            //Param:{"area":"USDT","method":"k_coinChart","range":"60","symbol":"BTC"}
            Map<String,String> map = new HashMap<>();
            map.put("area","USDT");
            map.put("method","k_coinChart");
            map.put("range","60");
            map.put("symbol","BTC");
            wsManage2.sendMessage(JSONUtil.getInstance().objectToJson(map));
            handler.postDelayed(this, MbsConstans.SECOND_TIME_500);
        }
    };*/


    //当前界面  不支持滑动返回
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
    @Override
    public void setBarTextColor(){
        StatusBarUtil.setDarkMode(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_coin_info_detail;
    }

    @Override
    public void init() {
        bundle = getIntent().getBundleExtra("bundle");
        //UI 初始化
        setStatusBar();
        initView();
        initClickListener();
        initData();

    }


    //HTTP请求  轮询
    private Runnable cnyRunnable = new Runnable() {
        @Override
        public void run() {
            // 获取币当前价
            getCurrentPriceAction();

            //获取K线数据
            getKLineAction();
            handler2.postDelayed(this, MbsConstans.SECOND_TIME_5000);
        }
    };

    private void getCurrentPriceAction() {
        mRequestTag = MethodUrl.CURRENT_PRICE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(CoinInfoDetailActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("area",area);
        map.put("symbol",symbol);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CURRENT_PRICE, map);
    }

    private void getKLineAction() {
        mRequestTag = MethodUrl.K_LINE;
        Map<String, String> map = new HashMap<>();
       /* if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(CoinInfoDetailActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);*/
        map.put("symbol",symbol.toLowerCase()+"_"+area.toLowerCase());
        map.put("period",period);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.K_LINE, map);
    }



    protected void initView() {
        ivBack = findViewById(R.id.ivBack);
        ivSelf = findViewById(R.id.ivSelf);
        ivKlineSet = findViewById(R.id.ivKlineSet);
        toolBar = findViewById(R.id.toolBar);
        tvCoinName = findViewById(R.id.tvCoinName);
        tvCoinPrice = findViewById(R.id.tvCoinPrice);
        tvCnyPrice = findViewById(R.id.tvCnyPrice);
        tvUpRatio = findViewById(R.id.tvUpRatio);
        tvHighPrice = findViewById(R.id.tvHighPrice);
        tvLowPrice = findViewById(R.id.tvLowPrice);
        tv24H = findViewById(R.id.tv24H);
        llOperate = findViewById(R.id.llOperate);
        drawerLayout = findViewById(R.id.drawer_layout);
        tlLeftCoinTitle = findViewById(R.id.tlLeftCoinTitle);
        tlTimeChoose = findViewById(R.id.tlTimeChoose);
        tvBuyIn = findViewById(R.id.tvBuyIn);
        tvSellOut = findViewById(R.id.tvSellOut);
        tvDataNotice = findViewById(R.id.tvDataNotice);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        kLineChartView = findViewById(R.id.kLineChartView);
        //0 红跌绿涨   1红涨绿跌
        colorType =  SPUtils.get(CoinInfoDetailActivity.this, MbsConstans.SharedInfoConstans.COLOR_TYPE,"0").toString();
        kLineChartView.setcolor(colorType);
        viewGone(tvDataNotice);
    }

    protected void initClickListener() {
        ivBack.setOnClickListener(this);
        ivKlineSet.setOnClickListener(this);
        tvBuyIn.setOnClickListener(this);
        ivSelf.setOnClickListener(this);
        tvSellOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvBuyIn: //买入
                //Eventbus  发送事件
               /* MessageEvent event = new MessageEvent();
                event.setType(MbsConstans.MessageEventType.UPDATE_BBFRAGMENT);
                Map<Object, Object> map = new HashMap<>();
                map.put("area",area);
                map.put("symbol",symbol);
                event.setMessage(map);
                EventBus.getDefault().post(event);
*/

                intent.putExtra("buySell", "1");
                //intent.putExtra("area", area);
                //intent.putExtra("symbol", symbol);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tvSellOut://卖出
                intent.putExtra("buySell", "2");
                //intent.putExtra("area", area);
                //intent.putExtra("symbol", symbol);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.ivKlineSet:
                // 指数选择
                indexChooseDialog.getDialog();
                indexChooseDialog.setConfirmClickListener(new IndexChooseDialog.ConfirmClickListener() {
                    @Override
                    public void onMaClick() {
                        if (mainIndex != 0) {
                            kLineChartView.hideSelectData();
                            mainIndex = 0;
                            kLineChartView.changeMainDrawType(Status.MA);
                        }
                    }

                    @Override
                    public void onBollClick() {
                        mainIndex = 1;
                        kLineChartView.changeMainDrawType(Status.BOLL);
                    }

                    @Override
                    public void onMainHideClick() {
                        kLineChartView.hideSelectData();
                        mainIndex = -1;
                        kLineChartView.changeMainDrawType(Status.NONE);
                    }

                    @Override
                    public void onMacdClick() {
                        kLineChartView.hideSelectData();
                        subIndex = 0;
                        kLineChartView.setChildDraw(subIndex);
                    }

                    @Override
                    public void onKDJClick() {
                        kLineChartView.hideSelectData();
                        subIndex = 1;
                        kLineChartView.setChildDraw(subIndex);
                    }

                    @Override
                    public void onRsiClick() {
                        kLineChartView.hideSelectData();
                        subIndex = 2;
                        kLineChartView.setChildDraw(subIndex);
                    }

                    @Override
                    public void onWrClick() {
                        kLineChartView.hideSelectData();
                        subIndex = 3;
                        kLineChartView.setChildDraw(subIndex);
                    }

                    @Override
                    public void onSubHideClick() {
                        kLineChartView.hideSelectData();
                        subIndex = -1;
                        kLineChartView.hideChildDraw();
                    }

                    @Override
                    public void onFenClick() {
                        kLineChartView.hideSelectData();
                        kLineChartView.setMainDrawLine(true);
                    }

                    @Override
                    public void onKlineClick() {
                        kLineChartView.hideSelectData();
                        kLineChartView.setMainDrawLine(false);
                    }
                });
                break;
            case R.id.ivSelf:
                setSelfAction();
                break;
            default:
        }
    }

    private void setSelfAction() {
        mRequestTag = MethodUrl.COIN_ADD_CANCEL;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(CoinInfoDetailActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("id",areaID);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.COIN_ADD_CANCEL, map);
    }

    protected void initData() {
        wsManage1 = BasicApplication.getWsManager1();
        wsManage2 = BasicApplication.getWsManager2();
        wsManage3 = BasicApplication.getWsManager3();
        indexChooseDialog = new IndexChooseDialog(CoinInfoDetailActivity.this);
        String[] stringArray = getResources().getStringArray(R.array.left_tab_view);
        for (String tabContent : stringArray) {
            tlLeftCoinTitle.addTab(tlLeftCoinTitle.newTab().setText(tabContent));
        }
        initLeftView();
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            symbol = mBundle.getString("symbol", "");
            area = mBundle.getString("area", "");
            cnyRatio = mBundle.getString("cnyRatio", "");
            coinCoinKlineParams.area = area;
            coinCoinKlineParams.symbol = symbol;
            currenctPriceWsParams.setArea(area);
            currenctPriceWsParams.setSymbol(symbol);
            String from = mBundle.getString("from", "");

            if (from.equals("1")) {
                viewGone(llOperate);
            }
            tvCoinName.setText(symbol + "/" + area);
            // 分时图选择器
            initTimeChoose();
        }
        // 初始化chartview
        initChartView();
    }





    protected void setStatusBar() {
        //StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        ImmersionBar.with(this)
                .statusBarColor(R.color.colorBlack2)     //状态栏颜色，不写默认透明色
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
//                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                .removeSupportAllView() //移除全部view支持
                .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
//                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
//                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
                .addTag("tag")  //给以上设置的参数打标记
                .getTag("tag")  //根据tag获得沉浸式参数
                .init();  //必须调用方可沉浸式
    }




    private WsStatusListener currenctPriceListener = new WsStatusListener() {
        @Override
        public void onOpen(Response response) {
            super.onOpen(response);
//            handler1.post(currentPriceRunnable);
        }

        @Override
        public void onMessage(String text) {
            if (wsManage1.getCurrentStatus() == WsStatus.CONNECTED) {
                LogUtilDebug.i("show","currentPrice data:"+text);

                Map<String,Object> map = JSONUtil.getInstance().jsonMap(text);
                if (!UtilTools.empty(map)){
                    if ((map.get("status")+"").equals("1")){
                        Map<String,Object> mapData= (Map<String, Object>) map.get("data");
                        if (!UtilTools.empty(mapData)){
                            // 当前价
                            tvCoinPrice.setText(UtilTools.formatNumber(mapData.get("price")+"", "#.########"));
                            // 对应cny价格
                            tvCnyPrice.setText(getResources().getString(R.string.defaultCny).replace("%S", UtilTools.formatNumber(mapData.get("cny_number")+"", "#.##")));
                            // 高
                            tvHighPrice.setText(UtilTools.formatNumber(mapData.get("high")+"", "#.########"));
                            // 低
                            tvLowPrice.setText(UtilTools.formatNumber(mapData.get("low")+"", "#.########"));
                            // 成交量
                            tv24H.setText(UtilTools.formatNumber(map.get("volume")+"", "0"));

                            //0 红跌绿涨   1红涨绿跌
                            //String colorType =  SPUtils.get(CoinInfoDetailActivity.this, MbsConstans.SharedInfoConstans.COLOR_TYPE,"0").toString();
                            // 涨跌幅
                            if ((mapData.get("ratio")+"").contains("-")) {
                                if (colorType.equals("0")){
                                    tvCoinPrice.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorRed));
                                    tvUpRatio.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorRed));
                                }else {
                                    tvCoinPrice.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorGreen));
                                    tvUpRatio.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorGreen));
                                }

                                tvUpRatio.setText(UtilTools.formatNumber(mapData.get("ratio")+"", "#.##") + "%");
                            } else {
                                if (colorType.equals("0")){
                                    tvCoinPrice.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorGreen));
                                    tvUpRatio.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorGreen));
                                }else {
                                    tvCoinPrice.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorRed));
                                    tvUpRatio.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorRed));
                                }
                                tvUpRatio.setText("+" + UtilTools.formatNumber(mapData.get("ratio")+"", "#.##") + "%");
                            }
                        }

                    }
                }

            }
        }
    };
    private WsStatusListener platformCoinKline = new WsStatusListener() {
        @Override
        public void onMessage(String text) {
            if (wsManage2.getCurrentStatus() == WsStatus.CONNECTED) {
                LogUtilDebug.i("show","Kline data:"+text);
                Map<String,Object> map = JSONUtil.getInstance().jsonMap(text);
                if (!UtilTools.empty(map)){
                    if ((map.get("status")+"").equals("1")){
                        List<List<String>> klineData = JSONUtil.getInstance().jsonToListStr2(map.get("data")+"");
                        kLineDataList.clear();
                        for (List<String> klineDataContent : klineData) {
                            KLineEntity kLineEntity = new KLineEntity();
                            // 时间
                            kLineEntity.id = Long.parseLong(klineDataContent.get(0));
                            // 开盘价
                            kLineEntity.open = Float.parseFloat(klineDataContent.get(1));
                            // 最高
                            kLineEntity.high = Float.parseFloat(klineDataContent.get(2));
                            // 最低
                            kLineEntity.low = Float.parseFloat(klineDataContent.get(3));
                            // 收盘
                            kLineEntity.close = Float.parseFloat(klineDataContent.get(4));
                            // 交易量
                            kLineEntity.amount = Float.parseFloat(klineDataContent.get(5));
                            kLineDataList.add(kLineEntity);
                        }
                        DataHelper.calculate(kLineDataList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFirstLoad) {
                                    kLineChartView.startAnimation();
                                    isFirstLoad = false;
                                    kLineChartAdapter.clearData();
                                }
                                kLineChartAdapter.addFooterData(kLineDataList);
                                kLineChartAdapter.notifyDataSetChanged();
                                kLineChartView.refreshComplete();
                                kLineChartView.refreshEnd();
                            }
                        });

                    }
                }

            }
        }
    };
    private Bundle bundle = new Bundle();
    private UnsubKlineDataParams unsubKlineDataParams = new UnsubKlineDataParams();
    private String cnyRatio;
  /*  private WsStatusListener klineCurrentPriceListener = new WsStatusListener() {
        @Override
        public void onOpen(Response response) {
            super.onOpen(response);
            // 连接成功, 开启订阅
//            sub24HKlineData();
            subKlineData();
        }

        @Override
        public void onMessage(ByteString bytes) {
            super.onMessage(bytes);
            if (wsManage3.getCurrentStatus() == WsStatus.CONNECTED) {
                try {
                    String result = GzipUtils.unCompress(bytes.toByteArray());
                    Log.i("TAG","货币ws 返回数据："+result);
                    if (result.contains("\"ping\":")) {
                        PingParams pingParams = GsonUtils.fromJson(result, PingParams.class);
                        pongParams.pong = pingParams.ping;
                        wsManage3.sendMessage(GsonUtils.toJson(pongParams));
                    } else if (result.contains("\"subbed\":")) {
                        SubSuccessBean subSuccessBean = GsonUtils.fromJson(result, SubSuccessBean.class);
                        if (subSuccessBean.getSubbed().equals(subKlineDataParams.sub)) {
                            reqKlineDataParams.req = subKlineDataParams.sub;
                            wsManage3.sendMessage(GsonUtils.toJson(reqKlineDataParams));
                        }
                    } else if (result.contains("\"rep\":")) {
                        ReqKlineDataBean reqKlineDataBean = GsonUtils.fromJson(result, ReqKlineDataBean.class);
                        if (reqKlineDataBean.getRep().equals(reqKlineDataParams.req)) {
                            // 数据库处理
                            //ObjectBoxManager.putDataCache(reqKlineDataParams.req, result);
                            List<KLineEntity> tick = reqKlineDataBean.getTick();
                            DataHelper.calculate(tick);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isFirstLoad) {
                                        kLineChartView.startAnimation();
                                        isFirstLoad = false;
                                    }
                                    kLineChartAdapter.addFooterData(tick);
                                    kLineChartAdapter.notifyDataSetChanged();
                                    kLineChartView.refreshEnd();
                                }
                            });
                        }
                    } else if (result.contains("\"ch\":") && result.contains(subKlineDataParams.sub)) {
                        CurrentPriceBean currentPriceBean = GsonUtils.fromJson(result, CurrentPriceBean.class);
                        KLineEntity kLineEntity = currentPriceBean.getTick();
                        tvCoinPrice.setText(kLineEntity.getClosePrice() + "");
                        if (UiTools.noEmpty(cnyRatio)) {
                            tvCnyPrice.setText(UiTools.getString(R.string.defaultCny5)
                                    .replace("0.00", UiTools.formatNumber(kLineEntity.getClosePrice() * Double.parseDouble(cnyRatio), "0.00")));
                        } else {
                            tvCnyPrice.setText("");
                        }
//                        //数据库管理
//                        DataCache dataCache = ObjectBoxManager.queryData(reqKlineDataParams.req);
//                        if (dataCache != null) {
//                            String resultContent = dataCache.getResultContent();
//                            ReqKlineDataBean reqKlineDataBean = GsonUtils.fromJson(resultContent, ReqKlineDataBean.class);
//                            List<KLineEntity> tick = reqKlineDataBean.getTick();
//                            if (tick.get(tick.size() - 1).id == kLineEntity.id) {
//                                tick.set(tick.size() - 1, kLineEntity);
//                            } else if (tick.get(tick.size() - 1).id < kLineEntity.id) {
//                                tick.add(kLineEntity);
//                            }
//                            DataHelper.calculate(tick);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (isFirstLoad) {
//                                        kLineChartView.startAnimation();
//                                        isFirstLoad = false;
//                                    }
//                                    kLineChartAdapter.addFooterData(tick);
//                                    kLineChartAdapter.notifyDataSetChanged();
//                                    kLineChartView.refreshEnd();
//                                }
//                            });
//                        }
                    } else if (result.contains("\"ch\":") && result.contains(subKlineDataParams1.sub)) {
//                        Trade24HDataBean trade24HDataBean = GsonUtils.fromJson(result, Trade24HDataBean.class);
//                        Trade24HDataBean.TickBean tick = trade24HDataBean.getTick();
//                        tvCoinPrice.setText(tick.getClose() + "");
//                        tvHighPrice.setText(tick.getHigh() + "");
//                        tvLowPrice.setText(tick.getLow() + "");
//                        tv24H.setText(UiTools.formatDecimal(tick.getAmount(), 0));
//                        tvUpRatio.setText(UiTools.formatNumber((tick.getClose() - tick.getOpen()) / tick.getOpen(), "0.00%"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

*/



    protected void getNetStatus(boolean isConnect) {
        this.isConnect = isConnect;
        // 初次进入时会调用一次, 之后只有网络状态变化时调用
        if (isConnect) {
            // 初次进入联网
            // 如果不是第一次,调用resume方法重新请求数据
            if (!isFirst) {
                onResume();
            }
        } else {
            // 初次进入未联网
            // 如果不是第一次,调用resume方法重新请求数据
            if (!isFirst) {
//                handler1.removeCallbacks(runnable1);
                OkGoUtils.cancelAll();
            }
        }
        isFirst = false;
    }

    private void initLeftView() {
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        toolBar.inflateMenu(R.menu.meun_coin_info);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.self:
                        UiTools.showToast("自选");
                        menuItem.setChecked(!menuItem.isChecked());
                        if (menuItem.isChecked()) {
                            menuItem.setIcon(R.drawable.icon_xing_s);
                        } else {
                            menuItem.setIcon(R.drawable.icon_xing);
                        }
                        Log.e("tag", menuItem.isChecked() + "");
                        break;
                    case R.id.fullScreen:
                        UiTools.showToast("全屏");
                        break;
                    case R.id.share:
                        UiTools.showToast("分享");
                        break;
                    case R.id.indexChoose:
                        // 指数选择
                        indexChooseDialog.getDialog();
                        indexChooseDialog.setConfirmClickListener(new IndexChooseDialog.ConfirmClickListener() {
                            @Override
                            public void onMaClick() {
                                if (mainIndex != 0) {
                                    kLineChartView.hideSelectData();
                                    mainIndex = 0;
                                    kLineChartView.changeMainDrawType(Status.MA);
                                }
                            }

                            @Override
                            public void onBollClick() {
                                mainIndex = 1;
                                kLineChartView.changeMainDrawType(Status.BOLL);
                            }

                            @Override
                            public void onMainHideClick() {
                                kLineChartView.hideSelectData();
                                mainIndex = -1;
                                kLineChartView.changeMainDrawType(Status.NONE);
                            }

                            @Override
                            public void onMacdClick() {
                                kLineChartView.hideSelectData();
                                subIndex = 0;
                                kLineChartView.setChildDraw(subIndex);
                            }

                            @Override
                            public void onKDJClick() {
                                kLineChartView.hideSelectData();
                                subIndex = 1;
                                kLineChartView.setChildDraw(subIndex);
                            }

                            @Override
                            public void onRsiClick() {
                                kLineChartView.hideSelectData();
                                subIndex = 2;
                                kLineChartView.setChildDraw(subIndex);
                            }

                            @Override
                            public void onWrClick() {
                                kLineChartView.hideSelectData();
                                subIndex = 3;
                                kLineChartView.setChildDraw(subIndex);
                            }

                            @Override
                            public void onSubHideClick() {
                                kLineChartView.hideSelectData();
                                subIndex = -1;
                                kLineChartView.hideChildDraw();
                            }

                            @Override
                            public void onFenClick() {
                                kLineChartView.hideSelectData();
                                kLineChartView.setMainDrawLine(true);
                            }

                            @Override
                            public void onKlineClick() {
                                kLineChartView.hideSelectData();
                                kLineChartView.setMainDrawLine(false);
                            }
                        });
                        break;
                    default:
                }
                return false;
            }
        });
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        toggle.setDrawerIndicatorEnabled(false);
//        toggle.setHomeAsUpIndicator(R.drawable.trade_drawer_open_kline_page);
//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ((System.currentTimeMillis() - mTime) > 1000) {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                    mTime = System.currentTimeMillis();
//                }
//            }
//        });
    }

    private void initTimeChoose() {
        timeChoose = getResources().getStringArray(R.array.timeChoose);
        timeChooseParams = getResources().getStringArray(R.array.timeChooseParams);
        klineTimeParams = getResources().getStringArray(R.array.klineTimeParams);
        for (String tabContent : timeChoose) {
            tlTimeChoose.addTab(tlTimeChoose.newTab().setText(tabContent));
        }
        tlTimeChoose.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tlTimeChoose.getSelectedTabPosition()){
                    case 0:
                        period = "60";
                        break;
                    case 1:
                        period = "300";
                        break;
                    case 2:
                        period = "900";
                        break;
                    case 3:
                        period = "1800";
                        break;
                    case 4:
                        period = "3600";
                        break;
                    case 5:
                        period = "86400";
                        break;
                    case 6:
                        period = "604800";
                        break;
                }

                getKLineAction();

                isFirstLoad = true;
                // 取消订阅
                //unSubKlineData();
                kLineChartAdapter.clearData();
                coinCoinKlineParams.range = klineTimeParams[tlTimeChoose.getSelectedTabPosition()];
                // K线订阅
                //subKlineData();
//                DataCache dataCache = ObjectBoxManager.queryData(subKlineDataParams.sub);
//                if (dataCache != null) {
//                    String resultContent = dataCache.getResultContent();
//                    ReqKlineDataBean reqKlineDataBean = GsonUtils.fromJson(resultContent, ReqKlineDataBean.class);
//                    List<KLineEntity> tick = reqKlineDataBean.getTick();
//                    DataHelper.calculate(tick);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isFirstLoad) {
//                                kLineChartAdapter.clearData();
//                                kLineChartView.startAnimation();
//                                isFirstLoad = false;
//                            }
//                            kLineChartAdapter.addFooterData(tick);
//                            kLineChartAdapter.notifyDataSetChanged();
//                            kLineChartView.refreshEnd();
//                        }
//                    });
//                } else {
//                    kLineChartView.justShowLoading();
//                }
                setWebsocketListener();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void subKlineData() {
        //Params:{"id":1567146933436,"sub":"market.btcusdt.kline.5min"}
        Log.i("TAG","货币websocket连接请求参数"+GsonUtils.toJson(subKlineDataParams));
        Map<String,Object> map = new HashMap<>();
        map.put("id","1567146933436");
        map.put("sub","market.btcusdt.kline.5min");
        wsManage2.sendMessage(JSONUtil.getInstance().objectToJson(map));
    }

    private void unSubKlineData() {
        // 切换K线周期或后台运行时, 取消订阅
        //{"id":1567147137299,"unsub":"market.btcusdt.kline.5min"}
        Log.i("TAG","货币websocket取消订阅连接请求参数"+GsonUtils.toJson(unsubKlineDataParams));
        Map<String,Object> map = new HashMap<>();
        map.put("id","1567147137299");
        map.put("unsub","market.btcusdt.kline.5min");
        wsManage2.sendMessage(JSONUtil.getInstance().objectToJson(map));
    }

    private void setWebsocketListener() {
        // 如果是火币币,连接火币接口
       /* if (getCoinObject() != null) {
            if (!wsManage3.isWsConnected()) {
                wsManage3.startConnect();
            }
            wsManage3.setWsStatusListener(klineCurrentPriceListener);
        } else {
            if (!wsManage2.isWsConnected()) {
                wsManage2.startConnect();
            }
            wsManage2.setWsStatusListener(platformCoinKline);
        }*/

        if (!wsManage2.isWsConnected()) {
            wsManage2.startConnect();
        }
        wsManage2.setWsStatusListener(platformCoinKline);
        if (!wsManage1.isWsConnected()) {
            wsManage1.startConnect();
        }
        wsManage1.setWsStatusListener(currenctPriceListener);

    }

    private JSONObject getCoinObject() {
//        try {
//            DataCache coinPrecision = ObjectBoxManager.queryData("CoinPrecision");
//            JSONObject jsonObject = new JSONObject(coinPrecision.getResultContent());
//            JSONObject jsonObject1 = jsonObject.optJSONObject("nameValuePairs");
//            return jsonObject1.optJSONObject(symbol.toLowerCase() + area.toLowerCase());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        return  null;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initChartView() {
        kLineChartAdapter = new KLineChartAdapter();
        kLineChartView.setAdapter(kLineChartAdapter);
        kLineChartView.setDateTimeFormatter(new DateMiddleTimeFormatter());
        kLineChartView.setGridColumns(4);
        kLineChartView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() == 2) {
                    nestedScrollView.requestDisallowInterceptTouchEvent(true);
                } else {
                    nestedScrollView.requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
        kLineChartView.justShowLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
//                case AppConstant.LOGIN_INFO:
//                    isLogin = true;
//                    mPresenter.getIsSelf(symbol + "/" + area);
//                    break;
//                default:
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //handler1.removeCallbacks(currentPriceRunnable);
        //handler.removeCallbacks(runnable);
        handler2.removeCallbacks(cnyRunnable);
        wsManage2.stopConnect();
        //unSubKlineData();
//        unSub24HKlineData();
        wsManage1.stopConnect();
        if (wsManage1.getWebSocket() != null) {
            wsManage1.getWebSocket().cancel();
        }

        if (wsManage2.getWebSocket() != null){
            wsManage2.getWebSocket().cancel();
        }

//        selfListSocket.stopConnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* setWebsocketListener();
        if (getCoinObject() != null) {
            // 开启K线订阅
            subKlineData();
//            sub24HKlineData();
        } else {
            handler.post(runnable);
        }*/
        //handler1.post(currentPriceRunnable);
        //http 请求
        //mPresenter.getIsSelf(symbol + "/" + area);
        handler2.post(cnyRunnable);
//        homeDataParams.collect = "yes";
//        homeDataParams.token = (String) SpUtils.get("token", "");
//        homeDataParams.area = "";
//        selfListSocket.sendMessage(GsonUtils.toJson(homeDataParams));
    }

    private void unSub24HKlineData() {
        // 切换K线周期或后台运行时, 取消订阅
        unsubKlineDataParams.unsub = subKlineDataParams1.sub;
        wsManage3.sendMessage(GsonUtils.toJson(unsubKlineDataParams));
    }

    private void sub24HKlineData() {
        subKlineDataParams1.sub = "market." + symbol.toLowerCase() + area.toLowerCase() + ".detail";
        wsManage3.sendMessage(GsonUtils.toJson(subKlineDataParams1));
    }

    public void setAddSuccess() {
        ivSelf.setSelected(!ivSelf.isSelected());
    }

    public void setAddError(SimpleResponse simpleResponse) {

    }

    public void setIsSelf(IsCollectBean isCollectBean) {
        isLogin = true;
        ivSelf.setSelected(isCollectBean.getIfCollect() == 1);
    }

    public void setIsSelfError(SimpleResponse simpleResponse) {
        if (simpleResponse != null) {
            if (simpleResponse.code == -1) {
                isLogin = false;

            }
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
        switch (mType){
            case MethodUrl.CURRENT_PRICE:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data")+"")){
                            Map<String,Object> mapData = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(mapData)){
                                // 当前价
                                tvCoinPrice.setText(UtilTools.formatNumber(mapData.get("price")+"", "#.########"));
                                // 对应cny价格
                                tvCnyPrice.setText(getResources().getString(R.string.defaultCny).replace("%S", UtilTools.formatNumber(mapData.get("cny_number")+"", "#.##")));
                                //tvCoinPrice.setText(UtilTools.formatDecimal(mapData.get("price")+"",8));
                                //tvCnyPrice.setText("≈"+UtilTools.formatDecimal(mapData.get("cny_number")+"",2)+"CNY");
                                //tvCnyPrice.setText(getResources().getString(R.string.defaultCny).replace("%S", UtilTools.formatNumber(mapData.get("cny_number")+"", "#.##")));
                                //0 红跌绿涨   1红涨绿跌
                                String colorType =  SPUtils.get(CoinInfoDetailActivity.this, MbsConstans.SharedInfoConstans.COLOR_TYPE,"0").toString();
                                if ((mapData.get("ratio")+"").contains("-")) { //跌
                                    if (colorType.equals("0")){
                                        tvCoinPrice.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorRed));
                                        tvUpRatio.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorRed));
                                    }else {
                                        tvCoinPrice.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorGreen));
                                        tvUpRatio.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorGreen));
                                    }

                                    tvUpRatio.setText(UtilTools.formatNumber(mapData.get("ratio")+"", "#.##") + "%");
                                } else {
                                    if (colorType.equals("0")){
                                        tvCoinPrice.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorGreen));
                                        tvUpRatio.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorGreen));
                                    }else {
                                        tvCoinPrice.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorRed));
                                        tvUpRatio.setTextColor(ContextCompat.getColor(CoinInfoDetailActivity.this,R.color.colorRed));
                                    }
                                    tvUpRatio.setText("+" + UtilTools.formatNumber(mapData.get("ratio")+"", "#.##") + "%");
                                }
                                // 高
                                tvHighPrice.setText(UtilTools.formatNumber(mapData.get("high")+"", "#.########"));
                                // 低
                                tvLowPrice.setText(UtilTools.formatNumber(mapData.get("low")+"", "#.########"));
                                // 成交量
                                tv24H.setText(UtilTools.formatNumber(mapData.get("volume")+"", "0"));

                                areaID = mapData.get("id")+"";
                                areaOptional = mapData.get("optional")+"";
                                if (!UtilTools.empty(areaOptional) && areaOptional.equals("1")){
                                    //已经自选
                                    ivSelf.setImageResource(R.drawable.icon_xing_s);
                                }else {
                                    ivSelf.setImageResource(R.drawable.icon_xing);
                                }

                            }
                        }


                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(CoinInfoDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case MethodUrl.K_LINE:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data")+"")){
                            List<List<String>> klineData = JSONUtil.getInstance().jsonToListStr2(tData.get("data")+"");
                            kLineDataList.clear();
                            for (List<String> klineDataContent : klineData) {
                                KLineEntity kLineEntity = new KLineEntity();
                                // 时间
                                kLineEntity.id = Long.parseLong(klineDataContent.get(0));
                                // 开盘价
                                kLineEntity.open = Float.parseFloat(klineDataContent.get(1));
                                // 最高
                                kLineEntity.high = Float.parseFloat(klineDataContent.get(2));
                                // 最低
                                kLineEntity.low = Float.parseFloat(klineDataContent.get(3));
                                // 收盘
                                kLineEntity.close = Float.parseFloat(klineDataContent.get(4));
                                // 交易量
                                kLineEntity.amount = Float.parseFloat(klineDataContent.get(5));
                                kLineDataList.add(kLineEntity);
                            }
                            DataHelper.calculate(kLineDataList);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isFirstLoad) {
                                        kLineChartView.startAnimation();
                                        isFirstLoad = false;
                                        kLineChartAdapter.clearData();
                                    }
                                    kLineChartAdapter.addFooterData(kLineDataList);
                                    kLineChartAdapter.notifyDataSetChanged();
                                    kLineChartView.refreshComplete();
                                    kLineChartView.refreshEnd();
                                }
                            });
                        }

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(CoinInfoDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case MethodUrl.COIN_ADD_CANCEL:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        getCurrentPriceAction();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(CoinInfoDetailActivity.this, LoginActivity.class);
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

    protected void viewGone(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setVisibility(View.GONE);
            }
        }
    }



}
