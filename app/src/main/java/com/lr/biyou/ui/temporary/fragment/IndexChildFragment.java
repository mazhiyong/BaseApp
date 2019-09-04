package com.lr.biyou.ui.temporary.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.activity.TestActivity;
import com.lr.biyou.ui.temporary.adapter.HomeAdapter;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.MyRefreshHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class IndexChildFragment extends Fragment implements View.OnClickListener,RequestView{

    private View rootView;
    private LRecyclerView mRecyclerView;



    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private HomeAdapter mDataAdapter;

    /**  对象定义  **/
    private RequestPresenterImp mRequestPresenterImp = null;

    private int mPage = 1;
    public IndexChildFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_index_child, container, false);
        initView();
        return rootView;
    }

    private void initView(){
        mRecyclerView = rootView.findViewById(R.id.refresh_list_view);

        mRecyclerView.setRefreshHeader(new MyRefreshHeader(getContext().getApplicationContext()));

        mRecyclerView.setLScrollListener(mLScrollListener);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                initData();
            }
        });
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineScalePulseOut); //设置下拉刷新Progress的样式
//        //mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头
//        //设置头部加载颜色
//        mRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.red ,R.color.white);
////设置底部加载颜色
//        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.red ,android.R.color.white);
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initData();
            }
        });

        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                initData();
            }
        });
       initData();
    }
    List<Map<String,Object>> list = new ArrayList<>();

    private void initData() {


        Map<String, Object> map = new HashMap<>();
        map.put("unit_id", 1);

        Map<String,String> mHeaderMap = new HashMap<String,String>();

      //  mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.indexAction, map);
    }


    private void responseData() {

        /*if (map == null){
            mPageView.showNetworkError();
            return;
        }else {
            mPageView.showContent();
        }*/


        if (mDataAdapter == null) {
            mDataAdapter = new HomeAdapter(getActivity());
            mDataAdapter.addAll(list);

            AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));

            mLRecyclerViewAdapter = new LRecyclerViewAdapter( adapter);
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRecyclerView.setLoadMoreEnabled(false);



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
            mDataAdapter.clear();
            mDataAdapter.addAll(list);
            mDataAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }
      /*  //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.black ,android.R.color.white);*/


        if (list == null || list.size() < 10) {
            mRecyclerView.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");
        } else {
            mRecyclerView.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");
        }

        mRecyclerView.refreshComplete(10);
        mDataAdapter.notifyDataSetChanged();
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

    }


    @Override
    public void loadDataError(Map<String,Object> map,String mType) {
        String msg = map.get("msg")+"";
    }
}
