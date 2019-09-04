package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.SelectBankAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;




import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择要绑定的银行卡的界面   界面
 */
public class SelectBankListActivity extends BasicActivity implements RequestView,ReLoadingData{

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
    @BindView(R.id.but_next)
    Button mButNext;



    private String mRequestTag ="";

    private SelectBankAdapter mSelectBankAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;

    private List<Boolean> mBooleanList ;

    private String mPatncode = "";

    private String mViewType = "";

    private Map<String,Object> mSelectBankMap ;



    @Override
    public int getContentView() {
        return R.layout.activity_select_bank_list;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.chose_bank));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mViewType = bundle.getString("TYPE");
            if (mViewType.equals("1")){//从  我的---银行卡  进来的
                mPatncode = bundle.getString("patncode");
            }else {
                mPatncode = bundle.getString("patncode");
                mDataList = (List<Map<String,Object>>) bundle.getSerializable("DATA");
                if (mDataList == null){
                    mDataList = new ArrayList<>();
                }
            }
        }
        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.shuaixuan);
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightLay.setVisibility(View.GONE);

        mButNext.setEnabled(false);

        initView();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.showLoading();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(SelectBankListActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                erLeiHuList();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
            }
        });

        showProgressDialog();
        erLeiHuList();
    }


    //二类户查询列表
    private void erLeiHuList(){

        mRequestTag = MethodUrl.erleiHuList;
        Map<String, String> map = new HashMap<>();
        map.put("patncode",mPatncode+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.erleiHuList, map);
    }

    //二类户绑定
    private void erLeiHuBind(){
        showProgress();

        mRequestTag = MethodUrl.erleiHuBind;
        Map<String, Object> map = new HashMap<>();
        map.put("patncode",mPatncode+"");
        map.put("crdno",mSelectBankMap.get("crdno")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.erleiHuBind, map);
    }


    private void responseData() {

        mBooleanList = new ArrayList<>();
        for (int i = 0; i<mDataList.size();i++){
            mBooleanList.add(false);
        }
        if (mSelectBankAdapter == null) {
            mSelectBankAdapter = new SelectBankAdapter(SelectBankListActivity.this);
            mSelectBankAdapter.setBooleanList(mBooleanList);
            mSelectBankAdapter.addAll(mDataList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mSelectBankAdapter);


            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);


            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mSelectBankAdapter.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });

            mSelectBankAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {
                    Map<String, Object> item = mSelectBankAdapter.getDataList().get(position);

                    if (mSelectBankAdapter != null) {
                        List<Boolean> list = mSelectBankAdapter.getBooleanList();
                        if (list != null && list.size() > 0) {
                            boolean b = false;
                            for (int i = 0; i < list.size(); i++) {
                                b = list.get(i);
                                if (b) {
                                    mButNext.setEnabled(true);
                                    break;
                                }
                            }
                            if (b){
                                mButNext.setEnabled(true);
                            }else {
                                mButNext.setEnabled(false);
                            }
                        } else {
                            mButNext.setEnabled(false);
                        }
                    }else {
                        mButNext.setEnabled(false);
                    }

                    mRefreshListView.post(new Runnable(){
                        @Override
                        public void run() {
                            mSelectBankAdapter.notifyDataSetChanged();
                            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
                        }
                    });
                }
            });



        } else {
            mSelectBankAdapter.clear();
            mSelectBankAdapter.addAll(mDataList);
            mSelectBankAdapter.setBooleanList(mBooleanList);
            mSelectBankAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        mRefreshListView.refreshComplete(10);
        mSelectBankAdapter.notifyDataSetChanged();
        if (mSelectBankAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
        }else {
            mPageView.showContent();
        }

    }




    @OnClick({R.id.back_img,R.id.right_lay, R.id.but_next,R.id.left_back_lay})
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
            case R.id.but_next:

                Map<String,Object> bankMap = null;
                if (mSelectBankAdapter != null) {
                    List<Boolean> list = mSelectBankAdapter.getBooleanList();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            boolean b = list.get(i);
                            if (b) {
                                bankMap = mSelectBankAdapter.getDataList().get(i);
                                break;
                            }
                        }
                        if (bankMap == null || bankMap.isEmpty()) {
                            showToastMsg("请选择银行卡");
                        } else {
                            mButNext.setEnabled(false);
                            PermissionsUtils.requsetRunPermission(SelectBankListActivity.this, new RePermissionResultBack() {
                                @Override
                                public void requestSuccess() {
                                    netWorkWarranty();
                                }

                                @Override
                                public void requestFailer() {
                                    toast(R.string.failure);
                                    mButNext.setEnabled(true);
                                }
                            },Permission.Group.STORAGE,Permission.Group.CAMERA);
                        }
                    } else {
                    }
                }else {
                    showToastMsg("暂无可用银行卡");
                }
                //butPressCheck();

                break;
        }
    }

    private void butPressCheck(){
        Map<String,Object> bankMap = null;
        if (mSelectBankAdapter != null) {
            List<Boolean> list = mSelectBankAdapter.getBooleanList();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    boolean b = list.get(i);
                    if (b) {
                        bankMap = mSelectBankAdapter.getDataList().get(i);
                        break;
                    }
                }
                if (bankMap == null || bankMap.isEmpty()) {
                    showToastMsg("请");
                } else {
                    mSelectBankMap = bankMap;
                    showProgressDialog();
                    erLeiHuBind();
                }
            } else {

            }
        }else {
            showToastMsg("暂无可用银行卡");
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

        switch (mType){
            case MethodUrl.erleiHuBind:
                showToastMsg(tData.get("result")+"");
                if (mViewType.equals("1")){//从  我的---银行卡  进来的
                    backTo(BankCardActivity.class,true);
                }else {
                    Intent intent = new Intent(SelectBankListActivity.this,BankBindSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case MethodUrl.erleiHuList://
                String result = tData.get("result")+"";
                if (UtilTools.empty(result)){
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                    responseData();

                }else {
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                    if (list != null){
                        mDataList.clear();
                        mDataList.addAll(list);
                    }
                    responseData();
                }
                mRefreshListView.refreshComplete(10);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.erleiHuList:
                        erLeiHuList();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        dealFailInfo(map,mType);
        switch (mType) {
            case MethodUrl.erleiHuBind:
                mButNext.setEnabled(true);
                finish();
                break;
            case MethodUrl.erleiHuList://
                if (mSelectBankAdapter != null){
                    if (mSelectBankAdapter.getDataList().size() <= 0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            erLeiHuList();
                        }
                    });
                }else {
                    mPageView.showNetworkError();
                }
                break;
        }

    }

    @Override
    public void reLoadingData() {
        showProgressDialog();
        erLeiHuList();
    }


    /**
     * -----------------------------------------------------------人脸识别代码
     */
    private static final int PAGE_INTO_LIVENESS = 101;
    private void enterNextPage() {
       // startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = null;
        Bundle bundle;
        if (requestCode == 1 ) {
            switch (resultCode){//
                case MbsConstans.FaceType.FACE_CHECK_BANK_BIND:

                    bundle = data.getExtras();
                    if (bundle == null){
                        mButNext.setEnabled(true);
                    }else {
                        butPressCheck();
                    }
                    break;
                default:
                    mButNext.setEnabled(true);
                    break;
            }

        }else if (requestCode == PAGE_INTO_LIVENESS){//人脸识别返回来的数据
            if (resultCode == RESULT_OK ) {
                bundle=data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_CHECK_BANK_BIND);
                intent = new Intent(SelectBankListActivity.this,ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent,1);
            }else {
                mButNext.setEnabled(true);
            }
        }
    }


    /**
     * 联网授权
     */
    private void netWorkWarranty() {

//        final String uuid = ConUtil.getUUIDString(this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Manager manager = new Manager(SelectBankListActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(SelectBankListActivity.this);
//                manager.registerLicenseManager(licenseManager);
//                manager.takeLicenseFromNetwork(uuid);
//                if (licenseManager.checkCachedLicense() > 0) {
//                    //授权成功
//                    mHandler.sendEmptyMessage(1);
//                }else {
//                    //授权失败
//                    mHandler.sendEmptyMessage(2);
//                }
//            }
//        }).start();
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    enterNextPage();
                    break;
                case 2:
                    showToastMsg("人脸验证授权失败");
                    mButNext.setEnabled(true);
                    break;
            }
        }
    };





    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
