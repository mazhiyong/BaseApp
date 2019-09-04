package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 预付款添加合同   界面
 */
public class AdvanceHtAddActivity extends BasicActivity implements RequestView, SelectBackListener {

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

    @BindView(R.id.has_upload_tv)
    TextView mHasUploadTv;
    @BindView(R.id.add_file_tv)
    TextView mAddFileTv;
    @BindView(R.id.file_num_tv)
    TextView mFileNumTv;
    @BindView(R.id.but_submit)
    Button mButSubmit;//下一步
    @BindView(R.id.hetong_code_edit)
    EditText mHetongCodeEdit;
    @BindView(R.id.ghf_edit)
    EditText mGhfEdit;
    @BindView(R.id.money_edit)
    EditText mMoneyEdit;
    @BindView(R.id.gmf_value_tv)
    TextView mGmfValueTv;
    @BindView(R.id.qd_date_value_tv)
    TextView mQdDateValueTv;
    @BindView(R.id.qd_date)
    CardView mQdDate;
    @BindView(R.id.jh_type_edit)
    EditText mJhTypeEdit;
    @BindView(R.id.sh_add_edit)
    EditText mShAddEdit;
    @BindView(R.id.zy_edit)
    EditText mZyEdit;
    @BindView(R.id.count_tv)
    TextView mCountTv;
    @BindView(R.id.cjr_value_tv)
    TextView mCjrValueTv;
    @BindView(R.id.fujian_lay)
    CardView mFujianLay;
    @BindView(R.id.fujian_content_lay)
    LinearLayout mFujianContentLay;

    private int MAX_COUNT = 42;

    private Map<String, Object> mHezuoMap = new HashMap<>();
    private Map<String, Object> mConfigMap = new HashMap<>();
    private Map<String, Object> mParamMap;
    private String mRequestTag = "";
    private DateSelectDialog mQdDateDialog;
    private String mQdDateStr = ""; //签订日期


    @Override
    public int getContentView() {
        return R.layout.activity_advanceht_add;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.add_ht));
        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.FILE_TIP_ACTION);
        registerReceiver(receiver, filter);
        initDialog();
        mAddFileTv.setVisibility(View.VISIBLE);
        mHasUploadTv.setVisibility(View.GONE);
        mFileNumTv.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mHezuoMap = (Map<String, Object>) intent.getSerializableExtra("DATA");
        }

        // 先去掉监听器，否则会出现栈溢出
        mZyEdit.addTextChangedListener(mTextWatcher);
        setLeftCount();
        mZyEdit.setSelection(mZyEdit.length());

        if (MbsConstans.USER_MAP != null){
            String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
            if (kind.equals("1")) {
                mGmfValueTv.setText(MbsConstans.USER_MAP.get("comname") + "");
            } else {
                mGmfValueTv.setText(MbsConstans.USER_MAP.get("name") + "");
            }
        }
        initContList();
        UtilTools.setMoneyEdit(mMoneyEdit, 0);

    }


    private void initDialog() {
        //签订日期
        mQdDateDialog = new DateSelectDialog(this, true, "选择日期", 2001);
        mQdDateDialog.setSelectBackListener(this);

    }


    /**
     * 查询分子公司信息
     */
    private void getCompanyList() {
        mRequestTag = MethodUrl.childfirm;
        Map<String, String> map = new HashMap<>();
        map.put("firmname", "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.childfirm, map);
    }


    private void addHetong() {

        mRequestTag = MethodUrl.addbillHt;

        String htCode = mHetongCodeEdit.getText() + "";//合同编号
        if (UtilTools.isEmpty(mHetongCodeEdit, getString(R.string.ht_code))) {
            mButSubmit.setEnabled(true);
            return;
        }

        String ghf = mGhfEdit.getText()+"";//供货方
        if (UtilTools.isEmpty(mGhfEdit, getString(R.string.ghf))) {
            mButSubmit.setEnabled(true);
            return;
        }
        String money = mMoneyEdit.getText() + "";//金额
        if (UtilTools.isEmpty(mMoneyEdit, getString(R.string.jine))) {
            mButSubmit.setEnabled(true);
            return;
        }


        //签订日期
        if (UtilTools.isEmpty(mQdDateValueTv, getString(R.string.qian_date))) {
            mButSubmit.setEnabled(true);
            return;
        }

        //交货方式
        String jhType = mJhTypeEdit.getText()+"";
        if (UtilTools.isEmpty(mJhTypeEdit, getString(R.string.jh_type))) {
            mButSubmit.setEnabled(true);
            return;
        }

        //收货地址
        String shAddress = mShAddEdit.getText()+"";
        if (UtilTools.isEmpty(mShAddEdit, getString(R.string.sh_add))) {
            mButSubmit.setEnabled(true);
            return;
        }

        //收货地址
        String shuoming = mZyEdit.getText()+"";
        if (UtilTools.isEmpty(mZyEdit, getString(R.string.sm))) {
            mButSubmit.setEnabled(true);
            return;
        }

        mParamMap = new HashMap<>();
        mParamMap.put("billmemo", shuoming);//说明
        mParamMap.put("billid", htCode);//合同编号
        mParamMap.put("dly", jhType);//交货方式
        mParamMap.put("patncode", mHezuoMap.get("patncode")+"");//合作方编号
        mParamMap.put("sgndt", mQdDateStr);//签订日期
        mParamMap.put("dlyaddr", shAddress);//收货地址
        mParamMap.put("ghsnm", ghf);//供货方
        mParamMap.put("totalamt", money);//合同金额
        mParamMap.put("contList", "");//附件列表

        List<Map<String, Object>> fileConfigList = (List<Map<String, Object>>) mConfigMap.get("contList");
        if (fileConfigList != null) {
            for (Map map1 : fileConfigList) {
                String sign = map1.get("isreq") + "";//是否必传(0:否1是)
                String filetype = map1.get("filetype") + "";

                if (sign.equals("1")) {
                    if (mFileList == null || mFileList.size() <= 0) {
                        showToastMsg("请上传必传的附件");
                        return;
                    } else {
                        for (Map map2 : mFileList) {
                            String code = map2.get("filetype") + "";
                            List<Map<String, Object>> files = (List<Map<String, Object>>) map2.get("files");
                            if (code.equals(filetype)) {
                                if (files == null || files.size() <= 0) {
                                    showToastMsg("请上传必传的附件");
                                    return;
                                }

                            }
                        }
                    }
                } else {

                }
            }
        }

        mParamMap.put("contList", mFileList);
        LogUtilDebug.i("打印log日志", "提交借款申请的参数" + mParamMap);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.addbillHt, mParamMap);

    }


    private void initContList() {

        mConfigMap = new HashMap<>();

        List<Map<String, Object>> mContList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("filetype", "01");
        map.put("name", "上传附件");
        mContList.add(map);

        mConfigMap.put("contList", mContList);
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
            case MethodUrl.addbillHt:
                mButSubmit.setEnabled(true);
                showToastMsg("添加合同成功");
                intent = new Intent();
                intent.setAction(MbsConstans.BroadcastReceiverAction.HTONGUPDATE);
                sendBroadcast(intent);
                finish();
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.addbillHt:
                        addHetong();
                        break;
                }
                break;

        }
    }


    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mButSubmit.setEnabled(true);
        switch (mType) {
            case MethodUrl.addbillHt:
                break;
        }

        dealFailInfo(map, mType);
    }

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
                case 300:
                    Bundle bundle2 = data.getExtras();
                    if (bundle2 != null) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) bundle2.getSerializable("resultList");
                    }
                    break;
            }
        }


    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        String value = map.get("name") + "";
        switch (type) {
            case 2001:
                mQdDateStr = map.get("date") + "";
                mQdDateValueTv.setText(map.get("year") + "-" + map.get("month") + "-" + map.get("day") + "");
                mQdDateValueTv.setError(null, null);
                break;

        }
    }

    @OnClick({R.id.back_img, R.id.fujian_lay, R.id.left_back_lay,R.id.qd_date, R.id.but_submit})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.fujian_lay:

                PermissionsUtils.requsetRunPermission(AdvanceHtAddActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        Intent intent = new Intent(AdvanceHtAddActivity.this, AddFileActivity.class);
                        intent.putExtra("DATA", (Serializable) mConfigMap);
                        intent.putExtra("TYPE", "2");
                        startActivityForResult(intent, 300);
                    }

                    @Override
                    public void requestFailer() {
                        showToastMsg(R.string.failure);
                    }
                }, Permission.Group.CAMERA, Permission.Group.STORAGE);
                break;

            case R.id.qd_date://选择签订日期
                mQdDateDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.but_submit:
                LogUtilDebug.i("show", "0000000");
                mButSubmit.setEnabled(false);
                addHetong();
                // finish();
                break;

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
                        mConfigMap.put("contList", list);
                        mFileList.clear();
                        for (Map<String, Object> map : list) {
                            Map<String, Object> resultMap = new HashMap<>();
                            resultMap.put("filetype", map.get("filetype") + "");
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
                    LogUtilDebug.i("打印log日志", "上传文件列表" + mFileList);
                }
            } else if (action.equals(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE)) {
                finish();
            }
        }
    };


    @Override
    public void finish() {
        super.finish();
        if (mFileNum > 0) {
            FileUtils.deleteDir(MbsConstans.UPLOAD_PATH);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }



    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;

        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = mZyEdit.getSelectionStart();
            editEnd = mZyEdit.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            mZyEdit.removeTextChangedListener(mTextWatcher);

            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            mZyEdit.setText(s);
            mZyEdit.setSelection(editStart);

            // 恢复监听器
            mZyEdit.addTextChangedListener(mTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    };


    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                //len++;
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 刷新剩余输入字数,最大值新浪微博是140个字，人人网是200个字
     */
    private void setLeftCount() {
        mCountTv.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }

    /**
     * 获取用户输入的分享内容字数
     *
     * @return
     */
    private long getInputCount() {
        return calculateLength(mZyEdit.getText().toString());
    }


}
