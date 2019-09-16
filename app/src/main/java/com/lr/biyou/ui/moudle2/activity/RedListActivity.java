package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 红包领取
 */
public class RedListActivity extends BasicActivity implements RequestView, SelectBackListener {


    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.record_iv)
    ImageView recordIv;
    @BindView(R.id.head_iv)
    ImageView headIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.money_tv)
    TextView moneyTv;
    @BindView(R.id.tip_tv)
    TextView tipTv;
    @BindView(R.id.refresh_list_view)
    LRecyclerView refreshListView;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.page_view)
    PageView pageView;
    @BindView(R.id.total_tv)
    TextView totalTv;
    @BindView(R.id.divide_line)
    View divideLine;

    @Override
    public int getContentView() {
        return R.layout.activity_red_list;
    }

    @Override
    public void setBarTextColor() {
        StatusBarUtil.setDarkMode(this);
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.red_package), MbsConstans.ALPHA);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
           String id =  bundle.getString("id");
           getRedStatusAction(id);
        }

    }

    private void getRedStatusAction(String id) {
        mRequestPresenterImp = new RequestPresenterImp(this, this);
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(RedListActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", id+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_RED_INFO, map);
    }

    @OnClick({R.id.back_iv, R.id.record_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                break;
            case R.id.record_iv:
                break;
        }
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType){
            case MethodUrl.CHAT_RED_INFO:
                switch (tData.get("code") + "") {
                    case "0": //请求成功

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(RedListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10:
                String s = (String) map.get("name"); //选择账户

                break;
            case 30: //选择币种
                String str = (String) map.get("name"); //选择币种

                break;
        }
    }



}
