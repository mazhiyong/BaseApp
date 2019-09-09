package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.ShowDetailPictrue;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加朋友 界面
 */
public class AddFriendActivity extends BasicActivity implements RequestView, SelectBackListener {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.my_qrcode_iv)
    ImageView myQrcodeIv;
    @BindView(R.id.user_icon_iv)
    ImageView userIconIv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.add_tv)
    TextView addTv;


    private String mRequestTag = "";

    private KindSelectDialog mDialog;
    private Map<String, Object> mapData;

    private String friendId ="";

    private List<Map<String, Object>> mImageList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mapData = (Map<String, Object>) bundle.getSerializable("DATA");
            }
        }


        mTitleText.setText("添加好友");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);


        //获取我的二维码信息
        getQrCodeAction();
    }


    @OnClick({R.id.back_img, R.id.left_back_lay,R.id.iv_search, R.id.my_qrcode_iv, R.id.add_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.iv_search:
                searchFriendAction();
                break;
            case R.id.my_qrcode_iv:
                intent = new Intent(AddFriendActivity.this, ShowDetailPictrue.class);
                intent.putExtra("position",0);
                intent.putExtra("DATA",(Serializable) mImageList);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.add_tv:
                addFriendAction();
                break;
        }
    }


    private void getQrCodeAction() {
        mRequestTag = MethodUrl.CHAT_QRCODE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddFriendActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_QRCODE, map);
    }



    private void  searchFriendAction() {
        if (UtilTools.empty(etSearch.getText().toString())){
            showToastMsg("请输入检索信息");
            return;
        }

        mRequestTag = MethodUrl.CHAT_SEARCH_FRIEND;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddFriendActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("keywords", etSearch.getText()+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_SEARCH_FRIEND, map);
    }

    private void  addFriendAction() {
      /*  if (UtilTools.empty(etSearch.getText().toString())){
            showToastMsg("请输入检索信息");
            return;
        }*/

        mRequestTag = MethodUrl.CHAT_ADD_FRIEND;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddFriendActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", friendId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_ADD_FRIEND, map);
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
            case MethodUrl.CHAT_QRCODE:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        String url = tData.get("data")+"";
                        Map<String,Object> mapImage= new HashMap<>();
                        mapImage.put("url",url);
                        mImageList.add(mapImage);
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case  MethodUrl.CHAT_SEARCH_FRIEND:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data")+"")){
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            userNameTv.setText(map.get("name")+"");
                            GlideUtils.loadImage(AddFriendActivity.this,map.get("portrait")+"",userIconIv);
                            friendId = map.get("id")+"";
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }

                break;

            case MethodUrl.CHAT_ADD_FRIEND:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        showToastMsg(tData.get("msg") + "");
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;
                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddFriendActivity.this, LoginActivity.class);
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


    /**---------------------------------------------------------------------以下代码申请权限---------------------------------------------
     * Request permissions.
     */


    /**
     * activity销毁前触发的方法
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * activity恢复时触发的方法
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 30:
                String str = (String) map.get("name");

                break;

        }
    }





}
