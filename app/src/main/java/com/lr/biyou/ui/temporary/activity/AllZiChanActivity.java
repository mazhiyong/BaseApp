package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;

import android.content.IntentFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.dialog.ZhanghuListDialog;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 总资产   界面
 */
public class AllZiChanActivity extends BasicActivity implements RequestView ,SelectBackListener{

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
    @BindView(R.id.title_bar_view)
    LinearLayout mTitleBarView;
    @BindView(R.id.money_tv)
    TextView mMoneyTv;
    @BindView(R.id.keyong_money_tv)
    TextView mKeyongMoneyTv;
    @BindView(R.id.dongjie_money_tv)
    TextView mDongjieMoneyTv;
    @BindView(R.id.chongzhi_lay)
    CardView mChongzhiLay;
    @BindView(R.id.tixian_lay)
    CardView mTixianLay;
    @BindView(R.id.hezuo_tv)
    TextView mHezuoTv;


    private String mRequestTag = "";

    private Map<String,Object> mZhangHuMap ;


    private List<Map<String,Object>> mZhuangHuList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_all_zichan;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.font_c), 0);

        StatusBarUtil.setTranslucentForImageView(this, MbsConstans.ALPHA, null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(this));
        mTitleBarView.setLayoutParams(layoutParams);
        mTitleBarView.setPadding(0, UtilTools.getStatusHeight2(this), 0, 0);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE);
        registerReceiver(mBroadcastReceiver,intentFilter);

        //mTitleBarView.setBackgroundResource(R.color.font_c);
        mTitleText.setText(getResources().getString(R.string.all_zichan));
        mTitleText.setTextColor(ContextCompat.getColor(this,R.color.white));
        mBackImg.setImageResource(R.drawable.write_back);
        mBackText.setText("");
        getMoneyAction();
    }

    @Override
    public void setBarTextColor(){
        StatusBarUtil.setDarkMode(this);
    }


    /**
     * 查询账户列表
     */
    private void zhanghuAction() {
        mRequestTag = MethodUrl.zhanghuList;

        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.zhanghuList, map);
    }

    /**
     * 请求资产信息（账号资金，可用资金，冻结资金）
     */
    private void getMoneyAction() {
        //查询类型(acct:资金账户,card:银行卡)
        mRequestTag = MethodUrl.allZichan;
        Map<String, String> map = new HashMap<>();
        map.put("qry_type","acct");
        map.put("accid","");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.allZichan, map);
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
        switch (mType){
               //获取合作商的信息
            case MethodUrl.zhanghuList://
                String result = tData.get("result")+"";
                if (UtilTools.empty(result)){
                }else {
                    List<Map<String,Object>> list =   JSONUtil.getInstance().jsonToList(result);
                    if (list != null){
                        mZhuangHuList.clear();
                        mZhuangHuList.addAll(list);
                    }else {
                    }
                }
                initList();
               // getMoneyAction();
                break;
            case MethodUrl.allZichan:
                String money = UtilTools.getRMBMoney(tData.get("bal_amt")+"");
                String yue = UtilTools.getRMBMoney(tData.get("use_amt")+"");
                String dongjie = UtilTools.getRMBMoney(tData.get("frz_amt")+"");

                mMoneyTv.setText(money);
                mKeyongMoneyTv.setText(yue);
                mDongjieMoneyTv.setText(dongjie);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.zhanghuList:
                        zhanghuAction();
                        break;
                    case MethodUrl.allZichan:
                        getMoneyAction();
                        break;
                }
                break;
        }
    }



    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }


    ZhanghuListDialog mSimpleCustomPop ;
    private void initList(){
        mSimpleCustomPop =new ZhanghuListDialog(AllZiChanActivity.this,mZhuangHuList,110);
        mSimpleCustomPop.setSelectBackListener(this);
        //mSimpleCustomPop.initData(mZhuangHuList);
        if (mZhuangHuList != null && mZhuangHuList.size()>0){
            mZhangHuMap = mZhuangHuList.get(0);
            if (UtilTools.empty(mZhangHuMap.get("orgname")+"")){
                mZhangHuMap.put("orgname","暂无合作方");
                mHezuoTv.setText("暂无合作方");
            }else {
                mHezuoTv.setText(mZhangHuMap.get("orgname")+"");
            }
            getMoneyAction();
        }
    }

    private void showList(){

        mSimpleCustomPop
                //.alignCenter(true)
                .anchorView(mHezuoTv)
                .gravity(Gravity.BOTTOM)
                //.showAnim(new SlideTopEnter())
                //.dismissAnim(new SlideTopExit())
                .offset(0, 10)
                .dimEnabled(false)
                .show();
    }


    @OnClick({R.id.back_img, R.id.chongzhi_lay, R.id.tixian_lay,R.id.hezuo_tv,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent ;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.chongzhi_lay:
                intent=new Intent(this,ChongzhiTestActivity.class);
                intent.putExtra("TYPE",100);
                startActivity(intent);
                break;
            case R.id.tixian_lay:
                intent=new Intent(this,TiXianActivity.class);
                intent.putExtra("TYPE",200);
                startActivity(intent);
                break;
            case R.id.hezuo_tv:
                showList();
                break;
        }
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type){
            case 110:
                mZhangHuMap = map;
                mHezuoTv.setText(mZhangHuMap.get("orgname")+"");
                getMoneyAction();
                break;
        }
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE)){
                getMoneyAction();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
