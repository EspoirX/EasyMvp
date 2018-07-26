package com.lzx.easymvp.example;


import com.lzx.easymvp.mvp.BasePresenter;

/**
 * create by lzx
 * time:2018/7/26
 */
public class RregisterPresenter extends BasePresenter<RregisterContract.View> implements RregisterContract.Presenter<RregisterContract.View> {
    @Override
    public void register() {
        mView.registerSuccess();
    }
}
