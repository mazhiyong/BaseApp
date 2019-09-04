package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mywidget.view.BankCardTextWatcher;
import com.jaeger.library.StatusBarUtil;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 开户协议 设置密码  界面
 */
public class BankOpenXieyiActivity extends BasicActivity implements RequestView {

    private String TAG = "BankOpenXieyiActivity";

    @BindView(R.id.card_num_tv)
    EditText mCardNumTv;
    @BindView(R.id.xieyi_tv)
    TextView mXieyiTv;
    @BindView(R.id.view1)
    TextView mView1;
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.containerLayout)
    LinearLayout mContainerLayout;
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
    @BindView(R.id.bank_name_tv)
    TextView mBankNameTv;
    ImageView mClear1;
    ImageView mClear2;
    @BindView(R.id.xieyi_checkbox)
    CheckBox mXieyiCheckBox;

    @BindView(R.id.bank_image_view)
    CircleImageView mBankImageView;
    @BindView(R.id.bank_bg_lay)
    CardView mBankBgView;
    @BindView(R.id.bank_open_toptips_tv)
    TextView mBankOpenTipTv;


    private String mRequestTag = "";

    private String mServerRandom = "";

    private String mAccno ="";
    private String mPatncode ="";
    private String mOpnbnknm ="";
    private String mOpnbnkid ="";
    private String mLogoPath ="";

    private String mClientPass = "";
    private String mClientRandom = "";

    private String mResultPass = "";

    private String mErLeiHuCard = "";
    private String mErLeiHuLogo = "";
    private String mErLeiBankName = "";

    @Override
    public int getContentView() {
        return R.layout.activity_bank_open_xieyi;
    }
    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
//      StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 60);

        mTitleText.setText(getResources().getString(R.string.bank_card_open_title));


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mAccno = bundle.getString("accno");
            mPatncode =  bundle.getString("patncode");
            mOpnbnknm =  bundle.getString("opnbnknm");
            mOpnbnkid =  bundle.getString("opnbnkid");
            mLogoPath =  bundle.getString("logopath");
        }

       /* mPatncode = "PHNM2018";
        mAccno = "13245546546546";*/

        Glide.with(this)
                .load(mLogoPath+"")

                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        BitmapDrawable bd = (BitmapDrawable) resource;
                        Bitmap bm = bd.getBitmap();
                        Palette p = createPaletteSync(bm );
                        //Palette.Swatch s = p.getVibrantSwatch();       //获取到充满活力的这种色调
                        //Palette.Swatch s = p.getDarkVibrantSwatch();    //获取充满活力的黑
                        //Palette.Swatch s = p.getLightVibrantSwatch();   //获取充满活力的亮
                        //Palette.Swatch s = p.getMutedSwatch();           //获取柔和的色调
                        //Palette.Swatch s = p.getDarkMutedSwatch();      //获取柔和的黑
                        //Palette.Swatch s = p.getLightMutedSwatch();    //获取柔和的亮
                        Palette.Swatch s = p.getVibrantSwatch();
                        if (s != null){
                            //设置背景颜色
                          //  mBankBgView.setCardBackgroundColor(s.getRgb());

                        }else {
                        }




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
                                    mBankBgView.setCardBackgroundColor(maxSwatch.getRgb());
                                    mBankBgView.getBackground().setAlpha((int)(0.8*255));
                                }
                            }
                        });


                        mBankImageView.setImageBitmap(bm);
                    }
                });

        initView();

        BankCardTextWatcher.bind(mCardNumTv);

        mCardNumTv.setFocusable(true);
        mCardNumTv.requestFocus();
        mCardNumTv.setEnabled(false);
        //mCardNumTv.setText(UtilTools.getIDXing(mAccno));
        mCardNumTv.setText(mAccno);
        mBankNameTv.setText(mOpnbnknm);

        String topXml = getResources().getString(R.string.bank_open_top_tips);
        String topStr = String.format(topXml,mOpnbnknm);
        mBankOpenTipTv.setText(topStr);

        initXieyi();
        serverRandomAction();
    }

    /**
     *
     * @param bitmap
     * @return
     */
    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }
    private void initView() {


    }


    /**
     * 服务协议的显示信息
     */
    private void initXieyi() {

        String tip = "同意《电子账户服务协议》";
        int dian = tip.length();
        if (tip.contains("《")) {
            dian = tip.indexOf("《");
        } else {
            dian = tip.length();
        }

       /* 用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
        Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)
        Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)*/
        SpannableString ss = new SpannableString(tip);
        ss.setSpan(new BankOpenXieyiActivity.TextSpanClick(false), dian, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.white5)), 0, dian, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)), dian, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mXieyiTv.setText(ss);
        //添加点击事件时，必须设置
        mXieyiTv.setMovementMethod(LinkMovementMethod.getInstance());

    }


    /**
     * 服务器端随机数
     */
    private void serverRandomAction() {
        mRequestTag= MethodUrl.serverRandom;
        Map<String,String> map=new HashMap<>();
        Map<String,String> mHeadermap=new HashMap<>();
        mRequestPresenterImp.requestGetToMap(mHeadermap,MethodUrl.serverRandom,map);
    }
    /**
     * 转加密获得密码
     */
    private void passJiaMi() {
        mRequestTag= MethodUrl.erLeihuPass;

        Map<String,Object> map=new HashMap<>();
        map.put("patncode",mPatncode);
        map.put("clientRandom",mClientRandom);
        map.put("serverRandom",mServerRandom);
        map.put("password",mClientPass);
        Map<String,String> mHeadermap=new HashMap<>();
        mRequestPresenterImp.requestPostToMap(mHeadermap,MethodUrl.erLeihuPass,map);
    }
    /**
     * 二类户开户提交信息
     */
    private void erleihuAction() {
        mRequestTag= MethodUrl.erLeihuPassOpen;

        Map<String,Object> map=new HashMap<>();
        map.put("patncode",mPatncode);
        map.put("crdpswd",mResultPass);
        map.put("accno",mAccno);
        map.put("opnbnkid",mOpnbnkid);
        map.put("opnbnknm",mOpnbnknm);
        Map<String,String> mHeadermap=new HashMap<>();
        mRequestPresenterImp.requestPostToMap(mHeadermap,MethodUrl.erLeihuPassOpen,map);
    }
    /**
     * 二类户开户成功后  开始绑卡
     */
    private void erleihuBind() {
        mRequestTag= MethodUrl.erleiHuBind;

        Map<String,Object> map=new HashMap<>();
        map.put("patncode",mPatncode);
        map.put("crdno",mErLeiHuCard);
        Map<String,String> mHeadermap=new HashMap<>();
        mRequestPresenterImp.requestPostToMap(mHeadermap,MethodUrl.erleiHuBind,map);
    }



    @OnClick({R.id.back_img, R.id.but_next,R.id.left_back_lay})
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
        switch (mType) {
            case  MethodUrl.serverRandom://服务器端随机数
                mServerRandom = tData.get("serverRandom")+"";
                break;
            case MethodUrl.erLeihuPass://二类户密码加密
                mResultPass = tData.get("pinPassword")+"";
                LogUtilDebug.i("南商行密码控件","转加密成功");
                erleihuAction();
                break;
            case MethodUrl.erLeihuPassOpen://提交最后开户信息
                showToastMsg("开户成功");
                mErLeiHuCard = tData.get("crdno")+"";
                mErLeiHuLogo = tData.get("logopath")+"";
                mErLeiBankName = tData.get("bankname")+"";
                erleihuBind();
                break;
            case MethodUrl.erleiHuBind:
                showToastMsg("绑卡成功");
                Intent intent2 = new Intent();
                intent2.setAction(MbsConstans.BroadcastReceiverAction.OPEN_BANK);
                sendBroadcast(intent2);

                Intent intent = new Intent(BankOpenXieyiActivity.this,BankQianyueActivity.class);
                Map<String,Object> erLeihuBank = new HashMap<>();
                erLeihuBank.put("patncode",mPatncode);
                erLeihuBank.put("crdno",mErLeiHuCard);
                erLeihuBank.put("logopath",mErLeiHuLogo);
                erLeihuBank.put("bankname",mErLeiBankName);
                intent.putExtra("DATA",(Serializable) erLeihuBank);
                startActivity(intent);
                finish();
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.serverRandom:
                        serverRandomAction();
                        break;
                    case MethodUrl.erLeihuPassOpen:
                        erleihuAction();
                        break;
                    case MethodUrl.erLeihuPass:
                        passJiaMi();
                        break;
                    case MethodUrl.erleiHuBind:
                        erleihuBind();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        dealFailInfo(map,mType);
    }

    private int offset = 0;




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
            Intent intent = new Intent(BankOpenXieyiActivity.this, HtmlActivity.class);
            String name = "";
            if (MbsConstans.USER_MAP == null){
                name = "";
            }else {
                name = MbsConstans.USER_MAP.get("name")+"";
            }
            LogUtilDebug.i("开户人姓名",name);
            intent.putExtra("title","开户协议");
            intent.putExtra("id",MbsConstans.XIEYI_URL+"H5/static/html/khxy.html?name="+name);
            startActivity(intent);
        }
    }




    private boolean isCheck = false;
    private static final int PAGE_INTO_LIVENESS = 101;
    private void enterNextPage() {
       // startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                Toast.makeText(BankOpenXieyiActivity.this, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        Intent intent = null;
        Bundle bundle;
        if (requestCode == 1 ) {
            switch (resultCode){//
                case MbsConstans.FaceType.FACE_CHECK_BANK_PASS:
                    bundle = data.getExtras();
                    if (bundle == null){
                        isCheck = false;
                    }else {
                        isCheck = true;
                    }
                    mButNext.setEnabled(true);
                    break;
                default:
                    mButNext.setEnabled(true);
                    break;

            }

        }else if (requestCode == PAGE_INTO_LIVENESS){//人脸识别返回来的数据
            if (resultCode == RESULT_OK ) {
                bundle=data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_CHECK_BANK_PASS);
                intent = new Intent(BankOpenXieyiActivity.this,ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent,1);
            }else {
                mButNext.setEnabled(true);
            }
        }
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {

//        final String uuid = ConUtil.getUUIDString(this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Manager manager = new Manager(BankOpenXieyiActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(BankOpenXieyiActivity.this);
//                manager.registerLicenseManager(licenseManager);
//                manager.takeLicenseFromNetwork(uuid);
//                if (licenseManager.checkCachedLicense() > 0) {
//                    //授权成功
//                    mHandler.sendEmptyMessage(1);
//                }else {
//                    //授权失败
//                    mHandler.sendEmptyMessage(2);
//                }
//            }
//        }).start();
    }




    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    enterNextPage();
                    break;
                case 2:
                    showToastMsg("人脸验证授权失败");
                    mButNext.setEnabled(true);
                    break;
            }
        }
    };







    private static final int REQUEST_CODE_SETTING = 10011;



    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
