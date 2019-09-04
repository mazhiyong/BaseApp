package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
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
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.PopuTipView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.temporary.adapter.ShouldShouMoneyAdapter;
import com.lr.biyou.utils.tool.DataHolder;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
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
 * 应收账款
 */
public class ShouldShouMoneyActivity extends BasicActivity implements RequestView, ReLoadingData {

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
    @BindView(R.id.divide_line)
    View mDivideLine;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.empty_lay)
    LinearLayout mEmptyLay;

    LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    ShouldShouMoneyAdapter mAdapter;

    @BindView(R.id.totall_shoumoney_tv)
    TextView mTotallShoumoneyTv;
    @BindView(R.id.max_jiemoney_tv)
    TextView mMaxJiemoneyTv;
    @BindView(R.id.date_tv)
    TextView mDateTv;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.tip_iv)
    ImageView mTipIv;

    private List<Map<String, Object>> mBooleanList = new ArrayList<>();
    private List<Map<String, Object>> mSelectList = new ArrayList<>();
    private List<Map<String, Object>> mDataList = new ArrayList<>();


    private String mRequestTag = "";

    private Map<String,Object> mSxMap = new HashMap<>();
    private String mPayCompayName = "";
    private String mPaycustid = "";


    @Override
    public int getContentView() {
        return R.layout.activity_should_shou_money;
    }


    @Override
    public void init() {

        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.shouldshou_money));

        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mSxMap  = (Map<String, Object>) bundle.getSerializable("DATA");
            mPayCompayName = bundle.getString("payfirmname");
            mPaycustid = bundle.getString("paycustid");
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(linearLayoutManager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
              // showProgressDialog();
                yszkList();

            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //payHistoryAction();
            }
        });


        showProgressDialog();
        yszkList();
    }


    private void yszkList() {
        mRequestTag = MethodUrl.yszkList;
        Map<String, String> map = new HashMap<>();
        map.put("flowdate",mSxMap.get("flowdate")+"");
        map.put("flowid",mSxMap.get("flowid")+"");
        map.put("autoid",mSxMap.get("autoid")+"");
        map.put("payfirmname",mPayCompayName);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.yszkList, map);
    }


    @OnClick({R.id.left_back_lay, R.id.btn_next, R.id.right_text_tv,R.id.tip_iv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.right_text_tv:
                intent = new Intent(ShouldShouMoneyActivity.this, InvoiceActivity.class);
                startActivity(intent);
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.btn_next:
                List<Map<String, Object>> list = mAdapter.getBooleanList();
                mSelectList.clear();
                boolean isChi = true;
                for (Map map : list) {
                    boolean b = (Boolean) map.get("selected");
                    Map<String, Object> mSelectMap = (Map<String, Object>) map.get("value");
                    if (b) {
                        mSelectList.add(mSelectMap);
                        String status = mSelectMap.get("poolsta")+"";
                        if (status.equals("0")){//入池状态：0：未入池  2：已入池
                            isChi = false;
                        }
                    }
                }
                LogUtilDebug.i("选择的应收账款信息",mSelectList);
                if (isChi){
                    intent = new Intent(ShouldShouMoneyActivity.this, BorrowMoneyActivity.class);
                }else {
                    intent = new Intent(ShouldShouMoneyActivity.this, HeTongSelectActivity.class);
                }
                intent.putExtra("DATA",(Serializable)mSxMap);
                DataHolder.getInstance().save("moneySelect",mSelectList);
                intent.putExtra("payfirmname",mPayCompayName);
                intent.putExtra("paycustid",mPaycustid);
                startActivity(intent);
                break;
            case R.id.tip_iv:
            /*    View inflate2 = View.inflate(ShouldShouMoneyActivity.this, R.layout.popup_bubble_text, null);
                TextView mTextView2 = inflate2.findViewById(R.id.tv_bubble);
                mTextView2.setText("注：截止2018年10月17日，您到期的\n" +
                        "应收账款为490,000.00元。最大可借款\n" +
                        "金额等于应收账款金额合计乘以最大\n" +
                        "比率，与授信可用额度之间的最小值。\n" +
                        "即490,000.00元×70.00%与97,400.00元\n" +
                        "之间的最小值。");*/
                String s = "注：截止2018年10月17日，您到期的\n" +
                        "应收账款为490,000.00元。最大可借款\n" +
                        "金额等于应收账款金额合计乘以最大\n" +
                        "比率，与授信可用额度之间的最小值。\n" +
                        "即490,000.00元×70.00%与97,400.00元\n" +
                        "之间的最小值。" ;

                PopuTipView mp= new PopuTipView(ShouldShouMoneyActivity.this,s,R.layout.popu_lay_bottom);
                mp.show(mTipIv,4);

               /* new BubblePopup(ShouldShouMoneyActivity.this, inflate2)
                        .anchorView(mTipIv)
                        .bubbleColor(Color.parseColor("#A6000000"))
                        .gravity(Gravity.BOTTOM)
                        //箭头宽度 高度
                        .triangleWidth(5)
                        .triangleHeight(5)
                        .showAnim(null)
                        .dismissAnim(null)
                        .show();*/
                break;
        }
    }


    @Override
    public void reLoadingData() {
        showProgressDialog();
        yszkList();
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
        dismissProgressDialog();
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
        switch (mType){
            case MethodUrl.yszkList:

                String t = tData.get("vchtrdtype")+"";
                if (t.equals("0")){//应收账款
                    mRightTextTv.setVisibility(View.GONE);
                    mRightImg.setVisibility(View.GONE);
                }else if (t.equals("1")){//发票
                    mRightTextTv.setVisibility(View.VISIBLE);
                    mRightTextTv.setText("导入发票");
                    mRightImg.setVisibility(View.VISIBLE);
                }

                String mm = tData.get("totalmny")+"";
                mTotallShoumoneyTv.setText(UtilTools.getMoney(mm));
                String mm2 = tData.get("maxcanloan")+"";
                mMaxJiemoneyTv.setText(UtilTools.getMoney(mm2));

                List<Map<String,Object>> list = (List<Map<String, Object>>) tData.get("yszkInfoList");
                if (list != null) {
                    mDataList.clear();
                    mDataList.addAll(list);
                    responseData();
                }

                mRefreshListView.refreshComplete(10);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.yszkList:
                        yszkList();
                        break;
                }
                break;
        }
    }

    private void responseData() {

        for (Map m : mDataList) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", m);
            map.put("selected", false);
            mBooleanList.add(map);
        }


        if (mAdapter == null) {
            mAdapter = new ShouldShouMoneyAdapter(this);
            mAdapter.setBooleanList(mBooleanList);
            mAdapter.addAll(mDataList);

        /*    AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mAdapter);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));
*/

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
                    //  .setHeight(R.dimen.dp_10)
                    //  .setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.body_bg)
                    .build();
            mRefreshListView.addItemDecoration(divider2);

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                }

            });

            mAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {

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

        switch (mType){
            case MethodUrl.yszkList:
                if(mAdapter!=null){
                    if(mAdapter.getDataList().size()<=0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            showProgressDialog();
                            yszkList();
                        }
                    });

                }else {
                    mPageView.showNetworkError();
                }
                break;
        }




        dealFailInfo(map, mType);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1234: {
                Toast.makeText(ShouldShouMoneyActivity.this, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                break;
            }
        }

    }


}
