package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.cardview.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle1.activity.HtmlActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 开户绑卡成功后签约网银
 */
public class BankQianyueActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.card_num_tv)
    TextView mCardNumTv;
    @BindView(R.id.xieyi_checkbox)
    CheckBox mXieyiCheckbox;
    @BindView(R.id.xieyi_tv)
    TextView mXieyiTv;
    @BindView(R.id.xiyi_lay)
    LinearLayout mXiyiLay;
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.tiaoguo_tv)
    TextView mTiaoGuoTv;
    @BindView(R.id.bank_name_tv)
    TextView mBankNameTv;
    @BindView(R.id.bank_image_view)
    CircleImageView mCircleImageView;
    @BindView(R.id.bank_lay)
    CardView mBankLay;


    private String mRequestTag = "";

    private Map<String,Object> mHeZuoMap = new HashMap<>();
    private String mAccid = "";


    @Override
    public int getContentView() {
        return R.layout.activity_bank_qianyue;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
//        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 60);

        mTitleText.setText(getResources().getString(R.string.but_open_wy));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mHeZuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
            if (mHeZuoMap.containsKey("accid")){
                mAccid = mHeZuoMap.get("accid")+"";
            }else if (mHeZuoMap.containsKey("crdno")){
                mAccid = mHeZuoMap.get("crdno")+"";
            }
            mCardNumTv.setText(UtilTools.getIDXing(mAccid));

            String bankName = mHeZuoMap.get("bankname")+"";
            if (UtilTools.empty(bankName)){
                bankName = "";
            }
            mBankNameTv.setText(bankName);
        }

        Glide.with(this)
                .load(mHeZuoMap.get("logopath")+"")
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        BitmapDrawable bd = (BitmapDrawable) resource;
                        Bitmap bm = bd.getBitmap();

                        Palette.from(bm).maximumColorCount(10).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@NonNull Palette palette) {
                                List<Palette.Swatch> list = palette.getSwatches();
                                int colorSize = 0;
                                Palette.Swatch maxSwatch = null;
                                for (int i = 0; i < list.size(); i++) {
                                    Palette.Swatch swatch = list.get(i);
                                    if (swatch != null) {
                                        int population = swatch.getPopulation();
                                        if (colorSize < population) {
                                            colorSize = population;
                                            maxSwatch = swatch;
                                        }
                                    }
                                }
                                if (maxSwatch != null){
                                    mBankLay.setCardBackgroundColor(maxSwatch.getRgb());
                                    mBankLay.getBackground().setAlpha((int)(0.8*255));

                                }
                            }
                        });
                        mCircleImageView.setImageBitmap(bm);
                    }
                });

        mXieyiCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mButNext.setEnabled(true);
                }else {
                    mButNext.setEnabled(false);
                }
            }
        });

        if (mXieyiCheckbox.isChecked()){
            mButNext.setEnabled(true);
        }else {
            mButNext.setEnabled(false);
        }
        initXieyi();
    }


    /**
     * 服务协议的显示信息
     */
    private void initXieyi() {

        String tip = "同意《电子银行客户服务条款》";
        int dian = tip.length();
        int end = tip.length();
        if (tip.contains("《") && tip.contains("》")) {
            dian = tip.indexOf("《");
            end = tip.indexOf("》")+1;
        } else {
            dian = tip.length();
        }

        /* 用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
        Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)
        Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)*/
        SpannableString ss = new SpannableString(tip);
        ss.setSpan(new TextSpanClick(false), dian, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.data_col)), 0, dian, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue1)), dian, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.data_col)), end, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mXieyiTv.setText(ss);
        //添加点击事件时，必须设置
        mXieyiTv.setMovementMethod(LinkMovementMethod.getInstance());

    }


    /**
     * 二类户网银签约
     * {prestate=10, iscmp=1, patnList=[{patncode=NSHCSHZF, vaccid=1831216000013914, secstatus=2,
     * accid=6235559020000001270, zifangnme=上海支行, zifangbho=NCBK6621}]}
     */
    private void qianyueAction() {
        if (mHeZuoMap == null){
            mButNext.setEnabled(true);
            return;
        }

        mRequestTag = MethodUrl.erLeihuQianYue;

        Map<String, Object> map = new HashMap<>();
        map.put("patncode",mHeZuoMap.get("patncode")+"");
        map.put("crdno",mAccid);
        Map<String, String> mHeadermap = new HashMap<>();
        LogUtilDebug.i("签约网银参数",map);
        mRequestPresenterImp.requestPostToMap(mHeadermap, MethodUrl.erLeihuQianYue, map);
    }


    private void getMsgCodeAction() {

        mRequestTag = MethodUrl.normalSms;
        Map<String, Object> map = new HashMap<>();
        //开通网银
        map.put("busi", "COMMON");

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        //mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.resetPassCode, map);
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.normalSms, map);
    }



    @OnClick({R.id.back_img, R.id.but_next,  R.id.left_back_lay,R.id.tiaoguo_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
                mButNext.setEnabled(false);
                if (!mXieyiCheckbox.isChecked()){
                    showToastMsg("请先阅读协议");
                    mButNext.setEnabled(true);
                    return;
                }
                getMsgCodeAction();
                //qianyueAction();
                break;
            case R.id.tiaoguo_tv:
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
        Intent intent;
        switch (mType) {
            case MethodUrl.normalSms:
                mButNext.setEnabled(true);
                showToastMsg("获取验证码成功");
                intent = new Intent(BankQianyueActivity.this,CodeMsgActivity.class);
                intent.putExtra("DATA",(Serializable) tData);
                intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_WANGYIN);
                intent.putExtra("showPhone", MbsConstans.USER_MAP.get("tel")+"");
                startActivityForResult(intent,1);
                break;
            case MethodUrl.erLeihuQianYue://二类户网银签约
                showToastMsg(tData.get("result")+"");
                intent = new Intent();
                intent.setAction(MbsConstans.BroadcastReceiverAction.QIAN_YUE_WY);
                sendBroadcast(intent);
                finish();
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.erLeihuQianYue:
                        qianyueAction();
                        break;
                    case MethodUrl.normalSms:
                        getMsgCodeAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mButNext.setEnabled(true);
        dealFailInfo(map,mType);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = null;
        Bundle bundle;
        if (requestCode == 1 ) {
            switch (resultCode){//通过短信验证码  安装证书
                case MbsConstans.CodeType.CODE_WANGYIN:
                    qianyueAction();
                    break;

            }

        }
    }




    private final class TextSpanClick extends ClickableSpan {
        private boolean status;

        public TextSpanClick(boolean status) {
            this.status = status;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);//取消下划线false
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BankQianyueActivity.this, HtmlActivity.class);
            intent.putExtra("id",MbsConstans.XIEYI_URL+"H5/static/html/wyxy.html");
            intent.putExtra("title","网银协议");
            //intent.putExtra("id",MbsConstans.XIEYI_URL+"H5/static/html/khxy.html?name="+MbsConstans.USER_MAP.get("name"));

            startActivity(intent);
        }
    }

}
