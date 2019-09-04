package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;

import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.dialog.MySelectDialog;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.IdCardUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加共同借款人   界面
 */
public class AddSamePeopleActivity extends BasicActivity implements RequestView,SelectBackListener{

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
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.guanxi_lay)
    CardView mGuanxiLay;
    @BindView(R.id.other_line)
    View mOtherLine;
    @BindView(R.id.other_valut_edit)
    EditText mOtherValutEdit;
    @BindView(R.id.other_lay)
    CardView mOtherLay;
    @BindView(R.id.idcard_valut_edit)
    EditText mIdcardValutEdit;
    @BindView(R.id.really_name_value_edit)
    EditText mReallyNameValueEdit;
    @BindView(R.id.but_next)
    Button mButNext;


    private MySelectDialog mGxDialog;

    private Map<String,Object> mGxMap ;

    private String mRequestTag = "";

    private String mIdno = "";
    private String mName = "";

    private Map<String,Object> mHezuoMap;
    char[] IDCARD = new char[]{'1','2','3','4','5','6','7','8','9','0','x','X','y','Y'};


    @Override
    public int getContentView() {
        return R.layout.activity_add_sp;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mHezuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }

        mTitleText.setText(getResources().getString(R.string.add_people_title));

        //List<Map<String, Object>> list = SelectDataUtil.guanxiPeople();
        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("unionRel");
        mGxDialog= new MySelectDialog(this, true, list, "选择关系", 30);
        mGxDialog.setSelectBackListener(this);

        mOtherLay.setVisibility(View.GONE);
        mOtherLine.setVisibility(View.GONE);


       mIdcardValutEdit.setKeyListener(new NumberKeyListener() {
           @NonNull
           @Override
           protected char[] getAcceptedChars() {
               return IDCARD;
           }

           @Override
           public int getInputType() {
               return  android.text.InputType.TYPE_CLASS_PHONE;
           }
       });

    }


    private void submitAction() {

        mName = mReallyNameValueEdit.getText()+"";
        mIdno = mIdcardValutEdit.getText()+"";

        if (mGxMap == null || mGxMap.isEmpty()){
            UtilTools.isEmpty(mNameTv,"共同借款人关系");
            showToastMsg("请选择共同借款人关系");
            return;
        }

        String code = mGxMap.get("code")+"";
        if (code.equals("3")){
            if (UtilTools.isEmpty(mOtherValutEdit,"其它")){
                showToastMsg("其它不能为空");
                return;
            }
        }


        if (UtilTools.isEmpty(mIdcardValutEdit,"身份证号")){
            showToastMsg("身份证号不能为空");
            return;
        }


        IdCardUtil idCardUtil = new IdCardUtil(mIdno);
        int correct = idCardUtil.isCorrect();
        String msg = idCardUtil.getErrMsg();
        if (0 == correct) {// 符合规范

        }else {
            showToastMsg(msg);
            return;
        }


        if (UtilTools.isEmpty(mReallyNameValueEdit,"真实姓名")){
            showToastMsg("真实姓名不能为空");
            return;
        }



        mRequestTag =  MethodUrl.addPeople;
        Map<String, Object> map = new HashMap<>();


        LogUtilDebug.i("添加共同借款人信息",map);

        map.put("name",mName);
        map.put("idno",mIdno);
        map.put("patncode",mHezuoMap.get("patncode")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.addPeople, map);
    }

    private void check(){

    }


    @OnClick({R.id.back_img, R.id.guanxi_lay, R.id.but_next,R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.guanxi_lay:
                mGxDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.but_next:
                submitAction();
                break;
        }
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type){
            case 30:
                mGxMap = map;
                String code = mGxMap.get("code")+"";
                if (code.equals("3")){
                    mOtherLay.setVisibility(View.VISIBLE);
                    mOtherLine.setVisibility(View.VISIBLE);
                }else {
                    mOtherLay.setVisibility(View.GONE);
                    mOtherLine.setVisibility(View.GONE);
                }

                mNameTv.setText(mGxMap.get("name")+"");
                mNameTv.setError(null,null);
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
        switch (mType){
            case MethodUrl.addPeople://{custid=null}

                Intent intent = new Intent();
                intent.putExtra("name",mReallyNameValueEdit.getText()+"");
                intent.putExtra("idno",mIdcardValutEdit.getText()+"");
                intent.putExtra("guanxi",mGxMap.get("code")+"");
                intent.putExtra("custid",tData.get("custid")+"");
                String otherStr = "";
                String code = mGxMap.get("code")+"";
                if (code.equals("3")){
                    otherStr = mOtherValutEdit.getText()+"";
                }
                intent.putExtra("other",otherStr);
                setResult(RESULT_OK, intent);
                finish();
                showToastMsg("添加借款人成功");
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.addPeople:
                        submitAction();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }
}
