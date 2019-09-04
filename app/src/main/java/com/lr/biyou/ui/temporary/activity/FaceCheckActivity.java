package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.jaeger.library.StatusBarUtil;




import com.yanzhenjie.permission.Permission;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  人脸认证界面  界面
 */
public class FaceCheckActivity extends BasicActivity  implements RequestView {

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
    @BindView(R.id.my_image)
    ImageView mMyImage;
    @BindView(R.id.face_but)
    Button mFaceBut;
    @BindView(R.id.loading_layout_WarrantyBar)
    ProgressBar mLoadingLayoutWarrantyBar;
    @BindView(R.id.loading_layout_WarrantyText)
    TextView mLoadingLayoutWarrantyText;
    @BindView(R.id.loading_layout_barLinear)
    LinearLayout mLoadingLayoutBarLinear;
    @BindView(R.id.loading_layout_againWarrantyBtn)
    Button mLoadingLayoutAgainWarrantyBtn;
    @BindView(R.id.loading_expiretime)
    TextView mLoadingExpiretime;

    private String mAuthFlow = "";
    private String mIdName = "";
    private String mIdNum = "";

    @Override
    public int getContentView() {
        return R.layout.activity_face_check;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mAuthFlow = bundle.getString("auth_flow");
            mIdName = bundle.getString("name");
            mIdNum = bundle.getString("idno");

        }

        mTitleText.setText(getResources().getString(R.string.face_title));
        netWorkWarranty();
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {
//
//       final String uuid = ConUtil.getUUIDString(this);
//
//
//        mLoadingLayoutAgainWarrantyBtn.setVisibility(View.GONE);
//        mLoadingLayoutBarLinear.setVisibility(View.VISIBLE);
//        mLoadingLayoutAgainWarrantyBtn.setVisibility(View.GONE);
//        mLoadingLayoutWarrantyText.setText(R.string.meglive_auth_progress);
//        mLoadingLayoutWarrantyBar.setVisibility(View.VISIBLE);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Manager manager = new Manager(FaceCheckActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(FaceCheckActivity.this);
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
                    mLoadingLayoutAgainWarrantyBtn.setVisibility(View.GONE);
                    mLoadingLayoutBarLinear.setVisibility(View.GONE);
                    break;
                case 2:
                    mLoadingLayoutAgainWarrantyBtn.setVisibility(View.VISIBLE);
                    mLoadingLayoutWarrantyText.setText(R.string.meglive_auth_failed);
                    mLoadingLayoutWarrantyBar.setVisibility(View.GONE);
                    break;
            }
        }
    };





    private void requestCameraPerm() {

        PermissionsUtils.requsetRunPermission(FaceCheckActivity.this, new RePermissionResultBack() {
            @Override
            public void requestSuccess() {
                toast(R.string.successfully);
                enterNextPage();
            }

            @Override
            public void requestFailer() {
                toast(R.string.failure);
            }
        },Permission.Group.CAMERA,Permission.Group.STORAGE);

    }

    private void enterNextPage() {
        //startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
    }

    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {// Permission Granted

                //ConUtil.showToast(this, "获取相机权限失败");
            } else
                enterNextPage();
        }
    }


    private static final int PAGE_INTO_LIVENESS = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK) {
//            String result = data.getStringExtra("result");
//            String delta = data.getStringExtra("delta");
//            Serializable images=data.getSerializableExtra("images");
            Bundle bundle=data.getExtras();
            bundle.putString("auth_flow",mAuthFlow);
            bundle.putString("idno",mIdNum);
            bundle.putString("name",mIdName);
            bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_AUTH);

            //ApplyAmountActivity.startActivity(this, bundle);
        }
    }


    @OnClick({R.id.back_img, R.id.face_but,R.id.loading_layout_againWarrantyBtn,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.face_but:
                requestCameraPerm();
                break;
            case R.id.loading_layout_againWarrantyBtn:
                netWorkWarranty();
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

    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {

    }


    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
