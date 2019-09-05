package com.lr.biyou.ui.temporary.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.CommonSelectDialog;
import com.lr.biyou.ui.moudle4.fragment.FBTradeFragment;
import com.lr.biyou.ui.temporary.fragment.LineDataFragment2;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 我的应收账款
 */
public class MyShouMoneyActivity extends BasicActivity implements RequestView, ReLoadingData , SelectBackListener {


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
    @BindView(R.id.line_lay)
    RelativeLayout mLineLay;
    @BindView(R.id.list_lay)
    RelativeLayout mListLay;
    @BindView(R.id.main_bottom)
    LinearLayout mMainBottom;
    @BindView(R.id.fragment_container)
    RelativeLayout mFragmentContainer;


    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.ll_lay)
    LinearLayout mLlLay;

    private int index;
    private int currentTabIndex;
    private Fragment[] fragments;
    private LineDataFragment2 mLineDataFragment;
    private FBTradeFragment mListDataFragment;

    private String mRequestTag = "";

    private CommonSelectDialog mDialog;

    private Map<String, Object> mSelectKehuMap;


    private List<Map<String,Object>> mKehuList  = new ArrayList<>();



    public Map<String, Object> getSelectKehuMap() {
        return mSelectKehuMap;
    }

    public void setSelectKehuMap(Map<String, Object> selectKehuMap) {
        mSelectKehuMap = selectKehuMap;
    }



    @Override
    public int getContentView() {
        return R.layout.activity_my_shou_money;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText("我的应收账款");

        getfukuanfangInfo();

        mLineDataFragment = new LineDataFragment2();
        mListDataFragment = new FBTradeFragment();
        fragments = new Fragment[]{mLineDataFragment, mListDataFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mLineDataFragment)
                .show(mLineDataFragment)
                .commitAllowingStateLoss();


        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
    }



    @OnClick({R.id.left_back_lay, R.id.ll_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.ll_lay: //查询付款方
                if (mKehuList != null && mKehuList.size()>0){
                    mDialog = new CommonSelectDialog(this, true, mKehuList, 10);
                    mDialog.setSelectBackListener(this);
                    mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                }else {
                    getfukuanfangInfo();
                }
                break;
        }
    }

    //查询付款方
    private void getfukuanfangInfo() {
        mRequestTag = MethodUrl.payCompanyList;
        Map<String, String> map = new HashMap<>();
       /* map.put("flowdate",mSxMap.get("flowdate")+"");
        map.put("flowid",mSxMap.get("flowid")+"");
        map.put("autoid",mSxMap.get("autoid")+"");*/
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.payCompanyList, map);
    }


    @Override
    public void reLoadingData() {
        getfukuanfangInfo();
    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {
        showProgressDialog();
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
        switch (mType) {
            case MethodUrl.payCompanyList:
                mKehuList = (List<Map<String, Object>>) tData.get("payFirmInfoList");
                if (mKehuList != null && mKehuList.size()>0) {
                    //默认第一个
                    Map<String,Object> map = mKehuList.get(0);
                    setSelectKehuMap(map);
                    mNameTv.setText(map.get("payfirmname")+"");

                    ((LineDataFragment2) fragments[index]).getShouMoneyInfoLine();
                }else {
                     showToastMsg("未查询到付款方数据");
                     mNameTv.setText("暂无付款方");
                }
                break;

            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.payCompanyList:
                        getfukuanfangInfo();
                        break;
                }
                break;
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


    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.line_lay: //折线
                index = 0;
                mLineLay.setBackgroundColor(ContextCompat.getColor(this, R.color.line_background));
                mListLay.setBackgroundColor(ContextCompat.getColor(this, R.color.list_background));
                //mSwipeBackHelper.setSwipeBackEnable(false);
                break;
            case R.id.list_lay: //列表
                index = 1;
                mListLay.setBackgroundColor(ContextCompat.getColor(this, R.color.line_background));
                mLineLay.setBackgroundColor(ContextCompat.getColor(this, R.color.list_background));
                //mSwipeBackHelper.setSwipeBackEnable(true);
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            } else {
                switch (index) {
                    case 0:
                        ((LineDataFragment2) fragments[index]).getShouMoneyInfoLine();
                        break;
                    case 1:

                        break;
                }
            }
            trx.show(fragments[index]).commitAllowingStateLoss();
        }

        currentTabIndex = index;
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10:
                setSelectKehuMap(map);
                mNameTv.setText(map.get("payfirmname") + "");
                break;
        }
    }
}
