package cn.wildfire.chat.kit;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;

import butterknife.BindView;


public class WfcWebViewActivity extends WfcBaseActivity {
    private String url;

    @BindView(R.id.webview)
    WebView webView;

    public static void loadUrl(Context context, String title, String url) {
        Intent intent = new Intent(context, WfcWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected int contentLayout() {
        return R.layout.activity_webview_kit;
    }

    @Override
    protected void afterViews() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    String title = view.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        setTitle(title);
                    }
                }
            });
        } else {
            setTitle(title);
        }
    }
}
