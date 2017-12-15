package com.qs.arm.integration;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.qs.arm.base.delegate.AppLifecycle;

import java.util.List;

/**
 * {@link ConfigModule} 可以给框架配置一些参数,需要实现 {@link ConfigModule} 后,在 AndroidManifest 中声明该实现类
 */
public interface ConfigModule {
    /**
     * 使用{@link com.qs.arm.di.module.ConfigModule.Builder}给框架配置一些配置参数
     *
     * @param context
     * @param builder
     */
    void applyOptions(Context context, com.qs.arm.di.module.ConfigModule.Builder builder);

    /**
     * 使用{@link AppLifecycle}在Application的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectAppLifecycle(Context context, List<AppLifecycle> lifecycles);

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks}在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles);


    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks}在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}
