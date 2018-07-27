package com.lzx.easymvp.example;

import android.util.Log;

import com.lzx.easymvp.R;
import com.lzx.easymvp.base.BaseMvpActivity;
import com.lzx.easymvp.example.login.LoginPresenter;
import com.lzx.easymvp.example.login.LoginView;
import com.lzx.easymvp.example.register.RegisterPresenter;
import com.lzx.easymvp.example.register.RegisterView;
import com.lzx.easymvp.mvp.CreatePresenter;
import com.lzx.easymvp.mvp.PresenterVariable;


/**
 * 例子1：多个Presenter和使用@PresenterVariable注解
 * create by lzx
 * time:2018/7/26
 */
@CreatePresenter(presenter = {LoginPresenter.class, RegisterPresenter.class})
public class ExampleActivity1 extends BaseMvpActivity implements LoginView, RegisterView {

    @PresenterVariable
    private LoginPresenter mLoginPresenter;
    @PresenterVariable
    private RegisterPresenter mRegisterPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
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
