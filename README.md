# EasyMvp
一个简单强大且灵活的MVP框架。

## 特点
- 一个 Activity 可以绑定多个 Presenter，已达到最大的复用功能。
- 采用注解的方式实现依赖注入，减少耦合。
- 可以灵活管理生命周期。

## 使用效果以及用法
先看看使用后代码的效果吧，以 Activity 为例：

```java
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
```
假如一个 Activity 中需要同时用到 LoginPresenter 和 RregisterPresenter 中的某些方法，可以看到自需要使用注解
@CreatePresenter 依次传入它们的 class 对象，内部就会自动实例化，并且与 Activity 绑定，非常方便。然后在使用
的时候，为了区分和调用各自的方法，可以为它们分别定义一个变量 mLoginPresenter 和 mRregisterPresenter，并且
为它们加上 @PresenterVariable 注解，它们就会各自得到实例化后的对象，就可以直接使用了。

下面来看看这个框架的使用方法吧（以上面代码为例）：
- . 首先，项目里面一般会有一个 BaseActivity，所以，我们需要先定义一个 BaseMvpActivity，这是使用的第一步。
1. 这个框架比较关键的类是 PresenterProviders ,它管理着很多东西(在后面原理分析时会讲到)，首先在 onCreate
方法中，我们先实例化 PresenterProviders：

```java
private PresenterProviders mPresenterProviders;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentView());
    mPresenterProviders = PresenterProviders.inject(this);
    ...
}
```

调用 inject 方法实例化，传入上下文，然后再绑定 Activity:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentView());
    mPresenterProviders = PresenterProviders.inject(this);
    mPresenterProviders.of().get().attachView(this, this);
}
```

链式调用 of() , get() , attachView 方法即可绑定。

在 Activity 结束的时候解绑释放资源：

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    mPresenterProviders.detachView();
}
```

调用 detachView 方法解绑。


