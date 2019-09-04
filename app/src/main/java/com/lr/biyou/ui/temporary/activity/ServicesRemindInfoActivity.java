package com.lr.biyou.ui.temporary.activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.ServiceRemindInfoAdapter;
import com.lr.biyou.ui.temporary.adapter.SwipeMenuAdapter2;
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
 *  服务提醒消息列表
 */
public class ServicesRemindInfoActivity extends BasicActivity implements RequestView,ReLoadingData{
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.service_remind_list_view)
    LRecyclerView mLRecyclerView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    private LRecyclerViewAdapter mAdapter;
    private ServiceRemindInfoAdapter mServiceRemindInfoAdapter;
    private String mRequestTag="";
    private List<Map<String,Object>> mDataList=new ArrayList<>();

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.service_remind));
        mPageView.setContentView(mContent);
        mPageView.showLoading();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mLRecyclerView.setLayoutManager(manager);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新请求数据(待办列表项数据测试)
                serviceListAction();

            }
        });
        serviceListAction();
    }
    private void serviceListAction() {
        mRequestTag = MethodUrl.workList;

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.workList,map);
    }


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
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        switch (mType){
            case MethodUrl.workList:
                String result = tData.get("result")+"";
                if (UtilTools.empty(result)){
                    responseData();
                }else {
                    mDataList.clear();
                    Map<String, Object> mm =   JSONUtil.getInstance().jsonMap(result);
                    //预授信回退列表
                    List<Map<String, Object>> list1 = (List<Map<String, Object>>) mm.get("prebackList");
                    if (list1 != null && list1.size() > 0) {
                        for (Map<String, Object> map : list1) {
                            map.put("type", "1");
                        }
                        mDataList.addAll(list1);

                    } else {

                    }

                    //授信签署列表
                    List<Map<String, Object>> list2 = (List<Map<String, Object>>) mm.get("creditSignList");
                    if (list2 != null && list2.size() > 0) {
                        for (Map<String, Object> map : list2) {
                            map.put("type", "2");
                        }
                        mDataList.addAll(list2);

                    } else {

                    }

                    //借款进度列表
                    List<Map<String, Object>> list3 = (List<Map<String, Object>>) mm.get("loanPlanList");
                    if (list3 != null && list3.size() > 0) {
                        for (Map<String, Object> map : list3) {
                            map.put("type", "3");
                        }
                        mDataList.addAll(list3);
                    } else {

                    }

                    //待还款列表
                    List<Map<String, Object>> list4 = (List<Map<String, Object>>) mm.get("replayList");
                    if (list4 != null && list4.size() > 0) {
                        for (Map<String, Object> map : list4) {
                            map.put("type", "4");
                        }
                        mDataList.addAll(list4);
                    } else {

                    }

                    //共同借款人审核列表
                    List<Map<String, Object>> list5 = (List<Map<String, Object>>) mm.get("gtPreList");
                    if (list5 != null && list5.size() > 0) {
                        for (Map<String, Object> map : list5) {
                            map.put("type", "5");
                        }
                        mDataList.addAll(list5);
                    } else {

                    }

                    responseData();
                }
                    mLRecyclerView.refreshComplete(10);
                    break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag){
                    case MethodUrl.workList:
                        serviceListAction();
                        break;
                }
                break;
        }
    }

    private void responseData() {
        if(mServiceRemindInfoAdapter==null){
            mServiceRemindInfoAdapter=new ServiceRemindInfoAdapter(this);
            mServiceRemindInfoAdapter.addAll(mDataList);
            mServiceRemindInfoAdapter.setOnSwipeListener(new SwipeMenuAdapter2.onSwipeListener() {
                @Override
                public void onDel(int pos) {
                    //删除

                /*//RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528
                mSwipeMenuAdapter.getDataList().remove(pos);
                mSwipeMenuAdapter.notifyItemRemoved(pos);//推荐用这个
                if(pos != (mSwipeMenuAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                    mSwipeMenuAdapter.notifyItemRangeChanged(pos, mSwipeMenuAdapter.getDataList().size() - pos);
                }
                mSwipeMenuAdapter.notifyDataSetChanged();*/

                    mDataList.remove(pos);
                    responseData();

                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();

                }

                @Override
                public void onTop(int pos) {
                   //置顶
                }
            });

            AnimationAdapter adapter=new ScaleInAnimationAdapter(mServiceRemindInfoAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(5f));

            mAdapter=new LRecyclerViewAdapter(adapter);
            mLRecyclerView.setAdapter(mAdapter);
            mLRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mLRecyclerView.setHasFixedSize(true);
            mLRecyclerView.setNestedScrollingEnabled(false);
            mLRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mLRecyclerView.setPullRefreshEnabled(true);
            mLRecyclerView.setLoadMoreEnabled(false);

            mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mLRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);


            DividerDecoration divider2 = new DividerDecoration.Builder(this)
                    .setHeight(R.dimen.divide_hight)
                    .setPadding(R.dimen.dp_14)
                    .setColorResource(R.color.divide_line)
                    .build();
            mLRecyclerView.addItemDecoration(divider2);



        }else {

            mServiceRemindInfoAdapter.clear();
            mServiceRemindInfoAdapter.addAll(mDataList);
            mServiceRemindInfoAdapter.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
        }
        //添加尾部布局
        mLRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mLRecyclerView.setNoMore(true);
        } else {
            mLRecyclerView.setNoMore(false);
        }

        if (mServiceRemindInfoAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }
    @Override
    public int getContentView() {
        return R.layout.activity_services_remind_info;
    }

    @Override
    public void reLoadingData() {
        serviceListAction();
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

}
