package com.qs.arm.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.qs.arm.integration.IRepositoryManager;

/**
 * 基类 Model
 *
 * @author 华清松
 */
public class BaseModel implements IModel, LifecycleObserver {
    /**
     * 用于管理网络请求层,以及数据缓存层
     */
    protected IRepositoryManager mRepositoryManager;

    public BaseModel(IRepositoryManager repositoryManager) {
        this.mRepositoryManager = repositoryManager;
    }

    /**
     * 在框架中 {@link BasePresenter#onDestroy()} 时会默认调用 {@link IModel#onDestroy()}
     */
    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
