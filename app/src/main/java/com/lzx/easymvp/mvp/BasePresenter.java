package com.lzx.easymvp.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * create by lzx
 * time:2018/7/26
 */
public class BasePresenter <V>  {

    protected Context mContext;
    protected V mView;

    protected void onCleared() {

    }

    public void attachView(Context context, V view) {
        this.mContext = context;
        this.mView = view;
    }


    public void detachView() {
        this.mView = null;
    }

    public boolean isAttachView() {
        return this.mView != null;
    }


    public void onCreatePresenter(@Nullable Bundle savedState) {

    }

    public void onDestroyPresenter() {
        this.mContext = null;
        detachView();
    }

    public void onSaveInstanceState(Bundle outState) {

    }
}
