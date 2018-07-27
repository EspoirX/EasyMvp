package com.lzx.easymvp.mvp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * create by lzx
 * time:2018/7/26
 */
public class PresenterProviders {

    private PresenterStore mPresenterStore = new PresenterStore<>();
    private Context mContext;

    public static PresenterProviders inject(Context context) {
        return new PresenterProviders(context);
    }

    private PresenterProviders(Context context) {
        mContext = checkContext(context);
        resolveCreatePresenter();
        resolvePresenterVariable();
    }

    private static Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to Application. You can't request PresenterProviders before onCreate call.");
        }
        return application;
    }

    private static Activity checkActivity(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Can't create PresenterProviders for detached fragment");
        }
        return activity;
    }

    private static Context checkContext(Context context) {
        Context resultContent = null;
        if (context instanceof Activity) {
            resultContent = context;
        }
        if (resultContent == null) {
            throw new IllegalStateException("Context must Activity Context");
        }
        return resultContent;
    }

    private <P extends BasePresenter> PresenterProviders resolveCreatePresenter() {
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

    private <P extends BasePresenter> PresenterProviders resolvePresenterVariable() {
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


    public <P extends BasePresenter> P getPresenter(int index) {
        CreatePresenter createPresenter = mContext.getClass().getAnnotation(CreatePresenter.class);
        if (createPresenter == null) {
            return null;
        }
        if (createPresenter.presenter().length == 0) {
            return null;
        }
        if (index >= 0 && index < createPresenter.presenter().length) {
            String key = createPresenter.presenter()[index].getCanonicalName();
            BasePresenter presenter = mPresenterStore.get(key);
            if (presenter != null) {
                return (P) presenter;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public PresenterStore getPresenterStore() {
        return mPresenterStore;
    }
}
