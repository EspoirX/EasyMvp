package com.lzx.easymvp.example;


import com.lzx.easymvp.mvp.BaseContract;

/**
 * create by lzx
 * time:2018/7/26
 */
public interface RregisterContract {
    interface Presenter<V> extends BaseContract.Presenter<V> {
        void register();
    }

    interface View extends BaseContract.View {
        void registerSuccess();
    }
}
