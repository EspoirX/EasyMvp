package com.lzx.easymvp.example;

import android.util.Log;

import com.lzx.easymvp.R;
import com.lzx.easymvp.base.BaseMvpActivity;
import com.lzx.easymvp.mvp.CreatePresenter;
import com.lzx.easymvp.mvp.PresenterVariable;


/**
 * create by lzx
 * time:2018/7/26
 */

@CreatePresenter(presenter = {LoginPresenter.class, RregisterPresenter.class})
public class MainActivity extends BaseMvpActivity implements LoginContract.View, RregisterContract.View {

    @PresenterVariable
    private LoginPresenter mLoginPresenter;
    @PresenterVariable
    private RregisterPresenter mRregisterPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mLoginPresenter.login();
        mRregisterPresenter.register();
    }

    @Override
    public void loginSuccess() {
        Log.i("MainActivity", "登陆成功");
    }

    @Override
    public void registerSuccess() {
        Log.i("MainActivity", "注册成功");
    }
}
