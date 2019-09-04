package com.lr.biyou.ui.temporary.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PDFLookActivity extends BasicActivity {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.pb_bar)
    ProgressBar mPbBar;
    @BindView(R.id.linearLayout1)
    LinearLayout mLinearLayout1;
    @BindView(R.id.load_imageview)
    ImageView mLoadImageView;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;

    private String id = "";
    private String mUrl = "";



    @Override
    public int getContentView() {
        return R.layout.activity_pdf;
    }


    @Override
    public void init() {

        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        }
        if (id.contains("http")) {
            mUrl = id;
        } else {
            mUrl = MbsConstans.SERVER_URL + MethodUrl.pdfUrl + "?path=" + id;
        }

        /**
         * 设置是否仅仅跟踪左侧边缘的滑动返回
         * 因为银行卡里面有嵌套 viewpager  所以要边界返回
         */
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);

        mTitleText.setText("查看协议");
        mRightImg.setImageResource(R.drawable.shared);
        // mRightLay.setVisibility(View.VISIBLE);
        //mRightImg.setVisibility(View.VISIBLE);
        initView();
        LogUtilDebug.i("pdfUrl",mUrl);
        preView(mUrl);

        initAnimation();
        mLinearLayout1.setVisibility(View.VISIBLE);
        startAnim();
    }

    protected void initView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);


        //------------------------------------------------------------------------------------------------
        //把本类的一个实例添加到js的全局对象window中，
        //这样就可以使用window.injs来调用它的方法
        mWebView.addJavascriptInterface(new InJavaScript(), "injs");
        //达到禁用国产机中点击数据进行拨号的bug
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //当有新连接时，使用当前的 WebView
                if (url != null && url.startsWith("http")) {
                    //view.loadUrl(url);
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                return true;
            }
        });
        //设置支持JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        mWebView.setFocusable(false);
        mWebView.setFocusableInTouchMode(false);
        //------------------------------------------------------------------------------------------------

        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                mPbBar.setVisibility(View.VISIBLE);
                mPbBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mPbBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //Log.e("ANDROID_LAB", "TITLE=" + title);
                LogUtilDebug.i("ANDROID_LAB", "TITLE=" + title);
                //mTitleView.setText(title);
            }

        });

    }

    /**
     * 预览pdf
     *
     * @param pdfUrl url或者本地文件路径
     */
    private void preView(String pdfUrl) {
        //1.只使用pdf.js渲染功能，自定义预览UI界面
        mWebView.loadUrl("file:///android_asset/index.html?" + pdfUrl);
//        mWebView.loadUrl("file:///android_asset/index.html?https://gagayi.oss-cn-beijing.aliyuncs.com/video/D57C71A83521E12EFD9334B6C27AE092.pdf" );
        //2.使用mozilla官方demo加载在线pdf
//        mWebView.loadUrl("http://mozilla.github.io/pdf.js/web/viewer.html?file=" + pdfUrl);
        //3.pdf.js放到本地
//        mWebView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + pdfUrl);
        //4.使用谷歌文档服务
//        mWebView.loadUrl("http://docs.google.com/gviewembedded=true&url=" + pdfUrl);
    }


    //在java中调用js代码 传参
    public void sendInfoToJs() {
        //调用js中的函数：showInfoFromJava(msg)
        mWebView.loadUrl("javascript:getFromAndroid('23546456')");
    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {

    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {

    }

    /**
     * @param tData 数据类型
     * @param mType
     * @descriptoin 请求数据成功
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }


    class InJavaScript {
        @JavascriptInterface
        public void runOnAndroidJavaScript(final String str,int type) {

            //可以请求后台，根据需要做相关的操作
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // 请求的信息，从后台得到信息;
                    Message msg = new Message();
                    msg.what = type;
                    mWebViewHandler.sendMessage(msg);
                }
            }).start();
        }

    }

    private Handler mWebViewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    mLinearLayout1.setVisibility(View.GONE);
                    stopAnim();
                    break;
                case 2:
                    mLinearLayout1.setVisibility(View.GONE);
                    stopAnim();
                    new AlertDialog.Builder(PDFLookActivity.this)
                            .setCancelable(false)
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                                        dialog.dismiss();
                                        finish();
                                        return true;
                                    }
                                    else {
                                        return false;
                                    }
                                }
                            })
                            .setTitle(R.string.title_dialog)
                            .setMessage("未能解析此文件，请稍后重试")
                            .setPositiveButton(R.string.but_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                    break;
            }

        }
    };


    @OnClick({R.id.back_img, R.id.left_back_lay,R.id.right_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                // sendInfoToJs();
                break;
        }
    }


    AnimationDrawable aniDrawable;

    private void initAnimation() {
        aniDrawable = (AnimationDrawable) mLoadImageView.getDrawable();
    }

    public void startAnim() {
        aniDrawable.start();
    }

    public void stopAnim() {
        aniDrawable.stop();
    }


    @Override
    protected void onDestroy() {
        if (mWebView != null) {

            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();

        }
        super.onDestroy();
    }


}
