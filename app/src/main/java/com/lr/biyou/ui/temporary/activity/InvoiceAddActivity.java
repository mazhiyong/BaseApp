package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.db.FaPiaoData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加发票信息
 */
public class InvoiceAddActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.code_edit)
    EditText mCodeEdit;
    @BindView(R.id.number_edit)
    EditText mNumberEdit;
    @BindView(R.id.date_tv)
    TextView mDateTv;
    @BindView(R.id.textcode_edit)
    EditText mTextcodeEdit;
    @BindView(R.id.money_edit)
    EditText mMoneyEdit;


    // private Map<String, Object> mMoneyMap;
    private Map<String, Object> mHezuoMap;
    private Map<String, Object> mConfigMap;
    private Map<String, Object> mQixianMap;


    private Map<String, Object> mParamMap;
    private String mType = "";

    private List<Map<String, Object>> mPeopleList = new ArrayList<>();


    private String mRequestTag = "";


    private DateSelectDialog mySelectDialog;

    @Override
    public int getContentView() {
        return R.layout.activity_invoice_add;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.invoice_check));


        mySelectDialog = new DateSelectDialog(this, true, "选择日期", 21);
        mySelectDialog.setSelectBackListener(this);
    }


    /**
     */
    private void getConfigAction() {

        mRequestTag = MethodUrl.jiekuanConfig;
        Map<String, String> map = new HashMap<>();
        map.put("patncode", mHezuoMap.get("patncode") + "");
        map.put("zifangbho", mHezuoMap.get("zifangbho") + "");
        map.put("creditno", mHezuoMap.get("creditno") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.jiekuanConfig, map);
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
            case MethodUrl.jiekuanHetong:

                break;
            case MethodUrl.jiekuanSubmit:
                showToastMsg("借款申请成功");
                intent = new Intent(this, ResultMoneyActivity.class);
                intent.putExtra(MbsConstans.ResultType.RESULT_KEY, MbsConstans.ResultType.RESULT_JIEKUAN);
                intent.putExtra("DATA", (Serializable) mParamMap);
                startActivity(intent);
                finish();
                break;
            case MethodUrl.jiekuanConfig:
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.jiekuanConfig:
                        getConfigAction();
                        break;
                    case MethodUrl.jiekuanHetong:
                        break;
                    case MethodUrl.jiekuanSubmit:
                        break;
                }
                break;

        }
    }


    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.jiekuanHetong:
                break;
            case MethodUrl.jiekuanSubmit:
                break;
            case MethodUrl.jiekuanConfig:
                finish();
                break;
        }

        dealFailInfo(map, mType);
    }

    String mSelectStartTime = "";
    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        String value = map.get("name") + "";
        switch (type) {
            case 21:
            mSelectStartTime = map.get("date")+"";
            String startShow = UtilTools.getStringFromSting2(mSelectStartTime,"yyyyMMdd","yyyy-MM-dd");
            mDateTv.setText(startShow);
            break;
            case 10:
                break;
        }
    }

    @OnClick({R.id.back_img, R.id.left_back_lay, R.id.gmf_lay,R.id.but_submit})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.gmf_lay:
                showDateDialog();
                break;
            case R.id.but_submit:
                insertData();
                break;
        }
    }

    private void showDateDialog() {
        mySelectDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
    }

    private void insertData() {
        if (UtilTools.isEmpty(mCodeEdit, getString(R.string.invoice_daima_hint))) {
            //showToastMsg("发票代码不能为空");
            return;
        }
        if (UtilTools.isEmpty(mNumberEdit, getString(R.string.invoice_haoma_hint))) {
            //showToastMsg("发票号码不能为空");
            return;
        }

        if (UtilTools.empty(mDateTv.getText().toString().trim())){
            showToastMsg(getString(R.string.invoice_date_hint));
            return;
        }


        if (UtilTools.isEmpty(mTextcodeEdit, getString(R.string.invoice_check_6))) {
            //showToastMsg("发票校验码不能为空");
            return;
        }
        if (UtilTools.isEmpty(mMoneyEdit, getString(R.string.invoice_money_hint))) {
            //showToastMsg("发票金额不能为空");
            return;
        }


        if (mCodeEdit.getText().toString().length() != 10 && mCodeEdit.getText().toString().length() != 12 ){
            showToastMsg("当前发票代码格式不正确，请查验!");
            return;
        }

        if (mNumberEdit.getText().toString().length() != 8 ){
            showToastMsg("当前发票号码格式不正确，请查验!");
            return;
        }

        if (mTextcodeEdit.getText().toString().length() != 6){
            showToastMsg("当前发票校验码格式不正确，请查验!");
            return;
        }


        //将发票信息录入数据库
        String fp_code = mCodeEdit.getText().toString();
        String fp_number = mNumberEdit.getText().toString();
        String fp_money = mMoneyEdit.getText().toString();
        String fp_date = mDateTv.getText().toString();


        if (FaPiaoData.getInstance().dataExist(fp_code,fp_number)){
            showToastMsg("当前发票信息已存在,请换一张试试");
        }else {
            showToastMsg("发票信息录入成功");
            FaPiaoData.getInstance().insertDB(fp_code,fp_number,fp_money,fp_date);
            Intent intent = new Intent(this, InvoiceListActivity.class);
            startActivity(intent);
            finish();
        }




    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
