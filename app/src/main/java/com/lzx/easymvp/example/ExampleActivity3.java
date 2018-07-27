package com.lzx.easymvp.example;

import android.util.Log;

import com.lzx.easymvp.R;
import com.lzx.easymvp.base.BaseMvpActivity;
import com.lzx.easymvp.example.login.LoginPresenter;
import com.lzx.easymvp.example.login.LoginView;
import com.lzx.easymvp.mvp.CreatePresenter;

/**
 * 例子3：一个Presenter和使用 getPresenter 方法获取实例
 */
@CreatePresenter(presenter = LoginPresenter.class)
public class ExampleActivity3 extends BaseMvpActivity<LoginPresenter> implements LoginView {

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        getPresenter().login();
    }

    @Override
    public void loginSuccess() {
        Log.i("ExampleActivity1", "登陆成功");
    }
}


