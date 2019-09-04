package com.wanou.framelibrary.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.gyf.immersionbar.ImmersionBar;
import com.wanou.framelibrary.R;
import com.wanou.framelibrary.manager.ActivityManage;
import com.wanou.framelibrary.receiver.NetStatusReceiver;
import com.wanou.framelibrary.weight.SimpleMultiStateView;


/**
 * Author by wodx521
 * Date on 2018/11/10.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView, View.OnClickListener {

    protected Bundle mBundle;
    protected SimpleMultiStateView mSimpleMultiStateView;
    protected NetStatusReceiver netStatusReceiver;

    public void startActivity(Context context, Bundle bundle, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        context.startActivity(intent);
    }

    public void compatStartActivity(Context context, Bundle intentBundle, Bundle bundle, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        if (intentBundle != null) {
            intent.putExtra("bundle", intentBundle);
        }
        ActivityCompat.startActivity(context, intent, bundle);
    }

    public void startActivityForResult(Context context, Bundle bundle, int requestCode, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).startActivityForResult(intent, requestCode);
        }
    }

    public void compatStartActivityForResult(Context context, Bundle intentBundle, Bundle bundle, int requestCode, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        if (intentBundle != null) {
            intent.putExtra("bundle", intentBundle);
        }
        if (context instanceof BaseActivity) {
            ActivityCompat.startActivityForResult((BaseActivity) context, intent, requestCode, bundle);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        highApiEffects();
        ActivityManage.getInstance().addActivity(this);
        setStatusBar();
        setContentView(getResId());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netStatusReceiver = new NetStatusReceiver();
        registerReceiver(netStatusReceiver, intentFilter);
        mBundle = getIntent().getBundleExtra("bundle");
        initView();
        initStateView();
        netStatusReceiver.setNetStatusListener(new NetStatusReceiver.NetStatusListener() {
            @Override
            public void onNetChange(boolean netStatus) {
                getNetStatus(netStatus);
            }

            @Override
            public void onWifi(boolean isWifiConneted) {

            }

            @Override
            public void onMobile(boolean isMobileData) {

            }
        });
    }

    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.libWhite)     //状态栏颜色，不写默认透明色
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                .removeSupportAllView() //移除全部view支持
                .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
//                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
//                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
                .addTag("tag")  //给以上设置的参数打标记
                .getTag("tag")  //根据tag获得沉浸式参数
                .init();  //必须调用方可沉浸式
    }

    //设置布局id
    protected abstract int getResId();

    //初始化view
    protected abstract void initView();

    private void initStateView() {
        if (mSimpleMultiStateView == null) return;
        mSimpleMultiStateView.setEmptyResource(R.layout.view_empty)
                .setRetryResource(R.layout.view_retry)
                .setLoadingResource(R.layout.view_loading)
                .setNoNetResource(R.layout.view_nonet)
                .build()
                .setonReLoadlistener(this::onRetry);
    }

    // 获取网络状态
    protected abstract void getNetStatus(boolean isConnect);

    /**
     * 初始化数据
     */
    public void initDatas() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManage.getInstance().removeActivity(this);
        if (netStatusReceiver != null) {
            unregisterReceiver(netStatusReceiver);
            netStatusReceiver = null;
        }
    }

    // 初始化点击事件
    protected void initClickListener() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void highApiEffects() {
        getWindow().getDecorView().setFitsSystemWindows(true);
        //透明状态栏 @顶部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    protected void viewGone(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setVisibility(View.GONE);
            }
        }
    }

    protected void viewVisible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void viewInvisible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showLoading() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showContent();
        }
    }

    @Override
    public void showFaild() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showErrorView();
        }
    }

    @Override
    public void showNoNet() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showNoNetView();
        }
    }

    @Override
    public void onRetry() {
        initDatas();
    }

    @Override
    public void onClick(View v) {

    }
}
