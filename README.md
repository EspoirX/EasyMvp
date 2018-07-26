# EasyMvp
一个简单强大且灵活的MVP框架。

## 特点
- 一个 Activity 可以绑定多个 Presenter，以达到最大的复用功能。
- 采用注解的方式实现依赖注入，减少耦合。
- 可以灵活管理生命周期。
- 使用起来方便简单
- 使用例子可以在项目中找到。

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
假如一个 Activity 中需要同时用到 LoginPresenter 和 RregisterPresenter 中的某些方法，可以看到只需要使用注解
@CreatePresenter 依次传入它们的 class 对象，内部就会自动实例化，并且与 Activity 绑定，非常方便。然后在使用
的时候，为了区分和调用各自的方法，可以为它们分别定义一个变量 mLoginPresenter 和 mRregisterPresenter，并且
为它们加上 @PresenterVariable 注解，它们就会各自得到实例化后的对象，就可以直接使用了。

下面来看看这个框架的使用方法吧（以上面代码为例）：
- . 首先，项目里面一般会有一个 BaseActivity，所以，我们需要先定义一个 BaseMvpActivity，这是使用的第一步。
这个框架比较关键的类是 PresenterProviders ,它管理着很多东西(在后面原理分析时会讲到)，首先在 onCreate
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

当我们只需要用到一个 Presenter 的时候，如果还像上面那样定义一个变量加注解的话，有点太麻烦了，所以可以
定义一个 getPresenter 方法直接获取就可以：

```java
protected P getPresenter() {
    return mPresenterProviders.getPresenter(0);
}
```

看到其实是调用了 PresenterProviders 提供的 getPresenter 方法，传入的是下标参数，因为只有一个 Presenter,
所以传 0 就可以了，事实上可以通过这种方式获取到所有 Presenter 实例，因为返回的是 P 泛型，所以 Activity 需要这样写：

```java
public abstract class BaseMvpActivity<P extends BaseContract.Presenter> extends AppCompatActivity implements BaseContract.View {
    ...
}
```

- 第二步，编写契约类定义 Presenter 和 View 方法

```java
public interface LoginContract {
    interface Presenter<V> extends BaseContract.Presenter<V> {
        void login();
    }

    interface View extends BaseContract.View {
        void loginSuccess();
    }
}
```
定义的 Presenter 和 View 接口必须要继承 BaseContract.Presenter 和 BaseContract.View，因为 Presenter
还需要和对应的 View 绑定，所以定义的时候还需要像上面一样定义个泛型。

看看 BaseContract 基础契约类写了什么：
```java
public interface BaseContract {
    interface View {
        void showError(String msg);

        void complete();

        void showProgressUI(boolean isShow);
    }

    interface Presenter<V> {
        void attachView(Context context, V view);

        void detachView();

        boolean isAttachView();

        void onCreatePresenter(@Nullable Bundle savedState);

        void onDestroyPresenter();

        void onSaveInstanceState(Bundle outState);
    }
}
```
其实就是根据自己实际需要定义了一些公共的方法，其中 attachView 和 detachView 应该都要有的了。

编写一个 BasePresenter:

```java
public class BasePresenter <V extends BaseContract.View> implements BaseContract.Presenter<V> {

    protected Context mContext;
    protected V mView;

    protected void onCleared() {
    }

    @Override
    public void attachView(Context context, V view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public boolean isAttachView() {
        return this.mView != null;
    }

    @Override
    public void onCreatePresenter(@Nullable Bundle savedState) {
    }

    @Override
    public void onDestroyPresenter() {
        this.mContext = null;
        detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }
}
```
BasePresenter 可以根据自己需要实现一些公共的操作，比如数据绑定和解绑，生命周期绑定，网络请求的取消等，上面是
一个编写例子。

编写 LoginPresenter:
```java
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View> {

    @Override
    public void login() {
        mView.loginSuccess();
    }
}
```
首先要继承你编写的 BasePresenter，泛型中传入要绑定的 View，然后实现你定义的 Presenter 接口即可，
在登陆方法中完成登陆后直接回调 loginSuccess 给 Activity 即可。

整个使用过程就这样简单，好了，下面简单分析一下具体实现：
1. 关于注解的知识，可以看这篇文章，看完应该就懂了：[深入理解Java注解类型(@Annotation)](https://blog.csdn.net/javazejian/article/details/71860633)
2. PresenterStore 类：
这个类的主要作用就是将 Presenter 的实例存储起来，用的是 HashMap 实现：
```java
private static final String DEFAULT_KEY = "PresenterStore.DefaultKey";
private final HashMap<String, P> mMap = new HashMap<>();

public final void put(String key, P presenter) {
    P oldPresenter = mMap.put(DEFAULT_KEY + ":" + key, presenter);
    if (oldPresenter != null) {
        oldPresenter.onCleared();
    }
}

public final P get(String key) {
    return mMap.get(DEFAULT_KEY + ":" + key);
}

public final void clear() {
    for (P presenter : mMap.values()) {
        presenter.onCleared();
    }
    mMap.clear();
}
```
因为需要处理的是一个或多个 Presenter 对象，所以这样做的目的是为了可以统一管理和查找，所以 attachView 和 detachView 的真正实现也都在这里：
```java
public void attachView(Context context, BaseContract.View view) {
    for (Map.Entry<String, P> entry : mMap.entrySet()) {
        BasePresenter presenter = entry.getValue();
        if (presenter != null) {
            presenter.attachView(context, view);
        }
    }
}

public void detachView() {
    for (Map.Entry<String, P> entry : mMap.entrySet()) {
        BasePresenter presenter = entry.getValue();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
```
当然其他一些 Presenter 的公共实现也可以这么做。

3. 然后到了主要的 PresenterProviders 类
这个类主要看几个方法，第一个 of() 方法：
```java
public <P extends BasePresenter> PresenterProviders of() {
    CreatePresenter createPresenter = mContext.getClass().getAnnotation(CreatePresenter.class);
    if (createPresenter != null) {
        Class<P>[] classes = (Class<P>[]) createPresenter.presenter();
        for (Class<P> clazz : classes) {
            String canonicalName = clazz.getCanonicalName();
            try {
                mPresenterStore.put(canonicalName, clazz.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    return this;
}
```
of() 方法主要的作用是解析 @CreatePresenter 注解，过程是这样的：首先获取到注解上所有定义的 class 对象数组 classes，然后
循环，取它们的 canonicalName 作为 key ,调用 newInstance 方法实例化后作为 value 存入上面说到的 HasMap 中。

接下来是 get 方法：
```java
public <P extends BasePresenter> PresenterProviders get() {
    for (Field field : mContext.getClass().getDeclaredFields()) {
        //获取字段上的注解
        Annotation[] anns = field.getDeclaredAnnotations();
        if (anns.length < 1) {
            continue;
        }
        if (anns[0] instanceof PresenterVariable) {
            String canonicalName = field.getType().getName();
            P presenterInstance = (P) mPresenterStore.get(canonicalName);
            if (presenterInstance != null) {
                try {
                    field.setAccessible(true);
                    field.set(mContext, presenterInstance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    return this;
}
```
get 方法主要的作用就是为将用 @PresenterVariable 注解标记的对象在 HashMap 中找到对应的实例，并赋值。过程是
这样的，首先通过 getDeclaredFields 获取类上所以的变量的 Field，然后判断如果该变量有标记 @PresenterVariable
 注解的话，就取它的 Type 对应的 Name，这个 Name 的值是会与 canonicalName 一样的，所以就可以通过它作为 key
  在 HashMap 中查找对应的实例，找到后通过 Field 的 set 方法给变量赋值。

整个过程就完成了，是不是很简单。
喜欢就给个 star 吧，欢迎留言提 Issues 和建议。