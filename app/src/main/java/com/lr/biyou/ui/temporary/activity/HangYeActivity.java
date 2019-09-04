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
import com.lr.biyou.ui.temporary.adapter.HangYeAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.OnSearchItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SelectDataUtil;
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
 * 所属行业   界面
 */
public class HangYeActivity extends BasicActivity implements RequestView,ReLoadingData{

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
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;

    private String mRequestTag ="";


    private HangYeAdapter mHangYeAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;

    private String mBankId = "";
    private String mCityCode = "";

    private String mType = "";

    @Override
    public int getContentView() {
        return R.layout.activity_bank_wd;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        Intent intent  = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mType = bundle.getString("TYPE");
            if (mType.equals("1")){
                String ss = SelectDataUtil.getJson(this,"hangye_work.json");
                mTitleText.setText(getResources().getString(R.string.work_kind));
                mDataList =   JSONUtil.getInstance().jsonToList(ss);
            }else if (mType.equals("2")){
                mDataList =(List<Map<String,Object>>) bundle.getSerializable("DATA");
                String name = bundle.getString("name");
                mTitleText.setText(name);

            }
        }

        initView();
        responseData();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        LinearLayoutManager manager = new LinearLayoutManager(HangYeActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
               // bankWdListAction();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //bankWdListAction();
            }
        });

        mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                bankWdListAction();
            }
        });

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                mHangYeAdapter.getFilter().filter(sequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void bankWdListAction(){

        mRequestTag = MethodUrl.bankWdList;
        Map<String, String> map = new HashMap<>();
        map.put("opnbnkid",mBankId);
        map.put("citycode",mCityCode);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.bankWdList, map);
    }



    private void responseData() {
        if (mHangYeAdapter == null) {
            mHangYeAdapter = new HangYeAdapter(HangYeActivity.this);
            mHangYeAdapter.addAll(mDataList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHangYeAdapter);

//            SampleHeader headerView = new SampleHeader(BankCardActivity.this, R.layout.item_bank_bind);
//            mLRecyclerViewAdapter.addHeaderView(headerView);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(false);
            mRefreshListView.setLoadMoreEnabled(false);


            DividerDecoration divider2 = new DividerDecoration.Builder(HangYeActivity.this)
                    .setHeight(R.dimen.divide_hight)
                    //.setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.divide_line)
                    .build();
            mRefreshListView.addItemDecoration(divider2);

        } else {
            if (mPage == 1) {
                mHangYeAdapter.clear();
            }
            mHangYeAdapter.addAll(mDataList);
            mHangYeAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mHangYeAdapter.setSourceList(mDataList);
        mHangYeAdapter.setOnSearchItemClickListener(new OnSearchItemClickListener() {
            @Override
            public void OnSearchItemClickListener(Map<String, Object> mSelectData) {

                /*List<Map<String,Object>> mChildList = (List<Map<String,Object>>) mSelectData.get("typeList");
                if (mChildList == null || mChildList.size() == 0){*/
                    Intent intent = new Intent();
                    intent.putExtra("DATA",(Serializable) mSelectData);
                    setResult(RESULT_OK,intent);
                    finish();
              /*  }else {
                    Intent intent = new Intent(HangYeActivity.this,HangYeActivity.class);
                    intent.putExtra("DATA",(Serializable) mChildList);
                    intent.putExtra("TYPE","2");
                    intent.putExtra("name",mSelectData.get("name")+"");
                    startActivityForResult(intent,60);
                }*/


            }
        });
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
        mHangYeAdapter.notifyDataSetChanged();
        if (mHangYeAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
        }else {
            mPageView.showContent();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 60:
                    bundle = data.getExtras();
                    if (bundle != null){
                       Map<String,Object> mHangyeMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        Intent intent = new Intent();
                        intent.putExtra("DATA",(Serializable) mHangyeMap);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                    break;
            }
        }
    }



    @OnClick({R.id.back_img,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
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

        Intent intent ;
        switch (mType){

            case MethodUrl.bankWdList://
                String result = tData.get("result")+"";
                if (UtilTools.empty(result)){
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                    responseData();
                }else {
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                    if (list != null){
                        mDataList.clear();
                        mDataList.addAll(list);
                        responseData();
                    }
                }

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.bankWdList:
                        bankWdListAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {

        switch (mType) {
            case MethodUrl.bankWdList:
                if (mHangYeAdapter != null){
                    if (mHangYeAdapter.getDataList().size() <= 0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            bankWdListAction();
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
        bankWdListAction();
    }
}
