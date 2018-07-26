package com.lzx.easymvp.example.login;


import com.lzx.easymvp.mvp.BaseContract;

/**
 * create by lzx
 * time:2018/7/26
 */
public interface LoginContract {
    interface Presenter<V> extends BaseContract.Presenter<V> {
        void login();
    }

    interface View extends BaseContract.View {
        void loginSuccess();
    }
}
