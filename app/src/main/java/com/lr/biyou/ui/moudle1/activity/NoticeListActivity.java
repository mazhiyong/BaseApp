package com.lr.biyou.ui.moudle1.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.dialog.utils.CornerUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.ui.moudle1.adapter.NoticeListAdapter;
import com.lr.biyou.ui.temporary.adapter.TradeDialogAdapter;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 公告消息中心  界面
 */
public class NoticeListActivity extends BasicActivity implements RequestView,ReLoadingData,SelectBackListener{

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
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    private String mRequestTag ="";

    private String mStartTime="";
    private String mEndTime = "";
    private String mBusiType = "";

    private String mSelectStartTime = "";
    private String mSelectEndTime = "";
    private String mSelectType = "";


    private NoticeListAdapter mListAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;

    private AnimUtil mAnimUtil;

    @Override
    public int getContentView() {
        return R.layout.activity_trade_list;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        mAnimUtil = new AnimUtil();

        mTitleText.setText("公告");
        mTitleText.setCompoundDrawables(null,null,null,null);

        mRightImg.setVisibility(View.GONE);
        mRightImg.setImageResource(R.drawable.shuaixuan);
        mRightTextTv.setVisibility(View.GONE);
        mRightTextTv.setText("筛选");
        mRightTextTv.setTextColor(ContextCompat.getColor(this,R.color.btn_login_normal));

        String sTime = UtilTools.getFirstDayOfMonthByDate(new Date());


        String eTime = UtilTools.getStringFromDate(new Date(),"yyyyMMdd");

        mSelectStartTime = sTime;
        mSelectEndTime = eTime;
        mStartTime = mSelectStartTime;
        mEndTime = mSelectEndTime;


        initView();
        showProgressDialog();
        traderListAction();

//        for (int i = 0; i <10 ; i++) {
//            Map<String,Object> map = new HashMap<>();
//            if (i == 0){
//                map.put("kind","0"); //充币
//            }else {
//                map.put("kind","1"); //提币
//            }
//            map.put("title","火币全球站将于6月13号开启首期FastTrack投票上币" +i);
//            mDataList.add(map);
//        }
//        responseData();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        LinearLayoutManager manager = new LinearLayoutManager(NoticeListActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                traderListAction();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                traderListAction();
            }
        });
    }
    //获取公告列表
    private void traderListAction(){

        mRequestTag = MethodUrl.NOTICE_LIST;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(NoticeListActivity.this,MbsConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.NOTICE_LIST, map);
    }



    private void responseData() {
        if (mListAdapter == null) {
            mListAdapter = new NoticeListAdapter(NoticeListActivity.this);
            mListAdapter.addAll(mDataList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mListAdapter);

//            SampleHeader headerView = new SampleHeader(BankCardActivity.this, R.layout.item_bank_bind);
//            mLRecyclerViewAdapter.addHeaderView(headerView);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(true);


            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mListAdapter.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });


        } else {
            if (mPage == 1) {
                mListAdapter.clear();
            }
            mListAdapter.addAll(mDataList);
            mListAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }
     /*   //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.black, android.R.color.white);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineScalePulseOut); //设置下拉刷新Progress的样式
        //mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.red ,android.R.color.white);
//设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.red ,android.R.color.white);*/

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
            mPage++;
        }

        mRefreshListView.refreshComplete(10);
        mListAdapter.notifyDataSetChanged();
        if (mListAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
        }else {
            mPageView.showContent();
        }

    }
    private RecyclerView mTypeRecyclerView;
    private  TradeDialogAdapter mTradeDialogAdapter;
    private TextView mOneTv;
    private TextView mThreeTv;
    private TextView mSetTimeTv;
    private TextView mStartTimeTv;
    private TextView mEndTimeTv;
    private Button mResetBut;
    private Button mSureBut;
    private  DateSelectDialog mySelectDialog;
    private  DateSelectDialog mySelectDialog2;


    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    public void showDialog() {
        initPopupWindow();
    }

    private void initPopupWindow(){

        int nH = UtilTools.getNavigationBarHeight(NoticeListActivity.this);
        LinearLayout mNagView ;

        if (mConditionDialog == null) {
            mySelectDialog = new DateSelectDialog(this, true, "选择日期", 21);
            mySelectDialog.setSelectBackListener(this);
            mySelectDialog2 = new DateSelectDialog(this, true, "选择日期", 22);
            mySelectDialog2.setSelectBackListener(this);

            popView = LayoutInflater.from(NoticeListActivity.this).inflate(R.layout.dialog_trade_condition,null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mConditionDialog.setClippingEnabled(false);
            initConditionDialog(popView);

            mNagView = popView.findViewById(R.id.navigation_b_view);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,nH);
            mNagView.setLayoutParams(layoutParams);


            int screenWidth=UtilTools.getScreenWidth(NoticeListActivity.this);
            int screenHeight=UtilTools.getScreenHeight(NoticeListActivity.this);
            mConditionDialog.setWidth((int)(screenWidth*0.8));
            mConditionDialog.setHeight(WindowManager.LayoutParams.MATCH_PARENT);

            //设置background后在外点击才会消失
            mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(NoticeListActivity.this,5)));
            //mConditionDialog.setOutsideTouchable(true);// 设置可允许在外点击消失
            //自定义动画
            mConditionDialog.setAnimationStyle(R.style.PopupAnimation);
//            mConditionDialog.setAnimationStyle(android.R.style.Animation_Activity);//使用系统动画
            mConditionDialog.update();
            mConditionDialog.setTouchable(true);
            mConditionDialog.setFocusable(true);
            //popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
            //			mConditionDialog.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
            mConditionDialog.showAtLocation(NoticeListActivity.this.getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
            toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggleBright();
                }
            });
        }else {
            mNagView = popView.findViewById(R.id.navigation_b_view);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,nH);
            mNagView.setLayoutParams(layoutParams);

            mConditionDialog.showAtLocation(NoticeListActivity.this.getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }



    private void initConditionDialog(View view){

        mTypeRecyclerView = view.findViewById(R.id.type_recycleview);

        mOneTv = view.findViewById(R.id.one_month_tv);
        mThreeTv = view.findViewById(R.id.three_month_tv);
        mSetTimeTv = view.findViewById(R.id.set_time_tv);
        mStartTimeTv = view.findViewById(R.id.start_time_value_tv);
        mEndTimeTv = view.findViewById(R.id.end_time_value_tv);
        mResetBut = view.findViewById(R.id.reset_but);
        mSureBut = view.findViewById(R.id.sure_but);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.one_month_tv:
                        mOneTv.setSelected(true);
                        mThreeTv.setSelected(false);
                        mSetTimeTv.setSelected(false);

                        String startOne = UtilTools.getMonthAgo(new Date(),-1);
                        String endOne = UtilTools.getStringFromDate(new Date(),"yyyyMMdd");

                        mSelectStartTime = startOne;
                        mSelectEndTime = endOne;

                        mStartTimeTv.setText(UtilTools.getStringFromSting2(mSelectStartTime,"yyyyMMdd","yyyy-MM-dd"));
                        mEndTimeTv.setText(UtilTools.getStringFromSting2(mSelectEndTime,"yyyyMMdd","yyyy-MM-dd"));

                        break;
                    case R.id.three_month_tv:
                        mOneTv.setSelected(false);
                        mThreeTv.setSelected(true);
                        mSetTimeTv.setSelected(false);

                        String startThree = UtilTools.getMonthAgo(new Date(),-3);
                        String endThree = UtilTools.getStringFromDate(new Date(),"yyyyMMdd");

                        mSelectStartTime = startThree;
                        mSelectEndTime = endThree;

                        mStartTimeTv.setText(UtilTools.getStringFromSting2(mSelectStartTime,"yyyyMMdd","yyyy-MM-dd"));
                        mEndTimeTv.setText(UtilTools.getStringFromSting2(mSelectEndTime,"yyyyMMdd","yyyy-MM-dd"));

                        break;
                    case R.id.set_time_tv:
                        mOneTv.setSelected(false);
                        mThreeTv.setSelected(false);
                        mSetTimeTv.setSelected(true);
                        break;
                    case R.id.start_time_value_tv:
                        showDateDialog();
                        break;
                    case R.id.end_time_value_tv:
                        showDateDialog2();
                        break;
                    case R.id.reset_but:
                        resetCondition();
                        break;
                    case R.id.sure_but:
                        showProgressDialog();
                        getSelectCondition();
                        mConditionDialog.dismiss();
                        break;
                }
            }
        };

        mOneTv.setOnClickListener(onClickListener);
        mThreeTv.setOnClickListener(onClickListener);
        mSetTimeTv.setOnClickListener(onClickListener);
        mSureBut.setOnClickListener(onClickListener);
        mResetBut.setOnClickListener(onClickListener);
        mStartTimeTv.setOnClickListener(onClickListener);
        mEndTimeTv.setOnClickListener(onClickListener);


        List<Map<String,Object>> maps = SelectDataUtil.getCondition();

        GridLayoutManager linearLayoutManager = new GridLayoutManager(NoticeListActivity.this,3);
        mTypeRecyclerView.setLayoutManager(linearLayoutManager);
        mTradeDialogAdapter = new TradeDialogAdapter(NoticeListActivity.this,maps);
        //第一次设置默认值
        mSelectType = "borrow";
        mTradeDialogAdapter.setSelectItme(0);

        mOneTv.setSelected(true);

        String startOne = UtilTools.getMonthAgo(new Date(),-1);
        String endOne = UtilTools.getStringFromDate(new Date(),"yyyyMMdd");
        mSelectStartTime = startOne;
        mSelectEndTime = endOne;

        mStartTimeTv.setText(UtilTools.getStringFromSting2(mSelectStartTime,"yyyyMMdd","yyyy-MM-dd"));
        mEndTimeTv.setText(UtilTools.getStringFromSting2(mSelectEndTime,"yyyyMMdd","yyyy-MM-dd"));

        mTradeDialogAdapter.setOnItemClickListener(new OnMyItemClickListener() {
            @Override
            public void OnMyItemClickListener(View view, int position) {
                Map<String,Object> itemMap =  mTradeDialogAdapter.getDatas().get(position);
                mSelectType = itemMap.get("code")+"";
            }
        });
        mTypeRecyclerView.setAdapter(mTradeDialogAdapter);
    }

    private void showDateDialog() {
        mySelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }
    private void showDateDialog2() {
        mySelectDialog2.showAtLocation(Gravity.BOTTOM, 0, 0);
    }



    private void getSelectCondition(){
        mStartTime = mSelectStartTime;
        mEndTime = mSelectEndTime;
        mBusiType = mSelectType;
        mPage = 1;
        traderListAction();
    }
    private void resetCondition(){
        mOneTv.setSelected(true);
        mThreeTv.setSelected(false);
        mSetTimeTv.setSelected(false);

        //设置默认值
        mSelectType = "borrow";
        mTradeDialogAdapter.setSelectItme(0);
        mTradeDialogAdapter.notifyDataSetChanged();

        String startOne = UtilTools.getMonthAgo(new Date(),-1);
        String endOne = UtilTools.getStringFromDate(new Date(),"yyyyMMdd");
        mSelectStartTime = startOne;
        mSelectEndTime = endOne;

        mStartTimeTv.setText(UtilTools.getStringFromSting2(mSelectStartTime,"yyyyMMdd","yyyy-MM-dd"));
        mEndTimeTv.setText(UtilTools.getStringFromSting2(mSelectEndTime,"yyyyMMdd","yyyy-MM-dd"));

    }





    @OnClick({R.id.back_img,R.id.right_lay,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.right_lay:
                showDialog();
                break;
        }
    }

    @Override
    public void showProgress() {
        //showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent ;
        switch (mType){
            case MethodUrl.NOTICE_LIST:
                switch (tData.get("code")+""){
                    case "0":
                        List<Map<String,Object>> list = (List<Map<String, Object>>) tData.get("data");
                        if (UtilTools.empty(list)){
                            mPageView.showEmpty();
                        }else {
                            mPageView.showContent();
                            mDataList.clear();
                            mDataList.addAll(list);
                            responseData();

                        }
                        mRefreshListView.refreshComplete(10);
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(NoticeListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        mPageView.showNetworkError();
                        showToastMsg(tData.get("msg")+"");
                        break;
                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.NOTICE_LIST:
                        traderListAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {

        switch (mType) {
            case MethodUrl.tradeList://
                if (mListAdapter != null){
                    if (mListAdapter.getDataList().size() <= 0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            traderListAction();
                        }
                    });
                }else {
                    mPageView.showNetworkError();
                }
                break;
        }

        dealFailInfo(map,mType);
    }

    @Override
    public void reLoadingData() {
        showProgressDialog();
        traderListAction();
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type){
            case 21:
                mSelectStartTime = map.get("date")+"";
                mStartTimeTv.setText(mSelectStartTime);
                mStartTimeTv.setText(UtilTools.getStringFromSting2(mSelectStartTime,"yyyyMMdd","yyyy-MM-dd"));
                break;
            case 22:
                mSelectEndTime = map.get("date")+"";
                mEndTimeTv.setText(mSelectEndTime);
                mEndTimeTv.setText(UtilTools.getStringFromSting2(mSelectEndTime,"yyyyMMdd","yyyy-MM-dd"));
                break;
        }

    }
}
