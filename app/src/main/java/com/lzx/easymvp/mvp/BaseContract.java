package com.lzx.easymvp.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * create by lzx
 * time:2018/7/26
 */
public interface BaseContract {
    interface View {
        void showError(String msg);

        void complete();

        void showProgressUI(boolean isShow);
    }

    interface Presenter<V> {
        void attachView(Context context, V view);

        void detachView();

        boolean isAttachView();

        void onCreatePresenter(@Nullable Bundle savedState);

        void onDestroyPresenter();

        void onSaveInstanceState(Bundle outState);
    }
}
