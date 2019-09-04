package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Spannable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.MyAmountAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.mywidget.view.SampleHeader;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.ParseTextUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的---我的额度   界面
 */
public class MyAmountActivity extends BasicActivity implements RequestView,ReLoadingData{

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
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    private TextView mUserfulMoneyTv;
    private TextView mTotleMoneyTv;

    private String mRequestTag ="";


    private MyAmountAdapter mMyAmountAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;

    private Map<String,Object> mMoneyMap = new HashMap<>();

    @Override
    public int getContentView() {
        return R.layout.activity_my_amount;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.my_larger_num));

        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.shuaixuan);
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightTextTv.setText("筛选");
        mRightLay.setVisibility(View.GONE);

        initView();
        showProgressDialog();
        totleMoney();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.showLoading();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(MyAmountActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                totleMoney();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                sxListAction();
            }
        });
    }


    /**
     * 授信列表请求
     */
    private void sxListAction(){

        mRequestTag = MethodUrl.jiekuanSxList;
        Map<String, String> map = new HashMap<>();
        //map.put("creditstate","");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.jiekuanSxList, map);
    }
    private void totleMoney(){

        mRequestTag = MethodUrl.totleMoney;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.totleMoney, map);
    }



    private void responseData() {
        if (mMyAmountAdapter == null) {
            mMyAmountAdapter = new MyAmountAdapter(MyAmountActivity.this);
            mMyAmountAdapter.addAll(mDataList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMyAmountAdapter);

            SampleHeader headerView = new SampleHeader(MyAmountActivity.this, R.layout.header_my_amount);
            mUserfulMoneyTv = headerView.findViewById(R.id.use_money_tv);
            mTotleMoneyTv = headerView.findViewById(R.id.totle_value_tv);
            headerViewValue();

            mLRecyclerViewAdapter.addHeaderView(headerView);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);


            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mMyAmountAdapter.getDataList().get(position);
                    Intent intent = null;

                    int type =  mMyAmountAdapter.getItemViewType(position);
                    switch (type){
                        case MyAmountAdapter.TYPE_FIRST:
                            intent = new Intent(MyAmountActivity.this, ShouxinPreDetailActivity.class);
                            intent.putExtra("DATA",(Serializable) item);
                            startActivity(intent);
                            break;
                        case MyAmountAdapter.TYPE_NORMAL:
                            intent = new Intent(MyAmountActivity.this, ShouxinDetailActivity.class);
                            intent.putExtra("DATA",(Serializable) item);
                            startActivity(intent);
                            break;
                    }

                }
            });


        } else {
            headerViewValue();
            if (mPage == 1) {
                mMyAmountAdapter.clear();
            }
            mMyAmountAdapter.addAll(mDataList);
            mMyAmountAdapter.notifyDataSetChanged();
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
        }

        mRefreshListView.refreshComplete(10);
        mMyAmountAdapter.notifyDataSetChanged();
        if (mMyAmountAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
            //mRefreshListView.setEmptyView( LayoutInflater.from(this).inflate(R.layout.page_view_empty, null));
        }else {
            mPageView.showContent();
        }

        if ( !mMoneyMap.isEmpty()){
            mPageView.showContent();
        }
        //mRefreshListView.setEmptyView( mEmptyView);
    }


    private void headerViewValue(){
        if (mUserfulMoneyTv != null && !mMoneyMap.isEmpty()){
            String mm = UtilTools.getRMBMoney(mMoneyMap.get("leftmoney")+"");
            ParseTextUtil m = new ParseTextUtil(MyAmountActivity.this);
            Spannable reuslt = m.getDianType(mm);
            mUserfulMoneyTv.setText(reuslt);

            String tmm = UtilTools.getRMBMoney(mMoneyMap.get("totalmoney")+"");
            mTotleMoneyTv.setText(tmm);
        }
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
            case MethodUrl.jiekuanSxList://

                Map<String,Object> preSXMap = (Map<String, Object>) tData.get("preCredit");
                List<Map<String,Object>> list = (List<Map<String,Object>>) tData.get("creditList");
                if (list != null){
                    mDataList.clear();
                    mDataList.addAll(list);
                }

                //判断预授信信息是否为空   为空的话列表就不要显示了
                if (preSXMap != null && !preSXMap.isEmpty()){
                    preSXMap.put("viewType",MyAmountAdapter.TYPE_FIRST);
                    mDataList.add(0,preSXMap);
                }
                responseData();
                mRefreshListView.refreshComplete(10);
                break;
            case MethodUrl.totleMoney:
                mMoneyMap = tData;
                sxListAction();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                showProgressDialog();
                switch (mRequestTag) {
                    case MethodUrl.jiekuanSxList:
                        sxListAction();
                        break;
                    case MethodUrl.totleMoney:
                        totleMoney();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {

        switch (mType) {
            case MethodUrl.jiekuanSxList://
                if (mMyAmountAdapter != null){
                    if (mMyAmountAdapter.getDataList().size() <= 0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            sxListAction();
                        }
                    });
                }else {

                }
                responseData();
                break;
            case MethodUrl.totleMoney:
                if (mMyAmountAdapter != null){

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
        totleMoney();
    }
}
