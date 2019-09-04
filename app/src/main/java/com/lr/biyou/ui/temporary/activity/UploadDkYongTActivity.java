package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.MyClickableSpan;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.DataHolder;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;




import com.yanzhenjie.permission.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 上传贷款用途 界面
 */
public class UploadDkYongTActivity extends BasicActivity implements RequestView {

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

    @BindView(R.id.has_fujian_content_lay)
    LinearLayout mHasFujianContentLay;
    @BindView(R.id.has_fujian_lay)
    CardView mHasFujianLay;
    @BindView(R.id.jiekuan_money_tv)
    TextView mJiekuanMoneyTv;
    @BindView(R.id.chujieren_tv)
    TextView mChujierenTv;
    @BindView(R.id.jiekuan_qixian_tv)
    TextView mJiekuanQixianTv;
    @BindView(R.id.nian_lilv_tv)
    TextView mNianLilvTv;
    @BindView(R.id.jiekuan_giveday_tv)
    TextView mJiekuanGivedayTv;
    @BindView(R.id.has_upload_tv)
    TextView mHasUploadTv;
    @BindView(R.id.add_file_tv)
    TextView mAddFileTv;
    @BindView(R.id.file_num_tv)
    TextView mFileNumTv;
    @BindView(R.id.fujian_lay)
    CardView mFujianLay;


    @BindView(R.id.has_upload_tv2)
    TextView mHasUploadTv2;
    @BindView(R.id.add_file_tv2)
    TextView mAddFileTv2;
    @BindView(R.id.file_num_tv2)
    TextView mFileNumTv2;

    @BindView(R.id.but_submit)
    Button mSubmitBut;
    @BindView(R.id.bulu_content_lay)
    LinearLayout mBuluContentLay;


    private Map<String, Object> mDataMap;
    private Map<String, Object> mConfigMap;


    private String mRequestTag = "";

    private Map<String, Object> mDefaultMap;

    private String mSign = "1";//标记  请求的是详情  还是提交，，，因为两个请求的url是一样的，提交方式不一样，所以需要标记一下

    @Override
    public int getContentView() {
        return R.layout.activity_upload_daikyongt;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        mTitleText.setText(getResources().getString(R.string.upload_daikuan_yongt));

        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.FILE_TIP_ACTION);
        registerReceiver(receiver, filter);

        mAddFileTv.setVisibility(View.VISIBLE);
        mHasUploadTv.setVisibility(View.GONE);
        mFileNumTv.setVisibility(View.GONE);
        getModifyAction();

        mSubmitBut.setEnabled(false);
    }


    /**
     * 获取借款信息  贷后详情
     */
    private void getModifyAction() {
        mSign = "1";

        mRequestTag = MethodUrl.daihouDetail;
        Map<String, String> map = new HashMap<>();
        map.put("loansqid", mDataMap.get("loansqid") + "");//借款申请编号
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.daihouDetail, map);
    }

    /**
     * 修改后  提交贷后  附件上传
     */
    private void submitAction() {
        mSign = "2";

        mRequestTag = MethodUrl.daihouFujianSubmit;
        Map<String, Object> map = new HashMap<>();
        map.put("loansqid", mDataMap.get("loansqid") + "");
        map.put("contList", mFileList);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.daihouFujianSubmit, map);
    }


    private void initModifyValue() {
        String status = mDefaultMap.get("austat")+"";
        switch (status){
            case "0":
                mSubmitBut.setText("审核中");
                mSubmitBut.setEnabled(false);
                mBuluContentLay.setVisibility(View.GONE);
                break;
            default:
                mBuluContentLay.setVisibility(View.VISIBLE);
                initFujianView();
                mSubmitBut.setText("提交");
                mSubmitBut.setEnabled(true);
                break;
        }
        mJiekuanMoneyTv.setText(UtilTools.getRMBMoney(mDefaultMap.get("loanmoney")+""));//借款金额
        mChujierenTv.setText(mDefaultMap.get("zifangnme")+"");//出借人

        String dw = mDefaultMap.get("limitunit")+"";
        //Map<String,Object> danweiMap = SelectDataUtil.getMap(dw,SelectDataUtil.getQixianDw());
        Map<String,Object> danweiMap = SelectDataUtil.getMap(dw,SelectDataUtil.getNameCodeByType("limitUnit"));
        mJiekuanQixianTv.setText(mDefaultMap.get("loanlimit")+""+danweiMap.get("name"));//借款期限
        mNianLilvTv.setText(UtilTools.getlilv(mDefaultMap.get("loanlilv")+""));//年化利率

        String time = mDefaultMap.get("loandate")+"";
        time = UtilTools.getStringFromSting2(time,"yyyyMMdd","yyyy年MM月dd日");
        mJiekuanGivedayTv.setText(time);//借款发放日

        List<Map<String,Object>> mHasFile = ( List<Map<String,Object>>) mDefaultMap.get("existFileList");
        int num = 0;
        if (mHasFile != null ){
            for (Map<String,Object> fileMap : mHasFile){
                List<Map<String,Object>> files = (List<Map<String,Object>>) fileMap.get("files");
                for (Map<String,Object> map:files){
                    List<Map<String,Object>> timeList = (List<Map<String,Object>>) map.get("optFiles");
                    num = num+timeList.size();
                }
            }
        }

        if (num == 0){
            mHasFujianContentLay.setVisibility(View.GONE);
        }else {
            mHasFujianContentLay.setVisibility(View.VISIBLE);
        }

//        if (num != 0){
        mAddFileTv2.setVisibility(View.GONE);
        mHasUploadTv2.setVisibility(View.VISIBLE);
        mFileNumTv2.setVisibility(View.VISIBLE);

//        }else {
//            mAddFileTv2.setVisibility(View.VISIBLE);
//            mHasUploadTv2.setVisibility(View.GONE);
//            mFileNumTv2.setVisibility(View.GONE);
//        }
        mFileNumTv2.setText(num+"个");


    }

    private void initFujianView(){
        List<Map<String,Object>> mList = (List<Map<String,Object>>) mDefaultMap.get("contList") ;
        if (mList.size() == 0 ){
            mBuluContentLay.setVisibility(View.GONE);
        }else {
            mBuluContentLay.setVisibility(View.VISIBLE);
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

        //{smstoken=sms_token@3948602038bb8ecf912e0ede4a577ebd, send_tel=151****3298}
        Intent intent;
        switch (mType) {
            case MethodUrl.daihouDetail:
                if (mSign.equals("1")){
                    mDefaultMap = tData;
                    initModifyValue();
                }else if (mSign.equals("2")){
                    showToastMsg("提交成功");
                    finish();
                }
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.daihouDetail:
                        if (mSign.equals("1")){
                            getModifyAction();
                        }else if (mSign.equals("2")){
                            submitAction();
                        }
                        break;
                }
                break;

        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }


    @OnClick({R.id.back_img, R.id.fujian_lay, R.id.but_submit, R.id.has_fujian_lay,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.has_fujian_lay:
                List<Map<String,Object>> mHasFile = ( List<Map<String,Object>>) mDefaultMap.get("existFileList");
                intent = new Intent(UploadDkYongTActivity.this, ModifyFileActivity.class);
                //intent.putExtra("DATA",(Serializable) mHasFile);
                DataHolder.getInstance().save("fileList", mHasFile);
                startActivity(intent);
                break;
            case R.id.fujian_lay:
                PermissionsUtils.requsetRunPermission(UploadDkYongTActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {

                        Intent intent = new Intent(UploadDkYongTActivity.this, AddFileActivity.class);
                        intent.putExtra("DATA", (Serializable) mDefaultMap);
                        intent.putExtra("TYPE", "1");
                        startActivityForResult(intent, 300);
                    }

                    @Override
                    public void requestFailer() {
                        toast(R.string.failure);
                    }
                },Permission.Group.CAMERA,Permission.Group.STORAGE);
                break;
            case R.id.but_submit:
                mSubmitBut.setEnabled(false);
                if (mFileList == null || mFileList.size()<=0){
                    showToastMsg("请上传附件后提交");
                    mSubmitBut.setEnabled(true);
                }else {
                    int num = 0;
                    for (Map<String,Object> fileMap : mFileList){
                        List<Map<String,Object>> files = (List<Map<String,Object>>) fileMap.get("files");
                        if (files != null){
                            num = num + files.size();
                        }
                    }
                    if (num > 0){

                        PermissionsUtils.requsetRunPermission(UploadDkYongTActivity.this, new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                netWorkWarranty();
                            }

                            @Override
                            public void requestFailer() {
                                toast(R.string.failure);
                            }
                        },Permission.Group.CAMERA,Permission.Group.STORAGE);
                    }else {
                        showToastMsg("请上传附件后提交");
                        mSubmitBut.setEnabled(true);
                    }
                }
                break;
        }
    }


    private boolean isCheck = false;
    private static final int PAGE_INTO_LIVENESS = 101;

    private void enterNextPage() {
        //startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
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
//                Manager manager = new Manager(UploadDkYongTActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(UploadDkYongTActivity.this);
//                manager.registerLicenseManager(licenseManager);
//                manager.takeLicenseFromNetwork(uuid);
//                if (licenseManager.checkCachedLicense() > 0) {
//                    //授权成功
//                    mHandler.sendEmptyMessage(1);
//                } else {
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
                    mSubmitBut.setEnabled(true);
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    //adapter.setList(selectList);
                    // adapter.notifyDataSetChanged();
                    break;
                case 400:

                    Bundle bundle2 = data.getExtras();
                    if (bundle2 != null) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) bundle2.getSerializable("resultList");
                    }

                    break;
            }
        }

        Intent intent = null;
        Bundle bundle;
        if (requestCode == 1) {
            switch (resultCode) {//通过短信验证码
                case MbsConstans.FaceType.FACE_UPLOAD_USE:
                    bundle = data.getExtras();
                    if (bundle == null) {
                        isCheck = false;
                        mSubmitBut.setEnabled(true);
                    } else {
                        mSubmitBut.setEnabled(false);
                        isCheck = true;
                        submitAction();
                    }
                    break;
                default:
                    mSubmitBut.setEnabled(true);
                    break;

            }

        } else if (requestCode == PAGE_INTO_LIVENESS) {//人脸识别返回来的数据
            if (resultCode == RESULT_OK) {
                bundle = data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_UPLOAD_USE);
                intent = new Intent(UploadDkYongTActivity.this, ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent, 1);
            } else {
                mSubmitBut.setEnabled(true);
            }
        }

    }


    private List<Map<String, Object>> mFileList = new ArrayList<>();
    private int mFileNum = 0;
    //广播监听
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (MbsConstans.BroadcastReceiverAction.FILE_TIP_ACTION.equals(action)) {

                if (b != null) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) b.getSerializable("DATA");
                    int num = 0;
                    if (list != null) {
                        mDefaultMap.put("contList", list);
                        mFileList.clear();
                        for (Map<String, Object> map : list) {
                            Map<String, Object> resultMap = new HashMap<>();
                            resultMap.put("connpk", map.get("connpk") + "");
                            resultMap.put("name", map.get("name") + "");
                            List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("resultData");
                            if (files != null) {
                                resultMap.put("files", files);
                                num = num + files.size();
                            } else {
                                files = new ArrayList<>();
                                resultMap.put("files", files);
                            }
                            mFileList.add(resultMap);
                        }
                    }

                    if (num != 0) {
                        mAddFileTv.setVisibility(View.GONE);
                        mHasUploadTv.setVisibility(View.VISIBLE);
                        mFileNumTv.setVisibility(View.VISIBLE);

                    } else {
                        mAddFileTv.setVisibility(View.VISIBLE);
                        mHasUploadTv.setVisibility(View.GONE);
                        mFileNumTv.setVisibility(View.GONE);

                    }
                    mFileNum = num;
                    mFileNumTv.setText(num + "个");

                    LogUtilDebug.i("打印log日志","UploadDkYongTActivity########################" + mFileList);
                }
            }
        }
    };


    private void initXieyiView() {
        String xiyiStr = "已阅读并同意签署";
        if (mConfigMap != null) {
            List<Map<String, Object>> xieyiList = (List<Map<String, Object>>) mConfigMap.get("signConts");
            for (Map map : xieyiList) {
                final String str = map.get("pdfname") + "";
                xiyiStr = xiyiStr + "《" + str + "》、";
            }
            SpannableString sp = new SpannableString(xiyiStr);

            for (Map map : xieyiList) {
                final String str = map.get("pdfname") + "";
                setClickableSpan(sp, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent intent = new Intent(ApplyAmountActivity.this,PDFLookActivity.class);
                        startActivity(intent);*/
                    }
                }, xiyiStr, "《" + str + "》");
            }
            //mXieyiTv.setText(sp);
        }
        //添加点击事件时，必须设置
        //mXieyiTv.setMovementMethod(LinkMovementMethod.getmContext());
    }

    private SpannableString setClickableSpan(SpannableString sp, View.OnClickListener l, String str, String span) {
        sp.setSpan(new MyClickableSpan(0xff1c91ea, l), str.indexOf(span), str.indexOf(span) + span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    @Override
    public void finish() {
        super.finish();
        if (mFileNum > 0){
            FileUtils.deleteDir(MbsConstans.UPLOAD_PATH);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }




    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
