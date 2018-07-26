package com.lzx.easymvp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lzx.easymvp.mvp.BaseContract;
import com.lzx.easymvp.mvp.PresenterProviders;


public abstract class BaseMvpActivity<P extends BaseContract.Presenter> extends AppCompatActivity implements BaseContract.View {

    private PresenterProviders mPresenterProviders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterProviders.of().get().attachView(this, this);
        init();
    }

    protected abstract int getContentView();

    public abstract void init();

    protected P getPresenter() {
        return mPresenterProviders.getPresenter(0);
    }

    public PresenterProviders getPresenterProviders() {
        return mPresenterProviders;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showProgressUI(boolean isShow) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterProviders.detachView();
    }
}
