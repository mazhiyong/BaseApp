package com.lr.biyou.ui.temporary.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.CartAdapter;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.mywidget.view.MyRefreshHeader;
import com.lr.biyou.mywidget.view.PageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class CartFragment extends BasicFragment {
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRecyclerView;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.page_view)
    PageView pageView;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.all_checkbox)
    CheckBox allCheckbox;
    @BindView(R.id.cart_totaltip)
    TextView cartTotaltip;
    @BindView(R.id.cart_totalprice)
    TextView cartTotalprice;
    @BindView(R.id.cart_btn_pay)
    Button cartBtnPay;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.bottom_lay)
    LinearLayout bottomLay;


    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private CartAdapter mDataAdapter;

    private boolean mJianPanShow = false;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_cart;
    }

    @Override
    public void init() {
        initView();
    }


    private void initView() {

        mRecyclerView.setRefreshHeader(new MyRefreshHeader(getActivity()));

        mRecyclerView.setLScrollListener(mLScrollListener);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
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
                requestData();
            }
        });

        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                requestData();
            }
        });
        handler.sendEmptyMessageDelayed(1, 0);



    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getData();
            responseData();
        }
    };
    List<Map<String, Object>> list = new ArrayList<>();

    private void requestData() {


        handler.sendEmptyMessageDelayed(1, 1000);

    }


    private void responseData() {

        /*if (map == null){
            mPageView.showNetworkError();
            return;
        }else {
            mPageView.showContent();
        }*/


        if (mDataAdapter == null) {
            mDataAdapter = new CartAdapter(getActivity());
            mDataAdapter.addAll(list);

            AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRecyclerView.setLoadMoreEnabled(false);

           /* mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mDataAdapter.getDataList().get(position);
                }

            });*/

        } else {
            mDataAdapter.clear();
            mDataAdapter.addAll(list);
            mDataAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }
      /*  //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.black ,android.R.color.white);*/


        if (list == null || list.size() < 10) {
            mRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        } else {
            mRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
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


    @OnClick({R.id.back_img, R.id.cart_btn_pay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                break;
            case R.id.cart_btn_pay:
               // intent = new Intent(CartActivity.this,WriteOrderActivity.class);
               // startActivity(intent);
                break;
        }
    }



    private void getData(){

        list.clear();

        Map<String,Object> map = new HashMap<>();
        map.put("name","苹果111");
        map.put("code","001");
        map.put("shopName","天仓旗舰店");
        list.add(map);

        map = new HashMap<>();
        map.put("name","苹果222");
        map.put("code","001");
        map.put("shopName","天仓旗舰店");
        list.add(map);


        map = new HashMap<>();
        map.put("name","苹果333");
        map.put("code","001");
        map.put("shopName","天仓旗舰店");
        list.add(map);


        map = new HashMap<>();
        map.put("name","西瓜11");
        map.put("code","002");
        map.put("shopName","天仓体验店");
        list.add(map);


        map = new HashMap<>();
        map.put("name","西瓜22");
        map.put("code","002");
        map.put("shopName","天仓体验店");
        list.add(map);


        map = new HashMap<>();
        map.put("name","西瓜33");
        map.put("code","002");
        map.put("shopName","天仓体验店");
        list.add(map);


        map = new HashMap<>();
        map.put("name","孙悟空111");
        map.put("code","003");
        map.put("shopName","天仓优惠店");
        list.add(map);


        map = new HashMap<>();
        map.put("name","孙悟空222");
        map.put("code","003");
        map.put("shopName","天仓优惠店");
        list.add(map);


    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {

    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {

    }

    /**
     * @param tData 数据类型
     * @param mType
     * @descriptoin 请求数据成功
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
