package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class HtmlActivity extends BasicActivity {


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
    @BindView(R.id.pb)
    ProgressBar mPb;
    @BindView(R.id.cotent_webView)
    WebView mCotentWebView;

    private String id = "";

    private String mHtmlContent = "";
    private String mUrl = "";

    String mTitle = "";


    private boolean mCanBack = true;

    private String mShowType = "";

    @Override
    public int getContentView() {
        return R.layout.activity_html_detail;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            id = bundle.getString("id");
            mTitle = bundle.getString("title");
            mShowType = bundle.getString("TYPE");
            if (mShowType == null){
                mShowType = "";
            }
        }


        mBackImg.setImageResource(R.drawable.aud);
        mBackText.setText("关闭");
        mBackText.setTextColor(ContextCompat.getColor(this,R.color.black22));

        /**
         * 设置是否仅仅跟踪左侧边缘的滑动返回
         * 因为银行卡里面有嵌套 viewpager  所以要边界返回
         */
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mCotentWebView.getSettings().setSafeBrowsingEnabled(false);
        }*/


        //mUrl = MbsConstans.SERVER_URL + MethodUrl.pdfUrl + "?path=" + id;
        //mUrl = "http://172.16.1.216:8089/#/login";
        //mUrl = "http://www.baidu.com";

        mUrl = id;
        if (mShowType.equals("1")){
            mUrl = "file:///android_asset/dr_about.html";
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        mPb.setMax(100);

        mCotentWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        mCotentWebView.getSettings().setJavaScriptEnabled(true);   //设置支持Javascript
        mCotentWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mCotentWebView.getSettings().setSupportMultipleWindows(true);
        mCotentWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        mCotentWebView.getSettings().setDomStorageEnabled(true);
        mCotentWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mCotentWebView.getSettings().setAppCachePath(appCachePath);
        mCotentWebView.getSettings().setAllowFileAccess(true);
        mCotentWebView.getSettings().setAppCacheEnabled(true);


        mCotentWebView.getSettings().setSupportZoom(true);
        //mCotentWebView.getSettings().setPluginsEnabled(true);
        mCotentWebView.getSettings().setUseWideViewPort(true);
        mCotentWebView.getSettings().setBuiltInZoomControls(true);
        mCotentWebView.requestFocus();
        //mCotentWebView.getSettings().setLoadWithOverviewMode(true);
        //mCotentWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mCotentWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {


                mPb.setVisibility(View.VISIBLE);
                mPb.setProgress(newProgress);
                if (newProgress == 100) {
                    mPb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e("ANDROID_LAB", "TITLE=" + title);
                if (UtilTools.empty(title)){
                    mTitleText.setText(title);
                }else {
                    mTitleText.setText(mTitle);
                }
            }

        });

        //mContentWebView.loadUrl("http://10.10.10.182/html5Test/pages/demo/demo2.html");
        //设置Web视图
        //达到禁用国产机中点击数据进行拨号的bug
        mCotentWebView.setWebViewClient(new WebViewClient() {

            /* @RequiresApi(api = Build.VERSION_CODES.N)
             public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                 if (request.isRedirect()) {
                     view.loadUrl(request.getUrl().toString());
                     return true;
                 }
                 return false;
             }*/
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                WebView.HitTestResult hitTestResult = view.getHitTestResult();

                if (hitTestResult != null){
                    return false;
                }
                //当有新连接时，使用当前的 WebView
                if (url != null && "tel".equals(url.substring(0, 3))) {
                    //String phoneNumber = url.substring(4, 14);
                    //url = "tel:" + "1" + phoneNumber;
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    HtmlActivity.this.startActivity(intent);
                } else {
                    if (url != null && url.startsWith("http")) {


                        //view.loadUrl(url);
                        // 如果想继续加载目标页面则调用下面的语句
                        // 如果不想那url就是目标网址，如果想获取目标网页的内容那你可以用HTTP的API把网页扒下来。
                        // 返回true表示停留在本WebView（不跳转到系统的浏览器）
                        //Android8.0以下的需要返回true 并且需要loadUrl；8.0之后效果相反
                        if(Build.VERSION.SDK_INT<26) {
                            view.loadUrl(url);
                            return true;
                        }else {
                            return false;
                        }

                    }
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.contains("registerSuccess")){
                    mCanBack = false;
                }else {
                    mCanBack = true;
                }
            }
        });

        if (mShowType.equals("1")){
            mCotentWebView.loadUrl(mUrl);
        }else {
            urlData();
        }


    }


    private void contentData() {
        mCotentWebView.loadDataWithBaseURL(null, mHtmlContent, "text/html", "utf-8", null);
        //mContentWebView.loadUrl(mUrl);
    }

    private void urlData() {

        LogUtilDebug.i("--------htmlActivity----------------",mUrl);
        if (mUrl.startsWith("http")){
            mCotentWebView.loadUrl(mUrl);
        }else {
            mCotentWebView.loadDataWithBaseURL(null, mUrl, "text/html", "utf-8", null);
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mCotentWebView.canGoBack() && mCanBack) {
            mCotentWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mCotentWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.back_img, R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        if (mCotentWebView != null) {

            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mCotentWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mCotentWebView);
            }

            mCotentWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mCotentWebView.getSettings().setJavaScriptEnabled(false);
            mCotentWebView.clearHistory();
            mCotentWebView.clearView();
            mCotentWebView.removeAllViews();
            mCotentWebView.destroy();

        }
        super.onDestroy();
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
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
