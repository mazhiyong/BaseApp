package com.wanou.framelibrary.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanou.framelibrary.R;
import com.wanou.framelibrary.receiver.NetStatusReceiver;
import com.wanou.framelibrary.utils.UiTools;
import com.wanou.framelibrary.weight.SimpleMultiStateView;

/**
 * Author by wodx521
 * Date on 2018/11/10.
 */
public abstract class BaseFragment extends Fragment implements BaseView, View.OnClickListener {
    protected NetStatusReceiver netStatusReceiverFragment;
    SimpleMultiStateView mSimpleMultiStateView;

    public void startActivity(Fragment fragment, Bundle bundle, Class<?> cls) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        fragment.startActivity(intent);
    }

    public void startActivityForResult(Fragment fragment, Bundle bundle, int requestCode, Class<?> cls) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        netStatusReceiverFragment = new NetStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        Context context = UiTools.getContext();

        context.registerReceiver(netStatusReceiverFragment, filter);
        if (getResId() > 0) {
            return inflater.inflate(getResId(), container, false);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initStateView();
        initClickListener();
        initData();

        netStatusReceiverFragment.setNetStatusListener(new NetStatusReceiver.NetStatusListener() {
            @Override
            public void onNetChange(boolean netStatus) {
                getNetStatus(netStatus);
            }

            @Override
            public void onWifi(boolean isWifiConnected) {

            }

            @Override
            public void onMobile(boolean isMobileData) {

            }
        });
    }

    protected abstract void initView(View view);

    private void initStateView() {
        if (mSimpleMultiStateView == null) return;
        mSimpleMultiStateView.setEmptyResource(R.layout.view_empty)
                .setRetryResource(R.layout.view_retry)
                .setLoadingResource(R.layout.view_loading)
                .setNoNetResource(R.layout.view_nonet)
                .build()
                .setonReLoadlistener(this::onRetry);
    }

    protected void initClickListener() {

    }

    protected abstract void initData();

    public abstract void getNetStatus(boolean isConnect);

    /**
     * 初始化数据的空实现，fragment切换加载数据的时候重写
     */
    protected void initDataOnUserVisible() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (netStatusReceiverFragment != null) {
            UiTools.getContext().unregisterReceiver(netStatusReceiverFragment);
            netStatusReceiverFragment = null;
        }
    }

    protected abstract int getResId();

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
        initDataOnUserVisible();
    }

    @Override
    public void onClick(View v) {

    }
}
