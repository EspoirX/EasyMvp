package com.lzx.easymvp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lzx.easymvp.mvp.BaseContract;
import com.lzx.easymvp.mvp.PresenterProviders;


public abstract class BaseMvpActivity extends AppCompatActivity implements BaseContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        PresenterProviders.inject(this).of().get().attachView(this, this);
        init();
    }

    protected abstract int getContentView();

    public abstract void init();

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showProgressUI(boolean isShow) {

    }

}
