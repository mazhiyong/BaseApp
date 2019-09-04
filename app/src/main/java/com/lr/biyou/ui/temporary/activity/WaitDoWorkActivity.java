package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.WaitDoWorkAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
/**
 *  待办事项列表
 */
public class WaitDoWorkActivity extends BasicActivity implements RequestView,ReLoadingData {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.dowork_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private String mRequestTag ="";
    private int mPage = 1;

    private WaitDoWorkAdapter mWaitDoWorkAdapter;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    @Override
    public int getContentView() {
        return R.layout.activity_wait_do_work;
    }
    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.wait_do_work));


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.DAIBAN_INFO_UPDATE);
        registerReceiver(mBroadcastReceiver,intentFilter);

        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(linearLayoutManager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                  doworkListAction();
//                mRefreshListView.refreshComplete(10);
//                mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        showProgressDialog();
        doworkListAction();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.DAIBAN_INFO_UPDATE)){
                doworkListAction();
            }
        }
    };

    @OnClick({R.id.back_img,R.id.left_back_lay})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
        }
    }

    private void doworkListAction() {
        mRequestTag =  MethodUrl.workList;

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.workList,map);
    }

    private void responseData() {
          if(mWaitDoWorkAdapter==null){
              mWaitDoWorkAdapter=new WaitDoWorkAdapter(this);
              mWaitDoWorkAdapter.addAll(mDataList);

              AnimationAdapter adapter = new ScaleInAnimationAdapter(mWaitDoWorkAdapter);
              adapter.setFirstOnly(false);
              adapter.setDuration(500);
              adapter.setInterpolator(new OvershootInterpolator(.5f));

              mLRecyclerViewAdapter=new LRecyclerViewAdapter(mWaitDoWorkAdapter);
              mRefreshListView.setAdapter(mLRecyclerViewAdapter);
              mRefreshListView.setItemAnimator(new DefaultItemAnimator());
              mRefreshListView.setHasFixedSize(true);
              mRefreshListView.setNestedScrollingEnabled(false);
              mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
              mRefreshListView.setPullRefreshEnabled(true);
              mRefreshListView.setLoadMoreEnabled(false);

              mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
              mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

              DividerDecoration divider2 = new DividerDecoration.Builder(this)
                      .setHeight(R.dimen.divide_hight)
                      //.setPadding(R.dimen.dp_10)
                      .setColorResource(R.color.divide_line)
                      .build();
              mRefreshListView.addItemDecoration(divider2);

          }else {

              mWaitDoWorkAdapter.clear();
              mWaitDoWorkAdapter.addAll(mDataList);
              mWaitDoWorkAdapter.notifyDataSetChanged();

              mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
          }
        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mWaitDoWorkAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

    }

    /*
      预加载数据
     */
    @Override
    public void reLoadingData() {
        showProgressDialog();
          doworkListAction();
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
        switch (mType){
            case MethodUrl.workList://
                String result = tData.get("result")+"";
                if (UtilTools.empty(result)){
                    responseData();
                }else {
                    mDataList.clear();
                    Map<String,Object> mm =   JSONUtil.getInstance().jsonMap(result);
                    //预授信回退列表
                    List<Map<String,Object>> list1 = (List<Map<String,Object>>) mm.get("prebackList");
                    if(list1!=null&&list1.size()>0){
                        for (Map<String,Object> map : list1){
                            map.put("type","1");
                        }
                        mDataList.addAll(list1);

                    }else {
                       // Toast.makeText(this,"预授信列表为空",Toast.LENGTH_SHORT).show();
                    }

                    //授信签署列表
                    List<Map<String,Object>> list2 = (List<Map<String,Object>>) mm.get("creditSignList");
                    if(list2!=null&&list2.size()>0){
                        for (Map<String,Object> map : list2){
                            map.put("type","2");
                        }
                        mDataList.addAll(list2);

                    }else {
                       // Toast.makeText(this,"授信签署列表为空",Toast.LENGTH_SHORT).show();
                    }

                    //借款进度列表
                    List<Map<String,Object>> list3 = (List<Map<String,Object>>) mm.get("loanPlanList");
                    if(list3!=null&&list3.size()>0){
                        for (Map<String,Object> map : list3){
                            map.put("type","3");
                        }
                        mDataList.addAll(list3);
                    }else {
                       // Toast.makeText(this,"借款进度列表为空",Toast.LENGTH_SHORT).show();
                    }

                    //待还款列表
                    List<Map<String,Object>> list4 = (List<Map<String,Object>>) mm.get("replayList");
                    if(list4!=null&&list4.size()>0){
                        for (Map<String,Object> map : list4){
                            map.put("type","4");
                        }
                        mDataList.addAll(list4);
                    }else {
                       // Toast.makeText(this,"待还款列表为空",Toast.LENGTH_SHORT).show();
                    }

                    //共同借款人审核列表
                    List<Map<String,Object>> list5 = (List<Map<String,Object>>) mm.get("gtPreList");
                    if(list5!=null&&list5.size()>0){
                        for (Map<String,Object> map : list5){
                            map.put("type","5");
                        }
                        mDataList.addAll(list5);
                    }else {
                       // Toast.makeText(this,"共同借款人列表为空",Toast.LENGTH_SHORT).show();
                    }

                    responseData();

                }
                mRefreshListView.refreshComplete(10);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.workList:
                       doworkListAction();
                        break;
                }
                break;
        }

    }
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        switch (mType) {
            case MethodUrl.workList:
                if(mWaitDoWorkAdapter!=null){
                    if(mWaitDoWorkAdapter.getDataList().size()<0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            doworkListAction();
                        }
                    });
                }else {
                    mPageView.showNetworkError();
                }
                break;
        }


        //根据处理错误类型
        dealFailInfo(map,mType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
