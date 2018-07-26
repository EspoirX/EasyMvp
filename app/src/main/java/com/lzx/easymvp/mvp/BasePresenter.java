package com.lzx.easymvp.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * create by lzx
 * time:2018/7/26
 */
public class BasePresenter <V extends BaseContract.View> implements BaseContract.Presenter<V> {

    protected Context mContext;
    protected V mView;

    protected void onCleared() {

    }

    @Override
    public void attachView(Context context, V view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }


    @Override
    public boolean isAttachView() {
        return this.mView != null;
    }

    @Override
    public void onCreatePresenter(@Nullable Bundle savedState) {

    }

    @Override
    public void onDestroyPresenter() {
        this.mContext = null;
        detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


}
