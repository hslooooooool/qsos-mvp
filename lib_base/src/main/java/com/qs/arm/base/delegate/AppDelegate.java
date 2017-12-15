package com.qs.arm.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.qs.arm.base.App;
import com.qs.arm.base.BaseApplication;
import com.qs.arm.di.component.AppComponent;
import com.qs.arm.di.component.DaggerAppComponent;
import com.qs.arm.di.module.AppModule;
import com.qs.arm.di.module.ClientModule;
import com.qs.arm.di.module.ConfigModule;
import com.qs.arm.http.imageloader.glide.ImageConfigImpl;
import com.qs.arm.integration.ActivityLifecycle;
import com.qs.arm.integration.ManifestParser;
import com.qs.arm.integration.lifecycle.ActivityLifecycleForRxLifecycle;
import com.qs.arm.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * AppDelegate 可以代理 Application 的生命周期,在对应的生命周期,执行对应的逻辑,因为 Java 只能单继承
 * 所以当遇到某些三方库需要继承于它的 Application 的时候,就只有自定义 Application 并继承于三方库的 Application
 * 这时就不用再继承 BaseApplication,只用在自定义Application中对应的生命周期调用AppDelegate对应的方法
 * (Application一定要实现APP接口),框架就能照常运行
 *
 * @author 华清松
 * @see BaseApplication
 */
public class AppDelegate implements App, AppLifecycle {
    private Application mApplication;
    private AppComponent mAppComponent;
    @Inject
    protected ActivityLifecycle mActivityLifecycle;
    @Inject
    protected ActivityLifecycleForRxLifecycle mActivityLifecycleForRxLifecycle;
    private List<com.qs.arm.integration.ConfigModule> mModules;
    private List<AppLifecycle> mAppLifeCycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallback;

    public AppDelegate(Context context) {
        this.mModules = new ManifestParser(context).parse();
        for (com.qs.arm.integration.ConfigModule module : mModules) {
            module.injectAppLifecycle(context, mAppLifeCycles);
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(Context base) {
        for (AppLifecycle lifecycle : mAppLifeCycles) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(Application application) {
        this.mApplication = application;
        mAppComponent = DaggerAppComponent
                .builder()
                // 提供application
                .appModule(new AppModule(mApplication))
                // 提供OKHttp和Retrofit的单例
                .clientModule(new ClientModule())
                // 加载全局配置
                .configModule(getConfigModule(mApplication, mModules))
                .build();
        mAppComponent.inject(this);
        mAppComponent.extras().put(com.qs.arm.integration.ConfigModule.class.getName(), mModules);

        this.mModules = null;

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);

        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }

        mComponentCallback = new AppComponentCallbacks(mApplication, mAppComponent);
        mApplication.registerComponentCallbacks(mComponentCallback);

        for (AppLifecycle lifecycle : mAppLifeCycles) {
            lifecycle.onCreate(mApplication);
        }

    }

    @Override
    public void onTerminate(Application application) {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mActivityLifecycleForRxLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);
        }
        if (mComponentCallback != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallback);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifeCycles != null && mAppLifeCycles.size() > 0) {
            for (AppLifecycle lifecycle : mAppLifeCycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycleForRxLifecycle = null;
        this.mActivityLifecycles = null;
        this.mComponentCallback = null;
        this.mAppLifeCycles = null;
        this.mApplication = null;
    }


    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link com.qs.arm.integration.ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return ConfigModule
     */
    private ConfigModule getConfigModule(Context context, List<com.qs.arm.integration.ConfigModule> modules) {
        ConfigModule.Builder builder = ConfigModule.builder();
        for (com.qs.arm.integration.ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }
        return builder.build();
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return AppComponent
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppComponent, "%s cannot be null,first call %s#onCreate(Application) in %s#onCreate()", AppComponent.class.getName(), getClass().getName(), Application.class.getName());
        return mAppComponent;
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {
        private Application mApplication;
        private AppComponent mAppComponent;

        AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.mApplication = application;
            this.mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            // 内存不足时清理图片缓存
            mAppComponent.imageLoader().clear(mApplication, ImageConfigImpl.builder().isClearMemory(true).build());
        }
    }

}

