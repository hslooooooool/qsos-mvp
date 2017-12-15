package com.qs.contact.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qs.arm.base.BaseActivity;
import com.qs.arm.di.component.AppComponent;
import com.qs.arm.integration.lifecycle.ActivityLifecycle;
import com.qs.arm.utils.ArmsUtils;
import com.qs.contact.R;
import com.qs.contact.constant.ConstantRouter;
import com.qs.contact.di.component.DaggerContactComponent;
import com.qs.contact.di.module.ContactModule;
import com.qs.contact.mvp.contract.ContactContract;
import com.qs.contact.mvp.model.entity.ContactGroup;
import com.qs.contact.mvp.presenter.ContactPresenter;

import java.util.Date;

import timber.log.Timber;

/**
 * @author 华清松
 * @date
 * @see com.qs.arm.base.BaseActivity
 * @since 联系人列表页
 */
@Route(group = ConstantRouter.group, path = ConstantRouter.main)
public class ContactActivity extends BaseActivity<ContactPresenter>
        implements ContactContract.View, ActivityLifecycle {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerContactComponent.builder()
                .appComponent(appComponent)
                .contactModule(new ContactModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.contact_activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.addContactGroup(new ContactGroup("1", new Date()));
        mPresenter.getGroupAndContact(false);
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");

    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");

    }

    @Override
    public void showMessage(String message) {
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
