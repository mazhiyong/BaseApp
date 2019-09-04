package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.NormalNameListAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.OnSearchItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
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
 * 用到的字典列表   比如   职业  民族   界面
 */
public class NormalNameListActivity extends BasicActivity implements RequestView,ReLoadingData{

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
    @BindView(R.id.search_edit)
    EditText mSearchEdit;


    private String mRequestTag ="";

    private NormalNameListAdapter mNormalNameListAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;

    private String mType = "";
    private String mUrl = "";


    @Override
    public int getContentView() {
        return R.layout.activity_normal_code;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.shuaixuan);
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightLay.setVisibility(View.GONE);
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                if (mNormalNameListAdapter != null){
                    mNormalNameListAdapter.getFilter().filter(sequence.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mType = bundle.getString("type");
            switch (mType){
                case "1":
                    mUrl = MethodUrl.minZuList;
                    mTitleText.setText("选择民族");
                    mSearchEdit.setHint(getResources().getString(R.string.hint_minzu_edit));
                    showProgressDialog();
                    getMinzuInfo();
                    break;
                case "2":
                    mUrl = MethodUrl.zhiyeList;
                    mTitleText.setText("选择职位");
                    mDataList = (List<Map<String,Object>>) bundle.getSerializable("DATA");
                    responseData();
                    mSearchEdit.setVisibility(View.GONE);
                    mSearchEdit.setHint(getResources().getString(R.string.hint_zhiwei_edit));
                    break;
                default:
                    break;
            }
        }
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.showLoading();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(NormalNameListActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                switch (mType){
                    case "1":
                        getMinzuInfo();
                        break;
                    case "2":
                        mRefreshListView.refreshComplete(10);
                        break;
                    default:
                        mRefreshListView.refreshComplete(10);
                        break;
                }
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                getMinzuInfo();
            }
        });
    }


    /**
     * 获取民族列表
     */
    private void getMinzuInfo() {

        mRequestTag = mUrl;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, mUrl, map);
    }


    private void responseData() {
        if (mNormalNameListAdapter == null) {
            mNormalNameListAdapter = new NormalNameListAdapter(NormalNameListActivity.this);
            mNormalNameListAdapter.addAll(mDataList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mNormalNameListAdapter);


            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);
            DividerDecoration divider2 = new DividerDecoration.Builder(this)
                    .setHeight(2f)
                    .setPadding(0f)
                    .setColorResource(R.color.divide_line)
                    .build();
            mRefreshListView.addItemDecoration(divider2);

            mNormalNameListAdapter.setOnSearchItemClickListener(new OnSearchItemClickListener() {
                @Override
                public void OnSearchItemClickListener(Map<String, Object> mSelectData) {
                    Intent intent = new Intent();
                    intent.putExtra("DATA",(Serializable) mSelectData);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });

            /*mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mNormalNameListAdapter.getDataList().get(position);
                    Intent intent = new Intent();
                    intent.putExtra("DATA",(Serializable) item);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            });*/


        } else {
            if (mPage == 1) {
                mNormalNameListAdapter.clear();
            }
            mNormalNameListAdapter.addAll(mDataList);
            mNormalNameListAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mNormalNameListAdapter.setSourceList(mNormalNameListAdapter.getDataList());
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
        mNormalNameListAdapter.notifyDataSetChanged();
        if (mNormalNameListAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
        }else {
            mPageView.showContent();
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
            case MethodUrl.zhiyeList:
            case MethodUrl.minZuList://
                String result = tData.get("result")+"";
                LogUtilDebug.i("打印log日志",result);
                if (UtilTools.empty(result)){
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                    responseData();
                }else {
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                    if (list != null){
                        mDataList.clear();
                        mDataList.addAll(list);
                        responseData();
                    }else {

                    }
                }
                mRefreshListView.refreshComplete(10);

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                showProgressDialog();
                switch (mRequestTag) {
                    case MethodUrl.opnbnk:
                        getMinzuInfo();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {

        switch (mType) {
            case MethodUrl.opnbnk://

                if (mNormalNameListAdapter != null){
                    if (mNormalNameListAdapter.getDataList().size() <= 0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            getMinzuInfo();
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
        getMinzuInfo();
    }

}
