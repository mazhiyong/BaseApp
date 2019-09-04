package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.SwipeMenuSxAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.DataHolder;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SelectDataUtil;
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
 * 我的额度  -- 点击列表  -- 授信详情界面  此处为预授信
 */
public class ShouxinPreDetailActivity extends BasicActivity implements RequestView {
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
    @BindView(R.id.shouxin_edu_tv)
    TextView mShouxinEduTv;
    @BindView(R.id.jiekuan_qixian_tv)
    TextView mJiekuanQixianTv;
    @BindView(R.id.huankuan_zhouqi_tv)
    TextView mHuankuanZhouqiTv;
    @BindView(R.id.lilv_type_tv)
    TextView mLilvTypeTv;
    @BindView(R.id.daikuan_zhonglei_tv)
    TextView mDaikuanZhongleiTv;
    @BindView(R.id.daikuan_yongtu_tv)
    TextView mDaikuanYongtuTv;
    @BindView(R.id.huankuan_fangshi_tv)
    TextView mHuankuanFangshiTv;
    @BindView(R.id.shengqing_date_tv)
    TextView mShengqingDateTv;
    @BindView(R.id.zhuangtai_tv)
    TextView mZhuangtaiTv;
    @BindView(R.id.same_people_list)
    LRecyclerView mSamePeopleList;
    @BindView(R.id.fujian_lay)
    CardView mFujianLay;
    @BindView(R.id.other_zhouqi_tv)
    TextView mOtherZhouqiTv;
    @BindView(R.id.other_zhouqi_lay)
    CardView mOtherZhouqiLay;
    @BindView(R.id.same_people_lay)
    LinearLayout mSamePeopleLay;


    private String mRequestTag = "";

    private Map<String, Object> mDataMap;

    private List<Map<String, Object>> mPeopleList = new ArrayList<>();

    private SwipeMenuSxAdapter mSwipeMenuAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;


    private Map<String, Object> mPayZhouQiMap;
    private Map<String, Object> mLilvMap;
    private Map<String, Object> mDaikuanUseMap;
    private Map<String, Object> mDaikuanZhonglMap;
    private Map<String, Object> mJieKuanQxianMap;

    private Map<String, Object> mHuanKuanTypeMap;
    private Map<String, Object> mDefaultMap;


    @Override
    public int getContentView() {
        return R.layout.activity_pre_shouxin_detail;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }

        mTitleText.setText(getResources().getString(R.string.my_larger_num));
        getModifyAction();
    }

    /**
     * 得到预授信详情  修改申请授信信息  比如驳回了要修改
     */
    private void getModifyAction(){

        mRequestTag = MethodUrl.reqShouxinDetail;
        Map<String, String> map = new HashMap<>();
        map.put("precreid",mDataMap.get("precreid")+ "");//预授信申请ID
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.reqShouxinDetail, map);
    }

    private void initModifyValue() {


        mShouxinEduTv.setText(UtilTools.getRMBMoney(mDefaultMap.get("creditmoney") + ""));
        //mJieKuanQxianMap = SelectDataUtil.getMap(mDefaultMap.get("singlelimit") + "", SelectDataUtil.jieKuanLimit());
        mJieKuanQxianMap = SelectDataUtil.getMap(mDefaultMap.get("singlelimit") + "", SelectDataUtil.getNameCodeByType("loanLimit"));
        mJiekuanQixianTv.setText(mJieKuanQxianMap.get("name") + "");//借款期限

        //mPayZhouQiMap = SelectDataUtil.getMap(mDefaultMap.get("interestaccmode") + "", SelectDataUtil.getHkZhouqi());
        mPayZhouQiMap = SelectDataUtil.getMap(mDefaultMap.get("interestaccmode") + "", SelectDataUtil.getNameCodeByType("repayCycle"));
        mHuankuanZhouqiTv.setText(mPayZhouQiMap.get("name") + "");
        if ((mPayZhouQiMap.get("code") + "").equals("19")) {
            mOtherZhouqiTv.setText(mDefaultMap.get("interestaccnm")+"");
            mOtherZhouqiLay.setVisibility(View.VISIBLE);
        } else {
            mOtherZhouqiLay.setVisibility(View.GONE);
        }

        mLilvMap = SelectDataUtil.getMap(mDefaultMap.get("lvtype") + "", SelectDataUtil.getLilvType());
        mLilvTypeTv.setText(mLilvMap.get("name") + "");

        //mDaikuanZhonglMap = SelectDataUtil.getMap(mDefaultMap.get("reqloantp") + "", SelectDataUtil.getDaikuanType());
        mDaikuanZhonglMap = SelectDataUtil.getMap(mDefaultMap.get("reqloantp") + "", SelectDataUtil.getNameCodeByType("loanType"));
        mDaikuanZhongleiTv.setText(mDaikuanZhonglMap.get("name") + "");

        //mDaikuanUseMap = SelectDataUtil.getMap(mDefaultMap.get("loanuse") + "", SelectDataUtil.getDaikuanUse());
        mDaikuanUseMap = SelectDataUtil.getMap(mDefaultMap.get("loanuse") + "", SelectDataUtil.getNameCodeByType("loanUse"));
        mDaikuanYongtuTv.setText(mDaikuanUseMap.get("name") + "");

        //mHuanKuanTypeMap = SelectDataUtil.getMap(mDefaultMap.get("hktype") + "", SelectDataUtil.getHkType());
        mHuanKuanTypeMap = SelectDataUtil.getMap(mDefaultMap.get("hktype") + "", SelectDataUtil.getNameCodeByType("repayWay"));
        mHuankuanFangshiTv.setText(mHuanKuanTypeMap.get("name") + "");

        String mStatus = mDefaultMap.get("creditstate")+"";
        if (mStatus.equals("14")){
            mZhuangtaiTv.setText("已生效");
        }else {
            mZhuangtaiTv.setText("审核中");
        }

        String time = mDefaultMap.get("sqdate")+"";
        String showTime = UtilTools.getStringFromSting2(time,"yyyyMMdd","yyyy-MM-dd");
        mShengqingDateTv.setText(showTime);

        mPeopleList = new ArrayList<>((List<Map<String, Object>>) mDefaultMap.get("gtList"));
        if (mPeopleList == null || mPeopleList.size() == 0){
            mSamePeopleLay.setVisibility(View.GONE);
        }else {
            mSamePeopleLay.setVisibility(View.VISIBLE);
        }

        responseData();

        List<Map<String, Object>> mFileTypeList = (List<Map<String, Object>>) mDefaultMap.get("contList");

        List<Map<String, Object>> mHasFile = (List<Map<String, Object>>) mDefaultMap.get("existFileList");
        int num = 0;
        if (mHasFile != null) {
            for (Map<String, Object> fileMap : mHasFile) {
                List<Map<String, Object>> files = (List<Map<String, Object>>) fileMap.get("files");
                for (Map<String, Object> map : files) {
                    List<Map<String, Object>> timeList = (List<Map<String, Object>>) map.get("optFiles");
                    num = num + timeList.size();
                }
            }
        }

        /*if (num != 0){
            mAddFileTv2.setVisibility(View.GONE);
            mHasUploadTv2.setVisibility(View.VISIBLE);
            mFileNumTv2.setVisibility(View.VISIBLE);

        }else {
            mAddFileTv2.setVisibility(View.VISIBLE);
            mHasUploadTv2.setVisibility(View.GONE);
            mFileNumTv2.setVisibility(View.GONE);

        }
        mFileNumTv2.setText(num+"个");*/


    }

    private void responseData() {


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mSamePeopleList.setLayoutManager(manager);
        mSwipeMenuAdapter = new SwipeMenuSxAdapter(this);
        mSwipeMenuAdapter.setSwipeEnable(false);
        mSwipeMenuAdapter.setDataList(mPeopleList);

       /* mSwipeMenuAdapter.setOnDelListener(new SwipeMenuAdapter2.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(ShouxinPreDetailActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();

                mPeopleList.remove(pos);
                responseData();
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
            }

            @Override
            public void onTop(int pos) {//置顶功能有bug，后续解决

            }
        });*/

        AnimationAdapter adapter = new ScaleInAnimationAdapter(mSwipeMenuAdapter);
        adapter.setFirstOnly(false);
        adapter.setDuration(500);
        adapter.setInterpolator(new OvershootInterpolator(.5f));


        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
        //mLRecyclerViewAdapter.addHeaderView(headerView);
        mSamePeopleList.setAdapter(mLRecyclerViewAdapter);
        mSamePeopleList.setItemAnimator(new DefaultItemAnimator());
        //mSamePeopleList.setHasFixedSize(true);
        mSamePeopleList.setNestedScrollingEnabled(false);

        mSamePeopleList.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        mSamePeopleList.setPullRefreshEnabled(false);
        mSamePeopleList.setLoadMoreEnabled(false);

        mSamePeopleList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mSamePeopleList.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        mSamePeopleList.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
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
        switch (mType) {
            case MethodUrl.reqShouxinDetail://
                mDefaultMap = tData;
                initModifyValue();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.reqShouxinDetail:
                        getModifyAction();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }


    @OnClick({R.id.back_img, R.id.left_back_lay, R.id.fujian_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.fujian_lay:
                List<Map<String,Object>> mHasFile = ( List<Map<String,Object>>) mDefaultMap.get("existFileList");
                intent = new Intent(ShouxinPreDetailActivity.this, ModifyFileActivity.class);
                //intent.putExtra("DATA",(Serializable) mHasFile);
                DataHolder.getInstance().save("fileList", mHasFile);
                startActivity(intent);
                break;
        }
    }


}
