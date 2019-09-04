package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.JkHetongAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  合同展示信息   界面
 */
public class HeTongShowActivity extends BasicActivity implements RequestView,ReLoadingData{

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
    RecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    private String mRequestTag ="";


    private List<Map<String,Object>> mDataList; //获取图片信息

    private JkHetongAdapter mHetongAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_hetong_show;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.borrow_hetong_info));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataList = (List<Map<String,Object>>) bundle.getSerializable("DATA");
        }
        if(mDataList == null){
            mDataList = new ArrayList<>();
        }


        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.shuaixuan);
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightLay.setVisibility(View.GONE);
        initView();
        initList();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        //mPageView.showEmpty();
        mPageView.showContent();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(HeTongShowActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);


    }

    private void initList(){
        mPageView.showContent();

        mHetongAdapter = new JkHetongAdapter(HeTongShowActivity.this,mDataList);
        mRefreshListView.setAdapter(mHetongAdapter);

    }



    @OnClick({R.id.back_img,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
        }
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

        Intent intent ;
        switch (mType){
            case MethodUrl.creUploadFile://

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.creUploadFile:
//                        UPLOAD_FILE();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {

        switch (mType) {
            case MethodUrl.creUploadFile://

                break;
        }

        dealFailInfo(map,mType);
    }

    @Override
    public void reLoadingData() {
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
