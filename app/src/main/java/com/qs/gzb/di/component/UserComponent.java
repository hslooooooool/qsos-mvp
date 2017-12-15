package com.qs.gzb.di.component;

import com.qs.arm.di.component.AppComponent;
import com.qs.arm.di.scope.ActivityScope;
import com.qs.gzb.di.module.UserModule;
import com.qs.gzb.mvp.ui.activity.UserActivity;

import dagger.Component;

/**
 * 展示 Component 的用法
 *
 * @author 华清松
 */
@ActivityScope
@Component(modules = UserModule.class, dependencies = AppComponent.class)
public interface UserComponent {
    /**
     * 提供UserActivity单例对象给实现类
     *
     * @param activity activity
     */
    void inject(UserActivity activity);
}
