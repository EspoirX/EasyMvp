package com.lzx.easymvp.example;


import android.util.Log;

import com.lzx.easymvp.R;
import com.lzx.easymvp.base.BaseMvpActivity;
import com.lzx.easymvp.example.login.LoginPresenter;
import com.lzx.easymvp.example.login.LoginView;
import com.lzx.easymvp.example.register.RegisterPresenter;
import com.lzx.easymvp.example.register.RegisterView;
import com.lzx.easymvp.mvp.CreatePresenter;

/**
 * 例子2：多个Presenter和使用 getPresenter 方法获取实例
 */
@CreatePresenter(presenter = {LoginPresenter.class, RegisterPresenter.class})
public class ExampleActivity2 extends BaseMvpActivity implements LoginView, RegisterView {

    private LoginPresenter mLoginPresenter;
    private RegisterPresenter mRegisterPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mLoginPresenter = getPresenterProviders().getPresenter(0);
        mRegisterPresenter = getPresenterProviders().getPresenter(1);

        mLoginPresenter.login();
        mRegisterPresenter.register();
    }

    @Override
    public void loginSuccess() {
        Log.i("ExampleActivity1", "登陆成功");
    }

    @Override
    public void registerSuccess() {
        Log.i("ExampleActivity1", "注册成功");
    }
}

