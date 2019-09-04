package com.lr.biyou.ui.temporary.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.activity.TestActivity;
import com.lr.biyou.ui.temporary.adapter.OrderAdapter;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.tool.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderChildFragment extends Fragment implements View.OnClickListener, RequestView,ReLoadingData {

    LRecyclerView mRecyclerView;
    LinearLayout mContent;
    PageView mPageView;

    private View mRootView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private OrderAdapter mDataAdapter;

    private int mPage = 1;

    private int mType ;

    /**
     * 对象定义
     **/
    private RequestPresenterImp mRequestPresenterImp = null;

    public OrderChildFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public OrderChildFragment(int mType){
        mType = mType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //mRootView = inflater.inflate(R.layout.fragment_order_child, (ViewGroup) getActivity().findViewById(R.id.order_manager_page), false);
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) mRootView.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        return mRootView;
    }


    private void initView() {

        mContent = mRootView.findViewById(R.id.content);
        mPageView = mRootView.findViewById(R.id.page_view);

        mPageView.setContentView(mContent);
        mPageView.showLoading();
        mPageView.subscribRefreshEvent(this);
        mPageView.setReLoadingData(this);

        mRecyclerView = mRootView.findViewById(R.id.refresh_list_view);

        mRecyclerView.setLScrollListener(mLScrollListener);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                initData();
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                initData();
            }
        });


        initData();
    }


    List<Map<String, Object>> list = new ArrayList<>();

    private void initData() {
        //模拟一下网络请求失败的情况
        if (NetworkUtils.isNetAvailable(getActivity())) {
        } else {
            mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                @Override
                public void reload() {
                    initData();
                }
            });
            return;
        }

      /*  "page": 0,
                "tab_index": 0,
                "unit_id": 0*/
        Map<String, Object> map = new HashMap<>();
        map.put("unit_id", 1);
        map.put("tab_index", mType);
        map.put("page", mPage);

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp = new RequestPresenterImp(this, getActivity());
     //   mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.orderList, map);
    }

    private void responseData() {

        if (mDataAdapter == null) {
            mDataAdapter = new OrderAdapter(getActivity());
            mDataAdapter.addAll(list);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            // mRecyclerView.setLoadMoreEnabled(false);

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mDataAdapter.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });


        } else {
            if (mPage == 1) {
                mDataAdapter.clear();
            }
            mDataAdapter.addAll(list);
            mDataAdapter.notifyDataSetChanged();
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

        mRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (list.size() < 10) {
            mRecyclerView.setNoMore(true);
        } else {
            mRecyclerView.setNoMore(false);
        }

        mRecyclerView.refreshComplete(10);
        mDataAdapter.notifyDataSetChanged();
        if (mDataAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
        }else {
            mPageView.showContent();
        }

    }


    private LRecyclerView.LScrollListener mLScrollListener = new LRecyclerView.LScrollListener() {
        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onScrolled(int distanceX, int distanceY) {

        }

        @Override
        public void onScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), TestActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

        list = (List<Map<String, Object>>) tData.get("rows");
        responseData();
    }


    @Override
    public void loadDataError(Map<String,Object> map,String mType) {
        String msg = map.get("msg")+"";
        mRecyclerView.refreshComplete(10);
        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                mPage++;
                initData();
            }
        });
        if (mPage == 1) {
            if (mDataAdapter != null && mDataAdapter.getDataList().size()>0){

            }else {
                mPageView.showNetworkError();
            }
        } else {
            mPage--;
        }
        Log.e("请求异常", msg);
//        mRecyclerView.refreshComplete(10);
    }

    @Override
    public void reLoadingData() {
        initData();
    }
}
