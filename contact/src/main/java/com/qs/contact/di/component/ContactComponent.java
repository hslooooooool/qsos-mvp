package com.qs.contact.di.component;

import com.qs.arm.di.component.AppComponent;
import com.qs.arm.di.scope.ActivityScope;
import com.qs.contact.di.module.ContactModule;
import com.qs.contact.mvp.ui.fragment.ContactFragment;

import dagger.Component;

/**
 * 展示 Component 的用法
 *
 * @author 华清松
 */
@ActivityScope
@Component(modules = ContactModule.class, dependencies = AppComponent.class)
public interface ContactComponent {
    /**
     * 提供 ContactFragment 单例对象给实现类
     *
     * @param fragment 用户列表界面
     */
    void inject(ContactFragment fragment);
}
