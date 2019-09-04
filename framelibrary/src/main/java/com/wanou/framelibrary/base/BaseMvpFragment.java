package com.wanou.framelibrary.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * Author by wodx521
 * Date on 2018/11/13.
 */
public abstract class BaseMvpFragment<T extends BasePresenter> extends BaseFragment {
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        //绑定View
        mPresenter.attachView(this);
    }

    protected abstract T getPresenter();


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void getNetStatus(boolean isConnect) {

    }
}
