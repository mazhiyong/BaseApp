package com.lr.biyou.ui.moudle1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.AppUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加支付方式  界面
 *
 *
 */
public class AddPayWayActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.but_sure)
    Button mButNext;
    @BindView(R.id.new_pass_edit)
    EditText mNewPassEdit;
    @BindView(R.id.new_pass_again_edit)
    EditText mNewPassAgainEdit;
    @BindView(R.id.togglePwd1)
    ToggleButton mTogglePwd1;
    @BindView(R.id.togglePwd2)
    ToggleButton mTogglePwd2;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.numter_tv)
    TextView numterTv;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_bank)
    EditText etBank;
    @BindView(R.id.bank_lay)
    LinearLayout bankLay;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.code_lay)
    LinearLayout codeLay;


    private String mAuthCode = "";
    private String mPhone = "";
    private String mKind = "0";
    private String mType = "0";
    private String mInvcode = "";

    private String mRequestTag = "";
    private Map<String,Object> mapData;
    private String imgUrl = "";

    @Override
    public int getContentView() {
        return R.layout.activity_add_pay_way;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //map.put("kind", "0");//0银行卡  1 支付宝 2 微信支付
        //map.put("type", "1"); //0未添加  1 已添加
        if (bundle != null) {
            mapData = (Map<String, Object>) bundle.getSerializable("DATA");
            if (!UtilTools.empty(mapData)){
                mKind = mapData.get("kind")+"";
                mType = mapData.get("type")+"";
                switch (mKind){
                    case "0":
                        numterTv.setText("银行卡号");
                        if (mType.equals("0")){
                            mTitleText.setText("添加银行卡");
                            numterTv.setText("银行卡号");
                            etNumber.setHint("请输入银行卡号");
                            etName.setHint("请输入姓名");
                            etBank.setHint("请输入开户行");
                        }else {
                            mTitleText.setText("变更银行卡");
                            etNumber.setText(mapData.get("number") + "");
                            etName.setText(mapData.get("name") + "");
                            etBank.setText( mapData.get("pay_message") + "");
                        }

                        bankLay.setVisibility(View.VISIBLE);
                        codeLay.setVisibility(View.GONE);
                        break;
                    case "1":
                        numterTv.setText("账号");
                        if (mType.equals("0")){
                            mTitleText.setText("添加支付宝");
                            etNumber.setHint("请输入账号");
                            etName.setHint("请输入姓名");
                            ivAdd.setImageResource(R.drawable.icon1_tu);
                        }else {
                            mTitleText.setText("变更支付宝");
                            etNumber.setText(mapData.get("number") + "");
                            etName.setText(mapData.get("name") + "");
                            imgUrl = mapData.get("pay_message") + "";
                            GlideUtils.loadImage(AddPayWayActivity.this,imgUrl,ivAdd);
                            imgUrl = imgUrl.substring(imgUrl.indexOf("/upload"));
                        }


                        bankLay.setVisibility(View.GONE);
                        codeLay.setVisibility(View.VISIBLE);
                        break;
                    case "2":

                        numterTv.setText("账号");
                        if (mType.equals("0")){
                            mTitleText.setText("添加微信");
                            etNumber.setHint("请输入账号");
                            etName.setHint("请输入姓名");
                            ivAdd.setImageResource(R.drawable.icon1_tu);
                        }else {
                            mTitleText.setText("变更微信");
                            etNumber.setText(mapData.get("number") + "");
                            etName.setText(mapData.get("name") + "");
                            imgUrl = mapData.get("pay_message") + "";
                            GlideUtils.loadImage(AddPayWayActivity.this,imgUrl,ivAdd);
                            imgUrl = imgUrl.substring(imgUrl.indexOf("/upload"));

                        }
                        bankLay.setVisibility(View.GONE);
                        codeLay.setVisibility(View.VISIBLE);
                        break;
                }
            }

        }
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);



    }


    @OnClick({R.id.back_img, R.id.left_back_lay, R.id.bt_sure,R.id.iv_add})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.iv_add: //上传二维码图片
                ActionSheetDialogNoTitle();

                break;
            case R.id.bt_sure:
                submitAction();
                btSure.setEnabled(false);
                break;

        }
    }

    private void uploadPicAction() {
        mRequestTag = MethodUrl.UPLOAD_FILE;
        Map<String, Object> signMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(AddPayWayActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("file",mHeadImgPath);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.UPLOAD_FILE,signMap, map,fileMap);
    }


    private void submitAction() {
        if (UtilTools.empty(etName.getText().toString())){
            showToastMsg("请输入姓名信息");
            return;
        }
        Map<String, String> mHeaderMap ;
        Map<String, Object> map ;

        switch (mKind){
            case "0":
                mRequestTag = MethodUrl.UPDATE_BANK;
                if (UtilTools.empty(etNumber.getText().toString())){
                    showToastMsg("请输入银行卡账号信息");
                    return;
                }
                if (UtilTools.empty(etBank.getText().toString())){
                    showToastMsg("请输入开户行信息");
                    return;
                }
                mHeaderMap = new HashMap<>();
                map = new HashMap<>();
                if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
                    MbsConstans.ACCESS_TOKEN = SPUtils.get(AddPayWayActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
                }
                map.put("token",MbsConstans.ACCESS_TOKEN);
                map.put("bank_name", etName.getText()+"");
                map.put("bank_number", etNumber.getText()+"");
                map.put("bank_opener", etBank.getText()+"");
                mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.UPDATE_BANK, map);

                break;

            case "1":
                mRequestTag = MethodUrl.UPDATE_ALIPAY;
                if (UtilTools.empty(etNumber.getText().toString())){
                    showToastMsg("请输入支付宝账号信息");
                    return;
                }
                if (UtilTools.empty(imgUrl)){
                    showToastMsg("请上传收款二维码图片信息");
                    return;
                }

                mHeaderMap = new HashMap<>();
                map = new HashMap<>();
                if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
                    MbsConstans.ACCESS_TOKEN = SPUtils.get(AddPayWayActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
                }
                map.put("token",MbsConstans.ACCESS_TOKEN);
                map.put("alipay_name", etName.getText()+"");
                map.put("alipay_number", etNumber.getText()+"");
                map.put("alipay_qrcode", imgUrl);
                mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.UPDATE_ALIPAY, map);
                break;

            case "2":
                mRequestTag = MethodUrl.UPDATE_WECHAT;
                if (UtilTools.empty(etNumber.getText().toString())){
                    showToastMsg("请输入微信账号信息");
                    return;
                }
                if (UtilTools.empty(imgUrl)){
                    showToastMsg("请上传收款二维码图片信息");
                    return;
                }

                mHeaderMap = new HashMap<>();
                map = new HashMap<>();
                if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
                    MbsConstans.ACCESS_TOKEN = SPUtils.get(AddPayWayActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
                }
                map.put("token",MbsConstans.ACCESS_TOKEN);
                map.put("wechat_name", etName.getText()+"");
                map.put("wechat_number", etNumber.getText()+"");
                map.put("wechat_qrcode", imgUrl);
                mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.UPDATE_WECHAT, map);
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
    public void loadDataSuccess(Map<String, Object> tData, String Type) {
        Intent intent;
        btSure.setEnabled(true);
        switch (Type) {
            case MethodUrl.UPLOAD_FILE: //上传二维码图片成功
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        imgUrl= tData.get("data")+"";
                        if (!UtilTools.empty(imgUrl)){
                            LogUtilDebug.i("show","imgurl:"+imgUrl);
                            showToastMsg("图片上传成功");
                        }else{
                            showToastMsg("图片上传失败");
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddPayWayActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }


                break;

            case MethodUrl.UPDATE_BANK:

            case MethodUrl.UPDATE_ALIPAY:

            case MethodUrl.UPDATE_WECHAT:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        if (mType.equals("0")){
                            showToastMsg("添加成功");
                        }else {
                            showToastMsg("变更成功");
                        }
                        finish();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(AddPayWayActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }




                break;


            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.RESET_PASSWORD:
                        submitAction();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType){
            case MethodUrl.UPLOAD_FILE: //上传二维码图片失败
                showToastMsg("图片上传失败");
                break;
            case MethodUrl.UPDATE_BANK:
            case MethodUrl.UPDATE_ALIPAY:
            case MethodUrl.UPDATE_WECHAT:
                showToastMsg("操作失败");
                btSure.setEnabled(true);
                break;
        }
        dealFailInfo(map, mType);
    }



    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"从相册选择", "拍照"};
        final ActionSheetDialog dialog = new ActionSheetDialog(AddPayWayActivity.this, stringItems, null);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                switch (position) {
                    case 0: //从相册选择

                        PermissionsUtils.requsetRunPermission(AddPayWayActivity.this, new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                //showToastMsg(R.string.successfully);
                                localPic();
                            }

                            @Override
                            public void requestFailer() {
                                showToastMsg(R.string.failure);
                            }
                        }, Permission.Group.STORAGE);
                        break;
                    case 1: //拍照

                        PermissionsUtils.requsetRunPermission(AddPayWayActivity.this, new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                //showToastMsg(R.string.successfully);
                                photoPic();
                            }

                            @Override
                            public void requestFailer() {
                                showToastMsg(R.string.failure);
                            }
                        },Permission.Group.STORAGE,Permission.Group.CAMERA);
                        break;
                }

            }
        });
    }

    private void localPic() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
         */
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    private void photoPic() {
        /**
         * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
         * 文档，you_sdk_path/docs/guide/topics/media/camera.html
         * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
         * 官方文档太长了就不看了，其实是错的，这个地方也错了，必须改正
         */
        Uri uri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(AddPayWayActivity.this, AppUtil.getAppProcessName(this)+".FileProvider", new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"));
        } else {
            uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 2);
    }

    private Intent dataIntent = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory() + "/xiaoma.jpg");
                if (temp.exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(AddPayWayActivity.this, AppUtil.getAppProcessName(AddPayWayActivity.this)+".FileProvider", temp);
                    } else {
                        uri = Uri.fromFile(temp);
                    }
                    startPhotoZoom(uri);
                }
                break;
            // 取得裁剪后的图片
            case 3:

                // 将Uri图片转换为Bitmap
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    // TODO，将裁剪的bitmap显示在imageview控件上
                    Drawable dr = new BitmapDrawable(getResources(),bitmap);
                    ivAdd.setImageDrawable(dr);
                    saveCroppedImage(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                //ImageLoader.getmContext().displayImage(MbsConstans.Pic_Path+MbsConstans.memberUser.getHeadPath(),mHeadImage);
                // UtilTools.showImage(MbsConstans.Pic_Path+MbsConstans.memberUser.getHeadPath(),mHeadImage, R.drawable.no_def);

                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private String imgName = "";
    private Uri uritempFile;

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        File file = new File(MbsConstans.BASE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        // intent.putExtra("noFaceDetection", true);
        // 宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);

        // intent.putExtra("scale", true);
        // intent.putExtra("return-data", true);
        // this.startActivityForResult(intent, AppFinal.RESULT_CODE_PHOTO_CUT);
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        imgName = System.currentTimeMillis() + ".jpg";
        uritempFile = Uri.parse("file:///"  + MbsConstans.BASE_PATH + "/" + imgName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);

    }

    private String mHeadImgPath = "";
    private void saveCroppedImage(Bitmap bmp) {

        try {
            File saveFile = new File(MbsConstans.IMAGE_CODE_PATH);

            mHeadImgPath = MbsConstans.IMAGE_CODE_PATH + new Date().getTime() + ".png";
            File file = new File(mHeadImgPath);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            File saveFile2 = new File(mHeadImgPath);

            FileOutputStream fos = new FileOutputStream(saveFile2);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();

            // uploadAliPic(new Date().getTime()+".png",filepath);

            //上传图片信息
            uploadPicAction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
