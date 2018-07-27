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
        void showError(String msg); //展示错误提示

        void complete();  //操作完成，比如网络请求等

        void showProgressUI(boolean isShow); //展示 loading UI等
    }

    interface Presenter<V> {
        void attachView(Context context, V view); //绑定View

        void detachView(); //解绑View

        boolean isAttachView(); //判断是否绑定View

        void onCreatePresenter(@Nullable Bundle savedState); //Presenter创建后调用

        void onDestroyPresenter();  //Presenter销毁后调用

        void onSaveInstanceState(Bundle outState);  //跟 onSaveInstanceState 方法一样
    }
}
