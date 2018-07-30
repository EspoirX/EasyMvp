package com.lzx.easymvp.example;

import com.lzx.easymvp.ExampleFragment;
import com.lzx.easymvp.R;
import com.lzx.easymvp.base.BaseMvpActivity;
import com.lzx.easymvp.example.login.LoginPresenter;
import com.lzx.easymvp.example.register.RegisterPresenter;
import com.lzx.easymvp.mvp.CreatePresenter;
import com.lzx.easymvp.mvp.PresenterVariable;

/**
 * 例子4：Fragment
 */
public class ExampleActivity5 extends BaseMvpActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_fragment;
    }

    @Override
    public void init() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, new ExampleFragment())
                .commit();
    }
}
