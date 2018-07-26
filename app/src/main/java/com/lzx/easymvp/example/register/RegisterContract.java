package com.lzx.easymvp.example.register;


import com.lzx.easymvp.mvp.BaseContract;

/**
 * create by lzx
 * time:2018/7/26
 */
public interface RegisterContract {
    interface Presenter<V> extends BaseContract.Presenter<V> {
        void register();
    }

    interface View extends BaseContract.View {
        void registerSuccess();
    }
}
