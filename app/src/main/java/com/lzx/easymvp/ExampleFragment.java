package com.lzx.easymvp;

import android.util.Log;

import com.lzx.easymvp.base.BaseMvpFragment;
import com.lzx.easymvp.example.login.LoginPresenter;
import com.lzx.easymvp.example.login.LoginView;
import com.lzx.easymvp.example.register.RegisterPresenter;
import com.lzx.easymvp.example.register.RegisterView;
import com.lzx.easymvp.mvp.CreatePresenter;
import com.lzx.easymvp.mvp.PresenterVariable;

/**
 * create by lzx
 * time:2018/7/30
 */
@CreatePresenter(presenter = {LoginPresenter.class, RegisterPresenter.class})
public class ExampleFragment extends BaseMvpFragment implements LoginView, RegisterView {

    @PresenterVariable
    private LoginPresenter mLoginPresenter;
    @PresenterVariable
    private RegisterPresenter mRegisterPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        mLoginPresenter.login();
        mRegisterPresenter.register();
    }

    @Override
    public void loginSuccess() {
        Log.i("ExampleFragment", "登陆成功");
    }

    @Override
    public void registerSuccess() {
        Log.i("ExampleFragment", "注册成功");
    }
}
