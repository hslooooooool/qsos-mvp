package com.qs.contact.di.module;

import com.qs.arm.di.scope.ActivityScope;
import com.qs.contact.mvp.contract.ContactContract;
import com.qs.contact.mvp.model.ContactModel;

import dagger.Module;
import dagger.Provides;

/**
 * 展示 Module 的用法
 *
 * @author 华清松
 */
@Module
public class ContactModule {
    private ContactContract.View view;

    public ContactModule(ContactContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ContactContract.View provideUserView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ContactContract.Model provideUserModel(ContactModel model) {
        return model;
    }

}
