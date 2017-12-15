
package com.qs.arm.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.qs.arm.base.delegate.AppDelegate;
import com.qs.arm.base.delegate.AppLifecycle;
import com.qs.arm.di.component.AppComponent;
import com.qs.arm.utils.Preconditions;

/**
 * 请在AndroidManifest修改为BaseApplication
 *
 * @author 华清松
 */
public class BaseApplication extends Application implements App {
    /**
     * Application生命周期代理
     */
    private AppLifecycle appLifecycle;

    /**
     * 在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (appLifecycle == null) {
            appLifecycle = new AppDelegate(base);
        }
        appLifecycle.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (appLifecycle != null) {
            appLifecycle.onCreate(this);
        }
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (appLifecycle != null) {
            appLifecycle.onTerminate(this);
        }
    }

    /**
     * 将 {@link AppComponent} 返回出去,供其它地方使用,{@link AppComponent} 中声明的方法所返回的实例
     * 在通过此方法拿到对象后都可以直接使用
     *
     * @return AppComponent
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(appLifecycle, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(appLifecycle instanceof App, "%s must be implements %s", AppDelegate.class.getName(), App.class.getName());
        return ((App) appLifecycle).getAppComponent();
    }

}
