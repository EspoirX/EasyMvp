# EasyMvp
一个简单强大且灵活的MVP框架。

## 特点
- 原理简单可以定制成自己想要的方式。
- 一个 Activity 可以绑定多个 Presenter，以达到最大的复用功能。
- 采用注解的方式实现依赖注入，减少耦合。
- 可以灵活管理生命周期。
- 使用起来方便简单
- 使用例子可以在项目中找到。

## 使用方法

（以简单的登陆注册为例）

- 单个 Presenter 的情况
1. 定义好你的 View 层接口方法并继承一个公共的 BaseMvpView 如 LoginView:
``` java
public interface LoginView extends BaseMvpView {
    void loginSuccess();
}


```
2. 编写 LoginPresenter 继承 BasePresenter 并实现你的 Presenter 功能方法：
```java
public class LoginPresenter extends BasePresenter<LoginView> {

    public void login() {
        mView.loginSuccess();
    }
}
```

3. Activity 继承 BaseMvpActivity 并实现你的 View 接口：
```java
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

```
其中在 Activity 中，Presenter 实例的获取方法可以有上面代码所示的通过 getPresenter 来获取，这种方法需要在 BaseMvpActivity 后面
填入泛型参数你的 Presenter 实现类，比如上面所示的 LoginPresenter。
除了这种方法，也可以通过注解的方式获取实例：

```java
@CreatePresenter(presenter = LoginPresenter.class)
public class ExampleActivity3 extends BaseMvpActivity implements LoginView {
    @PresenterVariable
    private LoginPresenter mLoginPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mLoginPresenter.login();
    }

    @Override
    public void loginSuccess() {
        Log.i("ExampleActivity1", "登陆成功");
    }
}
```

如果不喜欢注解，还可以通过直接获取的方式获取：
```java
@CreatePresenter(presenter = LoginPresenter.class)
public class ExampleActivity3 extends BaseMvpActivity implements LoginView {

    private LoginPresenter mLoginPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
      mLoginPresenter = getPresenterProviders().getPresenter(0);
      mLoginPresenter.login();
    }

    @Override
    public void loginSuccess() {
        Log.i("ExampleActivity1", "登陆成功");
    }
}
```
通过 getPresenterProviders().getPresenter(int index) 方法获取，传入的参数是你通过 @CreatePresenter 注解传入的 class 对象的
数组下标，这里因为只有一个 Presenter, 所以传 0 即可。


- 多个 Presenter 的情况

多个 Presenter 的情况前两个步骤跟上面一样，主要是在 Activity 绑定这边有些区别：
```java
@CreatePresenter(presenter = {LoginPresenter.class, RegisterPresenter.class})
public class ExampleActivity1 extends BaseMvpActivity implements  LoginView, RegisterView  {

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
        //也可以通过这种方式去获取实例
        //mLoginPresenter = getPresenterProviders().getPresenter(0);
        //mRegisterPresenter = getPresenterProviders().getPresenter(1);

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
```
如上所示，假如一个 Activity 中需要同时用到 LoginPresenter 和 RegisterPresenter 中的某些方法，只需要使用注解
@CreatePresenter 依次传入它们的 class 对象即可完成绑定。同样地为了得到各个 Presenter 的实例，可以通过 @PresenterVariable
注解去获取，当然如果不喜欢用注解，也可以通过 getPresenterProviders().getPresenter(int index) 方法去获取哦。

- 不需要使用 Mvp 的情况

并不是所有地方都需要用到 Mvp，当不需要用的时候，其实也没什么特别，就正常用就行：
```java
public class ExampleActivity4 extends BaseMvpActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {

    }
}
```

## 编写 BasePresenter, BaseMvpView, BaseMvpActivity 等一些基础类

上面例子中有用到 BasePresenter, BaseMvpView, BaseMvpActivity 等一些基础类，这里给出一种例子，用户可根据自己需要去编写。

- BaseMvpView

BaseMvpView 是基础的 View 层接口：
```java
public interface BaseMvpView {
    void showError(String msg);

    void complete();

    void showProgressUI(boolean isShow);
}

```
可以看到就是定义了一些公共的接口方法。其实这个接口可以不需要，但是一般为了方便，就写上吧。

- BasePresenter

BasePresenter 就是基础的 BasePresenter 了，作用也是实现一些公共的 Presenter 接口方法：

```java
public class BasePresenter <V>  {

    protected Context mContext;
    protected V mView;

    protected void onCleared() {

    }

    public void attachView(Context context, V view) {
        this.mContext = context;
        this.mView = view;
    }

    public void detachView() {
        this.mView = null;
    }

    public boolean isAttachView() {
        return this.mView != null;
    }

    public void onCreatePresenter(@Nullable Bundle savedState) {

    }

    public void onDestroyPresenter() {
        this.mContext = null;
        detachView();
    }

    public void onSaveInstanceState(Bundle outState) {

    }
}
```

里面实现一些公共的 Presenter 方法，一般像 attachView，detachView ，isAttachView 等方法是一定要实现的，因为这些与生命周期绑定有关，可以做资源的赋值和释放等操作。

- BaseMvpActivity

这个大家都知道，就是 Activity 的基类了，看看它的一种实现：
```java
public abstract class BaseMvpActivity<P  extends BasePresenter> extends AppCompatActivity  implements BaseMvpView {

    private PresenterProviders mPresenterProviders;
    private PresenterDispatch mPresenterDispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);

        mPresenterDispatch.attachView(this, this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDispatch.onSaveInstanceState(outState);
    }

    protected abstract int getContentView();

    public abstract void init();

    protected P getPresenter() {
        return mPresenterProviders.getPresenter(0);
    }

    public PresenterProviders getPresenterProviders() {
        return mPresenterProviders;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showProgressUI(boolean isShow) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterDispatch.detachView();
    }
}
```

BaseMvpActivity 后面定义的泛型 P 主要是为了一个 Presenter 的时候使用 getPresenter() 方法时用到的，代码如上面例子所示。可以看看 getPresenter() 的实现：
```java
protected P getPresenter() {
    return mPresenterProviders.getPresenter(0);
}
```
也只是调用了 PresenterProviders.getPresenter(int index) 方法而已。

然后 BaseMvpActivity 实现了 BaseMvpView 接口，默认实现了一些公共方法，当你继承它的时候，需要可以重写他们。

主要说一下 PresenterProviders，这个类的作用是解析用到的注解以及完成绑定和解绑 View 等一些公共的 Presenter 操作。

1. 首先调用 inject 方法实例化，传入上下文参数。
2. 通过查看 inject 的实现，它里面调用了构造方法，构造方法再调用 resolveCreatePresenter 和 resolvePresenterVariable 方法来进行对注解 @CreatePresenter 和 @PresenterVariable 的解析。
```java
public static PresenterProviders inject(Context context) {
    return new PresenterProviders(context);
}

private PresenterProviders(Context context) {
    mContext = checkContext(context);
    resolveCreatePresenter();
    resolvePresenterVariable();
}
```

下面简单分析一下 PresenterProviders 具体实现：

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
因为需要处理的是一个或多个 Presenter 对象，所以这样做的目的是为了可以统一管理和查找。

3. 然后到了主要的 PresenterProviders 类
这个类主要看几个方法，第一个 resolveCreatePresenter() 方法：
```java
public <P extends BasePresenter> PresenterProviders resolveCreatePresenter() {
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
resolveCreatePresenter() 方法主要的作用是解析 @CreatePresenter 注解，过程是这样的：首先获取到注解上所有定义的 class 对象数组 classes，然后
循环，取它们的 canonicalName 作为 key ,调用 newInstance 方法实例化后作为 value 存入上面说到的 HasMap 中。

接下来是 resolvePresenterVariable 方法：
```java
public <P extends BasePresenter> PresenterProviders resolvePresenterVariable() {
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
resolvePresenterVariable 方法主要的作用就是为将用 @PresenterVariable 注解标记的对象在 HashMap 中找到对应的实例，并赋值。过程是这样的，首先通过 getDeclaredFields 获取类上所以的变量的 Field，然后判断如果该变量有标记 @PresenterVariable
 注解的话，就取它的 Type 对应的 Name，这个 Name 的值是会与 canonicalName 一样的，所以就可以通过它作为 key 在 HashMap 中查找对应的实例，找到后通过 Field 的 set 方法给变量赋值。


还有一个类 PresenterDispatch ：

可以看到 attachView 和 detachView 等一些操作都是通过它来完成的，之所以需要这个类，是为了隔离开 PresenterProviders 和 PresenterStore，免得耦合。

```java
public class PresenterDispatch {
    private PresenterProviders mProviders;

    public PresenterDispatch(PresenterProviders providers) {
        mProviders = providers;
    }

    public <P extends BasePresenter> void attachView(Context context, BaseContract.View view) {
        PresenterStore store = mProviders.getPresenterStore();
        HashMap<String, P> mMap = store.getMap();
        for (Map.Entry<String, P> entry : mMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.attachView(context, view);
            }
        }
    }

    public <P extends BasePresenter> void detachView() {
        PresenterStore store = mProviders.getPresenterStore();
        HashMap<String, P> mMap = store.getMap();
        for (Map.Entry<String, P> entry : mMap.entrySet()) {
            P presenter = entry.getValue();
            if (presenter != null) {
                presenter.detachView();
            }
        }
    }
    ...
}
```

可以看到他里面是拿到了 PresenterProviders 对象，然后再拿到存储了 Presenter 实例的 HashMap，然后再进行操作。其他一些公共实现也可以这样做。


整个过程就完成了，是不是很简单。在实际的运用过程中可以根据自己额需要做对应的修改。
喜欢就给个 Star 吧，欢迎留言提 Issues 和建议。