package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.CallBackTotal;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle5.adapter.ChoseBiTypeAdapter;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  选择投诉类型
 */
public class ChoseReasonTypeActivity extends BasicActivity implements RequestView, ReLoadingData, CallBackTotal {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    private String mRequestTag = "";

    private ChoseBiTypeAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;


    private int mPage = 1;
    //数据源
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    //数据源的是否是被选中的状态集合
    private List<Map<String,Object>> mBooleanList ;
    //选中后的结果集合
    private List<Map<String, Object>> mSelectList = new ArrayList<>();
    private int RESULT_CODE=1;
    private String id;
    private String type;
    @Override
    public int getContentView() {
        return R.layout.activity_chose_reason_type;
    }

    @Override
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText("投诉");
        mTitleText.setCompoundDrawables(null,null,null,null);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            id = bundle.getString("id");
            type = bundle.getString("type");
        }


        initView();
        //请求原因数据
        reasonListAction();
        showProgressDialog();
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                if(sequence.toString().length()>0 && mAdapter !=null){
                    mAdapter.setBackTotal(ChoseReasonTypeActivity.this);
                    mAdapter.getFilter().filter(sequence.toString());
                }else {
                    if (mDataList != null && mDataList.size() > 0) {
                        responseData();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }




    private void initView() {

        mPageView.setContentView(mContent);
        mPageView.showEmpty();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mEtSearch.setText("");
                mPage = 1;
                reasonListAction();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                reasonListAction();

            }
        });


    }

    private void reasonListAction() {
        mRequestTag = MethodUrl.CHAT_REASON_LIST;
        mRequestPresenterImp = new RequestPresenterImp(this,this);
        Map<String,Object> map=new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChoseReasonTypeActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_REASON_LIST,map);
    }


    private void responseData() {

        mBooleanList = new ArrayList<>();

        for (Map m:mDataList){
            Map<String,Object> map=new HashMap<>();
            map.put("object",m);
            map.put("select",false);
            mBooleanList.add(map);
        }

        if (mAdapter == null) {
            mAdapter = new ChoseBiTypeAdapter(this);
            mAdapter.addAll(mDataList);
            mAdapter.setBooleanList(mBooleanList);

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            DividerDecoration divider2 = new DividerDecoration.Builder(this)
                    .setHeight(R.dimen.divide_hight)
                    //.setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.divide_line)
                    .build();
            mRefreshListView.addItemDecoration(divider2);

            mAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {
                    List<Map<String, Object>> list = mAdapter.getBooleanList();
                    mSelectList.clear();
                    for (Map m:list) {
                        boolean b = (Boolean)m.get("select");
                        Map<String,Object>mSelectObject= (Map<String, Object>) m.get("object");
                        if (mSelectObject != null){
                            mSelectObject.put("userid",id);
                            if (b) {
                                mSelectList.add(mSelectObject);
                                Intent mIntent = new Intent(ChoseReasonTypeActivity.this,TousuActivity.class);
                                mIntent.putExtra("DATA",(Serializable) mSelectObject);
                                mIntent.putExtra("type",type);
                                startActivity(mIntent);
                                finish();
                            }
                        }
                    }

                    mRefreshListView.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
                        }
                    });

                }
            });


        } else {
            mAdapter.setBooleanList(mBooleanList);
            mAdapter.clear();
            mAdapter.addAll(mDataList);
            mAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }



        mAdapter.setSourceList(mAdapter.getDataList());


        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        mRefreshListView.refreshComplete(10);
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

    }
    @OnClick({R.id.back_img, R.id.iv_search,R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.iv_search:
                break;
        }
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
        Intent intent;
        switch (mType){
            case  MethodUrl.CHAT_REASON_LIST:
                switch (tData.get("code")+"") {
                    case "0":
                        if (UtilTools.empty(tData.get("data")+"")){
                            mPageView.showEmpty();
                        }else {
                            mDataList = (List<Map<String, Object>>) tData.get("data");
                            if (mDataList != null && mDataList.size()>0){
                                for (Map<String,Object> map:mDataList){
                                    map.put("symbol",map.get("title")+"");
                                }
                            }
                            mPageView.showContent();
                            responseData();
                            mRefreshListView.refreshComplete(10);

                        }
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(ChoseReasonTypeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;

                }
                break;
            case  MethodUrl.REFRESH_TOKEN:

                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        dealFailInfo(map,mType);
    }

    @Override
    public void reLoadingData() {
        reasonListAction();
        showProgressDialog();

    }

    @Override
    public void setTotal(int size) {
        if(size==0){
            mPageView.showEmpty();
        }else {
            mPageView.showContent();
        }
    }
}
