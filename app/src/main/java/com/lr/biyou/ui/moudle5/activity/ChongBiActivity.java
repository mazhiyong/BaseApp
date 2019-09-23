package com.lr.biyou.ui.moudle5.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 充币
 */
public class ChongBiActivity extends BasicActivity implements RequestView {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.tv)
    TextView mTextView;

    Map<String, Object> mapData = new HashMap<>();
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.head_image)
    ImageView headImage;
    @BindView(R.id.save_tv)
    TextView saveTv;
    @BindView(R.id.link_tv)
    TextView linkTv;
    @BindView(R.id.copy_link_tv)
    TextView copyLinkTv;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.chongbi_tv)
    TextView chongbiTv;

    private String symbol = "";
    private String mRequestTag = "";
    private String imgUrl = "";

    private ClipboardManager mClipboardManager;
    private ClipData clipData;

    @Override
    public int getContentView() {
        return R.layout.activity_chong_bi;
    }


    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mapData = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        symbol = mapData.get("symbol") + "";
        mTitleText.setText("充币" + symbol);
        mTitleText.setCompoundDrawables(null, null, null, null);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.icon6_dingdan);

        mTextView.setText("温馨提示：\n" +
                "禁止向" + symbol + "地址充值除" + symbol + "之外的资产,任何冲入" + symbol + "地址的非" + symbol + "资产将不可找回");

        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        getBiMessageAction();

        headImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!UtilTools.empty(imgUrl)){
                    GlideUtils.downloadImage(ChongBiActivity.this,imgUrl);
                }
                return true;
            }
        });


        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0 && !UtilTools.empty(etNumber.getText()+"")){
                    chongbiTv.setEnabled(true);
                }else {
                    chongbiTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0 && !UtilTools.empty(etAddress.getText()+"")){
                    chongbiTv.setEnabled(true);
                }else {
                    chongbiTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getBiMessageAction() {

        mRequestTag = MethodUrl.CHONG_BI;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChongBiActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("symbol",symbol);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHONG_BI, map);
    }


    @OnClick({R.id.back_img, R.id.right_lay, R.id.head_image, R.id.copy_link_tv,R.id.chongbi_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.right_lay: //充提记录
                intent = new Intent(ChongBiActivity.this, TradeListActivity.class);
                startActivity(intent);
                break;
            case R.id.head_image:
                if (!UtilTools.empty(imgUrl)){
                    GlideUtils.downloadImage(ChongBiActivity.this,imgUrl);
                }

                break;

            case R.id.copy_link_tv:
                clipData = ClipData.newPlainText("币友",linkTv.getText().toString()+"");
                mClipboardManager.setPrimaryClip(clipData);
                showToastMsg("复制成功");
                break;

            case R.id.chongbi_tv:
                chongbiSumbitAction();
                chongbiTv.setEnabled(false);
                break;


        }
    }

    private void chongbiSumbitAction() {

        String number = etNumber.getText().toString();
        String address = etAddress.getText().toString();

        if (UtilTools.empty(number)){
            showToastMsg("请输入充值数量");
            chongbiTv.setEnabled(true);
            return;
        }
        if (UtilTools.empty(address)){
            showToastMsg("请输入充值地址");
            chongbiTv.setEnabled(true);
            return;
        }
        showProgressDialog();
        mRequestTag = MethodUrl.CHONG_BI_DEAL;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChongBiActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("symbol",symbol);
        map.put("number",number);
        map.put("address",address);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHONG_BI_DEAL, map);
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
        Intent intent;
        switch (mType){
            case MethodUrl.CHONG_BI:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            linkTv.setText(map.get("wallet_address")+"");
                            imgUrl = map.get("payment")+"";
                            GlideUtils.loadImage(ChongBiActivity.this,imgUrl,headImage);
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(ChongBiActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case MethodUrl.CHONG_BI_DEAL:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        showToastMsg(tData.get("msg") + "");
                        finish();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(ChongBiActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                chongbiTv.setEnabled(true);
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        chongbiTv.setEnabled(true);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
