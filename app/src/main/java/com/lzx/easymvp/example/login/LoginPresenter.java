package com.lzx.easymvp.example.login;


import com.lzx.easymvp.mvp.BasePresenter;

/**
 * create by lzx
 * time:2018/7/26
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View> {

    @Override
    public void login() {
        mView.loginSuccess();
    }
}
