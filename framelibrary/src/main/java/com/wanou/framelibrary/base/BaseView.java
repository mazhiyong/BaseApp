package com.wanou.framelibrary.base;

/**
 * Author by wodx521
 * Date on 2018/11/10.
 */
public interface BaseView {
    //显示进度中
    void showLoading();

    //显示请求成功
    void showSuccess();

    //失败重试
    void showFaild();

    //显示当前网络不可用
    void showNoNet();

    //重试
    void onRetry();
}
