package com.lr.biyou.ui.temporary.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.temporary.adapter.PingZhengSelectAdapter;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择关联应收凭证
 */
public class SelectPingZhengActivity extends BasicActivity implements RequestView, ReLoadingData {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.divide_line)
    View mDivideLine;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private PingZhengSelectAdapter mAdapter;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private List<Map<String, Object>> mBooleanList = new ArrayList<>();
    private List<Map<String, Object>> mSelectList = new ArrayList<>();


    private Map<String, Object> mHeTongMap ;

    @Override
    public int getContentView() {
        return R.layout.activity_select_ping_zheng;
    }


    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mRightTextTv.setText("全选");
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightImg.setVisibility(View.VISIBLE);
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();

        mTitleText.setText("选择应收凭证");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(linearLayoutManager);


        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                responseData();
                mRefreshListView.refreshComplete(10);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
             mHeTongMap= (Map<String, Object>) bundle.getSerializable("DATA");
        }


        initData();

        responseData();


    }


    @OnClick(R.id.left_back_lay)
    public void onViewClicked() {
        finish();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {

            Map<String, Object> map = new HashMap<>();
            map.put("flowdate","");
            map.put("flowid","");
            map.put("sgndt","");
            map.put("name", "深圳市国有兴头投资那个公司");
            map.put("money", "57690000");
            map.put("mumber", "128192" + i);
            mDataList.add(map);
        }
    }


    @Override
    public void reLoadingData() {

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


    private void responseData() {
        for (Map m : mDataList) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", m);
            map.put("selected", false);
            mBooleanList.add(map);
        }


        if (mAdapter == null) {
            mAdapter = new PingZhengSelectAdapter(this,mHeTongMap,0);
            mAdapter.setBooleanList(mBooleanList);
            mAdapter.addAll(mDataList);

            /*AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mAdapter);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));*/


            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(this)
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

            DividerDecoration divider2 = new DividerDecoration.Builder(this)
                    // .setHeight(R.dimen.dp_10)
                    //.setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.body_bg)
                    .build();
            mRefreshListView.addItemDecoration(divider2);

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

            });

            mAdapter.setOnChildClickListener(new OnChildClickListener() {
                @Override
                public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
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

           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mAdapter.setBooleanList(mBooleanList);
            mAdapter.clear();
            mAdapter.addAll(mDataList);
            mAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();

        } else {
            mPageView.showContent();
        }
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


    @OnClick({R.id.left_back_lay, R.id.right_lay, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();


                break;
            case R.id.right_lay: //全选
                if (mAdapter != null) {
                    if (mRightTextTv.getText().toString().equals("全选")) {
                        mAdapter.selectAll();
                        mRightTextTv.setText("取消");
                    } else {
                        mAdapter.cancelSelectAll();
                        mRightTextTv.setText("全选");
                    }
                }

                break;
            case R.id.btn_next:
                List<Map<String, Object>> list = mAdapter.getBooleanList();
                mSelectList.clear();
                for (Map map : list) {
                    boolean b = (Boolean) map.get("selected");
                    Map<String, Object> mSelectMap = (Map<String, Object>) map.get("value");
                    if (b) {
                        mSelectList.add(mSelectMap);
                    }
                }


                LogUtilDebug.i("show", "size:" + mSelectList.size());
                //关联合同


                //startActivity(new Intent(SelectPingZhengActivity.this, HeTongSelectActivity.class));



                break;
        }
    }


}
