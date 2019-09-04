package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 身份证认证   界面
 */
public class IdCardPicActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.loading_layout_WarrantyBar)
    ProgressBar mLoadingLayoutWarrantyBar;
    @BindView(R.id.loading_layout_WarrantyText)
    TextView mLoadingLayoutWarrantyText;
    @BindView(R.id.loading_layout_barLinear)
    LinearLayout mLoadingLayoutBarLinear;
    @BindView(R.id.loading_layout_againWarrantyBtn)
    Button mLoadingLayoutAgainWarrantyBtn;
    @BindView(R.id.my_front_image)
    ImageView mMyFrontImage;
    @BindView(R.id.my_back_image)
    ImageView mMyBackImage;
    @BindView(R.id.my_front_image_cardView)
    CardView mMyFrontImageCardView;
    @BindView(R.id.my_back_image_cardView)
    CardView mMyBackImageCardView;
    @BindView(R.id.front_tv)
    TextView mFrontTv;
    @BindView(R.id.back_tv)
    TextView mBackTv;
    @BindView(R.id.user_name_tv)
    TextView mUserNameTv;
    @BindView(R.id.front_info_delete)
    ImageView mFrontInfoDelete;
    @BindView(R.id.idcard_value_tv)
    TextView mIdcardValueTv;
    @BindView(R.id.front_value_lay)
    LinearLayout mFrontValueLay;
    @BindView(R.id.qianfa_jiguan_value)
    TextView mQianfaJiguanValue;
    @BindView(R.id.back_info_delete)
    ImageView mBackInfoDelete;
    @BindView(R.id.out_date_value)
    TextView mOutDateValue;
    @BindView(R.id.back_value_lay)
    LinearLayout mBackValueLay;

    private boolean isVertical;

    private String mFrontPath = "";
    private String mBackPath = "";


    private String mRequestTag = "";

    @Override
    public int getContentView() {
        return R.layout.activity_idcard_pic;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.id_card_check));
        mButNext.setEnabled(false);
        network();
    }


    private void uploadPicAction() {

        mRequestTag =  MethodUrl.idCardCheck;
        Map<String,Object> mSignMap = new HashMap<>();

        Map<String, Object> mParamMap = new HashMap<String, Object>();

        Map<String, Object> map = new HashMap<>();
        map.put("image",mFilePath);

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.idCardCheck,mSignMap, mParamMap,map);
    }
    private void submitInfoAction() {
        mRequestTag =  MethodUrl.idCardSubmit;

        Map<String, Object> mSignMap = new HashMap<>();


        Map<String, Object> map = new HashMap<>();
       /* map.put("idno","410725199103263616");
        map.put("idname","刘英超");
        map.put("expires","2037-08-01");
        map.put("issued","原阳县公安局");*/
        map.put("idno",mIdcardValueTv.getText()+"");
        map.put("idname",mUserNameTv.getText()+"");
        map.put("expires",mOutDateValue.getText()+"");
        map.put("issued",mQianfaJiguanValue.getText()+"");

        Map<String, Object> fileMap = new HashMap<>();

        fileMap.put("img_front",MbsConstans.IDCARD_IMAGE_PATH + MbsConstans.IDCARD_FRONT + ".png");
        fileMap.put("img_back",MbsConstans.IDCARD_IMAGE_PATH + MbsConstans.IDCARD_BACK + ".png");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.idCardSubmit,mSignMap, map,fileMap);
    }


    /**
     * 联网授权
     */
    private void network() {
//        mLoadingLayoutBarLinear.setVisibility(View.VISIBLE);
//        mLoadingLayoutAgainWarrantyBtn.setVisibility(View.GONE);
//        mLoadingLayoutWarrantyText.setText("正在联网授权中...");
//        mLoadingLayoutWarrantyBar.setVisibility(View.VISIBLE);
//
//        final String uuid = Util.getUUIDString(this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Manager manager = new Manager(IdCardPicActivity.this);
//                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(
//                        IdCardPicActivity.this);
//                manager.registerLicenseManager(idCardLicenseManager);
//                manager.takeLicenseFromNetwork(uuid);
//                if (idCardLicenseManager.checkCachedLicense() > 0) {
//                    //授权成功
//                    UIAuthState(true);
//                } else {
//                    //授权失败
//                    UIAuthState(false);
//                }
//            }
//        }).start();
    }


    private void UIAuthState(final boolean isSuccess) {
        runOnUiThread(new Runnable() {
            public void run() {
                authState(isSuccess);
            }
        });
    }

    private void authState(boolean isSuccess) {

        if (isSuccess) {
            mLoadingLayoutBarLinear.setVisibility(View.GONE);
            mLoadingLayoutAgainWarrantyBtn.setVisibility(View.GONE);
            mLoadingLayoutWarrantyText.setText("正在联网授权中...");
            mLoadingLayoutWarrantyBar.setVisibility(View.GONE);
        } else {
            mLoadingLayoutBarLinear.setVisibility(View.VISIBLE);
            mLoadingLayoutAgainWarrantyBtn.setVisibility(View.VISIBLE);
            mLoadingLayoutWarrantyText.setText("正在联网授权中...");
            mLoadingLayoutWarrantyBar.setVisibility(View.VISIBLE);
            mLoadingLayoutWarrantyText.setText("联网授权失败！请检查网络或找服务商");
        }
    }


    int mSide = 0;

    private void requestCameraPerm(int side) {
        mSide = side;

        PermissionsUtils.requsetRunPermission(this, new RePermissionResultBack() {
            @Override
            public void requestSuccess() {
                enterNextPage(mSide);
            }

            @Override
            public void requestFailer() {
                toast(R.string.failure);
            }
        },Permission.Group.CAMERA,Permission.Group.STORAGE);

    }

    private void enterNextPage(int side) {
//        Intent intent = new Intent(this, IDCardScanActivity.class);
//        intent.putExtra("side", side);
//        intent.putExtra("isvertical", isVertical);
//        startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
    }

    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {// Permission Granted
                //Util.showToast(this, "获取相机权限失败");
            } else
                enterNextPage(mSide);
        }
    }


    private static final int INTO_IDCARDSCAN_PAGE = 100;

    private String mFilePath =  "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
            int side = data.getIntExtra("side", 0);
            byte[] idcardImgData = data.getByteArrayExtra("idcardImg");
            Bitmap idcardBmp = BitmapFactory.decodeByteArray(idcardImgData, 0, idcardImgData.length);
            switch (side) {//0   正面照片      1  反面照片
                case 0:
                    saveCroppedImage(idcardBmp, MbsConstans.IDCARD_FRONT);
                    //mMyFrontImage.setImageBitmap(idcardBmp);
                    mFilePath = MbsConstans.IDCARD_IMAGE_PATH + MbsConstans.IDCARD_FRONT + ".png";
                    GlideUtils.loadUUIDImage(IdCardPicActivity.this,mFilePath,mMyFrontImage);
                    uploadPicAction();
                    break;
                case 1:
                    saveCroppedImage(idcardBmp, MbsConstans.IDCARD_BACK);
                    //mMyBackImage.setImageBitmap(idcardBmp);
                    mFilePath = MbsConstans.IDCARD_IMAGE_PATH + MbsConstans.IDCARD_BACK + ".png";
                    GlideUtils.loadUUIDImage(IdCardPicActivity.this,mFilePath,mMyBackImage);
                    uploadPicAction();
                    break;
            }

        }
    }


    private void saveCroppedImage(Bitmap bmp, String name) {

        try {
            File saveFile = new File(MbsConstans.IDCARD_IMAGE_PATH);

            String filepath = MbsConstans.IDCARD_IMAGE_PATH + name + ".png";
            File file = new File(filepath);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            File saveFile2 = new File(filepath);

            FileOutputStream fos = new FileOutputStream(saveFile2);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
            // uploadAliPic(new Date().getTime()+".png",filepath);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @OnClick({R.id.back_img, R.id.but_next, R.id.loading_layout_againWarrantyBtn, R.id.my_front_image_cardView,
            R.id.my_back_image_cardView,R.id.front_info_delete, R.id.back_info_delete,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
               /* intent = new Intent(IdCardPicActivity.this, FaceCheckActivity.class);
                startActivity(intent);*/
                submitInfoAction();
                break;
            case R.id.loading_layout_againWarrantyBtn:
                network();
                break;
            case R.id.my_front_image_cardView:
                requestCameraPerm(0);
                break;
            case R.id.my_back_image_cardView:
                requestCameraPerm(1);
                break;
            case R.id.front_info_delete:
                mFrontValueLay.setVisibility(View.GONE);
                mMyFrontImage.setImageResource(R.drawable.front_card);
                butIsEnable();
                mFrontTv.setText(getResources().getText(R.string.front_card));
                mFrontTv.setCompoundDrawablePadding(UtilTools.dip2px(IdCardPicActivity.this,0));
                mFrontTv.setCompoundDrawables(null,null,null,null);
                break;
            case R.id.back_info_delete:
                mBackValueLay.setVisibility(View.GONE);
                mMyBackImage.setImageResource(R.drawable.back_card);
                butIsEnable();
                mBackTv.setText(getResources().getText(R.string.back_card));
                mBackTv.setCompoundDrawablePadding(UtilTools.dip2px(IdCardPicActivity.this,0));
                mBackTv.setCompoundDrawables(null,null,null,null);
                break;
        }
    }

    private void butIsEnable(){
        if (mFrontValueLay.isShown()&& mBackValueLay.isShown()){
            mButNext.setEnabled(true);
        }else {
            mButNext.setEnabled(false);
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
        Intent intent ;
        switch (mType) {
            case MethodUrl.idCardCheck:
                Drawable drawable = ContextCompat.getDrawable(IdCardPicActivity.this,R.drawable.submit_success);
                if (mSide == 0){
                    String name = tData.get("idname")+"";
                    String cardNum = tData.get("idno")+"";
                    mUserNameTv.setText(name);
                    mIdcardValueTv.setText(cardNum);
                    mFrontValueLay.setVisibility(View.VISIBLE);
                    mFrontTv.setText("提交成功");
                    mFrontTv.setCompoundDrawablePadding(UtilTools.dip2px(IdCardPicActivity.this,5));
                    mFrontTv.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                }else {
                    String dateValue = tData.get("expires")+"";
                    String jiguan = tData.get("issued")+"";

                    mOutDateValue.setText(dateValue);
                    mQianfaJiguanValue.setText(jiguan);
                    mBackValueLay.setVisibility(View.VISIBLE);
                    mBackTv.setText("提交成功");
                    mBackTv.setCompoundDrawablePadding(UtilTools.dip2px(IdCardPicActivity.this,5));
                    mBackTv.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                }
                butIsEnable();
                break;
            case MethodUrl.idCardSubmit://{auth_flow=2576f7e49ef84d498676cf099cc32aab}
                FileUtils.deleteDir(MbsConstans.IDCARD_IMAGE_PATH);
                intent = new Intent(IdCardPicActivity.this,FaceCheckActivity.class);
                intent.putExtra("auth_flow",tData.get("auth_flow")+"");
                intent.putExtra("idno",mIdcardValueTv.getText()+"");
                intent.putExtra("name",mUserNameTv.getText()+"");
                startActivity(intent);
                finish();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.idCardCheck:
                        uploadPicAction();
                        break;
                    case MethodUrl.idCardSubmit:
                        submitInfoAction();
                        break;
                }
                break;
        }

    }


    @Override
    public void finish() {
        super.finish();
        FileUtils.deleteDir(MbsConstans.IDCARD_IMAGE_PATH);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        dealFailInfo(map,mType);
    }





    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
