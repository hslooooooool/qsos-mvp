package com.qs.contact.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qs.arm.di.scope.ActivityScope;
import com.qs.contact.mvp.contract.ContactContract;
import com.qs.contact.mvp.model.ContactModel;
import com.qs.contact.mvp.model.entity.ContactGroup;
import com.qs.contact.mvp.ui.adapter.ContactGroupAdapter;

import java.util.ArrayList;
import java.util.List;

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
    ContactContract.View provideContactView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ContactContract.Model provideContactModel(ContactModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    List<ContactGroup> provideContactGroups() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideContactGroupAdapter(List<ContactGroup> list) {
        return new ContactGroupAdapter(list);
    }
}
