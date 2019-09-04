package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.BankNameListAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.mywidget.view.WaveSideBar;
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
 * 开户行列表   界面
 */
public class BankNameListActivity extends BasicActivity implements RequestView,ReLoadingData{

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
    @BindView(R.id.right_bar)
    WaveSideBar mSideBar;

    @BindView(R.id.bank_image_view)
    ImageView mBankImageView;
    @BindView(R.id.bank_name_tv)
    TextView mBankNameTv;
    @BindView(R.id.head_bank_lay)
    LinearLayout mHeadBankLay;


    private String mRequestTag ="";
    private BankNameListAdapter mBankNameListAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;


    @Override
    public int getContentView() {
        return R.layout.activity_bank_name_list;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.chose_bank2));

        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.shuaixuan);
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightLay.setVisibility(View.GONE);

        initView();
        showProgressDialog();
        bankNameAction();
       // mPageView.showLoading();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.showLoading();
        //mPageView.showEmpty();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(BankNameListActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setAutoMeasureEnabled(false);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                bankNameAction();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                bankNameAction();
            }
        });

        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                if (mBankNameListAdapter != null){
                    //该字母首次出现的位置
                    int position = mBankNameListAdapter.getPositionForSection(letter);
                    if (position != -1) {
                        manager.scrollToPositionWithOffset(position +1, 0);
                    }
                }

            }
        });
    }


    private void bankNameAction(){
        mRequestTag = MethodUrl.opnbnk;
        Map<String, String> map = new HashMap<>();
        map.put("type","UDK");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.opnbnk, map);
    }


    private void responseData() {

        if (mBankNameListAdapter == null) {
            mBankNameListAdapter = new BankNameListAdapter(BankNameListActivity.this);
            mBankNameListAdapter.addAll(mDataList);
            mBankNameListAdapter.simpleOrder();

                    /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
                     adapter.setFirstOnly(false);
                     adapter.setDuration(500);
                     adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mBankNameListAdapter);


            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);
            DividerDecoration divider2 = new DividerDecoration.Builder(BankNameListActivity.this)
                    .setHeight(2f)
                    .setPadding(0f)
                    .setColorResource(R.color.divide_line)
                    .build();
            mRefreshListView.addItemDecoration(divider2);

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mBankNameListAdapter.getDataList().get(position);
                    Intent intent = new Intent();
                    intent.putExtra("DATA",(Serializable) item);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            });


        } else {
            if (mPage == 1) {
                mBankNameListAdapter.clear();
            }
            mBankNameListAdapter.addAll(mDataList);
            mBankNameListAdapter.simpleOrder();
            mBankNameListAdapter.notifyDataSetChanged();
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
        if (mBankNameListAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
        }else {
            mPageView.showContent();
        }


        Map<String,Object> mHeadMap = new HashMap<>();
        for (Map<String,Object> bankMap : mDataList){
            String code = bankMap.get("opnbnkid")+"";
            if (code.equals("3002")){
                mHeadMap = bankMap;
                break;
            }
        }

        if (!mHeadMap.isEmpty()){
            mHeadBankLay.setTag(mHeadMap);
            mBankNameTv.setText(mHeadMap.get("opnbnknm")+"");
            GlideUtils.loadCircleImage(BankNameListActivity.this,mHeadMap.get("logopath")+"",mBankImageView);
            mHeadBankLay.setVisibility(View.VISIBLE);
        }

        mHeadBankLay.setVisibility(View.GONE);

        dismissProgressDialog();

    }




    @OnClick({R.id.back_img,R.id.right_lay,R.id.left_back_lay,R.id.head_bank_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.head_bank_lay:
                Map<String,Object> item = (Map<String, Object>) mHeadBankLay.getTag();
                intent = new Intent();
                intent.putExtra("DATA",(Serializable) item);
                setResult(RESULT_OK,intent);
                finish();
                break;
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
//        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent ;
        switch (mType){
            case MethodUrl.opnbnk://
                String result = tData.get("result")+"";
                if (UtilTools.empty(result)){
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                }else {
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                    if (list != null){
                        mDataList.clear();
                        mDataList.addAll(list);
                    }else {

                    }
                }
                responseData();
                dismissProgressDialog();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                showProgressDialog();
                switch (mRequestTag) {
                    case MethodUrl.opnbnk:
                        bankNameAction();
                        break;
                }
                break;
        }


    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        dismissProgressDialog();
        switch (mType) {
            case MethodUrl.opnbnk://

                if (mBankNameListAdapter != null){
                    if (mBankNameListAdapter.getDataList().size() <= 0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            bankNameAction();
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
        bankNameAction();
    }
}
