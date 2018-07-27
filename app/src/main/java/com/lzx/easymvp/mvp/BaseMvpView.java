package com.lzx.easymvp.mvp;

/**
 * create by lzx
 * time:2018/7/27
 */
public interface BaseMvpView {
    void showError(String msg);

    void complete();

    void showProgressUI(boolean isShow);
}
