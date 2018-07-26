package com.lzx.easymvp.example;

import com.lzx.easymvp.R;
import com.lzx.easymvp.base.BaseMvpActivity;

/**
 * 例子4：不使用 mvp 的情况
 */
public class ExampleActivity4 extends BaseMvpActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {

    }
}
