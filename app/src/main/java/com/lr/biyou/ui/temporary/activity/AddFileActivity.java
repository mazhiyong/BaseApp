package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.AddFileAdapter;
import com.lr.biyou.ui.temporary.adapter.GridImageAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 *  上传附件信息   界面
 */
public class AddFileActivity extends BasicActivity implements RequestView,ReLoadingData{

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
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.but_next)
    Button mButNext;

    private String mRequestTag ="";

    private AddFileAdapter mAddFileAdapter;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;

    private List<LocalMedia> selectList = new ArrayList<>();

    private List<Map<String,Object>> mTitleList ;

    private Map<String,Object> mDataMap; //获取图片信息
    private String mSign = "";

    private int mCurrentPosition = 0;

    private int mFileNum = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_add_file;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.add_fujian));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
            mSign =  bundle.getString("TYPE");
            if (mDataMap != null){
                if (mSign.equals("1")){///申请额度  上传附件  标记
                    mTitleList = (List<Map<String,Object>>) mDataMap.get("contList") ;
                }else if (mSign.equals("2")){//借款申请  上传附件  标记 之前是conts  现在都统一成了  contList
                    mTitleList = (List<Map<String,Object>>) mDataMap.get("contList") ;
                }
            }
        }

        LogUtilDebug.i("打印log日志",mSign+"!!!!!!!!!!"+mTitleList);

        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.shuaixuan);
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightLay.setVisibility(View.GONE);



        initView();
        getSelectPic();
        // traderListAction();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        //mPageView.showEmpty();
        mPageView.showContent();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(AddFileActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);
        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
            }
        });
    }


    private void getSelectPic(){
        if (mTitleList == null || mTitleList.size() <= 0){
            mPageView.showEmpty();
            return;
        }

        mAddFileAdapter = new AddFileAdapter(AddFileActivity.this,mSign);
        mAddFileAdapter.setOnAddPicClickListene(onAddPicClickListener);
        mAddFileAdapter.setOnItemClickListener(mOnItemClickListener);
        mAddFileAdapter.addAll(mTitleList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAddFileAdapter);
        mRefreshListView.setAdapter(mLRecyclerViewAdapter);

        mRefreshListView.setPullRefreshEnabled(false);
        mRefreshListView.setLoadMoreEnabled(false);
    }


    GridImageAdapter.OnItemClickListener mOnItemClickListener = new GridImageAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View v,String mType) {

            List<LocalMedia> localMediaList = new ArrayList<>();
            for (int i =0;i<mTitleList.size();i++){
                Map<String,Object> item = mTitleList.get(i);
                String ss = item.get("connpk")+"";
                if (mSign.equals("1")){
                    ss = item.get("connpk")+"";
                }else if (mSign.equals("2")){
                    ss = item.get("filetype")+"";
                }
                if (mType.equals(ss)){
                    localMediaList = (List<LocalMedia>)item.get("selectPicList");
                    break;
                }
            }

            if (localMediaList.size() > 0) {
                LocalMedia media = localMediaList.get(position);
                String pictureType = media.getPictureType();
                int mediaType = PictureMimeType.pictureToVideo(pictureType);
                switch (mediaType) {
                    case 1:
                        // 预览图片 可自定长按保存路径
                        //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                        PictureSelector.create(AddFileActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, localMediaList);
                        break;
                    case 2:
                        // 预览视频
                        PictureSelector.create(AddFileActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case 3:
                        // 预览音频
                        PictureSelector.create(AddFileActivity.this).externalPictureAudio(media.getPath());
                        break;
                }
            }
        }
    };

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(String mType) {//点击添加时间，然后把文件类型 传递到前面过来  然后选择图片 上传图片

            for (int i =0;i<mTitleList.size();i++){
                Map<String,Object> item = mTitleList.get(i);
                String ss = item.get("connpk")+"";
                if (mSign.equals("1")){
                    ss = item.get("connpk")+"";
                }else if (mSign.equals("2")){
                    ss = item.get("filetype")+"";
                }
                if (mType.equals(ss)){
                    mCurrentPosition = i;
                    selectPic();
                    return;
                }
            }
        }

        @Override
        public void onDeleClick(int position,String mType) {
            for (int i =0;i<mTitleList.size();i++){
                Map<String,Object> item = mTitleList.get(i);
                String ss = item.get("connpk")+"";
                if (mSign.equals("1")){
                    ss = item.get("connpk")+"";
                }else if (mSign.equals("2")){
                    ss = item.get("filetype")+"";
                }
                if (mType.equals(ss)){
                    List<Map<String,Object>> resultData = (List<Map<String,Object>>)item.get("resultData");
                    resultData.remove(position);
                    mAddFileAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }

    };

    private void selectPic(){

        File path = new File( MbsConstans.UPLOAD_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(AddFileActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                // .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .glideOverride(160, 160)
                //.sizeMultiplier(0.4f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                // .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //  .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
                .compressSavePath( MbsConstans.UPLOAD_PATH)//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                //.openClickSound(true)// 是否开启点击声音 true or false
                //.selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                //.previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(80)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                // .videoQuality()// 视频录制质量 0 or 1 int
                //.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond()//视频秒数录制 默认60s int
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                .forResult(mCurrentPosition);//结果回调onActivityResult code
        //注意  这个  mCurrentPosition   是标记后台返回的文件类别类型的索引
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == mCurrentPosition){
                // 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                Map<String,Object> map = mTitleList.get(mCurrentPosition);
                String code = map.get("connpk")+"";
                if (mSign.equals("1")){
                    code = map.get("connpk")+"";
                }else if (mSign.equals("2")){
                    code = map.get("filetype")+"";
                }

             /*   Map<String,Object> map = mTitleList.get(mCurrentPosition);
					BitmapResizeUtil.compressBitmap(saveP, MbsConstans.PHOTO_PATH, fileName, RotatePictureUtil.getBitmapDegree(saveP));

                map.put("selectPicList",selectList);
                mAddFileAdapter.notifyDataSetChanged();*/
                mFileNum = 0;

                showProgressDialog("正在上传，请稍后...");
                for (int i = 0;i<selectList.size();i++){
                    LocalMedia localMedia = selectList.get(i);
                    /*String fileName = String.valueOf(System.currentTimeMillis())+"_"+i + ".jpg";
                    BitmapResizeUtil.compressBitmap(localMedia.getPath(),MbsConstans.UPLOAD_PATH, fileName, RotatePictureUtil.getBitmapDegree(localMedia.getPath()));
                    localMedia.setCompressPath(MbsConstans.UPLOAD_PATH + fileName);
                    BitmapUtil.saveCompressImg(localMedia.getPath(),MbsConstans.UPLOAD_PATH+fileName+".png");*/
                    uploadFile(localMedia,code);
                    LogUtilDebug.i("打印log日志","@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@开始上传");
                }
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                for (LocalMedia media : selectList) {
                    Log.i("图片-----》", media.getPath());
                }
                    /*mGridImageAdapter.setList(selectList);
                    mGridImageAdapter.notifyDataSetChanged();*/
            }
        }
    }

    private void uploadFile(LocalMedia localMedia,String code){

        // mRequestTag = MethodUrl.creUploadFile;
        Map<String,Object> mSignMap = new HashMap<>();
        mSignMap.put("file",localMedia);
        mSignMap.put("index",mCurrentPosition);
        mSignMap.put("code",code);
        String oldPath = localMedia.getPath();

        Map<String, Object> map = new HashMap<>();
        map.put("file",localMedia.getCompressPath());
        //map.put("fileName",mDataMap.get("precreid")+""+System.currentTimeMillis()+""+i);
        Map<String, String> mHeaderMap = new HashMap<String, String>();


        Map<String,Object> mParamMap = new HashMap<>();


        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.creUploadFile, mSignMap,mParamMap,map);
    }




    @OnClick({R.id.back_img,R.id.but_next,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.but_next:
                finish();
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
        }
    }

    @Override
    public void showProgress() {
        //showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        //dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

        Intent intent ;
        switch (mType){
            case MethodUrl.creUploadFile://
                mFileNum ++;
                String code = tData.get("code")+"";
                for (int i =0;i<mTitleList.size();i++){

                    Map<String,Object> item = mTitleList.get(i);
                    String ss = item.get("connpk")+"";
                    if (mSign.equals("1")){
                        ss = item.get("connpk")+"";
                    }else if (mSign.equals("2")){
                        ss = item.get("filetype")+"";
                    }
                    if (code.equals(ss)){
                        List<Map<String,Object>> resutData = (List<Map<String,Object>>) item.get("resultData");
                        Map<String,Object> mm = new HashMap<>();
                        String str1 = tData.get("remotepath")+"";
                        String str2 = tData.get("filemd5")+"";
                        mm.put("remotepath",str1);
                        mm.put("filemd5",str2);
                        if (resutData != null){
                            resutData.add(mm);
                        }else {
                            resutData = new ArrayList<>();
                            resutData.add(mm);
                            item.put("resultData",resutData);
                        }

                        List<LocalMedia> fileList = (List<LocalMedia>) item.get("selectPicList");

                        if (fileList != null){
                            fileList.add((LocalMedia) tData.get("file"));
                        }else {
                            fileList = new ArrayList<>();
                            fileList.add((LocalMedia) tData.get("file"));
                            item.put("selectPicList",fileList);
                        }

                       // mAddFileAdapter.notifyDataSetChanged();
                        break;
                    }
                }

                //mRefreshListView.refreshComplete(10);

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.creUploadFile:
//                        UPLOAD_FILE();
                        break;
                }
                break;
        }
        if (mFileNum == selectList.size()){
            mAddFileAdapter.notifyDataSetChanged();
            mRefreshListView.refreshComplete(10);
            dismissProgressDialog();
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {

        switch (mType) {
            case MethodUrl.creUploadFile://
                mFileNum ++;
                break;
        }
        if (mFileNum == selectList.size()){
            mAddFileAdapter.notifyDataSetChanged();
            mRefreshListView.refreshComplete(10);
            dismissProgressDialog();
        }
        dealFailInfo(map,mType);
    }

    @Override
    public void reLoadingData() {
    }


  /*  @Override
    public void finish() {
        super.finish();
        System.gc();
        System.runFinalization();
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            PictureFileUtils.deleteCacheDirFile(this);
            PictureFileUtils.deleteExternalCacheDirFile(this);
        }catch (Exception e){

        }
        //        FileUtils.deleteDir(MbsConstans.UPLOAD_PATH);
        Intent intent = new Intent();
        intent.setAction(MbsConstans.BroadcastReceiverAction.FILE_TIP_ACTION);
        intent.putExtra("DATA", (Serializable) mTitleList);
        sendBroadcast(intent);


    }
}
