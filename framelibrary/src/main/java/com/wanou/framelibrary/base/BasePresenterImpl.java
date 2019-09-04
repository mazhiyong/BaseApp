package com.wanou.framelibrary.base;

/**
 * Author by wodx521
 * Date on 2018/11/10.
 */
public class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {
    protected T mPresenterView;

    @Override
    public void attachView(T page) {
        this.mPresenterView = page;
    }

    @Override
    public void detachView() {
        this.mPresenterView = null;
    }

}
