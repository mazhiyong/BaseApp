package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.AppUtil;
import com.lr.biyou.utils.tool.BitmapResizeUtil;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.RotatePictureUtil;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 三次认证识别，需要自己上传图片认证    自己手动认证界面   界面
 */
public class IdCardMyPicActivity extends BasicActivity implements RequestView{

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
    @BindView(R.id.my_front_image)
    CardView mMyFrontImage;
    @BindView(R.id.my_back_image)
    CardView mMyBackImage;
    @BindView(R.id.my_hand_image)
    CardView mMyHandImage;

    @BindView(R.id.front_card_image)
    ImageView mFrontCardImage;
    @BindView(R.id.back_card_image)
    ImageView mBackCardImage;
    @BindView(R.id.hand_card_image)
    ImageView mHandCardImage;

    private boolean isVertical;


    private int mType = 0;//代表哪个拍摄  0 身份证正面    1  身份证反面   2 手持身份证

    private String mName = "";
    private String mIdNum = "";

    private String mRequestTag = "";

    @Override
    public int getContentView() {
        return R.layout.activity_idcard_my_pic;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!= null){
            mName = bundle.getString("idname");
            mIdNum = bundle.getString("idno");
        }
        mTitleText.setText(getResources().getString(R.string.id_card_check));
    }

    private void submitInfoAction() {

        mRequestTag = MethodUrl.peopleAuth;

        Map<String,Object> mSignMap = new HashMap<>();

        //String 请求参数
        Map<String, Object> map = new HashMap<>();
        map.put("idno",mIdNum);
        map.put("idname",mName);
        Map<String, Object> fileMap = new HashMap<>();

        if (mFrontCardImage.getTag(R.id.glide_tag) == null
                || mBackCardImage.getTag(R.id.glide_tag) == null
                || mHandCardImage.getTag(R.id.glide_tag) == null){
            showToastMsg("请上传相关证件照片");
            return;
        }

        //File 请求参数
        fileMap.put("img_front",mFrontCardImage.getTag(R.id.glide_tag)+"");
        fileMap.put("img_back",mBackCardImage.getTag(R.id.glide_tag)+"");
        fileMap.put("img_hand",mHandCardImage.getTag(R.id.glide_tag)+"");
        LogUtilDebug.i("人工认证提交的参数",""+fileMap);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.peopleAuth,mSignMap, map,fileMap);
    }


    @OnClick({R.id.back_img, R.id.but_next, R.id.my_front_image, R.id.my_back_image, R.id.my_hand_image,R.id.left_back_lay})
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
                submitInfoAction();
//                intent = new Intent(IdCardMyPicActivity.this,IdCardSuccessActivity.class);
//                startActivity(intent);
                break;
            case R.id.my_front_image:
                mType = 0;
                photo();
                break;
            case R.id.my_back_image:
                mType = 1;
                photo();
                break;
            case R.id.my_hand_image:
                mType = 2;
                photo();
                break;
        }
    }


    private static final int TAKE_PICTURE = 0x000001;

    private String saveP = "";

    public void photo() {

        PermissionsUtils.requsetRunPermission(this, new RePermissionResultBack() {
            @Override
            public void requestSuccess() {
                getCamera();
            }

            @Override
            public void requestFailer() {
                toast(R.string.failure);
            }
        },Permission.Group.CAMERA,Permission.Group.STORAGE);
    }

    private void getCamera(){
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = String.valueOf(System.currentTimeMillis());
        String savePath = MbsConstans.PHOTO_PATH;
        try {
            FileUtils.createSDDir("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveP = savePath + File.separator + fileName + ".jpg";

        Uri uri = Uri.fromFile(new File(saveP));

        //下面这句指定调用相机拍照后的照片存储的路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(IdCardMyPicActivity.this, AppUtil.getAppProcessName(IdCardMyPicActivity.this)+".fileProvider", new File(savePath, fileName + ".jpg"));
        } else {
            uri = Uri.fromFile(new File(savePath, fileName + ".jpg"));
        }
        // 指定存储路径，这样就可以保存原图了
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 拍照返回图片
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }



    private FileInputStream is = null;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {

					/*String fileName = String.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					String savePath = FileUtils.saveBitmap(bm, fileName);
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(savePath);
					takePhoto.setBitmap(bm);
					Bimp.tempSelectBitmap.add(takePhoto);*/

					/*ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(saveP);
					takePhoto.setBitmap(BitmapFactory.decodeFile(saveP));
					Bimp.tempSelectBitmap.add(takePhoto);*/

                    String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                    //调用工具类进行图片压缩（该类会把图片压缩后放置在相应的目录下，运行后直接去该路径拿图片即可）
                    BitmapResizeUtil.compressBitmap(saveP, MbsConstans.PHOTO_PATH, fileName, RotatePictureUtil.getBitmapDegree(saveP));
                    // takePhoto.setImagePath(MbsConstans.PHOTO_PATH + File.separator + fileName);
                    // takePhoto.setBitmap(BitmapFactory.decodeFile(MbsConstans.PHOTO_PATH + File.separator + fileName));

                    switch (mType){
                        case 0:
                            //mFrontCardImage.setImageBitmap(BitmapFactory.decodeFile(MbsConstans.PHOTO_PATH + File.separator + fileName));
                            GlideUtils.loadImage(IdCardMyPicActivity.this,MbsConstans.PHOTO_PATH + File.separator + fileName,mFrontCardImage);
                            mFrontCardImage.setTag(R.id.glide_tag,MbsConstans.PHOTO_PATH + File.separator + fileName);
                            break;
                        case 1:
                            //mBackCardImage.setImageBitmap(BitmapFactory.decodeFile(MbsConstans.PHOTO_PATH + File.separator + fileName));
                            GlideUtils.loadImage(IdCardMyPicActivity.this,MbsConstans.PHOTO_PATH + File.separator + fileName,mBackCardImage);
                            mBackCardImage.setTag(R.id.glide_tag,MbsConstans.PHOTO_PATH + File.separator + fileName);
                            break;
                        case 2:
                            //mHandCardImage.setImageBitmap(BitmapFactory.decodeFile(MbsConstans.PHOTO_PATH + File.separator + fileName));
                            GlideUtils.loadImage(IdCardMyPicActivity.this,MbsConstans.PHOTO_PATH + File.separator + fileName,mHandCardImage);
                            mHandCardImage.setTag(R.id.glide_tag,MbsConstans.PHOTO_PATH + File.separator + fileName);
                            break;
                    }
                }
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
        switch (mType){
            case MethodUrl.peopleAuth:
                FileUtils.deleteDir(MbsConstans.PHOTO_PATH);

                intent = new Intent(IdCardMyPicActivity.this,CheckWatiActivity.class);
                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.peopleAuth:
                        submitInfoAction();
                        break;
                }
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
       dealFailInfo(map,mType);
    }



    @Override
    public void finish() {
        super.finish();
        FileUtils.deleteDir(MbsConstans.PHOTO_PATH);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }






    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
